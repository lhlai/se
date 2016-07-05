package test.se.extractor;

import java.io.IOException;
import org.htmlparser.util.ParserException;
import se.extractor.algorithm.PageDistance;

public class PageDistanceTest {

	public static void main(String[] args) throws ParserException, IOException {
		long begin = System.currentTimeMillis();
//		PageDistance pagedistance = new PageDistance();
		double distance = PageDistance.getPageSim("http://www.sina.com.cn/", "http://sports.sina.com.cn/");
        System.out.println("url1与url2的距离是:"+(distance));
        long end = System.currentTimeMillis() - begin; 
		System.out.println("耗时：" + end + "毫秒");
	}
}
