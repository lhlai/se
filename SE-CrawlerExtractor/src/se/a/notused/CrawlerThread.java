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
 * ��ȡ��ҳ���߳�
 * @author pillar
 * @version 1.0
 */
public class CrawlerThread implements Runnable {
	private DownLoadPage crawler = null;
	public static final Object signal = new Object();
	private ComputeUrl contentfilter = new TopicWordComputeUrl();  //����URL������1
	private LinkFilter filter = new AgriKejiLinkFilter();             //����URL������2
	private int count = 0;        //�ȴ����߳�
	private int crawDepth  = 10;   //�������
    private int downNum = 0;      //������ҳ������
    
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
					int d = LinkQueue.geturldepth(tmpurl);      //��ȡ���
					content = crawler.downloadFile(tmpurl,d);
				    if(content!=null){
//				    	System.out.println("content:"+content);				    	
						System.out.println("����ҳ"+tmpurl+"�ɹ������Ϊ"+d+" �����߳�"+Thread.currentThread().getName()+"����");
						LinkQueue.addVisitedUrl(tmpurl);	        //���� url ���뵽�ѷ��ʵ� URL��
					    String charset = crawler.getEncoding();     //��ȡ��������ҳ�ı����ַ���  
					    if(d<crawDepth){   //�޶���ȡ���
				            Set<String> links = HtmlParserTool.extractLinks(content,filter,charset); //��ȡ����ҳ������	
				            System.out.println("links:"+links.size());
				            for(String link:links){
				            	System.out.println("content:"+123);
				            	System.out.println(link);
					            LinkQueue.addUnvisitedUrl(link,d+1); 	//����ȡ��δ���ʵ�URL���
				            }					            
				        }
					    saveToLocal(content,tmpurl);     //ͨ����������Է����ı���������						    
					    if(count>0){    
						    synchronized(signal){								    	
							    count--;
							    signal.notify();  
				            }
			            }
					    
//					    if(contentfilter.accept(tmpurl, content)){  //�������ӷ������������������
//						    saveToLocal(content,tmpurl);     //ͨ����������Է����ı���������						    
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
		    	synchronized(signal){  //signalΪͬ��������
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
	 * @param content	��ȡ����ҳԴ����
	 * @param url		��ҳurl��ַ
	 */
	private void saveToLocal(String content, String url) {
		String encode = crawler.getEncoding();
		String path = "html/kj/"+getFilePathByUrl(url);     //�ļ���·��
		String result = "<!--url:"+url+"-->"+"\r\n"+content;   //��url��ַ����html�ļ�����
		try {
			byte[] date = result.getBytes(encode);  
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					new File(path)));
			for (int i = 0; i < date.length; i++){
				out.write(date[i]);
			}
			out.flush();   			//��ջ�����
			out.close();   
			synchronized(signal){  
		    	downNum++;      	//����ɹ�����������ҳ����һ
		    }
		} catch (IOException e) {
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
	
    public static void main(String[] args){
    	LinkQueue.addUnvisitedUrl("http://www.agri.cn/kj/",1);
    	CrawlerThread crawler = new CrawlerThread();
    	crawler.run();
//    	for(int i=0;i<10;i++){  
//			new Thread(crawler,"thread-"+i).start();
//		}
    }
}
