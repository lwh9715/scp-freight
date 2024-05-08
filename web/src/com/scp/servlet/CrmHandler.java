package com.scp.servlet;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoRowException;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;

public class CrmHandler {
	@Resource
	public ServiceContext serviceContext;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public String handle(String action, HttpServletRequest request){
		String reqStr = "";//条件
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			
			String userid = (String)request.getSession().getAttribute("userid");
			//System.out.println("Session userid:"+userid);
			
			if("getCustomerTotal".equals(action)) {
				result = this.queryARAP(request,reqStr);
			}
			if("getSelectDepartment".equals(action)) {
				result = this.querySelectDepartment(request,reqStr);
			}
			if("getSelectSeals".equals(action)) {
				result = this.querySelectSeals(request,reqStr);
			}
			if("getSelectUsers".equals(action)) {
				result = this.querySelectUsers(request,reqStr);
			}
			if("getFromSelectSeals".equals(action)) {
				result = this.queryFromSelectSeals(request,reqStr);
			}
			if("getFromDepartment".equals(action)) {
				result = this.queryFromDepartment(request,reqStr);
			}
			if("getFromSelectCurrency".equals(action)) {
				result = this.queryFromSelectCurrency(request,reqStr);
			}
			if("getFromSelectUsers".equals(action)) {
				result = this.queryFromSelectUsers(request,reqStr);
			}
			if("getCustomerTotalEcharts".equals(action)) {
				result = this.queryARAPEcahrts(request,reqStr);
			}
			if("getCustomerfollowsnum".equals(action)) {
				result = this.queryCustomerfollowsnum(request,reqStr);
			}
			if("queryTransactioncycle".equals(action)) {
				result = this.queryTransactioncycle(request,reqStr);
			}
			if("getCustomerfollowsCahrts".equals(action)) {
				result = this.queryCustomerfollowsCahrts(request,reqStr);
			}
			if("queryTransactioncycleCahrts".equals(action)) {
				result = this.queryTransactioncycleCahrts(request,reqStr);
			}
			if("getCustomerfollowstype".equals(action)) {
				result = this.queryCustomerfollowstype(request,reqStr);
			}
			if("getCustomerprofit".equals(action)) {
				result = this.getCustomerprofit(request,reqStr);
			}
			if("getCustomerfollowstypeEcahrts".equals(action)) {
				result = this.queryCustomerfollowstypeEcahrts(request,reqStr);
			}
			if("getCustomerprofitEcahrts".equals(action)) {
				result = this.getCustomerprofitEcahrts(request,reqStr);
			}
			if("getOrderanAlysis".equals(action)) {
				result = this.QueryOrderanAlysis(request,reqStr);
			}
			if("getOrderanAlysisEcahrts".equals(action)) {
				result = this.QueryOrderanAlysisEcahrts(request,reqStr);
			}


			if("getUfmsDefGrid".equals(action)) {
				result = this.QueryUfmsDefGrid(request,reqStr);
			}
			if("saveUfmsDefGrid".equals(action)) {
				result = this.SaveUfmsDefGrid(request,reqStr);
			}
		} catch (Exception e) {
			result = "{\"results\":" + "error" + "}";
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @param querySql
	 * @return 返回json类型字符串
	 */
	public String getPGarray_to_json(String querySql){
		return "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
	}
	
	public String[] getDaterange10 (String daterange,String daterange10){
		String dateastart = "'0001-01-01'";
		String dateend = "'9999-12-31'";
		String returnv[]=new String[3];
		if(!StrUtils.isNull(daterange)){
			if(daterange.equals("0")){
				dateastart = " now()";
				dateend = " now()";
			}else if(daterange.equals("1")){
				dateastart = " now() - interval '1 Days'";
				dateend = " now() - interval '1 Days'";
			}else if(daterange.equals("2")){//本周
				dateastart = " current_date +cast(-1*(TO_NUMBER(to_char(DATE (current_date),'D'),'99')-2) ||' days' as interval)";//周一
				dateend = " current_date +cast(-1*(TO_NUMBER(to_char(DATE (current_date),'D'),'99')-8) ||' days' as interval)";//周日
			}else if(daterange.equals("3")){//上周
				dateastart = " current_date- interval '1 week' +cast(-1*(TO_NUMBER(to_char(DATE (current_date),'D'),'99')-2) ||' days' as interval)";
				dateend = " current_date- interval '1 week' +cast(-1*(TO_NUMBER(to_char(DATE (current_date),'D'),'99')-8) ||' days' as interval)";
			}else if(daterange.equals("4")){//本月
				dateastart = " (to_char(now(),'YYYYmm')||'01')::timestamp";
				dateend = " (to_char(now(),'YYYYmm')||'01')::timestamp + '1 Months' - interval '1 Days'";
			}else if(daterange.equals("5")){//上月
				dateastart = " (to_char(now()- interval '1 Months','YYYYmm')||'01')::timestamp";
				dateend = " (to_char(now()- interval '1 Months','YYYYmm')||'01')::timestamp + '1 Months' - interval '1 Days'";
			}else if(daterange.equals("6")){//本季度
				dateastart = " date_trunc('quarter',now())- interval '1 Days'";
				dateend = " date_trunc('quarter',now())+ '3 Months'- interval '1 Days'";
			}else if(daterange.equals("7")){//上季度
				dateastart = " date_trunc('quarter',now())- interval '3 Months'- interval '1 Days'";
				dateend = " date_trunc('quarter',now())- interval '1 Days'";
			}else if(daterange.equals("8")){//本年
				dateastart = " date_trunc('year',now())";
				dateend = " date_trunc('year',now()) + '1 Years'-interval '1 Days'";
			}else if(daterange.equals("9")){//去年
				dateastart = " date_trunc('year',now()) - interval '1 Years'+interval '1 Days'";
				dateend = " date_trunc('year',now())-interval '1 Days'";
			}else if(daterange.equals("10")){//自定义
				if(!StrUtils.isNull(daterange10)){
					String[] s = daterange10.split("->");
					dateastart = "'"+s[0]+"'::DATE";
					dateend = "'"+s[1]+"'::DATE";
				}
			}
		}
		returnv[0] = "BETWEEN "+dateastart+" AND "+dateend;
		returnv[1] = dateastart;
		returnv[2] = dateend;
		return returnv;
	}
	
	private String relevanceSealse(String userid){
		return "\n	AND( EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_user y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND y.userid = "+userid
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND x.userid = t.saleid " //关联的业务员的
		+ ")"
		+ "\n	OR EXISTS"
		+ "\n				(SELECT "
		+ "\n					1 "
		+ "\n				FROM sys_custlib x , sys_custlib_role y  "
		+ "\n				WHERE y.custlibid = x.id  "
		+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+userid+" AND z.roleid = y.roleid)"
		+ "\n					AND x.libtype = 'S'  "
		+ "\n					AND x.userid = t.saleid " //组关联业务员的
		+ "))";
	}
	
	private String queryARAP(HttpServletRequest request,String reqStr){
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String daterange10 =  request.getParameter("daterange10");
		String daterange10ba = getDaterange10(daterange, daterange10)[0];
		String userid = (String)request.getSession().getAttribute("userid");
		String result = "";
		if(StrUtils.isNull(daterange)&&StrUtils.isNull(departmentid)&&StrUtils.isNull(sealsid)&&StrUtils.isNull(daterange10)){
			//如果没输入搜索条件，直接返回空数据
			String returnv = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+"\"\""+"}";
			return returnv;
		}
		String querySql = "SELECT t.namec,syscorcount,syscorcountdeal,(CASE WHEN COALESCE(syscorcount,0)=0 THEN 0 ELSE syscorcountdeal::numeric/syscorcount::numeric*100 END)::numeric(18,2) AS dealrate,prifitsum,amountsum,amtstl2sum"
						+"\n				,(CASE WHEN COALESCE(amountsum,0)=0 THEN 0 ELSE amtstl2sum::numeric/amountsum::numeric*100 END)::numeric(18,2) AS moneyrate"
						+"\n	FROM (SELECT x.namec"
						+"\n			,(SELECT COUNT(1) FROM sys_corporation WHERE isdelete = FALSE AND salesid = x.id" 
						+"\n				AND inputtime "+daterange10ba+") AS syscorcount"
						+"\n			,(SELECT COUNT(1) FROM sys_corporation a WHERE isdelete = FALSE AND salesid = x.id"
						+"\n				AND EXISTS(SELECT 1 FROM fina_jobs t WHERE t.isdelete = FALSE AND t.customerid = a.id AND EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND jobid = t.id)"
						+"\n				AND jobdate "+daterange10ba+"" 
						+"\n				"+relevanceSealse(userid)+")) AS syscorcountdeal"
						+"\n			,(SELECT CAST(COALESCE(SUM(f_amtto(now()::DATE,a.currency,'USD',(CASE WHEN a.araptype = 'AR' THEN a.amount ELSE a.amount*-1 END))), 0) AS NUMERIC(18,6)) FROM fina_arap a,fina_jobs t WHERE a.jobid = t.id AND t.saleid = x.id AND a.isdelete = FALSE AND t.isdelete = FALSE AND a.rptype <> 'O' AND a.rptype <> 'W'" 
						+"\n						AND t.jobdate "+daterange10ba
						+"\n"+relevanceSealse(userid)+")::NUMERIC(18,2) AS prifitsum"
						+"\n			,(SELECT CAST(COALESCE(SUM(f_amtto(now()::DATE,a.currency,'USD',a.amount)) , 0) AS NUMERIC(18,6)) FROM fina_arap a,fina_jobs t WHERE a.jobid = t.id AND a.araptype = 'AR' AND t.saleid = x.id AND a.isdelete = FALSE AND t.isdelete = FALSE AND a.rptype <> 'O' AND a.rptype <> 'W'" 
						+"\n						AND t.jobdate "+daterange10ba
						+"\n"+relevanceSealse(userid)+")::NUMERIC(18,2) AS amountsum"
						+"\n			,(SELECT CAST(COALESCE(SUM(f_amtto(now()::DATE,a.currency,'USD',a.amtstl2)) , 0) AS NUMERIC(18,6)) FROM fina_arap a,fina_jobs t WHERE a.jobid = t.id AND t.saleid = x.id AND a.isdelete = FALSE AND t.isdelete = FALSE AND a.rptype <> 'O' AND a.rptype <> 'W'" 
						+"\n						AND t.jobdate "+daterange10ba
						+"\n"+relevanceSealse(userid)+")::NUMERIC(18,2) AS amtstl2sum"
						+"\n	FROM sys_user x"
						+"\n	WHERE isdelete = FALSE"
						+"\n	AND x.issales = TRUE " 
						+(StrUtils.isNull(departmentid)?"":("\n	AND x.deptid = ANY(regexp_split_to_array('"+ departmentid+"',',')::BIGINT[])"))
						+(StrUtils.isNull(sealsid)?"":("\n	AND x.id = ANY(regexp_split_to_array('"+ sealsid+"',',')::BIGINT[])"))
						+"\n) t";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(getPGarray_to_json(querySql));
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
		}else {
			result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		return result;
	}
	
	private String querySelectDepartment(HttpServletRequest request,String reqStr){
		String result = "";
		String querySql = "SELECT id,name||'/'||COALESCE((SELECT abbr FROM sys_corporation WHERE id = x.corpid),'') AS departname " +
						"FROM sys_department x WHERE isdelete = FALSE";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String querySelectSeals(HttpServletRequest request,String reqStr){
		String result = "";
		String querySql = "SELECT id,namec FROM sys_user WHERE isdelete = FALSE AND issales = TRUE";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String querySelectUsers(HttpServletRequest request,String reqStr){
		String result = "";
		String querySql = "SELECT id,namec FROM sys_user WHERE isdelete = FALSE";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryFromSelectUsers(HttpServletRequest request,String reqStr){
		String departmentid =  request.getParameter("department");
		String result = "";
		String querySql = "SELECT id AS value,namec AS name,'' AS selected,'' AS disabled " +
						  "\nFROM sys_user WHERE isdelete = FALSE AND issales = TRUE "+
						  ((StrUtils.isNull(departmentid))?"":("AND deptid = "+departmentid));
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(getPGarray_to_json(querySql));
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryFromSelectSeals(HttpServletRequest request,String reqStr){
		String departmentid =  request.getParameter("department");
		String userid = (String)request.getSession().getAttribute("userid");
		String result = "";
		String querySql = "SELECT id AS value,namec AS name,'' AS selected,'' AS disabled " +
						  "\nFROM sys_user t WHERE isdelete = FALSE "+
						  ((StrUtils.isNull(departmentid))?"":("AND deptid = ANY(regexp_split_to_array('"+departmentid+"',',')::BIGINT[])"))
						  + "\n	AND( EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_user y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND y.userid = "+userid
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND x.userid = t.id " //关联的业务员的
						+ ")"
						+ "\n	OR EXISTS"
						+ "\n				(SELECT "
						+ "\n					1 "
						+ "\n				FROM sys_custlib x , sys_custlib_role y  "
						+ "\n				WHERE y.custlibid = x.id  "
						+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+userid+" AND z.roleid = y.roleid)"
						+ "\n					AND x.libtype = 'S'  "
						+ "\n					AND x.userid = t.id " //组关联业务员的
						+ "))";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(getPGarray_to_json(querySql));
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryFromDepartment(HttpServletRequest request,String reqStr){
		String result = "";
		String querySql = "SELECT id AS value,name||'/'||COALESCE((SELECT abbr FROM sys_corporation WHERE id = x.corpid),'') AS name,'' AS selected,'' AS disabled " +
						  "\nFROM sys_department x WHERE isdelete = FALSE ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(getPGarray_to_json(querySql));
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryFromSelectCurrency(HttpServletRequest request,String reqStr){
		String departmentid =  request.getParameter("department");
		String result = "";
		String querySql = "SELECT code AS value,code AS name,(CASE WHEN code = 'USD' THEN 'USD' ELSE '' END) AS selected,'' AS disabled " +
						  "\nFROM _dat_currency d"+
						  "\nWHERE 1=1";
		String filterBySaas = SaasUtil.filterByCorpid("d" , request);
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(getPGarray_to_json(querySql + filterBySaas));
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryARAPEcahrts(HttpServletRequest request,String reqStr){
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String daterange10 =  request.getParameter("daterange10");
		String daterange10ba = getDaterange10(daterange, daterange10)[0];
		String userid = (String)request.getSession().getAttribute("userid");
		String result = "";
		String querySql = "SELECT ARRAY_AGG(months) AS months,ARRAY_AGG(syscorcount) AS syscorcount,ARRAY_AGG(syscorcountarap) AS syscorcountarap FROM("
						+getWithRc_months(daterange,daterange10)
						+"\n	SELECT "
						+"\n		"+getQueryMonths(daterange,daterange10)+" AS months"
						+"\n		,(SELECT COUNT(1) FROM sys_corporation a WHERE isdelete = FALSE AND "+getQueryExtract(daterange, "inputtime",daterange10)
						+"\n 			AND inputtime "+daterange10ba
						+(StrUtils.isNull(sealsid)?"":("\n 			AND EXISTS(SELECT 1 FROM sys_user WHERE a.salesid = id AND isdelete = FALSE AND id = ANY(regexp_split_to_array('"+ sealsid+"',',')::BIGINT[]))"))
						+(StrUtils.isNull(departmentid)?"":("\n 			AND EXISTS(SELECT 1 FROM sys_user WHERE a.salesid = id AND isdelete = FALSE AND deptid = ANY(regexp_split_to_array('"+ departmentid+"',',')::BIGINT[]))"))
						+"					) AS syscorcount"
						+"\n		,(SELECT COUNT(1) FROM sys_corporation a WHERE a.isdelete = FALSE  "
						+"\n				AND EXISTS(SELECT 1 FROM fina_jobs t WHERE t.isdelete = FALSE AND t.customerid = a.id AND EXISTS(SELECT 1 FROM fina_arap WHERE isdelete = FALSE AND jobid = t.id)"
						+"\n								AND "+getQueryExtract(daterange, "t.jobdate",daterange10)
						+"\n		 			AND jobdate "+daterange10ba+"\n"+relevanceSealse(userid)+")"
						+(StrUtils.isNull(sealsid)?"":("\n 			AND EXISTS(SELECT 1 FROM sys_user WHERE a.salesid = id AND isdelete = FALSE AND id = ANY(regexp_split_to_array('"+ sealsid+"',',')::BIGINT[]))"))
						+(StrUtils.isNull(departmentid)?"":("\n 			AND EXISTS(SELECT 1 FROM sys_user WHERE a.salesid = id AND isdelete = FALSE AND deptid = ANY(regexp_split_to_array('"+ departmentid+"',',')::BIGINT[]))"))
						+") AS syscorcountarap"
						+"\n	FROM rc_months x"
						+")t";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(getPGarray_to_json(querySql));
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryCustomerfollowsnum(HttpServletRequest request,String reqStr){//客户跟进次数分析
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String daterange10 =  request.getParameter("daterange10");
		String daterange10ba = getDaterange10(daterange, daterange10)[0];
		String userid = (String)request.getSession().getAttribute("userid");
		if(StrUtils.isNull(daterange)&&StrUtils.isNull(departmentid)&&StrUtils.isNull(sealsid)&&StrUtils.isNull(daterange10)){
			//如果没输入搜索条件，直接返回空数据
			String returnv = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+"\"\""+"}";
			return returnv;
		}
		String result = "";
		String querySql = "SELECT x.namec"
						+"\n			,(SELECT COUNT(1) FROM sys_corporation a WHERE isdelete = FALSE"
						+"\n				AND EXISTS(SELECT 1 FROM fina_jobs t,bus_shipping c WHERE x.isdelete = FALSE AND c.jobid = t.id AND t.customerid = a.id"
						+"\n					AND EXISTS(SELECT 1 FROM sys_user_assign WHERE userid = x.id AND linkid = a.id)" 
						+"\n				AND t.jobdate "+daterange10ba+relevanceSealse(userid)+")) AS syscorcountdeal"
						+"\n			,(SELECT COUNT(1) FROM fina_jobs t,bus_shipping b WHERE t.isdelete = FALSE AND b.jobid = t.id"
						+"\n				AND EXISTS(SELECT 1 FROM sys_user_assign WHERE userid = x.id AND linkid = b.id)" 
						+"\n				AND t.jobdate "+daterange10ba+relevanceSealse(userid)+") AS syscorcount"
						+"\n			FROM sys_user x"
						+"\n	WHERE isdelete = FALSE"
						+(StrUtils.isNull(departmentid)?"":("\n	AND x.deptid = "+ departmentid))
						+(StrUtils.isNull(sealsid)?"":("\n	AND x.id = "+ sealsid));
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
		}else {
			result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		return result;
	}
	
	private String queryTransactioncycle(HttpServletRequest request,String reqStr){//成交周期分析
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String daterange10 =  request.getParameter("daterange10");
		String daterange10ba = getDaterange10(daterange, daterange10)[0];
		if(StrUtils.isNull(daterange)&&StrUtils.isNull(departmentid)&&StrUtils.isNull(sealsid)&&StrUtils.isNull(daterange10)){
			//如果没输入搜索条件，直接返回空数据
			String returnv = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+"\"\""+"}";
			return returnv;
		}
		String result = "";
		String querySql = "SELECT x.namec"
						+"\n, (SELECT AVG((SELECT MIN(jobdate) FROM fina_jobs WHERE customerid = a.id AND jobdate "+daterange10ba+")::DATE - a.inputtime::DATE)::INT FROM sys_corporation a WHERE a.salesid = x.id) AS dayavg"
						+"\n, (SELECT SUM((SELECT MIN(jobdate) FROM fina_jobs WHERE customerid = a.id AND jobdate "+daterange10ba+")::DATE - a.inputtime::DATE)::INT FROM sys_corporation a WHERE a.salesid = x.id) AS daysum"
						+"\n			FROM sys_user x"
						+"\n	WHERE isdelete = FALSE"
						+(StrUtils.isNull(departmentid)?"":("\n	AND x.deptid = "+ departmentid))
						+(StrUtils.isNull(sealsid)?"":("\n	AND x.id = "+ sealsid));
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
		}else {
			result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		return result;
	}
	
	/**
	 * @param daterange 类型
	 * @param column 字段名
	 * @return 根据daterange类型返回不同查询语句
	 */
	protected String getQueryExtract(String daterange,String column,String daterange10){
		String resultv = "";
		String dateastart = getDaterange10(daterange, daterange10)[1];
		String dateend = getDaterange10(daterange, daterange10)[2];
		if(daterange.equals("0")||daterange.equals("1")||daterange.equals("2")||daterange.equals("3")||daterange.equals("4")||daterange.equals("5")){
			//月份以及以下查询天
			resultv = "to_char(("+dateastart+")::Date,'YYYYMM')||''||repeat('0',2-CHAR_LENGTH(x.months::TEXT))||x.months = to_char("+column+", 'YYYYMMDD')";
		}else{
			resultv = "extract(year from ("+dateastart+")::Date)||''||repeat('0',2-CHAR_LENGTH(x.months::TEXT))||x.months = to_char("+column+", 'YYYYMM')";
		}
		return resultv;
	}
	/**
	 * @param daterange 类型
	 * @param daterange 字段名
	 * @return 根据daterange类型返回不同查询语句
	 */
	protected String getQueryMonths(String daterange,String daterange10){
		String resultv = "";
		String dateastart = getDaterange10(daterange, daterange10)[1];
		String dateend = getDaterange10(daterange, daterange10)[2];
		if(daterange.equals("0")||daterange.equals("1")||daterange.equals("2")||daterange.equals("3")||daterange.equals("4")||daterange.equals("5")){
		//月份以及以下查询天
			resultv = "to_char(("+dateastart+")::Date,'YYYYMM')||''||repeat('0',2-CHAR_LENGTH(x.months::TEXT))||x.months";
		}else{//月以上查询月
			resultv = "extract(year from ("+dateastart+")::Date)||''||repeat('0',2-CHAR_LENGTH(x.months::TEXT))||x.months";
		}
		return resultv;
	}
	
	/**
	 * @param daterange 类型
	 * @return 根据daterange类型返回不同查询语句
	 */
	protected String getWithRc_months(String daterange,String daterange10){
		String resultv = "";
		String dateastart = getDaterange10(daterange, daterange10)[1];
		String dateend = getDaterange10(daterange, daterange10)[2];
		if(daterange.equals("0")||daterange.equals("1")||daterange.equals("2")||daterange.equals("3")||daterange.equals("4")||daterange.equals("5")){
			//月份以及以下查询天
			resultv = "WITH RECURSIVE rc_months(months) AS("
					+"\n		VALUES(extract (day from"+dateastart+"))"
					+"\n		UNION ALL"
					+"\n		SELECT months+1 FROM rc_months WHERE months< extract (day from"+dateend+")"
					+"\n	)";
		}else{//月以上查询月
			resultv = "WITH RECURSIVE rc_months(months) AS("
					+"\n		VALUES(1)"
					+"\n		UNION ALL"
					+"\n		SELECT months+1 FROM rc_months WHERE months<12"
					+"\n	)";
		}
		return resultv;
	}
	
	private String queryCustomerfollowsCahrts(HttpServletRequest request,String reqStr){//客户跟进次数分析
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String daterange10 =  request.getParameter("daterange10");
		String daterange10ba = getDaterange10(daterange, daterange10)[0];
		String userid = (String)request.getSession().getAttribute("userid");
		String result = "";
		String querySql = "SELECT ARRAY_AGG(months) AS months,ARRAY_AGG(syscorcountdeal) AS syscorcountdeal,ARRAY_AGG(syscorcount) AS syscorcount FROM("
						+"\n"+getWithRc_months(daterange, daterange10)
						+"\n	SELECT "
						+"\n		"+getQueryMonths(daterange,daterange10)+" AS months"
						+"\n		,(SELECT COUNT(1) FROM sys_corporation a WHERE isdelete = FALSE"
						+"\n				AND EXISTS(SELECT 1 FROM fina_jobs t,bus_shipping c WHERE t.isdelete = FALSE AND c.jobid = t.id AND t.customerid = a.id"
						+"\n				AND "+getQueryExtract(daterange, "t.jobdate", daterange10)+"" 
						+(StrUtils.isNull(sealsid)?"":("\n 			AND EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = c.id AND isdelete = FALSE AND userid = ANY(regexp_split_to_array('"+ sealsid+"',',')::BIGINT[]))"))
						+(StrUtils.isNull(departmentid)?"":("\n 			AND EXISTS(SELECT 1 FROM sys_user q,sys_user_assign w WHERE w.isdelete = FALSE AND w.linkid = c.id AND w.userid =q.id AND q.deptid = "+ departmentid+")"))
						+"\n 			AND t.jobdate "+daterange10ba+relevanceSealse(userid)
						+"\n		)) AS syscorcountdeal"
						+"\n			,(SELECT COUNT(1) FROM fina_jobs t,bus_shipping b WHERE t.isdelete = FALSE AND b.jobid = t.id"
						+"\n				AND EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = b.id)"
						+"\n				AND "+getQueryExtract(daterange, "t.jobdate", daterange10)
						+(StrUtils.isNull(sealsid)?"":("\n 			AND EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = b.id AND isdelete = FALSE AND userid = ANY(regexp_split_to_array('"+ sealsid+"',',')::BIGINT[]))"))
						+(StrUtils.isNull(departmentid)?"":("\n 			AND EXISTS(SELECT 1 FROM sys_user q,sys_user_assign w WHERE w.isdelete = FALSE AND w.linkid = b.id AND w.userid =q.id AND q.deptid = "+ departmentid+")"))
						+"\n 			AND jobdate "+daterange10ba+relevanceSealse(userid)
						+") AS syscorcount"
						+"\n	FROM rc_months x"
						+"\n	)t";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryTransactioncycleCahrts(HttpServletRequest request,String reqStr){//员工客户成交周期分析
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String daterange10 =  request.getParameter("daterange10");
		String daterange10ba = getDaterange10(daterange, daterange10)[0];
		String result = "";
		String querySql = "SELECT ARRAY_AGG(months) AS months,ARRAY_AGG(dayavg) AS dayavg,ARRAY_AGG(daysum) AS daysum FROM("
						+"\n"+getWithRc_months(daterange, daterange10)
						+"\n	SELECT "
						+"\n		"+getQueryMonths(daterange,daterange10)+" AS months"
						+"\n, (SELECT AVG((SELECT MIN(jobdate) FROM fina_jobs WHERE customerid = a.id AND "+getQueryExtract(daterange, "jobdate", daterange10)+")::DATE - a.inputtime::DATE)::INT FROM sys_corporation a) AS dayavg"
						+"\n, (SELECT SUM((SELECT MIN(jobdate) FROM fina_jobs WHERE customerid = a.id AND "+getQueryExtract(daterange, "jobdate", daterange10)+")::DATE - a.inputtime::DATE)::INT FROM sys_corporation a) AS daysum"
						+"\n	FROM rc_months x"
						+"\n	)t";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryCustomerfollowstype(HttpServletRequest request,String reqStr){
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		if(StrUtils.isNull(daterange)&&StrUtils.isNull(departmentid)&&StrUtils.isNull(sealsid)){
			//如果没输入搜索条件，直接返回空数据
			String returnv = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+"\"\""+"}";
			return returnv;
		}
		String result = "";
		String querySql = "SELECT servicetypes,count,((count::NUMERIC(18,2)/((SUM(count) OVER() )::NUMERIC(18,2)))*100)::NUMERIC(18,2) FROM("
						+"\n	SELECT (CASE y.servicetype WHEN 'A' THEN '电话' WHEN 'B' THEN 'QQ/MSN' WHEN 'C' THEN 'Email' WHEN 'D' THEN '传真' WHEN 'E' THEN '上门' WHEN 'F' THEN '邮寄' WHEN 'G' THEN '其他' END) AS servicetypes,count(x.id)"
						+"\n	FROM sys_corporation x,sys_corpservice y"
						+"\n	WHERE x.isdelete = FALSE AND y.isdelete = FALSE"
						+"\n	AND x.id = y.customerid"
						+"\n	GROUP BY(CASE y.servicetype WHEN 'A' THEN '电话' WHEN 'B' THEN 'QQ/MSN' WHEN 'C' THEN 'Email' WHEN 'D' THEN '传真' WHEN 'E' THEN '上门' WHEN 'F' THEN '邮寄' WHEN 'G' THEN '其他' END)) t";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
		}else {
			result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		return result;
	}
	
	private String getCustomerprofit(HttpServletRequest request,String reqStr){
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String userid = (String)request.getSession().getAttribute("userid");
		if(StrUtils.isNull(departmentid)&&StrUtils.isNull(sealsid)){
			//如果没输入搜索条件，直接返回空数据
			String returnv = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+"\"\""+"}";
			return returnv;
		}
		String result = "";
		String querySql = "SELECT 	abbr,sales,ar_apusd,sum(ar_apusd) OVER () AS ar_apusdsum,(ar_apusd/sum(ar_apusd) OVER ())::NUMERIC(18,4)*100 AS ar_apusd100"
						+"\nFROM("
						+"\n	SELECT abbr,sales"
						+"\n				,SUM(f_amtto(jobdate,currency,'USD',arsum-apsum))::NUMERIC(18,2) AS ar_apusd"
						+"\n	FROM("
						+"\n	SELECT x.abbr"
						+"\n	,(SELECT namec FROM sys_user WHERE id = x.salesid) AS sales"
						+"\n	,sum(CASE WHEN y.araptype = 'AR' THEN y.amount ELSE 0 END) AS arsum"
						+"\n	,sum(CASE WHEN y.araptype = 'AP' THEN y.amount ELSE 0 END) AS apsum"
						+"\n	,t.jobdate,y.currency"
						+"\n	FROM sys_corporation x,fina_arap y,fina_jobs t"
						+"\n	WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND t.isdelete = FALSE"
						+"\n	AND x.id = t.customerid AND y.jobid = t.id"
						+"\n	AND y.rptype = 'G'"
						+"\n"+relevanceSealse(userid)
						+(StrUtils.isNull(departmentid)?"":("\n	AND t.deptid = ANY(regexp_split_to_array('"+ departmentid+"',',')::BIGINT[])"))
						+(StrUtils.isNull(sealsid)?"":("\n	AND x.salesid = ANY(regexp_split_to_array('"+ sealsid+"',',')::BIGINT[])"))
						+"\n	GROUP BY x.abbr,x.salesid,t.jobdate,y.currency)t"
						+"\n	GROUP BY abbr,sales"
						+"\n) q";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
		}else {
			result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		return result;
	}
	
	private String queryCustomerfollowstypeEcahrts(HttpServletRequest request,String reqStr){
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String result = "";
		String querySql = "SELECT ARRAY_AGG(servicetypes) AS servicetypes,ARRAY_AGG(count) AS count,ARRAY_AGG(numeric) AS numeric FROM("
				+"\n 	SELECT servicetypes,count,((count::NUMERIC(18,2)/((SUM(count) OVER() )::NUMERIC(18,2)))*100)::NUMERIC(18,2) FROM("
				+"\n	SELECT (CASE y.servicetype WHEN 'A' THEN '电话' WHEN 'B' THEN 'QQ/MSN' WHEN 'C' THEN 'Email' WHEN 'D' THEN '传真' WHEN 'E' THEN '上门' WHEN 'F' THEN '邮寄' WHEN 'G' THEN '其他' END) AS servicetypes,count(x.id)"
				+"\n	FROM sys_corporation x,sys_corpservice y"
				+"\n	WHERE x.isdelete = FALSE AND y.isdelete = FALSE"
				+"\n	AND x.id = y.customerid"
				+"\n	GROUP BY(CASE y.servicetype WHEN 'A' THEN '电话' WHEN 'B' THEN 'QQ/MSN' WHEN 'C' THEN 'Email' WHEN 'D' THEN '传真' WHEN 'E' THEN '上门' WHEN 'F' THEN '邮寄' WHEN 'G' THEN '其他' END)) t"
				+"\n)q";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String getCustomerprofitEcahrts(HttpServletRequest request,String reqStr){
		String daterange =  request.getParameter("daterange");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String userid = (String)request.getSession().getAttribute("userid");
		String result = "";
		if(StrUtils.isNull(departmentid)&&StrUtils.isNull(sealsid)){
			//如果没输入搜索条件，直接返回空数据
			String returnv = "[{\"servicetypes\":[],\"count\":[],\"numeric\":[]}]";
			return returnv;
		}
		String querySql = "SELECT ARRAY_AGG(abbr) AS servicetypes,ARRAY_AGG(ar_apusd) AS count,ARRAY_AGG(ar_apusd) AS numeric FROM("
				+"\n 	SELECT 	abbr,sales,ar_apusd,sum(ar_apusd) OVER () AS ar_apusdsum"
				+"\nFROM("
				+"\n	SELECT abbr,sales"
				+"\n				,SUM(f_amtto(jobdate,currency,'USD',arsum-apsum))::NUMERIC(18,2) AS ar_apusd"
				+"\n	FROM("
				+"\n	SELECT x.abbr"
				+"\n	,(SELECT namec FROM sys_user WHERE id = x.salesid) AS sales"
				+"\n	,sum(CASE WHEN y.araptype = 'AR' THEN y.amount ELSE 0 END) AS arsum"
				+"\n	,sum(CASE WHEN y.araptype = 'AP' THEN y.amount ELSE 0 END) AS apsum"
				+"\n	,t.jobdate,y.currency"
				+"\n	FROM sys_corporation x,fina_arap y,fina_jobs t"
				+"\n	WHERE x.isdelete = FALSE AND y.isdelete = FALSE AND t.isdelete = FALSE"
				+"\n	AND x.id = t.customerid AND y.jobid = t.id"
				+"\n	AND y.rptype = 'G'"
				+(StrUtils.isNull(departmentid)?"":("\n	AND t.deptid = ANY(regexp_split_to_array('"+ departmentid+"',',')::BIGINT[])"))
				+(StrUtils.isNull(sealsid)?"":("\n	AND x.salesid = ANY(regexp_split_to_array('"+ sealsid+"',',')::BIGINT[])"))
				+"\n"+relevanceSealse(userid)
				+"\n	GROUP BY x.abbr,x.salesid,t.jobdate,y.currency)t"
				+"\n	GROUP BY abbr,sales"
				+"\n) w ORDER BY ar_apusd desc LIMIT 10" //只查询前10个
				+"\n)q";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String QueryOrderanAlysis(HttpServletRequest request,String reqStr){
		String yserv =  request.getParameter("yserv");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String type = request.getParameter("type");
		String currency = request.getParameter("currency");
		String userid = (String)request.getSession().getAttribute("userid");
		String result = "";
		if(StrUtils.isNull(yserv)&&StrUtils.isNull(departmentid)&&StrUtils.isNull(sealsid)&&StrUtils.isNull(currency)){
			//如果没输入搜索条件，直接返回空数据
			String returnv = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+"\"\""+"}";
			return returnv;
		}
		String querySql = "SELECT * FROM f_crm_order_analysis('year="+yserv+";userid="+userid+";departmentid="+departmentid+";sealsid="+sealsid+";type="+type+";currency="+(StrUtils.isNull(currency)?"":currency)+"')";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(getPGarray_to_json(querySql));
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
		}else {
			result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		return result;
	}
	private String QueryOrderanAlysisEcahrts(HttpServletRequest request,String reqStr){
		String yserv =  request.getParameter("yserv");
		String departmentid =  request.getParameter("department");
		String sealsid =  request.getParameter("seals");
		String type = request.getParameter("type");
		String currency = request.getParameter("currency");
		String result = "";
		String querySql = "SELECT ARRAY[m1]||m2||m3||m4||m5||m6||m7||m8||m9||m10||m11||m12 AS nonthv,title" +
				" FROM f_crm_order_analysis('year="+yserv+";userid=81433600;departmentid="+departmentid+";sealsid="+sealsid+";type="+type+";currency="+currency+"')";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(getPGarray_to_json(querySql));
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String QueryUfmsDefGrid(HttpServletRequest request,String reqStr){
		String gridid =  request.getParameter("gridid");
		String linkid =  request.getParameter("linkid");
		String linktbl =  request.getParameter("linktbl");
		String result = "";
		String querySql = "SELECT * FROM jsonb_populate_recordset(null::type_ufms_def_grid,"
				+"\n(SELECT coldefine FROM ufms_def_grid "
				+"\nWHERE true" 
				+(StrUtils.isNull(gridid)?"":"\nAND gridid = '"+gridid+"' ")
				+(StrUtils.isNumber(linkid)?"":"\nAND linkid = "+linkid)
				+(StrUtils.isNumber(linktbl)?"":"\nAND linktbl = '"+linktbl+"'")
				+"\n)::jsonb)";
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = "{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":"+map.get("json").toString()+"}";
		}else {
			result = "{\"code\":0,\"msg\":\"\",\"count\":0,\"data\":[]}";
		}
		return result;
	}
	
	private String SaveUfmsDefGrid(HttpServletRequest request,String reqStr){
		String jsonvalue =  request.getParameter("jsonvalue");
		String gridid =  request.getParameter("gridid");
		String linkid =  request.getParameter("linkid");
		String linktbl =  request.getParameter("linktbl");
		String result = "{\"success\":true,\"msg\":\"\"}";
		String querySql = "UPDATE ufms_def_grid SET coldefine = '"+jsonvalue.replace("'", "''")+"'"
						+ "\nWHERE true" 
						+(StrUtils.isNull(gridid)?"":"\nAND gridid = '"+gridid+"' ")
						+(StrUtils.isNumber(linkid)?"":"\nAND linkid = "+linkid)
						+(StrUtils.isNumber(linktbl)?"":"\nAND linktbl = '"+linktbl+"'")
						+ "\nAND linktbl = 'sys_corporation'";
		try{
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		}catch(NoRowException e){
		}catch(Exception e){
			result = "{\"success\":false,\"msg\":\"保存失败\"}";
		}
		return result;
	}
}
