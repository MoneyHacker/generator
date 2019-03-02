package cn.org.rapid.framework.generator.provider.db.model;
import cn.org.rapid.framework.generator.util.ActionScriptDataTypesUtils;
import cn.org.rapid.framework.generator.util.DatabaseDataTypesUtils;
import cn.org.rapid.framework.generator.util.StringHelper;
import cn.org.rapid.framework.generator.util.TestDataGenerator;
import lombok.extern.log4j.Log4j2;
import cn.org.rapid.framework.generator.util.JdbcType;

/**
 * 
 * @author badqiu lx freemarker并不要求对象一定得有对象的属性,但一定需要对应的getter或is方法,否则按默认规则查找getter或is方法
 * @email badqiu(a)gmail.com
 */
@Log4j2
public class Column {
	/**
	 * Reference to the containing table
	 */
	private final Table _table;

	/**
	 * The java.sql.Types type
	 */
	private final int _sqlType;

	/**
	 * The sql typename. provided by JDBC driver
	 */
	private final String _sqlTypeName;

	/**
	 * The name of the column
	 */
	private final String _sqlName;

	/**
	 * True if the column is a primary key
	 */
	private boolean _isPk;

	/**
	 * True if the column is a foreign key
	 */
	private boolean _isFk;

	/**
	 * @todo-javadoc Describe the column
	 */
	private final int _size;

	/**
	 * @todo-javadoc Describe the column
	 */
	private final int _decimalDigits;

	/**
	 * True if the column is nullable
	 */
	private final boolean _isNullable;

	/**
	 * True if the column is indexed
	 */
	private final boolean _isIndexed;

	/**
	 * True if the column is unique
	 */
	private final boolean _isUnique;

	/**
	 * Null if the DB reports no default value
	 */
	private final String _defaultValue;
	
	/**
	 * The comments of column
	 */
	private final String _remarks;
	
	/**
	 * Get static reference to Log4J Logger
	 */

//	String description;
//
//	String humanName;
//
//	int order;
//
//	boolean isHtmlHidden;
//
//	String validateString;

	/**
	 * Describe what the DbColumn constructor does
	 * 
	 * @param table
	 *            Describe what the parameter does
	 * @param sqlType
	 *            Describe what the parameter does
	 * @param sqlTypeName
	 *            Describe what the parameter does
	 * @param sqlName
	 *            Describe what the parameter does
	 * @param size
	 *            Describe what the parameter does
	 * @param decimalDigits
	 *            Describe what the parameter does
	 * @param isPk
	 *            Describe what the parameter does
	 * @param isNullable
	 *            Describe what the parameter does
	 * @param isIndexed
	 *            Describe what the parameter does
	 * @param defaultValue
	 *            Describe what the parameter does
	 * @param isUnique
	 *            Describe what the parameter does
	 */
	public Column(Table table, int sqlType, String sqlTypeName,
			String sqlName, int size, int decimalDigits, boolean isPk,
			boolean isNullable, boolean isIndexed, boolean isUnique,
			String defaultValue,String remarks) {
		_table = table;
		_sqlType = sqlType;
		_sqlName = sqlName;
		_sqlTypeName = sqlTypeName;
		_size = size;
		_decimalDigits = decimalDigits;
		_isPk = isPk;
		_isNullable = isNullable;
		_isIndexed = isIndexed;
		_isUnique = isUnique;
		_defaultValue = defaultValue;
		_remarks = remarks;
		
		log.debug(sqlName + " isPk -> " + _isPk);

	}

	/**
	 * Gets the SqlType attribute of the Column object
	 * 
	 * @return The SqlType value
	 */
	public int getSqlType() {
		return _sqlType;
	}

	/**
	 * Gets the Table attribute of the DbColumn object
	 * 
	 * @return The Table value
	 */
	public Table getTable() {
		return _table;
	}

	/**
	 * Gets the Size attribute of the DbColumn object
	 * 
	 * @return The Size value
	 */
	public int getSize() {
		return _size;
	}

	/**
	 * Gets the DecimalDigits attribute of the DbColumn object
	 * 
	 * @return The DecimalDigits value
	 */
	public int getDecimalDigits() {
		return _decimalDigits;
	}

	/**
	 * Gets the SqlTypeName attribute of the Column object
	 * 
	 * @return The SqlTypeName value
	 */
	public String getSqlTypeName() {
		return _sqlTypeName;
	}

	/**
	 * Gets the SqlName attribute of the Column object
	 * 
	 * @return The SqlName value
	 */
	public String getSqlName() {
		return _sqlName;
	}

	/**
	 * Gets the Pk attribute of the Column object
	 * 
	 * @return The Pk value
	 */
	public boolean isPk() {
		return _isPk;
	}

	/**
	 * Gets the Fk attribute of the Column object
	 * 
	 * @return The Fk value
	 */
	public boolean isFk() {
		return _isFk;
	}

