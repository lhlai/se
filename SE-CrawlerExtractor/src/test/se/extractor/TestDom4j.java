package test.se.extractor;

import se.extractor.pageHandler.Dom4jExtractorImpl;
import se.extractor.util.ProperConfig;

public class TestDom4j {
	
	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
        Dom4jExtractorImpl extractor = new Dom4jExtractorImpl();
        extractor.processExtract(ProperConfig.getPathValue("xml.path"),ProperConfig.getPathValue("files.path"));
        long end = System.currentTimeMillis() - begin; 
		System.out.println("∫ƒ ±£∫" + end + "∫¡√Î");
	}
	
}
