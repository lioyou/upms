<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="createDialog" class="crudDialog">
	<form id="createForm" method="post">
		<div class="form-group">
			<label for="name">名称</label>
			<input id="name" type="text" class="form-control" name="name" maxlength="20">
		</div>
		<div class="form-group">
			<label for="label">标签名</label>
			<input id="label" type="text" class="form-control" name="label" maxlength="20">
		</div>
		<div class="form-group">
			<label for="description">描述</label>
			<input id="description" type="text" class="form-control" name="description" maxlength="300">
		</div>
		<div class="form-group">
			<select id="pid" name="pid">
			    <label for="pid">所属上级</label>
				<option value="0">请选择所属上级组织</option>
				<c:forEach items="${upmsOrganizations }" var="organization">
					<option value="${organization.organizationId }">${organization.label }</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="btn btn-info" href="javascript:;" onclick="$('#createForm').submit();">保存</a>
			<a class="btn btn-info" href="javascript:;" onclick="dialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
//初始化select2插件
$('select').select2();
//数据验证
$('#createForm').validate({
	rules: {
		name: {
			required: true,
			minlength: 6,
		},
		label: 'required',
		description: 'required',
	},
	messages: {
		name: {
			required: '名称不能为空',
			minlength: '数字不能少于6位',
		},
		label: '标签名不能为空',
		description: '描述不能为空',
	},
	submitHandler: function(){
		actionSubmit('/manage/organization/create',$('#createForm').serialize());
	}
});

</script>