package test.se.extractor;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import se.extractor.algorithm.DepthFirstTraversal;

public class TestLabelPath {

	public static void main(String[] args) throws Exception {
		 long begin = System.currentTimeMillis();
		 DepthFirstTraversal labelpath = new DepthFirstTraversal();
		 SAXReader reader = new SAXReader();
	     //读取xml文件，获取Document,构建DOM树
	     Document document = reader.read(new File("xml/skills.xml"));
	     //未知页面
	     String[] path = labelpath.getfrequentpath(document);
		 String context = labelpath.getUnknowPageContext(document,path);
		 //详细页面
//	     String context =  new LabelPath().getDetailPageContext(document);
		 //导航页面
	     System.out.println(context);
		 long end = System.currentTimeMillis() - begin; 
		 System.out.println("耗时：" + end + "毫秒");

	}

}
