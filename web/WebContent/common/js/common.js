function del(){if(window.confirm("确认删除吗？")){doDel.submit();}}function formatArapFront(b,a){if(b=="AP"){a.style="background-color:#FFB7DD;";b="应付";}else{a.style="background-color:#66FF66;";b="应收";}return b;}function format2money(a){return a?fmoney(a,2):"";}function fmoney(b,d){d=d>0&&d<=20?d:2;b=parseFloat((b+"").replace(/[^\d\.]/g,"")).toFixed(d)+"";var a=b.split(".")[0].split("").reverse(),c=b.split(".")[1];t="";for(i=0;i<a.length;i++){t+=a[i]+((i+1)%3==0&&(i+1)!=a.length?",":"");}return t.split("").reverse().join("")+"."+c;}function rmoney(a){return parseFloat(a.replace(/[^\d\.]/g,""));}function formatCurrency(a){a=a.toString().replace(/\$|\,/g,"");if(isNaN(a)){a="0";}sign=(a==(a=Math.abs(a)));a=Math.floor(a*100+0.50000000001);cents=a%100;a=Math.floor(a/100).toString();if(cents<10){cents="0"+cents;}for(var b=0;b<Math.floor((a.length-(1+b))/3);b++){a=a.substring(0,a.length-(4*b+3))+","+a.substring(a.length-(4*b+3));}return(((sign)?"":"-")+a+"."+cents);}function uuid(){var d=[];var a="0123456789abcdef";for(var b=0;b<36;b++){d[b]=a.substr(Math.floor(Math.random()*16),1);}d[14]="4";d[19]=a.substr((d[19]&3)|8,1);d[8]=d[13]=d[18]=d[23]="-";var c=d.join("");return c;}

function showTip(tip, type) {
    var $tip = $('#tip');
    if ($tip.length == 0) {
        $tip = $('<span id="tip" style="text-align: center;line-height: 60px;width: 150px; height: 60px;border-radius:15px;color:#31708f;background-color:#D6E3F2;border-color:#bce8f1;font-weight:bold;position:absolute;top:80px;left: 50%;z-index:9999"></span>');
        $('body').append($tip);
    }
    $tip.stop(true).attr('class', 'alert alert-' + type).text(tip).css('margin-left', -$tip.outerWidth() / 2).fadeIn(500).delay(20000).fadeOut(500);
}

function showmsg() {
    //showTip("OK!", 'info');
	//alert("OK!");
	layer.msg("OK!", {icon: 1});
}

document.write("<script language=javascript src='/scp/common/js/jquery-2.0.0.min.js'></script>" +
				"<script language=javascript src='/scp/main_win/component/layer-v3.0.3/layer/layer.js'></script>" +
				"<script language=javascript src='/scp/common/js/check.js?t=1'></script>"
				);

document.write(
				"<script language=javascript src='/scp/reportEdit/file/layer/layer.js'></script>"
				);
window.alert = function(str){
	if("OK" == str || "OK!" == str || str.indexOf('OK') > -1){
		layer.msg(str,{icon: 1});
	}else{
		layer.msg(str,{icon: 7,time:8*1000});
	}
};

function openNewPage(url) {
	var openLink = $("#newWindows");
	if(openLink.length == 0){
		$('body').append('<a id="newWindows"></a>');
		openLink = $("#newWindows");
	}
	openLink.attr('href', url);
	openLink.attr('target', '_blank');
	openLink[0].click();
}

