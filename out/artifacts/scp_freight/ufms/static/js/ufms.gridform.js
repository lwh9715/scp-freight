
function getAction(){
	return getContextPath()+action;
}


var editWindow;
$(function () {
	editWindow = $('#editWindow').clone(true);
	$('#editWindow').remove();
	
	$("button[action]").each(function(){
		  $(this).on("click", function(){
			  var method = $(this).attr("action");
			  actionGet(action,method,'',function(res){
		  		console.log(res);	
		  		var data = JSON.parse(res);
		  		if(data.sucess){
		  			layer.msg(data.message,{icon: 7});
		  		}else{
		  			layer.msg(data.message,{icon: 7});
		  		}
		  	  })
		  });;
	});
});

function add(){
	//页面层
    index = layer.open({
        type: 1,
        title:'Add',
        skin: 'layui-layer-rim', //加上边框
        area: [window_width, window_height], //宽高
        content: editWindow.html()  //调到新增页面
    });
    var form = layui.form;
	form.render();
}


function search(){
	var qryJson = $("#gridQry").serializeJson();
	layui.table.reload('grid', {
		        page: {
		          curr: 1 //重新从第 1 页开始
		        }
		        ,where: {
		            qry: JSON.stringify(qryJson)
		        }
		      }, 'data');
    return false;
}

function gridInit(){
	layui.use('table', function(){
		  //$('#editWindow').hide();
		  var table = layui.table;
		  var tableDate = $('#grid'); // 找到目标表格

		  tableIns = table.render({
		     elem: '#grid'
		    ,id:'grid'
		    ,url:getAction()+'?method=queryList'
			,method:"POST"
		    ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		    ,cols: gridDefine
		    ,toolbar: true
		    ,totalRow: true
		    ,page: {theme: '#1e9fff'}
		    ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
		      return {
		        "code": '0', //解析接口状态
		        "msg": '', //解析提示文本
		        "count": res.count, //解析数据长度
		        "data": res.data //解析数据列表
		      };
		    }
		  	,toolbar: '#toolbarGrid'

		    ,done: function(res, curr, count){
		      //$('#grid').next().find('.layui-table-body').find("table" ).find("tbody").children("tr").on('dblclick',function(){
		         // var id = JSON.stringify($('#grid').next().find('.layui-table-body').find("table").find("tbody").find(".layui-table-hover").data('index'));
		          //var obj = res.data[id];
		          //console.log(obj);
		          //gridOnDblclick(obj);
		      //})
		    }
		  });
		  
		  //监听行单击事件（单击事件为：row）
		  table.on('rowDouble()', function(obj){
		    var data = obj.data;
		    gridOnDblclick(data);
		   // layer.alert(JSON.stringify(data), {
		   //   title: '当前行数据：'
		   // });
		    //标注选中样式
		    obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
		  });
		  
		//监听工具条
		  table.on('toolbar(grid)', function(obj){
		      console.log(obj);	
		      var checkStatus = table.checkStatus(obj.config.id);
		      switch(obj.event){
		        case 'getCheckData':
		          var data = checkStatus.data;
		          layer.alert(JSON.stringify(data));
		        break;
		        case 'getCheckLength':
		          var data = checkStatus.data;
		          layer.msg('选中了：'+ data.length + ' 个');
		        break;
		        case 'isAll':
		          layer.msg(checkStatus.isAll ? '全选': '未全选');
		        break;
		        
		        //自定义头工具栏右侧图标 - 提示
		        case 'LAYTABLE_TIPS':
		          layer.alert('这是工具栏右侧自定义的一个图标按钮');
		        break;
		      };
		    });
	});
}



function gridOnDblclick(obj){
	//页面层
    index = layer.open({
        type: 1,
        title:'Edit',
        skin: 'layui-layer-rim', //加上边框
        area: [window_width, window_height], //宽高
        content: editWindow.html()  //调到新增页面
    });
	
	$.ajax({
		  type: 'GET',
		  url: getAction()+"?method=find&id="+obj.id,
		  success: function(res){
		  	var jsonData = JSON.parse(res);
		  	console.log(jsonData);
			var form = layui.form;
			form.val('editForm',jsonData);
			form.render();
		  }
	});
}



function getGridQryJson(){
	var d = {};
    var t = $('#gridQry').serializeArray();
    $.each(t, function() {
      d[this.name] = this.value;
    });
    console.log(d);
    return JSON.stringify(d);
}