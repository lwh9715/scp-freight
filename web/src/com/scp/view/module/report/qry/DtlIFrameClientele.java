package com.scp.view.module.report.qry;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.report.qry.dtlIFrameClienteleBean", scope = ManagedBeanScope.REQUEST)
public class DtlIFrameClientele extends GridView{
	
	@Bind
	@SaveState
	public String code;
	
	@Bind
	@SaveState
	public String filtertype;
	
	
	@Bind
	@SaveState
	public String sales;
	
	@Bind
	@SaveState
	public String entercode;
	
	@Bind
	@SaveState
	public String qryValues="";//用于存所以的查询值
	
	@Bind
	@SaveState
	public String daysar;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if(m.containsKey("filter2")){
			m.remove("filter2");
		}
		if(m.containsKey("filter3")){
			m.remove("filter3");
		}
//		if(!StrUtils.isNull(code)){
//			m.put("filter2", "\nAND (code ILIKE '%"+StrUtils.getSqlFormat(code)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(code)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(code)+"%' OR abbr ILIKE '%"+StrUtils.getSqlFormat(code)+"%') ");
//		}
		if(!qryValues.equals("")){
			String sql = "";
			String[] split = qryValues.split(",");
			for(String s:split){
				if(sql.equals("")){
					sql+="\nAND ((code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR abbr ILIKE '%"+StrUtils.getSqlFormat(s)+"%')";
				}else{
					sql+=" OR(code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR abbr ILIKE '%"+StrUtils.getSqlFormat(s)+"%')";
				}
			}
			sql+=")";
			m.put("filter2",sql);
		}
		Long userid = AppUtils.getUserSession().getUserid();
		String usercode = AppUtils.getUserSession().getUsercode();
		StringBuffer sb = new StringBuffer();
		
		m.put("userid", userid);
		
//		String filter = ""
//			+ "\n AND	((a.iscustomer = false and isdelete = false) OR EXISTS"
//			+ "\n				(SELECT "
//			+ "\n					1 "
//			+ "\n				FROM sys_custlib x , sys_custlib_user y  "
//			+ "\n				WHERE y.custlibid = x.id  "
//			+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
//			+ "\n					AND x.libtype = 'S'  "
//			+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and " +
//					"									EXISTS (SELECT 1 FROM fina_jobs xx WHERE xx.isdelete = FALSE AND xx.customerid = a.id AND xx.saleid = z.id) " +
//					
//					"AND z.isdelete = false) " //关联的业务员的单，都能看到
//			+ "\n					)" 
//			+ "\n )";
		
		String jobSalesid = "a.salesid";//如果是计费客户，先按检索的条件直接把值过滤的，不对，应该是反查，这个客户所属的工作单的业务员，再检查业务员关联 Neo 20190520
		String jobCustomerid = "a.id";
		
