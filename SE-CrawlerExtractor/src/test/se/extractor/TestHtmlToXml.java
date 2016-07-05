package test.se.extractor;

import java.io.File;

import se.extractor.pageHandler.HtmlToXmlImpl;
import se.extractor.util.ProperConfig;

public class TestHtmlToXml{

	public static void main(String[] args) throws Exception{
		System.out.println("Start extracting pages...");
		processExtract(ProperConfig.getPathValue("html.path"),ProperConfig.getPathValue("xml.path"));
		System.out.println("Extraction is end.");
		System.out.println("======================");
    }

	private static void processExtract(String path,String outputpath) {
		File[] files = new File(path).listFiles();
		HtmlToXmlImpl htx = new HtmlToXmlImpl();
		for(int i = 0; i<files.length;i++){
			//isDirectory()���������жϸ�files[i]���Ƿ�ΪĿ¼�ļ���true���ǣ�false���ǡ�
			if(files[i].isDirectory() == true){
				//���files[i]��Ŀ¼�ļ�����ݹ�ִ��processExtract()������
				processExtract(files[i].getAbsolutePath(),outputpath);
			}
			else{
				htx.setConfigFile("txt/config2.txt");
				htx.extract(files[i].getAbsolutePath(),outputpath);
			}
	    }
    }
}
	
	
