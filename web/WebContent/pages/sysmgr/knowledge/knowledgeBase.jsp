<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/scp/common/js/jquery.min.js"></script>
<script type="text/javascript" src="/scp/common/js/jquery.toTop.min.js"></script>
<link href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>
<link rel="stylesheet" type="text/css" href="./index.css">
<link rel="stylesheet" type="text/css" href="./jquery.step.css">

<title>Knowledge</title>

</head>
<body>
<%
	String language = request.getParameter("language");
	String id = request.getParameter("id");
%>
<!--<div style="margin-top: 10px; width: 100%; height: 100%;"><div id="dates"><a href="#" onclick="openmore();" id="others"></a></div>
		
</div>

-->
<a class="to-top" style="cursor: pointer; display: none; position: fixed; right: 15px; bottom: 30px;">Top</a>


<div class="step-content">
	<div class="step-list" style="display: block;">
		<div class="page-panel-title">
		<h3 class="page-panel-title-left translate">知识库</h3>
		<!--<h3 class="page-panel-title-right">注：黄色为需要你配合的环节</h3>
		-->
		</div>
		<div class="intro-flow" id="dates">
			<!--<div class="intro-list">
				<div class="intro-list-left" >委托人</div>
				<div class="intro-list-right">
					<span>1</span>
					<div class="intro-list-content">.................</div>
				</div>
			</div>
			<div class="intro-list intro-list-active">
				<div class="intro-list-left">订舱代理</div>
				<div class="intro-list-right">
					<span>2</span>
					<div class="intro-list-content"></div>
				</div>
			</div>
		
			<div class="intro-list intro-list-last">
				<div class="intro-list-left">报关行</div>
				<div class="intro-list-right">
					<span>6</span>
					<div class="intro-list-content"></div>
				</div>
			</div>
		-->
		<br/>
		<hr/>
		</div>
	</div>
</div>

<script type="text/javascript"><!--
	$(document).ready(function(){
		$('.to-top').toTop();
		queryfaq();
	});


	function queryfaq(){
		$.ajax({
			type:'POST',
			url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=knowledgeBase.grid.page&qry=id=<%=id%>",
			contentType:'application/json',
			success:function(data){
				console.log(data);
				if(data == null || data == "")return;
				if(typeof(data)==="string"){
					data = JSON.parse(data);
				}
				if(data){
					$('#dates').empty();
					var html = '';
					var index = 1;
					var cssActive = "";
					for(var i = 0; i < data.length; i++){
						
						if(data[i].customerid!=null&&data[i].customerid!='null'){
							html+= addHtml('委托人',data[i].customerid);
						}
						if(data[i].agentid!=null&&data[i].agentid!='null'){
							html+= addHtml('订舱代理',data[i].agentid);
						}
						if(data[i].carrierid!=null&&data[i].carrierid!='null'){
							html+= addHtml('船公司',data[i].carrierid);
						}
						if(data[i].datline!=null&&data[i].datline!='null'){
							html+= addHtml('航线',data[i].datline);
						}
						if(data[i].polid!=null&&data[i].polid!='null'){
							html+= addHtml('起运港',data[i].polid);
						}
						if(data[i].pddid!=null&&data[i].pddid!='null'){
							html+= addHtml('卸货港',data[i].pddid);
						}
						if(data[i].podid!=null&&data[i].podid!='null'){
							html+= addHtml('目的港',data[i].podid);
						}
						if(data[i].truckid!=null&&data[i].truckid!='null'){
							html+= addHtml('托车行',data[i].truckid);
						}
						if(data[i].customid!=null&&data[i].customid!='null'){
							html+= addHtml('报关行',data[i].customid);
						}
						if(data[i].agentdesid!=null&&data[i].agentdesid!='null'){
							html+= addHtml('目的港代理',data[i].agentdesid);
						}
						if(data[i].airline!=null&&data[i].airline!='null'){
							html+= addHtml('航空公司',data[i].airline);
						}
						if(data[i].airagentdesid!=null&&data[i].airagentdesid!='null'){
							html+= addHtml('目的港代理',data[i].airagentdesid);
						}
						if(data[i].airpolid!=null&&data[i].airpolid!='null'){
							html+= addHtml('起飞地点',data[i].airpolid);
						}
						if(data[i].airpodid!=null&&data[i].airpodid!='null'){
							html+= addHtml('降落地点',data[i].airpodid);
						}
						if(data[i].carrierdatline!=null&&data[i].carrierdatline!='null'){
							html+= addHtml('船司航线',data[i].carrierdatline);
						}
						if(data[i].customerpolid!=null&&data[i].customerpolid!='null'){
							html+= addHtml('委托人起运港',data[i].customerpolid);
						}
						if(data[i].customerpodid!=null&&data[i].customerpodid!='null'){
							html+= addHtml('委托人卸货港',data[i].customerpodid);
						}
						if(data[i].customerpddid!=null&&data[i].customerpddid!='null'){
							html+= addHtml('委托人目的港',data[i].customerpddid);
						}
					}
					$('#dates').append(html);
					$('#dates > div').last().addClass("intro-list-last");
					$(".intro-list").fadeIn(3000);
						
					jQuery.i18n.properties({
			            name : 'strings', //资源文件名称
			            path : '/scp/common/i18n/', //资源文件路径
			            mode : 'map', //用Map的方式使用资源文件中的值
			            language : '<%=language%>',
			            callback : function() {//加载成功后设置显示内容
			                $(".translate").each(function(i){
							   $(this).html($.i18n.prop($(this).html()));
							});
			            }
			        });
				}
			}
		});
	}
	var index = 1;
	function addHtml(title,context){
		var html = '';
		var cssActive = "";
		if(index%2==0){
			cssActive="intro-list-active";
		}else{
			cssActive="";
		}
		html+='<div class="intro-list '+cssActive+'" style="display:none;">';
		html+='		<div class="intro-list-left"><span class="translate">'+title+'</span></div>';
		html+='		<div class="intro-list-right">';
		html+='			<span>'+index+'</span>';
		html+='			<div class="intro-list-content">'+context+'</div>';
		html+='		</div>';
		html+='</div>';
		index++;
		return html;
	}
	
</script>
	
</body>
</html>