/*
window.alert = function(str){
     var alertFram = document.createElement("DIV");
     alertFram.id="alertFram";
     alertFram.style.position = "absolute";
     
     alertFram.style.margin = "auto";
     alertFram.style.left = "50%";
     alertFram.style.top = "50%";
     alertFram.style.transform = "translate(-50%,-50%)";

     alertFram.style.borderRadius = "15px";
     alertFram.style.width = "auto";
     alertFram.style.minWidth = "168px";
     
     alertFram.style.height = "auto";
     alertFram.style.minHeight = "40px";
     
     alertFram.style.background = "#D6E3F2";
     alertFram.style.textAlign = "center";
     alertFram.style.lineHeight = "40px";
     alertFram.style.zIndex = "99999";     
     strHtml = "<ul style=\"list-style:none;margin:5px;padding:2px;width:100%;\">\n";
	 strHtml += " <li style=\"text-align:center;color:#31708f;font-weight:bold;font-size:16px;\">&nbsp;&nbsp;"+str+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>\n";
	 strHtml += "</ul>\n";
	 strHtml += " <div style=\"width:20px;height:20px;border-radius:60%;position:absolute;top:-5px;right:4px;color:#31708f;font-weight:bold;\"><a style=\"text-decoration:none\" href='javascript:void(0)' onclick='doOk()'>X</a></div>\n";
	 alertFram.innerHTML = strHtml;
	 document.body.appendChild(alertFram);
	 
	 var timeClear = 5000;//默认为5秒，OK类型的，为1秒
	 if("OK" == str)timeClear=1000;
     var time=0; 
     this.doOk = function(){
    	 closeTips();
         time=0;
         console.log("doOk clear ad:"+ad);
		 clearInterval(ad);
     };     

     <!-- 倒计时 -->
     function _fresh() {	
 		var nowtime = new Date();
 		if(time==0){time=nowtime;} 			
 		var leftsecond=parseInt((parseInt(time.getTime()) + timeClear - parseInt(nowtime.getTime()))/1000);
 		console.log("leftsecond:"+leftsecond);
 		if(leftsecond<1){
 			leftsecond=0;
 			closeTips();
 			console.log("_fresh clear ad:"+ad);
 			clearInterval(ad);
 		}
 	} 
     
    function closeTips(){
    	var closeObj = document.getElementById("alertFram");
		if(closeObj != null && closeObj.parentNode != null){
			closeObj.parentNode.removeChild(closeObj);
		}
   }

    <!-- 定时器 -->
    var ad = setInterval(_fresh,1000);
    console.log("add ad:"+ad);
    alertFram.focus();
    document.body.onselectstart = function(){return false;};
};
*/	

function setUserBgColor(){
	try {
		if (window.localStorage) {
			var bgcolor = window.localStorage.bgcolor;
			if(bgcolor != null){
				document.getElementById('cmsbg').href='/scp/common/css/common-bg-'+bgcolor+'.css';
			}
		}
	}catch(err){
	}	
}

setUserBgColor();

/**
 * 处理列表错位问题，右边滚动条右移
 * @param gridJsvar grid的jsvar
 */
function resizeGridWidth(gridJsvar){
	var totalWidth = gridJsvar.colModel.totalWidth;
	for (var i = 0, len = gridJsvar.colModel.config.length; i < len; i++) {
        if (!gridJsvar.colModel.isHidden(i)) {
            totalWidth += 2;
        }
    }
	totalWidth += 2;
	//console.log(totalWidth);
	$('.x-grid3-row').css('width',totalWidth+'px');
	$('.x-grid3-header-offset').css('width',totalWidth+'px');
	$('.x-grid3-scroller').css('width',($('.x-grid3-scroller').width()+20)+'px');
	$('.x-grid3-gridsummary-row-inner').css('width',totalWidth+'px');
}


