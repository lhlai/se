package se.extractor.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 定义一个类用于保存文件存储路径
 * @author Administrator
 *
 */
public class ProperConfig {

	private static String CONFIG_FILE = "config";  
	private static ResourceBundle bundle; 
	
	static {
		try{
			//使用指定的基本名称、默认的语言环境和调用者的类加载器获取资源包
			bundle = ResourceBundle.getBundle(CONFIG_FILE); //取得CONFIG_FILE的Bundle
		}catch(MissingResourceException mre){
			System.out.println("Cannot Find config file"+CONFIG_FILE+".properties");
		}
	}
	
	public static String getPathValue(String key) {
		// TODO Auto-generated method stub
		return bundle.getString(key);  //从此资源包或它的某个父包中获取给定键的字符串，调用此方法等同于调用 
		                               //(String) getObject(key)。 
	}
}
