package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DBDestInfo implements DataDestInfo{
    @JSONField(ordinal = 1)
    private String type="";
    @JSONField(ordinal = 2)
    private String host="";
    @JSONField(ordinal = 3)
    private String port="";
    @JSONField(ordinal = 4)
    private String dbName="";
    @JSONField(ordinal = 5)
    private String userName="";
    @JSONField(ordinal = 6)
    private String password="";
    @JSONField(ordinal = 7)
    private String tableName="";
    @JSONField(ordinal = 8)
    private String mode="";

    public static DBDestInfo.DBDestInfoBuilder builder(){
        return new DBDestInfo.DBDestInfoBuilder();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String getDestType() {
        return "Database";
    }

    public static final class DBDestInfoBuilder {
        private String type="";
        private String host="";
        private String port="";
        private String dbName="";
        private String userName="";
        private String password="";
        private String tableName="";
        private String mode="";

        private DBDestInfoBuilder() {
        }

        public static DBDestInfoBuilder aDBDestInfo() {
            return new DBDestInfoBuilder();
        }

        public DBDestInfoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public DBDestInfoBuilder host(String host) {
            this.host = host;
            return this;
        }

        public DBDestInfoBuilder port(String port) {
            this.port = port;
            return this;
        }

        public DBDestInfoBuilder dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public DBDestInfoBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public DBDestInfoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DBDestInfoBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public DBDestInfoBuilder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public DBDestInfo build() {
            DBDestInfo dBDestInfo = new DBDestInfo();
            dBDestInfo.setType(type);
            dBDestInfo.setHost(host);
            dBDestInfo.setPort(port);
            dBDestInfo.setDbName(dbName);
            dBDestInfo.setUserName(userName);
            dBDestInfo.setPassword(password);
            dBDestInfo.setTableName(tableName);
            dBDestInfo.setMode(mode);
            return dBDestInfo;
        }
    }
}
