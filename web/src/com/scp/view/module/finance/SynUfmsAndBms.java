package com.scp.view.module.finance;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ibm.icu.text.SimpleDateFormat;
import com.scp.dao.sys.SysLogDao;
import com.scp.model.finance.FinaActpayrec;
import com.scp.model.finance.FinaActpayrecdtl;
import com.scp.model.sys.SysLog;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.ufms.base.annotation.Action;
import com.ufms.base.web.BaseServlet;

@WebServlet("/synufmsandbms")
public class SynUfmsAndBms extends BaseServlet {
	/**
	 * BMS费用审核 
	 */
	public void synArapInfoToBms(List<Map> list, List<Map> list2, ServiceContext serviceContext) {
		String url = ConfigUtils.findSysCfgVal("sys_bms_url");//获取系统配置UMS URL
		try {
			for (int i = 0; i < list2.size(); i++) {
				if(null == list2.get(i)){
					MessageUtils.alert("数据异常，请联系管理员!");
					return;
				}
				Map map2 = list2.get(i);
				for (Object key : map2.keySet()) {
					if("mblno".equals(String.valueOf(key))){
						continue;
					}
					if(map2.get(key) == null || StrUtils.isNull(String.valueOf(map2.get(key)))){
			    		MessageUtils.alert(key +" cannot be empty!");
						return;
			    	}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				Map map2 = list.get(i);
				for (Object key : map2.keySet()) {
					if("bcfreightid".equals(String.valueOf(key)) || "creator".equals(String.valueOf(key)) || "createtime".equals(String.valueOf(key)) ){
						continue;
					}
					
					if(map2.get(key) == null || StrUtils.isNull(String.valueOf(map2.get(key)))){
			    		MessageUtils.alert(key +" cannot be empty!");
						return;
			    	}
				}
			}
			JSONObject json = new JSONObject();
			JSONArray info = new JSONArray();
			JSONArray array = new JSONArray();
			json.put("type", "audit");//类型  费用审核：audit
			
			for (int j = 0; j < list2.size(); j++) {
				JSONObject infojson = new JSONObject();
				Map map = list2.get(j);
				//订单数据
				JSONObject order = new JSONObject();
				order.put("publicApprovalPersonCode", AppUtils.getUserSession().getUsercode());//提交审核人ID
				order.put("publicBizSystemType", "UFMS");//提交审核的系统
				order.put("publicBusinessType", map.get("publicbusinesstype"));//业务类型 
				order.put("publicBusinessOrderId", map.get("publicbusinessorderid"));//工作单ID
				order.put("publicJobNo", map.get("publicjobno"));//工作单编号
				order.put("publicSettleOffice", map.get("publicsettleoffice"));//分公司MZC码
				order.put("publicSettleOfficeName", map.get("publicsettleofficename"));//分公司中文名
				order.put("creator", map.get("creator"));//创建人
				order.put("createTime", map.get("createtime"));//创建时间
				order.put("publicBusinessDate", map.get("publicbusinessdate"));//工作单日期
				order.put("publicBusinessTypeName",  map.get("publicbusinesstypename"));//业务类型名称
				order.put("cimcID",  map.get("cimcid"));//中集统一订单号ID
				order.put("cimcNo",  map.get("cimcno"));//中集统一订单号
				
				infojson.put("bcPublicOrder",order);
				
				//费用数据
				for (int i = 0; i < list.size(); i++) {
					String jobid = String.valueOf(list.get(i).get("jobid"));//费用表中jobid
					String id = String.valueOf(map.get("publicbusinessorderid"));//jobs表中的id
					if(id.equals(jobid) == false){
						continue;
					}
					JSONObject freight = new JSONObject();
					freight.put("bizSystemType", "UFMS");//提交费用审核的系统
					freight.put("bizSystemOrderId", list.get(i).get("jobid"));//工作单ID
					freight.put("bizSystemFreightId", list.get(i).get("bizsystemfreightid"));//费用ID
					freight.put("businessType", map.get("businesstype"));//进出口
					freight.put("rpFlag", String.valueOf(list.get(i).get("rpflag")).replace("A", ""));//应收应付
					freight.put("settleCustCode", list.get(i).get("settlecustcode"));//结算公司客商辅助编码1
					freight.put("settleCustName", list.get(i).get("settlecustname"));//结算公司中文名
					freight.put("settleOfficeDeptCode", map.get("settleofficedeptcode"));//产值部门（结算地所在部门CODE）
					freight.put("settleOfficeDeptName", map.get("settleofficedeptname"));//产值部门名称（结算地所在部门中文名）
					freight.put("businessCode", list.get(i).get("settlecustcode"));//结算公司客商辅助编码1
					freight.put("businessName", list.get(i).get("settlecustname"));//结算公司中文名
					freight.put("mblNo", map.get("mblno"));//MBLNO
					freight.put("ledgerTypeCode", list.get(i).get("rpflag"));//费用类型
					freight.put("freightType", list.get(i).get("freighttype"));//BMS费用类型
					freight.put("freightCode", list.get(i).get("freightcode"));//BMS费用编码
					freight.put("freightNameCn", list.get(i).get("freightnamecn"));//费用名称中文
					freight.put("isReplace", "N");//撤票Y 是 N 否  默认为N
					freight.put("isSpot", "N");//是否现场结费Y 是 N 否  默认为N
					freight.put("quantity", list.get(i).get("quantity"));//数量
					freight.put("unitPrice", list.get(i).get("unitprice"));//单价
					freight.put("settleCurrencyCode", list.get(i).get("settlecurrencycode"));//币别
					freight.put("settleAmount", list.get(i).get("amount"));//金额
					freight.put("settleOffice", list.get(i).get("settleoffice"));//分公司MZC码
					freight.put("settleOfficeName", list.get(i).get("settleofficename"));//分公司中文名
					freight.put("exchangeRate", list.get(i).get("exchangerate"));//汇率
					freight.put("baseCurrencyCode", "CNY");//本位币
					freight.put("baseCurrencyValue", list.get(i).get("basecurrencyvalue"));//本位币金额
					freight.put("isInvoice", "Y");//是否允许开票（N：不开票，Y：开票）默认Y
					freight.put("auditStatus", StrUtils.isNull(String.valueOf(list.get(i).get("bcfreightid"))) ? "N" : "Y");//审核状态 
					freight.put("auditDate", map.get("auditdate"));//审核时间
					freight.put("auditPersonCode", AppUtils.getUserSession().getUsercode());//审核人代码
					freight.put("auditPersonName", AppUtils.getUserSession().getUsername());//审核人名称
					freight.put("isTax", "N");//是否含税
					freight.put("isTaxFree", "N");//是否免税
					freight.put("estimatedAmount", list.get(i).get("basecurrencyvalue"));//不含税本位币金额
					freight.put("estimatedTaxRate", 0);//预估税率
					freight.put("estimatedTaxes", 0);//预估税金
					freight.put("writeoffStatus", "f".equals(list.get(i).get("isamend")) ? "A" : "N");//冲销状态（W 冲销  A 增补 N默认 B 被冲销）
					freight.put("creator", list.get(i).get("creator"));//创建人
					freight.put("createTime", list.get(i).get("createtime"));//创建时间
					freight.put("isInternalFrt", "N");//内配标识  Y是 N不是  默认N
					freight.put("estimatedInvoiceType", "AP".equals(list.get(i).get("rpflag"))?"VAT-C0P":"VAT-C0R");//预计开票类型 待确认
					freight.put("estimatedInvoiceTypeName", "增值税普通发票0%");//预计开票类型名称 待确认
					freight.put("freightSource", "H");
					
					array.add(freight);
				}
				infojson.put("freight",array);
				info.add(infojson);
			}
			json.put("info", info);
			
			JSONObject request = new JSONObject();
			JSONObject header = new JSONObject();
			header.put("serviceCode", "FR_FEE_AUDIT");
			request.put("header", header);
			request.put("body", json);
			String ret = httpPost(url, request.toString());
			//同步信息到fina_arap表，同步数据到新增表 
			if(ret == null){
				MessageUtils.alert("Confirm FAIL!");
				return;
			}
			JSONObject response = JSONObject.fromObject(ret);
			if(response.get("body") == null){
				MessageUtils.alert("Confirm FAIL!");
				return;
			}
			JSONObject body = JSONObject.fromObject(response.get("body"));
			if(body.containsKey("errMsg")&&"0".equals(body.get("errCode"))){
				//审核成功
				JSONArray arr = JSONArray.fromObject(body.get("info"));
				for (int j = 0; j < arr.size(); j++) {
					JSONObject.fromObject(arr.get(j));
					JSONObject requstorder = JSONObject.fromObject(JSONObject.fromObject(arr.get(0)).get("bcPublicOrder"));
					JSONArray requstfreight = JSONArray.fromObject(JSONObject.fromObject(arr.get(0)).get("freight"));
					JSONObject rqf	= new JSONObject();
					if(null == requstorder || null == requstfreight){
						MessageUtils.alert("Confirm FAIL!");
						return;
					}
					String orderid = String.valueOf(requstorder.get("bcPublicOrderId"));
					String publicBusinessOrderId = String.valueOf(requstorder.get("publicBusinessOrderId"));
					if(StrUtils.isNull(orderid) || StrUtils.isNull(publicBusinessOrderId)){
						MessageUtils.alert("Confirm FAIL!");
						return;
					}
					serviceContext.daoIbatisTemplate.updateWithUserDefineSql("UPDATE fina_jobs SET bcpublicorderId = "+orderid+" WHERE id = "+publicBusinessOrderId+" AND isdelete = FALSE");
					for (int k = 0; k < requstfreight.size(); k++) {
						rqf = JSONObject.fromObject(requstfreight.get(k));
						String freightid = String.valueOf(rqf.get("bcFreightId"));
						String bizSystemFreightId = String.valueOf(rqf.get("bizSystemFreightId"));
						if(StrUtils.isNull(freightid)){
							continue;
						}
						serviceContext.daoIbatisTemplate.updateWithUserDefineSql("UPDATE fina_arap SET bcfreightid = "+freightid+" WHERE bcfreightid is null AND id = " + bizSystemFreightId);
					}
				}
				MessageUtils.alert("Confirm OK!");
			}else{
				MessageUtils.alert(String.valueOf(body.get("errMsg")));
			}
		} catch (Exception e1) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e1.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
		}
	}

