# -*- coding:utf-8 -*-
/**
爬虫，信息抽取系统
@author pillar lai
@since  30 Jun 2016
*/

1. 定制爬虫
   本系统为定制爬虫,目标网站是中国农业信息网(http://www.agri.cn/)

2. 系统运行
    可直接导入eclipse等IDE工具

3. 功能演示
    有三大主类：MyCrawler.java,MyExtractor.java,TextPreprocess.java,运行即可
4. 附件说明
    html   保存爬虫爬取页面
	xml    html转成xml的文件
	files  xml抽取结构化文本得到的txt文件
	txt    保存从html抽取出的结构化文本
	PreProcessFile  把txt目录下文本做预处理（ps:分词）后得到的单个文件
	jtidyConfig     jtidy配置文件
	urlseeds        ip种子文件

4.后续补充说明