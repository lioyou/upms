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
		<div class="radio">
			<div class="radio radio-inline radio-success">
				<input id="type_1" type="radio" name="type" value="1" <c:if test="${permission.type==1}">checked</c:if>>
				<label for="type_1">目录 </label>
			</div>
			<div class="radio radio-inline radio-info">
				<input id="type_2" type="radio" name="type" value="2" <c:if test="${permission.type==2}">checked</c:if>>
				<label for="type_2">菜单 </label>
			</div>
			<div class="radio radio-inline radio-warning">
				<input id="type_3" type="radio" name="type" value="3" <c:if test="${permission.type==3}">checked</c:if>>
				<label for="type_3">按钮 </label>
			</div>
		</div>
		<div class="form-group">
			<span class="type1 type2 type3">
				<select id="systemId" name="systemId">
					<option value="0">请选择系统</option>
					<c:forEach var="upmsSystem" items="${upmsSystems}">
					<option value="${upmsSystem.systemId}" <c:if test="${permission.systemId==upmsSystem.systemId}">selected="selected"</c:if>>${upmsSystem.label}</option>
					</c:forEach>
				</select>
			</span>
			<span class="type2 type3" hidden>
				<select id="pid" name="pid">
					<option value="0">请选择上级</option>
				</select>
			</span>
		</div>
		<div class="form-group">
			<label for="label">名称</label>
			<input id="label" type="text" class="form-control" name="label" maxlength="20" value="${permission.label}">
			<input id="id" type="hidden" name="permissionId" value="${permission.permissionId }"/>
		</div>
		<div class="form-group">
			<label for="description">描述</label>
			<input id="description" type="text" class="form-control" name="description" maxlength="50" value="${permission.description }"/>
		</div>
		<div class="form-group type2 type3" hidden>
			<label for="name">资源标识</label>
			<input id="name" type="text" class="form-control" name="name" maxlength="50" value="${permission.name}">
		</div>
		<div class="form-group type2 type3" hidden>
			<label for="uri">路径</label>
			<input id="uri" type="text" class="form-control" name="uri" maxlength="100" value="${permission.uri}">
		</div>
		<div class="form-group type1 type3" <c:if test="${permission.type ==2 }">hidden</c:if>>
			<label for="icon">图标</label>
			<input id="icon" type="text" class="form-control" name="icon" maxlength="50" value="${permission.icon}">
		</div>
		<div class="radio">
			<div class="radio radio-inline radio-success">
				<input id="status_1" type="radio" name="status" value="1" <c:if test="${permission.status==1}">checked</c:if>>
				<label for="status_1">正常 </label>
			</div>
			<div class="radio radio-inline">
				<input id="status_0" type="radio" name="status" value="0" <c:if test="${permission.status==0}">checked</c:if>>
				<label for="status_0">锁定 </label>
			</div>
		</div>
		<div class="form-group text-right dialog-buttons">
			<a class="btn btn-info" href="javascript:;" onclick="$('#updateForm').submit();">保存</a>
			<a class="btn btn-info" href="javascript:;" onclick="dialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
//初始化select2插件
$('select').select2();
var pidType = 0;
var systemId = ${permission.systemId};
var type = ${permission.type};
$(function() {
	// 选择分类
	$('input:radio[name="type"]').change(function() {
		type = $(this).val();
		initType();
	});
	// 选择系统
	$('#systemId').change(function() {
		systemId = $(this).val();
		initPid();
	});
});
function initType() {
	// 显示对应必填项
	$('.type1,.type2,.type3').hide(0, function () {
		$('.type' + type).show();
	});
	// 级联菜单
	if (type == 2) {
		pidType = 1;
		initPid();
	}
	if (type == 3) {
		pidType = 2
		initPid();
	}
}
function initPid(val) {
	if (systemId != 0) {
		$.getJSON('${basePath}/manage/permission/list', {systemId: systemId, type: pidType, limit: 10000}, function(json) {
			var datas = [{id: 0, text: '请选择上级'}];
			for (var i = 0; i < json.rows.length; i ++) {
				var data = {};
				data.id = json.rows[i].permissionId;
				data.text = json.rows[i].label;
				datas.push(data);
			}
			$('#pid').empty();
			$('#pid').select2({
				data : datas
			});
			if (!!val) {
				$('#pid').select2().val(val).trigger('change');
			}
		});
	} else {
		$('#pid').empty();
		$('#pid').select2({
			data : [{id: 0, text: '请选择上级'}]
		});
	}
}
function initSelect2() {
	if (type == 2) {
		pidType = 1;
	}
	if (type == 3) {
		pidType = 2
	}
	initPid(${permission.pid});
}

//数据验证
$('#updateForm').validate({
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
			required: '资源标识不能为空',
			minlength: '数字不能少于6位',
		},
		label: '标签名不能为空',
		description: '描述不能为空',
	},
	submitHandler: function(){
		if ($('#systemId').val() == 0) {
			$.confirm({
				title: false,
				content: '请选择系统！',
				autoClose: 'cancel|3000',
				backgroundDismiss: true,
				buttons: {
					cancel: {
						text: '取消',
						btnClass: 'waves-effect waves-button'
					}
				}
			});
			return false;
		}
		if (type == 2 || type == 3) {
			if ($('#pid').val() == 0) {
				$.confirm({
					title: false,
					content: '请选择上级！',
					autoClose: 'cancel|3000',
					backgroundDismiss: true,
					buttons: {
						cancel: {
							text: '取消',
							btnClass: 'waves-effect waves-button'
						}
					}
				});
				return false;
			}
		}
		//提交更改
		actionSubmit('/manage/permission/update',$('#updateForm').serialize());
	}
});
</script>