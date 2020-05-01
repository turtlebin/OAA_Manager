package test

import org.apache.spark.sql.jdbc.JdbcDialect;

 case object MariaSqlDialect extends JdbcDialect {
  override def canHandle(url : String): Boolean = url.startsWith("jdbc:mariadb")
  override def quoteIdentifier(colName: String): String = {
    s"$colName"
  }
}