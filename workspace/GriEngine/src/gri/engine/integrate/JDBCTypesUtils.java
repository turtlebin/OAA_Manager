package gri.engine.integrate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class JDBCTypesUtils {

	private static Map<String, Integer> jdbcTypes; // Name to value
	private static Map<Integer, String> jdbcTypeValues; // value to Name
	private static Map<Integer, Class<?>> jdbcJavaTypes; // jdbc type to java
															// type
	static {
		jdbcTypes = new TreeMap<String, Integer>();
		jdbcTypeValues = new TreeMap<Integer, String>();
		jdbcJavaTypes = new TreeMap<Integer, Class<?>>();
		Field[] fields = java.sql.Types.class.getFields();
		for (int i = 0, len = fields.length; i < len; ++i) {
			if (Modifier.isStatic(fields[i].getModifiers())) {//getModifiers（）获取修饰符（如static final之类的）
				try {
					String name = fields[i].getName();
					Integer value = (Integer) fields[i]
							.get(java.sql.Types.class);//返回指定对象上此 Field 表示的字段的值。
					jdbcTypes.put(name, value);
					jdbcTypeValues.put(value, name);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
		// ��ʼ��jdbcJavaTypes��
		jdbcJavaTypes.put(new Integer(Types.LONGNVARCHAR), String.class); // -16
																			// �ַ���
		jdbcJavaTypes.put(new Integer(Types.NCHAR), String.class); // -15 �ַ���
		jdbcJavaTypes.put(new Integer(Types.NVARCHAR), String.class); // -9 �ַ���
		jdbcJavaTypes.put(new Integer(Types.ROWID), String.class); // -8 �ַ���
		jdbcJavaTypes.put(new Integer(Types.BIT), Boolean.class); // -7 ����
		jdbcJavaTypes.put(new Integer(Types.TINYINT), Byte.class); // -6 ����
		jdbcJavaTypes.put(new Integer(Types.BIGINT), Long.class); // -5 ����
		jdbcJavaTypes.put(new Integer(Types.LONGVARBINARY), Blob.class); // -4
																			// ������
		jdbcJavaTypes.put(new Integer(Types.VARBINARY), Blob.class); // -3 ������
		jdbcJavaTypes.put(new Integer(Types.BINARY), Blob.class); // -2 ������
		jdbcJavaTypes.put(new Integer(Types.LONGVARCHAR), String.class); // -1
																			// �ַ���
		// jdbcJavaTypes.put(new Integer(Types.NULL), String.class); // 0 /
		jdbcJavaTypes.put(new Integer(Types.CHAR), String.class); // 1 �ַ���
		jdbcJavaTypes.put(new Integer(Types.NUMERIC), BigDecimal.class); // 2 ����
		jdbcJavaTypes.put(new Integer(Types.DECIMAL), BigDecimal.class); // 3 ����
		jdbcJavaTypes.put(new Integer(Types.INTEGER), Integer.class); // 4 ����
		jdbcJavaTypes.put(new Integer(Types.SMALLINT), Short.class); // 5 ����
		
		/**
		 * ����ֻ�ǽ������ݸ�ֵ��������Ҫ���ȼ��㣬�˴���ʱ��������ת����
		 */
//		jdbcJavaTypes.put(new Integer(Types.FLOAT), BigDecimal.class); // 6 ����
		jdbcJavaTypes.put(new Integer(Types.REAL), BigDecimal.class); // 7 ����
//		jdbcJavaTypes.put(new Integer(Types.DOUBLE), BigDecimal.class); // 8 ����
		
		jdbcJavaTypes.put(new Integer(Types.FLOAT), Float.class); // 6 ����
		jdbcJavaTypes.put(new Integer(Types.DOUBLE), Double.class); // 8 ����
		
		jdbcJavaTypes.put(new Integer(Types.VARCHAR), String.class); // 12 �ַ���
		jdbcJavaTypes.put(new Integer(Types.BOOLEAN), Boolean.class); // 16 ����
		// jdbcJavaTypes.put(new Integer(Types.DATALINK), String.class); // 70 /
		jdbcJavaTypes.put(new Integer(Types.DATE), Date.class); // 91 ����
		jdbcJavaTypes.put(new Integer(Types.TIME), Date.class); // 92 ����
		jdbcJavaTypes.put(new Integer(Types.TIMESTAMP), Timestamp.class); // 93 ����
		jdbcJavaTypes.put(new Integer(Types.OTHER), Object.class); // 1111 �������ͣ�
		// jdbcJavaTypes.put(new Integer(Types.JAVA_OBJECT), Object.class); //
		// 2000
		// jdbcJavaTypes.put(new Integer(Types.DISTINCT), String.class); // 2001
		// jdbcJavaTypes.put(new Integer(Types.STRUCT), String.class); // 2002
		// jdbcJavaTypes.put(new Integer(Types.ARRAY), String.class); // 2003
		jdbcJavaTypes.put(new Integer(Types.BLOB), Blob.class); // 2004 ������
		jdbcJavaTypes.put(new Integer(Types.CLOB), Clob.class); // 2005 ���ı�
		// jdbcJavaTypes.put(new Integer(Types.REF), String.class); // 2006
		// jdbcJavaTypes.put(new Integer(Types.SQLXML), String.class); // 2009
		jdbcJavaTypes.put(new Integer(Types.NCLOB), Clob.class); // 2011 ���ı�
	}

	public static int getJdbcCode(String jdbcName) {
		return jdbcTypes.get(jdbcName);
	}

	public static String getJdbcName(int jdbcCode) {
		return jdbcTypeValues.get(jdbcCode);
	}

	public static Class<?> jdbcTypeToJavaType(int jdbcType) {
		return jdbcJavaTypes.get(jdbcType);
	}

	public static boolean isJavaNumberType(int jdbcType) {
		Class<?> type = jdbcJavaTypes.get(jdbcType);
		return (type == null) ? false
				: (Number.class.isAssignableFrom(type)) ? true : false;
	}

}