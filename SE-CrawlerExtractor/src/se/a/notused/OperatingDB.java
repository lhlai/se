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
	//��URLд�������
	public boolean writerURL(String fileName, String url,
			String databaseDBName, String rankPage) {
		boolean mark = false;
		// ���û��� https://community.oracle.com/thread/996592?start=0&tstart=0 �����ַ
		EnvironmentConfig envConfig = new EnvironmentConfig();

		// ������������
		envConfig.setTransactional(true);
		// ��������ھʹ�������
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

	// ��ȡû�з��ʹ���URL
	public String readerURL(String fileName, String databaseDBName) {
		// boolean mark = false;
		// ���û���
		EnvironmentConfig envConfig = new EnvironmentConfig();
		// ������������
		envConfig.setTransactional(true);
		// ��������ھʹ�������
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
			// cursor.getPrev()��cursor.getNext()������һ���Ǵ�ǰ�����ȡ��һ���ǴӺ���ǰ��ȡ
			// ���ｲ���ʱ������ݿ�ȫ������whileѭ����Ϊif�жϣ����ֻ��ȡ��һ������
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

	// ��ȡ�Ѿ���ȡ����URL
	public String readerUsedURL(String fileName, String databaseDBName,
			String url) {
		// ���û���
		EnvironmentConfig envConfig = new EnvironmentConfig();
		// ������������
		envConfig.setTransactional(true);
		// ��������ھʹ�������
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
			// cursor.getPrev()��cursor.getNext()������һ���Ǵ�ǰ�����ȡ��һ���ǴӺ���ǰ��ȡ
			// ���ｲ���ʱ������ݿ�ȫ������whileѭ����Ϊif�жϣ����ֻ��ȡ��һ������
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

	// ɾ���Ѿ���ȡ����URL
	public void deleteReadURL(String envHomePath, String databaseName,
			String key) {

		Environment mydbEnv = null;
		Database myDatabase = null;
		// ����һ��EnvironmentConfig���ö���
		EnvironmentConfig envCfg = new EnvironmentConfig();
		// ���������true���ʾ�����ݿ⻷��������ʱ�����´���һ�����ݿ⻷����Ĭ��Ϊfalse.
		envCfg.setAllowCreate(true);
		// �������ݿ⻺���С
		// envCfg.setCacheSize(1024 * 1024 * 20);
		// ����֧��,���Ϊtrue�����ʾ��ǰ����֧��������Ĭ��Ϊfalse����֧��������
		envCfg.setTransactional(true);
		try {
			mydbEnv = new Environment(new File(envHomePath), envCfg);
			DatabaseConfig dbCfg = new DatabaseConfig();
			// ������ݿⲻ�����򴴽�һ��
			dbCfg.setAllowCreate(true);
			// �������Ϊtrue,��֧��������Ĭ����false����֧������
			dbCfg.setTransactional(true);
			myDatabase = mydbEnv.openDatabase(null, databaseName, dbCfg);
			DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));

			// ɾ��
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
				// �ڹرջ���ǰ��������־
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