#Crawler&Extractor
爬虫，信息抽取系统

##功能简介
###1.定制爬虫
本爬虫为多线程垂直爬虫,目标网站是中国农业信息网，当然可通过urlseeds文件夹配置种子文件，并在源码几处稍作修改可定向抓取其他网站。

###2.信息抽取
采用两种方式：1)先利用Jtidy将html文件转xml，再利用Dom4j从xml中抽取结构化文本；
              2)利用Jsoup直接html/htm提取文本内容

##功能演示
有三大主类：MyCrawler.java,MyExtractor.java,TextPreprocess.java,分别负责抓取网页，抽取文本内容，文本预处理。
	
##附件说明
	html———— 保存爬虫爬取页面
	xml————  html转成xml的文件
	files———— xml抽取结构化文本得到的txt文件
	txt———— 保存从html抽取出的结构化文本
	PreProcessFile———— 把txt目录下文本做预处理（ps:分词）后得到的单个文件
	jtidyConfig———— jtidy配置文件
	urlseeds———— ip种子文件

