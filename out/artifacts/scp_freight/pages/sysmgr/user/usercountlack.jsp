<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
    pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/scp/common/js/jquery.min.js"></script>
<style type="text/css">
	h1{text-align: center;}
	samp{font-size: 18px;color: RED;}
</style>
<title>用户数不足</title>
</head>
<body>
<% String sysuser = request.getParameter("sysuser");%>
<% String syscount = request.getParameter("syscount");%>

<div style="border: solid black 0px; width: 100%; height: 200px;">
	<h1>用户数不足</h3>
	<p>已购买用户数：<samp id ="sysuser"></samp></p>
	<p>当前用户数：<samp id ="syscount"></samp></p>
	<p>请联系航迅（林小姐  电话：0755-22969686  QQ：3023141969），购买用户数。</p>

</div>
<script type="text/javascript">
	 $(document).ready(function() {
	 	query();
	 
	  });

	function query(){
		var sysuser = <%=sysuser%>
		var syscount = <%=syscount%>
		$('#sysuser').html(sysuser);
		$('#syscount').html(syscount);
	}

</script>
</body>
</html>