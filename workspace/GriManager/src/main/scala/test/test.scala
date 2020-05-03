package test

import org.apache.spark.sql.jdbc.JdbcDialects
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

object test extends App {
  val spark = SparkSession.builder()
    .config("spark.io.compression.codec", "snappy")
    .config("hive.metastore.uris", "thrift://master:9083").master("local").enableHiveSupport().getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._
  import spark.sql

    val csvDF = spark.read
      .format("csv")
      .option("header", false)
      .load("hdfs://master:9000/user/xhb/t*/*")//允许一定程度的正则匹配
    csvDF.createOrReplaceTempView("table")
    sql("select * from table").show()

}