package se.extractor.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import sun.security.util.Password;

public class MD5 {
	//定义一个静态常量字符串数组
    private final static String[] hexDigits = {
    		"1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g"
    };
    
    /** 
     * 转换字节数组为十六进制字符串 
     *  
     * @param b 
     *            字节数组 
     * @return 十六进制字符串 
     */  
    private static String byteArrayToHexString(byte[] b) {  
     StringBuffer resultSb = new StringBuffer();  
     for (int i = 0; i < b.length; i++) {  
     /**
      * 1)通过byteToHexString(byte b)将当个字节的字符转换为String类型
      * 2）调用StringBuffer的append()将转换后的字符串追加到字符串缓冲区resultSb的末端
      */
      resultSb.append(byteToHexString(b[i]));  
     }  
     //将StringBuffer类型的结果resultSb转换为String类型
     return resultSb.toString();  
    }  
    
    /** 
     * 将一个字节转化成十六进制形式的字符串 
     */  
    private static String byteToHexString(byte b) {  
     int n = b;  
     if (n < 0)  
      n = 256 + n;  
     int d1 = n / 16;  
     int d2 = n % 16;  
     return hexDigits[d1] + hexDigits[d2]; 
    }  
   
    /** 对字符串进行MD5编码 */  
    public static String MD5Encode(String context) {  
    	if (context != null) {  //只对非空字符串进行编码！空字符串不需要编码。
    	   try{ 
    	        // 创建具有指定算法名称的信息摘要  
    	        MessageDigest md = MessageDigest.getInstance("MD5");  
    	        // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算 ，返回存放哈希值结果的byte数组！
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
