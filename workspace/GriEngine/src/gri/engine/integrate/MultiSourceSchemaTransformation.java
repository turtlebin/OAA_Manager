package gri.engine.integrate;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import gir.engine.monitor.SendHelper;
import gri.engine.dest.Pair;
import gri.engine.exception.RecordsNullException;
import gri.engine.reflect.MethodMapper;
import gri.engine.reflect.MethodReflection;
import gri.engine.util.Constant;



public class MultiSourceSchemaTransformation {
	public static int process=0;
	public static int size=-1;
	public static int getProcess() {
		if(size>0) {
			return (process*100)/size;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * 对属性值joinedProperties进行外连接，等价于SQL的自然连接
	 * @param records1
	 * @param records2
	 * @param joinedProperties：要连接的属性组
	 * @return
	 */
	public static List<Record> outterJoinNatureOnProperties(List<Record> records1,List<Record> records2, List<? extends FieldPair<String, String>> joinedProperties){
		List<Record> results=new ArrayList<Record>();
		for(Record record1:records1){			
			for(Record record2:records2){
				//如果record1和record2的所有连接属性的值相等，可以进行连接
				if(ifJoined(record1, record2, joinedProperties)){
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);//先把record1的字段加入
					joinedRecord=addNonRepetitiveProperties(joinedRecord, record2, joinedProperties);//再把record2中的非连接字段加入
					results.add(joinedRecord);
				}
			}
			
		}
		return results;
	}
	
	public static List<Record> outterJoinNatureOnProperties(List<Record> records1, String dataSource1,List<Record> records2, List<? extends FieldPair<String, String>> joinedProperties,boolean joinPairInverse){
		List<Record> results=new ArrayList<Record>();
		for(Record record1:records1){			
			for(Record record2:records2){
				//如果record1和record2的所有连接属性的值相等，可以进行连接
				if(ifJoined(record1,dataSource1,record2, joinedProperties,joinPairInverse)){
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);//先把record1的字段加入
					joinedRecord=addNonRepetitiveProperties(joinedRecord, record2, joinedProperties);//再把record2中的非连接字段加入
					results.add(joinedRecord);
				}
			}
			
		}
		return results;
	}

	
	/**
	 * 将r2中非连接字段属性添加到r1记录中，等价于自然连接
	 * @param r1
	 * @param r2
	 * @param joinedProperties
	 * @return
	 */
	private static Record addNonRepetitiveProperties(Record r1, Record r2,List<? extends FieldPair<String, String>> joinedProperties){
		for(ColumnValue columnvalue:r2.getColumns()){
			for(FieldPair<String, String> field:joinedProperties){
				if(!columnvalue.getColumnMetaData().getColumnName().equalsIgnoreCase(field.getSecond())){
					//如果不在连接属性中，则添加r2中的该属性到r1中
					r1.getColumns().add(r2.getColumnValueByName(columnvalue.getColumnMetaData().getColumnName()));
					break;
				}
			}			
		}		
		return r1;
	}
	
	
	/**
	 * 将r2中非连接字段属性添加到r1记录中，进行的是等值连接，即r2中的连接属性也添加进去
	 * @param records1
	 * @param records2
	 * @param joinedProperties
	 * @return
	 */
	public static List<Record> outterJoinEqualOnProperties(List<Record> records1, List<Record> records2, List<? extends FieldPair<String, String>> joinedProperties){
		HashMap<String,ArrayList<Record>> map=createIndex(records2,joinedProperties,false);
		//WriteMap(map);
		String joinedInfo=false?joinedProperties.get(0).getSecond():joinedProperties.get(0).getFirst();
		List<Record> results=new ArrayList<Record>();
		for(Record record1:records1)
		{	
			String key=record1.getValueByColumnName(joinedInfo);
			if(key=="null")
			{
				key=null;
			}
			List<Record> tempList=map.get(key);
			if(tempList==null)
			{
				Record joinedRecord=new Record();
				joinedRecord=addAllProperties(joinedRecord,record1);
				joinedRecord=addNullProperties(joinedRecord,records2.get(0));
				results.add(joinedRecord);
				continue;
			}
			else
			{
				boolean joined=false;//设置一个布尔值，标记在多值连接的情况下是否有连接成功的数据，若没有，则records2只输出null
			    for(Record record:tempList)
				{
					if(ifJoined(record1,record, joinedProperties)){
						joined=true;
						Record joinedRecord=new Record();
						joinedRecord=addAllProperties(joinedRecord,record1);//先把record1的字段加入
						joinedRecord=addAllProperties(joinedRecord, record);//再把record2中的非连接字段加入
						results.add(joinedRecord);
					}
				}
				if(!joined) {
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);
					joinedRecord=addNullProperties(joinedRecord,records2.get(0));
					results.add(joinedRecord);
				}
			}
		}
		return results;
	}
	
	public static List<Record> outterJoinEqualOnProperties(List<Record> records1, List<Record> records2,
			List<? extends FieldPair<String, String>> joinedProperties,int fileIndex,int fileSize){
		HashMap<String,ArrayList<Record>> map=createIndex(records2,joinedProperties,false);
		//WriteMap(map);
		String joinedInfo=false?joinedProperties.get(0).getSecond():joinedProperties.get(0).getFirst();
		List<Record> results=new ArrayList<Record>();
		for(Record record1:records1)
		{	
			String key=record1.getValueByColumnName(joinedInfo);
			if(key=="null")
			{
				key=null;
			}
			List<Record> tempList=map.get(key);
			if(tempList==null)
			{
				if((!record1.isJoined())&&(fileIndex==fileSize)) {
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);
					joinedRecord=addNullProperties(joinedRecord,records2.get(0));
					results.add(joinedRecord);
				}
			}
			else
			{
			    for(Record record:tempList)
				{
					if(ifJoined(record1,record, joinedProperties)){
						record1.setJoined(true);
						Record joinedRecord=new Record();
						joinedRecord=addAllProperties(joinedRecord,record1);//先把record1的字段加入
						joinedRecord=addAllProperties(joinedRecord, record);//再把record2中的非连接字段加入
						results.add(joinedRecord);
					}
				}
				if((!record1.isJoined())&&(fileIndex==fileSize)) {
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);
					joinedRecord=addNullProperties(joinedRecord,records2.get(0));
					results.add(joinedRecord);
				}
			}
		}
		return results;
	}
	
	public static List<Record> outterJoinEqualOnProperties(List<Record> records1, String dataSource1,
			List<Record> records2, List<? extends FieldPair<String, String>> joinedProperties,
			boolean joinPairInverse)
	{
		HashMap<String, ArrayList<Record>> map = createIndex(records2, joinedProperties, joinPairInverse);
		String joinedInfo = joinPairInverse ? joinedProperties.get(0).getSecond() : joinedProperties.get(0).getFirst();
		List<Record> results = new ArrayList<Record>();
		for (Record record1 : records1) 
		{
			String key=record1.getValueByColumnNameAndDataSource(joinedInfo, dataSource1);
			if(key=="null")//hashMap中key可以为null
			{
				key=null;
			}
			List<Record> tempList = map.get(key);
			if (tempList == null) 
			{
				Record joinedRecord=new Record();
				joinedRecord=addAllProperties(joinedRecord,record1);
				joinedRecord=addNullProperties(joinedRecord,records2.get(0));
				results.add(joinedRecord);
				continue;
			}
			else
			{
				boolean joined=false;//设置一个布尔值，标记在多值连接的情况下是否有连接成功的数据，若没有，则records2只输出null
				for (Record record : tempList)
				{
					if (ifJoined(record1, dataSource1, record, joinedProperties, joinPairInverse)) 
					{
						joined=true;
						Record joinedRecord = new Record();
						joinedRecord = addAllProperties(joinedRecord, record1);// 先把record1的字段加入
						joinedRecord = addAllProperties(joinedRecord, record);// 再把record2中的非连接字段加入
						results.add(joinedRecord);
					}
				}
				if(!joined) {
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);
					joinedRecord=addNullProperties(joinedRecord,records2.get(0));
					results.add(joinedRecord);
				}
			}
		}
		return results;
	}
	
	public static List<Record> outterJoinEqualOnProperties(List<Record> records1, String dataSource1,
			List<Record> records2, List<? extends FieldPair<String, String>> joinedProperties,
			boolean joinPairInverse,int fileIndex,int fileSize)
	{
		HashMap<String, ArrayList<Record>> map = createIndex(records2, joinedProperties, joinPairInverse);
		String joinedInfo = joinPairInverse ? joinedProperties.get(0).getSecond() : joinedProperties.get(0).getFirst();
		List<Record> results = new ArrayList<Record>();
		for(Record record1:records1)
		{	
			String key=record1.getValueByColumnName(joinedInfo);
			if(key=="null")
			{
				key=null;
			}
			List<Record> tempList=map.get(key);
			if(tempList==null)
			{
				if((!record1.isJoined())&&(fileIndex==fileSize)) {
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);
					joinedRecord=addNullProperties(joinedRecord,records2.get(0));
					results.add(joinedRecord);
				}
			}
			else
			{
			    for(Record record:tempList)
				{
					if(ifJoined(record1,record, joinedProperties)){
						record1.setJoined(true);
						Record joinedRecord=new Record();
						joinedRecord=addAllProperties(joinedRecord,record1);//先把record1的字段加入
						joinedRecord=addAllProperties(joinedRecord, record);//再把record2中的非连接字段加入
						results.add(joinedRecord);
					}
				}
				if((!record1.isJoined())&&(fileIndex==fileSize)) {
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);
					joinedRecord=addNullProperties(joinedRecord,records2.get(0));
					results.add(joinedRecord);
				}
			}
		}
		return results;
	}
	
