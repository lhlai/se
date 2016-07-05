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
		//实例化一个IndexWriter，该writer负责将数据写入path路径对应的文件
		writer = new IndexWriter(path,new MMAnalyzer());
	}
	/**
	 * 建立索引文件步骤：
	 * 1）从files文件中读取信息（files文件是Extract解析后的页面数据），利用BufferedReader(Reader in)进行读取
	 * 2）BufferedReader(Reader in)中的Reader是FileReader(File,file);
	 * 3)将数据先存入Field[]数组里面，再逐行添加到document里面，最后把document的信息添加到writer中
	 * @param path
	 * @throws IOException
	 */
	public void build(String path)throws IOException{
		BufferedReader reader = null;
		File[] files = new File(path).listFiles();
		for(int i=0;i<files.length;i++){
			System.out.println("Dealing with the "+(1+i)+" file");
			reader = new BufferedReader(new FileReader(files[i]));
			//定义一个Document对象
			Document doc =new Document();
			Field[]fields = new Field[5];
			//Store the original field value in the index
			fields[0] = new Field("id",String.valueOf(i),Field.Store.YES,Field.Index.NO);  
			//读取文本的第一行数据，将其与url对应
			fields[1] = new Field("url",reader.readLine(),Field.Store.YES,Field.Index.NO);
			fields[2] = new Field("title",reader.readLine(),Field.Store.YES,Field.Index.TOKENIZED);
			fields[3] = new Field("score",reader.readLine(),Field.Store.YES,Field.Index.NO);
			fields[4] = new Field("context",getBodyFile(files[i].getAbsolutePath(),reader),Field.Store.YES,
					Field.Index.TOKENIZED);
			//创建Document
			for(int j = 0;j<fields.length;j++){
				//将fields[]数组里面的数据一次添加到Document里面
				doc.add(fields[j]);
			}
			//将Document添加到IndexWriter中
			writer.addDocument(doc);
		}
		writer.optimize();
		writer.close();
		reader.close();
	}
	
	/**
	 * 获取文本内容的方法
	 * @param path
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public String getBodyFile(String path,BufferedReader reader)throws IOException{
		StringBuffer buffer = new StringBuffer();
		String line = reader.readLine(); //读取首行字符串
		while(line!=null){
			buffer.append(line);     //将指定的字符串追加到此字符序列
			line = reader.readLine();//读取一个文本行。通过下列字符之一即可认为某行已终止：
                                     //换行 ('\n')、回车 ('\r') 或回车后直接跟着换行。
		}
		return buffer.toString();
	}
}
