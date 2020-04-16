package gri.manager.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SQLParser {
	public static List<String> getTables(String sql) {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
		// String sql2 = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM
		// MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "
		// + " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT *
		// FROM MY_TABLE6)";

		try {
			Statement statement = pm.parse(new StringReader(sql));
			if (statement instanceof Select) {
				Select selectStatement = (Select) statement;
				return tablesNamesFinder.getTableList(selectStatement);
			} else if (statement instanceof Insert) {
				Insert insertStatement = (Insert) statement;
				return tablesNamesFinder.getTableList(insertStatement);
			} else if (statement instanceof Update) {
				Update updateStatement = (Update) statement;
				return tablesNamesFinder.getTableList(updateStatement);
			} else if (statement instanceof Delete) {
				Delete deleteStatement = (Delete) statement;
				return tablesNamesFinder.getTableList(deleteStatement);
			}
		} catch (JSQLParserException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public static boolean isSelectSQL(String sql) {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		try {
			Statement statement = pm.parse(new StringReader(sql));
			if (statement instanceof Select)
				return true;

		} catch (JSQLParserException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "
				+ " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT *	 FROM MY_TABLE6)";

		for (String s : SQLParser.getTables(sql))
			System.out.println("table name:" + s);

	}

}
