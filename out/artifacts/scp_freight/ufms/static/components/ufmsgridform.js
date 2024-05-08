/**
  扩展一个form+grid模块
**/      
layui.define(['form','table','tablePlug','ufmsGrid'],function(exports){
	const version = '0.0.1';
	const modelName = 'ufmsGridForm'; // 组件名称
	const $ = layui.$,form=layui.form,table=layui.table, layer = layui.layer,ufmsGrid = layui.ufmsGrid;
	var obj={version:version};
	var windowWH = ['700px','500px']
	var gridOnDblclick = async function(data){
		let jsonData = await doAjax({url:`/${action}?method=find&id=${data.id}`});
		layer.open({
	        type: 1,
	        title:'Edit',
	        area: obj.windowWH,
	        content: $("#editWindow"),  //编辑页面
	        maxmin :true
	    });
		form.val('editForm',JSON.parse(jsonData.data));
	};
	obj={
		gridOnDblclick:gridOnDblclick //行双击事件
		,windowWH:windowWH  //宽高
	}
	$.extend(obj, ufmsGrid);
	table.on('rowDouble(gridfilter)', function(obj){//行双击事件
		gridOnDblclick(obj.data)
	});
	//输出接口
	exports(modelName, obj);
});