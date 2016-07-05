package se.a.notused;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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

public class UrlExtractor {
	//ListPage seeds
    private static String url1 = "http://www.agri.cn/kj/nszd/";
    private static String url2 = "http://www.agri.cn/kj/nyqxqb/tqyb/";
    private static String url3 = "http://www.agri.cn/kj/nyqxqb/zb/";
    private static String url4 = "http://www.agri.cn/kj/nyqxqb/yb/";
    private static String url5 = "http://www.agri.cn/kj/nyqxqb/jb/";
    private static String url6 = "http://www.agri.cn/kj/nyqxqb/nzwzb/";
    private static String url7 = "http://www.agri.cn/kj/syjs/zzjs/";
    private static String url8 = "http://www.agri.cn/kj/syjs/yzjs/";
    private static String url9 = "http://www.agri.cn/kj/syjs/jgjs/";
    private static String url10 = "http://www.agri.cn/V20/KJ/xxhjs_1/nyxxh/";
    private static String url11 = "http://www.agri.cn/V20/KJ/xypz/";
    private static String url12 = "http://www.agri.cn/kj/nqjc/";
    private static String url13 = "http://www.agri.cn/kj/nmzf/";
    
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
	 * 把所有翻页的导航页面加入HashSet，准备队列
	 * @param nvglinks
	 * @param url
	 * @param num
	 */
	private static void addNvgLinks(Set<String> nvglinks,String url, int num){		
		for(int i=1;i<=num;i++){
			String tmpurl = url+"index_"+i+".htm";
			nvglinks.add(tmpurl);
		}
	}
	/**
	 * 得到所有导航页面url--普通版
	 * @return
	 */
	public static Set<String> getAllnavigationLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 得到所有导航页面url
	 * @return         HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllKjNavigationLinks() {
		Set<String> nvglinks = new HashSet<String>();  
		for(int i=1; i<14; i++){       //13个初始及其翻页url加入HashSet，准备队列
			if(i==1){
				nvglinks.add(url1);	
				addNvgLinks(nvglinks,url1,21);
			}else if(i==2){
				nvglinks.add(url2);	
				addNvgLinks(nvglinks,url2,66);
			}else if(i==3){
				nvglinks.add(url3);	
				addNvgLinks(nvglinks,url3,6);
			}if(i==4){
				nvglinks.add(url4);	
				nvglinks.add(url4+"index.htm");	
				addNvgLinks(nvglinks,url4,1);
			}else if(i==5){
				nvglinks.add(url5);	
//				addNvgLinks(nvglinks,url5,66);
			}else if(i==6){
				nvglinks.add(url6);	
//				addNvgLinks(nvglinks,url6,6);
			}else if(i==7){
				nvglinks.add(url7);	
				addNvgLinks(nvglinks,url7,66);
			}if(i==8){
				nvglinks.add(url8);	
				addNvgLinks(nvglinks,url8,66);
			}else if(i==9){
				nvglinks.add(url9);	
				addNvgLinks(nvglinks,url9,66);
			}else if(i==10){
				nvglinks.add(url10);	
				addNvgLinks(nvglinks,url10,66);
			}if(i==11){
				nvglinks.add(url11);	
				addNvgLinks(nvglinks,url1,1);
			}else if(i==12){
				nvglinks.add(url12);	
				addNvgLinks(nvglinks,url12,33);
			}else if(i==13){
				nvglinks.add(url13);	
				addNvgLinks(nvglinks,url13,33);
			}			
		}		
		return nvglinks;
	}
	
	/**
	 * 通过种子地址获取导航页面url集合
	 * @param srcpath    存放url种子的路径
	 * @return
	 */
	private static Set<String> getNvglinksBySeeds(String srcpath){
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
						if(num!=0){    			//表示至少有1个翻页
							String tmpurl = line+"index_"+i+".htm";
							nvglinks.add(tmpurl);     
//							System.out.println("add new url: "+tmpurl);	
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
	 * 得到所有http://www.banana.agri.cn/科技板块的导航页面url
	 * @return   HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaKjNavigationLinks() {
		Set<String> nvglinks = null; 
		String src = "urlseeds/agriBananaSeeds/agriBananaKjSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;
	}
	
	/**
	 * 得到所有http://www.banana.agri.cn/新闻板块的导航页面url
	 * @return		HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaXinWenNavigationLinks() {
		Set<String> nvglinks = null; 
		String src = "urlseeds/agriBananaSeeds/agriBananaXinWenSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;
	}
	
	/**
	 * 得到所有http://www.banana.agri.cn/市场板块的导航页面url
	 * @return		HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaShiChangNavigationLinks() {
		Set<String> nvglinks = null;  
		String src = "urlseeds/agriBananaSeeds/agriBananaShiChangSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;		
	}
	
	/**
	 * 得到所有http://www.banana.agri.cn/服务板块的导航页面url
	 * @return		HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaFuWuNavigationLinks(){
		Set<String> nvglinks = null;  
		String src = "urlseeds/agriBananaSeeds/agriBananaFuWuSeeds.txt";
		nvglinks = getNvglinksBySeeds(src);
		return nvglinks;
	}
	
	/**
	 * 得到所有http://www.banana.agri.cn/文化板块的导航页面url
	 * @return		HashSet<String> 所有导航页面url集合
	 */
	public static Set<String> getAllBananaWenHuaNavigationLinks() {
		Set<String> nvglinks = null;  
		String src = "urlseeds/agriBananaSeeds/agriBananaWenHuaSeeds.txt";
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
