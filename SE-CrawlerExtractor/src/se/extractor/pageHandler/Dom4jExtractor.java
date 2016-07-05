package se.extractor.pageHandler;
/**
 * 使用dom4j抽取xml文档中的文本数据,并持久化到txt文件
 * @attention 所有的xml文档均为DetailPage情况下使用！!
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
	private int saveNum = 0;      //共保存的非空文本数量
	private int extractNum = 0;          //共抽取的非空文本数量
    private int xmlNum = 0;
    
    /**
     * 执行抽取的核心方法
     * @param xmlpath  xml文件地址
     * @param filepath 抽取xml后保存文本文件的路径
     * @author pillar
     * @update 2016.6.18
     */
    @Override
    public void processExtract(String xmlpath,String filepath){
    	File[] files = new File(xmlpath).listFiles();
    	for(int i=0;i<files.length;i++){
    		if(files[i].isDirectory() == true){ 	//如果files[i]是目录文件，则递归执行processExtract()方法
				processExtract(files[i].getAbsolutePath(),filepath);
			}else{
				try{
					xmlNum++;
					extract(files[i].getAbsolutePath(),filepath);
				}catch(Exception e){
					e.printStackTrace();		
				}	
				if(i==files.length-1){
	    			System.out.println("共保存了: "+saveNum+" 个非空文本!");
	    			System.out.println("共抽取了: "+extractNum+" 个非空文本!");
	    			System.out.println("共有: "+xmlNum+" 个xml文件!");
				}
			}		
    	}
    }
    
	 /**
     * 将数据存入Page
     * @param xmlfilename 	单个xml文件路径
     * @param filepath 		抽取xml后保存文本文件的路径
	 * @throws Exception 
     */
	@Override
	public void extract(String xmlfilename,String filepath) throws Exception{    //带存储路径xml网页文档
		String filename = null;
		Document document = null;
		System.out.println("Message:Now extracting "+xmlfilename);
		String fullname = xmlfilename.replace("\\", "/"); 
	    System.out.println("file is:"+fullname);
	    filename = fullname.substring(fullname.lastIndexOf("/")+1);//不带存储路径的xml文档名
	    SAXReader reader = new SAXReader();
		document = reader.read(new File("xml/"+filename));     //Jtidy解析后生成的xml文档放在xml文件夹下面
	    try{
	    	run(filename, document, filepath);
	    }catch(Exception e){	
	    	e.printStackTrace();	
	    }    
	}

	/**
	 * page.setTitle()
     * page.setContext()调用深度优先遍历算法，精准抽取数据
     * page.setScore()调用PageRank()算法及网页IR值计算网页综合得分
	 * @param srcfile   xml文件名
	 * @param Document  把xml构造成dom树后的document对象
	 * @param filepath 		抽取xml后保存文本文件的路径
	 * @update 2016.6.18
	 */
	private void run(String srcfile,Document document,String filepath){
        try{
        	Text text = new Text();
        	if(text.setContext(getContext_DetailPage(document))){ //判断context是否为空
        		extractNum++;
        		if(PageLib.storeText(text,srcfile, filepath)){	//当text对象不为空时，将text对象存入本地文件
        			saveNum++;        //计数器
        		}        		
        	}else{
        		System.out.println("The xml file: "+srcfile+" do not hava any content!");
        		System.exit(1);  //status状态码为非0时表示非正常结束
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	//获取详细页面的内容
	private String getContext_DetailPage(Document document){
		String context = new DepthFirstTraversal().getDetailPageContext(document);
        return context;
	}
	
}
