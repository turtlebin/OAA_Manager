package test

import java.util.Properties

import org.apache.spark.TaskContext
import org.apache.spark.sql.jdbc.JdbcDialects
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}

object print extends App {
  val spark = SparkSession.builder()
    .config("spark.io.compression.codec", "snappy")
    .config("hive.metastore.uris", "thrift://master:9083")
    .config("spark.sql.shuffle.partitions","3").master("local[3]").enableHiveSupport().getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")
  val mariadbSqlDialect = MariaSqlDialect
  JdbcDialects.registerDialect(mariadbSqlDialect)

  import spark.implicits._
  import spark.sql

//  val jdbcDF = spark
//    .read
//    .format("jdbc")
//    .option("url", "jdbc:oracle:thin:@192.168.31.100:1521:orcl")
//    .option("user", "root")
//    .option("password", "csasc")
//    .option("driver", "oracle.jdbc.driver.OracleDriver")
//    .option("dbtable", "type")
//    .load()
//
//  jdbcDF.show()
//  jdbcDF.printSchema()
//
  val jdbcDF2 = spark
    .read
    .format("jdbc")
    //    .option("url", "jdbc:mysql://192.168.31.100:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC")
    //    .option("driver", "com.mysql.cj.jdbc.Driver")
    .option("url", "jdbc:mariadb://192.168.31.100:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC")
    .option("driver", "org.mariadb.jdbc.Driver")
    .option("user", "root")
    .option("password", "csasc")
    .option("dbtable", "test1")
  .option("lowerBound","1")
  .option("upperBound","10")
  .option("partitionColumn","id")
  .option("numPartitions",3)
    .load()
  jdbcDF2.printSchema()
  //通过这轮测试可以发现，1.spark写入数据库是每个分区创建一个连接池然后单线程插入的 2.对于排序后的数据，插入数据库后会乱序，并不会保持真正的顺序。3.排序时用的并不是范围分区
  val jdbcDF3=jdbcDF2.orderBy($"id")
  println((jdbcDF3.rdd.partitioner))
  jdbcDF3.foreach(x=>println(TaskContext.getPartitionId()+":"+x.get(0)))
  jdbcDF3.show()
  val properties=new Properties()
  properties.setProperty("user","root")
  properties.setProperty("password","csasc")
  jdbcDF3.write.mode(SaveMode.Overwrite).jdbc("jdbc:mariadb://192.168.31.100:3306/test?characterEncoding=utf8&useSSL=false&serverTimezone=UTC","test3",properties)

//
//  val df3 = jdbcDF.join(jdbcDF2, Seq("id"))
//  df3.show()
//  df3.printSchema()
//
//  val df4 = jdbcDF.join(jdbcDF2, jdbcDF.col("id")===jdbcDF2.col("id"),"left")
//  df4.show()
//  df4.printSchema()

//  jdbcDF2.write.mode(SaveMode.Append).saveAsTable("xhb.xhb2");
//  jdbcDF2.write.mode(SaveMode.Append).insertInto("default.xhb3")//这种必须hive表列的数量与dataframe列的数量一致。
//  jdbcDF2.createOrReplaceTempView("test");
//  val df4=sql("insert into table default.xhb2(id,name) select id,name from test")//这种语法并不支持
//  val df5=sql("select * from xhb2")
//  df5.show()
//  df5.printSchema()
//  df5.write.jdbc("","",new Properties())
//  df4.show()
//  df4.printSchema()
//    val hiveDF = sql("select * from test")
//    hiveDF.show()
//    hiveDF.printSchema()

  //  val csvDF = spark.read
  //    .format("csv")
  //    .option("header", true)
  //    .load("hdfs://master:9000/user/xhb")
  //  csvDF.createOrReplaceTempView("table")
  //  sql("select * from table").show()



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