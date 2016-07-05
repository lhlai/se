package se.extractor.pageHandler;
/**
 * ʹ��dom4j��ȡxml�ĵ��е��ı�����,���־û���txt�ļ�
 * @attention ���е�xml�ĵ���ΪDetailPage�����ʹ�ã�!
 * @author pillar
 * @version 2.0
 * @since 2016.6.18
 */
import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import se.extractor.algorithm.DepthFirstTraversal;
import se.extractor.page.Text;
import se.extractor.util.PageLib;

public class Dom4jExtractor implements Extractor {
	private int saveNum = 0;      //������ķǿ��ı�����
	private int extractNum = 0;          //����ȡ�ķǿ��ı�����
    private int xmlNum = 0;
    
    /**
     * ִ�г�ȡ�ĺ��ķ���
     * @param xmlpath  xml�ļ���ַ
     * @param filepath ��ȡxml�󱣴��ı��ļ���·��
     * @author pillar
     * @update 2016.6.18
     */
    @Override
    public void processExtract(String xmlpath,String filepath){
    	File[] files = new File(xmlpath).listFiles();
    	for(int i=0;i<files.length;i++){
    		if(files[i].isDirectory() == true){ 	//���files[i]��Ŀ¼�ļ�����ݹ�ִ��processExtract()����
				processExtract(files[i].getAbsolutePath(),filepath);
			}else{
				try{
					xmlNum++;
					extract(files[i].getAbsolutePath(),filepath);
				}catch(Exception e){
					e.printStackTrace();		
				}	
				if(i==files.length-1){
	    			System.out.println("��������: "+saveNum+" ���ǿ��ı�!");
	    			System.out.println("����ȡ��: "+extractNum+" ���ǿ��ı�!");
	    			System.out.println("����: "+xmlNum+" ��xml�ļ�!");
				}
			}		
    	}
    }
    
	 /**
     * �����ݴ���Page
     * @param xmlfilename 	����xml�ļ�·��
     * @param filepath 		��ȡxml�󱣴��ı��ļ���·��
	 * @throws Exception 
     */
	@Override
	public void extract(String xmlfilename,String filepath) throws Exception{    //���洢·��xml��ҳ�ĵ�
		String filename = null;
		Document document = null;
		System.out.println("Message:Now extracting "+xmlfilename);
		String fullname = xmlfilename.replace("\\", "/"); 
	    System.out.println("file is:"+fullname);
	    filename = fullname.substring(fullname.lastIndexOf("/")+1);//�����洢·����xml�ĵ���
	    SAXReader reader = new SAXReader();
		document = reader.read(new File("xml/"+filename));     //Jtidy���������ɵ�xml�ĵ�����xml�ļ�������
	    try{
	    	run(filename, document, filepath);
	    }catch(Exception e){	
	    	e.printStackTrace();	
	    }    
	}

	/**
	 * page.setTitle()
     * page.setContext()����������ȱ����㷨����׼��ȡ����
     * page.setScore()����PageRank()�㷨����ҳIRֵ������ҳ�ۺϵ÷�
	 * @param srcfile   xml�ļ���
	 * @param Document  ��xml�����dom�����document����
	 * @param filepath 		��ȡxml�󱣴��ı��ļ���·��
	 * @update 2016.6.18
	 */
	private void run(String srcfile,Document document,String filepath){
        try{
        	Text text = new Text();
        	if(text.setContext(getContext_DetailPage(document))){ //�ж�context�Ƿ�Ϊ��
        		extractNum++;
        		if(PageLib.storeText(text,srcfile, filepath)){	//��text����Ϊ��ʱ����text������뱾���ļ�
        			saveNum++;        //������
        		}        		
        	}else{
        		System.out.println("The xml file: "+srcfile+" do not hava any content!");
        		System.exit(1);  //status״̬��Ϊ��0ʱ��ʾ����������
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	//��ȡ��ϸҳ�������
	private String getContext_DetailPage(Document document){
		String context = new DepthFirstTraversal().getDetailPageContext(document);
        return context;
	}
	
}
