package gri.engine.integrate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import gir.engine.monitor.SendHelper;
import gri.engine.exception.FileNotFoundException;
import gri.engine.util.Constant;

public class FileUtil {
	public static void main(String[] args) 
	{
//		List list=findFilebyName("C:\\Users\\admin\\Desktop\\", "xingming");
//		System.out.println(list.size());
//		deleteFilebyName("C:\\Users\\admin\\Desktop\\", "1");
//		renameFilebyName("C:\\Users\\admin\\Desktop\\","1","2");
	}

	public static List<String> findFilebyName(String path,String destTable,String fileName)
	{		
		String filePath=path+"/"+destTable;
		File file=new File(filePath);
		File[] tempFile=file.listFiles();
		List<String> fileList=new ArrayList<String>();
		for(int i=0;i<tempFile.length;i++) 
		{
			if(tempFile[i].getName().startsWith(fileName+"_")) 
			{
				fileList.add(tempFile[i].getName());
			}
		}
		return fileList;
	}
	
	public static List<String> findFilebyName(String path,String fileName)
	{		
		File file=new File(path);
		File[] tempFile=file.listFiles();
		List<String> fileList=new ArrayList<String>();
		for(int i=0;i<tempFile.length;i++) 
		{
			if(tempFile[i].getName().startsWith(fileName+"_")) 
			{
				fileList.add(tempFile[i].getName());
			}
		}
		if(fileList.size()==0) {
			try {
				throw new FileNotFoundException("在指定路径上找不到相应文件",String.format("指定路径为:%s", path),String.format("指定的文件为:%s", fileName))  ;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SendHelper.sendErrorFile(path+"/"+fileName, e);
			}
		}
		return fileList;
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
	
	public static boolean deleteFilebyName(String path,String destTable,String fileName)
	{
		String filePath=path+"/"+destTable+"/";
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
	
	public static boolean renameFilebyName(String path,String destTable,String fileName,String newName)
	{
		String filePath=path+"/"+destTable+"/";
		File file=new File(filePath);
		File[] tempFile=file.listFiles();
		int a=1;
		for(int i=0;i<tempFile.length;i++)
		{
			if(tempFile[i].getName().startsWith(fileName+"_")) 
			{
				tempFile[i].renameTo(new File(Constant.ObjectFolder+"/"+destTable+"/"+newName+"_"+a+".txt"));
				a++;
			}
		}
		return true;
	}
	
//	public static void WriteMain(IntegrateMain main,String name)
//	{
//		try 
//		{
//			String filePath=Constant.propertiesFolder+name+"_1.txt";
//			FileOutputStream file=new FileOutputStream(filePath);
//			BufferedOutputStream br=new BufferedOutputStream(file);
//			ObjectOutputStream output=new ObjectOutputStream(br);
//			output.writeObject(main);
//			output.flush();
//			output.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	public static void WriteMainToJson(IntegrateMain main,String name) {
		FileWriter fw=null;
		BufferedWriter bw=null;
		try {
			String jsonStr=JSON.toJSONString(main);
			String filePath=Constant.propertiesFolder+name+"_1.txt";
			fw=new FileWriter(filePath);
			bw=new BufferedWriter(fw);
			bw.write(jsonStr);
			bw.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(bw!=null)
				{
					bw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static IntegrateMain ReadMainFromJson(String filePath) {
		IntegrateMain main=null;
		String jsonStr=null;
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(filePath));
			String line=null;
			while((line=br.readLine())!=null) {
				jsonStr=(jsonStr==null)?line:jsonStr+line;
			}
		}catch(Exception e) {
			e.printStackTrace();
			SendHelper.sendErrorFile(filePath, e);
		}finally {
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					SendHelper.sendErrorFile(filePath, e);
				}
			}
		}
		if(jsonStr!=null) {
			main=JSON.parseObject(jsonStr,IntegrateMain.class);
		}
		return main;
	}
	
	public static IntegrateMain ReadMain(String filePath)
	{
		IntegrateMain main=null;
		try 
		{
			BufferedInputStream br=new BufferedInputStream(new FileInputStream(filePath));
			ObjectInputStream input=new ObjectInputStream(br);
			main=(IntegrateMain)input.readObject();
			input.close();

		}
		catch(Exception e)
		{
		}
		return main;
	}
	
}