	/**
	 * BMS费用取消审核
	 */
	public void cancelAudit(List<Map> list, ServiceContext serviceContext) {
		String url = ConfigUtils.findSysCfgVal("sys_bms_url");//获取系统配置UMS URL
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONArray bodyArray = new JSONArray();
		JSONObject bodyJson = null;
		String ret = "";
		try {
			for (int i = 0; i < list.size(); i++) {
				Map map2 = list.get(i);
				for (Object key : map2.keySet()) {
					if("bcfreightid".equals(String.valueOf(key)) && StrUtils.isNull(String.valueOf(map2.get("bcfreightid")))){
						MessageUtils.alert("CancleAudit OK!");
						return;
					}
					
					if(map2.get(key) == null || StrUtils.isNull(String.valueOf(map2.get(key)))){
			    		MessageUtils.alert(key +" cannot be empty!");
						return;
			    	}
				}
			}
			
			header.put("serviceCode", "CANCEL_AUDIT");
			for (int i = 0; i < list.size(); i++) {
				bodyJson = new JSONObject();
				bodyJson.put("bizSystemFreightId",list.get(i).get("id"));
				bodyJson.put("bcFreightId",String.valueOf(list.get(i).get("bcfreightid")));
				bodyArray.add(bodyJson);
			}
			json.put("header", header);
			json.put("body", bodyArray);
			ret = httpPost(url, json.toString());
			//同步信息到fina_arap表，同步数据到新增表
			if(ret == null){
				MessageUtils.alert("CancleAudit FAIL!");
				return;
			}
			JSONObject response = JSONObject.fromObject(ret);
			if(response.get("body") == null){
				MessageUtils.alert("CancleAudit FAIL!");
				return;
			}
			JSONObject body = JSONObject.fromObject(response.get("body"));
			if(body.containsKey("errMsg")&&"0".equals(body.get("errCode"))){
				MessageUtils.alert("CancleAudit OK!");
				//取消审核成功 
				for (int i = 0; i < list.size(); i++) {
					long arapid = Long.valueOf(String.valueOf(list.get(i).get("id")));
					if(arapid<1){
						continue;
					}
					serviceContext.daoIbatisTemplate.updateWithUserDefineSql("UPDATE fina_arap SET bcfreightid = null WHERE id = " + arapid);
				}
			}else{
				MessageUtils.alert(String.valueOf(body.get("errMsg")));
			}
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
		}
	}
	
//	/**
//	 * 订单同步BMS（待测试，代码暂时上传）
//	 */
//	@Action
//	public void orderSyn(Map map, ServiceContext serviceContext) {
//		String url = ConfigUtils.findSysCfgVal("sys_bms_url");//获取系统配置UMS URL
//		try {
//			for (Object key : map.keySet()) {
//				if(map.get(key) == null || StrUtils.isNull(String.valueOf(map.get(key)))){
//		    		MessageUtils.alert(key +" cannot be empty!");
//					return;
//		    	}
//			}
//			JSONObject body = new JSONObject();
//			body.put("type", "lockF");//类型  费用审核：audit
//			
//			//订单数据
//			JSONArray lockOrder = new JSONArray();
//			JSONObject order = new JSONObject();
//			order.put("publicBusinessOrderId", map.get("publicbusinessorderid"));//工作单ID
//			order.put("publicIsApproval", "N");//是否报批 Y:是 N：否  默认为N
//			order.put("publicApprovalPersonCode", AppUtils.getUserSession().getUsercode());//订单上报人ID
//			order.put("publicApprovalPersonName", AppUtils.getUserSession().getUsername());//订单上报人中文名
//			order.put("publicApprovalDate", map.get("publicapprovaldate"));//上报时间
//			order.put("publicBusinessType", map.get("publicbusinesstype"));//业务类型 
//			order.put("publicJobNo", map.get("publicjobno"));//工作单编号
//			order.put("publicSettleOffice", map.get("publicsettleoffice"));//分公司MZC码
//			order.put("publicSettleOfficeName", map.get("publicsettleofficename"));//分公司中文名
//			order.put("creator", map.get("creator"));//创建人
//			order.put("createTime", map.get("createtime"));//创建时间
//			order.put("publicBusinessDate", map.get("publicbusinessdate"));//工作单日期
//			order.put("publicBusinessTypeName",  map.get("publicbusinesstypename"));//业务类型名称
//			order.put("cimcID",  map.get("cimcid"));//中集统一订单号ID
//			order.put("cimcNo",  map.get("cimcno"));//中集统一订单号
//			lockOrder.add(order);
//			
//			body.put("lockOrder",lockOrder);
//			
//			JSONObject request = new JSONObject();
//			JSONObject header = new JSONObject();
//			header.put("serviceCode", "BMS_LOCK_SYN");
//			request.put("header", header);
//			request.put("body", body);
//			String ret = httpPost(url, request.toString());
//			//同步信息到fina_arap表，同步数据到新增表 
//			if(ret == null){
//				MessageUtils.alert("Audit FAIL!");
//				return;
//			}
//			JSONObject response = JSONObject.fromObject(ret);
//			if(response.get("body") == null){
//				MessageUtils.alert("Audit FAIL!");
//				return;
//			}
//			JSONObject reqbody = JSONObject.fromObject(response.get("body"));
//			if(reqbody.containsKey("errMsg")&&"0".equals(reqbody.get("errCode"))){
//				MessageUtils.alert("Audit OK!");
//				//审核成功 
//			}else{
//				MessageUtils.alert(String.valueOf(reqbody.get("errMsg")));
//			}
//		} catch (Exception e) {
//			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
//			SysLog syslog = new SysLog();
//			syslog.setInputer(AppUtils.getUserSession().getUsername());
//			syslog.setLogdesc(e.toString());
//			syslog.setLogtime(new Date());
//			sysLogDao.create(syslog);
//		}
//	}
//	/**
//	 * 订单上报BMS（待测试，代码暂时上传）
//	 */
//	@Action
//	public void orderSyn(Map map, ServiceContext serviceContext) {
//		String url = ConfigUtils.findSysCfgVal("sys_bms_url");//获取系统配置UMS URL
//		try {
//			for (Object key : map.keySet()) {
//				if(map.get(key) == null || StrUtils.isNull(String.valueOf(map.get(key)))){
//		    		MessageUtils.alert(key +" cannot be empty!");
//					return;
//		    	}
//			}
//			JSONObject body = new JSONObject();
//			body.put("type", "lockF");//类型  费用审核：audit
//			
//			//订单数据
//			JSONArray lockOrder = new JSONArray();
//			JSONObject order = new JSONObject();
//			order.put("publicBusinessOrderId", map.get("publicbusinessorderid"));//工作单ID
//			order.put("publicIsApproval", "N");//是否报批 Y:是 N：否  默认为N
//			order.put("publicApprovalPersonCode", AppUtils.getUserSession().getUsercode());//订单上报人ID
//			order.put("publicApprovalPersonName", AppUtils.getUserSession().getUsername());//订单上报人中文名
//			order.put("publicApprovalDate", map.get("publicapprovaldate"));//上报时间
//			order.put("publicBusinessType", map.get("publicbusinesstype"));//业务类型 
//			order.put("publicJobNo", map.get("publicjobno"));//工作单编号
//			order.put("publicSettleOffice", map.get("publicsettleoffice"));//分公司MZC码
//			order.put("publicSettleOfficeName", map.get("publicsettleofficename"));//分公司中文名
//			order.put("creator", map.get("creator"));//创建人
//			order.put("createTime", map.get("createtime"));//创建时间
//			order.put("publicBusinessDate", map.get("publicbusinessdate"));//工作单日期
//			order.put("publicBusinessTypeName",  map.get("publicbusinesstypename"));//业务类型名称
//			order.put("cimcID",  map.get("cimcid"));//中集统一订单号ID
//			order.put("cimcNo",  map.get("cimcno"));//中集统一订单号
//			lockOrder.add(order);
//			
//			body.put("lockOrder",lockOrder);
//			
//			JSONObject request = new JSONObject();
//			JSONObject header = new JSONObject();
//			header.put("serviceCode", "BMS_LOCK_SYN");
//			request.put("header", header);
//			request.put("body", body);
//			String ret = httpPost(url, request.toString());
//			//同步信息到fina_arap表，同步数据到新增表 
//			if(ret == null){
//				MessageUtils.alert("Audit FAIL!");
//				return;
//			}
//			JSONObject response = JSONObject.fromObject(ret);
//			if(response.get("body") == null){
//				MessageUtils.alert("Audit FAIL!");
//				return;
//			}
//			JSONObject reqbody = JSONObject.fromObject(response.get("body"));
//			if(reqbody.containsKey("errMsg")&&"0".equals(reqbody.get("errCode"))){
//				MessageUtils.alert("Audit OK!");
//				//审核成功
//			}else{
//				MessageUtils.alert(String.valueOf(reqbody.get("errMsg")));
//			}
//		} catch (Exception e) {
//			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
//			SysLog syslog = new SysLog();
//			syslog.setInputer(AppUtils.getUserSession().getUsername());
//			syslog.setLogdesc(e.toString());
//			syslog.setLogtime(new Date());
//			sysLogDao.create(syslog);
//		}
//	}
	
