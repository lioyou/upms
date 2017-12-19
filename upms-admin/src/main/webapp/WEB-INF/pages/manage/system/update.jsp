<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="updateDialog" class="crudDialog">
	<form id="updateForm" method="post">
		<div class="form-group">
			<label for="name">名称</label>
			<input id="name" type="text" class="form-control" name="name" maxlength="20" value="${system.name}" required="true">
			<input id="id" type="hidden" name="systemId" value="${system.systemId }"/>
		</div>
		<div class="form-group">
			<label for="theme"></label>
			<input id="theme" type="color" class="form-control" name="theme" maxlength="50" value="${system.theme}">
		</div>
		<div class="form-group">
			<label for="icon">图标</label>
			<input id="icon" type="text" class="form-control" name="icon" maxlength="20" value="${system.icon}">
		</div>
		<div class="form-group">
			<label for="label">标签名</label>
			<input id="label" type="text" class="form-control" name="label" maxlength="20" value="${system.label}" required="true">
		</div>
		<div class="row">
			<div class="col-lg-8 form-group">
				<label for="name">背景图</label>
				<input id="banner" type="text" class="form-control" name="banner" maxlength="150" value="${system.banner}">
				<input id="image-prefix" type="hidden" name="imagePrefix" value="${imagePrefix }"/>
			</div>
			<div class="col-lg-4">
				<div id="picker">上传图片</div>
			</div>
		</div>
		<div class="form-group">
			<label for="description">描述</label>
			<input id="description" type="text" class="form-control" name="description" maxlength="300" value="${system.description}">
		</div>
		<div class="form-group">
			<label for="baseurl">根目录</label>
			<input id="baseurl" type="text" class="form-control" name="baseurl" maxlength="100" value="${system.baseurl}">
		</div>
		<div class="radio">
			<div class="radio radio-inline radio-success">
				<input id="status_1" type="radio" name="status" value="1" <c:if test="${system.status==1}">checked</c:if>>
				<label for="status_1">正常 </label>
			</div>
			<div class="radio radio-inline">
				<input id="status_0" type="radio" name="status" value="0" <c:if test="${system.status==0}">checked</c:if>>
				<label for="status_0">锁定 </label>
			</div>
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="btn btn-info" herf="javascript:;"  onclick="$('#updateForm').submit();">保存</a>
			<a class="btn btn-info" herf="javascript:;" onclick="dialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
	$('#updateForm').validate({
		rules: {
			name: {
				required: true,
				minlength: 6,
			},
			theme: 'required',
			icon: 'required',
			label: 'required',
			banner: 'required',
			description: 'required',
			baseurl: 'required',
			status: 'required',
		},
		messages: {
			theme: '主题不能为空',
			icon: '图标不能为空',
			label: '标签名不能为空',
			name: {
				required: '名称不能为空',
				minlength: '数字不能少于6位',
			},
			banner: '背景不能为空',
			description: '描述不能为空',
			baseurl: '根目录不能为空',
			status: '状态不能为空',
		},
		submitHandler: function(){
			actionSubmit('/manage/system/update',$('#updateForm').serialize());
		}
	});	
</script>