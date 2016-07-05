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
		System.out.println("��ʱ4��" + (System.currentTimeMillis() - begin)/1000 + "��");

	}
	
	/**
	 * Build Index for files text
	 * @param indexpath
	 * @param filespath
     */
	private static void IndexBuilder(String indexpath, String filespath){
		try{
			//ʵ����һ��IndexBuilder,���ʾ���ʵ����һ��lucene��IndexWriter,���丳��IndexBuilder�����IndexWriter����
			IndexBuilderImpl index = new IndexBuilderImpl(ProperConfig.getPathValue(indexpath));
			//ִ��build
			index.build(ProperConfig.getPathValue(filespath));
			System.out.println("Index creating finished!");
		}catch(IOException ioe){
			System.out.println("Index creating failed!");
			ioe.printStackTrace();
		}
	}

}
