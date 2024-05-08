//<![CDATA[

function del() {
	if (window.confirm('确认删除吗？')) {
		doDel.submit();
	}
}

function formatArapFront(v, m) {
	if (v == "AP") {
		m.style = 'background-color:#FFB7DD;';
		v = "应付";
	} else {
		m.style = 'background-color:#66FF66;';
		v = "应收";
	}
	return v;
}

function format2money(v) {
	return v ? fmoney(v, 2) : '';
}

function fmoney(s, n) {
	n = n > 0 && n <= 20 ? n : 2;
	s = parseFloat((s + "").replace(/[^\d\.]/g, "")).toFixed(n) + "";
	var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
	t = "";
	for (i = 0; i < l.length; i++) {
		t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
	}
	return t.split("").reverse().join("") + "." + r;
}

function rmoney(s) {
	return parseFloat(s.replace(/[^\d\.]/g, ""));
}


/**
 * 将数值四舍五入(保留2位小数)后格式化成金额形式
 *
 * @param num 数值(Number或者String)
 * @return 金额格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatCurrency(num) {
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
 * UUID
 */
function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

// ]]>

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
	alert("OK!");
}

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
