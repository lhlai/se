package cn.org.bijia.model;

import java.io.IOException;

public interface Product {
	//tianmao---
	static final String tcom = "&sort=s";           //按综合
	static final String tsales = "&sort=d";         //按销量-降序
	static final String tascendprice = "&sort=p";     //按价格-升序
	static final String tdescendprice = "&sort=pd";   //按价格-降序
	static final String tpopularity = "&sort=rq";   //按人气降序
	static final String tnew = "&sort=new";         //按新品-降序
	//jingdong--
	static final String jcom = "";                  //按综合
	static final String jdescendprice = "&psort=1";    //按价格-降序
	static final String jascendprice = "&psort=2";     //按价格-升序
	static final String jsales = "&psort=3";        //按销量-降序
	static final String criticismNum = "&psort=4";  //按评论数-降序	
	static final String jnew = "&psort=5";         //按新品-降序
	
	
	void setQury(String qury);       //设置搜索词
	String getQury();
	
	void setSortStyle(String sort);  //设置排序类型
	String getSortStyle();
	
	String getMessage()throws Exception;
	
    //保存数据至本地
	void saveToLocal(String result, String keyword, String sortStyle) throws IOException;
}
