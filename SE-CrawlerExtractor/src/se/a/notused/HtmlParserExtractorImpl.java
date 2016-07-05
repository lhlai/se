package se.a.notused;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

import se.extractor.algorithm.MD5;
import se.extractor.page.Page;
import se.extractor.pageHandler.Extractor;
import se.extractor.util.PageLib;
import se.extractor.util.ProperConfig;
/**
 * 定义一个抽取类，实现对网页信息的解析、抽取，解析后的信息先存在page对象中，如果page不为空，
 * 则通过调用pageLib类将page对象的数据写入本地文件。此过程中还需要定义一个存储文件路径的对象properconfig
 * 对象以及用于生成新文件名的MD5对象。
 * @author Administrator
 *
 */
public class HtmlParserExtractorImpl implements Runnable,Extractor{
	private String filename;  //用来保存要解析网页的路径
	private Parser parser;    //定义一个网页解析器，起解析作用
	private Page page;        //引入Page类
	private String encode;    //保存网页的编码
	
	@Override
	public void processExtract(String htmlpath,String filepath) {
		File[] files = new File(htmlpath).listFiles();
		for(int i = 0; i<files.length;i++){
			//isDirectory()方法用于判断该files[i]下是否为目录文件，true则是，false则不是。
			if(files[i].isDirectory() == true){
				//如果files[i]是目录文件，则递归执行processExtract()方法。
				processExtract(files[i].getAbsolutePath(), filepath);
			}
			else{
				String encode = "GB2312";
			  try{
				  /**
				   * 1)用构造器new BufferedReader(Reader in)创建一个使用默认大小输入缓冲区的缓冲字符输入流;
				   * 2)在给定从中读取数据的File的情况下创建一个新 FileReader.即new FileReader(File file);
				   * 3)file[i].getAbsoluteFile()返回该file的绝对抽象路径名，它与此抽象路径名表示相同的文件或目录 
				   */
				  BufferedReader reader = new BufferedReader(new FileReader(files[i].getAbsoluteFile())); 
				  //file[i].getAbsolutePath()返回此抽象路径名的绝对路径名字符串。
				  String line = reader.readLine();  
				  //只要HTML文件中有内容，第一行就不可能为null，某一行空白，line不一定为null
				  while(line !=null){
					  if(line.indexOf("charset=")!=-1){  //缓冲区的line中有"charset"字符串
						  int start = line.indexOf("charset=");
						  start = start + 8;             //匹配charset=后面的字符
						  String tmp = line.substring(start, start+3);
						  if("ISO".equals(tmp)||"iso".equals(tmp)){   //如果charset=ISO或charset=iso
							  encode ="ISO-8859-1";
						  }
						  else if("UTF".equals(tmp)||"utf".equals(tmp)){
							  encode = "UTF-8";
						  }
						  else if("GBK".equals(tmp)||"gbk".equals(tmp)){
							  encode = "GBK";
						  }
						  else{
							  encode = "GB2312";
						  }
						  reader.close();
						  break;
					  }
					  else{
						  line = reader.readLine();  //读取文本行
					  }
				  }
			  }
			  catch(IOException ioe){
				  ioe.printStackTrace();
			  }
//			  HtmlParserExtractorImpl extractor = new HtmlParserExtractorImpl();
			  setEncode(encode);
			  extract(files[i].getAbsolutePath(), filepath);
			}
		}
	}
	/**
	 * 该类的核心方法。负责处理原始网页的DOM树结构，并将解析后的文件信息保存到page对象中，但是page对象的
	 * 数据此时仅存在于内容中。
	 * @param filename
	 */
	@Override
	public void extract(String filename,String filepath){
		System.out.println("Message:Now extracting "+filename);
		/*特别注意：replace("\\","/")表示用斜杠"\"代替单反斜杠"\"，
		 * 因为单个\首先被jvm识别为转义字符，如，\"代表单个双引号，如要打印出双引号，
		 * 要表达为System.out.println("\"\"");同理，反斜杠的转义字符应该写成\\。
		 */
	    this.filename = filename.replace("\\", "/"); 
	    System.out.println("filename is: "+this.filename);
	    run();  //通过解析原始文件，将解析后的文件存入page对象中，此时page对象中的数据仅存在于内存中！
	    if(this.page!=null){ //当page对象不为空时，将page对象存入本地文件
	    	PageLib.storePage(this.page,filepath);
	    }
	}
	