	/**
	 * 账单确认
	 */
	public boolean synCheckBillsToBms(List<Map> list, ServiceContext serviceContext){
		String url = ConfigUtils.findSysCfgVal("sys_bms_url");//获取系统配置UMS URL
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			for (Object key : map.keySet()) {
				if("bcfreightid".equals(String.valueOf(key))){
					continue;
				}
				if(map.get(key) == null || StrUtils.isNull(String.valueOf(map.get(key)))){
		    		MessageUtils.alert(key +" cannot be empty!");
					return false;
		    	}
			}
		}
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONArray body = new JSONArray();
		JSONObject bodyJson = new JSONObject();
		JSONArray freightInfo = new JSONArray();
		JSONArray reserveFundInfo = new JSONArray();
		JSONArray rateInfo = new JSONArray();
    	try {
    		bodyJson.put("bizLedgerCompId", list.get(0).get("bizledgercompid"));//业务系统账单ID
    		bodyJson.put("bizCompNo", list.get(0).get("bizcompno"));//业务系统账单号
    		
    		bodyJson.put("bizSystemType", "UFMS");//来源系统
    		bodyJson.put("rpFlag", String.valueOf(list.get(0).get("rpflag")).replace("A", ""));//收付标识 R/P
    		bodyJson.put("settleOffice", list.get(0).get("settleoffice"));//分公司mzccode
    		bodyJson.put("settleOfficeName", list.get(0).get("settleofficename"));//分公司名称
    		bodyJson.put("settleCustCode", list.get(0).get("settlecustcode"));//客商辅助编码
    		bodyJson.put("settleCustName", list.get(0).get("settlecustname"));//客商中文名
    		bodyJson.put("currencyCode", list.get(0).get("currencycode"));//币制
    		bodyJson.put("amount", list.get(0).get("amount"));//账单金额
    		bodyJson.put("confirmStatus", "Y");//账单确认状态
    		bodyJson.put("confirmDate", list.get(0).get("confirmdate"));//确认时间
    		bodyJson.put("confirmPersonCode", AppUtils.getUserSession().getUsercode());//确认人code
    		bodyJson.put("confirmPersonName", AppUtils.getUserSession().getUsername());//确认人名称
    		bodyJson.put("creator", list.get(0).get("creator"));//创建人
    		bodyJson.put("createTime", list.get(0).get("createtime"));//创建时间
    		
    		for (int i = 0; i < list.size(); i++) {
    			JSONObject freightJson = new JSONObject();
    			freightJson.put("bizSystemFreightId", list.get(i).get("id"));//业务系统费用ID
//    			freightJson.put("bcFreightId", list.get(i).get("bcfreightid"));//BMS费用ID
    			
    			freightInfo.add(freightJson);
			}
    		bodyJson.put("freightInfo", freightInfo);
    		bodyJson.put("reserveFundInfo", reserveFundInfo);
    		bodyJson.put("rateInfo", rateInfo);
    		body.add(bodyJson);
    		json.put("body", body);
    		header.put("serviceCode", "ADD_COMPS");
    		json.put("header", header);
    		
    		String ret = httpPost(url, json.toString());
			//账单确认成功
    		if(ret == null){
				MessageUtils.alert("Confirm FAIL!");
				return false;
			}
			JSONObject response = JSONObject.fromObject(ret);
			if(response.get("body") == null){
				MessageUtils.alert("Confirm FAIL!");
				return false;
			}
			JSONObject retbody = JSONObject.fromObject(response.get("body"));
			if(retbody.containsKey("errMsg")&&"0".equals(retbody.get("errCode"))){
				//账单确认成功 
				String bizLedgerCompId = String.valueOf(list.get(0).get("bizledgercompid"));
				if(StrUtils.isNull(bizLedgerCompId)){
					MessageUtils.alert("Confirm FAIL!");
					return false;
				}
				JSONArray compbackinfo = JSONArray.fromObject(retbody.get("CompBackInfo"));
				if(compbackinfo.size()>0 && null != compbackinfo.get(0)){
					JSONObject compbackJson = JSONObject.fromObject(compbackinfo.get(0));
					String bcledgercompid = null == compbackJson.get("bcLedgerCompId") ? null : String.valueOf(compbackJson.get("bcLedgerCompId"));
					serviceContext.daoIbatisTemplate.updateWithUserDefineSql("UPDATE fina_bill SET bcledgercompid = "+bcledgercompid+" WHERE isdelete = false AND id = " + bizLedgerCompId);
					MessageUtils.alert("Confirm OK!");
				}else{
					MessageUtils.alert("Confirm FAIL!");
					return false;
				}
			}else{
				MessageUtils.alert(String.valueOf(retbody.get("errMsg")));
				return false;
			}
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.alert(e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * 账单取消确认
	 */
	public boolean cancleCheckBillsToBms(List<Map> list, ServiceContext serviceContext){
		String url = ConfigUtils.findSysCfgVal("sys_bms_url");//获取系统配置UMS URL
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			for (Object key : map.keySet()) {
				if("bcfreightid".equals(String.valueOf(key))){
					continue;
				}
				if(map.get(key) == null || StrUtils.isNull(String.valueOf(map.get(key)))){
		    		MessageUtils.alert(key +" cannot be empty!");
					return false;
		    	}
			}
		}
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONArray body = new JSONArray();
		JSONObject bodyJson = new JSONObject();
    	try {
    		bodyJson.put("bizLedgerCompId", list.get(0).get("bizledgercompid"));//业务系统账单ID
    		bodyJson.put("bizCompNo", list.get(0).get("bizcompno"));//业务系统账单号
    		body.add(bodyJson);
    		json.put("body", body);
    		header.put("serviceCode", "DEL_COMPS");
    		json.put("header", header);
			
    		String ret = httpPost(url, json.toString());
    		
    		//账单取消确认成功
    		if(ret == null){
				MessageUtils.alert("ConfirmCancle FAIL!");
				return false;
			}
			JSONObject response = JSONObject.fromObject(ret);
			if(response.get("body") == null){
				MessageUtils.alert("ConfirmCancle FAIL!");
				return false;
			}
			JSONObject retbody = JSONObject.fromObject(response.get("body"));
			if(retbody.containsKey("errMsg")&&"0".equals(retbody.get("errCode"))){
				//取消审核成功 
				String bizLedgerCompId = String.valueOf(list.get(0).get("bizledgercompid"));
				if(StrUtils.isNull(bizLedgerCompId)){
					MessageUtils.alert("ConfirmCancle FAIL!");
					return false;
				}
				//账单表填入BMS系统的账单ID
				serviceContext.daoIbatisTemplate.updateWithUserDefineSql("UPDATE fina_bill SET bcledgercompid = null WHERE isdelete = false AND id = " + bizLedgerCompId);
				MessageUtils.alert("ConfirmCancle OK!");
			}else{
				MessageUtils.alert(String.valueOf(retbody.get("errMsg")));
				return false;
			}
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.alert(e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * 付款申请（跳转到BMS请款）
	 * loginBms()
	 */
	public void expense(String url){
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url); //windows打开网页
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.alert(e.toString());
		}
	}
	
	/**
	 * 接受BMS发票信息
	 * loginBms()
	 */
	public void receiveInvoiceInfo(String jsonStr){
		try {
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.alert(e.toString());
		}
	}
	
	
    /*
     * http post 请求raw
     */
    public String httpPost(String url, String rawBody){
    	String msg = "";
        HttpURLConnection conn = null;
        PrintWriter pw = null ;
        BufferedReader rd = null ;
        StringBuilder sb = new StringBuilder ();
        String line = null ;
        String response = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            pw = new PrintWriter(conn.getOutputStream());
            pw.print(rawBody);
            pw.flush();
            rd  = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null ) {
                sb.append(line);
            }
            response = sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            response = null;
        } catch (IOException e) {
            e.printStackTrace();
            MessageUtils.alert(e.getMessage());
            response = null;
        }finally{
            try {
                if(pw != null){
                    pw.close();
                }
                if(rd != null){
                    rd.close();
                }
                if(conn != null){
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
    
    /**
     * 检查字段是否为空
     * @param obj
     * @param paramName
     */
    public void checkNull(Object obj, Object paramName){
    	if(obj == null || StrUtils.isNull(String.valueOf(obj))){
    		MessageUtils.alert(paramName +" cannot be empty!");
			return;
    	}
    }
    
    @Action(method="synstart")
    public void synStart(){
    	String json = "";
//    	String pathname = "C:/Users/Administrator/Desktop/JSON.txt";
//    	String pathname = "C:/Users/Administrator/Desktop/税率JSON.txt";
//    	String pathname = "C:/Users/Administrator/Desktop/收款发票JSON.txt";
//    	String pathname = "C:/Users/Administrator/Desktop/成本票JSON.txt";
//    	String pathname = "C:/Users/Administrator/Desktop/付款申请JSON.txt";
    	String pathname = "C:/Users/Administrator/Desktop/销账JSON.txt";
        try {
        	String code = resolveCode(pathname);
        	File file = new File(pathname);
         	InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is, code);//防止中文乱码
        	BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                // 逐行读取数据
                json += line;
            }
            br.close();
//            reJsonInfo(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * java获取raw
     * @author zengwei
     * @email 1014483974@qq.com
     * @version 2019年3月01日 下午4:10:04
     */
    public String readRaw(InputStream inputStream) {

        String result = "";
        try {
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }

            outSteam.close();
            inputStream.close();

            result = new String(outSteam.toByteArray(), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    /**
     * 检查txt文件编码格式
     * @param path
     * @return
     * @throws Exception
     */
    public String resolveCode(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);    
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK  
        if (head[0] == -1 && head[1] == -2 )    
            code = "UTF-16";    
        else if (head[0] == -2 && head[1] == -1 )    
            code = "Unicode";    
        else if(head[0]==-17 && head[1]==-69 && head[2] ==-65)    
            code = "UTF-8";    
            
        inputStream.close();  
          
        return code;  
    }  
    
    /**
     * 接受推送JSON
     * @param param
     */
    @Action(method="rejsoninfo")
    public String reJsonInfo(HttpServletRequest request){
    	ServletInputStream inputstream = null;
    	try {
    		inputstream = request.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
			return "ERROR";
		}
		String jsonstr = readRaw(inputstream);
    	JSONObject json = JSONObject.fromObject(jsonstr);
    	if(null == json || null == json.get("header") || null == json.get("body")){
    		return "NULL";
    	}
    	try {
        	Object obj = json.get("header");
        	if("UFMS_RATE_UPDATE".equals(JSONObject.fromObject(json.get("header")).get("serviceCode"))){//费用
        		synFeeitem(JSONArray.fromObject(json.get("body")));
        	}else if("UFMS_RATE_UPDATE".equals(JSONObject.fromObject(json.get("header")).get("serviceCode"))){//汇率
        		synCurrency(JSONObject.fromObject(json.get("body")).getJSONArray("mdExchangeRate"));
        	}else if("UFMS_AR_INVOICE_UPDATE".equals(JSONObject.fromObject(json.get("header")).get("serviceCode"))){//收款发票信息回填 
        		synInvoice(JSONObject.fromObject(json.get("body")).getJSONArray("freightInfo"));
        	}else if("UFMS_AP_INVOICE_UPDATE".equals(JSONObject.fromObject(json.get("header")).get("serviceCode"))){//成本票信息回填 
        		synInvoice(JSONObject.fromObject(json.get("body")).getJSONArray("freightInfo"));
        	}else if("UFMS_RPREQ_UPDATE".equals(JSONObject.fromObject(json.get("header")).get("serviceCode"))){//付款申请回填 
        		synpayment(JSONObject.fromObject(json.get("body")).getJSONArray("freightInfo"));
        	}else if("UFMS_ACTPAYREC_UPDATE".equals(JSONObject.fromObject(json.get("header")).get("serviceCode"))){//销账信息回填 
        		synWriteOffResult(JSONObject.fromObject(json.get("body")).getJSONArray("freightInfo"));
        	}else if("UFMS_FINANCIAL_LOCK_UPDATE".equals(JSONObject.fromObject(json.get("header")).get("serviceCode"))){//财务锁凭证锁状态变更 
        		synFinancialLock(JSONArray.fromObject(json.get("body")));//暂无JSON
        	}else if("UFMS_AMEND_FEEITEM_UPDATE".equals(JSONObject.fromObject(json.get("header")).get("serviceCode"))){//财务锁凭证锁状态变更 
        		synFinancialLock(JSONArray.fromObject(json.get("body")));//暂无JSON
        	}
		} catch (Exception e) {
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			return "ERROR";
		}
		return "SUCCESS";
    }
    
    /**
     * 接受费目信息
     * @param jsonObject
     */
    public void synFeeitem(JSONArray array){
    	ServiceContext serviceContext =(ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
    	for (int i = 0; i < array.size(); i++) {
	    	JSONObject json = array.getJSONObject(i);
	    	if(null == json){
	    		return;
	    	}
	    	String id = String.valueOf(json.get("mdFreightCodeId"));
	    	String name = String.valueOf(json.get("freightNameCn"));
	    	String freightcode = String.valueOf(json.get("freightCode"));
	    	String freighttype = String.valueOf(json.get("freightType"));
	    	if(StrUtils.isNull(id) || StrUtils.isNull(name) || StrUtils.isNull(freightcode) || StrUtils.isNull(freighttype)){
	    		continue;
	    	}
	    	
    		Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT count(1) FROM dat_feeitem WHERE isdelete = FALSE AND id = "+id+";");
    		if(null != map.get("count") && "0".equals(String.valueOf(map.get("count")))){
    			String sql = "INSERT INTO dat_feeitem  (id,code,name,freighttype,freightcode,isdelete,inputtime,inputer,ispublic) " +
    					"VALUES ("+id+",'"+freightcode+"','"+name+"','"+freighttype+"','"+freightcode+"',FALSE,NOW(),'BMS',TRUE);";
    			//ID,代码，名称，BMS类型，BMS编码，录入时间，录入人，默认公共
    			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
    		}else{
    			serviceContext.daoIbatisTemplate.updateWithUserDefineSql("UPDATE dat_feeitem SET name ='" + name + "',freighttype='"+freighttype+"',freightcode='"+freightcode+"' WHERE id =" +id);
    		}
    	}
    }
    
    /**
     * 接受币别汇率信息
     * @param array
     */
    public void synCurrency(JSONArray array){
    	ServiceContext serviceContext =(ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
    	for (int i = 0; i < array.size(); i++) {
	    	JSONObject json = array.getJSONObject(i);
	    	if(null == json){
	    		return;
	    	}
	    	String id = String.valueOf(json.get("mdCurrencyId"));//ID
	    	String currencyfm = String.valueOf(json.get("currencyFrom"));//原币种
	    	String currencyto = String.valueOf(json.get("currencyTo"));//折算币种
	    	String rate = String.valueOf(json.get("rate"));//汇率
	    	String datafm = String.valueOf(json.get("effDate"));//有效日期
	    	String datato = String.valueOf(json.get("lefDate"));//失效日期
	    	String inputer = AppUtils.getUserSession().getUsercode();;//录入人
	    	String inputtime = String.valueOf(json.get("createTime"));//录入时间
	    	if(StrUtils.isNull(id) || StrUtils.isNull(currencyfm) || StrUtils.isNull(currencyto) || StrUtils.isNull(rate) || StrUtils.isNull(datafm)){
	    		continue;
	    	}
	    	boolean flag = StrUtils.isNull(datato) || "null".equals(datato);
	    	
	    	String sql = "INSERT INTO dat_exchangerate (id,currencyfm,currencyto,rate,datafm,"+(flag?"":"datato,")+"inputer,inputtime,isdelete,xtype) " +
	    			"VALUES ("+id+",'"+currencyfm+"','"+currencyto+"','"+rate+"','"+datafm+"',"+(flag?"":"'"+datato+"',")+"'"+inputer+"','"+inputtime+"',FALSE,'*');";
	    	//ID,原币，折合币，兑换率，期间起，期间止，录入人，录入时间，是否有效，折算符
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
    	}
    }
    
    /**
     * 接受收款发票信息
     * @param param
     */
    public void synInvoice(JSONArray array){
    	ServiceContext serviceContext =(ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
    	
    	String sql = "";
    	for (int i = 0; i < array.size(); i++) {
    		JSONObject json = new JSONObject();
    		json = array.getJSONObject(i);
	    	String arapid = "null".equals(String.valueOf(json.get("bizSystemFreightId"))) ? "" : String.valueOf(json.get("bizSystemFreightId"));//费用ID
	    	String invoiceno = "null".equals(String.valueOf(json.get("invoiceNo"))) ? "" : String.valueOf(json.get("invoiceNo"));//发票号
	    	String invstatus = "null".equals(String.valueOf(json.get("invoiceStatus")))?"":String.valueOf(json.get("invoiceStatus"));//发票状态
	    	boolean isdelete = false;
	    	if(StrUtils.isNull(arapid) || StrUtils.isNull(invstatus)){
	    		continue;
	    	}
	    	
	    	String insql = "".equals(invoiceno)?"":",bmsinvno='"+invoiceno+"'";
			if(!isdelete){
				sql += "UPDATE fina_arap SET bmsinvstatus ='"+invstatus+"'"+insql+" WHERE isdelete = FALSE AND id="+arapid+";";
			}else{
				sql += "UPDATE fina_arap SET bmsinvstatus ='',bmsinvno='' WHERE isdelete = FALSE AND id="+arapid+";";
			}
    	}
    	serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
    }
    
    /**
     * 接收付款申请信息
     * @param param
     */
    public void synpayment(JSONArray array){
    	ServiceContext serviceContext =(ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
    	
    	String sql = "";
    	for (int i = 0; i < array.size(); i++) {
    		JSONObject json = new JSONObject();
    		json = array.getJSONObject(i);
	    	String arapid = "null".equals(String.valueOf(json.get("bizSystemFreightId"))) ? "" : String.valueOf(json.get("bizSystemFreightId"));//费用ID
	    	String isapprove = "null".equals(String.valueOf(json.get("requestStatus")))?"":String.valueOf(json.get("requestStatus"));//付款申请审核状态
	    	String bmsreqno = "null".equals(String.valueOf(json.get("requestNo"))) ? "" : String.valueOf(json.get("requestNo"));//BMS付款申请编号
	    	boolean isdelete = false;
	    	if(StrUtils.isNull(arapid) || StrUtils.isNull(bmsreqno)){
	    		continue;
	    	}
	    	
			if(!isdelete){
				sql = "UPDATE fina_arap SET bmsreqstatus ='"+isapprove+"',bmsreqno='"+bmsreqno+"' WHERE isdelete = FALSE AND id="+arapid+";";
			}else{
				sql = "UPDATE fina_arap SET bmsreqstatus ='',bmsreqno='' WHERE isdelete = FALSE AND id="+arapid+";";
			}
    	}
    	serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
    }
    
    /**
     * 接收销账信息
     * @param param
     * @throws ParseException 
     */
    public void synWriteOffResult(JSONArray array) throws ParseException{
    	ServiceContext serviceContext =(ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
    	FinaActpayrec finaactpayrec = new FinaActpayrec();
    	
//    	String actsql = "";//记录销账表需要的SQL
    	String sql = "";
    	double amounts = 0d;
    	String recsql = "";
    	List<FinaActpayrecdtl> dtllist = new  ArrayList<FinaActpayrecdtl>();
    	for (int i = 0; i < array.size(); i++) {
    		FinaActpayrecdtl recdtl = new FinaActpayrecdtl();
    		JSONObject json = new JSONObject();
    		json = array.getJSONObject(i);
	    	String arapid = "null".equals(String.valueOf(json.get("bizSystemFreightId"))) ? "" : String.valueOf(json.get("bizSystemFreightId"));//费用ID
	    	String bmsactpayrecid = "null".equals(String.valueOf(json.get("bcAllocationEventId"))) ? "" : String.valueOf(json.get("bcAllocationEventId"));//BMS销账单ID
	    	String bmswriteoffStatus = "null".equals(String.valueOf(json.get("allocationStatus")))?"":String.valueOf(json.get("allocationStatus"));//销账状态
	    	String bmswriteoffno = "null".equals(String.valueOf(json.get("allocationNo"))) ? "" : String.valueOf(json.get("allocationNo"));//BMS销账编号
	    	String amount = "null".equals(String.valueOf(json.get("allocatedPrimeCurrencyValue"))) ? "" : String.valueOf(json.get("allocatedPrimeCurrencyValue"));//销账原币金额
	    	String actpayrecdate = "null".equals(String.valueOf(json.get("allocationDate"))) ? "" : String.valueOf(json.get("allocationDate"));//销账日期
	    	String inputer = AppUtils.getUserSession().getUsercode();//最后修改人
	    	String inputtime = "null".equals(String.valueOf(json.get("lastModifyTime"))) ? "" : String.valueOf(json.get("lastModifyTime"));//最后修改时间
	    	String rate = "null".equals(String.valueOf(json.get("allocationExchangeRate"))) ? "" : String.valueOf(json.get("allocationExchangeRate"));//汇率
	    	double xrate = Double.valueOf(rate);
	    	String currencyfrom = "CNY";
	    	if(xrate > 6 && xrate < 7){
	    		currencyfrom = "USD";
	    	}
	    	String currencyto = "CNY";
	    	boolean isdelete = false;
	    	if(StrUtils.isNull(arapid) || StrUtils.isNull(bmswriteoffno) || StrUtils.isNull(amount) || StrUtils.isNull(actpayrecdate)){
	    		continue;
	    	}
	    	amounts = amounts + Double.valueOf(amount);
	    	
	    	String s = "SELECT COUNT(1),COALESCE((SELECT actpayrecid FROM fina_actpayrecdtl WHERE isdelete = FALSE AND arapid = "+arapid+"),0) AS id" +
	    			",COALESCE((SELECT id FROM fina_actpayrecdtl WHERE isdelete = FALSE AND arapid = "+arapid+"),0) AS actpayrecdtlid" +
	    			",(SELECT customerid FROM fina_arap WHERE isdelete = FALSE AND id = "+arapid+") AS customerid" +
	    			",(SELECT actpayrecno FROM fina_actpayrec WHERE isdelete = FALSE AND id = COALESCE((SELECT actpayrecid FROM fina_actpayrecdtl WHERE isdelete = FALSE AND arapid = "+arapid+"),0))" +
			" FROM fina_actpayrec WHERE EXISTS (SELECT 1 FROM fina_actpayrecdtl WHERE isdelete = FALSE AND arapid = "+arapid+");";
	    	Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(s);
	    	
	    	String actpayrecid = "0";
	    	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
	    	finaactpayrec.setAmount(BigDecimal.valueOf(Double.valueOf(amounts)));
	    	finaactpayrec.setActpayrecdate(new Date(Long.valueOf(actpayrecdate)));
	    	finaactpayrec.setIsdelete(isdelete);
	    	finaactpayrec.setCurrency(currencyto);
	    	finaactpayrec.setClientid(Long.valueOf(String.valueOf(map.get("customerid"))));
	    	finaactpayrec.setCorpid(AppUtils.getUserSession().getCorpid());
	    	
	    	String recdtlid = "0";
	    	recdtl.setArapid(Long.valueOf(arapid));
	    	recdtl.setXrate(BigDecimal.valueOf(Double.valueOf(rate)));
	    	recdtl.setAmountwf(BigDecimal.valueOf(Double.valueOf(amount)));
	    	recdtl.setAmountrp(BigDecimal.valueOf(Double.valueOf(amount)));
	    	recdtl.setCurrencyfm(currencyfrom);
	    	recdtl.setCurrencyto(currencyto);
	    	recdtl.setIsdelete(isdelete);
	    	recdtl.setCorpid(AppUtils.getUserSession().getCorpid());
	    	if(null != map && "0".equals(String.valueOf(map.get("count"))) == false){
	    		finaactpayrec.setUpdater(inputer);
	    		finaactpayrec.setUpdatetime(sdf.parse(inputtime));
	    		actpayrecid = String.valueOf(map.get("id"));
	    		finaactpayrec.setId(Long.valueOf(actpayrecid));
	    		finaactpayrec.setActpayrecno(String.valueOf(map.get("actpayrecno")));
	    		
	    		recdtl.setUpdater(inputer);
		    	recdtl.setUpdatetime(sdf.parse(inputtime));
		    	recdtlid = String.valueOf(map.get("actpayrecdtlid"));
		    	recdtl.setId(Long.valueOf(recdtlid));
		    	recdtl.setActpayrecid(Long.valueOf(actpayrecid));
	    	}else{
	    		finaactpayrec.setInputtime(sdf.parse(inputtime));
	    		finaactpayrec.setInputer(inputer);
	    		finaactpayrec.setActpayrecno(bmswriteoffno);
	    		
	    		recdtl.setInputtime(sdf.parse(inputtime));
		    	recdtl.setInputer(inputer);
	    	}
	    	
			if(!isdelete){
				sql += "UPDATE fina_arap SET bmswriteoffStatus ='"+bmswriteoffStatus+"',bmswriteoffno='"+bmswriteoffno+"' WHERE isdelete = FALSE AND id="+arapid+";";
				List<FinaActpayrec> magas= new ArrayList<FinaActpayrec>();
				magas.add(finaactpayrec);
				List<String> filterField=new ArrayList<String>();
				String maindbText = AppUtils.modelBeanToJSON(filterField, magas);
				maindbText = StrUtils.getSqlFormat(maindbText);
				
				dtllist.add(recdtl);
				List<String> filterField2=new ArrayList<String>();
				String dtldbText = AppUtils.modelBeanToJSON(filterField2, dtllist);
				dtldbText = StrUtils.getSqlFormat(dtldbText);
				
				recsql = "SELECT f_fina_actpayrec('actpayrecid="+actpayrecid+";'"+
						",'"+maindbText+"'"+
						",'"+dtldbText+"'"+
						",''"+
						",'"+AppUtils.getUserSession().getUserid()+"') AS ret;";
			}else{
				sql += "UPDATE fina_arap SET bmswriteoffStatus ='',bmswriteoffno='' WHERE isdelete = FALSE AND id="+arapid+";";
			}
			//修改费用行信息
    	}
    	sql += recsql;
    	serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
    }
    
    /**
     * 财务锁信息回填
     * @param param
     */
    public void synFinancialLock(JSONArray array){
    	ServiceContext serviceContext =(ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
    	
    	String sql = "";
    	for (int i = 0; i < array.size(); i++) {
    		JSONObject json = new JSONObject();
    		json = array.getJSONObject(i);
	    	String arapid = "null".equals(String.valueOf(json.get("bizSystemFreightId"))) ? "" : String.valueOf(json.get("bizSystemFreightId"));//费用ID
	    	String isapprove = "null".equals(String.valueOf(json.get("requestStatus")))?"":String.valueOf(json.get("requestStatus"));//付款申请审核状态
	    	String bmsreqno = "null".equals(String.valueOf(json.get("requestNo"))) ? "" : String.valueOf(json.get("requestNo"));//BMS付款申请编号
	    	boolean isdelete = false;
	    	if(StrUtils.isNull(arapid) || StrUtils.isNull(bmsreqno)){
	    		continue;
	    	}
	    	
			if(!isdelete){
				sql = "UPDATE fina_arap SET bmsreqstatus ='"+isapprove+"',bmsreqno='"+bmsreqno+"' WHERE isdelete = FALSE AND id="+arapid+";";
			}else{
				sql = "UPDATE fina_arap SET bmsreqstatus ='',bmsreqno='' WHERE isdelete = FALSE AND id="+arapid+";";
			}
    	}
    	serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
    }
    
    /**
     * 红冲蓝补（增减费用）费用信息回填
     * @param param
     */
    public void synAmendFeeitem(JSONArray array){
    	ServiceContext serviceContext =(ServiceContext) AppUtils.getBeanFromSpringIoc("serviceContext");
    	
    	String sql = "";
    	for (int i = 0; i < array.size(); i++) {
    		JSONObject json = new JSONObject();
    		json = array.getJSONObject(i);
	    	String arapid = "null".equals(String.valueOf(json.get("bizSystemFreightId"))) ? "" : String.valueOf(json.get("bizSystemFreightId"));//费用ID
	    	String isapprove = "null".equals(String.valueOf(json.get("requestStatus")))?"":String.valueOf(json.get("requestStatus"));//付款申请审核状态
	    	String bmsreqno = "null".equals(String.valueOf(json.get("requestNo"))) ? "" : String.valueOf(json.get("requestNo"));//BMS付款申请编号
	    	boolean isdelete = false;
	    	if(StrUtils.isNull(arapid) || StrUtils.isNull(bmsreqno)){
	    		continue;
	    	}
	    	
			if(!isdelete){
				sql = "UPDATE fina_arap SET bmsreqstatus ='"+isapprove+"',bmsreqno='"+bmsreqno+"' WHERE isdelete = FALSE AND id="+arapid+";";
			}else{
				sql = "UPDATE fina_arap SET bmsreqstatus ='',bmsreqno='' WHERE isdelete = FALSE AND id="+arapid+";";
			}
    	}
    	serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
    }
}
