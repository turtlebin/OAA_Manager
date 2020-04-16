/*
package test;

import org.apache.spark.sql.*;
import scala.collection.Iterator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JDBCTest {
    private static final String url = "jdbc:mariadb://192.168.31.100:3306/test?characterEncoding=utf8";
    private static final String user = "root";
    private static final String password = "csasc";
    private static Connection conn=null;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
//            Class.forName("com.mysql.jdbc.Driver");
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Insert(String sql, Iterator<Row> itr){
        PreparedStatement ps=null;
        try{
            ps=conn.prepareStatement(sql);
            while(itr.hasNext()){
                Row row=itr.next();
                for(int index=1;index<=row.length();index++) {
                    ps.setObject(index, getValue(row, index-1));
                }
                ps.executeUpdate();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Object getValue(Row row,int index){
        row.getAs(index);
//        System.out.println(s);
        return row.<String>getAs(index);
    }
}
*/
