<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>UFMS IM</title>
		<link rel="stylesheet" href="css/amazeui.min.css" />
		<link rel="stylesheet" href="css/main.css" />
		
	</head>
	<body>
	
	
	
	<%	
	String userid = request.getParameter("userid");		
	
	String fromUserid = request.getParameter("touserid");
	String linktype = request.getParameter("linktype");
	String refid = request.getParameter("refid");
	String refno = request.getParameter("refno");
	
	if(fromUserid == null || fromUserid.equals("") || fromUserid.equals("null"))fromUserid="0";
	if(linktype == null || linktype.equals("") || linktype.equals("null"))linktype="";
	if(refid == null || refid.equals("") || refid.equals("null"))refid="0";
	if(refno == null || refno.equals("") || refno.equals("null"))refno="";
		
	String usernamec = request.getParameter("usernamec");
	String csno = request.getParameter("csno");
	String isSaas = request.getParameter("isSaas");
	String language = request.getParameter("language");
	%>
	
	
		<div class="box">
			<div class="wechat">
				
				<div class="sidestrip">
					<div class="am-dropdown" data-am-dropdown>
						<!--头像插件-->
						<div class="own_head am-dropdown-toggle"></div>
						<div class="am-dropdown-content">
					    	<div class="own_head_top">
					    		<div class="own_head_top_text">
					    			<p class="own_name">彭于晏丶plus<img src="images/icon/head.png" alt="" /></p>
					    			<p class="own_numb">微信号：jsk8787682</p>
					    		</div>
					    		<img src="images/own_head.jpg" alt="" />
					    	</div>
					    	<div class="own_head_bottom">
					    		<p><span>地区</span>江西 九江</p>
					    		<div class="own_head_bottom_img">
					    			<a href=""><img src="images/icon/head_1.png"/></a>
					    			<a href=""><img src="images/icon/head_2.png"/></a>
					    		</div>
					    	</div>
					  	</div>
					</div>
					<!--三图标-->
				  	<div class="sidestrip_icon">
				  		<a id="si_1" style="background: url(images/icon/head_2_1.png) no-repeat;"></a>
				  		<a id="si_2"></a>
				  		<!--<a id="si_3"></a>
				  	--></div>
				  	
				  	<!--底部扩展键-->
				  	<div id="doc-dropdown-justify-js">
				  		<div class="am-dropdown" id="doc-dropdown-js" style="position: initial;">
					  		<div class="sidestrip_bc am-dropdown-toggle"></div>
					  	    <ul class="am-dropdown-content" style="">
							    <li>
							    	<a href="#" data-am-modal="{target: '#doc-modal-1', closeViaDimmer: 0, width: 400, height: 225}">意见反馈</a>
							    	<div class="am-modal am-modal-no-btn" tabindex="-1" id="doc-modal-1">
									  <div class="am-modal-dialog">
									    <div class="am-modal-hd">Modal 标题
									      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
									    </div>
									    <div class="am-modal-bd">
									      UFMS
									    </div>
									  </div>
									</div>
							    </li>
							    
							    <li><a href="#">备份与恢复</a></li>
							    <li><a href="#">设置</a></li>
						    </ul>
					    </div>	
					</div>	
				</div>
				
				<!--聊天列表-->
				<div class="middle on">
					<div class="wx_search">
						<input type="text" placeholder="搜索"/>
						<button>+</button>
					</div>
					<div class="office_text">
						<ul class="user_list">
							<li class="user_active">
								<div class="user_head"><img src="images/head/15.jpg"/></div>
								<div class="user_text">
									<p class="user_name">AAA</p>
									<p class="user_message"></p>
								</div>
								<div class="user_time"></div>
							</li>
							<li>
								<div class="user_head"><img src="images/head/15.jpg"/></div>
								<div class="user_text">
									<p class="user_name">AAA</p>
									<p class="user_message"></p>
								</div>
								<div class="user_time"></div>
							</li>
						</ul>
					</div>	
				</div>
			    
			    <!--好友列表-->
				<div class="middle">
					<div class="wx_search">
						<input type="text" placeholder="搜索"/>
						<button>+</button>
					</div>
					<div class="office_text">
						<ul class="friends_list">
							<li>
								<p>新的朋友</p>
								<div class="friends_box">
									<div class="user_head"><img src="images/head/1.jpg"/></div>
									<div class="friends_text">
										<p class="user_name">新的朋友</p>
									</div>
								</div>
							</li>
						</ul>
					</div>	
				</div>
				
				<!--聊天窗口-->
				<div class="talk_window">
					<div class="windows_top">
						<div class="windows_top_box">
							<span>早安无恙</span>
							<ul class="window_icon">
								<li><a href=""><img src="images/icon/icon7.png"/></a></li>
								<li><a href=""><img src="images/icon/icon8.png"/></a></li>
								<li><a href=""><img src="images/icon/icon9.png"/></a></li>
								<li><a href=""><img src="images/icon/icon10.png"/></a></li>
							</ul>
							<div class="extend" class="am-btn am-btn-success" data-am-offcanvas="{target: '#doc-oc-demo3'}"></div>
							<!-- 侧边栏内容 -->
							<div id="doc-oc-demo3" class="am-offcanvas">
							    <div class="am-offcanvas-bar am-offcanvas-bar-flip">
								    <div class="am-offcanvas-content">
								    	<p><a href="http://music.163.com/#/song?id=385554" target="_blank">网易音乐</a></p>
								    </div>
							    </div>
							</div>
						</div>
					</div>
					
					<!--聊天内容-->
					<div class="windows_body">
						<div class="office_text" style="height: 100%;">
							<ul class="content" id="chatbox">
								<li class="me"><img src="images/own_head.jpg" title="金少凯"><span>疾风知劲草，板荡识诚臣</span></li>
								<li class="other"><img src="images/head/15.jpg" title="张文超"><span>勇夫安知义，智者必怀仁</span></li>
							</ul>
						</div>
					</div>
					
					<div class="windows_input" id="talkbox">
						<div class="input_icon">
							<a href="javascript:;"></a>
							<a href="javascript:;"></a>
							<a href="javascript:;"></a>
							<a href="javascript:;"></a>
							<a href="javascript:;"></a>
							<a href="javascript:;"></a>
						</div>
						<div class="input_box">
							<textarea name="" rows="" cols="" id="input_box"></textarea>
							<button id="send">发送（S）</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	<script src="http://www.jq22.com/jquery/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="js/amazeui.min.js"></script>
	<script type="text/javascript" src="js/zUI.js"></script>
	<script type="text/javascript" src="js/wechat.js"></script>
	
	<script type="text/javascript"><!--
		//三图标
		window.onload=function(){
			function a(){
				var si1 = document.getElementById('si_1');
				var si2 = document.getElementById('si_2');
				//var si3 = document.getElementById('si_3');
				si1.onclick=function(){
					si1.style.background="url(images/icon/head_2_1.png) no-repeat"
					si2.style.background="";
					//si3.style.background="";
				};
				si2.onclick=function(){
					si2.style.background="url(images/icon/head_3_1.png) no-repeat"
					si1.style.background="";
					//si3.style.background="";
				};
				//si3.onclick=function(){
				//	si3.style.background="url(images/icon/head_4_1.png) no-repeat"
				//	si1.style.background="";
				//	si2.style.background="";
				//};
			};
			function b(){
				var text = document.getElementById('input_box');
				var chat = document.getElementById('chatbox');
				var btn = document.getElementById('send');
				var talk = document.getElementById('talkbox');
				btn.onclick=function(){
					if(text.value ==''){
			            alert('不能发送空消息');
			        }else{
						chat.innerHTML += '<li class="me"><img src="'+'images/own_head.jpg'+'"><span>'+text.value+'</span></li>';
						text.value = '';
						chat.scrollTop=chat.scrollHeight;
						talk.style.background="#fff";
						text.style.background="#fff";
					};
				};
			};
			a();
			b();
			
			var userid = <%=userid%>;
			var languagetype = '<%=language%>';
			$('#usernamec').val("<%=usernamec%>");
	       	$('#isSaas').val("<%=isSaas%>");
	       	var csno = <%=csno%>;
	       	var lang = '<%=language%>';
	       	init(userid,csno,lang);
		};
		
		
		function init(userid , _csno,lang){
			curentUserid = userid;
			csno = _csno;
			language = lang;
			
			html = "";
			$('.user_list').empty();
			$.ajax({//获取联系人列表
		       type: "GET",  //提交方式  
		       dataType: "html", //数据类型  
		       contentType:'application/json',
		       async :false,//异步：false（否则不能访问全局变量） 
			   url:'/scp/service?src=webServer&action=commonQueryByXml&sqlId=activemq.index.grid.page&qry=userid ='+"'"+'curentUserid'+"'",
		       beforeSend: function () {
		       			
		       },
		       complete: function () {
		
		       },
		       success: function (jsonData) { //提交成功的回调函数  
		           if(jsonData == null || jsonData == "")return;
		           console.log(jsonData);
		           if(typeof(jsonData)==="string"){
						jsonData = JSON.parse(jsonData);
		           }
		           var userListCount = 0;//记录列表总行数
				   if(jsonData) {
					   //系统消息，id为8888
					   for (var i = 0; i < jsonData.length; i++) {
						    html+=	  '<li>'
		                    html+=    '<div class="user_head">'
		                    //console.log(jsonData[i].headimg);
		                    var username = '';
		                    if(language=="ch"){
		                    	username = jsonData[i].namec;
		                    }else{
		                    	username = jsonData[i].namee;
		                    }
	                    	if(jsonData[i].headimg != null&&jsonData[i].headimg!=""){	
	                    		html+=        '<img src="../attachment/'+jsonData[i].headimg+'"></a></img></div>'
	                    	}else{//默认头像
	                    		html+=        '<img src="./images/head/1.jpg"></a></img></div>'		                            
	                    	}
	                    	html+=        '<div class="user_text">'
                    		html+=        '		<p class="user_name">'+username+'</p>'
                    		html+=        '		<p class="user_message">'+'</p>'
                    		html+=        '<div class="user_time"></div>'
		                   
		                    html+='</li>';
		                    userListCount++;
					   		$('.user_list').append(html);
						}
					}
				}
		 	});
		 	
		}
	--></script>
	</body>
</html>
