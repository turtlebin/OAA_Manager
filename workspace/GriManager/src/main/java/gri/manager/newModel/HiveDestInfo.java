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
public class HiveDestInfo implements DataDestInfo{
    @JSONField(ordinal = 1)
    private String host;
    @JSONField(ordinal = 12)
    private String port;
    @JSONField(ordinal = 3)
    private String dbName;
    @JSONField(ordinal = 4)
    private String tableName;
    @JSONField(ordinal = 5)
    private String mode;

    public String getDestType() {
        return "Hive";
    }

    public static HiveDestInfo.HiveDestInfoBuilder builder(){
        return new HiveDestInfo.HiveDestInfoBuilder();
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

    public static final class HiveDestInfoBuilder {
        private String host;
        private String port;
        private String dbName;
        private String tableName;
        private String mode;

        private HiveDestInfoBuilder() {
        }

        public static HiveDestInfoBuilder aHiveDestInfo() {
            return new HiveDestInfoBuilder();
        }

        public HiveDestInfoBuilder host(String host) {
            this.host = host;
            return this;
        }

        public HiveDestInfoBuilder port(String port) {
            this.port = port;
            return this;
        }

        public HiveDestInfoBuilder dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public HiveDestInfoBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public HiveDestInfoBuilder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public HiveDestInfo build() {
            HiveDestInfo hiveDestInfo = new HiveDestInfo();
            hiveDestInfo.setHost(host);
            hiveDestInfo.setPort(port);
            hiveDestInfo.setDbName(dbName);
            hiveDestInfo.setTableName(tableName);
            hiveDestInfo.setMode(mode);
            return hiveDestInfo;
        }
    }
}

