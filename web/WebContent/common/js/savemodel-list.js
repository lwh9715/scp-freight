(function($){
	$.fn.initList = function(){
		opts = {// 是否开启选项可拖动，选项可双击
			openDrag: true,
			openDblClick: true
		}
		//opts = $.extend({}, opts);
		var selectTitle = $(this);
		var selectTitle = $(this);
		selectTitle.draggable({
			handle:'.list-title',
			opacity: 0.5,
			helper: 'clone'
		}); // 添加拖拽事件

		/**
		 * 单击列表单击: 改变样式
		 */
		var itemClickHandler = function(){
			if($(this).hasClass('selected-item')){
				$(this).removeClass('selected-item');
			} else{
				$(this).addClass('selected-item');
			}
		}

		/**
		 * 左边列表项移至右边
		 */
		var leftMoveRight = function(){
			if($('.list-body .right-box').children().is('.selected-item')){//如果右边存在选择的就插入到所选的下面
				$(this).next().addClass('selected-item');//标记下一个被选择
				selectTitle.find('.list-body .right-box').find('.selected-item').after($(this).removeClass('selected-item'));
			}else{//如果右边不存在选择的就插入到右边最下面
				$(this).next().addClass('selected-item');//标记下一个被选择
				selectTitle.find('.list-body .right-box').append($(this).removeClass('selected-item'));
			}
			initItemEvent();
		}

		/**
		 * 右边列表项移至左边
		 */
		var rightMoveLeft = function(){
			$(this).next().addClass('selected-item');//标记下一个被选择
			selectTitle.find('.list-body .left-box').append($(this).removeClass('selected-item'));
			initItemEvent();
		}

		/**
		 * 初始化列表项选择事件
		 */
		function initItemEvent(){
			// 左边列表项单击、双击事件
			selectTitle.find('.list-body .left-box').find('.item').unbind('click');
			selectTitle.find('.list-body .left-box').find('.item').unbind('dblclick');
			selectTitle.find('.list-body .left-box').find('.item').each(function(){
				$(this).on("click", itemClickHandler);
				if(!!opts.openDblClick){
					$(this).on('dblclick', leftMoveRight);	
				}
			});

			// 右边列表项单击、双击事件
			selectTitle.find('.list-body .right-box').find('.item').unbind('click');
			selectTitle.find('.list-body .right-box').find('.item').unbind('dblclick');
			selectTitle.find('.list-body .right-box').find('.item').each(function(){
				$(this).on('click', itemClickHandler);
				if(!!opts.openDblClick){
					$(this).on('dblclick', rightMoveLeft);
				}
			});
		}

		/**
		 * 获取选择的值
		 * @return json数组
		 */
		function getSelectedValue(){
			var rightBox = selectTitle.find('.list-body .right-box');
			var itemValues = [];
			var itemValue;

			rightBox.find('.item').each(function(){
				itemValue = {};
				itemValue[$(this).attr('data-id')] = $(this).text();
				itemValues.push(itemValue);
			});

			for(var i = 0; i < itemValues.length; i++){
				console.log(itemValues[i]);
			}

			return itemValues;
		}

		/**
		 * 初始化添加、移除、获取值按钮事件
		 */
		function initBtnEvent(){
			var btnBox = selectTitle.find('.list-body .center-box');
			var leftBox = selectTitle.find('.list-body .left-box');
			var rightBox = selectTitle.find('.list-body .right-box');

			// 添加一项
			btnBox.find('.add-one').on('click', function(){
				rightBox.append(leftBox.find('.selected-item').removeClass('selected-item')); // 触发双击事件
			});
			// 添加所有项
			btnBox.find('.add-all').on('click', function(){
				rightBox.append(leftBox.find('.item').removeClass('selected-item'));
			});
			// 移除一项
			btnBox.find('.remove-one').on('click', function(){
				leftBox.append(rightBox.find('.selected-item').removeClass('selected-item')); // 触发双击事件
			});
			// 移除所有项
			btnBox.find('.remove-all').on('click', function(){
				leftBox.append(rightBox.find('.item').removeClass('selected-item'));
			});

			//selectTitle.find('.list-footer').find('.selected-val').on('click',getSelectedValue);
		}

		initItemEvent();
		initBtnEvent();
		if(!!opts.openDrag){
			$('.item-box').sortable({
				placeholder: 'item-placeholder',
				connectWith: '.item-box',
				revert: true
			}).droppable({
				accept: '.item',
				hoverClass: 'item-box-hover',
				drop: function(event, ui){
					setTimeout(function(){
						ui.draggable.removeClass('selected-item');
					}, 500);
				}
			}).disableSelection();
		}
	}
})($);

