<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?cap_first>   
package ${basepackage}.web;

import java.util.Map;

import javacommon.base.BaseSpringController;
import javacommon.page.Page;
import javacommon.page.PageRequest;
import module.user.model.Client;
import module.user.service.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.dangdang.digital.controlller.BaseController;
import com.dangdang.digital.soa.utils.PageFinder;
import com.dangdang.digital.soa.utils.Query;
import com.dangdang.digital.model.${className};
import com.dangdang.digital.service.I${className}Service;
<#include "/java_imports.include">
/**
 * ${className} Controller.
 */
@Controller
@RequestMapping("${className?lower_case}")
public class ${className}Controller extends BaseController{

	@Resource I${className}Service ${className?uncap_first}Service;
	
	@RequestMapping(value="/list")
	public String get${className}(String pageIndex,final Model model,final ${className} ${className?uncap_first}){
		Query query = new Query();
		if(StringUtils.isNotBlank(pageIndex)){
			query.setPage(Integer.parseInt(pageIndex));
		}else{
			query.setPage(1);
		}
		PageFinder<${className}> pageFinder = ${className?uncap_first}Service.findPageFinderObjs(${className?uncap_first},query);
		if(pageFinder != null){
			model.addAttribute("pageFinder", pageFinder);
		}
		return "${className?lower_case}/list";
	}
	
	@RequestMapping(value="/delete")
	public String delete${className}(@RequestParam final int id){
		this.${className?uncap_first}Service.deleteById(id);
		return "redirect:list.go";
	}
	@RequestMapping(value="/edit")
	public String edit${className}(@RequestParam final int id,final Model model){
		${className} ${className?uncap_first} = this.${className?uncap_first}Service.get(id);
		model.addAttribute("${className?lower_case}", ${className?uncap_first});
		return "${className?lower_case}/edit";
	}
	@RequestMapping(value="/add")
	public String add${className}(final Model model){
		return "${className?lower_case}/add";
	}
	
	@RequestMapping(value="/save")
	public String save${className}(final ${className} ${className?uncap_first} ){
		${className?uncap_first}Service.save(label);
		return "redirect:list.go";
		
	}
	
	@RequestMapping(value="/update")
	public String update${className}(${className} ${className?uncap_first}){
		${className?uncap_first}Service.update(${className?uncap_first});
		return "redirect:list.go";
		
	}

<#macro generateIdParameter> 
	<#if table.compositeId>
		${className}Id id = new ${className}Id();
		bind(request, id);
	<#else>
		<#list table.compositeIdColumns as column>
		${column.javaType} id = new ${column.javaType}(request.getParameter("${column.columnNameLower}"));
		</#list>
	</#if>
</#macro>