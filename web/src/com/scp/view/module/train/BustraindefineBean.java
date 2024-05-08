package com.scp.view.module.train;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.exception.NoRowException;
import com.scp.model.bus.BusTrain;
import com.scp.model.finance.FinaJobs;
import com.scp.model.sys.SysReport;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.EncryptUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
@ManagedBean(name = "pages.module.train.bustraindefineBean", scope = ManagedBeanScope.REQUEST)
public class BustraindefineBean extends FormView {
	
	@SaveState
	@Accessible
	public BusTrain selectedRowData = new BusTrain();
	
	@SaveState
	@Bind
	public String billid;

	@Bind
	@SaveState
	public String control;
	
	@Bind
	@SaveState
	public boolean isadmin;
	
	@Bind
	@SaveState
	public String userid;
	
	@Bind
	@SaveState
	public String baseurl;
	
	@Bind
	@SaveState
	public String rptUrl;
	
	@Bind
	public String hblrptcheck;
	
	@Bind
	@SaveState
	public String billprintlock;
	
	@SaveState
	public String language;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String id = AppUtils.getReqParam("id");
		language = AppUtils.getUserSession().getMlType().toString();
		
		initControl();
		if(!StrUtils.isNull(id)){
			billid = id;
			
			this.refresh();
		}
	}	
	
	
	
	@Override
	public void refresh() {
		selectedRowData = this.serviceContext.busTrainMgrService.busTrainDao.findById(Long.parseLong(billid));
		try {
			baseurl = ConfigUtils.findSysCfgVal(ConfigUtils.SysCfgKey.rpt_srv_url.name()); 
		} catch (Exception e) {
		}
		
		ismake = selectedRowData.getIsmake();
		isadjust = selectedRowData.getIsadjust();
		isconfirm = selectedRowData.getIsconfirm();
		isprint = selectedRowData.getIsprint();
		issign = selectedRowData.getIssign();
		iscomplete = selectedRowData.getIscomplete();
		
		
		ismakembl = selectedRowData.getIsmakembl();
		isadjustmbl = selectedRowData.getIsadjustmbl();
		isconfirmmbl = selectedRowData.getIsconfirmmbl();
		isprintmbl = selectedRowData.getIsprintmbl();
		isgetmbl = selectedRowData.getIsgetmbl();
		isreleasembl = selectedRowData.getIsreleasembl();
		
		mblRpt = selectedRowData.getMblrpt();
		hblRpt = selectedRowData.getHblrpt();
		orderRpt = selectedRowData.getOrderrpt();
		bookRpt = 	selectedRowData.getBookrpt();
		isreleasemblwo = selectedRowData.getIsreleasemblwo();
		isreleasehblwo = selectedRowData.getIsreleasehblwo();
		
		this.userid = AppUtils.getUserSession().getUserid().toString();
		rptUrl = AppUtils.getRptUrl();
		
		//1496 系统设置页面调整，及增加提单预览前检查设置
		String hblrpt_check = ConfigUtils.findSysCfgVal("bu_shipping_hblrpt_check");
		if(hblrpt_check!=null&&hblrpt_check.equals("Y")){
			try{
				String sql = "SELECT f_bus_shipping_hblrpt_check('jobid="+selectedRowData.getJobid()+"') AS hblrptcheck";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				hblrptcheck = m.get("hblrptcheck").toString();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		
		billprintlock = ConfigUtils.findSysCfgVal("sys_billprintlock");
		
		super.refresh();
	}



	public void initControl(){
		isadmin = false;
		this.control = AppUtils.base64Encoder("false");
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.bus_ship_bill.getValue())) {
			if (s.endsWith("_templete")) {
				this.control = AppUtils.base64Encoder("true");
				isadmin = true;
			}
		}
		
		update.markUpdate(UpdateLevel.Data,"control");	
	}
	
	@Bind
	@SaveState
	public String hblRpt;
	
	
	@Action(id="hblRpt",event="onselect")
	public void hblRptonselct(){
		if(!isadmin && hblRpt!=null && !hblRpt.isEmpty()){
			String where = " isdelete = FALSE AND userid IS NOT NULL AND filename = '"+hblRpt+"' ";
			SysReport sysReport = null;
			try {
				sysReport = serviceContext.sysReportMgrService.sysReportDao.findOneRowByClauseWhere(where);
				if(AppUtils.getUserSession().getUserid().equals(sysReport.getUserid())){
					this.control = AppUtils.base64Encoder("true");
				}else{
					this.control = AppUtils.base64Encoder("false");
				}
			} catch (NoRowException e) {
				this.control = AppUtils.base64Encoder("false");
			}catch (Exception e) {
				e.printStackTrace();
				this.control = AppUtils.base64Encoder("false");
			}finally{
				Browser.execClientScript("updatec('"+control+"');");
			}
		}
	}
	
