<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/scp/common/js/jquery.min.js"></script>
<script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>  

<link href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<title>Insert title here</title>
<style type="text/css">
body {
		margin: 0px;
		padding: 0px;
		width: 100%;
		height: 100%;
		font-size: 12px;
		font-family:  "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
		color: #676a6c;
	}

table{
		border:1px solid #e7eaec;
		width: 100%;
		border-spacing:0px;
		border-collapse:collapse;
		font-family:  "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
	}
	
	.xq1{
		width: 64.3%;
		border: 1px solid #e7eaec;
		text-align: left;
	}
	
	.xq2{
		width: 16.9%;
		border: 1px solid #e7eaec;
		text-align: center;
	}

	.xq3{
		border: 1px solid #e7eaec;
		text-align: center;
		width: 18.8%;
	}
</style>
</head>
<body>
<% 
	String userid = request.getParameter("userid");
	String language = request.getParameter("language");
%>
<div style="margin-top:10px; width: 100%; height: 100%;">
<div style="text-align: right;"><a href="#" onclick="openmore();" id="others">查看更多</a></div>
	<table id="msg">
		<thead>
			<tr>
				<td class="xq1" id="title">标题</td>
				<td class="xq2" id="publisher">发布人</td>
				<td class="xq3" id="publishtime">发布时间</td>
			</tr>
		</thead>
		<tbody>
			<tr>
			</tr>
		</tbody>
	
	</table>
</div>


<script type="text/javascript">
	$(document).ready(function(){
		querymsg();
		
		jQuery.i18n.properties({
            name : 'strings', //资源文件名称
            path : '/scp/common/i18n/', //资源文件路径
            mode : 'map', //用Map的方式使用资源文件中的值
            language : '<%=language%>',
            callback : function() {//加载成功后设置显示内容
                $('#title').html($.i18n.prop('标题'));
                $('#publisher').html($.i18n.prop('发布人'));
                $('#publishtime').html($.i18n.prop('发布时间'));
                 $('#others').html($.i18n.prop('查看更多'));
            }
        });
	});


	function querymsg(){
		$.ajax({
			type:'POST',
			url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.querymsg&qry='<%=userid%>'",
			contentType:'application/json',
			success:function(data){
				if(data == null || data == "")return;
				if(typeof(data)==="string"){
					data = JSON.parse(data);
				}
				if(data){
					var html;
					for(var i = 0; i < data.length; i++){
						html = "<tr id='tr"+i+"'>";
						html+="<td class='xq1'><a href='./msgshow.xhtml?id="+data[i].id+"' target='_blank'><span>"+data[i].title+"</span></a></td>";
						html+="<td class='xq2'><span>"+data[i].inputer+"</span></td>";
						html+="<td class='xq3'><span>"+data[i].inputtime2+"</span></td>";
						html+="</tr>";
						$("#msg tbody").append(html);
					}
				}
			}
		});
	}
	
	
	function openmore(){
		window.open("/scp/pages/sysmgr/message/msg.xhtml")
	}
</script>
</body>
</html>