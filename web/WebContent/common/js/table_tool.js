//格式化成百分比
function changeToPercent(num){
	 if(!/\d+\.?\d+/.test(num)){
	  //alert("必须为数字");
	 }
	 var result = (num * 100).toString(),
	  index = result.indexOf(".");
	 if(index == -1 || result.substr(index+1).length <= 4){
	  return result + "%";
	 }
	 return result.substr(0, index + 5) + "%";
}

//格式化成百分比保留两位小数
function toPercent(point){
    var str=Number(point*100).toFixed(2);
    str+="%";
    return str;
}

//数值金额格式化000,000,000.00
function formatCurrency(num) {
	if(num == null){
		return num;
	}
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
	num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
	cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
	num = num.substring(0,num.length-(4*i+3))+','+
	num.substring(num.length-(4*i+3));
	return (((sign)?'':'-') + num + '.' + cents);
}

/**
* @ function：合并指定表格列（表格id为table_id）指定列（列数为table_colnum）的相同文本的相邻单元格
* @ param：table_id 为需要进行合并单元格的表格的id。如在HTMl中指定表格 id="data" ，此参数应为 #data 
* @ param：table_colnum 为需要合并单元格的所在列。为数字，从最左边第一列为1开始算起。
*/
function table_rowspan(table_id, table_colnum) {
    table_firsttd = "";
    table_currenttd = "";
    table_SpanNum = 0;
    table_Obj = $(table_id + " tr td:nth-child(" + table_colnum + ")");
    
    table_Obj.each(function (i) {
        if (i == 0) {
            table_firsttd = $(this);
            table_SpanNum = 1;
        } else {
            table_currenttd = $(this);
            if (table_firsttd.text() == table_currenttd.text()) { //这边注意不是val（）属性，而是text（）属性
                //td内容为空的不合并
                if(table_firsttd.text() !=""){
                    table_SpanNum++;
                    table_currenttd.hide(); //remove();
                    table_firsttd.attr("rowSpan", table_SpanNum);
                }
            } else {
                table_firsttd = $(this);
                table_SpanNum = 1;
            }
        }
    });
}

/*
* @ function：合并指定表格行（表格id为table_id）指定行（行数为table_rownum）的相同文本的相邻单元格
* @ param：table_id 为需要进行合并单元格的表格id。如在HTMl中指定表格 id="data" ，此参数应为 #data 
* @ param：table_rownum 为需要合并单元格的所在行。其参数形式请参考jquery中nth-child的参数。
          如果为数字，则从最左边第一行为1开始算起。
          "even" 表示偶数行
          "odd" 表示奇数行
          "3n+1" 表示的行数为1、4、7、10.......
* @ param：table_maxcolnum 为指定行中单元格对应的最大列数，列数大于这个数值的单元格将不进行比较合并。
          此参数可以为空，为空则指定行的所有单元格要进行比较合并。
*/
function table_colspan(table_id, table_rownum, table_maxcolnum) {
    if (table_maxcolnum == void 0) { table_maxcolnum = 0; }
    table_firsttd = "";
    table_currenttd = "";
    table_SpanNum = 0;
    $(table_id + " tr:nth-child(" + table_rownum + ")").each(function (i) {
        table_Obj = $(this).children();
        table_Obj.each(function (i) {
            if (i == 0) {
                table_firsttd = $(this);
                table_SpanNum = 1;
            } else if ((table_maxcolnum > 0) && (i > table_maxcolnum)) {
                return "";
            } else {
                table_currenttd = $(this);
                if (table_firsttd.text() == table_currenttd.text()) {
                    //td内容为空的不合并
                    if (table_firsttd.text() !="") {
                    table_SpanNum++;
                    table_currenttd.hide(); //remove();
                    table_firsttd.attr("colSpan", table_SpanNum);
                    table_firsttd.attr("style", "text-align:center");
                    }
                } else {
                    table_firsttd = $(this);
                    table_SpanNum = 1;
                }
            }
        });
    });
} 