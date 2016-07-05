package se.extractor.pageHandler;

public interface Extractor {
	
	 void processExtract(String path,String filepath);
	 
	 void extract(String xmlfilename, String filepath)throws Exception;

}
