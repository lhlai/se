package se.extractor.algorithm;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
/**
 * ��ҳ��������
 *     ͳ����ҳ�г����Ӹ��������㳬�����ܶ�
 * @author Administrator
 */
public class PageClassifier {
	private int HyperLinkNum = 0;
	private int pathNum = 0;
	
	//�ж���ҳ����
	public String jugePageType(Document document) {
		Element root = document.getRootElement();
		PageClassifier classifier = new PageClassifier();
		double HyperLinkNum = classifier.countHyperLinkNum(root);
		double pathnum = classifier.getPathNum();
		double ratio = HyperLinkNum/pathnum;
//		System.out.println("ratio:"+ratio);
//		System.out.println("HyperLinkNum:"+HyperLinkNum);
//		System.out.println("pathNum:"+pathnum);
		if(ratio<0.3&&HyperLinkNum<250.0){  //���������ܶȼ����Ӹ����ж�
		    return "DetailPage";
		}else{
		    if(ratio>0.5||HyperLinkNum>400){
		    	return "ListPage";
		    }else{
		    	return "UnknowPage";
		    }
		}
	}
	//Ҷ�ӽڵ�����
	public int getPathNum(){
		return this.pathNum;
	}
	//���㳬������
	public int countHyperLinkNum(Element node){
		if(!node.getTextTrim().equals("")&&!node.getTextTrim().equals("|")&&
				!node.getPath().contains("script")){
			this.pathNum++;
			if(node.getTextTrim().contains("href")){
				this.HyperLinkNum++;
			}
			if(!node.attributes().isEmpty()){	
				@SuppressWarnings("unchecked")
				List<Attribute> attribute = node.attributes();
				Iterator<Attribute> it = attribute.listIterator();
				while(it.hasNext()){
					Attribute at = it.next();
					String attributes = at.getName();
					if(attributes.contains("href")){
						this.HyperLinkNum++;	
					}
				}
			}
			if(!node.isTextOnly()){
	            @SuppressWarnings("unchecked")
				Iterator<Element> iterator = node.elementIterator();  
	    	    while(iterator.hasNext()){  
	                 Element e = iterator.next();  
	                 countHyperLinkNum(e);  
	            }
			}
		} else{
	        //ʹ�õݹ�  ,������ǰ�ڵ�����������ӽڵ�
			@SuppressWarnings("unchecked")
			Iterator<Element> iterator = node.elementIterator();  
            while(iterator.hasNext()){
                Element e = iterator.next();  
                countHyperLinkNum(e);  
            }  
	    }	
		return this.HyperLinkNum;
	}
	//�ַ������Ƿ�������
//	private boolean isContainChinese(String str) {
//        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
//        Matcher m = p.matcher(str);        
//        if (m.find()) {
//            return true;
//        }else{
//        	return false;
//        }
//    }
}