//	public static List<Record> outterJoinEqualOnProperties2(List<Record> records1, String dataSource1,List<Record> records2, List<? extends FieldPair<String, String>> joinedProperties,boolean joinPairInverse){
//		Map<String,ArrayList<Record>> map=createIndex(records2,joinedProperties,joinPairInverse);
//		String joinedInfo=joinPairInverse?joinedProperties.get(0).getSecond():joinedProperties.get(0).getFirst();
//		List<Record> results=new ArrayList<Record>();
//		for(Record record1:records1)
//		{	
//			for(Record record:map.get(record1.getValueByColumnName(joinedInfo)))
//			{
//				if(ifJoined(record1,dataSource1,record, joinedProperties,joinPairInverse)){
//					Record joinedRecord=new Record();
//					joinedRecord=addAllProperties(joinedRecord,record1);//先把record1的字段加入
//					joinedRecord=addAllProperties(joinedRecord, record);//再把record2中的非连接字段加入
//					results.add(joinedRecord);
//				}
//			}
//			
//		}
//		return results;
//	}
	
	private static HashMap<String,ArrayList<Record>> createIndex(List<Record> records,List<? extends FieldPair<String,String>> list,boolean JoinPairInverse)
	{
		HashMap<String,ArrayList<Record>> map=new HashMap<String,ArrayList<Record>>();
		Pair<String,String> joinPair=(Pair<String, String>) list.get(0);
		String joinedInfo=JoinPairInverse?joinPair.getFirst():joinPair.getSecond();
		for(Record record:records)
		{
			String joinData=record.getValueByColumnName(joinedInfo);
			if(joinData=="null")
			{
				joinData=null;
			}
			if(!map.containsKey(joinData))
			{
				ArrayList<Record> recordList=new ArrayList<Record>();
				recordList.add(record);
				map.put(joinData, recordList);
			}
			else
			{
				map.get(joinData).add(record);
			}
		}
		return map;
	}
	
	private static void WriteMap(HashMap map)
	{
		long start=System.currentTimeMillis();
		try
		{
			String filePath="C:\\Users\\admin\\Desktop\\test.txt";
			FileOutputStream file=new FileOutputStream(filePath);
			BufferedOutputStream br=new BufferedOutputStream(file);
			ObjectOutputStream output=new ObjectOutputStream(br);
			output.writeObject(map);
			output.flush();
			output.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		long end =System.currentTimeMillis();
		System.out.println("序列化花费的时间为："+(end-start));
	}
	
	public static void WriteRecordList(List<Record> records,String destTableName,String dataSource,int i)
	{
		Kryo kryo=KryoUtil.getInstance();
		try {
			File directory=new File(Constant.ObjectFolder+"/"+destTableName);
			if(!directory.exists()) {
				directory.mkdirs();
			}
			String filePath=Constant.ObjectFolder+"/"+destTableName+"/"+dataSource+"_"+i+".txt";
			FileOutputStream file=new FileOutputStream(filePath);
			BufferedOutputStream br=new BufferedOutputStream(file);
			Output output=new Output(br);
			kryo.writeObject(output, records);
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
			SendHelper.sendErrorFile(Constant.ObjectFolder+"/"+destTableName, e);
		}
//		try 
//		{
//			File directory=new File(Constant.ObjectFolder+"/"+destTableName);
//			if(!directory.exists()) {
//				directory.mkdirs();
//			}
//			String filePath=Constant.ObjectFolder+"/"+destTableName+"/"+dataSource+"_"+i+".txt";
//			FileOutputStream file=new FileOutputStream(filePath);
//			BufferedOutputStream br=new BufferedOutputStream(file);
//			ObjectOutputStream output=new ObjectOutputStream(br);
//			output.writeObject(records);
//			output.flush();
//			output.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	
//	public static void WriteRecordList(List<Record> records,String dataSource,int i)
//	{
//		try 
//		{
//			String filePath=Constant.ObjectFolder+dataSource+"_"+i+".txt";
//			FileOutputStream file=new FileOutputStream(filePath);
//			BufferedOutputStream br=new BufferedOutputStream(file);
//			ObjectOutputStream output=new ObjectOutputStream(br);
//			output.writeObject(records);
//			output.flush();
//			output.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	
	public static List<Record> ReadRecordList(String filePath)
	{
		Kryo kryo=KryoUtil.getInstance();
		List<Record> records = null;
		BufferedInputStream br=null;
		Input input=null;
		try 
		{
			br=new BufferedInputStream(new FileInputStream(filePath));
//			ObjectInputStream input=new ObjectInputStream(br);
			input=new Input(br);
			records=kryo.readObject(input, ArrayList.class);
//			input.close();
//			records=(List<Record>)input.readObject();
//			input.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			SendHelper.sendErrorFile(filePath, e);
		}
		finally {
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(input!=null)
			{
				input.close();
			}
			
		}
		if(records==null) {
			try {
				throw new RecordsNullException("文件读取错误,凡序列化后records为空",filePath);
			} catch (RecordsNullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SendHelper.sendErrorFile(filePath, e);
			}
		}
		return records;
	}
	
	/**
	 * 添加所有字段，为等值连接
	 * @param r1
	 * @param r2
	 * @param joinedProperties
	 * @return
	 */
	private static Record addAllProperties(Record r1, Record r2){
		for(ColumnValue columnvalue:r2.getColumns()){
			r1.getColumns().add(columnvalue);//无条件全部添加					
		}		
		return r1;
	}
	
	private static Record addNullProperties(Record r1,Record r2) {
		for(ColumnValue columnValue:r2.columns) {
			ColumnValue newColumn=new ColumnValue();
			newColumn.setDataSource(columnValue.getDataSource());
			newColumn.setColumnMetaData(columnValue.getColumnMetaData());
			newColumn.setValue(null);
			r1.getColumns().add(newColumn);
		}
		return r1;
	}
	
	/**
	 * 进行左连接，连接字段重复出现，records1中没有的字段置空
	 * @param records1
	 * @param records2
	 * @param joinedProperties
	 * @return
	 */
	public static List<Record> leftOutterJoinOnProperties(List<Record> records1, List<Record> records2, List<FieldPair<String, String>> joinedProperties){
		List<Record> results=new ArrayList<Record>();
		for(Record record1:records1){
			//是否进行左连接标识，如果records2中存在某个记录已经与record1发生过连接，置为false。如果始终为true，则该record1中对应于record2的字段置空
			boolean flag=true;
			for(Record record2:records2){
				//如果record1和record2的所有连接属性的值相等，可以进行连接
				if(ifJoined(record1, record2, joinedProperties)){
					Record joinedRecord=new Record();
					joinedRecord=addAllProperties(joinedRecord,record1);//先把record1的字段加入
					joinedRecord=addAllProperties(joinedRecord, record2);//再把record2中的所有字段加入
					results.add(joinedRecord);	
					flag=false;//已经经过连接
				}
			}
			if(flag) {//循环一圈后没有在records2中找到与r1可以连接的记录，则进行左连接
				Record joinedRecord=new Record();
				joinedRecord=addAllProperties(joinedRecord,record1);//先把record1的字段加入
				joinedRecord=addNullProperties(joinedRecord, records2);//再把record2中的所有字段加入
				results.add(joinedRecord);
			}
		}
		return results;
	}
	
	/**
	 * 將r1中属于r2的所有字段置空，实现左连接或右连接
	 * @param r1
	 * @param records2
	 * @return
	 */
	private static Record addNullProperties(Record r1, List<Record> records2){
		List<ColumnMeta> columnsMeta=records2.get(0).getColumnsMeta();
		for(ColumnMeta column:columnsMeta){//创建一个记录
			ColumnValue oneColumnValue=new ColumnValue();
			oneColumnValue.setColumnMetaData(column);
			//设置当前记录中字段column的值
			oneColumnValue.setValue(null);
			r1.columns.add(oneColumnValue);				
		}	
		return r1;
	}
	
	/**
	 * 右连接
	 * @param records1
	 * @param records2
	 * @param joinedProperties
	 * @return
	 */
	
	public static List<Record> recordsFilter(List<Record> records,Pair<String,String> preparedJoinPair,List<Pair<String,String>> joinList)
	{
		Iterator<Record> it=records.iterator();
		while(it.hasNext())
		{
			if(!filter(it.next(),preparedJoinPair,joinList))//在同一个结果集中进行筛选，不符合的record删除掉
			{
				it.remove();
			}
		}
		return records;
	}
	
	public static boolean recordsFilter2(String fileName,Pair<String,String> preparedJoinPair,List<Pair<String,String>> joinList,String destTable)
	{
		int a=1;
		List<String> fileList=FileUtil.findFilebyName(Constant.ObjectFolder,destTable, fileName);
		for(int i=1;i<=fileList.size();i++)
		{
		List<Record> records=(List<Record>)ReadRecordList(Constant.ObjectFolder+fileName+"_"+i+".txt");
		Iterator<Record> it=records.iterator();
		while(it.hasNext())
		{
			if(!filter(it.next(),preparedJoinPair,joinList))//在同一个结果集中进行筛选，不符合的record删除掉
			{
				it.remove();
			}
		}
		if(records.size()>0)
		{
			WriteRecordList(records,destTable,"temp",a);
			records.clear();
			a++;
		}
		}
		return true;
	}
	
	public static boolean filter(Record record,Pair<String,String> preparedJoinPair,List<Pair<String,String>> joinList)
	{
		for(Pair<String,String> pair:joinList)
		{
//			if(record.getValueByColumnNameAndDataSource(pair.getFirst(), preparedJoinPair.getFirst())==null&&
//					record.getValueByColumnNameAndDataSource(pair.getFirst(), preparedJoinPair.getFirst())==null)
//			{
//				continue;//均为空时返回true
//			}
//			else if(!(record.getValueByColumnNameAndDataSource(pair.getFirst(), preparedJoinPair.getFirst())==null||
//					record.getValueByColumnNameAndDataSource(pair.getFirst(), preparedJoinPair.getFirst())==null))
//			{
				if(!(record.getValueByColumnNameAndDataSource(pair.getFirst(), preparedJoinPair.getFirst()).equalsIgnoreCase(record.getValueByColumnNameAndDataSource(pair.getSecond(), preparedJoinPair.getSecond()))))
				{
					return false;
				}//均不为空时
//			}
//			else//剩下一种情况为一个为空一个非空，则返回false
//			{
//				return false;
//			}
		}
		return true;
	}
	
	public static List<Record> rightOutterJoinOnProperties(List<Record> records1, List<Record> records2, List<FieldPair<String, String>> joinedProperties){
		
		return leftOutterJoinOnProperties(records2, records1, joinedProperties);
	}
	
	
	private static boolean ifJoined(Record record1, Record record2, List<? extends FieldPair<String, String>> joinedProperties){
		for(FieldPair<String, String> pair:joinedProperties){
			//如果两个结果集中的某个连接字段的值不相等，则不进行连接
			if(!record1.getValueByColumnName(pair.getFirst()).equalsIgnoreCase(record2.getValueByColumnName(pair.getSecond()))){
				return false;
			}
		}
		//所有连接字段上的值都相等，可以进行连接
		return true;
	}
	
	private static boolean ifJoined(Record record1,String dataSource1,Record record2, List<? extends FieldPair<String, String>> joinedProperties,boolean joinPairInverse){
		if(!joinPairInverse)//如果连接信息pair的顺序不需要反转
		{
		for(FieldPair<String, String> pair:joinedProperties)
		{
			//如果两个结果集中的某个连接字段的值不相等，则不进行连接
			if(!(record1.getValueByColumnNameAndDataSource(pair.getFirst(),dataSource1).equalsIgnoreCase(record2.getValueByColumnName(pair.getSecond()))))
			{
				return false;
			}
		}
		//所有连接字段上的值都相等，可以进行连接
		return true;
		}
		else
		{
			for(FieldPair<String, String> pair:joinedProperties)
			{
				//如果两个结果集中的某个连接字段的值不相等，则不进行连接
				if(!(record1.getValueByColumnNameAndDataSource(pair.getSecond(),dataSource1).equalsIgnoreCase(record2.getValueByColumnName(pair.getFirst()))))
				{
					return false;
				}
			}
			//所有连接字段上的值都相等，可以进行连接
			return true;
		}
	}
	
	/**
	 * ResultSet转Record
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	public static List<Record> resultSetToRecords(String dataSource,ResultSet result, String sqlType) throws SQLException{
		List<Record> records=new ArrayList<Record>();
		List<ColumnMeta> columnsMeta=getColumnsMeta(result);
//		int i=0;
		while(result.next()){
//			i++;
			Record oneRecord=new Record();
			for(ColumnMeta column:columnsMeta){//创建一个记录
				ColumnValue oneColumnValue=new ColumnValue();
				oneColumnValue.setColumnMetaData(column);
				//设置当前记录中字段column的值
				oneColumnValue.setValue(getValueOfCorrenspondentType(column, result, sqlType));
				oneColumnValue.setDataSource(dataSource);
				oneRecord.columns.add(oneColumnValue);				
			}
			records.add(oneRecord);
//			int a=0;
//			if((a=i/1000000)!=0) 
//			{
//				WriteRecordList(records,a);
//				records.clear();
//			}
		}
		return records;
	}
	
	public static boolean resultSetToRecords2(String dataSource,String destTable,ResultSet result, String sqlType){
		try 
		{
		List<Record> records=new ArrayList<Record>();
		List<ColumnMeta> columnsMeta=getColumnsMeta(result);
		int i=0;
		while(result.next()){
			i++;
			Record oneRecord=new Record();
			for(ColumnMeta column:columnsMeta){//创建一个记录
				ColumnValue oneColumnValue=new ColumnValue();
				oneColumnValue.setColumnMetaData(column);
				//设置当前记录中字段column的值
				oneColumnValue.setValue(getValueOfCorrenspondentType(column, result, sqlType));
				oneColumnValue.setDataSource(dataSource);
				oneRecord.columns.add(oneColumnValue);				
			}
			records.add(oneRecord);
			int a=-1;
			if((a=i%Constant.threshold)==0) 
			{
				WriteRecordList(records,destTable,dataSource,(i/Constant.threshold));
				records.clear();
			}
		}
		if(records.size()>0)
		{
			//WriteRecordList(records,dataSource,((i%1000000==0))?1:(i/1000000)+1);//处理剩余的records序列化
			WriteRecordList(records,destTable,dataSource,(i/Constant.threshold)+1);//处理剩余的records序列化
			records.clear();
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void insertRecordsToTable(List<Record> records,Statement stmt, String tableName) throws SQLException{
		for(Record oneRecord:records){
			String insertSQL="INSERT INTO `"+tableName+"` VALUES (";
			for(ColumnValue oneColumn:oneRecord.getColumns()){
				insertSQL=insertSQL+oneColumn.getValue().toString();
			}
			if(insertSQL.endsWith(","))//去掉最后的“，”
				insertSQL=insertSQL.substring(0, insertSQL.length()-1);
			insertSQL=insertSQL+"); ";
			stmt.executeUpdate(insertSQL);//完成一行相应字段插入一个表中
		}
	}
	
	public static void insertRecordsToTable2(List<Record> records,Connection conn,String destPath,Map<String,String> insertMap) throws SQLException
	{
		String tableName=destPath.split("###")[6];
		String deleteSql="truncate table `"+tableName+"`";
		boolean deleteSuccess=conn.createStatement().execute(deleteSql);
		if(deleteSuccess) {
			System.out.println("delete Success");
		}
		else {
			System.out.println("delete failed");
		}
		
		Map<String,String> methodMap=null;
		MethodReflection methodReflect=new MethodReflection();	
		size=records.size();
		int columnCount=insertMap.size();
		StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
		int a=0;
		for(String key:insertMap.keySet())
		{
			insertSql.append((a == columnCount - 1) ?("`"+key+"`"):("`"+key+"`,"));
			a++;
		}
		insertSql.append(") values(");
		for (int i = 0; i < columnCount; i++)
		{
			insertSql.append((i == columnCount - 1) ? "?" : "?,");
		}
		insertSql.append(")");
		String sql = insertSql.toString();
		conn.setAutoCommit(false);
		PreparedStatement ps=conn.prepareStatement(sql);
		for(Record record:records)
		{
			int count=1;
			int i=1;
			for(Map.Entry<String, String> entry:insertMap.entrySet())
			{
				String temp=null;
				String colDestPath=destPath+"@@@"+entry.getKey();
				String colSourcePath=entry.getValue();

				if((temp=record.getValueByColumnNameAndDataSource(entry.getValue().split("@@@")[1], entry.getValue().split("@@@")[0]))=="null")
				{
					ps.setObject(i, null);
				}
				else
				{
					String key=colSourcePath+"!!!"+colDestPath;
					if((methodMap=MethodMapper.getMethodMapper()).containsKey(key)) {
						ps.setObject(i,methodReflect.load(methodMap.get(key).split("!!!")[0], methodMap.get(key).split("!!!")[1], temp));
					}
					else {
						ps.setObject(i, temp);
					}
				}
				i++;
			}
			i=1;
			count++;
			process++;
			ps.addBatch();
			if(count>=100000)
			{
				ps.executeBatch();
				ps.clearBatch();
				count=1;
				conn.commit();
			}
		}
		ps.executeBatch();
		ps.clearBatch();
		conn.commit();
		conn.setAutoCommit(true);
		ps.close();
		process=0;
		size=-1;
	}
	
	public static void insertRecordsToTable2WithoutCount(List<Record> records,Connection conn,String destPath,Map<String,String> insertMap) throws SQLException
	{
		String tableName=destPath.split("###")[6];
		Map<String,String> methodMap=null;
		MethodReflection methodReflect=new MethodReflection();	
		int columnCount=insertMap.size();
		StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
		int a=0;
		for(String key:insertMap.keySet())
		{
			insertSql.append((a == columnCount - 1) ?("`"+key+"`"):("`"+key+"`,"));
			a++;
		}
		insertSql.append(") values(");
		for (int i = 0; i < columnCount; i++)
		{
			insertSql.append((i == columnCount - 1) ? "?" : "?,");
		}
		insertSql.append(")");
		String sql = insertSql.toString();
		conn.setAutoCommit(false);
		PreparedStatement ps=conn.prepareStatement(sql);
		for(Record record:records)
		{
			int count=1;
			int i=1;
			for(Map.Entry<String, String> entry:insertMap.entrySet())
			{
				String temp=null;
				String colDestPath=destPath+"@@@"+entry.getKey();
				String colSourcePath=entry.getValue();
				if((temp=record.getValueByColumnNameAndDataSource(entry.getValue().split("@@@")[1], entry.getValue().split("@@@")[0]))=="null")
				{
					ps.setObject(i, null);
				}
				else
				{
					String key=colSourcePath+"!!!"+colDestPath;
					if((methodMap=MethodMapper.getMethodMapper()).containsKey(key)) {
						ps.setObject(i,methodReflect.load(methodMap.get(key).split("!!!")[0], methodMap.get(key).split("!!!")[1], temp));
					}
					else {
					ps.setObject(i, temp);
					}
				}
				i++;
			}
			i=1;
			count++;
			ps.addBatch();
			if(count>=100000)
			{
				ps.executeBatch();
				ps.clearBatch();
				count=1;
				conn.commit();
			}
		}
		ps.executeBatch();
		ps.clearBatch();
		conn.commit();
		conn.setAutoCommit(true);
		ps.close();
	}
	
	public static void insertRecordsWithFragment(Connection conn,String tableDest,Map<String,String> insertMap,String destTable) throws SQLException
	{
		String tableName=tableDest.split("###")[6];
		String deleteSql="truncate table `"+tableName+"`";
		boolean deleteSuccess=conn.createStatement().execute(deleteSql);
		if(deleteSuccess) {
			System.out.println("delete Success");
		}
		else {
			System.out.println("delete failed");
		}
		List<String> files=FileUtil.findFilebyName(Constant.ObjectFolder,destTable, "destination");
		size=files.size();
		process=0;
		for(int i=1;i<=files.size();i++)
		{
			List<Record> records=MultiSourceSchemaTransformation.ReadRecordList(Constant.ObjectFolder+"/"+destTable+"/destination"+"_"+i+".txt");
			insertRecordsToTable2WithoutCount(records,conn,tableDest,insertMap);
			process++;
		}
		size=-1;
		process=0;
	}
	
	
	/**
	 * 获得各列元数据
	 * @param result
	 * @return
	 * @throws SQLException 
	 */
	public static List<ColumnMeta> getColumnsMeta(ResultSet result) throws SQLException{
		ResultSetMetaData rsmd = result.getMetaData();
		int columnCount=rsmd.getColumnCount();
		ArrayList<ColumnMeta> columns=new ArrayList<ColumnMeta>();
		for(int i=1;i<=columnCount; i++){
			ColumnMeta column=new ColumnMeta();
			String thisTableName=rsmd.getTableName(i);
			column.setTableName(thisTableName);
			column.setResultSetIndex(i);
			column.setColumnName(filterNames(rsmd.getColumnName(i)));
			column.setColumnTypeName(rsmd.getColumnTypeName(i));
			column.setColumnType(rsmd.getColumnType(i));
			column.setPrecision(rsmd.getPrecision(i));
			column.setSigned(rsmd.isSigned(i));
			column.setNull(rsmd.isNullable(i)==0?false:true);
			column.setAutoIncrement(rsmd.isAutoIncrement(i));			
			columns.add(column);
		}	
		return columns;
	}
	
	
	
	/**
	 * 过滤oracle字段名字上的''或表名上的""
	 * @param name
	 * @return
	 */
	public static String filterNames(String name){
		if(name.startsWith("\'") && name.endsWith("\'"))
			name=name.split("\'")[1];
		else if(name.startsWith("\"") && name.endsWith("\""))
			name=name.split("\"")[1];
		return name;
	}
	
	
	/**
	 * 根据指定列的类型获得指定类型的数据值
	 * @param column
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	
	public static Object getValueOfCorrenspondentType(ColumnMeta column, ResultSet rs, String sqlType) throws SQLException{
//		String returned = null;
//		if(sqlType.equalsIgnoreCase("mysql")){
//			returned=getValueOfMysqlCorrenspondentType(column, rs);
//		}else if(sqlType.equalsIgnoreCase("oracle")){
//			returned=getValueOfOracleCorrenspondentType(column, rs);
//		}
//		return returned;
		Object returned=null;
		if(column.getColumnTypeName().indexOf("TIME")!=-1||column.getColumnTypeName().indexOf("DATE")!=-1)
		{
			returned = rs.getString(column.getColumnName());
			if (returned != null) {
				// time = time.split("\\.")[0];
				return returned;
			} else {
				return null;
			}
		}
		else
		{
			returned=rs.getObject(column.getColumnName());
		}
		return returned;
	}
	
	/**
	 * 根据指定列的类型获得指定类型的数据值
	 * @param column
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	
	public static String getValueOfMysqlCorrenspondentType(ColumnMeta column, ResultSet rs) throws SQLException{
		int i=column.getResultSetIndex();
		String columnJavaType=JDBCTypesUtils.jdbcTypeToJavaType(column.getColumnType()).getName();
		columnJavaType=columnJavaType.substring(columnJavaType.lastIndexOf(".")+1);
		String sql="";
		switch (columnJavaType){//需要对值为null这种情况进行特殊处理
			case "String":
				if(rs.getString(i)==null) {
					sql=rs.getString(i)+",";
				} else {
					sql="'"+rs.getString(i)+"',";
				}				
				break;
			case "Boolean":
				sql="'"+rs.getBoolean(i)+"',";
				break;
			case "Byte":
				sql="'"+rs.getByte(i)+"',";
				break;
			case "Long":
				sql="'"+rs.getLong(i)+"',";
				break;
			case "Blob":
				if(rs.getBlob(i)==null){
					sql=rs.getBlob(i)+",";
				}else {
					sql="'"+rs.getBlob(i)+"',";
				}
				break;
			case "BigDecimal":
				sql="'"+rs.getBigDecimal(i)+"',";
				break;
			case "Date":
				if(rs.getDate(i)==null){
					sql=rs.getDate(i)+",";
				}else {
					sql="'"+rs.getDate(i)+"',";
				}				
				break;
			case "Object":
				if(rs.getObject(i)==null){
					sql= rs.getObject(i)+",";
				}else {
					sql="'"+rs.getObject(i)+"',";
				}				
				break;
			case "Integer":
				sql="'"+rs.getInt(i)+"',";
				break;
			case "Short":
				sql="'"+rs.getShort(i)+"',";
				break;
			case "Clob":
				if(rs.getClob(i)==null){
					sql= rs.getClob(i)+",";
				}else {
					sql="'"+rs.getClob(i)+"',";
				}
				break;
			case "Float":
				sql="'"+rs.getFloat(i)+"',";
				break;
			case "Double":
				sql="'"+rs.getDouble(i)+"',";
				break;
		}		
		
		
		return sql;			
	}
	
	
	public static String getValueOfOracleCorrenspondentType(ColumnMeta column, ResultSet rs) throws SQLException{
		int i=column.getResultSetIndex();
		if(rs.getObject(i)==null) return "null,";
		String columnJavaType=JDBCTypesUtils.jdbcTypeToJavaType(column.getColumnType()).getName();
		columnJavaType=columnJavaType.substring(columnJavaType.lastIndexOf(".")+1);
		String sql="";	
		if(column.getColumnType()==-1 && rs.getString(column.getResultSetIndex()).length()>1000) return "empty_clob(), ";
		switch (columnJavaType){
			case "String":
				String temps=rs.getString(i);
				if(temps.equals("") && !column.isNull()) temps= temps+" ";
				sql="'"+temps.replaceAll("'", "''")+"',";
				break;
			case "Boolean":
				sql="'"+rs.getBoolean(i)+"',";
				break;
			case "Byte":
				sql="'"+rs.getByte(i)+"',";
				break;
			case "Long":
				sql="'"+rs.getLong(i)+"',";
				break;
			case "Blob":
				sql="'"+rs.getBlob(i)+"',";
				break;
			case "BigDecimal":
				sql="'"+rs.getBigDecimal(i)+"',";
				break;
			case "Date":
				String temp=rs.getDate(i).toString();
				sql="TO_DATE('"+temp.split("\\.")[0]+"', 'YYYY-MM-DD HH24:MI:SS'),";
				break;
			case "Timestamp":
				String temp1=rs.getTimestamp(i).toString();
				sql="TO_TIMESTAMP('"+temp1.split("\\.")[0]+"', 'YYYY-MM-DD HH24:MI:SS'),";
				break;
			case "Object":
				sql="'"+rs.getObject(i)+"',";
				break;
			case "Integer":
				sql="'"+rs.getInt(i)+"',";
				break;
			case "Short":
				sql="'"+rs.getShort(i)+"',";
				break;
			case "Clob":
				sql="'"+rs.getClob(i)+"',";
				break;
			case "Float":
				sql="'"+rs.getFloat(i)+"',";
				break;
			case "Double":
				sql="'"+rs.getDouble(i)+"',";
				break;
		}
		return sql;			
	}
	
	
	private String jdbcTypeToMySQL(ColumnMeta column) {
		switch (column.getColumnType()) {
		case -16:
			return "VARCHAR"; // LONGNVARCHAR
		case -15:
			return "VARCHAR"; // NCHAR
		case -9:
			return "VARCHAR"; // NVARCHAR
		case -8:
			return "ID"; // ROWID
		case -7:
			return "BIT"; // BIT
		case -6:
			return "TINYINT"; // TINYINT UNSIGNED
		case -5:
			return "BIGINT"; // BIGINT UNSIGNED
		case -4:
			return "BLOB"; //  BLOB 
		case -3:
			return "BLOB"; // TINYBLOB
		case -2:
			return "BLOB"; // BINARY
		case -1:
			if (column.getColumnTypeName().equalsIgnoreCase("TEXT"))
				return "TEXT"; // VARCHAR
			else if (column.getColumnTypeName().equalsIgnoreCase("BLOB"))
				return "BLOB";// MEDIUMBLOB
		case 1:
			return "CHAR"; // CHAR
		case 2:
			return "DECIMAL"; // NUMERIC
		case 3:
			return "DECIMAL"; // DECIMAL
		case 4:
			if (column.getColumnTypeName().equalsIgnoreCase("INTEGER"))
				return "INTEGER";// INTEGER UNSIGNED
			else if (column.getColumnTypeName().equalsIgnoreCase("MEDIUMINT"))
				return "MEDIUMINT";// MEDIUMINT
			else if (column.getColumnTypeName().equalsIgnoreCase("ID"))
				return "ID";// PK
		case 5:
			return "SMALLINT"; // SMALLINT UNSIGNED
		case 6:
			return "FLOAT"; // FLOAT
		case 7:
			return "FLOAT"; // REAL
		case 8:
			return "DOUBLE"; // DOUBLE
		case 12:
			return "VARCHAR"; // VARCHAR
		case 16:
			return "BIT"; // BOOLEAN
		case 91:
			if (column.getColumnTypeName().equalsIgnoreCase("DATE"))
				return "DATE"; // DATE
			else if (column.getColumnTypeName().equalsIgnoreCase("YEAR"))
				return "YEAR"; // YEAR
		case 92:
			return "TIME"; // TIME
		case 93:
			if (column.getColumnTypeName().equals("TIMESTAMP"))
				return "TIMESTAMP"; // TIMESTAMP
			else if (column.getColumnTypeName().equals("DATETIME"))
				return "DATETIME";// DATETIME
			else
				return "";
		case 1111:
			return ""; // OTHER
		case 2000:
			return ""; // JAVA_OBJECT
		case 2001:
			return ""; // DISTINCT
		case 2002:
			return ""; // STRUCT
		case 2003:
			return ""; // ARRAY
		case 2004:
			return "BLOB"; // BLOB
		case 2005:
			return "BLOB"; // CLOB
		case 2006:
			return ""; // REF
		case 2009:
			return ""; // SQLXML
		case 2011:
			return "BLOB"; // NCLOB
		default:
			return "";
		}
	}
	
	public static boolean joinWithFragment(List<? extends FieldPair<String, String>> pairs,String fileName1,String fileName2,String destName,String destTable)
	{
		List<String> fileList1=FileUtil.findFilebyName(Constant.ObjectFolder, destTable,fileName1);
		List<String> fileList2=FileUtil.findFilebyName(Constant.ObjectFolder, destTable,fileName2);
		List<Record> results=new ArrayList<Record>();
		int a=1;
		for(int i=1;i<=fileList1.size();i++)
		{
			List<Record> records1=MultiSourceSchemaTransformation.ReadRecordList(Constant.ObjectFolder+"/"+destTable+"/"+fileName1+"_"+i+".txt");
			for(int j=1;j<=fileList2.size();j++)
			{
				List<Record> records2=MultiSourceSchemaTransformation.ReadRecordList(Constant.ObjectFolder+"/"+destTable+"/"+fileName2+"_"+j+".txt");
				List<Record> tempList=MultiSourceSchemaTransformation.outterJoinEqualOnProperties(records1, records2, pairs,j,fileList2.size());//问题出在这里，因为是有循环在，所以遇到空值情况会重复连接，因此对于没有连接成功的results会重复出现
				if(tempList!=null)
				{
				results.addAll(tempList);
				if(results.size()>=Constant.threshold)
				{
					MultiSourceSchemaTransformation.WriteRecordList(results,destTable,destName, a);
					results.clear();
					a++;
				}
				}
				records2.clear();
			}
			records1.clear();
		}
		if(results.size()>0)
		{
			MultiSourceSchemaTransformation.WriteRecordList(results,destTable, destName, a);
			results.clear();
		}
		return true;
	}


public static boolean joinWithFragment2(List<? extends FieldPair<String, String>> pairs,String fileName1,String dataSource1,String fileName2,String destName,boolean joinPairInverse,String destTable)
{
	List<String> fileList1=FileUtil.findFilebyName(Constant.ObjectFolder,destTable, fileName1);
	List<String> fileList2=FileUtil.findFilebyName(Constant.ObjectFolder,destTable, fileName2);
	List<Record> results=new ArrayList<Record>();
	int a=1;
	for(int i=1;i<=fileList1.size();i++)
	{
		List<Record> records1=MultiSourceSchemaTransformation.ReadRecordList(Constant.ObjectFolder+"/"+destTable+"/"+fileName1+"_"+i+".txt");
		for(int j=1;j<=fileList2.size();j++)
		{
			List<Record> records2=MultiSourceSchemaTransformation.ReadRecordList(Constant.ObjectFolder+"/"+destTable+"/"+fileName2+"_"+j+".txt");
			List<Record> tempList=MultiSourceSchemaTransformation.outterJoinEqualOnProperties(records1, dataSource1, records2, pairs, joinPairInverse,j,fileList2.size());
			if(tempList!=null)
			{
			results.addAll(tempList);
			if(results.size()>=Constant.threshold)
			{
				MultiSourceSchemaTransformation.WriteRecordList(results,destTable,destName, a);
				results.clear();
				a++;
			}
			}
			records2.clear();
		}
		records1.clear();
	}
	if(results.size()>0)
	{
		MultiSourceSchemaTransformation.WriteRecordList(results,destTable,destName, a);
		results.clear();
	}
	return true;
}
}

