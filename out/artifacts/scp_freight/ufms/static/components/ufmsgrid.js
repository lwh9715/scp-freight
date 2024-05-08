/**
  扩展一个grid模块
**/      
layui.define(['form','table','tablePlug','soulTable'],function(exports){
	var version = '0.0.1';
	var modelName = 'ufmsGrid'; // 组件名称
	var $ = layui.$,form=layui.form,table=layui.table, layer = layui.layer,soulTable = layui.soulTable;
	var obj = {
		gridcols:[[]]
		,gridId:'grid'
		,tableIns :async function(gridDefine){
			const _obj = this//此处定义是防止调用this指向问题
			_obj.gridcols = gridDefine
			await _obj.getGridcols(_obj.gridcols[0]);
			console.log(_obj.gridcols)
			var mytableIns = table.render({
			     elem: '#'+_obj.gridId
			    ,id:'grid'
			    ,url:`${getAction()}?method=queryList`
				,method:"POST"
			    ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
			    ,cols: _obj.gridcols
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
			  		$('.layui-table-header tr').css({'background-color': '#1e9fff', 'color': 'white'});
			  		soulTable.render(this)
			    }
			});
			//头工具栏事件
			table.on('toolbar(gridfilter)', function(obj){
		      var checkStatus = table.checkStatus(obj.config.id);
		      switch(obj.event){
		      	case 'refresh'://刷新
		      	  console.log("刷新");
		      	  table.reload('grid', {
			        page: {
			          curr: 1 //重新从第 1 页开始
			        }
			      });
		        break;
		        case 'add'://新增
		          var data = checkStatus.data;
		          var div = $('#editWindow');
		          if(div[0]){//如果编辑表单存在就弹出编辑表单
		        	  layer.open({
		      	        type: 1,
		      	        title:'Edit',
		      	        area: obj.getWindowWH,
		      	        content: $("#editWindow"),  //调到新增页面
		      	        maxmin :true
		      	      });
		        	  $('form[lay-filter="editForm"]')[0].reset();//初始化编辑表单
		          }
		        break;
		        case 'del'://删除
		          var data = checkStatus.data;
		          //obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
		        break;
		        case 'saveColModel'://设置定制
		        	let cols = mytableIns.config.cols[0]
		        	var savecol = (cols) => _obj.savreCol(cols);
		        	savecol(cols);
		        break
		        case 'reductColModel'://还原定制
		        	var reductCol = () => _obj.reductCol();
		        	reductCol();
		        break
		      };
		    });
			//grid查询
			form.on('submit(queryGrid)',function(data){
				var datajsonstring = JSON.stringify(data.field);
				table.reload('grid', {
			        page: {
			          curr: 1
			        }
			        ,where: {
			            qry: datajsonstring
			        }
			    });
			});
		}
		,savreCol:async function(cols){//保存表格列的定制
			let colkey = "";
        	let colwidth = "";
        	let ishidden = "";
			cols.forEach(v=>{
				if(v.type === "numbers"){
					return
				}
				colkey += v.field + ',';
				colwidth += v.width + ',';
				ishidden += v.hide + ',';
			})
			let obj = new Object();
			obj.colkey = colkey.substr(0,colkey.length-1);
			obj.colwidth = colwidth.substr(0,colwidth.length-1);
			obj.ishidden = ishidden.substr(0,ishidden.length-1);
			console.log(obj);
			let jsonData = await doAjax({url:`/${action}?method=savecolmodel`,data:JSON.stringify(obj),method:'POST'});
		}
		,reductCol: async function(){//还原表格的定制
			let jsonData = await doAjax({url:`/${action}?method=reductcolmodel`});
			this.tableReload('grid')
		}
		//传入列表数组，去后台查询对应的顺序，列宽，影藏等，返回新的数组，如果后台没有数据就直接返回传入的数组
		,getGridcols: async function(gridcols){
			var jsonData = await doAjax({url:`/${action}?method=applygridgserdef`});
			if(jsonData.data == null || jsonData.data == ''){
				return gridcols;
			}
			let gridgserdef = JSON.parse(jsonData.data);
			var colkeys = gridgserdef.colkey.split(",");
			var colwidths = gridgserdef.colwidth.split(",");
			var ishiddens = gridgserdef.ishidden.split(",");
			gridcols.forEach((elem,index)=>{
				colkeys.forEach((elem2,index2)=>{
					if(elem.field === elem2){
						gridcols[index].width = colwidths[index2];
						gridcols[index].hide = (ishiddens[index2] === 'true'?true:false );
					}
				})
			});
			gridcols.sort((a,b)=>{
				let aindex,bindex
				colkeys.forEach((elem,index)=>{
					if(a.field === elem){
						aindex = index
					}
					if(b.field === elem){
						bindex = index
					}
				})
				console.log(b);
				if(b.type === 'numbers'){
					return 1;
				}
				if(aindex>aindex){
					return 1;
				}
				return -1;
			})
			console.log(gridcols);
		}
		,tableReload: function(gridname){
			table.reload('grid', {
				cols: this.gridcols
		    });
		}
	}
	//输出接口
	exports(modelName, obj);
});