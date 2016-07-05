package se.extractor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import se.extractor.page.Page;
import se.extractor.page.Text;

/**
 * ����PageLib�ཫPage,Text�ౣ�浽�ļ���ȥ
 * @author Administrator
 *
 */
public class PageLib {

	/**
	 * @param page
	 * @param filepath ��ȡxml�󱣴��ı��ļ���·��
	 */
	public static void storePage(Page page,String filePath) {
		//�����������ļ������ڶ���storepath
		String storepath = ProperConfig.getPathValue(filePath)+"/"+page.getSummary();		
		if(new File(storepath).exists() == true){
			System.out.println("Message: "+storepath+" is existed!");
			//System.out.println(storepath);
		}
		try{
			//����һ��ʹ��Ĭ�ϴ�С����������Ļ����ַ������
			BufferedWriter writer = new BufferedWriter(new FileWriter(storepath));
			//��һ��дURL
			writer.append(page.getUrl());
			writer.newLine();  //д��һ���зָ����������µ�һ��
			//�ڶ���д����
			writer.append(page.getTitle());
			writer.newLine();
			writer.append(page.getPageType());
			writer.newLine();
			//������Ϊ�÷�
			writer.append(String.valueOf(page.getScore())); //��int���͵�score��ת��ΪString����
			writer.newLine();
			//������Ϊ��ҳ����
			writer.append(page.getContext());
			writer.close(); //�ر������
			
		}
		catch(IOException ioe){
			System.out.println("Error :Processing"+page.getUrl()+"accurs error");
			ioe.printStackTrace();
		}
	}
	
	/**
	 * �־û���ȡ���Ĵ��ı�����
	 * @param text
	 * @param htmlfilename  html�ļ���
	 * @param filepath 		��ȡhtml�󱣴��ı��ļ���·��
	 * @return boolean 		�Ƿ񱣴�ɹ�
	 */
	public static boolean storeTextFromHtml(Text text,String htmlfilename,String filePath) {
		String filepath = filePath+"/"+htmlfilename.substring(0,htmlfilename.lastIndexOf("htm"))+"txt";
		if(new File(filepath).exists() == true){
			System.out.println("Message: "+filepath+" is existed!");
			return false;
		}else{
			try{
				//����һ��ʹ��Ĭ�ϴ�С����������Ļ����ַ������
				BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));			
				writer.append(text.getContext());//д���ı�����
				writer.close(); 	//�ر������
				return true;
			}catch(IOException ioe){
				System.out.println("Error :Processing"+htmlfilename+"accurs error");
				ioe.printStackTrace();
				return false;
			}
		}		
	}
	
	/**
	 * �־û���ȡ���Ĵ��ı�����
	 * @param text
	 * @param xmlfilename   xml�ļ���
	 * @param filepath 		��ȡxml�󱣴��ı��ļ���·��
	 * @return boolean 		�Ƿ񱣴�ɹ�
	 */
	public static boolean storeText(Text text,String xmlfilename,String filePath) {
		String filepath = filePath+"/"+xmlfilename.substring(0,xmlfilename.lastIndexOf("xml"))+".txt";
		if(new File(filepath).exists() == true){
			System.out.println("Message: "+filepath+" is existed!");
			return false;
		}else{
			try{
				//����һ��ʹ��Ĭ�ϴ�С����������Ļ����ַ������
				BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));			
				writer.append(text.getContext());//д���ı�����
				writer.close(); 	//�ر������
				return true;
			}catch(IOException ioe){
				System.out.println("Error :Processing"+xmlfilename+"accurs error");
				ioe.printStackTrace();
				return false;
			}
		}		
	}
}
