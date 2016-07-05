package se.preprocess.util;

import java.io.IOException;

import jeasy.analysis.MMAnalyzer;

/**
 * ���ķִʹ�����
 * @author pillar
 * @createDate 2016 3.28
 * @version 1.0
 *
 */
public class ChineseSpliter {
	
   /**
	* �Ը������ı��������ķִ�
	* @param text �������ı�
	* @param splitToken ���ڷָ�ı��,��"|"
	* @return �ִ���ϵ��ı�
	*/
	public static String split(String text,String splitToken){
		String result = null;
        MMAnalyzer analyzer = new MMAnalyzer();      
        try {
            result = analyzer.segment(text, splitToken);    
        }      
        catch (IOException e) {     
            e.printStackTrace();     
        }     
        return result;
	}

}
