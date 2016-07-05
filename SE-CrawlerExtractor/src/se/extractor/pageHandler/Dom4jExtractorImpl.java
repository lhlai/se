package se.extractor.pageHandler;
/**
 * ʹ��dom4j��ȡxml�ĵ��е��ı�����,���־û���txt�ļ�
 * @author pillar
 * @version 1.0
 */
import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import se.extractor.algorithm.DepthFirstTraversal;
import se.extractor.algorithm.MD5;
import se.extractor.algorithm.PageClassifier;
import se.extractor.page.Page;
import se.extractor.util.PageLib;

public class Dom4jExtractorImpl implements Runnable,Extractor {
	private String filename = null;  //��������Ҫ������xml�ĵ���
	private Page page = null;               //����Page��
//	private String encode = null;    //������ҳ�ı���
    private Document document = null;       
    
    /**
     * ִ�г�ȡ�ĺ��ķ���
     * @param xmlpath  xml�ļ���ַ
     * @param filepath ��ȡxml�󱣴��ı��ļ���·��
     * @author pillar
     */
    @Override
    public void processExtract(String xmlpath,String filepath){
    	File[] files = new File(xmlpath).listFiles();
    	for(int i=0;i<files.length;i++){
    		if(files[i].isDirectory() == true){ 	//���files[i]��Ŀ¼�ļ�����ݹ�ִ��processExtract()����
				processExtract(files[i].getAbsolutePath(), filepath);
			}else{
				try{
					extract(files[i].getAbsolutePath(), filepath);
				}catch(Exception e){
					e.printStackTrace();
				}	
			}
    	}
    }
	 /**
     * �����ݴ���Page
     * @param filepath ��ȡxml�󱣴��ı��ļ���·��
	 * @throws Exception 
     */
	@Override
	public void extract(String xmlfilename,String filepath) throws Exception{    //���洢·��xml��ҳ�ĵ�
		System.out.println("Message:Now extracting "+xmlfilename);
		String fullname = xmlfilename.replace("\\", "/"); 
	    System.out.println("file is:"+fullname);
	    this.filename = fullname.substring(fullname.lastIndexOf("/")+1);//�����洢·����xml�ĵ���
	    setDocument(filename);
	    try{
	    	run();
	    	if(this.page!=null){   //��page����Ϊ��ʱ����page������뱾���ļ�
		    	PageLib.storePage(this.page,filepath);
		    }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }    
	}
	
	private String getFilename(){
		return filename;
	}
	/**
	 * ���룺xml�ĵ�
	 * �����DOM������
	 */
	private void setDocument(String filename) throws Exception{
	    SAXReader reader = new SAXReader();
		this.document = reader.read(new File("xml/"+filename));   //Jtidy���������ɵ�xml�ĵ�����xml�ļ�������
	}   
	private Document getDocument(){
		return document;
	}

	/**
	 * ��дRunnable��run()����
     * page.setTitle()
     * page.setContext()����������ȱ����㷨����׼��ȡ����
     * page.setScore()����PageRank()�㷨����ҳIRֵ������ҳ�ۺϵ÷�
     */
    @Override
	public void run(){
        try{
        	page = new Page();
        	this.page.setUrl(getUrl(getFilename()));
//        	this.page.setTitle(getTitle(getDocument())); //���Ľ�
        	//���ݲ�ͬ����ҳ���ͣ����ò�ͬ��ȡ����
        	String pagetype = pageType(getDocument());
        	System.out.println("This is a "+pagetype);
        	this.page.setPageType(pagetype);
        	if("DetailPage".equals(pagetype)){
        		this.page.setContext(getContext_DetailPage(getDocument())); 
        	}else if("ListPage".equals(pagetype)){
        		this.page.setContext(getContext_ListPage(getDocument())); 
        	}else if("UnknowPage".equals(pagetype)){
        		this.page.setContext(getContext_UnknowPage(getDocument()));
        	}
//        	this.page.setScore(getScore(getFilename(),this.page.getUrl())); //���Ľ�
        	this.page.setScore(getScore(this.page.getUrl(),this.page.getScore()));
        	this.page.setSummary(getSummary(this.page.getContext()));
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
    //�ж���ҳ����
    private String pageType(Document document){
    	String pagetype = new PageClassifier().jugePageType(document);
    	return pagetype;		
    }
	//New Method
	private String getUrl(String filename) {
		String url = filename.replace("xml", "html");
		return url;
	}
	//New Method
//	private String getTitle(Document document){
//		Element ele = (Element) document.selectNodes("/html/head/title");
//		String title = ele.getText();
//		return title;
//	}
	
	//��ȡ����ҳ�������
	private String getContext_ListPage(Document document){
		String context = new DepthFirstTraversal().getDetailPageContext(document);
		return context;
	}
	//��ȡ��ϸҳ�������
	private String getContext_DetailPage(Document document){
		String context = new DepthFirstTraversal().getDetailPageContext(document);
        return context;
	}
	 //��ȡδ֪ҳ�������
	private String getContext_UnknowPage(Document document) {
		DepthFirstTraversal labelpath = new DepthFirstTraversal();
	    String[] path = labelpath.getfrequentpath(document);
		String context = labelpath.getUnknowPageContext(document,path);
		return context;
   }
	//���Ľ�����������PageRank�㷨
//	private int getScore(String filename,String url) {
//        
//		return 0;
//	}
	//old method
	private double getScore(String url,double score){
		String[] subStr = url.split("/");
		score = score-(subStr.length-1); //����URL�Ĳ㼶���򵥵ĸ�URL��ַ��֣�
		return score;  //��������
	}
	//�ļ���
	private String getSummary(String context) {
		if(context == null){
			context = "";
		}
		return MD5.MD5Encode(context)+".txt"; //����Ϊtxt�ı�
	} 
//    public String getEncode() {
//		return encode;
//	}
//	public void setEncode(String encode) {
//		this.encode = encode;
//	}
}
