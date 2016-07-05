package se.extractor.main;

import java.io.IOException;

import se.extractor.pageHandler.IndexBuilderImpl;
import se.extractor.util.ProperConfig;

public class MyIndexBuilder {

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		//1. Build Index For Lucene
		System.out.println("==============Start building index===================");
		IndexBuilder("index.path","files.path");
		System.out.println("==============Index building is end==================");
		System.out.println("耗时4：" + (System.currentTimeMillis() - begin)/1000 + "秒");

	}
	
	/**
	 * Build Index for files text
	 * @param indexpath
	 * @param filespath
     */
	private static void IndexBuilder(String indexpath, String filespath){
		try{
			//实例化一个IndexBuilder,本质就是实例化一个lucene的IndexWriter,将其赋给IndexBuilder对象的IndexWriter属性
			IndexBuilderImpl index = new IndexBuilderImpl(ProperConfig.getPathValue(indexpath));
			//执行build
			index.build(ProperConfig.getPathValue(filespath));
			System.out.println("Index creating finished!");
		}catch(IOException ioe){
			System.out.println("Index creating failed!");
			ioe.printStackTrace();
		}
	}

}
