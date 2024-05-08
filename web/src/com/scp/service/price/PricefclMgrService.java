package com.scp.service.price;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.SaveState;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.price.PricefclDao;
import com.scp.exception.NoRowException;
import com.scp.model.price.PriceFcl;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class PricefclMgrService {
	
	@Resource
	public PricefclDao pricefclDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	
	public void saveData(PriceFcl data) {
		if (0 > data.getId()) {
			pricefclDao.create(data);
		} else {
			pricefclDao.modify(data);
		}
	}
	
	public void release(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET isrelease = TRUE , daterelease = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
	}

	public void removes(String[] ids, String username) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET isdelete = TRUE, updatetime = NOW(),updater = '"+username+"' WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}

	public void updateBatch(String[] ids, String amtadd, String amttype, String type, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			if("+".equals(amttype)){
				String sql = "\nUPDATE price_fcl SET " + "cost" + type + " = cost" + type + " + " + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuffer.append(sql);
			}else{
				String sql = "\nUPDATE price_fcl SET " + "cost" + type + " = cost" + type + " - " + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuffer.append(sql);
			}
			
		}
		//AppUtils.debug(stringBuffer);
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBaseBatch(String[] ids, String amtadd, String amttype, String type, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			if("+".equals(amttype)){
				String sql = "\nUPDATE price_fcl SET " + "base" + type + " = base" + type + " + " + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuffer.append(sql);
			}else{
				String sql = "\nUPDATE price_fcl SET " + "base" + type + " = base" + type + " - " + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuffer.append(sql);
			}
			
		}
		//AppUtils.debug(stringBuffer);
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBatchValue(String[] ids, String amtadd, String type, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET " + "cost" + type + " = " + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBaseBatchValue(String[] ids, String amtadd, String type, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET " + "base" + type + " = " + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBatchother(String[] ids, String amtadd, String amttype, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			if("+".equals(amttype)){
				String sql = "\nUPDATE price_fcl SET pieceother=pieceother+" + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuffer.append(sql);
			}else{
				String sql = "\nUPDATE price_fcl SET pieceother=pieceother-" + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuffer.append(sql);
			}
			
		}
		//AppUtils.debug(stringBuffer);
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBaseBatchother(String[] ids, String amtadd, String amttype, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			if("+".equals(amttype)){
				String sql = "\nUPDATE price_fcl SET baseother=baseother+" + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuffer.append(sql);
			}else{
				String sql = "\nUPDATE price_fcl SET baseother=baseother-" + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
				stringBuffer.append(sql);
			}
			
		}
		//AppUtils.debug(stringBuffer);
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBatchotherValue(String[] ids, String amtadd, String type, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET pieceother=" + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		//AppUtils.debug(stringBuffer);
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBaseBatchotherValue(String[] ids, String amtadd, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET baseother=" + amtadd  + " , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		//AppUtils.debug(stringBuffer);
		pricefclDao.executeSQL(stringBuffer.toString());
		
	}


	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceFcl ddata = new PriceFcl();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			String pol=String.valueOf(jsonObject.get("pol")).trim();
			String pod=String.valueOf(jsonObject.get("pod")).trim();
			String shipping=String.valueOf(jsonObject.get("shipping")==null?"":jsonObject.get("shipping"));
			String cost20=null;
			if(jsonObject.get("cost20") != null){
				cost20=String.valueOf(jsonObject.get("cost20"));
			}
			String cost40gp=null;
			if(jsonObject.get("cost40gp") != null){
				cost40gp=String.valueOf(jsonObject.get("cost40gp"));
			}
			String cost40hq=null;
			if(jsonObject.get("cost40hq") != null){
				cost40hq=String.valueOf(jsonObject.get("cost40hq"));
			}
			String cost45hq =null;
			if(jsonObject.get("cost45hq") != null){
				cost45hq=String.valueOf(jsonObject.get("cost45hq"));
			}
			String schedule=String.valueOf(jsonObject.get("schedule")==null?"":jsonObject.get("schedule"));
			String tt=String.valueOf(jsonObject.get("tt")==null?"":jsonObject.get("tt"));
			String via=String.valueOf(jsonObject.get("via")==null?"":jsonObject.get("via"));
			String line=String.valueOf(jsonObject.get("line")==null?"":jsonObject.get("line"));
			String currency=String.valueOf(jsonObject.get("currency")==null?"":jsonObject.get("currency"));
			String typestart=String.valueOf(jsonObject.get("typestart")==null?"":jsonObject.get("typestart"));
			String typeend=String.valueOf(jsonObject.get("typeend")==null?"":jsonObject.get("typeend"));
			
			////System.out.println(jsonObject);
			
			ddata = pricefclDao.findById(Long.valueOf(id));
			ddata.setPol(pol);
			ddata.setPod(pod);
			ddata.setShipping(shipping);
			if(!StrUtils.isNull(cost20)){
				ddata.setCost20(new BigDecimal(cost20));
			}
			if(!StrUtils.isNull(cost40gp)){
				ddata.setCost40gp(new BigDecimal(cost40gp));
			}
			if(!StrUtils.isNull(cost40hq)){
				ddata.setCost40hq(new BigDecimal(cost40hq));
			}
			if(!StrUtils.isNull(cost45hq)){
				ddata.setCost45hq(new BigDecimal(cost45hq));
			}
			ddata.setSchedule(schedule);
			ddata.setTt(tt);
			ddata.setVia(via);
			ddata.setLine(line);
			ddata.setCurrency(currency);
			ddata.setTypestart(typestart);
			ddata.setTypeend(typeend);
			saveData(ddata);
		}		
	}

	public String refreshFeeDesc(Long pkVal) {
		String querySql = "SELECT feedesc FROM _price_fcl WHERE id = "+pkVal+";";
		Map map;
		try {
			map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			String val = StrUtils.getMapVal(map, "feedesc");
			return val;
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}


	public void updateBatch(String[] ids, String column,
			String value, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET " + column + " = '" + StrUtils.getSqlFormat(value.trim()) + "' , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		//AppUtils.debug(stringBuffer);
		pricefclDao.executeSQL(stringBuffer.toString());
	}
	
	/** 
	 * 把 value追加到字段值
	 * @param ids
	 * @param column
	 * @param value
	 * @param usercode
	 */
	public void addUpdateBatch(String[] ids, String column,String value, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET " + column + " = COALESCE(" + column + ",'')||'" + StrUtils.getSqlFormat(value.trim()) + "' , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
	}
	
	public void removePriceByPriceName(String pricename) {
		String sql = "UPDATE price_fcl SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"', updatetime = NOW() WHERE pricename = '"+pricename+"' AND isdelete = FALSE";
		pricefclDao.executeSQL(sql);
	}
	
	
	@Resource
	protected ServiceContext serviceContext;
	
	@SaveState
	protected List<PriceFclFeeadd> moduleList = null;
	
	
	public void saveBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceFcl ddata = new PriceFcl();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			String pol=String.valueOf(jsonObject.get("pol")).trim();
			String pod=String.valueOf(jsonObject.get("pod")).trim();
			String shipping=String.valueOf(jsonObject.get("shipping")==null?"":jsonObject.get("shipping"));
			String cost20=null;
			if(jsonObject.get("cost20") != null){
				cost20=String.valueOf(jsonObject.get("cost20"));
			}
			String cost40gp=null;
			if(jsonObject.get("cost40gp") != null){
				cost40gp=String.valueOf(jsonObject.get("cost40gp"));
			}
			String cost40hq=null;
			if(jsonObject.get("cost40hq") != null){
				cost40hq=String.valueOf(jsonObject.get("cost40hq"));
			}
			String cost45hq =null;
			if(jsonObject.get("cost45hq") != null){
				cost45hq=String.valueOf(jsonObject.get("cost45hq"));
			}
			String schedule=String.valueOf(jsonObject.get("schedule")==null?"":jsonObject.get("schedule"));
			String tt=String.valueOf(jsonObject.get("tt")==null?"":jsonObject.get("tt"));
			String via=String.valueOf(jsonObject.get("via")==null?"":jsonObject.get("via"));
			String line=String.valueOf(jsonObject.get("line")==null?"":jsonObject.get("line"));
			String currency=String.valueOf(jsonObject.get("currency")==null?"":jsonObject.get("currency"));
			String typestart=String.valueOf(jsonObject.get("typestart")==null?"":jsonObject.get("typestart"));
			String typeend=String.valueOf(jsonObject.get("typeend")==null?"":jsonObject.get("typeend"));
			String datefm=null;
			if(jsonObject.get("datefm") != null){
				datefm=String.valueOf(jsonObject.get("datefm"));
			}
			String dateto=null;
			if(jsonObject.get("dateto") != null){
				dateto=String.valueOf(jsonObject.get("dateto"));
			}
			String bargedatefm=null;
			if(jsonObject.get("bargedatefm") != null){
				bargedatefm=String.valueOf(jsonObject.get("bargedatefm"));
			}
			String bargedateto=null;
			if(jsonObject.get("bargedateto") != null){
				bargedateto=String.valueOf(jsonObject.get("bargedateto"));
			}
			String shipline=String.valueOf(jsonObject.get("shipline")==null?"":jsonObject.get("shipline"));
			String bargetype=String.valueOf(jsonObject.get("bargetype")==null?"":jsonObject.get("bargetype"));
			String bargetypend=String.valueOf(jsonObject.get("bargetypend")==null?"":jsonObject.get("bargetypend"));
			String country=String.valueOf(jsonObject.get("country")==null?"":jsonObject.get("country"));
			String remark_in=String.valueOf(jsonObject.get("remark_in")==null?"":jsonObject.get("remark_in"));
			String remark_out=String.valueOf(jsonObject.get("remark_out")==null?"":jsonObject.get("remark_out"));
			String pricename=String.valueOf(jsonObject.get("pricename")==null?"":jsonObject.get("pricename"));
			String bargepod=String.valueOf(jsonObject.get("bargepod")==null?"":jsonObject.get("bargepod"));
			String isrelease=String.valueOf(jsonObject.get("isrelease"));
			String isinvalid=String.valueOf(jsonObject.get("isinvalid"));
			String typestart2=String.valueOf(jsonObject.get("typestart2")==null?"":jsonObject.get("typestart2"));
			String typeend2=String.valueOf(jsonObject.get("typeend2")==null?"":jsonObject.get("typeend2"));
			String bartypestart2=String.valueOf(jsonObject.get("bartypestart2")==null?"":jsonObject.get("bartypestart2"));
			String bartypeend2=String.valueOf(jsonObject.get("bartypeend2")==null?"":jsonObject.get("bartypeend2"));
			String bargetypestr2=String.valueOf(jsonObject.get("bargetypestr2")==null?"":jsonObject.get("bargetypestr2"));
			String bargetypend2=String.valueOf(jsonObject.get("bargetypend2")==null?"":jsonObject.get("bargetypend2"));
			String baronend=String.valueOf(jsonObject.get("baronend")==null?"":jsonObject.get("baronend"));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ddata.setId(-1L);
			ddata.setPol(pol);
			ddata.setPod(pod);
			ddata.setShipping(shipping);
			if(!StrUtils.isNull(cost20)){
				ddata.setCost20(new BigDecimal(cost20));
			}
			if(!StrUtils.isNull(cost40gp)){
				ddata.setCost40gp(new BigDecimal(cost40gp));
			}
			if(!StrUtils.isNull(cost40hq)){
				ddata.setCost40hq(new BigDecimal(cost40hq));
			}
			if(!StrUtils.isNull(cost45hq)){
				ddata.setCost45hq(new BigDecimal(cost45hq));
			}
			ddata.setSchedule(schedule);
			ddata.setTt(tt);
			ddata.setVia(via);
			ddata.setLine(line);
			ddata.setCurrency(currency);
			ddata.setTypestart(typestart);
			ddata.setTypeend(typeend);
			try {
				if(!StrUtils.isNull(datefm)){
					ddata.setDatefm(sdf.parse(datefm));
				}
				if(!StrUtils.isNull(dateto)){
					ddata.setDateto(sdf.parse(dateto));
				}
				if(!StrUtils.isNull(bargedatefm)){
					ddata.setBargedatefm(sdf.parse(bargedatefm));
				}
				if(!StrUtils.isNull(bargedateto)){
					ddata.setBargedateto(sdf.parse(bargedateto));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ddata.setShipline(shipline);
			ddata.setBargetype(bargetype);
			ddata.setBargetypend(bargetypend);
			ddata.setCountry(country);
			ddata.setRemark_in(remark_in);
			ddata.setRemark_out(remark_out);
			ddata.setPricename(pricename);
			ddata.setBargepod(bargepod);
			ddata.setIsrelease(false);
			if("true".equals(isinvalid)){
				ddata.setIsinvalid(true);
			}else if("false".equals(isinvalid)){
				ddata.setIsinvalid(false);
			}
			ddata.setInputer(AppUtils.getUserSession().getUsercode());
			ddata.setInputtime(Calendar.getInstance().getTime());
			ddata.setCorpid(AppUtils.getUserSession().getCorpid());
			
			ddata.setTypestart2(typestart2);
			ddata.setTypeend2(typeend2);
			ddata.setBartypestart2(bartypestart2);
			ddata.setBartypeend2(bartypeend2);
			ddata.setBargetypestr2(bargetypestr2);
			ddata.setBargetypend2(bargetypend2);
			ddata.setBaronend(baronend);
			saveData(ddata);
			
			long dataid = ddata.getId();
			Long ids = Long.parseLong(id);
			List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid="+ids + " AND isdelete = false ORDER BY id");
			moduleList = new ArrayList<PriceFclFeeadd>();
			for (PriceFclFeeadd fclFeeadd : list) {
	        	PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
	        	priceFclFeeadd.setAmt(fclFeeadd.getAmt());
	        	priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
	        	priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
	        	priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
	        	priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
	        	priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
	        	priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
	        	priceFclFeeadd.setFclid(dataid);
	        	priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
	        	priceFclFeeadd.setFeeitemcode(fclFeeadd.getFeeitemcode());
	        	priceFclFeeadd.setFeeitemname(fclFeeadd.getFeeitemname());
				priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
				priceFclFeeadd.setUnit(fclFeeadd.getUnit());
				priceFclFeeadd.setInputer(AppUtils.getUserSession().getUsercode());
				priceFclFeeadd.setInputtime(Calendar.getInstance().getTime());
				priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
				moduleList.add(priceFclFeeadd);
			}
			 serviceContext.pricefclfeeaddMgrService.saveOrModify(moduleList);
			
		}		
	}
	
	
	public void unpublish(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET isrelease = FALSE  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
	}

	public void stop(String[] ids , boolean stop) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET istop = "+stop+"  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
	}
	
	
	
	public void saveBatch2EditGrid(String[] ids) {
		PriceFcl ddata = new PriceFcl();
		for (String id : ids) {
			PriceFcl old = pricefclDao.findById(Long.valueOf(id));
			
			ddata.setId(-1L);
			ddata.setPol(old.getPol());
			ddata.setPod(old.getPod());
			ddata.setShipping(old.getShipping());
			ddata.setCost20(old.getCost20());
			ddata.setCost40gp(old.getCost40gp());
			ddata.setCost40hq(old.getCost40hq());
			ddata.setCost45hq(old.getCost45hq());
			ddata.setPieceother(old.getPieceother());
			ddata.setCntypeothercode(old.getCntypeothercode());
			ddata.setSchedule(old.getSchedule());
			ddata.setTt(old.getTt());
			ddata.setVia(old.getVia());
			ddata.setLine(old.getLine());
			ddata.setCurrency(old.getCurrency());
			ddata.setTypestart(old.getTypestart());
			ddata.setTypeend(old.getTypeend());
			ddata.setDatefm(old.getDatefm());
			ddata.setDateto(old.getDateto());
			ddata.setBargedatefm(old.getBargedatefm());
			ddata.setBargedateto(old.getBargedateto());
			ddata.setShipline(old.getShipline());
			ddata.setBargetype(old.getBargetype());
			ddata.setBargetypend(old.getBargetypend());
			ddata.setCountry(old.getCountry());
			ddata.setPricename(old.getPricename());
			ddata.setIsrelease(false);
			ddata.setIsinvalid(old.getIsinvalid());
			ddata.setRemark_in(old.getRemark_in());
			ddata.setRemark_out(old.getRemark_out());
			ddata.setContacter(old.getContacter());
			ddata.setInputer(AppUtils.getUserSession().getUsercode());
			ddata.setInputtime(Calendar.getInstance().getTime());
			ddata.setCorpid(AppUtils.getUserSession().getCorpid());
			ddata.setBargepod(old.getBargepod());
			
			ddata.setTypestart2(old.getTypestart2());
			ddata.setTypeend2(old.getTypeend2());
			ddata.setBartypestart2(old.getBartypestart2());
			ddata.setBartypeend2(old.getBartypeend2());
			ddata.setBargetypestr2(old.getBargetypestr2());
			ddata.setBargetypend2(old.getBargetypend2());
			ddata.setBaronend(old.getBaronend());
			ddata.setPollink(old.getPollink());
			ddata.setCarrier(old.getCarrier());
			
			saveData(ddata);
			
			long dataid = ddata.getId();
			Long fclid = Long.parseLong(id);
			List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid="+fclid + " AND isdelete = false ORDER BY id");
			moduleList = new ArrayList<PriceFclFeeadd>();
			for (PriceFclFeeadd fclFeeadd : list) {
	        	PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
	        	priceFclFeeadd.setAmt(fclFeeadd.getAmt());
	        	priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
	        	priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
	        	priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
	        	priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
	        	priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
	        	priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
	        	priceFclFeeadd.setFclid(dataid);
	        	priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
	        	priceFclFeeadd.setFeeitemcode(fclFeeadd.getFeeitemcode());
	        	priceFclFeeadd.setFeeitemname(fclFeeadd.getFeeitemname());
				priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
				priceFclFeeadd.setUnit(fclFeeadd.getUnit());
				priceFclFeeadd.setInputer(AppUtils.getUserSession().getUsercode());
				priceFclFeeadd.setInputtime(Calendar.getInstance().getTime());
				priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
				moduleList.add(priceFclFeeadd);
			}
			 serviceContext.pricefclfeeaddMgrService.saveOrModify(moduleList);
			
		}		
	}
	
	
	public void clearBatch(String[] ids,String column,String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET " + column + " = null , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
	}
	
	public void clearPriceBatch(String[] ids,String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET cost20 = null,cost40gp = null,cost40hq = null,cost45hq = null,pieceother = null, updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
	}
	
	public void clearBasePriceBatch(String[] ids,String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET base20 = null,base40gp = null,base40hq = null,base45hq = null,baseother = null, updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
	}
	
	public void updateCntypeothercode(String[] ids,String cntypeothercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl SET cntypeothercode = '"+cntypeothercode+"', updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		pricefclDao.executeSQL(stringBuffer.toString());
	}
}
