var isStart = false;
var isQQMsgFlashStart = false;
var msgSize = 0;
var waitforrqs;
var waitforgoodsaa;

var receiveid = 0;

function init(){
	setInterval('waitforsubmit()',1000*60*5);//定时获取后台任务数据，时间不能太短，会影响页面上导航响应
	setInterval('run()',1000);//页面任务提示闪动效果
	setInterval('gdtitle()',1000);//页面title里面滚动提示
	$("#tippictureshide").hide();//隐藏第二个喇叭？ 未知待删
	
	$("#logout").attr("href","#{main.indexBean.fromurl}");
	//var int=self.setInterval("getNotice.submit();",1000);
	var imtime = 30*2;
	if(intervaltimeJs != null && intervaltimeJs != undefined && intervaltimeJs.getValue() > 0){
		imtime = intervaltimeJs.getValue();
	}
	self.setInterval("getIMMsgNotice()",imtime*1000); //获取QQ消息数，不管谁发过来有消息即提示，闪动效果

	$("#tippictures").dblclick(function(){//双击后隐藏第二个，显示第二个
	    $(this).hide();
	    $("#tippictureshide").show();
  	});
}
	 	
function checkState(){
	isStart = false;
	waitforrqs = waitforrqJsVar.getValue();  
	waitforgoodsaa = waitforgoodsJsVar.getValue();  
	if(waitforrqs != '' || waitforgoodsaa != ''){
		isStart = true;
	}
}
function run(){ 
	checkState();
	
	//QQ消息提示闪烁
	if(isQQMsgFlashStart == true){
		if($('#activemqImg').css('visibility') == "visible"){
			$('#activemqImg').css('visibility',"hidden");
		}else{
			$('#activemqImg').css('visibility',"visible");
		}
	}else{
		$('#activemqImg').css('visibility',"visible");
	}
	
	
	if(isStart == false){
		$('#tippictures').css('visibility',"visible");
		$(document).attr("title","UFMS");
		titlename= $(document).attr("title").split("")
		return;
	}
	if($(document).attr("title") == "UFMS"){
		$(document).attr("title","#{l.m['您有新的任务!']}"+waitforrqs+waitforgoodsaa);
		titlename= $(document).attr("title").split("")
	}
	
	//$('#tippictures').toggle(100);
	if($('#tippictures').css('visibility') == "visible"){
		$('#tippictures').css('visibility',"hidden");
	}else{
		$('#tippictures').css('visibility',"visible");
	}
	processTipsShowUp();
}; 

//将提示的任务数放右上角
function processTipsShowUp(){
	//console.log(waitforgoodsaa);
	//console.log(waitforrqs);
	if(waitforgoodsaa != ''){
		if(waitforgoodsaa.indexOf("(")>0){
			var number = waitforgoodsaa.substring(waitforgoodsaa.indexOf("("),waitforgoodsaa.indexOf(")")+1);
 			if(waitforgoodsaa.indexOf("(")>0){
 				var val = waitforgoodsaa.substring(0,waitforgoodsaa.indexOf("("));
 				$('#waitforgoods').text(val);  
 			}
 			$("#waitforgoods sup").remove();
 			$("#waitforgoods").append("<sup>"+number+"</sup>")
			$("#waitforgoods sup").css("background-color","#1AB394").css("color","#FFFFFF").css("border-radius","5px 5px 5px 5px");;
		}
	}
	if(waitforrqs != ''){
		if(waitforrqs.indexOf("(")>0){
			var number = waitforrqs.substring(waitforrqs.indexOf("("),waitforrqs.indexOf(")")+1);
 			if(waitforrqs.indexOf("(")>0){
 				var val = waitforrqs.substring(0,waitforrqs.indexOf("("));
 				$('#waitforrq').text(val);  
 			}
 			$("#waitforrq sup").remove();
 			$("#waitforrq").append("<sup>"+number+"</sup>")
			$("#waitforrq sup").css("background-color","#f8ac59").css("color","#FFFFFF").css("border-radius","5px 5px 5px 5px");;
		}
	}
}

function waitforsubmit(){
	waitforjs.submit();
}

function setactivemqImg(){
	$('#activemqImg').addClass("className");
}
function getIMMsgNotice(){
	if(isSaasJsVar.getValue()=='Y'){//SAAS模式下不接收消息且影藏图片
		return;
	}
	//判断是否存在消息
	var userid = useridJsVar.getValue();
	if(userid != null && userid != undefined && userid > 0)receiveid = userid;
	if(receiveid == 0 )return;
	
	//console.log("receiveid:"+receiveid);
	$.ajax({
        type: "GET",
        dataType: "html",
		contentType:'application/json',
        url: "/scp/webChat?method=process&src=qq&action=existQueueMsg&receiveid="+receiveid+"&csno="+csnoJsVar.getValue(),            
        success: function(jsonData) {
			if(typeof(jsonData)==="string"){
				jsonData = JSON.parse(jsonData);
			}
			if(jsonData) {
				//console.log(jsonData);
				msgSize = jsonData.queuemsgsize;
				$("#activemqImg + sup").remove();
				if(msgSize > 0){
					$("#activemqImg").after("<sup>"+msgSize+"</sup>");
				}
				console.log("msgSize:"+msgSize);
				if("true" == jsonData.existqueuemsg){
					//console.log("开始闪烁吧");
					isQQMsgFlashStart = true;
				}else{
					isQQMsgFlashStart = false;
				}
			}
        }
	});
};

function gdtitle(){
	if(isStart == false){
		return;
	}
	titlename.push(titlename[0]);
	titlename.shift();
	document.title = titlename.join("");
}