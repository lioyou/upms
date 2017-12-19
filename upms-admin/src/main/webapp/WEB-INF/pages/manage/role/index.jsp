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
	<title>角色管理</title>
	<jsp:include page="/resources/include/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<shiro:hasPermission name="upms:role:create"><a class="waves-effect waves-button" href="javascript:;" onclick="createAction('/manage/role/create','创建角色')"><i class="zmdi zmdi-plus"></i>创建角色</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:role:update"><a class="waves-effect waves-button" href="javascript:;" onclick="updateAction('/manage/role/update/','编辑角色','roleId')"><i class="zmdi zmdi-edit"></i> 编辑角色</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:role:delete"><a class="waves-effect waves-button" href="javascript:;" onclick="deleteAction('/manage/role/delete/','删除角色','roleId')"><i class="zmdi zmdi-close"></i> 删除角色</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:role:permission"><a class="waves-effect waves-button" href="javascript:;" onclick="permissionAction()"><i class="zmdi zmdi-key"></i> 角色权限</a></shiro:hasPermission>
	</div>
	<table id="table"></table>
</div>
<jsp:include page="/resources/include/js.jsp" flush="true"/>
<script>
var $table = $('#table');
$(function() {
	// bootstrap table初始化
	$table.bootstrapTable({
		url: '${basePath}/manage/role/list',
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
		idField: 'roleId',
		maintainSelected: true,
		toolbar: '#toolbar',
		columns: [
			{field: 'ck', checkbox: true},
			{field: 'roleId', title: '编号', sortable: true, align: 'center'},
			{field: 'name', title: '角色名称'},
			{field: 'label', title: '角色标题'},
            {field: 'description', title: '角色描述'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});
// 格式化操作按钮
function actionFormatter(value, row, index) {
    return [
		'<a class="update" href="javascript:;" onclick="updateAction(' + "'/manage/role/update/','编辑角色','roleId'" + ')" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
		'<a class="delete" href="javascript:;" onclick="deleteAction(' + "'/manage/role/delete/','删除角色','roleId'" + ')" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
    ].join('');
}

//根目录
var basePath = '${basePath}';
//窗口句柄，用于关闭窗口
var dialog



// 角色权限
var roleId;
function permissionAction() {
	var rows = $table.bootstrapTable('getSelections');
	if (rows.length != 1) {
		$.confirm({
			title: false,
			content: '请选择一条记录！',
			autoClose: 'cancel|3000',
			backgroundDismiss: true,
			buttons: {
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	} else {
		roleId = rows[0].roleId;
		dialog = $.dialog({
			animationSpeed: 300,
			title: '角色权限',
			content: 'url:${basePath}/manage/role/permission',
			onContentReady: function () {
				initMaterialInput();
				initTree();
			}
		});
	}
}

</script>
</body>
</html>