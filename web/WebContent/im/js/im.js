$("#chat").click(function() {
    $("#chat").attr("src","images/head_2_1.png");
    $("#friends").attr("src","images/head_3.png");
    $("#other").attr("src","images/head_4.png");
    $("#skin").attr("src","images/head_5.png");
    $("#copyright").attr("src","images/head_6.png");
    $("#userlist").html("会话");
})
$("#friends").click(function() {
    $("#chat").attr("src","images/head_2.png");
    $("#friends").attr("src","images/head_3_1.png");
    $("#other").attr("src","images/head_4.png");
    $("#skin").attr("src","images/head_5.png");
    $("#copyright").attr("src","images/head_6.png");
    $("#userlist").html("个人设置");
    
  //页面层
    index = layer.open({
        type: 2,
        title:'个人设置',
        skin: 'layui-layer-rim', //加上边框
        area: ['400px', '600px'], //宽高
        content: './usercfg.html'
    });
})
$("#other").click(function() {
    $("#chat").attr("src","images/head_2.png");
    $("#friends").attr("src","images/head_3.png");
    $("#other").attr("src","images/head_4_1.png");
    $("#skin").attr("src","images/head_5.png");
    $("#copyright").attr("src","images/head_6.png");
})


$("#skin").click(function() {
    $("#chat").attr("src","images/head_2.png");
    $("#friends").attr("src","images/head_3.png");
    $("#other").attr("src","images/head_4.png");
    $("#skin").attr("src","images/head_5_1.png");
    $("#copyright").attr("src","images/head_6.png");
    $("#userlist").html("主题选择");
    //window.prompt("请输入皮肤颜色","blue");
    
  //页面层
    index = layer.open({
        type: 2,
        title:'设置主题颜色',
        skin: 'layui-layer-rim', //加上边框
        area: ['400px', '600px'], //宽高
        content: './setcolor.html'
    });
    
    
})

$("#copyright").click(function() {
	 $("#chat").attr("src","images/head_2.png");
	 $("#friends").attr("src","images/head_3.png");
	 $("#other").attr("src","images/head_4.png");
	 $("#skin").attr("src","images/head_5.png");
	 $("#copyright").attr("src","images/head_6_1.png");
	 $("#userlist").html("版权信息");
	 
	 var copyHtml = '<div id="" class="layui-layer-content" style="padding: 10px;line-height: 3;">版本： 1.0.0<br>版权所有：<a href="http://www.ufms.cn" target="_blank">深圳市航迅科技开发有限公司</a></div>';
	//页面层
    index = layer.open({
        type: 1,
        title:'版权',
        skin: 'layui-layer-rim', //加上边框
        area: ['300px', '200px'], //宽高
        content: copyHtml
    });
})





var csno = "0000";
var language = "en";
var curentUserid = 0;
var currentIndex = 0; //当前是左边列表中的第几个


var currentIMFromUserid = 0; //当前聊天的对象ID
var currentIMFromUsername = "";//当前聊天的对象名称
var currentIMFromRunning = false; //当前聊天的对象正在接收消息中
var supsize = 0 ; //接受消息人数

var myHead = "img/head/defaultHead.png";

//var server = "http://192.168.0.188:8288/";
//var server = "http://ufms.cn/";
var server = "/scp/";

function refreshCurrentIMMsg(){//自动刷新当前聊天窗口消息
	if(currentIMFromUserid == 0)return;
	if(currentIMFromRunning == true){
		//console.log('receive...............');
		return;
	}
	getMsg(currentIMFromUsername , currentIMFromUserid , userid);
}

