package test.se.crawler;

import java.net.URISyntaxException;
import java.util.Set;

import se.crawler.framework.DownLoadPage;
import se.a.notused.UrlExtractor;
import se.crawler.url.fontier.AgriKejiLinkFilter;
import se.crawler.url.fontier.LinkFilter;

public class UrlExtractorTest {
	private LinkFilter filter = new AgriKejiLinkFilter();            //定义URL过滤器2
	
	public void test(){
		int d = 1;
		String url = "http://www.agri.cn/kj/syjs/zzjs/index_2.htm";		
        //1.下载源码
		DownLoadPage crawler = new DownLoadPage();
		try {
			String content = crawler.downloadFile(url, d);
			Set<String> links = UrlExtractor.extractLinks(url, content, filter);
			for(String link:links){
//				System.out.println("link: "+link);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		UrlExtractorTest test = new UrlExtractorTest();
		test.test();
		
	}

}
