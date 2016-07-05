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
 * ������ȱ����㷨
 *     ���������DOM�����󣬴洢���·�����жϲ�����Ƶ��·������Ϣ
 * @author pillar
 * @version 1.0
 * @since 2015.12
 */
public class DepthFirstTraversal {
    private int number = 0;    //��¼prepath���ܱ������
	private int num = 0;       //��¼�ϲ����prepath����path���ܱ������
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
     * ��ȡ����ҳ����Ϣ
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
	 * ��ȡ��ϸҳ������
	 * @param document   dom4j�������ҳDOM��ģ��
	 * @return context	 ��ȡ�������ҳ�ı���Ϣ
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
	 * ��ȡδ֪ҳ����ı�����
	 * @param document	dom4j�������ҳDOM��ģ��
	 * @param path	���·��
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
//		Element root = document.getRootElement();  //��ȡ�����rootԪ�ض���,Element��Dom4j����
//		ListNodes(root);                           //����Document���,getprepathlist
//		setPrepathandNumber(getPrepathlist());
//		System.out.println("�����ֱ�ǩ�Ľڵ����Ϊ:"+number);
//		setPathandCount(getPrepath());
//		setTrimPathandTrimCount(pathlist,countlist);
//		setSortPathandSortCount(getTrimpath(),getTrimcount());
//		setFrequentpathandFrequentcount(sortpath,sortcount);
//	}
//	private void readDetailPage(Document document){
//		Element root = document.getRootElement();  //��ȡ�����rootԪ�ض���,Element��Dom4j����
//		ListNodes(root);                           //����Document���,getprepathlist
//		setPrepathandNumber(getPrepathlist());
//		setPathandCount(getPrepath());
//		setTrimPathandTrimCount(pathlist,countlist);
//		setSortPathandSortCount(getTrimpath(),getTrimcount());
//		setFrequentpathandFrequentcount(sortpath,sortcount);
//	}
	private void readUnknowPage(Document document){
		Element root = document.getRootElement();  //��ȡ�����rootԪ�ض���,Element��Dom4j����
		ListNodes(root);                           //����Document���,getprepathlist
		setPrepathandNumber(getPrepathlist());
		setPathandCount(getPrepath());
		setTrimPathandTrimCount(pathlist,countlist);
		setSortPathandSortCount(getTrimpath(),getTrimcount());
		setFrequentpathandFrequentcount(sortpath,sortcount);
	}
	/**
	 * �ݹ��ȡ��ϸҳ��Ľڵ�����
	 * @param node	Dom4j����Ľڵ�ģ��
	 * @return  NodeText   �ڵ��ı���Ϣ
	 */
	private void setNodeText(Element node){
//		ArrayList<String> NodeText = new ArrayList<String>();
  	    if(!node.getTextTrim().equals("")&&!node.getTextTrim().equals("|")){
  	    	if(node.getTextTrim()!=null&&node.getTextTrim().length()!=0){
  	    		String str = node.getPath();
	    		if(!str.contains("script")&&str.contains("body")){
	    			String tmp = node.getTextTrim();
	    			if(StringTest(tmp)){   //�ж��ַ������ݳ����Ƿ����Ҫ��
	    				if(ChineseRatio(tmp)>0.4){  //�����ַ����е�����ռ��
	    					NodeText.add(tmp);
	    				}
	    			}
  	    		    if(!node.isTextOnly()){ 	    		    	
  	    			    @SuppressWarnings("unchecked")
  					    Iterator<Element> iterator = node.elementIterator();  
  		  	    	    while(iterator.hasNext()){  
  		  	                Element e = iterator.next();  
  		  	                setNodeText(e);  	//�ݹ�ص�
  		  	            }
  	    		    }
  	    		}	
  	    	}
  	   }else{	  
  	      //ʹ�õݹ�  ,������ǰ�ڵ�����������ӽڵ�
          @SuppressWarnings("unchecked")
		  Iterator<Element> iterator = node.elementIterator();  
          while(iterator.hasNext()){
              Element e = iterator.next();  
              setNodeText(e);  
          }  
  	   }
   }
	
