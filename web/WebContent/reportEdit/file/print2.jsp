<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE >
<html>
<head>
<meta name="renderer" content="webkit" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Print Preview</title>
<script language="javascript" src="jquery-1.4.4.min.js"></script>
	<script language="JavaScript" src="editorjs/Canvas.1.0.1.js"></script>
  <script language="javascript" src="editorjs/Print.js"></script>
  	<script language="JavaScript" src="editorjs/elementNode.js"></script>
  <script language="javascript" src="jquery.PrintArea.js"></script>
  <script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>  
  
  <script type="text/javascript" src="/scp/common/js/LodopFuncs.js?t=1"></script>
  
          <style>
            *{/* Basic CSS reset*/margin:0;padding:0;}
            body{/* These styles have nothing to do with the sucaihuo*/background:white;padding:0px 0 0;margin:auto;text-align:center;}
            .sucaihuo{display:inline-block;}
            .sucaihuo:after, .sucaihuo:before{margin-top:0.5em;content: "";float:left;border:1.5em solid #fff;}
            .sucaihuo:after{border-right-color:transparent;}
            .sucaihuo:before{border-left-color:transparent;}
            .sucaihuo a:link, .sucaihuo a:visited{color:#000;text-decoration:none;float:left;height:3.5em;overflow:hidden;}
            .sucaihuo span{background:#fff;display:inline-block;line-height:3em;padding:0 1em;margin-top:0.5em;position:relative;-webkit-transition: background-color 0.2s, margin-top 0.2s;/* Saf3.2+, Chrome*/-moz-transition: background-color 0.2s, margin-top 0.2s;/* FF4+*/-ms-transition: background-color 0.2s, margin-top 0.2s;/* IE10*/-o-transition: background-color 0.2s, margin-top 0.2s;/* Opera 10.5+*/transition: background-color 0.2s, margin-top 0.2s;}
            .sucaihuo a:hover span{background:#FFD204;margin-top:0;}
            .sucaihuo span:before{content: "";position:absolute;top:3em;left:0;border-right:0.5em solid #9B8651;border-bottom:0.5em solid #fff;}
            .sucaihuo span:after{content: "";position:absolute;top:3em;right:0;border-left:0.5em solid #9B8651;border-bottom:0.5em solid #fff;}
			.content{}
	        
	        page-break-inside: avoid !important;
        </style>
  <style media="print">
 .noprint { display: none } 
 .nextpage {page-break-after:always;}
 .translate{}
  </style>
  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
  
</head>

<body style="align:center" >    
	<div id="con" class="content" style="text-align: left;float: left;"  >
	</div>
</BODY>

<%
 	request.setCharacterEncoding("utf-8");
 	String html2pdf = (String)request.getParameter("html2pdf");
 	int hideButton = 0;
	if(html2pdf != null && "true".equals(html2pdf)){
		hideButton = 1;
	}
	String billid = (String)request.getParameter("b");
	String userid = (String)request.getParameter("u");
	
	String rp = (String)request.getParameter("rp");
 	String json = (String)request.getParameter("json");
 	String language = request.getParameter("language");
 	String reporttype = request.getParameter("reporttype");
 	String hblattrows = request.getParameter("hblattrows");
  %>

<script language="JavaScript">
	auto();
	//为低版本chrome和IE添加endsWith方法
	if (typeof String.prototype.endsWith != 'function') {
		  String.prototype.endsWith = function(suffix) {
		    return this.indexOf(suffix, this.length - suffix.length) !== -1;
		  };
		}
	function auto(){
		if($(window).width() < 1063){
			$(".sucaihuo").css("width","65");
		}else{//224
			$(".sucaihuo").css("width","420");
		}
		if(1==<%=hideButton%>)$(".sucaihuo").remove();
	}
	$(window).resize(function(){auto();})

	$(function(){ 
			if(1==<%=hideButton%>)$(".sucaihuo").remove();
	});
	
	var rp = '<%=rp%>';
	var reporttype = <%=reporttype%>;
	var billid = '<%=billid%>' + ',';
	var userid = '<%=userid%>';
	
	var billids= new Array(); 
	billids =billid.split(","); 
	for (i=0;i<billids.length ;i++ ){
		showPrintContent(rp,reporttype, billids[i] , userid);
	}
	
	
	
	function addpage(){
		 var content=document.getElementById('con');
		 var p=document.createElement('div');
		 p.setAttribute("class","nextpage");
		 content.appendChild(p);
	 }
 

	function showPrintContent(rp,reporttype , billid , userid){
		var mouseX, mouseY;  
	   	var objX, objY;  
	   	var isDowm = false;
		   		
		$.get("/scp/service?src=rogtemplete&action=toRogtemplete&rp="+rp+"&reporttype="+reporttype,
		  function(data){
		  	var jsonArray = JSON.parse(data);
		  	var json = jsonArray[0];
		  	console.log(json);
		  	json.container.left = json.container.left - 29;//整体左右位置(左减右加)
		  	json.container.top = json.container.top - 26;//整体上下位置(上减下加)
		  	if(data=="''"||data==null){
		  		
		  	}else{
		  		var args = location.search;
		  		args = '&rp='+rp+'&b='+billid+'&u='+userid;
		  		$.get("/scp/service?src=rogtemplete&action=getViewData"+args,
		  		function(data2){
		  			var print=new Print({
									id:"con",
									jdatasource:{"ok":true,"ORG":JSON.parse(data2)}	,
									template:json
						});
						$("div[id*='content']").css("background-size","827px 1169px");//背景图绝对大小  同名id，不能用id选择器，只能找到一个就返回
						$("div[id*='content']").css("background-position","25px 23px");//预览背景图相对位置(如修改此处请同时修改jquery.PrintArea.js中getbody()方法)
						//$("#content").css("font-weight","bold");//字体粗细调整
						$("#content").parent().append('<div style="page-break-before:always"></div>');
		  		});
			}
		  });	
	
	}

	function showConfig(){
		$('#configDiv').css('display','block');
	}
	function closeConfigDiv(){
		$('#configDiv').css('display','none');
	}
	
	
	var fontChange = false;
	function setFont(){
		if(fontChange == false){
			$('.weaizhi').css('font-family',' Courier New');
			//$('.weaizhi').css('font-weight',' 700');
			fontChange = true;
		}else{
			$('.weaizhi').css('font-family',' Arial, "Times New Roman", 宋体');
			//$('.weaizhi').css('font-weight',' normal');
			fontChange = false;
		}
	}
	
	function left(){
		var loffset = $('#leftConfig').val();
		$(".weaizhi").each(function(){
	    	//console.log($(this).offset().left);
	    	$(this).css('left',($(this).offset().left- loffset));
	  	});
	}

	function printHBL(){
		var html = $("#con").html();
		LODOP=getLodop();  
		LODOP.PRINT_INIT("");
		//LODOP.SET_PRINT_PAGESIZE(3,"2100mm","2970mm","A4");
		LODOP.SET_PRINT_PAGESIZE(1,"210mm","325mm","A4");
		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
		//LODOP.SET_PRINT_PAGESIZE(1,paper_w.getValue(),paper_h.getValue(),paper_name.getValue());
		//LODOP.ADD_PRINT_HTM(page_back_top.getValue(),page_back_left.getValue(),page_w.getValue(),page_h.getValue(),html);
		LODOP.PREVIEWA();
	}
	
	function downLoadPdf(){
		//$.get("/scp/service?src=rogtemplete&action=toRogtempletehbl2PDF&rp=<%=rp%>&b=<%=request.getParameter("b")%>",
		//	  function(data){
		//	  	console.log(data);
		//	  	window.open(data);
		//});
		var htmlStr;
		var btns = $("#btns");
		$("#btns").remove();
		htmlStr = $("html").html();
		$("body").append(btns);
		
		var url  = '/scp/service?src=rogtemplete&action=toRogtempletehbl2PDFfile&rp=<%=rp%>&b=<%=request.getParameter("b")%>';
		console.log('url:'+url);
		
		$.ajax({
	             type: "post",  //提交方式  
	             //data:JSON.stringify({"html":$("html").html()}),
	             //$(".sucaihuo").remove();
	             data:{"html":htmlStr},
	             dataType: "json", //收到数据类型  
	             //contentType: 'application/json',//发送数据类型
		  	     url:'/scp/service?src=rogtemplete&action=toRogtempletehbl2PDFfile&rp=<%=rp%>&b=<%=request.getParameter("b")%>',
		  	     success: function (data) { //提交成功的回调函数  
		  	     	console.log(data);
		  	     	window.open(data.results);
		  	     },
		  	     error:function(result){
		  	     	console.log(result);
		  	     }
	 	});
	}
	
	function  a(){
		//$("#ddd").jqprint();
		$("div#con").printArea();
		//$("div#ddd").printArea({mode:"popup",popClose:true});
		//window.print()
	}
	
	$(document).ready(function(){
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
	        //$("#content").css("padding","25 23");//预览界面图片向右下偏移
	});
	
	window.onload = function () {
   		$('div[targetid="freightcharge"]').css('line-height','20px');
   		$('div[targetid="collect"]').css('line-height','20px');
   		$('div[targetid="rate"]').css('line-height','20px');
   		$('div[targetid="prepaid"]').css('line-height','20px');
	}
   

</script>
</html>