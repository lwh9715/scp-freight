package com.scp.view.module.report;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UICardLayout;
import org.operamasks.faces.component.layout.impl.UITabLayout;
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

@ManagedBean(name = "pages.module.report.orderquantityBean", scope = ManagedBeanScope.REQUEST)
public class OrderQuantityBean extends GridFormView {

	@SaveState
	@Accessible
	public SysReport selectedRowData = new SysReport();

	
	@Bind
	@SaveState
	public String argsSale = "";

	@Bind
	@SaveState
	public String argsCarrier = "";
	
	@Bind
	@SaveState
	public String argsEmployee = "";
	
	@Bind
	@SaveState
	public String argsClientele = "";

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
	public Date ymd = new Date();
	
	
	@Bind
	@SaveState
	public Date dateXrate = new Date();
	
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
	
	@Bind
	@SaveState
	public boolean isprofit ;
	
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
	private UIIFrame dtlIFrameEmployee;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCarrier;
	
	@Bind
    private UICardLayout layoutinfo;
	
	@Bind
	@SaveState
	private BigDecimal profit;
	
	@Bind
	@SaveState
	private String profit_compare;
	
	@Bind
	@SaveState
	private String profit_currency;
	
	@Bind
	@SaveState
	public String corpid = "";
	
	@Bind
	@SaveState
	public String corpidop = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCorpid;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCorpidop;
	
	@Bind
	@SaveState
	public String tradeway = "";
	
	@Bind
	@SaveState
	public String ispecialprice = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameJobtype;
	
	@Bind
	@SaveState
	public String argjobtype = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameDepartment;
	
	@Bind
	@SaveState
	public String argsDepartment = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFramePol;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFramePod;
	
	@Bind
	@SaveState
	public String argpol = "";
	
	@Bind
	@SaveState
	public String argpod = "";
	
	@Bind
	@SaveState
	public String argpoa = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFramePoa;
	
	@Bind
	@SaveState
	public String ldtype = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameLine;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameImpexp;
	
	@Bind
	@SaveState
	public String argline = "";
	
	@Bind
	@SaveState
	public String argimpexp = "";
	
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
	
	@Bind
	@SaveState
	public String customerTypeid = "";
	
	@Bind
	@SaveState
	private String iscomplete="";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameGoods;
	
	@Bind
	@SaveState
	public String arggoodsid = "";
	
	@Bind
	public UITabLayout tabLayout;
	
	@Bind
	@SaveState
	public boolean isnopoaname ;
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCustomerType;
	
	@Bind
	@SaveState
	public String ldtypeid = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameLdtype;
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			reporttype = AppUtils.getReqParam("reportfilename");
			dtlIFrameCustomer.setSrc("qry/dtlIFrameCustomer.xhtml");
			update.markAttributeUpdate(dtlIFrameCustomer, "src");
			dtlIFrameSale.setSrc("qry/dtlIFrameSale.xhtml");
			update.markAttributeUpdate(dtlIFrameSale, "src");
			dtlIFrameCarrier.setSrc("qry/dtlIFrameCarrier.xhtml");
			update.markAttributeUpdate(dtlIFrameCarrier, "src");
			dtlIFrameEmployee.setSrc("qry/dtlIFrameEmployee.xhtml");
			update.markAttributeUpdate(dtlIFrameEmployee, "src");
			dtlIFrameCorpid.setSrc("qry/dtlIFrameCorpid.xhtml?type=corpid");
			update.markAttributeUpdate(dtlIFrameCorpid, "src");
			dtlIFrameCorpidop.setSrc("qry/dtlIFrameCorpid.xhtml?type=corpidop");
			update.markAttributeUpdate(dtlIFrameCorpidop, "src");
			dtlIFrameJobtype.setSrc("qry/dtlIFrameJobtype.xhtml");
			update.markAttributeUpdate(dtlIFrameJobtype, "src");
			dtlIFrameDepartment.setSrc("./qry/dtlIFrameDepartment.xhtml");
			update.markAttributeUpdate(dtlIFrameDepartment, "src");
			dtlIFramePol.setSrc("qry/dtlIFramePol.xhtml");
			update.markAttributeUpdate(dtlIFramePol, "src");
			dtlIFramePod.setSrc("qry/dtlIFramePod.xhtml");
			update.markAttributeUpdate(dtlIFramePod, "src");
			dtlIFramePoa.setSrc("qry/dtlIFramePoa.xhtml");
			update.markAttributeUpdate(dtlIFramePoa, "src");
			dtlIFrameLine.setSrc("qry/dtlIFrameLine.xhtml");
			update.markAttributeUpdate(dtlIFrameLine, "src");
			dtlIFrameImpexp.setSrc("qry/dtlIFrameImpexp.xhtml");
			update.markAttributeUpdate(dtlIFrameImpexp, "src");
			dtlIFrameVessel.setSrc("qry/dtlIFrameShipschedule.xhtml");
			update.markAttributeUpdate(dtlIFrameVessel, "src");
			dtlIFrameLinecode.setSrc("qry/dtlIFrameLinecode.xhtml");
			update.markAttributeUpdate(dtlIFrameLinecode, "src");
			dtlIFrameGoods.setSrc("qry/dtlIFrameGoods.xhtml");
			update.markAttributeUpdate(dtlIFrameGoods, "src");
			dtlIFrameCustomerType.setSrc("qry/dtlIFrameCustomerType.xhtml");
			update.markAttributeUpdate(dtlIFrameCustomerType, "src");
			dtlIFrameLdtype.setSrc("qry/dtlIFrameLdtype.xhtml");
			update.markAttributeUpdate(dtlIFrameLdtype, "src");
			isnos=false;
			isprofit = false;
			profit = BigDecimal.valueOf(0L);
			profit_compare = "gt";
			
