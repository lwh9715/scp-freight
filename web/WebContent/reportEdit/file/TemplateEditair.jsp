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
</head>
<%  
	String rp = request.getParameter("rp"); 
	String billid = request.getParameter("b"); 
	String userid = request.getParameter("u");
	String reporttype = request.getParameter("reporttype");
	if(!rp.isEmpty()){
		%>
		<script language="JavaScript">
			$.get("/scp/service?src=rogtemplete&action=toRogtempleteair&userid="+<%=userid%>+"&rp="+"<%=rp%>&reporttype=<%=reporttype%>",
			  function(data){
			  	//console.log(typeof(data));
			  	if(data=="''"||data==null){
			  		var template=new TemplateEdit({
						jdatasource:{ "json": { "HAWB": [ "carriercode","customerabbr","cnortitle","notifytitle","cneetitle","pol","pod","hawbno","mawbno","ppccgoods","piece","weight","unit","rateclass","markno","chargeweight","volume","volumeweight","charge","amount","goodsdesc","printtime","usertel1","usertel2","useremail1","useremail2","userfax","usernamec","usernamee","corpaddressc","corpaddresse","corpnamec","corpnamee","airfreightpp","airfreightcc","valueaddpp","valueaddpp","insurancepp","insurancecc","taxfeepp","taxfeecc","agentothfeepp","agentothfeecc","carrothfeepp","carrothfeecc","otherfeepp","otherfeecc","feesumpp","feesumcc","polcyid","transamt","podcyid","podxrate","podfee","ccsumto","sumto","insuranceamt","customeamt","flightno1","flightno2","flightdate1","flightdate2","agentdesabbr","ppccothfeepp","ppccothfeecc","ppccpaytypepp","ppccpaytypecc","to1","by1","to2","by2","to3","by3","polcode","podcode","other1","other2","other3","mawbno1","mawbno2","mblcnortitle","mblcneetitle","mblnotifytitle","notify2title","remarks","flightdate11","flightno11","mawbno11","mawbno12"]}},
						jTemplates:[]
					});
			  	}else{
				    var template=new TemplateEdit({
						jdatasource:{ "json": { "HAWB": [ "carriercode","customerabbr","cnortitle","notifytitle","cneetitle","pol","pod","hawbno","mawbno","ppccgoods","piece","weight","unit","rateclass","markno","chargeweight","volume","volumeweight","charge","amount","goodsdesc","printtime","usertel1","usertel2","useremail1","useremail2","userfax","usernamec","usernamee","corpaddressc","corpaddresse","corpnamec","corpnamee","airfreightpp","airfreightcc","valueaddpp","valueaddpp","insurancepp","insurancecc","taxfeepp","taxfeecc","agentothfeepp","agentothfeecc","carrothfeepp","carrothfeecc","otherfeepp","otherfeecc","feesumpp","feesumcc","polcyid","transamt","podcyid","podxrate","podfee","ccsumto","sumto","insuranceamt","customeamt","flightno1","flightno2","flightdate1","flightdate2","agentdesabbr","ppccothfeepp","ppccothfeecc","ppccpaytypepp","ppccpaytypecc","to1","by1","to2","by2","to3","by3","polcode","podcode","other1","other2","other3","mawbno1","mawbno2","mblcnortitle","mblcneetitle","mblnotifytitle","notify2title","remarks","flightdate11","flightno11","mawbno11","mawbno12"]}},
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
				$("#collapseOne1").css({"height":"700px","overflow":"auto","min-width":"120px"});
				$('.panel-body li').css('padding','0');
			  });
			  
			 
		</script>
		<%
	}
%>
<body style="overflow: auto;">
<div style="display: none;">
<form action="./printair.jsp" method="post" id="from">
	<input type="hidden" id="json" name="json" />
</form>
</div>
<script language="JavaScript">
	var map = {};
	
	
	map['carriercode']='航空代码';
	map['volume']='体积';
	map['volumeweight']='体积重量';
	
	map['airfreightpp']='航空运费pp';
	map['airfreightcc']='航空运费cc';
	map['valueaddpp']='声明值价pp';
	map['valueaddcc']='声明价值cc';
	map['insurancepp']='保险税pp';
	map['insurancecc']='保险税cc';
	map['taxfeepp']='税款pp';
	map['taxfeecc']='税款cc';
	map['agentothfeepp']='交代理人费';
	map['agentothfeecc']='交代理人费';
	map['carrothfeepp']='交承运人费';
	map['carrothfeecc']='交承运人费';
	map['otherfeepp']='其他费用pp';
	map['otherfeecc']='其他费用cc';
	map['feesumpp']='合计pp';
	map['feesumcc']='合计cc';
	map['polcyid']='起运港(国)币别';
	map['transamt']='运输金额';
	map['podcyid']='目的港(国)币制';
	map['podxrate']='目的港(国)汇率';
	map['podfee']='目的港费用';
	map['ccsumto']='到付合计折合';
	map['sumto']='合计金额';
	map['insuranceamt']='保险金额';
	map['customeamt']='报关金额';
	map['flightno1']='航班1';
	map['flightno2']='航班2';
	map['flightdate1']='航班时间1';
	map['flightdate2']='航班时间2';
	map['agentdesabbr']='目的港代理';
	
	map['ppccothfeepp']='其他费用PP';
	map['ppccpaytypepp']='付款方式PP';
	map['ppccothfeecc']='其他费用CC';
	map['ppccpaytypecc']='付款方式CC';
	
	//map['otherfeepp']='其他费用PP金额';
	//map['otherfeecc']='其他费用CC金额';
	
	map['customerabbr']='客户简称';
	map['cnortitle']='发货人';
	map['notifytitle']='通知人';
	map['cneetitle']='收货人';
	map['pol']='起飞地点';
	map['pod']='降落地点';
	map['polcode']='起飞地点代码';
	map['podcode']='降落地点代码';
	map['hawbno']='HAWB NO';
	map['mawbno']='MAWB NO';
	map['piece']='件数';
	map['ppccgoods']='货物运费';
	map['weight']='毛重';
	map['unit']='单位';
	map['rateclass']='运价类';
	map['markno']='标记与号码';
	map['chargeweight']='计费重';
	map['charge']='运价';
	map['amount']='运费';
	map['goodsdesc']='货品名称及描述';
	map['printtime']='当前时间';
	
	
	map['usertel1']='用户电话1';
	map['usertel2']='用户电话2';
	map['useremail1']='用户邮箱1';
	map['useremail2']='用户邮箱2';
	map['userfax']='用户fax';
	map['usernamec']='用户中文名';
	map['usernamee']='用户英文名';
	map['corpaddressc']='公司地址中文';
	map['corpaddresse']='公司地址英文';
	map['corpnamec']='公司中文名';
	map['corpnamee']='公司英文名';
	map['mblcnortitle']='MBL发货人';
	map['mblcneetitle']='MBL收货人';
	map['mblnotifytitle']='MBL通知人';
	map['notify2title']='第二通知人';
	map['remarks']='工作单备注';
	
	
	function saveTemplete(json){
		//console.log("success");
		$.post("/scp/service?src=rogtemplete&action=saveRogtempleteair&reporttype=<%=reporttype%>&rp="+"<%=rp%>", { "json": json },
		function(result){
			alert(result);
			//if(result.toUpperCase() == "SUCCESS"){
				location.href = location.origin + '/scp/reportEdit/file/printair.jsp' + location.search;
			//}
		}
		);
	}
	function printTemplete(json){
		var href = './printair.jsp' + location.search;
		var from = document.getElementById("from");
		var hi = document.getElementById("json");
		hi.value = json;
		from.action = href;
		from.submit();
	}
	function saveTemplateForPrivate(json,text,ispublic){
		var rp = "<%=rp%>";
		var url = location.search.substring(location.search.indexOf(rp)+rp.length);
		$.post("/scp/service?src=rogtemplete&action=checktempletenameair&reporttype=<%=reporttype%>&rp="+"<%=rp%>"+url+"&ispublic="+ispublic, { "json": json,"text":text },
		function(result){
			var obj = JSON.parse(result);
			if(obj.type=="success"){
				var surl = location.search;
				surl = "?rp="+ obj.value + ".raq"  + surl.substring(surl.indexOf(".raq&")+4)
				location.href = location.origin + '/scp/reportEdit/file/printair.jsp' + surl;
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
</script> 
</body>
</html>