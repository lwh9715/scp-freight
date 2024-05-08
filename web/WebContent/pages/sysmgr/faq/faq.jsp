<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/scp/common/js/jquery.min.js"></script>
<link href="http://apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="/scp/common/js/jquery.i18n.properties-min-1.0.9.js"></script>
<title>Insert title here</title>
<style type="text/css">
body {
		margin: 0px;
		padding: 0px;
		width: 100%;
		height: 100%;
		font-size: 12px;
		font-family:  "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
		color: #676a6c;
	}

table{
		border:1px solid #e7eaec;
		width: 100%;
		border-spacing:0px;
		border-collapse:collapse;
		font-family:  "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
	}
	
	.xq1{
		width: 64.3%;
		border: 1px solid #e7eaec;
		text-align: left;
	}
	
	.xq2{
		width: 16.9%;
		border: 1px solid #e7eaec;
		text-align: center;
	}

	.xq3{
		border: 1px solid #e7eaec;
		text-align: center;
		width: 18.8%;
	}
</style>
</head>
<body>
<%
	String language = request.getParameter("language");
 %>
<div style="margin-top:10px; width: 100%; height: 100%; ">
<div style="text-align: right;"><a href="#" onclick="openmore();" id="others">查看更多</a></div>
	<table id="faq">
		<thead>
			<tr>
				<td class="xq1" id="title">标题</td>
				<!--<td class="xq2">分类</td>
				<td class="xq2">关键字</td>
				--><td class="xq2" id="publisher">发布人</td>
				<td class="xq3" id="publishtime">发布时间</td>
			</tr>
		</thead>
		<tbody>
			<tr>
			</tr>
		</tbody>
	
	</table>
</div>


<script type="text/javascript"><!--
	$(document).ready(function(){
		queryfaq();
		jQuery.i18n.properties({
            name : 'strings', //资源文件名称
            path : '/scp/common/i18n/', //资源文件路径
            mode : 'map', //用Map的方式使用资源文件中的值
            language : '<%=language%>',
            callback : function() {//加载成功后设置显示内容
                $('#title').html($.i18n.prop('标题'));
                $('#publisher').html($.i18n.prop('发布人'));
                $('#publishtime').html($.i18n.prop('发布时间'));
                 $('#others').html($.i18n.prop('查看更多'));
            }
        });
	});


	function queryfaq(){
		$.ajax({
			type:'POST',
			url:"/scp/service?src=webServer&action=commonQueryByXml&sqlId=servlet.web.ff.queryfaq",
			contentType:'application/json',
			success:function(data){
				if(data == null || data == "")return;
				if(typeof(data)==="string"){
					data = JSON.parse(data);
				}
				if(data){
					var html;
					for(var i = 0; i < data.length; i++){
						html = "<tr id='tr"+i+"'>";
						html+="<td class='xq1'><a href='./faqshow.xhtml?id="+data[i].id+"' target='_blank'><span>"+data[i].subject+"</span></a></td>";
						//html+="<td class='xq3'><span>"+data[i].classify+"</span></td>";
						//html+="<td class='xq3'><span>"+data[i].keywords+"</span></td>";
						html+="<td class='xq2'><span>"+data[i].inputer+"</span></td>";
						html+="<td class='xq3'><span>"+data[i].inputtime2+"</span></td>";
						html+="</tr>";
						$("#faq tbody").append(html);
					}
				}
			}
		});
	}
	
	
	function openmore(){
		window.open("/scp/pages/sysmgr/faq/faq.xhtml")
	}
	
	function format(now,mask)
    {
        var d = now;
        var zeroize = function (value, length)
        {
            if (!length) length = 2;
            value = String(value);
            for (var i = 0, zeros = ''; i < (length - value.length); i++)
            {
                zeros += '0';
            }
            return zeros + value;
        };
     
        return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
        {
            switch ($0)
            {
                case 'd': return d.getDate();
                case 'dd': return zeroize(d.getDate());
                case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
                case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
                case 'M': return d.getMonth() + 1;
                case 'MM': return zeroize(d.getMonth() + 1);
                case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
                case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
                case 'yy': return String(d.getFullYear()).substr(2);
                case 'yyyy': return d.getFullYear();
                case 'h': return d.getHours() % 12 || 12;
                case 'hh': return zeroize(d.getHours() % 12 || 12);
                case 'H': return d.getHours();
                case 'HH': return zeroize(d.getHours());
                case 'm': return d.getMinutes();
                case 'mm': return zeroize(d.getMinutes());
                case 's': return d.getSeconds();
                case 'ss': return zeroize(d.getSeconds());
                case 'l': return zeroize(d.getMilliseconds(), 3);
                case 'L': var m = d.getMilliseconds();
                    if (m > 99) m = Math.round(m / 10);
                    return zeroize(m);
                case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
                case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
                case 'Z': return d.toUTCString().match(/[A-Z]+$/);
                // Return quoted strings with the surrounding quotes removed
                default: return $0.substr(1, $0.length - 2);
            }
        });
    };
--></script>
</body>
</html>