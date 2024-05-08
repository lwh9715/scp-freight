var form , layer;
layui.use(['form'], function(){
  form = layui.form
  ,layer = layui.layer
})

function apply(){
	var applyContact = $('#applyContact').val();
	var applyPhone = $('#applyPhone').val();
	var applyCompany = $('#applyCompany').val();
	
	if(applyContact == ''){
		layer.alert('请输入联系人信息', {title: '提示'})
		return;
	}
	if(applyPhone == ''){
		layer.alert('请输入联系电话', {title: '提示'})
		return;
	}
	//if(applyCompany == ''){
	//	layer.alert('请输入公司信息', {title: '提示'})
	//	return;
	//}
	//let tips = applyCompany + applyPhone + applyCompany;
	//layer.alert(tips, {
    //  title: '提示'
    //})
    var uplatUrl = 'http://hangxun.vicp.io:18888/uplat';
	var url= uplatUrl + "/applyLoan?method=apply&applyContact="+applyContact+"&applyPhone="+applyPhone+"&applyCompany="+applyCompany;
	console.log(url);
	$.ajax({
		type:'get',
		url:url,
		success:function(data){
			if(data == null || data == "")return;
			console.log(data);
			if(typeof(data)==="string"){
				data = JSON.parse(data);
			}
			if(data.success){
				layer.alert(data.message, {
				      title: '提示'
				})
			}
		}
	});
}

function init0(){
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
}

function initBpm(userid,usercode){
	$.ajax({
        type: "GET",  //提交方式  
        dataType: "html", //数据类型  
		   url:'/scp/service?src=webServer&action=commonQueryByXmldashbpm&sqlId=servlet.web.ff.dashboard.grid.page&qry='+usercode+'&qry2='+userid,
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

//当前用户是否为业务员
function getsales(userid){
	$.ajax({
		type:'POST',
		url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getsales&qry=id="+userid,
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

function getApplyInfo(userid){
	$.ajax({
		type:'POST',
		url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getApplyInfo&qry=id="+userid,
		contentType:'application/json',
		success:function(data){
			if(data == null || data == "")return;
			if(typeof(data)==="string"){
				data = JSON.parse(data);
			}
			console.log(data);
			$('#applyContact').val(data[0].namec);
			$('#applyPhone').val(data[0].tel);
			$('#applyCompany').val(data[0].company);
		}
	});
}

function getmessage(userid){
	$.ajax({
		type:'POST',
		url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getmessage&qry=userid="+userid,
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

function getsyscfg(userid,language){
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
						$("#custom1 iframe").attr("src","/scp/pages/module/salesmgr/customer_auto_alert.jsp?userid="+userid+"&language="+language);
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
						$("#custom2 iframe").attr("src","/scp/pages/module/salesmgr/customer_auto_trans.jsp?userid="+userid+"&language="+language);
					}else{
						$("#custom2 iframe").attr("src","");
						$("#custom2").hide();
					};
				}
			}
		}
	});
}

function initReport(userid){
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
		url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.getReport&qry="+userid,
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

function show(processinstanceid,workItemId,jobnos,nosid,formUri){
	window.open(formUri+"?id="+nosid+"&sn="+jobnos+"&workItemId="+workItemId+"&processInstanceId="+processinstanceid+"&type=edit");   
}

function showbpm(formUri,taskId){
	window.open(formUri+"&taskId="+taskId+"&formatLink=true");   
}

function showWindow(id){
	$('#iframeststus').attr('src','/scp/common/goodstrack.jsp?fkid='+id+'');
	$('#iframesid').window('open');
}
