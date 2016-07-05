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
	     //��ȡxml�ļ�����ȡDocument,����DOM��
	     Document document = reader.read(new File("xml/skills.xml"));
	     //δ֪ҳ��
	     String[] path = labelpath.getfrequentpath(document);
		 String context = labelpath.getUnknowPageContext(document,path);
		 //��ϸҳ��
//	     String context =  new LabelPath().getDetailPageContext(document);
		 //����ҳ��
	     System.out.println(context);
		 long end = System.currentTimeMillis() - begin; 
		 System.out.println("��ʱ��" + end + "����");

	}

}