//把接收到的消息放到对话框
function showMsg(namec,title,adress,sendTime){
	var g = title;
	//如果是表情，则用img拼接
	function h() { - 1 != g.indexOf("*#emo_") && (g = g.replace("*#", "<img src='img/").replace("#*", ".gif'/>"), h())
    }
	//如果是base64图片则用img
	function ifbase64() { - 1 != g.indexOf("data:image") && (g="<img class='img-responsive' src='"+g+"'/>")
    }
	//console.log("<img width='40' height='30' src='"+g+"'/>");
	var a = currentIndex,
	b = "img/head/2024.jpg";
	c = "img/head/2015.jpg";
	d = "\u738b\u65ed";//王旭
	var tile;
	h();
	ifbase64();
	if(adress!=""){
		tile = "<div class='message clearfix'><div class='user-logo'><img src='"+adress+"' class='setimgsize'/>" + "</div>" + "<h5 class='clearfix'>"+namec +"&nbsp;&nbsp;&nbsp;&nbsp;"+ sendTime + "</h5>" + "<div class='receive'><div class='arrow'></div>" + "<div style='padding:5px;'>" + g + "</div>" ;   	
	}else{
		tile = "<div class='message clearfix'><div class='user-logo'><img src='" + b + "' class='setimgsize'/>" + "</div>" + "<h5 class='clearfix'>"+namec +"&nbsp;&nbsp;&nbsp;&nbsp;"+ sendTime + "</h5>" + "<div class='receive'><div class='arrow'></div>" + "<div style='padding:5px;'>" + g + "</div>" ;   	
	}
	$(".mes" + a).append(tile);
	$(".chat01_content").scrollTop($(".mes" + a).height());
    message();
    $('div .receive img').zoomify();//给聊天窗口添加预览放大
}
 
//显示历史消息，自己发送的信息
function showMsgHisSelf(namec,title,adress,sendTime){
	var g = title;
	//如果是表情，则用img拼接
	function h() { - 1 != g.indexOf("*#emo_") && (g = g.replace("*#", "<img src='img/").replace("#*", ".gif'/>"), h())
    }
	//如果是base64图片则用img
	function ifbase64() { - 1 != g.indexOf("data:image") && (g="<img class='img-responsive' src='"+g+"'/>")
    }
	//console.log("<img width='40' height='30' src='"+g+"'/>");
	var a = currentIndex,
	b = "img/head/2024.jpg";
	c = "img/head/2015.jpg";
	d = "\u738b\u65ed";//王旭
	var tile;
	h();
	ifbase64();
	if(adress!=""){
		tile = "<div class='message clearfix'><div class='user-logo-self'><img src='"+adress+"' class='setimgsize'/>" + "</div>" + "<h5 class='clearfix' style='text-align: right;'>"+namec +"&nbsp;&nbsp;&nbsp;&nbsp;"+ sendTime + "</h5>" + "<div class='send'><div class='arrow'></div>" + "<div style='padding:5px;'>" + g + "</div>" ;   	
	}else{
		tile = "<div class='message clearfix'><div class='user-logo-self'><img src='" + b + "' class='setimgsize'/>" + "</div>" + "<h5 class='clearfix' style='text-align: right;'>"+namec +"&nbsp;&nbsp;&nbsp;&nbsp;"+ sendTime + "</h5>" + "<div class='send'><div class='arrow'></div>" + "<div style='padding:5px;'>" + g + "</div>" ;   	
	}
	$(".mes" + a).append(tile);
	$(".chat01_content").scrollTop($(".mes" + a).height());
    message();
    $('div .receive img').zoomify();//给聊天窗口添加预览放大
}


    
function getMsgSizeAll(){//获取收到的消息数并标记红色提示
 	$(".user_text").each(function ( index,element){
		var sendid = $(this).attr("id");
		var receiveid = userid;
		//console.log(sendid+"."+receiveid);
		if((currentIMFromUserid != sendid)){
			getMsgSize(sendid , receiveid);
		}else{
			//console.log(sendid+"//////////"+currentIMFromUserid);
		}
	});
}

