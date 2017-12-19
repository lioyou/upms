<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>权限管理系统</title>

	<!-- css导入位置 -->
	<jsp:include page="/resources/include/css.jsp"></jsp:include>

	<style>
		/** skins **/
		
		#header {background: #28A176;}
		.content_tab{background: #28A176;}
		.s-profile>a{background: url('${basePath}/resources/site/images/zheng-upms.png') left top no-repeat;}
	</style>
</head>
<body>
	<!-- 头部导入区 -->
	<jsp:include page="/WEB-INF/layout/header.jsp"></jsp:include>
<main>

	<!-- 左侧导航导入位置 -->
<jsp:include page="/WEB-INF/layout/aside.jsp"></jsp:include>

	<section id="content">
	<!-- /主内容上部导航区 -->
		<div class="content_tab">
			<div class="tab_left">
				<a class="waves-effect waves-light" href="javascript:;"><i class="zmdi zmdi-chevron-left"></i></a>
			</div>
			<div class="tab_right">
				<a class="waves-effect waves-light" href="javascript:;"><i class="zmdi zmdi-chevron-right"></i></a>
			</div>
			<ul id="tabs" class="tabs">
				<li id="tab_home" data-index="home" data-closeable="false" class="cur">
					<a class="waves-effect waves-light" href="javascript:;">首页</a>
				</li>
			</ul>
		</div>


		<!-- 主内容展示区 -->
		<div class="content_main">
		

		</div>
	</section>
</main>
	<!-- 尾部导入区 -->
	<jsp:include page="/WEB-INF/layout/footer.jsp"></jsp:include>
	
	
</body>

	<!-- js导入区 -->
	<jsp:include page="/resources/include/js.jsp"></jsp:include>
	<script type="text/javascript" src="${basePath }/resources/site/js/manage.js"></script></html>