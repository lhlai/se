package se.extractor.pageHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import se.extractor.page.Text;
import se.extractor.util.PageLib;

/**
 * 利用jsoup从html中抽取文本数据
 * @author pillar
 * @version 1.0
 * @since 2016.6.19
 */
public class JsoupExtractor {
	private int extractNum = 0;          //共抽取的非空文本数量
	private int saveNum = 0; 			 //保存的文本数量
    private int htmlNum = 0;			 //html文档数量
    
	public void processExtract(String htmlpath, String filepath){
		File[] files = new File(htmlpath).listFiles();
    	for(int i=0;i<files.length;i++){
    		if(files[i].isDirectory() == true){ 	//如果files[i]是目录文件，则递归执行processExtract()方法
				processExtract(files[i].getAbsolutePath(), filepath);
			}else{
				try{
					htmlNum++;
					System.out.println("Message:正在抽取第"+(i+1)+"篇文章:");
					extract(files[i].getAbsolutePath(),filepath);					
				}catch(Exception e){
					e.printStackTrace();
				}	
				if(i==files.length-1){
					System.out.println("共抽取了: "+extractNum+" 个非空html文件!");
					System.out.println("共保存了: "+saveNum+" 个非空文本!");
	    			System.out.println("共有: "+htmlNum+" 个html文件!");
				}
			}		
    	}
	}

	/**
	 * jsoup抽取html内文本
	 * @param htmlfilename    某个html文件路径
	 * @param filepath        保存抽取出的文本路径
	 */
	private void extract(String htmlfilename,String filepath) {
		String filename = null;
		System.out.println("Message:Now extracting "+htmlfilename);
		String fullname = htmlfilename.replace("\\", "/"); 
	    filename = fullname.substring(fullname.lastIndexOf("/")+1);  //不带存储路径的html文档名
	    
		String content = null;      //html源码内容
		String result = null;
		Elements e1 = null;		  //文章标题
		Elements e2 = null;		  //文章内容
		List<Element> detail_title = null;  //文章标题
		List<Element> detail_content = null; //文章内容	
		StringBuffer buffer = new StringBuffer();	  //jsoup抽取出的html内文本内容	
		try{
			content  = readHtmlToString(htmlfilename);
			Document doc = Jsoup.parse(content);
			//文章标题
			e1 = doc.select("[class=hui_15_cu]");	//中国农业信息网科技板块文章标题类型
			detail_title = e1.select("td");			
			String tmp_title1 = detail_title.toString();
			if(tmp_title1.equals("")||tmp_title1==null){
				e1 = doc.select("[class=detail_title]");
				detail_title = e1.select("h3");		
				e2 = doc.select("[class=detail_content]");				
				detail_content = e2.select("p");
				String tmp_title2 = detail_title.toString();	
				String tmp_content = detail_content.toString();
				if(tmp_title2.equals("")||tmp_title2==null){
					e1 = doc.select("[class=hh4_m121]");
					detail_title = e1;
					e2 = doc.select("[class=hh4_m125]");	
					detail_content = e2.select("p");
					if(tmp_content.equals("")||tmp_content==null){	
						
					}
				}
			}else{
				//文章内容
				e2 = doc.select("p");
				detail_content = e2;
			}	
			//提取文本合并
//			String title = detail_title.get(0).text();
//			buffer.append("文章标题: "+"\n"+title+"\n");
//			buffer.append("文章内容:  "+"\n");			
//			String tmpcontent = detail_content.get(0).siblingElements().text();
//			buffer.append(tmpcontent);		
			
			//标题和内容合并在同一行
			String title = detail_title.get(0).text();
			buffer.append(title+" ");	
			String tmpcontent = detail_content.get(0).siblingElements().text();
			buffer.append(tmpcontent);	
			result = buffer.toString();
//			System.out.println(result);   //打印文章内容
			try{
		    	run(filename, result, filepath);  
		    }catch(Exception e){	
		    	e.printStackTrace();
//		    	System.exit(1);
		    } 
		}catch(Exception e){
			e.printStackTrace();
//			System.exit(1);
		}		
	}

	/**
	 * 保存抽取出的文本内容为txt
	 * @param content     抽取出的文本内容
	 * @param srcfile     原html文件名
	 * @param filepath    保存txt文本路径
	 */
	private void run(String srcfile,String content, String filepath){
        try{
        	Text text = new Text();
        	if(text.setContext(content)){ //判断context是否为空
        		extractNum++;
        		if(PageLib.storeTextFromHtml(text, srcfile, filepath)){	//当text对象不为空时，将text对象存入本地文件
        			saveNum++;        //计数器
        		}        		
        	}else{
        		System.out.println("The html file: "+srcfile+" do not hava any content!");
//        		System.exit(1);  //status状态码为非0时表示非正常结束
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	/**
	 * 一次性把本地html文件的内容读入内存
	 * @param htmlfileName
	 * @return
	 */
	public String readHtmlToString(String htmlfileName) {  
//        String encoding = "ISO-8859-1";  
        String encoding = "utf-8";  
        File file = new File(htmlfileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    } 
}
