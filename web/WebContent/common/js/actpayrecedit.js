//初始化json数据，类似单例
var rpsumArray = new Array();

//新增处理化
function addInit() {
	rpsumArray = new Array();
	isRpDateCheckJsVar.setValue(false);
}

function formatcode(v,m,r) {
  	var isdeletes = r.get('isdelete');
	if(isdeletes == 'true') {
		m.attr="style='text-decoration: line-through; color: #ff0000; font-size: 9pt;'";
	}else{
		
	}
    return v;
}

function formatShow(v,m,r) {
	var isdeletes2 = r.get('isdelete');
	if(isdeletes2 == 'false') {
    	v="打开";
       var temp = '<a href="#" onclick="showVch('+r.get('id')+');">' + v + '</a>';
    	m.attr="style='color:blue;text-decoration: underline;'";
     }else{
     	var temp = "已删除";
     	m.attr="style='text-decoration: line-through; color: #ff0000; font-size: 9pt;'";
     }
      return temp;
}

function format2absmoney(v, m, r) {
	var money = v ? fabsmoney(v, 2) : '0';
	// alert(money);
	var araptype = r.get('araptype');
	if (araptype == 'AP') {
		m.attr = "style='color: red;'";
	} else if (araptype == 'AR') {
		m.attr = "style='color: GREEN;'";
	}
	return money;
}

function fabsmoney(s, n) {
	//s = s.replace("-", "");
	n = n > 0 && n <= 20 ? n : 2;
	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
	var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
	t = "";
	for ( i = 0; i < l.length; i++) {
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	}
	return t.split("").reverse().join("") + "." + r;
}

function formatamountwf(v, m, r) {
	var money = v ? v : 0;
	// alert(money);
	var araptype = r.get('araptype');
	if (araptype == 'AP') {
		m.attr = "style='color: red;background-color:#9ACDD6;'";
	} else if (araptype == 'AR') {
		m.attr = "style='color: GREEN;background-color:#9ACDD6;'";
	}
	return money;
}

function formatrpsum(v, m, r) {
	//var money = v ? v : 0;
	// alert(money);
	var araptype = r.get('rp');
	if (araptype == 'P') {
		m.attr = "style='color: red;background-color:#9ACDD6;'";
	} else if (araptype == 'R') {
		m.attr = "style='color: GREEN;background-color:#9ACDD6;'";
	} else{
		m.attr = "style='color: GREEN;background-color:#9ACDD6;'";
	}
	return v;
}

function formatrptype(v, m, r) {
	var araptype = r.get('rp');
	if (araptype == 'P') {
		m.attr = "style='color: red;background-color:#9ACDD6;'";
	} else if (araptype == 'R') {
		m.attr = "style='color: GREEN;background-color:#9ACDD6;'";
	} else{
		m.attr = "style='color: GREEN;background-color:#9ACDD6;'";
	}
	return v;
}

function formatexrates(v, m, r) {
	var money = v ? fabsmoney(v, 6) : '0';
	// alert(money);
	var araptype = r.get('araptype');
	if (araptype == 'AP') {
		m.attr = "style='color: red;background-color:#9ACDD6;'";
	} else if (araptype == 'AR') {
		m.attr = "style='color: GREEN;background-color:#9ACDD6;'";
	}
	return money;
}

function formateArAp(v, m, r) {
	var araptype = r.get('araptype');
	if (araptype == 'AP') {
		m.attr = "style='color: red;'";
	} else if (araptype == 'AR') {
		m.attr = "style='color: GREEN;'";
	}
	return v;
}
function formateArAps(v, m, r){
	var araptype = r.get('araptype');
	var jobids = r.get('jobid');
	var temp ;
	if (araptype == 'AP') {
		temp = '<a target="_blank" href="http://120.25.241.190:8888/scp/reportJsp/showReport.jsp?raq=/bus/feeprofit-en.raq&jobid='+jobids+'&userid='+useridjs.getValue()+'"><font color="red">' + araptype + '</font></a>';
	} else if (araptype == 'AR') {
		temp = '<a target="_blank" href="http://120.25.241.190:8888/scp/reportJsp/showReport.jsp?raq=/bus/feeprofit-en.raq&jobid='+jobids+'&userid='+useridjs.getValue()+'"><font color="GREEN">' + araptype + '</font></a>';	
	}
	return temp;
}

//按金额,兑换率,折算符自动计算折合金额
function countByRate(amt, rate, xtype) {
	//alert(amt);
	//alert(rate);
	//alert(xtype);
	var total = 0;
	if (xtype == "/") {
		total = amt / rate;
	} else {
		//alert("是*");
		total = amt * rate;
	}
	//alert("total="+total);
	//return total.toFixed(2);
	return roundFixed(total,2);
}

function formatArapFront(v, m) {
	if (v == "AP") {
		m.style = 'background-color:#FFB7DD;width: 40px;';
		v = "应付";
	} else {
		m.style = 'background-color:#66FF66;width: 40px;';
		v = "应收";
	}
	return v;
}

function roundFixed(num, fixed) {
	var pos = num.toString().indexOf('.'),
		decimal_places = num.toString().length - pos - 1,
		_int = num * Math.pow(10, decimal_places),
		divisor_1 = Math.pow(10, decimal_places - fixed),
		divisor_2 = Math.pow(10, fixed);
	return Math.round(_int / divisor_1) / divisor_2;
}

//console.log(roundFixed(2147483647.5657,2));//-> 2147483647.57

//记录当前双击的行，用来处理刷新后自动滚动到当前行 neo 2017-03-02
var currentRowId;

