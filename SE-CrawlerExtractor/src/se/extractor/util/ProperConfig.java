package se.extractor.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ����һ�������ڱ����ļ��洢·��
 * @author Administrator
 *
 */
public class ProperConfig {

	private static String CONFIG_FILE = "config";  
	private static ResourceBundle bundle; 
	
	static {
		try{
			//ʹ��ָ���Ļ������ơ�Ĭ�ϵ����Ի����͵����ߵ����������ȡ��Դ��
			bundle = ResourceBundle.getBundle(CONFIG_FILE); //ȡ��CONFIG_FILE��Bundle
		}catch(MissingResourceException mre){
			System.out.println("Cannot Find config file"+CONFIG_FILE+".properties");
		}
	}
	
	public static String getPathValue(String key) {
		// TODO Auto-generated method stub
		return bundle.getString(key);  //�Ӵ���Դ��������ĳ�������л�ȡ���������ַ��������ô˷�����ͬ�ڵ��� 
		                               //(String) getObject(key)�� 
	}
}
