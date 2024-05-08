package com.scp.view.module.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.module.report.receivablespayablesBean", scope = ManagedBeanScope.REQUEST)
public class ReceivablesPayablesBean extends GridSelectView {

	@SaveState
	@Accessible
	public SysReport selectedRowData = new SysReport();

	
	@Bind
	@SaveState
	public String argsSale = "";

	@Bind
	@SaveState
	public String argsOperation = "";
	
	@Bind
	@SaveState
	public String argsClientele = "";
	
	@Bind
	@SaveState
	public String argsDepartment = "";

	@Bind
	@SaveState
	public String argsFeeitem = "";
	
	@Bind
	@SaveState
	public boolean lazyfilter;
	
	@Bind
	@SaveState
	public Date jobDateFrom  = new Date();

	@Bind
	@SaveState
	public Date jobdateTo = new Date();
	
	@Bind
	@SaveState
	public Integer ym_y;
	
	@Bind
	@SaveState
	public Integer ym_m;
	
	@Bind
	@SaveState
	public Integer ym_y2;
	
	@Bind
	@SaveState
	public Integer ym_m2;
	
	@Bind
	@SaveState
	public Date ymd = new Date();
	
	@Bind
	@SaveState
	public String argAgentdedid = "";
	
	@Bind
	@SaveState
	public boolean currentAccount = true;
	
	@Bind
	@SaveState
	public Date dateXrate = new Date();
	
	@Bind
	@SaveState
	public String jobNoStart;
	
	@Bind
	@SaveState
	public String jobNoEnd;
	
	@Bind
	@SaveState
	public String jobNoPerfixStart;
	
	@Bind
	@SaveState
	public String jobNoPerfixEnd;
	
	@Bind
	@SaveState
	public String flightno1;
	
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
	private String datetype = "YM";
	
	@Bind
	private String dateclassify = "jobdate";
	
	@Bind
	@SaveState
	public String customerclassify = "customerofarap";
	
	@Bind
	@SaveState
	public String reporttype = "";
	
	@Bind
	@SaveState
	public boolean isReadOnly = false;
	
	@Bind
	@SaveState
	public boolean isnos ;
	
	@Bind
	@SaveState
	public boolean filterArFinish;
	
	@Bind
	@SaveState
	public boolean filterArNo;
	
	@Bind
	@SaveState
	public boolean filterApFinish;
	
	@Bind
	@SaveState
	public boolean filterApNo;
	
	@Bind
	@SaveState
	public String nosv = "";
	
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
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameOperation;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameDepartment;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameFeeitem;
	
	@Bind
    private UICardLayout layoutinfo;
	
	@Bind
	@SaveState
	private String ppcc;
	
	@Bind
	@SaveState
	private String iscomplete;
	
	@Bind
	@SaveState
	public String generals = "all";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameLine;
	
	@Bind
	@SaveState
	public String argline = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCarrier;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameAccount;
	
	@Bind
	@SaveState
	public String argsCarrier = "";
	
	@Bind
	@SaveState
	public String argsAccount = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCorpid;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCorpidop;
	
	@Bind
	@SaveState
	public String corpid = "";
	
	@Bind
	@SaveState
	public String corpidop = "";
	
	@Bind
	@SaveState
	public String isamend = "A";
	
	@Bind
	@SaveState
	public String argpol = "";
	
	@Bind
	@SaveState
	public String argpod = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFramePol;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFramePod;
	
	@Bind
	@SaveState
	public Long pkVal;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameJobtype;
	
	@Bind
	@SaveState
	public String argjobtype = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameAgentedid;
	
	@Bind
	@SaveState
	public boolean jobsArFinish;
	
	@Bind
	@SaveState
	public boolean jobsApFinish;
	
	@Bind
	@SaveState
	public boolean jobNosFilter;
	
	@Bind
	@SaveState
	public boolean jobNosPerfixFilter;
	
