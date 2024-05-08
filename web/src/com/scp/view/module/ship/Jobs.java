package com.scp.view.module.ship;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.beans.factory.annotation.Autowired;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.base.ConstantBean.Module;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.sysmgr.filedownload.FiledownloadBean;

@ManagedBean(name = "pages.module.ship.jobsBean", scope = ManagedBeanScope.REQUEST)
public class Jobs extends GridView {

	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind
	@SaveState
	private String startDateJ;

	@Bind
	@SaveState
	private String endDateJ;

	@Bind
	@SaveState
	private String startDateA;

	@Bind
	@SaveState
	private String endDateA;

	@Bind
	@SaveState
	private String startDateATA;

	@Bind
	@SaveState
	private String endDateATA;

	@Bind
	@SaveState
	private String carryitem;

	@Bind
	@SaveState
	private String startDateETD;

	@Bind
	@SaveState
	private String endDateETD;

	@Bind
	@SaveState
	private String startDateATD;

	@Bind
	@SaveState
	private String endDateATD;
	
	@Bind
	@SaveState
	private String dates;
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	private boolean isjob;
	
	@Bind
	@SaveState
	private String numbers;
	
	@Bind
	@SaveState
	private boolean isrefno;
	
	@Bind
	@SaveState
	private boolean isbook;
	
	@Bind
	@SaveState
	private boolean ishbl;
	
	@Bind
	@SaveState
	private boolean ismbl;
	
	@Bind
	@SaveState
	private boolean issendc;
	
	@Bind
	@SaveState
	private boolean iscustoms;
	
	@Bind
	@SaveState
	private boolean isreceipt;
	@Bind
	@SaveState
	private boolean isinvoice;
	@Bind
	@SaveState
	private boolean isnumber;
	@Bind
	@SaveState
	private boolean isno;
	@Bind
	@SaveState
	private boolean istitle;
	@Bind
	@SaveState
	private boolean iscommission;
	@Bind
	@SaveState
	private boolean isbill;	
	@Bind
	@SaveState
	private boolean islink;
	@Bind
	@SaveState
	private boolean ispo;
	@Bind
	@SaveState
	private boolean isdate=false;
	@Bind
	@SaveState
	private boolean isprofit;
	@Bind
	@SaveState
	private BigDecimal profit ;
	@Bind
	@SaveState
	private String profit_currency;
	@Bind
	@SaveState
	private String profit_compare;
	
	@Bind
	@SaveState
	private String vessel="";
	@Bind
	@SaveState
	private String voyage="";
	@Bind
	@SaveState
	private String destination="";
	@Bind
	@SaveState
	private String ldtype="";
	@Bind
	@SaveState
	private String corpid = "" ;
	@Bind
	@SaveState
	private String deptid = "" ;
	@Bind
	@SaveState
	private String corpidop = "" ;
	@Bind
	@SaveState
	private String  saleid="" ;
	@Bind
	@SaveState
	private long sale=0 ;
	@Bind
	@SaveState
	private long sale2=0 ;
	@Bind
	@SaveState
	private long assigneesales = 0;
	@Bind
	@SaveState
	private String port="";
	
	@Bind
	@SaveState
	private String mblqry="";
	
	@Bind
	@SaveState
	private String portss;
	@Bind
	@SaveState
	private String allcustomer="";
	@Bind
	@SaveState
	private long customerid=0;

	@Bind
	@SaveState
	private long customerid2=0;
	@Bind
	@SaveState
	private String usergetmbl;
	@Bind
	@SaveState
	private String userreleasembl;
	@Bind
	@SaveState
	private String filsid;
	
	@Bind
	@SaveState
	private String operationid;
	
	@Bind
	@SaveState
	private String callid;

	@Bind
	@SaveState
	private String marketid;
	
	@Bind
	@SaveState
	private String priceuserid;

	@Bind
	@SaveState
	private String busstatusdesc;
	
	@Bind
	@SaveState
	private String assignid;
	
	@Bind
	@SaveState
	private String routecode="";
	
	
//	@Bind
//	@SaveState
//	public Boolean iscare = true;

	@Bind
	public UIWindow searchWindow;

	@Bind
	public UIButton addMaster;
	
	@Bind
	public UIButton addcopy;
	
	@Bind
	public UIButton delMaster;
	
	public Long userid;
	
	@Bind
	@SaveState
	public String cnortitle;
	
	@Bind
	@SaveState
	public String cneetitle;
	
	@Bind
	@SaveState
	public String notifytitle;
	
	@Bind
	@SaveState
	public String impexp;
	
	
	@Bind
	@SaveState
	String dynamicClauseWhere  ="";

	@Bind(id="deptids")
	@SelectItems
	public List<SelectItem> deptids = new ArrayList<SelectItem>();
	
	@Bind
	@SaveState
	public String setDays ;
	
	@Bind
	@SaveState
	public String sortlabel_1;
	
	@Bind
	@SaveState
	public String sortvalue_1;
	
	@Bind
	@SaveState
	public String sortlabel_2;
	
	@Bind
	@SaveState
	public String sortvalue_2;
	
	@Bind
	@SaveState
	public String sortlabel_3;
	
	@Bind
	@SaveState
	public String sortvalue_3;
	
	@Bind
	@SaveState
	public String customertype;
	
	@Bind
	public UIWindow sortWindow;
	
	@Bind
	public String isuserdzz;
	
	@Bind
	@SaveState
	public String iscl;
	
	@Bind
	@SaveState
	private String isbustruck="";
	
	@Bind
	@SaveState
	private String isbuscustoms="";
	
	@Bind
	@SaveState
	private String isvgm="";
	
	@Bind
	@SaveState
	private String isso="";
	
	@Bind
	@SaveState
	private String isfeeding="";
	
	@Bind
	@SaveState
	private String isbltype="";
	
	@Bind
	@SaveState
	private String istelrelv="";
	
	@Bind
	@SaveState
	private String roletype="";//高级检索中的动态类型
	
	@Bind
	@SaveState
	private String statetracenext="";
	
	@Bind
	@SaveState
	private String iscompleteqry;

	@Bind
	@SaveState
	private String ischeck;
	
	@Bind
	@SaveState
	private String carriername;

	@Bind
	@SaveState
	private String tradewaycode;

	@Bind
	@SaveState
	private String cargotype;

	@Bind
	@SaveState
	private String qrymbltype="";
	
	@Bind
	@SaveState
	private String cnortitlembl="";
	@Bind
	@SaveState
	private String cneetitlembl="";
	@Bind
	@SaveState
	private String notifytitlembl="";
	
	@Bind
	private UIButton batchupUpdate;
	
	@Bind
	private UIButton batchAddFee;
	
	@Bind
	@SaveState
	private String araptype;
	
	@Bind
	@SaveState
	private String ppcctype;
	
	@Bind
	@SaveState
	private String customersid;
	
	@Bind
	@SaveState
	private String arapdate;
	
	@Bind
	@SaveState
	private String feeitemid;
	
	@Bind
	@SaveState
	private String price;
	
	@Bind
	@SaveState
	private String piece;
	
	@Bind
	@SaveState
	private String payplace;
	
	@Bind
	@SaveState
	private String currency;
	
	@Bind
	@SaveState
	private String corp;
	
	@Bind
	@SaveState
	private String putstatus="";
	
	

	@Bind
	@SaveState
	private boolean isreleasecnt;
	
	@Bind
	@SaveState
	private boolean ispickcnt;
	
	@Bind
	@SaveState
	private boolean isTotalList;
	
	@Bind
	@SaveState
	private String agentList;
	
	@Bind
	@SaveState
	private boolean releasecnt;
	
	@Bind
	@SaveState
	private Date releasecnttime;
	
	@Bind
	@SaveState
	private boolean pickcnt;
	
	@Bind
	@SaveState
	private Date pickcnttime;
	
	@Bind
	@SaveState
	private String potcode;//批量修改中转港
	@Bind
	@SaveState
	private String pono;//PO

	@Bind
	@SaveState
	private String remark;//备注

	@Bind
	@SaveState
	private String potcod;//高级检索中转港
	
	@Bind
	@SaveState
	private Date sotime;
	
	@Bind
	@SaveState
	private String editbusstatus;
	
	@Bind
	@SaveState
	private BigInteger opcomp;
	
	@Bind
	@SaveState
	private String shipagent;
	
	@Bind
	@SaveState
	private String shipagents = "";
	
	@Bind
	@SaveState
	private Date sodate;


	@Bind
	@SaveState
	private String sonoselect;
	
