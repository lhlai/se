package se.crawler.framework;
/**
 * ��ȡ�й��������߳� ---��������
 * @since 2016.6.15
 * @author pillar
 * @version 1.0
 */
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Set;

import se.crawler.url.fontier.LinkFilter;
import se.crawler.url.fontier.TeaLinkFilter;
import se.crawler.url.queue.LinkQueue;

public class CrawlerTea implements Runnable{
	private DownLoadPage crawler = null;
	public static final Object signal = new Object();
	private LinkFilter filter = new TeaLinkFilter();           //����URL������2
	private int count = 0;        //�ȴ����߳�
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
					    if(d==1){ 	   									//��ҳʱ�����е���ҳ���url�Ž�����
					    	Set<String> links =null;  
					    	if("http://www.tea.agri.cn/"==tmpurl){					    		
					    		links = UrlExtractorTea.getAllTeaNavigationLinks();	
					    		System.out.println("�й���������: "+links.size()+" ������ҳ��!");
					    		for(String link:links){
						            LinkQueue.addUnvisitedUrl(link,d+1); 	  			//���е���ҳ�����
					            }
					    	}else{
					    		System.out.println("����ץȡ�й������Ķ�������,���ṩ������url����: "+tmpurl);
					    	}
				    	}else{				    		
				    		String urlType = "";
				    		if(tmpurl.contains("htm")&&!tmpurl.contains("index")){	//��ϸҳ��
					    		urlType = "DetailPage";				    			
					    		saveToLocal(urlType,content,tmpurl);        		//��ϸҳ�汣��������
					    	}else{  				
					    		urlType = "ListPage";
					    		Set<String> links = null;
					    		saveToLocal(urlType,content,tmpurl); 				    
					    		links = UrlExtractorTea.extractLinks(tmpurl,content,filter);
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
			if(url.contains("newscw")){     //����Ƶ��
				tmppath = "html/tea-newscw/DetailPage/";				
			}else if(url.contains("jspd")){  //����Ƶ��
				tmppath = "html/tea-jspd/DetailPage/";
			}else if(url.contains("shichangcw")){  //����Ƶ��
				tmppath = "html/tea-shichangcw/DetailPage/";
			}else if(url.contains("wenhuacw")){		//�Ļ�Ƶ��
				tmppath = "html/tea-wenhuacw/DetailPage/";
			}else if(url.contains("kejicw")){     	//Ʒ��Ƶ��
				tmppath = "html/tea-kejicw/DetailPage/";
			}else if(url.contains("fuwu")){     	//����Ƶ��
				tmppath = "html/tea-guoji/DetailPage/";
			}else if(url.contains("sppd/spzb")){     	//����Ƶ��
				tmppath = "html/tea-shipin/DetailPage/";
			}else if(url.contains("gdcg")){     	//������
				tmppath = "html/tea-gdcg/DetailPage/";
			}else{}
			files = new File(tmppath);
			if(!files.exists()){
				files.mkdirs();
			}	
			path = tmppath+getFilePathByUrl(url);    //�����ϸҳ���·��
		}else if(urlType=="ListPage"){
			if(url.contains("newscw")){     //����Ƶ��
				tmppath = "html/tea-newscw/ListPage/";				
			}else if(url.contains("jspd")){  //����Ƶ��
				tmppath = "html/tea-jspd/ListPage/";
			}else if(url.contains("shichangcw")){  //����Ƶ��
				tmppath = "html/tea-shichangcw/ListPage/";
			}else if(url.contains("wenhuacw")){		//�Ļ�Ƶ��
				tmppath = "html/tea-wenhuacw/ListPage/";
			}else if(url.contains("kejicw")){     	//Ʒ��Ƶ��
				tmppath = "html/tea-kejicw/ListPage/";
			}else if(url.contains("fuwu")){     	//����Ƶ��
				tmppath = "html/tea-guoji/ListPage/";
			}else if(url.contains("sppd/spzb")){     	//��ƵƵ��
				tmppath = "html/tea-shipin/ListPage/";
			}else if(url.contains("gdcg")){     	//������
				tmppath = "html/tea-gdcg/ListPage/";
			}else{}
			files = new File(tmppath);
			if(!files.exists()){
				files.mkdirs();
			}	
			path = tmppath+getFilePathByUrl(url);    //��ŵ���ҳ���·��
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
