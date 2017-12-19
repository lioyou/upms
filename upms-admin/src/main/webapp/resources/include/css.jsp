<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!-- BEGIN BOOTSTRAP CSS -->
	<link rel="stylesheet" href="${basePath}/resources/dependence/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${basePath}/resources/dependence/bootstrap/css/bootstrap-theme.min.css">
<!-- END BOOTSTRAP CSS -->

	<!-- BENGIN PLUGINS CSS -->
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/bootstrap-table-1.11.0/bootstrap-table.min.css">
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css">
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/select2/css/select2.css">
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/select2/theme/select2-bootstrap.css">

	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/waves-0.7.5/waves.min.css">
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/jquery-confirm/jquery-confirm.min.css">
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/webuploader-0.1.5/webuploader.css">
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/material-design-iconic-font-2.2.0/css/material-design-iconic-font.min.css"/>
	<link rel="stylesheet" href="${basePath}/resources/dependence/plugins/malihu-custom-scrollbar-plugin/jquery.mCustomScrollbar.min.css"/>
<!-- END PLUGINS CSS -->

<!-- BEGIN SITE CSS -->
	<link rel="stylesheet" href="${basePath}/resources/site/css/common.css">
	<link  rel="stylesheet" href="${basePath}/resources/site/css/admin.css"/>
<!-- END SITE CSS -->