	/**
	 * 通过解析原始文件，将解析后的文件存入page对象中,
	 * 此时page对象中的数据仅存在于内存中！
	 * 1)run()先调用本对象中的实例方法getUrl(()计算出准确的url地址，再调用page的setUrl(String url)将url地址写入page的url属性；
	 * 2)run()先根据具体的文件（含路径）实例化一个parser对象，
	 * 第二步，根据parser实例化一个网页解析器对象，即HtmlPage；
	 * 第三步，调用HtmlPage的getTitle()方法取得原始网页的title；
	 * 第四步，调用page的setTitle(String title)(其中title参数值来自于HtmlPage.getTitle()的返回值)将title写入page的title属性.
	 * 3)通过调用page的setContext(String context)将context写入page的context属性；
	 * 4)通过调用page的setSummary(String summary)将summary写入page的summary属性；
	 * 5)通过调用page的setScore(int score)将score写入page的score属性；
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			//parser是全局变量，此处实例化一个parser对象
			parser = new Parser(this.filename);
			//Set the encoding for the page this parser is reading from
			parser.setEncoding(this.encode);  
			//上述代码等价于下面的代码
//			parser.setEncoding(getEncode()); //通过对象中的setEncode()方法给this.encode赋值，再利用getEncode()取得encode参数
			//vistior不是全局变量，需要定义。需为HtmlPage的构造方法传入一个解析器对象parser
			HtmlPage visitor = new HtmlPage(parser); 
			//Apply the given visitor to the current page
			parser.visitAllNodesWith(visitor); //允许visitor访问当前的页面
			page = new Page();
			//获取网页的URL
			this.page.setUrl(getUrl(this.filename));
			//获取网页的标题
			this.page.setTitle(visitor.getTitle());
			//调用HtmlPage的getBody()方法取得网页的Node节点内容
			if(visitor.getBody()==null){
				this.page.setContext(null);
			}
			else{
				//如果不为空，则提取内容,toNodeArray()返回页面节点数组Node[]
				this.page.setContext(combineNodeText(visitor.getBody().toNodeArray()));
			}
			//计算网页的得分。根据网页的URL及网页初始评分，计算出网页的得分。
			this.page.setScore(getScore(this.page.getUrl(),this.page.getScore()));
			//计算网页的内容,根据网页的内容计算出文件摘要信息
			this.page.setSummary(getSummary(this.page.getContext()));
		}
		catch(ParserException pe){
			this.page = null;
			pe.printStackTrace();
			System.out.println("Continue...");		
		}
	}
	/**
	 * 
	 * @param filename
	 * @return
	 */
	private String getUrl(String filename){
		String url = filename;
		//调用String的replace(CharSequence target,CharSequence replacement)方法
		//将replacement替换target。初始的url包含了全路径，用replace(,)方法将抓取下来的文件存储路径（/mirror以前的路径[含/mirror]）取消。
		//保留下来的url地址即是该网页真实的URL地址!!
		url = url.replace(ProperConfig.getPathValue("mirror.path"),"");
//		if((url.substring(url.length()-1)).equals("/")){
//			url = url.substring(0,url.length()-1);
//		}
		if(url.lastIndexOf("/")==url.length()-1){ //lastIndexOf()返回指定子字符串在此字符串中最右边出现处的索引，返回int值
			//返回一个子字符串，该字符串从url的第0个字符检索到第(url.length-1)-1个字符。
			url = url.substring(0,url.length()-1);
		}
		//返回一个子字符串，该字符串从指定索引处开始，知道该字符串的末尾。
		url = url.substring(1);
		return url;
	}
	/**
	 * 定义一个计算网页得分的方法。原理是，根据URL地址的层级数来简单判断该URL地址的“权威度”，
	 * 层级划分越多，即"/"字符在该URL中出现的次数越多，表明该URL地址就越不“权威”，反之亦然。
	 * @param url
	 * @param score
	 * @return
	 */
	private double getScore(String url,double score){
		String[] subStr = url.split("/");
		score = score-(subStr.length-1); //根据URL的层级数简单的给URL地址打分！
		return score;  //返回评分
	}
	/**
	 * 计算网页的摘要,根据网页的内容计算出文件摘要信息。
	 * MD5Encode()方法根据页面内容来计算摘要信息！
	 * @param context
	 * @return
	 */
	private String getSummary(String context){
		if(context == null){
			context="";
		}
		return MD5.MD5Encode(context);
	}
	/**
	 * 自定义一个combineNodeText()方法
	 * 该方法通过递归来解析网页中的各个层次的元素，
	 * 网页的内容会出现在parser划分的文本节点，用TextNode对象来表示一个文本节点，LinkNode对象表示
	 * 一个超链接节点
	 * @param nodes
	 * @return
	 */
	private String combineNodeText(Node[] nodes){
		StringBuffer buffer = new StringBuffer();
//		System.out.println("第一次传进来的nodes是:"+nodes);
		for(int i = 0;i<nodes.length;i++){
			Node anode = (Node)nodes[i];
			String line = null;  
			if(anode instanceof TextNode){  //判断anode是否为TextNode的一个实例
				TextNode textnode = (TextNode)anode;
				line = textnode.getText();  //TextNode重写了AbstractNode的getText()方法，返回该节点(node)                          //的文本(text).
			}
			else if(anode instanceof LinkTag){ //判断是否为超链接标签
				LinkTag linktag = (LinkTag)anode;
				line = linktag.getLinkText();
//				System.out.println("linktag是:"+line);    
			}
			else if(anode instanceof Div){  //判断是否为层节点
				if(anode.getChildren()!=null){ //包含子节点
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			else if(anode instanceof ParagraphTag){ //判断是否为段落节点
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			//span标签被用来组合文档中的行内元素
			/**
			 * eg. <p class="tip"><span>提示：</span>... ... ...</p>
			 * css样式:p.tip span {
			 * font-weight:bold;
			 * color:#ff9955；}
			 */
			else if(anode instanceof Span){  //判断是否为span标签
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			else if(anode instanceof TableTag){  //判断是否为表格标签
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			else if(anode instanceof TableRow){ //判断是否为表格的行标签
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			else if(anode instanceof TableColumn){  //判断是否为列标签
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			 if(line!=null){
				 buffer.append(line); //将line内容加入到buffer
			 }
		}
		return buffer.toString(); 
	}
	
	/**
	 * 给本地的encode属性赋值，接收的参数来自于TestExtractor对象。
	 * @param encode
	 */
	public void setEncode(String encode){
		this.encode = encode;
	}
	/**
	 * 返回实例化后的encode参数值
	 * @return
	 */
	public String getEncode(){
		return encode;
	}

}
