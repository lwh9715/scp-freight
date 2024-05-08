<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width" />
    <title></title>
    <script src="http://apps.bdimg.com/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="./WritingPad.js"></script>
    <script src="./jq-signature.js"></script>
</head>
<body>
</body>
<script>

        $(function () {
            var wp = new WritingPad();
            
             $.ajax({
				type:'POST',
				dataType: 'html',
				url:'/scp/service?src=webServer&action=getFile&userid='+<%=request.getParameter("userid")%>,
				success:function(date){
					if(date.toString().indexOf("ERROR") == 0){
						return;
					}
					var img = $('<img>').attr('src', date);
		            $(myImg).append($('<p>').text(""));
		            $(myImg).append(img);
				}
            });
        });
        
        
        function savaimg (){
        	//console.log("1111111111111111111");
        }
        
        $(document).on("click", "#myClose,.close", null, function () {
           //console.log("1111111111111111111");
           
        });
        
        $(document).on("click", "#mySave", null, function () {
        	//console.log("1111111111111111111");
            var myImg = $('#myImg').empty();
            var dataUrl = $('.js-signature').jqSignature('getDataURL');
            
            var img = $('<img>').attr('src', dataUrl);
            $(myImg).append($('<p>').text(""));
            $(myImg).append(img);
            
            $.ajax({
				type:'POST',
				dataType: 'html',
				url:'/scp/service?src=webServer&action=uploadFile&userid='+<%=request.getParameter("userid")%>,
				data: 'imgStr='+dataUrl.replace(/\+/g, "%2B").replace(/\&/g, "%26"),
				success:function(date){
					//console.log(date);
					alert(date);
				}
            });
        });

    </script>
</html>