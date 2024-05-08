function saveColModelFun(grid) {
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
	
	doSaveModel.addParam("key",key);
	doSaveModel.addParam("value",value);
	doSaveModel.addParam("width",width);
	doSaveModel.addParam("order",order);
	doSaveModel.addParam("ishidden",ishidden);
	doSaveModel.submit();
}

function applyGridUserDef(keys, colorders, colwidths, ishiddens,grid) {
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
		var colorder = colorders.split(",");
		var colwidth = colwidths.split(",");
		var ishidden = ishiddens.split(",");
		
		for(var i = 0; i<key.length; i++) {
			var index = cms.findColumnIndex(key[i]);
			cms.setColumnWidth(index,Number(colwidth[i]));
			var h = ishidden[i];
			var f = true;
			if(h == 'Y') {
				f = true;
			} else {
				f = false;
			}
			cms.setHidden(index,f);
			//设置排序
    		cms.moveColumn(index,oldorder[i]);
		}
	}
}


function defineOrderfun() {
	var arrays = Ext.query("*[id$=\\_key]");
	var keystr = "";
	var xstr = "";
	var ystr = "";
	var names = "";
	if(arrays != null && arrays.length > 0) {
		for(var i = 0; i<arrays.length; i++) {
			var key = arrays[i].id;
			keystr += key;
			var name = Ext.get(key).dom.textContent;//名字
			names += (name.trim());
			var x = Ext.get(key).getX();
			xstr += x;
  			var y = Ext.get(key).getY();
  			ystr += y;
  			if(i < arrays.length - 1) {
  				keystr += ","
  				names += ",";	
  				xstr += ",";
  				ystr += ","
  			}
		}
		doDefineOrder.addParam("key",keystr);
		doDefineOrder.addParam("names",names);
		doDefineOrder.addParam("xstr",xstr);
		doDefineOrder.addParam("ystr",ystr);
		doDefineOrder.submit();
	}
}

function applyFormUserDef(keys, xs, ys, ishiddens) {
	if(keys != null && keys.length > 0) {
		var key = keys.split(",");
		var x = xs.split(",");
		var y = ys.split(",");
		var ishidden = ishiddens.split(",");
		for(var i = 0; i<key.length; i++) {
			Ext.get(key[i]).setLocation(x[i],y[i],true);
			if(ishidden[i] == 'Y') {
				Ext.get(key[i]).setVisible(false);
			} else {
				Ext.get(key[i]).setVisible(true);
			}
		}
	}
}


function designfun() {
	var arrays = Ext.query("*[id$=\\_key]");//查找以_key结尾的id
 	if(arrays != null && arrays.length > 0) {
 		for(var i = 0; i<arrays.length; i++) {
 			var key = arrays[i].id;
 			OM.E(function() {
				new Ext.dd.DDProxy(key); 
			});
 		}
 	}
} 


function applyDesktopUserDef(keys , xs, ys) {//colid为页面的id
	if(keys != null && keys.length > 0) {
		var key = keys.split(",");
		var x = xs.split(",");
		var y = ys.split(",");
		for(var i = 0; i<key.length; i++) {
			Ext.get(key[i]).setLocation(x[i],y[i],true);	
		}
	}
}