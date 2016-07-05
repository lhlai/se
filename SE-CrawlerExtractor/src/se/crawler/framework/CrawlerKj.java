package se.crawler.framework;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import se.crawler.url.fontier.AgriKejiLinkFilter;
import se.crawler.url.fontier.LinkFilter;
import se.crawler.url.queue.LinkQueue;

/**
 * 爬取中国农业信息网科技板块子线程 ---定制爬虫
 * @author pillar
 * @version 1.0
 */
public class CrawlerKj implements Runnable {
	private DownLoadPage crawler = null;
	public static final Object signal = new Object();
	private LinkFilter filter = new AgriKejiLinkFilter();            //定义URL过滤器2
	private int count = 0;        //等待的线程
//	private int crawDepth  = 4;   //爬虫深度
    private int downNum = 0;      //下载网页的数量
    
	@Override
	public void run() {
		while(true){
			String tmpurl = LinkQueue.getAUnVisitedurl();			
			if(tmpurl!=null){
				String content = null;
				try {
					crawler = new DownLoadPage();
					int d = LinkQueue.geturldepth(tmpurl);      //爬取深度	
					content = crawler.downloadFile(tmpurl,d);
				    if(content!=null){		    				    	
						System.out.println("爬网页"+tmpurl+"成功，深度为"+d+" 是由线程"+Thread.currentThread().getName()+"来爬");
						LinkQueue.addVisitedUrl(tmpurl);	            //将该 url 放入到已访问的 URL中
//					    String charset = crawler.getEncoding();     	//提取出下载网页的编码字符集  
					    if(d==1){ 	   //首页时把所有导航页面的url放进队列
					    	Set<String> links = null;  
					    	if("http://www.agri.cn/kj/"==tmpurl){
					    		links = UrlExtractorKeji.getAllKjNavigationLinks();	
					    		System.out.println("中国农业信息网科技板块共有: "+links.size()+" 个导航页面!");
					    		for(String link:links){
						            LinkQueue.addUnvisitedUrl(link,d+1); 	  			//所有导航页面入队
					            }
					    	}else{
					    		System.out.println("这是抓取中国农业信息网科技板块定制爬虫,您提供的种子url有误: "+tmpurl);
					    		links = UrlExtractorKeji.getAllnavigationLinks();	
					    	}			    	
					    }
				    }else{				    	
				    	String urlType = "";
				    	if(tmpurl.contains("htm")&&!tmpurl.contains("index")){	//详细页面
					    	urlType = "DetailPage";
				    		saveToLocal(urlType,content,tmpurl);        		//详细页面保存至本地
					    }else if(tmpurl.contains("doc")){
					    	urlType = "DocPage";
				    		saveToLocal(urlType,content,tmpurl);        		//详细页面保存至本地
					    }else{  								    			//导航页面抽取url
					    	urlType = "ListPage";
					    	Set<String> links = null;
					    	saveToLocal(urlType,content,tmpurl); 				    
					    	links = UrlExtractorKeji.extractLinks(tmpurl,content,filter);
					    	for(String link:links){
						          LinkQueue.addUnvisitedUrl(link,d+1); 	//新提取的未访问的详细页面URL入队
					         }
					    }
				    }					    			            				           				            				   
				    if(count>0){    
					    synchronized(signal){								    	
						    count--;
						    signal.notify();   //唤醒睡眠的线程
			            }
		            }					    
				}catch (URISyntaxException e) {				
					e.printStackTrace();
				}
		    }else{
		    	synchronized(signal){  //signal为同步监视器
		    		try{
		    			count++; 
		    			System.out.println("现在有"+count+"个线程在等待");
		    			signal.wait();	//当前线程暂停
		    		}catch(InterruptedException e){
		    			e.printStackTrace();
		    		}
		    	}
		    }
		}
	}

	/**
	 * 保存至本地
	 * @author pillar
	 * @param content	爬取的网页源代码
	 * @param url		网页url地址
	 * @param tmpurl 
	 */
	private void saveToLocal(String urlType,String content, String url) {
//		String encode = crawler.getEncoding();
		String path = "";
		String tmppath = "";
		File files = null;
		if(urlType=="DetailPage"){
			tmppath = "html/kj/DetailPage/";
//			创建文件目录
			files = new File(tmppath);
			if(!files.exists()){
				files.mkdirs();
			}	
			path = tmppath+getFilePathByUrl(url);   	 //存放详细页面的路径
		}else if(urlType=="ListPage"){
			tmppath = "html/kj/ListPage/";
			files = new File(tmppath);
			if(!files.exists()){
				files.mkdirs();
			}	
			path = tmppath+getFilePathByUrl(url);        //存放导航页面的路径
		}else if(urlType=="DocPage"){
			tmppath = "html/kj/DocPage/";
			files = new File(tmppath);
			if(!files.exists()){
				files.mkdirs();
			}	
			path = tmppath+getFilePathByUrl(url);     
		}else{}
		String result = "<!--url:"+url+"-->"+"\r\n"+content;   //把url地址存入html文件首行
		try {
			byte[] data = result.getBytes("utf-8");  
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					new File(path)));
			for (int i = 0; i < data.length; i++){
				out.write(data[i]);
			}
			out.flush();   			//清空缓冲区
			out.close();   
			synchronized(signal){  
		    	downNum++;      	//保存成功后下载数网页数加一
		    }
			System.out.print("SaveToLocal: "+url+"\n");
		}catch(Exception e){
			System.out.print("Fail to save page: "+url+"\n");
			e.printStackTrace();
		}
	}
	/**
	 * 根据url制定文件名
	 * @author pillar
	 * @param url	网页的URL地址
	 * @return	返回处理后可作为文件名的URL片断
	 */
	private String getFilePathByUrl(String url){
		String uri = url.substring(7);	//remove "http://";
		String contentType = crawler.getContentType();
		if(contentType!=null){		
			if(contentType.indexOf("html")!=-1){  //text/html类型
				if(uri.contains("htm")){
					return uri.replaceAll("[\\/:*?\"<>|]", "_");
				}else{
					return uri.replaceAll("[\\/:*?\"<>|]", "_")+".html";
				}
			}else{ //如application/pdf类型				
	            return uri.replaceAll("[\\?/:*|<>\"]", "_")+"."+
	            contentType.substring(contentType.lastIndexOf("/")+1);
			}
		}else{
			if(uri.contains("htm")){
				return uri.replaceAll("[\\/:*?\"<>|]", "_");
			}else{
				return uri.replaceAll("[\\/:*?\"<>|]", "_")+".html";
			}
		}			
	}
	
	public int getCount(){
		return count; 
	}
	public int getDownNum(){
		return downNum;
	}
	
//    public static void main(String[] args){
//    	LinkQueue.addUnvisitedUrl("http://www.agri.cn/kj/",1);
//    	CrawlerKj crawler = new CrawlerKj();
//    	crawler.run();
//    }
}
