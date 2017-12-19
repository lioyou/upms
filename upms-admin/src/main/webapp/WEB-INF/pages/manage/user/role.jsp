<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="roleDialog" class="crudDialog">
	<form id="roleForm" method="post">
		<div class="form-group">
			<select id="roleId" name="roleId" multiple="multiple" style="width: 100%">
				<c:forEach var="upmsRole" items="${upmsRoles}">
					<option value="${upmsRole.roleId}" <c:forEach var="upmsUserRole" items="${upmsUserRoles}"><c:if test="${upmsRole.roleId==upmsUserRole.roleId}">selected="selected"</c:if></c:forEach>>${upmsRole.label}</option>
				</c:forEach>
			</select>
			<input type="hidden" name="userId" value="${userId }"/>
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="btn btn-info" href="javascript:;" onclick="actionSubmit('/manage/user/role',$('#roleForm').serialize());">保存</a>
			<a class="btn btn-info" href="javascript:;" onclick="dialog.close();">取消</a>
		</div>
	</form>
</div>
