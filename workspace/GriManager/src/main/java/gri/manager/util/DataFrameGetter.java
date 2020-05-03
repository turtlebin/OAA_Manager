package gri.manager.util;

import Support.ContextManager;
import Support.DataFrameHelper;
import gri.manager.newModel.*;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.*;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class DataFrameGetter {
    private String driver;
    private String url;
    private String db_name;
    private SparkSession spark= ContextManager.getSparkContext();

    private DataSourceInfo sourceInfo;
    public DataFrameGetter(DataSourceInfo sourceInfo){
        this.sourceInfo=sourceInfo;
    }
    public DataFrameGetter(){

    }
    public Dataset<Row> getDataFrame(){
        Dataset<Row> df=null;
        if(this.sourceInfo instanceof DBSourceInfo){
            analyzeDBParameter((DBSourceInfo)sourceInfo);
            df = DataFrameHelper.getDBDataFrame((DBSourceInfo)sourceInfo,driver,url);
        }else if(this.sourceInfo instanceof HiveSourceInfo){
            df =DataFrameHelper.getHiveDataFrame((HiveSourceInfo) sourceInfo);
        }else if(this.sourceInfo instanceof FileSourceInfo){
            analyzeFileParameter((FileSourceInfo)sourceInfo);
            df=DataFrameHelper.getFileDataFrame((FileSourceInfo)sourceInfo,url);
        }
        return df;
    }

    public Dataset<Row> getJoinDataFrame(JoinInfo info, LinkedHashMap<String,Dataset<Row>> nameToDataFrame){
        Dataset<Row> df=null;
        String joinType=info.getJoinType();
        String joinInfo=info.getJoinInfo();
        Dataset<Row> leftDF=nameToDataFrame.get(info.getLeft());
        Dataset<Row> rightDF=nameToDataFrame.get(info.getRight());
        if(info.getUsing().equals("false")||checkNameNotEqual(joinInfo)) {
            df = leftDF.join(rightDF, getJoinColumn(leftDF, rightDF, joinInfo), joinType);
        }else{
            df= leftDF.join(rightDF,getColumnSeq(leftDF,rightDF,joinInfo),joinType);
        }
        Dataset<Row> df2=DataFrameHelper.getUniqueColNameDF(df);
        df2.show();
        df2.printSchema();
        return df2;
    }

    private Seq<String> getColumnSeq(Dataset<Row> left,Dataset<Row> right,String joinInfo){
        List<String> list=new ArrayList<>();
        String[] joinInfos=joinInfo.split("&&");
        for(String s:joinInfos){
            String[] array=s.split("===");
            list.add(array[0]);
        }
        return JavaConverters.asScalaIteratorConverter(list.iterator()).asScala().toSeq();
    }

    private boolean checkNameNotEqual(String joinInfo){
        if(joinInfo.contains(">=")||joinInfo.contains("<=")||joinInfo.contains(">")||joinInfo.contains("<")){
            return true;
        }
        String[] joinInfos=joinInfo.split("&&");
        for(String s:joinInfos){
            String[] array=s.split("===");
            if(!array[0].equalsIgnoreCase(array[1])){
                return true;
            }
        }
        return false;
    }

    private Column getJoinColumn(Dataset<Row> left,Dataset<Row> right,String joinInfo){
        Column result=null;
        Column column=null;
        String[] joinInfos=joinInfo.split("&&");
        for(String s:joinInfos){
            if(s.contains("===")){
                String[] array=s.split("===");
                column=left.col(array[0]).equalTo(right.col(array[1]));
            }else if(s.contains(">=")){
                String[] array=s.split(">=");
                column=left.col(array[0]).geq(right.col(array[1]));
            }else if(s.contains("<=")){
                String[] array=s.split("<=");
                column=left.col(array[0]).leq(right.col(array[1]));
            }else if(s.contains(">")){
                String[] array=s.split(">");
                column=left.col(array[0]).gt(right.col(array[1]));
            }else if(s.contains("<")){
                String[] array=s.split("<");
                column=left.col(array[0]).lt(right.col(array[1]));
            }else{
                return null;
            }
            if(result==null){
                result=column;
            }else{
                result=result.and(column);
            }
        }
        return result;
    }

    private boolean analyzeDBParameter(DBSourceInfo sourceInfo) {
        String type=sourceInfo.getType();
        String host=sourceInfo.getHost();
        String port=sourceInfo.getPort();
        this.db_name=sourceInfo.getDbName();

        if (type.equalsIgnoreCase("MySQL")) {
            this.driver = "com.mysql.cj.jdbc.Driver";
            this.url = "jdbc:mysql://" + host + ":" + port + "/" + db_name
                    + "?characterEncoding=utf8&useSSL=false&serverTimezone=UTC";// 生成url
        } else if (type.equalsIgnoreCase("SQL Server")) {
            this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            this.url = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName="
                    + db_name;
        } else if (type.equalsIgnoreCase("Oracle")) {
            this.driver = "oracle.jdbc.driver.OracleDriver";
            this.url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + db_name;
        } else
            return false;

        return true;
    }

    private boolean analyzeFileParameter(FileSourceInfo sourceInfo){
        String host=sourceInfo.getHost();
        String port=sourceInfo.getPort();
        String path=sourceInfo.getPath();
        this.url="hdfs://"+host+":"+port+path;
        return true;
    }

}
