//此组件定义公共的方法属性
layui.define(['table'], function (exports) {
	var version = '0.0.1';
	var modelName = 'tablePlug'; // 组件名称
	var pathTemp = layui.cache.modules[modelName] || ''; // 正常情况下不会出现未定义的情况
	var tablePlug = {
		version: version
	}
	// 对table的全局config进行深拷贝继承
	tablePlug.set = function (config) {
	  $.extend(true, table.config, config || {});
	};
	
	//输出接口
	exports(modelName, tablePlug);
});