package com.scp.view.module.elogistics.jobs;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.base.CommonComBoxBean;
import com.scp.model.elogistics.data.Elogistics;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.elogistics.jobs.jobsBean", scope = ManagedBeanScope.REQUEST)
public class JobsBean  extends GridView{
	
	/**
	 * 工作id
	 */
	@SaveState
	@Accessible
	public Long jobid;
	
	@SaveState
	@Accessible
	public Elogistics selectedRowData = new Elogistics();

	

	
	@Bind(id="qryfabwms")
    public List<SelectItem> getqryfabwms() {
    	try {
    		return CommonComBoxBean.getComboxItems("p.code ||'/'|| COALESCE(p.city,'') ||'/'|| COALESCE(p.zip,'')","p.code ||'/'|| COALESCE(p.city,'') ||'/'|| COALESCE(p.zip,'')","dat_warehouse AS p"
    				,"WHERE isdelete = false AND p.isfba = true ","ORDER BY p.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }

	@Bind
	@SaveState
	public Boolean iscare = false;

	@Bind
	@SaveState
	public String setDays;
	@SaveState
	String dynamicClauseWhere  ="";
	@Bind
	@SaveState
	private boolean isdate=false;

	@Bind
	@SaveState
	private String impexp;

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
	
	
	@Bind
	@SaveState
	private String numbers;
	
	@Bind
	@SaveState
	private String ldtype="";
	
	@Bind
	@SaveState
	private long customerid=0;
	@Bind
	@SaveState
	private String allcustomer="";
	@Bind
	@SaveState
	private String iscompleteqry;
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
	public String sono; 
	
	@Bind
	@SaveState
	public String sonos; 
	
	@Bind
	@SaveState
	public String driverno; 
	
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
		
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(m.containsKey("ordersql")){
			m.remove("ordersql");
		}
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql;
			m.put("ordersql", ordersql);
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
		if(customerid!=0&&allcustomer.equals("customerid")){
			dynamicClauseWhere +="\nAND "+allcustomer+" ='"+customerid+"'";
		}
		if(customerid!=0&&allcustomer.equals("clientid")){
			dynamicClauseWhere +="\nAND EXISTS (SELECT 1 FROM fina_arap fa WHERE  isdelete = FALSE AND fa.jobid=t.id AND fa.customerid="+customerid+")";
		}
		
		//完成状态
		if(!StrUtils.isNull(iscompleteqry)){
			dynamicClauseWhere += "\n AND t.iscomplete=" + iscompleteqry;
		}
		
		//装箱
		if(ldtype!=""){
			dynamicClauseWhere+="\nAND t.ldtype ILIKE '%"+ldtype+"%'";
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
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_truck y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)";

		// 权限控制 neo 2014-05-30
		m.put("filter", sql);
		String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+") " +
		//"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+" OR COALESCE(t.saleid,0) <= 0) ";
		"\n OR COALESCE(t.saleid,0) <= 0) ";
		m.put("corpfilter", corpfilter);
		
		m.put("userid", AppUtils.getUserSession().getUserid());
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(sono))qry+="\n AND EXISTS(SELECT 1 FROM bus_truck WHERE isdelete = FALSE AND jobid = t.id AND sono ILIKE '%"+sono+"%')";
		if(!StrUtils.isNull(sonos))qry+="\n AND EXISTS(SELECT 1 FROM bus_ship_container WHERE isdelete = FALSE AND jobid = t.id AND sono ILIKE '%"+sonos+"%')";
		if(!StrUtils.isNull(driverno))qry+="\n AND EXISTS(SELECT 1 FROM bus_truck WHERE isdelete = FALSE AND jobid = t.id AND driverno ILIKE '%"+driverno+"%')";
		m.put("qry", qry);
		//2412 工作单编辑界面上一单下一单按列表过滤条件（将列表查询的条件先保存到个人设置里面，编辑界面上一单下一单的时候，再把列表的过滤条件查出来拼进去）
		try {
			ConfigUtils.refreshUserCfg("pages.module.land.jobsBean.search", 
					StrUtils.getSqlFormat(" AND "+qry+" "+dynamicClauseWhere)
					, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}
	@Action
	public void add() {
		String winId = "_edit_jobs land";
		String url = "./jobedit.xhtml?impexp="+impexp;
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void delMaster() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}
		try {
			serviceContext.elogisticsService.removeDate(this.getGridSelectId(),
					AppUtils.getUserSession().getUsercode());
			this.alert("OK");
			this.qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

}