function click(grid, rowindex, e){
   var record = grid.store.getAt(rowindex);//取出点击的那条记录
   currentRowId = record.get("dtlid");;
}

//滚动Grid到上次操作的行
function scrollToLastRow(){
	//alert(currentRowIndex);
	//console.dir(currentRowId);  
	//var row = arapGridJsvar.store.getById(currentRowId);
	var index = arapGridJsvar.store.find('arapid',currentRowId)
	//var index =arapGridJsvar.getStore().indexOf(row);
	//console.dir(index); 
	if(index > 0){ 
		scrollToRow(arapGridJsvar,index);
	}
}

//滚动Grid到指定的列  
function scrollToRow(grid, rowindex){
	var firstRow = Ext.get(grid.view.getRow(0));  
    var row = Ext.get(grid.view.getRow(rowindex));  
    var distance = row.getOffsetsTo(firstRow)[1];  
    grid.view.scroller.dom.scrollTop = distance;
}


//从json数据刷新rpsum表格数据
function refreshRpSum() {
	// 计算rpsum列表数据 neo 20160706
	var storeRpSum = rpSumGridJsvar.getStore();
	// 应收，应付的总数
	//console.dir(rpsumArray);
	storeRpSum.each(function(r_sum) {
		var cyid_sum = r_sum.get("cyid");
		for (x in getRpSumArray()){
			if(cyid_sum == x){
				r_sum.set("amt", rpsumArray[x].amt);
				r_sum.set("amtother", rpsumArray[x].amtother);
				r_sum.set("amtback", rpsumArray[x].amtback);
				r_sum.set("amtrest", (rpsumArray[x].amtrest).toFixed(2));
				r_sum.set("rp", rpsumArray[x].rp);
			}
		}
	});
	//console.dir(rpsumArray);
}

function rpDateCheck() {
	var v = isRpDateCheckJsVar.getValue();
	var date = rpdate.getValue();
	//alert(v);
	if (v) {
		if(date==''){
			rpdate.setValue(new Date());
		}
	} else {
		rpdate.setValue(null);
	}
}
function rpdateChange(){
	var date = rpdate.getValue();
	if(date==null || date == ''){
		isRpDateCheckJsVar.setValue(false);
	}else{
		isRpDateCheckJsVar.setValue(true);
	}
	
}

function initCmp() {
	//autoShowLink();
	if(actpayrecidJsVar.getValue()<0){
		isRpDateCheckJsVar.setValue(false);
	}else{
		var v = rpdate.getValue();
		//alert(v);
		if (v != undefined && v != null && v != '') {
			isRpDateCheckJsVar.setValue(true);
		} else {
			isRpDateCheckJsVar.setValue(false);
		}
	}
	refreshXrate();
}


function getRpSumArray() {
	//neo 
	if(rpsumArray["CNY"] != null){
		return rpsumArray;
	}else{
		var storeRpSum = rpSumGridJsvar.getStore();
		storeRpSum.each(function(r_sum) {
			var cyid_sum = r_sum.get("cyid");
			var rpsum = {};
			rpsum["cyid"]=cyid_sum;
			rpsum["amt"]=r_sum.get("amt");
			rpsum["rp"]=r_sum.get("rp");
			rpsum["ar"]=0;
			rpsum["ap"]=0;
			rpsum["arcut"]=0;
			rpsum["apcut"]=0;
			
			//rpsum["amtrest"]=0;
			//rpsum["amtother"]=0;
			//rpsum["amtback"]=0;
			
			if(r_sum.get("amtrest") == undefined || r_sum.get("amtrest") == null){
				rpsum["amtrest"]=0;
			}else{
				rpsum["amtrest"]=r_sum.get("amtrest");
			}
			
			if(r_sum.get("amtother") == undefined || r_sum.get("amtother") == null){
				rpsum["amtother"]=0;
			}else{
				rpsum["amtother"]=r_sum.get("amtother");
			}
			
			if(r_sum.get("amtback") == undefined || r_sum.get("amtback") == null){
				rpsum["amtback"]=0;
			}else{
				rpsum["amtback"]=r_sum.get("amtback");
			}
			
			
			rpsumArray[cyid_sum] = rpsum;
		});
		//console.dir(rpsumArray);
	}
	return rpsumArray;
}


//设置兑换率后确认，重算列表的收付金额
function sureRate() {
	// alert(currencyJsvar.getValue());
	//if (!currencyJsvar || currencyJsvar.getValue() == "") {
	//	alert("请选择销账的币制");
	//	currencyJsvar.focus();
	//	return;
	//}
	// 临时屏蔽grid的onafteredit的function
	// arapGridJsvar.un('celldblclick', function(){});
	
	getRpSumArray();
	
	var xratecyidfm = xrate_cyidfm.getValue();
	var xratecyidto = xrate_cyidto.getValue();

	var store = arapGridJsvar.getStore();
	var rowno = 0;
	store.each(function(r) {
		//alert(r);
		var currencyfm = r.get("currency");
		
		if(currencyfm == xratecyidfm){
			r.set("currencyto", xratecyidto);
			
			var currencyto = r.get("currencyto");
			var rates = getRateMap(currencyfm , currencyto);
			
			if(rates == undefined) {
				rowno++;
				return;
			}else{
				var xrate_old = rates.get("rate");
				var xtype_old = rates.get("xtype");
				r.set("xrates", xrate_old);
		
				var amountremaind_old = r.get("amountactrecpay0");
				var amountwf_old = r.get("amountwf"); //销账金额
				var amountrpflag_old = r.get("amountrpflag"); //实收付金额
				var amountrpflag_new = countByRate(amountwf_old, xrate_old, xtype_old);
				//实收付金额
				r.set("amountrpflag", amountrpflag_new);
				r.set("xtype", xtype_old);
				r.set("xrate", xrate_old);
				
				//清空上面对应币制的数据 neo
				rpsumArray[currencyfm].amt = 0;
			}
		}
	});
	refreshRpSum();
	// 恢复编辑后事件监听
	// grid.on('celldblclick',countAmtga);
	autoCalcAmountwfremaind();
}

