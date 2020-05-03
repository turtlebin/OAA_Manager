package gri.manager.newModel;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

@Data
public class JoinInfo {
    @JSONField(ordinal = 1)
    private String tempTableName;
    @JSONField(ordinal = 2)
    private String left;
    @JSONField(ordinal = 3)
    private String right;
    @JSONField(ordinal = 4)
    private String joinType;
    @JSONField(ordinal =5)
    private String using;
    @JSONField(ordinal = 6)
    private String joinInfo;

    public JoinInfo(String tempTableName,String left,String right,String joinType,String using,String joinInfo){
         this.tempTableName=tempTableName;
         this.left=left;
         this.right=right;
         this.joinType=joinType;
         this.using=using;
         this.joinInfo=joinInfo;
    }

    public static JoinInfo.JoinInfoBuilder builder(){
        return new JoinInfo.JoinInfoBuilder();
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getUsing() {
        return using;
    }

    public void setUsing(String using) {
        this.using = using;
    }

    public String getJoinInfo() {
        return joinInfo;
    }

    public void setJoinInfo(String joinInfo) {
        this.joinInfo = joinInfo;
    }

    public String getTempTableName() {
        return tempTableName;
    }

    public void setTempTableName(String tempTableName) {
        this.tempTableName = tempTableName;
    }

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

    public static final class JoinInfoBuilder {
        private String left;
        private String right;
        private String joinType;
        private String using;
        private String joinInfo;
        private String tempTableName;

        private JoinInfoBuilder() {
        }

        public static JoinInfoBuilder aJoinInfo() {
            return new JoinInfoBuilder();
        }

        public JoinInfoBuilder left(String left) {
            this.left = left;
            return this;
        }

        public JoinInfoBuilder right(String right) {
            this.right = right;
            return this;
        }

        public JoinInfoBuilder joinType(String joinType) {
            this.joinType = joinType;
            return this;
        }

        public JoinInfoBuilder using(String using) {
            this.using = using;
            return this;
        }

        public JoinInfoBuilder joinInfo(String joinInfo) {
            this.joinInfo = joinInfo;
            return this;
        }

        public JoinInfoBuilder tempTableName(String tempTableName) {
            this.tempTableName = tempTableName;
            return this;
        }

        public JoinInfo build() {
            return new JoinInfo(tempTableName,left, right, joinType, using, joinInfo);
        }
    }
}
