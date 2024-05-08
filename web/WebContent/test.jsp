<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html;charset=GBK"%>
<%@page language="java" session="false"%>
<%@page import="java.io.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href="https://www.layuicdn.com/layui-v2.5.4/css/layui.css" media="all">
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://www.layuicdn.com/layui-v2.5.4/layui.js" charset="utf-8"></script>
<title>test</title>

</head>
<body >
	<blockquote class="layui-elem-quote layui-text">
	    <%
	    	for(int i = 0 ; i< 100; i++){
	    		out.println(i);
	    	}
	     %>
	</blockquote>
 	
	
	<div class="site-demo-button" id="showError" style="margin-bottom: 0;display: none;">
		<button onclick="show();" class="layui-btn">Show</button>
	  	<button onclick="showDetail();" class="layui-btn">Show detail</button>
	</div>
	
	
	<script>
		
	</script> 
</body>




</html>
