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
 * ����jsoup��html�г�ȡ�ı�����
 * @author pillar
 * @version 1.0
 * @since 2016.6.19
 */
public class JsoupExtractor {
	private int extractNum = 0;          //����ȡ�ķǿ��ı�����
	private int saveNum = 0; 			 //������ı�����
    private int htmlNum = 0;			 //html�ĵ�����
    
	public void processExtract(String htmlpath, String filepath){
		File[] files = new File(htmlpath).listFiles();
    	for(int i=0;i<files.length;i++){
    		if(files[i].isDirectory() == true){ 	//���files[i]��Ŀ¼�ļ�����ݹ�ִ��processExtract()����
				processExtract(files[i].getAbsolutePath(), filepath);
			}else{
				try{
					htmlNum++;
					System.out.println("Message:���ڳ�ȡ��"+(i+1)+"ƪ����:");
					extract(files[i].getAbsolutePath(),filepath);					
				}catch(Exception e){
					e.printStackTrace();
				}	
				if(i==files.length-1){
					System.out.println("����ȡ��: "+extractNum+" ���ǿ�html�ļ�!");
					System.out.println("��������: "+saveNum+" ���ǿ��ı�!");
	    			System.out.println("����: "+htmlNum+" ��html�ļ�!");
				}
			}		
    	}
	}

	/**
	 * jsoup��ȡhtml���ı�
	 * @param htmlfilename    ĳ��html�ļ�·��
	 * @param filepath        �����ȡ�����ı�·��
	 */
	private void extract(String htmlfilename,String filepath) {
		String filename = null;
		System.out.println("Message:Now extracting "+htmlfilename);
		String fullname = htmlfilename.replace("\\", "/"); 
	    filename = fullname.substring(fullname.lastIndexOf("/")+1);  //�����洢·����html�ĵ���
	    
		String content = null;      //htmlԴ������
		String result = null;
		Elements e1 = null;		  //���±���
		Elements e2 = null;		  //��������
		List<Element> detail_title = null;  //���±���
		List<Element> detail_content = null; //��������	
		StringBuffer buffer = new StringBuffer();	  //jsoup��ȡ����html���ı�����	
		try{
			content  = readHtmlToString(htmlfilename);
			Document doc = Jsoup.parse(content);
			//���±���
			e1 = doc.select("[class=hui_15_cu]");	//�й�ũҵ��Ϣ���Ƽ�������±�������
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
				//��������
				e2 = doc.select("p");
				detail_content = e2;
			}	
			//��ȡ�ı��ϲ�
//			String title = detail_title.get(0).text();
//			buffer.append("���±���: "+"\n"+title+"\n");
//			buffer.append("��������:  "+"\n");			
//			String tmpcontent = detail_content.get(0).siblingElements().text();
//			buffer.append(tmpcontent);		
			
			//��������ݺϲ���ͬһ��
			String title = detail_title.get(0).text();
			buffer.append(title+" ");	
			String tmpcontent = detail_content.get(0).siblingElements().text();
			buffer.append(tmpcontent);	
			result = buffer.toString();
//			System.out.println(result);   //��ӡ��������
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
	 * �����ȡ�����ı�����Ϊtxt
	 * @param content     ��ȡ�����ı�����
	 * @param srcfile     ԭhtml�ļ���
	 * @param filepath    ����txt�ı�·��
	 */
	private void run(String srcfile,String content, String filepath){
        try{
        	Text text = new Text();
        	if(text.setContext(content)){ //�ж�context�Ƿ�Ϊ��
        		extractNum++;
        		if(PageLib.storeTextFromHtml(text, srcfile, filepath)){	//��text����Ϊ��ʱ����text������뱾���ļ�
        			saveNum++;        //������
        		}        		
        	}else{
        		System.out.println("The html file: "+srcfile+" do not hava any content!");
//        		System.exit(1);  //status״̬��Ϊ��0ʱ��ʾ����������
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	/**
	 * һ���԰ѱ���html�ļ������ݶ����ڴ�
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
