package com.scp.service.finance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.base.ApplicationConf;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaArapDao;
import com.scp.dao.finance.FinaJobsDao;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;

@Component
public class ArapMgrService {

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	@Resource
	public FinaArapDao finaArapDao;
	
	@Resource
	public FinaJobsDao finaJobsDao;

	@Resource
	public ApplicationConf applicationConf;


	public Long saveDataReturnId(FinaArap data) {
		//系统设置中结算方式必填
		String sql = "SELECT EXISTS(SELECT * FROM sys_config WHERE key = 'sys_settlement_method' AND val = 'Y') AS issettlement;";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		boolean issettlement = Boolean.parseBoolean(m.get("issettlement").toString());
		if(issettlement&&StrUtils.isNull(data.getPayplace())){
			throw new RuntimeException("<DB Exception>: 结算方式设置为必填");
		}
		if (0 == data.getId()) {
			Long feeId = finaArapDao.create(data);
			return feeId;
		} else {
			finaArapDao.modify(data);
			return null;
		}
	}

	
	public void saveData(FinaArap data) {
		//系统设置中结算方式必填
		String sql = "SELECT EXISTS(SELECT * FROM sys_config WHERE key = 'sys_settlement_method' AND val = 'Y') AS issettlement;";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		boolean issettlement = Boolean.parseBoolean(m.get("issettlement").toString());
		if(issettlement&&StrUtils.isNull(data.getPayplace())){
			throw new RuntimeException("<DB Exception>: 结算方式设置为必填");
		}
		if (0 == data.getId()) {
			finaArapDao.create(data);
		} else {
			finaArapDao.modify(data);
		}
	}