//	@Action
//	public void printHbl() {
//		String rpturl = AppUtils.getRptUrl();
//		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
//				+ hblRpt;
//		String upg = "UPDATE bus_train set hblrpt = '" + hblRpt + "' where id = " + billid + ";";
//		this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
//		AppUtils.openWindow("_shippingHblReport", openUrl + getArgs());
//	}
	
	@Action
	public void printattach(){
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
				+ "HBL(GSIT_ORG)-ATTACHMENT.raq";
		AppUtils.openWindow("_shippingHblReport", openUrl + getArgs());
	}
	
	@Action
	public void printattachmbl(){
		
		if("Y".equals(billprintlock)){
			if(isprintmbl){
				MessageUtils.alert("MBL has been printed and cannot be printed again!");
				return;
			}
		}
		
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/"+ "train_MBL.raq";
		AppUtils.openWindow("_shippingHblReport", openUrl + getArgs());
	}
	
	@Action
	public void savelastbill(){
		String hblrptjs = AppUtils.getReqParam("hblrptjs");
		String upg = "UPDATE bus_train set hblrpt = '" + hblrptjs + "' where id = " + billid + ";";
		this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
	}
	
	@Action
	public void savelastbillmbl(){
		String mblrptjs = AppUtils.getReqParam("mblrptjs");
		String upg = "UPDATE bus_train set mblrpt = '" + mblrptjs + "' where id = " + billid + ";";
		this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
	}
	
	@Action
	public void hblRegister() {
		String openUrl = "./billregister.xhtml?billnos="+selectedRowData.getHblno()+"&billid="+billid;
		AppUtils.openWindow("_billregister", openUrl);
	}
	
	@SaveState
	@Bind
	public String mblRpt;
	
	@Action(id="mblRpt",event="onselect")
	public void mblRptOnselect(){
		if(isadmin && mblRpt!=null && !mblRpt.isEmpty()){
			String where = " isdelete = FALSE AND userid IS NOT NULL AND filename = '"+mblRpt+"' ";
			SysReport sysReport = null;
			try {
				sysReport = serviceContext.sysReportMgrService.sysReportDao.findOneRowByClauseWhere(where);
				if(AppUtils.getUserSession().getUserid().equals(sysReport.getUserid())){
					this.control = AppUtils.base64Encoder("true");
				}else{
					this.control = AppUtils.base64Encoder("false");
				}
			} catch (NoRowException e) {
				this.control = AppUtils.base64Encoder("false");
			}catch (Exception e) {
				e.printStackTrace();
				this.control = AppUtils.base64Encoder("false");
			}finally{
				Browser.execClientScript("updatec('"+control+"');");
			}
		}
	}
	
	@Action
	public void printMbl() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
				+ mblRpt;
		String upg = "UPDATE bus_train set mblrpt = '" + mblRpt + "' where id = " + billid + ";";
		this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
		AppUtils.openWindow("_shippingMblReport", openUrl + getArgs());
	}
	
	@SaveState
	@Bind
	public String orderRpt;
	
	@Action
	public void printOrder() {
		if(StrUtils.isNull(orderRpt)){
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
				+ orderRpt;
		String url = openUrl + getArgsusr();
		
		String upg = "UPDATE bus_train set orderrpt = '" + orderRpt + "' where id = " + billid + ";";
		this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
		
		AppUtils.openWindow("_shippingOrderReport",url);
	}
	private String getArgsusr() {
		BusTrain bs = serviceContext.busTrainMgrService.busTrainDao.findById(Long.parseLong(billid));
		String args = "&para=jobid="+bs.getJobid()+":userid="+AppUtils.getUserSession().getUserid()+":corpidcurrent="+AppUtils.getUserSession().getCorpid()+":";
		args = args+"&id=" + this.billid;
		args = args+"&userid=" + AppUtils.getUserSession().getUserid();
		return args;
	}
	
	@SaveState
	@Bind
	public String bookRpt;
	
	@Action
	public void printBook() {
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/define/"
				+ bookRpt;
		String upg = "UPDATE bus_train set bookrpt = '" + bookRpt + "' where id = " + billid + ";";
		this.serviceContext.userMgrService.sysUserDao.executeSQL(upg);
		AppUtils.openWindow("_shippingBookReport", openUrl + getArgs());
	}
	
	
	private String getArgs() {
		String args = "";
		args += "&id=" + this.billid;
		return args;
	}
	
	@Bind(id = "hblrpt")
	public List<SelectItem> getHblrpt() {
		try {
			if("demo".equalsIgnoreCase(AppUtils.getUserSession().getUsercode())){
				return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
						"sys_report AS d", "WHERE modcode='trainhbl' AND isdelete = FALSE ",
				"order by code");
			}else{
				String sql = "WHERE modcode='trainhbl' AND isdelete = FALSE AND (userid IS NULL OR ispublic OR userid = "+AppUtils.getUserSession().getUserid()+")";
				String qry = "\nAND (d.corpid iS NULL OR (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+")))";
				if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())sql += qry;//非saas模式不控制
				return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
						"sys_report AS d", sql,
						"order by code");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind(id = "mblrpt")
	public List<SelectItem> getMblrpt() {
		try {
			if("demo".equalsIgnoreCase(AppUtils.getUserSession().getUsercode())){
				return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
						"sys_report AS d", "WHERE modcode='trainmbl' AND isdelete = FALSE ",
				"order by code");
			}else{
				String sql = "WHERE modcode='trainmbl' AND isdelete = FALSE AND (userid IS NULL OR ispublic OR userid = "+AppUtils.getUserSession().getUserid()+")";
				String qry = "\nAND (d.corpid iS NULL OR (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+")))";
				if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())sql += qry;//非saas模式不控制
				return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
						"sys_report AS d", sql,
				"order by code");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind(id = "orderrpt")
	public List<SelectItem> getOrderrpt() {
		try {
			String sql = "WHERE modcode='trainorder' AND isdelete = FALSE";
			String qry = "\nAND (d.corpid iS NULL OR (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+")))";
			if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())sql += qry;//非saas模式不控制
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", sql,
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind(id = "bookrpt")
	public List<SelectItem> getBookrpt() {
		try {
			String sql = "WHERE modcode='trainbook' AND isdelete = FALSE";
			String qry = "\nAND (d.corpid iS NULL OR (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+")))";
			if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())sql += qry;//非saas模式不控制
			return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
					"sys_report AS d", sql,
					"order by code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind
	private Boolean ismake = false;
	
	@Bind
	public Boolean isadjust = false;
	
	@Bind
	public Boolean isconfirm = false;
	
	@Bind
	public Boolean isprint = false;
	
	@Bind
	public Boolean issign = false;
	
	@Bind
	public Boolean iscomplete = false;
	
	@Bind
	private Boolean ismakembl = false;
	
	@Bind
	public Boolean isadjustmbl = false;
	
	@Bind
	public Boolean isconfirmmbl = false;
	
	@Bind
	public Boolean isprintmbl = false;
	
	@Bind
	public Boolean isreleasembl = false;
	
	@Bind
	public Boolean isgetmbl = false;
	
	@Bind
	public Boolean isreleasehblwo = false;
	
	@Bind
	public Boolean isreleasemblwo = false;
	
	@Action
	public void ismake_oncheck(){
		selectedRowData.setIsmake(ismake);
		selectedRowData.setDatemake(ismake?Calendar.getInstance().getTime():null);
		selectedRowData.setUsermake(ismake?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isadjust_oncheck(){
		selectedRowData.setIsadjust(isadjust);
		selectedRowData.setDateadjust(isadjust?Calendar.getInstance().getTime():null);
		selectedRowData.setUseradjust(isadjust?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isconfirm_oncheck(){
		selectedRowData.setIsconfirm(isconfirm);
		selectedRowData.setDateconfirm(isconfirm?Calendar.getInstance().getTime():null);
		selectedRowData.setUserconfirm(isconfirm?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isprint_oncheck(){
		selectedRowData.setIsprint(isprint);
		selectedRowData.setDateprint(isprint?Calendar.getInstance().getTime():null);
		selectedRowData.setUserprint(isprint?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void issign_oncheck(){
		selectedRowData.setIssign(issign);
		selectedRowData.setDatesign(issign?Calendar.getInstance().getTime():null);
		selectedRowData.setUsersign(issign?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void iscomplete_oncheck(){
		selectedRowData.setIscomplete(iscomplete);
		selectedRowData.setDatecomplete(iscomplete?Calendar.getInstance().getTime():null);
		selectedRowData.setUsercomplete(iscomplete?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	
	@Action
	public void ismakembl_oncheck(){
		selectedRowData.setIsmakembl(ismakembl);
		selectedRowData.setDatemakembl(ismakembl?Calendar.getInstance().getTime():null);
		selectedRowData.setUsermakembl(ismakembl?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isadjustmbl_oncheck(){
		selectedRowData.setIsadjustmbl(isadjustmbl);
		selectedRowData.setDateadjustmbl(isadjustmbl?Calendar.getInstance().getTime():null);
		selectedRowData.setUseradjustmbl(isadjustmbl?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isconfirmmbl_oncheck(){
		selectedRowData.setIsconfirmmbl(isconfirmmbl);
		selectedRowData.setDateconfirmmbl(isconfirmmbl?Calendar.getInstance().getTime():null);
		selectedRowData.setUserconfirmmbl(isconfirmmbl?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isprintmbl_oncheck(){
		selectedRowData.setIsprintmbl(isprintmbl);
		selectedRowData.setDateprintmbl(isprintmbl?Calendar.getInstance().getTime():null);
		selectedRowData.setUserprintmbl(isprintmbl?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isgetmbl_oncheck(){
		selectedRowData.setIsgetmbl(isgetmbl);
		selectedRowData.setDategetmbl(isgetmbl?Calendar.getInstance().getTime():null);
		selectedRowData.setUsergetmbl(isgetmbl?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isreleasembl_oncheck(){
		selectedRowData.setIsreleasembl(isreleasembl);
		selectedRowData.setDatereleasembl(isreleasembl?Calendar.getInstance().getTime():null);
		selectedRowData.setUserreleasembl(isreleasembl?AppUtils.getUserSession().getUsercode():null);
		try {
			this.save();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isreleasehblwo_oncheck(){
		selectedRowData.setIsreleasehblwo(isreleasehblwo);
		this.save();
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Action
	public void isreleasemblwo_oncheck(){
		selectedRowData.setIsreleasemblwo(isreleasemblwo);
		this.save();
		refresh();
		Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
	}
	
	@Override
	public void save() {
//		super.save();
		this.serviceContext.busTrainMgrService.saveData(this.selectedRowData);
	}
	
	@Action
	public void deletehbl(){
		String hblraq = AppUtils.getReqParam("hblraq");
		if(hblraq == null || hblraq.isEmpty()){
			MessageUtils.alert("Data is NULL!");
			return;
		}
		StringBuilder sbsql = new StringBuilder();
		//demo可以删除   其他必须本人删除
		sbsql.append("\n UPDATE sys_report SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE ('"+AppUtils.getUserSession().getUsercode()+"'='demo' OR userid =");
		sbsql.append(AppUtils.getUserSession().getUserid());
		sbsql.append(" AND isdelete = FALSE AND userid IS NOT NULL)");
		sbsql.append("\n AND filename = '");
		sbsql.append(hblraq);
		sbsql.append("'");
		sbsql.append("\n AND modcode='trainhbl'");
		try {
			this.serviceContext.sysReportMgrService.sysReportDao.executeSQL(sbsql.toString());
			MessageUtils.alert("OK!");
			this.hblRpt = "";
			update.markUpdate(true,UpdateLevel.Data,"hblRpt");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void deletebook(){
		if(bookRpt == null || bookRpt.isEmpty()){
			MessageUtils.alert("Data is NULL!");
			return;
		}
		StringBuilder sbsql = new StringBuilder();
		//demo可以删除   其他必须本人删除
		sbsql.append("\n UPDATE sys_report SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE ('"+AppUtils.getUserSession().getUsercode()+"'='demo' OR userid =");
		sbsql.append(AppUtils.getUserSession().getUserid());
		sbsql.append(" AND isdelete = FALSE AND userid IS NOT NULL)");
		sbsql.append("\n AND filename = '");
		sbsql.append(bookRpt);
		sbsql.append("'");
		sbsql.append("\n AND modcode='trainbook'");
		try {
			this.serviceContext.sysReportMgrService.sysReportDao.executeSQL(sbsql.toString());
			MessageUtils.alert("OK!");
			this.hblRpt = "";
			update.markUpdate(true,UpdateLevel.Data,"bookRpt");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void deletembl(){
		String mblraq = AppUtils.getReqParam("mblraq");
		if(mblraq == null || mblraq.isEmpty()){
			MessageUtils.alert("Data is NULL!");
			return;
		}
		StringBuilder sbsql = new StringBuilder();
		//demo可以删除   其他必须本人删除
		sbsql.append("\n UPDATE sys_report SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE ('"+AppUtils.getUserSession().getUsercode()+"'='demo' OR userid =");
		sbsql.append(AppUtils.getUserSession().getUserid());
		sbsql.append("\n AND isdelete = FALSE AND userid IS NOT NULL)");
		sbsql.append("\n AND filename = '");
		sbsql.append(mblraq);
		sbsql.append("'");
		sbsql.append("\n AND modcode='trainmbl'");
		try {
			this.serviceContext.sysReportMgrService.sysReportDao.executeSQL(sbsql.toString());
			MessageUtils.alert("OK!");
			this.mblRpt = "";
			update.markUpdate(true,UpdateLevel.Data,"mblRpt");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow hxWindow;
	
	@Bind
	@SaveState
	private String hxurl;
	
	@Action
	public void onlineFeedLink(){
		StringBuilder sb = new StringBuilder();
		long currentTimeMillis = System.currentTimeMillis();//时间戳
		String ApiKey ="";
		try {
			ApiKey = apiKeyGenerate(currentTimeMillis+"",this.selectedRowData.getHblno());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(this.selectedRowData.getHblno()==null||this.selectedRowData.getHblno().equals("")){
			MessageUtils.alert("HBL No can not be null!");
			return;
		}
		String h = "HBL";
		String csUrlbase = ConfigUtils.findSysCfgVal("cs_url_base");
		//String csUrlbase = "http://127.0.0.1:8020/hx";
		String csUrlbasetime = ConfigUtils.findSysCfgVal("cs_url_base_time");//系统中URL有效时间
		int parseInt = csUrlbasetime==null||csUrlbasetime.equals("")?3:Integer.parseInt(csUrlbasetime);
		FinaJobs job = serviceContext.jobsMgrService.finaJobsDao.findById(this.selectedRowData.getJobid());
		if(job.getNos().equals("")){
			MessageUtils.alert("Job No. can not be null!");
			return;
		}
		String url = csUrlbase+"/si/esi/"+job.getNos()+"?tp="+currentTimeMillis+"&key="+ApiKey+"&billtype="+h;
		sb.append(url);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateformat.format(currentTimeMillis+parseInt * 60 * 60 * 1000).toString();
		sb.append("\n\n链接有效时间至："+time) ;
		String hblno = (selectedRowData.getHblno()==null?"":selectedRowData.getHblno());
		String sono = (selectedRowData.getSono()==null?"":selectedRowData.getSono());
		sb.append("\n\nSO:"+sono);
		sb.append("\n\nHBL:"+hblno);
		this.hxurl = sb.toString();
		update.markUpdate(true,UpdateLevel.Data,"hxurl");
		hxWindow.show();
		Browser.execClientScript("sono.setValue('"+sono.replaceAll("\n"," \\n ")+"');hblno.setValue('"+hblno+"');testUrl.setValue('"+url+"');time.setValue('"+time+"');refreshQRCode();");
	}
	
	@Action
	public void onlineFeedLinkmbl(){
		StringBuilder sb = new StringBuilder();
		long currentTimeMillis = System.currentTimeMillis();//时间戳
		String ApiKey ="";
		try {
			ApiKey = apiKeyGenerate(currentTimeMillis+"",this.selectedRowData.getMblno());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(this.selectedRowData.getMblno()==null||this.selectedRowData.getMblno().equals("")){
			MessageUtils.alert("MBL No. can not be null!");
			return;
		}
		String m = "MBL";
		String csUrlbase = ConfigUtils.findSysCfgVal("cs_url_base");
		//String csUrlbase = "http://127.0.0.1:8020/hx";
		String csUrlbasetime = ConfigUtils.findSysCfgVal("cs_url_base_time");//系统中URL有效时间
		int parseInt = csUrlbasetime==null||csUrlbasetime.equals("")?3:Integer.parseInt(csUrlbasetime);
		FinaJobs job = serviceContext.jobsMgrService.finaJobsDao.findById(this.selectedRowData.getJobid());
		if(job.getNos().equals("")){
			MessageUtils.alert("Job No. can not be null!");
			return;
		}
		String url = csUrlbase+"/si/esi/"+job.getNos()+"?tp="+currentTimeMillis+"&key="+ApiKey+"&billtype="+m;
		sb.append(url);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateformat.format(currentTimeMillis+parseInt * 60 * 60 * 1000).toString();
		sb.append("\n\n链接有效时间至："+time) ;
		String mblno = (selectedRowData.getMblno()==null?"":selectedRowData.getMblno());
		String sono = (selectedRowData.getSono()==null?"":selectedRowData.getSono());
		sb.append("\n\nSO:"+sono);
		sb.append("\n\nMBL:"+mblno);
		this.hxurl = sb.toString();
		update.markUpdate(true,UpdateLevel.Data,"hxurl");
		hxWindow.show();
		Browser.execClientScript("sono.setValue('"+sono+"');hblno.setValue('"+mblno+"');testUrl.setValue('"+url+"');time.setValue('"+time+"');refreshQRCodembl();");
	}
	
	
	public String apiKeyGenerate(String ApiSecret,String ApiSalt) throws UnsupportedEncodingException{
		return EncryptUtil.md5Encode(ApiSecret+ApiSalt,"UTF-8");
	}
}
