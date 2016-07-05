package se.extractor.main;
/**
 * 从html中抽取出结构化的文本，把其存入数据库，并建立lucene索引库
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
		System.out.println("耗时2：" + (System.currentTimeMillis() - begin)/1000 + "秒");
		System.out.println("##############html Extracting is end###################");
		
		// 1. Exchange Html To Xml
//		System.out.println("**************Start extracting pages**************");
//		ExchangeHtmlToXml(ProperConfig.getPathValue("html.path"),ProperConfig.getPathValue("xml.path"),
//				ProperConfig.getPathValue("config.path"));
//		System.out.println("**************Extraction is end*******************");
//		System.out.println("耗时1：" + (System.currentTimeMillis() - begin)/1000 + "秒");

		// 2. Extract text from Xml file
//		System.out.println("##############Start Extracting xml files##############");
//		XmlExtraxtor("html.path","files.path");
//		System.out.println("耗时2：" + (System.currentTimeMillis() - begin)/1000 + "秒");
//		System.out.println("##############Xml Extracting is end###################");

		// 3. Write text to Database
//		System.out.println("耗时3：" + (System.currentTimeMillis() - begin)/1000 + "秒");

		// 4. Build Index For Lucene
//		System.out.println("==============Start building index===================");
//		IndexBuilder("index.path","files.path");
//		System.out.println("==============Index building is end==================");
//		System.out.println("耗时4：" + (System.currentTimeMillis() - begin)/1000 + "秒");
	}

	/**
	 * Extract text from html file 
	 * @param htmlpath    html文件路径
	 * @param filepath    保存抽取出的txt文件路径
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
	 * @param xmlpath  xml文件地址
	 * @param filepath 抽取xml后保存文本文件的路径
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
			//实例化一个IndexBuilder,本质就是实例化一个lucene的IndexWriter,将其赋给IndexBuilder对象的IndexWriter属性
			IndexBuilderImpl index = new IndexBuilderImpl(ProperConfig.getPathValue(indexpath));
			//执行build
			index.build(ProperConfig.getPathValue(filespath));
			System.out.println("Index creating finished!");
		}catch(IOException ioe){
			System.out.println("Index creating failed!");
			ioe.printStackTrace();
		}
	}
}
