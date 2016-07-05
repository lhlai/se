package se.crawler.url.queue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Queue;
/**
 * �����Ѿ�ץȡ���ļ���ץȡ��URL��ַ
 * @author Administrator
 */
public class LinkQueue {
	private static Set<String> visitedUrl = new HashSet<String>();   //�ѷ��ʵ� url ����
	private static Queue<String> unVisitedUrl = new PriorityQueue<String>();  //�����ʵ� url ����
	private static HashMap<String,Integer> depth= new HashMap<String, Integer>();  //�洢URL��ȡ���

	//���URL����
//	public Queue<String> getUnVisitedUrl() {
//		return unVisitedUrl;
//	}
    //��ӵ����ʹ���URL������
	public static void addVisitedUrl(String url) { //����Ҫͬ��
		visitedUrl.add(url);
	}
    //�Ƴ����ʹ���URL
//	public synchronized void removeVisitedUrl(String url) {
//		visitedUrl.remove(url);
//	}

	//��ȡΪ����URL��ͷ�����Ƴ�֮
    public synchronized static String getAUnVisitedurl(){
    	if(unVisitedUrl.isEmpty()){
    		return null;
    	}else{
    		String tmpurl = unVisitedUrl.poll(); //��ͷ���У����Ƴ�
    		return tmpurl;
    	}
    }
	// ��֤ÿ�� url ֻ������һ��
	public synchronized static void addUnvisitedUrl(String url,int d) {
		if(url.endsWith("/")){
			if (url != null && !url.trim().equals("")				
	                && !visitedUrl.contains(url)      //ȷ���ѷ��ʹ���URL�����в����ڵ�ǰURL
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
		                && !visitedUrl.contains(url)      //ȷ���ѷ��ʹ���URL�����в����ڵ�ǰURL
						&& !unVisitedUrl.contains(url)){
					unVisitedUrl.add(url);
				    depth.put(url,d);
				}
			}else{
				if (url!= null && !"".equals(url.trim())				
		                && !visitedUrl.contains(url)      //ȷ���ѷ��ʹ���URL�����в����ڵ�ǰURL
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
    //����Ѿ����ʵ�URL��Ŀ
	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}
    //�ж�δ���ʵ�URL�������Ƿ�Ϊ��
	public static boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}
	public static int geturldepth(String url){
		return depth.get(url);
	}

}
