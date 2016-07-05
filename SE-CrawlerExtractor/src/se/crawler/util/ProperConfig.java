package se.crawler.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 定义一个类用于保存存储路径
 * @author Administrator
 *
 */
public class ProperConfig {

	private static String CHARSET_FILE = "charset";
	private static String TOPIC_FILE = "topic";
	private static ResourceBundle charset_bundle;
	private static ResourceBundle topic_bundle;
	static {
		try{
			charset_bundle = ResourceBundle.getBundle(CHARSET_FILE); //取得CONFIG_FILE的Bundle
			topic_bundle = ResourceBundle.getBundle(TOPIC_FILE);
		}catch(MissingResourceException mre){
			System.out.println("Cannot Find config file"+CHARSET_FILE+".properties.");
		}
	}
	public static String getValue(String key) {
		return charset_bundle.getString(key);  //从此资源包或它的某个父包中获取给定键的字符串
	}
	public static String getTopic(String key) {
		return topic_bundle.getString(key);    //从此资源包或它的某个父包中获取给定键的字符串
	}
	public static double getWeight(String key) {
		double weight = 0.0;
		String doublestr = topic_bundle.getString(key);  //从此资源包或它的某个父包中获取给定键的字符串
		weight = Double.valueOf(doublestr);
		return weight;  
	}
	public static int getNum(String key) {
		int topicNum = 0;
		String doublestr = topic_bundle.getString(key);  //从此资源包或它的某个父包中获取给定键的字符串
		topicNum = Integer.valueOf(doublestr);
		return topicNum;  
	}
}