function getMsgSizeAlls(){//获取收到的消息数并标记红色提示 neo 20190114 优化
 	var data ={};
 	data.src = "qq";
 	data.action = "getQueueMsgSizeAll";
 	data.receiveid = userid;
 	data.csno = csno;
		
	$.ajax({
		type: "POST",
		data: data,
		contentType: 'application/x-www-form-urlencoded;charset=GBK',
		url: server+"webChat?method=process",            
		success: function(jsonData) {
			if(typeof(jsonData)==="string"){
				jsonData = JSON.parse(jsonData);
			}
			console.log(jsonData);
			if(jsonData) {
				for(var i in jsonData){
					var sendid = jsonData[i].sendid;
					var size = jsonData[i].queuemsgsize;
					if(size > 0){
						$(".chat03 div[id="+sendid+"] .user_name sup").remove();
						$(".chat03 div[id="+sendid+"] .user_name").append("<sup>&nbsp"+size+"&nbsp</sup>")
						$(".chat03 div[id="+sendid+"] .user_name sup").css("background-color","RED").css("color","#FFFFFF").css("border-radius","8px 8px 8px 8px").css("font-size","16px").css("height","10px");
						$(".chat03 div[id="+sendid+"] .user_name").parent().parent().find('img').addClass("className");//增加抖动:shake shake-slow
					}
					if(size == 0){
						$(".chat03 div[id="+sendid+"] .user_name sup").remove();
						$(".chat03 div[id="+sendid+"] .user_name").parent().parent().find('img').removeClass("className");//移除抖动
					}
					//if(supsize!=$('.chat03_content a sup').size()){//当有新的消息时候就重新排序li
					//	$('.chat03_content a sup').each(function(){//对有sup标签的进行循环排序
							//console.log($(this).parents('li').html());
							//把有新接受的消息的li整体插入到不含sup标签的前面
					//		$('.chat03_content li').not(":has(sup)").first().before('<li>'+$(this).parents('li').html()+'</li>');
					//		$(this).parents('li').remove();//删除已经插入的li标签
					//	});
					//}
					//supsize = $('.chat03_content a sup').size();
				}
			}
		 }
	});
}

function getMsg(sender , sendid , receiveid){
	currentIMFromRunning = true;
	$.ajax({
            type: "GET",
            dataType: "html",
			contentType:'application/json',
            url:server+"webChat?method=process&src=qq&action=receive&sendid="+sendid+"&receiveid="+receiveid+"&csno="+csno,            
            success: function(jsonData) {
				if(typeof(jsonData)==="string"){
					jsonData = JSON.parse(jsonData);
				}
				if(jsonData) {
					//console.log(jsonData);
					for (i in jsonData) {
					    //console.log(jsonData[i]);
						if("NULL" != jsonData[i].msg){
							var talkTo = $(".talkTo a").attr("id");
							var adress = $(".choosed").children(".user_head").children("img").attr("src");
							//console.log(sender + ":" + jsonData.msg);
							showMsg(sender,jsonData[i].msg,adress,jsonData[i].sendtime);
							$.cookie(''+sendid, ''+sendid, { expires: 7 });  //把常联系人写入cookie
						}
					}
					
					getMsgSize(sendid , receiveid);
				}
				currentIMFromRunning = false;
            }
	  });
}

function showHistoryAll(){
	var sender = '';
	var sendid = curentUserid;
	var receiveid = currentIMFromUserid;
	$.ajax({
        type: "GET",
        dataType: "html",
		contentType:'application/json',
        url: server+"webChat?method=process&src=qq&action=getHistoryAll&sendid="+sendid+"&receiveid="+receiveid+"&csno="+csno,            
        success: function(jsonData) {
			if(typeof(jsonData)==="string"){
				jsonData = JSON.parse(jsonData);
			}
			if(jsonData) {
				//console.log(jsonData);
				$(".mes" + currentIndex).empty();//先清空
				for (i in jsonData) {
				    //console.log(jsonData[i]);
					if("NULL" != jsonData[i].msg){
						
						var sendidHis = jsonData[i].sendid;
					    var receiveidHis = jsonData[i].receiveid;
					    
					    if(sendidHis == sendid){//对方发的
					    	var talkTo = $(".talkTo a").attr("id");
							var adress = $(".choosed").children(".user_head").children("img").attr("src");
							//console.log(sender + ":" + jsonData.msg);
							showMsg(sender,jsonData[i].msg,adress,jsonData[i].sendtime);
					    }
					    if(sendidHis == receiveid){//自己发的
					    	var talkTo = $(".talkTo a").attr("id");
					    	var adress = $(".choosed").children(".user_head").children("img").attr("src");
							//console.log(sender + ":" + jsonData.msg);
							showMsgHisSelf('',jsonData[i].msg,adress,jsonData[i].sendtime);
					    }
					}
				}
			}
        }
  });
}
function getMsgHistory(sender , sendid , receiveid){
	$.ajax({
            type: "GET",
            dataType: "html",
			contentType:'application/json',
            url: server+"webChat?method=process&src=qq&action=getHistory&sendid="+sendid+"&receiveid="+receiveid+"&csno="+csno,            
            success: function(jsonData) {
				if(typeof(jsonData)==="string"){
					jsonData = JSON.parse(jsonData);
				}
				if(jsonData) {
					//console.log(jsonData);
					for (i in jsonData) {
					    //console.log(jsonData[i]);
						if("NULL" != jsonData[i].msg){
							
							var sendidHis = jsonData[i].sendid;
						    var receiveidHis = jsonData[i].receiveid;
						    
						    if(sendidHis == sendid){//对方发的
						    	var talkTo = $(".talkTo a").attr("id");
						    	var adress = $(".choosed").children(".user_head").children("img").attr("src");
								//console.log(sender + ":" + jsonData.msg);
								showMsg(sender,jsonData[i].msg,adress,jsonData[i].sendtime);
						    }
						    if(sendidHis == receiveid){//自己发的
						    	var talkTo = $(".talkTo a").attr("id");
						    	var adress = $(".choosed").children(".user_head").children("img").attr("src");
								//console.log(sender + ":" + jsonData.msg);
								showMsgHisSelf('',jsonData[i].msg,adress,jsonData[i].sendtime);
						    }
						}
					}
				}
            }
	  });
}

