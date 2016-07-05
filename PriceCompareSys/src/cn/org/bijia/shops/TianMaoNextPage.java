package cn.org.bijia.shops;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * ��ȡ����ҳ����
 * @author Administrator
 */
public class TianMaoNextPage {
	private String qury = null;
	public void setQury(String qury) {
		this.qury = qury;
	}

	public String getQury(){
		return qury;
	}
	public void getMessage()throws Exception {
		String result = null;
		try{	
			HttpClientBuilder builder = HttpClients.custom();  
	        builder.setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");  
	        CloseableHttpClient httpClient = builder.build(); 
//		    URL url = new URL(strurl);      
//		    URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		    URI uri = new URI("https","list.tmall.com","/search_product.htm",
		    		"q="+getQury()+"&s=120"+"&#J_Filter",null);
		    HttpGet httpget = new HttpGet(uri);
			httpget.addHeader("Referer","https://list.tmall.com/search_product.htm?q=%D0%C2%B0%D9%C2%D7&type=p&cat=all");
			CloseableHttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null){				
			    result = EntityUtils.toString(entity,"gbk");
			    EntityUtils.consume(entity);
			}
			response.close();
			httpClient.close();
		}catch(ClientProtocolException cpe){
			cpe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
//	    Writer writer = new BufferedWriter(new OutputStreamWriter
//	               (new FileOutputStream("tmp\\5636.html"), "gbk"));
//        //���Է���д��
//        writer.write(result);
//        writer.close();  

	    //jsoup��ȡ����
		//�۸�
		Document doc = Jsoup.parse(result);
		Elements e1 = doc.select("[class=productPrice]");
		List<Element> prices = e1.select("em");			
		//��Ʒ����
		Elements e2 = doc.select("[class=productTitle]");
		List<Element> products = e2.select("a");	
		//�̵�
		Elements e3 = doc.select("[class=productShop]");
		List<Element> shops = e3.select("a");
		//�³ɽ���
		Elements e4 = doc.select("[class=productStatus]");
		List<Element> status = e4.select("em");
		
		for(int i=0;i<products.size();i++){
			String product = products.get(i).siblingElements().text();
			String price = prices.get(i).siblingElements().text();		
			String shop = shops.get(i).siblingElements().text();
			String statu = status.get(i).siblingElements().text();
			System.out.println("��"+(i+1)+"����Ʒ:");
			System.out.println("��Ʒ:"+product);
			System.out.println("�۸�:"+price+"Ԫ");
			System.out.println("�̼�:"+shop);
			System.out.println("�³ɽ���:"+statu);
			System.out.println();
		}

    }
	public static void main(String[] args) throws Exception {
		double begin = System.currentTimeMillis();
		TianMaoNextPage t = new TianMaoNextPage();
		t.setQury("�°���");
		t.getMessage();
		double timeConsume = System.currentTimeMillis() - begin; 
	    System.out.println("��ʱ��" + timeConsume/1000 + "��");

	}

}
