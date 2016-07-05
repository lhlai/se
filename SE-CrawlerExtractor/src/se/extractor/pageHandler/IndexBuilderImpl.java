package se.extractor.pageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import jeasy.analysis.MMAnalyzer;

public class IndexBuilderImpl implements IndexBuilder{
	IndexWriter writer;
	public IndexBuilderImpl(String path)throws IOException{
		//ʵ����һ��IndexWriter����writer��������д��path·����Ӧ���ļ�
		writer = new IndexWriter(path,new MMAnalyzer());
	}
	/**
	 * ���������ļ����裺
	 * 1����files�ļ��ж�ȡ��Ϣ��files�ļ���Extract�������ҳ�����ݣ�������BufferedReader(Reader in)���ж�ȡ
	 * 2��BufferedReader(Reader in)�е�Reader��FileReader(File,file);
	 * 3)�������ȴ���Field[]�������棬��������ӵ�document���棬����document����Ϣ��ӵ�writer��
	 * @param path
	 * @throws IOException
	 */
	public void build(String path)throws IOException{
		BufferedReader reader = null;
		File[] files = new File(path).listFiles();
		for(int i=0;i<files.length;i++){
			System.out.println("Dealing with the "+(1+i)+" file");
			reader = new BufferedReader(new FileReader(files[i]));
			//����һ��Document����
			Document doc =new Document();
			Field[]fields = new Field[5];
			//Store the original field value in the index
			fields[0] = new Field("id",String.valueOf(i),Field.Store.YES,Field.Index.NO);  
			//��ȡ�ı��ĵ�һ�����ݣ�������url��Ӧ
			fields[1] = new Field("url",reader.readLine(),Field.Store.YES,Field.Index.NO);
			fields[2] = new Field("title",reader.readLine(),Field.Store.YES,Field.Index.TOKENIZED);
			fields[3] = new Field("score",reader.readLine(),Field.Store.YES,Field.Index.NO);
			fields[4] = new Field("context",getBodyFile(files[i].getAbsolutePath(),reader),Field.Store.YES,
					Field.Index.TOKENIZED);
			//����Document
			for(int j = 0;j<fields.length;j++){
				//��fields[]�������������һ����ӵ�Document����
				doc.add(fields[j]);
			}
			//��Document��ӵ�IndexWriter��
			writer.addDocument(doc);
		}
		writer.optimize();
		writer.close();
		reader.close();
	}
	
	/**
	 * ��ȡ�ı����ݵķ���
	 * @param path
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public String getBodyFile(String path,BufferedReader reader)throws IOException{
		StringBuffer buffer = new StringBuffer();
		String line = reader.readLine(); //��ȡ�����ַ���
		while(line!=null){
			buffer.append(line);     //��ָ�����ַ���׷�ӵ����ַ�����
			line = reader.readLine();//��ȡһ���ı��С�ͨ�������ַ�֮һ������Ϊĳ������ֹ��
                                     //���� ('\n')���س� ('\r') ��س���ֱ�Ӹ��Ż��С�
		}
		return buffer.toString();
	}
}
