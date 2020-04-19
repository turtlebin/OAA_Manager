package Support

import java.util

import gri.manager.newModel.{Col2, DBSourceInfo, FileSourceInfo, HiveSourceInfo}
import org.apache.spark.sql.{Column, DataFrame}
import scala.collection.JavaConverters


object DataFrameHelper {
  val spark = ContextManager.getSparkContext()
  import spark.implicits._
  import spark.sql
  def getDBDataFrame(sourceInfo: DBSourceInfo, driver: String, url: String):DataFrame = {
    val df = spark.read.format("jdbc")
      .option("url", url)
      .option("driver", driver)
      .option("user", sourceInfo.getUserName)
      .option("password", sourceInfo.getPassword)
    if (sourceInfo.getAddSource.equals("编写Sql语句")) {
      df.option("dbtable", s"(${sourceInfo.getSql}) ${sourceInfo.getTableName}")
    } else {
      df.option("dbtable", sourceInfo.getTableName)
    }
    if (!(sourceInfo.getPartitionColumn.equals("") || sourceInfo.getLowerBound.equals(-1)
      || sourceInfo.getUpperBound.equals(-1) || sourceInfo.getPartitionCount.equals(-1))) {
      df.option("partitionColumn", sourceInfo.getPartitionColumn)
      df.option("lowerBound",sourceInfo.getLowerBound)
      df.option("upperBound",sourceInfo.getUpperBound)
      df.option("numPartitions",sourceInfo.getPartitionCount)
    }
    val df2=df.load()
    if(!sourceInfo.getAddSource.equals("编写Sql语句")){
    var df3=if(sourceInfo.getColList!=null&&sourceInfo.getColList.size()>0) {
      df2.select(getColumns(df2, sourceInfo.getColList): _*)
    }else {
      df2
    }
    df3=if(!sourceInfo.getWhereClause.equals("")&&(!sourceInfo.getAddSource.equals("编写Sql语句"))){
        df3.where(sourceInfo.getWhereClause)
      }else{
        df3
      }
    df3=if(!(sourceInfo.getTimeStampColumn.equals("")||sourceInfo.getTime.equals(""))&&(!sourceInfo.getAddSource.equals("编写Sql语句"))){
        val timeStampColumn=sourceInfo.getTimeStampColumn
        df3.where(df3.col(timeStampColumn)>=sourceInfo.getTime)
      }else{
        df3
      }
      df3.show()
      df3.printSchema()
      df3
    }else{
      df2.show()
      df2.printSchema()
      df2
    }
  }

  def getHiveDataFrame(sourceInfo:HiveSourceInfo):DataFrame={
    if(sourceInfo.getAddSource.equals("编写Sql语句")){
     val df= sql(sourceInfo.getSql)
      df.show
      df.printSchema()
      df
    }else{
      val s=if(sourceInfo.getDbName.equals(""))
        s"select * from ${sourceInfo.getTableName}"
      else
        s"select * from ${sourceInfo.getDbName}.${sourceInfo.getTableName}"
      val df=sql(s)
      var df2=df.select(getColumns(df,sourceInfo.getColList): _*)
      df2=if(!sourceInfo.getWhereClause.equals("")){
        df2.where(sourceInfo.getWhereClause)
      }else{
        df2
      }
      df2=if(!(sourceInfo.getTimeStampColumn.equals("")||sourceInfo.getTime.equals(""))){
        val timeStampColumn=sourceInfo.getTimeStampColumn
        df2.where(df2.col(timeStampColumn)>=sourceInfo.getTime)
      }else{
        df2
      }
      df2.printSchema()
      df2.show()
      df2
    }
  }

  def getFileDataFrame(sourceInfo:FileSourceInfo,url:String):DataFrame={
    val df=spark.read
      .format(sourceInfo.getType)
      .option("header",true)
      .load(url)
    if(sourceInfo.getAddSource.equals("编写Sql语句")){
      val tableName=getFileTableName(sourceInfo)
      df.createOrReplaceTempView(tableName)
      //由于文件没有表名的限制，因此这里输入的sql语句必须为getFileTableName中返回的tableName
      //且必须只能查询该表
      val df3=sql(sourceInfo.getSql)
      df3.show()
      df3.printSchema()
      df3
    }else{
      var df2=df.select(getColumns(df,sourceInfo.getColList): _*)
      df2=if(!sourceInfo.getWhereClause.equals("")){
        df2.where(sourceInfo.getWhereClause)
      }else{
        df2
      }
      df2=if(!(sourceInfo.getTimeStampColumn.equals("")||sourceInfo.getTime.equals(""))){
        val timeStampColumn=sourceInfo.getTimeStampColumn
        df2.where(df2.col(timeStampColumn)>=sourceInfo.getTime)
      }else{
        df2
      }
      df2.show()
      df2.printSchema()
      df2
    }
  }

  def getCast(col:Column,cast:String):Column={
    if(cast.equals("")){
      col
    }else{
      col.cast(cast)
    }
  }

  def getColumns(df:DataFrame,colList:util.ArrayList[Col2]):Seq[Column] ={
    val seq=JavaConverters.asScalaIteratorConverter(colList.iterator()).asScala.toSeq
    val cols=seq.map(x=>getCast(df(x.getName),x.getModifiedType))
    cols
  }

  def getFileTableName(sourceInfo:FileSourceInfo): String ={
   sourceInfo.getPath.split("/").reverse.filter(x=>(!x.equals("*"))).head
  }

}
