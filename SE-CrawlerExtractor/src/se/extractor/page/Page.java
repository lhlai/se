package se.extractor.page;
/**
 * Page�����ڱ��洦������ҳ���������������
 * @author Administrator
 *
 */
public class Page {
	private String url = null;       //��ַ
	private String title =null;      //����
	private String summary = null;   //ժҪ��Ϣ��Ҳ���Ǳ�����ҳ���ݵ��ļ���
	private String context = null;   //����
	private double score = 10;       //��ҳ�÷�
	private String pageType = null;  //��ҳ����:����ҳ�桢��ϸҳ�桢δ֪ҳ�棿
	
	public Page(){
		url = null;
		title = null;
		summary = null;
		context = null;
		pageType = null;
		score = 10;
	}
    
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	public String getPageType(){
		return pageType;
	}
	public void setPageType(String pageType){
		this.pageType = pageType;
	}
}
