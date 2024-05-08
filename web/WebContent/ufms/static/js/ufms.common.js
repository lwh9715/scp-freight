function actionPost(service,action,jsonData,callback){
	$.ajax({
		  type: 'POST',
		  url: getContextPath()+"/"+service+"?method="+action,
		  contentType:'application/json',
		  data: jsonData,
	        success:function(result){
	            callback(result);
	        },
	        error:function(result){
	            callback(result);
	        }
	});
}

// 公共ajax请求
//url、type、async、cache、headers、data、dataType、contentType、callback
//https://www.liangzl.com/get-article-detail-2595.html
function action(service,action,jsonData,callback){
	$.ajax({
	  type: 'POST',
	  url: getContextPath()+"/"+service+"?method="+action,
	  contentType:'application/json',
	  data: jsonData,
        success:function(result){
            callback(result);
        },
        error:function(result){
            callback(result);
        }
    });
}

function actionGet(service,action,args,callback){
	$.ajax({
	  type: 'GET',
	  url: getContextPath()+"/"+service+"?method="+action+"&"+args,
	  contentType:'application/json',
	  success:function(result){
	  	callback(result);
	  },
	  error:function(result){
        callback(result);
      }
    });
}

function actionAsyncGet(service,action,args,callback){
	$.ajax({
	  type: 'GET',
	  url: getContextPath()+"/"+service+"?method="+action+"&"+args,
	  contentType:'application/json',
	  async : false,
	  success:function(result){
	  	callback(result);
	  },
	  error:function(result){
        callback(result);
      }
    });
}

function getContextPath(){ 
	var pathName = document.location.pathname; 
	var index = pathName.substr(1).indexOf("/"); 
	var result = pathName.substr(0,index+1); 
	return result; 
} 

function getRequest() {
    var url = location.search; // 获取url中含"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for ( var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

/* 公共ajax请求(使用ES6语法)
 * https://www.cnblogs.com/aspwebchh/p/6629333.html
 * */
async function doAjax({url,data,method='GET'}){
	return new Promise(function(resolve, reject){
		$.ajax({
		    type: method,
		    url: getContextPath()+url,
		    contentType:'application/json',
		    data: data,
		    dataType:'json',
	        success:function(result){console.log(result.data);
				if(result.success == true){
					if(!result.data||result.data === '{}'||result.data === ''){
						if(result.message === ''){
							layer.msg("OK",{icon: 1});
						}else{
							layer.msg(result.message,{icon: 1});
						}
					}
					resolve(result);
				}else{
					console.log(result);
					if(result.message = 'NoRow'){
						console.log(result.message);
					}else{
						layer.msg(result.message,{icon: 7});
					}
				}
	        },
	        error:function(result){
	        	reject(result);
	        }
		});
	})
}

//全局定义一次, 加载自定义组件
layui.config({
     base: `${getContextPath()}/ufms/static/components/` //此处路径请自行处理, 可以使用绝对路径
}).extend({
	tablePlug:'tableplug'
	,ufmsGrid: 'ufmsgrid'
	,ufmsGridForm:'ufmsgridform'
	,tableFilter: 'sorttable/tableFilter'
	,tableChild: 'sorttable/tableChild'
	,tableMerge: 'sorttable/tableMerge'
	,soulTable: 'sorttable/soulTable'
	,excel: 'sorttable/excel'
});

function getAction(){
	return getContextPath()+action;
}
