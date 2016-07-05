package se.extractor.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import sun.security.util.Password;

public class MD5 {
	//����һ����̬�����ַ�������
    private final static String[] hexDigits = {
    		"1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g"
    };
    
    /** 
     * ת���ֽ�����Ϊʮ�������ַ��� 
     *  
     * @param b 
     *            �ֽ����� 
     * @return ʮ�������ַ��� 
     */  
    private static String byteArrayToHexString(byte[] b) {  
     StringBuffer resultSb = new StringBuffer();  
     for (int i = 0; i < b.length; i++) {  
     /**
      * 1)ͨ��byteToHexString(byte b)�������ֽڵ��ַ�ת��ΪString����
      * 2������StringBuffer��append()��ת������ַ���׷�ӵ��ַ���������resultSb��ĩ��
      */
      resultSb.append(byteToHexString(b[i]));  
     }  
     //��StringBuffer���͵Ľ��resultSbת��ΪString����
     return resultSb.toString();  
    }  
    
    /** 
     * ��һ���ֽ�ת����ʮ��������ʽ���ַ��� 
     */  
    private static String byteToHexString(byte b) {  
     int n = b;  
     if (n < 0)  
      n = 256 + n;  
     int d1 = n / 16;  
     int d2 = n % 16;  
     return hexDigits[d1] + hexDigits[d2]; 
    }  
   
    /** ���ַ�������MD5���� */  
    public static String MD5Encode(String context) {  
    	if (context != null) {  //ֻ�Էǿ��ַ������б��룡���ַ�������Ҫ���롣
    	   try{ 
    	        // ��������ָ���㷨���Ƶ���ϢժҪ  
    	        MessageDigest md = MessageDigest.getInstance("MD5");  
    	        // ʹ��ָ�����ֽ������ժҪ���������£�Ȼ�����ժҪ���� �����ش�Ź�ϣֵ�����byte���飡
    	        byte[] results = md.digest(context.getBytes());
    	        String resultString = byteArrayToHexString(results);  
    	        return resultString.toUpperCase();
    	   }catch(NoSuchAlgorithmException nsae){
    	    	nsae.printStackTrace();
    	   }
    	}
		return null;
    }
}