function getRateMap(cyfm , cyto) {
	var store = rateGrid.getStore();
	var row;
	store.each(function(r) {
		var cyid = r.get("currencyfm");
		if(r.get("currencyfm") == cyfm && r.get("currencyto") == cyto){
			row = r;
		}
	});
	return row;
}

function refreshRate(currencyVal) {
	//alert(currencyVal);
	if (currencyVal == null) {
		currencyVal = "CNY";
	}
	doRefreshRate.addParam("cyid", currencyVal);
	doRefreshRate.submit();
}


function calcAmountRest(r) {
	var amountactrecpay0_old = r.get("amountactrecpay0");//取出销账余额计算值
	var amountactrecpay_old = r.get("amountactrecpay");//取出销账余额原值
	var amountwf_old = r.get("amountwf");//取出销账金额原值
	var amount_old = r.get("amount");//原金额
	
	//console.log("amountactrecpay0_old:"+amountactrecpay0_old);
	//console.log("amountactrecpay_old:"+amountactrecpay_old);
	//console.log("amountwf_old:"+amountwf_old);
	//console.log("amount_old:"+amount_old);
	
	
	var ret;
	if(Number(amountactrecpay_old) == Number(amount_old)){
		ret = Number(amount_old) - Number(amountwf_old);
		return ret.toFixed(2);
	}
	if(Number(amountactrecpay_old) > 0){
		ret = Number(amountactrecpay_old) - Number(amountwf_old);
		return ret.toFixed(2);
	}
	if(Number(amountactrecpay_old) == 0){
		ret = Number(amount_old) - Number(amountwf_old);
		//if(ret > Number(amountactrecpay0_old)){
		//	ret = 0;
		//}
		if(Number(amountactrecpay0_old) - Number(amountwf_old)){
			ret = 0;
		}
		return ret.toFixed(2);
	}
	
}
/*点击工作单号直接打开工作单 */
    function jobnofanction(v,m,r) {
	 	//var url = AppUtils.chaosUrlArs("../ship/jobsedit.xhtml") + "&id="+this.selectedRowData.getJobid();
		//AppUtils.openWindow("_showJobno_"+this.selectedRowData.getJobno(), url);
		var id = r.get('jobid');
		var jobno = r.get('jobno');
		var udid = uuid();
	  	var ufid = uuid();
		var temp;

		if(jobno==null){
			temp = jobno;
			m.attr="style='text-decoration: line-through; color: #ff0000; font-size: 9pt;'";
		}else{
			temp = '<a target="_blank" href="/scp/pages/module/ship/jobsedit.xhtml?udid='+udid+'&ufid='+ufid+'&id='+id+'"><font color="blue">' + jobno + '</font></a>';	
		}
		return temp;	
    }

