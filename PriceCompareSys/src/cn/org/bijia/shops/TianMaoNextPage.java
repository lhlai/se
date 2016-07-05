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
 * 爬取非首页数据
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
//        //可以分行写入
//        writer.write(result);
//        writer.close();  

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
		
		for(int i=0;i<products.size();i++){
			String product = products.get(i).siblingElements().text();
			String price = prices.get(i).siblingElements().text();		
			String shop = shops.get(i).siblingElements().text();
			String statu = status.get(i).siblingElements().text();
			System.out.println("第"+(i+1)+"件商品:");
			System.out.println("商品:"+product);
			System.out.println("价格:"+price+"元");
			System.out.println("商家:"+shop);
			System.out.println("月成交额:"+statu);
			System.out.println();
		}

    }
	public static void main(String[] args) throws Exception {
		double begin = System.currentTimeMillis();
		TianMaoNextPage t = new TianMaoNextPage();
		t.setQury("新百伦");
		t.getMessage();
		double timeConsume = System.currentTimeMillis() - begin; 
	    System.out.println("耗时：" + timeConsume/1000 + "秒");

	}

}
