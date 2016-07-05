package test.se.crawler;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;

import se.crawler.framework.DownLoadPage;
import se.a.notused.HtmlParserTool;
import se.crawler.url.fontier.AgriKejiLinkFilter;
import se.crawler.url.fontier.LinkFilter;

public class UrlParserTest {
    
	public static void main(String[] args) throws URISyntaxException {
		DownLoadPage crawler = new DownLoadPage();
		String url = "http://www.sina.com.cn/";
		int d =1;
		String content = crawler.downloadFile(url,d);
//		System.out.println(content);
		crawler.setEncoding(url);
		String charset = crawler.getEncoding();
		LinkFilter linkFilter = new AgriKejiLinkFilter();  
		Set<String> urlSet = HtmlParserTool.extractLinks(content, linkFilter,charset);  
		Iterator<String> it = urlSet.iterator();  
		while(it.hasNext()){  
		      System.out.println(it.next());  
		} 
	}
}
