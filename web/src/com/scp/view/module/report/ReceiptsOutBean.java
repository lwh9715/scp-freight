package com.scp.view.module.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UICardLayout;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.sys.SysReport;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.ConfigUtils;
import com.scp.util.DateTimeUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.report.receiptsoutBean", scope = ManagedBeanScope.REQUEST)
public class ReceiptsOutBean extends GridFormView {
	
	@SaveState
	@Accessible
	public SysReport selectedRowData = new SysReport();

	@Bind
	@SaveState
	public String argsSale = "";

	@Bind
	@SaveState
	public String argsClientele = "";

	@Bind
	@SaveState
	public String customerclassify = "customerofarap";
	
	@Bind
	@SaveState
	public String arap_p2 = "arandap";
	
	@Bind
	@SaveState
	public String argsDepartment = "";
	
	@Bind
	private String dateclassify = "jobdate";
	
	@Bind
	private String dateclassify_p2 = "jobdate";
	
	@Bind
	@SaveState
	public Date jobDateFrom  = new Date();

	@Bind
	@SaveState
	public Date jobdateTo = new Date();
	
	@Bind
	@SaveState
	public Date ym = new Date();
	
	@Bind
	@SaveState
	public Date ymd = new Date();
	
	@Bind
	@SaveState
	public Date jobDateFrom_p2 = new Date();
	
	@Bind
	@SaveState
	public Date jobdateTo_p2 = new Date();
	
	@Bind
	@SaveState
	public Date ym_p2 = new Date();
	
	@Bind
	@SaveState
	public Date ymd_p2 = new Date();
	
	@Bind
	@SaveState
	public boolean isReadOnly = false;
	
	@Bind
	@SaveState
	public boolean lazyfilter;
	
	@Bind
	@SaveState
	public Date dateXrate = new Date();
	
	@Bind
	@SaveState
	public Date dateRPBefore = new Date();
	
	@SaveState
	@Accessible
	public String filename;
	
	@Bind
	@SaveState
	public String currency = "";
	
	@Bind
	@SaveState
	public String currency_p2 = "";
	
	@Bind
	private String datetype = "YM";
	
	@Bind
	private String datetype_p2 = "YM";
	
	@Bind
	@SaveState
	public String reporttype = "";
	
	@Bind
	@SaveState
	public boolean currentAccount = true;
	
	@Bind
	@SaveState
	public boolean currentAccount_p2 = true;
	
	@Bind
	@SaveState
	public boolean isnos ;
	
	@Bind
	@SaveState
	private String ppcc;
	
	@Bind
	@SaveState
	private String ppcc_p2;
	
	
	@Bind
	@SaveState
	private String jobno;
	@Bind
	@SaveState
	private String refno;
	@Bind
	@SaveState
	private String actpayrecno;
	@Bind
	@SaveState
	private String notesdesc;
	
	@SaveState
	@Bind
	public Boolean dateRPBeforeCheck;
	
	@SaveState
	@Bind
	public Boolean dateXrateCheck;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameClientele;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameSale;
	
	@Bind
    private UICardLayout layoutinfo;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameBank;
	
	@Bind
	@SaveState
	public String argBanks = "";
	
	@Bind
	@SaveState
	public String corpid = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCorpid;
	
