package scala.test

import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object print extends App {
  val spark = SparkSession.builder()
    .config("spark.io.compression.codec", "snappy")
    .config("hive.metastore.uris", "thrift://master:9083").master("local").enableHiveSupport().getOrCreate()

  import spark.sql

  //    val jdbcDF = spark
  //      .read
  //      .format("jdbc")
  //      .option("url", "jdbc:oracle:thin:@192.168.31.100:1521:orcl")
  //      .option("user", "root")
  //      .option("password", "csasc")
  //      .option("driver", "oracle.jdbc.driver.OracleDriver")
  //      .option("dbtable", "type")
  //      .load()
  //
  //
  //    jdbcDF.show()
  //    jdbcDF.printSchema()

  //        val jdbcDF = spark
  //        .read
  //        .format("jdbc")
  //        .option("url", "jdbc:mysql://192.168.31.100:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC")
  //        .option("user", "root")
  //        .option("password", "csasc")
  //        .option("driver", "com.mysql.cj.jdbc.Driver")
  //        .option("dbtable", "type")
  //        .load()
  //      jdbcDF.show()
  //      jdbcDF.printSchema()

  //  val hiveDF = sql("select * from test")
  //  hiveDF.show()
  //  hiveDF.printSchema()

  val csvDF = spark.read
    .format("csv")
    .option("header", true)
    .load("hdfs://master:9000/user/xhb")
  csvDF.createOrReplaceTempView("table")
  sql("select * from table").show()

  //  csvDF.show
  //  csvDF.printSchema()
  //  val csvDF2 = csvDF.select(csvDF("id").cast("int"),csvDF("dou").cast("double"))
  //  csvDF.where("id=1 and dou=1.23").show
  //  csvDF2.where("id=1 and dou=1.23").show

  //  csvDF2.show()
  //  csvDF2.printSchema()

  //  val join=jdbcDF.join(csvDF2,jdbcDF("id")===csvDF2("id")&&jdbcDF("dou")===csvDF2("dou"),"left")
  //  join.show()
  //  join.printSchema()

  //  csvDF.foreachPartition(x => {
  //    val sql = "insert into type values(?,?,?,?,?,?,?,?,?)"
  //    new JDBCTest().Insert(sql, x)
  //  })
}