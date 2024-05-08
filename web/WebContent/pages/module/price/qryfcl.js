function formatLinkCharts(v,m,r){
	var pol = r.get('pol');
	var pod = r.get('pod');
	var shipping = r.get('shipping').trim();
	shipping = shipping.split(/[\r\n]/g)[0];
	shipping = shipping.replace(/[\r\n]/g, ""); //去掉回车换行
	var dateto = r.get('dateto');
	return '<a style="color:#00F;" onclick="showCharts('+"'"+pol+"','"+pod+"','"+shipping+"','"+dateto+"'"+');">SHOW</a>';
}

function showCharts(pol,pod,shipping,dateto){
	showChartsWindow.addParam("pol",pol);
	showChartsWindow.addParam("pod",pod);
	showChartsWindow.addParam("shipping",shipping);
	showChartsWindow.addParam("dateto",dateto);
	showChartsWindow.submit();
}


function showEdit(pkid) {
	linkEdit.addParam("pkid",pkid);
	linkEdit.submit();
}

function exportExcel() {
	var config = gridJsvar.getSelectionModel().grid.events.columnmove.obj.plugins[0].grid.colModel.config;
	var key = "";
	var value = "";
	for(var i=1;i<config.length;i++) {
		if(!config[i].hidden) {
			key += config[i].dataIndex;
    		value += config[i].header;
    		if(i < config.length - 1) {
    			key += ",";
    			value += ",";
    		}
		}
	}
	exportexl.addParam("key",key);
	exportexl.addParam("value",value);
	exportexl.submit();
}        

function resize(){
	var newHeight = (gridPanelJsvar.getSize().height) - 106;
	var newWidth = (gridPanelJsvar.getSize().width);
    gridJsvar.setHeight(newHeight);
    gridJsvar.setWidth(newWidth);
	gridJsvar.render();
}

function hidecolumn(){
	var cms = gridJsvar.getColumnModel();
	var index1 = cms.findColumnIndex("barcost20");
	var index2 = cms.findColumnIndex("barcost40gp");
	var index3 = cms.findColumnIndex("barcost40hq");
	var index4 = cms.findColumnIndex("barremark");
	//cms.setHidden(index1,true);
	//cms.setHidden(index2,true);
	//cms.setHidden(index3,true);
	cms.setHidden(index4,true);
}

function showcolumn(){
	var cms = gridJsvar.getColumnModel();
	var index1 = cms.findColumnIndex("barcost20");
	var index2 = cms.findColumnIndex("barcost40gp");
	var index3 = cms.findColumnIndex("barcost40hq");
	var index4 = cms.findColumnIndex("barremark");
	//cms.setHidden(index1,false);
	//cms.setHidden(index2,false);
	//cms.setHidden(index3,false);
	cms.setHidden(index4,false);
}

function columnWindowFunction(){
	showALLColumn(gridJsvar,'pages.module.price.qryfclBean.grid');
	columnWindow.show();
	$('#selectTitle').initList();
	$('.center-box').find('.selected-val').click(function(){
		showALLColumna(gridJsvar,'pages.module.price.qryfclBean.grid');
		columnRefresh.submit();
		columnWindow.hide();
	});
	$( "#sortable" ).sortable();//启用jQuery鼠标拖拽
	$( "#sortable" ).disableSelection();
}

function setstyle(v,m,r) {
	var temp = "";
	if(v==0||v==''||v==null){
		temp = '--'
	}else{
		temp = v;
	}
	m.attr = "style = 'color:#DF4A4D;font-weight:bolder;font-size: 14px;'";
	return "<span name='prices'>"+temp+"</span>";
}

function setbargepol(){
	setbargepolajax.addParam("pol",polJsvar.getValue());
	setbargepolajax.submit();
}

function setBargeClolr(){
	$('span[name="prices"]').each(function (index,domEle){
		if(domEle != undefined){
			var array = domEle.innerHTML.split("<br>");
			
			if(array != undefined && array.length==2){
				var newStr = array[0] + "<br>" + "<span style='color:BLUE'>" + array[1] + "</span>";
				domEle.innerHTML=newStr;
			}
			if(array != undefined && array.length==3){
				var newStr = array[0] + "<br>" + "<span style='color:BLUE'>" + array[1] + "</span>" 
				+ "<br>" + "<span style='color:GREEN'>" + array[2] + "</span>";
				domEle.innerHTML=newStr;
			}
			if(array != undefined && array.length==4){
				var newStr = array[0] + "<br>" + "<span style='color:BLUE'>" + array[1] + "</span>" 
				+ "<br>" + "<span style='color:GREEN'>" + array[2] + "</span>"
				+ "<br>" + "<span style='color:GRAY'>" + array[3] + "</span>";
				domEle.innerHTML=newStr;
			}
			if(array != undefined && array.length==5){
				var newStr = array[0] + "<br>" + "<span style='color:BLUE'>" + array[1] + "</span>" 
				+ "<br>" + "<span style='color:GREEN'>" + array[2] + "</span>"
				+ "<br>" + "<span style='color:GRAY'>" + array[3] + "</span>"
				+ "<br>" + "<span style='color:GRAY'>" + array[4] + "</span>";
				domEle.innerHTML=newStr;
			}
			if(array != undefined && array.length==6){
				var newStr = array[0] + "<br>" + "<span style='color:BLUE'>" + array[1] + "</span>" 
				+ "<br>" + "<span style='color:GREEN'>" + array[2] + "</span>"
				+ "<br>" + "<span style='color:GRAY'>" + array[3] + "</span>"
				+ "<br>" + "<span style='color:GRAY'>" + array[4] + "</span>"
				+ "<br>" + "<span style='color:GRAY'>" + array[5] + "</span>";
				domEle.innerHTML=newStr;
			}
		}
	});
}

