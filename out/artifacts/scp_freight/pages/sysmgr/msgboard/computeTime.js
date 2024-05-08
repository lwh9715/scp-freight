/**
  * 格式化时间(年/月/日 时:分:秒.毫秒)
 **/	
function formatTime(time) {
	var a = time.indexOf('.')
	if(a > -1) {
		time = time.substring(0,a);
	}
	time = time.replace(/[-]/g, '/');
	return time;
}

/**
 * 格式化时间(年/月/日)
**/	
function formatDate(dt) {
	return dt.getFullYear() + '/' + (dt.getMonth()+1) + '/' + dt.getDate();
}
     
/**
  * 查看距离现在时刻相差时间
 **/		
function computeTime(lastTime) {
			//时间整体(年/月/日 时:分:秒.毫秒)格式化
			lastTime = formatTime(lastTime);
			var dt= new Date(Date.parse(lastTime));
			//时间(年-月-日)格式化
			l_date = formatDate(dt);
			n_date = formatDate(new Date());
			res_date = dateDiff('D',l_date,n_date);
			if(res_date > 2) {
				return l_date;
			}
			if(res_date == 2 ) {
				return '前天';
			}
			if(res_date <= 1) {
				var hour =  dateDiff('H',lastTime);
				if(hour > 24) {
					return '昨天';
				}else if(hour > 0) {
        			return hour +'小时前';
        		} else {
        			var m = dateDiff('M',lastTime);                 			
        			if(m > 0) {
        				return m + '分钟前';
        			}else {
        				return "刚刚";
        			}
        		}
			}
}

/**
  * 距现在的时间数(天、小时、分钟)
 **/
function dateDiff(interval, date1, date2){
	var objInterval = {'D':1000 * 60 * 60 * 24,'H':1000 * 60 * 60,'M':1000 * 60,'S':1000,'T':1};
	interval = interval.toUpperCase();
	var dt1 = new Date(Date.parse(date1));
	var dt2 ;
	if(date2) {
		var dt2 = new Date(Date.parse(date2));
	} else {
		dt2 = new Date();
	}
	try
	{
		 return Math.floor((dt2.getTime() - dt1.getTime()) / eval('objInterval .'+interval));
	}
	catch (e)
	{
		 return e.message;
	}
}          
