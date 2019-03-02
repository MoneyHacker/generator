<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="width:99%">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>携手金融--后台登陆</title>
</head>   
<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
  <body>
	 <div class="page"><!--page开始-->
		<div class="main clear"><!--main开始-->
			<div class="right">
				<div class="m-r">
					<h3>系统参数&gt;&gt;参数列表</h3>
					<div style="padding:5px;background:none repeat scroll 0 0 #E4EAF6; margin-bottom:5px;border:1px solid #4F69A0">
		      		<form action="/${classNameLower}/list.go" method="post" id="query" name="query" onsubmit=" return checkQuery()">
			      		 <table >
		        			<tr>
						  <td>
						  <#list table.columns as column>
							<#if !column.htmlHidden>
								${column.constantName}:<input type="text" id="${column.columnNameLower}" name="${column.columnNameLower}"
								value='${r"<#if RequestParameters."+column.columnNameLower+r"??>${RequestParameters."+column.columnNameLower+r"}</#if>"}'>
							</#if>
						</#list>
							
								<button  type="submit" >查询</button>
								<button type="button" onclick="window.location='/${classNameLower}/add.go'">新增</button>
								</td>
							</tr>
						</table>
				    </form>
				    </div>
				    <div>
				    <table class="table2">
		            	<tr>
							<#list table.columns as column>
								<#if !column.htmlHidden>
									<th>${column.constantName}</th>
								</#if>
							</#list>
	               	    </tr>
 				<#noparse>
						<#assign i = 0>
	              	 	  <#if (pageFinder?size>0)>
			    			<#list pageFinder.data as </#noparse> ${classNameLower}<#noparse> >
			    				<#assign i = i+1>
			    				<#if i%2 == 0>
			    					<tr style="background-color: #E4EAF6;" onmouseover="overCurrent(${i})" onmouseout="outCurrent(${i})" id="row_${i}">
			    				<#else>
			    					<tr onmouseover="overCurrent(${i})" onmouseout="outCurrent(${i})" id="row_${i}">
			    				</#if>
					</#noparse>
							
							<#list table.columns as column>
								<#if !column.htmlHidden>
								<td><#rt>
								<#compress>
								<#assign beanAttr=classNameLower+ "."+column.columnNameLower>
								${r"<#if "+beanAttr+r"??>${"+beanAttr+r"}</#if>"}
								</#compress>
								<#lt></td>
								</#if>
							</#list>
						      </tr>
		            </table>
		            </div>
			    </div>
			    <div>
			    <div class="pagination rightPager"></div>
			    </div>
		    </div>
		</div>
	</div>
  </body>
<#noparse>
  <script type="text/javascript">
	    function checkQuery(){
	    }
  	    function hintDelete(){
  	    	return window.confirm("您确认删除配置项吗?");
  	    }
	   	$(function(){
		    $('.pagination').pagination(${pageFinder.rowCount?c}, {
				items_per_page: ${pageFinder.pageSize?c},
				current_page: ${pageFinder.pageNo - 1},
				prev_show_always:false,
				next_show_always:false,
				link_to: encodeURI('/${classNameLower}/list.go?pageIndex=__id__&'+decodeURIComponent($("#query").serialize(),true))
		    });
	   	});
	</script>
</#noparse>
</html>
<#macro generateIdQueryString>
	<#if table.compositeId>
		<#assign itemPrefix = 'item.id.'>
	<#else>
		<#assign itemPrefix = 'item.'>
	</#if>
<#compress>
		<#list table.compositeIdColumns as column>
			<#t>${column.columnNameLower}=<@jspEl itemPrefix + column.columnNameLower/>&
		</#list>				
</#compress>
</#macro>
