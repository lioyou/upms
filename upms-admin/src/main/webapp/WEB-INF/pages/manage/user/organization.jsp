<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="organizationDialog" class="crudDialog">
	<form id="organizationForm" method="post">
		<div class="form-group">
			<select id="organizationId" name="organizationId" multiple="multiple" style="width: 100%">
				<c:forEach var="upmsOrganization" items="${upmsOrganizations}">
					<option value="${upmsOrganization.organizationId}" <c:forEach var="upmsUserOrganization" items="${upmsUserOrganizations}"><c:if test="${upmsOrganization.organizationId==upmsUserOrganization.organizationId}">selected="selected"</c:if></c:forEach>>${upmsOrganization.label}</option>
				</c:forEach>
			</select>
			<input type="hidden" name="userId"  value="${userId }"/>
		</div>
		<div class="form-group text-right dialog-buttons">
		    <a class="btn btn-info" href="javascript:;" onclick="actionSubmit('/manage/user/organization',$('#organizationForm').serialize());">保存</a>
			<a class="btn btn-info" href="javascript:;" onclick="dialog.close();">取消</a>
		</div>
	</form>
</div>
