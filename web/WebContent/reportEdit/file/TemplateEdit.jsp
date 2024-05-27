<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE >
<html>
<head>
<meta name="renderer" content="webkit" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TemplateEdit</title>

	<link rel="stylesheet" href="css/bootstrap.min.css">
	<script language="JavaScript" src="SystemEvent.js"></script>
	<script language="JavaScript" src="editorjs/elementProperty.js"></script>
	<script language="JavaScript" src="editorjs/elementNode.js"></script>
	<script language="JavaScript" src="editorjs/Canvas.1.0.1.js"></script>
	<script language="JavaScript" src="editorjs/TemplateEdit.js"></script>
	<script language="JavaScript" src="editorjs/DataSource.js"></script>
	 <script language="JavaScript" src="../../common/js/base64.js"></script>
	 <script language="JavaScript" src="editorjs/Tools.js"></script>
	 <script language="JavaScript" src="editorjs/PublicData.js"></script>
	 <script src="jquery.min.js"></script>
	<script src="dropdown.js"></script>
	<script src="tooltip.js"></script>
	<script language="javascript" src="jquery.jqprint-0.3.js"></script>
	<script type="text/javascript" src="jquery.PrintArea.js"></script>   
	<script src="transition.js"></script>
	<script src="collapse.js"></script>
	<script src="modal.js"></script>
	<script src="layer/layer.js"></script>
	<link href="css/ruler.css" rel="stylesheet" type="text/css" />
	<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
  <script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>
</head>
<%  
	String rp = request.getParameter("rp"); 
	String billid = request.getParameter("b");
	String language = request.getParameter("language");
	String userid = request.getParameter("u");
	String reporttype = request.getParameter("reporttype");
	if(!rp.isEmpty()){
		%>
		<script language="JavaScript">
			$.get("/scp/service?src=rogtemplete&action=toRogtemplete&rp="+"<%=rp%>&reporttype=<%=reporttype%>",
			  function(data){
			  	console.log(data);
			  	if(data=="''"||data==null){
			  		var template=new TemplateEdit({
						jdatasource:{ "json": { 
												"ORG": [ "cnortitle","cneetitle","notifytitle","poa","pol"
													,"vessel","voyage","vesselvoyage","pretrans","pdd","pod","destination","plable","hblsono","hblno","mblno","cntinfos","goodsinfo","goodsinfo2","piece","packer","markhead","cntdesc","grswgt","cbm","atdtitle","atd","totledesc","billcount","signplace","agentitle","etd","eta","carrierfullname","carriername","carrierinfo","dono","piecepacker","remark","dotype","carryitem","freightitemdesc","loaditem","usernamec","usertel","userfax","corpnamec","corpnamee","corpaddressc","now","cnortitlembl","cneetitlembl","notifytitlembl","bkagent","bkagentnamec","bkagentcontact","marksnombl","goodsdescmbl","goodsnamee","goodsnamee2","freightitempp","freightitemcc","ocfprice","ocfamt","ocfamtdhs","payplace","payplacepp","payplacecc","freightcharge","rate","prepaid","collect","mblpol","mblpdd","mblpod","mbldestination","total","totalcurrency","refno","remark_booking","hscode","userqq","cntno","连接符","wmsinnos","paymentitempp","paymentitemcc","polnamecc","podnamecc","poanamecc","pddnamecc","ldtype","sealno","cnotainerinfo","jobno","cnorname","cneename","notifyname","jcustomerc","jcustomere","派送收货人名称","派送地址","派送联系人","派送电话","mbltype","提单要求","委托要求","备注1","备注2","第二通知人","bargeetd","hbltype","agentdesnamec","agentdesnamee","onboarddate"]
											  }
									},
						jTemplates:[]
					});
			  	}else{
			  		var jsonDate = JSON.parse(data)
					for(var p in jsonDate[0].elements){//遍历json数组时，这么写p为索引，0,1
						if(jsonDate[0].elements[p]['fontSize'] == undefined || jsonDate[0].elements[p]['fontSize'] == null){
							jsonDate[0].elements[p]['fontSize']='13px';	
						}
					}
			  		
			  		console.log(jsonDate);
				    var template=new TemplateEdit({
						jdatasource:{ "json": { 
												"ORG": [ "cnortitle","cneetitle","notifytitle","poa","pol" ,"vessel","voyage","vesselvoyage","pretrans","pdd","pod","destination","plable","hblsono","hblno","mblno","cntinfos","goodsinfo","goodsinfo2","piece","packer","markhead","cntdesc","grswgt","cbm","atdtitle","atd","totledesc","billcount","signplace","agentitle","etd","eta","carrierfullname","carriername","carrierinfo","dono","piecepacker","remark","dotype","carryitem","freightitemdesc","loaditem","usernamec","usertel","userfax","corpnamec","corpnamee","corpaddressc","now","cnortitlembl","cneetitlembl","notifytitlembl","bkagent","bkagentnamec","bkagentcontact","marksnombl","goodsdescmbl","goodsnamee","goodsnamee2","freightitempp","freightitemcc","ocfprice","ocfamt","ocfamtdhs","payplace","payplacepp","payplacecc","freightcharge","rate","prepaid","collect","mblpol","mblpdd","mblpod","mbldestination","total","totalcurrency","refno","remark_booking","hscode","userqq","cntno","连接符","wmsinnos","paymentitempp","paymentitemcc","polnamecc","podnamecc","poanamecc","pddnamecc","ldtype","sealno","cnotainerinfo","jobno","cnorname","cneename","notifyname","jcustomerc","jcustomere","派送收货人名称","派送地址","派送联系人","派送电话","mbltype","提单要求","委托要求","备注1","备注2","第二通知人","bargeetd","hbltype","agentdesnamec","agentdesnamee","onboarddate"]
											  }
									},
						jTemplates:jsonDate
					});
				}
				$("#content").css("background-size","827px 1169px");
				//$("#collapseOne1 ul li a").after("[Hello]");
				
				$("#collapseOne1 ul li a").each(function (index,domEle){
				  	var key = this.innerText;
				  	var v = map[key];
				  	if(v != undefined && v != ''){
				  		//console.log(map[key]);
				  		$(this).after("<span style='font-size:8px'>["+v+"]</span>");
				  	}
				});
				
				$("#collapseOne1").css({"height":"700px","overflow":"auto"});
				$('.panel-body li').css('padding','0');
			  });
		</script>
		<%
	}
