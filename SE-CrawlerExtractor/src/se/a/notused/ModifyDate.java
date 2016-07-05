package se.a.notused;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
/**
 * 网页更新时间
 * @author Administrator
 */
public class ModifyDate {

	    public static void main(String[] args) throws Exception {
//	        URL u = new URL("http://www.steelccl.com/A/info_jghz.asp");
	        URL u = new URL("http://www.sina.com.cn/");
	        HttpURLConnection http = (HttpURLConnection) u.openConnection();
	        http.setRequestMethod("HEAD");
	        Date date=new Date(http.getLastModified());
	        System.out.println(u + " 更新时间 " + date);
	    }
}

