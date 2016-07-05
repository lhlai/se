package se.extractor.pageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.w3c.tidy.Tidy;

public class HtmlToXmlImpl implements HtmlToXml { //采用Runnable接口方式创建多线程时不同线程可以共享线程类的实例变量
	private String srcFile = null;
	private String outFile= null;
	private String configFile= null;
//    private String logFile= null;
	private String encoding= null;
    private int htmNum = 0;			 //htm数量
    private int exchangeNum = 0;     //成功转为xml的htm数量
	
	/**
	 * Exchange Html To Xml
	 * @param htmlpath
     */
	public void processExchangeHtmlToXml(String htmlpath,String outputpath,String configfilepath) {
		File[] files = new File(htmlpath).listFiles();
		HtmlToXmlImpl htx = new HtmlToXmlImpl();
		for(int i = 0; i<files.length;i++){
			//isDirectory()方法用于判断该files[i]下是否为目录文件，true则是，false则不是。
			if(files[i].isDirectory() == true){
				//如果files[i]是目录文件，则递归执行processExtract()方法。
				processExchangeHtmlToXml(files[i].getAbsolutePath(),outputpath,configfilepath);
			}
			else{				
				htmNum++;
				htx.setConfigFile(configfilepath);
				htx.extract(files[i].getAbsolutePath(),outputpath);
				
				if(i==files.length-1){
	    			System.out.println("共成功转换了: "+exchangeNum+" 个xml文件!");
	    			System.out.println("共有: "+htmNum+" 个htm文件!");
				}
			}
		}
	}
	
	public void extract(String htmlfilename,String outputpath){		
		try{
			System.out.println("Message:Now extracting "+htmlfilename);
		    String filename = htmlfilename.replace("\\", "/"); 
		    System.out.println("filename is: "+filename);
		    setSrcFile(filename);
		    int start = filename.lastIndexOf("/");
		    int end = filename.lastIndexOf(".");
			String output = filename.substring(start, end);
			setOutFile(outputpath+output+".xml");
//			System.out.println("output:"+output);
			setEncoding(filename);		
			if(run()){
				exchangeNum++;
			}; 
		}catch(Exception e){
			e.printStackTrace();
		}		 
	}
	/**
	 * 负责将html文件转换为xml文件的核心方法
	 * @return
	 * @throws FileNotFoundException 
	 */
	public boolean  run(){
		try{	
			Tidy t = new Tidy();
		    FileInputStream in = null;
		    FileOutputStream out = null;
			in = new FileInputStream(getSrcFile());
			out = new FileOutputStream(getOutFile());
			t.setConfigurationFromFile(getConfigFile());
			t.setInputEncoding(getEncoding());
			t.setOutputEncoding("utf-8");
			t.parseDOM(in, out);						
			in.close();  //关闭输入输出流
			out.close();
			return true;
		  }catch (FileNotFoundException fnfe) {			  
			  fnfe.printStackTrace();
			  return false;
		  } catch (IOException ioe) {
			  ioe.printStackTrace();
			  return false;
		  }
	}
	
	public String getSrcFile() {
		return srcFile;
	}
	public void setSrcFile(String srcFile) {
		this.srcFile = srcFile;
	}
	public String getOutFile() {
		return outFile;
	}
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	public void setEncoding(String srcfile){
		String encode = "GB2312";
		try{
			  BufferedReader reader = new BufferedReader(new FileReader(srcfile)); 
			  String line = reader.readLine();
			  while(line!=null){
				  if(line.indexOf("charset=")!=-1){
					  int start = line.indexOf("charset=");
					  start = start+8;
					  String tmp = line.substring(start,start+4);
					  if("UTF-".equals(tmp)||"utf-".equals(tmp)){
						  encode = "UTF-8";
					  }
					  else if("ISO-".equals(tmp)||"iso-".equals(tmp)){
						  encode = "ISO-8859-1";
					  }
					  else if("GBK2".equals(tmp)||"gbk2".equals(tmp)){
						  encode = "GBK2312";
					  }
					  else if("GB23".equals(tmp)||"gb23".equals(tmp)){
						  encode = "gb2312";
					  }
					  else if("\"GBK".equals(tmp)||"\"gbk".equals(tmp)){
						  encode = "gbk2312";						
				      }
					  else if("\"GB2".equals(tmp)||"\"gb2".equals(tmp)){
							  encode = "gb2312";						
					  }
					  else if("\"UTF".equals(tmp)||"\"utf".equals(tmp)){
						  encode = "UTF-8";
					  }
					  else if("\"ISO".equals(tmp)||"\"iso".equals(tmp)){
						  encode = "ISO-8859-1";
					  }
					  else{ 
						  encode = "GBK2312";
					  }
					  reader.close();
					  break;
				  }else{
					  line = reader.readLine();
				  }
			  }
		}catch(Exception e){
			e.printStackTrace();
		}
	    this.encoding = encode;
	}
	public String getEncoding(){
		return encoding;
	}
}
