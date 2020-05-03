package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.apache.spark.sql.types.DataType;

@Data
public class Col2 {
    @JSONField(ordinal = 1)
    private String name;
    @JSONField(ordinal = 2)
    private String srcType;
    @JSONField(ordinal = 3)
    private String modifiedType;

    public Col2(String name, String srcType, String modifiedType) {
        this.name = name;
        this.srcType = srcType;
        this.modifiedType = modifiedType;
    }

    public String getName(){
        return this.name;
    }

    public String getSrcType(){
        return this.srcType;
    }

    public String getModifiedType(){
        return this.modifiedType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Col2) {
            if (this.name.equals(((Col2) obj).name) &&
                    this.srcType.equals(((Col2) obj).srcType) &&
                    this.modifiedType.equals(((Col2) obj).modifiedType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        int result=17;
        result=31*result+(name==null?0:name.hashCode());
        result=31*result+(srcType==null?0:srcType.hashCode());
        result=31*result+(modifiedType==null?0:modifiedType.hashCode());
        return result;
    }

}
