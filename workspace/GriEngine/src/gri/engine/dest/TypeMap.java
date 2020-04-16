package gri.engine.dest;



public class TypeMap {

	public static String jdbcTypeToOracle(Column column) {
		String typeName=column.getColumnTypeName();		
		if(column.getColumnTypeName().toLowerCase().indexOf("unsigned")!=-1){
			//防止类似`id` INT UNSIGNED(10)情况出现
			typeName=column.getColumnTypeName().substring(0, column.getColumnTypeName().toLowerCase().indexOf("unsigned"));	
		}
		switch (typeName) {
		case "VARCHAR2":
			return "VARCHAR2(255)";
		case "NUMBER":
			if(Integer.parseInt(column.getScale())<-84) {
				return "NUMBER";
			}
			else if(Integer.parseInt(column.getScale())!=0){
				if(Integer.parseInt(column.getPrecision())>38){return "FLOAT";}
				else if(Integer.parseInt(column.getScale())<-84) return "NUMBER";
				else return "NUMBER("+column.getPrecision()+","+column.getScale()+")";
			}
			else return "NUMBER("+column.getPrecision()+")";
		case "INT":
			return "NUMBER("+column.getPrecision()+")";
		case "MEDIUMINT":
			return "NUMBER("+column.getPrecision()+")";
		case "RAW":
			return "RAW(255)";
		case "BINARY_DOUBLE":
			return "BINARY_DOUBLE";
		case "BINARY_FLOAT":
			return "BINARY_FLOAT";
		case "LONGNVARCHAR":
			return "VARCHAR2(255)"; // LONGNVARCHAR
		case"YEAR":
			return "NUMBER";
		case "NCHAR":
			return "VARCHAR2(255)"; // NCHAR
		case "NVARCHAR":
			return "VARCHAR2(255)"; // NVARCHAR
		case "ROWID":
			return "ROWID"; // ROWID
		case "BIT":
			return "RAW(255)"; // BIT
		case "TINYINT":
			return "NUMBER("+column.getPrecision()+")";
		case "BIGINT":
			return "NUMBER("+column.getPrecision()+")";
		case "LONGVARBINARY":
			return "RAW(2000)"; // LONGVARBINARY
		case "VARBINARY":
			return "RAW(2000)"; // VARBINARY
		case "BINARY":
			return "RAW(2000)"; // BINARY
		case "LONGVARCHAR":
			return "CLOB"; // LONGVARCHAR
		case "NULL":
			return "NULL"; // NULL
		case "LONGBLOB":
			return "BLOB";
		case "CHAR":
			return "CHAR(2000)"; // CHAR
		case "NUMERIC":
			return "NUMBER("+column.getPrecision()+","+column.getScale()+")"; // NUMERIC
		case "DECIMAL":
			return "NUMBER("+column.getPrecision()+","+column.getScale()+")"; // DECIMAL
		case "INTEGER":
			return "NUMBER("+column.getPrecision()+")";// INTEGER
		case "SMALLINT":
			return "NUMBER("+column.getPrecision()+")"; // SMALLINT
		case "FLOAT":
			return "FLOAT";// FLOAT
		case "REAL":
			return "FLOAT(24)";// REAL
		case "DOUBLE":
			return "FLOAT(24)";// DOUBLE
		case "VARCHAR":
			return "VARCHAR2(255)"; // VARCHAR
		case "BOOLEAN":
			return "NUMBER"; // BOOLEAN
		case "DATALINK":
			return ""; // DATALINK
		case "DATE":
			return "DATE"; // DATE
		case "DATETIME":
			return "DATE";
		case "TIME":
			return ""; // TIME
		case "TIMESTAMP":
			return "TIMESTAMP";
		case "OTHER":
			return ""; // OTHER
		case "JAVA_OBJECT":
			return ""; // JAVA_OBJECT
		case "DISTINCT":
			return ""; // DISTINCT
		case "STRUCT":
			return ""; // STRUCT
		case "ARRAY":
			return ""; // ARRAY
		case "BLOB":
			return "BLOB"; // BLOB
		case "CLOB":
			return "CLOB"; // CLOB
		case "REF":
			return ""; // REF
		case "SQLXML":
			return ""; // SQLXML
		case "NCLOB":
			return "CLOB"; // NCLOB
		default:
			return typeName;
		}
		}
//number,double int,integer,float,decimal,binary_double,binary_float,tinyInt,mediumint,bigint,numeric,smallint float,
	public static String jdbcTypeToMysql(Column column) {
		String typeName=column.getColumnTypeName().toUpperCase();		
		if(column.getColumnTypeName().toLowerCase().indexOf("unsigned")!=-1){
			//防止类似`id` INT UNSIGNED(10)情况出现
			typeName=column.getColumnTypeName().substring(0, column.getColumnTypeName().toLowerCase().indexOf("unsigned"));	
		}
		switch (typeName) {
		case "VARCHAR2":
			return Integer.parseInt(column.getPrecision())>3000?"TEXT":"VARCHAR("+column.getPrecision()+")"; // VARCHAR
		case "NUMBER"://这部分需要处理
		    if(Integer.parseInt(column.getScale())==0){
		    	if(Integer.parseInt(column.getPrecision())==0) {return "BIGINT";};
		    	if(Integer.parseInt(column.getPrecision())>0&&Integer.parseInt(column.getPrecision())<=3){return "TINYINT";};
		        if(Integer.parseInt(column.getPrecision())<=5){return "SMALLINT";};
		        if(Integer.parseInt(column.getPrecision())<=7){return "MEDIUMINT";};
		        if(Integer.parseInt(column.getPrecision())<=10){return "INT";}
		        else return "BIGINT";
		    }
		    else{
		    	if(Integer.parseInt(column.getPrecision())>=38||Integer.parseInt(column.getScale())<=-84
		    			||Integer.parseInt(column.getPrecision())==Integer.parseInt(column.getScale())){return "DOUBLE";}
		    	else return "DECIMAL("+column.getPrecision()+","+column.getScale()+")";
		    }
		case "RAW":
			return "VARBINARY(255)";
		case "BINARY_DOUBLE":
			return "DOUBLE";
		case "BINARY_FLOAT":
			return "FLOAT";
		case "LONGNVARCHAR":
			return "LONGNVARCHAR"; // LONGNVARCHAR
		case "INT":
			return "INT";
		case "MEDIUMINT":
			return "MEDIUMINT";
		case"YEAR":
			return "YEAR";
		case "NCHAR":
			return "NCHAR(255)"; // NCHAR
		case "NVARCHAR":
			return "NVARCHAR"; // NVARCHAR
		case "ROWID":
			return ""; // ROWID
		case "BIT":
			return "BIT"; // BIT
		case "TINYINT":
			return "TINYINT"; // TINYINT
		case "BIGINT":
			return "BIGINT"; // BIGINT
		case "LONGVARBINARY":
			return "LONGVARBINARY"; // LONGVARBINARY
		case "VARBINARY":
			return "VARBINARY(255)"; // VARBINARY
		case "BINARY":
			return "BINARY(255)"; // BINARY
		case "LONGVARCHAR":
			return "LONGVARCHAR"; // LONGVARCHAR
		case "NULL":
			return "NULL"; // NULL
		case "CHAR":
			return "CHAR(255)"; // CHAR
		case "NUMERIC":
			return "NUMERIC(20,5)"; // NUMERIC
		case "DECIMAL":
			if(Integer.parseInt(column.getScale())>=Integer.parseInt(column.getPrecision())) {return "DECIMAL(20,5)";}
			return "DECIMAL("+column.getPrecision()+","+column.getScale()+")";
		case "INTEGER":
			return "INTEGER"; // INTEGER
		case "SMALLINT":
			return "SMALLINT"; // SMALLINT
		case "FLOAT":
			if(Integer.parseInt(column.getScale())>=Integer.parseInt(column.getPrecision())){return "FLOAT";}
			else return "FLOAT("+column.getPrecision()+","+column.getScale()+")"; // DOUBLE
		case "REAL":
			return "REAL"; // REAL
		case "DOUBLE":
			if(Integer.parseInt(column.getScale())>=Integer.parseInt(column.getPrecision())){return "DOUBLE";}
			else return "DOUBLE("+column.getPrecision()+","+column.getScale()+")"; // DOUBLE
		case "VARCHAR":
			if(Integer.parseInt(column.getPrecision())==0) {return "VARCHAR(255)";}
			else
			return Integer.parseInt(column.getPrecision())>3000?"TEXT":"VARCHAR("+column.getPrecision()+")"; // VARCHAR
		case "BOOLEAN":
			return "BOOLEAN"; // BOOLEAN
		case "DATALINK":
			return ""; // DATALINK
		case "DATE":
			return "DATE"; // DATE
		case"DATETIME":
			return "DATETIME";
		case "TIME":
			return "TIME"; // TIME
		case "TIMESTAMP":
			return "TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00'";
		case "OTHER":
			return ""; // OTHER
		case "JAVA_OBJECT":
			return ""; // JAVA_OBJECT
		case "DISTINCT":
			return ""; // DISTINCT
		case "STRUCT":
			return ""; // STRUCT
		case "ARRAY":
			return ""; // ARRAY
		case "BLOB":
			return "LONGBLOB"; // BLOB
		case "CLOB":
			return "LONGTEXT"; // CLOB
		case "REF":
			return ""; // REF
		case "SQLXML":
			return ""; // SQLXML
		case "NCLOB":
			return "LONGTEXT"; // NCLOB
		case "MONEY":
			return "DOUBLE";
		case "TID":
			return "VARCHAR(30)";
		case "SMALLDATETIME":
			return "DATETIME";
		default:
			return typeName;
		}
		}
	//number,double int,integer,float,decimal,binary_double,binary_float,tinyInt,mediumint,bigint,numeric,smallint float,
	public static boolean isNumber(String colType){
		if(colType.toLowerCase().indexOf("unsigned")!=-1){
			//防止类似`id` INT UNSIGNED(10)情况出现
			colType=colType.substring(0, colType.toLowerCase().indexOf("unsigned"));	
		}
		switch(colType){
		case "NUMBER":
			return true;
		case "DOUBLE":
			return true;
		case "FLOAT":
			return true;
		case "INT":
			return true;
		case "INTEGER":
			return true;
		case "DECIMAL":
			return true;
		case "BINARY_DOUBLE":
			return true;
		case "BINARY_FLOAT":
			return true;
		case "TINYINT":
			return true;
		case "MEDIUMINT":
			return true;
		case "BIGINT":
			return true;
		case "NUMERIC":
			return true;
		case "SMALLINT":
			return true;
		case "REAL":
			return true;
		default:
			return false;
		}
	}
}

