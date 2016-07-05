package se.a.notused;

import java.util.HashSet;  
import java.util.Set;  
  
import org.htmlparser.Node;  
import org.htmlparser.NodeFilter;  
import org.htmlparser.Parser;  
import org.htmlparser.filters.NodeClassFilter;  
import org.htmlparser.filters.OrFilter;  
import org.htmlparser.tags.LinkTag;  
import org.htmlparser.util.NodeList;  
import org.htmlparser.util.ParserException;

import se.crawler.url.fontier.LinkFilter;  
  
/**
 * ��ȡ��ҳ�еĳ�����
 * @author Administrator
 *
 */
public class HtmlParserTool {  
  
    // ����������URL��ȡĳ��html�ĵ�����Ƕ������  
    public static Set<String> extractLinks(String content, LinkFilter filter,String charset) {  
        Set<String> links = new HashSet<String>();  
        try {  
            // 1������һ��Parser����������ص�����  
//            Parser parser = new Parser(url);  
            @SuppressWarnings("static-access")
			Parser parser = new Parser().createParser(content, charset); //������ҳ���ݳ�ȡ
            parser.setEncoding(charset);  
  
            // 2.1���Զ���һ��Filter�����ڹ���<Frame >��ǩ��Ȼ��ȡ�ñ�ǩ�е�src����ֵ  
            NodeFilter frameNodeFilter = new NodeFilter() {  
                @Override  
                public boolean accept(Node node) {  
                    if (node.getText().startsWith("frame src=")) {  
                        return true;  
                    } else {  
                        return false;  
                    }  
                }  
            };           
            //2.2�������ڶ���Filter������<a>��ǩ  
            NodeFilter aNodeFilter = new NodeClassFilter(LinkTag.class);  
              
            //2.3����������2��Filter�γ�һ������߼�Filter��  
            OrFilter linkFilter = new OrFilter(frameNodeFilter, aNodeFilter);  
              
            //3��ʹ��parser����filter��ȡ�����з��������Ľڵ�  
            NodeList nodeList = parser.extractAllNodesThatMatch(linkFilter);  
              
            //4����ȡ�õ�Node���д���  
            for(int i = 0; i<nodeList.size();i++){  
                Node node = nodeList.elementAt(i);  
                String linkURL = "";  
                //�����������Ϊ<a />  
                if(node instanceof LinkTag){  
                    LinkTag link = (LinkTag)node;  
                    linkURL= link.getLink();  
                }else{  
                    //�������Ϊ<frame />  
                    String nodeText = node.getText();  
                    int beginPosition = nodeText.indexOf("src=");  
                    nodeText = nodeText.substring(beginPosition);  
                    int endPosition = nodeText.indexOf(" ");  
                    if(endPosition == -1){  
                        endPosition = nodeText.indexOf(">");  
                    }  
                    linkURL = nodeText.substring(5, endPosition - 1);  
                }  
                //�ж��Ƿ����ڱ���������Χ��url  
                if(filter.accept(linkURL)){  
                    links.add(linkURL);  
                }  
            }              
        } catch (ParserException e) {  
            e.printStackTrace();  
        }  
        return links;  
    }  
}  