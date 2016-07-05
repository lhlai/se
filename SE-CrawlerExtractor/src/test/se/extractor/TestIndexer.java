package test.se.extractor;

import java.io.IOException;

import se.extractor.pageHandler.IndexBuilderImpl;
import se.extractor.util.ProperConfig;

/**
 * ��������������
 * @author Administrator
 *
 */
public class TestIndexer {
	public static void main(String[] args) {
		try{
			//ʵ����һ��IndexBuilder,���ʾ���ʵ����һ��lucene��IndexWriter,���丳��IndexBuilder�����IndexWriter����
			IndexBuilderImpl index = new IndexBuilderImpl(ProperConfig.getPathValue("index.path"));
			//ִ��build
			index.build(ProperConfig.getPathValue("files.path"));
		}catch(IOException ioe){
			System.out.println("Index creating failed!");
			ioe.printStackTrace();
		}
		System.out.println("Index creating finished!");
	}
}
