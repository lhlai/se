package se.preprocess.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import se.preprocess.util.ChineseSpliter;
import se.preprocess.util.StopWordHandler;

/**
 * �ı�Ԥ���������������д��ÿһ���ļ�����,����Ҫд������ǩ��
 * @author pillar
 * @since 2016 3.28
 * @update 2016.6.21
 * @version 2.0
 */
public class TextPreprocess {
	
	public static void main(String[] args){
		preprocess();
	}
	
	private static String txtPath = "txt";  	//�ı�·��
//	private static String PerProcessFile = "PreProcessFile/preprocessfile.txt";  //Ԥ������ı�--�ִʲ�ȥͣ�ô�
//	private static String spiltData = "PreProcessFile/spiltData.txt";  //Ԥ������ı�--ֻ�ִ�
	private static String rawData = "PreProcessFile/rawData.txt";  //Ԥ������ı�--���ִʲ�ȥͣ�ô�
	
	/**
	 * ��ѵ���������ı��ļ�д��һ���ı��ļ��ڣ�һ�б�ʾһ���ı�
	 * @author pillar
	 * @param finalText  �ִʲ�ȥ��ͣ�ô�֮����ı�����
	 * @param filepath   ����Ӹ����ļ���ȡ�ı��������ļ�·��
	 * @return   �Ƿ�д��ɹ�
	 */
	public boolean writeTextToFile(String[] finalText, String filepath){
		String temp = "";
		for(String s:finalText){
			temp+= s;
			temp+= " ";
		}
		BufferedWriter writer = null;
		FileWriter fw = null;
		try{
			File file = new File(filepath);    			//�����ı��ļ�
			if(!file.exists()){                			//�ж��ı��ļ��Ƿ��Ѿ�����
				file.createNewFile();  
			}					
			fw = new FileWriter(file,true);    			//�ǵüӵڶ�������������Ĭ��false���ı�ֻ�ܱ������һ��д������
			writer = new BufferedWriter(fw);
			String str = temp+"\r\n";    //�Զ�����
			writer.write(str); 				   			//д���ļ�
			writer.flush();					   			//�ѻ���������ѹ���ļ� 
			writer.close();					   			//�ر��ļ�
		}catch(IOException ioe){
			ioe.printStackTrace();
			System.out.println("д��ʧ��!");
			return false;
		}
		return true;	
	}
	
	/**
	 * �ѷ�ɢ��txt�ı�д��һ��txt�ļ�s
	 * @param rawText    ĳ��txt�ļ��ı�����
	 * @param filepath   ĳ��txt�ļ�·��
	 * @return
	 */
	public boolean writeRawTextToFile(String rawText, String filepath){
		BufferedWriter writer = null;
		FileWriter fw = null;
		try{
			File file = new File(filepath);    			//�����ı��ļ�
			if(!file.exists()){                			//�ж��ı��ļ��Ƿ��Ѿ�����
				file.createNewFile();  
			}					
			fw = new FileWriter(file,true);    			//�ǵüӵڶ�������������Ĭ��false���ı�ֻ�ܱ������һ��д������
			writer = new BufferedWriter(fw);
			String str = rawText+"\r\n";    //�Զ�����
			writer.write(str); 				   			//д���ļ�
			writer.flush();					   			//�ѻ���������ѹ���ļ� 
			writer.close();					   			//�ر��ļ�
		}catch(IOException ioe){
			ioe.printStackTrace();
			System.out.println("д��ʧ��!");
			return false;
		}
		return true;	
	}
	
	/**
	 * ��ȡѵ����ָ��Ŀ¼��ָ���ı��ļ�������
	 * @param filename  ѵ����ĳ��Ŀ¼�µ�ĳ���ı��ļ�
	 * @return content  �ı�����
	 */
	public String readText(String filename){
		StringBuffer buffer = new StringBuffer();
		FileInputStream in = null;
		InputStreamReader reader = null;    		//����������reader
		BufferedReader br = null;		   			//���ļ�����ת�ɼ�����ܶ��������ԵĶ���
		File file = new File(filename);
		try{
			if(file.exists()){
				in = new FileInputStream(file);
				reader = new InputStreamReader(in);
				br = new BufferedReader(reader);
				String line = br.readLine(); 		//��ʱ����
				while(line!=null){
					buffer.append(line);
					line = br.readLine(); 		   // һ�ζ���һ������
				}
			}else{
				System.out.println("�ļ�:"+filename+"������!");
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}	
		return buffer.toString();	
	}
	/**
	 * ���ķִ�
	 * @author pillar
	 * @param rawText  δ�����ԭʼ�ı�
	 * @return  terms   �������ķִʵ��ı�����(����)
	 */
	private String[] chineseSpilt(String rawText) {
		String[] terms = null;
        terms= ChineseSpliter.split(rawText, " ").split(" "); //���ķִʴ���(�ִʺ������ܻ�������ͣ�ôʣ�
        return terms;
	}
	/**
	 * ȥͣ�ô�
	 * @param spiltText  �Ѿ��ִʺ���ı�����(����)
	 * @return   newWords  ȥ��ͣ�ôʺ���ı�����
	 */
	private String[] DropStopWords(String[] spiltText) {
		Vector<String> v1 = new Vector<String>();
        for(int i=0;i<spiltText.length;++i)
        {
            if(StopWordHandler.IsStopWord(spiltText[i])==false)
            {//����ͣ�ô�
                v1.add(spiltText[i]);
            }
        }
        String[] newWords = new String[v1.size()];
        v1.toArray(newWords);       
		return newWords;
	}
	
	/**
	 * ����txt��·���õ����ļ��ľ���·��
	 * @param txtpath    txt�ļ����ܴ洢·��
	 * @return  String[] txt�ı��ļ�·������
	 */
	private String[] getFilePath(String txtpath){
		File filepath = new File(txtpath);  		//ѵ����
		if (!filepath.isDirectory()) 
        {
            throw new IllegalArgumentException("ѵ�����Ͽ�����ʧ�ܣ� [" +filepath+ "]");
        }else{  	
        	return filepath.list();     //��txt�ļ�·������
        }	
	}
	
	/**
	 * �ı�Ԥ����
	 * @author pillar
	 * @update 2016.6.21
	 */
	public static void preprocess(){
		double begin = System.currentTimeMillis();	
		String rawText = "";    							 //д��ѵ���ı�������
//		String[] spiltText = null; 							 //���ķִʺ��ı�
//		String[] finalText = null;							 //���մ���ѵ�������ı�����
		String[] filepaths = null;		
		TextPreprocess preprocess = new TextPreprocess();		
		try{
			filepaths = preprocess.getFilePath(txtPath);     //�ļ�·������
			for(String filepath:filepaths){					 //�����ļ�
				rawText = preprocess.readText(txtPath+"/"+filepath);     //��ȡѵ�����ı�����
				System.out.println(rawText);
//				spiltText = preprocess.chineseSpilt(rawText);
//				finalText = preprocess.DropStopWords(spiltText);
//				boolean status = preprocess.writeTextToFile(spiltText, spiltData);  //д���ļ�
				boolean status = preprocess.writeRawTextToFile(rawText, rawData);  //д���ļ�
				if(status){
					System.out.println("�ļ�д��ɹ�!");
				}else{
					System.out.println("�ļ�д��ʧ��!");
				}
			}			
		}catch(Exception e){
			e.printStackTrace();
		}			
		System.out.println("����ʱ:"+(System.currentTimeMillis()-begin)/1000+"��");
	}	
}
