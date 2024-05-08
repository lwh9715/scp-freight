
function getAction(){
	return getContextPath()+action;
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