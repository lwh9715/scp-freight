


function changeModify(type) {
	var obj = $("#arapGrid").jqGrid("getRowData");
	for (row = 0; row < obj.length; row++){
		rowData = obj[row];
		//console.log(row);
		//console.log(rowData);
		if (type == "A") { //置数所有
			$("#arapGrid").setCell( row+1 ,'amountwf' , rowData.amount);
			$("#arapGrid").setCell( row+1 ,'amountrpflag' , rowData.amount);
		} else if (type == "R") {//置数应收
			if(rowData.araptype == 'AR'){
				$("#arapGrid").setCell( row+1 ,'amountwf' , rowData.amount);
				$("#arapGrid").setCell( row+1 ,'amountrpflag' , rowData.amount);
			}
		} else if (type == "P") {//置数应付
			if(rowData.araptype == 'AP'){
				$("#arapGrid").setCell( row+1 ,'amountwf' , rowData.amount);
				$("#arapGrid").setCell( row+1 ,'amountrpflag' , rowData.amount);
			}
		} else if (type == "N") {//清空所有
			$("#arapGrid").setCell( row+1 ,'amountwf' , 0);
			$("#arapGrid").setCell( row+1 ,'amountrpflag' , 0);
		}
	}
}


