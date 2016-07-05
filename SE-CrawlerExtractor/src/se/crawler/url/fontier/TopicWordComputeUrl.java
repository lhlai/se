package se.crawler.url.fontier;

import se.crawler.util.ProperConfig;
/**
 * 计算网页内容与主题相关性，如果相关性得分大则先处理，相关性得分小则丢弃
 * @author Administrator
 */
public class TopicWordComputeUrl implements ComputeUrl {
	private static final double A1 = 0.30;
//	private static final double A2 = 0.15;
//	private static final double A3 = 0.15;
	private static final double A4 = 0.70;
	private String topic1 = ProperConfig.getTopic("topic1");
	private String topic2 = ProperConfig.getTopic("topic2");
	private String topic3 = ProperConfig.getTopic("topic3");
	private String topic4 = ProperConfig.getTopic("topic4");
	private String topic5 = ProperConfig.getTopic("topic5");
	private String topic6 = ProperConfig.getTopic("topic6");
	private String topic7 = ProperConfig.getTopic("topic7");
	private String topic8 = ProperConfig.getTopic("topic8");
	private double similartity = 0;
	@Override
	public boolean accept(String url, String pageContent){
		if(priority(url,pageContent)>0.50&&similartity>12){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 网页评分
	 */
	public double priority(String url,String pageContent){
		double priority = 0;
		double domainWeight = getDomain_weight(url);
		similartity = getSimilartity(pageContent);
		double sum = domainWeight+similartity;
		priority = A1*(domainWeight/sum)+A4*(similartity/sum);
		return priority;
	}
	//根据URL域名计算得分
	private int getDomain_weight(String url) {
		int score = 10;
		String tmp = url.replace("http://", "");
		score = score-(tmp.split("/").length-1);		
		return score;
	}
	//该URL网页被其他网页引用的次数
//	private double getLink_popularity(String url) {
//		return 0;
//	}
	//双亲URL的评分 
//	private double getParent_priority(String url) {
//		
//	    return 0;
//	}
	/**
	 * 网页主题相似度计算
	 */
	public double getSimilartity(String pageContent) {
		double Sim = 0;
		int topicNum = ProperConfig.getNum("topic.num");
		Sim = (getTopic1Count(pageContent)*ProperConfig.getWeight("topic1.weight")+
				getTopic2Count(pageContent)*ProperConfig.getWeight("topic2.weight")+
				getTopic3Count(pageContent)*ProperConfig.getWeight("topic3.weight")+
				getTopic4Count(pageContent)*ProperConfig.getWeight("topic4.weight")+
				getTopic5Count(pageContent)*ProperConfig.getWeight("topic5.weight")+
				getTopic6Count(pageContent)*ProperConfig.getWeight("topic6.weight")+
				getTopic7Count(pageContent)*ProperConfig.getWeight("topic7.weight")+
				getTopic8Count(pageContent)*ProperConfig.getWeight("topic8.weight"))/topicNum;
		return Sim;
	}
	private int getTopic1Count(String pageContent) {
		int num = 0;
		num = pageContent.split(topic1).length-1;
//		System.out.println("农业出现的个数为:"+num);
		return num;
	}
	private int getTopic2Count(String pageContent) {
		int num = 0;
		num = pageContent.split(topic2).length-1;
//		Pattern p = Pattern.compile("新浪");
//		Matcher m = p.matcher(pageContent);
//		while(m.find()){
//			num++;
//		}
//		System.out.println(num);
		return num;
	}
	private int getTopic3Count(String pageContent) {
		int num = 0;
		num = pageContent.split(topic3).length-1;
		return num;
	}
	private int getTopic4Count(String pageContent) {
		int num = 0;
		num = pageContent.split(topic4).length-1;
		return num;
	}
	private int getTopic5Count(String pageContent) {
		int num = 0;
		num = pageContent.split(topic5).length-1;
		return num;
	}
	private int getTopic6Count(String pageContent) {
		int num = 0;
		num = pageContent.split(topic6).length-1;
		return num;
	}
	private int getTopic7Count(String pageContent) {
		int num = 0;
		num = pageContent.split(topic7).length-1;
		return num;
	}
    private int getTopic8Count(String pageContent) {
    	int num = 0;
		num = pageContent.split(topic8).length-1;
		return num;
	}
}
