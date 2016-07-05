package test.se.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import se.crawler.url.fontier.TopicWordComputeUrl;

public class WordCountTest {
	
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		TopicWordComputeUrl twcu = new TopicWordComputeUrl();
	    StringBuffer buffer =new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				 new FileInputStream(new File("tempfile//news.qq.com.73.htm")),"gb2312"));
//		BufferedReader reader2 = new BufferedReader(new InputStreamReader(
//				 new FileInputStream(new File("tempfile//www.agri.txt"))));
		String line = reader.readLine();
		while(line!=null){
			buffer.append(line);
			line = reader.readLine();
			buffer.append("\r\n"); //ªª––
		}
//		reader2.close();
		reader.close();
		String str = buffer.toString();
		double priority = twcu.priority("http://www.agri.cn/", str);
		System.out.println("priority «:"+priority);
		System.out.println(twcu.accept("http://www.agri.cn/", str));
        long end = System.currentTimeMillis()-start;
        System.out.println("∫ƒ ±" + end + "∫¡√Î");
	}
}