	@Bind
	@SaveState
	private String linkid;
	
	@Bind
	@SaveState
	private String linkid2;
	
	@Bind
	@SaveState
	private String userId;

	@Bind
	@SaveState
	private String isSendReceive;

	@Bind
	@SaveState
	public String priceuser;

	@Bind
	@SaveState
	public String priceuserconfirm;

	@Bind
	@SaveState
	public String customerabbr;


	@Bind
	@SaveState
	public String busstatusstr = "";
	@Bind
	@SaveState
	public String notshipped = "";
	@Bind
	@SaveState
	public String shipped = "";
	@Bind
	@SaveState
	public String unloaded = "";


	@Action
	public void batchupUpdate(){
		String[] ids = this.grid.getSelectedIds();
		
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}
		try {
			String type = "update";
			String str = this.serviceContext.jobsMgrService.batchupUpdateMoneyDate(ids,araptype,feeitemid,price,currency,AppUtils.getUserSession().getUserid(),type);
			MessageUtils.alert(str);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void batchAddFee(){
		String[] ids = this.grid.getSelectedIds();
		
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}
		try {
			String type = "add";
			String str = this.serviceContext.jobsMgrService.batchupUpdateMoneyDate(ids,araptype,feeitemid,price,currency,AppUtils.getUserSession().getUserid(),ppcctype,customersid,arapdate,piece,payplace,corp,type);
			MessageUtils.alert(str);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void searchfee() {
		this.qryRefresh();
		this.deptids = getqueryDepartid();
		//this.grid.reload();
	}

	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		
		isSendReceive = ConfigUtils.findSysCfgVal("sys_forbidden_find_and_view_receipt");

		if (isSendReceive.equals("Y")) {
			Browser.execClientScript("cnortitleJsVar.hide();");
			Browser.execClientScript("cneetitleJsVar.hide();");
			Browser.execClientScript("notifytitleJsVar.hide();");
			Browser.execClientScript("cnortitlemblJsVar.hide();");
			Browser.execClientScript("cneetitlemblJsVar.hide();");
			Browser.execClientScript("notifytitlemblJsVar.hide();");
		}

		if (!isPostBack) {
			super.applyGridUserMultipleDef();
			initCtrl();
			initData();
			initControl();
			profit_compare = ">";
			profit_currency = "CNY";
			profit = BigDecimal.valueOf(0L);
			//this.qryMap.put("nos", "#");
			gridLazyLoad = true;
//			boolean isUseDzz = applicationConf.getIsUseDzz();
//			if(!isUseDzz){
//				isuserdzz = "true";
//			}
			if("8888".equals(ConfigUtils.findSysCfgVal("CSNO")) || AppUtils.isDebug){
				Browser.execClientScript("setTimeout('applicationgsitJsVar.show()',5000);");
				Browser.execClientScript("setTimeout('applicationJsVar.hide()',5000);");
			}
			linkid = "";
			userId = AppUtils.getUserSession().getUserid()+"";
		}
	}

	private void initCtrl() {
		addMaster.setDisabled(true);
		addcopy.setDisabled(true);
		delMaster.setDisabled(true);
		showDynamic.setDisabled(true);
		List<String> jobsRoles = AppUtils.getUserRoleModuleCtrl(Module.fina_jobs.getValue());
		for (String s : jobsRoles) {
			if (s.endsWith("_add")) {
				addMaster.setDisabled(false);
				addcopy.setDisabled(false);
			} else if (s.endsWith("_update")) {
			} else if (s.endsWith("_delete")) {
				delMaster.setDisabled(false);
			}
		}
		List<String> arapRoles = AppUtils.getUserRoleModuleCtrl(Module.fina_arap.getValue());
		for (String s : arapRoles) {
			if (s.endsWith("_report")) {
				showDynamic.setDisabled(false);
			}
		}
	}
	
	@Action
	public void addMaster() {
		String winId = "_edit_jobs";
		String url ="./jobsedit.xhtml";
		AppUtils.openNewPage(url);
	}
	
	@Action
	public void application() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择记录");
			return;
		}
		boolean custemerEqually = serviceContext.jobsMgrService.custemerEqually(ids);
		if(custemerEqually){
			MessageUtils.alert("选择多单情况下必须是同一客户");
			return;
		}
		String winId = "_edit_releasebillapply";
		if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
			String url =  "/scp/bpm/apply/bpmreleasebillapply.xhtml?jobid=" + StrUtils.array2List(ids);
			AppUtils.openWindow(true,winId, url,false);
		}else{
			String url =  "../../../pages/ff/apply/releasebillapply.xhtml?jobid=" + StrUtils.array2List(ids);
			AppUtils.openWindow(true,winId, url,false);
		}
	}
	
	@Action
	public void newApplication() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择记录");
			return;
		}
		try {
			Map m1 = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT count(1) AS c from (SELECT DISTINCT saleid FROM fina_jobs WHERE isdelete = FALSE AND id IN(" + StrUtils.array2List(ids) + ") ) t");
			boolean custemerEqually = serviceContext.jobsMgrService.custemerEqually(ids);
			
			if(custemerEqually){
				MessageUtils.alert("选择多单情况下必须是同一客户");
				return;
			}
			if(!"1".equals(String.valueOf(m1.get("c")))){
				MessageUtils.alert("选择多单情况下必须是同一业务");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT COUNT(1) FROM fina_jobs WHERE isdelete = FALSE AND iscomplete_ar = TRUE AND iscomplete_ap= TRUE AND id in ("+StrUtils.array2List(ids)+");");
			if(Integer.valueOf(String.valueOf(m.get("count"))) == 0){
				MessageUtils.alert("费用应收应付未完成!");
				return;
			}
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT count(1) FROM (SELECT COALESCE(SUM(f_amtto(a.arapdate, a.currency, 'USD', a.amount)),0) > 100 AS flag from fina_jobs j LEFT JOIN fina_arap a ON (a.isdelete = false AND a.jobid = j.id) WHERE	j.isdelete =FALSE AND j.id in ("+StrUtils.array2List(ids)+")	GROUP BY j.id) t WHERE t.flag = FALSE");
			if(Integer.valueOf(String.valueOf(m.get("count"))) > 0){
//				MessageUtils.alert("应收不足一百美金，请补全应收费用");
				// return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String winId = "_edit_releasebillapply";
		if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
			String url =  "/scp/bpm/apply/bpmnewreleasebillapply.xhtml?jobid=" + StrUtils.array2List(ids);
			AppUtils.openWindow(true,winId, url,false);
		}else{
			String url =  "../../../pages/ff/apply/bpmnewreleasebillapply.xhtml?jobid=" + StrUtils.array2List(ids);
			AppUtils.openWindow(true,winId, url,false);
		}
	}
	
	@Action
	public void applicationgsit() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择记录");
			return;
		}
		boolean custemerEqually = serviceContext.jobsMgrService.custemerEqually(ids);
		if(custemerEqually){
			MessageUtils.alert("选择多单情况下必须是同一客户");
			return;
		}
		String winId = "_edit_releasebillapply";
		String url =  "/scp/bpm/apply/bpmreleasebillapply.xhtml?jobid=" + StrUtils.array2List(ids)+"&type=gsit";
		AppUtils.openWindow(true,winId, url,false);
	}

	@Action
	public void delMaster() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}
		try {
			for (String id : ids) {
				serviceContext.jobsMgrService.removeDate(Long.valueOf(id),AppUtils.getUserSession().getUsercode());
			}
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_jobs";
		String url = "./jobsedit.xhtml?id=" + this.getGridSelectId();
		AppUtils.openNewPage(url);
		// AppUtil.openDzzWindow(winId, url , "Jobs");
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		// ATD日期和工作单日期区间查询
		String qry = StrUtils.getMapVal(m, "qry");
		
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(m.containsKey("ordersql")){
			m.remove("ordersql");
		}
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql;
			m.put("ordersql", ordersql);
		}