		//Neo 20190610 能看所有外办订到本公司的单权限的人能看到对应单
		boolean isShowJobsCorpOp = false;
		try {
			Map map = this.serviceContext.sysCorporationService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+") AS flag");
			if(StrUtils.getMapVal(map, "flag").equals("1"))isShowJobsCorpOp=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		String filterByJobsCorpOp = "";
		if("customerofarap".equals(filtertype)){//计费客户 
			jobSalesid = "ANY(SELECT DISTINCT zz.saleid FROM fina_jobs zz , fina_arap xx WHERE zz.id  = xx.jobid AND xx.customerid = a.id AND xx.isdelete = FALSE AND zz.isdelete = FALSE)";
			//jobCustomerid = "ANY(SELECT DISTINCT zz.customerid FROM fina_jobs zz , fina_arap xx WHERE zz.id  = xx.jobid AND xx.customerid = a.id AND xx.isdelete = FALSE AND zz.isdelete = FALSE)";
			jobCustomerid = "ANY(SELECT DISTINCT zz.customerid FROM fina_jobs zz WHERE zz.isdelete = FALSE AND zz.isclose = FALSE)";
			if(isShowJobsCorpOp){
				filterByJobsCorpOp = " OR EXISTS(SELECT 1 FROM fina_jobs zz , fina_arap xx where xx.customerid = a.id AND zz.id = xx.jobid AND xx.isdelete = FALSE AND zz.isdelete = FALSE AND zz.corpid <> zz.corpidop AND zz.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")";
			}
		}else{//委托客户
			jobSalesid = "a.salesid";
			jobCustomerid = "a.id";
			if(isShowJobsCorpOp){
				filterByJobsCorpOp = " OR EXISTS(SELECT 1 FROM fina_jobs xx where xx.customerid = a.id AND xx.corpid <> xx.corpidop AND xx.isdelete = FALSE AND xx.corpidop = "+AppUtils.getUserSession().getCorpidCurrent()+")";
			}
		}
		
		
		String filter = ""
			+ "\nAND ((a.iscustomer = false and isdelete = false) "
			+ "\n					OR EXISTS(SELECT "
			+ "\n							1 "
			+ "\n						  FROM sys_custlib x , sys_custlib_user y  "
			+ "\n						  WHERE y.custlibid = x.id  "
			+ "\n						  	AND y.userid = "+AppUtils.getUserSession().getUserid()
			+ "\n							--AND x.libtype = 'S'  "//关联的业务员的单，都能看到
			+ "\n							AND EXISTS(SELECT 1 FROM sys_custlib_cust z where z.custlibid = x.id and z.corpid = "+jobCustomerid+") " 
			+ "\n						  )" 
			+ "\n					OR (EXISTS (SELECT 1 FROM fina_arap x, sys_user_assign y , bus_shipping z WHERE x.isdelete = FALSE AND x.customerid = "+jobCustomerid+" AND x.amount != 0 AND y.userid = "+AppUtils.getUserSession().getUserid()+" AND y.linkid = z.id and x.jobid = z.jobid ))"
			+ "\n					OR EXISTS(SELECT * FROM sys_user_assign x WHERE x.linkid = "+jobCustomerid+" AND x.roletype = 'S' AND linktype = 'C'"//组关联业务员——分派中业务员
			+ "\n								AND EXISTS (SELECT * FROM sys_custlib y WHERE y.userid = x.userid AND y.libtype = 'S'"
			+ "\n								AND EXISTS (SELECT * FROM sys_custlib_role z WHERE z.custlibid = y.id"
			+ "\n								AND EXISTS (SELECT * FROM sys_userinrole w WHERE w.isdelete = FALSE AND w.roleid = z.roleid AND w.userid = "+AppUtils.getUserSession().getUserid()+"))))"
			+ "\n					OR EXISTS (SELECT * FROM sys_custlib y WHERE y.userid = "+jobSalesid+" AND y.libtype = 'S'"//组关联业务员——客户默认业务员
			+ "\n								AND EXISTS (SELECT * FROM sys_custlib_role z WHERE z.custlibid = y.id"
			+ "\n								AND EXISTS (SELECT * FROM sys_userinrole w WHERE w.isdelete = FALSE AND w.roleid = z.roleid AND w.userid = "+AppUtils.getUserSession().getUserid()+")))"
			+"\n					" + filterByJobsCorpOp
			+ "\n	)";
		
		////System.out.println(filter);
