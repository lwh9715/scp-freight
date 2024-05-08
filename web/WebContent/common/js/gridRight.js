var columnId , columnVal;
var mapRightCol = new Map();


function cellclick(grid,rowIndex,columnIndex,e){
	var record = grid.getStore().getAt(rowIndex);
	var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
	columnId = grid.getColumnModel().getColumnAt(columnIndex).dataIndex;
	columnVal = record.get(fieldName);
	console.log('cellclick:'+columnId + "-" + columnVal);
	
	initFilterColumn();
}

//右键菜单代码关键部分 
function rightClickFn(grid,rowindex,e){ 
    e.preventDefault(); 
    rightClick.showAt(e.getXY()); 
    
}

var filterByColumn = new Ext.menu.Menu({
    ignoreParentClicks: true,
});

var rightClick = new Ext.menu.Menu({ 
    id:'rightClickCont', 
    items: [ 
            
		{ 
		     id: 'rMenu4'
		    ,text: '复制',iconCls:'ico_search'
			,handler:function()
		    {
		       	//alert(columnId + "-" + columnVal);
				//console.log(columnId + "-" + columnVal); 
				if(columnVal != undefined && columnVal != null){
					let input = document.createElement('input');
		    		input.setAttribute('readonly', 'readonly');
		    		input.setAttribute('value', columnVal);
		    		document.body.appendChild(input);
		    		input.focus();
		    		input.setSelectionRange(0,columnVal.length);
		    		if (document.execCommand('copy')) {
		    			document.execCommand('copy');
		    			console.log('复制成功');
		    		}
		    	    document.body.removeChild(input);
				}
		    }
		}
		,new Ext.menu.Separator()
		,{ 
            id: 'rMenu1', 
            text: '连续过滤',iconCls:'ico_search',
            handler:function()
            {
               //alert(columnId + "-" + columnVal);
        		console.log(columnId + "-" + columnVal); 
        		var mapMultiColumn;
        		var mapTmp = mapRightCol.get(columnId);
        		if(mapTmp){
        			mapMultiColumn = mapTmp;
        		}else{
        			mapMultiColumn = new Map();
        		}
        		mapMultiColumn.set(columnVal,columnVal);
        		mapRightCol.set(columnId,mapMultiColumn);
        		
                map2qry();
            },
        }
        ,{ 
            id: 'rMenu2', 
            text: '取消过滤',iconCls:'ico_search',
            handler:function()
            {
        		mapRightCol.clear();
        		map2qry();
            },
        }
        ,new Ext.menu.Separator()
        ,{ 
             id: 'rMenu3'
            ,text: '多选过滤',iconCls:'ico_search'
        	,menu: filterByColumn
        }
        
    ] 
});

	

//右键菜单代码关键部分 
function rightClickFn(grid,rowindex,e){ 
    e.preventDefault(); 
    rightClick.showAt(e.getXY()); 
};

function map2qry(){
	//gridRightQryJsVar.setValue(qry);
	gridThis.getStore().filterBy(function(record, id) {
		var flag = false;
		var dsJs='';
		
		for (var key of mapRightCol.keys()) {  
			var obj = mapRightCol.get(key);
			var multiMap = new Map();
			if(obj){
				multiMap = mapRightCol.get(key);
			}
			var dsJsInner='';
			console.log('multiMap:'+multiMap); 
			for (var keyInner of multiMap.keys()) {  
				dsJsInner += 'record.get("'+key+'") == "'+multiMap.get(keyInner)+'"' + '||';
			}
			dsJsInner = '('+dsJsInner.substring(0, dsJsInner.length - 2)+')';
			console.log('dsJsInner:'+dsJsInner); 
			dsJs += dsJsInner + '&&';
		}
		dsJs = dsJs.substring(0, dsJs.length - 2);
		console.log('dsJs:'+dsJs); 
		if(dsJs == ''){
			return true;
		}else{
			return eval(dsJs);
		}
    });
}

var gridThis;
function initGridRight(grid){
	gridThis = grid;
	grid.addListener('cellcontextmenu',cellclick);
	grid.addListener('rowcontextmenu', rightClickFn);
}

function initFilterColumn(){
	var valuesMap = new Map();
	gridThis.getStore().each(function(item, index, count) {
		let val = item.get(columnId);
		valuesMap.set(val , val);
	})
	filterByColumn.removeAll();
	for (var key of valuesMap.keys()) {  
	  console.log(key + ": " + valuesMap.get(key));  
	  filterByColumn.addMenuItem(new Ext.menu.CheckItem({
	        text: key
	        ,checkHandler:onItemCheck
	        ,hideOnClick:false
	    }))
	} 
}

function onItemCheck(item, checked){
	//Ext.MessageBox.alert('Item Check', String.format('You {1} the "{0}" menu item.', item.text, checked ? 'checked' : 'unchecked'));
	console.log(columnId + "-" + item.text); 
	var columnVal = item.text;
	
	var mapMultiColumn;
	var mapTmp = mapRightCol.get(columnId);
	if(mapTmp){
		mapMultiColumn = mapTmp;
	}else{
		mapMultiColumn = new Map();
	}
	if(checked){
		mapMultiColumn.set(columnVal,columnVal);	
	}else{
		mapMultiColumn.delete(columnVal);
	}
	
	mapRightCol.set(columnId,mapMultiColumn);
	
	map2qry();
}

//init(editGridJsvar);