function changeModify(type) {
	//记录可销账余额是否有正数的情况，有正数的时候，置数AR，置数AP从余额里面扣减方式处理
	var setRpsumArrayTmp = new Array();
	var setRpsumArray = getRpSumArray();
	var isSetByRpAmtRest = false;
	for (x in setRpsumArray){
		if(setRpsumArray[x].amtrest > 0){
			setRpsumArrayTmp[x] = setRpsumArray[x];
			isSetByRpAmtRest = true;
		}
	}
	
	var store = arapGridJsvar.getStore();
	
	//判断列表有没有勾选
	var selects = arapGridJsvar.selModel.selections.items;
	
	
	store.each(function(r) {
		var isOnlySelect = false;
		if(selects.length > 0){//列表有勾选
			selects.forEach((item,index,selects)=>{
				if(r.get("dtlid") == item.id){
					isOnlySelect = true;
					return true;
				}else{
					//console.log(item.id);
				}
			})
		}
		
		if(selects.length > 0){//列表有勾选
			if(!isOnlySelect){
				//console.log(r.get("jobno"));
				return true;;
			}
		}
		
		var currencyfm = r.get("currency");
		var currencyto = r.get("currencyto");
		
		var rates = getRateMap(currencyfm , currencyto);

		if (rates.get("rate") == null) {
			alert("无法取到兑换率，请确认相应兑换率正确！");
			return;
		}
		
		
		if(currencyto == rates.get("currencyto")){
			var xtype_old = rates.get("xtype");
			var amountremaind_old = r.get("amountactrecpay");
	
			var amountrpflag = countByRate(amountremaind_old, rates.get("rate"), rates.get("xtype"));
			var amountrpflagNew = process(amountrpflag,'*');//处理折让比率
			amountrpflag = amountrpflagNew;
	
			var amountactrecpay_old = r.get("amountactrecpay");//取出销账余额原值
			var amountwf_old = r.get("amountwf");//取出销账金额原值
			var amount_old = r.get("amount");
			
			var araptype = r.get("araptype");
			// alert(amountremaind_old);
			if (type == "A") { //置数所有
				r.set("amountwf", amountremaind_old);
				r.set("amountrpflag", amountrpflag);
				r.set("amountactrecpay0", calcAmountRest(r));//将销账金额从销账余额里面减去
				r.set("xtype", rates.get("xtype"));
				r.set("xrate", rates.get("rate"));
			} else if (type == "R") {//置数应收
				if (araptype == "AR") {//Neo 20190523 如果可销账余额为正数的情况下，按可销账余额自动扣减赋值
					if(isSetByRpAmtRest == true){
						for (x in setRpsumArrayTmp){
							var rpSumCyid = setRpsumArrayTmp[x].cyid;
							var rpSumAmtRest = setRpsumArrayTmp[x].amtrest;
							console.log('cyid/amtrest:'+rpSumCyid +':'+rpSumAmtRest);
							if(rpSumCyid == r.get("currency")){
								if(rpSumAmtRest > 0 && r.get("amountwf")==0){
									if(r.get("amountactrecpay0")<=rpSumAmtRest){
										r.set("amountwf", r.get("amountactrecpay0"));
										r.set("isfinish",true);
									}else{
										r.set("amountwf", rpSumAmtRest);
									}
									setRpsumArrayTmp[x].amtrest = rpSumAmtRest - r.get("amountwf");
									r.set("amountrpflag", r.get("amountwf"));
									r.set("amountactrecpay0", calcAmountRest(r));//将销账金额从销账余额里面减去
									r.set("xtype", rates.get("xtype"));
									r.set("xrate", rates.get("rate"));
								}
							}
						}
					}else{
						r.set("amountwf", amountremaind_old);
						r.set("amountrpflag", amountrpflag);
						r.set("amountactrecpay0", calcAmountRest(r));//将销账金额从销账余额里面减去
						r.set("xtype", rates.get("xtype"));
						r.set("xrate", rates.get("rate"));
					}
					
				} else {
					r.set("amountwf", "0");
					r.set("amountrpflag", "0");
					r.set("amountactrecpay0", calcAmountRest(r));//将清空的销账金额累加到销账余额里面去
					r.set("xtype", "*");
					r.set("xrate", "0");
				}
			} else if (type == "P") {//置数应付
				if (araptype == "AP") {//Neo 20190523 如果可销账余额为正数的情况下，按可销账余额自动扣减赋值
					if(isSetByRpAmtRest == true){
						for (x in setRpsumArrayTmp){
							var rpSumCyid = setRpsumArrayTmp[x].cyid;
							var rpSumAmtRest = setRpsumArrayTmp[x].amtrest;
							console.log('cyid/amtrest:'+rpSumCyid +':'+rpSumAmtRest);
							if(rpSumCyid == r.get("currency")){
								if(rpSumAmtRest > 0 && r.get("amountwf")==0){
									if(r.get("amountactrecpay0")<=rpSumAmtRest){
										r.set("amountwf", r.get("amountactrecpay0"));
										r.set("isfinish",true);
									}else{
										r.set("amountwf", rpSumAmtRest);
									}
									setRpsumArrayTmp[x].amtrest = rpSumAmtRest - r.get("amountwf");
									r.set("amountrpflag", r.get("amountwf"));
									r.set("amountactrecpay0", calcAmountRest(r));//将销账金额从销账余额里面减去
									r.set("xtype", rates.get("xtype"));
									r.set("xrate", rates.get("rate"));
								}
							}
						}
					}else{
						r.set("amountwf", amountremaind_old);
						r.set("amountrpflag", amountrpflag);
						r.set("amountactrecpay0", calcAmountRest(r));//将销账金额从销账余额里面减去
						r.set("xtype", rates.get("xtype"));
						r.set("xrate", rates.get("rate"));
					}
				} else {
					r.set("amountwf", "0");
					r.set("amountrpflag", "0");
					r.set("amountactrecpay0", calcAmountRest(r));//将清空的销账金额累加到销账余额里面去
					r.set("xtype", "*");
					r.set("xrate", "0");
				}
			} else if (type == "N") {//清空所有
				r.set("amountwf", "0");
				r.set("amountrpflag", "0");
				r.set("amountactrecpay0", calcAmountRest(r));//将清空的销账金额累加到销账余额里面去
				r.set("xtype", "*");
				r.set("xrate", "0");
			}
			formateArAp(arapGridJsvar, store, r);
		}
	});

	autoCalcAmountwfremaind();
}



//高级检索里面确定后自动置数对数据
function autoSetDataByFilter() {
	rpsumArray = new Array();
	var store = arapGridJsvar.getStore();
	store.each(function(r) {
		var currencyfm = r.get("currency");
		var currencyto = r.get("currencyto");
		
		var rates = getRateMap(currencyfm , currencyto);

		if (rates.get("rate") == null) {
			alert("无法取到兑换率，请确认相应兑换率正确！");
			return;
		}
		
		if(currencyto == rates.get("currencyto")){
			var xtype_old = rates.get("xtype");
			var amountremaind_old = r.get("amountactrecpay");
	
			var amountrpflag = countByRate(amountremaind_old, rates.get("rate"), rates.get("xtype"));
			var amountrpflagNew = process(amountrpflag,'*');//处理折让比率
			amountrpflag = amountrpflagNew;
	
			var amountactrecpay_old = r.get("amountactrecpay");//取出销账余额原值
			var amountwf_old = r.get("amountwf");//取出销账金额原值
			var amount_old = r.get("amount");
			
			var araptype = r.get("araptype");
			// alert(amountremaind_old);
			if (r.get("actpayrecid") == 0) { //置数所有
				r.set("amountwf", amountremaind_old);
				r.set("amountrpflag", amountrpflag);
				r.set("amountactrecpay0", calcAmountRest(r));//将销账金额从销账余额里面减去
				r.set("xtype", rates.get("xtype"));
				r.set("xrate", rates.get("rate"));
			}
			formateArAp(arapGridJsvar, store, r);
		}
	});

	autoCalcAmountwfremaind();
}

