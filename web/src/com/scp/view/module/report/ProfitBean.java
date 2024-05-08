package com.scp.view.module.report;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

@ManagedBean(name = "pages.module.report.profitBean", scope = ManagedBeanScope.REQUEST)
public class ProfitBean extends GridFormView {

	@SaveState
	@Accessible
	public SysReport selectedRowData = new SysReport();

	@Bind
	@SaveState
	public String customerclassify = "customerofarap";
	
	@Bind
	@SaveState
	public boolean lazyfilter;

	@Bind
	@SaveState
	public String argsSale = "";

	@Bind
	@SaveState
	public String argsClientele = "";

	@Bind
	@SaveState
	public String argsCustservice = "";
	
	@Bind
	@SaveState
	public String argsDepartment = "";

	@Bind
	@SaveState
	public String argsFeeitem = "";
	
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
	private String datetype = "YM";
	
	@Bind
	private String dateclassify = "jobdate";
	
	@Bind
	@SaveState
	public String reporttype = "";
	
	@Bind
	@SaveState
	public boolean isReadOnly = false;
	
	
	@Bind
	@SaveState
	public boolean isnos ;
	
	
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
	private UIIFrame dtlIFrameCustservice;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameSale;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameDepartment;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameFeeitem;
	
	@Bind
    private UICardLayout layoutinfo;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCarrier;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameLine;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameImpexp;
	
	@Bind
	@SaveState
	private String ppcc;
	
	@Bind
	@SaveState
	private String isconfirm2_pp;
	
	@Bind
	@SaveState
	private String iscomplete="";
	
	@Bind
	@SaveState
	public String isamend = "A";
	
	@Bind
	@SaveState
	public String argline = "";
	
	@Bind
	@SaveState
	public String argimpexp = "";
	
	@Bind
	@SaveState
	public String argsCarrier = "";
	
	@Bind
	@SaveState
	public String ldtype = "";
	
	@Bind
	@SaveState
	public String cntnoyn = "";
	
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
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameVessel;
	@Bind
	@SaveState
	public String argVesselid = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameLinecode;
	@Bind
	@SaveState
	public String arglinecodeid = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameAgentedid;
	@Bind
	@SaveState
	public String argAgentdedid = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameAgenterid;
	@Bind
	@SaveState
	public String argAgenterid = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFramePoa;
	@Bind
	@SaveState
	public String argpoa = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameJobtype;
	
	@Bind
	@SaveState
	public String argjobtype = "";
	
	@Bind
	@SaveState
	public String tradeway = "";
	
	@Bind
	@SaveState
	public String argCountry = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCountry;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameAirline;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCustomerType;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameLdtype;
	
	@Bind
	@SaveState
	public String airlineid = "";
	
	@Bind
	@SaveState
	public String customerTypeid = "";
	
	@Bind
	@SaveState
	public String ldtypeid = "";
	
	@Bind
	@SaveState
	public boolean jobNosFilter;
	
	@Bind
	@SaveState
	public boolean jobNosPerfixFilter;
	
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
	public boolean isprofit;
	
	@Bind
	@SaveState
	private String profit_compare;
	

	@Bind
	@SaveState
	private BigDecimal profit;
	
	@Bind
	@SaveState
	private String profit_currency;
	
	@Bind
	@SaveState
	public boolean currentAccount = true;

	@Bind
	@SaveState
	public boolean isFinished = false;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			dtlIFrameClientele.setSrc("./qry/dtlIFrameClientele.xhtml");
			
			filterIframeClientele();
			
			
			dtlIFrameCustservice.setSrc("./qry/dtlIFrameCustservice.xhtml");
			dtlIFrameSale.setSrc("./qry/dtlIFrameSale.xhtml");
			dtlIFrameDepartment.setSrc("./qry/dtlIFrameDepartment.xhtml");
			dtlIFrameFeeitem.setSrc("./qry/dtlIFrameFeeitem.xhtml");
			update.markAttributeUpdate(dtlIFrameClientele, "src");
			update.markAttributeUpdate(dtlIFrameCustservice, "src");
			update.markAttributeUpdate(dtlIFrameSale, "src");
			update.markAttributeUpdate(dtlIFrameDepartment, "src");
			update.markAttributeUpdate(dtlIFrameFeeitem, "src");
			String cyidUser = ConfigUtils.findUserCfgVal("fee_profits_cy2", AppUtils.getUserSession().getUserid());
			if(!StrUtils.isNull(cyidUser)){
				this.currency = cyidUser;
			}else{
				this.currency = "USD";
			}
			if(!StrUtils.isNull(cyidUser)){
				this.profit_currency = cyidUser;
			}else{
				this.profit_currency = "CNY";
			}
			
