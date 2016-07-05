package se.extractor.algorithm;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
/**
 * 网页分类器：
 *     统计网页中超链接个数，计算超链接密度
 * @author Administrator
 */
public class PageClassifier {
	private int HyperLinkNum = 0;
	private int pathNum = 0;
	
	//判断网页类型
	public String jugePageType(Document document) {
		Element root = document.getRootElement();
		PageClassifier classifier = new PageClassifier();
		double HyperLinkNum = classifier.countHyperLinkNum(root);
		double pathnum = classifier.getPathNum();
		double ratio = HyperLinkNum/pathnum;
//		System.out.println("ratio:"+ratio);
//		System.out.println("HyperLinkNum:"+HyperLinkNum);
//		System.out.println("pathNum:"+pathnum);
		if(ratio<0.3&&HyperLinkNum<250.0){  //根据链接密度及链接个数判断
		    return "DetailPage";
		}else{
		    if(ratio>0.5||HyperLinkNum>400){
		    	return "ListPage";
		    }else{
		    	return "UnknowPage";
		    }
		}
	}
	//叶子节点总数
	public int getPathNum(){
		return this.pathNum;
	}
	//计算超链接数
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
	        //使用递归  ,迭代当前节点下面的所有子节点
			@SuppressWarnings("unchecked")
			Iterator<Element> iterator = node.elementIterator();  
            while(iterator.hasNext()){
                Element e = iterator.next();  
                countHyperLinkNum(e);  
            }  
	    }	
		return this.HyperLinkNum;
	}
	//字符串中是否含有中文
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
