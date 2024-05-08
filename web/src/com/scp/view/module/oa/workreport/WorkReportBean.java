package com.scp.view.module.oa.workreport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UINumberField;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.NoRowException;
import com.scp.model.oa.OaWorkReport;
import com.scp.model.sys.SysEmail;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.oa.workreport.workreportBean", scope = ManagedBeanScope.REQUEST)
public class WorkReportBean extends GridFormView {

//	@Bind
//	public UISimpleHtmlEditor msgContent;

	@SaveState
	@Accessible
	public OaWorkReport selectedRowData = new OaWorkReport();
	
	@SaveState
	@Accessible
	public SysEmail sysemail = new SysEmail();
	
	@Bind
	@SaveState
	public UINumberField weeks;
	
	@SaveState
	@Accessible
	public Long nowweeks;
	
	@SaveState
	@Accessible
	public String reptoemail;
	
	@SaveState
	@Accessible
	public Long sendemailid;
	
	@Bind
	@SaveState
	@Accessible
	public Long nowweeks2;
	
	@Bind
	@SaveState
	public String centercontext;
	

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		try {
			this.nowweeks2 =this.getWeekOfYear();
			this.nowweeks = this.nowweeks2;
		} catch (Exception e) {
			alert("获取当今周数异常!");
			e.printStackTrace();
		}
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
//		if (weeks != 0 && !"".equals(weeks) && weeks != null ) {
//			this.title = this.setTitle(weeks);
//		}else{
//			this.title = this.setTitle(this.getWeekOfYear());
//		}
		try{
			if(this.selectedRowData.getRep2userid() != null){
				SysUser user = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getRep2userid());
				if(user!=null){
					Browser.execClientScript("$('#sales_input').val('"+user.getNamec()+"')");
				}
			}
		}catch(NoRowException e){
			
		}
		
	}

	@Override
	public void add() {
//		selectedRowData = new SysMsgboard();
		super.add();
		this.refresh();
		selectedRowData = new OaWorkReport();
		selectedRowData.setReptype("W");
		selectedRowData.setWeekno(this.nowweeks);
		this.timeForReptype("W");
		
		 
		 
//		selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
//		selectedRowData.setInputtime(Calendar.getInstance().getTime());
//		msgContent.setValue((String) this.selectedRowData.getMsgcontent());
//		msgContent.repaint();
	}

	@Override
	protected void doServiceFindData() {
		this.pkVal = this.getGridSelectId();
		String sql = "  id = " + this.pkVal;
		this.selectedRowData = this.serviceContext.workReportService().workReportDao.findOneRowByClauseWhere(sql);
				
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		String usercode = AppUtils.getUserSession().getUsercode();
		//20141226 增加被汇报的人也能看到数据
		qry += "\nAND (inputer = '" +usercode+"' OR rep2userid = "+AppUtils.getUserSession().getUserid()+")";
		m.put("qry", qry);
		return m;
	}
	/**
	 * 保存以后生成一封自动发送邮件
	 */
	@Override
	protected void doServiceSave() {
		// //System.out.println("id为"+selectedRowData.getId());
		this.serviceContext.workReportService().saveData(selectedRowData);//新增或修改
		Long newid = this.getNewId(selectedRowData.getInputer());
		String type = this.getCount(newid);
		if(this.getrep2useridEmail(selectedRowData
				.getRep2userid()) !=null ){
			this.reptoemail = this.getrep2useridEmail(selectedRowData
					.getRep2userid());
		}
		if (type.equals("0")) {
			if (selectedRowData.getTimeend().compareTo(new Date()) == 1) {// 判断为截止时间在当前时间之后,生成自动发送邮件
				sysemail.setAddressee(reptoemail);
				sysemail.setSubject(selectedRowData.getReptitle());
				sysemail.setContent(selectedRowData.getRepcontent());
				sysemail.setSender(this.getPop3Email());
				sysemail.setEmailtype("S");
				sysemail.setIsautosend(true);
				sysemail.setLinktbl("工作报告");
			} else {// 判断为截止时间与当前时间相同(截止时间设置为夜里零点才会自动发送,不做另外考虑)，或小于当前时间,为补发邮件
				sysemail.setAddressee(reptoemail);
				sysemail.setSubject(selectedRowData.getReptitle());
				sysemail.setContent(selectedRowData.getRepcontent());
				sysemail.setSender(this.getPop3Email());
				sysemail.setEmailtype("S");
				sysemail.setIsautosend(false);
				sysemail.setLinktbl("工作报告补发邮件");
			}
			try {
				this.sendemailid = newid;
				sysemail.setLinkid(this.sendemailid);
			} catch (Exception e1) {
				alert("用户名不存在或发生异常,请刷新试试!");
				e1.printStackTrace();
			}
			try {
				this.serviceContext.sysEmailService.sysEmailDao
						.create(sysemail);
			} catch (Exception e) {
				alert("生成邮件发生异常!");
				e.printStackTrace();
			}
		} else if (type.equals("1")) {
			SysEmail oldemail = this.getsysEmailForId(selectedRowData.getId());
			oldemail.setAddressee(reptoemail);
			oldemail.setSubject(selectedRowData.getReptitle());
			oldemail.setContent(selectedRowData.getRepcontent());
			this.serviceContext.sysEmailService.saveData(oldemail);
		}if (type.equals("2")) {
			alert("异常!无法发送邮件!");
		}
		editWindow.close();
		alert("ok");
	}
	
	@Override
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			alert("请选择一行");
			return;
		}
		for (String id : ids) {
			String sql = "UPDATE oa_workreport SET isdelete = TRUE WHERE id = "
					+ Long.valueOf(id);
			try {
				serviceContext.workReportService().workReportDao.executeSQL(sql);
				this.refresh();
				alert("ok");
			} catch (Exception e) {
				alert("id not find!");
				e.printStackTrace();
			}
		}
	}
	
	@Action
	public void addcopy(){
		doServiceFindData();
		OaWorkReport workreport = new OaWorkReport();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_MONTH, 1);
		if (selectedRowData.getReptitle() != null
				&& !"".equals(selectedRowData.getReptitle())) {
			String type = selectedRowData.getReptype();
			if (type.equals("W")) {// 如果是周报，标题相应的要修改
				Long weekno = selectedRowData.getWeekno() + 1;
				String title = "周报" + "(" + weekno + "周" + ")"
						+ "--" + AppUtils.getUserSession().getUsercode();
				workreport.setReptitle(title);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				workreport.setTimestart(cal.getTime());// 下周一
				cal.add(Calendar.WEEK_OF_MONTH, 1);
				 cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				workreport.setTimeend(cal.getTime());// 下周日
			} else if (type.equals("D")) {
				String daytitle = "日报" + "("
						+ df.format(selectedRowData.getTimestart()) + ")"
						+ "--" + AppUtils.getUserSession().getUsercode();
				workreport.setReptitle(daytitle);
				workreport.setTimeend(selectedRowData.getTimeend());
				workreport.setTimestart(selectedRowData.getTimestart());
			} else {
				workreport.setReptitle(selectedRowData.getReptitle());
				workreport.setTimeend(selectedRowData.getTimeend());
				workreport.setTimestart(selectedRowData.getTimestart());
			}
		}
		if (selectedRowData.getRepcontent() != null
				&& !"".equals(selectedRowData.getRepcontent())) {
			workreport.setRepcontent(selectedRowData.getRepcontent());
		}
		workreport.setRep2userid(selectedRowData.getRep2userid());
		workreport.setReptype(selectedRowData.getReptype());
		workreport.setWeekno(selectedRowData.getWeekno() + 1);
		try {
			serviceContext.workReportService().saveData(workreport);
			this.refresh();
			alert("ok");
		} catch (Exception e) {
			alert("数据异常,复制新增失败!");
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前时间处于一年中第多少周数
	 */
	public Long getWeekOfYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(new Date());
		return Long.valueOf(String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));
	}
	
	/**
	 * 根据报告类型发生变化来确定 起始结束时间(以当前时间为准来计算日期范围)
	 */
	public void timeForReptype(String type){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat df3 = new SimpleDateFormat("yyyy");
		String username = AppUtils.getUserSession().getUsercode();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		if (type != null && !"".equals(type)) {
			if (type.equals("D")) {// 日报
				this.selectedRowData.setTimestart(date);
				this.selectedRowData.setTimeend(date);
				this.centercontext = "日报" + "("+ df.format(date)+")"+"--"+username;//日报显示内容
			} else if (type.equals("W")) {// 周报
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				this.selectedRowData.setTimestart(cal.getTime());// 获得本周周一日期
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				cal.add(Calendar.WEEK_OF_YEAR, 1);
				this.selectedRowData.setTimeend(cal.getTime());// 获得本周周末日期
				this.centercontext = "周报" + "("+ nowweeks.toString() +"周"+")" + "--"+username;//周报显示内容
			}else if (type.equals("M")){//月报
				cal.set(Calendar.DAY_OF_MONTH, 1);
				this.selectedRowData.setTimestart(cal.getTime());//获得本月的第一天
				String start = df2.format(cal.getTime());
				cal.roll(Calendar.DAY_OF_MONTH, -1);
				this.selectedRowData.setTimeend(cal.getTime());//获得本月的最后一天
				this.centercontext = "月报" + "(" + start + "月份" + ")" + "--"+username;
			}else if (type.equals("Y")){//年报
				cal.set(Calendar.DAY_OF_YEAR, 1);
				this.selectedRowData.setTimestart(cal.getTime());//获得本年的第一天
				cal.roll(Calendar.DAY_OF_YEAR, -1);
				this.selectedRowData.setTimeend(cal.getTime());//获得本年的最后一天
				this.centercontext = "年报" + "(" + df3.format(cal.getTime()) + "年" + ")" + "--"+username;
			}else{this.centercontext=""; alert("没有查询到起末时间范围!");};
			this.selectedRowData.setReptitle(this.centercontext);
			update.markUpdate(true, UpdateLevel.Data,
			"datefirst");
			update.markUpdate(true, UpdateLevel.Data,
			"dateend");
			update.markUpdate(true, UpdateLevel.Data,
			"reptitle");
			update.markUpdate(true, UpdateLevel.Data,
			"rep2userid");
		}
	}
	/**
	 * 汇报时间发生变化
	 */
	@Action
	public void changeReptype(){
		String reptype = AppUtils.getReqParam("reptype");
		this.timeForReptype(reptype);
		if(reptype.equals("W")){
			weeks.setDisabled(false);
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			update.markUpdate(true, UpdateLevel.Data, "massagepanel");
			update.markUpdate(true, UpdateLevel.Data, "weeks");
		}
	}
	
	/**
	 * 编辑界面周数发生变化
	 */
	@Action
	public void weeknochange(){
		String weeknos = AppUtils.getReqParam("weeknos");
		this.nowweeks = Long.valueOf(weeknos);
		this.centercontext = "周报" + "("+ nowweeks.toString() +"周"+")" + "--"+AppUtils.getUserSession().getUsercode();
		this.selectedRowData.setReptitle(this.centercontext);
		update.markUpdate(true, UpdateLevel.Data,
		"weeks");
	}
	
	/**
	 * 获取刚生成selectedRowData对象的id
	 */
	public Long getNewId(String name){
		String sql = " SELECT  id  FROM oa_workreport WHERE inputer ='"+name+"' ORDER BY inputtime DESC ";
		 List list = serviceContext.workReportService().workReportDao.executeQuery(sql);
		 if(list != null && list.size()> 0){
			 //System.out.println("刚生成工作报告id:"+Long.valueOf(list.get(0).toString()));
			return Long.valueOf(list.get(0).toString());
		 }
		 return null;
	}
	
	@Action
	public void sendMail() {
		 String issendemail = getIsemailsent();
		 if(issendemail.equals("true")){
			 alert("邮件已发送,请不要重复发送!");
			 return;
		 }
		 Long workreportid = this.getGridSelectId();
		 String sql = "SELECT id FROM sys_email WHERE isdelete = FALSE AND linkid  = " + workreportid;
		  Map map;
		try {
			map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		    Long emailid = Long.valueOf(map.get("id").toString());
			String url = AppUtils.getContextPath() + "/pages/sysmgr/mail/emailsendedit.xhtml?type=workreoport&id="+emailid;
			AppUtils.openWindow("_sendMail_workreport", url);
		} catch (Exception e) {
			alert("此工作报告尚未自动生成邮件,请进入编辑界面点击保存按钮!(此保存功能,将自动生成一份工作报告邮件,根据截止时间来判断是否自动发送这封邮件!)");
			e.printStackTrace();
		}
	}
	/**
	 * 此处判断一封邮件是否只对应一条工作报告数据
	 */
	public String getCount(Long reportid){
		String sql = "SELECT COUNT(*) FROM sys_email WHERE isdelete = FALSE AND linkid  = " + reportid;
		Map	map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
	    Long count = Long.valueOf(map.get("count").toString());
	    if(count == 0 ){
	    	return "0";//处于新增id,没有生成邮件
	    }else if(count ==1){
	    	return "1";//处于修改
	    }else{return "2";}//异常，生成多条邮件!
	}
	/*
	 * 根据rep2userid，找到对应的邮箱地址
	 */
	public String getrep2useridEmail(Long rep2userid){
			String sql = "SELECT email1, email2 FROM sys_user WHERE isdelete = FALSE AND id = "+rep2userid;
			 Map map;
		try {
			map = serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			String eamil1 = map.get("email1").toString();
			String eamil2 = map.get("email2").toString();
			if (!StrUtils.isNull(eamil1)) {// 以eamil1为主
				return eamil1;
			} else if (!StrUtils.isNull(eamil2)) {
				return eamil2;
			} else {
				return null;
			}
		} catch (Exception e) {
			alert("系统中没有寻找到此用户名或发生异常!");
			e.printStackTrace();
			return null;
		}
	}
	
	public SysEmail getsysEmailForId(Long emailid){
		String sql = " isdelete = FALSE  AND  linkid = " + emailid;
		return serviceContext.sysEmailService.sysEmailDao.findOneRowByClauseWhere(sql);
	}
	
	public String getIsemailsent(){
		String sql = "  id = " +  this.getGridSelectId();
		OaWorkReport RowData = this.serviceContext.workReportService().workReportDao.findOneRowByClauseWhere(sql);
		 return RowData.getIsemailsent().toString();
	}
	/**
	 * 获得POP3发送人邮箱地址
	 */
	public String getPop3Email(){
		String sql = "SELECT val FROM sys_configuser WHERE key = 'email_pop3_account' AND userid  =  " + AppUtils.getUserSession().getUserid();
		 try {
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			 return map.get("val").toString();
		} catch (Exception e) {
			alert("无法获取您的邮箱地址,请查看是否设置邮箱!");
			e.printStackTrace();
			return null;
			
		}
	}
}
