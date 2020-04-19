package Support

import org.apache.spark.sql.DataFrame
import gri.manager.newModel.Col
object SourceInfoHelper {
  val spark = ContextManager.getSparkContext()
  import spark.sql
  import spark.implicits._
  def getSourceInfo(sourceType: String, driver: String, url: String,
                    fileType: String, db_name: String, tableName: String, userName: String, password: String) = {
    val result = sourceType match {
      case "DATABASE" =>getDatabaseSourceInfo(driver,url,tableName,userName,password)
      case "HIVE" =>getHiveSourceInfo(db_name,tableName)
      case "FILE" =>getFileSourceInfo(fileType,url)
    }
    val sourceInfo=result.first().schema.fields.map(x=>new Col(x.name,x.dataType))
    sourceInfo
  }

  def getDatabaseSourceInfo(driver: String, url: String,tableName: String,userName:String,password:String): DataFrame = {
    val df=spark.read.format("jdbc")
      .option("url", url)
      .option("driver", driver)
      .option("dbtable",tableName)
      .option("user",userName)
      .option("password",password)
      .load()
    df
  }

  def getHiveSourceInfo(db_name:String,tableName: String): DataFrame ={
    val df=sql(s"select * from $db_name.$tableName")
    df
  }

  def getFileSourceInfo(fileType:String,url:String): DataFrame ={
    val df=spark.read
      .format(fileType)
      .option("header",true)
      .load(url)
    df
  }

}
