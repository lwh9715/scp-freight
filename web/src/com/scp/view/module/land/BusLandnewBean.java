package com.scp.view.module.land;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.ship.BusTruck;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysFormdef;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.land.buslandnewBean", scope = ManagedBeanScope.REQUEST)
public class BusLandnewBean extends GridFormView {
	
	@SaveState
	@Accessible
	public FinaJobs selectedRowData = new FinaJobs();
	
	@SaveState
	@Accessible
	public BusTruck bustruckDate = new BusTruck();
	
	@SaveState
	public SysFormdef sysFormdef = new SysFormdef();
	
	@Bind(id="deptids")
	@SelectItems
	@SaveState
	public List<SelectItem> deptids = new ArrayList<SelectItem>();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			super.applyGridUserDef();
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
			String findUserCfgVal = ConfigUtils.findUserCfgVal("jobs_land_date", AppUtils.getUserSession().getUserid());
			if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
				this.setDays="365";
			}else{
				this.setDays = findUserCfgVal;
			}
			impexp = "E";
			addMaster();
		}
	}

	@Override
	public void add() {
		selectedRowData = new FinaJobs();
		bustruckDate = new BusTruck();
		super.add();
	}
	
	@Bind
	@SaveState
	@Accessible
	public Long salescorpid = null;

	private List<SelectItem> getqueryDepartid(){
		try {
			List<SelectItem> list = null;
			if(salescorpid == null){
				Long id = this.selectedRowData != null && this.selectedRowData.getCorpid() != null ? this.selectedRowData.getCorpid() : -1L;
				 list = CommonComBoxBean.getComboxItems("d.id" ,"COALESCE(d.namee,d.name)","d.name","sys_department as d"
						 , "WHERE d.isdelete = FALSE AND d.corpid ="+id, "ORDER BY d.name");
			}else{
				 list = CommonComBoxBean.getComboxItems("d.id" ,"COALESCE(d.namee,d.name)","d.name","sys_department as d"
						 , "WHERE d.isdelete = FALSE AND d.corpid ="+salescorpid, "ORDER BY d.name");
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Action
	private void changeCorpid(){
		String corptmp = AppUtils.getReqParam("corpid");
		if(corptmp != null && !corptmp.isEmpty()){
			if(!this.selectedRowData.getCorpid().equals(corptmp)){
				this.selectedRowData.setDeptid(null);
				this.selectedRowData.setCorpid(Long.parseLong(corptmp));
				this.deptids = getqueryDepartid();
				update.markUpdate(true,UpdateLevel.Data, "deptcomb");
				Browser.execClientScript("clearDept();");
			}
		}
	}
	
	@Action
	public void salesChangeAjaxSubmit() {
		String salesid = AppUtils.getReqParam("salesid");
		String sql  = "SELECT deptid,corpid FROM sys_user WHERE id = " + salesid;
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String deptid = StrUtils.getMapVal(m, "deptid");
			String cid = StrUtils.getMapVal(m, "corpid");
			if(!StrUtils.isNull(deptid)){
				this.selectedRowData.setDeptid(StrUtils.isNull(deptid)?0l:Long.valueOf(deptid));
			}
			if(!StrUtils.isNull(cid)){
				this.salescorpid = Long.valueOf(cid);
			}
			update.markUpdate(true, UpdateLevel.Data, "deptcomb");
			this.deptids = getqueryDepartid();
		} catch (NoRowException e) {
			this.selectedRowData.setDeptid(0l);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Action
	public void changeSalesAjaxSubmit() {
		String customerid = AppUtils.getReqParam("customerid");
		Long salesid;
		Long deptid;
		String salesname;
		Long usercorpid;
		String querySql  = "SELECT u.id AS salesid , u.namec AS salesname , c.tradeway,deptid,u.corpid AS usercorpid FROM sys_corporation c , sys_user u where c.id = "
			+customerid+" AND u.id = c.salesid and u.isdelete = false limit 1";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			String salesidStr = StrUtils.getMapVal(m, "salesid");
			salesid = StrUtils.isNull(salesidStr)?0l:Long.valueOf(salesidStr);
			deptid = StrUtils.isNull(StrUtils.getMapVal(m, "deptid"))?0l:Long.valueOf(StrUtils.getMapVal(m, "deptid"));
			salesname = StrUtils.getMapVal(m, "salesname");
			usercorpid = StrUtils.isNull(StrUtils.getMapVal(m, "usercorpid"))?0l:Long.valueOf(StrUtils.getMapVal(m, "usercorpid"));
			this.selectedRowData.setSaleid(salesid);
			this.selectedRowData.setSales(salesname);
			this.selectedRowData.setDeptid(deptid);
			this.selectedRowData.setCorpid(usercorpid);
			this.selectedRowData.setCorpidop(usercorpid);
			String corpid = StrUtils.getMapVal(m, "usercorpid");
			this.salescorpid =  StrUtils.isNull(corpid)?0l:Long.valueOf(corpid);
			this.deptids = getqueryDepartid();
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "saleid");
			update.markUpdate(true, UpdateLevel.Data, "deptcomb");
			update.markUpdate(true, UpdateLevel.Data, "corpid");
			update.markUpdate(true, UpdateLevel.Data, "corpidop");
			Browser.execClientScript("initSalesByCustomer('"+salesname+"');");
		} catch (NoRowException e) {
			this.selectedRowData.setSaleid(0l);
			this.selectedRowData.setSales("");
			update.markUpdate(true, UpdateLevel.Data, "sales");
			update.markUpdate(true, UpdateLevel.Data, "salesid");
			Browser.execClientScript("initSalesByCustomer('');");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	public UIButton saveMaster;
	@Bind
	public UIButton delMaster;
	@Bind
	public UIButton addMaster;
	
	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		delMaster.setDisabled(isCheck);
		addMaster.setDisabled(isCheck);
	}
	
	@Bind
	public Long department;
	
	@Action
	public void addMaster() {
		disableAllButton(false);
		this.selectedRowData = new FinaJobs();
		this.pkVal = -1l;
		selectedRowData.setJobdate(new Date());
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		selectedRowData.setIsland(true);
		selectedRowData.setJobtype("L");
		selectedRowData.setIsair(false);
		selectedRowData.setIsshipping(false);
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		selectedRowData.setCorpidop(AppUtils.getUserSession().getCorpid());
		selectedRowData.setCustomerabbr("");
		selectedRowData.setSaleid(null);
		selectedRowData.setDeptid(null);
		selectedRowData.setIslock(false);
		selectedRowData.setSales("");
		addBustruck();
		this.department = null;
		this.deptids = getqueryDepartid();
		this.update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		Browser.execClientScript("deptcombJsVar.setValue();");
		refreshForm();
	}
	
	public void addBustruck(){
		bustruckDate = new BusTruck();
		bustruckDate.setJobid(this.mPkVal);
		bustruckDate.setNos(this.selectedRowData.getNos());
		bustruckDate.setAreatype("");
		bustruckDate.setTruckstate("I");
		bustruckDate.setSingtime(this.selectedRowData.getJobdate());
	}

	@Override
	protected void doServiceSave() {
		serviceContext.jobsMgrService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			serviceContext.jobsMgrService.removeDate(Long.parseLong(ids[0]),AppUtils.getUserSession().getUsercode());
			refresh();
		}
	}
	
	@Bind
	@SaveState
	public Boolean iscare = false;
	@Bind
	@SaveState
	private String impexp;
	
	@SaveState
	String dynamicClauseWhere  ="";
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if (!iscare) {
			m.put("icare", "1=1");
		} else {
			String sql="(EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="+AppUtils.getUserSession().getUserid()+")";
			sql+="OR t.inputer ='"+AppUtils.getUserSession().getUsercode()+"' OR t.updater = '"+AppUtils.getUserSession().getUsercode()+"')";
			m.put("icare",sql);
			
		}
		if("0".equals(this.setDays)){
			String setDays = "\n AND 1=1";
			m.put("setDays", setDays);
		}else{
			String setDays = "\n AND t.jobdate::DATE >= NOW()::DATE-"+(StrUtils.isNull(this.setDays)?"365":this.setDays);
			m.put("setDays", setDays);
		}
		
		
		//初始化
		dynamicClauseWhere = "\n AND 1=1 AND impexp = '"+impexp+"'";
		//高级检索中日期区间查询拼接语句
		if(isdate){	
			dynamicClauseWhere  += "\nAND "+dates+"::DATE BETWEEN '"
				+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
				+ "' AND '"
				+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
				+ "'";
		}
		
		//高级检索中单号查询拼接
		if(isjob||isrefno||isreceipt||isinvoice||isnumber||iscntno){
			dynamicClauseWhere += "\nAND (FALSE ";
			if(isjob){
				dynamicClauseWhere +="\nOR nos ILIKE '%"+numbers+"%'";			
			}
			if(isrefno){
				dynamicClauseWhere +="\nOR refno ILIKE '%"+numbers+"%'";	
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
			}if(iscntno){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM bus_ship_container fb WHERE isdelete = FALSE AND fb.jobid=t.id AND fb.cntno ILIKE '%"+numbers+"%')";	
			}
			dynamicClauseWhere += ")";
		}
		
		//客户
		if(customerids!=0&&allcustomer.equals("customerid")){
			dynamicClauseWhere +="\nAND "+allcustomer+" ='"+customerids+"'";
		}
		if(customerids!=0&&allcustomer.equals("clientid")){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM fina_arap fa WHERE  isdelete = FALSE AND fa.jobid=t.id AND fa.customerid="+customerids+")";
		}
		
		//业务员
		if(sale!=0){
			dynamicClauseWhere +="\nAND t.saleid = '"+sale+"'";
		}
		//录入人
		if(saleid!=""){
			dynamicClauseWhere +="\nAND t.inputer = '"+saleid+"'";
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
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.id=t.corpidop AND sc.id = "+corpidop+")";		
		}
		//搜索指派中的文件
		if(!StrUtils.isNull(filsid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign WHERE linkid = t.shipid AND linktype = 'J' AND roletype = 'D' AND userid ='"+filsid+"')";
		}
		//搜索清关行
		if(!StrUtils.isNull(customid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.namec=t.customid AND sc.id = "+customid+")";
		}
		//搜索指派中的操作
		if(!StrUtils.isNull(operationid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign WHERE linkid = t.shipid AND linktype = 'J' AND roletype = 'O' AND userid ='"+operationid+"')";
		}
		//搜索指派中的客服
		if(!StrUtils.isNull(callid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign WHERE linkid = t.shipid AND linktype = 'J' AND roletype = 'C' AND userid ='"+callid+"')";
		}
		m.put("dynamicClauseWhere", dynamicClauseWhere);
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + 
							//能看所有外办订到本公司的单权限的人能看到对应单
							"AND t.corpid <> t.corpidop AND t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid "
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				//+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
				+ "\n					AND x.userid = t.saleid "
				+ ")"
				
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="
			+AppUtils.getUserSession().getUserid()+") " +
		"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+") ";
		m.put("corpfilter", corpfilter);
		return m;
	}
	
	@Bind
	public String customerNamec;
	
	@Override
	protected void doServiceFindData() {
		batchedit();
		selectedRowData = serviceContext.jobsMgrService.finaJobsDao.findById(this.pkVal);
		if(selectedRowData.getCustomerid() != null && selectedRowData.getCustomerid() > 0){
			SysCorporation sysCorporation  = serviceContext.customerMgrService.sysCorporationDao.findById(selectedRowData.getCustomerid());
			this.customerNamec = StrUtils.isNull(sysCorporation.getNamec())?sysCorporation.getNamee():sysCorporation.getNamec();
		}
		this.deptids = getqueryDepartid();
		try {
			bustruckDate = this.serviceContext.busTruckMgrService.busTruckDao.findOneRowByClauseWhere("jobid = "+ this.selectedRowData.getId()+ " AND isdelete = false");
		} catch (Exception e) {
//			e.printStackTrace();
			addBustruck();
		}
	}
	
	@Override
	public void refreshForm() {
		update.markUpdate(true, UpdateLevel.Data, "edtiPanel");
		update.markUpdate(true, UpdateLevel.Data, "congrid");
		this.congrid.reload();
		Browser.execClientScript("$('#customer_input').val('"+selectedRowData.getCustomerabbr()+"');");
		Browser.execClientScript("$('#sales_input').val('"+selectedRowData.getSales()+"');");
		if(bustruckDate!=null){
			Browser.execClientScript("$('#pol_input').val('"+(StrUtils.isNull(bustruckDate.getLoadplace())?"":bustruckDate.getLoadplace())+"');");
			Browser.execClientScript("$('#pod_input').val('"+(StrUtils.isNull(bustruckDate.getDestination())?"":bustruckDate.getDestination())+"');");
		}
	}
	
	@SaveState
	private Long corpids;
	@Bind
	public String copyAndCreateCorpid;
	@Bind
	public Date copyAndCreateJobdate;
	@Bind
	public String copyAndCreateCorpidop;
	@Bind
	public UIWindow copyAndCreatingWindow;
	
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
	
	@Bind
	public String includFee;
	@Bind
	public String serialno;
	
	@Action
	public void copyAndCreating(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String nos = serviceContext.jobsMgrService.addcopyland(ids[0], AppUtils.getUserSession().getUserid(), AppUtils.getUserSession()
					.getUsercode(), this.copyAndCreateCorpid, this.copyAndCreateCorpidop, dateFormat.format(this.copyAndCreateJobdate),this.includFee,this.serialno);
			MessageUtils.alert("复制生成的单号是:" + nos);
			this.qryMap.clear();
			this.qryMap.put("nos", nos);
			refresh();
			this.copyAndCreatingWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void searchfee() {
		this.qryRefresh();
		this.deptids = getqueryDepartid();
	}
	
	@Action
	public void clear() {
		this.clearQryKey();
	}
	
	@Bind
	@SaveState
	private boolean isdate=false;
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	private String numbers;
	
	@Bind
	@SaveState
	private String allcustomer="";
	
	@Bind
	@SaveState
	private long customerids=0;
	
	@Bind
	@SaveState
	private long sale=0 ;
	
	@Bind
	@SaveState
	private String  saleid="" ;
	
	@Bind
	@SaveState
	private String corpid = "" ;
	
	@Bind
	@SaveState
	private String corpidop = "" ;
	
	@Bind
	@SaveState
	private String deptid = "" ;
	
	@Bind
	@SaveState
	private String filsid;
	
	@Bind
	@SaveState
	private String operationid;
	
	@Bind
	@SaveState
	private String customid;
	
	@Bind
	@SaveState
	private String callid;
	@Bind
	@SaveState
	private String dates;
	@Bind
	@SaveState
	private boolean isjob;
	
	@Bind
	@SaveState
	private boolean isrefno;
	
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
	private boolean iscntno;
	
	@Override
	public void clearQryKey() {
		isdate=false;
		dates="";
		dateastart="";
		dateend="";
		isjob=false;
		isrefno=false;
		isreceipt=false;
		isinvoice=false;
		isnumber=false;
		iscntno=false;
		numbers="";
		allcustomer="";
		customerids=0;
		sale=0;
		saleid="";
		corpid="";
		corpidop="";
		deptid="";
		filsid = "";
		operationid = "";
		customid="";
		callid = "";
		Browser.execClientScript("$('#sales_input').val('')");
		Browser.execClientScript("$('#inputers_input').val('')");
		Browser.execClientScript("$('#fils_input').val('')");
		Browser.execClientScript("$('#operation_input').val('')");
		Browser.execClientScript("$('#customcompany_input').val('')");
		Browser.execClientScript("$('#call_input').val('')");
		Browser.execClientScript("$('#customer_input').val('')");
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
	}
	
	@Bind
	public UIWindow searchWindow;
	
	@Action
	public void clearAndClose() {
		this.clearQryKey();
		this.searchWindow.close();
	}
	
	@Action
	public void delMaster() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			serviceContext.jobsMgrService.removeDate(this.getGridSelectId(),
					AppUtils.getUserSession().getUsercode());
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind(id = "serial")
	public  List<SelectItem> getSerial() {
		Long csid =  this.corpids;
		try {
			List<SelectItem> list = CommonComBoxBean.getComboxItems("r.prefix", "r.prefix",
					"sys_busnodesc t ,sys_busnorule r ", "WHERE r.corpid="
					+csid+" AND t.id = r.busnotypeid AND t.code like 'fina_jobs_lan%' AND t.isdelete = FALSE AND r.isdelete = FALSE",
			"group by prefix order by prefix");
			return list;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind
	@SaveState
	public String setDays;
	
	@Action
	public void confirmSave(){
		try {
			ConfigUtils.refreshUserCfg("jobs_land_date",this.setDays, AppUtils.getUserSession().getUserid());
			alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind
	@SaveState
	public Long mPkVal = -1L;
	
	@Action
	public void saveMaster(){
		try {
			serviceContext.jobsMgrService.saveData(selectedRowData);
			serviceContext.busTruckMgrService.saveData(bustruckDate);
			this.pkVal = selectedRowData.getId();
			mPkVal = bustruckDate.getId();
			this.alert("ok");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		refreshForm();
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	@Bind
	public UIWindow setnosWindow;
	
	@Action
	public void setJobNos(){
		this.grid.reload();
		setnosWindow.show();
	}
	
	@Bind
	public UIDataGrid setnosgrid;
	
	@Bind(id="setnosgrid")
	public List<Map> getRateGrids() throws Exception{
		List<Map> list = null;
		Map map = new HashMap();
		String clauseWhere = "\nAND 1=1";
		map.put("qry", clauseWhere);
		list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.land.buslandnewBean.setnos.grid.page", map);
		return list;
	}
	
	@Bind
	public UIDataGrid congrid;
	
	@Bind(id="congrid")
	public List<Map> getConGrids() throws Exception{
		List<Map> list = null;
		Map map = new HashMap();
		String clauseWhere = "\n 1=1";
		clauseWhere += "\nAND (NOT EXISTS (SELECT 1 FROM bus_truck_link x WHERE x.containerid = t.id)"
			+ "\nOR EXISTS(SELECT 1 FROM bus_truck_link k WHERE k.truckid ="
			+ this.mPkVal
			+ " AND k.containerid = t.id))"
			+ "\nAND t.jobid = " + this.pkVal;
		map.put("qry", clauseWhere);
		list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.land.buslandnewBean.container.grid.page", map);
		return list;
	}
	
	@SaveState
	public String codes;
	
	@Action
	public void choosenos(){
		String[] ids = this.setnosgrid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String querySql  = "SELECT code  FROM sys_busnodesc WHERE id = "+ids[0];
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			codes = StrUtils.getMapVal(m, "code");
		} catch (NoRowException e) {
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
		getJobNos();
		setnosWindow.close();
	}
	
	@Action
	public void getJobNos() {
		if(this.selectedRowData!=null&&this.selectedRowData.getNos()!=null&&!this.selectedRowData.getNos().isEmpty()){
			MessageUtils.alert("工作号已生成!");
			return;
		}
		String querySql ;
		if(this.mPkVal > 0){
			querySql  = "SELECT f_find_jobs_getnos('id="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";code="+codes+"') AS nos;";
		}else{
			querySql  = "SELECT f_find_jobs_getnos('id="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()
			+";code="+codes+";corpid="+selectedRowData.getCorpid()+";jobdate="+selectedRowData.getJobdate()+"') AS nos;";
		}
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			String nos = StrUtils.getMapVal(m, "nos");
			this.selectedRowData.setNos(nos);
			MessageUtils.alert(nos);
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		} catch (NoRowException e) {
			MessageUtils.showException(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	public void setnosgrid_ondblclick() {
		choosenos();
	}
	
	@Action
	public void addarapMaster(){
		sysFormdef = new SysFormdef();
		sysFormdef.setId(0l);
		Browser.execClientScript("editarapWindow.show();");
		update.markUpdate(true, UpdateLevel.Data, "editarapPanel");
	}
	
	@Action
	public void saveArapModual(){
		if(this.pkVal==null||pkVal<0){
			MessageUtils.alert("请先保存");
			return;
		}
		sysFormdef.setBeaname(this.selectedRowData.getId()+"");
		sysFormdef.setInputype("UINumberField");
		sysFormdef.setIshidden(false);
		serviceContext.sysFormdefService.saveData(sysFormdef);
		Browser.execClientScript("editarapWindow.close();");
	}
	
	@Bind
	public UIIFrame dtlIFrameArap;
	
	public void batchedit() {
		try {
			dtlIFrameArap.setSrc("../finance/iframarapeditbrief.xhtml?jobid="+ this.pkVal);
			update.markAttributeUpdate(dtlIFrameArap, "src");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();
	
	@Bind
	public UIWindow coneditWindow;
	
	@Action
	public void addcon() {
		if(!(this.pkVal>0)){
			MessageUtils.alert("请先双击行选择一个工作单");
			return;
		}
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.pkVal);
		if(coneditWindow != null)coneditWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "coneditPanel");
	}
	//新增
	@Action
	protected void containeradd(){
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.pkVal);
		update.markUpdate(true, UpdateLevel.Data, "coneditPanel");
	}
	//保存
	@Action
	protected void save1() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			this.congrid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	//保存并新增
	@Action
	protected void saveForm() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			this.congrid.reload();
			dtlData = new BusShipContainer();
			dtlData.setJobid(this.pkVal);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	/**
	 * 保存关闭
	 */
	@Action
	protected void save2() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			this.congrid.reload();
			this.coneditWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void delcon() {
		String[] ids = this.congrid.getSelectedIds();
		serviceContext.busShipContainerMgrService.removeDate(ids);
		this.congrid.reload();
	}
	
	@Action
	public void refreshcon(){
		this.congrid.reload();
	}
	
	@Action
	public void congrid_ondblclick() {
		String[] ids = this.congrid.getSelectedIds();
		this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao
		.findById(Long.parseLong(ids[0]));
		if(coneditWindow != null)coneditWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "coneditPanel");
	}
	
}
