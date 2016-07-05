package se.extractor.page;
/**
 * Page类用于保存处理后的网页，并被其他类调用
 * @author Administrator
 *
 */
public class Page {
	private String url = null;       //地址
	private String title =null;      //主题
	private String summary = null;   //摘要信息，也即是保存网页内容的文件名
	private String context = null;   //内容
	private double score = 10;       //网页得分
	private String pageType = null;  //网页类型:导航页面、详细页面、未知页面？
	
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
