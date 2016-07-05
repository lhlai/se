package cn.org.bijia.shops;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

import cn.org.bijia.model.Product;
/**
 * ����������Ʒ��ҳ����
 * @author Administrator
 *
 */
public class JingDongProduct implements Product{
	private String qury = null;
	private String sort = "";
	
	@Override
	public void setQury(String qury) {
		this.qury  = qury;
	}
	@Override
	public String getQury(){
		return qury;
	}
	@Override
	public void setSortStyle(String sort) {
		this.sort = sort;
	}
	@Override
	public String getSortStyle() {
		return sort;
	}
	
	@Override
	public String getMessage() throws Exception {
//		String strurl = "http://search.jd.com/Search?keyword=�°���&enc=utf-8&pvid=oy1a1hii.kd9cf";
		String result = null;
		try{	
			HttpClientBuilder builder = HttpClients.custom();  
	        builder.setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");  
	        CloseableHttpClient httpClient = builder.build(); 
//	        URL url = new URL(strurl);     
//		    URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		    URI uri = new URI("http","search.jd.com","/Search",
		    		"keyword="+getQury()+getSortStyle()+"&enc=utf-8&pvid=oy1a1hii.kd9cf",null);
		    HttpGet httpget = new HttpGet(uri);
			httpget.addHeader("Referer","http://www.jd.com/");
			CloseableHttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null){		
			    result = EntityUtils.toString(entity,"utf-8");
			    EntityUtils.consume(entity);
			}
			response.close();
			httpClient.close();
		}catch(ClientProtocolException cpe){
			cpe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}

	    //jsoup��ȡ����
		//�۸�
		Document doc = Jsoup.parse(result);
		Elements e1 = doc.select("[class=p-price]");
		List<Element> prices = e1.select("i");			
		//��Ʒ����
		Elements e2 = doc.select("[class=p-name p-name-type-2]");
		List<Element> products = e2.select("em");	
		
		//�̵�
		//�³ɽ���
		
		StringBuffer buffer = new StringBuffer();
		System.out.println("******************************************�����̳Ǽ��������");
		for(int i=0;i<products.size();i++){
			buffer.append("��"+(i+1)+"����Ʒ:"+"\r\n");
			String product = products.get(i).siblingElements().text();
			buffer.append("��Ʒ:"+product+"\r\n");
			String price = prices.get(i).siblingElements().text().substring(1);		
			buffer.append("�۸�:"+price+"Ԫ"+"\r\n");
			buffer.append("\r\n");
			
			System.out.println("��"+(i+1)+"����Ʒ:");
			System.out.println("��Ʒ:"+product);
			System.out.println("�۸�:"+price+"Ԫ");
			System.out.println();
		}
		return buffer.toString();					
	}
	
	@Override
	public void saveToLocal(String result,String keyword, String sortStyle) throws IOException {
		if(sortStyle.contains("1")){
			sortStyle = "descend";
		}else if(sortStyle.contains("2")){
			sortStyle = "ascend";
		}else if(sortStyle.contains("3")){
			sortStyle = "sale";
		}else if(sortStyle.contains("4")){
			sortStyle = "criticism";
		}else if(sortStyle.contains("5")){
			sortStyle = "new";
		}else{
			sortStyle = "com";
		}
		Writer writer = new BufferedWriter(new OutputStreamWriter
	               (new FileOutputStream("tmp\\"+keyword+"-"+sortStyle+"-"+"jindong.txt"),"gbk"));
        writer.write(result);
        writer.close(); 
	}
	
	public static void main(String[] args) throws Exception{
		double begin = System.currentTimeMillis();	
		String keyword = "�°���";
		String sortStyle = jascendprice;   //���۸�����
		JingDongProduct j = new JingDongProduct();
		j.setSortStyle(sortStyle);  
		j.setQury(keyword);
		String result = j.getMessage();
		j.saveToLocal(result, keyword, sortStyle);	
		double timeConsume = System.currentTimeMillis() - begin; 
	    System.out.println("��ʱ��" + timeConsume/1000 + "��");
	}
}
