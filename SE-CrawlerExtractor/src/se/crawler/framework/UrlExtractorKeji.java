package se.crawler.framework;
/**
 * 1)��ȡ����ҳ������ϸҳ���url��ַ
 * 2)��ȡ���е���ҳ���url��ַ
 * @version 2.0
 * @author pillar
 * @date��  2016.6.13
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.crawler.url.fontier.LinkFilter;

public class UrlExtractorKeji {

	/**
     * ��ȡ����ҳ������ϸҳ���url��ַ
     * @param url       
     * @param content   String ĳ������ҳ��Դ��
     * @param filter    LinkFilter url������
     * @return   HashSet<String> ĳ������ҳ����������ϸurl�ļ���
     */
	public static Set<String> extractLinks(String url, String content, LinkFilter filter) {
		Set<String> links = new HashSet<String>();  
		Document document = Jsoup.parse(content);
		// ��ϸҳ���ǩ:<a class="" href="" target="_blank" />
		Elements elements = document.getElementsByTag("a");		
		for(Element element : elements){						
			String href = element.attr("href"); //href����
			if(href!=""){								
				if(!href.contains("http://")){
					try{						
						if(url.contains("index")){
							String tmpurl = url.substring(0, url.indexOf("index"));
							href = tmpurl+href.substring(2);
						}else{
							href = url+href.substring(2);
						}
						if(filter.accept(href)){							
							links.add(href);    //��������href����hashset
						}
					}catch(Exception e){
						System.out.println("This is an exception url!");
					}					
				}	
			}else{	
				href = element.attr("src");	
				if(""==href){					
				}
			}					
		}	
		return links;
	}
	/**
	 * ͨ�����ӵ�ַ��ȡ����ҳ��url����----���ж����ڴ�
	 * @param srcpath    ���url���ӵ�·��
	 * @return
	 */
	private static Set<String> getNvglinksBySeeds(String srcpath){
		Set<String> nvglinks = new HashSet<String>();  
		try{
			BufferedReader reader = new BufferedReader(new FileReader(srcpath)); 
			String line = reader.readLine();   //���ж����ڴ�
			while(line!=null){
				if(line.contains("http")){
					nvglinks.add(line);
					String pageNum = reader.readLine();					
					int num = Integer.parseInt(pageNum.substring(pageNum.indexOf("=")+1));
					for(int i=1;i<=num;i++){
						if(num!=0){    			//��ʾ������1����ҳ
							String tmpurl = line+"index_"+i+".htm";
							nvglinks.add(tmpurl);     
						}																												
					}
					line = reader.readLine();
				}else{
					line = reader.readLine();	
				}
			}
			reader.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return nvglinks;
	}
	
	/**
	 * �õ�http://www.agri.cn/kj/  ҳ�����е���ҳ��url
	 * ������http://www.agri.cn/ �Ƽ����ĵ���ҳ��url
	 * @return   HashSet<String> ���е���ҳ��url����
	 */
	public static Set<String> getAllKjNavigationLinks() {
		Set<String> nvglinks = null; 
		String src = "urlseeds/agriKejiSeeds/agriKejiSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;
	}
	/**
	 * �õ����е���ҳ��url--��ͨ��
	 * @return
	 */
	public static Set<String> getAllnavigationLinks() {
		System.out.println("�˹��ܴ�����...��getAllnavigationLinks()!");
		return null;
	}
}
