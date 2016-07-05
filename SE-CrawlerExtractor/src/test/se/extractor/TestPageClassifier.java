package test.se.extractor;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import se.extractor.algorithm.PageClassifier;

public class TestPageClassifier {
	
    //测试一和测试二不能同时进行
	public static void main(String[] args) throws DocumentException {
		 long begin = System.currentTimeMillis();
		 SAXReader reader = new SAXReader();
	     Document document = reader.read(new File("xml/banana17.xml"));
	     //测试一：jugePageType(Document document)方法
//	     System.out.println("document是:"+document);
	     System.out.println("This is a "+new PageClassifier().jugePageType(document));   
	      
	     //测试二：关键测试countHyperLinkNum(Element node)
//	     System.out.println(pageType(document));
		 long end = System.currentTimeMillis() - begin; 
		 System.out.println("耗时：" + end + "毫秒");
	}
//	private static String pageType(Document document){
//		Element root = document.getRootElement();
//		double HyperLinkNum = PageClassifier.countHyperLinkNum(root);
//		double ratio = HyperLinkNum/PageClassifier.getPathNum();
//		System.out.println("HyperLinkNum:"+HyperLinkNum);
//		System.out.println("path:"+PageClassifier.getPathNum());
//		System.out.println("ratio:"+ratio);
//		if(ratio<0.3&&HyperLinkNum<250.0){
//		    return "This is a DetailPage";
//		}else{
//		    if(ratio>0.5||HyperLinkNum>400){
//		    	return "This is a ListPage";
//		    }else{
//		    	return "This is a UnknowPage";
//		    }
//		}
//	}
}
