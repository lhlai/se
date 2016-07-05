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
			//isDirectory()方法用于判断该files[i]下是否为目录文件，true则是，false则不是。
			if(files[i].isDirectory() == true){
				//如果files[i]是目录文件，则递归执行processExtract()方法。
				processExtract(files[i].getAbsolutePath(),outputpath);
			}
			else{
				htx.setConfigFile("txt/config2.txt");
				htx.extract(files[i].getAbsolutePath(),outputpath);
			}
	    }
    }
}
	
	
