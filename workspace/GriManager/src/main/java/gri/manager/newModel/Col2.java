package gri.manager.newModel;

import lombok.Data;
import org.apache.spark.sql.types.DataType;

@Data
public class Col2 {
    private String name;
    private String srcType;
    private String modifiedType;

    public Col2(String name, String srcType, String modifiedType) {
        this.name = name;
        this.srcType = srcType;
        this.modifiedType = modifiedType;
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
