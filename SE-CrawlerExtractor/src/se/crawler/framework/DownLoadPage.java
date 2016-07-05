package se.crawler.framework;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import se.crawler.url.queue.LinkQueue;
import se.crawler.util.ProperConfig;

public class DownLoadPage {
	private String contentType = null;
	private String encode = null;
	
	/* 下载 url 指向的网页 */
	public String downloadFile(String strurl,int deep) throws URISyntaxException{	
		String result = null;
		try{	
			HttpClientBuilder builder = HttpClients.custom();  
	        builder.setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");  //设置代理
	        CloseableHttpClient httpclient = builder.build(); 
//		    CloseableHttpClient httpclient = HttpClients.createDefault();    
		    // 设置 Http 连接超时 5s
		    URL url = new URL(strurl);   
		    URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		    HttpGet httpget = new HttpGet(uri);
			HttpContext HTTP_CONTEXT = new BasicHttpContext();
			CloseableHttpResponse response = httpclient.execute(httpget,HTTP_CONTEXT);
			StatusLine statusLine = response.getStatusLine();
			
			int statusCode = statusLine.getStatusCode();
			if(statusCode>=HttpStatus.SC_MULTIPLE_CHOICES){
				System.out.println("UnExpection statusCode: "+statusCode+" url:"+strurl);
				if(statusCode==404){
					
				}else{
					LinkQueue.addUnvisitedUrl(strurl,deep);   //返回码错误的url扔回队列继续抓取
				}
//				throw new HttpResponseException(
//						statusCode,
//						statusLine.getReasonPhrase());
			}else{
				HttpEntity entity = response.getEntity();
				if (entity == null){
				    throw new ClientProtocolException("Response contains no content");
			    }else {
			    	setEncoding(strurl);
                    result = EntityUtils.toString(entity,getEncoding()); 	
                    this.contentType = entity.getContentType().getValue();
			     }
			}
			httpclient.close();
			httpget.releaseConnection();
		}catch(ClientProtocolException cpe){
			cpe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public String getEncoding() {
		return encode;
	}
	
	public void setEncoding(String url) {
		if(url.contains("agri")&&!url.contains("gdagri")&&!url.contains("12316")){
            encode  = ProperConfig.getValue("agri.charset");
        }else if(url.contains("gdagri")){
         	encode = ProperConfig.getValue("gdagri.charset");
        }else if(url.contains("12316.agri")){
         	encode = ProperConfig.getValue("12316.charset");
        }else if(url.contains("sina")){
         	encode = ProperConfig.getValue("sina.charset");
        }else if(url.contains("163.com")){
         	encode = ProperConfig.getValue("163.charset");
        }else if(url.contains("qq.com")){
         	encode = ProperConfig.getValue("qq.charset");
        }else if(url.contains("scau")){
         	encode = ProperConfig.getValue("scau.charset");
        }else{
         	encode = "utf-8";
        }
	}
    public String getContentType(){
    	return contentType;
    }
}
