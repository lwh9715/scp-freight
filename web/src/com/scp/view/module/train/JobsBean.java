package com.scp.view.module.train;

import java.math.BigDecimal;
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
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
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
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.train.jobsBean", scope = ManagedBeanScope.REQUEST)
public class JobsBean extends GridView {
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
	private String busstatus;
	
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
	private String ischeck;

	@Bind
	@SaveState
	private String useridstr;

	@Action
	public void searchfee() {
		this.qryRefresh();
		this.deptids = getqueryDepartid();
		//this.grid.reload();
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		this.useridstr = String.valueOf(AppUtils.getUserSession().getUserid());
		if (!isPostBack) {
			super.applyGridUserDef();
			initCtrl();
			initData();
			profit_compare = ">";
			profit_currency = "CNY";
			profit = BigDecimal.valueOf(0L);
			//this.qryMap.put("nos", "#");
			gridLazyLoad = true;
//			boolean isUseDzz = applicationConf.getIsUseDzz();
//			if(!isUseDzz){
//				isuserdzz = "true";
//			}
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
		AppUtils.openWindow(winId, url);
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
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT count(1) FROM (SELECT COALESCE(SUM(f_amtto(a.arapdate, a.currency, 'USD', a.amount)),0) > 100 AS flag from fina_jobs j LEFT JOIN fina_arap a ON (a.isdelete = false AND a.jobid = j.id) WHERE	j.isdelete =FALSE AND j.id in ("+StrUtils.array2List(ids)+")	GROUP BY j.id) t WHERE t.flag = FALSE");
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
		AppUtils.openWindow(winId, url);
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

		m.put("qry", qry);

		if (StrUtils.isNull(carryitem)) { // 运费条款的查询（连接bus_shipping）
			m.put("carryitem", "1=1");
		} else {
			String sql = "EXISTS(SELECT 1 FROM bus_train WHERE jobid = t.id AND isdelete = FALSE AND carryitem = '"
					+ carryitem + "')";
			m.put("carryitem", sql);
		}
		//初始化
		dynamicClauseWhere = " 1=1 ";
		//高级检索中日期区间查询拼接语句
		if(isdate){	
				dynamicClauseWhere  += "\nAND "+dates+"::DATE BETWEEN '"
					+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
					+ "' AND '"
					+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
					+ "'";
			
		}
		//高级检索中单号查询拼接
		if(isjob||isrefno||isbook||ishbl||ismbl||issendc||iscustoms||isreceipt||isinvoice||isnumber||isno||istitle||iscommission||isbill||islink){
			dynamicClauseWhere += "\nAND (FALSE ";
			if(isjob){
				dynamicClauseWhere +="\nOR nos ILIKE '%"+numbers+"%'";			
			}
			if(isrefno){
				dynamicClauseWhere +="\nOR refno ILIKE '%"+numbers+"%'";	
			}
			if(isbook){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM bus_train bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.sono ILIKE '%"+numbers+"%')";
			}
			if(ishbl){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM bus_ship_bill bs WHERE  isdelete = FALSE AND bltype='H' AND bs.jobid=t.id AND bs.hblno ILIKE '%"+numbers+"%')";
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.hblno ILIKE '%"+numbers+"%')";
			}
			if(ismbl){
				dynamicClauseWhere +="\nOR (EXISTS (SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.mblno ILIKE '%"+numbers+"%') OR EXISTS (SELECT 1 FROM bus_ship_bill bs WHERE  isdelete = FALSE AND bltype='M' AND bs.jobid=t.id AND bs.mblno ILIKE '%"+numbers+"%'))";	
			}
			if(issendc){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM bus_truck bt WHERE isdelete = FALSE AND bt.jobid=t.id AND bt.nos ILIKE '%"+numbers+"%')";	
			}
			if(iscustoms){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM bus_customs bc WHERE isdelete = FALSE AND bc.jobid=t.id AND bc.nos ILIKE '%"+numbers+"%')";	
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
			dynamicClauseWhere += ")";
		}
		//船名航次拼接
		if(vessel!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_train bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.vessel ILIKE '%"+vessel+"%')";
		}
		if(voyage!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_train bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.voyage ILIKE '%"+voyage+"%')";
		}
		//目的地
		if(destination!=""){
			dynamicClauseWhere+="\nAND EXISTS " +
					"(SELECT 1 FROM bus_train bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.destination ILIKE '%"+destination+"%')";
		}
		//业务状态
		if(!StrUtils.isNull(busstatus)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_train x WHERE x.jobid = t.id AND x.busstatus ='"+busstatus+"')";
		}else{
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_train x WHERE x.jobid = t.id AND x.busstatus != 'R')";
		}
		//航线
		if(routecode!=""){
			dynamicClauseWhere+="\nAND EXISTS " +
					"(SELECT 1 FROM bus_train bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.routecode ILIKE '%"+routecode+"%')";
		}
		//装箱
		if(ldtype!=""){
			dynamicClauseWhere+="\nAND t.ldtype ILIKE '%"+ldtype+"%'";
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
		//港口
		if(port!=""){
			dynamicClauseWhere +="\nAND "+portss+" LIKE '%"+port+"%'";
		}
		
		//MBL提单号
		if(mblqry!=""){
			dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.mblno ILIKE '%"+mblqry+"%') OR EXISTS (SELECT 1 FROM bus_ship_bill bs WHERE isdelete = FALSE AND bs.jobid=t.id AND bs.mblno ILIKE '%"+mblqry+"%'))";
		}
		
		
		//MBL取单
		if(!StrUtils.isNull(usergetmbl)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.usergetmbl='"+usergetmbl+"')";
		}
		//MBL放单
		if(!StrUtils.isNull(userreleasembl)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.userreleasembl ='"+userreleasembl+"')";
		}
		//客户
		if(customerid!=0&&allcustomer.equals("customerid")){
			dynamicClauseWhere +="\nAND "+allcustomer+" ='"+customerid+"'";
		}
		if(customerid!=0&&allcustomer.equals("agentdesid")||(allcustomer.equals("carrierid"))||(allcustomer.equals("agentid"))){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_train bs WHERE isdelete = FALSE AND bs.jobid=t.id AND bs."+allcustomer+" ='"+customerid+"')";
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
		//发货人
		if(!StrUtils.isNull(cnortitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.cnortitle ILIKE '%"+StrUtils.getSqlFormat(cnortitle)+"%')";
		}
		//收货人
		if(!StrUtils.isNull(cneetitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.cneetitle ILIKE '%"+StrUtils.getSqlFormat(cneetitle)+"%')";
		}
		//通知人
		if(!StrUtils.isNull(notifytitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_train x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.notifytitle ILIKE '%"+StrUtils.getSqlFormat(notifytitle)+"%')";
		}
		//搜索指派中的文件
		if(!StrUtils.isNull(filsid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_train y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'D' AND x.userid ='"+filsid+"')";
		}
		//搜索指派中的操作
		if(!StrUtils.isNull(operationid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_train y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'O' AND x.userid ='"+operationid+"')";
		}
		//搜索指派中的客服
		if(!StrUtils.isNull(callid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_train y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'C' AND x.userid ='"+callid+"')";
		}
		//搜索指派中的市场
		if(!StrUtils.isNull(marketid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_train y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = 'M' AND x.userid ='"+marketid+"')";
		}
		//询价人
		if(!StrUtils.isNull(priceuserid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.priceuserid ='"+priceuserid+"')";
		}
		//搜索指派中的财务跟单
		//改为动态类型，取数据字典中的类型然后匹配
		if(!StrUtils.isNull(assignid)&&!StrUtils.isNull(roletype)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign x , bus_train y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = '"+roletype+"' AND x.userid ='"+assignid+"')";
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
			dynamicClauseWhere += "\n AND EXISTS(SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.isdelete = false" +
								  "\n AND EXISTS(SELECT 1 FROM dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 110 AND " +
								  "\n s.trucktype = d.code AND d.namec = '我司安排'))";
		}
		if(!StrUtils.isNull(isbuscustoms)){
			dynamicClauseWhere += "\n AND "+(isbuscustoms.equals("Y")?"EXISTS":"NOT EXISTS")+"(SELECT 1 FROM bus_customs WHERE jobid = t.id AND isdelete = false)";
			dynamicClauseWhere += "\n AND EXISTS(SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.isdelete = false" +
								  "\n AND EXISTS(SELECT 1 FROM dat_filedata d WHERE d.isleaf = TRUE AND d.isdelete = false AND d.fkcode = 40 AND " +
								  "\n s.custype = d.code AND d.namec = '我司安排'))";
		}
		//VGM
		if(!StrUtils.isNull(isvgm)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_train s WHERE s.jobid = t.id " +
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
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_train s WHERE s.jobid = t.id AND s.istelrel = "
								+(istelrelv.equals("Y")?"TRUE":"FALSE")+")";
		}
		//跟踪
		if(!StrUtils.isNull(statetracenext)){
			dynamicClauseWhere +="\nAND t.statetrace ILIKE '%"+statetracenext+"%' ";	
		}

		//完成状态
		if(!StrUtils.isNull(ischeck)){
			dynamicClauseWhere += "\n AND t.ischeck=" + ischeck;
		}

		m.put("dynamicClauseWhere", dynamicClauseWhere);
		m.put("icare", "1=1");
			

		//2412 工作单编辑界面上一单下一单按列表过滤条件（将列表查询的条件先保存到个人设置里面，编辑界面上一单下一单的时候，再把列表的过滤条件查出来拼进去）
		try {
			ConfigUtils.refreshUserCfg("pages.module.ship.jobsBean.search", 
					StrUtils.getSqlFormat(qry+" AND "+dynamicClauseWhere)
					, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
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
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
				+ ")"
				
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_train y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT t.corpid UNION SELECT t.corpidop UNION SELECT t.corpidop2 UNION SELECT corpid FROM fina_corp WHERE isdelete = FALSE AND jobid = t.id) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+" OR COALESCE(t.saleid,0) <= 0)";
		m.put("corpfilter", corpfilter);
		if("0".equals(this.setDays)){
			String setDays = "\n AND 1=1";
			m.put("setDays", setDays);
		}else{
			String setDays = "\n AND t.jobdate::DATE >= NOW()::DATE-"+(StrUtils.isNull(this.setDays)?"365":this.setDays);
			m.put("setDays", setDays);
		}
		
		m.put("userid", AppUtils.getUserSession().getUserid());
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
	
	@SaveState
	private Long corpids;
	
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
			String nos = serviceContext.jobsMgrService.addCopyTrain(ids[0], AppUtils.getUserSession().getUserid(), AppUtils.getUserSession().getUsercode()
					,this.copyAndCreateCorpid, this.copyAndCreateCorpidop, dateFormat.format(this.copyAndCreateJobdate),this.includFee,this.serialno
					,this.entrust,this.bookinginfo,this.portinfo,this.packing,this.bookingagent,this.productinfo,this.shippingterms,this.boxnum,this.vesvoy
					,this.refeno,this.ordernum,this.bookingnum,this.hblnum,this.mblnum,this.cutday,this.etacopy,this.etdcopy,this.atdcopy,this.atacopy);
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
//		try {
//			serviceContext.jobsMgrService.editJobs(ids, eta, etd,clstime,sidate,vessel1,voyage1,atd,ata,loadtime,storehousedate,destination1,agentdesid,clearancecusid,docuserid,opruserid,agentid,puserid,
//					mblno,bltype,mbltype,hbltype,routecodev,carrieridv,user);
//			refresh();
//			Browser.execClientScript("showmsg()");
//			showbatchupdateWindow.close();
//		} catch (Exception e) {
//			MessageUtils.showException(e);
//			return;
//		}
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
					"sys_report AS d", " WHERE modcode = 'jobs_train' AND isdelete = FALSE",
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
		String sql = "SELECT id FROM bpm_processinstance WHERE refid = '"+jobid+"' LIMIT 1";
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
	@Accessible
	public String showwmsinfilename;
	@Bind
	public UIEditDataGrid editGrid;
	
	
	@Action
	public void preview() {
		try {
			String[] id =this.grid.getSelectedIds(); 
			 
			
			if(id == null || id.length == 0) {
				MessageUtils.alert("请先勾选要预览的行");
				return;
			}
			if( id.length > 1) {
				MessageUtils.alert("只可以选中一行");
				return;
			}
			
			if (showwmsinfilename == null || "".equals(showwmsinfilename)) {
				MessageUtils.alert("请选择格式！");
				return;
			}
			String rpturl = AppUtils.getRptUrl();
			String openUrl;
			String args =null;
			String args2 =null;
			for(int i=0;i<id.length;i++){
				String querySql="select i.id as id from wms_in i where id=ANY(SELECT DISTINCT z.inid FROM  del_loadtl x , wms_outdtlref y , wms_indtl z WHERE x.loadid = (select p.id from del_load p,_fina_jobs b where p.refno= b.nos and b.id='"+id[i]+"') AND x.outdtlid = y.outdtlid AND y.indtlid = z.id AND x.isdelete = false)";
				List<Map>  list= this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql);
			
				if(list.size()==0){
					String[] ids= new String[1];
					ids[0]="-1";
					args = "&ids=" + StrUtils.array2List(ids);
				}else{
					String[] ids= new String[list.size()];
					for(int i1=0;i1<list.size();i1++){
						
						ids[i1]=list.get(i1).get("id").toString();
						
						
				}
					
					args = "&ids=" + StrUtils.array2List(ids);
				}
				
				
			}
			
			if("RAIL REPORT.raq".equals(showwmsinfilename)){
				openUrl = rpturl+ "/reportJsp/showReport.jsp?raq=/static/stock/"+ showwmsinfilename;
				 args2 = "&ids=" + StrUtils.array2List(id);
				AppUtils.openWindow("_shipbillReport", openUrl + args2
						+ "&userid="
						+ AppUtils.getUserSession().getUserid());
			}else{
				openUrl = rpturl+ "/reportJsp/showReport.jsp?raq=/static/train/"+ showwmsinfilename;
				AppUtils.openWindow("_shipbillReport", openUrl + args
						+ "&userid="
						+ AppUtils.getUserSession().getUserid());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
