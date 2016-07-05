package cn.org.bijia.main;

import cn.org.bijia.model.Product;
import cn.org.bijia.shops.JingDongProduct;
import cn.org.bijia.shops.TianMaoProduct;
/**
 * @author pillar
 * @since  2015.12
 */
public class PriceCompare {

	public static void main(String[] args) throws Exception {
		double begin = System.currentTimeMillis();
		String keyword = "文胸 聚拢";           //定义搜索的商品名称
		String tsortStyle = Product.tsales;      //定义天猫排序方式
		String jsortStyle = Product.jsales;      //定义京东排序方式
		
		Product t = new TianMaoProduct();	
		t.setSortStyle(tsortStyle);  
		t.setQury(keyword);
		String tresult = t.getMessage();		
		t.saveToLocal(tresult,keyword ,tsortStyle);  

     	Product j = new JingDongProduct();		
		j.setSortStyle(jsortStyle);  
		j.setQury(keyword);
		String jresult = j.getMessage();
		j.saveToLocal(jresult, keyword, jsortStyle);
		
        double timeConsume = System.currentTimeMillis() - begin; 
	    System.out.println("耗时：" + timeConsume/1000 + "秒");
	}
}
