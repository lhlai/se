package cn.org.bijia.test;

import cn.org.bijia.model.Product;
import cn.org.bijia.shops.JingDongProduct;
import cn.org.bijia.shops.TianMaoProduct;
/**
 * �ȼ�ϵͳ������
 * @author Administrator
 */
public class PriceCompareTest {

	public static void main(String[] args) throws Exception {
		double begin = System.currentTimeMillis();
		
		String keyword = "�ڴ��� ����";
		String tsortStyle = Product.tsales;    
		String jsortStyle = Product.jsales;    
		
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
	    System.out.println("��ʱ��" + timeConsume/1000 + "��");
	}
}