function getLastMsg(receiveid){
	$.ajax({
			type: "GET",
			dataType: "html",
			contentType:'application/json',
			url: server+"webChat?method=process&src=qq&action=getQueueLastMsg&receiveid=" + receiveid+"&csno="+csno,            
			success: function(jsonData) {
				if(typeof(jsonData)==="string"){
					jsonData = JSON.parse(jsonData);
				}
				if(jsonData) {
					console.log(jsonData);
					for (i in jsonData) {
						$("#"+jsonData[i].sendid).children(".user_message").html(delHtmlTag(jsonData[i].msg));
						var sendtime = new Date(jsonData[i].sendtime).toWeiXinString();
						$("#"+jsonData[i].sendid).next(".user_time").html(sendtime + "<span style='display: none;'>"+jsonData[i].sendtime+"</span>");
					}
				}
			}
	});
}

function sortLiByTime(){
	var ul = $(".chat03_content ul");
    var lis = [];

    lis = $(".chat03_content ul li");
    var ux = [];
    //循环提取时间，并调用排序方法进行排序
    for (var i=0; i<lis.length; i++){
        var tmp = {};
        tmp.dom = lis.eq(i);
        var spanTime = lis.eq(i).find("span").eq(0).html();
        if(spanTime != undefined && spanTime != ''){
        	var tm = lis.eq(i).find("span").eq(0).html().replace(/-/g,'/');
            tmp.date = new Date(tm);
            ux.push(tmp);
        }else{
        	tmp.date = new Date('1900-01-01 00:00:00');
        }
        ux.push(tmp);
    }
    //数组排序，支持年的比较
    ux.sort(function(a,b){
       var myDate = new Date();
       var year = myDate.getYear();
       if(a.date.getYear < year && b.date.getYear == year){
          return true;
       }
       return b.date - a.date;
    });
    //移除原先顺序错乱的li内容
    $('.chat03_content ul li').remove();
    //重新填写排序好的内容
    for (var i=0; i<ux.length; i++){
       ul.append(ux[i].dom);
    }
}

function delHtmlTag(str){
	if(str == undefined || str == null || str == '')return '';
	return str.replace(/<[^>]+>/g,"");//去掉所有的html标记
} 

Date.prototype.toWeiXinString = function() {
	let str;
	const now = new Date();
	const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
	const yesterday = new Date(now.getFullYear(), now.getMonth(), now.getDate()-1);
	const beforeYesterday = new Date(now.getFullYear(), now.getMonth(), now.getDate()-2);
	const monday = new Date(today);
	monday.setDate(today.getDate()-(today.getDay()?today.getDay()-1:6));
	//注意：date初始化默认是按本地时间初始的，但打印默认却是按GMT时间打印的，也就是说打印出的不是本地现在的时间
	//LocaleString的打印也有点问题，"0点"会被打印为"上午12点"
	if(this.getTime() > today.getTime()) {
		str = this.format("HH:ss");
	} else if(this.getTime() > yesterday.getTime()) {
		str = "昨天";
	} else if(this.getTime() > beforeYesterday.getTime()) {
		str = "前天";
	} else if(this.getTime() > monday.getTime()) {
		const week = {"0":"周日","1":"周一","2":"周二","3":"周三","4":"周四","5":"周五","6":"周六"}; 
		str = week[this.getDay()+""];
	} else {
		const hour = ["凌晨","早上","下午","晚上"];
		const h=this.getHours();
		if(h==12) str = "中午";
		else str = hour[parseInt(h/6)];
		str = this.format("MM月dd ") + str;
	}
	//str += this.format("HH:ss");
	return str;
}

