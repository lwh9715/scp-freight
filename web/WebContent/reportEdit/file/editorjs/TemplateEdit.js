/**
 * @author 无量
 */
function TemplateEdit(Config){
	var Property = {title:"",jdatasource:{},jTemplates:[]};
	for(p in Property)
	this[p]=Config==null||(typeof Config[p]=='undefined')?Property[p]:Config[p]
	this.datasource=null;
	this.canvas=null;
	this.tools=null;
	datajson=this.jdatasource;
	this.Templates=[];
	this.Init();
};
TemplateEdit.prototype.ResetPosition=function()
{
	this.canvas.ResetPosition();
}
TemplateEdit.prototype.Init=function(n)
{
	var obj=this;
	with(this)
	{
		 tools=new Tools('tool1');
		 datasource=new DataSource(jdatasource);
		 document.body.appendChild(tools.element);
		 var divleft=document.createElement('div');
		 divleft.setAttribute("style","float:left;width:10%;height:827px;");//1667 827
		 var divright=document.createElement('div');
		 divright.setAttribute("style","float:right;width:90%;height:1169px;");//1879 1169
		 divright.setAttribute("id","content");
		 divleft.appendChild(datasource.element);
		 document.body.appendChild(divleft);
		 document.body.appendChild(divright);
		 canvas = new Canvas('content',{ enable:false,minWidth: 0, minHeight: 0, minLeft: 1, minTop: 1, maxLeft: '100%', maxTop: '100%' })
		 canvas.ondragstart = function(isResize) { };
		 canvas.isElement = function(elm)
		 {
		 return true;
		 };
		 canvas.isHandle = function(elm)
		 {
		 return true;
		 };		
		 //加载模板
		 for(var i=0;i<jTemplates.length;i++)
		 {
			 var jtemplate=jTemplates[i];
			 var template=new Template("",jtemplate);
			 obj.Templates.push(template);
			 tools.DropDownTemplate.Add(template.title);

			 if(i==0)
			 {
				 canvas.template=template; 
				 tools.TextPc.SetValue(template.printpc);
				 canvas.ResetLayout();
			 }
		 }
		 //设置模板名称
		tools.DropDownTemplate.span.innerHTML=canvas.template.title;
		 datasource.eventClick=function(e)
		 {
		 		var node=new ElementNode('node1',{backgroundColor:"#ccffff",text:e,color:"green"});
		  		var Hdiv=node.Create();
				canvas.add(Hdiv);
		 };
		 tools.eventNewTemplate=function(e)
		 {
			 var b=true;
			for(var i=0;i< obj.Templates.length;i++)
			{
				if(obj.Templates[i].title==e)
				{
					alert("已存在此模板！");	
					b=false;
					break;
				}
			};
			if(b)
			{
				 var template=new Template(e);
				 template.isedit=true;
				 obj.Templates.push(template);
				 canvas.template=template; 
				 template.container=canvas.container;
				 canvas.ResetLayout();
			 }
  	     };
	 	 tools.eventNewText=function(e)
		 {
	 		var node=new ElementNode('node1',{backgroundColor:"#ccffff",text:e,color:"green"});
		  	var Hdiv=node.Create();
			canvas.add(Hdiv);
  	     };
		 tools.eventNewTable=function(e)
		 {
			 str = [];
			 var tr=[];
			 for(var i=0;i<e.length;i++)
			 {
				 var otd={};
				 otd.text=e[i].text;
				 tr[tr.length]=otd;
			 }
			 var node=new ElementNode('node',{tr:tr});
		  	 var Hdiv=node.Create(str);
			 canvas.add(Hdiv);
  	     };
	 
		tools.eventFontSize=function(e){
			if(canvas.telement)
			{
				canvas.telement.style.fontSize=e;
			}
		};
		tools.eventFontBlod=function(e){
			if(canvas.telement)
			{
				canvas.telement.style.fontWeight=canvas.telement.style.fontWeight=="bold"?"lighter":"bold";
			}
		};
		tools.eventFontItalic=function(e){
			if(canvas.telement)
			{
				canvas.telement.style.fontStyle=canvas.telement.style.fontStyle=="italic"?"normal":"italic";
			}
		};
		tools.eventFontDel=function(e){
			if(canvas.telement)
			{
				canvas.telement.style.textDecoration=canvas.telement.style.textDecoration=="line-through"?"":"line-through";
			}
		};
		tools.eventBackGruodImg=function(e){
			if(canvas.template)
			{
				var url="url('images/"+e+"')";
				canvas.template.backgroundImage=url;
				canvas.control.style.backgroundImage=url;
				canvas.control.style.backgroundRepeat="no-repeat";
				//alert(obj.Templates[0].backgroundImage);
			}
		};
		//清除底图
		tools.eventClearBackGruodImg=function(e){
			if(canvas.template)
			{
				var url="url('')";
				canvas.template.backgroundImage=url;
				canvas.control.style.backgroundImage=url;
				canvas.control.style.backgroundRepeat="no-repeat";
			}
		};
		tools.eventSelectTemplate=function(e){
			var temp;
			for(var i=0;i< obj.Templates.length;i++)
			{
				if(obj.Templates[i].title==e)
				{
					temp=obj.Templates[i];
					break;
				}
			};
			if(temp)
			{
				tools.TextPc.SetValue(temp.printpc);
				temp.isedit=true;
				canvas.template=temp;
				canvas.ResetLayout();
			}
		};
		tools.eventSetTextPc=function(e)
		{
			if(canvas.template)
			{
				canvas.template.printpc=e;
			}
		};
		//保存模版
		tools.eventSaveTemplate=function(e){
			var json=[];
			for(var i=0;i< obj.Templates.length;i++)
			{
				json[json.length]=obj.Templates[i].ToJson();
				//console.log(obj.Templates[i]);
			}
			//console.log(JSON.stringify(json));
			saveTemplete(JSON.stringify(json));
			//alert(JSON.stringify(json));
		};
		//不保存模版
		tools.eventPrintTemplate=function(e){
			var json=[];
			for(var i=0;i< obj.Templates.length;i++)
			{
				json[json.length]=obj.Templates[i].ToJson();
			}
			//console.log(JSON.stringify(json));
			printTemplete(JSON.stringify(json));
			//alert(JSON.stringify(json));
		};
		//另存为模版
		tools.eventSaveTemplateForPrivate=function(text,checkbox){
			//console.log("text:"+text);
			if(typeof(text)=="undefined"||text.trim().length==0){
				alert("模版名称不能为空!");
				return;
			}
			text = text.trim();
			var ispublic = typeof(checkbox)=="undefined" ? false : true;
			//console.log(text+":"+ispublic);
			var json=[];
			for(var i=0;i< obj.Templates.length;i++)
			{
				json[json.length]=obj.Templates[i].ToJson();
			}
			//console.log(JSON.stringify(json));
			saveTemplateForPrivate(JSON.stringify(json),text,ispublic);
		};
		
	}
};