package se.crawler.url.queue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Queue;
/**
 * 保存已经抓取过的及待抓取的URL地址
 * @author Administrator
 */
public class LinkQueue {
	private static Set<String> visitedUrl = new HashSet<String>();   //已访问的 url 集合
	private static Queue<String> unVisitedUrl = new PriorityQueue<String>();  //待访问的 url 集合
	private static HashMap<String,Integer> depth= new HashMap<String, Integer>();  //存储URL爬取深度

	//获得URL队列
//	public Queue<String> getUnVisitedUrl() {
//		return unVisitedUrl;
//	}
    //添加到访问过的URL队列中
	public static void addVisitedUrl(String url) { //不需要同步
		visitedUrl.add(url);
	}
    //移除访问过的URL
//	public synchronized void removeVisitedUrl(String url) {
//		visitedUrl.remove(url);
//	}

	//获取为访问URL队头，并移除之
    public synchronized static String getAUnVisitedurl(){
    	if(unVisitedUrl.isEmpty()){
    		return null;
    	}else{
    		String tmpurl = unVisitedUrl.poll(); //队头出列，并移除
    		return tmpurl;
    	}
    }
	// 保证每个 url 只被访问一次
	public synchronized static void addUnvisitedUrl(String url,int d) {
		if(url.endsWith("/")){
			if (url != null && !url.trim().equals("")				
	                && !visitedUrl.contains(url)      //确保已访问过的URL集合中不存在当前URL
					&& !unVisitedUrl.contains(url)){			
				String suburl = url.substring(0, url.length()-1);
				if(!visitedUrl.contains(suburl)&&!unVisitedUrl.contains(suburl)){					
					unVisitedUrl.add(url);
				    depth.put(url,d);
				}				
			}
		}else{
			if(url.contains("htm")){
				if (url!= null && !"".equals(url.trim())				
		                && !visitedUrl.contains(url)      //确保已访问过的URL集合中不存在当前URL
						&& !unVisitedUrl.contains(url)){
					unVisitedUrl.add(url);
				    depth.put(url,d);
				}
			}else{
				if (url!= null && !"".equals(url.trim())				
		                && !visitedUrl.contains(url)      //确保已访问过的URL集合中不存在当前URL
						&& !unVisitedUrl.contains(url)){
					String sub = url+"\\";
					if(!visitedUrl.contains(sub) &&!unVisitedUrl.contains(sub)){
						unVisitedUrl.add(url);
					    depth.put(url,d);
					}
				}
			}	
		}
	}
    //获得已经访问的URL数目
	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}
    //判断未访问的URL队列中是否为空
	public static boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}
	public static int geturldepth(String url){
		return depth.get(url);
	}

}
