package se.crawler.url.fontier;
/**
 * 中国茶网url过滤器
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeaLinkFilter implements LinkFilter {

	@Override
	public boolean accept(String url) {
		if(url.startsWith("http://www.tea.agri.cn/")&&!url.contains("?")){//不抓取动态网页
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
					if(htmNum>1){      //url中有1个以上htm的为重复url,去掉
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
