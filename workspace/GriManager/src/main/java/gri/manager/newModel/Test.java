package gri.manager.newModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
//        String s="{\"sourceType\":\"Database\",\"type\":\"MySQL\",\"partitionColumn\":\"\",\"lowerBound\":-1,\"upperBound\":-1,\"partitionCount\":-1,\"userName\":\"root\",\"password\":\"csasc\",\"dbName\":\"test\",\"tableName\":\"type\",\"whereClause\":\"\",\"timeStampColumn\":\"\",\"time\":\"\",\"addSource\":\"编写Sql语句\",\"host\":\"localhost\",\"port\":\"3306\",\"sql\":\"\"}";
//
//        String s2="{\"sourceType\":\"Database\",\"type\":\"MySQL\",\"partitionColumn\":\"\",\"lowerBound\":-1,\"upperBound\":-1,\"partitionCount\":-1,\"userName\":\"root\",\"password\":\"csasc\",\"dbName\":\"test\",\"tableName\":\"type\",\"whereClause\":\"\",\"timeStampColumn\":\"\",\"time\":\"\",\"addSource\":\"选择字段\",\"host\":\"localhost\",\"port\":\"3306\",\"sql\":\"\",\"colList\":[{\"modifiedType\":\"\",\"name\":\"id\",\"srcType\":\"integer\"},{\"modifiedType\":\"\",\"name\":\"dou\",\"srcType\":\"double\"},{\"modifiedType\":\"\",\"name\":\"flo\",\"srcType\":\"double\"}]}";
//        String s3="{\"sourceType\":\"Database\",\"type\":\"MySQL\",\"partitionColumn\":\"id\",\"lowerBound\":23,\"upperBound\":21345,\"partitionCount\":1,\"userName\":\"root\",\"password\":\"csasc\",\"dbName\":\"test\",\"tableName\":\"type\",\"whereClause\":\"\",\"timeStampColumn\":\"\",\"time\":\"\",\"addSource\":\"选择字段\",\"host\":\"localhost\",\"port\":\"3306\",\"sql\":\"\",\"colList\":[{\"modifiedType\":\"\",\"name\":\"id\",\"srcType\":\"integer\"},{\"modifiedType\":\"\",\"name\":\"dou\",\"srcType\":\"double\"},{\"modifiedType\":\"\",\"name\":\"flo\",\"srcType\":\"double\"}]}";
////        DBSourceInfo info = new DBSourceInfo();
////        ArrayList<String> list=new ArrayList<>();
////        list.add("sdawe");
////        list.add("dawe");
////        list.add("awe");
////        list.add("we");
////        list.add("swedawe");
////        list.add("aswe");
////        list.add("sdaasdwe");
////        info = info.builder().dbName("test").addSource("test").host("localhost").lowerBound(1).partitionColumn("sadas").test(list).build();
//
//        DBSourceInfo info=JSON.parseObject(s3,DBSourceInfo.class);
//        DBSourceInfo info2=JSON.parseObject(s3,DBSourceInfo.class);
//        System.out.println(info.equals(info2));

        String s="{\"sourceList\":[{\"sourceType\":\"Database\",\"alias\":\"test1\",\"type\":\"MySQL\",\"partitionColumn\":\"\",\"lowerBound\":-1,\"upperBound\":-1,\"partitionCount\":-1,\"userName\":\"root\",\"password\":\"csasc\",\"dbName\":\"test\",\"tableName\":\"test1\",\"whereClause\":\"\",\"timeStampColumn\":\"\",\"time\":\"\",\"addSource\":\"选择字段\",\"host\":\"localhost\",\"port\":\"3306\",\"sql\":\"\",\"colList\":[{\"name\":\"id\",\"srcType\":\"integer\",\"modifiedType\":\"\"},{\"name\":\"name\",\"srcType\":\"string\",\"modifiedType\":\"\"}]}],\"joinInfos\":[],\"insertMapInfo\":{\"dataSourceTableName\":\"test1\",\"destInfo\":{\"destType\":\"Database\",\"type\":\"MySQL\",\"host\":\"localhost\",\"port\":\"3306\",\"dbName\":\"test\",\"userName\":\"root\",\"password\":\"csasc\",\"tableName\":\"type\",\"mode\":\"覆盖\"},\"insertColumnsMap\":[\"id->id\",\"name->dou2\"]},\"syncConfig\":{\"syncType\":\"全量同步\",\"syncMethod\":\"warm\",\"syncTimeConfig\":\"0#0 0 0/2 * * ?\"}}";
//        String s="{\"dataSource\":\"test1\",\"destInfo\":{\"destType\":\"Database\",\"type\":\"MySQL\",\"host\":\"localhost\",\"port\":\"3306\",\"dbName\":\"test\",\"userName\":\"root\",\"password\":\"csasc\",\"tableName\":\"type\",\"mode\":\"覆盖\"},\"insertColumnsMap\":[\"id->id\",\"name->dat2\",\"id->var2\"]}";
//        String s2="{\"dataSource\":\"test1\",\"destInfo\":{\"destType\":\"Hive\",\"host\":\"master\",\"dbName\":\"default\",\"tableName\":\"test\",\"mode\":\"覆盖\",\"port\":\"9083\"},\"selectedColumns\":[\"id\",\"name\"]}";
        JSONObject object=JSON.parseObject(s).getJSONObject("insertMapInfo").getJSONObject("destInfo");
        if(object.get("destType").equals("Database")) {
            IntegrateModel model = JSON.parseObject(s, new TypeReference<IntegrateModel<DBDestInfo>>() {});
            System.out.println(model.toString());
            System.out.println(JSON.toJSONString(model));
            System.out.println(model.getSyncConfig().getSyncTimeConfig());
            if(((DBDestInfo)model.getInsertMapInfo().getDestInfo()).getDestType().equals("Database")){
                model.getInsertMapInfo().getInsertColumnsMap().forEach(x -> System.out.println(x));
            }
        }else if(object.get("destType").equals("Hive")){
            IntegrateModel model = JSON.parseObject(s, new TypeReference<IntegrateModel<HiveDestInfo>>() {});
            System.out.println(model.toString());
            System.out.println(JSON.toJSONString(model));
            System.out.println(model.getSyncConfig().getSyncTimeConfig());
            if (((HiveDestInfo) model.getInsertMapInfo().getDestInfo()).getDestType().equals("Hive")) {
                model.getInsertMapInfo().getSelectedColumns().forEach(x -> System.out.println(x));
            }
        }

        JSONObject json=JSON.parseObject(s);
        JSONArray array=json.getJSONArray("sourceList");
        for(int i=0;i<array.size();i++){
            JSONObject jsonObject=array.getJSONObject(i);
            if(jsonObject.get("sourceType").equals("Database")){
                DBSourceInfo sourceInfo=JSON.parseObject(jsonObject.toJSONString(),DBSourceInfo.class);
                System.out.println(JSON.toJSONString(sourceInfo));
            }
        }



    }

}