/*
 * grid行双击事件,有数字的清空，没有数字的置数
 */
function autoSetAmount(arapGridJsvar, rowIndex, colIndex, e) {
	//alert(colIndex);
	//if (colIndex != 9 || colIndex != 10) {
	
	
	
	var colIndexUn1 = arapGridJsvar.getColumnModel().findColumnIndex('amountrpflag');
	var colIndexUn2 = arapGridJsvar.getColumnModel().findColumnIndex('currencyto');
	
	console.log('autoSetAmount:colIndexUn1:'||colIndexUn1);
	
	console.time("autoSetAmount:before:");
	//if (colIndex != 15 && colIndex != 16) {
	if (colIndex != colIndexUn1 && colIndex != colIndexUn2) {
		//临时屏蔽grid的onafteredit的function
		console.time("autoSetAmount:unafteredit:");
		arapGridJsvar.un('afteredit', function() {});
		console.timeEnd("autoSetAmount:unafteredit:");
		//arapGridJsvar.on('afteredit', function() {});

		var store = arapGridJsvar.getStore();
		var row = store.getAt(rowIndex);
		//var xrate_old = row.get("xrates");
		
		var araptype = row.get("araptype");
		var currencyfm = row.get("currency");
		var currencyto = row.get("currencyto");
		
		console.time("autoSetAmount:getRateMap:");
		var rates = getRateMap(currencyfm , currencyto);
		console.timeEnd("autoSetAmount:getRateMap:");
		
		//alter(rateMap);
		//var rates = rateMap[row.get("currency")];
		var xtype_old = rates.get("xtype");
		var amountremaind_old = row.get("amountactrecpay0");//销账金额
		console.time("autoSetAmount:countByRate:");
		var amountrpflag = countByRate(amountremaind_old, rates.get("rate"), rates.get("xtype"));
		console.timeEnd("autoSetAmount:countByRate:");
		//alert("amountrpflag="+amountrpflag);
		//var xrate_old = rates.get("rate");
		// r.set("xrates", xrate_old);
		//alert(amountrpflag);
		// alert(xrate_old);
		// alert(amountremaind_old);
		// alert(rmoney(amountremaind_old));
		var amountwfdesc = row.get("amountwf");
		if(amountwfdesc == null||amountwfdesc ==""){
			amountwfdesc = "0";
		}
		amountwfdesc =  new Number(amountwfdesc);

		var amount_old = row.get("amount");
		var amountactrecpay_old = row.get("amountactrecpay");//取出销账余额原值
		var amountwf_old = row.get("amountwf");//取出销账金额原值
		//console.log("amountwfdesc:"+amountwfdesc);
		//console.log("amount_old:"+amount_old);
		//console.log("amountactrecpay_old:"+amountactrecpay_old);
		//console.log("amountwf_old:"+amountwf_old);
		//console.log("amountremaind_old:"+amountremaind_old);
		//console.log("amountrpflag:"+amountrpflag);
		
		if (amountwfdesc!= 0) {//置为0，清空数据
			console.time("autoSetAmount:set111111:");
			row.set("amountwf", "0");//销账金额
			row.set("amountrpflag", "0");//实收付金额
			row.set("rpdate", null);
			row.set("xtype", "*");
			row.set("xrate", "0");
			console.timeEnd("autoSetAmount:set111111:");
			//console.log("amountactrecpay_old:"+amountactrecpay_old);
			//console.log("amountwfdesc:"+amountwfdesc);
			//console.log("amount_old:"+amount_old);
			
			console.time("autoSetAmount:set22222:");
			if(amountactrecpay_old + amountwfdesc<=amount_old){
				row.set("amountactrecpay", amountactrecpay_old + amountwfdesc);
				row.set("amountactrecpay0", amountactrecpay_old + amountwfdesc);//neo 20170306将清空的销账金额累加到销账余额里面去
			}
			console.timeEnd("autoSetAmount:set22222:");
			//console.dir("amountactrecpay01"+row.get("amountactrecpay0"));
			console.time("autoSetAmount:calcAmountRest:");
			row.set("amountactrecpay0",calcAmountRest(row));
			console.timeEnd("autoSetAmount:calcAmountRest:");
			//console.dir("amountactrecpay02"+row.get("amountactrecpay0"));
			//var amountremaind_old = row.get("amountactrecpay");
		} else {//置数
		
			//neo 20170112 处理兑换率中设置好了折合后自动计算折合
			var xratecyidfm = xrate_cyidfm.getValue();
			var xratecyidto = xrate_cyidto.getValue();
			console.time("autoSetAmount:getRateMapcountByRate:");
			if(currencyfm == xratecyidfm){
				rates = getRateMap(currencyfm , xratecyidto);
				row.set("currencyto", xratecyidto);
				amountrpflag = countByRate(amountremaind_old, rates.get("rate"), rates.get("xtype"));
			}else{
				
			}
			console.timeEnd("autoSetAmount:getRateMapcountByRate:");
		
			row.set("amountwf", amountremaind_old);//销账金额
			
			console.time("autoSetAmount:process:");
			var amountrpflagNew = process(amountrpflag,'*');//处理折让比率
			console.timeEnd("autoSetAmount:process:");
			
			console.time("autoSetAmount:set333333333:");
			row.set("xtype", rates.get("xtype"));
			row.set("xrate", rates.get("rate"));
			row.set("amountrpflag", amountrpflagNew);//实收付金额
			row.set("amountactrecpay", 0);
			
			console.timeEnd("autoSetAmount:set333333333:");
			
			console.time("autoSetAmount:calcAmountRest:");
			row.set("amountactrecpay0", calcAmountRest(row));//将销账金额从销账余额里面减去
			console.timeEnd("autoSetAmount:calcAmountRest:");
			
			store = null;
			row = null;
			rates = null;
		}
		console.timeEnd("autoSetAmount:before:");
		//formateArAp(arapGridJsvar, store, row);
		console.time('autoSetAmount:autoCalcAmountwfremaind:');
		autoCalcAmountwfremaind();
		console.timeEnd('autoSetAmount:autoCalcAmountwfremaind:');
		
		console.time('autoSetAmount:afteredit:');
		//恢复编辑后事件监听
		arapGridJsvar.on('afteredit', autoCalc);
		console.timeEnd('autoSetAmount:afteredit:');
	}
}

