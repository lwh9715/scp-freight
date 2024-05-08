var linenum = [];   // 行号
function reSelectAfterShowWindows() { 
	var recs=editGridJsvar.getSelectionModel().getSelections(); // 把所有选中项放入数组
    linenum.length = 0;               
    for(var i=0;i<recs.length;i++){ 
		linenum[i]=editGridJsvar.getStore().indexOfId(recs[i].get("id"));   //当前每行的ID获得当前行号
    	//console.log(linenum); 
    } 
	//editGridJsvar.getSelectionModel().selectRows(linenum);
}  

function exportExcel() {
	var config = editGridJsvar.getSelectionModel().grid.events.columnmove.obj.plugins[0].grid.colModel.config;
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

function formateValid(v,m,r){
	var isvalid = r.get('isvalid');
	if(isvalid =='R'){
		m.attr="style='color: GREEN;'";
	}else if(isvalid =='A'){
		m.attr="style='color: BLUE;'";
	}else {
		m.attr="style='color: GRAY;'";
	}	
    return v;
}  



function formateReleaseColor(v,m,r){
	var isrelease = r.get('isrelease');
	if(isrelease =='false'){
		m.attr="style='color: red;'";
	}else {
		m.attr="style='color: GREEN;'";
	}	
    return v;
}  

function showEdit(pkid) {
	linkEdit.addParam("pkid",pkid);
	linkEdit.submit();
}

function resize(){
	var newHeight = (gridPanelJsvar.getSize().height) - 80;
	var newWidth = (gridPanelJsvar.getSize().width);
    editGridJsvar.setHeight(newHeight);
    editGridJsvar.setWidth(newWidth);
	editGridJsvar.render();
}

//第四个柜型，如果没有选，移开 400/550/550/1000 时，自动赋值 最后一个柜型为45HQ，如果有选，则不变动所选柜型
function setcostvluses(){
	var costvluses = costvlusesJsvar.getValue();
	if(costvluses!=''){
		var strs= new Array();
		strs = costvluses.split("/");	
		cost20Jsvar.setValue(strs[0]==undefined?cost20Jsvar.getValue():strs[0]);
		cost40gpJsvar.setValue(strs[1]==undefined?cost40gpJsvar.getValue():strs[1]);
		cost40hqJsvar.setValue(strs[2]==undefined?cost40hqJsvar.getValue():strs[2]);
		cost45hqJsvar.setValue(strs[2]==undefined?cost45hqJsvar.getValue():strs[3]);
	}
}

function setcost(){
	var strs= new Array();
	var str = '';
	strs[0] = cost20Jsvar.getValue();
	strs[1] = cost40gpJsvar.getValue();
	strs[2] = cost40hqJsvar.getValue();
	strs[3] = cost45hqJsvar.getValue();
	for (i=0;i<strs.length ;i++ ) {
		i==3?(str += strs[i]):(str += strs[i]+"/");
	} 
	costvlusesJsvar.setValue(str);
}

function updatecostvluses1(){
	var costvluses = costupdate1Jsvar.getValue();
	if(costvluses!=''){
		var strs= new Array();
		strs = costvluses.split("/");	
		z20gpJsvar.setValue(strs[0]==undefined?z20gpJsvar.getValue():strs[0]);
		z40gpJsvar.setValue(strs[1]==undefined?z40gpJsvar.getValue():strs[1]);
		z40hqJsvar.setValue(strs[2]==undefined?z40hqJsvar.getValue():strs[2]);
		z45hqJsvar.setValue(strs[2]==undefined?z45hqJsvar.getValue():strs[3]);
		zcntypeotherJsvar.setValue(strs[2]==undefined?z45hqJsvar.getValue():strs[4]);
	}
}

function updatecostvluses2(){
	var costvluses = costupdate2Jsvar.getValue();
	if(costvluses!=''){
		var strs= new Array();
		strs = costvluses.split("/");	
		z20gpvJsvar.setValue(strs[0]==undefined?z20gpvJsvar.getValue():strs[0]);
		z40gpvJsvar.setValue(strs[1]==undefined?z40gpvJsvar.getValue():strs[1]);
		z40hqvJsvar.setValue(strs[2]==undefined?z40hqvJsvar.getValue():strs[2]);
		z45hqvJsvar.setValue(strs[2]==undefined?z45hqvJsvar.getValue():strs[3]);
		zcntypeothervJsvar.setValue(strs[2]==undefined?z45hqvJsvar.getValue():strs[4]);
	}
}
function updatecostvluses3(){
	var costvluses = baseupdate1Jsvar.getValue();
	if(costvluses!=''){
		var strs= new Array();
		strs = costvluses.split("/");	
		b20gpJsvar.setValue(strs[0]==undefined?z20gpJsvar.getValue():strs[0]);
		b40gpJsvar.setValue(strs[1]==undefined?z40gpJsvar.getValue():strs[1]);
		b40hqJsvar.setValue(strs[2]==undefined?z40hqJsvar.getValue():strs[2]);
		b45hqJsvar.setValue(strs[2]==undefined?z45hqJsvar.getValue():strs[3]);
		bcntypeotherJsvar.setValue(strs[2]==undefined?z45hqJsvar.getValue():strs[4]);
	}
}

function updatecostvluses4(){
	var costvluses = baseupdate2Jsvar.getValue();
	if(costvluses!=''){
		var strs= new Array();
		strs = costvluses.split("/");	
		b20gpvJsvar.setValue(strs[0]==undefined?z20gpvJsvar.getValue():strs[0]);
		b40gpvJsvar.setValue(strs[1]==undefined?z40gpvJsvar.getValue():strs[1]);
		b40hqvJsvar.setValue(strs[2]==undefined?z40hqvJsvar.getValue():strs[2]);
		b45hqvJsvar.setValue(strs[2]==undefined?z45hqvJsvar.getValue():strs[3]);
		bcntypeothervJsvar.setValue(strs[2]==undefined?z45hqvJsvar.getValue():strs[4]);
	}
}

function setcost1(){
	var strs= new Array();
	var str = '';
	strs[0] = z20gpJsvar.getValue();
	strs[1] = z40gpJsvar.getValue();
	strs[2] = z40hqJsvar.getValue();
	strs[3] = z45hqJsvar.getValue();
	strs[4] = zcntypeotherJsvar.getValue();
	for (i=0;i<strs.length ;i++ ) {
		i==4?(str += strs[i]):(str += strs[i]+"/");
	} 
	costupdate1Jsvar.setValue(str);
}

function setcost2(){
	var strs= new Array();
	var str = '';
	strs[0] = z20gpvJsvar.getValue();
	strs[1] = z40gpvJsvar.getValue();
	strs[2] = z40hqvJsvar.getValue();
	strs[3] = z45hqvJsvar.getValue();
	strs[4] = zcntypeothervJsvar.getValue();
	for (i=0;i<strs.length ;i++ ) {
		i==4?(str += strs[i]):(str += strs[i]+"/");
	} 
	costupdate2Jsvar.setValue(str);
}

function setcost3(){
	var strs= new Array();
	var str = '';
	strs[0] = b20gpJsvar.getValue();
	strs[1] = b40gpJsvar.getValue();
	strs[2] = b40hqJsvar.getValue();
	strs[3] = b45hqJsvar.getValue();
	strs[4] = bcntypeotherJsvar.getValue();
	for (i=0;i<strs.length ;i++ ) {
		i==4?(str += strs[i]):(str += strs[i]+"/");
	} 
	baseupdate1Jsvar.setValue(str);
}

function setcost4(){
	var strs= new Array();
	var str = '';
	strs[0] = b20gpvJsvar.getValue();
	strs[1] = b40gpvJsvar.getValue();
	strs[2] = b40hqvJsvar.getValue();
	strs[3] = b45hqvJsvar.getValue();
	strs[4] = bcntypeothervJsvar.getValue();
	for (i=0;i<strs.length ;i++ ) {
		i==4?(str += strs[i]):(str += strs[i]+"/");
	} 
	baseupdate2Jsvar.setValue(str);
}

function columnWindowFunction(){
		showALLColumn(editGridJsvar,'pages.module.price.mgrfclBean.editGrid');
		columnWindow.show();
		$('#selectTitle').initList();
		$('.center-box').find('.selected-val').click(function(){
			showALLColumna(editGridJsvar,'pages.module.price.mgrfclBean.editGrid');
			columnRefresh.submit();
			columnWindow.hide();
		});
		$( "#sortable" ).sortable();//启用jQuery鼠标拖拽
    	$( "#sortable" ).disableSelection();
	}

function setNosislock(nosislock){
	$("#nosislock").html(nosislock);
}

function clear(){
	editGridJsvar.getSelectionModel().clearSelections();
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

function clearvesvel(value) {
   	clearSubmit.addParam("value",value);
	clearSubmit.submit();
}

function showbox(){
	var type = typestartJs.getValue();
	if(type =='BCETD'){
		bargeonboardJs.show();
		onboardJs.hide();
	}
	if(type =='ETD'){
		onboardJs.show();
		bargeonboardJs.hide();
	}
	if(type !='BCETD' && type !='ETD'){
		onboardJs.hide();
		bargeonboardJs.hide();
	}
}

function showbox2(){
	var type = typeendJs.getValue();
	if(type =='BCETD'){
		bargeonboard2Js.show();
		onboard2Js.hide();
	}
	if(type =='ETD'){
		onboard2Js.show();
		bargeonboard2Js.hide();
	}
	if(type !='BCETD' && type !='ETD'){
		onboard2Js.hide();
		bargeonboard2Js.hide();
	}
}

 function showstart(){
	var type = batchTypestartJs.getValue();
	if(type =='BCETD'){
		startbaronJs.show();
		startonJs.hide();
		bartypestart2Js.show();
		typestart2Js.hide();
	}
	if(type =='ETD'){
		startbaronJs.hide();
		startonJs.show();
		typestart2Js.show();
		bartypestart2Js.hide();
	}
	if(type !='BCETD' && type !='ETD'){
		startbaronJs.hide();
		startonJs.hide();
		bartypestart2Js.show();
		typestart2Js.hide();
	}
}

function showend(){
	var type = batchTypeendJs.getValue();
	if(type =='BCETD'){
		endbaronJs.show();
		endonJs.hide();
		bartypeend2Js.show();
		typeend2Js.hide();
	}
	if(type =='ETD'){
		endbaronJs.hide();
		endonJs.show();
		typeend2Js.show();
		bartypeend2Js.hide();
	}
	if(type !='BCETD' && type !='ETD'){
		endbaronJs.hide();
		endonJs.hide();
		bartypeend2Js.show();
		typeend2Js.hide();
	}
}

function showbox3(){
	var type = bargetypeJs.getValue();
	if(type =='ONBOARD'){
		bargetypestr2Js.show();
	}
	if(type !='ONBOARD'){
		bargetypestr2Js.hide();
	}
}

function showbox4(){
	var type = bargetypendJs.getValue();
	if(type =='ONBOARD'){
		bargetypend2Js.show();
		baronendJs.hide();
	}
	if(type =='ETD'){
		bargetypend2Js.hide();
		baronendJs.show();
	}
	if(type !='ONBOARD' && type !='ETD'){
		bargetypend2Js.hide();
		baronendJs.hide();
	}
}


function showstart2(){
	var type = batchbargetypeJs.getValue();
	if(type =='ONBOARD'){
		barstartbaronJs.show();
		bargetypestr2JsVar.show();
	}
	if(type !='ONBOARD'){
		barstartbaronJs.hide();
		bargetypestr2JsVar.hide();
	}
}

function showend2(){
	var type = batchbargetypendJs.getValue();
	if(type =='ONBOARD'){
		barendbaronJs.show();
		barendonJs.hide();
		bargetypend2JsVar.show();
		baronendJsVar.hide();
	}
	if(type =='ETD'){
		barendbaronJs.hide();
		barendonJs.show();
		bargetypend2JsVar.hide();
		baronendJsVar.show();
	}
	if(type !='ONBOARD' && type !='ETD'){
		barendbaronJs.hide();
		barendonJs.hide();
		bargetypend2JsVar.show();
		baronendJsVar.hide();
	}
}

var flag = '+';
function batchChangeCom(){
	if(flag == '+'){
		flag = '-';
	}else{
		flag = '+';
	}
    c20gpJsVar.setValue(flag);
    c40gpJsVar.setValue(flag);
    c40hqJsVar.setValue(flag);
    c45hqJsVar.setValue(flag);
    cotherJsVar.setValue(flag);
}

function formatTotaljobs(v,m,r) {
   	var id = r.get('id');
   	var jobscount = r.get('totaljobs');
   	if(jobscount != "0"){
   		var temp = '<a href="/scp/pages/module/price/showjobs.xhtml?priceid='+id+'" onclick="showJobsWindow.show()" target="jobsIframe">'+jobscount+'</a>';
    	return temp;
   	}else{
   		return '';
   	}
}