/*
对Date的扩展，将 Date 转化为指定格式的String
月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q)可以用1-2个占位符
年(y)可以用1-4个占位符，毫秒(S)只能用1个占位符号(是1-3为的数字)
例子：
(new Date()).Format("yyyy-MM-dd hh:mm:ss.S")	==> 2006-07-02 08:09:04.423
(new Date()).Format("yyyy-M-d h:m:s.S")			==> 2006-7-2 8:9:4.18
*/
Date.prototype.format = function(fmt) {
	const o = {
		"y+": this.getFullYear(),
		"M+": this.getMonth()+1,
		"d+": this.getDate(),
		"H+": this.getHours(),
		"m+": this.getMinutes(),
		"s+": this.getSeconds(),
		"S+": this.getMilliseconds(),
		"q+": Math.floor(this.getMonth()/3) + 1,
		"h+": (()=>{
			const hour=this.getHours()%12;
			return hour==0?12:hour;
		})(),
		"E+": (()=>{
			const week = {"0":"Sunday","1":"Monday","2":"Tuesday","3":"Wednesday","4":"Thursday","5":"Friday","6":"Saturday"}; 
			return week[this.getDay()+""];
		})(),
		/*
		"e+": (()=>{
			const week = {"0":"Sun","1":"Mon","2":"Tue","3":"Wed","4":"Thu","5":"Fri","6":"Sat"}; 
			return week[this.getDay()+""];
		})(),
		*/
		"x1": (()=>{
			const week = {"0":"周日","1":"周一","2":"周二","3":"周三","4":"周四","5":"周五","6":"周六"}; 
			return week[this.getDay()+""];
		})(),
		"x2": (()=>{
			const hour = ["凌晨","早上","下午","晚上"];
			const h=this.getHours();
			if(h==12) return "中午";
			return hour[parseInt(h/6)];
		})(),
	}
	for(var k in o) {
		if(new RegExp("("+k+")", "g").test(fmt)) {
			const len = RegExp.$1.length;
			fmt = fmt.replace(RegExp.$1, len==1?o[k]:("00"+o[k]).substr(-len));
		}
	}
	return fmt;
}


function getMsgSize(sendid , receiveid){
	$.ajax({
			type: "GET",
			dataType: "html",
			contentType:'application/json',
			url: server+"webChat?method=process&src=qq&action=getQueueMsgSize&sendid="+sendid+"&receiveid=" + receiveid+"&csno="+csno,            
			success: function(jsonData) {
				if(typeof(jsonData)==="string"){
					jsonData = JSON.parse(jsonData);
				}
				if(jsonData) {
					console.log(jsonData.queuemsgsize);
					var size = jsonData.queuemsgsize;
					if(size > 0){
						$("#"+sendid+" sup").remove();
						$("#"+sendid).children(".user_name").append("<sup>&nbsp"+size+"&nbsp</sup>")
						$("#"+sendid+" sup").css("background-color","RED").css("color","#FFFFFF").css("border-radius","8px 8px 8px 8px").css("font-size","16px").css("height","10px");
						$("#"+sendid).parent().find('img').addClass("className");//增加抖动:shake shake-slow
					}
					if(size == 0){
						$("#"+sendid+" sup").remove();
						$("#"+sendid).parent().find('img').removeClass("className");//移除抖动
					}
					if(supsize!=$('.chat03_content a sup').size()){//当有新的消息时候就重新排序li
						$('.chat03_content a sup').each(function(){//对有sup标签的进行循环排序
							//console.log($(this).parents('li').html());
							//把有新接受的消息的li整体插入到不含sup标签的前面
							$('.chat03_content li').not(":has(sup)").first().before('<li>'+$(this).parents('li').html()+'</li>');
							$(this).parents('li').remove();//删除已经插入的li标签
						});
					}
					supsize = $('.chat03_content a sup').size();
				}
			}
	});
}

