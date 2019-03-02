<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;
<#include "/java_imports.include">
import com.dangdang.digital.dao.impl.BaseDaoImpl;
import ${basepackage}.model.${className};

/**
 * ${className} DAO.
 */
@Repository
public class ${className}DaoImpl extends BaseDaoImpl<${className}> implements I${className}Dao{

}