	@Bind
	@SaveState
	public String corpidop = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCorpidop;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			dtlIFrameClientele.setSrc("./qry/dtlIFrameClientele.xhtml");
			dtlIFrameSale.setSrc("./qry/dtlIFrameSale.xhtml");
//			dtlIFrameDepartment.setSrc("./qry/dtlIFrameDepartment.xhtml");
			dtlIFrameBank.setSrc("./qry/dtlIFrameBank.xhtml");
			update.markAttributeUpdate(dtlIFrameClientele, "src");
			update.markAttributeUpdate(dtlIFrameSale, "src");
//			update.markAttributeUpdate(dtlIFrameDepartment, "src");
			dtlIFrameCorpid.setSrc("qry/dtlIFrameCorpid.xhtml?type=corpid");
			update.markAttributeUpdate(dtlIFrameCorpid, "src");
			dtlIFrameCorpidop.setSrc("qry/dtlIFrameCorpid.xhtml?type=corpidop");
			update.markAttributeUpdate(dtlIFrameCorpidop, "src");
			
			
			String cyidUser = ConfigUtils.findUserCfgVal("fee_profits_cy2", AppUtils.getUserSession().getUserid());
			if(!StrUtils.isNull(cyidUser)){
				this.currency = cyidUser;
				this.currency_p2 = cyidUser;
			}else{
				this.currency = "USD";
				currency_p2 = "USD";
			}
			
