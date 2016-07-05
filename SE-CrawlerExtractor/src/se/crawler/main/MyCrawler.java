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
	private int threadcount = 10; //�߳�����
	private static double begin;
	
	 //ʹ�����ӳ�ʼ�� URL ����
	private void initCrawlerWithSeeds(String[] seeds)
	{
		for(int i=0;i<seeds.length;i++){
			LinkQueue.addUnvisitedUrl(seeds[i],1);
		}	
	}	
	//ץȡ����
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
			    System.out.println("�ܹ�����"+LinkQueue.getVisitedUrlNum()+"����ҳ");
			    System.out.println("�ܹ�������"+myTeaCrawler.getDownNum()+"����ҳ");
			    double timeConsume = System.currentTimeMillis() - begin; 
			    System.out.println("��ʱ��" + timeConsume/1000 + "��");
			    System.exit(1);  //status״̬��Ϊ��0ʱ��ʾ����������
			}
		}
	}
	//main �������
	public static void main(String[]args){
		begin = System.currentTimeMillis();
		MyCrawler crawler = new MyCrawler();
		System.out.println("��ʼ����.........................................");
		String[] seeds = {"http://www.tea.agri.cn/"};  //�й��㽶��
		crawler.crawling(seeds);
	}
}
