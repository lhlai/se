package se.extractor.page;
/**
 * Text�����ڱ��洦������ҳ�Ĵ��ı�����
 * @author Administrator
 *
 */
public class Text {
	private String context = null;   //��ȡ�����ı�����
	
	public Text(){
		this.context = null;
	}	

	public String getContext() {
		return context;
	}

	public boolean setContext(String context) {	
		if(context!=""||context!=null){   //�ж��Ƿ����ı�����
			this.context = context;
			return true;
		}else{
			return false;
		}
	}	
}