/**
 * floatTool 包含加减乘除四个方法，能确保浮点数运算不丢失精度
 *
 * 我们知道计算机编程语言里浮点数计算会存在精度丢失问题（或称舍入误差），其根本原因是二进制和实现位数限制有些数无法有限表示
 * 以下是十进制小数对应的二进制表示
 *      0.1 >> 0.0001 1001 1001 1001…（1001无限循环）
 *      0.2 >> 0.0011 0011 0011 0011…（0011无限循环）
 * 计算机里每种数据类型的存储是一个有限宽度，比如 JavaScript 使用 64 位存储数字类型，因此超出的会舍去。舍去的部分就是精度丢失的部分。
 *
 * ** method **
 *  add / subtract / multiply /divide
 *
 * ** explame **
 *  0.1 + 0.2 == 0.30000000000000004 （多了 0.00000000000004）
 *  0.2 + 0.4 == 0.6000000000000001  （多了 0.0000000000001）
 *  19.9 * 100 == 1989.9999999999998 （少了 0.0000000000002）
 *
 * floatObj.add(0.1, 0.2) >> 0.3
 * floatObj.multiply(19.9, 100) >> 1990
 * 
 * floatTool.add(a,b);//相加
 * floatTool.subtract(a,b);//相减
 * floatTool.multiply(a,b);//相乘
 * floatTool.divide(a,b);//相除
 *
 */
var floatTool = function() {
    /*
     * 判断obj是否为一个整数
     */
    function isInteger(obj) {
        return Math.floor(obj) === obj
    }

    /*
     * 将一个浮点数转成整数，返回整数和倍数。如 3.14 >> 314，倍数是 100
     * @param floatNum {number} 小数
     * @return {object}
     *   {times:100, num: 314}
     */
    function toInteger(floatNum) {
        var ret = {times: 1, num: 0}
        if (isInteger(floatNum)) {
            ret.num = floatNum
            return ret
        }
        var strfi  = floatNum + ''
        var dotPos = strfi.indexOf('.')
        var len    = strfi.substr(dotPos+1).length
        var times  = Math.pow(10, len)
        if(floatNum<0){
        	floatNum = floatNum * times - 0.5;
        }else{
        	floatNum = floatNum * times + 0.5;
        }
        var intNum = parseInt(floatNum, 10)
        ret.times  = times
        ret.num    = intNum
        return ret
    }

    /*
     * 核心方法，实现加减乘除运算，确保不丢失精度
     * 思路：把小数放大为整数（乘），进行算术运算，再缩小为小数（除）
     *
     * @param a {number} 运算数1
     * @param b {number} 运算数2
     * @param digits {number} 精度，保留的小数点数，比如 2, 即保留为两位小数
     * @param op {string} 运算类型，有加减乘除（add/subtract/multiply/divide）
     *
     */
    function operation(a, b, op) {
        var o1 = toInteger(a)
        var o2 = toInteger(b)
        var n1 = o1.num
        var n2 = o2.num
        var t1 = o1.times
        var t2 = o2.times
        var max = t1 > t2 ? t1 : t2
        var result = null
        switch (op) {
            case 'add':
                if (t1 === t2) { // 两个小数位数相同
                    result = n1 + n2
                } else if (t1 > t2) { // o1 小数位 大于 o2
                    result = n1 + n2 * (t1 / t2)
                } else { // o1 小数位 小于 o2
                    result = n1 * (t2 / t1) + n2
                }
                return result / max
            case 'subtract':
                if (t1 === t2) {
                    result = n1 - n2
                } else if (t1 > t2) {
                    result = n1 - n2 * (t1 / t2)
                } else {
                    result = n1 * (t2 / t1) - n2
                }
                return result / max
            case 'multiply':
                result = (n1 * n2) / (t1 * t2)
                return result
            case 'divide':
                return result = function() {
                    var r1 = n1 / n2
                    var r2 = t2 / t1
                    return operation(r1, r2, 'multiply')
                }()
        }
    }

    // 加减乘除的四个接口
    function add(a, b) {
        return operation(a, b, 'add')
    }
    function subtract(a, b) {
        return operation(a, b, 'subtract')
    }
    function multiply(a, b) {
        return operation(a, b, 'multiply')
    }
    function divide(a, b) {
        return operation(a, b, 'divide')
    }

    // exports
    return {
        add: add,
        subtract: subtract,
        multiply: multiply,
        divide: divide
    }
}();