<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <meta name="viewport" content="width=device-width, initial-scale=1.0">
 <meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Dash Board</title>

	<link href="res/bootstrap.min.css" rel="stylesheet">
    <link href="res/font-awesome.css" rel="stylesheet">

    <!-- Toastr style -->
    <link href="res/toastr.min.css" rel="stylesheet">

    <!-- Gritter -->
    <link href="res/jquery.gritter.css" rel="stylesheet">

    <link href="res/animate.css" rel="stylesheet">
    <link href="res/style.css" rel="stylesheet">
    <link href="res/easyui.css" rel="stylesheet" />
	
</head>
<style type="text/css">
	.ui-stepProcess{margin-top:6px;inline-block;float:left;width:10px;height:5px;background:#15ec7f;top:0;left:0}
	.arrow-right{inline-block;float:left; width: 0;height: 0;border-top: 10px dashed transparent;border-bottom: 10px dashed transparent;border-left: 9px solid #15ec7f;font-sie: 0;}
</style>
<body>

<%
 String userid = request.getParameter("userid");
 String usercode = request.getParameter("usercode");
 String language = request.getParameter("language");
 %>

	 <div class="wrapper wrapper-content animated fadeIn">
 		<div class="row">
            <div class="col-sm-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <h3 style="display: inline;" id="Kanban">看板 </h3><small id="details">消息详情</small><a class="btn btn-outline btn-success" onclick="init();" id="refrash"><i class="fa fa-refresh"></i>&nbsp;刷新</a>
                    </div>
                    <div class="ibox-content">
                        <div class="row">
                            <div id="processeding" class="col-sm-12">
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        		海运出口
                                    </div>
                                    <div class="panel-body pre-scrollable">
					                     <div style="width:200px;float:left;">
	                                    	<h4>待处理任务：</h4>
					                        <ul id="" class="list-group clear-list m-t">
					                            <li class="list-group-item fist-item">
					                                <span class="label label-success">1</span> Please contact me
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-info">2</span> Sign a contract
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">3</span> Open new shop
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-default">4</span> Call back to Sylvia
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">5</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">6</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">7</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">8</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">9</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">10</span> Write a letter to Sandra
					                            </li>
					                        </ul>
					                     </div>
					                     <div style="width:200px;float:left;">
	                                    	<h4>待处理任务：</h4>
					                        <ul id="" class="list-group clear-list m-t">
					                            <li class="list-group-item fist-item">
					                                <span class="label label-success">1</span> Please contact me
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-info">2</span> Sign a contract
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">3</span> Open new shop
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-default">4</span> Call back to Sylvia
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">5</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">6</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">7</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">8</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">9</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">10</span> Write a letter to Sandra
					                            </li>
					                        </ul>
					                     </div>
                                    </div>
                                </div>
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        		海运出口
                                    </div>
                                    <div class="panel-body pre-scrollable">
					                     <div style="width:200px;float:left;">
	                                    	<h4>待处理任务：</h4>
					                        <ul id="" class="list-group clear-list m-t">
					                            <li class="list-group-item fist-item">
					                                <span class="label label-success">1</span> Please contact me
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-info">2</span> Sign a contract
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">3</span> Open new shop
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-default">4</span> Call back to Sylvia
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">5</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">6</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">7</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">8</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">9</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">10</span> Write a letter to Sandra
					                            </li>
					                        </ul>
					                     </div>
                                    </div>
                                </div>
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        		海运出口
                                    </div>
                                    <div class="panel-body pre-scrollable">
					                     <div style="width:200px;float:left;">
	                                    	<h4>待处理任务：</h4>
					                        <ul id="" class="list-group clear-list m-t">
					                            <li class="list-group-item fist-item">
					                                <span class="label label-success">1</span> Please contact me
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-info">2</span> Sign a contract
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">3</span> Open new shop
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-default">4</span> Call back to Sylvia
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">5</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">6</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">7</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">8</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">9</span> Write a letter to Sandra
					                            </li>
					                            <li class="list-group-item">
					                                <span class="label label-primary">10</span> Write a letter to Sandra
					                            </li>
					                        </ul>
					                     </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    
            
            <!-- Mainly scripts -->
    <script src="res/jquery-2.1.1.js"></script>
    <script src="res/bootstrap.min.js"></script>
    <script src="res/jquery.metisMenu.js"></script>
    <script src="res/jquery.slimscroll.min.js"></script>

    <!-- Flot -->
    <script src="res/jquery.flot.js"></script>
    <script src="res/jquery.flot.tooltip.min.js"></script>
    <script src="res/jquery.flot.spline.js"></script>
    <script src="res/jquery.flot.resize.js"></script>
    <script src="res/jquery.flot.pie.js"></script>

    <!-- Peity -->
    <script src="res/jquery.peity.min.js"></script>
    <script src="res/peity-demo.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="res/inspinia.js"></script>
    <script src="res/pace.min.js"></script>

    <!-- jQuery UI -->
    <script src="res/jquery-ui.min.js"></script>

    <!-- GITTER -->
    <script src="res/jquery.gritter.min.js"></script>

    <!-- Sparkline -->
    <script src="res/jquery.sparkline.min.js"></script>

    <!-- Sparkline demo data  -->
    <script src="res/sparkline-demo.js"></script>

    <!-- ChartJS-->
    <script src="res/Chart.min.js"></script>

    <!-- Toastr -->
    <script src="res/toastr.min.js"></script>

	<script src="res/jquery.easyui.min.js" type="text/javascript"></script>
	<script src="res/easyui-lang-zh_CN.js" type="text/javascript"></script>
	
	<script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>  
	
    <script>
        $(document).ready(function() {
        	console.log(<%=userid%>);
        	init();
        	jQuery.i18n.properties({
	            name : 'strings', //资源文件名称
	            path : '/scp/common/i18n/', //资源文件路径
	            mode : 'map', //用Map的方式使用资源文件中的值
	            language : '<%=language%>',
	            callback : function() {//加载成功后设置显示内容
	               $('#Kanban').html($.i18n.prop('看板'));
	               $('#details').html($.i18n.prop('消息详情'));
	               $('#refrash').html($.i18n.prop('刷新'));
	            }
	        });
        });
        
        function drawline(){
        	$('.panel-body.pre-scrollable h4').wrap("<div style='float:left;'></div>");
        	$('.panel-body.pre-scrollable h4').parent().after("<div class='ui-stepProcess'></div><div class='arrow-right'></div>");
        	$('.panel-body.pre-scrollable h4').css("display","inline");
        	$('.ui-stepProcess').each(
					function() {
						$(this).width(130-($(this).parent().find('h4').width()));
					}
			)
        }
        
        var html = "";
        function init(){
        	html = "";
        	$("#processeding").empty();
        	$.ajax({
               type: "GET",  //提交方式  
               dataType: "html", //数据类型  
               //async :false,//异步：false（否则不能访问全局变量） 
			   url:'/scp/service?src=webServer&action=commonQueryByXml&sqlId=pages.main.showmoremsg.filedata.grid.page&qry=userid ='+"'"+'<%=userid%>'+"'",
               beforeSend: function () {
               		$('.fa.fa-refresh').addClass("fa-spin");
               },
               complete: function () {
		 			drawline();
		 			$('.fa.fa-refresh').removeClass("fa-spin");
	           },
               success: function (jsonData) { //提交成功的回调函数  
                   if(jsonData == null || jsonData == "")return;
                   if(typeof(jsonData)==="string"){
							jsonData = JSON.parse(jsonData);
					}
				   if(jsonData) {
				   		//console.dir(jsonData);
				   		var k = 1;
			        	for (var i = 0; i < jsonData.length; i++) {
			        		if(i>0&&jsonData[i].code==jsonData[i-1].code){
			        			
			        		}else{
			        			k = k+1;
								if(k%2==0){
					        		html+='<div class="panel panel-success">';
					        	}else{
					        		html+='<div class="panel panel-primary">';
					        	}
					            html+= '<div class="panel-heading">'
					            html+=     		jsonData[i].namec;
					            html+= '</div>'
					            html+= '<div class="panel-body pre-scrollable">'
					        }
				            //$.ajax({
					               //type: "GET",  //提交方式  
					               //dataType: "html", //数据类型 
					               //async :false,//异步：false（否则不能访问全局变量） 
								   //url:'/scp/service?src=webServer&action=commonQueryByXml&sqlId=pages.main.showmoremsg.temp.grid.page&qry='+jsonData[i].code,
					               //success: function (jsonData2) { //提交成功的回调函数  
				                   	 	//if(typeof(jsonData2)==="string"){
											//jsonData2 = JSON.parse(jsonData2);
									 	//}
									 	//if(jsonData2 == null || jsonData2 == ""||jsonData2.label=="")return;
									 	//if(jsonData2) {
						               		//for (var i = 0; i < jsonData2.length; i++) {
							               		html+=     	'<div style="width:140px;float:left;">'
										        html+=              '<h4>'+jsonData[i].statusc+'</h4>'
											    html+=            	'<ul id="" class="list-group clear-list m-t">';
											    	$.ajax({
											    	//console.log(jsonData[i].statusc+"1111111111");
											               type: "GET",  //提交方式  
											               dataType: "html", //数据类型 
											               async :false,//异步：false（否则不能访问全局变量） 
														   url:'/scp/service?src=webServer&action=commonQueryByXml&sqlId=pages.main.showmoremsg.tempnos.grid.page&qry='+jsonData[i].code+'&qry2='+jsonData[i].id+'&qry3='+<%=userid%>,
											               success: function (jsonData3) { //提交成功的回调函数
											               		if(jsonData3 == null || jsonData3 == ""||jsonData3.label=="")return;
											               		//console.dir(jsonData3);
										                   	 	if(typeof(jsonData3)==="string"){
																	jsonData3 = JSON.parse(jsonData3);
															 	}
															 	if(jsonData3) {
															 			var j=1;
															 			for (var i = 0; i < jsonData3.length; i++) {
															 				html+=               '<li class="list-group-item fist-item">';
															 				if(jsonData3[i].isair == false&&jsonData3[i].isshipping == true){
																 				if(i%2==0){
																	        		html+=           '<span class="label label-success">'+j+'</span>'+'<a class="btn btn-outline btn-link btn-xs" href ="/scp/pages/module/ship/jobsedit.aspx?id='+jsonData3[i].id+'" target="_blank">'+jsonData3[i].nos+'</a>';
																	        	}else{
																	        		html+=           '<span class="label label-info">'+j+'</span>'+'<a class="btn btn-outline btn-link btn-xs" href ="/scp/pages/module/ship/jobsedit.aspx?id='+jsonData3[i].id+'" target="_blank">'+jsonData3[i].nos+'</a>';
																	        	}
																	        }else{
																	        	if(i%2==0){
																	        		html+=           '<span class="label label-success">'+j+'</span>'+'<a class="btn btn-outline btn-link btn-xs" href ="/scp/pages/module/air/jobsedit.aspx?id='+jsonData3[i].id+'" target="_blank">'+jsonData3[i].nos+'</a>';
																	        	}else{
																	        		html+=           '<span class="label label-info">'+j+'</span>'+'<a class="btn btn-outline btn-link btn-xs" href ="/scp/pages/module/air/jobsedit.aspx?id='+jsonData3[i].id+'" target="_blank">'+jsonData3[i].nos+'</a>';
																	        	}
																	        }
																		    j=j+1;
																		    html+=               '</li>';
															 			}
																}
														    }
											       })	
											    html+=            '</ul>'
											    html+=         '</div>'
											//}
										//}
							       //}
							 //});
							 
							if(i<jsonData.length-1&&jsonData[i+1].code==jsonData[i].code){
			        			
			        		}else{
						        html+=     '</div>'
					         	html+=' </div>'
					        }
				         }
				         $("#processeding").append(html);
		         	}
				}
		 	});
        }
    </script>
</body>
</html>