	@Bind
	@SaveState
	public boolean flightno1Check;
	
	
	@Bind
	@SaveState
	public String ArApStaticRptHideAmend;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			dtlIFrameClientele.setSrc("./qry/dtlIFrameClientele.xhtml");
			dtlIFrameSale.setSrc("./qry/dtlIFrameSale.xhtml");
			dtlIFrameDepartment.setSrc("./qry/dtlIFrameDepartment.xhtml");
			dtlIFrameFeeitem.setSrc("./qry/dtlIFrameFeeitem.xhtml");
			update.markAttributeUpdate(dtlIFrameClientele, "src");
			update.markAttributeUpdate(dtlIFrameSale, "src");
			update.markAttributeUpdate(dtlIFrameDepartment, "src");
			update.markAttributeUpdate(dtlIFrameFeeitem, "src");
			dtlIFrameLine.setSrc("qry/dtlIFrameLine.xhtml");
			update.markAttributeUpdate(dtlIFrameLine, "src");
			dtlIFrameCarrier.setSrc("qry/dtlIFrameCarrier.xhtml");
			update.markAttributeUpdate(dtlIFrameCarrier, "src");
			dtlIFrameAccount.setSrc("qry/dtlIFrameAccount.xhtml");
			update.markAttributeUpdate(dtlIFrameAccount, "src");
			dtlIFrameCorpid.setSrc("qry/dtlIFrameCorpid.xhtml?type=corpid");
			update.markAttributeUpdate(dtlIFrameCorpid, "src");
			dtlIFrameCorpidop.setSrc("qry/dtlIFrameCorpid.xhtml?type=corpidop");
			update.markAttributeUpdate(dtlIFrameCorpidop, "src");
			dtlIFramePol.setSrc("qry/dtlIFramePol.xhtml");
			update.markAttributeUpdate(dtlIFramePol, "src");
			dtlIFramePod.setSrc("qry/dtlIFramePod.xhtml");
			update.markAttributeUpdate(dtlIFramePol, "src");
			dtlIFrameJobtype.setSrc("qry/dtlIFrameJobtype.xhtml");
			update.markAttributeUpdate(dtlIFrameJobtype, "src");
			dtlIFrameAgentedid.setSrc("qry/dtlIFrameAgented.xhtml");
			update.markAttributeUpdate(dtlIFrameAgentedid, "src");
			
			String cyidUser = ConfigUtils.findUserCfgVal("fee_profits_cy2", AppUtils.getUserSession().getUserid());
			if(!StrUtils.isNull(cyidUser)){
				this.currency = cyidUser;
			}else{
				this.currency = "USD";
			}
			
			
			lazyfilter = true;
			dtlIFrameOperation.setSrc("./qry/dtlIFrameSale.xhtml?isOperation=true");
			
			Date ym = Calendar.getInstance().getTime();
			ym_y = ym.getYear()+1900;
			ym_m = ym.getMonth()+1;
			ym_y2 = ym.getYear()+1900;
			ym_m2 = ym.getMonth()+1;
			ArApStaticRptHideAmend = ConfigUtils.findSysCfgVal("ArApStaticRptHideAmend");
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
	public void updateOperation(){
		if(argsOperation!=null && argsOperation.isEmpty()){
			dtlIFrameOperation.setSrc("./qry/dtlIFrameSale.xhtml");
			update.markAttributeUpdate(dtlIFrameOperation, "src");
		}
	}
	
	@Action
	public void updateDepartment(){
		if(argsDepartment!=null && argsDepartment.isEmpty()){
			dtlIFrameDepartment.setSrc("./qry/dtlIFrameDepartment.xhtml");
			update.markAttributeUpdate(dtlIFrameOperation, "src");
		}
	}
	
	@Action
	public void updateFeeitem(){
		if(argsFeeitem!=null && argsFeeitem.isEmpty()){
			dtlIFrameFeeitem.setSrc("./qry/dtlIFrameFeeitem.xhtml");
			update.markAttributeUpdate(dtlIFrameFeeitem, "src");
		}
	}
	