	//�ж��ı������Ƿ����Ҫ��
	private boolean StringTest(String tmp) {
		int number = tmp.length();
		if(number>10){    //�򵥵����ַ�����Ϊ�ж�����
			return true;
		}else{
			return false;
		}
	}
	//�ж��ַ������Ƿ�������
	public boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }else{
        	return false;
        }
    }
	//�ж��ַ��������ĵ�ռ��
	private double ChineseRatio(String str){
		double ratio = 0;
		double count=0;
		char[] c = str.toCharArray(); 
	    for(int i=0;i<c.length;i++){
	       String a = String.valueOf(c[i]);   //��char����ת��ΪStrign����
	       if(isContainChinese(a)){           //����������ʽ�ж��Ƿ�Ϊ�����ַ�
	    	   count++;
	       }
	    }
	    ratio = count/c.length;
		return ratio;
	}
	/**
     * ������ǰ�ڵ��µ��������ı����ݵĽڵ㣬����¼�Ӹ��ڵ㵽ĩ�˵Ľڵ�·���ݴ���prepathlist����,�ٴ���prepath����
     * @param node
     */
    private void ListNodes(Element node){
    	  //�����ǰ�ڵ����ݲ�Ϊ�գ������
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
    	        //ʹ�õݹ�  ,������ǰ�ڵ�����������ӽڵ�
                @SuppressWarnings("unchecked")
				Iterator<Element> iterator = node.elementIterator();  
                while(iterator.hasNext()){
                    Element e = iterator.next();  
                    ListNodes(e);  
                }  
    	  }
    }
    /**
     * �ж�prepath�Ƿ��Ѿ�����ǹ�
     * @param prepath   ԭʼ·��
     * @param pathlist  �����ϵ�·������
     * @return
     */
    private boolean marked(String prepath,List<String> pathlist){
  		// ����list�İ�������,�����ж�
  	    if(pathlist.contains(prepath)){
  		      return true;
  		  }else{
  		      return false;
  		  }
  	}
	/**
	 * @return  ԭʼ�ڵ�·������
	 */
	private List<String> getPrepathlist(){
    	return prepathlist;
    }
	/**
	 * prepath ���ı����ݵı��·���ַ�������
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
    			System.out.println("prepath["+i+"]����nullֵ");
	    	    continue;   //����ѭ��
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
     * �޼��ڵ㣺1)�޳�����script��·����2)�޳�������body��·����
     * 3)�޳�����������Title��������Ժ�С��·����4)
     * @param pathlist   //���ظ�·����·������
     * @param countlist  //���ظ�·����·������Ӧ�ĸ�������
     * @return
     */
    private void setTrimPathandTrimCount(List<String> pathlist,List<Integer> countlist){
    	List<Integer> list = new ArrayList<Integer>();
    	for(int i =0;i<pathlist.size();i++){
    		String str = pathlist.get(i);  //��i����������ֵ
    		if(str.contains("script")||!str.contains("body")){  //�޳�����script�򲻰���body��ǩ��·��     
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
    	this.trimpath = (String[])pathlist.toArray(new String[pathlist.size()]);  //�޼��ڵ���·������
    	this.trimcount = new int[countlist.size()];   //�޼��ڵ���·������Ӧ����int[]����
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
     * ��ð���trimcout������sortpath����
     * @param trimpath
     * @param trimcount
     * @return
     */
    private void setSortPathandSortCount(String[] trimpath,int[] trimcount){
		this.sortpath = new String[trimpath.length];    
		this.sortcount = new int[trimcount.length]; 
    	sortpath = SelectedSort.sort(trimpath,trimcount);  //��trimpath�������trimcount����       
        sortcount = SelectedSort.sort(trimcount);     //��trimcount[]��������
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
    	//��int[]ת��List����
    	for(int m=0;m<sortcount.length;m++){
    		frequentcountlist.add(sortcount[m]);
    	}
    	for(int i=0;i<sortpath.length;i++){
    		if(sortcount[i]>5&&sortcount[i]<250){  //��ȡ·���ظ���������5С��50��·��
    			frequentpathlist.add(sortpath[i]);
    			frequentcountlist.add(sortcount[i]);
    		}
    	}
    	Collections.reverse(frequentpathlist);   //��תsortpathlist��˳��
    	Collections.reverse(frequentcountlist);
    	this.frequentpath = (String[])frequentpathlist.toArray(new String[frequentpathlist.size()]);
    	this.frequentcount = new int[frequentcountlist.size()];
    	for(int n = 0;n<frequentcountlist.size();n++){
    		frequentcount[n] = frequentcountlist.get(n);
    	}
    }
    /**
 	* @return  Ƶ�����·��frequentpath
 	*/
 	public String[] getfrequentpath(Document document){
 		readUnknowPage(document); 
 		return frequentpath;
 	}
 	
	public static void main(String[] args) throws DocumentException{
		 long begin = System.currentTimeMillis();
		 SAXReader reader = new SAXReader();
	     //��ȡxml�ļ�����ȡDocument,����DOM��
	     Document document = reader.read(new File("xml/detail-ifxmpnqf9614617.xml"));
	     String[] path = new DepthFirstTraversal().getfrequentpath(document);
//	     for(int i=0;i<path.length;i++){
//	    	 System.out.println(path[i]);
//	     }
	     String context =  new DepthFirstTraversal().getDetailPageContext(document);
//	     String context =  new LabelPath().getUnknowPageContext(document, path);
	     System.out.println(context);
		 long end = System.currentTimeMillis() - begin; 
		 System.out.println("��ʱ��" + end + "����");
	}
}