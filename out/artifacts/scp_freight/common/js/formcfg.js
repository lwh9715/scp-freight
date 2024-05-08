//用来存放表单设置的data
var requiredcolums = null;
 
//设置颜色和影藏
function setSysformcfg(data){
	if(data == null || data == "")return;
	requiredcolums = data;
	if(typeof(data)==="string"){
		data = JSON.parse(data);
	}
	if(data){
		requiredcolums = data;
		for(var i = 0; i < data.length; i++){
			if(data[i].color!=null&&data[i].color!=''){
				//设置颜色
				$("input[id$=\\:"+data[i].columnid+"],textarea[id$=\\:"+data[i].columnid+"],input[id="+data[i].columnid+"_input]").css({'background-image':'none','background-color':data[i].color});
			}
			if(data[i].ishide!=null&&data[i].ishide!=''&&data[i].ishide==true){
				//设置影藏
				$("input[id$=\\:"+data[i].columnid+"],textarea[id$=\\:"+data[i].columnid+"],checkbox[id$="+data[i].columnid+"],input[id="+data[i].columnid+"_input]").parent('div').hide();
				$("label[id$="+data[i].columnid+"Lable").hide();
			}
		}
		//console.log(requiredcolums);
	}
}

//保存时候调用检查是否在表单设置中设置了必填
function ifisrequired(languge){
	var isrequiredtext = '';
	if(requiredcolums!=null){
		//console.log(requiredcolums);
		for(var i = 0; i < requiredcolums.length; i++){
			if(requiredcolums[i].isrequired == true || requiredcolums[i].isrequired == 'true'){
				var value = $("input[id$=\\:"+requiredcolums[i].columnid+"]").val();
				var value2 = $("textarea[id$=\\:"+requiredcolums[i].columnid+"]").val();
				var value3 = $("input[id="+requiredcolums[i].columnid+"_input]").val();
				if((value == null||value == "")&&(value2 == null||value2 == "")&&(value3 == null||value3 == "")){
					if(languge == 'en_US'){
						isrequiredtext = isrequiredtext+requiredcolums[i].namee+',';
						
					}else{
						isrequiredtext = isrequiredtext+requiredcolums[i].namec+',';
						
					}
					
				}
			}
		}
	}
	if(isrequiredtext!=''){
		if(languge == 'en_US'){
			alert(isrequiredtext + "  Required in form settings");
		}else{
			alert(isrequiredtext + "  表单设置中为必填");
		}
		return false;
	}
	return true;
}