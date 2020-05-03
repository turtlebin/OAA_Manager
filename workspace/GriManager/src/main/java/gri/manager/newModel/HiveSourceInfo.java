package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.ArrayList;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class HiveSourceInfo implements DataSourceInfo {
    @JSONField(ordinal = 1)
    private String alias;
    @JSONField(ordinal = 2)
    private String host;
    @JSONField(ordinal = 3)
    private String port;
    @JSONField(ordinal = 4)
    private String dbName;
    @JSONField(ordinal = 5)
    private String tableName;
    @JSONField(ordinal = 6)
    private String whereClause;
    @JSONField(ordinal = 7)
    private String timeStampColumn;
    @JSONField(ordinal = 8)
    private String time;
    @JSONField(ordinal = 9)
    private String addSource;
    @JSONField(ordinal = 10)
    private String sql;
    @JSONField(ordinal = 11)
    private ArrayList<Col2> colList;

    public String getSourceType() {
        return "Hive";
    }

    public static HiveSourceInfo.HiveSourceInfoBuilder builder(){
        return new HiveSourceInfo.HiveSourceInfoBuilder();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public String getTimeStampColumn() {
        return timeStampColumn;
    }

    public void setTimeStampColumn(String timeStampColumn) {
        this.timeStampColumn = timeStampColumn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddSource() {
        return addSource;
    }

    public void setAddSource(String addSource) {
        this.addSource = addSource;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ArrayList<Col2> getColList() {
        return colList;
    }

    public void setColList(ArrayList<Col2> colList) {
        this.colList = colList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof HiveSourceInfo) {
            if (this.dbName.equals(((HiveSourceInfo) obj).dbName) &&
                    this.tableName.equals(((HiveSourceInfo) obj).tableName) &&
                    this.whereClause.equals(((HiveSourceInfo) obj).whereClause) &&
                    this.timeStampColumn.equals(((HiveSourceInfo) obj).timeStampColumn) &&
                    this.time.equals(((HiveSourceInfo) obj).time) &&
                    this.host.equals(((HiveSourceInfo) obj).host) &&
                    this.port.equals(((HiveSourceInfo) obj).port) &&
                    this.sql.equals(((HiveSourceInfo) obj).sql) &&
                    this.addSource.equals(((HiveSourceInfo) obj).addSource)
                    && checkListEquality(this.colList, ((HiveSourceInfo) obj).getColList()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (dbName == null ? 0 : dbName.hashCode());
        result = 31 * result + (tableName == null ? 0 : tableName.hashCode());
        result = 31 * result + (whereClause == null ? 0 : whereClause.hashCode());
        result = 31 * result + (timeStampColumn == null ? 0 : timeStampColumn.hashCode());
        result = 31 * result + (time == null ? 0 : time.hashCode());
        result = 31 * result + (host == null ? 0 : host.hashCode());
        result = 31 * result + (port == null ? 0 : port.hashCode());
        result = 31 * result + (sql == null ? 0 : sql.hashCode());
        result = 31 * result + (addSource == null ? 0 : addSource.hashCode());
        return result;
    }

    public boolean checkListEquality(ArrayList<Col2> cols, ArrayList<Col2> cols2) {
        if (!addSource.equals("选择字段")) {
            return true;
        }
        if (cols == null || cols2 == null) {
            return false;
        }
        if (cols.size() != cols2.size()) {
            return false;
        }
        for (int i = 0; i < cols.size(); i++) {
            if (!cols.get(i).equals(cols2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static final class HiveSourceInfoBuilder {
        private String alias;
        private String host;
        private String port;
        private String dbName;
        private String tableName;
        private String whereClause;
        private String timeStampColumn;
        private String time;
        private String addSource;
        private String sql;
        private ArrayList<Col2> colList;

        private HiveSourceInfoBuilder() {
        }

        public static HiveSourceInfoBuilder aHiveSourceInfo() {
            return new HiveSourceInfoBuilder();
        }

        public HiveSourceInfoBuilder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public HiveSourceInfoBuilder host(String host) {
            this.host = host;
            return this;
        }

        public HiveSourceInfoBuilder port(String port) {
            this.port = port;
            return this;
        }

        public HiveSourceInfoBuilder dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public HiveSourceInfoBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public HiveSourceInfoBuilder whereClause(String whereClause) {
            this.whereClause = whereClause;
            return this;
        }

        public HiveSourceInfoBuilder timeStampColumn(String timeStampColumn) {
            this.timeStampColumn = timeStampColumn;
            return this;
        }

        public HiveSourceInfoBuilder time(String time) {
            this.time = time;
            return this;
        }

        public HiveSourceInfoBuilder addSource(String addSource) {
            this.addSource = addSource;
            return this;
        }

        public HiveSourceInfoBuilder sql(String sql) {
            this.sql = sql;
            return this;
        }

        public HiveSourceInfoBuilder colList(ArrayList<Col2> colList) {
            this.colList = colList;
            return this;
        }

        public HiveSourceInfo build() {
            HiveSourceInfo hiveSourceInfo = new HiveSourceInfo();
            hiveSourceInfo.setAlias(alias);
            hiveSourceInfo.setHost(host);
            hiveSourceInfo.setPort(port);
            hiveSourceInfo.setDbName(dbName);
            hiveSourceInfo.setTableName(tableName);
            hiveSourceInfo.setWhereClause(whereClause);
            hiveSourceInfo.setTimeStampColumn(timeStampColumn);
            hiveSourceInfo.setTime(time);
            hiveSourceInfo.setAddSource(addSource);
            hiveSourceInfo.setSql(sql);
            hiveSourceInfo.setColList(colList);
            return hiveSourceInfo;
        }
    }
}