function formatRemark(v,m,r) {
  	var temp = "";
  	if(v==null)return "";
  	var f = "showRemark('"+v+"')";
	temp = '<a target="" onclick="'+f+'" href="javascript:void(0);"><font color="blue">' + v + '</font></a>';
    return temp;
}

function showRemark(v){
	remarkWindow.show();
	$("#tipsTextArea").css("display","block");
	tipsTextArea.setValue(v);
}

function sortAction(ct, column, direction, eOpts) { 
	var argss = "";	
	var arg1 =column.field;
	var arg2 = column.direction;
	argss += arg1 +" "+ arg2 +",";
	if(column.field == 'pricedesc'){//点运价才去后台重新排序
		sortGridRemoteJsVar.addParam("desctype",column.direction);
	}else{
		sortGridRemoteJsVar.addParam("desctype","");
		sortGridRemoteJsVar.addParam("argss",argss.substring(0,argss.length-1));
	}
	sortGridRemoteJsVar.submit();
}


function formateValid(v,m,r){
	var isvalid = r.get('isvalid');
	if(isvalid =='R'){
		m.attr="style='color: blue;'";
	}else {
		m.attr="style='color: GRAY;'";
	}	
    return v;
}  

function checkBefore(){
	var polValue = polJsvar.getValue();
	if(polValue == undefined || polValue == ''){
		alert('POL is NULL!');
		return false;
	}
	var podValue = podJsvar.getValue();
	if(podValue == undefined || podValue == ''){
		alert('POD is NULL!');
		return false;
	}
	return true;
}

function opennewwinf(){
	var hraf = window.location.href
	var newTab=window.open('about:blank');
	newTab.location.href=hraf;
}

function exportExcel() {
	var config = gridJsvar.getSelectionModel().grid.events.columnmove.obj.plugins[0].grid.colModel.config;
	var key = "";
	var value = "";
	for(var i=1;i<config.length;i++) {
		if(!config[i].hidden) {
			key += config[i].dataIndex;
    		value += config[i].header;
    		if(i < config.length - 1) {
    			key += ",";
    			value += ",";
    		}
		}
	}
	exportexl.addParam("key",key);
	exportexl.addParam("value",value);
	exportexl.submit();
}

function init(){
	gridJsvar.store.on("load",function(store) { 
		setBargeClolr();
	},gridJsvar); 
	
	if(actionJsTextJsVar.getValue() != null ){
		eval(actionJsTextJsVar.getValue());
	}
	
	$('form input').attr("autocomplete","off");
	
}

function initFlexBox(customerabbr , pol , pod, userid){
	$('#customer').flexbox('/scp/service?src=flexbox&action=customer&userid='+userid+'&type=ship', {
		resultTemplate: '<div class="col1">{name}</div><div style="width: 225px;" class="col2">{namec}</div><div class="col3">{namee}</div>',
		watermark: '',
		initialValue: customerabbr,
		width: 155,
		onSelect: function() {
			var id = $('input[name=customer]').val();
			customeridJsVar.setValue(id);
		}
	});
    
    $('#fclpol').flexbox('/scp/service?src=flexbox&action=fclpol', {
		resultTemplate: '<div class="col1">{name}</div><div class="col2">{namee}</div><div class="col3">{namec}</div>',
		watermark: '',
		initialValue: pol,
		width: 120,
		onSelect: function() {
			var str1 = $(this).next().next().children().children(".row.ffb-sel").children(".col2").html();
			str1 = str1.replace('<span class="ffb-match">',"");
			str1 = str1.replace("</span>","");
			$(this).val(str1);
			polJsvar.setValue(str1);
		}
	});
	
	$('#fclpod').flexbox('/scp/service?src=flexbox&action=fclpod', {
		resultTemplate: '<div class="col1">{name}</div><div class="col2">{namee}</div><div class="col3">{namec}</div>',
		watermark: '',
		initialValue: pod,
		width: 120,
		onSelect: function() {
			var str1 = $(this).next().next().children().children(".row.ffb-sel").children(".col2").html();
			str1 = str1.replace('<span class="ffb-match">',"");
			str1 = str1.replace("</span>","");
			$(this).val(str1);
			podJsvar.setValue(str1);
		}
	});
	
	$('#fclpol_input').keydown(function(e) {//回车事件
		if (e.keyCode == 13) {
  			//$('#j_id35\\:qryRefresh button').click();
			qryRefreshJs.fireEvent('click');
		}
	});
	$('#fclpod_input').keydown(function(e) {
		if (e.keyCode == 13) {
  			//$('#j_id35\\:qryRefresh button').click();
  			qryRefreshJs.fireEvent('click');
		}
	});
}