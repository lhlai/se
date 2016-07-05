package test.se.extractor;
/**
 * ≤‚ ‘JsoupExtractor
 */
import se.extractor.pageHandler.JsoupExtractor;

public class TestJsoupExtractor {
    
	public static void main(String[] args) {
		JsoupExtractor extractor = new JsoupExtractor();
		String htmlpath = "tempfile";
		String filepath = "txt";
		extractor.processExtract(htmlpath,filepath);
	}
	
}
