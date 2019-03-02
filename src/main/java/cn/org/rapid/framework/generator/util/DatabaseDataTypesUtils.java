package cn.org.rapid.framework.generator.util;
import java.sql.Types;
import java.util.HashMap;

import cn.org.rapid.framework.generator.provider.db.model.Column;

public class DatabaseDataTypesUtils {

	private final static IntStringMap _preferredJavaTypeForSqlType = new IntStringMap();

	public static boolean isFloatNumber(final int sqlType,final int size,final int decimalDigits) {
		final String javaType = getPreferredJavaType(sqlType,size,decimalDigits);
		if(javaType.endsWith("Float") || javaType.endsWith("Double") || javaType.endsWith("BigDecimal")) {
			return true;
		}
		return false;
	}

	public static boolean isIntegerNumber(final int sqlType,final int size,final int decimalDigits) {
		final String javaType = getPreferredJavaType(sqlType,size,decimalDigits);
		if(javaType.endsWith("Long") || javaType.endsWith("Integer") || javaType.endsWith("Short") || javaType.endsWith("Byte")) {
			return true;
		}
		return false;
	}

	public static boolean isDate(Column column,final int sqlType,final int size,final int decimalDigits) {
		final String javaType = getPreferredJavaType(sqlType,size,decimalDigits);
		if(javaType.endsWith("Date") || javaType.endsWith("Timestamp") || javaType.endsWith("Time")) {
			return true;
		}
		return false;
	}

	public static boolean isString(final int sqlType,final int size,final int decimalDigits) {
		final String javaType = getPreferredJavaType(sqlType,size,decimalDigits);
		if(javaType.endsWith("String")) {
			return true;
		}
		return false;
	}

	public static String getPreferredJavaType(final int sqlType, final int size,
			final int decimalDigits) {
		if ((sqlType == Types.DECIMAL || sqlType == Types.NUMERIC)
				&& decimalDigits == 0) {
			if (size < 10) {
				return "Integer";
			} else {
				return "Long";
			}
		}
		String result = _preferredJavaTypeForSqlType.getString(sqlType);
		if (result == null) {
			result = "Object";
		}
		return result;
	}

   static {
      _preferredJavaTypeForSqlType.put(Types.TINYINT, "Byte");
      _preferredJavaTypeForSqlType.put(Types.SMALLINT, "Short");
      _preferredJavaTypeForSqlType.put(Types.INTEGER, "Integer");
      _preferredJavaTypeForSqlType.put(Types.BIGINT, "Long");
      _preferredJavaTypeForSqlType.put(Types.REAL, "Float");
      _preferredJavaTypeForSqlType.put(Types.FLOAT, "Double");
      _preferredJavaTypeForSqlType.put(Types.DOUBLE, "Double");
      _preferredJavaTypeForSqlType.put(Types.DECIMAL, "Double");
      _preferredJavaTypeForSqlType.put(Types.NUMERIC, "Double");
      _preferredJavaTypeForSqlType.put(Types.BIT, "Boolean");
      _preferredJavaTypeForSqlType.put(Types.BOOLEAN, "Boolean");
      _preferredJavaTypeForSqlType.put(Types.CHAR, "String");
      _preferredJavaTypeForSqlType.put(Types.VARCHAR, "String");
      // according to resultset.gif, we should use java.io.Reader, but String is more convenient for EJB
      _preferredJavaTypeForSqlType.put(Types.LONGVARCHAR, "String");
      _preferredJavaTypeForSqlType.put(Types.BINARY, "byte[]");
      _preferredJavaTypeForSqlType.put(Types.VARBINARY, "byte[]");
      _preferredJavaTypeForSqlType.put(Types.LONGVARBINARY, "byte[]");
      //在这儿设置数据库Date类型与Java的对应关系
      _preferredJavaTypeForSqlType.put(Types.DATE, "Date");
      _preferredJavaTypeForSqlType.put(Types.TIME, "Date");
      _preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "Date");
//      _preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "java.sql.Timestamp");
      _preferredJavaTypeForSqlType.put(Types.CLOB, "String");
      _preferredJavaTypeForSqlType.put(Types.BLOB, "byte[]");
      _preferredJavaTypeForSqlType.put(Types.ARRAY, "java.sql.Array");
      _preferredJavaTypeForSqlType.put(Types.REF, "java.sql.Ref");
      _preferredJavaTypeForSqlType.put(Types.STRUCT, "Object");
      _preferredJavaTypeForSqlType.put(Types.JAVA_OBJECT, "Object");
   }

   private static class IntStringMap extends HashMap {

		public String getString(final int i) {
			return (String) get(Integer.valueOf(i));
		}

		public void put(final int i, final String s) {
			put(Integer.valueOf(i), s);
		}
	}

}
