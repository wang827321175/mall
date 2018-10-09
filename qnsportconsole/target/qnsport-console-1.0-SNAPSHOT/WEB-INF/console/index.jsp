<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/console/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>后台首页</title>
</head>
<frameset rows="72,*" frameborder="0" border="0" framespacing="0">
	<frame src="${pageContext.request.contextPath}/top.do" name="topFrame" nojsutilsize="nojsutilsize" id="leftFrame" />
	<frame src="${pageContext.request.contextPath}/main.do" name="mainFrame" id="mainFrame" />
</frameset>
<noframes><body></body></noframes>
</html>