//		sb.append("\n	AND ((");
//		sb.append("\n		EXISTS (SELECT 1 ");
//		sb.append("\n					FROM sys_custlib_cust y,sys_custlib_user z");
//		sb.append("\n					WHERE");
//		sb.append("\n							y.custlibid = z.custlibid");
//		sb.append("\n					AND y.corpid = a.id");
//		sb.append("\n					AND z.userid = "+userid);
//		sb.append("\n							)");
//		sb.append("\n			OR EXISTS(SELECT 1");
//		sb.append("\n					FROM sys_user_assign xx");
//		sb.append("\n					WHERE");
//		sb.append("\n							xx.linkid = a.id");
//		sb.append("\n					AND xx.userid = "+userid);
//		sb.append("\n				))");
//		sb.append("\n			OR a.inputer = '"+usercode+"'");
//		sb.append("\n			)");
		m.put("filter", filter);
		//分公司过滤客户
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			StringBuffer sbcorp = new StringBuffer();
//			sbcorp.append("\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND x.corpid="+AppUtils.getUserSession().getCorpidCurrent()+")");
//			m.put("corpfilter", sbcorp.toString());
//		}
		if(m.containsKey("typefilter")){
			m.remove("typefilter");
		}
		//System.out.println("filtertype:"+filtertype);
		
		if(StrUtils.isNull(filtertype)){
			filtertype = "customerofarap";
		}
		if("customerofarap".equals(filtertype)){//计费客户
			//m.put("typefilter", "\nAND EXISTS (SELECT 1 FROM fina_arap x, sys_user_corplink y WHERE x.isdelete = FALSE AND x.customerid = a.id AND x.amount != 0 AND y.userid = "+AppUtils.getUserSession().getUserid()+" AND y.ischoose = true AND (x.corpid = y.corpid))");
			
			String corpfilter = "";
			if("2199".equals(ConfigUtils.findSysCfgVal("CSNO"))){
				corpfilter = "\n AND (CASE WHEN EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = "+AppUtils.getUserSession().getCorpid()+") " +
						"\n 		THEN " +
						"\n				xx.corpid = ANY(SELECT id AS corpid FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice())" +
						"\n								UNION ALL" +
						"\n								SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + 
						"\n								)" +
						"\n			ELSE " +
						"\n				xx.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + ")" +
						"\n			END)";
			}else{
				corpfilter = "\n AND xx.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = " + AppUtils.getUserSession().getUserid() + ")";
				//m.put("typefilter", "\nAND EXISTS (SELECT 1 FROM fina_arap xx WHERE xx.isdelete = FALSE AND xx.customerid = a.id AND xx.amount != 0 AND xx.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE  x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+"))");
			}
			m.put("typefilter", "\nAND EXISTS (SELECT 1 FROM fina_arap xx WHERE xx.isdelete = FALSE AND xx.customerid = a.id AND xx.amount != 0 "+corpfilter+")");
		}else if("customerofclient".equals(filtertype)){//委托客户
			//m.put("typefilter", "\nAND EXISTS (SELECT 1 FROM fina_jobs x , sys_user_corplink y WHERE x.isdelete = FALSE AND x.customerid = a.id AND y.userid = "+AppUtils.getUserSession().getUserid()+" AND y.ischoose = true AND (x.corpid = y.corpid OR x.corpidop = y.corpid))");
			m.put("typefilter", "\nAND EXISTS (SELECT 1 FROM fina_jobs xx WHERE xx.isdelete = FALSE AND xx.customerid = a.id AND xx.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE  x.ischoose = TRUE AND x.userid = "+AppUtils.getUserSession().getUserid()+"))"); 
		}
		if(m.containsKey("sales")){
			m.remove("sales");
		}
		if(!StrUtils.isNull(sales)){
			StringBuilder sbsales = new StringBuilder();
			sbsales.append("\nAND (EXISTS (SELECT * FROM fina_jobs x WHERE x.isdelete = FALSE AND x.saleid IN("+sales+") AND a.id = x.customerid)");
			sbsales.append("\n OR  EXISTS (SELECT * FROM fina_arap x INNER JOIN fina_jobs y ON x.jobid = y.id AND x.isdelete = FALSE AND y.isdelete = FALSE AND x.customerid = a.id AND y.saleid IN("+sales+")))");
			m.put("sales", sbsales.toString());
		}
		
		if(!StrUtils.isNull(daysar)){
			m.put("daysar", "\nAND daysar="+daysar);
		}
//		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = a.id AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
//		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()){//非saas模式不控制
//			m.put("corpfilter", qry);
//		}
		if("Y".equals(ConfigUtils.findSysCfgVal("sys_rpt_customer_filter_by_sales"))){
			m.put("filterBySales", "\nAND a.id = ANY(SELECT id FROM f_sys_corporation_filter_rpt('srctype="+filtertype+";userid=" + AppUtils.getUserSession().getUserid() + "'))");
		}
		
		return m;
	}
	
	
	@Override
	public void clearQryKey() {
		this.qryValues = "";
		this.code = "";
		super.clearQryKey();
		Browser.execClientScript("parent.window","argsClienteleJsVar.setValue('');");
		Browser.execClientScript("parent.window","setClienteleiIds('');");
	}
	@Override
	public void refresh() {
		if (grid != null) {
			if(code!=null&&!code.equals("")){
				qryValues = code+",";
			}
			this.grid.reload();
//			this.entercode = this.code;
//			update.markUpdate(UpdateLevel.Data, "entercode");
//			Browser.execClientScript("resize();");
		}
	}
	@Action
	public void filterClientele(){
		filtertype = AppUtils.getReqParam("type");
		update.markUpdate(UpdateLevel.Data, "filtertype");
		refresh();
	}
	@Action
	public void filterSales(){
		Browser.execClientScript("parent.window","filterClienteleBySales();");
	}
	
	@Action
	public void filterClienteleBySales(){
		sales = AppUtils.getReqParam("sales");
		update.markUpdate(UpdateLevel.Data, "sales");
		refresh();
	}
	
	@Action
	public void qrysAction(){
		if(code!=null&&!code.equals("")){
			qryValues += code+",";
		}
		update.markUpdate(UpdateLevel.Data, "qryValues");
		this.grid.reload();
	}
}
