<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangdang.digital.dao.IBaseDao;
import ${basepackage}.model.${className};
import ${basepackage}.dao.I${className}Dao;
import ${basepackage}.service.I${className}Service;
<#include "/java_imports.include">
/**
 * ${className} Manager.
 */
@Service
public class ${className}ServiceImpl extends BaseServiceImpl<${className}, ${table.idColumn.javaType}> implements I${className}Service {

	@Resource
	I${className}Dao dao;
	
	public IBaseDao<${className}> getBaseDao() {
		return dao;
	}
	
<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return ${classNameLower}Dao.getBy${column.columnName}(v);
	}	
	</#if>
</#list>
}
