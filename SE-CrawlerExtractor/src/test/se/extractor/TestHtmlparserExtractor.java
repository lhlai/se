package test.se.extractor;

import se.a.notused.HtmlParserExtractorImpl;
import se.extractor.util.ProperConfig;
/**
 * ��������ȡ���浽���ص���ҳ��Ϣ�г�ȡ���ݵĲ�����
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
	 * ִ�г�ȡ����Extract,����ҳ����Ϣ��ȡ
	 * processExtract()������Ҫ�������£�1����ÿ���ļ����ñ������ͣ�2��ִ��ҳ���ȡ����Extract.extract()
	 * �¼�һ��Ҫ��ɴ������̣�
	 * 1�������ж��ļ����������ÿ���ļ��Ƿ������ļ������У���ݹ�ִ��processExtract()������ֱ���ҵ���Ŀ¼�ļ���
	 * 2��������Ĭ�ϵı�������Ϊ"GB2312",��while()ѭ�����Ѱ�ҡ�charset="�ַ�����ֱ���ҵ�ʱ����ѭ��
	 * 3)�ҵ�"charset="�ַ�����ƥ���������ַ���������Ӧ�ı����ʽ��
	 * �¼�����Ҫ�������̣�
	 * 1������Extract�Ĺ�������ʵ����һ��Extract����
	 * 2������Extract��setEncode(String encode)���������¼�һȷ����encodeֵ���ݸ�Extract�����encode
	 * 3)������Extract�ı����ʽ�󣬵���extract(String filename)��������ʽִ����ҳ��Ϣ��ȡ
	 * @param value
	 */
//	private static void processExtract(String path) {
		//
//		File[] files = new File(path).listFiles();
//		for(int i = 0; i<files.length;i++){
//			//isDirectory()���������жϸ�files[i]���Ƿ�ΪĿ¼�ļ���true���ǣ�false���ǡ�
//			if(files[i].isDirectory() == true){
//				//���files[i]��Ŀ¼�ļ�����ݹ�ִ��processExtract()������
//				processExtract(files[i].getAbsolutePath());
//			}
//			else{
//				String encode = "GB2312";
//			  try{
//				  /**
//				   * 1)�ù�����new BufferedReader(Reader in)����һ��ʹ��Ĭ�ϴ�С���뻺�����Ļ����ַ�������;
//				   * 2)�ڸ������ж�ȡ���ݵ�File������´���һ���� FileReader.��new FileReader(File file);
//				   * 3)file[i].getAbsoluteFile()���ظ�file�ľ��Գ���·����������˳���·������ʾ��ͬ���ļ���Ŀ¼ 
//				   */
//				  BufferedReader reader = new BufferedReader(new FileReader(files[i].getAbsoluteFile())); 
//				  //file[i].getAbsolutePath()���ش˳���·�����ľ���·�����ַ�����
////				  BufferedReader reader2 = new BufferedReader(new FileReader(files[i].getAbsolutePath())); 
//				  //��ȡһ���ı��С�ͨ�������ַ�֮һ������Ϊĳ������ֹ������ ('\n')���س� ('\r') ��س���ֱ�Ӹ��Ż��С� 
//				  String line = reader.readLine();  
//				  //ע�⣺ֻҪHTML�ļ��������ݣ���һ�оͲ�����Ϊnull��ĳһ�пհף�line��һ��Ϊnull
//				  //���ҽ������������κ���Ϣ���������з��ţ�ʱ���ٴ�ִ��render.readline()����ʱ�Ż᷵��line=null
//				  while(line !=null){
//					  if(line.indexOf("charset=")!=-1){ //��������line����"charset"�ַ���
//						  int start = line.indexOf("charset=");
//						  start = start + 8;  //ƥ��charset=������ַ�
//						  String tmp = line.substring(start, start+3);
//						  if("ISO".equals(tmp)||"iso".equals(tmp)){ //���charset=ISO��charset=iso
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
//						  line = reader.readLine(); //��ȡ�ı���
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
