<%@ page contentType="text/html;charset=UTF-8"%>
<#include "/macro.include"/>
<#include "/custom.include"/> 
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
	<form id="add"  action="${classNameLower}/save.go" method="post" onsubmit="">
		<table class="table2">
		<#list table.columns as column> 
			<#if !column.htmlHidden>
			 <tr>
				<td>${column.constantName}</td>
				<td>
					<#rt>
						<#compress><input type="input" id="${column.columnNameLower}" name="${column.columnNameLower}"></#compress> 
					<#lt>
			  </td>
		  </tr>
		 </#if> 
		 </#list>
		<tr>
			<td colspan="2"><button type="submit">确认</button>&nbsp;&nbsp;<button type="reset">重置</button></td>
		</tr>
	</table>
</form>
		