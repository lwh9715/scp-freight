package com.scp.view.module.air;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.model.SelectItem;

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
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.air.jobsBean", scope = ManagedBeanScope.REQUEST)
public class Jobs extends GridView {
	
	@Autowired
	public ApplicationConf applicationConf;
	
	@Bind
	@SaveState
	public Boolean iscare = false;
	
	public Long userid;


	@Bind
	@SaveState
	private String linkid;

	@Bind
	@SaveState
	private String linkid2;


	@Bind
	@SaveState
	public String sortlabel_1;
	
	@Bind
	@SaveState
	private String busstatus;
	
	@Bind
	@SaveState
	private String iscompleteqry;
	
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
	public UIWindow searchWindow;
	
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
	private long customerid=0;
	
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
	private String agentdesid;
	
	@Bind
	@SaveState
	private String callid;
	
	@SaveState
	String dynamicClauseWhere  ="";
	
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
	private boolean iscommission;
	
	@Bind
	@SaveState
	private boolean ismawbno;
	
	@Bind
	@SaveState
	private boolean isflightno;
	
	@Bind
	@SaveState
	public String iscl;
	
	@Bind
	public String isuserdzz;
	
	@Bind
	public String serialno;
	
	@Bind
	public String includFee;
	
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
	private String polid = "" ;
	
	@Bind
	@SaveState
	private String podid = "" ;

	@Bind
	@SaveState
	private String isSendReceive;

	@Bind
	@SaveState
	private String ischeck;

	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		isSendReceive = ConfigUtils.findSysCfgVal("sys_forbidden_find_and_view_receipt");

		if (isSendReceive.equals("Y")) {
			Browser.execClientScript("cnortitleJsVar.hide();");
			Browser.execClientScript("cneetitleJsVar.hide();");
			Browser.execClientScript("notifytitleJsVar.hide();");
		}

		if (!isPostBack) {
			initData();
			initCtrl();
			super.applyGridUserDef();
//			boolean isUseDzz = applicationConf.getIsUseDzz();
//			if(!isUseDzz){
//				isuserdzz = "true";
//			}
		}
		linkid = "";
	}
	
	@Bind
	public UIButton add;
	
	@Bind
	public UIButton addcopy;
	
	@Bind
	public UIButton delMaster;
	
	private void initCtrl() {
		add.setDisabled(true);
		addcopy.setDisabled(true);
		delMaster.setDisabled(true);
		showDynamic.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_air.getValue())) {
			if (s.endsWith("_add")) {
				add.setDisabled(false);
				addcopy.setDisabled(false);
			} else if (s.endsWith("_update")) {
			} else if (s.endsWith("_delete")) {
				delMaster.setDisabled(false);
			}
		}
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_arap.getValue())) {
			if (s.endsWith("_report")) {
				showDynamic.setDisabled(false);
			}
		}
	}
	
	
	@Action
	public void add() {
		String winId = "_edit_jobsair";
		String url = "./jobsedit.xhtml";
		AppUtils.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_jobsair";
		String url = "./jobsedit.xhtml?id="+this.getGridSelectId();
		AppUtils.openWindow(winId, url);
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
				Browser.execClientScript("showmsg()");
			}
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(m.containsKey("ordersql")){
			m.remove("ordersql");
		}
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql;
			m.put("ordersql", ordersql);
		}
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" 
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
									"AND t.corpid <> t.corpidop AND (t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+" OR t.corpidop2 = "+AppUtils.getUserSession().getCorpidCurrent()+"))"+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) "
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
				+ ")"
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid =" + AppUtils.getUserSession().getUserid() + ")" + "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		
		//String sql="(EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND s.linkid = t.shipid AND s.userid ="+AppUtils.getUserSession().getUserid()+")";
		//sql+="OR t.inputer ='"+AppUtils.getUserSession().getUsercode()+"' OR t.updater = '"+AppUtils.getUserSession().getUsercode()+"')";
		m.put("icare","1=1");
		// 分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			String corpfilter = " AND (EXISTS(SELECT 1 FROm sys_corporation cor where cor.iscustomer = false and cor.abbcode = 'DB' AND (cor.id = t.corpid OR cor.id = t.corpidop))" +
