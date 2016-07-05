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
 * 文本预处理管理器，按行写入每一个文件内容,不需要写入分类标签！
 * @author pillar
 * @since 2016 3.28
 * @update 2016.6.21
 * @version 2.0
 */
public class TextPreprocess {
	
	public static void main(String[] args){
		preprocess();
	}
	
	private static String txtPath = "txt";  	//文本路径
//	private static String PerProcessFile = "PreProcessFile/preprocessfile.txt";  //预处理后文本--分词并去停用词
//	private static String spiltData = "PreProcessFile/spiltData.txt";  //预处理后文本--只分词
	private static String rawData = "PreProcessFile/rawData.txt";  //预处理后文本--不分词不去停用词
	
	/**
	 * 把训练集所有文本文件写入一个文本文件内，一行表示一个文本
	 * @author pillar
	 * @param finalText  分词并去掉停用词之后的文本内容
	 * @param filepath   保存从各子文件抽取文本的最终文件路径
	 * @return   是否写入成功
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
			File file = new File(filepath);    			//创建文本文件
			if(!file.exists()){                			//判断文本文件是否已经存在
				file.createNewFile();  
			}					
			fw = new FileWriter(file,true);    			//记得加第二个参数，否则默认false，文本只能保存最后一次写入数据
			writer = new BufferedWriter(fw);
			String str = temp+"\r\n";    //自动换行
			writer.write(str); 				   			//写入文件
			writer.flush();					   			//把缓存区内容压入文件 
			writer.close();					   			//关闭文件
		}catch(IOException ioe){
			ioe.printStackTrace();
			System.out.println("写入失败!");
			return false;
		}
		return true;	
	}
	
	/**
	 * 把分散的txt文本写入一个txt文件s
	 * @param rawText    某个txt文件文本内容
	 * @param filepath   某个txt文件路径
	 * @return
	 */
	public boolean writeRawTextToFile(String rawText, String filepath){
		BufferedWriter writer = null;
		FileWriter fw = null;
		try{
			File file = new File(filepath);    			//创建文本文件
			if(!file.exists()){                			//判断文本文件是否已经存在
				file.createNewFile();  
			}					
			fw = new FileWriter(file,true);    			//记得加第二个参数，否则默认false，文本只能保存最后一次写入数据
			writer = new BufferedWriter(fw);
			String str = rawText+"\r\n";    //自动换行
			writer.write(str); 				   			//写入文件
			writer.flush();					   			//把缓存区内容压入文件 
			writer.close();					   			//关闭文件
		}catch(IOException ioe){
			ioe.printStackTrace();
			System.out.println("写入失败!");
			return false;
		}
		return true;	
	}
	
	/**
	 * 读取训练集指定目录下指定文本文件的内容
	 * @param filename  训练集某个目录下的某个文本文件
	 * @return content  文本内容
	 */
	public String readText(String filename){
		StringBuffer buffer = new StringBuffer();
		FileInputStream in = null;
		InputStreamReader reader = null;    		//输入流对象reader
		BufferedReader br = null;		   			//把文件内容转成计算机能读懂的语言的对象
		File file = new File(filename);
		try{
			if(file.exists()){
				in = new FileInputStream(file);
				reader = new InputStreamReader(in);
				br = new BufferedReader(reader);
				String line = br.readLine(); 		//临时变量
				while(line!=null){
					buffer.append(line);
					line = br.readLine(); 		   // 一次读入一行数据
				}
			}else{
				System.out.println("文件:"+filename+"不存在!");
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}	
		return buffer.toString();	
	}
	/**
	 * 中文分词
	 * @author pillar
	 * @param rawText  未处理的原始文本
	 * @return  terms   经过中文分词的文本向量(数组)
	 */
	private String[] chineseSpilt(String rawText) {
		String[] terms = null;
        terms= ChineseSpliter.split(rawText, " ").split(" "); //中文分词处理(分词后结果可能还包含有停用词）
        return terms;
	}
	/**
	 * 去停用词
	 * @param spiltText  已经分词后的文本向量(数组)
	 * @return   newWords  去掉停用词后的文本向量
	 */
	private String[] DropStopWords(String[] spiltText) {
		Vector<String> v1 = new Vector<String>();
        for(int i=0;i<spiltText.length;++i)
        {
            if(StopWordHandler.IsStopWord(spiltText[i])==false)
            {//不是停用词
                v1.add(spiltText[i]);
            }
        }
        String[] newWords = new String[v1.size()];
        v1.toArray(newWords);       
		return newWords;
	}
	
	/**
	 * 根据txt总路径得到各文件的具体路径
	 * @param txtpath    txt文件的总存储路径
	 * @return  String[] txt文本文件路径集合
	 */
	private String[] getFilePath(String txtpath){
		File filepath = new File(txtpath);  		//训练集
		if (!filepath.isDirectory()) 
        {
            throw new IllegalArgumentException("训练语料库搜索失败！ [" +filepath+ "]");
        }else{  	
        	return filepath.list();     //各txt文件路径数组
        }	
	}
	
	/**
	 * 文本预处理
	 * @author pillar
	 * @update 2016.6.21
	 */
	public static void preprocess(){
		double begin = System.currentTimeMillis();	
		String rawText = "";    							 //写入训练文本的内容
//		String[] spiltText = null; 							 //中文分词后文本
//		String[] finalText = null;							 //最终存入训练集的文本内容
		String[] filepaths = null;		
		TextPreprocess preprocess = new TextPreprocess();		
		try{
			filepaths = preprocess.getFilePath(txtPath);     //文件路径集合
			for(String filepath:filepaths){					 //单个文件
				rawText = preprocess.readText(txtPath+"/"+filepath);     //读取训练集文本内容
				System.out.println(rawText);
//				spiltText = preprocess.chineseSpilt(rawText);
//				finalText = preprocess.DropStopWords(spiltText);
//				boolean status = preprocess.writeTextToFile(spiltText, spiltData);  //写入文件
				boolean status = preprocess.writeRawTextToFile(rawText, rawData);  //写入文件
				if(status){
					System.out.println("文件写入成功!");
				}else{
					System.out.println("文件写入失败!");
				}
			}			
		}catch(Exception e){
			e.printStackTrace();
		}			
		System.out.println("共耗时:"+(System.currentTimeMillis()-begin)/1000+"秒");
	}	
}