function showALLColumn(grid,gridid){//将影藏的显示在左边，显示的显示在右边
	$(".item-box.right-box").empty();
	$(".item-box.left-box").empty();
	var value = "";
	var key = "";
	var config = grid.getSelectionModel().grid.events.columnmove.obj.plugins[0].grid.colModel.config;
	//console.log(config[0].header);
	for(var i=0;i<config.length;i++) {
		key = config[i].dataIndex;
		value = config[i].header;
		//console.log(key);
		//if(value.match(/[a-z]/g)==null){//去除不含英文字符（小写）的
		if(value.indexOf('id')==-1){//去除包含id的
			if(!config[i].hidden) {
				$(".item-box.right-box").append("<span class='item' data-id='"+key+"'>"+value+"</span>"); ;//显示的显示在右边
			} else {
				$(".item-box.left-box").append("<span class='item' data-id='"+key+"'>"+value+"</span>"); ;//影藏的显示在左边
			}
		}
	}
}

function showALLColumna(grid,gridid){
	var config = grid.getSelectionModel().grid.events.columnmove.obj.plugins[0].grid.colModel.config;
	var value2 = "";
	var key2 = "";
	var width2 = "";
	var ishidden2 = "";
	
	for(var i=1;i<config.length;i++) {
		$('.list-body').find('.right-box').find('.item').each(function(){//遍历右边选择的
			console.log(config[i].dataIndex+'///////////////////////'+$(this).attr("data-id"));
			if($(this).attr("data-id")==config[i].dataIndex) {
				ishidden3 = "N";//显示
				return false;
			} else {
				ishidden3 = "Y";//隐藏
			}
		});
		if(ishidden3=='Y'){
			key2 += config[i].dataIndex+",";
			value2 += config[i].header +",";
			if(typeof config[i].width != 'undefined') {
	    		width2 += config[i].width + ",";
			} else {
				width2 += 100 + ",";
			}
			ishidden2 += "Y" + ",";
		}
	}
	
	$('.list-body').find('.right-box').find('.item').each(function(){//遍历右边选择的
		value2 = value2 + $(this).text() + ",";
		key2 = key2 + $(this).attr("data-id") + ",";
		for(var i=0;i<config.length;i++) {//查找对应的宽度和是否影藏
			if($(this).attr("data-id")==config[i].dataIndex){
				if(typeof config[i].width != 'undefined') {
		    		width2 += config[i].width + ",";
				} else {
					width2 += 100 + ",";
				}
				if(value2.indexOf(config[i].header)!=-1) {
					ishidden2 += "N" + ",";//显示
				} else {
					ishidden2 += "Y" + ",";//隐藏
				}
			}
		}
	});
	value2=value2.substring(0,value2.length-1);
	key2=key2.substring(0,key2.length-1);
	width2=width2.substring(0,width2.length-1);
	ishidden2=ishidden2.substring(0,ishidden2.length-1);
//	$('.list-body').find('.left-box').find('.item').each(function(){//遍历
//		value2 = value2 + $(this).text() + ",";
//		key2 = key2 + $(this).attr("data-id") + ",";
//		for(var i=0;i<config.length;i++) {//查找对应的宽度和是否影藏
//			if($(this).attr("data-id")==config[i].dataIndex){
//				if(typeof config[i].width != 'undefined') {
//		    		width2 += config[i].width + ",";
//				} else {
//					width2 += 100 + ",";
//				}
//				ishidden2 += "Y" + ",";//在左边没被选择的全部隐藏
//			}
//		}
//	});
//	
	//console.log(value2);
	saveGridUserDef.addParam("gridid",gridid);
	saveGridUserDef.addParam("colkey",key2);
	saveGridUserDef.addParam("colwidth",width2);
	saveGridUserDef.addParam("ishidden",ishidden2);
	saveGridUserDef.submit();
}

function findColmn(){//查找并定位到对应行
	//$("span").css("background-color","transparent");//先清空所有背景色
	$('#selectTitle').initList();
	var findValue = findValueJs.getValue();
	$("span:contains('"+findValue+"')").css("background-color","#40E0D0").offset().top;//设置背景色
	var container = $(".item-box.left-box");
	var containerright = $(".item-box.right-box");
    var scrollTo = $("span:contains('"+findValue+"')");
	container.scrollTop(
		    scrollTo.offset().top - container.offset().top + container.scrollTop()//定位到指定位置
		);
	containerright.scrollTop(
		    scrollTo.offset().top - container.offset().top + container.scrollTop()//定位到指定位置
		);
}

