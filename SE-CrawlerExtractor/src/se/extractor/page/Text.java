package se.extractor.page;
/**
 * Text类用于保存处理后的网页的纯文本数据
 * @author Administrator
 *
 */
public class Text {
	private String context = null;   //抽取出的文本内容
	
	public Text(){
		this.context = null;
	}	

	public String getContext() {
		return context;
	}

	public boolean setContext(String context) {	
		if(context!=""||context!=null){   //判断是否有文本数据
			this.context = context;
			return true;
		}else{
			return false;
		}
	}	
}
