package se.crawler.url.fontier;
/**
 * �й�����url������
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeaLinkFilter implements LinkFilter {

	@Override
	public boolean accept(String url) {
		if(url.startsWith("http://www.tea.agri.cn/")&&!url.contains("?")){//��ץȡ��̬��ҳ
			if(!url.contains("jhtml")){
				int num =0;
				int htmNum = 0;
				Pattern p = Pattern.compile("/");
				Pattern p2 = Pattern.compile("htm");
				Matcher m = p.matcher(url);
				Matcher m2 = p2.matcher(url);
				while(m.find()){
					num++;
				}
				while(m2.find()){
					htmNum++;
				}
				if(num>9||url.length()>70){  //
					return false;
				}else{
					if(htmNum>1){      //url����1������htm��Ϊ�ظ�url,ȥ��
						return false;
					}else{
						if(url.endsWith("htm")||url.endsWith("html")||url.endsWith("htm/")){
							return true;
						}else{
							return false;
						}
					}
				}
			}else{
				return false;
			}
       }else{
            return false;  
       }	
	}

}
