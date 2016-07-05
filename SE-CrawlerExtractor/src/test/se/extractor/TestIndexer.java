package test.se.extractor;

import java.io.IOException;

import se.extractor.pageHandler.IndexBuilderImpl;
import se.extractor.util.ProperConfig;

/**
 * 建立索引测试类
 * @author Administrator
 *
 */
public class TestIndexer {
	public static void main(String[] args) {
		try{
			//实例化一个IndexBuilder,本质就是实例化一个lucene的IndexWriter,将其赋给IndexBuilder对象的IndexWriter属性
			IndexBuilderImpl index = new IndexBuilderImpl(ProperConfig.getPathValue("index.path"));
			//执行build
			index.build(ProperConfig.getPathValue("files.path"));
		}catch(IOException ioe){
			System.out.println("Index creating failed!");
			ioe.printStackTrace();
		}
		System.out.println("Index creating finished!");
	}
}
