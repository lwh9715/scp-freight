package com.scp.service.finance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaArapDao;
import com.scp.dao.finance.FinaInvoiceDao;
import com.scp.dao.finance.FinaInvoiceDtlDao;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaInvoice;
import com.scp.model.finance.FinaInvoiceDtl;
import com.scp.util.AppUtils;
import com.scp.util.Base64;
import com.scp.util.ConfigUtils;
import com.scp.util.DESDZFP;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

/**
 * @author Administrator
 *
 */
@Component
public class InvoiceMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaInvoiceDao finaInvoiceDao; 
	
	@Resource
	public FinaInvoiceDtlDao finaInvoiceDtlDao; 
	
	@Resource
	public FinaArapDao finaArapDao; 

	public void saveData(FinaInvoice data) {
		if(0 == data.getId()){
			finaInvoiceDao.create(data);
		}else{
			finaInvoiceDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		String sql = "UPDATE fina_invoice SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id;
		finaInvoiceDao.executeSQL(sql);
	}


	public String getCustomerids(Long jobid) {
		String sql="SELECT f_lists(CAST(a.customerid AS VARCHAR))AS customerids FROM fina_arap a where a.isdelete = false and a.jobid ="+jobid;
		Map m=daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		return (String) m.get("customerids");
		
	}



	/**
	 * 根据jobid过滤得到所有的单号信息
	 * @param jobid
	 * @return
	 */
	public List<FinaInvoice> getFinaBillListByJobid(Long jobid , Long userid) {
		String whereSql = "isdelete = false AND jobid = " + jobid + "ORDER BY invoiceno";
		//分公司过滤
//		if(AppUtils.getUserSession().getCorpidCurrent() > 0){
//			whereSql = "isdelete = false AND jobid = " + jobid + " AND corpid = "+AppUtils.getUserSession().getCorpidCurrent()+" ORDER BY invoiceno";
//		}
		whereSql = "isdelete = false AND jobid = " + jobid + " AND (EXISTS(SELECT 1 FROM SysUserCorplink x WHERE (x.corpid = thql.corpid) AND x.ischoose = TRUE AND userid ="+userid+"))";
		
		return this.finaInvoiceDao.findAllByClauseWhere(whereSql);
	}



	public void removeDate(String[] ids, String usercode) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nUPDATE fina_invoice SET isdelete = TRUE , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuilder.append(sql);
		}
		String execSql = stringBuilder.toString();
		if(!StrUtils.isNull(execSql)) {
			this.finaInvoiceDao.executeSQL(execSql);
		}
	}

	public String findSumInfo(Long pkVal) {
		String qry = "SELECT f_fina_invoice_suminfo('id="+pkVal+"') AS suminfo;";
		Map m=daoIbatisTemplate.queryWithUserDefineSql4OnwRow(qry);
		return (String) m.get("suminfo");
	} 
	
	public void removeInvoiceAndDtl(Long pkid){
		//删发票主表
		this.finaInvoiceDao.executeSQL("UPDATE fina_invoice SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE isdelete = FALSE AND id = "+pkid);
		this.finaInvoiceDtlDao.executeSQL("DELETE FROM fina_invoice_dtl WHERE invoiceid = "+pkid);
		this.finaArapDao.executeSQL("UPDATE fina_arap SET invoiceid = null,isunionfee = false WHERE isdelete = FALSE AND invoiceid = "+pkid);
	}
	/**
	 * 
	 * @param invoiceid
	 */
	public void transferToUnionInvoice(Long invoiceid){
		try{
			finaArapDao.executeSQL(" UPDATE fina_arap SET isunionfee = TRUE WHERE invoiceid ="+invoiceid+" AND isdelete = FALSE AND (isunionfee = FALSE OR isunionfee IS NULL)");
			FinaInvoice invoice = finaInvoiceDao.findById(invoiceid);
			invoice.setInvoicetype("U");
			FinaInvoiceDtl fid = new FinaInvoiceDtl();
			fid.setAraptype("AR");
			fid.setInvoiceid(invoice.getId());
			fid.setCurrency(invoice.getCurrency());
			fid.setAmount(invoice.getAmounts());
			fid.setFeeitemdec("代理费");
			fid.setInvoicextype("*");
			fid.setInvoicexrate(new BigDecimal(1));
			fid.setInvoiceamountflag(invoice.getAmounts());
			finaInvoiceDtlDao.create(fid);
			finaInvoiceDao.modify(invoice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 综合发票修改币制
	 * @param selectdRowData
	 */
	public void changeInvoiceCurrency(FinaInvoice selectdRowData){
		String toCurrency = selectdRowData.getCurrency();
		FinaInvoiceDtl fid = finaInvoiceDtlDao.findOneRowByClauseWhere("invoiceid="+selectdRowData.getId());
		String fmCuttency = fid.getCurrency();
		BigDecimal fmAmount = fid.getAmount();
		BigDecimal toAmount = fmAmount;
		StringBuilder sb = new StringBuilder();
		sb.append("\n SELECT x.currencyfm,x.currencyto,x.rate,x.xtype,x.datafm,x.datato");
		sb.append("\n  FROM _inv_exchangerate x WHERE ");
		sb.append("\n  x.isdelete = FALSE AND now() BETWEEN x.datafm AND x.datato");
		sb.append("\n  AND currencyfm = '"+fmCuttency+"' AND currencyto = '"+toCurrency+"'");
		sb.append("\n  ORDER BY x.currencyfm,x.datafm DESC LIMIT 1");
		try {
			Map exchangerate = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			BigDecimal rate = BigDecimal.valueOf(Double.valueOf(exchangerate.get("rate").toString()));
			String xtype = exchangerate.get("xtype").toString();
			if("*".equals(xtype)){
				toAmount = fmAmount.multiply(rate);
			}else{//:/  四舍五入模式
				toAmount = fmAmount.divide(rate,BigDecimal.ROUND_HALF_UP);
			}
			fid.setInvoicexrate(rate);
			fid.setInvoicextype(xtype);
			fid.setInvoiceamountflag(toAmount);
			selectdRowData.setAmounts(toAmount);
			finaInvoiceDtlDao.modify(fid);
		} catch (NoRowException e) {
			throw new NoRowException("(NoRowException:)未设置可用的汇率,请管理员在'档案管理'→'汇率'中设置相应的汇率!");
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new NullPointerException("(NullPointerException:)未设置可用的汇率,请管理员在'档案管理'→'汇率'中设置相应的汇率!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String openinginvoice(String ids[],String invoiceTypev,String listed,String CheckEWM,String ncpFlag){
		String invSvr = "";
		for(int i=0;i<ids.length;i++){
			FinaInvoice inv = finaInvoiceDao.findById(Long.parseLong(ids[i]));
			Boolean isinvsvr = inv.getIsinvsvr();
			Boolean iscancel = inv.getIscancel();
			if(inv.getAmounts()!=null&&inv.getAmounts().intValue()>99999){
				MessageUtils.alert("发票面额超出金税设备最大限额,请拆分处理");
				return invSvr;
			}
			try{
				String findp = "SELECT EXISTS(SELECT 1 FROM fina_invoice WHERE isdelete = FALSE AND parentid = "+inv.getId()+") AS findp";
				Map findm = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(findp);
				if(findm!=null&&(findm.get("findp").toString()).equals("true")){
					MessageUtils.alert("已拆分的发票不可开具发票");
					return invSvr;
				}
			}catch(NoRowException e){
			}catch(Exception e){
				MessageUtils.showException(e);
				e.printStackTrace();
				return invSvr;
			}
			if(isinvsvr&&iscancel==false){
				MessageUtils.alert(inv.getInvoiceno()+"[此发票已开具过，请勿重复开具]");
				return invSvr;
			}
			String sql = "SELECT f_getinvoice_tojson('invoiceid="+ids[i]+";invoiceType="+invoiceTypev
					   + ";listed="+listed+";CheckEWM="+CheckEWM+";ncpFlag="+ncpFlag+"') AS datejson;";
			Map m = null;
			try{
				m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			}catch(Exception e){
				MessageUtils.showException(e);
				e.printStackTrace();
				return invSvr;
			}
			String datejson = m.get("datejson").toString();
			JSONObject jb = new JSONObject();
			jb.put("SID", 1);
			jb.put("SIDParam",Base64.encode(datejson,"GB2312").replaceAll("\\+","_"));
			//String returnjson = sendPost("http://127.0.0.1:8889/hx/pricefclmgr/InvSvr", "SID=1&SIDParam="+Base64.encode(datejson,"GB2312").replaceAll("\\+","_"));
			String para = "SID=1&SIDParam="+Base64.encode(datejson,"GB2312").replaceAll("\\+","_");
			System.out.println("开始开票");
			String returnjson = AppUtils.sendPost(ConfigUtils.findSysCfgVal("sys_invsvr_url"),para );
			JSONObject json = JSONObject.fromObject(returnjson);
			String ENCMSG = Base64.decode(json.getString("ENCMSG").replaceAll("_","+") ,"GB2312");
			System.out.println(ENCMSG);
			JSONObject ENCMSGjson = JSONObject.fromObject(ENCMSG);
			String retcode = ENCMSGjson.get("retcode")!=null?ENCMSGjson.get("retcode").toString():"";
			String retmsg = ENCMSGjson.get("retmsg")!=null?ENCMSGjson.get("retmsg").toString():"";
			String InfoAmount = ENCMSGjson.get("InfoAmount")!=null?ENCMSGjson.get("InfoAmount").toString():"";
			String InfoTaxAmount = ENCMSGjson.get("InfoTaxAmount")!=null?ENCMSGjson.get("InfoTaxAmount").toString():"";
			String InfoDate = ENCMSGjson.get("InfoDate")!=null?ENCMSGjson.get("InfoDate").toString():"";
			String InfoTypeCode = ENCMSGjson.get("InfoTypeCode")!=null?ENCMSGjson.get("InfoTypeCode").toString():"";
			String InfoNumber = ENCMSGjson.get("InfoNumber")!=null?ENCMSGjson.get("InfoNumber").toString():"";
			String hisInfoTypeCode = ENCMSGjson.get("hisInfoTypeCode")!=null?ENCMSGjson.get("hisInfoTypeCode").toString():"";
			String hisInfoNumber = ENCMSGjson.get("hisInfoNumber")!=null?ENCMSGjson.get("hisInfoNumber").toString():"";
			String hisInfoKind = ENCMSGjson.get("hisInfoKind")!=null?ENCMSGjson.get("hisInfoKind").toString():"";
			String ListFlag = ENCMSGjson.get("ListFlag")!=null?ENCMSGjson.get("ListFlag").toString():"";
			String nsrsbh = ENCMSGjson.get("nsrsbh")!=null?ENCMSGjson.get("nsrsbh").toString():"";
			String kpfwqh = ENCMSGjson.get("kpfwqh")!=null?ENCMSGjson.get("kpfwqh").toString():"";
			String kpdh = ENCMSGjson.get("kpdh")!=null?ENCMSGjson.get("kpdh").toString():"";
			if(retcode.equals("4011")){
				invSvr += "\n开具的发票号码："+InfoNumber;
				if(!StrUtils.isNull(InfoNumber)){
					inv.setInvoiceno(InfoNumber);
					inv.setInvoicecode(InfoTypeCode);
					inv.setIsinvsvr(true);
					inv.setIscancel(false);
					try {
						finaInvoiceDao.modify(inv);
					} catch (Exception e) {
						MessageUtils.showException(e);
						return invSvr;
					}
				}
				invSvr += "\n返回码："+(retcode.equals("4011")?retcode+"-开票成功":retcode+"-开票失败");
			}
			else{
				invSvr = "\n返回码："+(retcode.equals("4011")?retcode+"-开票成功":retcode+"-开票失败");
				invSvr+= "\n返回信息："+retmsg;
				invSvr+= "\n开具的金额："+InfoAmount;
				invSvr+= "\n开具的税额："+InfoTaxAmount;
				invSvr+= "\n开票日期："+InfoDate;
				invSvr+= "\n开具的发票代码："+InfoTypeCode;
				
				invSvr+= "\n上一张发票代码："+hisInfoTypeCode;
				invSvr+= "\n上一张发票号码："+hisInfoNumber;
				invSvr+= "\n上一张发票号码："+hisInfoKind;
				invSvr+= "\n清单标志："+(ListFlag.equals("1")?"清单发票":"非清单发票");
				invSvr+= "\n销方税号："+nsrsbh;
				invSvr+= "\n开票服务器号："+kpfwqh;
				invSvr+= "\n开票点号："+kpdh+"\n";
			}
			invSvr += "\n------------------------------------------------------------";
		}
		return invSvr;
	}
	
	/**
	 * 开具发票(电子)
	 * @param ids
	 * @param identity 身份认证
	 * @param kptype 开票类型
	 * @param fphxz 发票行性质
	 * @return
	 */
	public String openingElectronInvoice(String ids[],String identity,String kptype,String fphxz){
		String invSvr = "";
		for(int i=0;i<ids.length;i++){
			FinaInvoice inv = finaInvoiceDao.findById(Long.parseLong(ids[i]));
			Boolean isinvsvr = inv.getIsinvsvr();
			Boolean iscancel = inv.getIscancel();
			if(inv.getAmounts()!=null&&inv.getAmounts().intValue()>99999){
				invSvr += "<br/>发票面额超出金税设备最大限额,请拆分处理";
				invSvr += "<br/>------------------------------------------------------------";
				continue;
			}
			try{
				String findp = "SELECT EXISTS(SELECT 1 FROM fina_invoice WHERE isdelete = FALSE AND parentid = "+inv.getId()+") AS findp";
				Map findm = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(findp);
				if(findm!=null&&(findm.get("findp").toString()).equals("true")){
					invSvr += "<br/>已拆分的发票不可开具发票";
					invSvr += "<br/>------------------------------------------------------------";
					continue;
				}
			}catch(NoRowException e){
			}catch(Exception e){
				MessageUtils.showException(e);
				e.printStackTrace();
				continue;
			}
			if("1".equals(kptype)&&isinvsvr&&iscancel==false){
				invSvr += inv.getInvoiceno()+"[此发票已开具过，请勿重复开具]";
				invSvr += "<br/>------------------------------------------------------------";
				continue;
			}
			if("2".equals(kptype)&&(isinvsvr==false)){
				invSvr += inv.getInvoiceno()+"[此发票还未开具发票,不可红冲]";
				invSvr += "<br/>------------------------------------------------------------";
				continue;
			}
			if("2".equals(kptype)&&(isinvsvr==true)&&!StrUtils.isNull(inv.getFpqqlsh())&&StrUtils.isNull(inv.getInvoiceno())){
				invSvr += inv.getInvoiceno()+"[此发票未获取发票信息，请先获取发票信息]";
				invSvr += "<br/>------------------------------------------------------------";
				continue;
			}
			String sql = "SELECT unnest(f_getinvoice_electron_tojson('invoiceid="+ids[i]+";identity="+identity
					   + ";kptype="+kptype+";fphxz="+fphxz+"')) AS datejson;";
			List<Map> m = null;
			try{
				m = daoIbatisTemplate.queryWithUserDefineSql(sql);
			}catch(Exception e){
				MessageUtils.showException(e);
				e.printStackTrace();
				continue;
			}
			String iserro = m.get(0).get("datejson").toString();
			String datejson = m.get(1).get("datejson").toString();
			if("f".equals(iserro)){
				invSvr += "<br/>"+datejson;
				invSvr += "<br/>------------------------------------------------------------";
				continue;
			}
			System.out.println("开始开票");
			String returnjson = AppUtils.sendPost(ConfigUtils.findSysCfgVal("sys_invsvr_electron_url"),"order="+DESDZFP.encrypt(datejson));
			System.out.println("返回json："+returnjson);
			JSONObject ENCMSGjson = JSONObject.fromObject(returnjson);
			String status = ENCMSGjson.get("status")!=null?ENCMSGjson.get("status").toString():"";
			String msg = ENCMSGjson.get("msg")!=null?ENCMSGjson.get("msg").toString():"";//返回信息
			String message = ENCMSGjson.get("message")!=null?ENCMSGjson.get("message").toString():"";//返回信息
			String fpqqlsh = ENCMSGjson.get("fpqqlsh")!=null?ENCMSGjson.get("fpqqlsh").toString():"";//发票流水号
			if(status.equals("0000")){
				invSvr += "<br/>开具的发票请求流水号："+fpqqlsh;
				if(!StrUtils.isNull(fpqqlsh)){
					String inviceInfoJson = inviceInfo(fpqqlsh, identity);
					inv.setFpqqlsh(fpqqlsh);
					if(!StrUtils.isNull(inviceInfoJson)){
						JSONObject inviceInfo = JSONObject.fromObject(inviceInfoJson);
						String result = inviceInfo.get("result")!=null?inviceInfo.get("result").toString():"";
						if("success".equals(result)){
							if("1".equals(kptype)){
								JSONArray infoList =  JSONArray.fromObject(inviceInfo.get("list"));
								String c_msg = infoList.getJSONObject(0).getString("c_msg");
								System.out.println("开票状态："+c_msg);
								invSvr += "开票状态："+c_msg;
								inv.setInvoiceno(infoList.getJSONObject(0).getString("c_fphm"));
								inv.setInvoicecode(infoList.getJSONObject(0).getString("c_fpdm"));
								inv.setIsinvsvr(true);
								inv.setIscancel(false);
								inv.setInvstatus(c_msg);
								
								String pdfUrl = infoList.getJSONObject(0).getString("c_url");
								inv.setUrlpdf(pdfUrl);
								
								invSvr += " 发票号："+infoList.getJSONObject(0).getString("c_fphm");
								invSvr += " <a href='"+pdfUrl+"' target='_blank'>下载PDF</a>";
								invSvr += " <a href='http://"+infoList.getJSONObject(0).getString("c_jpg_url")+"' target='_blank'>打开诺诺</a>";
							}else{
								inv.setIsinvsvr(false);
								inv.setIscancel(true);
								inv.setInvoiceno("");
								inv.setInvoicecode("");
							}
						}else{
							invSvr += "<br/>查询发票信息异常："+status;
						}
					}
					try {
						finaInvoiceDao.modify(inv);
					} catch (Exception e) {
						MessageUtils.showException(e);
						return invSvr;
					}
				}
				invSvr += "<br/>返回码："+status+"-开票成功";
			}else{
				invSvr += "<br/>返回码："+status+"-开票失败";
				invSvr += "<br/>信息："+message+msg;
			}
			invSvr += "<br/>------------------------------------------------------------";
		}
		return invSvr;
	}
	
	
	/**
	 * 根据订单号查询发票信息
	 * @param fpqqlsh 发票流水号
	 * @return
	 */
	public String inviceInfo(String orderNo,String identity){
		String datejson="{\"identity\":\""+identity+"\",\"orderno\":[\""+orderNo+"\"]}";
		System.out.println("开始根据请求流水号获取发票信息:"+datejson);
		String returnjson = AppUtils.sendPost(ConfigUtils.findSysCfgVal("sys_invsvr_electron_find_url"),"order="+DESDZFP.encrypt(datejson));
		System.out.println("获取发票信息返回json："+returnjson);
		return returnjson;
	}
	
	/**
	 * 根据订单号查询发票信息赋值发票代码号码
	 * @param ids
	 */
	public String getInvoiceByOrderno(String ids[],String identity){
		String invSvr = "";
		if(StrUtils.isNull(identity)){
			MessageUtils.alert("身份认证ID不能为空");
			return "";
		}
		for(int i=0;i<ids.length;i++){
			FinaInvoice inv = finaInvoiceDao.findById(Long.parseLong(ids[i]));
			String orderNo = inv.getSeqno();
			if(!StrUtils.isNull(orderNo)){//如果不为空，说明已经开票，可以通过发票流水号查询信息
				String inviceInfoJson = inviceInfo(orderNo, identity);
				if(!StrUtils.isNull(inviceInfoJson)){
					JSONObject inviceInfo = JSONObject.fromObject(inviceInfoJson);
					String result = inviceInfo.get("result")!=null?inviceInfo.get("result").toString():"";
					if("success".equals(result)){
						JSONArray infoList =  JSONArray.fromObject(inviceInfo.get("list"));
						String c_msg = infoList.getJSONObject(0).getString("c_msg");//开票状态
						System.out.println("开票状态："+c_msg);
						inv.setInvstatus(c_msg);
						invSvr += "<br/>开票状态："+c_msg;
						invSvr += "<br/><br/>结果信息："+infoList.getJSONObject(0).getString("c_resultmsg");
						if("开票失败".equals(c_msg)){
							inv.setIsinvsvr(false);
							inv.setIscancel(false);
							inv.setFpqqlsh("");
							invSvr += "<br/><br/>开票失败，请重新开具发票";
						}else{
							inv.setInvoiceno(infoList.getJSONObject(0).getString("c_fphm"));
							inv.setInvoicecode(infoList.getJSONObject(0).getString("c_fpdm"));
							inv.setIsinvsvr(true);
							inv.setIscancel(false);
							inv.setFpqqlsh(infoList.getJSONObject(0).getString("c_fpqqlsh"));
							
							String pdfUrl = infoList.getJSONObject(0).getString("c_url");
							inv.setUrlpdf(pdfUrl);
							
							invSvr += " 发票号："+infoList.getJSONObject(0).getString("c_fphm");
							invSvr += " <a href='"+pdfUrl+"' target='_blank'>下载PDF</a>";
							invSvr += " <a href='http://"+infoList.getJSONObject(0).getString("c_jpg_url")+"' target='_blank'>打开诺诺</a>";
						}
						try {
							finaInvoiceDao.modify(inv);
						} catch (Exception e) {
							MessageUtils.showException(e);
						}
					}else{
						invSvr += "<br/>[获取失败]:"+inviceInfo.get("errorMsg");
					}
				}
				invSvr += "<br/>------------------------------------------------------------";
			}else{
				invSvr += "<br/>[此发票还未开具发票，请先开具发票]";
				invSvr += "<br/>------------------------------------------------------------";
			}
		}
		return invSvr;
	}
	
	public String printinvoice(String ids[],String infoKindTypev,String printKind){
		String printInvSvr = "";
		for(int i = 0;i<ids.length;i++){
			FinaInvoice inv = finaInvoiceDao.findById(Long.parseLong(ids[i]));
			String datejson = "{\"InfoKind\":"+infoKindTypev+",\"InfoNumber\":\""+inv.getInvoiceno()+"\",\"InfoTypeCode\":\""+inv.getInvoicecode()
							+"\",\"PrintKind\":"+(StrUtils.isNull(printKind)?0:printKind)+",\"ShowPrintDlg\":0"+"}";
			String para = "SID=2&SIDParam="+Base64.encode(datejson,"GB2312").replaceAll("\\+","_");
			String returnjson = AppUtils.sendPost(ConfigUtils.findSysCfgVal("sys_invsvr_url"),para );
			JSONObject json = JSONObject.fromObject(returnjson);
			String ENCMSG = Base64.decode(json.getString("ENCMSG").replaceAll("_","+"),"GB2312");
			JSONObject ENCMSGjson = JSONObject.fromObject(ENCMSG);
			String retcode = ENCMSGjson.get("retcode")!=null?ENCMSGjson.get("retcode").toString():"";
			String retmsg = ENCMSGjson.get("retmsg")!=null?ENCMSGjson.get("retmsg").toString():"";
			printInvSvr += "\n"+inv.getInvoiceno()+":";
			printInvSvr += "\n返回码："+(retcode.equals("5011")?retcode+"-打印成功":retcode+"-打印失败");
			printInvSvr += "\n返回信息："+retmsg;
			printInvSvr += "\n------------------------------------------------------------";
		}
		return printInvSvr;
	}
	
	public String cancelInvoice(String[] ids, String infoKindTypev) {
		String printInvSvr = "";
		for(int i = 0;i<ids.length;i++){
			FinaInvoice inv = finaInvoiceDao.findById(Long.parseLong(ids[i]));
			String datejson = "{\"InfoKind\":"+infoKindTypev+",\"InfoNumber\":\""+inv.getInvoiceno()+"\",\"InfoTypeCode\":\""+inv.getInvoicecode()
							+"\",\"issuer\":\""+AppUtils.getUserSession().getUsername()+"\"}";
			String para = "SID=4&SIDParam="+Base64.encode(datejson,"GB2312").replaceAll("\\+","_");
			String returnjson = AppUtils.sendPost(ConfigUtils.findSysCfgVal("sys_invsvr_url"),para );
			JSONObject json = JSONObject.fromObject(returnjson);
			String ENCMSG = Base64.decode(json.getString("ENCMSG").replaceAll("_","+"),"GB2312");
			JSONObject ENCMSGjson = JSONObject.fromObject(ENCMSG);
			String retcode = ENCMSGjson.get("retcode")!=null?ENCMSGjson.get("retcode").toString():"";
			String retmsg = ENCMSGjson.get("retmsg")!=null?ENCMSGjson.get("retmsg").toString():"";
			printInvSvr += "\n"+inv.getInvoiceno()+":";
			printInvSvr += "\n返回码："+(retcode.equals("6011")?retcode+"-作废成功":retcode+"-作废失败");
			printInvSvr += "\n返回信息："+retmsg;
			if(retcode!=null&&retcode.equals("6011")){
				inv.setIscancel(true);
				try {
					finaInvoiceDao.modify(inv);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return "";
				}
			}
			printInvSvr += "\n------------------------------------------------------------";
		}
		return printInvSvr;
	}
}
