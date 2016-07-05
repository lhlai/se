package se.extractor.main;
/**
 * ��html�г�ȡ���ṹ�����ı�������������ݿ⣬������lucene������
 * @author pillar
 * @since 2016.6.17
 * @version 1.0
 */

import se.extractor.pageHandler.Dom4jExtractor;
import se.extractor.pageHandler.HtmlToXmlImpl;
import se.extractor.pageHandler.IndexBuilderImpl;
import se.extractor.pageHandler.JsoupExtractor;
import se.extractor.util.ProperConfig;

import java.io.File;
import java.io.IOException;

public class MyExtractor {

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		// 0. Extract text from html
		System.out.println("##############Start Extracting html files##############");
		HtmExtraxtor("html.path","files.path");
		System.out.println("��ʱ2��" + (System.currentTimeMillis() - begin)/1000 + "��");
		System.out.println("##############html Extracting is end###################");
		
		// 1. Exchange Html To Xml
//		System.out.println("**************Start extracting pages**************");
//		ExchangeHtmlToXml(ProperConfig.getPathValue("html.path"),ProperConfig.getPathValue("xml.path"),
//				ProperConfig.getPathValue("config.path"));
//		System.out.println("**************Extraction is end*******************");
//		System.out.println("��ʱ1��" + (System.currentTimeMillis() - begin)/1000 + "��");

		// 2. Extract text from Xml file
//		System.out.println("##############Start Extracting xml files##############");
//		XmlExtraxtor("html.path","files.path");
//		System.out.println("��ʱ2��" + (System.currentTimeMillis() - begin)/1000 + "��");
//		System.out.println("##############Xml Extracting is end###################");

		// 3. Write text to Database
//		System.out.println("��ʱ3��" + (System.currentTimeMillis() - begin)/1000 + "��");

		// 4. Build Index For Lucene
//		System.out.println("==============Start building index===================");
//		IndexBuilder("index.path","files.path");
//		System.out.println("==============Index building is end==================");
//		System.out.println("��ʱ4��" + (System.currentTimeMillis() - begin)/1000 + "��");
	}

	/**
	 * Extract text from html file 
	 * @param htmlpath    html�ļ�·��
	 * @param filepath    �����ȡ����txt�ļ�·��
	 */
	private static void HtmExtraxtor(String htmlpath, String filepath) {
		try{
			JsoupExtractor jsoupExtractor = new JsoupExtractor();			
			jsoupExtractor.processExtract(ProperConfig.getPathValue(htmlpath),ProperConfig.getPathValue(filepath));
		}catch(Exception e){
			e.printStackTrace();
		}	
		
	}

	/**
	 * Exchange Html To Xml
	 * @param htmlpath
     */
	private static void ExchangeHtmlToXml(String htmlpath,String outputpath,String configfilepath) {		
		HtmlToXmlImpl htx = new HtmlToXmlImpl();
		htx.processExchangeHtmlToXml(htmlpath, outputpath,configfilepath);
	}

	/**
	 * Extract text from xml file
	 * @param xmlpath  xml�ļ���ַ
	 * @param filepath ��ȡxml�󱣴��ı��ļ���·��
     */
	private static void XmlExtraxtor(String xmlpath,String filepath){		
		try{
			Dom4jExtractor extractor = new Dom4jExtractor();			
			extractor.processExtract(ProperConfig.getPathValue(xmlpath),ProperConfig.getPathValue(filepath));
		}catch(Exception e){
			e.printStackTrace();
		}				
	}

	/**
	 * Build Index for files text
	 * @param indexpath
	 * @param filespath
     */
	private static void IndexBuilder(String indexpath, String filespath){
		try{
			//ʵ����һ��IndexBuilder,���ʾ���ʵ����һ��lucene��IndexWriter,���丳��IndexBuilder�����IndexWriter����
			IndexBuilderImpl index = new IndexBuilderImpl(ProperConfig.getPathValue(indexpath));
			//ִ��build
			index.build(ProperConfig.getPathValue(filespath));
			System.out.println("Index creating finished!");
		}catch(IOException ioe){
			System.out.println("Index creating failed!");
			ioe.printStackTrace();
		}
	}
}