/*
 * rpsum grid行双击事件
 */
function autoSetAmountRpSum(rpSumGridJsvar, rowIndex, colIndex, e) {
	//alert(colIndex);
	rpSumGridJsvar.un('afteredit', function() {});
	if (colIndex == 3) { //
		var storeRpSum = rpSumGridJsvar.getStore();
		var rowCount = 0;
		storeRpSum.each(function(r_sum) {
			if(rowCount == rowIndex){
				var cyid_sum = r_sum.get("cyid");
				for (x in getRpSumArray()){
					if(cyid_sum == x){
						rpsumArray[x].amt = 0;
						r_sum.set("amt",0);
					}
				}
			}
			rowCount+=1;
		});
		refreshRpSum();
	}
	if (colIndex == 6) { //计算实收付金额
		// 计算rpsum列表数据 neo 20160706
		var storeRpSum = rpSumGridJsvar.getStore();
		// 应收，应付的总数
		var rowCount = 0;
		storeRpSum.each(function(r_sum) {
			if(rowCount == rowIndex){
				var rp_sum = r_sum.get("rp");
				var cyid_sum = r_sum.get("cyid");
				var amtrest_sum = r_sum.get("amtrest");
				var amtother_sum = r_sum.get("amtother");
				var amtback_sum = r_sum.get("amtback");
				var amt_sum = r_sum.get("amt");
				for (x in getRpSumArray()){
					if(cyid_sum == x){
						var amt = amt_sum - amtrest_sum;
						rpsumArray[x].amt = amt;
						rpsumArray[x].amtrest = 0;
						rpsumArray[x].ar = 0;
						rpsumArray[x].ap = 0;
					}
				}
			}
			rowCount+=1;
		});
		refreshRpSum();
	}
	//恢复编辑后事件监听
	rpSumGridJsvar.on('afteredit', autoCalcRpSum);
}

// 修改实收付金额后自动计算销账金额
function autoCalc(obj) {
	//alert("dit");
	if (obj) {
		var columnfield = obj.field;
		//console.dir(columnfield)
		var r = obj.record;
		
		var amount_old = r.get("amount");
		var amountactrecpay_old = r.get("amountactrecpay0");//取出销账余额原值

		//******************************************************************************************************************************************
		if(columnfield == "amountwf"){//修改销账金额
			var amtRest = calcAmountRest(r);
			if(Number(amtRest)<0){
				alert('销账金额超出总余额，请检查!');
				r.set("amountwf", 0);
				r.set("amountrpflag", 0);
				return;
			}
			r.set("amountactrecpay0", amtRest);//将销账金额从销账余额里面减去
			return;
		}

		if(columnfield == "amountrpflag"){//修改实收付金额
			var auto = autoCalcAmtStl.getValue();
			if(auto == false){
				//alert("不自动算");
				autoCalcAmountwfremaind();
				return;
			}
		}
		var amountrpflagVar = new Number(r.get("amountrpflag") + "");
		//alert(amountrpflagVar);
		
		var currencyfm = r.get("currency");
		var currencyto = r.get("currencyto");
		var rates = getRateMap(currencyfm , currencyto);
		
		if (rates.get("rate") == null) {
			alert("无法取到兑换率，请确认相应兑换率正确！");
			return;
		}

		var amountrpflag = r.get("amountrpflag");

		var total = 0;
		var amt = amountrpflag;
		var rate = rates.get("rate");
		var xtype = rates.get("xtype");
		if (xtype == "/") {
			total = amt * rate;
		} else {
			total = amt / rate;
		}
		//结果取两位
		var amountwfVal = total.toFixed(2);
		//var amountwfVal = total;
		//alert(amountwfVal);
		//var amountactrecpay = r.get("amountactrecpay");
		//if (amountwfVal.toFixed(2) > amountactrecpay.toFixed(2)) {
		//	alert("销账金额不应超出待核销金额!"+amountwfVal+">"+amountactrecpay);
		///	r.set("amountwf", 0);
		//	r.set("amountrpflag", 0);
		//	return;
		//}
		
		if(columnfield == "currencyto"){//修改币制
			r.set("xtype", xtype);
			r.set("xrate", rate);
			r.set("amountwf", 0);
			r.set("amountrpflag", 0);
			r.set("amountactrecpay0", calcAmountRest(r));//将销账金额从销账余额里面减去
			return;
		}else{
			r.set("xtype", xtype);
			r.set("xrate", rate);
			r.set("amountwf", amountwfVal);
			
			
			
			var amtRest = calcAmountRest(r);
			if(Number(amtRest)<0){
				alert('销账金额超出总余额，请检查!');
				r.set("amountwf", 0);
				r.set("amountrpflag", 0);
				return;
			}
			
			r.set("amountactrecpay0", calcAmountRest(r));//将销账金额从销账余额里面减去
			autoCalcAmountwfremaind();
		}
	}
}


