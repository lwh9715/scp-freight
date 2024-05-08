package com.scp.servlet;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.sql.simple.SimpleDynamicSql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.module.finance.ActPayRecEditNewBean;
import com.scp.view.module.finance.ArapEditBean;

public class JqGridHandler {
	@Resource
	public ServiceContext serviceContext;
	
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public String handle(String action, HttpServletRequest request){
		String reqStr = "";//条件
//		try {
//			reqStr = URLDecoder.decode(request.getParameter("q"), "UTF-8");
//		} catch (Exception e1) {
//			//e1.printStackTrace();
//		}
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			if("getRPFee".equals(action)) {
				result = this.queryRPFee(request,reqStr);
			}
			if("getARAP".equals(action)) {
				result = this.queryARAP(request,reqStr);
			}
		} catch (Exception e) {
			result = "{\"results\":" + "error" + "}";
			e.printStackTrace();
		}
		return result;
	}
	
	private String queryARAP(HttpServletRequest request,String reqStr){
		String type =  request.getParameter("type");
		
		String sidx = request.getParameter("sidx");//来获得排序的列名，
		String sord = request.getParameter("sord");//来获得排序方式，
		
		//System.out.println(sidx);
		//System.out.println(sord);
	
		String result = "";
		
		ArapEditBean actPayRecEditBean = (ArapEditBean)AppUtils.getBeanFromSpringIoc("pages.module.finance.arapeditBean");
		
		String sqlId = "pages.module.finance.arapeditBean.grid.page";
		SqlMapClientImpl sqlmap = (SqlMapClientImpl) daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient();
		MappedStatement stmt = sqlmap.getMappedStatement(sqlId);
		SimpleDynamicSql staticSql = (SimpleDynamicSql) stmt.getSql();
		String querySql = staticSql.getSql(null, actPayRecEditBean.getQryClauseWhere(actPayRecEditBean.qryMap));
		
		//System.out.println(querySql);
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		
//		String querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (SELECT    *    ,amountactrecpay AS amountactrecpay0    ,(CASE WHEN amountactrecpay = 0 THEN TRUE ELSE FALSE END) AS isfinish   FROM    (SELECT     actpayrecid     ,t.id ||'-0' AS dtlid     ,x.id AS arapid     ,x.customerid     ,x.araptype     ,j.id AS jobid     ,j.nos AS jobno     ,x.feeitemdec     ,x.arapdate     ,x.currency     ,(CASE WHEN feetype = 'A' THEN x.amtcost ELSE (x.amount - COALESCE(x.amtcost,0)) END) AS amount     ,(CASE WHEN (SELECT COUNT(1) > 0 FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = t.arapid AND xx.amountwf > 0) THEN 0       ELSE        (CASE WHEN feetype = 'A' THEN x.amtcost - (SELECT sum(xx.amountwf) FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = t.arapid AND xx.feetype = 'A')          ELSE (x.amount - (SELECT sum(xx.amountwf) FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = t.arapid AND COALESCE(xx.feetype,'') != 'A') )        END)       END) AS amountactrecpay     ,currencyto     ,t.xtype AS xtype     ,xrate     ,t.amountrp AS amountrpflag     ,rpdate     ,amountwf     ,billno     ,invoiceno     ,0 AS amountremaindabs     ,x.descinfo As arapdesc     ,x.remarks AS arapremarks     ,arapbranch     ,COALESCE((SELECT xx.corpidlink FROM sys_corporation xx WHERE xx.id = x.corpid and xx.iscustomer = false AND xx.corpidlink IS NOT NULL),x.corpid) AS arapcorpid     ,(SELECT zz.payplace FROM fina_arap zz WHERE zz.id = x.id) AS payplace      ,x.ppcc     ,x.piece     ,x.price     ,x.customecode     ,j.sales     ,j.vessel AS ves     ,j.voyage AS voy     ,j.etd AS etd     ,j.eta AS eta     ,j.bargeetd     ,j.ispayagree     ,(SELECT zz.abbr FROM sys_corporation zz WHERE zz.id = j.customerid) AS jobscustomerabbr     ,j.jobdate AS jobdate     ,COALESCE((SELECT (CASE WHEN COALESCE(zz.blno,'')='' THEN NULL ELSE zz.blno END) FROM fina_arap zz WHERE zz.id = x.id),j.mblno) AS mblno     ,j.orderno AS orderno     ,j.sono AS sono     ,(SELECT string_agg(zz.cntno,',') FROM bus_ship_container zz WHERE zz.jobid = x.jobid AND zz.isdelete = FALSE AND zz.parentid IS NULL) AS cntnos     ,100 AS odno     ,j.isconfirm_bus     ,COALESCE(x.amtcost,0) AS amtcost     ,x.inputer AS arapinputer     ,j.refno     ,(CASE WHEN t.feetype = 'A' THEN TRUE ELSE FALSE END) AS isamtcost    FROM fina_actpayrecdtl t , _fina_arap x , _fina_jobs_rp j    WHERE 1=1 AND x.id = t.arapid AND x.jobid = j.id     AND (CASE WHEN -1 = -1 THEN FALSE ELSE (t.actpayrecid = -1) END)     AND t.isdelete = false     AND x.isdelete = false     AND j.isdelete = false                UNION ALL        SELECT     0 AS actpayrecid     ,a.id ||'-1' AS dtlid     ,a.id AS arapid     ,customerid     ,araptype     ,jobid     ,jobno     ,feeitemdec     ,arapdate     ,currency     ,(a.amount - COALESCE(a.amtcost,0)) AS amount          ,COALESCE((a.amount - COALESCE(a.amtcost,0) -COALESCE((SELECT sum(xx.amountwf) FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = a.id AND COALESCE(xx.feetype,'') != 'A'),0)      ),0) AS amountactrecpay     ,a.currency AS currencyto     ,'' AS xtype     ,0 AS xrate     ,0 AS amountrp     ,NULL AS rpdate     ,0 AS amountwf     ,billno     ,invoiceno     ,amountremaind2 AS amountremaindabs     ,a.descinfo AS arapdesc     ,a.remarks AS arapremarks     ,arapbranch     ,COALESCE((SELECT x.corpidlink FROM sys_corporation x WHERE x.id = a.corpid and x.iscustomer = false AND x.corpidlink IS NOT NULL),a.corpid) AS arapcorpid     ,(SELECT zz.payplace FROM fina_arap zz WHERE zz.id = a.id) AS payplace      ,ppcc     ,piece     ,price     ,a.customecode     ,(SELECT j.sales FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS sales     ,(SELECT j.vessel FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS ves     ,(SELECT j.voyage FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS voy     ,(SELECT j.etd FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS etd     ,(SELECT j.eta FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS eta     ,(SELECT j.bargeetd FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS bargeetd     ,(SELECT j.ispayagree FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS ispayagree     ,(SELECT zz.abbr FROM sys_corporation zz , fina_jobs j WHERE j.id = a.jobid AND j.isdelete = FALSE AND zz.id = j.customerid) AS jobscustomerabbr     ,(SELECT j.jobdate FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS jobdate     ,COALESCE((SELECT (CASE WHEN COALESCE(zz.blno,'')='' THEN NULL ELSE zz.blno END) FROM fina_arap zz WHERE zz.id = a.id),(SELECT mblno FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE)) AS mblno     ,(SELECT orderno FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE) AS orderno     ,(SELECT sono FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE) AS sono     ,(SELECT string_agg(zz.cntno,',') FROM bus_ship_container zz WHERE zz.jobid = a.jobid AND zz.isdelete = FALSE AND zz.parentid IS NULL) AS cntnos     ,(CASE WHEN EXISTS(SELECT 1 FROM fina_actpayrecdtl x,fina_arap y where x.actpayrecid = -1 AND x.arapid = y.id AND x.isdelete = false AND y.jobid = a.jobid AND y.isdelete = false) THEN 100 ELSE 0 END) AS odno     ,(SELECT j.isconfirm_bus FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS isconfirm_bus      ,COALESCE(a.amtcost,0) AS amtcost     ,a.inputer AS arapinputer      ,(SELECT j.refno FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS refno     ,false AS isamtcost    FROM _fina_arap a    WHERE a.isfinish2 = FALSE     AND a.rptype != 'S'     AND a.rptype != 'O'     AND (a.amountremaind2 - COALESCE(a.amtcost,0)) != 0     AND a.isdelete = false     AND (a.customerid = 75062199 OR EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idto = 75062199 AND x.idfm = a.customerid) OR EXISTS(SELECT 1 FROM fina_actpayrec_clients x WHERE actpayrecid = -1 AND x.clientid = a.customerid AND x.isdelete = FALSE))     AND a.jobno IS NOT NULL      AND a.jobno != ''     AND NOT EXISTS(SELECT 1 FROM fina_actpayrecdtl x where x.actpayrecid = -1 AND x.arapid = a.id AND x.isdelete = false AND a.isfinish2 = true)   "+            
//		" AND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = 81433600)              UNION ALL        SELECT     0 AS actpayrecid     ,a.id ||'-2' AS dtlid     ,a.id AS arapid     ,customerid     ,araptype     ,jobid     ,jobno     ,feeitemdec     ,arapdate     ,currency     ,a.amtcost AS amount     ,COALESCE((a.amtcost -COALESCE((SELECT sum(xx.amountwf) FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = a.id AND COALESCE(xx.feetype,'') = 'A'),0)      ),0) AS amountactrecpay     ,a.currency AS currencyto     ,'' AS xtype     ,0 AS xrate     ,0 AS amountrp     ,NULL AS rpdate     ,0 AS amountwf     ,billno     ,invoiceno     ,amountremaind2 AS amountremaindabs     ,a.descinfo AS arapdesc     ,a.remarks AS arapremarks     ,arapbranch     ,COALESCE((SELECT x.corpidlink FROM sys_corporation x WHERE x.id = a.corpid and x.iscustomer = false AND x.corpidlink IS NOT NULL),a.corpid) AS arapcorpid     ,(SELECT zz.payplace FROM fina_arap zz WHERE zz.id = a.id) AS payplace      ,ppcc     ,piece     ,price     ,a.customecode     ,(SELECT j.sales FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS sales     ,(SELECT j.vessel FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS ves     ,(SELECT j.voyage FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS voy     ,(SELECT j.etd FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS etd     ,(SELECT j.eta FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS eta     ,(SELECT j.bargeetd FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS bargeetd     ,(SELECT j.ispayagree FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS ispayagree     ,(SELECT zz.abbr FROM sys_corporation zz , fina_jobs j WHERE j.id = a.jobid AND j.isdelete = FALSE AND zz.id = j.customerid) AS jobscustomerabbr     ,(SELECT j.jobdate FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS jobdate     ,COALESCE((SELECT (CASE WHEN COALESCE(zz.blno,'')='' THEN NULL ELSE zz.blno END) FROM fina_arap zz WHERE zz.id = a.id),(SELECT mblno FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE)) AS mblno     ,(SELECT orderno FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE) AS orderno     ,(SELECT sono FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE) AS sono     ,(SELECT string_agg(zz.cntno,',') FROM bus_ship_container zz WHERE zz.jobid = a.jobid AND zz.isdelete = FALSE AND zz.parentid IS NULL) AS cntnos     ,(CASE WHEN EXISTS(SELECT 1 FROM fina_actpayrecdtl x,fina_arap y where x.actpayrecid = -1 AND x.arapid = y.id AND x.isdelete = false AND y.jobid = a.jobid AND y.isdelete = false) THEN 100 ELSE 0 END) AS odno     ,(SELECT j.isconfirm_bus FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS isconfirm_bus      ,COALESCE(a.amtcost,0) AS amtcost     ,a.inputer AS arapinputer      ,(SELECT j.refno FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS refno     ,true AS isamtcost    FROM _fina_arap a    WHERE a.isfinish2 = FALSE     AND COALESCE(a.amtcost,0) != 0      AND a.rptype != 'S'     AND a.rptype != 'O'     AND a.amountremaind2 != 0     AND a.isdelete = false     AND (a.customerid = 75062199 OR EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idto = 75062199 AND x.idfm = a.customerid) OR EXISTS(SELECT 1 FROM fina_actpayrec_clients x WHERE actpayrecid = -1 AND x.clientid = a.customerid AND x.isdelete = FALSE))     AND a.jobno IS NOT NULL      AND a.jobno != ''     AND NOT EXISTS(SELECT 1 FROM fina_actpayrecdtl x where x.actpayrecid = -1 AND x.arapid = a.id AND x.isdelete = false AND a.isfinish2 = true) "+              
//		" AND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = 81433600)         ) As a   WHERE ( "+
//		"1=1  OR actpayrecid != 0)       ORDER BY odno DESC,araptype DESC,jobno,currency,arapid LIMIT 100)   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
	private String queryRPFee(HttpServletRequest request,String reqStr){
		String type =  request.getParameter("type");
		
		String sidx = request.getParameter("sidx");//排序条件
		String sord = request.getParameter("sord");//最后个字段排序方式
		
		
		
		//System.out.println("sidx - sord:......................"+ sidx + " - " + sord);
	
		String result = "";
		
		ActPayRecEditNewBean actPayRecEditBean = (ActPayRecEditNewBean)AppUtils.getBeanFromSpringIoc("pages.module.finance.actpayreceditnewBean");
		
		if("cs".equals(type)){
			String customerid =  request.getParameter("customerid");
			String actpayrecid =  request.getParameter("actpayrecid");
			//System.out.println("cs:......................customerid:"+ customerid );
			actPayRecEditBean.customerid = customerid;
			actPayRecEditBean.actpayrecid = Long.parseLong(actpayrecid);
			actPayRecEditBean.ishideallarap = false;
			actPayRecEditBean.userid = "81433600";
			actPayRecEditBean.corpid = "100";
		}
		
		String sqlId = "pages.module.finance.actpayreceditBean.grid.page";
		SqlMapClientImpl sqlmap = (SqlMapClientImpl) daoIbatisTemplate.getSqlMapClientTemplate().getSqlMapClient();
		MappedStatement stmt = sqlmap.getMappedStatement(sqlId);
		SimpleDynamicSql staticSql = (SimpleDynamicSql) stmt.getSql();
		
		Map clauseMap = actPayRecEditBean.getQryClauseWhere();
		clauseMap.put("orderby", "ORDER BY odno DESC,araptype DESC,jobno,currency,arapid");
		String orderColumn = "";//ConfigUtils.findUserCfgVal("rpgrid.order", Long.valueOf(actPayRecEditBean.userid));
		if(!StrUtils.isNull(orderColumn)){
			clauseMap.put("orderby", "ORDER BY " + orderColumn);
		}
		
		if(!"id".equals(sidx)){
			//clauseMap.put("orderby", "ORDER BY " + sidx + " " + sord);
		}
		
		String querySql = staticSql.getSql(null, clauseMap);
		
		//System.out.println(SQLUtils.formatPGSql(querySql, null));
		querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (" + querySql + ")   AS T ";
		
//		String querySql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (SELECT    *    ,amountactrecpay AS amountactrecpay0    ,(CASE WHEN amountactrecpay = 0 THEN TRUE ELSE FALSE END) AS isfinish   FROM    (SELECT     actpayrecid     ,t.id ||'-0' AS dtlid     ,x.id AS arapid     ,x.customerid     ,x.araptype     ,j.id AS jobid     ,j.nos AS jobno     ,x.feeitemdec     ,x.arapdate     ,x.currency     ,(CASE WHEN feetype = 'A' THEN x.amtcost ELSE (x.amount - COALESCE(x.amtcost,0)) END) AS amount     ,(CASE WHEN (SELECT COUNT(1) > 0 FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = t.arapid AND xx.amountwf > 0) THEN 0       ELSE        (CASE WHEN feetype = 'A' THEN x.amtcost - (SELECT sum(xx.amountwf) FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = t.arapid AND xx.feetype = 'A')          ELSE (x.amount - (SELECT sum(xx.amountwf) FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = t.arapid AND COALESCE(xx.feetype,'') != 'A') )        END)       END) AS amountactrecpay     ,currencyto     ,t.xtype AS xtype     ,xrate     ,t.amountrp AS amountrpflag     ,rpdate     ,amountwf     ,billno     ,invoiceno     ,0 AS amountremaindabs     ,x.descinfo As arapdesc     ,x.remarks AS arapremarks     ,arapbranch     ,COALESCE((SELECT xx.corpidlink FROM sys_corporation xx WHERE xx.id = x.corpid and xx.iscustomer = false AND xx.corpidlink IS NOT NULL),x.corpid) AS arapcorpid     ,(SELECT zz.payplace FROM fina_arap zz WHERE zz.id = x.id) AS payplace      ,x.ppcc     ,x.piece     ,x.price     ,x.customecode     ,j.sales     ,j.vessel AS ves     ,j.voyage AS voy     ,j.etd AS etd     ,j.eta AS eta     ,j.bargeetd     ,j.ispayagree     ,(SELECT zz.abbr FROM sys_corporation zz WHERE zz.id = j.customerid) AS jobscustomerabbr     ,j.jobdate AS jobdate     ,COALESCE((SELECT (CASE WHEN COALESCE(zz.blno,'')='' THEN NULL ELSE zz.blno END) FROM fina_arap zz WHERE zz.id = x.id),j.mblno) AS mblno     ,j.orderno AS orderno     ,j.sono AS sono     ,(SELECT string_agg(zz.cntno,',') FROM bus_ship_container zz WHERE zz.jobid = x.jobid AND zz.isdelete = FALSE AND zz.parentid IS NULL) AS cntnos     ,100 AS odno     ,j.isconfirm_bus     ,COALESCE(x.amtcost,0) AS amtcost     ,x.inputer AS arapinputer     ,j.refno     ,(CASE WHEN t.feetype = 'A' THEN TRUE ELSE FALSE END) AS isamtcost    FROM fina_actpayrecdtl t , _fina_arap x , _fina_jobs_rp j    WHERE 1=1 AND x.id = t.arapid AND x.jobid = j.id     AND (CASE WHEN -1 = -1 THEN FALSE ELSE (t.actpayrecid = -1) END)     AND t.isdelete = false     AND x.isdelete = false     AND j.isdelete = false                UNION ALL        SELECT     0 AS actpayrecid     ,a.id ||'-1' AS dtlid     ,a.id AS arapid     ,customerid     ,araptype     ,jobid     ,jobno     ,feeitemdec     ,arapdate     ,currency     ,(a.amount - COALESCE(a.amtcost,0)) AS amount          ,COALESCE((a.amount - COALESCE(a.amtcost,0) -COALESCE((SELECT sum(xx.amountwf) FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = a.id AND COALESCE(xx.feetype,'') != 'A'),0)      ),0) AS amountactrecpay     ,a.currency AS currencyto     ,'' AS xtype     ,0 AS xrate     ,0 AS amountrp     ,NULL AS rpdate     ,0 AS amountwf     ,billno     ,invoiceno     ,amountremaind2 AS amountremaindabs     ,a.descinfo AS arapdesc     ,a.remarks AS arapremarks     ,arapbranch     ,COALESCE((SELECT x.corpidlink FROM sys_corporation x WHERE x.id = a.corpid and x.iscustomer = false AND x.corpidlink IS NOT NULL),a.corpid) AS arapcorpid     ,(SELECT zz.payplace FROM fina_arap zz WHERE zz.id = a.id) AS payplace      ,ppcc     ,piece     ,price     ,a.customecode     ,(SELECT j.sales FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS sales     ,(SELECT j.vessel FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS ves     ,(SELECT j.voyage FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS voy     ,(SELECT j.etd FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS etd     ,(SELECT j.eta FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS eta     ,(SELECT j.bargeetd FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS bargeetd     ,(SELECT j.ispayagree FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS ispayagree     ,(SELECT zz.abbr FROM sys_corporation zz , fina_jobs j WHERE j.id = a.jobid AND j.isdelete = FALSE AND zz.id = j.customerid) AS jobscustomerabbr     ,(SELECT j.jobdate FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS jobdate     ,COALESCE((SELECT (CASE WHEN COALESCE(zz.blno,'')='' THEN NULL ELSE zz.blno END) FROM fina_arap zz WHERE zz.id = a.id),(SELECT mblno FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE)) AS mblno     ,(SELECT orderno FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE) AS orderno     ,(SELECT sono FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE) AS sono     ,(SELECT string_agg(zz.cntno,',') FROM bus_ship_container zz WHERE zz.jobid = a.jobid AND zz.isdelete = FALSE AND zz.parentid IS NULL) AS cntnos     ,(CASE WHEN EXISTS(SELECT 1 FROM fina_actpayrecdtl x,fina_arap y where x.actpayrecid = -1 AND x.arapid = y.id AND x.isdelete = false AND y.jobid = a.jobid AND y.isdelete = false) THEN 100 ELSE 0 END) AS odno     ,(SELECT j.isconfirm_bus FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS isconfirm_bus      ,COALESCE(a.amtcost,0) AS amtcost     ,a.inputer AS arapinputer      ,(SELECT j.refno FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS refno     ,false AS isamtcost    FROM _fina_arap a    WHERE a.isfinish2 = FALSE     AND a.rptype != 'S'     AND a.rptype != 'O'     AND (a.amountremaind2 - COALESCE(a.amtcost,0)) != 0     AND a.isdelete = false     AND (a.customerid = 75062199 OR EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idto = 75062199 AND x.idfm = a.customerid) OR EXISTS(SELECT 1 FROM fina_actpayrec_clients x WHERE actpayrecid = -1 AND x.clientid = a.customerid AND x.isdelete = FALSE))     AND a.jobno IS NOT NULL      AND a.jobno != ''     AND NOT EXISTS(SELECT 1 FROM fina_actpayrecdtl x where x.actpayrecid = -1 AND x.arapid = a.id AND x.isdelete = false AND a.isfinish2 = true)   "+            
//		" AND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = 81433600)              UNION ALL        SELECT     0 AS actpayrecid     ,a.id ||'-2' AS dtlid     ,a.id AS arapid     ,customerid     ,araptype     ,jobid     ,jobno     ,feeitemdec     ,arapdate     ,currency     ,a.amtcost AS amount     ,COALESCE((a.amtcost -COALESCE((SELECT sum(xx.amountwf) FROM fina_actpayrecdtl xx where xx.isdelete = false AND xx.arapid = a.id AND COALESCE(xx.feetype,'') = 'A'),0)      ),0) AS amountactrecpay     ,a.currency AS currencyto     ,'' AS xtype     ,0 AS xrate     ,0 AS amountrp     ,NULL AS rpdate     ,0 AS amountwf     ,billno     ,invoiceno     ,amountremaind2 AS amountremaindabs     ,a.descinfo AS arapdesc     ,a.remarks AS arapremarks     ,arapbranch     ,COALESCE((SELECT x.corpidlink FROM sys_corporation x WHERE x.id = a.corpid and x.iscustomer = false AND x.corpidlink IS NOT NULL),a.corpid) AS arapcorpid     ,(SELECT zz.payplace FROM fina_arap zz WHERE zz.id = a.id) AS payplace      ,ppcc     ,piece     ,price     ,a.customecode     ,(SELECT j.sales FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS sales     ,(SELECT j.vessel FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS ves     ,(SELECT j.voyage FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS voy     ,(SELECT j.etd FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS etd     ,(SELECT j.eta FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS eta     ,(SELECT j.bargeetd FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS bargeetd     ,(SELECT j.ispayagree FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS ispayagree     ,(SELECT zz.abbr FROM sys_corporation zz , fina_jobs j WHERE j.id = a.jobid AND j.isdelete = FALSE AND zz.id = j.customerid) AS jobscustomerabbr     ,(SELECT j.jobdate FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS jobdate     ,COALESCE((SELECT (CASE WHEN COALESCE(zz.blno,'')='' THEN NULL ELSE zz.blno END) FROM fina_arap zz WHERE zz.id = a.id),(SELECT mblno FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE)) AS mblno     ,(SELECT orderno FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE) AS orderno     ,(SELECT sono FROM _fina_jobs_rp b WHERE b.id = a.jobid AND b.isdelete = FALSE) AS sono     ,(SELECT string_agg(zz.cntno,',') FROM bus_ship_container zz WHERE zz.jobid = a.jobid AND zz.isdelete = FALSE AND zz.parentid IS NULL) AS cntnos     ,(CASE WHEN EXISTS(SELECT 1 FROM fina_actpayrecdtl x,fina_arap y where x.actpayrecid = -1 AND x.arapid = y.id AND x.isdelete = false AND y.jobid = a.jobid AND y.isdelete = false) THEN 100 ELSE 0 END) AS odno     ,(SELECT j.isconfirm_bus FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS isconfirm_bus      ,COALESCE(a.amtcost,0) AS amtcost     ,a.inputer AS arapinputer      ,(SELECT j.refno FROM _fina_jobs_rp j WHERE j.id = a.jobid AND j.isdelete = FALSE) AS refno     ,true AS isamtcost    FROM _fina_arap a    WHERE a.isfinish2 = FALSE     AND COALESCE(a.amtcost,0) != 0      AND a.rptype != 'S'     AND a.rptype != 'O'     AND a.amountremaind2 != 0     AND a.isdelete = false     AND (a.customerid = 75062199 OR EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idto = 75062199 AND x.idfm = a.customerid) OR EXISTS(SELECT 1 FROM fina_actpayrec_clients x WHERE actpayrecid = -1 AND x.clientid = a.customerid AND x.isdelete = FALSE))     AND a.jobno IS NOT NULL      AND a.jobno != ''     AND NOT EXISTS(SELECT 1 FROM fina_actpayrecdtl x where x.actpayrecid = -1 AND x.arapid = a.id AND x.isdelete = false AND a.isfinish2 = true) "+              
//		" AND a.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND x.userid = 81433600)         ) As a   WHERE ( "+
//		"1=1  OR actpayrecid != 0)       ORDER BY odno DESC,araptype DESC,jobno,currency,arapid LIMIT 100)   AS T ";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
		Map map = list.get(0);
		if(map != null && map.containsKey("json")&&map.get("json")!=null){
			result = map.get("json").toString();
		}else {
			result = "{\"label\": \"\"}";
		}
		return result;
	}
	
}
