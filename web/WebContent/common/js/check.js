var userAgent = navigator.userAgent;
var isIE = false;
if(userAgent.match(/msie/img)){
	isIE = true;
}
if (!!window.ActiveXObject || "ActiveXObject" in window){
	isIE = true;
}
if(isIE){
	window.location.href = "/scp/checkIE.html?url="+location.href;
}