//					"\nOR  (t.corpid = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR t.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+
//					"\nOR t.saleid = "+AppUtils.getUserSession().getUserid() +
//					"))";
//			m.put("corpfilter", corpfilter);
//		}
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = ANY(SELECT t.corpid UNION SELECT t.corpidop UNION SELECT t.corpidop2 UNION SELECT corpid FROM fina_corp WHERE isdelete = FALSE AND jobid = t.id) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		//"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+"  OR COALESCE(t.saleid,0) <= 0) ";
		"\n OR COALESCE(t.saleid,0) <= 0) ";
		m.put("corpfilter", corpfilter);
		if("0".equals(this.setDays)){
			String setDays = "\n AND 1=1";
			m.put("setDays", setDays);
		}else{
			String setDays = "\n AND t.jobdate::DATE >= NOW()::DATE-"+(StrUtils.isNull(this.setDays)?"365":this.setDays);
			m.put("setDays", setDays);
		}
		
		//初始化
		dynamicClauseWhere = "\n AND 1=1 ";
		//高级检索中日期区间查询拼接语句
		if(isdate){	
				dynamicClauseWhere  += "\nAND "+dates+"::DATE BETWEEN '"
					+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
					+ "' AND '"
					+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
					+ "'";
			
		}
		//高级检索中单号查询拼接
		if(isjob||isrefno||iscustoms||isreceipt||isinvoice||isnumber||iscommission||ismawbno||isflightno){
			dynamicClauseWhere += "\nAND (FALSE ";
			if(isjob){
				dynamicClauseWhere +="\nOR nos ILIKE '%"+numbers+"%'";			
			}
			if(isrefno){
				dynamicClauseWhere +="\nOR refno ILIKE '%"+numbers+"%'";	
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
			if(iscommission){
				dynamicClauseWhere +="\nOR " +
						"EXISTS (SELECT 1 FROM bus_order bo WHERE bo.isdelete = FALSE AND bo.id = t.orderid AND bo.orderno ILIKE" +"'%"+numbers+"%')";	
			}
			if(ismawbno){
				dynamicClauseWhere +="\nOR " +
				"EXISTS (SELECT 1 FROM bus_air x WHERE x.jobid = t.id AND x.mawbno ILIKE" +"'%"+numbers+"%')";
			}
			if(isflightno){
				dynamicClauseWhere +="\nOR " +
						"EXISTS (SELECT 1 FROM bus_air x WHERE x.jobid = t.id AND (x.flightno1 ILIKE " +"'%"+numbers+"%'"+" OR x.flightno1 ILIKE " +"'%"+numbers+"%'))";	
			}
			dynamicClauseWhere += ")";
		}
		//客户
		if(customerid!=0&&allcustomer.equals("customerid")){
			dynamicClauseWhere +="\nAND "+allcustomer+" ='"+customerid+"'";
		}
		if(customerid!=0&&allcustomer.equals("carrierid")){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_air x WHERE x.jobid = t.id AND x."+allcustomer+" ='"+customerid+"')";
		}
		if(customerid!=0&&allcustomer.equals("clientid")){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM fina_arap fa WHERE  isdelete = FALSE AND fa.jobid=t.id AND fa.customerid="+customerid+")";
		}
		if(customerid!=0&&allcustomer.equals("agentid")){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_air x WHERE x.jobid = t.id AND x."+allcustomer+" ='"+customerid+"')";
		}
		if(!StrUtils.isNull(busstatus)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_air x WHERE x.jobid = t.id AND x.busstatus ='"+busstatus+"')";
		}else{
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_air x WHERE x.jobid = t.id AND x.busstatus != 'R')";
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
			dynamicClauseWhere +="\nAND "+corpidop+" = ANY(SELECT t.corpidop UNION SELECT c.corpid FROM fina_corp c WHERE c.jobid = t.id)";
		}
		//搜索指派中的文件
		if(!StrUtils.isNull(filsid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign WHERE linkid = t.shipid AND linktype = 'J' AND roletype = 'D' AND userid ='"+filsid+"')";
		}
		//搜索清关行
		if(!StrUtils.isNull(customid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.namec=t.customid AND sc.id = "+customid+")";
		}
		//搜索目的港代理
		if(!StrUtils.isNull(agentdesid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.code=t.agentdesid AND sc.id = "+agentdesid+")";
		}
		//搜索指派中的操作
		if(!StrUtils.isNull(operationid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign WHERE linkid = t.shipid AND linktype = 'J' AND roletype = 'O' AND userid ='"+operationid+"')";
		}
		//搜索指派中的客服
		if(!StrUtils.isNull(callid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_user_assign WHERE linkid = t.shipid AND linktype = 'J' AND roletype = 'C' AND userid ='"+callid+"')";
		}
		if(iscl!=null&&iscl.equals("1")){
			dynamicClauseWhere+= "\n AND t.isclc=FALSE";
		}else if(iscl!=null&&iscl.equals("2")){
			dynamicClauseWhere+= "\n AND t.isclc=TRUE";
		}
		
		//发货人
		if(!StrUtils.isNull(cnortitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.cnortitle ILIKE '%"+StrUtils.getSqlFormat(cnortitle)+"%')";
		}
		//收货人
		if(!StrUtils.isNull(cneetitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.cneetitle ILIKE '%"+StrUtils.getSqlFormat(cneetitle)+"%')";
		}
		//通知人
		if(!StrUtils.isNull(notifytitle)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.notifytitle ILIKE '%"+StrUtils.getSqlFormat(notifytitle)+"%')";
		}
		
		//起飞地点
		if(!StrUtils.isNull(polid)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.polid = "+StrUtils.getSqlFormat(polid)+")";
		}
		//降落地点
		if(!StrUtils.isNull(podid)){
			dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM bus_air x WHERE x.isdelete = FALSE AND x.jobid = t.id AND x.podid = "+StrUtils.getSqlFormat(podid)+")";
		}
		//完成状态
		if(!StrUtils.isNull(iscompleteqry)){
			dynamicClauseWhere += "\n AND t.iscomplete=" + iscompleteqry;
		}
		//审核状态
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
		m.put("dynamicClauseWhere", dynamicClauseWhere);
		m.put("userid", AppUtils.getUserSession().getUserid());
		String qry = StrUtils.getMapVal(m, "qry");
		//2412 工作单编辑界面上一单下一单按列表过滤条件（将列表查询的条件先保存到个人设置里面，编辑界面上一单下一单的时候，再把列表的过滤条件查出来拼进去）
		try {
			ConfigUtils.refreshUserCfg("pages.module.air.jobsBean.search", 
					StrUtils.getSqlFormat(" AND "+qry+" "+dynamicClauseWhere)
					, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
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
	
	@Bind(id="deptids")
	@SelectItems
	public List<SelectItem> deptids = new ArrayList<SelectItem>();
	
	@Action
	public void clear() {
		this.clearQryKey();
	}
	
	@Action
	public void clearAndClose() {
		this.clearQryKey();
		this.searchWindow.close();
	}
	
	@Override
	public void clearQryKey() {
		isdate=false;
		dates="";
		dateastart="";
		dateend="";
		isjob=false;
		isrefno=false;
		iscustoms=false;
		isreceipt=false;
		isinvoice=false;
		isnumber=false;
		iscommission=false;
		numbers="";
		allcustomer="";
		customerid=0;
		sale=0;
		saleid="";
		corpid="";
		corpidop="";
		deptid="";
		filsid = "";
		operationid = "";
		customid="";
		agentdesid="";
		callid = "";
		iscl = "";
		polid = "";
		podid = "";
		Browser.execClientScript("$('#sales_input').val('')");
		Browser.execClientScript("$('#inputers_input').val('')");
		Browser.execClientScript("$('#usergetmblflex_input').val('')");
		Browser.execClientScript("$('#userreleasemblflex_input').val('')");
		Browser.execClientScript("$('#fils_input').val('')");
		Browser.execClientScript("$('#operation_input').val('')");
		Browser.execClientScript("$('#customcompany_input').val('')");
		Browser.execClientScript("$('#call_input').val('')");
		Browser.execClientScript("$('#customer_input').val('')");
		Browser.execClientScript("$('#pol_input').val('')");
		Browser.execClientScript("$('#pod_input').val('')");
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
		this.update.markUpdate(UpdateLevel.Data, "iscl");
//		super.clearQryKey();
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
	}
	
	@Action
	public void searchfee() {
		this.qryRefresh();
		this.deptids = getqueryDepartid();
		//this.grid.reload();
	}
	
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
					+ ";type=airJobs;jobid=" + id + ";dzzuid=" + dzzuid
					+ ";') As t;";
			Map m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sqlQry);
			String sql = StrUtils.getMapVal(m, "t");
			sqlBatch.add(sql);
		}
		try {
//			this.serviceContext.dzzService.executeQueryBatchByJdbc(sqlBatch);
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow unloaDaysWindows;
	
	@Bind
	@SaveState
	public String setDays ;
	
	@Action
	public void cancel(){
		unloaDaysWindows.close();
	}
	
	protected void initData(){
		String findUserCfgVal = ConfigUtils.findUserCfgVal("jobs_air_date", AppUtils.getUserSession().getUserid());
		if(StrUtils.isNull(findUserCfgVal)&&findUserCfgVal==null){
			this.setDays="365";
		}else{
			this.setDays = findUserCfgVal;
		}
	}
	
	@Action
	public void confirmSave(){
		try {
			ConfigUtils.refreshUserCfg("jobs_air_date",this.setDays, AppUtils.getUserSession().getUserid());
			Browser.execClientScript("showmsg()");
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
		Browser.execClientScript("showmsg()");
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
		Browser.execClientScript("showmsg()");
	}
	
	@Bind
	public String copyAndCreateCorpid;
	
	@Bind
	public String copyAndCreateCorpidop;
	
	@Bind
	public Date copyAndCreateJobdate;
	
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
	
	@Action
	public void copyAndCreating(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String nos = serviceContext.jobsMgrService.addcopyair(ids[0], AppUtils
					.getUserSession().getUserid(), AppUtils.getUserSession()
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
	
	@SaveState
	private Long corpids;
	
	@Bind(id = "serial")
	public  List<SelectItem> getSerial() {
		Long csid =  this.corpids;
		try {
			List<SelectItem> list = CommonComBoxBean.getComboxItems("r.prefix", "r.prefix",
					"sys_busnodesc t ,sys_busnorule r ", "WHERE r.corpid="+csid+" AND t.id = r.busnotypeid AND t.code like 'fina_jobs_air%' AND t.isdelete = FALSE AND r.isdelete = FALSE",
			"group by prefix order by prefix");
			return list;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
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
	@SaveState
	public String filterStatus;

	@Action
	public void filterByStatus() {
		this.filterStatus = AppUtils.getReqParam("type");
		this.refresh();
	}
}