	@Action
    public void datetype_onchange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
	        if("YM".equals(datetype)){
	        	Date ym = new Date();
	        	ym_y = ym.getYear()+1900;
				ym_m = ym.getMonth()+1;
			}else if("YMD Before".equals(datetype)){
				ymd = df.parse(DateTimeUtil.getLastDay());
			}else if("YMD".equals(datetype)){
				jobDateFrom  = df.parse(DateTimeUtil.getFirstDay());
				jobdateTo = df.parse(DateTimeUtil.getLastDay());
			}else if("yearperiod".equals(datetype)){
				Date ym = new Date();
	        	ym_y = ym.getYear()+1900;
				ym_m = ym.getMonth()+1;
				ym_y2 = ym.getYear()+1900;
				ym_m2 = ym.getMonth()+1;
			}
        } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Action
    public void dateclassify_onchange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if("yearperiod".equals(dateclassify)){
        	datetype = "yearperiod";
        	isamend = "C";
		}else{
			datetype = "YM";
			isamend = "A";
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
	public void grid_ondblclick(){
		//super.grid_ondblclick();
//		this.pkVal = getGridSelectId();
//		doServiceFindData();
//		String infos = selectedRowData.getInfo();
//		reporttype = selectedRowData.getFilename();
//		report();
		Browser.execClientScript("reportJsVar.fireEvent('click')");
	}
	
	protected void doServiceFindData() {
		selectedRowData = serviceContext.sysReportMgrService.sysReportDao
		.findById(this.pkVal);
		
	}
	
	@Action
	public void report() {
		if(reporttype == null || reporttype.isEmpty()){
			MessageUtils.alert("请先选择需要查询的统计表!");
			return;
		}
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("user", AppUtils.getUserSession().getUsercode());

		
		if("YM".equals(datetype)){
			argsMap.put("jobdatetype","YM");
			argsMap.put("jobdate", ym_y+"-"+(ym_m<10?("0"+ym_m):ym_m));
		}else if("YMD Before".equals(datetype)){
			argsMap.put("jobdatetype","YMDBefore");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd").format(ymd));
		}else if("YMD".equals(datetype)){
			argsMap.put("jobdatetype","YMD");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd").format(jobDateFrom)+ "," + new SimpleDateFormat("yyyy-MM-dd").format(jobdateTo));
		}else if("yearperiod".equals(datetype)){
			argsMap.put("jobdatetype","yearperiod");
			argsMap.put("jobdate", ym_y+"-"+ym_m+"," + ym_y2+"-"+ym_m2);
			argsMap.put("year", ym_y+"");
		}
		argsMap.put("currency", currency);
		argsMap.put("datetype", datetype);
		argsMap.put("dateclassify", dateclassify);
		argsMap.put("customerclassify", customerclassify);
		argsMap.put("ppcc", ppcc);
		argsMap.put("iscomplete", iscomplete);
		argsMap.put("corporationid", argsCarrier);
		if(argsAccount.split(",").length>1){
			MessageUtils.alert("只能勾选一条银行账号");
			return;
		}
		argsMap.put("accountid", argsAccount);
		argsMap.put("argline", argline);
		if(isnos){
			argsMap.put("isnos", "Y");
		}else if(isnos){
			argsMap.put("isnos", "N");
		}
		
		argsMap.put("filterArFinish", filterArFinish?"Y":"N");
		argsMap.put("filterArNo", filterArNo?"Y":"N");
		argsMap.put("filterApFinish", filterApFinish?"Y":"N");
		argsMap.put("filterApNo", filterApNo?"Y":"N");
		
		if(currentAccount){
			argsMap.put("currentAccount", "Y");
		}else{
			argsMap.put("currentAccount", "N");
		}
		
		if(jobsArFinish){
			argsMap.put("jobsArFinish", "Y");
		}else{
			argsMap.put("jobsArFinish", "N");
		}
		if(jobsApFinish){
			argsMap.put("jobsApFinish", "Y");
		}else{
			argsMap.put("jobsApFinish", "N");
		}
		
		if(dateRPBeforeCheck){
			argsMap.put("dateRPBefore",new SimpleDateFormat("yyyy-MM-dd").format(dateRPBefore));
		}
		if(dateXrateCheck){
			argsMap.put("dateXrate",new SimpleDateFormat("yyyy-MM-dd").format(dateXrate));
		}
		
		if(jobNosFilter){
			argsMap.put("jobNosFilter" , jobNoStart + "@" + jobNoEnd);
		}
		
		if(jobNosPerfixFilter){
			argsMap.put("jobNosPerfixFilter" , jobNoPerfixStart + "@" + jobNoPerfixEnd);
		}
		
		if(flightno1Check){
			argsMap.put("flightno1Check" , flightno1);
		}
		
		argsMap.put("userid", AppUtils.getUserSession().getUserid().toString());
		argsMap.put("isamend",isamend);
		String urlArgs1 = AppUtils.map2Url(argsMap, "&");
		////System.out.println(urlArgs1);
//		argsMap.clear();
		Long corpidCurrent = AppUtils.getUserSession().getCorpidCurrent();
		////System.out.println(corpidCurrent);
		argsMap.put("corpid",AppUtils.base64Encoder(String.valueOf(corpidCurrent)));
		argsMap.put("salesid", argsSale);
		argsMap.put("feeitem", argsFeeitem);
		argsMap.put("department", argsDepartment);
		argsMap.put("clienteleid", argsClientele);
		argsMap.put("operationid", argsOperation);
		argsMap.put("corpidj",corpid);
		argsMap.put("corpidop",corpidop);
		argsMap.put("polids", argpol);
		argsMap.put("podids", argpod);
		argsMap.put("argjobtype", argjobtype);
		argsMap.put("nos", nosv);
		argsMap.put("agentdedid", argAgentdedid);

		// if(reporttype.equals("arap_sales_ap_zhuohang.raq")||reporttype.equals("arap_sales_ar_zhuohang.raq")){
		// 	//卓航业务员有权限看到这个两个表格里面 自己的单			业务员应收余额表-卓航深圳		业务员应收余额表-卓航深圳
		// 	String sql  = "SELECT issales FROM sys_user WHERE id = " + AppUtils.getUserSession().getUserid();
		// 	Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		// 	String issales = StrUtils.getMapVal(m, "issales");
		// 	if(!StrUtils.isNull(issales) && "true".equals(issales)){
		// 		argsMap.put("salesid", AppUtils.getUserSession().getUserid().toString());
		// 	}
		// }
		
		if("Y".equals(ConfigUtils.findSysCfgVal("sys_rpt_required_condition"))){
			//2712 统计报表预览前增加检查 右边列表条件部分，如果一个都没选提示并返回，提示：请选择过滤条件
			if(StrUtils.isNull(argsMap.get("salesid").toString())&&StrUtils.isNull(argsMap.get("operationid").toString())
					&&StrUtils.isNull(argsMap.get("feeitem").toString())&&StrUtils.isNull(argsMap.get("clienteleid").toString())
					&&StrUtils.isNull(argsMap.get("clienteleid").toString())&&StrUtils.isNull(argsMap.get("argline").toString())
					&&StrUtils.isNull(argsMap.get("corporationid").toString())&&StrUtils.isNull(argsMap.get("accountid").toString())
					&&StrUtils.isNull(argsMap.get("corpidj").toString())&&StrUtils.isNull(argsMap.get("corpidop").toString())
					&&StrUtils.isNull(argsMap.get("polids").toString())&&StrUtils.isNull(argsMap.get("podids").toString())
					&&StrUtils.isNull(argsMap.get("argjobtype").toString())&&StrUtils.isNull(argsMap.get("agentdedid").toString())
					&&StrUtils.isNull(argsMap.get("department").toString())
			){
//				MessageUtils.alert("请选择右边列表过滤条件!");
//				return;
			}
		}
		
		
		
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
		//System.out.println(urlArgs2);
		String arg = urlArgs1 + "&" + "para=" + urlArgs2;
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/"+(isReadOnly?"showReportReadOnly.jsp":"showReport.jsp")+"?raq=/static/arap/" + reporttype;
//		if("DEBIT_CREDIT_NOTE.raq".equals(reporttype)){
//			if(argsClientele == null || argsClientele.isEmpty() || argsClientele.split(",").length > 1){
//				MessageUtils.alert("收付款对账单(英文)表必须且只能选择一位客户!请选择后重新查看报表");
//				return;
//			}
//		}
		AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
		
	}

	protected void doServiceSave() {

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
			setgeneralsql.append("\n AND EXISTS(SELECT 1 FROM sys_report_config WHERE reportid = t.id AND rptype = 'arap' AND userid = ") ;
			setgeneralsql.append("\n" + AppUtils.getUserSession().getUserid()+" AND isuse = "+ isAll + ")");
		}
		map.put("setgeneralsql", setgeneralsql.toString());
		return map;
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
			String d = ym_y + "-" + (ym_m<10?("0"+ym_m):ym_m)+"-01";
			Date ym = CommonUtil.DateDayUp(sdf.parse(d),-1);
			ym_y = ym.getYear() + 1900;
			ym_m = ym.getMonth() + 1;
			
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
			String d = ym_y + "-" + (ym_m<10?("0"+ym_m):ym_m)+"-01";
			Date ym = CommonUtil.DateMonthUp(sdf.parse(d),1);
			ym_y = ym.getYear() + 1900;
			ym_m = ym.getMonth() + 1;
			
			ymd = CommonUtil.DateDayUp(CommonUtil.DateMonthUp(sdf.parse(sdf.format(ymd)+"-01"),2),-1);
			jobDateFrom = CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobDateFrom)+"-01"),1);
			jobdateTo = CommonUtil.DateDayUp(CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobdateTo)+"-01"),2),-1);
			update.markUpdate(true, UpdateLevel.Data, "listPanel");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	@Action
