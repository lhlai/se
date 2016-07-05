package se.crawler.framework;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Set;

import se.crawler.url.fontier.BananaLinkFilter;
import se.crawler.url.fontier.LinkFilter;
import se.crawler.url.queue.LinkQueue;

/**
 * ��ȡ�й��㽶��ҳ�߳� ---��������
 * @since 2016.6.15
 * @author pillar
 * @version 1.0
 */
public class CrawlerBanana implements Runnable {
	private DownLoadPage crawler = null;
	public static final Object signal = new Object();
	private LinkFilter filter = new BananaLinkFilter();           //����URL������2
	private int count = 0;        //�ȴ����߳�
//	private int crawDepth  = 4;   //�������
    private int downNum = 0;      //������ҳ������
    
	@Override
	public void run() {
		while(true){
			String tmpurl = LinkQueue.getAUnVisitedurl();			
			if(tmpurl!=null){
				String content = null;
				try {
					crawler = new DownLoadPage();
					int d = LinkQueue.geturldepth(tmpurl);      	//��ȡ���	
					content = crawler.downloadFile(tmpurl,d);
				    if(content!=null){		    				    	
						System.out.println("����ҳ"+tmpurl+"�ɹ������Ϊ"+d+" �����߳�"+Thread.currentThread().getName()+"����");
						LinkQueue.addVisitedUrl(tmpurl);	            //���� url ���뵽�ѷ��ʵ� URL��
//					    String charset = crawler.getEncoding();     	//��ȡ��������ҳ�ı����ַ���  
					    if(d==1){ 	   									//��ҳʱ�����е���ҳ���url�Ž�����
					    	Set<String> links =null;  
					    	Set<String> links2 = null;  
					    	Set<String> links3 = null;  
					    	Set<String> links4 = null;  
					    	Set<String> links5 = null; 
					    	if("http://www.banana.agri.cn/"==tmpurl){					    		
					    		links = UrlExtractorBanana.getAllBananaKjNavigationLinks();
					    		System.out.println("�й��㽶���Ƽ���鹲��: "+links.size()+" ������ҳ��!");
					    		links2 = UrlExtractorBanana.getAllBananaXinWenNavigationLinks();
					    		System.out.println("�й��㽶�����Ű�鹲��: "+links2.size()+" ������ҳ��!");
					    		links.addAll(links2);
					    		links3 = UrlExtractorBanana.getAllBananaShiChangNavigationLinks();
					    		System.out.println("�й��㽶���г���鹲��: "+links3.size()+" ������ҳ��!");
					    		links.addAll(links3);
					    		links4 = UrlExtractorBanana.getAllBananaFuWuNavigationLinks();
					    		System.out.println("�й��㽶�������鹲��: "+links4.size()+" ������ҳ��!");
					    		links.addAll(links4);
					    		links5 = UrlExtractorBanana.getAllBananaWenHuaNavigationLinks();
					    		System.out.println("�й��㽶���Ļ���鹲��: "+links.size()+" ������ҳ��!");
					    		links.addAll(links5);
					    		System.out.println("�й��㽶������: "+links.size()+" ������ҳ��!");
					    		for(String link:links){					   			
						            LinkQueue.addUnvisitedUrl(link,d+1); 	  			//���е���ҳ�����
					            }
					    	}else{
					    		System.out.println("����ץȡ�й��㽶���Ķ�������,���ṩ������url����: "+tmpurl);
					    	}
				    	}else{				    		
				    		String urlType = "";
				    		if(tmpurl.contains("htm")&&!tmpurl.contains("index")){	//��ϸҳ��
					    		urlType = "DetailPage";
				    			saveToLocal(urlType,content,tmpurl);        		//��ϸҳ�汣��������
					    	}else if(tmpurl.contains("doc")){
					    		urlType = "DocPage";
				    			saveToLocal(urlType,content,tmpurl);        		//��ϸҳ�汣��������
					    	}else{  				
					    		urlType = "ListPage";
					    		Set<String> links = null;
					    		saveToLocal(urlType,content,tmpurl); 				    
					    		links = UrlExtractorBanana.extractLinks(tmpurl,content,filter);
					    		for(String link:links){
						            LinkQueue.addUnvisitedUrl(link,d+1); 			//����ȡ����ϸҳ��URL���						            
					            }
					    	}
				    	}					    			            				           				            
				    }
				    if(count>0){    
					    synchronized(signal){								    	
						    count--;
						    signal.notify();   //����˯�ߵ��߳�
			            }
		            }					    
				}catch (URISyntaxException e) {				
					e.printStackTrace();
				}
		    }else{
		    	synchronized(signal){  		  //signalΪͬ��������
		    		try{
		    			count++; 
		    			System.out.println("������"+count+"���߳��ڵȴ�");
		    			signal.wait();	//��ǰ�߳���ͣ
		    		}catch(InterruptedException e){
		    			e.printStackTrace();
		    		}
		    	}
		    }
		}
	}

