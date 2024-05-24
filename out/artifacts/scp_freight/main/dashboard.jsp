<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" session="false" %>
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
    <link href="res/easyui.css" rel="stylesheet"/>

    <style type="text/css">.jqstooltip {
        position: absolute;
        left: 0px;
        top: 0px;
        visibility: hidden;
        background: rgb(0, 0, 0) transparent;
        background-color: rgba(0, 0, 0, 0.6);
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);
        -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";
        color: white;
        font: 10px arial, san serif;
        text-align: left;
        white-space: nowrap;
        padding: 5px;
        border: 1px solid white;
        z-index: 10000;
    }

    .jqsfield {
        color: white;
        font: 10px arial, san serif;
        text-align: left;
    }</style>
</head>
<style type="text/css">
    .btn:focus {

    }

    .translate {
    }
</style>
<body>

<%
    String userid = request.getParameter("userid");
    String usercode = request.getParameter("usercode");
    String language = request.getParameter("language");
    String sysbpm = request.getParameter("sysbpm");
    request.getSession().setAttribute("userid", userid);
%>


<div id="page-wrapper" class="gray-bg dashbard-1" style="min-height: 300px;">
    <div class="row border-bottom">
        <div style="height: 300px;" class="row  border-bottom white-bg dashboard-header">
            <div class="col-lg-4">
                <small id="renwu"><span class="translate">待处理任务</span>：</small>
                <a class="btn btn-outline btn-success" onclick="init();" id="refrash"><i class="fa fa-refresh"></i>&nbsp;<span
                        class="translate">刷新</span></a>
                <ul id="processeding" class="list-group clear-list m-t">
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
                        <div class="ibox-content no-padding">
                            <iframe frameborder="0" style="width: 100%; height: 110px;border: 0;"
                                    src="/scp/pages/sysmgr/message/msg.jsp?userid=<%=userid%>&language=<%=language%>"></iframe>
                        </div>

                        <h5>FAQ：</h5>
                        <div class="ibox-content no-padding">
                            <iframe frameborder="0" style="width: 100%; height: 100px;border: 0;"
                                    src="/scp/pages/sysmgr/faq/faq.jsp?language=<%=language%>"></iframe>
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
                            <div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <h5 id="news"><span class="translate">消息</span></h5><h5 style="margin-left: 80%;">
                                    <a target="_blank"
                                       href="/scp/main/showmoremsg.jsp?userid=<%=userid%>&language=<%=language%>"
                                       id="sees"><span
                                            class="translate">查看更多</span></a></h5>
                                    <div style="width: 100%;height: 415px; overflow: auto;">
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
                                <iframe src="memo.jsp?&language=<%=language%>" style="width: 100%;height:410px;border: 0"></iframe>
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
                                        <iframe style="width: 100%; height: 70px;border: 0;"
                                                src="http://i.tianqi.com/index.php?c=code&id=12&icon=1&num=3&language=<%=language%>"></iframe>
                                    </div>
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
                                        <iframe frameborder="0" style="width: 100%; height: 165px;border: 0;"></iframe>
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
    $(document).ready(function () {
        getsales();
        getsyscfg();
        getmessage();
        setTimeout(function () {
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
            [0, 0], [1, 15], [2, 20], [3, 10], [4, 4], [5, 16], [6, 5], [7, 11], [8, 0], [9, 0], [10, 0], [11, 0], [12, 0]
        ];
        //去年同期
        var data2 = [
            [0, 0], [1, 60], [2, 25], [3, 33], [4, 40], [5, 50], [6, 55], [7, 40], [8, 68], [9, 90], [10, 100], [11, 80], [12, 78]
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
                xaxis: {
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
            name: 'strings', //资源文件名称
            path: '/scp/common/i18n/', //资源文件路径
            mode: 'map', //用Map的方式使用资源文件中的值
            language: '<%=language%>',
            callback: function () {//加载成功后设置显示内容
                $(".translate").each(function (i) {
                    $(this).html($.i18n.prop($(this).html()));
                });
            }
        });

    });

    function init() {
        if ("en" == "<%=language%>") {
            $("#tianqi").remove();
            $("#tianqiDiv").remove();
        }
        $("#processeding").fadeToggle("slow");
        $("#processeding").empty();
        $('#processeding').text('');
        if ("FF" == "<%=sysbpm%>") {
            $.ajax({
                type: "GET",  //提交方式
                dataType: "html", //数据类型
                url: '/scp/service?src=webServer&action=commonQueryByXmldash&sqlId=servlet.web.ff.dashboard.grid.page&qry=<%=usercode%>',
                complete: function () {
                    $(".translates").each(function (i) {
                        $(this).html($.i18n.prop($(this).html()));
                    });
                },
                success: function (jsonData) { //提交成功的回调函数
                    if (jsonData == null || jsonData == "") return;
                    if (typeof (jsonData) === "string") {
                        jsonData = JSON.parse(jsonData);
                    }
                    if (jsonData) {
                        var html = "";
                        for (var i = 0; i < jsonData.length; i++) {
                            //console.log(jsonData[i].nos+'---------------------'+jsonData[i].formUri);
                            var j = i + 1;
                            html += '<li class="list-group-item">';
                            html += '<span class="pull-right">';
                            html += jsonData[i].processstartedtime;
                            html += '</span>';
                            if (i % 2 == 0) {
                                html += '<span class="label label-success">' + j + '</span>';
                            } else {
                                html += '<span class="label label-info">' + j + '</span>';
                            }
                            html += '<span width:50px;>' + jsonData[i].taskdisplayname + '</span>'
                            html += '<span>&nbsp;' + jsonData[i].workitemstatedesc;
                            html += '</span>';
                            html += '<span>&nbsp;' + jsonData[i].nos;
                            html += '</span>';
                            html += '<span>&nbsp;' + jsonData[i].processname;
                            html += '</span>';
                            html += '<span>&nbsp;<a class="btn btn-outline btn-success btn-xs" onclick="show(' + "'" + jsonData[i].processinstanceid + "','" + jsonData[i].id + "','" + jsonData[i].jobnos + "','" + jsonData[i].nosid + "','" + jsonData[i].formUri + "'" + ')" href="#"><span class="translates">查看</span>';
                            html += '<i class="fa fa-location-arrow"></i></a></span>';
                            html += ' </li>';
                        }
                        $("#processeding").append(html);
                        $("#processeding").fadeToggle("slow");
                    }
                }
            });
        } else if ("BPM" == "<%=sysbpm%>") {
            $.ajax({
                type: "GET",  //提交方式
                dataType: "html", //数据类型
                url: '/scp/service?src=webServer&action=commonQueryByXmldashbpm&sqlId=servlet.web.ff.dashboard.grid.page&qry=<%=usercode%>&qry2=<%=userid%>',
                complete: function () {
                    $(".translates").each(function (i) {
                        $(this).html($.i18n.prop($(this).html()));
                    });
                },
                success: function (jsonData) { //提交成功的回调函数
                    if (jsonData == null || jsonData == "") return;
                    if (typeof (jsonData) === "string") {
                        jsonData = JSON.parse(jsonData);
                    }
                    if (jsonData) {
                        var html = "";
                        for (var i = 0; i < jsonData.length; i++) {
                            //console.log(jsonData[i].nos+'---------------------'+jsonData[i].formUri);
                            var j = i + 1;
                            html += '<li class="list-group-item">';
                            html += '<span class="pull-right">';
                            html += '</span>';
                            if (i % 2 == 0) {
                                html += '<span class="label label-success">' + j + '</span>';
                            } else {
                                html += '<span class="label label-info">' + j + '</span>';
                            }
                            html += '<span width:50px;>' + jsonData[i].taskdisplayname + '</span>'
                            html += '<span>&nbsp;' + jsonData[i].workitemstatedesc;
                            html += '</span>';
                            html += '<span>&nbsp;' + jsonData[i].nos;
                            html += '</span>';
                            html += '<span>&nbsp;' + jsonData[i].processname;
                            html += '</span>';
                            html += '<span>&nbsp;<a class="btn btn-outline btn-success btn-xs" onclick="showbpm(' + "'" + jsonData[i].formUri + "','" + jsonData[i].id + "'" + ')" href="#"><span class="translates">查看</span>';
                            html += '<i class="fa fa-location-arrow"></i></a></span>';
                            html += ' </li>';

                        }
                        $("#processeding").append(html);
                        $("#processeding").fadeToggle("slow");
                    }
                }
            });

        }


    }

    $(function ($) {
        init();
        setInterval("init()", 1000 * 60 * 5);
    });

    function show(processinstanceid, workItemId, jobnos, nosid, formUri) {
        window.open(formUri + "?id=" + nosid + "&sn=" + jobnos + "&workItemId=" + workItemId + "&processInstanceId=" + processinstanceid + "&type=edit");
    }

    function showbpm(formUri, taskId) {
        window.open(formUri + "&taskId=" + taskId + "&formatLink=true");
    }

    //当前用户是否为业务员
    function getsales() {
        $.ajax({
            type: 'POST',
            url: "/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getsales&qry=id=<%=userid%>",
            contentType: 'application/json',
            success: function (data) {
                if (data == null || data == "") return;
                if (typeof (data) === "string") {
                    data = JSON.parse(data);
                }
                var str = "false";
                if (data.issales == str) {
                    $("#custom1").hide();
                    $("#custom2").hide();
                }
            }
        });
    }

    function getsyscfg() {
        $.ajax({
            type: 'POST',
            url: "/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getsyscfg&qry=AND key='sys_customer_alert_auto'",
            contentType: 'application/json',
            success: function (data) {
                if (data == null || data == "") return;
                if (typeof (data) === "string") {
                    data = JSON.parse(data);
                }
                if (data) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].key == "sys_customer_alert_auto" && data[i].val == "Y") {
                            $("#custom1 iframe").attr("src", "/scp/pages/module/salesmgr/customer_auto_alert.jsp?userid='<%=userid%>'&language=<%=language%>");
                        } else {
                            $("#custom1 iframe").attr("src", "");
                            $("#custom1").hide();
                        }
                        ;
                    }
                }
            }
        });
        $.ajax({
            type: 'POST',
            url: "/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getsyscfg&qry=AND key='sys_customer_transpublic_auto'",
            contentType: 'application/json',
            success: function (data) {
                if (data == null || data == "") return;
                if (typeof (data) === "string") {
                    data = JSON.parse(data);
                }
                if (data) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].key == "sys_customer_transpublic_auto" && data[i].val == "Y") {
                            $("#custom2 iframe").attr("src", "/scp/pages/module/salesmgr/customer_auto_trans.jsp?userid=<%=userid%>&language=<%=language%>");
                        } else {
                            $("#custom2 iframe").attr("src", "");
                            $("#custom2").hide();
                        }
                        ;
                    }
                }
            }
        });
    }


    function getmessage() {
        $.ajax({
            type: 'POST',
            url: "/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getmessage&qry=userid=<%=userid%>",
            contentType: 'application/json',
            success: function (data) {
                if (data == null || data == "") return;
                if (typeof (data) === "string") {
                    data = JSON.parse(data);
                }
                //console.log(data);
                if (data) {
                    var html = "";
                    var j = 0;
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].title == null || data[i].title == "") {

                        } else {
                            j = j + 1;
                            html += '<li class="list-group-item">';
                            html += '<span>' + j + '';
                            html += '</span>';
                            html += '<span width:50px;>' + data[i].title + '</span>'
                            html += ' </li>';
                        }
                    }
                    $("#showmassage").append(html);
                }

                $("#showmassage span:contains('欠款超期')").css("color", "blue");
                $("#showmassage span:contains('利润亏损')").css("color", "red");

            }
        });
    }

    function showWindow(id) {
        $('#iframeststus').attr('src', '/scp/common/goodstrack.jsp?fkid=' + id + '');
        $('#iframesid').window('open');
    }

    function initReport() {
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
            type: 'POST',
            url: "/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getReport&qry=<%=userid%>",
            contentType: 'application/json',
            success: function (data) {
                if (data == null || data == "") return;
                if (typeof (data) === "string") {
                    data = JSON.parse(data);
                }
                //console.log(data);

                if (data) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].year == 2018) {
                            currentYear[data[i].month - 1] = data[i].counts;
                        } else {
                            lastYear[data[i].month - 1] = data[i].counts;
                        }
                    }
                    for (var i = 0; i < 11; i++) {
                        if (lastYear[i] == null) lastYear[i] = 0;
                    }
                    for (var i = 0; i < currentYear.length; i++) {
                        if (currentYear[i] == null) currentYear[i] = 0;
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
                            data: lastYear
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
                            data: currentYear
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