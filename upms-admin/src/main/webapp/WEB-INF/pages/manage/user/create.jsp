<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="createDialog" class="crudDialog">
	<form id="createForm" method="post">
		<div class="form-group">
			<label for="username">用户名</label>
			<input id="username" type="text" class="form-control" name="username" maxlength="20">
		</div>
		<div class="form-group">
			<label for="password">密码</label>
			<input id="password" type="text" class="form-control" name="password" maxlength="32">
		</div>
		<div class="form-group">
			<label for="realname">姓名</label>
			<input id="realname" type="text" class="form-control" name="realname" maxlength="20">
		</div>
		<div class="row">
			<div class="col-lg-8 form-group">
				<label for="avatar">头像</label>
				<input id="avatar" type="text" class="form-control" name="avatar" maxlength="150">
				<input id="image-prefix" name="imagePrefix" type="hidden"/>
			</div>
			<div class="col-lg-4">
				<div id="picker">上传头像</div>
			</div>
		</div>
		<div class="form-group">
			<label for="phone">电话</label>
			<input id="phone" type="text" class="form-control" name="phone" maxlength="20">
		</div>
		<div class="form-group">
			<label for="email">邮箱</label>
			<input id="email" type="text" class="form-control" name="email" maxlength="50">
		</div>
		<div class="radio">
			<div class="radio radio-inline radio-info">
				<input id="sex_1" type="radio" name="sex" value="1" checked>
				<label for="sex_1">男 </label>
			</div>
			<div class="radio radio-inline radio-danger">
				<input id="sex_0" type="radio" name="sex" value="0">
				<label for="sex_0">女 </label>
			</div>
			<div class="radio radio-inline radio-success">
				<input id="locked_0" type="radio" name="status" value="1" checked>
				<label for="locked_0">正常</label>
			</div>
			<div class="radio radio-inline">
				<input id="locked_1" type="radio" name="status" value="0">
				<label for="locked_1">锁定</label>
			</div>
		</div>
		<div class="form-group text-right dialog-buttons">
			<button class="btn btn-info" type="submit">保存</button>
			<a class="btn btn-info" href="javascript:;" onclick="dialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
$('#createForm').validate({
	rules: {
		username: {
			required: true,
			minlength: 6,
		},
		password: {
			required: true,
			minlength: 6,
		},
		realname: 'required',
		phone: 'required',
		email: {
			required: true,
			email: true,
		},
		sex: 'required',
		status: 'required',
	},
	messages: {
		username: {
			required: '用户名不能为空',
			minlength: '字符总数不能小于6',
		},
		password: {
			required: '密码不能为空',
			minlength: '字符总长度不能小于6',
		},
		realname: '真实名不能为空',
		phone: '电话不能为空',
		email: {
			required: '邮箱不能为空',
			email: '邮件格式错误',
		},
		sex: '性别不能为空',
		status: '状态不能为空',
	},
	submitHandler: function(){
		actionSubmit('/manage/user/create',$('#createForm').serialize());
	}
});

</script>