// 修改RPSUM实收付金额后自动计算余额
function autoCalcRpSum(obj) {
	if (obj) {
		if(rpsumArray.length ==0)getRpSumArray();
		var columnIndex = obj.column;
		var r = obj.record;
		var amtrest_sum = r.get("amtrest");
		var amtother_sum = r.get("amtother");
		var amtback_sum = r.get("amtback");
		var amt_sum = r.get("amt");
		var cyid_sum = r.get("cyid");
		
		if(columnIndex == 4){//修改实收付金额
			var amtrestNew = Number(amtrest_sum) + Number(obj.value) - Number(obj.originalValue);//将销账金额从销账余额里面减去
			rpsumArray[cyid_sum].amt = Number(obj.value);
			rpsumArray[cyid_sum].amtrest = amtrestNew;
		}
		
		if(columnIndex == 5){//修改其他费用
			var amtrestNew = Number(amtrest_sum) + Number(obj.value) - Number(obj.originalValue);//将销账金额从销账余额里面减去
			rpsumArray[cyid_sum].amtother = obj.value;
			rpsumArray[cyid_sum].amtrest = amtrestNew;
		}
		if(columnIndex == 9){//修改退款
			var amtrestNew = Number(amtrest_sum) -(Number(obj.value) - Number(obj.originalValue));//将销账金额从销账余额里面减去
			rpsumArray[cyid_sum].amtback = obj.value;
			console.log('obj.value:'+obj.value);
			rpsumArray[cyid_sum].amtrest = amtrestNew;
		}
		console.dir(rpsumArray);
		if(columnIndex == 4 || columnIndex == 5 || columnIndex == 9){
			refreshRpSum();
		}
	}
}



// 自动计算可销账余额
function autoCalcAmountwfremaind() {



	var store = arapGridJsvar.getStore();
	// 应收，应付的总数
	var totalR = 0;
	var totalP = 0;
	var total = 0;
	var existar = false;
	var existap = false;
	
	getRpSumArray();
	
	
	for (x in rpsumArray){
		rpsumArray[x].ar = 0;
		rpsumArray[x].ap = 0;
	}
			
	store.each(function(r) {
		var araptype = r.get("araptype");
		//var amountrpflag = new Number(r.get("amountrpflag") + ""); 可能返回NaN
		var amountrpflag = r.get("amountrpflag");
		var currencyto  = r.get("currencyto");
		
		if(amountrpflag == null||amountrpflag ==""){
			amountrpflag = "0";
		}
		amountrpflag = new Number(r.get("amountrpflag"));
		
		
		
		
		for (x in rpsumArray){
			if(currencyto == x && amountrpflag != "0"){
				if (araptype == "AR") {
					rpsumArray[currencyto].ar += Number(amountrpflag);
					//console.log(rpsumArray[currencyto].ar);
					//alert(rpsumArray[currencyto].ar);
				} else if (araptype == "AP") {
					rpsumArray[currencyto].ap += Number(amountrpflag);
					//console.log(rpsumArray[currencyto].ap);
				}
			}
		}
		
		if (araptype == "AR") {
			totalR += amountrpflag;
			existar = true;
		} else if (araptype == "AP") {
			totalP += amountrpflag;
			existap = true;
		}
		if(rpsumArray[currencyto] == undefined){
			//console.log(currencyto);
			alert("undefined currency:"+currencyto + " please check!");
		}
		rpsumArray[currencyto].amtrest = rpsumArray[currencyto].amt + rpsumArray[currencyto].amtother - rpsumArray[currencyto].amtback - Math.abs((rpsumArray[currencyto].ar - rpsumArray[currencyto].ap));
		if((rpsumArray[currencyto].ar - rpsumArray[currencyto].ap) > 0){
			rpsumArray[currencyto].rp = "R";
		}else{
			rpsumArray[currencyto].rp = "P";
		}
		if((rpsumArray[currencyto].ar - rpsumArray[currencyto].ap) == 0 && rpsumArray[currencyto].ar > 0 && rpsumArray[currencyto].ap > 0){
			rpsumArray[currencyto].rp = "C";
		}
	});
	////console.dir(rpsumArray);
	
	//console.dir(rpsumArray);
	
	refreshRpSum();
}



function refreshXrate() {
	//var currencyVal = currencyJsvar.getValue();
	var currencyVal = "CNY";
	if(currencyVal == null || currencyVal == '')return;
	//alert(currencyVal);
	var store = arapGridJsvar.getStore();
	var rowno = 0;
	store.each(function(r) {
		r.set("currencyto", currencyVal);
		// setColor(rowno++,r);
		console.log('refreshXrate.store.each');
	});
	refreshRate(currencyVal);
}

//处理折让比率
function process(amt,xtype) {
	var rate = lessRate.getValue();
	var total;
	rate = Number(rate)/100;
	if (xtype == "*") {
		total = amt * rate;
	} else {
		total = amt / rate;
	}
	return total;
}

