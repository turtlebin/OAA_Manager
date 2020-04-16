package gri.engine.integrate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.JoinRowSetImpl;

import gri.engine.dest.Pair;
import gri.engine.util.Constant;

public class Test {
	public static void main(String args[]) throws ClassNotFoundException, SQLException{
		
		Class.forName("com.mysql.jdbc.Driver"); 
		String url= "jdbc:mysql://localhost:3306/test";
		Connection conn=DriverManager.getConnection(url, "root", "csasc");
		Statement stmt= conn.createStatement();
		
		long start=System.currentTimeMillis();
		ResultSet rs1=stmt.executeQuery("select * from doublejoin");
//		
//		CachedRowSet cache=new CachedRowSetImpl();
//		cache.populate(rs1);
//		rs1.close();
////		
		Statement stmt1= conn.createStatement();
		ResultSet rs2=stmt1.executeQuery("select * from doublejoin2");
////		
//		CachedRowSet cache2=new CachedRowSetImpl();
//		cache2.populate(rs2);
//		rs2.close();
////		
//		JoinRowSet join=new JoinRowSetImpl();
//		cache.setMatchColumn("ID");
//		
//		cache2.setMatchColumn("ID2");
//		
		Statement stmt3=conn.createStatement();
		ResultSet rs3=stmt3.executeQuery("select * from jiguan2");
//		
//		CachedRowSet cache3=new CachedRowSetImpl();
//		cache3.populate(rs3);
//		rs3.close();
//
//
//         
//		join.addRowSet(cache);
//		join.addRowSet(cache2,"ID2");
//		
//		cache=join;
//		cache.setMatchColumn("JGBH");
//		
//		JoinRowSet join2=new JoinRowSetImpl();
//		join2.addRowSet(cache);
//		join2.addRowSet(cache3,"JG");
//
//		
//		long end=System.currentTimeMillis();
//		long time=end-start;
//		System.out.println("连接花费的时间为："+time);
		//printRowSet(join2);
		
//		Statement stmt2= conn.createStatement();
//		String table="join";
		
		
//		MultiSourceSchemaTransformation.resultSetToRecords2("xingming",rs1, "mysql");
//		MultiSourceSchemaTransformation.resultSetToRecords2("xingbie",rs2, "mysql");
//		Pair<String, String> pair=new Pair<>();
//		pair.setFirst("ID");
//		pair.setSecond("ID2");
//		Pair<String, String> pair2=new Pair<>();
//		pair2.setFirst("XM");
//		pair2.setSecond("XM2");
//		
//		List<Pair<String, String>> pairs=new ArrayList<Pair<String,String>>();
//		pairs.add(pair);
//		pairs.add(pair2);
//		MultiSourceSchemaTransformation.joinWithFragment(pairs,"xingming","xingbie","destination");
	    

//		List<Record> records1=MultiSourceSchemaTransformation.resultSetToRecords("xingming",rs1, "mysql");
//		List<Record> records2=MultiSourceSchemaTransformation.resultSetToRecords("xingbie",rs2, "mysql");
//		Pair<String, String> pair=new Pair<>();
//		pair.setFirst("ID");
//		pair.setSecond("ID2");
//		List<FieldPair<String, String>> pairs=new ArrayList<FieldPair<String,String>>();
//		pairs.add(pair);
//		List<Record> results=MultiSourceSchemaTransformation.outterJoinEqualOnProperties(records1, records2, pairs);
//		records1.clear();
//		rs1.close();
//		records2.clear();
//		rs2.close();
//		Pair<String, String> pair2=new Pair<>();
//		pair2.setFirst("JG");
//		pair2.setSecond("JGBH");
//		List<FieldPair<String, String>> pairs2=new ArrayList<FieldPair<String,String>>();
//		pairs2.add(pair2);
		//MultiSourceSchemaTransformation.resultSetToRecords2("jiguan",rs3, "mysql");
        printlnResutlSet(Constant.ObjectFolder+"destination_1.txt");
//		List<Record> results2=MultiSourceSchemaTransformation.outterJoinEqualOnProperties(results, records3, pairs2);
	//	MultiSourceSchemaTransformation.joinWithFragment2(pairs2, "destination", "xingbie", "jiguan", "temp", true);
		long end=System.currentTimeMillis();
		long time=end-start;
		System.out.println("连接花费的时间为："+time);
		//printRecordList(results);
		//MultiSourceSchemaTransformation.insertRecordsToTable(results, stmt2, table);
	}
	
	
	public static void printlnResutlSet(String path)
	{
		try 
		{
			List<Record> records=MultiSourceSchemaTransformation.ReadRecordList(path);
			printRecordList(records);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void printRowSet(ResultSet rs)
	{
		System.out.println("The data in JoinRowSet:");
		try {
			int columnSize=rs.getMetaData().getColumnCount();
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
			{ 
		         System.out.print(rs.getMetaData().getColumnName(i)+"   ");
		    }
			System.out.println("");
			while(rs.next())
			{
				for(int i=1;i<=columnSize;i++)
				{
				System.out.print(rs.getString(i)+"   ");
				}
				System.out.println("");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printRecordList(List<Record> records)
	{
		for(Record record:records)
		{
			for(ColumnValue value:record.getColumns())
			{
				System.out.print(value.getValue());
			}
			System.out.println("");
		}
	}
}
