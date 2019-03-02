<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
<#include "/java_imports.include">

/**
 * ${className} Entity.
 */
@Setter
@Getter
@ToString
public class ${className} implements Serializable{
	//date formats
	<#if isDateToString=='true'>
	<#list table.columns as column>
	<#if column.isDateTimeColumn>
	public static final String FORMAT_${column.constantName} = DATE_TIME_FORMAT;
	</#if>
	</#list>
	</#if>
	//列信息
	<#list table.columns as column>
	private ${column.javaType} ${column.columnNameLower};
	</#list>
}




