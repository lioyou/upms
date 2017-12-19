<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>系统管理</title>
	<jsp:include page="/resources/include/css.jsp" flush="true"/>
</head>
<body>
<jsp:include page="/resources/include/css.jsp"/>
<div id="main">
	<div id="toolbar">
		<shiro:hasPermission name="upms:system:create"><a class="waves-effect waves-button" href="javascript:;" onclick="createAction('/manage/system/create','创建系统','#banner');"><i class="zmdi zmdi-plus"></i> 新增系统</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:system:update"><a class="waves-effect waves-button" href="javascript:;" onclick="updateAction('/manage/system/update/','更新系统','systemId','#banner');"><i class="zmdi zmdi-edit"></i> 编辑系统</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:system:delete"><a class="waves-effect waves-button" href="javascript:;" onclick="deleteAction('/manage/system/delete/','您确定删除此系统吗？','systemId')"><i class="zmdi zmdi-close"></i> 删除系统</a></shiro:hasPermission>
	</div>
	<table id="table"></table>
</div>
<jsp:include page="/resources/include/js.jsp" flush="true"/>
<script>
var $table = $('#table');
$(function() {
	// bootstrap table初始化
	$table.bootstrapTable({
		url: '${basePath}/manage/system/list',
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
		idField: 'systemId',
		maintainSelected: true,
		toolbar: '#toolbar',
		columns: [
			{field: 'ck', checkbox: true},
			{field: 'systemId', title: '编号', sortable: true, align: 'center'},
			{field: 'icon', title: '图标', sortable: true, align: 'center', formatter: 'iconFormatter'},
            {field: 'label', title: '系统名称',sortable: true, align: 'center',},
			{field: 'baseurl', title: '根目录',sortable: true, align: 'center',},
			{field: 'status', title: '状态', sortable: true, align: 'center', formatter: 'statusFormatter'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});
// 格式化操作按钮
function actionFormatter(value, row, index) {
    return [
        '<a class="update" href="javascript:;" onclick="updateAction(' + "'/manage/system/update/','更新系统','systemId','#banner'" + ')" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
        '<a class="delete" href="javascript:;" onclick="deleteAction(' + "'/manage/system/delete/','您确定删除此系统吗？','systemId'" + ')" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
    ].join('');
}
// 格式化图标
function iconFormatter(value, row, index) {
    return '<i class="' + value + '"></i>';
}
// 格式化状态
function statusFormatter(value, row, index) {
	if (value == 1) {
		return '<span class="label label-success">正常</span>';
	} else {
		return '<span class="label label-default">锁定</span>';
	}
}
var basePath = '${basePath}';
// 窗口句柄，用于关闭窗口
var dialog
</script>
</body>
</html>