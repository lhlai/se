package test.se.extractor;

import se.extractor.pageHandler.HtmlToXmlImpl;

public class TestHtmlToXmlSingle {

	public static void main(String[] args) {
		ExchangeHtmlToXml("tempfile","xml","jtidyConfig/config2.txt");
		System.out.println("Extraction is end.");
		System.out.println("=============================================");
	}
	
	/**
	 * Exchange Html To Xml
	 * @param htmlpath
     */
	private static void ExchangeHtmlToXml(String htmlpath,String outputpath,String configfilepath) {		
		HtmlToXmlImpl htx = new HtmlToXmlImpl();
		htx.processExchangeHtmlToXml(htmlpath, outputpath,configfilepath);
	}
}
