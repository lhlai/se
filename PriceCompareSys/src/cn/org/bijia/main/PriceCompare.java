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
		String keyword = "���� ��£";           //������������Ʒ����
		String tsortStyle = Product.tsales;      //������è����ʽ
		String jsortStyle = Product.jsales;      //���復������ʽ
		
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