			String cyidUser = ConfigUtils.findUserCfgVal("fee_profits_cy2", AppUtils.getUserSession().getUserid());
			if(!StrUtils.isNull(cyidUser)){
				this.profit_currency = cyidUser;
			}else{
				this.profit_currency = "CNY";
			}
			
			isnopoaname=false;
			
			
			Date ym = Calendar.getInstance().getTime();
			ym_y = ym.getYear()+1900;
			ym_m = ym.getMonth()+1;
			
			init();
		}
	}
	
	@Bind
	@SaveState
	public String argsCustomer = "";
	
	@SaveState
	@Bind
	private UIIFrame dtlIFrameCustomer;
	
	@Action
	public void updateCustomer(){
		////System.out.println("argsCustomer:"+argsCustomer);
		if(argsCustomer!=null && argsCustomer.isEmpty()){
			dtlIFrameCustomer.setSrc("qry/dtlIFrameCustomer.xhtml");
			update.markAttributeUpdate(dtlIFrameCustomer, "src");
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
			}else if("YMD".equals(datetype)){
				jobDateFrom  = df.parse(DateTimeUtil.getFirstDay());
				jobdateTo = df.parse(DateTimeUtil.getLastDay());
			}
        } catch (ParseException e) {
			e.printStackTrace();
		}
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
	@Action
	public void grid_onrowselect(){
		super.grid_onrowselect();
		this.pkVal = getGridSelectId();
		doServiceFindData();
		String infos = selectedRowData.getInfo();
		reporttype = selectedRowData.getFilename();
		isReadOnly = selectedRowData.getIsreadonly();
	}
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.sysReportMgrService.sysReportDao
		.findById(this.pkVal);
		
	}
	
	@Action
	public void report() {
		String sql = "SELECT COUNT(1) FROM sys_user_corplink WHERE userid = "+AppUtils.getUserSession().getUserid()+" AND ischoose = TRUE;";
		Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if("f_rpt_operation_service_1_copy.raq".equals(reporttype)||"f_rpt_operation_service_2_copy.raq".equals(reporttype)){
			if(m!=null&&!"1".equals(m.get("count").toString())){
					alert("此报表切换公司只能选一个分公司!");
					return;
			}
			if(!"YM".equals(datetype)){
				alert("此报表请选择YM日期类型!");
				return;
			}
		}
		if(reporttype == null || reporttype.isEmpty()){
			MessageUtils.alert("请先选择需要查询的统计表!");
			return;
		}
		
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put("user", AppUtils.getUserSession().getUsercode());
		if("YM".equals(datetype)){
			argsMap.put("jobdatetype","YM");
			argsMap.put("jobdate", ym_y+"-"+(ym_m<10?("0"+ym_m):ym_m));
		}else if("YMD".equals(datetype)){
			argsMap.put("jobdatetype","YMD");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd")
					.format(jobDateFrom)
					+ "," + new SimpleDateFormat("yyyy-MM-dd").format(jobdateTo));
		}
		argsMap.put("userid", AppUtils.getUserSession().getUserid().toString());
		String urlArgs1 = AppUtils.map2Url(argsMap, "&");
		argsMap.clear();
		argsMap.put("salesid", argsSale);
		argsMap.put("customerid", argsCustomer);
		argsMap.put("corporationid", argsCarrier);
		argsMap.put("employeeid", argsEmployee);
		argsMap.put("reportcode", selectedRowData.getCode());
		argsMap.put("ldtypeid", ldtypeid);
		if("YM".equals(datetype)){
			argsMap.put("jobdatetype","YM");
			argsMap.put("jobdate", ym_y+"-"+(ym_m<10?("0"+ym_m):ym_m));
		}else if("YMD".equals(datetype)){
			argsMap.put("jobdatetype","YMD");
			argsMap.put("jobdate", new SimpleDateFormat("yyyy-MM-dd")
					.format(jobDateFrom)
					+ "," + new SimpleDateFormat("yyyy-MM-dd").format(jobdateTo));
		}
		argsMap.put("dateclassify", dateclassify);
		if(isnos){
			argsMap.put("isnos", "Y");
		}else if(isnos){
			argsMap.put("isnos", "N");
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
		if(isnopoaname){
			argsMap.put("isnopoaname", "Y");
		}else if(isnopoaname){
			argsMap.put("isnopoaname", "N");
		}
		argsMap.put("corpid",AppUtils.base64Encoder(String.valueOf(AppUtils.getUserSession().getCorpidCurrent())));
		argsMap.put("userid", AppUtils.getUserSession().getUserid().toString());
		argsMap.put("tradeway",tradeway);
		argsMap.put("ispecialprice",ispecialprice);
		argsMap.put("corpidj",corpid);
		argsMap.put("corpidop",corpidop);
		argsMap.put("argjobtype", argjobtype);
		argsMap.put("departid",argsDepartment);
		argsMap.put("argpolids", argpol);
		argsMap.put("argpodids", argpod);
		argsMap.put("argpoaids", argpoa);
		argsMap.put("ldtype", ldtype);
		argsMap.put("argline", argline);
		argsMap.put("argimpexp",argimpexp);
		argsMap.put("vesselid", argVesselid);
		argsMap.put("linecodeid", arglinecodeid);
		argsMap.put("iscomplete", iscomplete);
		argsMap.put("arggoodsid", arggoodsid);
		argsMap.put("customerTypeid", customerTypeid);
		String urlArgs2 = AppUtils.map2Url(argsMap, ":");
		String arg = urlArgs1 + "&" + "para=" + urlArgs2;
		String openUrl = AppUtils.getRptUrl()
				+ "/reportJsp/"+(isReadOnly?"showReportReadOnly.jsp":"showReport.jsp")+"?raq=/static/boxs/" + reporttype;
		
		if("Y".equals(ConfigUtils.findSysCfgVal("sys_rpt_required_condition"))){
			//2712 统计报表预览前增加检查 右边列表条件部分，如果一个都没选提示并返回，提示：请选择过滤条件
			if(StrUtils.isNull(argsMap.get("employeeid").toString())&&StrUtils.isNull(argsMap.get("salesid").toString())
					&&StrUtils.isNull(argsMap.get("departid").toString())&&StrUtils.isNull(argsMap.get("customerid").toString())
					&&StrUtils.isNull(argsMap.get("corporationid").toString())&&StrUtils.isNull(argsMap.get("corpidj").toString())
					&&StrUtils.isNull(argsMap.get("corpidop").toString())&&StrUtils.isNull(argsMap.get("argjobtype").toString())
					&&StrUtils.isNull(argsMap.get("argpolids").toString())&&StrUtils.isNull(argsMap.get("argpodids").toString())
					&&StrUtils.isNull(argsMap.get("argpoaids").toString())&&StrUtils.isNull(argsMap.get("argline").toString())
					&&StrUtils.isNull(argsMap.get("argimpexp").toString())&&StrUtils.isNull(argsMap.get("vesselid").toString())
					&&StrUtils.isNull(argsMap.get("linecodeid").toString())&&StrUtils.isNull(argsMap.get("customerTypeid").toString())
					&&StrUtils.isNull(argsMap.get("ldtypeid").toString())
			){
				MessageUtils.alert("请选择右边列表过滤条件!");
				return;
			}
		}
		
		
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
			setgeneralsql.append("\n AND EXISTS(SELECT 1 FROM sys_report_config WHERE reportid = t.id AND rptype = 'orderquantity' AND userid = ") ;
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
	
	@Bind
	@SaveState
	public char ischaojie = '0';
	
	public void init() {
		String sql = "SELECT f_sys_getcsno() = '2221' AS csno";
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if (m != null && m.get("csno") != null && m.get("csno").toString().equals("true")) {
				ischaojie = '1';
			}
		} catch (Exception e) {
			ischaojie = '0';
		}
	}
	
}
