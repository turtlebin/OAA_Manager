package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.ArrayList;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSourceInfo implements DataSourceInfo{
    @JSONField(ordinal=1)
    private String type="";
    @JSONField(ordinal=2)
    private String host="";
    @JSONField(ordinal=3)
    private String port="";
    @JSONField(ordinal=4)
    private String path="";
    @JSONField(ordinal=5)
    private String whereClause="";
    @JSONField(ordinal=6)
    private String timeStampColumn="";
    @JSONField(ordinal=7)
    private String time="";
    @JSONField(ordinal=8)
    private String addSource="";
    @JSONField(ordinal = 9)
    private String sql="";
    @JSONField(ordinal = 10)
    private ArrayList<Col2> colList;

    public String getSourceType(){
        return "File";
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
}
