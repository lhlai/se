package se.a.notused;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 1)抽取导航页面内详细页面的url地址
 * 2)获取所有导航页面的url地址
 * @version 2.0
 * @author pillar
 * @date　  2016.6.13
 */
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.crawler.url.fontier.LinkFilter;

public class UrlExtractorBananaold {

    
    /**
     * 抽取导航页面内详细页面的url地址
     * @param url       
     * @param content   String 某个导航页面源码
     * @param filter    LinkFilter url过滤器
     * @return   HashSet<String> 某个导航页面内所有详细url的集合
     */
	public static Set<String> extractLinks(String url, String content, LinkFilter filter) {
		Set<String> links = new HashSet<String>();  
		Document document = Jsoup.parse(content);
		// 详细页面标签:<a class="" href="" target="_blank" />
		Elements elements = document.getElementsByTag("a");		
		for(Element element : elements){						
			String href = element.attr("href"); //href属性
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
							links.add(href);    //解析到底href加入hashset
						}
					}catch(Exception e){
						System.out.println("This is an exception url!");
//						e.printStackTrace();
					}					
				}else{
//					href = href;
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
	 * 通过种子地址获取导航页面url集合
	 * @param srcpath    存放url种子的路径
	 * @return
	 */
	public static Set<String> getNvglinksBySeeds(String srcpath){
		Set<String> nvglinks = new HashSet<String>();  
		try{
			BufferedReader reader = new BufferedReader(new FileReader(srcpath)); 
			String line = reader.readLine();
			while(line!=null){
//				System.out.println("line: "+line);
				if(line.contains("http")){
					nvglinks.add(line);
//					System.out.println("add new url: "+line);	
					String pageNum = reader.readLine();					
					int num = Integer.parseInt(pageNum.substring(pageNum.length()-1));
					for(int i=1;i<=num;i++){
						String tmpurl = line+"index_"+i+".htm";
						nvglinks.add(tmpurl);     
//						System.out.println("add new url: "+tmpurl);																							
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
	 * 得到所有http://www.banana.agri.cn/科技板块的导航页面url
	 * @return   HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaKjNavigationLinks() {
		Set<String> nvglinks = null; 
		String src = "agriBananaSeeds/agriBananaKjSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;
	}
	
	/**
	 * 得到所有http://www.banana.agri.cn/新闻板块的导航页面url
	 * @return		HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaXinWenNavigationLinks() {
		Set<String> nvglinks = null; 
		String src = "agriBananaSeeds/agriBananaXinWenSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;
	}
	
	/**
	 * 得到所有http://www.banana.agri.cn/市场板块的导航页面url
	 * @return		HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaShiChangNavigationLinks() {
		Set<String> nvglinks = null;  
		String src = "agriBananaSeeds/agriBananaShiChangSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;		
	}
	
	/**
	 * 得到所有http://www.banana.agri.cn/服务板块的导航页面url
	 * @return		HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaFuWuNavigationLinks() {
		Set<String> nvglinks = null;  
		String src = "agriBananaSeeds/agriBananaFuWuSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;
	}
	
	/**
	 * 得到所有http://www.banana.agri.cn/文化板块的导航页面url
	 * @return		HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaWenHuaNavigationLinks() {
		Set<String> nvglinks = null;  
		String src = "agriBananaSeeds/agriBananaWenHuaSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;
	}

//	public static void main(String[] args){
//		Set<String> nvglinks = getAllBananaKjnavigationLinks();
//		for(String link:nvglinks){
//			System.out.println("nvglink: "+link);
//		}
//	}
}