function init(userid , _csno,lang){
	curentUserid = userid;
	//console.log(_csno);
	csno = _csno;
	//console.log(csno);
	language = lang;
	
	$(".chat03_content li").live("click",function() {
        var b = $(this).index() + 1;
        currentIndex = b;
        var talkToName = $(this).children(".user_text").clone();
		talkToName.children('sup').remove();
		var sender = $(this).children(".user_text").children(".user_name").text();
        var sendid = $(this).children(".user_text").attr("id");
        currentIMFromUserid = sendid;
        currentIMFromUsername = sender;
        var receiveid = userid;
        
        var msgSize = $.trim($("a[id="+sendid+"] sup").text());
        
        $(".mes" + currentIndex).empty();//先清空
        getMsgHistory(sender , sendid , receiveid);
		getMsg(sender , sendid , receiveid);
		$(".chat01_content").scrollTop($(".mes"+currentIndex).height());//滚动到底部
    })
	
	
	html = "";
	$('.chat03_content ul').empty();
	$.ajax({//获取联系人列表
       type: "GET",  //提交方式  
       dataType: "html", //数据类型  
       contentType:'application/json',
       async :false,//异步：false（否则不能访问全局变量） 
	   url:'/scp/service?src=webServer&action=commonQueryByXml&sqlId=activemq.index.grid.page&qry=userid ='+"'"+'curentUserid'+"'",
       beforeSend: function () {
       			
       },
       complete: function () {

       },
       success: function (jsonData) { //提交成功的回调函数  
           if(jsonData == null || jsonData == "")return;
           //console.log(jsonData);
           if(typeof(jsonData)==="string"){
				jsonData = JSON.parse(jsonData);
           }
           var userListCount = 0;//记录列表总行数
		   if(jsonData) {
			   //系统消息，id为8888
			   html+=	 '<li>'
               html+=    '<label class="offline">'
               html+=    '</label>'
               html+=    '<div class="user_head">'
            	   
               var username = '';
               if(language=="ch"){
               	username = '系统通知';
               }else{
               	username = 'System Notice';
               }   
               html+=        '<img src="../main/img/laba.png"></a></img></div>'		
               html+=        '<div class="user_text" id="8888">'
           	   html+=        '		<p class="user_name">'+username+'</p>'
           	   html+=        '		<p class="user_message">'+''+'</p>'
           	   html+=        '</div>'
           	   html+=        '<div class="user_time"></div>'
               html+=	 '</li>';
			   
			 //系统消息，id为8888
			   for (var i = 0; i < jsonData.length; i++) {
				    //html = '';
				    html+=	  '<li>'
                    html+=    '<div class="user_head">'
                    //console.log(jsonData[i].headimg);
                    var username = '';
                    if(language=="ch"){
                    	username = jsonData[i].namec;
                    }else{
                    	username = jsonData[i].namee;
                    }
                	if(jsonData[i].headimg != null&&jsonData[i].headimg!=""){	
                		html+=        '<img src="../attachment/'+jsonData[i].headimg+'"></a></img></div>'
                	}else{//默认头像
                		html+=        '<img src="img/head/defaultHead.png"></a></img></div>'		                            
                	}
                	html+=        '<div class="user_text" id="'+jsonData[i].id+'">'
            		html+=        '		<p class="user_name">'+username+'</p>'
            		html+=        '		<p class="user_message">'+''+'</p>'
            		html+=        '</div>'
            		html+=        '<div class="user_time"></div>'
                   
                    html+='</li>';
                    userListCount++;
				}
			   $('.chat03_content ul').append(html);
			   
			  
			   
			   //初始化每个聊天的对话窗口div
			   for (var i = 0; i <= userListCount + 1; i++) {
				   $('.chat01_content').append("<div class='message_box mes"+i+"'></div>");
			   }
			}
			//$(".talkTo a").text($(".chat03_content li").children(".chat03_name").first().text());//取列表第一个人到发送消息
			$(".talkTo a").text('');//修改发送人为空
		}
 	});
}
function sender(textarea){//发送消息
	//var textarea = $("#textarea").val();
	var talkTo = $(".talkTo a").attr("id");
	
	if(undefined == talkTo || talkTo == null){
		alert("请选择聊天对象!");
		return false;
	}
	
	if(textarea==""){
		alert("消息不能为空!");
		return false;
	}
	if(textarea.indexOf("data:image")){
		
	}else{
		textarea = textarea.replace(/\r/ig, "").replace(/\n/ig,"");
	}
	//textarea = encodeURI(encodeURI(textarea));
 	var data ={};
 	data.src = "qq";
 	data.action = "send";
 	data.sendid = curentUserid;
 	data.receiveid = talkTo;
 	data.msg = textarea;
 	data.csno = csno;
 	$.ajax({
        type: "POST",
		contentType: 'application/x-www-form-urlencoded;charset=GBK',
		data: data,
        //url: server+"webchat/service?src=qq&action=send&sendid="+<%=userid%>+"&receiveid="+talkTo+"&msg="+textarea+"",
        url: server+"webChat?method=process",                 
        success: function(jsonData) {
			if(typeof(jsonData)==="string"){
				jsonData = JSON.parse(jsonData);
			}
			if(jsonData) {
				//console.log(jsonData);
			}
        }
	});
	return true;
}

