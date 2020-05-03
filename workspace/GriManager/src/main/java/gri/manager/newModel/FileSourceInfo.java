package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.ArrayList;

@Data
@ToString(callSuper = true)
public class FileSourceInfo implements DataSourceInfo{
    @JSONField(ordinal=1)
    private String alias;
    @JSONField(ordinal=2)
    private String type="";
    @JSONField(ordinal=3)
    private String host="";
    @JSONField(ordinal=4)
    private String port="";
    @JSONField(ordinal=5)
    private String path="";
    @JSONField(ordinal=6)
    private String whereClause="";
    @JSONField(ordinal=7)
    private String timeStampColumn="";
    @JSONField(ordinal=8)
    private String time="";
    @JSONField(ordinal=9)
    private String addSource="";
    @JSONField(ordinal = 10)
    private String sql="";
    @JSONField(ordinal = 11)
    private ArrayList<Col2> colList;

    public String getSourceType(){
        return "File";
    }

    public static FileSourceInfo.FileSourceInfoBuilder builder(){
        return new FileSourceInfo.FileSourceInfoBuilder();
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if(obj==null){
            return false;
        }
        if(obj instanceof FileSourceInfo){
            if(this.type.equals(((FileSourceInfo) obj).type)&&
                    this.whereClause.equals(((FileSourceInfo) obj).whereClause)&&
                    this.timeStampColumn.equals(((FileSourceInfo) obj).timeStampColumn)&&
                    this.time.equals(((FileSourceInfo) obj).time)&&
                    this.host.equals(((FileSourceInfo) obj).host)&&
                    this.port.equals(((FileSourceInfo) obj).port)&&
                    this.sql.equals(((FileSourceInfo) obj).sql)&&
                    this.path.equals(((FileSourceInfo) obj).path)&&
                    this.addSource.equals(((FileSourceInfo) obj).addSource)
                    &&checkListEquality(this.colList,((FileSourceInfo) obj).getColList()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        int result=17;
        result=31*result+(type==null?0:type.hashCode());
        result=31*result+(whereClause==null?0:whereClause.hashCode());
        result=31*result+(timeStampColumn==null?0:timeStampColumn.hashCode());
        result=31*result+(time==null?0:time.hashCode());
        result=31*result+(host==null?0:host.hashCode());
        result=31*result+(port==null?0:port.hashCode());
        result=31*result+(sql==null?0:sql.hashCode());
        result=31*result+(path==null?0:path.hashCode());
        result=31*result+(addSource==null?0:addSource.hashCode());
        return result;
    }

    public boolean checkListEquality(ArrayList<Col2> cols,ArrayList<Col2> cols2){
        if(!addSource.equals("选择字段")){
            return true;
        }
        if(cols==null||cols2==null){
            return false;
        }
        if(cols.size()!= cols2.size()){
            return false;
        }
        for( int i=0;i<cols.size();i++){
            if(!cols.get(i).equals(cols2.get(i))){
                return false;
            }
        }
        return true;
    }

    public static final class FileSourceInfoBuilder {
        private String alias;
        private String type="";
        private String host="";
        private String port="";
        private String path="";
        private String whereClause="";
        private String timeStampColumn="";
        private String time="";
        private String addSource="";
        private String sql="";
        private ArrayList<Col2> colList;

        private FileSourceInfoBuilder() {
        }

        public static FileSourceInfoBuilder aFileSourceInfo() {
            return new FileSourceInfoBuilder();
        }

        public FileSourceInfoBuilder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public FileSourceInfoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public FileSourceInfoBuilder host(String host) {
            this.host = host;
            return this;
        }

        public FileSourceInfoBuilder port(String port) {
            this.port = port;
            return this;
        }

        public FileSourceInfoBuilder path(String path) {
            this.path = path;
            return this;
        }

        public FileSourceInfoBuilder whereClause(String whereClause) {
            this.whereClause = whereClause;
            return this;
        }

        public FileSourceInfoBuilder timeStampColumn(String timeStampColumn) {
            this.timeStampColumn = timeStampColumn;
            return this;
        }

        public FileSourceInfoBuilder time(String time) {
            this.time = time;
            return this;
        }

        public FileSourceInfoBuilder addSource(String addSource) {
            this.addSource = addSource;
            return this;
        }

        public FileSourceInfoBuilder sql(String sql) {
            this.sql = sql;
            return this;
        }

        public FileSourceInfoBuilder colList(ArrayList<Col2> colList) {
            this.colList = colList;
            return this;
        }

        public FileSourceInfo build() {
            FileSourceInfo fileSourceInfo = new FileSourceInfo();
            fileSourceInfo.setAlias(alias);
            fileSourceInfo.setType(type);
            fileSourceInfo.setHost(host);
            fileSourceInfo.setPort(port);
            fileSourceInfo.setPath(path);
            fileSourceInfo.setWhereClause(whereClause);
            fileSourceInfo.setTimeStampColumn(timeStampColumn);
            fileSourceInfo.setTime(time);
            fileSourceInfo.setAddSource(addSource);
            fileSourceInfo.setSql(sql);
            fileSourceInfo.setColList(colList);
            return fileSourceInfo;
        }
    }
}
