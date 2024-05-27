<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE >
<html>
<head>
<meta name="renderer" content="webkit" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Air Bill Edit</title>
	<script language="javascript" src="jquery-1.4.4.min.js"></script>
	<script language="JavaScript" src="editorjs/Canvas.1.0.1.js"></script>
  	<script language="javascript" src="editorjs/Printair.js"></script>
  	<script language="JavaScript" src="editorjs/elementNode.js?t=2"></script>
  	<script language="javascript" src="jquery.PrintAreaAir.js"></script>
    <script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>  
  	<script type="text/javascript" src="/scp/common/js/LodopFuncs.js?t=1"></script>
  	<!--<script type="text/javascript" src="printedit.js?t=1"></script>
  
          --><style>
            *{/* Basic CSS reset*/margin:0;padding:0;}
            body{/* These styles have nothing to do with the sucaihuo*/background:white;padding:0px 0 0;margin:auto;text-align:center;}
            .sucaihuo{display:inline-block}
            .sucaihuo:after, .sucaihuo:before{margin-top:0.5em;content: "";float:left;border:1.5em solid #fff;}
            .sucaihuo:after{border-right-color:transparent;}
            .sucaihuo:before{border-left-color:transparent;}
            .sucaihuo a:link, .sucaihuo a:visited{color:#000;text-decoration:none;float:left;height:3.5em;overflow:hidden;}
            .sucaihuo span{background:#fff;display:inline-block;line-height:3em;padding:0 1em;margin-top:0.5em;position:relative;-webkit-transition: background-color 0.2s, margin-top 0.2s;/* Saf3.2+, Chrome*/-moz-transition: background-color 0.2s, margin-top 0.2s;/* FF4+*/-ms-transition: background-color 0.2s, margin-top 0.2s;/* IE10*/-o-transition: background-color 0.2s, margin-top 0.2s;/* Opera 10.5+*/transition: background-color 0.2s, margin-top 0.2s;}
            .sucaihuo a:hover span{background:#FFD204;margin-top:0;}
            .sucaihuo span:before{content: "";position:absolute;top:3em;left:0;border-right:0.5em solid #9B8651;border-bottom:0.5em solid #fff;}
            .sucaihuo span:after{content: "";position:absolute;top:3em;right:0;border-left:0.5em solid #9B8651;border-bottom:0.5em solid #fff;}
			.content{} 
			
			.input{
	            padding:1px 1px;
	            border:1px solid #ddd;
	            background-color:rgba(241,241,241,.98);
	        }
	        .input:empty::before {
	            content: attr(placeholder);
	        }
			
        </style>
  <style media="print">
 .noprint { display: none } 
 .nextpage {page-break-after:always;}
  </style>
  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
  
</head>
<body style="align:center">    
  <div id="btns" class='sucaihuo'><!--
            <a href="#" onclick="a()"><span>打印</span></a>
            <a href="#" onclick="downLoadPdf()"><span>PDF</span></a>
            --><a href="#" onclick="location.href=location.origin + '/scp/reportEdit/file/TemplateEditairnew.jsp' + location.search;" ><span>调整</span></a><!--
            <a href="#" onclick="printHBL()"><span class="translate">正本</span></a>
            --><a href="#" onclick="submitdate()"><span class="translate">保存</span></a>
        </div>
  <div id="con" class="content" style="text-align: left;float: left;"  >
  </div>
	
	<textarea rows="10" cols="50" id="temp" style="display: none;border:0;border-radius:5px;background-color:rgba(241,241,241,.98);padding: 2px;font-size: 12px; text-align: left; vertical-align: middle; line-height: 13px; font-family: Arial, 'Times New Roman', 宋体;resize: none;"></textarea>
	<input id="tag" style="display: none;" />
</BODY>
<script language="JavaScript"><!--
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
			$(".sucaihuo").css("width","384");
		}
	}
	$(window).resize(function(){auto();})
	
function  a(){
//$("#ddd").jqprint();
$("div#con").printArea();
//$("div#ddd").printArea({mode:"popup",popClose:true});
//window.print()
}

 var targetidEdit;

 function addpage(){
	 var content=document.getElementById('con');
	 var p=document.createElement('div');
	 p.setAttribute("class","nextpage");
	 content.appendChild(p);
 }
 <%
 	request.setCharacterEncoding("utf-8");
 	String rp = (String)request.getParameter("rp");
 	String json = (String)request.getParameter("json");
 	String reporttype = request.getParameter("reporttype");
 	String datatype = request.getParameter("datatype");
 	String jobid = (String)request.getParameter("jobid");
 	if(json == null){
 		json = "";
 	}
	if(!rp.isEmpty()&&json.isEmpty()){
	
  %>
  			var mouseX, mouseY;  
        	var objX, objY;  
        	var isDowm = false;
        		
  			$.get("/scp/service?src=rogtemplete&action=toRogtempleteair&rp="+"<%=rp%>&reporttype=<%=reporttype%>",
			  function(data){
			  	
			  	var jsonArray = JSON.parse(data);
			  	var json = jsonArray[0];
			  	json.container.left = json.container.left - 35;//整体左右位置(左减右加)
			  	json.container.top = json.container.top - 30;//整体上下位置(上减下加)
			  	if(data=="''"||data==null){
			  	}else{
			  		$.get("/scp/service"+location.search+"&src=rogtemplete&action=getViewDataairnew",
			  		function(data2){
			  			//console.log(data2);
			  			var print=new Print({
 									id:"con",
 									jdatasource:{"ok":true,"HAWB":JSON.parse(data2)}	,
 									template:json
 						});
 						document.getElementById("content").style.backgroundPosition = "1px 23px";//预览背景图相对位置(如修改此处请同时修改jquery.PrintArea.js中getbody()方法)
 						$("#content").css("background-size","827px 1169px");//背景图绝对大小
 						//$("#content").css("font-weight","bold");//字体粗细调整
 						
 						$('div[targetid="chargeweight2"]').mouseout(function(){
							var	a = $('div[targetid="chargeweight2"]').text();
							var	b = $('div[targetid="charge"]').text();
							if(a !="" && a != null && b !="" && b != null){
								$('div[targetid="amount"]').text(parseFloat(a)*parseFloat(b))
							}
							});
 							
						$('div[targetid="charge"]').mouseout(function(){
							var	a = $('div[targetid="chargeweight2"]').text();
							var	b = $('div[targetid="charge"]').text();
							if(a !="" && a != null && b !="" && b != null){
								$('div[targetid="amount"]').text(parseFloat(a)*parseFloat(b))
							}
						});
						
						$('div[targetid="amount"]').focus(function(){
							var	a = $('div[targetid="chargeweight2"]').text();
							var	b = $('div[targetid="charge"]').text();
							if(a !="" && a != null && b !="" && b != null){
								$('div[targetid="amount"]').text(parseFloat(a)*parseFloat(b))
							}
						});
						
 						
						$("#content").find("#node1").click(function(){
							
							if($(this).attr("targetid") == "otherfeepp"){
								return;
							}
							
							targetidEdit = $(this).attr("targetid");
							
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
							console.log(':' + $(this).height());
							//$("#temp").css('width',$(this).css("width")).css('height',$(this).css("height"));
							$("#temp").width($(this).width()+4);
							$("#temp").height($(this).height()+4);
							$("#temp").focus();
						});
						
						$("#temp").focusout(function(){
							if($("#tag").val().endsWith("p")){
								var tmp = $("#tag").val().replace('p','');
								$(document).find("#node1").eq(tmp).children().get(0).innerText=$(this).val().toUpperCase();
								$(document).find("#node1").eq(tmp).css("display","block");
							}else{
								$(document).find("#node1").eq($("#tag").val()).get(0).innerText=$(this).val().toUpperCase();
								$(document).find("#node1").eq($("#tag").val()).css("display","block");
							}
							//将每次textarea里面修改后的值，放入到对应json中取，提交时一次提交
							var valEdit = $(this).val().toUpperCase();
							console.log(targetidEdit + ':' + valEdit);
							jsonData[targetidEdit] = valEdit;
							
							$(this).css("display","none");
						})
				
						$('.weaizhi').addClass('input');
						$('.weaizhi').attr('contenteditable','true');
						$('div[targetid="otherfeepp"]').dblclick(function(){
							var x = window.screen.height;
						    var y = window.screen.width;
						    var h = 500;
						    var w = 800;
						    var model = "title=word,resizable=yes,scrollbars=yes,height=" + h + ",width=" + w + ",status=yes,toolbar=no,menubar=no,location=no,top=" + (x-h)/2 + ",left=" + (y-w)/2;
						    var url = "/scp/pages/module/air/airextfee.xhtml?type=choose&jobid=<%=jobid%>";
						    var oOpen = window.open(url,"adviceDetail",model);
						    oOpen.focus();
						   // $('div[targetid="otherfeepp"]').text("11111111")
						})
			  		});
				}
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
			  	json.container.left = json.container.left - 35;//整体左右位置(左减右加)
			  	json.container.top = json.container.top - 30;//整体上下位置(上减下加)
			  		$.get("/scp/service"+location.search+"&src=rogtemplete&action=getViewDataairnew",
			  		function(data2){
					  //	console.log(data2);
			  			var print=new Print({
 									id:"con",
 									jdatasource:{"ok":true,"HAWB":JSON.parse(data2)}	,
 						template:json
 						});
 						document.getElementById("content").style.backgroundPosition = "1px 23px";//预览背景图相对位置(如修改此处请同时修改jquery.PrintArea.js中getbody()方法)
 						$("#content").css("background-size","827px 1169px");//背景图绝对大小
 						//$("#content").css("font-weight","bold");//字体粗细调整
 						
 						$('div[targetid="chargeweight2"]').blur(function(){
							var	a = $('div[targetid="chargeweight2"]').text();
							var	b = $('div[targetid="charge"]').text();
							if(a !="" && a != null && b !="" && b != null){
								$('div[targetid="amount"]').text(parseFloat(a)+parseFloat(b))
							}
							});
 							
						$('div[targetid="charge"]').blur(function(){
							var	a = $('div[targetid="chargeweight2"]').text();
							var	b = $('div[targetid="charge"]').text();
							if(a !="" && a != null && b !="" && b != null){
								$('div[targetid="amount"]').text(parseFloat(a)+parseFloat(b))
							}
						});
 						
 						$("#content").find("#node1").click(function(){
 							if($(this).attr("targetid") == "otherfeepp"){
								return;
							}
							targetidEdit = $(this).attr("targetid");
							
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
 							console.log($(this).attr("width"));
 							$("#temp").css('width',$(this).css("width")).css('height',$(this).css("height"));
 							$("#temp").focus();
 						});
 						
 						$("#temp").focusout(function(){
							if($("#tag").val().endsWith("p")){
								var tmp = $("#tag").val().replace('p','');
								$(document).find("#node1").eq(tmp).children().get(0).innerText=$(this).val().toUpperCase();
								$(document).find("#node1").eq(tmp).css("display","block");
							}else{
								$(document).find("#node1").eq($("#tag").val()).get(0).innerText=$(this).val().toUpperCase();
								$(document).find("#node1").eq($("#tag").val()).css("display","block");
							}
							//将每次textarea里面修改后的值，放入到对应json中取，提交时一次提交
							var valEdit = $(this).val().toUpperCase();
							console.log(targetidEdit + ':' + valEdit);
							jsonData[targetidEdit] = valEdit;
							
							$(this).css("display","none");
						})
						
						$('.weaizhi').addClass('input');
						$('.weaizhi').attr('contenteditable','true');

						$('div[targetid="otherfeepp"]').dblclick(function(){
							var x = window.screen.height;
						    var y = window.screen.width;
						    var h = 500;
						    var w = 800;
						    var model = "title=word,resizable=yes,scrollbars=yes,height=" + h + ",width=" + w + ",status=yes,toolbar=no,menubar=no,location=no,top=" + (x-h)/2 + ",left=" + (y-w)/2;
						    var url = "/scp/pages/module/air/airextfee.xhtml?type=choose&jobid=<%=jobid%>";
						    var oOpen = window.open(url,"adviceDetail",model);
						    oOpen.focus();
						   // $('div[targetid="otherfeepp"]').text("11111111")
						})
			  		});
  <%
  	}
   %>
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
	
	
	function printHBL(){
		var html = $("#con").html();
		LODOP=getLodop();  
		LODOP.PRINT_INIT("");
		//LODOP.SET_PRINT_PAGESIZE(3,"2100mm","2970mm","A4");
		LODOP.SET_PRINT_PAGESIZE(1,"210mm","325mm","A4");
		LODOP.ADD_PRINT_HTM(-27,0,"100%","100%",html);
		//LODOP.SET_PRINT_PAGESIZE(1,paper_w.getValue(),paper_h.getValue(),paper_name.getValue());
		//LODOP.ADD_PRINT_HTM(page_back_top.getValue(),page_back_left.getValue(),page_w.getValue(),page_h.getValue(),html);
		LODOP.PREVIEWA();
	}
	
	
	
	
	function otherfeeinto(fee){
		$('div[targetid="otherfeepp"]').text(fee);
	}
	
	
	var jsonData = {};
	
	function submitdate(){
		var billid = getRequest().b;
		jsonData.id = billid;
		jsonData = JSON.stringify(jsonData);
		$.ajax({
			type: 'POST',
			url: getContextPath()+"/service?src=rogtemplete&action=saveDataAirEdit&rp=", 
			contentType: 'application/json',//发送数据类型
			data: jsonData,
			success:function(result){
			    alert("OK!"); 
			},
			error:function(result){
			    alert("ERROR!"+result); 
			}
	    });
		
		
	}
	
	
	function getContextPath(){ 
		var pathName = document.location.pathname; 
		var index = pathName.substr(1).indexOf("/"); 
		var result = pathName.substr(0,index+1); 
		return result; 
	} 

	function getRequest() {
	    var url = location.search; // 获取url中含"?"符后的字串
		var theRequest = new Object();
		if (url.indexOf("?") != -1) {
			var str = url.substr(1);
			strs = str.split("&");
			for ( var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
			}
		}
		return theRequest;
	}
	
//document.getElementById("content").style.backgroundPosition = "10px 16px";
--></script>
</html>