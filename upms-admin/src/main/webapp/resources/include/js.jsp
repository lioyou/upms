<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>


	<!-- BEGIN JQUERY JAVASCRIPT -->
	<script type="text/javascript" src="${basePath}/resources/dependence/jquery-2.2.4.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/dependence/plugins/jquery-confirm/jquery-confirm.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/dependence/plugins/zTree_v3/js/jquery.ztree.all.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/dependence/plugins/jquery-validation/jquery.validate.min.js"></script>
	<!-- END JQUERY JAVASCRIPT -->

<!-- BEGIN BOOTSTRAP JAVASCRIPT -->
	<script type="text/javascript" src="${basePath}/resources/dependence/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/dependence/plugins/bootstrap-table-1.11.0/bootstrap-table.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/dependence/plugins/bootstrap-table-1.11.0/locale/bootstrap-table-zh-CN.min.js"></script>
<!-- END BOOTSTRAP JAVASCRIPT -->

<!-- BEGIN JAVASCRIPT -->
	<script type="text/javascript" src="${basePath}/resources/dependence/plugins/waves-0.7.5/waves.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/dependence/plugins/select2/js/select2.min.js"></script>
	<script type="text/javascript" src="${basePath}/resources/dependence/plugins/webuploader-0.1.5/webuploader.min.js"></script>
<!-- END JAVASCRIPT -->

<script type="text/javascript"  src="${basePath}/resources/dependence/plugins/malihu-custom-scrollbar-plugin/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript"  src="${basePath}/resources/dependence/plugins/BootstrapMenu.min.js"></script>
<script type="text/javascript"  src="${basePath}/resources/dependence/plugins/device.min.js"></script>
<script type="text/javascript"  src="${basePath}/resources/dependence/plugins/jquery.cookie.js"></script>
<script type="text/javascript"  src="${basePath}/resources/dependence/plugins/fullPage/jquery.fullPage.min.js"></script>
<script type="text/javascript"  src="${basePath}/resources/dependence/plugins/fullPage/jquery.jdirk.min.js"></script>
<!-- BEGIN SITE JAVASCRIPT -->
	<script type="text/javascript" src="${basePath}/resources/site/js/common.js"></script>
	<script type="text/javascript" src="${basePath}/resources/site/js/admin.js"></script>
	<script type="text/javascript" src="${basePath}/resources/site/js/action.js"></script>
	<script type="text/javascript" src="${basePath}/resources/site/js/submit.js"></script>
	<script type="text/javascript" src="${basePath}/resources/site/js/fileupload.js"></script>
<!-- END SITE JAVASCRIPT -->
