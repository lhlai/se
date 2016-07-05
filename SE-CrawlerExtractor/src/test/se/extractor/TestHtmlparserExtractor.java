package test.se.extractor;

import se.a.notused.HtmlParserExtractorImpl;
import se.extractor.util.ProperConfig;
/**
 * 从爬虫爬取保存到本地的网页信息中抽取数据的测试类
 * @author pillar
 *
 */
public class TestHtmlparserExtractor {
	public static void main(String[] args){
		System.out.println("Start extracting pages...");
		HtmlParserExtractorImpl extractor = new HtmlParserExtractorImpl();
		extractor.processExtract(ProperConfig.getPathValue("html.path"),ProperConfig.getPathValue("files.path"));
		System.out.println("Extraction is end.");
		System.out.println("======================");
	}
	/**
	 * 执行抽取对象Extract,进行页面信息抽取
	 * processExtract()方法主要干两件事：1、给每个文件设置编码类型；2、执行页面抽取方法Extract.extract()
	 * 事件一主要完成代码流程：
	 * 1）首先判断文件类型数组的每个文件是否含有子文件，若有，则递归执行processExtract()方法，直到找到非目录文件；
	 * 2）先设置默认的编码类型为"GB2312",用while()循环语句寻找“charset="字符串，直到找到时跳出循环
	 * 3)找到"charset="字符串后，匹配其后面的字符，设置相应的编码格式。
	 * 事件二主要代码流程：
	 * 1）调用Extract的构造器，实例化一个Extract对象
	 * 2）调用Extract的setEncode(String encode)方法，将事件一确定的encode值传递给Extract对象的encode
	 * 3)设置完Extract的编码格式后，调用extract(String filename)方法，正式执行网页信息抽取
	 * @param value
	 */
//	private static void processExtract(String path) {
		//
//		File[] files = new File(path).listFiles();
//		for(int i = 0; i<files.length;i++){
//			//isDirectory()方法用于判断该files[i]下是否为目录文件，true则是，false则不是。
//			if(files[i].isDirectory() == true){
//				//如果files[i]是目录文件，则递归执行processExtract()方法。
//				processExtract(files[i].getAbsolutePath());
//			}
//			else{
//				String encode = "GB2312";
//			  try{
//				  /**
//				   * 1)用构造器new BufferedReader(Reader in)创建一个使用默认大小输入缓冲区的缓冲字符输入流;
//				   * 2)在给定从中读取数据的File的情况下创建一个新 FileReader.即new FileReader(File file);
//				   * 3)file[i].getAbsoluteFile()返回该file的绝对抽象路径名，它与此抽象路径名表示相同的文件或目录 
//				   */
//				  BufferedReader reader = new BufferedReader(new FileReader(files[i].getAbsoluteFile())); 
//				  //file[i].getAbsolutePath()返回此抽象路径名的绝对路径名字符串。
////				  BufferedReader reader2 = new BufferedReader(new FileReader(files[i].getAbsolutePath())); 
//				  //读取一个文本行。通过下列字符之一即可认为某行已终止：换行 ('\n')、回车 ('\r') 或回车后直接跟着换行。 
//				  String line = reader.readLine();  
//				  //注意：只要HTML文件中有内容，第一行就不可能为null，某一行空白，line不一定为null
//				  //当且仅当行下面无任何信息（包括换行符号）时，再次执行render.readline()方法时才会返回line=null
//				  while(line !=null){
//					  if(line.indexOf("charset=")!=-1){ //缓冲区的line中有"charset"字符串
//						  int start = line.indexOf("charset=");
//						  start = start + 8;  //匹配charset=后面的字符
//						  String tmp = line.substring(start, start+3);
//						  if("ISO".equals(tmp)||"iso".equals(tmp)){ //如果charset=ISO或charset=iso
//							  encode ="ISO-8859-1";
//						  }
//						  else if("UTF".equals(tmp)||"utf".equals(tmp)){
//							  encode = "UTF-8";
//						  }
//						  else if("GBK".equals(tmp)||"gbk".equals(tmp)){
//							  encode = "GBK";
//						  }
//						  else{
//							  encode = "GB2312";
//						  }
//						  reader.close();
//						  break;
//					  }
//					  else{
//						  line = reader.readLine(); //读取文本行
//					  }
//				  }
//			  }
//			  catch(IOException ioe){
//				  ioe.printStackTrace();
//			  }
//			  HtmlParserExtractorImpl extractor = new HtmlParserExtractorImpl();
//			  extractor.setEncode(encode);
//			  extractor.extract(files[i].getAbsolutePath());
//			}
//		}
//	}
}