	/**
	 * ����������
	 * @author pillar
	 * @param urlType   ҳ������
	 * @param content   ��ҳԴ����
	 * @param url    	��ҳurl��ַ
	 */
	private void saveToLocal(String urlType,String content, String url) {
		String path = "";
		String tmppath = "";
		File files = null;
		if(urlType=="DetailPage"){
			if(url.contains("keji")){
				tmppath = "html/banana-kj/DetailPage/";				
			}else if(url.contains("shichang")){
				tmppath = "html/banana-sc/DetailPage/";
			}else if(url.contains("xinwen")){
				tmppath = "html/banana-xw/DetailPage/";
			}else if(url.contains("fuwu")){
				tmppath = "html/banana-fw/DetailPage/";
			}else if(url.contains("wenhua")){
				tmppath = "html/banana-wh/DetailPage/";
			}else{}
//			�����ļ�Ŀ¼
			files = new File(tmppath);
			if(!files.exists()){
				files.mkdirs();
			}	
			path = tmppath+getFilePathByUrl(url);    //�����ϸҳ���·��
		}else if(urlType=="ListPage"){
			if(url.contains("keji")){
				tmppath = "html/banana-kj/ListPage/";				
			}else if(url.contains("shichang")){
				tmppath = "html/banana-sc/ListPage/";
			}else if(url.contains("xinwen")){
				tmppath = "html/banana-xw/ListPage/";
			}else if(url.contains("fuwu")){
				tmppath = "html/banana-fw/ListPage/";
			}else if(url.contains("wenhua")){
				tmppath = "html/banana-wh/ListPage/";
			}else{}
//			�����ļ�Ŀ¼
			files = new File(tmppath);
			if(!files.exists()){
				files.mkdirs();
			}	
			path = tmppath+getFilePathByUrl(url);    //��ŵ���ҳ���·��
		}else if(urlType=="DocPage"){
			if(url.contains("keji")){
				tmppath = "html/banana-kj/DocPage/";				
			}else if(url.contains("shichang")){
				tmppath = "html/banana-sc/DocPage/";
			}else if(url.contains("xinwen")){
				tmppath = "html/banana-xw/DocPage/";
			}else if(url.contains("fuwu")){
				tmppath = "html/banana-fw/DocPage/";
			}else if(url.contains("wenhua")){
				tmppath = "html/banana-wh/DocPage/";
			}else{}
//			�����ļ�Ŀ¼
			files = new File(tmppath);
			if(!files.exists()){
				files.mkdirs();
			}	
			path = tmppath+getFilePathByUrl(url);      //���docҳ���·��
		}else{}
		String result = "<!--url:"+url+"-->"+"\r\n"+content;   //��url��ַ����html�ļ�����
		try {
			byte[] data = result.getBytes("utf-8");  
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(path)));
			for (int i = 0; i < data.length; i++){
				out.write(data[i]);
			}
			out.flush();   			//��ջ�����
			out.close();   
			synchronized(signal){  
		    	downNum++;      	//����ɹ�����������ҳ����һ
		    }
			System.out.print("SaveToLocal: "+url+"\n");
		}catch(Exception e){
			System.out.print("Fail to save page: "+url+"\n");
			e.printStackTrace();
		}
	}
	/**
	 * ����url�ƶ��ļ���
	 * @author pillar
	 * @param url	��ҳ��URL��ַ
	 * @return	���ش�������Ϊ�ļ�����URLƬ��
	 */
	private String getFilePathByUrl(String url){
		String uri = url.substring(7);	//remove "http://";
		String contentType = crawler.getContentType();
		if(contentType!=null){		
			if(contentType.indexOf("html")!=-1){  //text/html����
				if(uri.contains("htm")){
					return uri.replaceAll("[\\/:*?\"<>|]", "_");
				}else{
					return uri.replaceAll("[\\/:*?\"<>|]", "_")+".html";
				}
			}else{ //��application/pdf����				
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
	
}
