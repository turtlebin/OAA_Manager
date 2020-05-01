package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class JoinInfo {
    @JSONField(ordinal = 1)
    private String left;
    @JSONField(ordinal = 2)
    private String right;
    @JSONField(ordinal = 3)
    private String joinType;
    @JSONField(ordinal =4)
    private String using;
    @JSONField(ordinal = 5)
    private String joinInfo;
    @JSONField(ordinal = 6)
    private String tempTableName;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof JoinInfo) {
            if(this.tempTableName.equals(((JoinInfo) obj).tempTableName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
//        result = 31 * result + (left == null ? 0 : left.hashCode());
//        result = 31 * result + (right == null ? 0 : right.hashCode());
//        result = 31 * result + (joinType == null ? 0 : joinType.hashCode());
        result = 31 * result + (tempTableName == null ? 0 : tempTableName.hashCode());
        return result;
    }
}
