package se.extractor.algorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 深度优先遍历算法
 *     根据输入的DOM树对象，存储标记路径，判断并返回频繁路径等信息
 * @author pillar
 * @version 1.0
 * @since 2015.12
 */
public class DepthFirstTraversal {
    private int number = 0;    //记录prepath的总标记数量
	private int num = 0;       //记录合并后的prepath，即path的总标记数量
	private String[] prepath= null;
	private String[] path= null;
	private int[] count= null;
	private String[] trimpath = null;
	private int[] trimcount = null;
	private String[] sortpath = null;
	private int[] sortcount = null;
	private String[] frequentpath = null;
	private int[] frequentcount= null;
	private List<String> NodeText = new ArrayList<String>();
	private List<String> prepathlist = new ArrayList<String>();
	private List<String> pathlist = new ArrayList<String>();
	private List<Integer> countlist = new ArrayList<Integer>();
    /**
     * 抽取导航页面信息
     * @author pillar
     * @param document
     * @param path
     * @return context 
     * @throws Exception
     */
	public String getListContext(Document document,String[] path) throws Exception{
		String context=null;
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<path.length;i++){
			@SuppressWarnings("unchecked")
			List<Element> in = document.selectNodes(path[i]);
			Iterator<Element> it = in.iterator();
			while(it.hasNext()){
				Element tmpelm = (Element)it.next();
				if(!tmpelm.getTextTrim().equals("")&&!tmpelm.getTextTrim().equals("|")){
					Element elm = tmpelm;
					buffer.append(elm.getText());
			    }
			}
	    }
		context = buffer.toString();
		return context;
	}
	/**
	 * 抽取详细页面数据
	 * @param document   dom4j构造的网页DOM树模型
	 * @return context	 抽取输出的网页文本信息
	 */
	public String getDetailPageContext(Document document){
		String context=null;
		StringBuffer buffer = new StringBuffer();
		Element root = document.getRootElement();
		setNodeText(root);	
        for(int i=0;i<NodeText.size();i++){       	
        	buffer.append(NodeText.get(i)+"\n");        	
//        	System.out.println("("+i+")"+NodeText.get(i));
        }
		context = buffer.toString();
		return context;
	}
	/**
	 * 获取未知页面的文本内容
	 * @param document	dom4j构造的网页DOM树模型
	 * @param path	标记路径
	 * @return
	 */
	public String getUnknowPageContext(Document document,String[] path){
		String context=null;
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<path.length;i++){
			@SuppressWarnings("unchecked")
			List<Element> in = document.selectNodes(path[i]);
			Iterator<Element> it = in.iterator();
			while(it.hasNext()){
				Element tmpelm = (Element)it.next();
				if(!tmpelm.getTextTrim().equals("")&&!tmpelm.getTextTrim().equals("|")){
					Element elm = tmpelm;
					buffer.append(elm.getText());
			    }
			}
	    }
		context = buffer.toString();
		return context;
	}
    
