<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html;charset=GBK" isErrorPage="true"%>
<%@page language="java" session="false"%>
<%@page import="java.io.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href="https://www.layuicdn.com/layui-v2.5.4/css/layui.css" media="all">
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="https://www.layuicdn.com/layui-v2.5.4/layui.js" charset="utf-8"></script>
<title>服务器内部错误500</title>

</head>
<body >
	<blockquote class="layui-elem-quote layui-text">
	    <p class="error">当前页面失效了，请按F5刷新当前页面重试一下，如还不能解决，请联系以下技术人员</p>
	 	<p class="error">The current page is invalid, please press F5 to refresh the current page and try again. If it still can not be solved, please contact the following technical personnel</p>
			
		<p class="links">技术支持(Technical Support):<br/>
				<a id="lily">lily@ufms.cn</a><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3358599169&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:89115698:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a><br/>
				<a id="jerry"> jerry@ufms.cn</a><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1584522655&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:3023141969:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a><br/>
				<a id="susan">susan@ufms.cn</a><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3023141969&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:3023141969:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a><br/>
				<span onclick="$('#showError').toggle();">电话(TEL)：0755-22969686/28705686</span>
		</p>
	</blockquote>
 	
	
	<div class="site-demo-button" id="showError" style="margin-bottom: 0;display: none;">
		<button onclick="show();" class="layui-btn">Show</button>
	  	<button onclick="showDetail();" class="layui-btn">Show detail</button>
	</div>
	
	
	<div id="exception" style="display: none">
		 <pre class="layui-code">
			<%=exception.getLocalizedMessage()%>
      	</pre>
	</div>
	
	<div id="exceptionDetail" style="display: none">
		 <pre class="layui-code">
			<%exception.printStackTrace(new PrintWriter(out));%>	
      	</pre>
	</div>
 
	
	<script>
		layui.use('layer', function(){ //独立版的layer无需执行这一句
	  		var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
	  	});
	  	
	  	function show(){
		      layer.open({
		         type: 1
		        ,title: false
		        ,offset:'auto'
		        ,closeBtn: true
		        ,area: '800px;'
		        ,shade: 0.8
		        ,id: 'LAY_layuiErr' //设定一个id，防止重复弹出
		        ,btnAlign: 'c'
		        ,moveType: 1 //拖拽模式，0或者1
		        ,content: $('#exception').html()
		        ,success: function(layero){
		          
		        }
		      });
	  	}
	  	
	  	function showDetail(){
		      layer.open({
		         type: 1
		        ,title: false
		        ,offset:'auto'
		        ,closeBtn: true
		        ,area: '800px;'
		        ,shade: 0.8
		        ,id: 'LAY_layuiErrDetail'
		        ,btnAlign: 'c'
		        ,moveType: 1
		        ,content: $('#exceptionDetail').html()
		        ,success: function(layero){
		          
		        }
		      });
	  	}
	</script> 
</body>




</html>
