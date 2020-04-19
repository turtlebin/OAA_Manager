package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.ArrayList;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DBSourceInfo implements DataSourceInfo {
    @JSONField(ordinal = 1)
    private String type = "";
    @JSONField(ordinal = 2)
    private String partitionColumn = "";
    @JSONField(ordinal = 3)
    private int lowerBound = -1;
    @JSONField(ordinal = 4)
    private int upperBound = -1;
    @JSONField(ordinal = 5)
    private int partitionCount = -1;
    @JSONField(ordinal = 6)
    private String userName = "";
    @JSONField(ordinal = 7)
    private String password = "";
    @JSONField(ordinal = 8)
    private String dbName = "";
    @JSONField(ordinal = 9)
    private String tableName = "";
    @JSONField(ordinal = 10)
    private String whereClause = "";
    @JSONField(ordinal = 11)
    private String timeStampColumn = "";
    @JSONField(ordinal = 12)
    private String time = "";
    @JSONField(ordinal = 13)
    private String addSource = "";
    @JSONField(ordinal = 14)
    private String host = "";
    @JSONField(ordinal = 15)
    private String port = "";
    @JSONField(ordinal = 16)
    private String sql = "";
    @JSONField(ordinal = 17)
    private ArrayList<Col2> colList;
    public String getSourceType() {
        return "Database";
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

}
