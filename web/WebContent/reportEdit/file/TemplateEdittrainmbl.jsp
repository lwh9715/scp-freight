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
			$.get("/scp/service?src=rogtemplete&action=toRogtempletetrainmbl&rp="+"<%=rp%>&reporttype=<%=reporttype%>",
			  function(data){
			  	//console.log(typeof(data));
			  	if(data=="''"||data==null){
			  		var template=new TemplateEdit({
						jdatasource:{ "json": { "ORG": [ "cnortitlembl","cneetitlembl","notifytitlembl","agentitlembl","poa","pol","pdd","pod" ,"mblpol","mblpod","mblpdd","mbldestination","vessel","voyage","vesselvoyage","pretrans","destination","plable","mblsono","mblno","cntinfos","goodsinfo","carryitem","loaditem","piece","packer","markhead","cntdesc","grswgt","cbm","atd","totledesc","freightitemdesc","billcount","signplace","agentitle","etd","carrierfullname","dono","piecepacker","remark","paymentitem","bkagent","bkagentcontact","marksnombl","goodsdescmbl","goodsnamee","goodsnamee2","freightitempp","freightitemcc","payplace","payplacepp","payplacecc","usernamee","tel1","tel2","email1","email2","fax","jobno","freightitemmbl","remark_booking","cntdesc","telrelnos","telreler"]}},
						jTemplates:[]
					});
			  	}else{
				    var template=new TemplateEdit({
						jdatasource:{ "json": { "ORG": [ "cnortitlembl","cneetitlembl","notifytitlembl","agentitlembl","poa","pol","pdd","pod" ,"mblpol","mblpod","mblpdd","mbldestination","vessel","voyage","vesselvoyage","pretrans","destination","plable","mblsono","mblno","cntinfos","goodsinfo","carryitem","loaditem","piece","packer","markhead","cntdesc","grswgt","cbm","atd","totledesc","freightitemdesc","billcount","signplace","agentitle","etd","carrierfullname","dono","piecepacker","remark","paymentitem","bkagent","bkagentcontact","marksnombl","goodsdescmbl","goodsnamee","goodsnamee2","freightitempp","freightitemcc","payplace","payplacepp","payplacecc","usernamee","tel1","tel2","email1","email2","fax","jobno","freightitemmbl","remark_booking","cntdesc","telrelnos","telreler"]}},
						jTemplates:JSON.parse(data)
					});
				}
				$("#content").css("background-size","827px 1169px");
				
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
<form action="./printtrainmbl.jsp" method="post" id="from">
	<input type="hidden" id="json" name="json" />
	<%
		
	%>
</form>
</div>
<script language="JavaScript">

	var map = {};
	//[ "cnortitle","cneetitle","notifytitle","poa","pol" ,"vessel","voyage","vesselvoyage","pretrans","pdd","pod","destination","plable","hblsono","hblno","mblno","cntinfos","goodsinfo","piece","packer","markhead","cntdesc","grswgt","cbm","atdtitle","atd","totledesc","freightitemdesc","billcount","signplace","agentitle","etd","carrierfullname","carriername","dono","piecepacker","remark","dotype","carryitem","freightitemdesc","loaditem","usernamec","usertel","userfax","corpnamec","corpnamee","corpaddressc","now","cnortitlembl","cneetitlembl","notifytitlembl","bkagent","bkagentcontact","marksnombl","goodsdescmbl"]
	map["cnortitlembl"] = "发货人";
	map["cneetitlembl"] = "收货人";
	map["notifytitlembl"] = "通知人";
	map["agentitlembl"] = "MBL代理";
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
	map["mblpol"] = "装港MBL";
	map["mblpod"] = "目的港MBL";
	map["mblpdd"] = "卸货港MBL";
	map["pdd"] = "卸港";
	map["destination"] = "目的地";
	map["mbldestination"] = "目的地MBL";
	map["loaditem"] = "装船条款";
	map["totledesc"] = "合计箱数";
	map["goodsnamee"] = "货品名称";
	map["goodsnamee2"] = "货品名称勾选";
	map["freightitempp"] = "运费条款PP";
	map["freightitemcc"] = "运费条款CC";
	map["freightitemdesc"] = "运费条款";
	map["payplace"] = "付款地点";
	map["payplacepp"] = "付款地点PP";
	map["payplacecc"] = "付款地点CC";
	map["grswgt"] = "毛重";
	map["cbm"] = "体积";
	map["mblsono"] = "SO号码";
	map["mblno"] = "MBL提单号";
	map["cntinfos"] = "唛头";
	map["goodsinfo"] = "物品种类与货名";
	map["atd"] = "实开船日";
	map["markhead"] = "唛头";
	map["cntdesc"] = "件数包装货名";
	map["billcount"] = "提单份数";
	map["signplace"] = "签单地点";
	map["agentitle"] = "HBL代理";
	map["etd"] = "ETD";
	map["carrierfullname"] = "运输公司全称";
	map["dono"] = "DO";
	map["piecepacker"] = "件数包装";
	map["remark"] = "备注";
	map["bkagent"] = "订舱代理全称";
	map["bkagentcontact"] = "订舱代理联系人";
	map["usernamee"] = "用户英文名";
	map["tel1"] = "用户电话1";
	map["tel2"] = "用户电话2";
	map["email1"] = "用户email1";
	map["email2"] = "用户email2";
	map["fax"] = "用户fax";
	map["paymentitem"] = "付款条款";
	map["jobno"] = "工作单号";
	map["freightitemmbl"] = "运费条款MBL";
	map["remark_booking"] = "订舱备注";
	map["cntdesc"] = "件数包装货名";
	map["telrelnos"] = "电放号";
	map["telreler"] = "电放人";
	
	

	function saveTemplete(json){
		//console.log("success");
		$.post("/scp/service?src=rogtemplete&action=saveRogtempletetrainmbl&userid="+<%=userid%>+"&rp="+"<%=rp%>"+"&reporttype="+"<%=reporttype%>", { "json": json },
		function(result){
			alert(result);
			//if(result.toUpperCase() == "SUCCESS"){
				location.href = location.origin + '/scp/reportEdit/file/printtrainmbl.jsp' + location.search;
			//}
		}
		);
	}
	function printTemplete(json){
		var href = './printtrainmbl.jsp' + location.search;
		var from = document.getElementById("from");
		var hi = document.getElementById("json");
		hi.value = json;
		from.action = href;
		from.submit();
	}
	function saveTemplateForPrivate(json,text,ispublic){
		var rp = "<%=rp%>";
		var url = location.search.substring(location.search.indexOf(rp)+rp.length);
		$.post("/scp/service?src=rogtemplete&action=checktempletenametrainmbl&reporttype=<%=reporttype%>&rp="+"<%=rp%>"+url+"&ispublic="+ispublic, { "json": json,"text":text },
		function(result){
			var obj = JSON.parse(result);
			if(obj.type=="success"){
				var surl = location.search;
				surl = "?rp="+ obj.value + ".raq"  + surl.substring(surl.indexOf(".raq&")+4)
				location.href = location.origin + '/scp/reportEdit/file/printtrainmbl.jsp' + surl;
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
    },500);	
</script> 
</body>
</html>