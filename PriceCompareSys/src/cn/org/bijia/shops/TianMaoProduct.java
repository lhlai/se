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
 * 天猫搜索商品首页数据
 * @author Administrator
 *
 */
public class TianMaoProduct implements Product{
	private String qury = null;
	private String sort = "";

	@Override
	public void setQury(String qury) {
		this.qury = qury;
	}
	@Override
	public String getQury(){
		return qury;
	}
	@Override
	public void setSortStyle(String sort){
		this.sort  = sort;
	}
	@Override
	public String getSortStyle(){
		return sort;
	}
	@Override
	public String getMessage()throws Exception {	
		String result = null;
		try{	
			HttpClientBuilder builder = HttpClients.custom();  
	        builder.setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");  
	        CloseableHttpClient httpClient = builder.build(); 
	        
//		    URL url = new URL(strurl);      
//		    URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		    URI uri = new URI("https","list.tmall.com","/search_product.htm",
		    		"q="+getQury()+getSortStyle(),null);
		    HttpGet httpget = new HttpGet(uri);
			httpget.addHeader("Referer","https://www.tmall.com/?spm=a220m.1000858.a2226mz.1.RzPkM0");
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

	    //jsoup获取数据
		//价格
		Document doc = Jsoup.parse(result);
		Elements e1 = doc.select("[class=productPrice]");
		List<Element> prices = e1.select("em");			
		//商品名称
		Elements e2 = doc.select("[class=productTitle]");
		List<Element> products = e2.select("a");	
		//商店
		Elements e3 = doc.select("[class=productShop]");
		List<Element> shops = e3.select("a");
		//月成交额
		Elements e4 = doc.select("[class=productStatus]");
		List<Element> status = e4.select("em");
		StringBuffer buffer = new StringBuffer();
		System.out.println("******************************************天猫商城检索结果：");
		for(int i=0;i<products.size();i++){
			buffer.append("第"+(i+1)+"件商品:"+"\r\n");
			String product = products.get(i).siblingElements().text();
			buffer.append("商品:"+product+"\r\n");
			String price = prices.get(i).siblingElements().text().substring(1);		
			buffer.append("价格:"+price+"元"+"\r\n");
			String shop = shops.get(i).siblingElements().text();
			buffer.append("商家:"+shop+"\r\n");
			String statu = status.get(i).siblingElements().text();
			buffer.append("月成交额:"+statu+"\r\n");
			buffer.append("\r\n");
			System.out.println("第"+(i+1)+"件商品:");
			System.out.println("商品:"+product);
			System.out.println("价格:"+price+"元");
			System.out.println("商家:"+shop);
			System.out.println("月成交额:"+statu);
			System.out.println();
		}
		return buffer.toString();
    }
	
	@Override
	public void saveToLocal(String result,String keyword,String sortStyle) throws IOException{
		 if(sortStyle.contains("pd")){
			 sortStyle = "descend";
		 }else if(sortStyle.contains("p")){
			 sortStyle = "ascend";
		 }else if(sortStyle.contains("sort=d")){
			 sortStyle = "sale";
		 }else if(sortStyle.contains("sort=rq")){
			 sortStyle = "popularity";
		 }else if(sortStyle.contains("new")){
			 sortStyle = "new";
		 }else if(sortStyle.contains("sort=s")){
			 sortStyle = "com";
		 }else {
			 sortStyle = "qita";
		 }       
		 
		 Writer writer = new BufferedWriter(new OutputStreamWriter
	               (new FileOutputStream("tmp\\"+keyword+"-"+sortStyle+"-"+"tianmao.txt"), "gbk"));
         //可以分行写入
         writer.write(result);
         writer.close();  
	}
	
	public static void main(String[] args) throws Exception{
		double begin = System.currentTimeMillis();
		String keyword = "新百�?";
		String sortStyle = tascendprice;  //按价格升�?
		TianMaoProduct t = new TianMaoProduct();
		t.setSortStyle(sortStyle);  
		t.setQury(keyword);
		String result = t.getMessage();		
		t.saveToLocal(result,keyword ,sortStyle);
		
		double timeConsume = System.currentTimeMillis() - begin; 
	    System.out.println("耗时�?" + timeConsume/1000 + "�?");
	}
}
