<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>权限管理系统</title>
    <!-- CSS文件导入位置 -->
	<jsp:include page="/resources/include/css.jsp"></jsp:include>
	<link  rel="stylesheet" href="${basePath}/resources/site/css/login.css"/>
</head>
<body>
<div id="login-window">
	<form id="login"> 
	    <div class="input-group m-b-20">
	        <span class="input-group-addon"><i class="zmdi zmdi-account"></i></span>
	        <div class="fg-line">
	            <input id="username" type="text" class="form-control" name="username" value="leecp">
	        </div>
	    </div>
	    <div class="input-group m-b-20">
	        <span class="input-group-addon"><i class="zmdi zmdi-male"></i></span>
	        <div class="fg-line">
	            <input id="password" type="password" class="form-control" name="password" value="123456">
	        </div>
	    </div>
	    <button id="login-bt" type="submit" class="waves-effect waves-button waves-float"><i class="zmdi zmdi-arrow-forward"></i></button>
	</form>
</div>
</body>
<jsp:include page="/resources/include/js.jsp"></jsp:include>
<script>
	//根目录
	var basePath = "${basePath}";
</script>
<script src="${basePath}/resources/site/js/login.js"></script>
</html>