//		if (!StrUtils.isNull(startDateJ) || !StrUtils.isNull(endDateJ)) {
//			qry += "\nAND t.jobdate BETWEEN '"
//					+ (StrUtils.isNull(startDateJ) ? "0001-01-01" : startDateJ)
//					+ "' AND '"
//					+ (StrUtils.isNull(endDateJ) ? "9999-12-31" : endDateJ)
//					+ "'";
//		}
//		if (!StrUtils.isNull(startDateA) || !StrUtils.isNull(endDateA)) {
//			qry += "\nAND (t.atd BETWEEN '"
//					+ (StrUtils.isNull(startDateA) ? "0001-01-01" : startDateA)
//					+ "' AND '"
//					+ (StrUtils.isNull(endDateA) ? "9999-12-31" : endDateA)
//					+ "')";
//		}
//
//		if (!StrUtils.isNull(startDateATA) || !StrUtils.isNull(endDateATA)) {
//			qry += "\nAND (t.ata BETWEEN '"
//					+ (StrUtils.isNull(startDateATA) ? "0001-01-01"
//							: startDateATA) + "' AND '"
//					+ (StrUtils.isNull(endDateATA) ? "9999-12-31" : endDateATA)
//					+ "')";
//		}
//
//		if (!StrUtils.isNull(startDateETD) || !StrUtils.isNull(endDateETD)) {
//			qry += "\nAND (t.etd BETWEEN '"
//					+ (StrUtils.isNull(startDateETD) ? "0001-01-01"
//							: startDateETD) + "' AND '"
//					+ (StrUtils.isNull(endDateETD) ? "9999-12-31" : endDateETD)
//					+ "')";
//		}
//
//		if (!StrUtils.isNull(startDateATD) || !StrUtils.isNull(endDateATD)) {
//			qry += "\nAND (t.atd BETWEEN '"
//					+ (StrUtils.isNull(startDateATD) ? "0001-01-01"
//							: startDateATD) + "' AND '"
//					+ (StrUtils.isNull(endDateATD) ? "9999-12-31" : endDateATD)
//					+ "')";
//		}

		m.put("qry", qry);

		if(StrUtils.isNull(sonoselect)){
			m.put("sonoselect", "1=1");
		}else {
			sonoselect = sonoselect.trim();
			m.put("sonoselect", "EXISTS (SELECT 1 FROM bus_ship_container bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.sono ILIKE '%"+sonoselect+"%')");
		}

		if (StrUtils.isNull(carryitem)) { // 运费条款的查询（连接bus_shipping）
			m.put("carryitem", "1=1");
		} else {
			String sql = "EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.id AND isdelete = FALSE AND carryitem = '"
					+ carryitem + "')";
			m.put("carryitem", sql);
		}
		//初始化
		dynamicClauseWhere = " 1=1 ";
		//高级检索中日期区间查询拼接语句
		if(isdate){	
			if("sodate".equals(dates)){
				dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.sodate BETWEEN '"
					+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
					+ "' AND '"
					+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
					+ "'))";
			}else if("dategatein".equals(dates)){
				dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.dategatein BETWEEN '"
					+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
					+ "' AND '"
					+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
					+ "'))";
			}else{
				dynamicClauseWhere  += "\nAND "+dates+"::DATE BETWEEN '"
				+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
				+ "' AND '"
				+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
				+ "'";
			}
		}
		//高级检索中单号查询拼接
		if(isjob||isrefno||isbook||ishbl||ismbl||issendc||iscustoms||isreceipt||isinvoice||isnumber||isno||istitle||iscommission||isbill||islink||ispo){
			dynamicClauseWhere += "\nAND (FALSE ";
			if(isjob){
				if(!StrUtils.isNull(numbers)){
					String sign = "";
					numbers = numbers.trim().replace("，", ",");;
					if(numbers.indexOf(",")>-1){//判断是否用英文逗号分割
						sign = ",";
					}else if(numbers.indexOf(" ")>-1 ){//以上两组不满足则判断是否用空格分割
						sign = " ";
					}
					if("".equals(sign)){
						//10位以上的工作单号精准查询
						if(numbers.length()>10){
							dynamicClauseWhere +="\nOR nos = '"+numbers+"'";	
						}else{
							dynamicClauseWhere +="\nOR nos ILIKE '%"+numbers+"%'";	
						}
					}else{
						String[] number = numbers.split(sign);
						dynamicClauseWhere +="\nOR nos = any(select '"+number[0].trim()+"'";
						for (int i = 1; i < number.length; i++) {
							number[i] = number[i].trim();
							dynamicClauseWhere +=" UNION select '"+number[i]+"'";
						}
						dynamicClauseWhere += ")";
					}
				}		
			}
			if(isrefno){
				dynamicClauseWhere +="\nOR refno ILIKE '%"+numbers+"%'";	
			}
			if(isbook){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.sono ILIKE '%"+numbers+"%')";
			}
			if(ishbl){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM bus_ship_bill bs WHERE  isdelete = FALSE AND bltype='H' AND bs.jobid=t.id AND bs.hblno ILIKE '%"+numbers+"%')";
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.hblno ILIKE '%"+numbers+"%')";
			}
			if(ismbl){
				dynamicClauseWhere +="\nOR (EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.mblno ILIKE '%"+numbers+"%') OR EXISTS (SELECT 1 FROM bus_ship_bill bs WHERE  isdelete = FALSE AND bltype='M' AND bs.jobid=t.id AND bs.mblno ILIKE '%"+numbers+"%'))";	
			}
			if(issendc){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM bus_truck bt WHERE isdelete = FALSE AND bt.jobid=t.id AND bt.nos ILIKE '%"+numbers+"%')";	
			}
			if(iscustoms){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM bus_customs bc WHERE isdelete = FALSE AND bc.jobid=t.id AND bc.customno ILIKE '%"+numbers+"%')";	
			}
			if(isreceipt){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM fina_bill fb WHERE isdelete = FALSE AND fb.jobid=t.id AND fb.billno ILIKE '%"+numbers+"%')";	
			}
			if(isinvoice){
				dynamicClauseWhere +="\nOR t.invoceno ILIKE '%"+numbers+"%' ";	
			}
			if(isnumber){
				dynamicClauseWhere +="\nOR " +
						"EXISTS(SELECT 1 FROM fina_invoice fi WHERE isdelete = FALSE AND fi.jobid=t.id AND fi.invoiceno ILIKE '%"+numbers+"%')";	
			}
			if(isno){
				String numbersplit = numbers.replaceAll(",","%,%");
				dynamicClauseWhere +="\nOR " +
						"EXISTS(SELECT 1 FROM bus_ship_container bs WHERE isdelete = FALSE AND bs.jobid=t.id " +
						"AND bs.cntno ILIKE ANY(regexp_split_to_array('%"+numbersplit+"%',',')))";	
			}
			if(istitle){
				dynamicClauseWhere +="\nOR " +
						"EXISTS (SELECT 1 FROM bus_ship_container bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.sealno ILIKE '%"+numbers+"%')";	
			}
			if(iscommission){
				dynamicClauseWhere +="\nOR " +
						"EXISTS (SELECT 1 FROM bus_order bo WHERE bo.isdelete = FALSE AND bo.id = t.orderid AND bo.orderno ILIKE" +"'%"+numbers+"%')";	
			}
			if(isbill){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM bus_ship_bill bsb WHERE bsb.isdelete = FALSE AND bsb.jobid=t.id AND (bsb.hblno ILIKE " +
						"'%"+numbers+"%' OR bsb.mblno LIKE '%"+numbers+"%'))";	
			}
			if(islink){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM fina_jobs j,fina_jobs_link k WHERE k.jobidto = j.id AND k.jobidfm = t.id AND nos LIKE '%"+numbers+"%')";
			}
			if(ispo){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.pono ILIKE '%"+numbers+"%')";
			}
			dynamicClauseWhere += ")";
		}
		boolean flag = false;
		//业务状态

		busstatusstr = "";
		if ("true".equals(notshipped)) {
			busstatusstr += "F,";
		}
		if ("true".equals(shipped)) {
			busstatusstr += "T,";
		}
		if ("true".equals(unloaded)) {
			busstatusstr += "R,";
		}
		if(!StrUtils.isNull(busstatusstr)){
			flag = true;
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.busstatus "+FiledownloadBean.getInCondition(busstatusstr.split(","), " ")+")";
		}
		
		//订舱代理  目前是匹配单个订舱代理，后期根据需求有可能需要做多个匹配
		if(!StrUtils.isNull(agentList)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.agentid = '"+agentList+"')";
		}
		
		//放箱
		if(isreleasecnt){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.isreleasecnt = "+isreleasecnt+")";
		}
		
		//提箱
		if(ispickcnt){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.ispickcnt = "+ispickcnt+")";
		}
		
		//是总单
		if(isTotalList){
			dynamicClauseWhere += "\n AND t.parentid is null";
		}
		
		//船名航次拼接
		if(vessel!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.vessel ILIKE '%"+vessel+"%')";
		}
		if(voyage!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.voyage ILIKE '%"+voyage+"%')";
		}
		//目的地
		if(destination!=""){
			dynamicClauseWhere+="\nAND EXISTS " +
					"(SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.destination ILIKE '%"+destination+"%')";
		}
		//航线
		if(routecode!=""){
			dynamicClauseWhere+="\nAND EXISTS " +
					"(SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.routecode ILIKE '%"+routecode+"%')";
		}
		//装箱
		if(!StrUtils.isNull(ldtype)){
			dynamicClauseWhere+="\nAND t.ldtype = '"+ldtype+"'";
		}
		
		//进出口
		if(!StrUtils.isNull(impexp)){
			dynamicClauseWhere+="\nAND impexp = '"+impexp+"'";
		}
		
		//接单公司
		if(corpid!=""){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.id=t.corpid AND sc.id = "+corpid+")";		
		}
		//接单公司部门
		if(deptid!=""){
			dynamicClauseWhere +="\nAND EXISTS(SELECT 1 FROM sys_user u WHERE u.isdelete = FALSE AND u.id = t.saleid AND u.deptid = "+deptid+")";		
		}
		//操作公司
		if(corpidop!=""){
			dynamicClauseWhere +="\nAND "+corpidop+" = ANY(SELECT t.corpidop UNION SELECT c.corpid FROM fina_corp c WHERE c.jobid = t.id)";
		}
		//录入人
		if(saleid!=""){
			dynamicClauseWhere +="\nAND t.inputer = '"+saleid+"'";
		}
		//业务员
		if(sale!=0){
			dynamicClauseWhere +="\nAND t.saleid = '"+sale+"'";
		}
		//业务员
		if(sale2!=0){
			dynamicClauseWhere +="\nAND t.saleid = '"+sale2+"'";
		}
		//指派业务员
		if(assigneesales!=0){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'S' AND x.userid ='"+assigneesales+"')";
		}
		//港口
		if(port!=""){
			dynamicClauseWhere +="\nAND "+portss+" LIKE '%"+port+"%'";
		}
		
		//MBL提单号
		if(mblqry!=""){
			dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.mblno ILIKE '%"+mblqry+"%') OR EXISTS (SELECT 1 FROM bus_ship_bill bs WHERE isdelete = FALSE AND bs.jobid=t.id AND bs.mblno ILIKE '%"+mblqry+"%'))";
		}		
		//MBL取单
		if(!StrUtils.isNull(usergetmbl)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.usergetmbl='"+usergetmbl+"')";
		}
		//MBL放单
		if(!StrUtils.isNull(userreleasembl)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.userreleasembl ='"+userreleasembl+"')";
		}		
		//客户
		if(customerid!=0&&allcustomer.equals("customerid")){
			dynamicClauseWhere +="\nAND "+allcustomer+" ='"+customerid+"'";
		}
		if(customerid!=0&&allcustomer.equals("agentdesid")||(allcustomer.equals("carrierid"))||(allcustomer.equals("agentid"))){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE isdelete = FALSE AND bs.jobid=t.id AND bs."+allcustomer+" ='"+customerid+"')";
		}
		if(customerid!=0&&allcustomer.equals("clientid")){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM fina_arap fa WHERE  isdelete = FALSE AND fa.jobid=t.id AND fa.customerid="+customerid+")";
		}
		if(customerid!=0&&allcustomer.equals("truckid")){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_truck bt WHERE  isdelete = FALSE AND bt.jobid=t.id AND bt."+allcustomer+" ='"+customerid+"')";
		}
		if(customerid!=0&&allcustomer.equals("customid")||allcustomer.equals("clearancecusid")){
			dynamicClauseWhere+="\n AND EXISTS(SELECT 1 FROM bus_customs WHERE isdelete = FALSE AND jobid = t.id AND "+allcustomer+"="+customerid+")";
		}

		//客户2
		if(customerid2!=0){
			dynamicClauseWhere +="\nAND customerid ='"+customerid2+"'";
		}

		//发货人
		if(!StrUtils.isNull(cnortitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.cnortitle ILIKE '%"+StrUtils.getSqlFormat(cnortitle)+"%')";
		}
		//收货人
		if(!StrUtils.isNull(cneetitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.cneetitle ILIKE '%"+StrUtils.getSqlFormat(cneetitle)+"%')";
		}
		//通知人
		if(!StrUtils.isNull(notifytitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.notifytitle ILIKE '%"+StrUtils.getSqlFormat(notifytitle)+"%')";
		}
		
		//MBL发货人
		if(!StrUtils.isNull(cnortitlembl)){
			/*System.out.println("cnortitlembl==>"+cnortitlembl);*/
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.cnortitlembl ILIKE '%"+StrUtils.getSqlFormat(cnortitlembl)+"%')";
		}
		//MBL收货人
		if(!StrUtils.isNull(cneetitlembl)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.cneetitlembl ILIKE '%"+StrUtils.getSqlFormat(cneetitlembl)+"%')";
		}
		//MBL通知人
		if(!StrUtils.isNull(notifytitlembl)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.notifytitlembl ILIKE '%"+StrUtils.getSqlFormat(notifytitlembl)+"%')";
		}
		
		//搜索指派中的文件
		if(!StrUtils.isNull(filsid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'D' AND x.userid ='"+filsid+"')";
		}
		//搜索指派中的操作
		if(!StrUtils.isNull(operationid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'O' AND x.userid ='"+operationid+"')";
		}
		//搜索指派中的客服
		if(!StrUtils.isNull(callid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'C' AND x.userid ='"+callid+"')";
		}
		//搜索指派中的市场
		if(!StrUtils.isNull(marketid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'M' AND x.userid ='"+marketid+"')";
		}
		//询价人
		if(!StrUtils.isNull(priceuserid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.priceuserid ='"+priceuserid+"')";
		}
		//业务状态
		if(!StrUtils.isNull(busstatusdesc)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.busstatus ='"+busstatusdesc+"')";
		}
		//完成状态
		if(!StrUtils.isNull(iscompleteqry)){
			dynamicClauseWhere += "\n AND t.iscomplete=" + iscompleteqry;
		}
		//完成状态
		if(!StrUtils.isNull(ischeck)){
			dynamicClauseWhere += "\n AND t.ischeck=" + ischeck;
		}
		//工作单未审核,费用未完成; 23年8月新需求;该过滤只需要23年7月之后的数据
		if (!StrUtils.isNull(filterStatus)){
			String checkDate = ConfigUtils.findSysCfgVal("sys_saled_check_date");
			if(filterStatus.equals("jobsunapproved")){
				dynamicClauseWhere += "\n AND CURRENT_DATE > t.submitime + INTERVAL '"+checkDate+" day' AND t.submitime > '2023-07-01'";
				dynamicClauseWhere += "\n AND t.ischeck = false";
			}else if (filterStatus.equals("arapunapproved")) {
				dynamicClauseWhere += "\n AND CURRENT_DATE > t.submitime + INTERVAL '"+checkDate+" day' AND t.submitime > '2023-07-01'";
				dynamicClauseWhere += "\n AND (t.iscomplete_ap = false OR t.iscomplete_ar = false)";
			}
		}
		//船公司
		if(!StrUtils.isNull(carriername)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE isdelete = FALSE AND bs.jobid=t.id AND bs.carrierid ='"+carriername+"')";
		}
		//贸易方式
		if(!StrUtils.isNull(tradewaycode)){
            dynamicClauseWhere += "\n AND t.tradeway='" + tradewaycode+"'";
        }
		//贸易方式
		if(!StrUtils.isNull(cargotype)){
            dynamicClauseWhere += "\n AND t.cargotype='" + cargotype+"'";
        }
		//搜索指派中的财务跟单
		//改为动态类型，取数据字典中的类型然后匹配
		if(!StrUtils.isNull(assignid)&&!StrUtils.isNull(roletype)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = '"+roletype+"' AND x.userid ='"+assignid+"')";
		}
		if(isprofit){//工作单利润折合金额
			dynamicClauseWhere += "\n AND (f_findarapinfo_profit_only('jobid='||t.id||';tax=Y;userid="+AppUtils.getUserSession().getUserid()
				  +";currency="+profit_currency+"'))"+profit_compare+profit;
		}
		if(iscl!=null&&iscl.equals("1")){
			dynamicClauseWhere+= "\n AND t.isclc=FALSE";
		}else if(iscl!=null&&iscl.equals("2")){
			dynamicClauseWhere+= "\n AND t.isclc=TRUE";
		}
		if(!StrUtils.isNull(isbustruck)){
			dynamicClauseWhere += "\n AND "+(isbustruck.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM bus_truck WHERE jobid = t.id AND isdelete = false)";
			dynamicClauseWhere += "\n AND EXISTS(SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.isdelete = false" +
								  "\n AND EXISTS(SELECT 1 FROM dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 110 AND " +
								  "\n s.trucktype = d.code AND d.namec = '我司安排'))";
		}
		if(!StrUtils.isNull(isbuscustoms)){
			dynamicClauseWhere += "\n AND "+(isbuscustoms.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM bus_customs WHERE jobid = t.id AND isdelete = false)";
			dynamicClauseWhere += "\n AND EXISTS(SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.isdelete = false" +
								  "\n AND EXISTS(SELECT 1 FROM dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 40 AND " +
								  "\n s.custype = d.code AND d.namec = '我司安排'))";
		}
		//VGM
		if(!StrUtils.isNull(isvgm)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id " +
								 "AND s.vgmstatus ='"+(isvgm.equals("Y")?"A":"U")+"')";
		}
		//SO/补料/提单正本：工作单附件，存在文件组类型：SO  原始补料  提单正本
		if(!StrUtils.isNull(isso)){
			dynamicClauseWhere += "\nAND "+(isso.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM sys_attachment x WHERE x.linkid = t.id " +
							      "AND EXISTS(SELECT 1 FROM sys_role WHERE id = x.roleid AND name = 'SO'))";
		}
		if(!StrUtils.isNull(isfeeding)){
			dynamicClauseWhere += "\nAND "+(isfeeding.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM sys_attachment x WHERE x.linkid = t.id " +
		      "AND EXISTS(SELECT 1 FROM sys_role WHERE id = x.roleid AND name = '原始补料'))";
		}
		if(!StrUtils.isNull(isbltype)){
			dynamicClauseWhere += "\nAND "+(isbltype.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM sys_attachment x WHERE x.linkid = t.id " +
		      "AND EXISTS(SELECT 1 FROM sys_role WHERE id = x.roleid AND name = '提单正本扫描'))";
		}
		//电放
		if(!StrUtils.isNull(istelrelv)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.istelrel = "
								+(istelrelv.equals("Y")?"TRUE":"FALSE")+")";
		}
		//跟踪
		if(!StrUtils.isNull(statetracenext)){
			dynamicClauseWhere +="\nAND t.statetrace ILIKE '%"+statetracenext+"%' ";	
		}
		//MBL类型
		if(!StrUtils.isNull(qrymbltype)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.mbltype='"+qrymbltype+"')";
		}
		
		if(!StrUtils.isNull(customertype)){
			dynamicClauseWhere +="\nAND t.customerid = ANY(SELECT c.id FROM sys_corporation c WHERE c.customertype = '"+customertype+"' AND isdelete = FALSE)";
		}
		//放货状态
		if(putstatus!="" && !putstatus.equals(" ")){
			dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.putstatus ILIKE '%"+putstatus+"%'))";
		}
		if(null != this.qryMap.get("nos") && !StrUtils.isNull(String.valueOf(this.qryMap.get("nos")))){
			flag = true;
		}
		if(null != this.qryMap.get("orderno") && !StrUtils.isNull(String.valueOf(this.qryMap.get("orderno")))){
			flag = true;
		}
		if(null != this.qryMap.get("refno") && !StrUtils.isNull(String.valueOf(this.qryMap.get("refno")))){
			flag = true;
		}
		if(null != this.qryMap.get("mblno") && !StrUtils.isNull(String.valueOf(this.qryMap.get("mblno")))){
			flag = true;
		}
		if(null != this.qryMap.get("hblno") && !StrUtils.isNull(String.valueOf(this.qryMap.get("hblno")))){
			flag = true;
		}
		if(!flag){
			String csno = ConfigUtils.findSysCfgVal("CSNO");
			if("8888".equals(csno) || "2274".equals(csno)){//世倡和中集 ：退载默认不显示
				dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_shipping x WHERE x.isdelete = FALSE AND x.jobid = t.id AND COALESCE(x.busstatus,'') <> 'R')";
			}
		}
		if(putstatus.equals(" ")){
			dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND (s.putstatus = '' OR s.putstatus is null)))";
		}
		if(!StrUtils.isNull(shipagents)){
			shipagents = shipagents.trim();
			dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.shipagent ILIKE '%"+shipagents+"%'))";
		}
		if(!StrUtils.isNull(potcod)){
			potcod = potcod.trim();
			dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.potcode ILIKE '%"+potcod+"%'))";
		}

		// dynamicClauseWhere+= "\n and shipjoinos is not null";
		m.put("dynamicClauseWhere", dynamicClauseWhere);
