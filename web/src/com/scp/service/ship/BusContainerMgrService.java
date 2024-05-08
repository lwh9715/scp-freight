package com.scp.service.ship;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusPacklistDao;
import com.scp.model.ship.BusPacklist;

@Component
public class BusContainerMgrService{

	
	@Resource
	public BusPacklistDao busPacklistDao; 
	

	public void saveData(BusPacklist data) {
		if(0 == data.getId()){
			busPacklistDao.create(data);
		}else{
			busPacklistDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusPacklist data = busPacklistDao.findById(id);
		busPacklistDao.remove(data);
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		BusPacklist data = new BusPacklist();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = busPacklistDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("factoryname")!=null){
				data.setFactoryname(String.valueOf(jsonObject.get("factoryname")));
			}else{
				data.setFactoryname(null);
			}
			if(jsonObject.get("factorynamee")!=null){
				data.setFactorynamee(String.valueOf(jsonObject.get("factorynamee")));
			}else{
				data.setFactorynamee(null);
			}
			if(jsonObject.get("goodsname2")!=null){
				data.setGoodsname2(String.valueOf(jsonObject.get("goodsname2")));
			}else{
				data.setGoodsname2(null);
			}
			if(jsonObject.get("piece")!=null){
				data.setPiece(new Long(String.valueOf(jsonObject.get("piece"))));
			}else{
				data.setPiece(null);
			}
			if(jsonObject.get("packagee")!=null){
				data.setPackagee(String.valueOf(jsonObject.get("packagee")));
			}else{
				data.setPackagee(null);
			}
			if(jsonObject.get("piece2")!=null&&!jsonObject.get("piece2").toString().equals("")){
				data.setPiece2(new Long(String.valueOf(jsonObject.get("piece2"))));
			}else{
				data.setPiece2(null);
			}
			if(jsonObject.get("packagee2")!=null&&!jsonObject.get("packagee2").toString().equals("")){
				data.setPackagee2(String.valueOf(jsonObject.get("packagee2")));
			}else{
				data.setPackagee2(null);
			}
			if(jsonObject.get("price")!=null&&!jsonObject.get("price").toString().equals("")){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(null);
			}
			if(jsonObject.get("grswgt")!=null&&!jsonObject.get("grswgt").toString().equals("")){
				data.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt"))));
			}else{
				data.setGrswgt(null);
			}
			if(jsonObject.get("cbm")!=null&&!jsonObject.get("cbm").toString().equals("")){
				data.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm"))));
			}else{
				data.setCbm(null);
			}
			if(jsonObject.get("cbm2")!=null&&!jsonObject.get("cbm2").toString().equals("")){
				data.setCbm2(new BigDecimal(String.valueOf(jsonObject.get("cbm2"))));
			}else{
				data.setCbm2(null);
			}
			if(jsonObject.get("hscode")!=null&&!jsonObject.get("hscode").toString().equals("")){
				data.setHscode(String.valueOf(jsonObject.get("hscode")));
			}else{
				data.setHscode(null);
			}
			if(jsonObject.get("markno")!=null&&!jsonObject.get("markno").toString().equals("")){
				data.setMarkno(String.valueOf(jsonObject.get("markno")));
			}else{
				data.setMarkno(null);
			}
			if(jsonObject.get("netwgt")!=null&&!jsonObject.get("netwgt").toString().equals("")){
				data.setNetwgt(new BigDecimal(String.valueOf(jsonObject.get("netwgt"))));
			}else{
				data.setNetwgt(null);
			}
			if(jsonObject.get("amt")!=null&&!jsonObject.get("amt").toString().equals("")){
				data.setAmt((new BigDecimal(String.valueOf(jsonObject.get("piece2"))).multiply(new BigDecimal(String.valueOf(jsonObject.get("price"))))));
			}else{
				data.setAmt(null);
			}
			if(jsonObject.get("currency")!=null&&!jsonObject.get("currency").toString().equals("")){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			saveData(data);
		}
	}

	public void addBatchEditGrid(Object addData, long cid) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			BusPacklist data = new BusPacklist();
			data.setLinkid(cid);
			
