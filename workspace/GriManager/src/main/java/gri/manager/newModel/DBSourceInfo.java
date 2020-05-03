package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.ArrayList;

@Data
@ToString(callSuper = true)
public class DBSourceInfo implements DataSourceInfo {
    @JSONField(ordinal = 1)
    private String alias;
    @JSONField(ordinal = 2)
    private String type = "";
    @JSONField(ordinal = 3)
    private String partitionColumn = "";
    @JSONField(ordinal = 4)
    private int lowerBound = -1;
    @JSONField(ordinal = 5)
    private int upperBound = -1;
    @JSONField(ordinal = 6)
    private int partitionCount = -1;
    @JSONField(ordinal = 7)
    private String userName = "";
    @JSONField(ordinal = 8)
    private String password = "";
    @JSONField(ordinal = 9)
    private String dbName = "";
    @JSONField(ordinal = 10)
    private String tableName = "";
    @JSONField(ordinal = 11)
    private String whereClause = "";
    @JSONField(ordinal = 12)
    private String timeStampColumn = "";
    @JSONField(ordinal = 13)
    private String time = "";
    @JSONField(ordinal = 14)
    private String addSource = "";
    @JSONField(ordinal = 15)
    private String host = "";
    @JSONField(ordinal = 16)
    private String port = "";
    @JSONField(ordinal = 17)
    private String sql = "";
    @JSONField(ordinal = 18)
    private ArrayList<Col2> colList;
    public String getSourceType() {
        return "Database";
    }

    public static DBSourceInfo.DBSourceInfoBuilder builder(){
        return new DBSourceInfo.DBSourceInfoBuilder();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartitionColumn() {
        return partitionColumn;
    }

    public void setPartitionColumn(String partitionColumn) {
        this.partitionColumn = partitionColumn;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public int getPartitionCount() {
        return partitionCount;
    }

    public void setPartitionCount(int partitionCount) {
        this.partitionCount = partitionCount;
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
        if (obj instanceof DBSourceInfo) {
            if (this.type.equals(((DBSourceInfo) obj).type) &&
                    this.userName.equals(((DBSourceInfo) obj).userName) &&
                    this.password.equals(((DBSourceInfo) obj).password) &&
                    this.dbName.equals(((DBSourceInfo) obj).dbName) &&
                    this.tableName.equals(((DBSourceInfo) obj).tableName) &&
                    this.partitionCount==((DBSourceInfo) obj).partitionCount &&
                    this.partitionColumn.equals(((DBSourceInfo) obj).partitionColumn) &&
                    this.lowerBound==(((DBSourceInfo) obj).lowerBound) &&
                    this.upperBound==(((DBSourceInfo) obj).upperBound) &&
                    this.whereClause.equals(((DBSourceInfo) obj).whereClause) &&
                    this.timeStampColumn.equals(((DBSourceInfo) obj).timeStampColumn) &&
                    this.time.equals(((DBSourceInfo) obj).time) &&
                    this.host.equals(((DBSourceInfo) obj).host) &&
                    this.port.equals(((DBSourceInfo) obj).port) &&
                    this.sql.equals(((DBSourceInfo) obj).sql) &&
                    this.addSource.equals(((DBSourceInfo) obj).addSource)
                    && checkListEquality(this.colList, ((DBSourceInfo) obj).getColList()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (type == null ? 0 : type.hashCode());
        result = 31 * result + (userName == null ? 0 : userName.hashCode());
        result = 31 * result + (password == null ? 0 : password.hashCode());
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

    public static final class DBSourceInfoBuilder {
        private String alias;
        private String type = "";
        private String partitionColumn = "";
        private int lowerBound = -1;
        private int upperBound = -1;
        private int partitionCount = -1;
        private String userName = "";
        private String password = "";
        private String dbName = "";
        private String tableName = "";
        private String whereClause = "";
        private String timeStampColumn = "";
        private String time = "";
        private String addSource = "";
        private String host = "";
        private String port = "";
        private String sql = "";
        private ArrayList<Col2> colList;

        private DBSourceInfoBuilder() {
        }

        public static DBSourceInfoBuilder aDBSourceInfo() {
            return new DBSourceInfoBuilder();
        }

        public DBSourceInfoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public DBSourceInfoBuilder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public DBSourceInfoBuilder partitionColumn(String partitionColumn) {
            this.partitionColumn = partitionColumn;
            return this;
        }

        public DBSourceInfoBuilder lowerBound(int lowerBound) {
            this.lowerBound = lowerBound;
            return this;
        }

        public DBSourceInfoBuilder upperBound(int upperBound) {
            this.upperBound = upperBound;
            return this;
        }

        public DBSourceInfoBuilder partitionCount(int partitionCount) {
            this.partitionCount = partitionCount;
            return this;
        }

        public DBSourceInfoBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public DBSourceInfoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DBSourceInfoBuilder dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public DBSourceInfoBuilder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public DBSourceInfoBuilder whereClause(String whereClause) {
            this.whereClause = whereClause;
            return this;
        }

        public DBSourceInfoBuilder timeStampColumn(String timeStampColumn) {
            this.timeStampColumn = timeStampColumn;
            return this;
        }

        public DBSourceInfoBuilder time(String time) {
            this.time = time;
            return this;
        }

        public DBSourceInfoBuilder addSource(String addSource) {
            this.addSource = addSource;
            return this;
        }

        public DBSourceInfoBuilder host(String host) {
            this.host = host;
            return this;
        }

        public DBSourceInfoBuilder port(String port) {
            this.port = port;
            return this;
        }

        public DBSourceInfoBuilder sql(String sql) {
            this.sql = sql;
            return this;
        }

        public DBSourceInfoBuilder colList(ArrayList<Col2> colList) {
            this.colList = colList;
            return this;
        }

        public DBSourceInfo build() {
            DBSourceInfo dBSourceInfo = new DBSourceInfo();
            dBSourceInfo.setAlias(alias);
            dBSourceInfo.setType(type);
            dBSourceInfo.setPartitionColumn(partitionColumn);
            dBSourceInfo.setLowerBound(lowerBound);
            dBSourceInfo.setUpperBound(upperBound);
            dBSourceInfo.setPartitionCount(partitionCount);
            dBSourceInfo.setUserName(userName);
            dBSourceInfo.setPassword(password);
            dBSourceInfo.setDbName(dbName);
            dBSourceInfo.setTableName(tableName);
            dBSourceInfo.setWhereClause(whereClause);
            dBSourceInfo.setTimeStampColumn(timeStampColumn);
            dBSourceInfo.setTime(time);
            dBSourceInfo.setAddSource(addSource);
            dBSourceInfo.setHost(host);
            dBSourceInfo.setPort(port);
            dBSourceInfo.setSql(sql);
            dBSourceInfo.setColList(colList);
            return dBSourceInfo;
        }
    }
}