			dtlIFrameCarrier.setSrc("qry/dtlIFrameCarrier.xhtml");
			update.markAttributeUpdate(dtlIFrameCarrier, "src");
			dtlIFrameLine.setSrc("qry/dtlIFrameLine.xhtml");
			update.markAttributeUpdate(dtlIFrameLine, "src");
			dtlIFrameImpexp.setSrc("qry/dtlIFrameImpexp.xhtml");
			update.markAttributeUpdate(dtlIFrameImpexp, "src");
			dtlIFrameCorpid.setSrc("qry/dtlIFrameCorpid.xhtml?type=corpid");
			update.markAttributeUpdate(dtlIFrameCorpid, "src");
			dtlIFrameCorpidop.setSrc("qry/dtlIFrameCorpid.xhtml?type=corpidop");
			update.markAttributeUpdate(dtlIFrameCorpidop, "src");
			dtlIFramePol.setSrc("qry/dtlIFramePol.xhtml");
			update.markAttributeUpdate(dtlIFramePol, "src");
			dtlIFramePod.setSrc("qry/dtlIFramePod.xhtml");
			update.markAttributeUpdate(dtlIFramePol, "src");
			dtlIFrameVessel.setSrc("qry/dtlIFrameShipschedule.xhtml");
			update.markAttributeUpdate(dtlIFrameVessel, "src");
			dtlIFrameLinecode.setSrc("qry/dtlIFrameLinecode.xhtml");
			update.markAttributeUpdate(dtlIFrameLinecode, "src");
			dtlIFrameAgentedid.setSrc("qry/dtlIFrameAgented.xhtml");
			update.markAttributeUpdate(dtlIFrameAgentedid, "src");
			dtlIFrameAgenterid.setSrc("qry/dtlIFrameAgenter.xhtml");
			update.markAttributeUpdate(dtlIFrameAgenterid, "src");
			dtlIFramePoa.setSrc("qry/dtlIFramePoa.xhtml");
			update.markAttributeUpdate(dtlIFramePoa, "src");
			dtlIFrameJobtype.setSrc("qry/dtlIFrameJobtype.xhtml");
			update.markAttributeUpdate(dtlIFrameJobtype, "src");
			dtlIFrameCountry.setSrc("qry/dtlIFrameCountry.xhtml");
			update.markAttributeUpdate(dtlIFrameCountry, "src");
			dtlIFrameAirline.setSrc("qry/dtlIFrameAirline.xhtml");
			update.markAttributeUpdate(dtlIFrameAirline, "src");
			dtlIFrameCustomerType.setSrc("qry/dtlIFrameCustomerType.xhtml");
			update.markAttributeUpdate(dtlIFrameCustomerType, "src");
			dtlIFrameLdtype.setSrc("qry/dtlIFrameLdtype.xhtml");
			update.markAttributeUpdate(dtlIFrameLdtype, "src");
			lazyfilter = true;
			isprofit = false;
			profit_compare = "gt";
			profit = BigDecimal.valueOf(0L);
			Date ym = Calendar.getInstance().getTime();
			ym_y = ym.getYear()+1900;
			ym_m = ym.getMonth()+1;
			
