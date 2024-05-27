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

<%
		request.setCharacterEncoding("utf-8");
	 	String html2pdf = (String)request.getParameter("html2pdf");
	 	int hideButton = 0;
		if(html2pdf != null && "true".equals(html2pdf)){
			hideButton = 1;
		}
 		String rp = (String)request.getParameter("rp");
 		String json = (String)request.getParameter("json");
 		String language = request.getParameter("language");
 		String reporttype = request.getParameter("reporttype");
 		String hblattrows = request.getParameter("hblattrows");
	  %>

<body style="align:center">    
  <div id="btns" class='sucaihuo'>
            <a href="#" onclick="a()"><span class="translate">打印</span></a>
            <a href="#" onclick="downLoadPdf()"><span>PDF</span></a>
            <a href="#" onclick="location.href=location.origin + '/scp/reportEdit/file/TemplateEditmbl.jsp' + location.search;" ><span class="translate">调整</span></a>
        </div>
  <div id="con" class="content" style="text-align: left;float: left;"  >
  </div>
  <textarea rows="10" cols="30" id="temp" style="display: none;" ></textarea>
  <input id="tag" style="display: none;" />
</BODY>
<script language="JavaScript">
	auto();
	if (typeof String.prototype.endsWith != 'function') {
		  String.prototype.endsWith = function(suffix) {
		    return this.indexOf(suffix, this.length - suffix.length) !== -1;
		  };
		}
	function auto(){
		if($(window).width() < 1063){
			$(".sucaihuo").css("width","65");
		}else{//224
			$(".sucaihuo").css("width","288");
		}
		if(1==<%=hideButton%>)$(".sucaihuo").remove();
	}
	$(window).resize(function(){auto();})
	
	$(function(){ 
	　　console.log("2342342:"+<%=hideButton%>);
		//alert("2342342:"+<%=hideButton%>);
		if(1==<%=hideButton%>)$(".sucaihuo").remove();
	}); 
	
	var tips = 'SEE ATTACHED';
	$.ajax({
		type:'POST',
		url:"/scp/service?src=webServer&action=commonQuery&querySql=SELECT f_sys_config('hbl_att_rows_tips') AS cfg",
		contentType:'application/json',
		success:function(data){
			if(data == null || data == "")return;
			if(typeof(data)=="string"){
				data = JSON.parse(data);
			}
			if(data.length>0){
				var html = data[0].cfg;
				html = html.replace(/\n/g,'<br/>');
				console.log(html);
				tips = '<span style="line-height: 17px;">'+html+'</span>';
			}
		}	
	});
	
	function autoProcessAttachment(){
		 var hblattrows = '<%=hblattrows%>';
		 if(hblattrows == null || hblattrows == '' || hblattrows == 'null' || hblattrows == '0' || hblattrows == 0){
		 	return;
		 }
		 console.log("hblattrows:"+hblattrows);
		 var goodsnamee = $('div[targetid=goodsnamee]').html();
		 var cntdesc = $('div[targetid=cntdesc]').html();
		 var markhead = $('div[targetid=markhead]').html();
		 
		 var markheadRow = calcRows(markhead);
		 var goodsnameeRow = calcRows(goodsnamee);
		 var cntdescRow = calcRows(cntdesc);
		 
		 console.log("goodsnameeRow:"+goodsnameeRow);
		 console.log("cntdescRow:"+cntdescRow);
		 
		 if(Number(goodsnameeRow) > Number(hblattrows) || Number(cntdescRow) > Number(hblattrows)  || Number(markheadRow) > Number(hblattrows)){
		 	//$('div[targetid=cntdesc]').remove();
		 	$('div[targetid=markhead]').remove();
		 	//$('div[targetid=loaditem]').remove();
		 	//$('div[targetid=piece]').remove();
		 	//$('div[targetid=packer]').remove();
		 	//$('div[targetid=grswgt]').remove();
		 	//$('div[targetid=cbm]').remove();
		 	//$('div[targetid=totledesc]').remove();
		 	//$('div[targetid=freightitemdesc]').remove();
		 	//$('div[targetid=carryitem]').remove();
		 	
		 	$('div[targetid=goodsnamee]').html(tips);
		 }
	 }
	
	function  a(){
		//$("#ddd").jqprint();
		$("div#con").printArea();
		//$("div#ddd").printArea({mode:"popup",popClose:true});
		//window.print()
	}
	
	function loadExtjs(){
		$.ajax({
			type:'GET',
			url:"/scp/service?src=webServer&action=commonQuery&querySql=SELECT extjs FROM sys_report WHERE extjs is not null AND filename = '<%=rp%>' LIMIT 1",
			contentType:'application/json',
			success:function(data){
				if(data == null || data == "")return;
				if(typeof(data)=="string"){
					data = JSON.parse(data);
				}
				//console.log(data);
				if(data.length>0){
					var jsExt = data[0].extjs;
					//console.log(jsExt);
					eval(jsExt);
				}
			}	
		});
	
	}
	
	function calcRows(str){
	    let num=0;
	    if(str == null)return num;
	    num = (str.split('<br>')).length-1;
		return num;
	}
	
	function addpage(){
		 var content=document.getElementById('con');
		 var p=document.createElement('div');
		 p.setAttribute("class","nextpage");
		 content.appendChild(p);
	 }
	 
	function downLoadPdf(){
		//$.get("/scp/service?src=rogtemplete&action=toRogtempletembl2PDF&rp=<%=rp%>&b=<%=request.getParameter("b")%>",
		//	  function(data){
		//	  	console.log(data);
		//	  	window.open(data);
		//	  });
		var htmlStr;
		var btns = $("#btns");
		$("#btns").remove();
		htmlStr = $("html").html();
		$("body").append(btns);	  
		$.ajax({
             type: "post",  //提交方式  
             //data:JSON.stringify({"html":$("html").html()}),
             data:{"html":htmlStr},
             dataType: "json", //收到数据类型  
             //contentType: 'application/json',//发送数据类型
	  	     url:'/scp/service?src=rogtemplete&action=toRogtempletehbl2PDFfile&ismbl=true&rp=<%=rp%>&b=<%=request.getParameter("b")%>',
	  	     success: function (data) { //提交成功的回调函数  
	  	     	console.log(data);
	  	     	window.open(data.results);
	  	     },
	  	     error:function(result){
	  	     	console.log(result);
	  	     }
 		});
	}
 <%
 	
 	if(json == null){
 		json = "";
 	}
	if(!rp.isEmpty()&&json.isEmpty()){
	
  %>
  			var mouseX, mouseY;  
        	var objX, objY;  
        	var isDowm = false;
        		
  			$.get("/scp/service?src=rogtemplete&action=toRogtempletembl&rp="+"<%=rp%>&reporttype=<%=reporttype%>",
			  function(data){
			  	var jsonArray = JSON.parse(data);
			  	var json = jsonArray[0];
			  	json.container.left = json.container.left - 29;//整体左右位置(左减右加)
			  	json.container.top = json.container.top - 26;//整体上下位置(上减下加)
			  	if(data=="''"||data==null){
			  		
			  	}else{
			  		$.get("/scp/service"+location.search+"&src=rogtemplete&action=getViewDatambl",
			  		function(data2){
			  			var print=new Print({
 									id:"con",
 									jdatasource:{"ok":true,"ORG":JSON.parse(data2)}	,
 						template:json
 						});
 						document.getElementById("content").style.backgroundPosition = "25px 23px";//预览背景图相对位置(如修改此处请同时修改jquery.PrintArea.js中getbody()方法)
 						$("#content").css("background-size","827px 1169px");//背景图绝对大小
 						//$("#content").css("font-weight","bold");//字体粗细调整
 						
 						$("#content").find("#node1").mousedown(function(even){
					        $(this).css("cursor","move");
					        $(this).css("zIndex",12000);
					        objX = $(this).css("left");
					        objY = $(this).css("top");
					        mouseX = even.clientX;
					        mouseY = even.clientY;
					        isDowm = true;
 						})
 						$("#content").find("#node1").mousemove(function(even){
 							if($(this).css("cursor")=="move"){
					            var x = even.clientX;
					            var y = even.clientY;
					            if (isDowm) {
					            	$(this).css("left",parseInt(objX) + parseInt(x) - parseInt(mouseX) + "px");
					            	$(this).css("top",parseInt(objY) + parseInt(y) - parseInt(mouseY) + "px");
					            }
				            }
 						})
 						$("#content").find("#node1").mouseup(function(even){
 							 if (isDowm && $(this).css("cursor")=="move") {
				                var x = even.clientX;
				                var y = even.clientY;
				                $(this).css("zIndex","");
				                $(this).css("left",(parseInt(x) - parseInt(mouseX) + parseInt(objX)) + "px");
				                $(this).css("top",(parseInt(y) - parseInt(mouseY) + parseInt(objY)) + "px");
				                mouseX = x;
				                rewmouseY = y;
				                $(this).css("cursor","default")
				                isDowm = false;
				            }
 						})
 						
 						$("#content").find("#node1").click(function(){
 							$("#temp").css("display","block");
 							$("#temp").css("position","absolute");
 							$("#temp").css("top",$(this).css("top"));
 							$("#temp").css("left",$(this).css("left"));
 							if($(this).children().size()==1 && $(this).get(0).getElementsByTagName("p").length == 1){
 								$("#temp").val($(this).children().get(0).innerText);
	 							$("#tag").val($(this).index()+"p");
 							}else{
	 							$("#temp").val($(this).get(0).innerText);
	 							$("#tag").val($(this).index());
 							}
 							$(this).css("display","none");
 							$("#temp").focus();
 						});
 						// 
 							$("#temp").focusout(function(){
 								if($("#tag").val().endsWith("p")){
 									var tmp = $("#tag").val().replace('p','');
 									$(document).find("#node1").eq(tmp).children().get(0).innerText=$(this).val();;
 									$(document).find("#node1").eq(tmp).css("display","block");
 								}else{
 									$(document).find("#node1").eq($("#tag").val()).get(0).innerText=$(this).val();
 									$(document).find("#node1").eq($("#tag").val()).css("display","block");
 								}
 								$(this).css("display","none");
 							})
 						$(".weaizhi").css("font-family","Arial");
 						if('true'==<%=hideButton%>)$(".sucaihuo").remove();
 						
 						autoProcessAttachment();
 						loadExtjs();
			  		});
				}
				$(".weaizhi").css("font-family","Arial");
				if('true'==<%=hideButton%>)$(".sucaihuo").remove();
			  });
 <%
 	}//JSON.parse(data)
 	if(!json.isEmpty()){
 %>
 				var mouseX, mouseY;  
        		var objX, objY;  
        		var isDowm = false;
        		
			  	var jsonArray = <%=json%>;
			  	var json = jsonArray[0];
			  	json.container.left = json.container.left - 29;//整体左右位置(左减右加)
			  	json.container.top = json.container.top - 26;//整体上下位置(上减下加)
			  		$.get("/scp/service"+location.search+"&src=rogtemplete&action=getViewDatambl",
			  		function(data2){
			  			var print=new Print({
 									id:"con",
 									jdatasource:{"ok":true,"ORG":JSON.parse(data2)}	,
 						template:json
 						});
 						document.getElementById("content").style.backgroundPosition = "25px 23px";//预览背景图相对位置(如修改此处请同时修改jquery.PrintArea.js中getbody()方法)
 						$("#content").css("background-size","827px 1169px");//背景图绝对大小
 						//$("#content").css("font-weight","bold");//字体粗细调整
 						
 						$("#content").find("#node1").mousedown(function(even){
					        $(this).css("cursor","move");
					        $(this).css("zIndex",12000);
					        objX = $(this).css("left");
					        objY = $(this).css("top");
					        mouseX = even.clientX;
					        mouseY = even.clientY;
					        isDowm = true;
 						})
 						$("#content").find("#node1").mousemove(function(even){
 							if($(this).css("cursor")=="move"){
					            var x = even.clientX;
					            var y = even.clientY;
					            if (isDowm) {
					            	$(this).css("left",parseInt(objX) + parseInt(x) - parseInt(mouseX) + "px");
					            	$(this).css("top",parseInt(objY) + parseInt(y) - parseInt(mouseY) + "px");
					            }
				            }
 						})
 						$("#content").find("#node1").mouseup(function(even){
 							 if (isDowm && $(this).css("cursor")=="move") {
				                var x = even.clientX;
				                var y = even.clientY;
				                $(this).css("zIndex","");
				                $(this).css("left",(parseInt(x) - parseInt(mouseX) + parseInt(objX)) + "px");
				                $(this).css("top",(parseInt(y) - parseInt(mouseY) + parseInt(objY)) + "px");
				                mouseX = x;
				                rewmouseY = y;
				                $(this).css("cursor","default")
				                isDowm = false;
				            }
 						})
 						
 						$("#content").find("#node1").click(function(){
 							$("#temp").css("display","block");
 							$("#temp").css("position","absolute");
 							$("#temp").css("top",$(this).css("top"));
 							$("#temp").css("left",$(this).css("left"));
 							if($(this).children().size()==1 && $(this).get(0).getElementsByTagName("p").length == 1){
 								$("#temp").val($(this).children().get(0).innerText);
	 							$("#tag").val($(this).index()+"p");
 							}else{
	 							$("#temp").val($(this).get(0).innerText);
	 							$("#tag").val($(this).index());
 							}
 							$(this).css("display","none");
 							$("#temp").focus();
 							$(".weaizhi").css("font-family","Arial");
 						});
 						// 
 							$("#temp").focusout(function(){
 								if($("#tag").val().endsWith("p")){
 									var tmp = $("#tag").val().replace('p','');
 									$(document).find("#node1").eq(tmp).children().get(0).innerText=$(this).val();;
 									$(document).find("#node1").eq(tmp).css("display","block");
 								}else{
 									$(document).find("#node1").eq($("#tag").val()).get(0).innerText=$(this).val();
 									$(document).find("#node1").eq($("#tag").val()).css("display","block");
 								}
 								$(this).css("display","none");
 							})
 						$(".weaizhi").css("font-family","Arial");
 						if('true'==<%=hideButton%>)$(".sucaihuo").remove();
 						
 						autoProcessAttachment();
 						loadExtjs();
			  		});
  <%
  	}
   %>
//document.getElementById("content").style.backgroundPosition = "10px 16px";
	//alert('12312323');
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
	});
</script>
</html>