function initDataFromBusiness(fromUserid,linktype,refid,refno){
	currentIMFromUserid=fromUserid;
    $("div[id="+currentIMFromUserid+"]").click();
	//if(linktype == 'J'){
	//	console.log(refid);
	//	var a = '<a target="_blank" href="/scp/pages/module/ship/jobsedit.xhtml?id='+refid+'">refno</a>';
	//	$("#textarea").val("Jobno:"+refno + a);
	//}
}

function findColmn(){//查找触发单击事件并定位到对应行
	var inputValue = $('.chat03_content #sechInput').val();
	if(inputValue==""||inputValue==null){
		return;
	}
	$('.chat03_content ul li .user_name:contains("'+inputValue+'")').parent().parent().click();
	var ul = $('.chat03_content ul');
	var sechInput = $('.chat03_content ul li .user_name:contains("'+inputValue+'")').parent().parent();
	ul.scrollTop(
		sechInput.offset().top - ul.offset().top + ul.scrollTop()//定位到指定位置
	);
}

$('.chat03_content #sechInput').keydown(function(e){
	if(e.keyCode==13){
		findColmn(); //处理事件 
	}
});

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes();
            //+ seperator2 + date.getSeconds();
    return currentdate;
}

//textarea图片粘贴发送
var textapp = document.getElementById('textarea');
textapp.addEventListener('paste', function(e){
	for (var i = 0; i < e.clipboardData.items.length; i++) {
		// 检测是否为图片类型
		if (e.clipboardData.items[i].kind == "file" && /image\//.test(e.clipboardData.items[i].type)) {
			var imageFile = e.clipboardData.items[i].getAsFile();
			var imgsize = imageFile.size;
			if(imgsize / (1024 * 1024) < 2) {
				var fileReader = new FileReader();
				fileReader.onloadend = function(e) {
					//图片的base64编码
					if(sender(e.target.result)){
						adde(e.target.result);
			    		//console.log(e.target.result);
			    	}
				};
				 //将文件读取为DataUrl
				fileReader.readAsDataURL(imageFile);
				e.preventDefault();
				break;
			} else {
                alert("上传图片大小不能超过2M");
            }
		}
	}
});

function lauage(){
	jQuery.i18n.properties({
        name : 'strings', //资源文件名称
        path : '/scp/common/i18n/', //资源文件路径
        mode : 'map', //用Map的方式使用资源文件中的值
        language : languagetype,
        callback : function() {//加载成功后设置显示内容
            $('#userlist').html($.i18n.prop('成员列表'));
            $('#retrieval').html($.i18n.prop('检索'));
            $('#chatrecord').html($.i18n.prop('聊天记录'));
            $('#expressions').html($.i18n.prop('常用表情'));
            $('#source a').html($.i18n.prop('UFMS航迅软件'));
        }
    });
}