//	private void readListPage(Document document){
//		Element root = document.getRootElement();  //获取跟结点root元素对象,Element是Dom4j包的
//		ListNodes(root);                           //遍历Document结点,getprepathlist
//		setPrepathandNumber(getPrepathlist());
//		System.out.println("有文字标签的节点个数为:"+number);
//		setPathandCount(getPrepath());
//		setTrimPathandTrimCount(pathlist,countlist);
//		setSortPathandSortCount(getTrimpath(),getTrimcount());
//		setFrequentpathandFrequentcount(sortpath,sortcount);
//	}
//	private void readDetailPage(Document document){
//		Element root = document.getRootElement();  //获取跟结点root元素对象,Element是Dom4j包的
//		ListNodes(root);                           //遍历Document结点,getprepathlist
//		setPrepathandNumber(getPrepathlist());
//		setPathandCount(getPrepath());
//		setTrimPathandTrimCount(pathlist,countlist);
//		setSortPathandSortCount(getTrimpath(),getTrimcount());
//		setFrequentpathandFrequentcount(sortpath,sortcount);
//	}
	private void readUnknowPage(Document document){
		Element root = document.getRootElement();  //获取跟结点root元素对象,Element是Dom4j包的
		ListNodes(root);                           //遍历Document结点,getprepathlist
		setPrepathandNumber(getPrepathlist());
		setPathandCount(getPrepath());
		setTrimPathandTrimCount(pathlist,countlist);
		setSortPathandSortCount(getTrimpath(),getTrimcount());
		setFrequentpathandFrequentcount(sortpath,sortcount);
	}
	/**
	 * 递归抽取详细页面的节点数据
	 * @param node	Dom4j构造的节点模型
	 * @return  NodeText   节点文本信息
	 */
	private void setNodeText(Element node){
//		ArrayList<String> NodeText = new ArrayList<String>();
  	    if(!node.getTextTrim().equals("")&&!node.getTextTrim().equals("|")){
  	    	if(node.getTextTrim()!=null&&node.getTextTrim().length()!=0){
  	    		String str = node.getPath();
	    		if(!str.contains("script")&&str.contains("body")){
	    			String tmp = node.getTextTrim();
	    			if(StringTest(tmp)){   //判断字符串数据长度是否符合要求
	    				if(ChineseRatio(tmp)>0.4){  //控制字符串中的中文占比
	    					NodeText.add(tmp);
	    				}
	    			}
  	    		    if(!node.isTextOnly()){ 	    		    	
  	    			    @SuppressWarnings("unchecked")
  					    Iterator<Element> iterator = node.elementIterator();  
  		  	    	    while(iterator.hasNext()){  
  		  	                Element e = iterator.next();  
  		  	                setNodeText(e);  	//递归回调
  		  	            }
  	    		    }
  	    		}	
  	    	}
  	   }else{	  
  	      //使用递归  ,迭代当前节点下面的所有子节点
          @SuppressWarnings("unchecked")
		  Iterator<Element> iterator = node.elementIterator();  
          while(iterator.hasNext()){
              Element e = iterator.next();  
              setNodeText(e);  
          }  
  	   }
   }
	
	//判断文本数据是否符合要求
	private boolean StringTest(String tmp) {
		int number = tmp.length();
		if(number>10){    //简单地以字符长度为判断依据
			return true;
		}else{
			return false;
		}
	}
	//判断字符串中是否含有中文
	public boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }else{
        	return false;
        }
    }
	//判断字符串中中文的占比
	private double ChineseRatio(String str){
		double ratio = 0;
		double count=0;
		char[] c = str.toCharArray(); 
	    for(int i=0;i<c.length;i++){
	       String a = String.valueOf(c[i]);   //将char类型转换为Strign类型
	       if(isContainChinese(a)){           //调用正则表达式判断是否为中文字符
	    	   count++;
	       }
	    }
	    ratio = count/c.length;
		return ratio;
	}
	/**
     * 遍历当前节点下的所有有文本数据的节点，并记录从根节点到末端的节点路径暂存在prepathlist集合,再传入prepath数组
     * @param node
     */
    private void ListNodes(Element node){
    	  //如果当前节点内容不为空，则输出
    	  if(!node.getTextTrim().equals("")&&!node.getTextTrim().equals("|")){
//    	     	System.out.println(node.getName()+":"+node.getText());
    		  this.prepathlist.add(node.getPath());
    		  if(!node.isTextOnly()){
    	            @SuppressWarnings("unchecked")
					Iterator<Element> iterator = node.elementIterator();  
    	    	    while(iterator.hasNext()){  
    	                 Element e = iterator.next();  
    	                 ListNodes(e);  
    	            }
  			  }
    	  }
    	  else{
    	        //使用递归  ,迭代当前节点下面的所有子节点
                @SuppressWarnings("unchecked")
				Iterator<Element> iterator = node.elementIterator();  
                while(iterator.hasNext()){
                    Element e = iterator.next();  
                    ListNodes(e);  
                }  
    	  }
    }
    /**
     * 判断prepath是否已经被标记过
     * @param prepath   原始路径
     * @param pathlist  已整合的路径集合
     * @return
     */
    private boolean marked(String prepath,List<String> pathlist){
  		// 利用list的包含方法,进行判断
  	    if(pathlist.contains(prepath)){
  		      return true;
  		  }else{
  		      return false;
  		  }
  	}
	/**
	 * @return  原始节点路径集合
	 */
	private List<String> getPrepathlist(){
    	return prepathlist;
    }
	/**
	 * prepath 有文本数据的标记路径字符串数组
	 * @param list
	 */
	private void setPrepathandNumber(List<String> prepathlist){
    	this.number = prepathlist.size();
    	this.prepath = (String[])prepathlist.toArray(new String[number]);
    }
	private String[] getPrepath() {
		return prepath;
	}
//	private int getNumber(){
//		return number;
//	}
	/**
	 * @param prepath 
	 */
	private void setPathandCount(String[] prepath){
    	for(int i = 0;i<prepath.length;i++){
    		if(prepath[i].equals(null)){
    			System.out.println("prepath["+i+"]出现null值");
	    	    continue;   //跳出循环
	    	}else{
	    		if(marked(prepath[i],pathlist)){
	    			int tmp = pathlist.indexOf(prepath[i]);
	    			int n = countlist.get(tmp);
	    			countlist.set(tmp, n+1);
	    	    }else{
	    	    	pathlist.add(prepath[i]);
	    	    	countlist.add(1);
    	    	}
    	    }
    	}
    	this.num = pathlist.size();
    	this.path = (String[])pathlist.toArray(new String[num]);
    	this.count = new int[num];
    	for(int k=0;k<num;k++){
    		count[k] = countlist.get(k);
    	}
    }
