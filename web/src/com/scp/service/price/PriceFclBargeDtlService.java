package com.scp.service.price;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.SaveState;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.price.PriceFclBargefeeDtlDao;
import com.scp.model.price.PriceFclBargefeeDtl;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class PriceFclBargeDtlService {
	
	@Resource
	public PriceFclBargefeeDtlDao priceFclBargefeeDtlDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	
	public void saveData(PriceFclBargefeeDtl data) {
		if (0 > data.getId()) {
			priceFclBargefeeDtlDao.create(data);
		} else {
			priceFclBargefeeDtlDao.modify(data);
		}
	}

	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceFclBargefeeDtl data = new PriceFclBargefeeDtl();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = priceFclBargefeeDtlDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("polnamee")!=null){
				data.setPolnamee(String.valueOf(jsonObject.get("polnamee")));
			}else{
				data.setPolnamee(null);
			}
			if(jsonObject.get("polnamec")!=null){
				data.setPolnamec(String.valueOf(jsonObject.get("polnamec")));
			}else{
				data.setPolnamec(null);
			}
			if(jsonObject.get("podnamee")!=null){
				data.setPodnamee(String.valueOf(jsonObject.get("podnamee")));
			}else{
				data.setPodnamee(null);
			}
			if(jsonObject.get("podnamec")!=null){
				data.setPodnamec(String.valueOf(jsonObject.get("podnamec")));
			}else{
				data.setPodnamec(null);
			}
			if(jsonObject.get("cost20")!=null && !String.valueOf(jsonObject.get("cost20")).isEmpty()){
				data.setCost20(new BigDecimal(String.valueOf(jsonObject.get("cost20"))));
			}else{
				data.setCost20(new BigDecimal(0));
			}
			if(jsonObject.get("cost40gp")!=null && !String.valueOf(jsonObject.get("cost40gp")).isEmpty()){
				data.setCost40gp(new BigDecimal(String.valueOf(jsonObject.get("cost40gp"))));
			}else{
				data.setCost40gp(new BigDecimal(0));
			}
			if(jsonObject.get("cost40hq")!=null && !String.valueOf(jsonObject.get("cost40hq")).isEmpty()){
				data.setCost40hq(new BigDecimal(String.valueOf(jsonObject.get("cost40hq"))));
			}else{
				data.setCost40hq(new BigDecimal(0));
			}
			if(jsonObject.get("cost45hq")!=null && !String.valueOf(jsonObject.get("cost45hq")).isEmpty()){
				data.setCost45hq(new BigDecimal(String.valueOf(jsonObject.get("cost45hq"))));
			}else{
				data.setCost45hq(new BigDecimal(0));
			}
			if(jsonObject.get("cost202")!=null && !String.valueOf(jsonObject.get("cost202")).isEmpty()){
				data.setCost202(new BigDecimal(String.valueOf(jsonObject.get("cost202"))));
			}else{
				data.setCost202(new BigDecimal(0));
			}
			if(jsonObject.get("cost40gp2")!=null && !String.valueOf(jsonObject.get("cost40gp2")).isEmpty()){
				data.setCost40gp2(new BigDecimal(String.valueOf(jsonObject.get("cost40gp2"))));
			}else{
				data.setCost40gp2(new BigDecimal(0));
			}
			if(jsonObject.get("cost40hq2")!=null && !String.valueOf(jsonObject.get("cost40hq2")).isEmpty()){
				data.setCost40hq2(new BigDecimal(String.valueOf(jsonObject.get("cost40hq2"))));
			}else{
				data.setCost40hq2(new BigDecimal(0));
			}
			if(jsonObject.get("cost45hq2")!=null && !String.valueOf(jsonObject.get("cost45hq2")).isEmpty()){
				data.setCost45hq2(new BigDecimal(String.valueOf(jsonObject.get("cost45hq2"))));
			}else{
				data.setCost40hq2(new BigDecimal(0));
			}
			if(jsonObject.get("area")!=null){
				data.setArea(String.valueOf(jsonObject.get("area")));
			}else{
				data.setArea(null);
			}
			if(jsonObject.get("remark")!=null){
				data.setRemark(String.valueOf(jsonObject.get("remark")));
			}else{
				data.setRemark(null);
			}
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			
			if(jsonObject.get("conditiontype")!=null){
				data.setConditiontype(String.valueOf(jsonObject.get("conditiontype")));
			}else{
				data.setConditiontype(null);
			}
			if(jsonObject.get("condition")!=null){
				data.setCondition(String.valueOf(jsonObject.get("condition")));
			}else{
				data.setCondition(null);
			}
			if(jsonObject.get("conditionvalue")!=null){
				data.setConditionvalue(String.valueOf(jsonObject.get("conditionvalue")));
			}else{
				data.setConditionvalue(null);
			}
			if(jsonObject.get("conditiontype2")!=null){
				data.setConditiontype2(String.valueOf(jsonObject.get("conditiontype2")));
			}else{
				data.setConditiontype2(null);
			}
			if(jsonObject.get("condition2")!=null){
				data.setCondition2(String.valueOf(jsonObject.get("condition2")));
			}else{
				data.setCondition2(null);
			}
			if(jsonObject.get("conditionvalue2")!=null){
				data.setConditionvalue2(String.valueOf(jsonObject.get("conditionvalue2")));
			}else{
				data.setConditionvalue2(null);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String datefm=null;
			if(jsonObject.get("datefm") != null){
				datefm=String.valueOf(jsonObject.get("datefm"));
			}
			String dateto=null;
			if(jsonObject.get("dateto") != null){
				dateto=String.valueOf(jsonObject.get("dateto"));
			}
			try {
				if(!StrUtils.isNull(datefm)){
					data.setDatefm(sdf.parse(datefm));
				}
				if(!StrUtils.isNull(dateto)){
					data.setDateto(sdf.parse(dateto));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(jsonObject.get("line")!=null){
				data.setLine(String.valueOf(jsonObject.get("line")));
			}else{
				data.setLine(null);
			}
			saveData(data);
		}		
	}
	
	public void addBatchEditGrid(Object addData, long bargefeeid) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			PriceFclBargefeeDtl data = new PriceFclBargefeeDtl();
			data.setId(-1L);
			if(data.getBargefeeid()==null){
				data.setBargefeeid(bargefeeid);
			}
			if(jsonObject.get("polnamee")!=null){
				data.setPolnamee(String.valueOf(jsonObject.get("polnamee")));
			}else{
				data.setPolnamee(null);
			}
			if(jsonObject.get("polnamec")!=null){
				data.setPolnamec(String.valueOf(jsonObject.get("polnamec")));
			}else{
				data.setPolnamec(null);
			}
			if(jsonObject.get("podnamee")!=null){
				data.setPodnamee(String.valueOf(jsonObject.get("podnamee")));
			}else{
				data.setPodnamee(null);
			}
			if(jsonObject.get("podnamec")!=null){
				data.setPodnamec(String.valueOf(jsonObject.get("podnamec")));
			}else{
				data.setPodnamec(null);
			}
			if(jsonObject.get("cost20")!=null && !String.valueOf(jsonObject.get("cost20")).isEmpty()){
				data.setCost20(new BigDecimal(String.valueOf(jsonObject.get("cost20"))));
			}else{
				data.setCost20(new BigDecimal(0));
			}
			if(jsonObject.get("cost40gp")!=null && !String.valueOf(jsonObject.get("cost40gp")).isEmpty()){
				data.setCost40gp(new BigDecimal(String.valueOf(jsonObject.get("cost40gp"))));
			}else{
				data.setCost40gp(new BigDecimal(0));
			}
			if(jsonObject.get("cost40hq")!=null && !String.valueOf(jsonObject.get("cost40hq")).isEmpty()){
				data.setCost40hq(new BigDecimal(String.valueOf(jsonObject.get("cost40hq"))));
			}else{
				data.setCost40hq(new BigDecimal(0));
			}
			if(jsonObject.get("cost45hq")!=null && !String.valueOf(jsonObject.get("cost45hq")).isEmpty()){
				data.setCost45hq(new BigDecimal(String.valueOf(jsonObject.get("cost45hq"))));
			}else{
				data.setCost45hq(new BigDecimal(0));
			}
			if(jsonObject.get("cost202")!=null && !String.valueOf(jsonObject.get("cost202")).isEmpty()){
				data.setCost202(new BigDecimal(String.valueOf(jsonObject.get("cost202"))));
			}else{
				data.setCost202(new BigDecimal(0));
			}
			if(jsonObject.get("cost40gp2")!=null && !String.valueOf(jsonObject.get("cost40gp2")).isEmpty()){
				data.setCost40gp2(new BigDecimal(String.valueOf(jsonObject.get("cost40gp2"))));
			}else{
				data.setCost40gp2(new BigDecimal(0));
			}
			if(jsonObject.get("cost40hq2")!=null && !String.valueOf(jsonObject.get("cost40hq2")).isEmpty()){
				data.setCost40hq2(new BigDecimal(String.valueOf(jsonObject.get("cost40hq2"))));
			}else{
				data.setCost40hq2(new BigDecimal(0));
			}
			if(jsonObject.get("cost45hq2")!=null && !String.valueOf(jsonObject.get("cost45hq2")).isEmpty()){
				data.setCost45hq2(new BigDecimal(String.valueOf(jsonObject.get("cost45hq2"))));
			}else{
				data.setCost40hq2(new BigDecimal(0));
			}
			if(jsonObject.get("area")!=null){
				data.setArea(String.valueOf(jsonObject.get("area")));
			}else{
				data.setArea(null);
			}
			if(jsonObject.get("remark")!=null){
				data.setRemark(String.valueOf(jsonObject.get("remark")));
			}else{
				data.setRemark(null);
			}
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("line")!=null){
				data.setLine(String.valueOf(jsonObject.get("line")));
			}else{
				data.setLine(null);
			}
			if(jsonObject.get("conditiontype")!=null){
				data.setConditiontype(String.valueOf(jsonObject.get("conditiontype")));
			}else{
				data.setConditiontype(null);
			}
			if(jsonObject.get("condition")!=null){
				data.setCondition(String.valueOf(jsonObject.get("condition")));
			}else{
				data.setCondition(null);
			}
			if(jsonObject.get("conditionvalue")!=null){
				data.setConditionvalue(String.valueOf(jsonObject.get("conditionvalue")));
			}else{
				data.setConditionvalue(null);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String datefm=null;
			if(jsonObject.get("datefm") != null){
				datefm=String.valueOf(jsonObject.get("datefm"));
			}
			String dateto=null;
			if(jsonObject.get("dateto") != null){
				dateto=String.valueOf(jsonObject.get("dateto"));
			}
			try {
				if(!StrUtils.isNull(datefm)){
					data.setDatefm(sdf.parse(datefm));
				}
				if(!StrUtils.isNull(dateto)){
					data.setDateto(sdf.parse(dateto));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			saveData(data);
		}
	}
	
	public void removedBatchEditGrid(Object removedData) {
		JSONArray jsonArray = (JSONArray) removedData;
		PriceFclBargefeeDtl data = new PriceFclBargefeeDtl();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("id")!=null&&!String.valueOf(jsonObject.get("id")).isEmpty()&&Pattern.matches("^\\d+$", String.valueOf(jsonObject.get("id")))){
				ids += String.valueOf(jsonObject.get("id"))+",";
			}
		}
		if(!ids.isEmpty()){
			ids = ids.substring(0, ids.length()-1);
			String sql = "delete from price_fcl_bargefeedtl where id IN ("+ids+");";
			priceFclBargefeeDtlDao.executeSQL(sql);
		}
	}
	
	@Resource
	protected ServiceContext serviceContext;
	
	@SaveState
	protected List<PriceFclFeeadd> moduleList = null;
	
	public void saveBatchEditGrid(String[] ids){
		PriceFclBargefeeDtl data = new PriceFclBargefeeDtl();
		for (String id : ids) {
			data.setId(-1L);
			PriceFclBargefeeDtl old = priceFclBargefeeDtlDao.findById(Long.valueOf(id));
			data.setId(-1L);
			data.setBargefeeid(old.getBargefeeid());
			data.setPolnamee(old.getPolnamee());
			data.setPolnamec(old.getPolnamec());
			data.setPodnamee(old.getPodnamee());
			data.setPodnamec(old.getPodnamee());
			data.setCost20(old.getCost20());
			data.setCost40gp(old.getCost40gp());
			data.setCost40hq(old.getCost40hq());
			data.setCost45hq(old.getCost45hq());
			data.setCost202(old.getCost202());
			data.setCost40gp2(old.getCost40gp2());
			data.setCost40hq2(old.getCost40hq2());
			data.setArea(old.getArea());
			data.setRemark(old.getRemark());
			data.setCurrency(old.getCurrency());
			data.setLine(old.getLine());
			data.setIsinvalid(old.getIsinvalid());
			data.setConditiontype(old.getConditiontype());
			data.setCondition(old.getCondition());
			data.setConditionvalue(old.getConditionvalue());
			data.setConditiontype2(old.getConditiontype2());
			data.setCondition2(old.getCondition2());
			data.setConditionvalue2(old.getConditionvalue2());
			data.setDatefm(old.getDatefm());
			data.setDateto(old.getDateto());
			data.setCorpid(old.getCorpid());
			data.setIsrelease(old.getIsrelease());
			saveData(data);
			long dataid = data.getId();
			Long idv = Long.parseLong(id);
			List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid="+idv + " AND isdelete = false ORDER BY id");
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
	
	public void saveBatchEditGrid(Object modifiedData,long bargefeeid){
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceFclBargefeeDtl data = new PriceFclBargefeeDtl();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			data.setId(-1L);
			if(data.getBargefeeid()==null){
				data.setBargefeeid(bargefeeid);
			}
			if(jsonObject.get("polnamee")!=null){
				data.setPolnamee(String.valueOf(jsonObject.get("polnamee")));
			}else{
				data.setPolnamee(null);
			}
			if(jsonObject.get("polnamec")!=null){
				data.setPolnamec(String.valueOf(jsonObject.get("polnamec")));
			}else{
				data.setPolnamec(null);
			}
			if(jsonObject.get("podnamee")!=null){
				data.setPodnamee(String.valueOf(jsonObject.get("podnamee")));
			}else{
				data.setPodnamee(null);
			}
			if(jsonObject.get("podnamec")!=null){
				data.setPodnamec(String.valueOf(jsonObject.get("podnamec")));
			}else{
				data.setPodnamec(null);
			}
			if(jsonObject.get("cost20")!=null && !String.valueOf(jsonObject.get("cost20")).isEmpty()){
				data.setCost20(new BigDecimal(String.valueOf(jsonObject.get("cost20"))));
			}else{
				data.setCost20(new BigDecimal(0));
			}
			if(jsonObject.get("cost40gp")!=null && !String.valueOf(jsonObject.get("cost40gp")).isEmpty()){
				data.setCost40gp(new BigDecimal(String.valueOf(jsonObject.get("cost40gp"))));
			}else{
				data.setCost40gp(new BigDecimal(0));
			}
			if(jsonObject.get("cost40hq")!=null && !String.valueOf(jsonObject.get("cost40hq")).isEmpty()){
				data.setCost40hq(new BigDecimal(String.valueOf(jsonObject.get("cost40hq"))));
			}else{
				data.setCost40hq(new BigDecimal(0));
			}
			if(jsonObject.get("cost45hq")!=null && !String.valueOf(jsonObject.get("cost45hq")).isEmpty()){
				data.setCost45hq(new BigDecimal(String.valueOf(jsonObject.get("cost45hq"))));
			}else{
				data.setCost45hq(new BigDecimal(0));
			}
			if(jsonObject.get("cost202")!=null && !String.valueOf(jsonObject.get("cost202")).isEmpty()){
				data.setCost202(new BigDecimal(String.valueOf(jsonObject.get("cost202"))));
			}else{
				data.setCost202(new BigDecimal(0));
			}
			if(jsonObject.get("cost40gp2")!=null && !String.valueOf(jsonObject.get("cost40gp2")).isEmpty()){
				data.setCost40gp2(new BigDecimal(String.valueOf(jsonObject.get("cost40gp2"))));
			}else{
				data.setCost40gp2(new BigDecimal(0));
			}
			if(jsonObject.get("cost40hq2")!=null && !String.valueOf(jsonObject.get("cost40hq2")).isEmpty()){
				data.setCost40hq2(new BigDecimal(String.valueOf(jsonObject.get("cost40hq2"))));
			}else{
				data.setCost40hq2(new BigDecimal(0));
			}
			if(jsonObject.get("area")!=null){
				data.setArea(String.valueOf(jsonObject.get("area")));
			}else{
				data.setArea(null);
			}
			if(jsonObject.get("remark")!=null){
				data.setRemark(String.valueOf(jsonObject.get("remark")));
			}else{
				data.setRemark(null);
			}
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("line")!=null){
				data.setLine(String.valueOf(jsonObject.get("line")));
			}else{
				data.setLine(null);
			}
			String isinvalid=String.valueOf(jsonObject.get("isinvalid"));
			if("true".equals(isinvalid)){
				data.setIsinvalid(true);
			}else if("false".equals(isinvalid)){
				data.setIsinvalid(false);
			}
			if(jsonObject.get("conditiontype")!=null){
				data.setConditiontype(String.valueOf(jsonObject.get("conditiontype")));
			}else{
				data.setConditiontype(null);
			}
			if(jsonObject.get("condition")!=null){
				data.setCondition(String.valueOf(jsonObject.get("condition")));
			}else{
				data.setCondition(null);
			}
			if(jsonObject.get("conditionvalue")!=null){
				data.setConditionvalue(String.valueOf(jsonObject.get("conditionvalue")));
			}else{
				data.setConditionvalue(null);
			}
			if(jsonObject.get("conditiontype2")!=null){
				data.setConditiontype2(String.valueOf(jsonObject.get("conditiontype2")));
			}else{
				data.setConditiontype2(null);
			}
			if(jsonObject.get("condition2")!=null){
				data.setCondition2(String.valueOf(jsonObject.get("condition2")));
			}else{
				data.setCondition2(null);
			}
			if(jsonObject.get("conditionvalue2")!=null){
				data.setConditionvalue2(String.valueOf(jsonObject.get("conditionvalue2")));
			}else{
				data.setConditionvalue2(null);
			}
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String datefm=null;
			if(jsonObject.get("datefm") != null){
				datefm=String.valueOf(jsonObject.get("datefm"));
			}
			String dateto=null;
			if(jsonObject.get("dateto") != null){
				dateto=String.valueOf(jsonObject.get("dateto"));
			}
			try {
				if(!StrUtils.isNull(datefm)){
					data.setDatefm(sdf.parse(datefm));
				}
				if(!StrUtils.isNull(dateto)){
					data.setDateto(sdf.parse(dateto));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.setCorpid(AppUtils.getUserSession().getCorpid());
			data.setIsrelease(false);
			saveData(data);
			
			long dataid = data.getId();
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
	
	public void updateBatch(String[] ids, String column, String value,String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl_bargefeedtl SET " + column + " = '" + value + "',updater = '"+usercode+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		priceFclBargefeeDtlDao.executeSQL(stringBuffer.toString());
	}
	
	public void clearBatch(String[] ids,String column,String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_fcl_bargefeedtl SET " + column + " = null , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		priceFclBargefeeDtlDao.executeSQL(stringBuffer.toString());
	}
	
	
}
