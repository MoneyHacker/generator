<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;
import ${basepackage}.model.${className};
<#include "/java_imports.include">

public interface I${className}Dao extends IBaseDao<${className}> {
}