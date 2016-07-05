package se.crawler.main;

import se.crawler.framework.CrawlerBanana;
import se.crawler.framework.CrawlerPotato;
import se.crawler.framework.CrawlerRice;
import se.crawler.framework.CrawlerTea;
import se.crawler.url.queue.LinkQueue;
/**
 * 
 * @author pillar
 * @version 1.0
 */
public class MyCrawler {
	private int threadcount = 10; //线程数量
	private static double begin;
	
	 //使用种子初始化 URL 队列
	private void initCrawlerWithSeeds(String[] seeds)
	{
		for(int i=0;i<seeds.length;i++){
			LinkQueue.addUnvisitedUrl(seeds[i],1);
		}	
	}	
	//抓取过程
	public void crawling(String[] seeds){   
		initCrawlerWithSeeds(seeds);
//		CrawlerBanana myBananaCrawler = new CrawlerBanana();
//		CrawlerRice  myRiceCrawler = new CrawlerRice();
		CrawlerTea myTeaCrawler = new CrawlerTea();
//		CrawlerPotato myPotatoCrawler = new CrawlerPotato();
		for(int i=0;i<threadcount;i++){  
			new Thread(myTeaCrawler,"thread-"+i).start();
		}

		while(true){
			if(LinkQueue.unVisitedUrlsEmpty()&&Thread.activeCount()== 1||myTeaCrawler.getCount()==threadcount){	
			    System.out.println("总共爬了"+LinkQueue.getVisitedUrlNum()+"个网页");
			    System.out.println("总共下载了"+myTeaCrawler.getDownNum()+"个网页");
			    double timeConsume = System.currentTimeMillis() - begin; 
			    System.out.println("耗时：" + timeConsume/1000 + "秒");
			    System.exit(1);  //status状态码为非0时表示非正常结束
			}
		}
	}
	//main 方法入口
	public static void main(String[]args){
		begin = System.currentTimeMillis();
		MyCrawler crawler = new MyCrawler();
		System.out.println("开始爬虫.........................................");
		String[] seeds = {"http://www.tea.agri.cn/"};  //中国香蕉网
		crawler.crawling(seeds);
	}
}
