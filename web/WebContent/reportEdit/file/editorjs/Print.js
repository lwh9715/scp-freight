/**
 * @author 无量

 */	
//new T
function Print(Config){
	var Property = {id:"",jdatasource:{},template:null};
	for(p in Property)
	this[p]=Config==null||(typeof Config[p]=='undefined')?Property[p]:Config[p]
	this.parent=document.getElementById(this.id);
	this.printelement=null;
	this.container=null;
	//20161118修改,设置cntdesc下对齐
	if(typeof(this.jdatasource.ORG[0].cntdesc)=="string"){
		this.jdatasource.ORG[0].cntdesc = "<p style='position:absolute;bottom:0px;padding:0px;margin:0px;'>" + this.jdatasource.ORG[0].cntdesc + "</p>";
	}
	this.Init();
};
Print.prototype.ResetPosition=function(){
	with(this)
	{
		var top=container.top-template.container.top;
		var left=container.left-template.container.left;
		for(var i=0;i<this.template.elements.length;i++)
		{
				var element=this.template.elements[i];
				element.top=parseFloat(element.top)+top+'px';
				element.left=parseFloat(element.left)+left+'px';
		}
	}
};
Print.prototype.Init=function(n)
{
	var obj=this;
	with(this)
	{

		var content=document.createElement('div');
		content.setAttribute("style","top:50px");
		
		content.setAttribute("id","content");
			
		content.style.width = "827px";//1179 827
		content.style.height = "1169px";//1667 1169
		
		content.style.margin="0 auto";
		content.innerHTML="";
		content.style.backgroundImage=template.backgroundImage;
		content.style.backgroundRepeat="no-repeat";
		printelement=content;
		parent.appendChild(printelement);
		container=new LContainer(printelement);
		ResetPosition();
		for(var i=0;i<this.template.elements.length;i++)
		{
			var element=this.template.elements[i];
			var node=new ElementNode('',element);
			var Hdiv=node.CreatePrint(jdatasource);
			printelement.appendChild(Hdiv);
			
		}
		
	}
};