	/**
	 * Gets the Nullable attribute of the Column object
	 * 
	 * @return The Nullable value
	 */
	public final boolean isNullable() {
		return _isNullable;
	}

	/**
	 * Gets the Indexed attribute of the DbColumn object
	 * 
	 * @return The Indexed value
	 */
	public final boolean isIndexed() {
		return _isIndexed;
	}

	/**
	 * Gets the Unique attribute of the DbColumn object
	 * 
	 * @return The Unique value
	 */
	public boolean isUnique() {
		return _isUnique;
	}

	/**
	 * Gets the DefaultValue attribute of the DbColumn object
	 * 
	 * @return The DefaultValue value
	 */
	public final String getDefaultValue() {
		return _defaultValue;
	}
	
	public final String getRemarks() {
		return _remarks;
	}

	/**
	 * Describe what the method does
	 * 
	 * @return Describe the return value
	 */
	@Override
	public int hashCode() {
		return (getTable().getSqlName() + "#" + getSqlName()).hashCode();
	}

	/**
	 * Describe what the method does
	 * 
	 * @param o
	 *            Describe what the parameter does
	 * @return Describe the return value
	 */
	@Override
	public boolean equals(Object o) {
		// we can compare by identity, since there won't be dupes
		return this == o;
	}

	/**
	 * Describe what the method does
	 * 
	 * @return Describe the return value
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" table=").append(getTable().getSqlName())
		  .append("  columnname=").append(this.getColumnName())	
		  .append("  javatype=").append(this.getJavaType())	
		  .append("  sqltype=").append(this.getSqlType())	
		  .append("  sqltypename=").append(this.getSqlTypeName())	
		  .append("  SqlName=").append(this.getSqlName());
		return sb.toString();
	}

	/**
	 * Describe what the method does
	 * 
	 * @return Describe the return value
	 */
	protected final String prefsPrefix() {
		return "tables/" + getTable().getSqlName() + "/columns/" + getSqlName();
	}

	/**
	 * Sets the Pk attribute of the DbColumn object
	 * 
	 * @param flag
	 *            The new Pk value
	 */
	void setFk(boolean flag) {
		_isFk = flag;
	}
	
	public String getColumnName() {
		return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(getSqlName()));
	}
	
	public String getColumnNameFirstLower() {
		return StringHelper.uncapitalize(getColumnName());
	}
	
	public String getColumnNameLowerCase() {
		return getColumnName().toLowerCase();
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public String getColumnNameLower() {
		return getColumnNameFirstLower();
	}
	
	public String getJdbcSqlTypeName() {
		String result = JdbcType.getJdbcSqlTypeName(getSqlType());
		//if(result == null) throw new RuntimeException("jdbcSqlTypeName is null column:"+getSqlName()+" sqlType:"+getSqlType());
		return result;
	}
	
	public String getColumnAlias() {
		return StringHelper.emptyIf(getRemarks(), getColumnNameFirstLower());
	}
	
	public String getConstantName() {
		return StringHelper.toUnderscoreName(getSqlName()).toUpperCase();
	}
	
	public boolean getIsNotIdOrVersionField() {
		return !isPk();
	}
	
	public String getValidateString() {
		String result = getNoRequiredValidateString();
		if(!isNullable()) {
			result = "{required:true," + result;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 1);
			result += "}";
		}
		return result;
	}
	
	private String getNoRequiredValidateString() {
		String result = "";
		if(getSqlName().indexOf("mail") >= 0) {
			result += "email:true,";
		}
		if(DatabaseDataTypesUtils.isFloatNumber(getSqlType(), getSize(), getDecimalDigits())) {
			result += "number:true,";
		}
		if(DatabaseDataTypesUtils.isIntegerNumber(getSqlType(), getSize(), getDecimalDigits())) {
			result += "digits:true,range:[1,99999999999999],";
		}
		return result;
	}
	
	public boolean getIsStringColumn() {
		return DatabaseDataTypesUtils.isString(getSqlType(), getSize(), getDecimalDigits());
	}
	
	public boolean getIsDateTimeColumn() {
		
		return DatabaseDataTypesUtils.isDate(this,getSqlType(), getSize(), getDecimalDigits());
	}
	
	public boolean getIsNumberColumn() {
		return DatabaseDataTypesUtils.isFloatNumber(getSqlType(), getSize(), getDecimalDigits()) || DatabaseDataTypesUtils.isIntegerNumber(getSqlType(), getSize(), getDecimalDigits());
	}
	
	public boolean isHtmlHidden() {
		return isPk() && _table.isSingleId();
	}
	public String getJavaType() {
		return DatabaseDataTypesUtils.getPreferredJavaType(getSqlType(), getSize(), getDecimalDigits());
	}
	
	public String getAsType() {
		return ActionScriptDataTypesUtils.getPreferredAsType(getJavaType());
	}
	
	public String getTestData() {
		return new TestDataGenerator().getTestData(getColumnName(),getJavaType(),getSize());
	}
}
