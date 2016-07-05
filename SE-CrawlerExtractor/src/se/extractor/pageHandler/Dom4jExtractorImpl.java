package se.extractor.pageHandler;
/**
 * 使用dom4j抽取xml文档中的文本数据,并持久化到txt文件
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
	private String filename = null;  //用来保存要解析的xml文档名
	private Page page = null;               //引入Page类
//	private String encode = null;    //保存网页的编码
    private Document document = null;       
    
    /**
     * 执行抽取的核心方法
     * @param xmlpath  xml文件地址
     * @param filepath 抽取xml后保存文本文件的路径
     * @author pillar
     */
    @Override
    public void processExtract(String xmlpath,String filepath){
    	File[] files = new File(xmlpath).listFiles();
    	for(int i=0;i<files.length;i++){
    		if(files[i].isDirectory() == true){ 	//如果files[i]是目录文件，则递归执行processExtract()方法
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
     * 将数据存入Page
     * @param filepath 抽取xml后保存文本文件的路径
	 * @throws Exception 
     */
	@Override
	public void extract(String xmlfilename,String filepath) throws Exception{    //带存储路径xml网页文档
		System.out.println("Message:Now extracting "+xmlfilename);
		String fullname = xmlfilename.replace("\\", "/"); 
	    System.out.println("file is:"+fullname);
	    this.filename = fullname.substring(fullname.lastIndexOf("/")+1);//不带存储路径的xml文档名
	    setDocument(filename);
	    try{
	    	run();
	    	if(this.page!=null){   //当page对象不为空时，将page对象存入本地文件
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
	 * 输入：xml文档
	 * 输出：DOM树对象
	 */
	private void setDocument(String filename) throws Exception{
	    SAXReader reader = new SAXReader();
		this.document = reader.read(new File("xml/"+filename));   //Jtidy解析后生成的xml文档放在xml文件夹下面
	}   
	private Document getDocument(){
		return document;
	}

	/**
	 * 重写Runnable的run()方法
     * page.setTitle()
     * page.setContext()调用深度优先遍历算法，精准抽取数据
     * page.setScore()调用PageRank()算法及网页IR值计算网页综合得分
     */
    @Override
	public void run(){
        try{
        	page = new Page();
        	this.page.setUrl(getUrl(getFilename()));
//        	this.page.setTitle(getTitle(getDocument())); //待改进
        	//根据不同的网页类型，设置不同抽取规则
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
//        	this.page.setScore(getScore(getFilename(),this.page.getUrl())); //待改进
        	this.page.setScore(getScore(this.page.getUrl(),this.page.getScore()));
        	this.page.setSummary(getSummary(this.page.getContext()));
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
    //判断网页类型
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
	
	//获取导航页面的内容
	private String getContext_ListPage(Document document){
		String context = new DepthFirstTraversal().getDetailPageContext(document);
		return context;
	}
	//获取详细页面的内容
	private String getContext_DetailPage(Document document){
		String context = new DepthFirstTraversal().getDetailPageContext(document);
        return context;
	}
	 //获取未知页面的内容
	private String getContext_UnknowPage(Document document) {
		DepthFirstTraversal labelpath = new DepthFirstTraversal();
	    String[] path = labelpath.getfrequentpath(document);
		String context = labelpath.getUnknowPageContext(document,path);
		return context;
   }
	//待改进方法，调用PageRank算法
//	private int getScore(String filename,String url) {
//        
//		return 0;
//	}
	//old method
	private double getScore(String url,double score){
		String[] subStr = url.split("/");
		score = score-(subStr.length-1); //根据URL的层级数简单的给URL地址打分！
		return score;  //返回评分
	}
	//文件名
	private String getSummary(String context) {
		if(context == null){
			context = "";
		}
		return MD5.MD5Encode(context)+".txt"; //保存为txt文本
	} 
//    public String getEncode() {
//		return encode;
//	}
//	public void setEncode(String encode) {
//		this.encode = encode;
//	}
}
