<%@ page language="java" import="java.util.*" isErrorPage="true" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>错误页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <body>
	<h1>这是一个错误的页面，非常抱歉，出现了一些我没办法解决的操作！</h1>
	<body>
	<% Exception e = null != exception ? (Exception) exception : (Exception)request.getAttribute("ex"); %>
	<h2>错误: <%= e.getClass().getSimpleName()%></h2>
	<hr />
	<h5>错误描述：</h5>
	<%= e.getMessage()%>
	<h5>错误信息：</h5>
	<pre>
	<% e.printStackTrace(new java.io.PrintWriter(out)); %>
	</pre>
  </body>
</html>
