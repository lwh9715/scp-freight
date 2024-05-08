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

                    <div class="col-lg-4">
                        <!--<h2>Welcome</h2>
                        --><small id="renwu"><span class="translate">待处理任务</span>：</small>
                        <a class="btn btn-outline btn-success" onclick="init();" id="refrash"><i class="fa fa-refresh"></i>&nbsp;<span class="translate">刷新</span></a>
                        <ul id="processeding" class="list-group clear-list m-t">
                        	<!--
                             <li class="list-group-item fist-item">
                                <span class="pull-right">
                                    09:00 pm
                                </span>
                                <span class="label label-success">1</span> Please contact me
                            </li>
                        	-->
                        </ul>
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
                            
                                <div class="ibox float-e-margins">
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

                            </div>
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
	                                    <h5 id="remind"><span class="translate">客户未接单提醒</span></h5>
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
    <script>
	    $(document).ready(function() {
	        getsales();
	        getsyscfg();
	        getmessage();
            setTimeout(function() {
                toastr.options = {
                    closeButton: true,
                    progressBar: true,
                    showMethod: 'slideDown',
                    timeOut: 10000
                };
                //toastr.success('让软件变得简单，让货代变得简单', '欢迎使用!!!');
                toastr.success($.i18n.prop('让软件变得简单，让货代变得简单, 欢迎使用!!!'));
            }, 1300);
            
            initReport();

			//单量
            var data1 = [
                [0,0],[1,15],[2,20],[3,10],[4,4],[5,16],[6,5],[7,11],[8,0],[9,0],[10,0],[11,0],[12,0]
            ];
            //去年同期
            var data2 = [
                [0,0],[1,60],[2,25],[3,33],[4,40],[5,50],[6,55],[7,40],[8,68],[9,90],[10,100],[11,80],[12,78]
            ];
            $("#flot-dashboard-chart").length && $.plot($("#flot-dashboard-chart"), [
                data1, data2
            ],
                    {
                        series: {
                            lines: {
                                show: false,
                                fill: true
                            },
                            splines: {
                                show: true,
                                tension: 0.4,
                                lineWidth: 1,
                                fill: 0.4
                            },
                            points: {
                                radius: 0,
                                show: true
                            },
                            shadowSize: 2
                        },
                        grid: {
                            hoverable: true,
                            clickable: true,
                            tickColor: "#d5d5d5",
                            borderWidth: 1,
                            color: '#d5d5d5'
                        },
                        colors: ["#1ab394", "#1C84C6"],
                        xaxis:{
                        	ticks: 12
                        },
                        yaxis: {
                            ticks: 8
                        },
                        tooltip: false
                    }
            );

            var doughnutData = [
                {
                    value: 300,
                    color: "#a3e1d4",
                    highlight: "#1ab394",
                    label: "App"
                },
                {
                    value: 50,
                    color: "#dedede",
                    highlight: "#1ab394",
                    label: "Software"
                },
                {
                    value: 100,
                    color: "#A4CEE8",
                    highlight: "#1ab394",
                    label: "Laptop"
                }
            ];

            var doughnutOptions = {
                segmentShowStroke: true,
                segmentStrokeColor: "#fff",
                segmentStrokeWidth: 2,
                percentageInnerCutout: 45, // This is 0 for Pie charts
                animationSteps: 100,
                animationEasing: "easeOutBounce",
                animateRotate: true,
                animateScale: false
            };

            //var ctx = document.getElementById("doughnutChart").getContext("2d");
            //var DoughnutChart = new Chart(ctx).Doughnut(doughnutData, doughnutOptions);

            var polarData = [
                {
                    value: 300,
                    color: "#a3e1d4",
                    highlight: "#1ab394",
                    label: "App"
                },
                {
                    value: 140,
                    color: "#dedede",
                    highlight: "#1ab394",
                    label: "Software"
                },
                {
                    value: 200,
                    color: "#A4CEE8",
                    highlight: "#1ab394",
                    label: "Laptop"
                }
            ];

            var polarOptions = {
                scaleShowLabelBackdrop: true,
                scaleBackdropColor: "rgba(255,255,255,0.75)",
                scaleBeginAtZero: true,
                scaleBackdropPaddingY: 1,
                scaleBackdropPaddingX: 1,
                scaleShowLine: true,
                segmentShowStroke: true,
                segmentStrokeColor: "#fff",
                segmentStrokeWidth: 2,
                animationSteps: 100,
                animationEasing: "easeOutBounce",
                animateRotate: true,
                animateScale: false
            };
            //var ctx = document.getElementById("polarChart").getContext("2d");
            //var Polarchart = new Chart(ctx).PolarArea(polarData, polarOptions);
            jQuery.i18n.properties({
	            name : 'strings', //资源文件名称
	            path : '/scp/common/i18n/', //资源文件路径
	            mode : 'map', //用Map的方式使用资源文件中的值
	            language : '<%=language%>',
	            callback : function() {//加载成功后设置显示内容
	                $(".translate").each(function(i){
					   $(this).html($.i18n.prop($(this).html()));
					});
	            }
	        });
            
        });
        
        function init(){
        	if("en" == "<%=language%>"){
        		$("#tianqi").remove();
        		$("#tianqiDiv").remove();
        	}
        	$("#processeding").fadeToggle("slow");
        	$("#processeding").empty();
        	$('#processeding').text('');
        	if("FF" == "<%=sysbpm%>"){
        		$.ajax({
               type: "GET",  //提交方式  
               dataType: "html", //数据类型  
			   url:'/scp/service?src=webServer&action=commonQueryByXmldash&sqlId=servlet.web.ff.dashboard.grid.page&qry=<%=usercode%>',
			   complete: function () {
		 			$(".translates").each(function(i){
					   $(this).html($.i18n.prop($(this).html()));
					});
	           },
               success: function (jsonData) { //提交成功的回调函数  
                   if(jsonData == null || jsonData == "")return;
                   if(typeof(jsonData)==="string"){
							jsonData = JSON.parse(jsonData);
					}
				   if(jsonData) {
	                   var html = "";
				       for (var i = 0; i < jsonData.length; i++) {
				       		//console.log(jsonData[i].nos+'---------------------'+jsonData[i].formUri);
				       		var j=i+1;
				        	html+='<li class="list-group-item">';
				        	html+='<span class="pull-right">';
				        	html+=jsonData[i].processstartedtime;
				        	html+='</span>';
				        	if(i%2==0){
				        		html+='<span class="label label-success">'+j+'</span>';
				        	}else{
				        		html+='<span class="label label-info">'+j+'</span>';
				        	}
				        	html+='<span width:50px;>'+jsonData[i].taskdisplayname+'</span>'
				        	html+='<span>&nbsp;'+jsonData[i].workitemstatedesc;
				        	html+='</span>';
				        	html+='<span>&nbsp;'+jsonData[i].nos;
				        	html+='</span>';
				        	html+='<span>&nbsp;'+jsonData[i].processname;
				        	html+='</span>';
				        	html+='<span>&nbsp;<a class="btn btn-outline btn-success btn-xs" onclick="show('+"'"+jsonData[i].processinstanceid+"','"+jsonData[i].id+"','"+jsonData[i].jobnos+"','"+jsonData[i].nosid+"','"+jsonData[i].formUri+"'"+')" href="#"><span class="translates">查看</span>';
				        	html+='<i class="fa fa-location-arrow"></i></a></span>';
				        	html+=' </li>';
				       }
				       $("#processeding").append(html);
				       $("#processeding").fadeToggle("slow");
				   }
				}
		 	});
        	}else if("BPM" == "<%=sysbpm%>"){
        		$.ajax({
               type: "GET",  //提交方式  
               dataType: "html", //数据类型  
			   url:'/scp/service?src=webServer&action=commonQueryByXmldashbpm&sqlId=servlet.web.ff.dashboard.grid.page&qry=<%=usercode%>&qry2=<%=userid%>',
			   complete: function () {
		 			$(".translates").each(function(i){
					   $(this).html($.i18n.prop($(this).html()));
					});
	           },
               success: function (jsonData) { //提交成功的回调函数  
                   if(jsonData == null || jsonData == "")return;
                   if(typeof(jsonData)==="string"){
							jsonData = JSON.parse(jsonData);
					}
				   if(jsonData) {
	                   var html = "";
				       for (var i = 0; i < jsonData.length; i++) {
				       		//console.log(jsonData[i].nos+'---------------------'+jsonData[i].formUri);
				       		var j=i+1;
				        	html+='<li class="list-group-item">';
				        	html+='<span class="pull-right">';
				        	html+='</span>';
				        	if(i%2==0){
				        		html+='<span class="label label-success">'+j+'</span>';
				        	}else{
				        		html+='<span class="label label-info">'+j+'</span>';
				        	}
				        	html+='<span width:50px;>'+jsonData[i].taskdisplayname+'</span>'
				        	html+='<span>&nbsp;'+jsonData[i].workitemstatedesc;
				        	html+='</span>';
				        	html+='<span>&nbsp;'+jsonData[i].nos;
				        	html+='</span>';
				        	html+='<span>&nbsp;'+jsonData[i].processname;
				        	html+='</span>';
				        	html+='<span>&nbsp;<a class="btn btn-outline btn-success btn-xs" onclick="showbpm('+"'"+jsonData[i].formUri+"','"+jsonData[i].id+"'"+')" href="#"><span class="translates">查看</span>';
				        	html+='<i class="fa fa-location-arrow"></i></a></span>';
				        	html+=' </li>';
				       		
				       }
				       $("#processeding").append(html);
				       $("#processeding").fadeToggle("slow");
				   }
				}
		 	});
        	
        	}
        	
        	
        }
        $(function($) {
        	init();
        	setInterval("init()",1000*60*5);
		});
		function show(processinstanceid,workItemId,jobnos,nosid,formUri){
			window.open(formUri+"?id="+nosid+"&sn="+jobnos+"&workItemId="+workItemId+"&processInstanceId="+processinstanceid+"&type=edit");   
		}
		
		function showbpm(formUri,taskId){
			window.open(formUri+"&taskId="+taskId+"&formatLink=true");   
		}
		
		//当前用户是否为业务员
		function getsales(){
			$.ajax({
				type:'POST',
				url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getsales&qry=id=<%=userid%>",
				contentType:'application/json',
				success:function(data){
					if(data == null || data == "")return;
					if(typeof(data)==="string"){
						data = JSON.parse(data);
					}
					var str = "false";
					if(data.issales == str){
						$("#custom1").hide();
						$("#custom2").hide();
					}
				}
			});
		}
		
		function getsyscfg(){
			$.ajax({
				type:'POST',
				url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getsyscfg&qry=AND key='sys_customer_alert_auto'",
				contentType:'application/json',
				success:function(data){
					if(data == null || data == "")return;
					if(typeof(data)==="string"){
						data = JSON.parse(data);
					}
					if(data){
						 for(var i = 0; i < data.length; i++) {
							if(data[i].key =="sys_customer_alert_auto" && data[i].val== "Y" ){
								$("#custom1 iframe").attr("src","/scp/pages/module/salesmgr/customer_auto_alert.jsp?userid='<%=userid%>'&language=<%=language%>");
							}else{
								$("#custom1 iframe").attr("src","");
								$("#custom1").hide();
							};
						}
					}
				}
			});
			$.ajax({
				type:'POST',
				url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getsyscfg&qry=AND key='sys_customer_transpublic_auto'",
				contentType:'application/json',
				success:function(data){
					if(data == null || data == "")return;
					if(typeof(data)==="string"){
						data = JSON.parse(data);
					}
					if(data){
						 for(var i = 0; i < data.length; i++) {
							if(data[i].key =="sys_customer_transpublic_auto" && data[i].val=="Y" ){
								$("#custom2 iframe").attr("src","/scp/pages/module/salesmgr/customer_auto_trans.jsp?userid=<%=userid%>&language=<%=language%>");
							}else{
								$("#custom2 iframe").attr("src","");
								$("#custom2").hide();
							};
						}
					}
				}
			});
		}
		
		
		function getmessage(){
			$.ajax({
				type:'POST',
				url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getmessage&qry=userid=<%=userid%>",
				contentType:'application/json',
				success:function(data){
					if(data == null || data == "")return;
					if(typeof(data)==="string"){
						data = JSON.parse(data);
					}
					//console.log(data);
					if(data){
						  var html = "";
						  var j = 0;
						   for (var i = 0; i < data.length; i++) {
					       		if(data[i].title ==null || data[i].title==""){
					       			
					       		}else{
					       			j = j+1;
						        	html+='<li class="list-group-item">';
						        	html+='<span>'+j+'';
						        	html+='</span>';
						        	html+='<span width:50px;>'+data[i].title+'</span>'
						        	html+=' </li>';
						       	}
					       	}
					       	$("#showmassage").append(html);
					}
					
					$("#showmassage span:contains('欠款超期')").css("color","blue");
					$("#showmassage span:contains('利润亏损')").css("color","red");
					
				}
			});
		}
		
		function showWindow(id){
			$('#iframeststus').attr('src','/scp/common/goodstrack.jsp?fkid='+id+'');
			$('#iframesid').window('open');
		}
		
		function initReport(){
			var lineOptions = {
                scaleShowGridLines: true,
                scaleGridLineColor: "rgba(0,0,0,.05)",
                scaleGridLineWidth: 1,
                bezierCurve: true,
                bezierCurveTension: 0.4,
                pointDot: true,
                pointDotRadius: 4,
                pointDotStrokeWidth: 1,
                pointHitDetectionRadius: 20,
                datasetStroke: true,
                datasetStrokeWidth: 2,
                datasetFill: true,
                responsive: true,
            };
			var lastYear = [];
			var currentYear = [];
			$.ajax({
				type:'POST',
				url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getReport&qry=<%=userid%>",
				contentType:'application/json',
				success:function(data){
					if(data == null || data == "")return;
					if(typeof(data)==="string"){
						data = JSON.parse(data);
					}
					//console.log(data);
					
					if(data){
						for (var i = 0; i < data.length; i++) {
							if(data[i].year == 2018){
								currentYear[data[i].month-1] = data[i].counts;
							}else{
						  		lastYear[data[i].month-1] = data[i].counts;
						  	}
						}
						for (var i = 0; i < 11; i++) {
							if(lastYear[i] == null)lastYear[i]=0;
						}
						for (var i = 0; i < currentYear.length; i++) {
							if(currentYear[i] == null)currentYear[i]=0;
						}
						
					}
					//console.log(lastYear);
					//console.log(currentYear);
					
					var lineData = {
		                labels: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
		                datasets: [
		                    {
		                        label: "Last Year",
		                        fillColor: "rgba(220,220,220,0.5)",
		                        strokeColor: "rgba(220,220,220,1)",
		                        pointColor: "rgba(220,220,220,1)",
		                        pointStrokeColor: "#fff",
		                        pointHighlightFill: "#fff",
		                        pointHighlightStroke: "rgba(220,220,220,1)",
		                        //data: [165, 159, 180, 181, 156, 155, 140 ,165, 159, 180, 181, 156, 155, 140]
		                        data:lastYear
		                    },
		                    {
		                        label: "Current Year",
		                        fillColor: "rgba(26,179,148,0.5)",
		                        strokeColor: "rgba(26,179,148,0.7)",
		                        pointColor: "rgba(26,179,148,1)",
		                        pointStrokeColor: "#fff",
		                        pointHighlightFill: "#fff",
		                        pointHighlightStroke: "rgba(26,179,148,1)",
		                        //data: [128, 148, 140, 159, 186, 187, 196]
		                        data:currentYear
		                    }
		                ]
		            };	
		            
		             var ctx = document.getElementById("lineChart").getContext("2d");
           			 var myNewChart = new Chart(ctx).Line(lineData, lineOptions);
				}
			});
           
		}
		
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