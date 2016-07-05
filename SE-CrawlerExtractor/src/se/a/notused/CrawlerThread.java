package se.a.notused;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import se.crawler.framework.DownLoadPage;
import se.crawler.url.fontier.ComputeUrl;
import se.crawler.url.fontier.AgriKejiLinkFilter;
import se.crawler.url.fontier.LinkFilter;
import se.crawler.url.fontier.TopicWordComputeUrl;
import se.crawler.url.queue.LinkQueue;

/**
 * 爬取网页子线程
 * @author pillar
 * @version 1.0
 */
public class CrawlerThread implements Runnable {
	private DownLoadPage crawler = null;
	public static final Object signal = new Object();
	private ComputeUrl contentfilter = new TopicWordComputeUrl();  //定义URL过滤器1
	private LinkFilter filter = new AgriKejiLinkFilter();             //定义URL过滤器2
	private int count = 0;        //等待的线程
	private int crawDepth  = 10;   //爬虫深度
    private int downNum = 0;      //下载网页的数量
    
	@Override
	public void run() {
		while(true){
			String tmpurl = LinkQueue.getAUnVisitedurl();
			if(tmpurl==null){
				System.out.println("tempurl:"+tmpurl);
			}else{	
				
			}
			if(tmpurl!=null){
				System.out.println("tmpurl:"+tmpurl);
				String content = null;
				try {
					crawler = new DownLoadPage();
					int d = LinkQueue.geturldepth(tmpurl);      //爬取深度
					content = crawler.downloadFile(tmpurl,d);
				    if(content!=null){
//				    	System.out.println("content:"+content);				    	
						System.out.println("爬网页"+tmpurl+"成功，深度为"+d+" 是由线程"+Thread.currentThread().getName()+"来爬");
						LinkQueue.addVisitedUrl(tmpurl);	        //将该 url 放入到已访问的 URL中
					    String charset = crawler.getEncoding();     //提取出下载网页的编码字符集  
					    if(d<crawDepth){   //限定爬取深度
				            Set<String> links = HtmlParserTool.extractLinks(content,filter,charset); //抽取该网页超链接	
				            System.out.println("links:"+links.size());
				            for(String link:links){
				            	System.out.println("content:"+123);
				            	System.out.println(link);
					            LinkQueue.addUnvisitedUrl(link,d+1); 	//新提取的未访问的URL入队
				            }					            
				        }
					    saveToLocal(content,tmpurl);     //通过主题相关性分析的保存至本地						    
					    if(count>0){    
						    synchronized(signal){								    	
							    count--;
							    signal.notify();  
				            }
			            }
					    
//					    if(contentfilter.accept(tmpurl, content)){  //进行链接分析，计算主题相关性
//						    saveToLocal(content,tmpurl);     //通过主题相关性分析的保存至本地						    
//						    if(count>0){    
//							    synchronized(signal){								    	
//								    count--;
//								    signal.notify();  
//					            }
//				            }
//					    }
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
	 */
	private void saveToLocal(String content, String url) {
		String encode = crawler.getEncoding();
		String path = "html/kj/"+getFilePathByUrl(url);     //文件的路径
		String result = "<!--url:"+url+"-->"+"\r\n"+content;   //把url地址存入html文件首行
		try {
			byte[] date = result.getBytes(encode);  
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					new File(path)));
			for (int i = 0; i < date.length; i++){
				out.write(date[i]);
			}
			out.flush();   			//清空缓冲区
			out.close();   
			synchronized(signal){  
		    	downNum++;      	//保存成功后下载数网页数加一
		    }
		} catch (IOException e) {
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
	
    public static void main(String[] args){
    	LinkQueue.addUnvisitedUrl("http://www.agri.cn/kj/",1);
    	CrawlerThread crawler = new CrawlerThread();
    	crawler.run();
//    	for(int i=0;i<10;i++){  
//			new Thread(crawler,"thread-"+i).start();
//		}
    }
}