%>
<body style="overflow: auto;">
<div style="display: none;">
<form action="./print.jsp" method="post" id="from">
	<input type="hidden" id="json" name="json" />
	<%
		
	%>
</form>
</div>
<script language="JavaScript">

	var map = {};
	//[ "cnortitle","cneetitle","notifytitle","poa","pol" ,"vessel","voyage","vesselvoyage","pretrans","pdd","pod","destination","plable","hblsono","hblno","mblno","cntinfos","goodsinfo","piece","packer","markhead","cntdesc","grswgt","cbm","atdtitle","atd","totledesc","freightitemdesc","billcount","signplace","agentitle","etd","carrierfullname","carriername","dono","piecepacker","remark","dotype","carryitem","freightitemdesc","loaditem","usernamec","usertel","userfax","corpnamec","corpnamee","corpaddressc","now","cnortitlembl","cneetitlembl","notifytitlembl","bkagent","bkagentcontact","marksnombl","goodsdescmbl"]
	map["cnortitle"] = "发货人";
	map["cneetitle"] = "收货人";
	map["notifytitle"] = "通知人";
	map["vessel"] = "船名";
	map["voyage"] = "航次";
	map["vesselvoyage"] = "船名/航次";
	map["pretrans"] = "前程运输";
	map["piece"] = "件数";
	map["packer"] = "包装";
	map["marksnombl"] = "唛头MBL";
	map["goodsdescmbl"] = "货描MBL";
	map["loaditem"] = "装船条款";
	map["carryitem"] = "运输条款";
	map["pol"] = "装港";
	map["pod"] = "目的港";
	map["poa"] = "收货地";
	map["pdd"] = "卸港";
	map["destination"] = "目的地";
	map["loaditem"] = "装船条款";
	map["totledesc"] = "合计箱数";
	map["goodsnamee"] = "货品名称";
	map["goodsnamee2"] = "货品名称勾选";
	map["freightitempp"] = "运费条款PP";
	map["freightitemcc"] = "运费条款CC";
	map["freightitemdesc"] = "运费条款";
	map["grswgt"] = "毛重";
	map["cbm"] = "体积";
	map["payplace"] = "付款地点";
	map["payplacepp"] = "付款地点PP";
	map["payplacecc"] = "付款地点CC";
	map["ocfprice"] = "OCF单价";
	map["ocfamt"] = "OCF金额";
	map["ocfamtdhs"] = "OCF-DHS";
	map["hblsono"] = "SO号码";
	map["hblno"] = "HBL提单号";
	map["mblno"] = "MBL提单号";
	map["cntinfos"] = "唛头";
	map["goodsinfo"] = "物品种类与货名";
	map["goodsinfo2"] = "物品种类与货名2";
	map["markhead"] = "唛头";
	map["cntdesc"] = "件数包装货名";
	map["atdtitle"] = "SHIPPED ON BOARD";
	map["atd"] = "实开船日";
	map["billcount"] = "提单份数";
	map["signplace"] = "签单地点";
	map["agentitle"] = "HBL代理";
	map["etd"] = "ETD";
	map["eta"] = "ETA";
	map["carrierfullname"] = "船公司全称";
	map["carriername"] = "船公司名称";
	map["carrierinfo"] = "船公司";
	map["dono"] = "DO";
	map["piecepacker"] = "件数包装";
	map["remark"] = "备注";
	map["dotype"] = "箱量箱型";
	map["usernamec"] = "用户中文名";
	map["usertel"] = "用户电话1";
	map["userfax"] = "用户fax";
	map["corpnamec"] = "公司中文名";
	map["corpnamee"] = "公司英文名";
	map["corpaddressc"] = "公司地址";
	map["now"] = "当前时间";
	map["cnortitlembl"] = "MBL发货人";
	map["cneetitlembl"] = "MBL收货人";
	map["notifytitlembl"] = "MBL通知人";
	map["bkagent"] = "订舱代理全称";
	map["bkagentnamec"] = "订舱代理全称中文";
	map["bkagentcontact"] = "订舱代理联系人";
	
	map["freightcharge"] = "freightcharge";
	map["rate"] = "rate";
	map["prepaid"] = "prepaid";
	map["collect"] = "collect";
	map["mblpol"] = "mbl装港";
	map["mblpdd"] = "mbl卸港";
	map["mblpod"] = "mbl目的港";
	map["mbldestination"] = "mbl目的地";
	map["total"] = "TOTAL";
	map["totalcurrency"] = "totalcurrency";
	map["refno"] = "参考号";
	map["remark_booking"] = "订舱备注";
	map["hscode"] = "商品编码";
	map["userqq"] = "用户QQ";
	map["cntno"] = "柜号";
	map["连接符"] = "连接符";
	map["wmsinnos"] = "入仓单号";
	map["paymentitempp"] = "付款条款PP";
	map["paymentitemcc"] = "付款条款CC";
	map["polnamecc"] = "装港中文";
	map["podnamecc"] = "目的港中文";
	map["poanamecc"] = "收货地中文";
	map["pddnamecc"] = "卸港中文";
	map["ldtype"] = "装箱类型";
	map["sealno"] = "封条号";
	map["cnotainerinfo"] = "柜子明细";
	map["jobno"] = "工作单号";
	map["cnorname"] = "发货人名称";
	map["cneename"] = "收货人名称";
	map["notifyname"] = "通知人名称";
	map["jcustomerc"] = "委托人中文名";
	map["jcustomere"] = "委托人英文名";
	map["mbltype"] = "MBL类型";
	map["hbltype"] = "HBL类型";
	map["bargeetd"] = "驳船ETD";
	map["agentdesnamec"] = "目的港代理中文";
	map["agentdesnamee"] = "目的港代理英文";
	map["onboarddate"] = "装船日期";
	
	

	function saveTemplete(json){
		//console.log("success");
		$.post("/scp/service?src=rogtemplete&action=saveRogtemplete&userid="+<%=userid%>+"&rp="+"<%=rp%>"+"&reporttype="+"<%=reporttype%>", { "json": json },
		function(result){
			alert(result);//弹出返回状态提示
			//if(result.toUpperCase() == "SUCCESS"){
				location.href = location.origin + '/scp/reportEdit/file/print.jsp' + location.search;
			//}
		}
		);
	}
	function printTemplete(json){
		var rp = "<%=rp%>";
		var surl = location.search;
		surl = "?rp="+ rp + surl.substring(surl.indexOf(".raq&")+4)
		var href = './print.jsp' + surl;
		var from = document.getElementById("from");
		var hi = document.getElementById("json");
		hi.value = json;
		from.action = href;
		from.submit();
	}
	function saveTemplateForPrivate(json,text,ispublic){
		var rp = "<%=rp%>";
		var url = location.search.substring(location.search.indexOf(rp)+rp.length);
		$.post("/scp/service?src=rogtemplete&action=checktempletename&reporttype=<%=reporttype%>&rp="+"<%=rp%>"+url+"&ispublic="+ispublic, { "json": json,"text":text },
		function(result){
			var obj = JSON.parse(result);
			if(obj.type=="success"){
				var surl = location.search;
				surl = "?rp="+ obj.value + ".raq"  + surl.substring(surl.indexOf(".raq&")+4)
				location.href = location.origin + '/scp/reportEdit/file/print.jsp' + surl;
			}else{
				alert(obj.value);
			}
		});
	}
	function drawPoint(opts){
    	document.write("<span id='"+opts.point[0]+""+opts.point[1]+"' style='display: inline-block; width: "+opts.pw+"px; height: "+opts.ph+"px; background-color: "+opts.color+"; position: absolute "+opts.point[0]+"px; top: "+opts.point[1]+"px;'>"+(opts.point[2]?("<div style='position: relative;'><span style='position: absolute; left: 5px; bottom: 1px; text-align: left; width: 100px;'>"+opts.point[2]+"</span></div>"):"")+"</span>");
	}
	window.onresize = function(){
		template.ResetPosition();
	}
	
	$(document).ready(function(){
	 	setTimeout(function(){//用定时器是因为直接监听不到
	    	jQuery.i18n.properties({
	            name : 'strings', //资源文件名称
	            path : '/scp/common/i18n/', //资源文件路径
	            mode : 'map', //用Map的方式使用资源文件中的值
	            language : '<%=language%>',
	            callback : function() {//加载成功后设置显示内容
					$('nav ul:.nav.navbar-nav li a:.dropdown-toggle').each(function(){
			    		if($(this).text()!=''&&$(this).text().search('[\u4E00-\u9FA5]')>-1){
			    			$(this).html($.i18n.prop($(this).html()));
			    		}
			    	});
			    	$('nav ul:.nav.navbar-nav li span:contains("连打偏差")').text($.i18n.prop("连打偏差:"))
	            }
	        });
	   	 },10000);	
	});
	
	function drawLine(){
		$("#content").append('<div id="node1" class="weaizhi" style="top: 46px; left: 150px; width: 100px; height: 1px; position: absolute; vertical-align: middle;cursor: default;"><hr></div>');
	}
	
	function drawLineW(){
		$("#content").append('<div id="node1" class="weaizhi" style="top: 46px; left: 150px; width: 1px; height: 1px; position: absolute; vertical-align: middle;cursor: default;"><table style="height: 1260px;border-color:000000;border-left-style:solid;border-width:1px;"><tbody><tr><td valign="top"></td></tr></tbody></table></div>');
	}
	
	
	
	
</script> 
</body>
</html>