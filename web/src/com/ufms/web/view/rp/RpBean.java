package com.ufms.web.view.rp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.alibaba.druid.sql.SQLUtils;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.sql.simple.SimpleDynamicSql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.annotation.ManagedBean;
import com.ufms.base.db.DaoUtil;
import com.ufms.base.web.BaseServlet;

@WebServlet("/rp")
@ManagedBean(name = "pages.module.price.priceBean")
public class RpBean extends BaseServlet {
	
	@Action(method="rp")
	public String rp(HttpServletRequest request){
		String id = request.getParameter("id");
		String querySql = 
			"\nSELECT " +
			"\nactpayrecno " +
			"\nFROM fina_actpayrec a "+
			"\nWHERE isdelete = false " +
			"\nAND id = " + id;
		return DaoUtil.queryForJson(querySql);
	}
	
	@Action(method="rateGrid")
	public String rateGrid(HttpServletRequest request){
		String actpayrecid = request.getParameter("actpayrecid");
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String sqlId = "pages.module.finance.actpayreceditBean.grid.rate";
		SqlMapClientImpl sqlmap = (SqlMapClientImpl) daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient();
		MappedStatement stmt = sqlmap.getMappedStatement(sqlId);
		SimpleDynamicSql staticSql = (SimpleDynamicSql) stmt.getSql();
		
		Map map = new HashMap<String, String>();
		String clauseWhere = "\nAND EXISTS(SELECT 1 FROM dat_currency z WHERE z.code = x.currencyto AND z.isdelete = FALSE AND z.isuseinact = TRUE)";
		map.put("qry", clauseWhere);
		String querySql = staticSql.getSql(null, map);
		
		String formatSql = SQLUtils.formatPGSql(querySql, null );
		formatSql = formatSql.replaceAll(";", "");
		//System.out.println("@@@@"+formatSql);
		
		return DaoUtil.queryForJsonArray(formatSql);
	}
	
	
	@Action(method="rpsum")
	public String rpsum(HttpServletRequest request){
		String actpayrecid = request.getParameter("actpayrecid");
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String sqlId = "pages.module.finance.actpayreceditBean.grid.rpsum";
		SqlMapClientImpl sqlmap = (SqlMapClientImpl) daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient();
		MappedStatement stmt = sqlmap.getMappedStatement(sqlId);
		SimpleDynamicSql staticSql = (SimpleDynamicSql) stmt.getSql();
		
		Map map = new HashMap<String, String>();
		map.put("actpayrecid", actpayrecid);
		String qry = StrUtils.getMapVal(map, "qry");
		map.put("qry", qry);
		
		String querySql = staticSql.getSql(null, map);
		
		String formatSql = SQLUtils.formatPGSql(querySql, null );
		//System.out.println("@@@@"+formatSql);
		
		return DaoUtil.queryForJsonArray(querySql);
	}
	
	
	@Action(method="save")
	public String save(HttpServletRequest request){
		String actpayrecid = request.getParameter("actpayrecid");
		try {
			InputStream is = request.getInputStream();
			String json = IOUtils.toString(is, "utf-8");
	        //System.out.println("saveObject.json:......"+json.trim());
			
			String maindbText = "";
			String dtldbText = "";
			String rpsumdbText = json;
			String userid = "-100";
			String sql = 
				"SELECT f_fina_actpayrec('actpayrecid="+actpayrecid+";'"+
				",'"+maindbText+"'"+
				",'"+dtldbText+"'"+
				",'"+rpsumdbText+"'"+
				",'"+userid+"') AS ret";
			//System.out.println("sql:"+sql);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
}