function checkSubmit() {
	
	var storeRpSum = rpSumGridJsvar.getStore();
	var flag = false;
	storeRpSum.each(function(r_sum) {
		var amtrest = r_sum.get("amtrest");
		if(Number(amtrest) < 0){
			flag = true;
			return false;
		}
	});
	if(flag){
		alert("可销账余额不能小于0，请双击!");
		return false;
	}
	
	var checkArapCorpidflag = false;
	var store = arapGridJsvar.getStore();
	var tips = "存在非入账公司结算地费用";
	
	var rp2vchFixArapCorpidFalg = rp2vchFixArapCorpidJsVar.getValue();
	if(rp2vchFixArapCorpidFalg == 'Y'){//销账时自动校正结算地
	}else{
		store.each(function(r) {
			var arapcorpid = r.get("arapcorpid");
			if(r.get("amountwf") >0){
				if(agentcorpidJsVar.getValue() == false){//未勾选代收代付
					var isMatchBranchCode = (accountCorpJsVar.getRawValue().indexOf(r.get("arapbranch")) > -1);
					if(arapcorpid != accountCorpJsVar.getValue() && isMatchBranchCode == false){
						checkArapCorpidflag = true;
						tips += "，请检查是否勾选了代收代付公司?";
						return false;
					}
				}else{
					if(arapcorpid != agentcorpidJsVar.getValue()){
						checkArapCorpidflag = true;
						tips += "，请检查代收代付公司是否正确?";
						return false;
					}
				}
			}
		});
		if(checkArapCorpidflag){
			alert(tips);
			return false;
		}
	}
	
	
	
	
	//var amountrpflag = amountwfremaindJsvar.getValue();
	//if (amountrpflag != null && amountrpflag < 0) {
	//	alert("可销账余额不能小于0!");
	//	return false;
	//}
	
	//var currencyV = currencyJsvar.getValue();
	//if (currencyV == null || currencyV == "") {
	//	alert("收付币值必填!");
	//	return false;
	//}
	
	var billtypeV = billtype.getValue();
	if (billtypeV == null || billtypeV == "") {
		//alert("票据类别必填!");
		return true;
	}else{
		if(billtypeV == "B"){//银行存款
			//if (bankid.getValue() == null || bankid.getValue() == "") {
			//	alert("银行必填!");
			//	return false;
			//}
		}else if(billtypeV == "C"){//支票
			//if (chequeno.getValue() == null || chequeno.getValue() == "") {
			//	alert("支票号码必填!");
			//	return false;
			//}
			//if (chequeendate.getValue() == null || chequeendate.getValue() == "") {
			//	alert("提示：支票到期日期未填!");
			//	return false;
			//}
		}else if(billtypeV == "D"){
		//应收 等于应付
			var store = arapGridJsvar.getStore();
		//应收，应付的总数
			var totalR = 0;
			var totalP = 0;
			var total = 0;
			var existar = false;
			var existap = false;
			store.each(function(r) {
				var araptype = r.get("araptype");
				
				var amountrpflag = r.get("amountrpflag");
				if(amountrpflag == null||amountrpflag ==""){
					amountrpflag = "0";
				}
				amountrpflag = new Number(r.get("amountrpflag"));
				
				//var amountrpflag = new Number(r.get("amountrpflag") + "");
				if (araptype == "AR" && amountrpflag != null&& amountrpflag !=0) {
					totalR += amountrpflag;
					existar = true;
				} else if (araptype == "AP" && amountrpflag != null&& amountrpflag !=0) {
					totalP += amountrpflag;
					existap = true;
				}
			});
			//total =  - totalP.toFixed(2);
			//在最后保存的时候计算
			if(totalR.toFixed(2)!=totalP.toFixed(2)){
				//alert("对冲实收付金额总和应该为0:应收:"+totalR.toFixed(2)+" 应付:"+totalP.toFixed(2));
				return true;
			}
		}
	}
	return true;
}


function changgridFilterPage(){
	var page = gridFilterPageComJsvar.getValue();
	doChangeGridFilterPage.addParam("page", page);
	doChangeGridFilterPage.submit();
}


function showVch(id){
	//showVchEditWindow.show();
	showVchWinAction.addParam("id",id);
	showVchWinAction.submit();
}
    
function resize(){
    var newHeight2 = (arapGridPanelJsvar.getSize().height) - 50;
    arapGridJsvar.setHeight(newHeight2);

    var newWidth2 = (arapGridPanelJsvar.getSize().width);
    arapGridJsvar.setWidth(newWidth2);

    arapGridJsvar.render();
}


function autoShowLink11111() {
	var bankidLabel = Ext.get('f1:bankidLabel');
	var chequenoLabel = Ext.get('f1:chequenoLabel');
	var chequeendateLabel = Ext.get('f1:chequeendateLabel');
	var otherFeeLabel = Ext.get('f1:otherFeeLabel');
	

	bankid.hide();
	chequeno.hide();
	chequeendate.hide();
	otherfee.hide();
	
	bankidLabel.hide();
	chequenoLabel.hide();
	chequeendateLabel.hide();
	otherFeeLabel.hide();
	

	var v = billtype.getValue();
	if (v == "B") {
		bankid.show();
		otherfee.show();
		if(otherfee.getValue() == 0)otherfee.setValue(null);
		bankidLabel.show();
		otherFeeLabel.show();
	} else if (v == "C") {
		chequeno.show();
		chequeendate.show();
		chequenoLabel.show();
		chequeendateLabel.show();
	}
}
function openRpt(rpid) {
	var rptUrl ="http://120.25.241.190:8888/scp/reportJsp/showReport.jsp?raq=/finance/rp_detail.raq&id="+rpid;
	window.open(rptUrl);
}