	public void removeDate(String[] ids, String user) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			if(!StrUtils.isNumber(id))continue;
			String sql = "\nUPDATE fina_arap SET isdelete = TRUE , updater = '"
					+ user + "' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuilder.append(sql);
		}
		String execSql = stringBuilder.toString();
		if (!StrUtils.isNull(execSql)) {
			finaArapDao.executeSQL(execSql);
		}
	}

	public String shareFee(String[] ids, String user) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nSELECT f_fina_fee_share('id=" + ids[i] + ";user="
					+ user + "');";
			List list = finaArapDao.executeQuery(sql);
			stringBuilder.append(list.toString() + "\n");
		}

		return stringBuilder.toString();
	}

	/**
	 * 批量修改
	 * 
	 * @param ids
	 * @param araptype
	 * @param ppcctype
	 * @param feeitemid
	 * @param arapdate
	 * @param customercode
	 * @param pricenotax
	 * @param parities
	 * @param piece
	 * @param UserCode
	 */
	public void editParities(String[] ids, String araptype, String ppcctype,String currency,
			Long feeitemid, Date arapdate, Long customerid,
			String customercode, BigDecimal pricenotax, BigDecimal parities,
			BigDecimal piece, BigDecimal corpid, BigDecimal corpid2,
			String UserCode,String remarks,String sharetype,String payplace) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String idss = StrUtils.array2List(ids);
		String sql = "select f_bus_arap_edit('idss=" + idss + ";parities="
				+ parities + ";araptype=" + araptype + ";ppcctype=" + ppcctype
				+ ";currency=" + currency + ";feeitemid=" + feeitemid + ";arapdate=" + sdf.format(arapdate)
				+ ";customerid=" + customerid + ";customercode=" + customercode
				+ ";pricenotax=" + pricenotax + ";parities=" + parities
				+ ";piece=" + piece + ";corpid=" + corpid + ";corpid2="
				+ corpid2 + ";usercode=" + UserCode + ";remarks="+remarks+";sharetype="+sharetype+";payplace="+payplace+"');";
		finaArapDao.executeQuery(sql);
	}

	/**
	 * 根据jobid获取json格式未锁定费用条目
	 * 
	 * @param jobid
	 * @param arapIds
	 * @return
	 */
	public String getArapsJsonByJobid(Long jobid, String[] arapIds , Long userid , Long corpidCurrent) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n 	(SELECT");
		sb.append("\n 		ff.ID,");
		sb.append("\n 		ff.taxrate,");
		sb.append("\n 		ff.araptype,");
		sb.append("\n 		ff.arapdate,");
		sb.append("\n 		ff.customerid,");
		sb.append("\n 		ff.feeitemid,");
		sb.append("\n 		(SELECT y.name FROM dat_feeitem y WHERE y.id=ff.feeitemid) AS feeitemnamec,");
		sb.append("\n 		ff.amount,");
		sb.append("\n 		ff.currency,");
		sb.append("\n 		ff.remarks,");
		sb.append("\n 		ff.piece,");
		sb.append("\n 		ff.price,");
		sb.append("\n 		ff.jobid,");
		sb.append("\n 		ff.unit,");
		sb.append("\n 		ff.sharetype,");
		sb.append("\n 		(SELECT x.abbr FROM sys_corporation x WHERE x.id=ff.customerid) AS customercode,");
		sb.append("\n 		(SELECT x.namec FROM sys_corporation x WHERE x.id=ff.customerid) AS customernamec,");
		sb.append("\n 		ff.corpid,");
		sb.append("\n 		ff.ppcc,");
		sb.append("\n 		ff.payplace,");
		sb.append("\n 		ff.isamend,");
		sb.append("\n 		(SELECT fe.code AS feeiname FROM dat_feeitem fe WHERE fe. ID = ff.feeitemid)");
		sb.append("\n 		FROM");
		sb.append("\n 			fina_arap AS ff");
		sb.append("\n 		WHERE");
		sb.append("\n 		    ff.isdelete = FALSE");
		sb.append("\n 		AND (ff.parentid IS NULL)");
		sb.append("\n 		AND ff.rptype !='H'");
		sb.append("\n 		AND  EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid="+jobid+";userid="+userid+ ";corpidcurrent="+corpidCurrent+"') x WHERE x.id = ff.id)");
		sb.append("\n 		AND	jobid =" + jobid);
		sb.append("\n 		AND (");
		sb.append("\n 			   (isconfirm IS NOT NULL AND isconfirm = TRUE)");
		sb.append("\n 			OR (fmsdcno IS NOT NULL AND fmsdcno != '')");
		sb.append("\n 			OR (fmsinvno IS NOT NULL AND fmsinvno != '')");
		sb.append("\n 			OR (billid IS NOT NULL AND billid != 0)");
		sb.append("\n 			OR (invoiceid IS NOT NULL AND invoiceid != 0)");
		sb.append("\n 			OR (amtstl IS NOT NULL AND amtstl != 0)");
		sb.append("\n 			OR (amtstl2 IS NOT NULL AND amtstl2 != 0)");
		sb.append("\n 			OR EXISTS (SELECT 1 FROM fina_jobs WHERE ID = ff.jobid AND isdelete = FALSE AND islock = TRUE)");
		sb.append("\n 			OR EXISTS (SELECT 1 FROM fs_vch WHERE srcid = ff.jobid AND isdelete = FALSE)");
		sb.append("\n 		) = FALSE");
		if (arapIds != null) {
			sb.append("\n 		AND ( false ");
			for (String s : arapIds) {
				sb.append("\n 		OR ff.id = " + s);
			}
			sb.append("\n 		)");
		}
		
		String orderby = ConfigUtils.findSysCfgVal("arap_filter_orderby_inputtime");
		if(StrUtils.isNull(orderby) || "N".equals(orderby)){
			sb.append("\n ORDER BY araptype desc, customerid , feeitemid , currency ) T");
		}else{
			//map.put("orderSql", "araptype DESC,inputtime,");
			sb.append("\n ORDER BY araptype DESC,inputtime) T");
		}
		
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils
				.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sb.toString());
		////System.out.println(sb.toString());
		if (map != null && 1 == map.size()) {
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}

	/**
	 * 新建或更新运单费用
	 * 
	 * @param moduleList
	 */
	public void saveOrModify(List<FinaArap> moduleList) {
		for (FinaArap instance : moduleList) {
			finaArapDao.createOrModify(instance);
		}
	}

	/**
	 * 根据id删除运单费
	 * 
	 * @param lists
	 */
	public void removes(List<Long> lists) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Long id : lists) {
			String sql = "\nUPDATE fina_arap SET isdelete = TRUE, updater = '"
				+ AppUtils.getUserSession().getUsercode() + "' , updatetime = NOW()  WHERE id = "
				+ id + ";";
			stringBuffer.append(sql);
		}
		finaArapDao.executeSQL(stringBuffer.toString());
	}

	/**
	 * 批量修改费用
	 * 
	 * @param ids
	 *            费用ids
	 * @param unit20gp
	 * @param unit40gp
	 * @param unit40hq
	 * @param costother 
	 * @param cost40hq 
	 * @param cost40gp 
	 * @param cost20gp 
	 */
	public void batchUpdate(String[] ids, String unit20gp, String unit40gp,
			String unit40hq, String cost20gp, String cost40gp, String cost40hq, String costother) {
		String id = ids != null && ids.length > 0 ? ids[0] : "-1";
		String sql = "SELECT * FROM f_ship_cost_confirm_update('arapid="
				+ id.split("-")[0]
				+ ";unit20gp=" + unit20gp 
				+ ";unit40gp=" + unit40gp 
				+ ";unit40hq=" + unit40hq
				+ ";cost20gp=" + cost20gp
				+ ";cost40gp=" + cost40gp
				+ ";cost40hq=" + cost40hq
				+ ";costother=" + costother
				+ "');";
		this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
	}

	/**
	 * 商务中心费用确认批量修改
	 * @param modifiedData
	 */
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray json = (JSONArray) modifiedData;
		for (Object o : json) {
			JSONObject jsobj = (JSONObject) o;
			String[] ids = { String.valueOf(jsobj.get("arapid")) };
			String unit20gp = String.valueOf(jsobj.get("unit20gp"));
			String unit40gp = String.valueOf(jsobj.get("unit40gp"));
			String unit40hq = String.valueOf(jsobj.get("unit40hq"));
			
			String cost20gp = String.valueOf(jsobj.get("cost20gp"));
			String cost40gp = String.valueOf(jsobj.get("cost40gp"));
			String cost40hq = String.valueOf(jsobj.get("cost40hq"));
			String costother = String.valueOf(jsobj.get("costother"));
			
			this.batchUpdate(ids, unit20gp, unit40gp, unit40hq, cost20gp, cost40gp,cost40hq, costother);
		}
	}
	// String bookids = StrTools.array2List(ids);
	// //bookids = bookids.replaceAll(",", "-");
	// String
	// sql="SELECT f_bus_book_choose('bookingids="+bookids+";shipid="+shipid+";inputer="+user+";userid="+userid+"');";
	// busShipBookingDao.executeQuery(sql);
	/**
	 * 批量修改发票汇率
	 */
	public void updateRateForInvoice(JSONArray obj,String jobid,String invoiceid){
		for(int i = 0;i<obj.size();i++){
			JSONObject json = (JSONObject) obj.get(i);
			String currency = (String) json.get("currency");
			String xtype = (String) json.get("xtype");
			String rateStr = "";
			if(json.get("rate") instanceof Long){
				Long rate = (Long) json.get("rate");
				rateStr = String.valueOf(rate);
			}
			if(json.get("rate") instanceof Double){
				Double rate = (Double) json.get("rate");
				rateStr = String.valueOf(rate);
			}
			if(xtype==null||xtype.isEmpty()||rateStr==null||rateStr.isEmpty()){
				continue;
			}
			String sql = "UPDATE fina_arap SET invoicexrate = "+rateStr+",invoicextype = '"+xtype+"',invoiceamount= amount"+xtype+rateStr+" WHERE isdelete = FALSE AND jobid = "+jobid+" AND invoiceid = "+invoiceid+" AND currency = '"+currency+"'";
			this.finaArapDao.executeSQL(sql);
		}
		
	}
	/**
	 * 批量修改账单汇率
	 * @param obj
	 * @param jobid
	 * @param invoiceid
	 */
	public void updateRateForBusBill(JSONArray obj,String jobid,String billid){
		for(int i = 0;i<obj.size();i++){
			JSONObject json = (JSONObject) obj.get(i);
			String currency = (String) json.get("currency");
			String xtype = (String) json.get("xtype");
			String rateStr = "";
			if(json.get("rate") instanceof Long){
				Long rate = (Long) json.get("rate");
				rateStr = String.valueOf(rate);
			}
			if(json.get("rate") instanceof Double){
				Double rate = (Double) json.get("rate");
				rateStr = String.valueOf(rate);
			}
			if(xtype==null||xtype.isEmpty()||rateStr==null||rateStr.isEmpty()){
				continue;
			}
			String sql = "UPDATE fina_arap SET billxrate = "+rateStr+",billxtype = '"+xtype+"',billamount= amount"+xtype+rateStr+" WHERE isdelete = FALSE AND jobid = "+jobid+" AND billid = "+billid+" AND currency = '"+currency+"'";
			this.finaArapDao.executeSQL(sql);
		}
	}
	
	/**
	 * 批量修改账单汇率
	 * @param value 汇率初始数据
	 * @param modified 改变数据
	 * @param currencyto 折合币制
	 * @param ids 费用ids
	 */
	public void updateRateForBill(ArrayList<Object> value,JSONArray modified,String currencyto,String[] ids){
		HashMap<String, HashMap<String, String>> newData = null;//存放最终grid数据
		StringBuffer idssql = new StringBuffer();
		if(ids !=null && ids.length > 0){
			idssql.append(" AND id = any(array[" + StrUtils.array2List(ids) + "])");
		}else{
			return;
		}
		if(value != null && value.size() > 0){
			
			newData = new HashMap<String, HashMap<String, String>>();
			for (Object v : value) {
				HashMap<String, Object> map = (HashMap<String, Object>)v;
				String id = map.get("id").toString();
				String currencyfm = map.get("currencyfm").toString();
				String rate = map.get("rate").toString();
				String xtype = map.get("xtype").toString();
				HashMap<String,String> maptmp = new HashMap<String, String>();
				maptmp.put("id", id);
				maptmp.put("currencyfm", currencyfm);
				maptmp.put("rate", rate);
				maptmp.put("xtype", xtype);
				newData.put(id,maptmp);
			}
			if(modified != null && modified.size() > 0){
				for (Object mod : modified) {
					JSONObject jsobj = (JSONObject) mod;
					String id = jsobj.get("id").toString();
					if(newData.containsKey(id)){
						newData.remove(id);
						HashMap<String,String> maptmp = new HashMap<String, String>();
						maptmp.put("id", id);
						maptmp.put("currencyfm", jsobj.get("currencyfm").toString());
						maptmp.put("rate", jsobj.get("rate").toString());
						maptmp.put("xtype", jsobj.get("xtype").toString());
						newData.put(id, maptmp);
					}
				}
			}
			if(newData != null && newData.size()>0){
				Set<String> keys = newData.keySet();
				for (String key : keys) {
					HashMap<String, String> map = newData.get(key);
					String currencyfm = map.get("currencyfm");
					String rate = map.get("rate");
					String xtype = map.get("xtype");
					StringBuffer sb = new StringBuffer();
					sb.append("\n UPDATE fina_arap ");
					sb.append("\n SET billxrate = "+rate+",");
					sb.append("\n billxtype = '"+xtype+"',");
					sb.append("\n billamount = amount"+xtype+rate);
					sb.append("\n WHERE ");
					sb.append("\n isdelete = FALSE ");
					sb.append("\n AND currency = '"+currencyfm+"' ");
					sb.append(idssql.toString());
					this.finaArapDao.executeSQL(sb.toString());
				}
			}
		}
		//处理原币转原币
		StringBuffer sbo = new StringBuffer();
		sbo.append("\n UPDATE fina_arap ");
		sbo.append("\n SET billxrate = 1,");
		sbo.append("\n billxtype = '*',");
		sbo.append("\n billamount = amount");
		sbo.append("\n WHERE ");
		sbo.append("\n isdelete = FALSE ");
		sbo.append("\n AND currency = '"+currencyto+"' ");
		sbo.append(idssql.toString());
		this.finaArapDao.executeSQL(sbo.toString());
	}
	
	/**
	 * 批量修改发票汇率
	 * @param value 汇率初始数据
	 * @param modified 改变数据
	 * @param currencyto 折合币制
	 * @param ids 费用ids
	 */
	public void updateRateForInvoice(ArrayList<Object> value,JSONArray modified,String currencyto,String[] ids){
		HashMap<String, HashMap<String, String>> newData = null;//存放最终grid数据
		StringBuffer idssql = new StringBuffer();
		if(ids !=null && ids.length > 0){
			idssql.append(" AND id = any(array[" + StrUtils.array2List(ids) + "])");
		}else{
			return;
		}
		if(value != null && value.size() > 0){
			
			newData = new HashMap<String, HashMap<String, String>>();
			for (Object v : value) {
				HashMap<String, Object> map = (HashMap<String, Object>)v;
				String id = map.get("id").toString();
				String currencyfm = map.get("currencyfm").toString();
				String rate = map.get("rate").toString();
				String xtype = map.get("xtype").toString();
				HashMap<String,String> maptmp = new HashMap<String, String>();
				maptmp.put("id", id);
				maptmp.put("currencyfm", currencyfm);
				maptmp.put("rate", rate);
				maptmp.put("xtype", xtype);
				newData.put(id,maptmp);
			}
			if(modified != null && modified.size() > 0){
				for (Object mod : modified) {
					JSONObject jsobj = (JSONObject) mod;
					String id = jsobj.get("id").toString();
					if(newData.containsKey(id)){
						newData.remove(id);
						HashMap<String,String> maptmp = new HashMap<String, String>();
						maptmp.put("id", id);
						maptmp.put("currencyfm", jsobj.get("currencyfm").toString());
						maptmp.put("rate", jsobj.get("rate").toString());
						maptmp.put("xtype", jsobj.get("xtype").toString());
						newData.put(id, maptmp);
					}
				}
			}
			if(newData != null && newData.size()>0){
				Set<String> keys = newData.keySet();
				for (String key : keys) {
					HashMap<String, String> map = newData.get(key);
					String currencyfm = map.get("currencyfm");
					String rate = map.get("rate");
					String xtype = map.get("xtype");
					StringBuffer sb = new StringBuffer();
					sb.append("\n UPDATE fina_arap ");
					sb.append("\n SET invoicexrate = "+rate+",");
					sb.append("\n invoicextype = '"+xtype+"',");
					sb.append("\n invoiceamount = amount"+xtype+rate);
					sb.append("\n WHERE ");
					sb.append("\n isdelete = FALSE ");
					sb.append("\n AND currency = '"+currencyfm+"' ");
					sb.append(idssql.toString());
					this.finaArapDao.executeSQL(sb.toString());
				}
			}
		}
		//处理原币转原币
		StringBuffer sbo = new StringBuffer();
		sbo.append("\n UPDATE fina_arap ");
		sbo.append("\n SET invoicexrate = 1,");
		sbo.append("\n invoicextype = '*',");
		sbo.append("\n invoiceamount = amount");
		sbo.append("\n WHERE ");
		sbo.append("\n isdelete = FALSE ");
		sbo.append("\n AND currency = '"+currencyto+"' ");
		sbo.append(idssql.toString());
		this.finaArapDao.executeSQL(sbo.toString());
	}
	public void addInvoiceDtl(Long pkVal,String[] ids){
		if(pkVal != null && pkVal > 0 && ids !=null && ids.length > 0){
			StringBuffer bfsql = new StringBuffer();
			bfsql.append("UPDATE fina_arap SET invoiceid = NULL WHERE isdelete = FALSE AND invoiceid = "+pkVal+";");
			bfsql.append("UPDATE fina_arap SET invoiceid = "+pkVal+" WHERE isdelete = FALSE ");
			bfsql.append(" AND id = ANY(array["+StrUtils.array2List(ids)+"])");
			bfsql.append(" );");
			this.finaArapDao.executeSQL(bfsql.toString());
		}
	}
	
	
	/**
	 * 工作单费用生成往来消息推送
	 * @param uid
	 * @param code 接收人code 
	 * @param jobno 工作号
	 * @param senderUserName 发送者名
	 * @param basePath
	 * @param namec 业务员中文名
	 * @param namee 业务员英文名
	 */
	public void sendMessageToArapService(Long sendid,Long uid, String code,String jobno,String senderUserName,String basePath,String namec,String namee,Long linkid,String ids,String saleid) {
		List<String> receiveCodes = new ArrayList<String>();
		receiveCodes.add(code);
		String remindTitle = "往来费用提醒";
		String remindContext = senderUserName
		+ "发来的往来费用跟进提醒";
		String url = "/scp/pages/module/ship/jobsedit.aspx?id="
		+ linkid;
		String sendContext = "业务员" + namec + "(" + namee + ")"
		+ "的工作单[url=" + url + "]" + jobno
		+ "[/url]往来费用已生成,请跟进确认!";
//		if(applicationConf.getIsUseDzz()){
//			AppUtils.sendMessage(uid , receiveCodes, remindTitle, remindContext, url,
//				sendContext);
//		}else{
			String a = "业务员" + namec + "(" + namee + ")"
			+ "的工作单<a target=_blank href=" + url + ">" + jobno
			+ "</a>往来费用已生成,请跟进确认!";
			if(null == ids || ids.isEmpty()){
				//sender.sendMessage(new MessageBean(sendid,Long.parseLong(saleid),remindContext,a,"C"));
				AppUtils.sendIMMsg(sendid.toString(), saleid, remindContext + a);
			}else{
				//sender.sendMessage(new MessageBean(sendid,Long.parseLong(ids),remindContext,a,"C"));
				AppUtils.sendIMMsg(sendid.toString(), ids, remindContext + a);
			}
//		}
	}
	
	/**
	 * @param jobid
	 * @return 通过jobid查出对应sys_formdef中的费用项目
	 * inputlable 费用中文名，columnname 费用id，defvalue 应收应付，
	 */
	public String getArapAndFormdefJsonByJobid(String jobid,String beaname){
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n 	(SELECT id,inputlable,columnname,defvalue");
		sb.append("\n 	,(SELECT code FROM dat_feeitem WHERE x.columnname::BIGINT = id) AS feecode");
		sb.append("\n 	,(SELECT sum(amount) FROM fina_arap WHERE jobid = "+jobid+" AND feeitemid = x.columnname::BIGINT " +
				"AND isdelete = FALSE AND araptype = x.defvalue) AS amount");
		sb.append("\n 		FROM sys_formdef x");
		sb.append("\n 		WHERE beaname = '"+beaname+"'");
		sb.append("\n ORDER BY inputlable ) T");
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils
				.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sb.toString());
		if (map != null && 1 == map.size()&&map.get("json")!=null) {
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}
	
	/**
	 * 查找对应工作单中的费用没有就新增，有就update
	 * @param jobid
	 * @param araptype
	 * @param feeid
	 * @param price
	 */
	public void updateOrAddarapByfeeid(Long jobid,String araptype,Long feeid,BigDecimal price){
		List<FinaArap> araps = finaArapDao
		.findAllByClauseWhere(" isdelete = FALSE AND araptype = '"+araptype+"' AND jobid = "+jobid);
		boolean isfind = false;
		for(FinaArap arap:araps){
			if(arap.getFeeitemid().longValue() == feeid.longValue()){
				arap.setAmount(price);
				arap.setPrice(price);
				arap.setUpdater("t1");
				arap.setUpdatetime(new Date());
				finaArapDao.modify(arap);
				isfind = true;
			}
		}
		FinaJobs job = finaJobsDao.findById(jobid);
		if(!isfind){//没找到就新增
			FinaArap arap = new FinaArap();
			arap.setJobid(jobid);
			arap.setFeeitemid(feeid);
			arap.setPiece(new BigDecimal(1));
			arap.setPrice(price);
			arap.setAmount(price);
			arap.setCustomerid(job.getCustomerid());
			arap.setPayplace("CS");
			arap.setAraptype(araptype);
			arap.setCorpid(AppUtils.getUserSession()
					.getCorpidCurrent() < 0 ? AppUtils.getUserSession().getCorpid() : AppUtils.getUserSession().getCorpidCurrent());
			arap.setArapdate(new Date());
			arap.setPpcc("PP");
			arap.setSharetype("N");
			arap.setCurrency(AppUtils.getUserSession().getBaseCurrency());
			arap.setTaxrate(new BigDecimal(1));
			finaArapDao.create(arap);
		}
	}
	
	
	/**
	 * 删除集合中存在的AR或AP费用
	 * @param jobid
	 * @param araptype
	 * @param feeids
	 */
	public void delarapByfeeid(Long jobid,String araptype,ArrayList<Long> feeids){
		List<FinaArap> araps = finaArapDao
		.findAllByClauseWhere(" isdelete = FALSE AND araptype = '"+araptype+"' AND jobid = "+jobid);
		for(FinaArap arap:araps){
			boolean in = feeids.contains(arap.getFeeitemid());
			if(in){
				arap.setIsdelete(true);
				arap.setUpdatetime(new Date());
				arap.setUpdater(AppUtils.getUserSession().getUsercode());
				finaArapDao.modify(arap);
			}
		}
	}
	
	/**
	 * 工作单中费用编辑列表update
	 * @param modifiedData
	 */
	public void updateArapEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		FinaArap data = new FinaArap();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = finaArapDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("price")!=null && !String.valueOf(jsonObject.get("price")).isEmpty()){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(new BigDecimal(0));
			}
			if(jsonObject.get("piece")!=null && !String.valueOf(jsonObject.get("piece")).isEmpty()){
				data.setPiece(new BigDecimal(String.valueOf(jsonObject.get("piece"))));
			}else{
				data.setPiece(new BigDecimal(0));
			}
			if(jsonObject.get("amount")!=null && !String.valueOf(jsonObject.get("amount")).isEmpty()){
				String amount = String.valueOf(jsonObject.get("amount")); //Neo 20190514 去掉千分位逗号
				if(!StrUtils.isNull(amount)){
					amount = amount.replaceAll(",", "");
					data.setAmount(new BigDecimal(amount));
				}
				
			}else{
				data.setAmount(new BigDecimal(0));
			}
			if(jsonObject.get("payplace")!=null){
				data.setPayplace(String.valueOf(jsonObject.get("payplace")));
			}else{
				data.setPayplace(null);
			}
			if(jsonObject.get("amtcost")!=null && !String.valueOf(jsonObject.get("amtcost")).isEmpty()){
				data.setAmtcost(new BigDecimal(String.valueOf(jsonObject.get("amtcost"))));
			}else{
				data.setAmtcost(new BigDecimal(0));
			}
			if(jsonObject.get("corper")!=null && !String.valueOf(jsonObject.get("corper")).isEmpty()){
				data.setCorpid(Long.parseLong(String.valueOf(jsonObject.get("corper"))));
			}else{
				data.setCorpid(null);
			}
			if(jsonObject.get("blno")!=null){
				data.setBlno(String.valueOf(jsonObject.get("blno")));
			}else{
				data.setBlno(null);
			}
			if(jsonObject.get("araptype")!=null){
				data.setAraptype(String.valueOf(jsonObject.get("araptype")));
			}else{
				data.setAraptype(null);
			}
			if(jsonObject.get("ppcc")!=null){
				data.setPpcc(String.valueOf(jsonObject.get("ppcc")));
			}else{
				data.setPpcc(null);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(jsonObject.get("arapdate")!=null){
				try {
					data.setArapdate(sdf.parse((jsonObject.get("arapdate")).toString()));
				} catch (ParseException e) {
					data.setArapdate(null);
					e.printStackTrace();
				}
			}else{
				data.setArapdate(null);
			}
			if(jsonObject.get("unit")!=null){
				data.setUnit(String.valueOf(jsonObject.get("unit")));
			}else{
				data.setUnit(null);
			}
			if(jsonObject.get("sharetype")!=null){
				data.setSharetype(String.valueOf(jsonObject.get("sharetype")));
			}else{
				data.setSharetype(null);
			}
			if(jsonObject.get("remarks")!=null){
				data.setRemarks(String.valueOf(jsonObject.get("remarks")));
			}else{
				data.setRemarks(null);
			}
			if(jsonObject.get("descinfo")!=null){
				data.setDescinfo(String.valueOf(jsonObject.get("descinfo")));
			}else{
				data.setDescinfo(null);
			}
			if(jsonObject.get("pricenotax")!=null && !String.valueOf(jsonObject.get("pricenotax")).isEmpty()){
				data.setPricenotax(new BigDecimal(String.valueOf(jsonObject.get("pricenotax"))));
			}else{
				data.setPricenotax(new BigDecimal(0));
			}
			if(jsonObject.get("customerid")!=null && !String.valueOf(jsonObject.get("customerid")).isEmpty()){
				data.setCustomerid(Long.parseLong(String.valueOf(jsonObject.get("customerid"))));
			}else{
				data.setCustomerid(null);
			}
			if(jsonObject.get("customercode")!=null){
				data.setCustomercode(String.valueOf(jsonObject.get("customercode")));
			}else{
				data.setCustomercode(null);
			}
			if(jsonObject.get("feeitemid")!=null && !String.valueOf(jsonObject.get("feeitemid")).isEmpty()){
				data.setFeeitemid(Long.parseLong(String.valueOf(jsonObject.get("feeitemid"))));
			}else{
				data.setFeeitemid(null);
			}
			if(jsonObject.get("corpid")!=null && !String.valueOf(jsonObject.get("corpid")).isEmpty()){
				data.setCorpid(Long.parseLong(String.valueOf(jsonObject.get("corpid"))));
			}else{
				data.setCorpid(null);
			}
			String isamend=String.valueOf(jsonObject.get("isamend"));
			if("true".equals(isamend)){
				data.setIsamend(true);
			}else if("false".equals(isamend)){
				data.setIsamend(false);
			}
			saveData(data);
		}		
	}
	/**
	 * 工作单中费用编辑列表add
	 * @param modifiedData
	 */
	public List<Long> addArapEditGrid(Object addData, long jobid) {
		JSONArray jsonArray = (JSONArray) addData;
		Calendar calendar = Calendar.getInstance ();
		List<Long> list = new ArrayList<Long>();

		for (int i = 0; i < jsonArray.size(); i++) {
			FinaArap data = new FinaArap();
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("price")!=null && !String.valueOf(jsonObject.get("price")).isEmpty()){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(new BigDecimal(0));
			}
			if(jsonObject.get("piece")!=null && !String.valueOf(jsonObject.get("piece")).isEmpty()){
				data.setPiece(new BigDecimal(String.valueOf(jsonObject.get("piece"))));
			}else{
				data.setPiece(new BigDecimal(0));
			}
			if(jsonObject.get("amount")!=null && !String.valueOf(jsonObject.get("amount")).isEmpty()){
				String amount = String.valueOf(jsonObject.get("amount")); //Neo 20190514 去掉千分位逗号
				if(!StrUtils.isNull(amount)){
					amount = amount.replaceAll(",", "");
					data.setAmount(new BigDecimal(amount));
				}
				
			}else{
				data.setAmount(new BigDecimal(0));
			}
			if(jsonObject.get("payplace")!=null){
				data.setPayplace(String.valueOf(jsonObject.get("payplace")));
			}else{
				data.setPayplace(null);
			}
			if(jsonObject.get("amtcost")!=null && !String.valueOf(jsonObject.get("amtcost")).isEmpty()){
				data.setAmtcost(new BigDecimal(String.valueOf(jsonObject.get("amtcost"))));
			}else{
				data.setAmtcost(new BigDecimal(0));
			}
			if(jsonObject.get("corper")!=null && !String.valueOf(jsonObject.get("corper")).isEmpty()){
				data.setCorpid(Long.parseLong(String.valueOf(jsonObject.get("corper"))));
			}else{
				data.setCorpid(null);
			}
			if(jsonObject.get("blno")!=null){
				data.setBlno(String.valueOf(jsonObject.get("blno")));
			}else{
				data.setBlno(null);
			}
			if(jsonObject.get("araptype")!=null){
				data.setAraptype(String.valueOf(jsonObject.get("araptype")));
			}else{
				data.setAraptype(null);
			}
			if(jsonObject.get("ppcc")!=null){
				data.setPpcc(String.valueOf(jsonObject.get("ppcc")));
			}else{
				data.setPpcc(null);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(jsonObject.get("arapdate")!=null){
				try {
					data.setArapdate(sdf.parse((jsonObject.get("arapdate")).toString()));
				} catch (ParseException e) {
					data.setArapdate(null);
					e.printStackTrace();
				}
			}else{
				data.setArapdate(null);
			}
			if(jsonObject.get("unit")!=null){
				data.setUnit(String.valueOf(jsonObject.get("unit")));
			}else{
				data.setUnit(null);
			}
			if(jsonObject.get("sharetype")!=null){
				data.setSharetype(String.valueOf(jsonObject.get("sharetype")));
			}else{
				data.setSharetype(null);
			}
			if(jsonObject.get("remarks")!=null){
				data.setRemarks(String.valueOf(jsonObject.get("remarks")));
			}else{
				data.setRemarks(null);
			}
			if(jsonObject.get("descinfo")!=null){
				data.setDescinfo(String.valueOf(jsonObject.get("descinfo")));
			}else{
				data.setDescinfo(null);
			}
			if(jsonObject.get("pricenotax")!=null && !String.valueOf(jsonObject.get("pricenotax")).isEmpty()){
				data.setPricenotax(new BigDecimal(String.valueOf(jsonObject.get("pricenotax"))));
			}else{
				data.setPricenotax(new BigDecimal(0));
			}
			if(jsonObject.get("customerid")!=null && !String.valueOf(jsonObject.get("customerid")).isEmpty()){
				data.setCustomerid(Long.parseLong(String.valueOf(jsonObject.get("customerid"))));
			}else{
				data.setCustomerid(null);
			}
			if(jsonObject.get("customercode")!=null){
				data.setCustomercode(String.valueOf(jsonObject.get("customercode")));
			}else{
				data.setCustomercode(null);
			}
			if(jsonObject.get("feeitemid")!=null && !String.valueOf(jsonObject.get("feeitemid")).isEmpty()){
				data.setFeeitemid(Long.parseLong(String.valueOf(jsonObject.get("feeitemid"))));
			}else{
				data.setFeeitemid(null);
			}
			if(jsonObject.get("corpid")!=null && !String.valueOf(jsonObject.get("corpid")).isEmpty()){
				data.setCorpid(Long.parseLong(String.valueOf(jsonObject.get("corpid"))));
			}else{
				data.setCorpid(null);
			}
			data.setJobid(jobid);
			
			calendar.add (Calendar.SECOND, 1);
			//System.out.println(calendar.getTime());
			data.setInputtime(calendar.getTime());
			String isamend=String.valueOf(jsonObject.get("isamend"));
			if("true".equals(isamend)){
				data.setIsamend(true);
			}else if("false".equals(isamend)){
				data.setIsamend(false);
			}

			Long feeId = saveDataReturnId(data);
			list.add(feeId);


			//保存报价金额
			String quoteamount = String.valueOf(null == jsonObject.get("quoteamount") ? "" : jsonObject.get("quoteamount"));
			String quotecurrency = String.valueOf(null == jsonObject.get("quotecurrency") ? "" : jsonObject.get("quotecurrency"));
			if (!StrUtils.isNull(quoteamount) && !StrUtils.isNull(quotecurrency)) {
				String sql = "UPDATE fina_arap_link_quote Set isdelete = TRUE WHERE arapid = "+data.getId()+";";
				sql += "\nINSERT INTO fina_arap_link_quote(id ,arapid ,quoteamount ,quotecurrency )" +
					"\nSELECT getid()," + data.getId() + "," + new BigDecimal(quoteamount) + ",'"+quotecurrency+"'" +
					"\nFROM _virtual WHERE NOT EXISTS(SELECT 1 FROM fina_arap_link_quote WHERE isdelete = FALSE AND arapid = " + data.getId() +");";
				daoIbatisTemplate.updateWithUserDefineSql(sql);
			}
		}
		return list;
	}

	/**
	 * 根据arapid获取json格式未锁定费用条目
	 *
	 * @param jobid
	 * @param arapIds
	 * @return
	 */
	public String getArapsJsonByArapid(String[] arapIds , Long userid , Long corpidCurrent) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n SELECT");
		sb.append("\n array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json");
		sb.append("\n FROM");
		sb.append("\n 	(SELECT");
		sb.append("\n 		ff.ID,");
		sb.append("\n 		ff.taxrate,");
		sb.append("\n 		ff.araptype,");
		sb.append("\n 		ff.arapdate,");
		sb.append("\n 		ff.customerid,");
		sb.append("\n 		ff.feeitemid,");
		sb.append("\n 		(SELECT y.name FROM dat_feeitem y WHERE y.id=ff.feeitemid) AS feeitemnamec,");
		sb.append("\n 		ff.amount,");
		sb.append("\n 		ff.currency,");
		sb.append("\n 		ff.remarks,");
		sb.append("\n 		ff.piece,");
		sb.append("\n 		ff.price,");
		sb.append("\n 		ff.jobid,");
		sb.append("\n 		ff.unit,");
		sb.append("\n 		ff.sharetype,");
		sb.append("\n 		(SELECT x.abbr FROM sys_corporation x WHERE x.id=ff.customerid) AS customercode,");
		sb.append("\n 		(SELECT x.namec FROM sys_corporation x WHERE x.id=ff.customerid) AS customernamec,");
		sb.append("\n 		ff.corpid,");
		sb.append("\n 		ff.ppcc,");
		sb.append("\n 		ff.payplace,");
		sb.append("\n 		ff.isamend,");
		sb.append("\n 		(SELECT fe.code AS feeiname FROM dat_feeitem fe WHERE fe. ID = ff.feeitemid)");
		sb.append("\n 		FROM");
		sb.append("\n 			fina_arap AS ff");
		sb.append("\n 		WHERE");
		sb.append("\n 		    ff.isdelete = FALSE");
		sb.append("\n 		AND (ff.parentid IS NULL)");
		sb.append("\n 		AND ff.rptype !='H'");
		if (arapIds != null) {
			sb.append("\n 		AND ( false ");
			for (String s : arapIds) {
				sb.append("\n 		OR ff.id = " + s);
			}
			sb.append("\n 		)");
		}

		String orderby = ConfigUtils.findSysCfgVal("arap_filter_orderby_inputtime");
		if(StrUtils.isNull(orderby) || "N".equals(orderby)){
			sb.append("\n ORDER BY araptype desc, customerid , feeitemid , currency ) T");
		}else{
			//map.put("orderSql", "araptype DESC,inputtime,");
			sb.append("\n ORDER BY araptype DESC,inputtime) T");
		}


		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils
				.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sb.toString());
		////System.out.println(sb.toString());
		if (map != null && 1 == map.size()) {
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}
}
