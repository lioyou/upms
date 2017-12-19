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
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>组织管理</title>
	<jsp:include page="/resources/include/css.jsp" flush="true"/>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<shiro:hasPermission name="upms:organization:create"><a class="waves-effect waves-button" href="javascript:;" onclick="createAction('/manage/organization/create','新建组织')"><i class="zmdi zmdi-plus"></i> 新建组织</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:organization:update"><a class="waves-effect waves-button" href="javascript:;" onclick="updateAction('/manage/organization/update/','编辑组织','organizationId')"><i class="zmdi zmdi-edit"></i> 编辑组织</a></shiro:hasPermission>
		<shiro:hasPermission name="upms:organization:delete"><a class="waves-effect waves-button" href="javascript:;" onclick="deleteAction('/manage/organization/delete/','删除组织','organizationId')"><i class="zmdi zmdi-close"></i> 删除组织</a></shiro:hasPermission>
	</div>
	<table id="table"></table>
</div>
<jsp:include page="/resources/include/js.jsp" flush="true"/>
<script>
var $table = $('#table');
$(function() {
	// bootstrap table初始化
	$table.bootstrapTable({
		url: '${basePath}/manage/organization/list',
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
		idField: 'organizationId',
		maintainSelected: true,
		toolbar: '#toolbar',
		columns: [
			{field: 'ck', checkbox: true},
			{field: 'organizationId', title: '编号', sortable: true, align: 'center'},
			{field: 'name', title: '组织名称',align: 'center'},
			{field: 'label', title: '组织标签名',align: 'center'},
            {field: 'description', title: '组织描述'},
			{field: 'action', title: '操作', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	});
});
// 格式化操作按钮
function actionFormatter(value, row, index) {
    return [
		'<a class="update" href="javascript:;" onclick="updateAction(' + "'/manage/organization/update/','编辑组织','organizationId'" + ')" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
		'<a class="delete" href="javascript:;" onclick="deleteAction(' + "'/manage/organization/delete/','删除组织','organizationId'" + ')" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
    ].join('');
}


//根目录
var basePath = '${basePath}'
//窗口句柄
var dialog
</script>
</body>
</html>