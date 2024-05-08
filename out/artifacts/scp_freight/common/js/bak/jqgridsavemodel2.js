function saveColModelFun(grid,gridid) {
	var cms=$(grid).jqGrid('getGridParam','colModel');
	var key = "";
	var width = "";
	var ishidden = "";
	for(var j = 2; j<cms.length; j++) {
		key += cms[j].name;
		width += cms[j].width + "";
		if(!cms[j].hidden) {
			ishidden += "N";//显示
		} else {
			ishidden += "Y";//隐藏
		}
		if(j < cms.length - 1) {
			key += ",";
			width += ","
			ishidden += ",";
		}
	}
	saveGridUserDef.addParam("gridid",gridid);
	saveGridUserDef.addParam("colkey",key);
	saveGridUserDef.addParam("colwidth",width);
	saveGridUserDef.addParam("ishidden",ishidden);
	saveGridUserDef.submit();
	
}


function applyGridUserDef(keys, colwidths, ishiddens,grid) {
	if(keys != null && keys.length > 0) {
    	var cms=$(grid).jqGrid('getGridParam','colModel');
		var key = keys.split(",");
		var colwidth = colwidths.split(",");
		var ishidden = ishiddens.split(",");
		var newOrder = [0,1];
		var newHide = [];
		for(var i = 0; i<key.length; i++) {
			for(var j = 2; j<cms.length; j++) {
				//console.dir(cms[j].name+"/"+key[i]+"/"+(cms[j].name == key[i]));
				if(cms[j].name == key[i]){
			 		newOrder.push(j);
			 		break;
				}
			}
			var h = ishidden[i];
			if(h == 'Y') {
				newHide.push(key[i]);
			}
		}
		$(grid).remapColumns(newOrder,true,false);//设置列顺序
		$(grid).hideCol(newHide);
	}
}

function saveColModelFunDefault(gridid) {
	if(confirm("Confirm restoring current list personalization? Restore will not be revoked(确认还原当前列表个性化设置?还原后将不可撤销)!")){
		saveGridUserDefSetDefault.addParam("gridid",gridid);
		saveGridUserDefSetDefault.submit();
	}
}

function columnWindowFunction(grid){
	 jQuery(grid).jqGrid('columnChooser',grid, {
  	   done : function (perm) {
  	      if (perm) {
  	          // "OK" button are clicked
  	          this.jqGrid("remapColumns", perm, true);
  	          // the grid width is probably changed co we can get new width
  	          // and adjust the width of other elements on the page
  	          //var gwdth = this.jqGrid("getGridParam","width");
  	          //this.jqGrid("setGridWidth",gwdth);
  	      } else {
  	          // we can do some action in case of "Cancel" button clicked
  	      }
  	   }
  	});
}  


