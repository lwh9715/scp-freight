function saveColModelFun(grid,gridid) {
	var config = grid.getSelectionModel().grid.events.columnmove.obj.plugins[0].grid.colModel.config;
	var key = "";
	var value = "";//value可以不用,暂时这么写
	var width = "";
	var order = "";
	var ishidden = "";
	for(var i=1;i<config.length;i++) {
		key += config[i].dataIndex;
		value += config[i].header;
		order += i;
		if(typeof config[i].width != 'undefined') {
    		width += config[i].width + "";
		} else {
			width += 100 + "";
		}
		if(!config[i].hidden) {
			ishidden += "N";//显示
		} else {
			ishidden += "Y";//隐藏
		}
		if(i < config.length - 1) {
			key += ",";
			value += ",";
			width += ","
			order += ",";
			ishidden += ",";
		}
	}
	saveGridUserDef.addParam("gridid",gridid);
	saveGridUserDef.addParam("colkey",key);
	//saveGridUserDef.addParam("colval",value);
	saveGridUserDef.addParam("colwidth",width);
	//saveGridUserDef.addParam("colorder",order);
	saveGridUserDef.addParam("ishidden",ishidden);
	saveGridUserDef.submit();
	
}


function applyGridUserDef(keys, colwidths, ishiddens,grid) {
	var oldorder = "";
	if(keys != null && keys.length > 0) {
		var key = keys.split(",");
		for(var i = 1; i<key.length + 1; i++) {
			oldorder += i;
			if(i < key.length) {
				oldorder += ",";
			}
		}
		oldorder = oldorder.split(",");
	}
	if(keys != null && keys.length > 0) {
    	var cms=grid.getColumnModel();
		var key = keys.split(",");
		var colwidth = colwidths.split(",");
		var ishidden = ishiddens.split(",");
		
		for(var i = 0; i<key.length; i++) {
			var index = cms.findColumnIndex(key[i]);
			if(index<=0)continue;
			//console.log('index:' + index + ' cms:' + cms + ' key[i]:' + key[i] );
			//console.log('width:' + cms.getColumnWidth(index) + ':' + Number(colwidth[i]));
			if(cms != undefined && cms.getColumnWidth(index) != Number(colwidth[i])){
				//console.log('width:' + cms.getColumnWidth(index) + ':' + Number(colwidth[i]));
				cms.setColumnWidth(index,Number(colwidth[i]));
			}
			var h = ishidden[i];
			//var f = true;
			if(h == 'Y') {
				//f = true;
				cms.setHidden(index,true);
			//} else {
			//	f = false;
			}
			//cms.setHidden(index,f);
			if(index != oldorder[i]){
				//console.log('order:' + index + ':' + oldorder[i]);
				//设置排序
	    		cms.moveColumn(index,oldorder[i]);
			}
		}
	}
}

function saveColModelFunDefault(gridid) {
	if(confirm("Confirm restoring current list personalization? Restore will not be revoked(确认还原当前列表个性化设置?还原后将不可撤销)!")){
		saveGridUserDefSetDefault.addParam("gridid",gridid);
		saveGridUserDefSetDefault.submit();
	}
}





function saveColModelMultipleFun(grid,gridid) {
	var config = grid.getSelectionModel().grid.events.columnmove.obj.plugins[0].grid.colModel.config;
	var key = "";
	var value = "";//value可以不用,暂时这么写
	var width = "";
	var order = "";
	var ishidden = "";
	for(var i=1;i<config.length;i++) {
		key += config[i].dataIndex;
		value += config[i].header;
		order += i;
		if(typeof config[i].width != 'undefined') {
			width += config[i].width + "";
		} else {
			width += 100 + "";
		}
		if(!config[i].hidden) {
			ishidden += "N";//显示
		} else {
			ishidden += "Y";//隐藏
		}
		if(i < config.length - 1) {
			key += ",";
			value += ",";
			width += ","
			order += ",";
			ishidden += ",";
		}
	}
	saveColModelMultipleFunAjax.addParam("gridid",gridid);
	saveColModelMultipleFunAjax.addParam("colkey",key);
	saveColModelMultipleFunAjax.addParam("colwidth",width);
	saveColModelMultipleFunAjax.addParam("ishidden",ishidden);
	saveColModelMultipleFunAjax.submit();

}


function reductionconfigurenameFun() {
	reductionconfigurenameAjax.addParam('reductionconfigurenamevalue', reductionconfigurenameJsVar.getValue());
	reductionconfigurenameAjax.submit();
}

function deleteColModelMultipleFun(grid,gridid) {
	var config = grid.getSelectionModel().grid.events.columnmove.obj.plugins[0].grid.colModel.config;
	var key = "";
	var value = "";//value可以不用,暂时这么写
	var width = "";
	var order = "";
	var ishidden = "";
	for(var i=1;i<config.length;i++) {
		key += config[i].dataIndex;
		value += config[i].header;
		order += i;
		if(typeof config[i].width != 'undefined') {
			width += config[i].width + "";
		} else {
			width += 100 + "";
		}
		if(!config[i].hidden) {
			ishidden += "N";//显示
		} else {
			ishidden += "Y";//隐藏
		}
		if(i < config.length - 1) {
			key += ",";
			value += ",";
			width += ","
			order += ",";
			ishidden += ",";
		}
	}
	deleteconfigurenameAjax.addParam("gridid",gridid);
	deleteconfigurenameAjax.addParam("colkey",key);
	deleteconfigurenameAjax.addParam("colwidth",width);
	deleteconfigurenameAjax.addParam("ishidden",ishidden);
	deleteconfigurenameAjax.submit();

}