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
    
    <link rel="stylesheet" href="/scp/layui/css/layui.css">

	<style type="text/css">.jqstooltip { position: absolute;left: 0px;top: 0px;visibility: hidden;background: rgb(0, 0, 0) transparent;background-color: rgba(0,0,0,0.6);filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";color: white;font: 10px arial, san serif;text-align: left;white-space: nowrap;padding: 5px;border: 1px solid white;z-index: 10000;}.jqsfield { color: white;font: 10px arial, san serif;text-align: left;}</style>
</head>
<style type="text/css">
	.btn:focus {
         
     }
     .translate{}
</style>
<body>

<%
 String userid = request.getParameter("userid");
 String usercode = request.getParameter("usercode");
 String language = request.getParameter("language");
 String sysbpm = request.getParameter("sysbpm");
 request.getSession().setAttribute("userid",userid);
 %>


<div id="page-wrapper" class="gray-bg dashbard-1" style="min-height: 300px;">
        <div class="row border-bottom">
        <!--<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
            <form role="search" class="navbar-form-custom" action="http://www.jq22.com/demo/web20160509/search_results.html">
                <div class="form-group">
                    <input type="text" placeholder="Search for something..." class="form-control" name="top-search" id="top-search">
                </div>
            </form>
        </div>
            <ul class="nav navbar-top-links navbar-right">
                <li>
                    <span class="m-r-sm text-muted welcome-message">Welcome to INSPINIA+ Admin Theme.</span>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-envelope"></i>  <span class="label label-warning">16</span>
                    </a>
                    <ul class="dropdown-menu dropdown-messages">
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="http://www.jq22.com/demo/web20160509/profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="res/a7.jpg">
                                </a>
                                <div class="media-body">
                                    <small class="pull-right">46h ago</small>
                                    <strong>Mike Loreipsum</strong> started following <strong>Monica Smith</strong>. <br>
                                    <small class="text-muted">3 days ago at 7:58 pm - 10.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="http://www.jq22.com/demo/web20160509/profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="res/a4.jpg">
                                </a>
                                <div class="media-body ">
                                    <small class="pull-right text-navy">5h ago</small>
                                    <strong>Chris Johnatan Overtunk</strong> started following <strong>Monica Smith</strong>. <br>
                                    <small class="text-muted">Yesterday 1:21 pm - 11.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="http://www.jq22.com/demo/web20160509/profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="res/profile.jpg">
                                </a>
                                <div class="media-body ">
                                    <small class="pull-right">23h ago</small>
                                    <strong>Monica Smith</strong> love <strong>Kim Smith</strong>. <br>
                                    <small class="text-muted">2 days ago at 2:30 am - 11.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="http://www.jq22.com/demo/web20160509/mailbox.html">
                                    <i class="fa fa-envelope"></i> <strong>Read All Messages</strong>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell"></i>  <span class="label label-primary">8</span>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        <li>
                            <a href="http://www.jq22.com/demo/web20160509/mailbox.html">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> You have 16 messages
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="http://www.jq22.com/demo/web20160509/profile.html">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="http://www.jq22.com/demo/web20160509/grid_options.html">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="http://www.jq22.com/demo/web20160509/notifications.html">
                                    <strong>See All Alerts</strong>
                                    <i class="fa fa-angle-right"></i>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>


                <li>
                    <a href="http://www.jq22.com/demo/web20160509/login.html">
                        <i class="fa fa-sign-out"></i> Log out
                    </a>
                </li>
                <li>
                    <a class="right-sidebar-toggle">
                        <i class="fa fa-tasks"></i>
                    </a>
                </li>
            </ul>

        </nav>
        </div>
                -->
                
                
       <div style="height: 300px;" class="row  border-bottom white-bg dashboard-header">

                    <div class="col-sm-8 col-md-6 col-lg-4">
                        <!--<h2>Welcome</h2>
                        --><small id="renwu"><span class="translate">待处理任务</span>：</small><a class="btn btn-outline btn-success" onclick="init();" id="refrash"><i class="fa fa-refresh"></i>&nbsp;<span class="translate">刷新</span></a>
                        <ul id="processeding" class="list-group clear-list m-t"><!--
                             <li class="list-group-item fist-item">
                                <span class="pull-right">
                                    09:00 pm
                                </span>
                                <span class="label label-success">1</span> Please contact me
                            </li>
                            <li class="list-group-item">
                                <span class="pull-right">
                                    10:16 am
                                </span>
                                <span class="label label-info">2</span> Sign a contract
                            </li>
                            <li class="list-group-item">
                                <span class="pull-right">
                                    08:22 pm
                                </span>
                                <span class="label label-primary">3</span> Open new shop
                            </li>
                            <li class="list-group-item">
                                <span class="pull-right">
                                    11:06 pm
                                </span>
                                <span class="label label-default">4</span> Call back to Sylvia
                            </li>
                            <li class="list-group-item">
                                <span class="pull-right">
                                    12:00 am
                                </span>
                                <span class="label label-primary">5</span> Write a letter to Sandra
                            </li> 
                        --></ul>
                    </div> 
                    
                    
                    <div class="col-lg-4">
                    	<h3 class="font-bold no-margins">
							<span class="translate">单量年度趋势图</span>
						</h3>
						<div>
							<canvas id="lineChart" height="136"></canvas>
						</div>
					</div>
                   
                   <div class="col-lg-4">
                                <div>
	                                <div>
	                                    <h5 id="notice"><span class="translate">公告</span>：</h5>
	                                    <!--
	                                    <div class="ibox-tools">
	                                        <a class="collapse-link">
	                                            <i class="fa fa-chevron-up"></i>
	                                        </a>
	                                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                                            <i class="fa fa-wrench"></i>
	                                        </a>
	                                        <ul class="dropdown-menu dropdown-user">
	                                            <li><a href="#">Config option 1</a>
	                                            </li>
	                                            <li><a href="#">Config option 2</a>
	                                            </li>
	                                        </ul>
	                                        <a class="close-link">
	                                            <i class="fa fa-times"></i>
	                                        </a>
	                                    </div>
	                                    -->
	                                    <div class="ibox-content no-padding">
                                    		<iframe frameborder="0" style="width: 100%; height: 110px;border: 0;" src="/scp/pages/sysmgr/message/msg.jsp?userid=<%=userid%>&language=<%=language%>"></iframe>
                                		</div>
                                		
                                		<h5>FAQ：</h5>
		                                 <div class="ibox-content no-padding">
		                                		<iframe frameborder="0" style="width: 100%; height: 100px;border: 0;" src="/scp/pages/sysmgr/faq/faq.jsp?language=<%=language%>"></iframe>
		                            	 </div>
	                                </div>
	                            </div>
                     </div>

            </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="wrapper wrapper-content">
                        <div class="row">
                        <div class="col-lg-4">
                            <!--<div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <h5>New data for the report</h5> <span class="label label-primary">IN+</span>
                                    <div class="ibox-tools">
                                        <a class="collapse-link">
                                            <i class="fa fa-chevron-up"></i>
                                        </a>
                                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                            <i class="fa fa-wrench"></i>
                                        </a>
                                        <ul class="dropdown-menu dropdown-user">
                                            <li><a href="#">Config option 1</a>
                                            </li>
                                            <li><a href="#">Config option 2</a>
                                            </li>
                                        </ul>
                                        <a class="close-link">
                                            <i class="fa fa-times"></i>
                                        </a>
                                    </div>
                                </div>
                                <div class="ibox-content">
                                    <div>

                                        <div class="pull-right text-right">

                                            <span class="bar_dashboard" style="display: none;">5,3,9,6,5,9,7,3,5,2,4,7,3,2,7,9,6,4,5,7,3,2,1,0,9,5,6,8,3,2,1</span><svg class="peity" height="16" width="100"><rect fill="#1ab394" x="0" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="3.2580645161290325" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#1ab394" x="6.516129032258065" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#d7d7d7" x="9.774193548387098" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#1ab394" x="13.03225806451613" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="16.290322580645164" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#1ab394" x="19.548387096774196" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#d7d7d7" x="22.806451612903228" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#1ab394" x="26.06451612903226" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="29.322580645161292" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="32.58064516129033" y="8.88888888888889" width="2.2580645161290325" height="7.111111111111111"></rect><rect fill="#d7d7d7" x="35.83870967741936" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#1ab394" x="39.09677419354839" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="42.35483870967742" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="45.612903225806456" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#d7d7d7" x="48.87096774193549" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#1ab394" x="52.12903225806452" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#d7d7d7" x="55.38709677419355" y="8.88888888888889" width="2.2580645161290325" height="7.111111111111111"></rect><rect fill="#1ab394" x="58.645161290322584" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="61.903225806451616" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#1ab394" x="65.16129032258065" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="68.41935483870968" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="71.67741935483872" y="14.222222222222221" width="2.2580645161290325" height="1.7777777777777777"></rect><rect fill="#d7d7d7" x="74.93548387096774" y="15" width="2.2580645161290325" height="1"></rect><rect fill="#1ab394" x="78.19354838709678" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#d7d7d7" x="81.45161290322581" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#1ab394" x="84.70967741935485" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#d7d7d7" x="87.96774193548387" y="1.7777777777777786" width="2.2580645161290325" height="14.222222222222221"></rect><rect fill="#1ab394" x="91.22580645161291" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="94.48387096774194" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="97.74193548387098" y="14.222222222222221" width="2.2580645161290325" height="1.7777777777777777"></rect></svg><svg class="peity" height="16" width="100"><rect fill="#1ab394" x="0" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="3.2580645161290325" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#1ab394" x="6.516129032258065" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#d7d7d7" x="9.774193548387098" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#1ab394" x="13.03225806451613" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="16.290322580645164" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#1ab394" x="19.548387096774196" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#d7d7d7" x="22.806451612903228" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#1ab394" x="26.06451612903226" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="29.322580645161292" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="32.58064516129033" y="8.88888888888889" width="2.2580645161290325" height="7.111111111111111"></rect><rect fill="#d7d7d7" x="35.83870967741936" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#1ab394" x="39.09677419354839" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="42.35483870967742" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="45.612903225806456" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#d7d7d7" x="48.87096774193549" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#1ab394" x="52.12903225806452" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#d7d7d7" x="55.38709677419355" y="8.88888888888889" width="2.2580645161290325" height="7.111111111111111"></rect><rect fill="#1ab394" x="58.645161290322584" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#d7d7d7" x="61.903225806451616" y="3.555555555555557" width="2.2580645161290325" height="12.444444444444443"></rect><rect fill="#1ab394" x="65.16129032258065" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="68.41935483870968" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="71.67741935483872" y="14.222222222222221" width="2.2580645161290325" height="1.7777777777777777"></rect><rect fill="#d7d7d7" x="74.93548387096774" y="15" width="2.2580645161290325" height="1"></rect><rect fill="#1ab394" x="78.19354838709678" y="0" width="2.2580645161290325" height="16"></rect><rect fill="#d7d7d7" x="81.45161290322581" y="7.111111111111111" width="2.2580645161290325" height="8.88888888888889"></rect><rect fill="#1ab394" x="84.70967741935485" y="5.333333333333334" width="2.2580645161290325" height="10.666666666666666"></rect><rect fill="#d7d7d7" x="87.96774193548387" y="1.7777777777777786" width="2.2580645161290325" height="14.222222222222221"></rect><rect fill="#1ab394" x="91.22580645161291" y="10.666666666666668" width="2.2580645161290325" height="5.333333333333333"></rect><rect fill="#d7d7d7" x="94.48387096774194" y="12.444444444444445" width="2.2580645161290325" height="3.5555555555555554"></rect><rect fill="#1ab394" x="97.74193548387098" y="14.222222222222221" width="2.2580645161290325" height="1.7777777777777777"></rect></svg>
                                            <br>
                                            <small class="font-bold">$ 20 054.43</small>
                                        </div>
                                        <h4>NYS report new data!
                                            <br>
                                            <small class="m-r"><a href="http://www.jq22.com/demo/web20160509/graph_flot.html"> Check the stock price! </a> </small>
                                        </h4>
                                        </div>
                                    </div>
                                </div>
                            --><div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <h5 id="news"><span class="translate">消息</span></h5><h5 style="margin-left: 80%;"><a target="_blank" href="/scp/main/showmoremsg.jsp?userid=<%=userid%>&language=<%=language%>" id="sees"><span class="translate">查看更多</span></a></h5>
                                    <div style="width: 100%;height: 460px; overflow: auto;">
                                          <ul id="showmassage" class="list-group clear-list m-t"></ul>
                                    </div>
                                </div>
                            </div>
                            
                            
                        </div>
                            <div class="col-lg-4">
                            	
                            	<div class="ibox-title">
                                    <h5 id="memorandum"><span class=""></span></h5>
                                    <div class="form-box js-form-bg">
										<h3 class="font-bold no-margins">货代专属信用贷款   </h3>
										<hr/>
										 	<div class="layui-form-item" style="margin-bottom: 1px;">
										     	 <div class="layui-inline">
											      <label class="layui-form-label">联系人</label>
											      <div class="layui-input-inline">
											        <input type="text" id="applyContact" class="layui-input">
											      </div>
											    </div>
											    <div class="layui-inline">
											      <label class="layui-form-label">电话</label>
											      <div class="layui-input-inline">
											        <input type="text" id="applyPhone" class="layui-input">
											      </div>
											    </div>
											    <div class="layui-inline" style="display: none">
											      <label class="layui-form-label">公司名</label>
											      <div class="layui-input-inline">
											        <input type="text" id="applyCompany" class="layui-input">
											      </div>
											    </div>
										      	<button class="layui-btn" onclick="apply()">申请</button>
											</div>
										<pre class="form-btn" id="btnNext0" style="word-wrap: break-word;white-space: pre-wrap;">
联合微众银行等金融机构为货代行业定制的信用贷款
1）信用额度最高500万,可一次提取,可分批,也可做为备用金需要时再用
2）利率低至年化3.6%,平均6～8%,等额本金，随借随还，降低成本
3）完全无抵押,纯信用,提供2张开具的发票即可极速授信
4）2分钟审核迅速,5分钟快速放款,最快半小时完成所有环节

郑重提醒：
1.绝不在放款前收取任何费用
2.请您注意识别仿冒情形，防止受骗</pre>
									</div>
                                </div>
                            
                                <div class="ibox-title">
                                    <h5 id="memorandum"><span class="translate">备忘录</span></h5>
                                    <div class="ibox-tools">
                                        <a class="collapse-link">
                                            <i class="fa fa-chevron-up"></i>
                                        </a>
                                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                            <i class="fa fa-wrench"></i>
                                        </a>
                                        <ul class="dropdown-menu dropdown-user">
                                            <li><a href="#">Config option 1</a>
                                            </li>
                                            <li><a href="#">Config option 2</a>
                                            </li>
                                        </ul>
                                        <a class="close-link">
                                            <i class="fa fa-times"></i>
                                        </a>
                                    </div>
                                    <iframe src="memo.jsp?&language=<%=language%>" style="width: 100%;height:410px;border: 0" ></iframe>
                                </div>
                            
                                <!--<div class="ibox float-e-margins">
	                                <div class="ibox-title">
	                                    <h5 id="attendance"><span class="translate">当日考勤记录</span></h5>
	                                    <div class="ibox-tools">
	                                        <a class="collapse-link">
	                                            <i class="fa fa-chevron-up"></i>
	                                        </a>
	                                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                                            <i class="fa fa-wrench"></i>
	                                        </a>
	                                        <ul class="dropdown-menu dropdown-user">
	                                            <li><a href="#">Config option 1</a>
	                                            </li>
	                                            <li><a href="#">Config option 2</a>
	                                            </li>
	                                        </ul>
	                                        <a class="close-link">
	                                            <i class="fa fa-times"></i>
	                                        </a>
	                                    </div>
	                                    <div class="ibox-content no-padding">
                                    		<iframe frameborder="1" style="width: 100%; height: 195px;border: 0;" src="/scp/pages/module/oa/timesheet/todayreport.jsp?userid=<%=userid%>&language=<%=language%>"></iframe>
                                		</div>
	                                </div>
	                                <div class="ibox-content no-padding">
	                                    
	                                </div>
	                            </div>

                            --></div>
                         <div class="col-lg-4">
                                <div class="ibox float-e-margins">
	                                <div class="ibox-title" id="tianqiDiv">
	                                    <h5>天气</h5>
	                                    <div class="ibox-tools">
	                                        <a class="collapse-link">
	                                            <i class="fa fa-chevron-up"></i>
	                                        </a>
	                                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                                            <i class="fa fa-wrench"></i>
	                                        </a>
	                                        <ul class="dropdown-menu dropdown-user">
	                                            <li><a href="#">Config option 1</a>
	                                            </li>
	                                            <li><a href="#">Config option 2</a>
	                                            </li>
	                                        </ul>
	                                        <a class="close-link">
	                                            <i class="fa fa-times"></i>
	                                        </a>
	                                    </div>
                                    		<div class="ibox-content no-padding" id="tianqi">
			                                  <iframe style="width: 100%; height: 61px;border: 0;"  src="http://i.tianqi.com/index.php?c=code&id=12&icon=1&num=3&language=<%=language%>"></iframe>
			                              </div>
	                                </div>
	                            </div>

								<div id="custom1" class="ibox float-e-margins">
	                                <div class="ibox-title">
	                                    <h5 id="remind"><span class="translate">客户未接单11提醒</span></h5>
	                                    <div class="ibox-tools">
	                                        <a class="collapse-link">
	                                            <i class="fa fa-chevron-up"></i>
	                                        </a>
	                                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                                            <i class="fa fa-wrench"></i>
	                                        </a>
	                                        <ul class="dropdown-menu dropdown-user">
	                                            <li><a href="#">Config option 1</a>
	                                            </li>
	                                            <li><a href="#">Config option 2</a>
	                                            </li>
	                                        </ul>
	                                        <a class="close-link">
	                                            <i class="fa fa-times"></i>
	                                        </a>
	                                    </div>
	                                    <div class="ibox-content no-padding">
                                    		<iframe frameborder="1" style="width: 100%; height: 210px;border: 0;" ></iframe>
                                		</div>
	                                </div>
	                                <div class="ibox-content no-padding">
	                                    
	                                </div>
	                            </div>
	                            
	                            
	                            <div id="custom2" class="ibox float-e-margins">
	                                <div class="ibox-title">
	                                    <h5 id="publicremind"><span class="translate">客户将转为公共客户提醒</span></h5>
	                                    <div class="ibox-tools">
	                                        <a class="collapse-link">
	                                            <i class="fa fa-chevron-up"></i>
	                                        </a>
	                                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                                            <i class="fa fa-wrench"></i>
	                                        </a>
	                                        <ul class="dropdown-menu dropdown-user">
	                                            <li><a href="#">Config option 1</a>
	                                            </li>
	                                            <li><a href="#">Config option 2</a>
	                                            </li>
	                                        </ul>
	                                        <a class="close-link">
	                                            <i class="fa fa-times"></i>
	                                        </a>
	                                    </div>
	                                    <div class="ibox-content no-padding">
                                    		<iframe frameborder="0" style="width: 100%; height: 165px;border: 0;" ></iframe>
                                		</div>
	                                </div>
	                                <div class="ibox-content no-padding">
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
	
	<script src="/scp/layui/layui.js"></script>
	
	<script src="./dashboard.js?t=4"></script>
	
    <script>
	    $(document).ready(function() {
	    	var userid = '<%=userid%>';
	    	var language = '<%=language%>';
	    	var usercode = '<%=usercode%>';
	        getsales(userid);
	        getApplyInfo(userid);
	        getsyscfg(userid,language);
	        getmessage(userid);
            initReport(userid);
			init0();
        });
        
        function init(){
        	if("en" == "<%=language%>"){
        		$("#tianqi").remove();
        		$("#tianqiDiv").remove();
        	}
        	$("#processeding").fadeToggle("slow");
        	$("#processeding").empty();
        	$('#processeding').text('');
        	
        	var userid = '<%=userid%>';
        	var usercode = '<%=usercode%>';
        	initBpm(userid,usercode);
        }
        $(function($) {
        	init();
        	setInterval("init()",1000*60*5);
		});
		
    </script>
            

</body>
	<div id="iframesid" title="状态跟踪" class="easyui-window" data-options="iconCls:'icon-save',
    	modal:false,closed:true,collapsible:false" style="width:1200px;height:190px;padding:5px;">
        <div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center'">
				<iframe id="iframeststus" width="100%" height="135" frameborder="0"></iframe>					
	    	</div>
    	</div>
    </div>
</html>