			lazyfilter = true;
		}
	}
	
	@Action
	public void updateBank(){
		if(argBanks!=null && argBanks.isEmpty()){
			dtlIFrameBank.setSrc("./qry/dtlIFrameBank.xhtml");
			update.markAttributeUpdate(dtlIFrameBank, "src");
		}
	}
	
	@Action
	public void updateClientele(){
		if(argsClientele!=null && argsClientele.isEmpty()){
			dtlIFrameSale.setSrc("./qry/dtlIFrameSale.xhtml");
			update.markAttributeUpdate(dtlIFrameSale, "src");
		}
	}
	
	
	@Action
    public void datetype_onchange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
	        if("YM".equals(datetype)){
	        	ym = new Date();
			}else if("YMD Before".equals(datetype)){
				ymd = df.parse(DateTimeUtil.getLastDay());
			}else if("YMD".equals(datetype)){
				jobDateFrom  = df.parse(DateTimeUtil.getFirstDay());
				jobdateTo = df.parse(DateTimeUtil.getLastDay());
			}
        } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Action
    public void datetype_p2_onchange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
	        if("YM".equals(datetype_p2)){
	        	ym_p2 = new Date();
			}else if("YMD Before".equals(datetype_p2)){
				ymd_p2 = df.parse(DateTimeUtil.getLastDay());
			}else if("YMD".equals(datetype_p2)){
				jobDateFrom_p2  = df.parse(DateTimeUtil.getFirstDay());
				jobdateTo_p2 = df.parse(DateTimeUtil.getLastDay());
			}
        } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void grid_onrowselect(){
		super.grid_onrowselect();
		this.pkVal = getGridSelectId();
		doServiceFindData();
		String infos = selectedRowData.getInfo();
		reporttype = selectedRowData.getFilename();
		isReadOnly = selectedRowData.getIsreadonly();
	}
	
	@Override
	public void grid_ondblclick() {
		//super.grid_ondblclick();
//		this.pkVal = getGridSelectId();
//		doServiceFindData();
//		String infos = selectedRowData.getInfo();
//		reporttype = selectedRowData.getFilename();
//		report();
		Browser.execClientScript("reportJsVar.fireEvent('click')");
	}
	
	
	@Action
	public void report() {
		if(reporttype == null || reporttype.isEmpty()){
			MessageUtils.alert("请先选择需要查询的统计表!");
			return;
		}
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("user", AppUtils.getUserSession().getUsercode());

		
		if("YM".equals(datetype)){
			argsMap.put("jobdatetype","YM");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM").format(ym));
		}else if("YMD Before".equals(datetype)){
			argsMap.put("jobdatetype","YMDBefore");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd").format(ymd));
		}else if("YMD".equals(datetype)){
			argsMap.put("jobdatetype","YMD");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd").format(jobDateFrom)+ "," + new SimpleDateFormat("yyyy-MM-dd").format(jobdateTo));
		}
		argsMap.put("currency", currency);
		argsMap.put("datetype", datetype);
		argsMap.put("dateclassify", dateclassify);
		argsMap.put("customerclassify", customerclassify);
		argsMap.put("ppcc", ppcc);
		if(isnos){
			argsMap.put("isnos", "Y");
		}else if(isnos){
			argsMap.put("isnos", "N");
		}
		if(currentAccount){
			argsMap.put("currentAccount", "Y");
		}else{
			argsMap.put("currentAccount", "N");
		}
		
		if(dateRPBeforeCheck){
			argsMap.put("dateRPBefore",new SimpleDateFormat("yyyy-MM-dd").format(dateRPBefore));
		}
		if(dateXrateCheck){
			argsMap.put("dateXrate",new SimpleDateFormat("yyyy-MM-dd").format(dateXrate));
		}
		
		argsMap.put("userid", AppUtils.getUserSession().getUserid().toString());
		
		String urlArgs1 = AppUtils.map2Url(argsMap, "&");
		////System.out.println(urlArgs1);
//		argsMap.clear();
		Long corpidCurrent = AppUtils.getUserSession().getCorpidCurrent();
		////System.out.println(corpidCurrent);
		argsMap.put("corpid",AppUtils.base64Encoder(String.valueOf(corpidCurrent)));
		argsMap.put("salesid", argsSale);
		argsMap.put("department", argsDepartment);
		argsMap.put("clienteleid", argsClientele);
		argsMap.put("banksid", argBanks);
		argsMap.put("corpidj",corpid);
		argsMap.put("corpidop",corpidop);
		
		argsMap.put("jobno", jobno);
		argsMap.put("refno", refno);
		argsMap.put("actpayrecno", actpayrecno);
		argsMap.put("notesdesc", notesdesc);
		
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
		//System.out.println(urlArgs2);
		String arg = urlArgs1 + "&" + "para=" + urlArgs2;
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/"+(isReadOnly?"showReportReadOnly.jsp":"showReport.jsp")+"?raq=/static/rp/" + reporttype;
//		if("DEBIT_CREDIT_NOTE.raq".equals(reporttype)){
//			if(argsClientele == null || argsClientele.isEmpty() || argsClientele.split(",").length > 1){
//				MessageUtils.alert("收付款对账单(英文)表必须且只能选择一位客户!请选择后重新查看报表");
//				return;
//			}
//		}
		//2712 统计报表预览前增加检查 右边列表条件部分，如果一个都没选提示并返回，提示：请选择过滤条件
		if(StrUtils.isNull(argsMap.get("clienteleid").toString())&&StrUtils.isNull(argsMap.get("salesid").toString())
				&&StrUtils.isNull(argsMap.get("banksid").toString())&&StrUtils.isNull(argsMap.get("corpid").toString())&&StrUtils.isNull(argsMap.get("corpidop").toString())
				
		){
			MessageUtils.alert("请选择过滤条件!");
			return;
		}
		AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
		
		
	}
	@Action
	public void report_p2() {
		if(reporttype == null || reporttype.isEmpty()){
			MessageUtils.alert("请先选择需要查询的统计表!");
			return;
		}
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("user", AppUtils.getUserSession().getUsercode());
		
		
		if("YM".equals(datetype_p2)){
			argsMap.put("jobdatetype","YM");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM").format(ym_p2));
		}else if("YMD Before".equals(datetype_p2)){
			argsMap.put("jobdatetype","YMDBefore");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd").format(ymd_p2));
		}else if("YMD".equals(datetype_p2)){
			argsMap.put("jobdatetype","YMD");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd").format(jobDateFrom_p2)+ "," + new SimpleDateFormat("yyyy-MM-dd").format(jobdateTo_p2));
		}
		argsMap.put("currency", currency_p2);
		argsMap.put("datetype", datetype_p2);
		argsMap.put("dateclassify", dateclassify_p2);
		argsMap.put("customerclassify", customerclassify);
		argsMap.put("ppcc", ppcc_p2);
		argsMap.put("arap", arap_p2);
		if(isnos){
			argsMap.put("isnos", "Y");
		}else if(isnos){
			argsMap.put("isnos", "N");
		}
		if(currentAccount_p2){
			argsMap.put("currentAccount", "Y");
		}else{
			argsMap.put("currentAccount", "N");
		}
		
		if(dateRPBeforeCheck){
			argsMap.put("dateRPBefore",new SimpleDateFormat("yyyy-MM-dd").format(dateRPBefore));
		}
		if(dateXrateCheck){
			argsMap.put("dateXrate",new SimpleDateFormat("yyyy-MM-dd").format(dateXrate));
		}
		
		String urlArgs1 = AppUtils.map2Url(argsMap, "&");
		Long corpidCurrent = AppUtils.getUserSession().getCorpidCurrent();
		argsMap.put("corpid",AppUtils.base64Encoder(String.valueOf(corpidCurrent)));
		argsMap.put("salesid", argsSale);
		argsMap.put("department", argsDepartment);
		argsMap.put("clienteleid", argsClientele);
		argsMap.put("banksid", argBanks);
		argsMap.put("corpidj",corpid);
		argsMap.put("corpidop",corpidop);
		
		argsMap.put("jobno", jobno);
		argsMap.put("refno", refno);
		argsMap.put("actpayrecno", actpayrecno);
		argsMap.put("notesdesc", notesdesc);
		
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
		//System.out.println(urlArgs2);
		String arg = urlArgs1 + "&" + "para=" + urlArgs2;
		String openUrl = AppUtils.getRptUrl()
		+ "/reportJsp/"+(isReadOnly?"showReportReadOnly.jsp":"showReport.jsp")+"?raq=/static/rp/" + reporttype;
		
		if("Y".equals(ConfigUtils.findSysCfgVal("sys_rpt_required_condition"))){
			//2712 统计报表预览前增加检查 右边列表条件部分，如果一个都没选提示并返回，提示：请选择过滤条件
			if(StrUtils.isNull(argsMap.get("clienteleid").toString())&&StrUtils.isNull(argsMap.get("salesid").toString())
					&&StrUtils.isNull(argsMap.get("banksid").toString())&&StrUtils.isNull(argsMap.get("corpid").toString())&&StrUtils.isNull(argsMap.get("corpidop").toString())
					
			){
				MessageUtils.alert("请选择右边列表过滤条件!");
				return;
			}
		}
		
		AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
		
		
	}
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.sysReportMgrService.sysReportDao
				.findById(this.pkVal);

	}
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		StringBuilder filter =  new StringBuilder();
		filter.append("\n AND (EXISTS(SELECT 1 FROM sys_report_ctrl x WHERE x.reportid = t.id AND x.linktype = 'U' AND x.linkid = "+AppUtils.getUserSession().getUserid()+")");
		filter.append("\n 	OR EXISTS(SELECT 1 FROM sys_report_ctrl c ");
		filter.append("\n 	WHERE c.linktype = 'M'");
		filter.append("\n 	AND c.reportid = t.id");
		filter.append("\n 	AND EXISTS(SELECT 1 FROM sys_role r WHERE r.isdelete = FALSE AND c.linkid = r.id");
		filter.append("\n 	AND EXISTS(SELECT 1 FROM sys_userinrole x WHERE x.isdelete = FALSE AND x.roleid = r.id");
		filter.append("\n 	AND EXISTS(SELECT 1 FROM sys_user y WHERE y.isdelete = FALSE AND y.id = x.userid AND y.id = "+AppUtils.getUserSession().getUserid()+")))))");
		filter.append("\n 	AND isdelete = false");
		map.put("filter", filter);
		boolean isAll = false ;
		if(generals.equals("general")){
			isAll = true;
		}else if(generals.equals("notgeneral")){
			isAll = false;
		}
		StringBuilder setgeneralsql =  new StringBuilder();
		setgeneralsql.append("\n AND 1 = 1");
		if(!generals.equals("all")){
			setgeneralsql.append("\n AND EXISTS(SELECT 1 FROM sys_report_config WHERE reportid = t.id AND rptype = 'rece' AND userid = ") ;
			setgeneralsql.append("\n" + AppUtils.getUserSession().getUserid()+" AND isuse = "+ isAll + ")");
		}
		map.put("setgeneralsql", setgeneralsql.toString());
		return map;
	}
	@Override
	protected void doServiceSave() {
	}
	@Action(id="customerclassify",event="onchange")
	public void filterIframeClientele(){
		if(lazyfilter){
			lazyfilter = !lazyfilter;
			update.markUpdate(UpdateLevel.Data,"lazyfilter");
		}else{
			if(!lazyfilter&&!StrUtils.isNull(customerclassify)){
				Browser.execClientScript("document.getElementById('dtlIFrameClientele').contentWindow.filterIframeClientele('"+customerclassify+"');");
			}
		}
	}
	
	@Bind
	@SaveState
	public String generals = "all";
	
	@Action
	public void setgeneral(){
		setgeneralornot(true);
	}
	
	@Action
	public void setnotgeneral(){
		setgeneralornot(false);
	}
	
	public void setgeneralornot(boolean generalor){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请至少选择一行记录");
			return;
		}
		//serviceContext.sysReportConfigService.initialize(this.selectedRowData.getModcode(), generalor);
		for(String id : ids){
			serviceContext.sysReportConfigService.saveSetUsercinfig(this.selectedRowData.getModcode(), id, generalor);
		}
		super.refresh();
		alert("OK");
	}
	
	//2387 统计报表部分修改调整（加一个月到第一天至最后一天）
	@Action
	public void monthUp(){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			ym = CommonUtil.DateDayUp(sdf.parse(sdf.format(ym)+"-01"),-1);
			ymd = CommonUtil.DateDayUp(sdf.parse(sdf.format(ymd)+"-01"),-1);
			jobDateFrom = CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobDateFrom)+"-01"),-1);
			jobdateTo = CommonUtil.DateDayUp(sdf.parse(sdf.format(jobdateTo)+"-01"),-1);
			update.markUpdate(true, UpdateLevel.Data, "listPanel");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Action
	public void monthDown(){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			ym = CommonUtil.DateMonthUp(sdf.parse(sdf.format(ym)+"-01"),1);
			ymd = CommonUtil.DateDayUp(CommonUtil.DateMonthUp(sdf.parse(sdf.format(ymd)+"-01"),2),-1);
			jobDateFrom = CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobDateFrom)+"-01"),1);
			jobdateTo = CommonUtil.DateDayUp(CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobDateFrom)+"-01"),1),-1);
			update.markUpdate(true, UpdateLevel.Data, "listPanel");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//2387 统计报表部分修改调整（加一个月到第一天至最后一天）
	@Action
	public void monthUp2(){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			ym_p2 = CommonUtil.DateDayUp(sdf.parse(sdf.format(ym_p2)+"-01"),-1);
			ymd_p2 = CommonUtil.DateDayUp(sdf.parse(sdf.format(ymd_p2)+"-01"),-1);
			jobDateFrom_p2 = CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobDateFrom_p2)+"-01"),-1);
			jobdateTo_p2 = CommonUtil.DateDayUp(sdf.parse(sdf.format(jobdateTo_p2)+"-01"),-1);
			update.markUpdate(true, UpdateLevel.Data, "listPanel");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Action
	public void monthDown2(){
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			ym_p2 = CommonUtil.DateMonthUp(sdf.parse(sdf.format(ym_p2)+"-01"),1);
			ymd_p2 = CommonUtil.DateDayUp(CommonUtil.DateMonthUp(sdf.parse(sdf.format(ymd_p2)+"-01"),2),-1);
			jobDateFrom_p2 = CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobDateFrom_p2)+"-01"),1);
			jobdateTo_p2 = CommonUtil.DateDayUp(CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobdateTo_p2)+"-01"),1),-1);
			update.markUpdate(true, UpdateLevel.Data, "listPanel");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
