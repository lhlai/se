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
 * 京东搜索商品首页数据
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
//		String strurl = "http://search.jd.com/Search?keyword=新百伦&enc=utf-8&pvid=oy1a1hii.kd9cf";
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

	    //jsoup获取数据
		//价格
		Document doc = Jsoup.parse(result);
		Elements e1 = doc.select("[class=p-price]");
		List<Element> prices = e1.select("i");			
		//商品名称
		Elements e2 = doc.select("[class=p-name p-name-type-2]");
		List<Element> products = e2.select("em");	
		
		//商店
		//月成交额
		
		StringBuffer buffer = new StringBuffer();
		System.out.println("******************************************京东商城检索结果：");
		for(int i=0;i<products.size();i++){
			buffer.append("第"+(i+1)+"件商品:"+"\r\n");
			String product = products.get(i).siblingElements().text();
			buffer.append("商品:"+product+"\r\n");
			String price = prices.get(i).siblingElements().text().substring(1);		
			buffer.append("价格:"+price+"元"+"\r\n");
			buffer.append("\r\n");
			
			System.out.println("第"+(i+1)+"件商品:");
			System.out.println("商品:"+product);
			System.out.println("价格:"+price+"元");
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
		String keyword = "新百伦";
		String sortStyle = jascendprice;   //按价格升序
		JingDongProduct j = new JingDongProduct();
		j.setSortStyle(sortStyle);  
		j.setQury(keyword);
		String result = j.getMessage();
		j.saveToLocal(result, keyword, sortStyle);	
		double timeConsume = System.currentTimeMillis() - begin; 
	    System.out.println("耗时：" + timeConsume/1000 + "秒");
	}
}
