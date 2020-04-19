package gri.manager.util;

import Support.DataFrameHelper;
import gri.manager.newModel.DBSourceInfo;
import gri.manager.newModel.DataSourceInfo;
import gri.manager.newModel.FileSourceInfo;
import gri.manager.newModel.HiveSourceInfo;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class DataFrameGetter {
    private String driver;
    private String url;
    private String db_name;

    private DataSourceInfo sourceInfo;
    public DataFrameGetter(DataSourceInfo sourceInfo){
        this.sourceInfo=sourceInfo;
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