//		if (!iscare) {
			m.put("icare", "1=1");
			
			
//		} else {
//			String sql1 = "(EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="
//					+ AppUtils.getUserSession().getUserid() + ")";
//			sql1 += "OR t.inputer ='" + AppUtils.getUserSession().getUsercode()
//					+ "' OR t.updater = '"
//					+ AppUtils.getUserSession().getUsercode() + "')";
//			m.put("icare", sql1);
//
//		}
		//2412 工作单编辑界面上一单下一单按列表过滤条件（将列表查询的条件先保存到个人设置里面，编辑界面上一单下一单的时候，再把列表的过滤条件查出来拼进去）
		try {
			ConfigUtils.refreshUserCfg("pages.module.ship.jobsBean.search", 
					StrUtils.getSqlFormat(qry+" AND "+dynamicClauseWhere)
					, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean isshow = false;
		try{
			String isshowpublicsql = "SELECT EXISTS(SELECT 1 FROM sys_role sr , sys_modinrole am WHERE sr.code = '"+AppUtils.getUserSession().getUsercode()+"' AND roletype = 'C' AND am.roleid = sr.id AND am.moduleid = 299220)"
							+"\nOR EXISTS(SELECT 1 FROM sys_role sr , sys_modinrole am , sys_userinrole ur WHERE roletype = 'M' AND am.roleid = sr.id AND am.moduleid = 299220 AND ur.roleid = sr.id AND ur.userid = "+AppUtils.getUserSession().getUserid()+") AS flag";
			Map ispublic = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(isshowpublicsql);
			if("true".equals(StrUtils.getMapVal(ispublic, "flag"))){
				isshow = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			isshow = false;
		}
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
							"AND t.corpid <> "+AppUtils.getUserSession().getCorpidCurrent()+" AND "+AppUtils.getUserSession().getCorpidCurrent()+" = ANY(SELECT t.corpidop UNION SELECT t.corpidop2 UNION (SELECT corpid FROM fina_corp c WHERE c.jobid = t.id AND c.isdelete =FALSE)))"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid " //组关联业务员的单，都能看到
				+ ")"
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND x.isdelete = FALSE AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)"
				//2749 税局查账系统分离方案
				+(isshow==true?"\nAND EXISTS(SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.jobid = t.id"
						+" AND EXISTS(SELECT 1 FROM sys_corporation y WHERE y.isdelete = FALSE AND y.iscustomer = TRUE "
						+"AND y.id = x.customerid))":"");

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		
		
//		String corpfilter = "";
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			//分公司过滤(忽略迪拜)
//			corpfilter = "AND  (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = t.corpid OR cor.id = t.corpidop)) " +
//					"\nOR t.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR t.saleid = "+AppUtils.getUserSession().getUserid() +
//					")";
//			m.put("corpfilter", corpfilter);
//		}
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT t.corpid UNION SELECT t.corpidop UNION SELECT t.corpidop2 UNION SELECT corpid FROM fina_corp c WHERE c.jobid = t.id AND c.isdelete = FALSE) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		//"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+" OR COALESCE(t.saleid,0) <= 0)";
		"\n OR COALESCE(t.saleid,0) <= 0)";
		m.put("corpfilter", corpfilter);
		if("0".equals(this.setDays)){
			String setDays = "\n AND 1=1";
			m.put("setDays", setDays);
		}else{
			String setDays = "\n AND t.jobdate::DATE >= NOW()::DATE-"+(StrUtils.isNull(this.setDays)?"365":this.setDays);
			m.put("setDays", setDays);
		}
		
		
		m.put("userid", AppUtils.getUserSession().getUserid());
		// 针对中东世联达 中东并单情况，如接单公司是途曦，操作公司是中集世联达，中集世联达 用户 发货人固定显示 TOP CHINA
		m.put("corpid", AppUtils.getUserSession().getCorpid());

		String goodstrack_isShowIscs = ConfigUtils.findUserCfgVal("goodstrack_isShowIscs",AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(goodstrack_isShowIscs)||"false".equals(goodstrack_isShowIscs)){
			m.put("isShowIscs", "\nAND iscs = FALSE ");
		}

		String sqlpriceuser = "";
		if(!StrUtils.isNull(priceuser)){
			sqlpriceuser += "\n AND EXISTS (SELECT 1 FROM sys_user WHERE (namec like '%"+priceuser+"%' OR namee like '%"+priceuser+"%' OR  code like '%"+priceuser+"%') AND id = " +
					"(select priceuserid from bus_shipping bs where bs.isdelete = FALSE and bs.jobid = t.id limit 1) AND isdelete = FALSE)";
			m.put("sqlpriceuser", sqlpriceuser);
		}
		if(!StrUtils.isNull(priceuserconfirm)){
			sqlpriceuser += "\n AND EXISTS (select 1 from bus_shipping bs where bs.isdelete = FALSE and bs.jobid = t.id and bs.priceuserconfirm ="+priceuserconfirm+" limit 1)";
			m.put("sqlpriceuser", sqlpriceuser);
		}
//		if(!StrUtils.isNull(customerabbr)){
//			sqlpriceuser += "\n AND (t.customerabbr = '"+customerabbr.trim()+"' OR t.customerid = "+customerabbr.trim()+")";
//			m.put("sqlpriceuser", sqlpriceuser);
//		}
		
		m.put("sqlloguserid", AppUtils.getUserSession().getUserid());
		
		return m;
	}
	
	@Bind
	public UIWindow copyAndCreatingWindow;
	
	@Bind
	public UIWindow unloaDaysWindows;
	
	@Bind
	@SaveState
	public String copyAndCreateCorpid;
	
	@Bind
	@SaveState
	public String copyAndCreateCorpidop;
	
	@Bind
	@SaveState
	public Date copyAndCreateJobdate;
	
	@Bind
	public String includFee;
	
	@Bind
	public String serialno;
	
	@Bind
	public String entrust;
	
	@Bind
	public String bookinginfo;
	
	@Bind
	public String portinfo;
	
	@Bind
	public String packing;
	
	@Bind
	public String bookingagent;
	
	@Bind
	public String productinfo;
	
	@Bind
	public String shippingterms;
	
	@Bind
	public String boxnum;
	
	@Bind
	public String vesvoy;
	
	@Bind
	public String refeno;
	
	@Bind
	public String ordernum;
	
	@Bind
	public String bookingnum;
	
	@Bind
	public String hblnum;
	
	@Bind
	public String mblnum;
	
	@Bind
	public String cutday;
	
	@Bind
	public String etacopy;
	
	@Bind
	public String etdcopy;
	
	@Bind
	public String atdcopy;
	
	@Bind
	public String atacopy;
	@Bind
	public String dispcopy;
	
	@SaveState
	private Long corpids;
	
	@Bind
	private String packinglist;
	
	@Action
	public void addcopy() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		FinaJobs jobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(Long.parseLong(ids[0]));
		this.corpids = jobs.getCorpid();
		this.copyAndCreateCorpid = jobs.getCorpid().toString();
		this.copyAndCreateCorpidop = jobs.getCorpidop().toString();
		this.copyAndCreateJobdate = new Date();
		update.markUpdate(UpdateLevel.Data, "copyAndCreateCorpid");
		update.markUpdate(UpdateLevel.Data, "copyAndCreateCorpidop");
		update.markUpdate(UpdateLevel.Data, "copyAndCreateJobdate");
		update.markUpdate(UpdateLevel.Data, "serialno");
		this.copyAndCreatingWindow.show();
	}
	
	@Action
	public void cancelCopyAndCreating(){
		this.copyAndCreatingWindow.close();
	}
	
	@Action
	public void copyAndCreating(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String nos = serviceContext.jobsMgrService.addcopy(ids[0], AppUtils.getUserSession().getUserid(), AppUtils.getUserSession().getUsercode()
					,this.copyAndCreateCorpid, this.copyAndCreateCorpidop, dateFormat.format(this.copyAndCreateJobdate),this.includFee,this.serialno
					,this.entrust,this.bookinginfo,this.portinfo,this.packing,this.bookingagent,this.productinfo,this.shippingterms,this.boxnum,this.vesvoy
					,this.refeno,this.ordernum,this.bookingnum,this.hblnum,this.mblnum,this.cutday,this.etacopy,this.etdcopy,this.atdcopy,this.atacopy,this.packinglist,this.dispcopy);
			MessageUtils.alert("复制生成的单号是:" + nos);
			this.qryMap.clear();
			this.qryMap.put("nos", nos);
			refresh();
			this.copyAndCreatingWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void clearQryKey() {
		this.startDateJ = "";
		this.endDateJ = "";
		this.startDateA = "";
		this.endDateA = "";
		this.startDateATA = "";
		this.endDateATA = "";
		startDateETD = "";
		endDateETD = "";
		startDateATD = "";
		endDateATD = "";
		isdate=false;
		dates="";
		dateastart="";
		dateend="";
		isjob=false;
		isrefno=false;
		isbook=false;
		ishbl=false;
		ismbl=false;
		issendc=false;
		iscustoms=false;
		isreceipt=false;
		isinvoice=false;
		isnumber=false;
		isno=false;
		istitle=false;
		iscommission=false;
		isbill=false;
		numbers="";
		allcustomer="";
		customerid=0;
		vessel="";
		voyage="";
		portss="";
		port="";
		destination="";
		sale=0;
		sale2=0;
		ldtype="";
		saleid="";
		corpid="";
		corpidop="";
		deptid="";
		usergetmbl="";
		userreleasembl="";	
		filsid = "";
		operationid = "";
		callid = "";
		marketid = "";
		priceuserid = "";
		assignid = "";
		iscl = "3";
		isbustruck = "";
		isbuscustoms = "";
		isvgm = "";
		isso = "";
		isfeeding = "";
		isbltype = "";
		istelrelv = "";
		routecode = "";
		cnortitle = "";
		cneetitle = "";
		notifytitle = "";
		mblqry = "";
		agentList 	= "";		//订舱代理
		isTotalList = false;	//是总单
		shipagents = "";		//是总单
		busstatusstr = "";			//业务状态
		this.priceuserid = "";	//询价员
		roletype = "";			//
		cnortitlembl = "";		//发货人MBL
		cneetitlembl = "";		//收货人MBL
		notifytitlembl = "";	//通知人MBL
		statetracenext = "";	//跟踪
		customertype = "";		//客户类型
		isreleasecnt = false;	//客户类型
		ispickcnt = false;		//客户类型
		potcod = "";			//客户类型
		ispo = false;

		customerid2 = 0;
		Browser.execClientScript("$('#customer2_input').val('')");

		sale2 = 0;
		Browser.execClientScript("$('#sales2_input').val('')");

		Browser.execClientScript("$('#sales_input').val('')");
		Browser.execClientScript("$('#inputers_input').val('')");
		Browser.execClientScript("$('#usergetmblflex_input').val('')");
		Browser.execClientScript("$('#userreleasemblflex_input').val('')");
		Browser.execClientScript("$('#fils_input').val('')");
		Browser.execClientScript("$('#operation_input').val('')");
		Browser.execClientScript("$('#call_input').val('')");
		Browser.execClientScript("$('#customer_input').val('')");
		Browser.execClientScript("$('#port_input').val('')");
		Browser.execClientScript("$('#market_input').val('')");
		Browser.execClientScript("$('#finance_input').val('')");
		this.update.markUpdate(UpdateLevel.Data, "startDateJ");
		this.update.markUpdate(UpdateLevel.Data, "endDateJ");
		this.update.markUpdate(UpdateLevel.Data, "startDateA");
		this.update.markUpdate(UpdateLevel.Data, "endDateA");
		this.update.markUpdate(UpdateLevel.Data, "startDateATA");
		this.update.markUpdate(UpdateLevel.Data, "endDateATA");
		this.update.markUpdate(UpdateLevel.Data, "startDateETD");
		this.update.markUpdate(UpdateLevel.Data, "endDateETD");
		this.update.markUpdate(UpdateLevel.Data, "startDateATD");
		this.update.markUpdate(UpdateLevel.Data, "endDateATD");
//		super.clearQryKey();
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
	}

	@Action
	public void search() {
		this.searchWindow.show();
	}

	@Action
	public void clear() {
		this.clearQryKey();
	}
	
	@Action
	public void clearAndClose() {
		this.clearQryKey();
		this.searchWindow.close();
	}
	
	
	

	// @Action
	// public void showTrace(){
	// Long id = Long.valueOf(AppUtil.getReqParam("id"));
	// //如果没有操作说明就不弹窗
	// String sql = "SELECT 1 FROM bus_goodstrack WHERE fkid = "+id+" LIMIT 1";
	// try {
	// Map m = serviceContext.jobsMgrService.daoIbatisTemplate
	// .queryWithUserDefineSql4OnwRow(sql);
	// String sql2 = "SELECT f_bus_goodstrack_get('id="+id+";type=P')AS trace";
	//			
	// Map m2 = serviceContext.jobsMgrService.daoIbatisTemplate
	// .queryWithUserDefineSql4OnwRow(sql2);
	// trace = (String) m.get("trace");
	// this.update.markUpdate(UpdateLevel.Data, "trace");
	// showDtlWindow.show();
	// } catch (Exception e) {
	// showDtlWindow.close();
	// }
	//		
	// }

	@Bind
	@SaveState
	public String trace;

	@Bind
	public UIWindow showDtlWindow;

	@Action
	private void createDesktopLink() {
//		if(!applicationConf.getIsUseDzz()){
//			this.alert("DZZ未启用");
//			return;
//		}
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录");
			return;
		}
		String httport = AppUtils.getServerHttPort();
		String dzzuid = AppUtils.getUserSession().getDzzuid();
		Vector<String> sqlBatch = new Vector<String>();
		for (String id : ids) {
			String sqlQry = "SELECT f_dzz_desktop_add('httport=" + httport
					+ ";type=Jobs;jobid=" + id + ";dzzuid=" + dzzuid
					+ ";') As t;";
			Map m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sqlQry);
			String sql = StrUtils.getMapVal(m, "t");
			sqlBatch.add(sql);
		}
		try {
//			this.serviceContext.dzzService.executeQueryBatchByJdbc(sqlBatch);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action(id="corpid",event="onchange")
	public void corpid_onchange(){
		this.deptids = getqueryDepartid();
		this.deptid = "";
		update.markUpdate(true,UpdateLevel.Data,"deptid");
		update.markUpdate(true,UpdateLevel.Data,"deptids");
	}
	
	private List<SelectItem> getqueryDepartid(){
		try {
			Long id =  !StrUtils.isNull(this.corpid)  ? Long.parseLong(this.corpid) : -1L;
			List<SelectItem> list = CommonComBoxBean.getComboxItems("de.id", "de.name", "sys_department as de", "WHERE de.isdelete = FALSE AND EXISTS (SELECT * FROM sys_user us WHERE us.isdelete = FALSE AND us.deptid = de.id AND corpid = "+id+")", "ORDER BY de.name");
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Action
	public void cancel(){
		unloaDaysWindows.close();
	}
	
	protected void initData(){
		String findUserCfgVal = ConfigUtils.findUserCfgVal("jobs_ship_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			this.setDays="365";
		}else{
			this.setDays = findUserCfgVal;
		}
	}
	
	@Action
	public void confirmSave(){
		try {
			ConfigUtils.refreshUserCfg("jobs_ship_date",this.setDays, AppUtils.getUserSession().getUserid());
			alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void showSortWindow(){
		sortlabel_1 = null;
		sortvalue_1 = "asc";
		sortlabel_2 = null;
		sortvalue_2 = "asc";
		sortlabel_3 = null;
		sortvalue_3 = "asc";
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(!StrUtils.isNull(ordersql)){
			String[] sorts = ordersql.split(",");
			if(sorts != null){
				for (int i = 0; i < sorts.length; i++) {
						switch (i+1) {
						case 1:
							sortlabel_1 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_1 = "desc";
							}else{
								sortvalue_1 = "asc";
							}
							break;
						case 2:
							sortlabel_2 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_2 = "desc";
							}else{
								sortvalue_2 = "asc";
							}
							break;
						case 3:
							sortlabel_3 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_3 = "desc";
							}else{
								sortvalue_3 = "asc";
							}
							break;
						}
				}
			}
		}
		update.markUpdate(true,UpdateLevel.Data, "sortpanel");
		sortWindow.show();
	}
	@Action
	public void resetUserColorder(){
		sortlabel_1 = null;
		sortvalue_1 = "asc";
		sortlabel_2 = null;
		sortvalue_2 = "asc";
		sortlabel_3 = null;
		sortvalue_3 = "asc";
		update.markUpdate(true,UpdateLevel.Data, "sortpanel");
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", null);
		MessageUtils.alert("OK!");
	}
	@Action
	public void saveUserColorder(){
		String colorder = "";
		if(!StrUtils.isNull(sortlabel_1)&&!StrUtils.isNull(sortvalue_1)){
			colorder += sortlabel_1 + " " + sortvalue_1 + ",";
		}
		if(!StrUtils.isNull(sortlabel_2)&&!StrUtils.isNull(sortvalue_2)){
			colorder += sortlabel_2 + " " + sortvalue_2 + ",";
		}
		if(!StrUtils.isNull(sortlabel_3)&&!StrUtils.isNull(sortvalue_3)){
			colorder += sortlabel_3 + " " + sortvalue_3 + ",";
		}
		
		colorder = colorder.endsWith(",") ? colorder.substring(0, colorder.length()-1) : colorder;
		
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", colorder);
		MessageUtils.alert("OK!");
	}
	
	@Bind
	public UIButton batchModify;
	
	@Bind
	public UIWindow showbatchupdateWindow;
	
	@Bind
	public UIWindow showbatchupMoneyWindow;
	
	@Bind
	public UIButton batchModifyMoney;
	
	@Action
	public void batchModifyMoney(){
		String[] ids = this.grid.getSelectedIds(); 
		if (ids == null ||ids.length == 0) { 
			MessageUtils.alert("请选择一条记录"); 
		}else{
			showbatchupMoneyWindow.show();
		}
	}
	
	
	@Action
	public void batchModify() {
		String[] ids = this.grid.getSelectedIds(); 
		if (ids == null ||ids.length == 0) { 
			MessageUtils.alert("请选择一条记录"); 
		}else{
			showbatchupdateWindow.show();
		}
	}
	
	@Bind
	@SaveState
	private Date etd;
	@Bind
	@SaveState
	private Date eta;
	@Bind
	@SaveState
	private Date clstime;
	@Bind
	@SaveState
	private Date sidate;
	@Bind
	@SaveState
	private String vessel1;
	@Bind
	@SaveState
	private String voyage1;
	
	@Bind
	@SaveState
	private Date atd;
	@Bind
	@SaveState
	private Date ata;
	@Bind
	@SaveState
	private Date loadtime;
	@Bind
	@SaveState
	private Date storehousedate;
	@Bind
	@SaveState
	private String destination1;
	@Bind
	@SaveState
	private Long agentdesid;
	@Bind
	@SaveState
	private Long clearancecusid;
	@Bind
	@SaveState
	private Long docuserid;
	@Bind
	@SaveState
	private Long opruserid;
	@Bind
	@SaveState
	private Long agentid;
	@Bind
	private Long puserid;
	@Bind
	private String mblno;
	@Bind
	private String bltype;
	@Bind
	private String mbltype;
	@Bind
	private String hbltype;
	@Bind
	private String routecodev;
	@Bind
	private Long carrieridv;
	
	
	
	
	@Action
	public void changeJobs() { 
		String[] ids = this.grid.getSelectedIds(); 
		String user = AppUtils.getUserSession().getUsercode();
		try {
			serviceContext.jobsMgrService.editJobs(ids, eta, etd,clstime,sidate,vessel1,voyage1,atd,ata,loadtime,storehousedate,destination1,agentdesid,clearancecusid,docuserid,opruserid,agentid,puserid,
					mblno,bltype,mbltype,hbltype,routecodev,carrieridv,user,editbusstatus,releasecnt,releasecnttime,pickcnt,pickcnttime,opcomp,potcode,sotime,pono,remark);
			refresh();
			Browser.execClientScript("showmsg()");
			showbatchupdateWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	@Action
	public void impTemplet() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行!");
			return;
		}
		String winId = "_arapTemplet";
		String url = "../other/feetemplatechooser.xhtml?type=batchjobs&joinids="+StrUtils.array2List(ids); 
		AppUtils.openWindow(winId, url);
	}
	
	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
					"sys_report AS d", " WHERE modcode = 'jobs_ship' AND isdelete = FALSE",
					"order by filename");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Action
	public void claerbatchupdate(){
		showbatchupdateWindow.close();
	}
	
	@Bind(id = "serial")
	public  List<SelectItem> getSerial() {
		Long csid =  this.corpids;
		try {
			List<SelectItem> list = CommonComBoxBean.getComboxItems("r.prefix", "r.prefix",
					"sys_busnodesc t ,sys_busnorule r ", "WHERE r.corpid="+csid+" AND t.id = r.busnotypeid AND t.code like 'fina_jobs%' AND t.isdelete = FALSE AND r.isdelete = FALSE AND t.code NOT LIKE 'fina_jobs_air%'",
			"group by prefix order by prefix");
			return list;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Action
	private void changerpid(){
		String corpidjs = AppUtils.getReqParam("corpidjs");
		String corpdopjs = AppUtils.getReqParam("corpdopjs");
		if(corpidjs != null && !corpidjs.isEmpty()){
			this.corpids = Long.parseLong(corpidjs);
			this.copyAndCreateCorpid = corpidjs;
			this.copyAndCreateCorpidop = corpdopjs;
			update.markUpdate(UpdateLevel.Data, "copyAndCreateCorpid");
			update.markUpdate(UpdateLevel.Data, "copyAndCreateCorpidop");
			update.markUpdate(UpdateLevel.Data, "serialno");
		}
	}
	
	@Bind
	public UIButton showDynamic;
	
	@Action
	public void showDynamic() {
		String[] ids = this.grid.getSelectedIds(); 
		if (ids == null ||ids.length == 0||ids.length > 1) { 
			MessageUtils.alert("请选择一条记录"); 
		}else{
			String url = AppUtils.getContextPath();
			String openurl = url + "/pages/module/jobs/jobsinfo.xhtml";
			AppUtils.openWindow("_showDynamic", openurl + "?jobid=" + ids[0]
					+ "&userid=" + AppUtils.getUserSession().getUserid());
		}
	}
	
	@Bind
	public UIIFrame taskCommentsIframe;
	@Bind
	public UIIFrame traceIframe;
	
	@Action
	public void showTaskCheckInfo(){
		String jobid = AppUtils.getReqParam("jobid");
		String sql = "SELECT id FROM bpm_processinstance WHERE refid = '"+jobid+"' ORDER BY id DESC LIMIT 1";
		Map m = null;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		} catch (Exception e) {
		}
		taskCommentsIframe.load("../../../bpm/bpmshowcomments.jsp?processinstanceid="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		traceIframe.load("../../../bpm/model/trace.html?language="+AppUtils.getUserSession().getMlType()
				+"&id="+((m!=null&&m.get("id")!=null)?m.get("id"):"0")
				+"&userid="+AppUtils.getUserSession().getUserid()+"&language="+AppUtils.getUserSession().getMlType().name());
		Browser.execClientScript("taskCheckInfoWindowJsVar.show();traceWindow.show()");
	}
	
	@Bind
	@SaveState
	public String againsType;
	
	@Bind
	@SaveState
	public Long againsaleid;
	
	
	@Action
	public void addAgainsale(){
		String[] ids = this.grid.getSelectedIds(); 
		if (ids == null ||ids.length == 0) { 
			MessageUtils.alert("请至少选择一条记录");
			return ;
		}
		try{
			serviceContext.sysUserAssignMgrService.addsAgainsale(againsType, againsaleid, ids);
			alert("OK");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Action
	public void coverAgainsale(){
		String[] ids = this.grid.getSelectedIds(); 
		if (ids == null ||ids.length == 0) { 
			MessageUtils.alert("请至少选择一条记录");
			return ;
		}
		try{
			serviceContext.sysUserAssignMgrService.coverAgainsale(againsType, againsaleid, ids);
			alert("OK");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 扣款申请流程
	 * @param ids
	 * @return
	 */
	@Action
	public void deductionapply() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 ) {
			MessageUtils.alert("请选择记录");
			return;
		}
		boolean custemerEqually = serviceContext.jobsMgrService.custemerEqually(ids);
		if(custemerEqually){
			MessageUtils.alert("选择多单情况下必须是同一客户");
			return;
		}
		
		String sql = "SELECT ((EXISTS (SELECT 1 FROM fina_arap a WHERE a.isdelete = FALSE AND a.araptype = 'AP' AND isconfirm = true AND " +
		"a.feeitemid = (SELECT id FROM dat_feeitem WHERE code = 'OCF' AND isdelete = FALSE LIMIT 1) AND a.jobid = "+Long.parseLong(ids[0])+"))" +
		" OR (SELECT COALESCE((SELECT isconfirm FROM fina_arap WHERE isdelete = FALSE AND jobid = "+Long.parseLong(ids[0])+" AND araptype = 'AP' GROUP BY isconfirm),FALSE))) AS exists";
		try {
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map.containsKey("exists") == false || "false".equals(String.valueOf(map.get("exists")))){
				MessageUtils.alert("工作单海运费尚未确认！");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String winId = "_edit_deductionapply";
		String url =  "/scp/bpm/apply/bpmdeductionapply.xhtml?jobid=" + StrUtils.array2List(ids)+"&type=gsit";
		AppUtils.openWindow(true,winId, url,false);
	}
	
	/**
	 * 费用结算地，只提取工作单里面的接单公司或操作公司，2选1
	 * @return
	 */
	@Bind(id="corpAbbr")
    public List<SelectItem> getCorpAbbr() {
    	try {
    		String qry = SaasUtil.filterById("d");
    		return CommonComBoxBean.getComboxItems("id","COALESCE(abbcode,'')","sys_corporation ","WHERE iscustomer = false and isdelete = false","ORDER BY id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;
	
	
	

	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}

	@Action
	public void importDataBatch() {
		String[] ids = this.grid.getSelectedIds();
		
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				// importDataText = importDataText.replaceAll("'", "''");
				String callFunction = "f_imp_arap";
				String args = "'"+StrUtils.array2List(ids) + "'" + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				this.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Bind(id = "hblrpt")
	public List<SelectItem> getHblrpt() {
		try {
			if("demo".equalsIgnoreCase(AppUtils.getUserSession().getUsercode())){
				return CommonComBoxBean.getComboxItems("d.filename", "d.code ",
						"sys_report AS d", "WHERE modcode='shippinghbl' AND isdelete = FALSE ",
				"order by code");
			}else{
				String sql = "WHERE modcode='shippinghbl' AND isdelete = FALSE AND (userid IS NULL OR ispublic OR userid = "+AppUtils.getUserSession().getUserid()+")";
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
	
	@SaveState
	@Bind
	public String billid = "";
	
	@Bind
	@SaveState
	public String control;
	
	@Bind
	@SaveState
	public boolean isadmin;
	
	
	@Bind
	@SaveState
	public String baseurl;
	
	@Bind
	@SaveState
	public String rptUrl;
	
	@Bind
	@SaveState
	public String useridString;
	
	@SaveState
	public String language = AppUtils.getUserSession().getMlType().toString();;
	
	@Action
	public void viewHBL(){	
		billid = "";
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}
		
		String querySql = "SELECT STRING_AGG(id::TEXT,',') AS jobid from bus_shipping WHERE jobid IN ("+StrUtils.array2List(ids)+")";
		//System.out.println("sql:"+querySql);
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		billid = StrUtils.getMapVal(map, "jobid");
		useridString = AppUtils.getUserSession().getUserid().toString();
		rptUrl = AppUtils.getRptUrl();
		update.markUpdate(UpdateLevel.Data,"billid");
		update.markUpdate(UpdateLevel.Data,"useridString");
		Browser.execClientScript("view();");
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
	
	/**
	 * 订舱代理下拉框
	 * @return
	 */
	@Bind(id="agents")
	public List<SelectItem> getAgents() {
    	try {
			return CommonComBoxBean.getComboxItems("b.agentid ","COALESCE(STRING_AGG(DISTINCT c.namec,''), '') ","STRING_AGG(DISTINCT c.namec,'')","sys_corporation c,bus_shipping b","WHERE b.agentid = c.id AND b.isdelete = FALSE AND c.isdelete = FALSE AND b.agentid > 0 GROUP BY b.agentid ","ORDER BY STRING_AGG(DISTINCT c.namec,'')");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind
	@SaveState
	public String filterStatus;

	@Action
	public void filterByStatus() {
		this.filterStatus = AppUtils.getReqParam("type");
		this.refresh();
	}
}
