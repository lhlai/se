package se.a.notused;
import java.io.File;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

public class OperatingDB {
	//讲URL写入队列中
	public boolean writerURL(String fileName, String url,
			String databaseDBName, String rankPage) {
		boolean mark = false;
		// 配置环境 https://community.oracle.com/thread/996592?start=0&tstart=0 问题地址
		EnvironmentConfig envConfig = new EnvironmentConfig();

		// 设置配置事务
		envConfig.setTransactional(true);
		// 如果不存在就创建环境
		envConfig.setAllowCreate(true);
		File file = new File(fileName);
		file.mkdirs();
		try {
			Environment exampleEnv = new Environment(file, envConfig);

			Transaction txn = exampleEnv.beginTransaction(null, null);

			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setTransactional(true);
			dbConfig.setAllowCreate(true);
			dbConfig.setSortedDuplicates(false);
			Database exampleDb = exampleEnv.openDatabase(txn, databaseDBName,
					dbConfig);
			txn.commit();
			DatabaseEntry theKey = new DatabaseEntry(url.getBytes("utf-8"));
			DatabaseEntry theData = new DatabaseEntry(
					rankPage.getBytes("utf-8"));
			exampleDb.put(null, theKey, theData);
			exampleDb.close();
			exampleEnv.close();
		} catch (Exception e) {
			e.printStackTrace();
			mark = false;
		}

		return mark;
	}

	// 读取没有访问过的URL
	public String readerURL(String fileName, String databaseDBName) {
		// boolean mark = false;
		// 配置环境
		EnvironmentConfig envConfig = new EnvironmentConfig();
		// 设置配置事务
		envConfig.setTransactional(true);
		// 如果不存在就创建环境
		envConfig.setAllowCreate(true);
		File file = new File(fileName);
		String theKey = null;
		// file.mkdirs();
		try {

			Environment exampleEnv = new Environment(file, envConfig);
			// Transaction txn = exampleEnv.beginTransaction(null,null);

			DatabaseConfig dbConfig = new DatabaseConfig();

			dbConfig.setTransactional(true);
			dbConfig.setAllowCreate(true);
			dbConfig.setSortedDuplicates(false);

			Database myDB = exampleEnv.openDatabase(null, databaseDBName,
					dbConfig);
			// txn.commit();
			// txn = exampleEnv.beginTransaction(null,null);
			Cursor cursor = myDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundValue = new DatabaseEntry();
			// cursor.getPrev()与cursor.getNext()的区别：一个是从前往后读取，一个是从后往前读取
			// 这里讲访问遍历数据库全部数据while循环噶为if判断，则就只读取第一条数据
			if (cursor.getNext(foundKey, foundValue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				theKey = new String(foundKey.getData(), "UTF-8");
			}
			cursor.close();
			myDB.close();
			exampleEnv.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return theKey;
	}

	// 读取已经爬取过的URL
	public String readerUsedURL(String fileName, String databaseDBName,
			String url) {
		// 配置环境
		EnvironmentConfig envConfig = new EnvironmentConfig();
		// 设置配置事务
		envConfig.setTransactional(true);
		// 如果不存在就创建环境
		envConfig.setAllowCreate(true);
		File file = new File(fileName);
		String theKey = null;
		// file.mkdirs();
		try {
			Environment exampleEnv = new Environment(file, envConfig);
			Transaction txn = exampleEnv.beginTransaction(null, null);
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setTransactional(true);
			dbConfig.setAllowCreate(true);
			dbConfig.setSortedDuplicates(false);
			Database myDB = exampleEnv.openDatabase(txn, databaseDBName,
					dbConfig);
			txn.commit();
			Cursor cursor = myDB.openCursor(null, null);
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundValue = new DatabaseEntry();
			// cursor.getPrev()与cursor.getNext()的区别：一个是从前往后读取，一个是从后往前读取
			// 这里讲访问遍历数据库全部数据while循环噶为if判断，则就只读取第一条数据
			while (cursor.getNext(foundKey, foundValue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				theKey = new String(foundKey.getData(), "UTF-8");
				if (theKey.equals(url)) {
					return theKey;
				}
			}
			cursor.close();
			myDB.close();
			exampleEnv.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 删除已经读取过的URL
	public void deleteReadURL(String envHomePath, String databaseName,
			String key) {

		Environment mydbEnv = null;
		Database myDatabase = null;
		// 创建一个EnvironmentConfig配置对象
		EnvironmentConfig envCfg = new EnvironmentConfig();
		// 如果设置了true则表示当数据库环境不存在时候重新创建一个数据库环境，默认为false.
		envCfg.setAllowCreate(true);
		// 设置数据库缓存大小
		// envCfg.setCacheSize(1024 * 1024 * 20);
		// 事务支持,如果为true，则表示当前环境支持事务处理，默认为false，不支持事务处理。
		envCfg.setTransactional(true);
		try {
			mydbEnv = new Environment(new File(envHomePath), envCfg);
			DatabaseConfig dbCfg = new DatabaseConfig();
			// 如果数据库不存在则创建一个
			dbCfg.setAllowCreate(true);
			// 如果设置为true,则支持事务处理，默认是false，不支持事务
			dbCfg.setTransactional(true);
			myDatabase = mydbEnv.openDatabase(null, databaseName, dbCfg);
			DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));

			// 删除
			myDatabase.delete(null, keyEntry);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != myDatabase) {
				try {
					myDatabase.close();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}
			if (null != mydbEnv) {
				// 在关闭环境前清理下日志
				try {
					mydbEnv.cleanLog();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
				try {
					mydbEnv.close();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
				mydbEnv = null;
			}
		}
	}

	
	public static void main(String[] args) {
		OperatingDB odb = new OperatingDB();
		 odb.writerURL( "c:/data/","www.163.com","data","1");
		 String url = odb.readerURL("c:/data/", "data");
		 odb.writerURL( "c:/data/","www.baidu.com","data","2");	
		String url2 = odb.readerURL("c:/data/", "data");
		System.out.println(url+url2);
//		if(url != null){
//			odb.deleteReadURL("c:/data/","data",url);
//		}
//		else{/
//			System.out.println("url is null !!!");
//		}
	}
}