			if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
				isamend = "C";
			}
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
	public void updateCustservice(){
		if(argsCustservice!=null && argsCustservice.isEmpty()){
			dtlIFrameCustservice.setSrc("./qry/dtlIFrameCustservice.xhtml");
			update.markAttributeUpdate(dtlIFrameCustservice, "src");
		}
	}
	
	@Action
	public void updateDepartment(){
		if(argsDepartment!=null && argsDepartment.isEmpty()){
			dtlIFrameDepartment.setSrc("./qry/dtlIFrameDepartment.xhtml");
			update.markAttributeUpdate(dtlIFrameDepartment, "src");
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
		//this.pkVal = getGridSelectId();
		//doServiceFindData();
		//String infos = selectedRowData.getInfo();
		//reporttype = selectedRowData.getFilename();
		//report();
		Browser.execClientScript("reportJsVar.fireEvent('click')");
	}
	
	@Override
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
		String sql = "SELECT * FROM sys_user_corplink WHERE userid = "+AppUtils.getUserSession().getUserid()+" AND ischoose = TRUE";
		String branchid = null;//分公司简称
		try{
			List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if((list.size()>1&&reporttype.equals("profit_sales_month_branch.raq"))||(list.size()>1&&reporttype.equals("profit_branch_month.raq"))){
				alert("此报表只能选择一个分公司");
				return;
			}
			if(list.size()>0){
				Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT id FROM sys_corporation WHERE id = "+list.get(0).get("corpid"));
				branchid = m.get("id").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			argsMap.put("customerid", argsClientele+"");
		}
		if(isprofit){
			argsMap.put("isprofit", "true");
		}else{
			argsMap.put("isprofit", "false");
		}
		if(null != profit){
			argsMap.put("profit", profit.toString());
		}else{
			argsMap.put("profit", "0");
		}
		if(null != profit_compare){
			argsMap.put("profit_compare", profit_compare);
		}else{
			argsMap.put("profit_compare", "gt");
		}
		
		if(null != profit_currency){
			argsMap.put("profit_currency", profit_currency);
		}else{
			argsMap.put("profit_currency", "CNY");
		}
		
		argsMap.put("currency", currency);
		argsMap.put("datetype", datetype);
		argsMap.put("dateclassify", dateclassify);
		argsMap.put("ppcc", ppcc);
		argsMap.put("isconfirm2_pp", isconfirm2_pp);
		argsMap.put("iscomplete", iscomplete);
		argsMap.put("corporationid", argsCarrier);
		argsMap.put("argline", argline);
		argsMap.put("argimpexp",argimpexp);
		argsMap.put("corpidj",corpid);
		argsMap.put("corpidop",corpidop);
		argsMap.put("polids", argpol);
		argsMap.put("podids", argpod);
		argsMap.put("userid", AppUtils.getUserSession().getUserid().toString());
		if(isnos){
			argsMap.put("isnos", "Y");
		}else if(isnos){
			argsMap.put("isnos", "N");
		}
		argsMap.put("isamend",isamend);
		String urlArgs1 = AppUtils.map2Url(argsMap, "&");
//		argsMap.clear();
		//if(AppUtils.getUserSession().)
		argsMap.put("corpid",AppUtils.base64Encoder(String.valueOf(AppUtils.getUserSession().getCorpidCurrent())));
		argsMap.put("salesid", argsSale);
		argsMap.put("feeitem", argsFeeitem);
		argsMap.put("department", argsDepartment);
		argsMap.put("custservice", argsCustservice);
		argsMap.put("clienteleid", argsClientele);
		argsMap.put("customerclassify", customerclassify);
		argsMap.put("ldtype", ldtype);
		argsMap.put("cntnoyn", cntnoyn);
		argsMap.put("vesselid", argVesselid);
		argsMap.put("linecodeid", arglinecodeid);
		argsMap.put("agentdedid", argAgentdedid);
		argsMap.put("agenterid", argAgenterid);
		argsMap.put("poa", argpoa);
		argsMap.put("argjobtype", argjobtype);
		argsMap.put("tradeway",tradeway);
		
		
		
		argsMap.put("countryids", argCountry);
		argsMap.put("airlineid", airlineid);
		argsMap.put("customerTypeid", customerTypeid);
		argsMap.put("ldtypeid", ldtypeid);
		if(branchid!=null&&(reporttype.equals("profit_sales_month_branch.raq")||reporttype.equals("profit_branch_month.raq")
				||reporttype.equals("profit_sales_month_grossprofit.raq"))){
			argsMap.put("branchid", branchid);
		}
		
		if("Y".equals(ConfigUtils.findSysCfgVal("sys_rpt_required_condition"))){
			if((reporttype.equals("profit_sales_month_branch.raq"))||(reporttype.equals("profit_branch_month.raq"))){
			//2712 统计报表预览前增加检查 右边列表条件部分，如果一个都没选提示并返回，提示：请选择过滤条件
			}else if(StrUtils.isNull(argsMap.get("clienteleid").toString())&&StrUtils.isNull(argsMap.get("salesid").toString())
					&&StrUtils.isNull(argsMap.get("department").toString())&&StrUtils.isNull(argsMap.get("feeitem").toString())
					&&StrUtils.isNull(argsMap.get("custservice").toString())&&StrUtils.isNull(argsMap.get("custservice").toString())
					&&StrUtils.isNull(argsMap.get("corporationid").toString())&&StrUtils.isNull(argsMap.get("polids").toString())
					&&StrUtils.isNull(argsMap.get("podids").toString())&&StrUtils.isNull(argsMap.get("argline").toString())
					&&StrUtils.isNull(argsMap.get("argimpexp").toString())&&StrUtils.isNull(argsMap.get("corpidj").toString())
					&&StrUtils.isNull(argsMap.get("corpidop").toString())&&StrUtils.isNull(argsMap.get("vesselid").toString())
					&&StrUtils.isNull(argsMap.get("linecodeid").toString())&&StrUtils.isNull(argsMap.get("agentdedid").toString())
					&&StrUtils.isNull(argsMap.get("agenterid").toString())&&StrUtils.isNull(argsMap.get("poa").toString())
					&&StrUtils.isNull(argsMap.get("argjobtype").toString())&&StrUtils.isNull(argsMap.get("countryids").toString())
					&&StrUtils.isNull(argsMap.get("airlineid").toString())&&StrUtils.isNull(argsMap.get("customerTypeid").toString())
					&&StrUtils.isNull(argsMap.get("ldtypeid").toString())
			){
				//MessageUtils.alert("请选择右边列表过滤条件!");
				//return;
			}
		}
		
		if(jobNosFilter){
			argsMap.put("jobNosFilter" , jobNoStart + "@" + jobNoEnd);
		}
		if(jobNosPerfixFilter){
			argsMap.put("jobNosPerfixFilter" , jobNoPerfixStart + "@" + jobNoPerfixEnd);
		}
		
		//默然包含往来费用--传参是否包含往来费用
		if(currentAccount){
			argsMap.put("currentAccount", "Y");
		}else{
			argsMap.put("currentAccount", "N");
		}

		//默然不勾选费用完成
		if(isFinished){
			argsMap.put("isFinished", "Y");
		}else{
			argsMap.put("isFinished", "N");
		}
		
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
		
		String arg = urlArgs1 + "&" + "para=" + urlArgs2;
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/"+(isReadOnly?"showReportReadOnly.jsp":"showReport.jsp")+"?raq=/static/profit/" + reporttype;
//		if("DEBIT_CREDIT_NOTE.raq".equals(reporttype)){
//			if(argsClientele == null || argsClientele.isEmpty() || argsClientele.split(",").length > 1){
//				MessageUtils.alert("收付款对账单(英文)表必须且只能选择一位客户!请选择后重新查看报表");
//				return;
//			}
//		}
		System.out.print(arg);
		AppUtils.openWindow("_apAllCustomReport", openUrl + arg);
		
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
			setgeneralsql.append("\n AND EXISTS(SELECT 1 FROM sys_report_config WHERE reportid = t.id AND rptype = 'profit' AND userid = ") ;
			setgeneralsql.append("\n" + AppUtils.getUserSession().getUserid()+" AND isuse = "+ isAll + ")");
		}
		map.put("setgeneralsql", setgeneralsql.toString());
		return map;
	}
	@Override
	protected void doServiceSave() {

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
			jobdateTo = CommonUtil.DateDayUp(CommonUtil.DateMonthUp(sdf.parse(sdf.format(jobDateFrom)+"-01"),1),-1);
			update.markUpdate(true, UpdateLevel.Data, "listPanel");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
