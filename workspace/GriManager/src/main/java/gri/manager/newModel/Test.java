package gri.manager.newModel;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        String s="{\"sourceType\":\"Database\",\"type\":\"MySQL\",\"partitionColumn\":\"\",\"lowerBound\":-1,\"upperBound\":-1,\"partitionCount\":-1,\"userName\":\"root\",\"password\":\"csasc\",\"dbName\":\"test\",\"tableName\":\"type\",\"whereClause\":\"\",\"timeStampColumn\":\"\",\"time\":\"\",\"addSource\":\"编写Sql语句\",\"host\":\"localhost\",\"port\":\"3306\",\"sql\":\"\"}";

        String s2="{\"sourceType\":\"Database\",\"type\":\"MySQL\",\"partitionColumn\":\"\",\"lowerBound\":-1,\"upperBound\":-1,\"partitionCount\":-1,\"userName\":\"root\",\"password\":\"csasc\",\"dbName\":\"test\",\"tableName\":\"type\",\"whereClause\":\"\",\"timeStampColumn\":\"\",\"time\":\"\",\"addSource\":\"选择字段\",\"host\":\"localhost\",\"port\":\"3306\",\"sql\":\"\",\"colList\":[{\"modifiedType\":\"\",\"name\":\"id\",\"srcType\":\"integer\"},{\"modifiedType\":\"\",\"name\":\"dou\",\"srcType\":\"double\"},{\"modifiedType\":\"\",\"name\":\"flo\",\"srcType\":\"double\"}]}";
        String s3="{\"sourceType\":\"Database\",\"type\":\"MySQL\",\"partitionColumn\":\"id\",\"lowerBound\":23,\"upperBound\":21345,\"partitionCount\":1,\"userName\":\"root\",\"password\":\"csasc\",\"dbName\":\"test\",\"tableName\":\"type\",\"whereClause\":\"\",\"timeStampColumn\":\"\",\"time\":\"\",\"addSource\":\"选择字段\",\"host\":\"localhost\",\"port\":\"3306\",\"sql\":\"\",\"colList\":[{\"modifiedType\":\"\",\"name\":\"id\",\"srcType\":\"integer\"},{\"modifiedType\":\"\",\"name\":\"dou\",\"srcType\":\"double\"},{\"modifiedType\":\"\",\"name\":\"flo\",\"srcType\":\"double\"}]}";
//        DBSourceInfo info = new DBSourceInfo();
//        ArrayList<String> list=new ArrayList<>();
//        list.add("sdawe");
//        list.add("dawe");
//        list.add("awe");
//        list.add("we");
//        list.add("swedawe");
//        list.add("aswe");
//        list.add("sdaasdwe");
//        info = info.builder().dbName("test").addSource("test").host("localhost").lowerBound(1).partitionColumn("sadas").test(list).build();

        DBSourceInfo info=JSON.parseObject(s3,DBSourceInfo.class);
        DBSourceInfo info2=JSON.parseObject(s3,DBSourceInfo.class);
        System.out.println(info.equals(info2));



//        System.out.println(info.getTest().size());
//        System.out.println(info.toString());
//        String jsonString= JSON.toJSONString(info);
//        System.out.println(jsonString);

//        DBSourceInfo info2=JSON.parseObject(s,DBSourceInfo.class);
//        DBSourceInfo info3=JSON.parseObject(s,DBSourceInfo.class);
//
//        System.out.println(info2.equals(info3));

//        Gson gson=new Gson();
//        System.out.println(gson.toJson(info));
//        System.out.println(JSON.toJSONString(info));

    }
}
