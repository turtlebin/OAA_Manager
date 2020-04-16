package gri.engine.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import gri.engine.integrate.Record;
import gri.engine.util.Constant;

public class FileTest {

	public FileTest() {
		// TODO Auto-generated constructor stub
	}
	public static void WriteRecordList(String destTableName,String dataSource,int i)
	{
		try 
		{
			File directory=new File(Constant.ObjectFolder+"/"+destTableName);
			if(!directory.exists()) {
				directory.mkdirs();
			}
			String filePath=Constant.ObjectFolder+"/"+destTableName+"/"+dataSource+"_"+i+".txt";
			FileOutputStream file=new FileOutputStream(filePath);
			BufferedOutputStream br=new BufferedOutputStream(file);
			ObjectOutputStream output=new ObjectOutputStream(br);
			//output.writeObject(records);
			output.flush();
			output.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static boolean deleteFilebyName(String path,String destTable,String fileName)
	{
		String filePath=path+"/"+destTable;
		File file=new File(filePath);
		File[] tempFile=file.listFiles();
		for(int i=0;i<tempFile.length;i++)
		{
			if(tempFile[i].getName().startsWith(fileName+"_")) 
			{
				tempFile[i].delete();
			}
		}
		return true;
	}
	
	public static boolean clearAllFiles(String path,String destTable) {
		String filePath=path+"/"+destTable+"/";
		File file=new File(filePath);
		if(!file.exists()) {
			return true;
		}
		File[] tempFile=file.listFiles();
		for(int i=0;i<tempFile.length;i++)
		{
			tempFile[i].delete();
		}
		return true;
	}
	
	public static void main(String[] args) {
//		WriteRecordList("测试","123",1);
//		deleteFilebyName(Constant.ObjectFolder,"测试","123");
		clearAllFiles(Constant.ObjectFolder,"测试");
	}
}
