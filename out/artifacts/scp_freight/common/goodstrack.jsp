<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/scp/common/js/jquery.min.js"></script>
<script type="text/javascript" src="/scp/common/js/jquery.toTop.min.js"></script>
<link href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>
<link rel="stylesheet" type="text/css" href="/scp/bpm/res/index.css?t=1">
<link rel="stylesheet" type="text/css" href="/scp/bpm/res/jquery.step.css">

<link rel="stylesheet" href="https://www.layuicdn.com/layui-v2.5.4/css/layui.css" media="all">
<script src="https://www.layuicdn.com/layui-v2.5.4/layui.js" charset="utf-8"></script>

<title>Trace</title>

<style type="text/css">
	.red   
    {
        border: 2px solid #ffffff;
    }
</style>

</head>
<body>
<% 
	String fid = request.getParameter("fkid");
	String iscs = request.getParameter("iscs");
	String language = request.getParameter("language");
	String userid = request.getParameter("userid");
	String jobno = request.getParameter("jobno");
	String cntno = request.getParameter("cntno");
%>
<!--<div style="margin-top: 10px; width: 100%; height: 100%;"><div id="dates"><a href="#" onclick="openmore();" id="others"></a></div>
</div>
-->
<a class="to-top" style="cursor: pointer; display: none; position: fixed; right: 15px; bottom: 30px;">Top</a>

<div class="step-content">
	<div class="step-list" style="display: block;">
		<div class="page-panel-title">
			<a href="javascript:void(0)" style="padding-top: 20px;float: left;">Tracking</a>
			<a href="javascript:void(0)" onclick="showGps()" style="padding-top: 20px;padding-left:40px;float: left;">GPS</a>
		</div>
		<div class="intro-flow" id="dates" style="margin-top:0;">
		<br/>
		<hr/>
		</div>
	</div>
</div>
     
 
<script type="text/javascript">   
	var fid;
	var count = 0;
	var j = 0;
	var dataJson;
	var jobno = '';
	$(function(){
		$('.to-top').toTop();
		jobno = '<%=jobno%>';
		query();
	});
	
	layui.use(['layer'], function() {
		layer = layui.layer //弹层
	});
	
	function showGps(){
		var url = '/scp/pages/module/gps/track.html?jobno='+jobno;
		console.log(url);
		//页面层
	    index = layer.open({
	        type: 2,
	        title:'GPS',
	        maxmin: true,
	        skin: 'layui-layer-rim', //加上边框
	        area: ['800px', '600px'], //宽高
	        content: [url]
	    });
	}
	
	function query(){
		$.ajax({
			type:'POST',
			url:"/scp/service?src=webServer&action=getstatuseinfo&sqlId=servlet.web.ff.getstatuse&userid=<%=userid%>&qry="+(!isEmpty('<%=iscs%>')?"iscs=<%=iscs%>%20AND%20":"")+"fkid='<%=fid%>'",
			async:true,
			contentType:'application/json',
			success:function(data){
				if(data == null || data == "")return;
				if(typeof(data)==="string"){
					data = JSON.parse(data);
				}
				console.log(data);
				if(data){
					$("#statusctable tr").empty();
					var html = '';
					var index = 1;
					var cssActive = "";
					for(var i = 0; i < data.length; i++){
						html+= addHtml(data[i].statusdesc,data[i]);
					}
					$('#dates').append(html);
					$('#dates > div').last().addClass("intro-list-last");
					$(".intro-list").fadeIn(1000);
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
			        
			        shake($(".shake"+(lastActive+1)));
				}
			}
		});
	}
	var index = 1;
	var lastActive = 1;
	var commentuserlast ="";//记录上一步的流转人员
	function addHtml(title,data){
		var html = '';
		var cssActive = "";
		if(index%2==0){
			cssActive="intro-list-active";
		}else{
			cssActive="";
		}
		
		var processer = data.updater;
		var processdate = data.dealdate;
		if(processer==null){
			processer='';
		}else{
			lastActive = index;
		}
		if(processdate==null)processdate='';
		if(index > lastActive){
			cssActive="intro-list-active2";
		}
		html+='<div class="intro-list '+cssActive+'" style="display:none;">';
		html+='		<div class="intro-list-left" style="width:30%"><span class="shake'+index+'">'+title+'</span></div>';
		html+='		<div class="intro-list-right" style="width:70%;padding-right: 15px;">';
		html+='			<span class="shake'+index+'">'+index+'</span>';
		html+='			<div class="intro-list-content">';
		html+='				<div class=""><span class="translate" style="font-weight:bold">处理人</span>:'+processer+'</div>';
		html+='				<div class=""><span class="translate" style="font-weight:bold">处理时间</span>:'+processdate+'</div>';
		html+='			</div>';
		html+='		</div>';
		html+='</div>';
		index++;
		commentuserlast = data.commentuser;
		return html;
	}
	
	//判断字符是否为空的方法
	function isEmpty(obj){
	    if(typeof obj == "undefined" || obj == null || obj == "" || obj == "null"){
	        return true;
	    }else{
	        return false;
	    }
	}
	
	function shake(ele) {
         t = setInterval(function () {
             if(ele.css('visibility') == "visible"){
 				ele.css('visibility',"hidden");
 			}else{
 				ele.css('visibility',"visible");
 			}
         }, 800);
     };
</script>
</body>
</html>
