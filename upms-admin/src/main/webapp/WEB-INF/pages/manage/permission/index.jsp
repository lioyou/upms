<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>权限管理</title>
	<jsp:include page="/resources/include/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<shiro:hasPermission name="upms:permission:create"><a class="waves-effect waves-button" href="javascript:;" onclick="createAction('/manage/permission/create','创建权限')"><i class="zmdi zmdi-plus"></i> 新增权限</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:permission:update"><a class="waves-effect waves-button" href="javascript:;" onclick="updateAction('/manage/permission/update/','更新权限','permissionId')"><i class="zmdi zmdi-edit"></i> 编辑权限</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:permission:delete"><a class="waves-effect waves-button" href="javascript:;" onclick="deleteAction('/manage/permission/delete/','删除权限','permissionId')"><i class="zmdi zmdi-close"></i> 删除权限</a></shiro:hasPermission>
	</div>
	<table id="table"></table>
</div>
<jsp:include page="/resources/include/js.jsp" flush="true"/>
<script>
var $table = $('#table');
$(function() {
	// bootstrap table初始化
	$table.bootstrapTable({
		url: '${basePath}/manage/permission/list',
		height: getHeight(),
		striped: true,
		search: true,
		showRefresh: true,
		showColumns: true,
		minimumCountColumns: 2,
		clickToSelect: true,
		detailView: true,
		detailFormatter: 'detailFormatter',
		pagination: true,
		paginationLoop: false,
		sidePagination: 'server',
		silentSort: false,
		smartDisplay: false,
		escape: true,
		searchOnEnterKey: true,
		idField: 'permissionId',
		maintainSelected: true,
		toolbar: '#toolbar',
		columns: [
			{field: 'ck', checkbox: true},
			{field: 'permissionId', title: '编号', sortable: true, align: 'center'},
            {field: 'systemId', title: '所属系统'},
			{field: 'pid', title: '所属上级'},
			{field: 'label', title: '权限名称'},
			{field: 'type', title: '类型', formatter: 'typeFormatter'},
			{field: 'name', title: '资源标识'},
			{field: 'uri', title: '路径'},
			{field: 'icon', title: '图标', align: 'center', formatter: 'iconFormatter'},
			{field: 'status', title: '状态', sortable: true, align: 'center', formatter: 'statusFormatter'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});
// 格式化操作按钮
function actionFormatter(value, row, index) {
    return [
		'<a class="update" href="javascript:;" onclick="updateAction(' + "/manage/permission/update/','更新权限','permissionId'" + ')" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
		'<a class="delete" href="javascript:;" onclick="deleteAction(' + "'/manage/permission/delete/','删除权限','permissionId'" + ')" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
    ].join('');
}
// 格式化图标
function iconFormatter(value, row, index) {
    return '<i class="' + value + '"></i>';
}
// 格式化类型
function typeFormatter(value, row, index) {
	if (value == 1) {
		return '目录';
	}
	if (value == 2) {
		return '菜单';
	}
	if (value == 3) {
		return '按钮';
	}
	return '-';
}
// 格式化状态
function statusFormatter(value, row, index) {
	if (value == 1) {
		return '<span class="label label-success">正常</span>';
	} else {
		return '<span class="label label-default">锁定</span>';
	}
}
//根目录
var basePath='${basePath}';
//窗口句柄
var dialog
</script>
</body>
</html>