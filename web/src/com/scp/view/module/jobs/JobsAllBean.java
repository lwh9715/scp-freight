package com.scp.view.module.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.jobs.jobsAllBean", scope = ManagedBeanScope.REQUEST)
public class JobsAllBean extends GridView {
	
	@Bind
	@SaveState
	public String sonoq;
	
	@Bind
	@SaveState
	public String cntnoq;
	
	@Bind
	@SaveState
	public String mblnoq;
	
	@Bind
	@SaveState
	public String hblnoq;
	
	public Long userid;
	
	@Bind
	@SaveState
	private String roletype="";//高级检索中的动态类型
	
	@Bind
	@SaveState
	private String assignid;

	@Bind
	@SaveState
	private String userId;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		this.userId = String.valueOf(AppUtils.getUserSession().getUserid());
		if (!isPostBack) {
			super.applyGridUserDef();
		}
	}
	
	@Override
	public void grid_ondblclick() {
		long id = this.getGridSelectId();
		FinaJobs jobs = serviceContext.jobsMgrService.finaJobsDao.findByjobId(id);
		String winId = "_edit_jobsland";
		String url = "";
		if(jobs!=null&&!StrUtils.isNull(jobs.getJobtype())&&jobs.getJobtype().equals("S")){
			url = "../ship/jobsedit.xhtml?id="+id;
		}else if(jobs!=null&&!StrUtils.isNull(jobs.getJobtype())&&jobs.getJobtype().equals("A")){
			url = "../air/jobsedit.xhtml?id="+id;
		}else if(jobs!=null&&!StrUtils.isNull(jobs.getJobtype())&&jobs.getJobtype().equals("L")){
			url = "../land/jobsedit.xhtml?id="+id;
		}else if(jobs!=null&&!StrUtils.isNull(jobs.getJobtype())&&jobs.getJobtype().equals("C")){
			url = "../customs/jobsedit.xhtml?id="+id;
		}else if(jobs!=null&&!StrUtils.isNull(jobs.getJobtype())&&jobs.getJobtype().equals("H")){
			url = "../train/jobsedit.xhtml?id="+id;
		}else{
			return;
		}
		AppUtils.openWindow(winId, url);
	}
	
	@Bind
	private UIIFrame jobsinfoFrame;
	
	@Override
	public void grid_onrowselect() {
		long id = this.getGridSelectId();
		String url = AppUtils.getContextPath()
			+ "/pages/module/jobs/jobsinfo.xhtml?userid="+AppUtils.getUserSession().getUserid()+"&jobid=" + id;
		jobsinfoFrame.setSrc(url);
		update.markAttributeUpdate(jobsinfoFrame, "src");
	}



	@Bind(id="deptids")
	@SelectItems
	public List<SelectItem> deptids = new ArrayList<SelectItem>();
	
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
	
	@Bind
	@SaveState
	private boolean isdate=false;
	
	@Bind
	@SaveState
	String dynamicClauseWhere  ="";
	
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
	private String numbers;
	
	@Bind
	@SaveState
	private String vessel="";
	@Bind
	@SaveState
	private String voyage="";
	
	@Bind
	@SaveState
	private String flightno1="";
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
	private String routecode="";
	
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
	private String portss;
	
	@Bind
	@SaveState
	private String priceuserid;
	
	@Bind
	@SaveState
	private String allcustomer="";
	@Bind
	@SaveState
	private long customerid=0;
	
	@Bind
	@SaveState
	private boolean isship;
	
	@Bind
	@SaveState
	private boolean isair;
	
	@Bind
	@SaveState
	private boolean island;
	
	@Bind
	@SaveState
	private String impexp="";
	
	@Bind
	@SaveState
	private String flightdate1;
	
	@Bind
	@SaveState
	private String saler="";
	
	@Bind
	@SaveState
	private boolean iscus;
	
	@Bind
	@SaveState
	private String iscontain;
	
	@Bind
	@SaveState
	private String feeitemnamec;
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		
		//初始化
		dynamicClauseWhere = " AND 1=1 ";
		//高级查询拼接条件
		if(isdate){	
			if(!"etd".equals(dates)){
				dynamicClauseWhere  += "\nAND "+dates+"::DATE BETWEEN '"
				+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
				+ "' AND '"
				+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
				+ "'";
			}
			else{
				dynamicClauseWhere  += "\nAND (EXISTS(SELECT 1 FROM bus_shipping x where  x.jobid = t.id and x.etd BETWEEN '"
					+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
					+ "' AND '"+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)+"')"
					+ " OR  EXISTS(SELECT 1 FROM bus_air x where x.jobid = t.id and x.flightdate1 BETWEEN '"
					+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
					+ "' AND '"+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)+"')" 
					+ " OR EXISTS(SELECT 1 FROM bus_truck x where x.jobid = t.id and x.etd BETWEEN '"
					+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
					+ "' AND '"+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)+"')" 
					
					+		")";
			}
		}
	
		if(isship||isair||island||iscus){
			dynamicClauseWhere += "\nAND (FALSE ";
			if(isship){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM fina_jobs bs WHERE isdelete = FALSE AND bs.id=t.id AND jobtype = 'S')";
			}
			if(isair){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM fina_jobs bs WHERE isdelete = FALSE AND bs.id=t.id AND jobtype = 'A')";
			}
			if(island){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM fina_jobs bs WHERE isdelete = FALSE AND bs.id=t.id AND jobtype = 'L')";
			}
			if(iscus){
				dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM fina_jobs bs WHERE isdelete = FALSE AND bs.id=t.id AND jobtype = 'C')";
			}
			dynamicClauseWhere += ")";
		}
		
		if(isjob||isrefno||isbook||ishbl||ismbl||issendc||iscustoms||isreceipt||isinvoice||isnumber||isno||istitle||iscommission||isbill||islink){
			dynamicClauseWhere += "\nAND (FALSE ";
			if(isjob){
				dynamicClauseWhere +="\nOR nos ILIKE '%"+numbers+"%'";			
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
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM bus_customs bc WHERE isdelete = FALSE AND bc.jobid=t.id AND bc.nos ILIKE '%"+numbers+"%')";	
			}
			if(isreceipt){
				dynamicClauseWhere +="\nOR EXISTS(SELECT 1 FROM fina_bill fb WHERE isdelete = FALSE AND fb.jobid=t.id AND fb.billno ILIKE '%"+numbers+"%')";	
			}
			if(isinvoice){
				dynamicClauseWhere +="\nOR " +
				"EXISTS(SELECT 1 FROM fina_invoice fi,fina_arap fa WHERE fi.isdelete = FALSE and fa.isdelete = false AND fi.id=fa.invoiceid AND fa.jobid = t.id and fi.invoiceno ILIKE '%"+numbers+"%')";
			}
			if(isnumber){
				dynamicClauseWhere +="\nOR " +
						"EXISTS(SELECT 1 FROM _fina_actpayrec_search fi WHERE fi.jobid=t.id AND fi.actpayrecno ILIKE '%"+numbers+"%')";	
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
		
		//进出口
		if(impexp!=""){
			dynamicClauseWhere+="\nAND t.impexp ILIKE '%"+impexp+"%'";
		}
		
		//船名航次拼接
		if(vessel!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.vessel ILIKE '%"+vessel+"%')";
		}
		if(voyage!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.voyage ILIKE '%"+voyage+"%')";
		}
		
		//航班
		if(flightno1!=""){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_air bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.flightno1 ILIKE '%"+flightno1+"%')";
		}
		//航班日期
		if(!StrUtils.isNull(flightdate1)){
			dynamicClauseWhere+="\nAND EXISTS (SELECT 1 FROM bus_air bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND bs.flightdate1::DATE = '"+flightdate1+"')";
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
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM sys_corporation sc WHERE  isdelete = FALSE AND sc.id=t.corpidop AND sc.id = "+corpidop+")";		
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
		if(saler!=""){
			dynamicClauseWhere +="\nAND EXISTS(SELECT 1 FROM fina_jobs b WHERE isdelete = FALSE AND b.id=t.id AND b.sales ILIKE '%"+saler+"%')";	
		}
		
		//港口
		if(port!=""){
			/*dynamicClauseWhere +="\nAND "+portss+" LIKE '%"+port+"%'";*/
			dynamicClauseWhere+="\nAND EXISTS " +
			"(SELECT 1 FROM bus_shipping bs WHERE  isdelete = FALSE AND bs.jobid=t.id AND "+portss+" ILIKE '%"+port+"%')";
		}
		//询价人
		if(!StrUtils.isNull(priceuserid)){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM bus_shipping s WHERE s.jobid = t.id AND s.priceuserid ='"+priceuserid+"')";
		}
		
		if(!StrUtils.isNull(iscontain) && iscontain.equals("contain")){
			if(!StrUtils.isNull(feeitemnamec)){			
				//System.out.println("feeitemnamec--->"+feeitemnamec);
				dynamicClauseWhere += "\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.jobid=t.id AND x.feeitemid = '"+feeitemnamec+"')";
			}
		}
		
		if(!StrUtils.isNull(iscontain) && iscontain.equals("uncontain")){
			if(!StrUtils.isNull(feeitemnamec)){			
				//System.out.println("feeitemnamec--->"+feeitemnamec);
				dynamicClauseWhere += "\n AND NOT EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.jobid=t.id AND x.feeitemid = '"+feeitemnamec+"')";
			}
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
		
		//动态类型指派查询
		if(!StrUtils.isNull(assignid)&&!StrUtils.isNull(roletype)){
			dynamicClauseWhere +="\nAND (EXISTS (SELECT 1 FROM sys_user_assign x , bus_shipping y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = '"+roletype+"' AND x.userid ='"+assignid+"')";
			dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM sys_user_assign x , bus_air y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = '"+roletype+"' AND x.userid ='"+assignid+"')";
			dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM sys_user_assign x , bus_truck y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = '"+roletype+"' AND x.userid ='"+assignid+"')";
			dynamicClauseWhere +="\nOR EXISTS (SELECT 1 FROM sys_user_assign x , bus_customs y WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.roletype = '"+roletype+"' AND x.userid ='"+assignid+"'))";
		}
		
		
		m.put("dynamicClauseWhere", dynamicClauseWhere);
		
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		String sql = "\nAND ( t.saleid = "+AppUtils.getUserSession().getUserid()
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
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
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_air y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_truck y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x WHERE x.linkid = t.id AND x.linktype = 'J' AND x.userid ="+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="
			+AppUtils.getUserSession().getUserid()+") " +
		//"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+" OR COALESCE(t.saleid,0) <= 0)";
		"\n OR COALESCE(t.saleid,0) <= 0)";	
		m.put("corpfilter", corpfilter);
		String qry = m.get("qry").toString();
		if(!StrUtils.isNull(sonoq)){
			qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.id AND isdelete = FALSE AND sono ILIKE '%"+sonoq+"%')";
		}
		if(!StrUtils.isNull(cntnoq)){
			qry += "\n AND EXISTS(SELECT 1 FROM bus_ship_container WHERE jobid = t.id AND isdelete = FALSE AND cntno ILIKE '%"+cntnoq+"%')";
		}
		if(!StrUtils.isNull(mblnoq)){
			qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.id AND isdelete = FALSE AND mblno ILIKE '%"+mblnoq+"%')";
		}
		if(!StrUtils.isNull(hblnoq)){
			qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.id AND isdelete = FALSE AND hblno ILIKE '%"+hblnoq+"%')";
		}
		m.put("qry",qry);
		
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(m.containsKey("ordersql")){
			m.remove("ordersql");
		}
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql;
			m.put("ordersql", ordersql);
		}
		return m;
	}

	@Override
	public void clearQryKey() {
		sonoq = "";
		cntnoq = "";
		mblnoq = "";
		hblnoq = "";
		super.clearQryKey();
	}
	
	
	@Action
	public void searchfee() {
		this.qryRefresh();
	}
	
	@Bind
	public UIWindow searchWindow;
	
	@Action
	public void search() {
		this.searchWindow.show();
	}

	@Action
	public void clear() {
		isship = false;
		isair = false;
		island = false;
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
		priceuserid="";
		routecode="";
		impexp="";
		flightdate1="";
		flightno1="";
		saler="";
		assignid="";
		roletype="";
		iscontain="";
		feeitemnamec="";
		Browser.execClientScript("$('#sales_input').val('')");
		Browser.execClientScript("$('#inputers_input').val('')");
		Browser.execClientScript("$('#customer_input').val('')");
		Browser.execClientScript("$('#port_input').val('')");
		Browser.execClientScript("$('#priceuser_input').val('')");
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
	}
	
	@Action
	public void clearAndClose() {
		this.clear();
		this.searchWindow.close();
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
}