//    private String[] getPath() {
//	    return path;
//    }
//	private int[] getCount(){
//		return count;
//	}
//	private int getNum(){
//		return num;
//	}
	/**
     * 修剪节点：1)剔除包含script的路径；2)剔除不包含body的路径；
     * 3)剔除文字内容与Title主题相关性很小的路径；4)
     * @param pathlist   //无重复路径的路径数组
     * @param countlist  //无重复路径的路径所对应的个数数组
     * @return
     */
    private void setTrimPathandTrimCount(List<String> pathlist,List<Integer> countlist){
    	List<Integer> list = new ArrayList<Integer>();
    	for(int i =0;i<pathlist.size();i++){
    		String str = pathlist.get(i);  //第i个索引处的值
    		if(str.contains("script")||!str.contains("body")){  //剔除包含script或不包含body标签的路径     
    			list.add(i); 
    		}
    	}
    	int[] tmp = new int[list.size()];
    	for(int j = 0;j<list.size();j++){
    		tmp[j] = list.get(j);
    	}
    	for(int k = 0;k<tmp.length;k++){
    		if(tmp[k]==0){
    			pathlist.remove(tmp[k]);
    			countlist.remove(tmp[k]);
    		}else{
    			pathlist.remove(tmp[k]-k);
    			countlist.remove(tmp[k]-k);
    		}
    	}
    	this.trimpath = (String[])pathlist.toArray(new String[pathlist.size()]);  //修剪节点后的路径数组
    	this.trimcount = new int[countlist.size()];   //修剪节点后的路径所对应个数int[]数组
    	for(int n=0;n<countlist.size();n++){
    		trimcount[n] = countlist.get(n);
    	}
    }
    public String[] getTrimpath(){
    	return trimpath;
    }
    public int[] getTrimcount(){
    	return trimcount;
    }
    /**
     * 获得按照trimcout排序后的sortpath数组
     * @param trimpath
     * @param trimcount
     * @return
     */
    private void setSortPathandSortCount(String[] trimpath,int[] trimcount){
		this.sortpath = new String[trimpath.length];    
		this.sortcount = new int[trimcount.length]; 
    	sortpath = SelectedSort.sort(trimpath,trimcount);  //将trimpath数组根据trimcount排序       
        sortcount = SelectedSort.sort(trimcount);     //将trimcount[]数组排序
    }
    public String[] getSortPath(){
    	return sortpath;
    }
    public int[] getSortCount(){
    	return sortcount;
    }
    private void setFrequentpathandFrequentcount(String[] sortpath, int[] sortcount){
    	List<String> frequentpathlist = new ArrayList<String>();
    	List<Integer> frequentcountlist = new ArrayList<Integer>();
    	//将int[]转入List集合
    	for(int m=0;m<sortcount.length;m++){
    		frequentcountlist.add(sortcount[m]);
    	}
    	for(int i=0;i<sortpath.length;i++){
    		if(sortcount[i]>5&&sortcount[i]<250){  //截取路径重复次数大于5小于50的路径
    			frequentpathlist.add(sortpath[i]);
    			frequentcountlist.add(sortcount[i]);
    		}
    	}
    	Collections.reverse(frequentpathlist);   //反转sortpathlist的顺序
    	Collections.reverse(frequentcountlist);
    	this.frequentpath = (String[])frequentpathlist.toArray(new String[frequentpathlist.size()]);
    	this.frequentcount = new int[frequentcountlist.size()];
    	for(int n = 0;n<frequentcountlist.size();n++){
    		frequentcount[n] = frequentcountlist.get(n);
    	}
    }
    /**
 	* @return  频繁标记路径frequentpath
 	*/
 	public String[] getfrequentpath(Document document){
 		readUnknowPage(document); 
 		return frequentpath;
 	}
 	
	public static void main(String[] args) throws DocumentException{
		 long begin = System.currentTimeMillis();
		 SAXReader reader = new SAXReader();
	     //读取xml文件，获取Document,构建DOM树
	     Document document = reader.read(new File("xml/detail-ifxmpnqf9614617.xml"));
	     String[] path = new DepthFirstTraversal().getfrequentpath(document);
//	     for(int i=0;i<path.length;i++){
//	    	 System.out.println(path[i]);
//	     }
	     String context =  new DepthFirstTraversal().getDetailPageContext(document);
//	     String context =  new LabelPath().getUnknowPageContext(document, path);
	     System.out.println(context);
		 long end = System.currentTimeMillis() - begin; 
		 System.out.println("耗时：" + end + "毫秒");
	}
}