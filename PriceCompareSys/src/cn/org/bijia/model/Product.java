package cn.org.bijia.model;

import java.io.IOException;

public interface Product {
	//tianmao---
	static final String tcom = "&sort=s";           //���ۺ�
	static final String tsales = "&sort=d";         //������-����
	static final String tascendprice = "&sort=p";     //���۸�-����
	static final String tdescendprice = "&sort=pd";   //���۸�-����
	static final String tpopularity = "&sort=rq";   //����������
	static final String tnew = "&sort=new";         //����Ʒ-����
	//jingdong--
	static final String jcom = "";                  //���ۺ�
	static final String jdescendprice = "&psort=1";    //���۸�-����
	static final String jascendprice = "&psort=2";     //���۸�-����
	static final String jsales = "&psort=3";        //������-����
	static final String criticismNum = "&psort=4";  //��������-����	
	static final String jnew = "&psort=5";         //����Ʒ-����
	
	
	void setQury(String qury);       //����������
	String getQury();
	
	void setSortStyle(String sort);  //������������
	String getSortStyle();
	
	String getMessage()throws Exception;
	
    //��������������
	void saveToLocal(String result, String keyword, String sortStyle) throws IOException;
}
