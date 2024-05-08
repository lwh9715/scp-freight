package com.ufms.web.view.rp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.sql.simple.SimpleDynamicSql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.db.DaoUtil;
import com.ufms.base.web.BaseServlet;
import com.ufms.base.web.GridDataProvider;

@WebServlet("/rplist")
@ManagedBean(name = "pages.module.RpListBean")
public class RpListBean extends BaseServlet {
	
	@Action(method="leftGrid")
	public String rpsum(HttpServletRequest request){
		String userid = request.getParameter("userid");
		String qryFilter = request.getParameter("qry");
		String str = "";
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("page", request.getParameter("page"));
		args.put("limit", request.getParameter("limit"));
		//args.put("order", request.getParameter("order"));
		args.put("userid", userid);
		if(!StrUtils.isNull(qryFilter)){
			str = CommonUtil.decodeUnicode(qryFilter);
		}
		args.put("qryFilter", str);
		
		
		Map<String, Object> qryMap = new HashMap<String, Object>();
		
		GridDataProvider gridDataProvider =  getGridDataProvider(args , qryMap);
		String ret  = gridDataProvider.getGridJsonData(); 
		return ret;
	}
	
	protected GridDataProvider getGridDataProvider(Map<String, Object> args , Map<String, Object> qryMap){
		GridDataProvider gridDataProvider =  new GridDataProvider(args , qryMap) {
			@Override
			public String getElements() {
				StringBuilder sb = new StringBuilder();
				sb.append("\nSELECT ");
				sb.append("\n(CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isfinish2 = FALSE AND x.araptype = 'AR') THEN 'R' ELSE '' END) || (CASE WHEN EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid  AND x.isfinish2 = FALSE AND x.araptype = 'AP') THEN 'P' ELSE '' END) AS araptype");
				sb.append("\n,a.*  ");
				sb.append("\nFROM _sys_corporation a  ");
				sb.append("\nWHERE isdelete = false  ");
				sb.append("\nAND EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isdelete = FALSE)  ");
				sb.append("\nAND (a.code ILIKE '%$qryFilter$%' OR a.namec ILIKE '%$qryFilter$%' OR a.namee ILIKE '%$qryFilter$%' ) ");
				if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))){
					sb.append("\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND x.corpid = ANY(SELECT DISTINCT y.corpid FROM sys_user_corplink y WHERE y.ischoose = TRUE AND y.userid ="+81433600+")");
					//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
				}
				sb.append("\nLIMIT $limit$ OFFSET $offset$");
				String querySql = sb.toString();
				querySql = processSqlArgs(querySql);
				//System.out.println(querySql);
				return DaoUtil.queryForJsonArray(querySql);
			}
			
			@Override
			public String getTotalCount() {
				StringBuilder sb = new StringBuilder();
				sb.append("\nSELECT ");
				sb.append("\n COUNT(*) AS count");
				sb.append("\nFROM _sys_corporation a  ");
				sb.append("\nWHERE isdelete = false  ");
				sb.append("\nAND EXISTS (SELECT 1 FROM fina_arap x WHERE a.id = x.customerid AND x.isdelete = FALSE)  ");
				sb.append("\nAND (a.code ILIKE '%$qryFilter$%' OR a.namec ILIKE '%$qryFilter$%' OR a.namee ILIKE '%$qryFilter$%' ) ");
				if("N".equals(ConfigUtils.findSysCfgVal("sys_cfg_branch_share_arapcom"))){
					sb.append("\n AND EXISTS (SELECT 1 FROM fina_arap x WHERE x.isdelete = FALSE AND x.customerid = a.id AND x.corpid = ANY(SELECT DISTINCT y.corpid FROM sys_user_corplink y WHERE y.ischoose = TRUE AND y.userid ="+81433600+")");
					//分公司过滤 Neo 20180608 取系统设置中是否共享分公司结算单位，如果为空或是，不加这个过滤条件，如果为否，加这个条件
				}
				String querySql = sb.toString();
				querySql = processSqlArgs(querySql);
				//System.out.println(querySql);
				Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
				return StrUtils.getMapVal(m, "count");
			}
		};
		return gridDataProvider;
	}
	
	
	@Action(method="rightGrid")
	public String rightGrid(HttpServletRequest request){
		String actpayrecid = request.getParameter("actpayrecid");
		String clientid = request.getParameter("clientid");
		//收付款单号
		String actpayrecno = request.getParameter("actpayrecno");
		String clientcode = request.getParameter("clientcode");
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String sqlId = "pages.module.finance.actpayrecBean.grid.page";
		SqlMapClientImpl sqlmap = (SqlMapClientImpl) daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient();
		MappedStatement stmt = sqlmap.getMappedStatement(sqlId);
		SimpleDynamicSql staticSql = (SimpleDynamicSql) stmt.getSql();
		
		Map map = new HashMap<String, String>();
		map.put("actpayrecid", actpayrecid);
		map.put("clientid", clientid);
		map.put("corpfilter", "");
		map.put("setDays", "");
		map.put("filter", "1=1");
		String qry ="\n1=1 AND clientid = " + clientid;
		
		
		if(!StrUtils.isNull(actpayrecno)) {
			qry += " and  actpayrecno = '"+actpayrecno+"'";
		}
		if(!StrUtils.isNull(clientcode)) {
			qry += " and  clientcode like '%"+clientcode+"%'";
		}
		map.put("qry", qry);
		
		
		String page = request.getParameter("page");
		String limit = request.getParameter("limit");
		String offset = "0";
		try {
			if(StrUtils.isNull(limit))limit = "100";
			offset = (Integer.parseInt(page)*Integer.parseInt(limit) - Integer.parseInt(limit))+"";
		} catch (Exception e) {
			offset = "0";
		}
		map.put("limit", limit);
		map.put("start", offset);
		
		String querySql = staticSql.getSql(null, map);
		//System.out.println(querySql);
		String elements = DaoUtil.queryForJsonArray(querySql);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", 0);
		jsonObject.put("msg", "");
		jsonObject.put("count", 100);
		jsonObject.put("data", elements);
		
		
		return jsonObject.toString();
	}
	
}