			if(jsonObject.get("factorynamee")!=null){
				data.setFactorynamee(String.valueOf(jsonObject.get("factorynamee")));
			}else{
				data.setFactorynamee(null);
			}
			if(jsonObject.get("factoryname")!=null){
				data.setFactoryname(String.valueOf(jsonObject.get("factoryname")));
			}else{
				data.setFactoryname(null);
			}
			if(jsonObject.get("piece")!=null&&!jsonObject.get("piece").toString().equals("")){
				data.setPiece(new Long(String.valueOf(jsonObject.get("piece"))));
			}else{
				data.setPiece(null);
			}
			if(jsonObject.get("price")!=null&&!jsonObject.get("price").toString().equals("")){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(null);
			}
			if(jsonObject.get("currency")!=null&&!jsonObject.get("currency").toString().equals("")){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("packagee")!=null){
				data.setPackagee(String.valueOf(jsonObject.get("packagee")));
			}else{
				data.setPackagee(null);
			}
			if(jsonObject.get("piece2")!=null&&!jsonObject.get("piece2").toString().equals("")){
				data.setPiece2(new Long(String.valueOf(jsonObject.get("piece2"))));
			}else{
				data.setPiece2(null);
			}
			if(jsonObject.get("packagee2")!=null){
				data.setPackagee2(String.valueOf(jsonObject.get("packagee2")));
			}else{
				data.setPackagee2(null);
			}
			if(jsonObject.get("grswgt")!=null&&!jsonObject.get("grswgt").toString().equals("")){
				data.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt"))));
			}else{
				data.setGrswgt(null);
			}
			if(jsonObject.get("netwgt")!=null&&!jsonObject.get("netwgt").toString().equals("")){
				data.setNetwgt(new BigDecimal(String.valueOf(jsonObject.get("netwgt"))));
			}else{
				data.setNetwgt(null);
			}
			if(jsonObject.get("cbm")!=null&&!jsonObject.get("cbm").toString().equals("")){
				data.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm"))));
			}else{
				data.setCbm(null);
			}
			if(jsonObject.get("cbm2")!=null&&!jsonObject.get("cbm2").toString().equals("")){
				data.setCbm2(new BigDecimal(String.valueOf(jsonObject.get("cbm2"))));
			}else{
				data.setCbm2(null);
			}
			if(jsonObject.get("hscode")!=null&&!jsonObject.get("hscode").toString().equals("")){
				data.setHscode(String.valueOf(jsonObject.get("hscode")));
			}else{
				data.setHscode(null);
			}
			if(jsonObject.get("goodsname2")!=null&&!jsonObject.get("goodsname2").toString().equals("")){
				data.setGoodsname2(String.valueOf(jsonObject.get("goodsname2")));
			}else{
				data.setGoodsname2(null);
			}
			
