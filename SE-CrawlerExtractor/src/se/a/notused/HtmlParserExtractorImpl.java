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
 * ����һ����ȡ�࣬ʵ�ֶ���ҳ��Ϣ�Ľ�������ȡ�����������Ϣ�ȴ���page�����У����page��Ϊ�գ�
 * ��ͨ������pageLib�ཫpage���������д�뱾���ļ����˹����л���Ҫ����һ���洢�ļ�·���Ķ���properconfig
 * �����Լ������������ļ�����MD5����
 * @author Administrator
 *
 */
public class HtmlParserExtractorImpl implements Runnable,Extractor{
	private String filename;  //��������Ҫ������ҳ��·��
	private Parser parser;    //����һ����ҳ�����������������
	private Page page;        //����Page��
	private String encode;    //������ҳ�ı���
	
	@Override
	public void processExtract(String htmlpath,String filepath) {
		File[] files = new File(htmlpath).listFiles();
		for(int i = 0; i<files.length;i++){
			//isDirectory()���������жϸ�files[i]���Ƿ�ΪĿ¼�ļ���true���ǣ�false���ǡ�
			if(files[i].isDirectory() == true){
				//���files[i]��Ŀ¼�ļ�����ݹ�ִ��processExtract()������
				processExtract(files[i].getAbsolutePath(), filepath);
			}
			else{
				String encode = "GB2312";
			  try{
				  /**
				   * 1)�ù�����new BufferedReader(Reader in)����һ��ʹ��Ĭ�ϴ�С���뻺�����Ļ����ַ�������;
				   * 2)�ڸ������ж�ȡ���ݵ�File������´���һ���� FileReader.��new FileReader(File file);
				   * 3)file[i].getAbsoluteFile()���ظ�file�ľ��Գ���·����������˳���·������ʾ��ͬ���ļ���Ŀ¼ 
				   */
				  BufferedReader reader = new BufferedReader(new FileReader(files[i].getAbsoluteFile())); 
				  //file[i].getAbsolutePath()���ش˳���·�����ľ���·�����ַ�����
				  String line = reader.readLine();  
				  //ֻҪHTML�ļ��������ݣ���һ�оͲ�����Ϊnull��ĳһ�пհף�line��һ��Ϊnull
				  while(line !=null){
					  if(line.indexOf("charset=")!=-1){  //��������line����"charset"�ַ���
						  int start = line.indexOf("charset=");
						  start = start + 8;             //ƥ��charset=������ַ�
						  String tmp = line.substring(start, start+3);
						  if("ISO".equals(tmp)||"iso".equals(tmp)){   //���charset=ISO��charset=iso
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
						  line = reader.readLine();  //��ȡ�ı���
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
	 * ����ĺ��ķ�����������ԭʼ��ҳ��DOM���ṹ��������������ļ���Ϣ���浽page�����У�����page�����
	 * ���ݴ�ʱ�������������С�
	 * @param filename
	 */
	@Override
	public void extract(String filename,String filepath){
		System.out.println("Message:Now extracting "+filename);
		/*�ر�ע�⣺replace("\\","/")��ʾ��б��"\"���浥��б��"\"��
		 * ��Ϊ����\���ȱ�jvmʶ��Ϊת���ַ����磬\"������˫���ţ���Ҫ��ӡ��˫���ţ�
		 * Ҫ���ΪSystem.out.println("\"\"");ͬ����б�ܵ�ת���ַ�Ӧ��д��\\��
		 */
	    this.filename = filename.replace("\\", "/"); 
	    System.out.println("filename is: "+this.filename);
	    run();  //ͨ������ԭʼ�ļ�������������ļ�����page�����У���ʱpage�����е����ݽ��������ڴ��У�
	    if(this.page!=null){ //��page����Ϊ��ʱ����page������뱾���ļ�
	    	PageLib.storePage(this.page,filepath);
	    }
	}
	
	/**
	 * ͨ������ԭʼ�ļ�������������ļ�����page������,
	 * ��ʱpage�����е����ݽ��������ڴ��У�
	 * 1)run()�ȵ��ñ������е�ʵ������getUrl(()�����׼ȷ��url��ַ���ٵ���page��setUrl(String url)��url��ַд��page��url���ԣ�
	 * 2)run()�ȸ��ݾ�����ļ�����·����ʵ����һ��parser����
	 * �ڶ���������parserʵ����һ����ҳ���������󣬼�HtmlPage��
	 * ������������HtmlPage��getTitle()����ȡ��ԭʼ��ҳ��title��
	 * ���Ĳ�������page��setTitle(String title)(����title����ֵ������HtmlPage.getTitle()�ķ���ֵ)��titleд��page��title����.
	 * 3)ͨ������page��setContext(String context)��contextд��page��context���ԣ�
	 * 4)ͨ������page��setSummary(String summary)��summaryд��page��summary���ԣ�
	 * 5)ͨ������page��setScore(int score)��scoreд��page��score���ԣ�
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			//parser��ȫ�ֱ������˴�ʵ����һ��parser����
			parser = new Parser(this.filename);
			//Set the encoding for the page this parser is reading from
			parser.setEncoding(this.encode);  
			//��������ȼ�������Ĵ���
//			parser.setEncoding(getEncode()); //ͨ�������е�setEncode()������this.encode��ֵ��������getEncode()ȡ��encode����
			//vistior����ȫ�ֱ�������Ҫ���塣��ΪHtmlPage�Ĺ��췽������һ������������parser
			HtmlPage visitor = new HtmlPage(parser); 
			//Apply the given visitor to the current page
			parser.visitAllNodesWith(visitor); //����visitor���ʵ�ǰ��ҳ��
			page = new Page();
			//��ȡ��ҳ��URL
			this.page.setUrl(getUrl(this.filename));
			//��ȡ��ҳ�ı���
			this.page.setTitle(visitor.getTitle());
			//����HtmlPage��getBody()����ȡ����ҳ��Node�ڵ�����
			if(visitor.getBody()==null){
				this.page.setContext(null);
			}
			else{
				//�����Ϊ�գ�����ȡ����,toNodeArray()����ҳ��ڵ�����Node[]
				this.page.setContext(combineNodeText(visitor.getBody().toNodeArray()));
			}
			//������ҳ�ĵ÷֡�������ҳ��URL����ҳ��ʼ���֣��������ҳ�ĵ÷֡�
			this.page.setScore(getScore(this.page.getUrl(),this.page.getScore()));
			//������ҳ������,������ҳ�����ݼ�����ļ�ժҪ��Ϣ
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
		//����String��replace(CharSequence target,CharSequence replacement)����
		//��replacement�滻target����ʼ��url������ȫ·������replace(,)������ץȡ�������ļ��洢·����/mirror��ǰ��·��[��/mirror]��ȡ����
		//����������url��ַ���Ǹ���ҳ��ʵ��URL��ַ!!
		url = url.replace(ProperConfig.getPathValue("mirror.path"),"");
//		if((url.substring(url.length()-1)).equals("/")){
//			url = url.substring(0,url.length()-1);
//		}
		if(url.lastIndexOf("/")==url.length()-1){ //lastIndexOf()����ָ�����ַ����ڴ��ַ��������ұ߳��ִ�������������intֵ
			//����һ�����ַ��������ַ�����url�ĵ�0���ַ���������(url.length-1)-1���ַ���
			url = url.substring(0,url.length()-1);
		}
		//����һ�����ַ��������ַ�����ָ����������ʼ��֪�����ַ�����ĩβ��
		url = url.substring(1);
		return url;
	}
	/**
	 * ����һ��������ҳ�÷ֵķ�����ԭ���ǣ�����URL��ַ�Ĳ㼶�������жϸ�URL��ַ�ġ�Ȩ���ȡ���
	 * �㼶����Խ�࣬��"/"�ַ��ڸ�URL�г��ֵĴ���Խ�࣬������URL��ַ��Խ����Ȩ��������֮��Ȼ��
	 * @param url
	 * @param score
	 * @return
	 */
	private double getScore(String url,double score){
		String[] subStr = url.split("/");
		score = score-(subStr.length-1); //����URL�Ĳ㼶���򵥵ĸ�URL��ַ��֣�
		return score;  //��������
	}
	/**
	 * ������ҳ��ժҪ,������ҳ�����ݼ�����ļ�ժҪ��Ϣ��
	 * MD5Encode()��������ҳ������������ժҪ��Ϣ��
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
	 * �Զ���һ��combineNodeText()����
	 * �÷���ͨ���ݹ���������ҳ�еĸ�����ε�Ԫ�أ�
	 * ��ҳ�����ݻ������parser���ֵ��ı��ڵ㣬��TextNode��������ʾһ���ı��ڵ㣬LinkNode�����ʾ
	 * һ�������ӽڵ�
	 * @param nodes
	 * @return
	 */
	private String combineNodeText(Node[] nodes){
		StringBuffer buffer = new StringBuffer();
//		System.out.println("��һ�δ�������nodes��:"+nodes);
		for(int i = 0;i<nodes.length;i++){
			Node anode = (Node)nodes[i];
			String line = null;  
			if(anode instanceof TextNode){  //�ж�anode�Ƿ�ΪTextNode��һ��ʵ��
				TextNode textnode = (TextNode)anode;
				line = textnode.getText();  //TextNode��д��AbstractNode��getText()���������ظýڵ�(node)                          //���ı�(text).
			}
			else if(anode instanceof LinkTag){ //�ж��Ƿ�Ϊ�����ӱ�ǩ
				LinkTag linktag = (LinkTag)anode;
				line = linktag.getLinkText();
//				System.out.println("linktag��:"+line);    
			}
			else if(anode instanceof Div){  //�ж��Ƿ�Ϊ��ڵ�
				if(anode.getChildren()!=null){ //�����ӽڵ�
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			else if(anode instanceof ParagraphTag){ //�ж��Ƿ�Ϊ����ڵ�
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			//span��ǩ����������ĵ��е�����Ԫ��
			/**
			 * eg. <p class="tip"><span>��ʾ��</span>... ... ...</p>
			 * css��ʽ:p.tip span {
			 * font-weight:bold;
			 * color:#ff9955��}
			 */
			else if(anode instanceof Span){  //�ж��Ƿ�Ϊspan��ǩ
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			else if(anode instanceof TableTag){  //�ж��Ƿ�Ϊ����ǩ
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			else if(anode instanceof TableRow){ //�ж��Ƿ�Ϊ�����б�ǩ
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			else if(anode instanceof TableColumn){  //�ж��Ƿ�Ϊ�б�ǩ
				if(anode.getChildren()!=null){
					line = combineNodeText(anode.getChildren().toNodeArray());
				}
			}
			 if(line!=null){
				 buffer.append(line); //��line���ݼ��뵽buffer
			 }
		}
		return buffer.toString(); 
	}
	
	/**
	 * �����ص�encode���Ը�ֵ�����յĲ���������TestExtractor����
	 * @param encode
	 */
	public void setEncode(String encode){
		this.encode = encode;
	}
	/**
	 * ����ʵ�������encode����ֵ
	 * @return
	 */
	public String getEncode(){
		return encode;
	}

}
