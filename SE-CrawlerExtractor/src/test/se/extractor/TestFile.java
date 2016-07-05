package test.se.extractor;

public class TestFile {

	public static void main(String[] args) {
		String htmlfilepath = "E:\\eclipse_javaee_workspace\\SE-CrawlerExtractor\\html\\usedata\\banana-sc\\DetailPage\\www.banana.agri.cn_shichang_fxyc_201509_t20150908_4820266.htm";
		String htmlfilepath2 = "E:\\eclipse_javaee_workspace\\SE-CrawlerExtractor\\html\\usedata\\banana-sc\\DetailPage\\www.banana.agri.cn_shichang_fxyc_201509_t20150908_4819605.htm";
		
		System.out.println("Message:Now extracting "+htmlfilepath);
		System.out.println("Message2:Now extracting "+htmlfilepath2);
	    String filename = htmlfilepath.replace("\\", "/"); 
	    String filename2 = htmlfilepath2.replace("\\", "/"); 
	    System.out.println("filename is: "+filename);
	    System.out.println("filename2 is: "+filename2);
//	    setSrcFile(filename);
	    int start = filename.lastIndexOf("/");
	    int end = filename.lastIndexOf(".");
	    int start2 = filename2.lastIndexOf("/");
	    int end2 = filename2.lastIndexOf(".");
	    
		String output = filename.substring(start, end);
		System.out.println("output: "+output);
		String outputfilename = "xml"+output+".xml";
		System.out.println("outputfilename: "+outputfilename);
//		setOutFile("xml/"+output+".xml");
		
		String output2 = filename2.substring(start2, end2);
		System.out.println("output2: "+output2);
		String outputfilename2 = "xml"+output2+".xml";
		System.out.println("outputfilename2: "+outputfilename2);

	}

}
