package se.extractor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import se.extractor.page.Page;
import se.extractor.page.Text;

/**
 * 定义PageLib类将Page,Text类保存到文件中去
 * @author Administrator
 *
 */
public class PageLib {

	/**
	 * @param page
	 * @param filepath 抽取xml后保存文本文件的路径
	 */
	public static void storePage(Page page,String filePath) {
		//保存解析后的文件保存在对象storepath
		String storepath = ProperConfig.getPathValue(filePath)+"/"+page.getSummary();		
		if(new File(storepath).exists() == true){
			System.out.println("Message: "+storepath+" is existed!");
			//System.out.println(storepath);
		}
		try{
			//创建一个使用默认大小输出缓冲区的缓冲字符输出流
			BufferedWriter writer = new BufferedWriter(new FileWriter(storepath));
			//第一行写URL
			writer.append(page.getUrl());
			writer.newLine();  //写入一个行分隔符。另起新的一行
			//第二行写标题
			writer.append(page.getTitle());
			writer.newLine();
			writer.append(page.getPageType());
			writer.newLine();
			//第三行为得分
			writer.append(String.valueOf(page.getScore())); //将int类型的score先转换为String类型
			writer.newLine();
			//第四行为网页内容
			writer.append(page.getContext());
			writer.close(); //关闭输出流
			
		}
		catch(IOException ioe){
			System.out.println("Error :Processing"+page.getUrl()+"accurs error");
			ioe.printStackTrace();
		}
	}
	
	/**
	 * 持久化抽取出的纯文本数据
	 * @param text
	 * @param htmlfilename  html文件名
	 * @param filepath 		抽取html后保存文本文件的路径
	 * @return boolean 		是否保存成功
	 */
	public static boolean storeTextFromHtml(Text text,String htmlfilename,String filePath) {
		String filepath = filePath+"/"+htmlfilename.substring(0,htmlfilename.lastIndexOf("htm"))+"txt";
		if(new File(filepath).exists() == true){
			System.out.println("Message: "+filepath+" is existed!");
			return false;
		}else{
			try{
				//创建一个使用默认大小输出缓冲区的缓冲字符输出流
				BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));			
				writer.append(text.getContext());//写入文本数据
				writer.close(); 	//关闭输出流
				return true;
			}catch(IOException ioe){
				System.out.println("Error :Processing"+htmlfilename+"accurs error");
				ioe.printStackTrace();
				return false;
			}
		}		
	}
	
	/**
	 * 持久化抽取出的纯文本数据
	 * @param text
	 * @param xmlfilename   xml文件名
	 * @param filepath 		抽取xml后保存文本文件的路径
	 * @return boolean 		是否保存成功
	 */
	public static boolean storeText(Text text,String xmlfilename,String filePath) {
		String filepath = filePath+"/"+xmlfilename.substring(0,xmlfilename.lastIndexOf("xml"))+".txt";
		if(new File(filepath).exists() == true){
			System.out.println("Message: "+filepath+" is existed!");
			return false;
		}else{
			try{
				//创建一个使用默认大小输出缓冲区的缓冲字符输出流
				BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));			
				writer.append(text.getContext());//写入文本数据
				writer.close(); 	//关闭输出流
				return true;
			}catch(IOException ioe){
				System.out.println("Error :Processing"+xmlfilename+"accurs error");
				ioe.printStackTrace();
				return false;
			}
		}		
	}
}