//	public void showRate(){
//		this.resetRate();
//	}
	
//	@Bind
//	public UIEditDataGrid rateGrid;
//	
//	@SaveState
//	@Accessible
//	public Map<String, Object> rateQryMap = new HashMap<String, Object>();
//	
//	@Bind(id="rateGrid")
//	public List<Map> getRateGrids() throws Exception{
//		List<Map> list = null;
//		if(rateQryMap.size() > 0){
//			list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.finance.invoiceBean.grid.rate", rateQryMap);
//		}
//		return list;
//	}
	
//	@SaveState
//	public String dateexchange="";
//	
//	@Action
//	public void exchangeRate(){
//		JSONArray modified = (JSONArray) this.rateGrid.getModifiedData();
//		ArrayList<Object> values = (ArrayList<Object>) this.rateGrid.getValue();
//		String dateTojson = serviceContext.sysReportConfigService.getDateTojson(modified, values);
//		dateexchange = dateTojson.replaceAll("\"", "__");//此处替换是因为  给dateexchange赋值后店家页面按钮会JS错误
//		dateexchange = dateexchange.replaceAll(":","~~");
////		System.out.println(dateexchange);
//	}
	
//	@Bind
//	private UIWindow exchangeRateWindow;
//	
//	@Action
//	public void resetRate(){
//		if(StrUtils.isNull(currency)){
//			MessageUtils.alert("请先选择折合币制");
//			return;
//		}
//		rateQryMap.put("date","NOW()");
//		rateQryMap.put("filter", "AND x.currencyto = '"+currency+"' ");
//		this.rateGrid.reload();
//		this.exchangeRateWindow.show();
//	}
}
