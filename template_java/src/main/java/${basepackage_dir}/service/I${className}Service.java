<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ${basepackage}.model.${className};
import com.dangdang.digital.service.IBaseService;

<#include "/java_imports.include">

/**
 * ${className} Manager.
 */
public interface I${className}Service extends IBaseService<${className}, ${table.idColumn.javaType}> {}