			if(jsonObject.get("amt")!=null&&!jsonObject.get("amt").toString().equals("")){
				data.setAmt((new BigDecimal(String.valueOf(jsonObject.get("piece2"))).multiply(new BigDecimal(String.valueOf(jsonObject.get("price"))))));
			}else{
				data.setAmt(null);
			}
			saveData(data);
		}
	}
	
	public void updateDangerEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		BusPacklist data = new BusPacklist();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = busPacklistDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("emergencycontact")!=null){
				data.setEmergencycontact(String.valueOf(jsonObject.get("emergencycontact")));
			}else{
				data.setEmergencycontact(null);
			}
			if(jsonObject.get("emergencytel")!=null){
				data.setEmergencytel(String.valueOf(jsonObject.get("emergencytel")));
			}else{
				data.setEmergencytel(null);
			}
			if(jsonObject.get("emergencyemail")!=null){
				data.setEmergencyemail(String.valueOf(jsonObject.get("emergencyemail")));
			}else{
				data.setEmergencyemail(null);
			}
			if(jsonObject.get("emergencyreference")!=null){
				data.setEmergencyreference(String.valueOf(jsonObject.get("emergencyreference")));
			}else{
				data.setEmergencyreference(null);
			}
			if(jsonObject.get("unnumber")!=null){
				data.setUnnumber(String.valueOf(jsonObject.get("unnumber")));
			}else{
				data.setUnnumber(null);
			}
			if(jsonObject.get("imoclass")!=null){
				data.setImoclass(String.valueOf(jsonObject.get("imoclass")));
			}else{
				data.setImoclass(null);
			}
			if(jsonObject.get("uom")!=null){
				data.setUom(String.valueOf(jsonObject.get("uom")));
			}else{
				data.setUom(null);
			}
			if(jsonObject.get("flashpoint")!=null){
				data.setFlashpoint(String.valueOf(jsonObject.get("flashpoint")));
			}else{
				data.setFlashpoint(null);
			}
			if(jsonObject.get("outerpackagecode")!=null){
				data.setOuterpackagecode(String.valueOf(jsonObject.get("outerpackagecode")));
			}else{
				data.setOuterpackagecode(null);
			}
			if(jsonObject.get("outerquantity")!=null){
				data.setOuterquantity(String.valueOf(jsonObject.get("outerquantity")));
			}else{
				data.setOuterquantity(null);
			}
			if(jsonObject.get("tnnerpackagecode")!=null){
				data.setTnnerpackagecode(String.valueOf(jsonObject.get("tnnerpackagecode")));
			}else{
				data.setTnnerpackagecode(null);
			}
			if(jsonObject.get("innerquantity")!=null){
				data.setInnerquantity(String.valueOf(jsonObject.get("innerquantity")));
			}else{
				data.setInnerquantity(null);
			}
			if(jsonObject.get("chemicalname")!=null){
				data.setChemicalname(String.valueOf(jsonObject.get("chemicalname")));
			}else{
				data.setChemicalname(null);
			}
			if(jsonObject.get("marinepollutant")!=null){
				data.setMarinepollutant(Boolean.parseBoolean(String.valueOf(jsonObject.get("marinepollutant"))));
			}else{
				data.setMarinepollutant(false);
			}
			
			if(jsonObject.get("price")!=null && !String.valueOf(jsonObject.get("price")).isEmpty()){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(new BigDecimal(0));
			}
			
			if(jsonObject.get("grossweight")!=null && !String.valueOf(jsonObject.get("grossweight")).isEmpty()){
				data.setGrossweight(new BigDecimal(String.valueOf(jsonObject.get("grossweight"))));
			}else{
				data.setGrossweight(new BigDecimal(0));
			}
			
			if(jsonObject.get("netweight")!=null && !String.valueOf(jsonObject.get("netweight")).isEmpty()){
				data.setNetweight(new BigDecimal(String.valueOf(jsonObject.get("netweight"))));
			}else{
				data.setNetweight(new BigDecimal(0));
			}
			if(jsonObject.get("goodsname2")!=null&&!jsonObject.get("goodsname2").toString().equals("")){
				data.setGoodsname2(String.valueOf(jsonObject.get("goodsname2")));
			}else{
				data.setGoodsname2(null);
			}
			saveData(data);
		}
	}

	public void addDangerEditGrid(Object addData, long cid) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			BusPacklist data = new BusPacklist();
			data.setLinkid(cid);
			
			if(jsonObject.get("emergencycontact")!=null){
				data.setEmergencycontact(String.valueOf(jsonObject.get("emergencycontact")));
			}else{
				data.setEmergencycontact(null);
			}
			if(jsonObject.get("emergencytel")!=null){
				data.setEmergencytel(String.valueOf(jsonObject.get("emergencytel")));
			}else{
				data.setEmergencytel(null);
			}
			if(jsonObject.get("emergencyemail")!=null){
				data.setEmergencyemail(String.valueOf(jsonObject.get("emergencyemail")));
			}else{
				data.setEmergencyemail(null);
			}
			if(jsonObject.get("emergencyreference")!=null){
				data.setEmergencyreference(String.valueOf(jsonObject.get("emergencyreference")));
			}else{
				data.setEmergencyreference(null);
			}
			if(jsonObject.get("unnumber")!=null){
				data.setUnnumber(String.valueOf(jsonObject.get("unnumber")));
			}else{
				data.setUnnumber(null);
			}
			if(jsonObject.get("imoclass")!=null){
				data.setImoclass(String.valueOf(jsonObject.get("imoclass")));
			}else{
				data.setImoclass(null);
			}
			if(jsonObject.get("uom")!=null){
				data.setUom(String.valueOf(jsonObject.get("uom")));
			}else{
				data.setUom(null);
			}
			if(jsonObject.get("flashpoint")!=null){
				data.setFlashpoint(String.valueOf(jsonObject.get("flashpoint")));
			}else{
				data.setFlashpoint(null);
			}
			if(jsonObject.get("outerpackagecode")!=null){
				data.setOuterpackagecode(String.valueOf(jsonObject.get("outerpackagecode")));
			}else{
				data.setOuterpackagecode(null);
			}
			if(jsonObject.get("outerquantity")!=null){
				data.setOuterquantity(String.valueOf(jsonObject.get("outerquantity")));
			}else{
				data.setOuterquantity(null);
			}
			if(jsonObject.get("tnnerpackagecode")!=null){
				data.setTnnerpackagecode(String.valueOf(jsonObject.get("tnnerpackagecode")));
			}else{
				data.setTnnerpackagecode(null);
			}
			if(jsonObject.get("innerquantity")!=null){
				data.setInnerquantity(String.valueOf(jsonObject.get("innerquantity")));
			}else{
				data.setInnerquantity(null);
			}
			if(jsonObject.get("chemicalname")!=null){
				data.setChemicalname(String.valueOf(jsonObject.get("chemicalname")));
			}else{
				data.setChemicalname(null);
			}
			
			saveData(data);
		}
	}
	
	public void removedBatchEditGrid(Object removedData) {
		JSONArray jsonArray = (JSONArray) removedData;
		BusPacklist data = new BusPacklist();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("id")!=null&&!String.valueOf(jsonObject.get("id")).isEmpty()&&Pattern.matches("^\\d+$", String.valueOf(jsonObject.get("id")))){
				ids += String.valueOf(jsonObject.get("id"))+",";
			}
		}
		if(!ids.isEmpty()){
			ids = ids.substring(0, ids.length()-1);
			String sql = "DELETE FROM bus_packlist WHERE id IN ("+ids+");";
			busPacklistDao.executeSQL(sql);
		}
	}
}