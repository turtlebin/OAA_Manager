package gri.manager.util;

import Support.SourceInfoHelper;
import gri.manager.newModel.Col;

public class SourceHelper {
    private String driver;
    private String url;
    private String fileType;

    private String db_name;
    private String userName;
    private String password;
    private String tableName;

    public Col[] getSourceInfo(String sourceType, String dataSource) {
        switch (sourceType.toUpperCase()) {
            case "DATABASE":
                analyzeDBParameter(dataSource);
                break;
            case "HIVE":
                analyzeHiveParameter(dataSource);
                break;
            case "FILE":
                analyzeFileParameter(dataSource);
        }
        Col[] cols=SourceInfoHelper.getSourceInfo(sourceType.toUpperCase(),driver,url,fileType,db_name,tableName,userName,password);
        return cols;
    }

    private boolean analyzeDBParameter(String dataSource) {
        String[] parameters=dataSource.split("###");
        String type=parameters[0];
        String host=parameters[1];
        String port=parameters[2];
        this.db_name=parameters[3];
        this.userName=parameters[4];
        this.password=parameters[5];
        this.tableName=parameters[6];

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

    private boolean analyzeHiveParameter(String dataSource) {
        String[] parameters=dataSource.split("###");
        String host=parameters[0];
        String port=parameters[1];
        this.db_name=parameters[2];
        this.tableName=parameters[3];
        this.url="thrift://"+host+":"+port;
        return true;
    }

    private boolean analyzeFileParameter(String dataSource) {
        String[] parameters=dataSource.split("###");
        this.fileType=parameters[0];
        String host=parameters[1];
        String port=parameters[2];
        String path=parameters[3];

        this.url="hdfs://"+host+":"+port+path;
        return true;
    }

}
