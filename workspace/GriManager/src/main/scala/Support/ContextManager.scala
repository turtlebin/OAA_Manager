package Support

import org.apache.spark.sql.SparkSession

object ContextManager {
  val spark = SparkSession.builder()
    .config("spark.io.compression.codec", "snappy")
    .config("hive.metastore.uris", "thrift://master:9083")
    .master("local")
    .enableHiveSupport()
    .getOrCreate()

  def getSparkContext(): SparkSession ={
    spark
  }
}
