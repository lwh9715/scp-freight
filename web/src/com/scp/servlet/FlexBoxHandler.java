package com.scp.servlet;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.scp.base.ApplicationConf;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;

public class FlexBoxHandler {
	
	@Resource
	public ApplicationConf applicationConf;
	
	
	@Resource
	public ServiceContext serviceContext;
	
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public String handle(String action, HttpServletRequest request){
		String pageIndex = request.getParameter("p");//页数  
		String pageSize = request.getParameter("s");//每页条数 
		String reqStr = "";//条件
		try {
			// 再将这些字节用 UTF-8 解码
			reqStr = new String(request.getParameter("q").getBytes("ISO-8859-1"), "UTF-8");
			reqStr = reqStr.replaceAll("'", "''");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String result = "";
		try {
			serviceContext = (ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
			daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			applicationConf  = (ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf");
			if("pol".equals(action)) {
				result = this.queryPol(request,pageIndex,pageSize,reqStr);
			}else if("pod".equals(action)) {
				result = this.queryPod(request,pageIndex,pageSize,reqStr);
			}else if("poa".equals(action)) {
				result = this.queryPoa(request,pageIndex,pageSize,reqStr);
			}else if("allwarehouse".equals(action)) {
				result = this.queryAllwarehouse(request,pageIndex,pageSize,reqStr);
			}else if("lines".equals(action)) {
				result = this.queryLines(request,pageIndex,pageSize,reqStr);
			}else if("pdd".equals(action)) {
				result = this.queryPdd(request,pageIndex,pageSize,reqStr);
			}else if("pot".equals(action)) {
				result = this.queryPot(request,pageIndex,pageSize,reqStr);
			}else if("destination".equals(action)) {
				result = this.queryDestination(request,pageIndex,pageSize,reqStr);
			}else if("customer".equals(action)) {
				result = this.queryCustomer(request,pageIndex,pageSize,reqStr);
			}else if("delivercompany".equals(action)) {
				result = this.queryDelivercompany(request,pageIndex,pageSize,reqStr);
			}else if("busshippingCustomer".equals(action)) {
				result = this.querybusshippingCustomer(request,pageIndex,pageSize,reqStr);
			}else if("truckfactory".equals(action)) {
				result = this.queryFactory(request,pageIndex,pageSize,reqStr);
			}else if("busbillcustomer".equals(action)) {
				result = this.queryBusbillCustomer(request,pageIndex,pageSize,reqStr);
			}else if("customerFilterCustomer".equals(action)) {
				result = this.queryCustomerFilterCustomer(request,pageIndex,pageSize,reqStr);
			}else if("feei".equals(action)) {
				result = this.queryFeei(request,pageIndex,pageSize,reqStr);
			}else if("currency".equals(action)) {
				result = this.queryCurrency(request,pageIndex,pageSize,reqStr);
			}else if("unit".equals(action)) {
				result = this.queryUnit(request,pageIndex,pageSize,reqStr);
			}else if("cscode".equals(action)) {
				result = this.queryCscode(request,pageIndex,pageSize,reqStr);
			}else if("account".equals(action)) {
				result = this.accountCscode(request,pageIndex,pageSize,reqStr);
			}else if("sales".equals(action)) {
				result = this.salesCscode(request,pageIndex,pageSize,reqStr);
			}else if("price".equals(action)) {
				result = this.priceCscode(request,pageIndex,pageSize,reqStr);
			}else if("book".equals(action)) {
				result = this.bookCscode(request,pageIndex,pageSize,reqStr);
			}else if("act".equals(action)) {
				result = this.queryAct(request,pageIndex,pageSize,reqStr);
			}else if("otherCntype".equals(action)) {
				result = this.queryOtherCntype(request, pageIndex, pageSize, reqStr);
			}else if("port".equals(action)) {
				result = this.port(request, pageIndex, pageSize, reqStr);
			}else if("incarico".equals(action)){
				result=this.incaricocode(request, pageIndex, pageSize, reqStr);
			}else if("route".equals(action)){
				result=this.datline(request, pageIndex, pageSize, reqStr);
			}else if("inputers".equals(action)){
				result=this.inputerscode(request, pageIndex, pageSize, reqStr);
			}else if("corpaccount".equals(action)) {
				result = this.corpaccountCscode(request,pageIndex,pageSize,reqStr);
			}else if("customcompany".equals(action)) {
				result = this.customcompanys(request,pageIndex,pageSize,reqStr);
			}
			else if("goodstype".equals(action)) {
				result = this.goodstypes(request,pageIndex,pageSize,reqStr);
			}
			else if("priceuser".equals(action)) {
				result = this.priceusers(request,pageIndex,pageSize,reqStr);
			}else if("carrier".equals(action)) {
				result = this.queryCarrier(request,pageIndex,pageSize,reqStr);
			}else if("cntype".equals(action)) {
				result = this.queryCntype(request, pageIndex, pageSize, reqStr);
			}else if("whcustomer".equals(action)) {
				result = this.queryWhcustomer(request, pageIndex, pageSize, reqStr);
			}else if("clientname".equals(action)) {
				result = this.queryClientname(request, pageIndex, pageSize, reqStr);
			}else if("agenter".equals(action)) {
				result = this.qryagenter(request, pageIndex, pageSize, reqStr);
			}else if("truckcompany".endsWith(action)){
				result = this.queryTruckcompany(request, pageIndex, pageSize, reqStr);
			}else if("getcustomcompany".endsWith(action)){
				result = this.queryCustomcompany(request, pageIndex, pageSize, reqStr);
			}else if("fclpod".endsWith(action)){
				result = this.fclpod(request, pageIndex, pageSize, reqStr);
			}else if("fclpol".endsWith(action)){
				result = this.fclpol(request, pageIndex, pageSize, reqStr);
			}else if("airline".endsWith(action)){//航空公司
				result = this.queryAirline(request, pageIndex, pageSize, reqStr);
			}else if("agentdes".endsWith(action)){//目的港代理
				result = this.queryAgentdes(request, pageIndex, pageSize, reqStr);
			}else if("maersk_port".equals(action)) {
				result = this.queryMaerskPort(request,pageIndex,pageSize,reqStr);
			}else if("queryAgent".endsWith(action)){
				result = this.queryAgent(request, pageIndex, pageSize, reqStr);
			}else if("cosco_port".equals(action)) {
				result = this.queryCoscokPort(request,pageIndex,pageSize,reqStr);
			}else if("getFsastList".equals(action)) {
				result = this.getFsastList(request,pageIndex,pageSize,reqStr);
			}else if("polhistory".equals(action)) {
				result = this.polhistory(request,pageIndex,pageSize,reqStr);
			}else if("podhistory".equals(action)) {
				result = this.podhistory(request,pageIndex,pageSize,reqStr);
			}
		} catch (Exception e) {
			result = "Server error";
			e.printStackTrace();
		}
		return result;
	}



	private String bookCscode(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String userid = request.getParameter("userid");
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(namec,'') as name,COALESCE(code,'') as namec,COALESCE(corper,'') as namee ");
		sb.append("\n FROM");
		sb.append("\n	 sys_user a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		//sb.append("\n   AND jobdesc like '%订舱%'");
		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = a.corpid AND x.ischoose = TRUE AND userid ="+userid+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())sb.append(qry);//非saas模式不控制
		sb.append("\n	AND (namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR corper ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY namee,name");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_user a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 	isdelete = FALSE");
		//sbTotal.append("\n  	AND jobdesc like '%订舱%'");
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())sbTotal.append(qry);//非saas模式不控制
		sbTotal.append("\n 		AND (namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR corper ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String priceCscode(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String userid = request.getParameter("userid");
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(namec,'') as name,COALESCE(namee,'') as namec,COALESCE(corper,'') as namee ");
		sb.append("\n FROM");
		sb.append("\n	 sys_user a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		//sb.append("\n  AND jobdesc like '%市场%'");
		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = a.corpid AND x.ischoose = TRUE AND userid ="+userid+"))";
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())sb.append(qry);//非saas模式不控制
		sb.append("\n	AND (namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR corper ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY namee,name");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_user a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
//		sbTotal.append("\n  	AND jobdesc like '%市场%'");
		if(((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas())sbTotal.append(qry);//非saas模式不控制
		sbTotal.append("\n 		AND (namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR corper ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String accountCscode(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		//String corpidcurrent =  request.getParameter("corpidcurrent").toString();
		String userid = request.getParameter("userid");
		String corpid = request.getParameter("corpid");
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(code,'') as name,COALESCE(bankdesc,'') as namec,COALESCE(accountcont,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	 _dat_account c");
		sb.append("\n WHERE");
		sb.append("\n	 (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR accountno ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR accountnoe ILIKE '%"+reqStr+"%')");
//		sb.append("\n	AND EXISTS (SELECT * FROM dat_bank x WHERE x.isdelete = FALSE AND x.corpid = "+corpidcurrent+" AND x.id = c.bankid)");
		sb.append("\n 	AND (CASE WHEN f_sys_isaas('') = TRUE THEN (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = c.corpid) AND x.ischoose = TRUE AND x.userid = "+userid+")) ELSE TRUE END)");
		sb.append("\n	AND c.isinuse = TRUE");

		//系统设置是否按照抬头过滤
		String str = ConfigUtils.findSysCfgVal("bill_account");
		if(StrUtils.isNull(str) || "N".equals(str)){
		}else{
			sb.append("\n	AND c.corpid = "+Long.valueOf(corpid)+"");
		}
		sb.append("\n	ORDER BY c.orderno DESC,code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		_dat_account c");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		 (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR accountno ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR accountnoe ILIKE '%"+reqStr+"%')");
//		sbTotal.append("\n		AND EXISTS (SELECT * FROM dat_bank x WHERE x.isdelete = FALSE AND x.corpid = "+corpidcurrent+" AND x.id = c.bankid)");
		sbTotal.append("\n 		AND (CASE WHEN f_sys_isaas('') = TRUE THEN (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = c.corpid) AND x.ischoose = TRUE AND x.userid = "+userid+")) ELSE TRUE END)");
		sbTotal.append("\n 		AND c.isinuse = TRUE");
		if(StrUtils.isNull(str) || "N".equals(str)){
		}else{
			sbTotal.append("\n	AND c.corpid = "+Long.valueOf(corpid)+"");
		}
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String corpaccountCscode(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String corpidcurrent =  request.getParameter("corpidcurrent");
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(bankname,'') as name,COALESCE(accountno,'') as namec,COALESCE(accountcont,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	 sys_corpaccount c");
		sb.append("\n WHERE");
		sb.append("\n	 (bankname ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR bankaddr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR accountno ILIKE '%"+reqStr+"%')");
		sb.append("\n	AND customerid = "+corpidcurrent);
		sb.append("\n	AND isdelete = false");
		sb.append("\n	ORDER BY bankname DESC,bankaddr");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corpaccount c");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		 (bankname ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR bankaddr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR accountno ILIKE '%"+reqStr+"%')");
		sbTotal.append("\n	AND customerid = "+corpidcurrent);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String salesCscode(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String customerid = request.getParameter("custmerid");
		if(customerid==null||StrUtils.isNull(customerid)){
			customerid = "0";
		}
		String type = request.getParameter("type");
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n 	array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(namec,'') as name,COALESCE(namee,'') as namec,COALESCE((SELECT c.abbr FROM sys_corporation c WHERE c.isdelete = false and c.id = d.corpid LIMIT 1),'') as namee ");
		sb.append("\n FROM");
		sb.append("\n	 sys_user d");
		sb.append("\n WHERE");
		sb.append("\n isadmin = 'N'");
		sb.append("\n AND valid = 'Y'");//有效的用户
		sb.append("\n	AND isdelete = false");
		sb.append("\n	AND issales = true ");
		sb.append("\n	AND id <> 81433600 ");
		sb.append("\n	AND (namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR corper ILIKE '%"+reqStr+"%')");
		if(StrUtils.isNull(type)){
			sb.append("\n AND CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'jobs_sales_filterby_client' AND val = 'Y') THEN (EXISTS(SELECT 1 FROM sys_corporation WHERE salesid = d.id AND id = "+customerid+")");
			sb.append("\n OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = "+customerid+" AND linktype = 'C' AND roletype = 'S' AND userid = d.id)) ELSE 1=1 END");
		}
		if(applicationConf.isSaas()){
			String userid = request.getParameter("userid");
			String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.corpid AND x.ischoose = TRUE AND userid ="+userid+"))";
			sb.append(qry);
		}
		sb.append("\n	ORDER BY namee,name");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_user a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n isadmin = 'N'");
		sbTotal.append("\n 		AND isdelete = FALSE");
		sbTotal.append("\n		AND issales = true");
		sbTotal.append("\n 		AND (namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR corper ILIKE '%"+reqStr+"%')");
		if(StrUtils.isNull(type)){
			sbTotal.append("\n AND CASE WHEN EXISTS(SELECT 1 FROM sys_config WHERE key = 'jobs_sales_filterby_client' AND val = 'Y') THEN (EXISTS(SELECT 1 FROM sys_corporation WHERE salesid = a.id AND id = "+customerid+")");
			sbTotal.append("\n OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linkid = "+customerid+" AND linktype = 'C' AND roletype = 'S' AND userid = a.id)) ELSE 1=1 END");
		}
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	/*
	 * 指派
	 * */
	private String inputerscode(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String filiale = request.getParameter("filiale");
		String filialesql = "";
		if(!StrUtils.isNull(filiale)){
			filialesql = "\n AND EXISTS (SELECT * FROM sys_corporation x WHERE x.isdelete = FALSE AND a.corpid = x.id AND x.abbcode = '"+StrUtils.getSqlFormat(filiale)+"')";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(namec,'') as name,COALESCE(code,'') as namec,COALESCE(namee ,'') as namee ,(SELECT abbcode FROM sys_corporation WHERE id = a.corpid AND isdelete = FALSE)");
		sb.append("\n FROM");
		sb.append("\n	 sys_user a");
		sb.append("\n WHERE");
		sb.append("\n isadmin = 'N'");
		sb.append("\nAND id<>81433600");
		sb.append("\n	AND isdelete = false");
		sb.append(filialesql);
		sb.append("\n	AND (namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		
		if(applicationConf.isSaas()){
			String userid = request.getParameter("currentUserid");
			String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = a.corpid AND x.ischoose = TRUE AND userid ="+userid+"))";
			sb.append(qry);
		}
		
		sb.append("\n	ORDER BY namee");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_user a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n isadmin = 'N'");
		sbTotal.append("\n 		AND isdelete = FALSE");
		sbTotal.append(filialesql);
		sbTotal.append("\n 		AND (namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	/*
	 * 指派所有人
	 * */
	private String incaricocode(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(namec,'') as name,COALESCE(code,'') as namec,COALESCE(namee,'') as namee ");
		sb.append("\n FROM");
		sb.append("\n	 sys_user");
		sb.append("\n WHERE");
		sb.append("\n isadmin = 'N'");
		sb.append("\n	AND isdelete = false");
		sb.append("\n	AND id <> 81433600");
		sb.append("\n	AND isinvalid = true");
		sb.append("\n	AND (namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY namee,name");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_user");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n isadmin = 'N'");
		sbTotal.append("\n 		AND isdelete = FALSE");
		sbTotal.append("\n 		AND isinvalid = TRUE");
		sbTotal.append("\n 		AND (namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String port(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryPol(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}else if(type != null && !type.isEmpty() && "ship".equals(type)){
			filterType = "AND isship = TRUE ";
		}else if(type != null && !type.isEmpty() && "train".equals(type)){
			filterType = "AND istrain = TRUE ";
		}
		filterType += "AND isvalid = TRUE ";

		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispol = true");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND ispol = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}

	private String queryPot(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}else if(type != null && !type.isEmpty() && "ship".equals(type)){
			filterType = "AND isship = TRUE ";
		}else if(type != null && !type.isEmpty() && "train".equals(type)){
			filterType = "AND istrain = TRUE ";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispod = true");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND ispod = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryPod(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}else if(type != null && !type.isEmpty() && "ship".equals(type)){
			filterType = "AND isship = TRUE ";
		}else if(type != null && !type.isEmpty() && "train".equals(type)){
			filterType = "AND istrain = TRUE ";
		}
		filterType += "AND isvalid = TRUE ";

		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispod = true");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND ispod = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryPoa(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}else if(type != null && !type.isEmpty() && "ship".equals(type)){
			filterType = "AND isship = TRUE ";
		}else if(type != null && !type.isEmpty() && "train".equals(type)){
			filterType = "AND istrain = TRUE ";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND isbarge = true");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND isbarge = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryAllwarehouse(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_warehouse");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		//sb.append("\n	AND isbarge = true");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		//sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_warehouse");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		//sbTotal.append("\n 		AND isbarge = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		//sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
private String queryLines(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
	String type =  request.getParameter("type");
	String filterType = "";
	if(type != null && !type.isEmpty() && "T".equals(type)){
		filterType = "AND lintype = 'T' ";
	}else if(type != null && !type.isEmpty() && "S".equals(type)){
		filterType = "AND lintype = 'S' ";
	}
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_line");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_line");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		//sbTotal.append("\n 		AND isbarge = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryDestination(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}else if(type != null && !type.isEmpty() && "ship".equals(type)){
			filterType = "AND isship = TRUE ";
		}else if(type != null && !type.isEmpty() && "train".equals(type)){
			filterType = "AND istrain = TRUE ";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND isdestination = true");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND isdestination = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryPdd(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}else if(type != null && !type.isEmpty() && "ship".equals(type)){
			filterType = "AND isship = TRUE ";
		}else if(type != null && !type.isEmpty() && "train".equals(type)){
			filterType = "AND istrain = TRUE ";
		}
		filterType += "AND isvalid = TRUE ";

		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispdd = true");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND ispdd = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	
	/**
	 * 查询客户分页(全部)
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryCustomer(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String userid = request.getParameter("userid");
		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "\nAND isair = TRUE";
		}
		if(type != null && !type.isEmpty() && "train".equals(type)){
			filterType = "\nAND istrain = TRUE";
		}
		//arap neo 20161118 if factory filter uncheck data
		if(type != null && !type.isEmpty() && "arap".equals(type)){
			String value = ConfigUtils.findSysCfgVal("sys_factoryneedcheck");
			if(value!=null&&value.equals("Y")){
				filterType = "\nAND (CASE WHEN isfactory = TRUE THEN ischeck = TRUE ELSE 1=1 END)";
			}
			String isolation = ConfigUtils.findSysCfgVal("customer_isolation");
			//2775 系统设置增加设置
			//设置为 Y 时，费用编辑界面，下拉框选择中，增加过滤条件：
			//如果客户只勾了海运空运(两者或其一)，其他属性都没有勾选的情况。费用编辑里面，选择结算单位的时候，增减过滤条件，按业务员关联过滤
			if(isolation!=null&&isolation.equals("Y")){
				filterType = "\nAND (CASE WHEN iscarrier=FALSE AND isairline=FALSE AND isairline=FALSE AND issupplier = FALSE AND isagent=FALSE"
							+"\nAND iswarehouse = FALSE AND iscustom=FALSE AND iscooperative=FALSE"
							+"\nAND isfactory=FALSE AND istruck=FALSE AND ishipper=FALSE AND isconsignee=FALSE"
							+"\nAND isagentdes=FALSE"
							+"\nAND(isfcl = TRUE OR isair = TRUE OR istrain = TRUE) THEN"
							+"\n(EXISTS"
							+"\n	(SELECT" 
							+"\n		1 "
							+"\n	FROM sys_custlib x , sys_custlib_user y  "
							+"\n	WHERE y.custlibid = x.id  "
							+"\n		AND y.userid = "+userid
							+"\n		AND x.libtype = 'S'  "
							//+"\n		AND (x.userid = a.salesid  OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linktype = 'C' AND linkid = a.id AND userid = x.userid AND roletype = 'S')))"
							+"\n		AND x.userid = ANY(SELECT a.salesid  UNION (SELECT userid FROM sys_user_assign zzz WHERE linktype = 'C' AND linkid = a.id AND roletype = 'S' AND zzz.isdelete = FALSE)))"
							+"\nOR EXISTS"
							+"\n	(SELECT" 
							+"\n		1 "
							+"\n	FROM sys_custlib x , sys_custlib_role y " 
							+"\n	WHERE y.custlibid = x.id  "
							//+"\n		AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+userid+" AND z.roleid = y.roleid)"
							+"\n		AND y.roleid = ANY (SELECT DISTINCT z.roleid FROM sys_userinrole z WHERE z.userid = "+userid+" )"
							
							+"\n		AND x.libtype = 'S'  "
							//+"\n		AND (x.userid = a.salesid OR EXISTS(SELECT 1 FROM sys_user_assign WHERE linktype = 'C' AND linkid = a.id AND userid = x.userid AND roletype = 'S'))))"
							+"\n		AND x.userid = ANY(SELECT a.salesid UNION (SELECT userid FROM sys_user_assign zzz WHERE linktype = 'C' AND linkid = a.id AND roletype = 'S' AND zzz.isdelete = FALSE))))"
							+"\nELSE TRUE END)";
			}
		}
		
		if(type != null && !type.isEmpty() && ("fcl".equals(type) || "lcl".equals(type) || "ddp".equals(type) || "ship".equals(type))){
			filterType = "\nAND (isfcl = true OR isddp = true OR islcl = true)";
		}
		if(type != null && !type.isEmpty() && "customer".equals(type)){
			filterType = "\n AND (a.isfcl = true OR a.isair = true)";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,code) as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee,COALESCE((SELECT namec FROM dat_filedata WHERE code = a.customertype AND isdelete = FALSE AND fkcode = 95 LIMIT 1),'') AS customertype,COALESCE(a.daysar::TEXT,'') as daysar");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		if(type != null && !type.isEmpty() && !"joblist".equals(type)){//工作单高级检索不过滤合并客户
			sb.append("\n	AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype = 'J')");//过滤掉已经被合并的客户 neo 20161025 neo 20161227 L类型只是关联应该可以查到
		}
		String unchek = ConfigUtils.findSysCfgVal("arap_filter_unchek_customer");
		if(unchek!=null&&unchek.equals("Y")){//2112 系统设置中增加设置：未审核不能录入费用 默认否 兼容现在方式
			sb.append("\n AND a.isofficial = true");
		}
		String isarap = ConfigUtils.findSysCfgVal("fee_customer_filterby_araptype");
		if(null != isarap && "Y".equals(isarap) && "arap".equals(type)){
			sb.append( "\nAND (isar = true or isap = true)");
		}
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		if(!"arap".equals(type)&&!"customer".equals(type)){//arap neo 20161118 if arap show all data
			
			sb.append(custCtrlClauseWhere(request, pageIndex, pageSize, reqStr));
		}
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		if(type != null && !type.isEmpty() && !"joblist".equals(type)){//工作单高级检索不过滤合并客户
			sbTotal.append("\n	AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype <> 'J')");//过滤掉已经被合并的客户 neo 20161025 neo 20161227 L类型只是关联应该可以查到
		}
		if(unchek!=null&&unchek.equals("Y")){//2112 系统设置中增加设置：未审核不能录入费用 默认否 兼容现在方式
			sbTotal.append("\n AND a.isofficial = true");
		}
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		if(null != isarap && "Y".equals(isarap) && "arap".equals(type)){
			sbTotal.append( "\nAND (isar = true or isap = true)");
		}
		if(!"arap".equals(type)&&!"customer".equals(type)){//arap neo 20161118 if arap show all data
			sbTotal.append(custCtrlClauseWhere(request, pageIndex, pageSize, reqStr));
		}
		
		//System.out.println(sbTotal.toString());
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}

	private String queryDelivercompany(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,code) as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation d");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}

	private String querybusshippingCustomer(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String userid = request.getParameter("userid");
		String filterType = "";
		filterType = "\n AND (iscarrier = TRUE or isagent = TRUE)";

		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,code) as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n AND a.isofficial = true");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n AND a.isofficial = true");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}

	/**
	 * 查询客户拖车公司
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryTruckcompany(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,code) as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n		AND istruck=true AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype = 'J')");
		sb.append("\n	ORDER BY abbr");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append("\n		AND istruck=true AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype = 'J')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());
		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	/**
	 * 查询客户报关公司
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryCustomcompany(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,code) as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	AND iscustom=true AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype = 'J')");
		sb.append("\n	ORDER BY abbr");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append("\n		AND iscustom=true AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype = 'J')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());
		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryBusbillCustomer(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		Long jobid = Long.parseLong(request.getParameter("jobid"));
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') AS name,COALESCE(namec,'') AS namec,COALESCE(namee,'') AS namee ");
		sb.append("\n FROM");
		sb.append("\n	_sys_corporation AS a");
		sb.append("\n WHERE");
		sb.append("\n	a.isdelete = FALSE");
		sb.append("\n	AND EXISTS (SELECT * FROM fina_arap x WHERE x.isdelete=FALSE AND x.jobid = "+jobid+" AND a.id = x.customerid)");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY abbr");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (id) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		_sys_corporation AS a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		a.isdelete = FALSE");
		sbTotal.append("\n		AND EXISTS (SELECT * FROM fina_arap x WHERE x.isdelete=FALSE AND x.jobid = "+jobid+" AND a.id = x.customerid)");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());
		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	/**
	 * 查询客户分页(过滤本部)
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryCustomerFilterCustomer(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND iscustomer = true");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(custCtrlClauseWhere(request, pageIndex, pageSize, reqStr));
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n	AND iscustomer = true");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(custCtrlClauseWhere(request, pageIndex, pageSize, reqStr));
		//
		
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	/**
	 * 费用条目分页
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryFeei(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
//		boolean isen = AppUtils.getUserSession().getMlType().equals(LMapBase.MLType.en);
		String filterBySaas = SaasUtil.filterByCorpid("d",request);
		String languge =  request.getParameter("languge");
		boolean isen = languge!=null&&languge.equals("en")?true:false;
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,code as name,"+(isen?"namee":"name")+" as namec,namee");
		sb.append("\n FROM");
		sb.append("\n	dat_feeitem d");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n" + filterBySaas);
		sb.append("\n	AND (code ILIKE '"+reqStr+"%'");
		sb.append("\n	OR name ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_feeitem d");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n" + filterBySaas);
		sbTotal.append("\n 		AND (code ILIKE '"+reqStr+"%'");
		sbTotal.append("\n 		OR name ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	/**
	 * 查询币种分页
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryCurrency(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String filterBySaas = SaasUtil.filterByCorpid("d",request);
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,code as name,name as namec");
		sb.append("\n FROM");
		sb.append("\n	dat_currency d");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n" + filterBySaas);
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_currency d");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n" + filterBySaas);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());
		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	/**
	 * 查询单位分页
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryUnit(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	un.ID,un.code as name,un.namec,un.namee");
		sb.append("\n FROM");
		sb.append("\n	_dat_unit AS un");
		sb.append("\n WHERE");
		sb.append("\n	un.isdelete = FALSE");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		_dat_unit AS un");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		un.isdelete = FALSE");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());
		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	/**
	 * 获取所有客服
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryCscode(HttpServletRequest request,String pageIndex,String pageSize,String reqStr) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	sys_user");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND isadmin = 'N'");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY iscs");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_user");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND isadmin = 'N'");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	private String custCtrlClauseWhere(HttpServletRequest request,String pageIndex,String pageSize,String reqStr) {
		String userid = request.getParameter("userid");
		String usercode = null;
		if(userid==null||userid.isEmpty()) {
			//AppUtils.debug("下拉框没有接收到有效的userid,故userid设值-100,请检查flexboxJs是否传递userid");
			userid = "-100";
		}else {
			usercode = serviceContext.userMgrService.sysUserDao.findById(Long.parseLong(userid)).getCode();
		}
		StringBuffer sb = new StringBuffer(); 
		sb.append("\n	AND ((");
		sb.append("\n		EXISTS (SELECT 1 ");
		sb.append("\n					FROM sys_custlib_cust y,sys_custlib_user z");
		sb.append("\n					WHERE");
		sb.append("\n							y.custlibid = z.custlibid");
		sb.append("\n					AND y.corpid = a.id");
		sb.append("\n					AND z.userid = "+userid);
		sb.append("\n							)");
		sb.append("\n			OR EXISTS(SELECT 1");
		sb.append("\n					FROM sys_user_assign xx");
		sb.append("\n					WHERE");
		sb.append("\n							xx.linkid = a.id");
		sb.append("\n					AND xx.userid = "+userid);
		sb.append("\n							)");
		sb.append("\n			OR EXISTS(SELECT 1");
		sb.append("\n					FROM sys_custlib x , sys_custlib_role y ");
		sb.append("\n					WHERE y.custlibid = x.id ");
		//--neo 20191202 优化写法
		//sb.append("\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+userid+" AND z.roleid = y.roleid) ");
		sb.append("\n					AND y.roleid = ANY (SELECT DISTINCT z.roleid FROM sys_userinrole z WHERE z.userid = "+userid+" AND z.roleid IS NOT NULL) ");
		sb.append("\n					AND x.libtype = 'S' ");
		//sb.append("\n					AND (EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = a.salesid)");

//		sb.append("\n					AND (x.userid = a.salesid");
//		sb.append("\n						OR EXISTS(SELECT 1 FROM sys_user z , sys_user_assign zz where z.id = x.userid AND zz.linkid = a.id AND z.id = zz.userid AND zz.roletype = 'S' AND z.isdelete = false AND zz.isdelete = false)");
//		sb.append("\n						)");
		
		//--neo 20200628 优化写法 客户里面的业务员和客户分配业务员判断业务员关联部分，之前OR写法，sys_user_assign表数据量起来后比较慢	
		sb.append("\n					AND x.userid = ANY(SELECT a.salesid");
		sb.append("\n										UNION ");
		sb.append("\n									   SELECT z.id FROM sys_user z , sys_user_assign zz where zz.linkid = a.id AND z.id = zz.userid AND zz.roletype = 'S' AND z.isdelete = false AND zz.isdelete = false");
		sb.append("\n									   )");
		
		
		sb.append("\n				))");
		//sb.append("\n			OR a.inputer = '"+usercode+"'");
		sb.append("\n			)");
		return sb.toString();
	}
	
	//获取拖车工厂
	private String queryFactory(HttpServletRequest request,String pageIndex,String pageSize,String reqStr) {
		String userid = request.getParameter("userid");
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND isfactory = TRUE");
		sb.append("\n	AND id = ANY(SELECT id FROM f_sys_corporation_filter('srctype=factory;userid="+userid+"'))");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		
		sb.append("\n	UNION ");
		sb.append("\n	SELECT");
		sb.append("\n	c.id,COALESCE(c.name,'') as name,COALESCE(c.name,'') as namec,'' as namee");
		sb.append("\n	FROM");
		sb.append("\n	sys_corporation a , sys_corpcontacts c");
		sb.append("\n	WHERE");
		sb.append("\n	a.isdelete = false");
		sb.append("\n	AND c.customerid = a.id AND c.contactype = 'T'");
		sb.append("\n	AND a.isfactory = TRUE");
		sb.append("\n	AND a.id = ANY(SELECT id FROM f_sys_corporation_filter('srctype=factory;userid="+userid+"'))");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		//sb.append("\n	ORDER BY iscs");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT SUM(total) :: TEXT AS total FROM (");
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (1) AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND isfactory = TRUE");
		sbTotal.append("\n	AND id = ANY(SELECT id FROM f_sys_corporation_filter('srctype=factory;userid="+userid+"'))");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		
		sbTotal.append("\n 	UNION");
		sbTotal.append("\n 	SELECT");
		sbTotal.append("\n 		COUNT (1) AS total");
		sbTotal.append("\n 	FROM");
		sbTotal.append("\n 		sys_corporation a , sys_corpcontacts c");
		sbTotal.append("\n	WHERE");
		sbTotal.append("\n	a.isdelete = false");
		sbTotal.append("\n	AND c.customerid = a.id AND c.contactype = 'T'");
		sbTotal.append("\n	AND a.isfactory = TRUE");
		sbTotal.append("\n	AND a.id = ANY(SELECT id FROM f_sys_corporation_filter('srctype=factory;userid="+userid+"'))");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append("\n 		) AS T");
		
		
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	/**
	 * 科目
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryAct(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String actsetid = request.getParameter("actsetid");
		String yeardesc = request.getParameter("yeardesc");
		String perioddesc = request.getParameter("perioddesc");
		
		if(actsetid == null || yeardesc == null || perioddesc == null || actsetid.isEmpty() || yeardesc.isEmpty() || perioddesc.isEmpty()) {
			actsetid = "9999999999";
			yeardesc = "-1";
			perioddesc = "-1";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	d.id,");
		sb.append("\n	COALESCE(d.cyid||'|'||d.dir||'|'||isastypeb::TEXT||'|'||isastypec::TEXT||'|'||isastyped::TEXT||'|'||isastypee::TEXT||'|'||");
		sb.append("\n	(isastypeb OR isastypec OR isastyped OR isastypee)||'|'||COALESCE((SELECT currencyfm||'-'||rate::NUMERIC(18,4)||'-'||xtype");
		sb.append("\n	 FROM _fs_xrate t WHERE t.actsetid = d.actsetid");
		sb.append("\n	AND yeardesc = '"+yeardesc+"' AND perioddesc = '"+perioddesc+"' AND isdelete = FALSE AND t.currencyfm = d.cyid LIMIT 1),");
		sb.append("\n	CASE WHEN d.cyid = '*' THEN (SELECT s.basecy FROM fs_actset s WHERE s.isdelete = FALSE AND s.id = d.actsetid) ELSE d.cyid END ||'-1-*')::TEXT,'') AS name,");
		sb.append("\n	COALESCE(d.code||'/'||f_fs_vchdtl_getactcombox('id='||d.id||';'),'') as namec");
		sb.append("\n   , COALESCE ( (isastypeb :: TEXT || '|' || isastypec :: TEXT || '|' || isastyped :: TEXT || '|' || isastypef :: TEXT), '' ) AS isastypeallsap");
		sb.append("\n FROM");
		sb.append("\n	fs_act AS d ");
		sb.append("\n WHERE");
		sb.append("\n	d.isdelete = FALSE");
		sb.append("\n	AND d.isinuse = TRUE ");
		sb.append("\n	AND NOT EXISTS (SELECT 1 FROM fs_act x WHERE x.isdelete = FALSE AND d.id = x.parentid) ");
		sb.append("\n	AND d.actsetid = "+actsetid);
		sb.append("\n	AND (code ILIKE '"+reqStr+"%'");//记录:有匹配后再排序，优先左模糊不会写，code暂时写成左匹配,name全匹配;
		sb.append("\n	OR f_fs_vchdtl_getactcombox('id='||d.id||';') ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY CAST(d.code AS VARCHAR)");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (id) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		fs_act AS d");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		d.isdelete = FALSE");
		sbTotal.append("\n 		AND d.isinuse = TRUE ");
		sbTotal.append("\n 		AND NOT EXISTS (SELECT 1 FROM fs_act x WHERE x.isdelete = FALSE AND d.id = x.parentid) ");
		sbTotal.append("\n		AND d.actsetid = "+actsetid);
		sbTotal.append("\n 		AND (code ILIKE '"+reqStr+"%'");
		sbTotal.append("\n 		OR f_fs_vchdtl_getactcombox('id='||d.id||';') ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	/**
	 * 订单模块,其它柜型下拉框(除20gp,40gp,40hq)
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryOtherCntype(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	a.id,COALESCE(a.code,'') as name");
		sb.append("\n FROM");
		sb.append("\n	dat_cntype AS a ");
		sb.append("\n WHERE");
		sb.append("\n	a.isdelete = FALSE");
		sb.append("\n	AND a.code <> '20''GP'");
		sb.append("\n	AND a.code <> '40''GP'");
		sb.append("\n	AND a.code <> '40''HQ'");
		sb.append("\n	AND code ILIKE '%"+reqStr+"%'");
		sb.append("\n	ORDER BY CAST(a.code AS VARCHAR) ");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_cntype AS a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		a.isdelete = FALSE");
		sbTotal.append("\n 		AND a.code <> '20''GP'");
		sbTotal.append("\n 		AND a.code <> '40''GP'");
		sbTotal.append("\n 		AND a.code <> '40''HQ'");
		sbTotal.append("\n 		AND code ILIKE '%"+reqStr+"%'");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	/**
	 * 航线
	 */
	private String datline(HttpServletRequest request,String pageIndex,String pageSize,String reqStr) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_line");
		sb.append("\n WHERE");
		sb.append("\n 		isdelete = FALSE");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_line");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String customcompanys(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String filiale = request.getParameter("filiale");
		String filialesql = "";
		if(!StrUtils.isNull(filiale)){
			filialesql = "\n AND EXISTS (SELECT * FROM sys_corporation x WHERE x.isdelete = FALSE AND a.corpid = x.id AND x.abbcode = '"+StrUtils.getSqlFormat(filiale)+"')";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(code,'') as name,COALESCE(abbr,'') as namec,COALESCE(namee,'') as namee ");
		sb.append("\n FROM");
		sb.append("\n	 sys_corporation a");
		sb.append("\n WHERE");
		//sb.append("\n iscustom=true");
		sb.append("\n isclc=true");
		sb.append("\n	AND isdelete = false");
		sb.append(filialesql);
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY namee DESC,name");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		//sbTotal.append("\n iscustom=true");
		sbTotal.append("\n isclc=true");
		sbTotal.append("\n 		AND isdelete = FALSE");
		sbTotal.append(filialesql);
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String goodstypes(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	_dat_goodstype");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'"); 
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		_dat_goodstype");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String priceusers(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') AS namec,COALESCE(namee,'') AS namee");
		sb.append("\n FROM");
		sb.append("\n	_sys_user");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'"); 
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		_sys_user");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	/**
	 * 查询船公司分页(全部)
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryCarrier(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,abbr as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n 	AND iscarrier = TRUE");
		sb.append("\n	AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype = 'J')");//过滤掉已经被合并的客户 neo 20161025 neo 20161227 L类型只是关联应该可以查到
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 	AND iscarrier = TRUE");
		sbTotal.append("\n	AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype <> 'J')");//过滤掉已经被合并的客户 neo 20161025 neo 20161227 L类型只是关联应该可以查到
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	/**
	 * 查询箱型分页
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryCntype(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(code,'') as namec");
		sb.append("\n FROM");
		sb.append("\n	dat_cntype");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_currency");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());
		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	/**
	 * 查询仓储客户分页(全部)
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryWhcustomer(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,abbr as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n 	AND iswarehouse = TRUE");
		sb.append("\n	AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype = 'J')");//过滤掉已经被合并的客户 neo 20161025 neo 20161227 L类型只是关联应该可以查到
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 	AND iswarehouse = TRUE");
		sbTotal.append("\n	AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x WHERE x.idfm = a.id AND jointype <> 'J')");//过滤掉已经被合并的客户 neo 20161025 neo 20161227 L类型只是关联应该可以查到
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	
	/**
	 * 收付款申请客户
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryClientname(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		String araptype = request.getParameter("araptype");
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(abbr,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee ");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation s");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid AND a.araptype = '"+araptype+"' AND a.isdelete = FALSE AND amount <> 0) ");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation s");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n	AND EXISTS (SELECT 1 FROM fina_arap a WHERE s.id = a.customerid AND a.araptype = '"+araptype+"' AND a.isdelete = FALSE AND amount <> 0) ");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String qryagenter(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String filiale = request.getParameter("filiale");
		String filialesql = "";
		if(!StrUtils.isNull(filiale)){
			filialesql = "\n AND EXISTS (SELECT * FROM sys_corporation x WHERE x.isdelete = FALSE AND a.corpid = x.id AND x.abbcode = '"+StrUtils.getSqlFormat(filiale)+"')";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n id,COALESCE(code,'') as name,COALESCE(abbr,'') as namec,COALESCE(namee,'') as namee ");
		sb.append("\n FROM");
		sb.append("\n	 sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n isagent=true");
		sb.append("\n	AND isdelete = false");
		sb.append(filialesql);
		//2495 委托下面订舱代理下拉框，按华展服务号2199，客户中需要勾选正式后才能选到
		sb.append("\n	AND (CASE WHEN f_sys_getcsno() = '2199' THEN isofficial = TRUE ELSE TRUE END)");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY namee DESC,name");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
		
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n isagent=true");
		sbTotal.append("\n 		AND isdelete = FALSE");
		sbTotal.append(filialesql);
		sbTotal.append("\n	AND (CASE WHEN f_sys_getcsno() = '2199' THEN isofficial = TRUE ELSE TRUE END)");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	
	private String fclpol(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') AS namec,COALESCE(namee,'') AS namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port p");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false AND isship IS TRUE AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port p");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE AND isship IS TRUE AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	
	private String fclpod(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') AS namec,COALESCE(namee,'') as namee");
		sb.append("\n FROM");
		sb.append("\n	dat_port p");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false AND isship IS TRUE AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port p");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE AND isship IS TRUE AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	
	
	/**
	 * 查询航空公司分页(全部)
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryAirline(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,code) as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND isairline = TRUE");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n	AND isairline = TRUE");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	/**
	 * 查询目的港代理分页(全部)
	 * @param request
	 * @param pageIndex
	 * @param pageSize
	 * @param reqStr
	 * @return
	 */
	private String queryAgentdes(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(abbr,code) as name,(CASE WHEN (namec is NULL OR namec = '') THEN namee ELSE  namec END) AS namec ,(CASE WHEN (namee is NULL OR namee = '') THEN namec ELSE  namee END) AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation a");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND isagentdes = TRUE");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation a");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n		AND isagentdes = TRUE");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryAgent(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(abbr,'') AS namec,COALESCE(namec,'') AS namee");
		sb.append("\n FROM");
		sb.append("\n	sys_corporation d");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND isagent = TRUE");
		sb.append("\n	AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')");
		sb.append("\n	AND (abbr ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
	
		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		sys_corporation d");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n		AND isagent = TRUE");
		sbTotal.append("\n		AND NOT EXISTS(SELECT 1 FROM sys_corporation_join x where x.idfm = d.id AND x.jointype = 'J')");
		sbTotal.append("\n 		AND (abbr ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());
		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryMaerskPort(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String filterType = "";
		if(type != null && !type.isEmpty() && "pol".equals(type)){
			filterType = "AND ispol = TRUE ";
		}else if(type != null && !type.isEmpty() && "pod".equals(type)){
			filterType = "AND ispod = TRUE ";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	id,COALESCE(rkstCode,'') as name,COALESCE(unLocCode,'') AS namec,cityName||'/'||countryName AS namee");
		sb.append("\n FROM");
		sb.append("\n	api_maersk_port");
		sb.append("\n WHERE");
		sb.append("\n	(rkstCode ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR cityName ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR countryName ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR unLocCode ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY rkstCode");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		api_maersk_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n	(rkstCode ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n	OR cityName ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n	OR countryName ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n	OR unLocCode ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
	
	private String queryCoscokPort(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (SELECT");
		sb.append("\n	cityid AS id ,COALESCE(code,'') as name,  COALESCE(name,'') AS namec, COALESCE(cityname,'') AS namee");
		sb.append("\n FROM");
		sb.append("\n	api_cosco_port");
		sb.append("\n WHERE");
		sb.append("\n	(code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR cntyname ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR name ILIKE '%"+reqStr+"%')");
		sb.append("\n	ORDER BY code");
		sb.append("\n	LIMIT "+pageSize);
		sb.append("\n	OFFSET "+((Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageSize)));
		sb.append("\n	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (id) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		api_cosco_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n	(code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n	OR code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n	OR cntyname ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n	OR name ILIKE '%"+reqStr+"%')");
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}

	private String getFsastList(HttpServletRequest request, String pageIndex, String pageSize, String reqStr){
		String actsetid = request.getParameter("actsetid");
		String astypecode = request.getParameter("astypecode");

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM (");
		sb.append(
				"SELECT" +
				" *     ," +
				"COALESCE (" +
				"( SELECT x.namec FROM sys_user x WHERE x.code = T.inputer AND x.isdelete = FALSE LIMIT 1 )," +
				"T.inputer " +
				") AS inputername," +
				"COALESCE (" +
				"( SELECT x.namec FROM sys_user x WHERE x.code = T.updater AND x.isdelete = FALSE LIMIT 1 )," +
				"T.updater " +
				") AS updatername " +
				"FROM _fs_ast T"
		);
		sb.append(" WHERE  1 = 1 AND actsetid = " + actsetid + " AND astypecode = '"+astypecode +"'" );
		sb.append("	AND (code ILIKE '%" + reqStr + "%'");
		sb.append("	OR name ILIKE '%" + reqStr + "%')");

		sb.append(" LIMIT " + pageSize + " OFFSET " + ((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)));
		sb.append("	) T");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());




		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("	SELECT" +
				"	COUNT(*)  :: TEXT AS total" +
				"	FROM _fs_ast t");
		sbTotal.append(" WHERE 1 = 1 AND actsetid = " + actsetid + " AND astypecode = '"+astypecode +"'");
		sbTotal.append("	AND (code ILIKE '%" + reqStr + "%'");
		sbTotal.append("	OR name ILIKE '%" + reqStr + "%')");

		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());
		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}


	private String polhistory(HttpServletRequest request, String pageIndex, String pageSize, String reqStr) {
		String type = request.getParameter("type");
		String userid = request.getParameter("userid");

		String filterType = "";
		if (type != null && !type.isEmpty() && "air".equals(type)) {
			filterType = "AND isair = TRUE ";
		} else if (type != null && !type.isEmpty() && "ship".equals(type)) {
			filterType = "AND isship = TRUE ";
		} else if (type != null && !type.isEmpty() && "train".equals(type)) {
			filterType = "AND istrain = TRUE ";
		}
		filterType += "AND isvalid = TRUE ";

		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (select * from (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee,(select count(1) from sys_operationlog so where so.userid=" + userid + " and menuname='船期查询' and elementname='polcode' and so.usevalue=code ) as usenumber");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispol = true");
		sb.append("\n	AND (code ILIKE '%" + reqStr + "%'");
		sb.append("\n	OR namec ILIKE '%" + reqStr + "%'");
		sb.append("\n	OR namee ILIKE '%" + reqStr + "%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code ");
		sb.append("\n	)aaa ORDER BY usenumber desc");
		sb.append("\n	LIMIT " + pageSize);
		sb.append("\n	OFFSET " + ((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)));
		sb.append("\n	) T ");
		Map<String, String> result = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND ispol = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%" + reqStr + "%'");
		sbTotal.append("\n 		OR namec ILIKE '%" + reqStr + "%'");
		sbTotal.append("\n 		OR namee ILIKE '%" + reqStr + "%')");
		sbTotal.append(filterType);
		Map<String, String> total = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":" + result.get("json") + ",\"total\":" + total.get("total") + "}";
	}


	private String podhistory(HttpServletRequest request,String pageIndex,String pageSize,String reqStr){
		String type =  request.getParameter("type");
		String userid = request.getParameter("userid");

		String filterType = "";
		if(type != null && !type.isEmpty() && "air".equals(type)){
			filterType = "AND isair = TRUE ";
		}else if(type != null && !type.isEmpty() && "ship".equals(type)){
			filterType = "AND isship = TRUE ";
		}else if(type != null && !type.isEmpty() && "train".equals(type)){
			filterType = "AND istrain = TRUE ";
		}
		filterType += "AND isvalid = TRUE ";

		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n (select * from (SELECT");
		sb.append("\n	id,COALESCE(code,'') as name,COALESCE(namec,'') as namec,COALESCE(namee,'') as namee,(select count(1) from sys_operationlog so where so.userid=" + userid + " and menuname='船期查询' and elementname='podcode' and so.usevalue=code ) as usenumber");
		sb.append("\n FROM");
		sb.append("\n	dat_port");
		sb.append("\n WHERE");
		sb.append("\n	isdelete = false");
		sb.append("\n	AND ispod = true");
		sb.append("\n	AND (code ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namec ILIKE '%"+reqStr+"%'");
		sb.append("\n	OR namee ILIKE '%"+reqStr+"%')");
		sb.append(filterType);
		sb.append("\n	ORDER BY code");
		sb.append("\n	)aaa ORDER BY usenumber desc");
		sb.append("\n	LIMIT " + pageSize);
		sb.append("\n	OFFSET " + ((Integer.parseInt(pageIndex) - 1) * Integer.parseInt(pageSize)));
		sb.append("\n	) T ");
		Map<String,String> result =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());

		StringBuffer sbTotal = new StringBuffer();
		sbTotal.append("\n SELECT");
		sbTotal.append("\n 		COUNT (ID) :: TEXT AS total");
		sbTotal.append("\n FROM");
		sbTotal.append("\n 		dat_port");
		sbTotal.append("\n WHERE");
		sbTotal.append("\n 		isdelete = FALSE");
		sbTotal.append("\n 		AND ispod = TRUE");
		sbTotal.append("\n 		AND (code ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namec ILIKE '%"+reqStr+"%'");
		sbTotal.append("\n 		OR namee ILIKE '%"+reqStr+"%')");
		sbTotal.append(filterType);
		Map<String,String> total =	daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sbTotal.toString());

		return "{\"results\":"+result.get("json")+",\"total\":"+total.get("total")+"}";
	}
}
