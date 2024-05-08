package com.scp.service.ship;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.data.DatCntypeDao;
import com.scp.dao.ship.BusShipContainerDao;
import com.scp.model.ship.BusShipContainer;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class BusShipContainerMgrService{
	
	@Resource
	public DatCntypeDao datCntypeDao;
	
	@Resource
	public BusShipContainerDao busShipContainerDao; 
	

	public void saveData(BusShipContainer data) {
		if(0 == data.getId()){
			busShipContainerDao.create(data);
		}else{
			busShipContainerDao.modify(data);
		}
	}

	public void removeDate(Long id) {
//		BusShipContainer data = busShipContainerDao.findById(id);
//		busShipContainerDao.remove(data);
		String sql = "UPDATE bus_ship_container SET isdelete = TRUE ,updater='"+AppUtils.getUserSession().getUsercode()+"' WHERE id="+id+";";
		busShipContainerDao.executeSQL(sql);
	} 
	
	public void removeDate(String[] ids) {
//		BusShipContainer data = busShipContainerDao.findById(id);
//		busShipContainerDao.remove(data);
		String sql = "UPDATE bus_ship_container SET isdelete = TRUE,updater='"+AppUtils.getUserSession().getUsercode()
					+"' WHERE id = ANY(ARRAY["+StrUtils.array2List(ids)+"]);";
		busShipContainerDao.executeSQL(sql);
	} 
	
	public void updateContainerSelects(String[] ids,Long shipid){
		if(shipid == null){
			return;
		}
		String sql = "UPDATE bus_ship_container SET isselect = FALSE WHERE isdelete = FALSE AND shipid = "+shipid;
		busShipContainerDao.executeSQL(sql);
		if(ids!=null&&ids.length>0){
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("\nUPDATE bus_ship_container SET isselect = TRUE WHERE isdelete = FALSE ");
			sbsql.append(" AND id = ANY(array["+StrUtils.array2List(ids)+"])");
			busShipContainerDao.executeSQL(sbsql.toString());
		}
	}

	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		BusShipContainer data = new BusShipContainer();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = busShipContainerDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("cntypeid")!=null && !String.valueOf(jsonObject.get("cntypeid")).isEmpty()){
				//List<DatCntype> list = datCntypeDao.findAllByClauseWhere("isdelete = FALSE AND code='"+String.valueOf(jsonObject.get("cntypedesc")).replaceAll("'", "''")+"'");
				//if(list!=null&&list.size()>0){
					//data.setCntypeid(list.get(0).getId());
					data.setCntypeid(Long.parseLong(String.valueOf(jsonObject.get("cntypeid"))));
				//}
			}else{
				data.setCntypeid(null);
			}
			if(jsonObject.get("ldtype")!=null){
				if(!String.valueOf(jsonObject.get("ldtype")).isEmpty()){
					data.setLdtype(String.valueOf(jsonObject.get("ldtype")));
				}else{
					data.setLdtype(null);
				}
			}else{
				data.setLdtype(null);
			}
			if(jsonObject.get("goodsvalue")!=null && !String.valueOf(jsonObject.get("goodsvalue")).isEmpty()){
				data.setGoodsvalue(new BigDecimal(String.valueOf(jsonObject.get("goodsvalue"))));
			}else{
				data.setGoodsvalue(null);
			}
			if(jsonObject.get("grswgt2")!=null && !String.valueOf(jsonObject.get("grswgt2")).isEmpty()){
				data.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt2"))));
			}else if(jsonObject.get("grswgt")!=null && !String.valueOf(jsonObject.get("grswgt")).isEmpty()){
				data.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt"))));
			}else{
				data.setGrswgt(new BigDecimal(0));
			}
			if(jsonObject.get("price")!=null && !String.valueOf(jsonObject.get("price")).isEmpty()){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(new BigDecimal(0));
			}
			if(jsonObject.get("orderno")!=null && !String.valueOf(jsonObject.get("orderno")).isEmpty()){
				data.setOrderno(new Integer(String.valueOf(jsonObject.get("orderno"))));
			}else{
				data.setOrderno(0);
			}
			if(jsonObject.get("sono")!=null){
				data.setSono(String.valueOf(jsonObject.get("sono")));
			}else{
				data.setSono(null);
			}
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			
			if(jsonObject.get("sealno2")!=null){
				data.setSealno2(String.valueOf(jsonObject.get("sealno2")));
			}else{
				data.setSealno2(null);
			}
			if(jsonObject.get("packagee")!=null){
				data.setPackagee(String.valueOf(jsonObject.get("packagee")));
			}else{
				data.setPackagee(null);
			}
			if(jsonObject.get("markno")!=null){
				data.setMarkno(AppUtils.replaceStringByRegEx(String.valueOf(jsonObject.get("markno"))));
			}else{
				data.setMarkno(null);
			}
			if(jsonObject.get("cbm2")!=null && !String.valueOf(jsonObject.get("cbm2")).isEmpty()){
				data.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm2"))));
			}else if(jsonObject.get("cbm")!=null && !String.valueOf(jsonObject.get("cbm")).isEmpty()){
				data.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm"))));
			}else{
				data.setCbm(new BigDecimal(0));
			}
			if(jsonObject.get("declareno")!=null){
				data.setDeclareno(String.valueOf(jsonObject.get("declareno")));
			}else{
				data.setDeclareno(null);
			}
			if(jsonObject.get("netwgt")!=null && !String.valueOf(jsonObject.get("netwgt")).isEmpty()){
				data.setNetwgt(new BigDecimal(String.valueOf(jsonObject.get("netwgt"))));
			}else{
				data.setNetwgt(new BigDecimal(0));
			}
			if(jsonObject.get("vgm")!=null && !String.valueOf(jsonObject.get("vgm")).isEmpty()){
				data.setVgm(new BigDecimal(String.valueOf(jsonObject.get("vgm"))));
			}else{
				data.setVgm(new BigDecimal(0));
			}
			if(jsonObject.get("grswgtempty")!=null && !String.valueOf(jsonObject.get("grswgtempty")).isEmpty()){
				data.setGrswgtempty(new BigDecimal(String.valueOf(jsonObject.get("grswgtempty"))));
			}else{
				data.setGrswgtempty(new BigDecimal(0));
			}
			if(jsonObject.get("spacecbm")!=null && !String.valueOf(jsonObject.get("spacecbm")).isEmpty()){
				data.setSpacecbm(new BigDecimal(String.valueOf(jsonObject.get("spacecbm"))));
			}else{
				data.setSpacecbm(new BigDecimal(0));
			}
			if(jsonObject.get("ispace")!=null&&jsonObject.get("ispace").equals(true)){
				data.setIspace(true);
			}else{
				data.setIspace(false);
			}
			if(jsonObject.get("goodsnamedesc")!=null){
				data.setGoodsnamee(AppUtils.replaceStringByRegEx(String.valueOf(jsonObject.get("goodsnamedesc"))));
			}else{
				data.setGoodsnamee(null);
			}
			if(jsonObject.get("goodsname")!=null){
				data.setGoodsname(AppUtils.replaceStringByRegEx(String.valueOf(jsonObject.get("goodsname"))));
			}else{
				data.setGoodsname(null);
			}
			if(jsonObject.get("piece2")!=null && !String.valueOf(jsonObject.get("piece2")).isEmpty()){
				data.setPiece(new Integer(String.valueOf(jsonObject.get("piece2"))));
			}else if(jsonObject.get("piece")!=null && !String.valueOf(jsonObject.get("piece")).isEmpty()){
				data.setPiece(new Integer(String.valueOf(jsonObject.get("piece"))));
				
			}else{
				data.setPiece(new Integer(0));
			}
			if(jsonObject.get("isselect")!=null){
				data.setIsselect(new Boolean(String.valueOf(jsonObject.get("isselect"))));
			}else{
				data.setIsselect(false);
			}
			if(jsonObject.get("cntno")!=null){
				data.setCntno(String.valueOf(jsonObject.get("cntno")));
			}else{
				data.setCntno(null);
			}
			if(jsonObject.get("sealno")!=null){
				data.setSealno(String.valueOf(jsonObject.get("sealno")));
			}else{
				data.setSealno(null);
			}
			if(jsonObject.get("netcbm")!=null && !String.valueOf(jsonObject.get("netcbm")).isEmpty()){
				data.setNetcbm(new BigDecimal(String.valueOf(jsonObject.get("netcbm"))));
			}else{
				data.setNetcbm(new BigDecimal(0));
			}
			if(jsonObject.get("hscode")!=null){
				data.setHscode(String.valueOf(jsonObject.get("hscode")));
			}else{
				data.setHscode(null);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(jsonObject.get("date_gatein")!=null){
				try {
					data.setDate_gatein(sdf.parse((jsonObject.get("date_gatein")).toString()));
				} catch (ParseException e) {
					data.setSend_driver(null);
					e.printStackTrace();
				}
			}else{
				data.setSend_driver(null);
			}
			saveData(data);
		}		
	}
	
	public void updateBatchEditGridForeegnde(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		BusShipContainer data = new BusShipContainer();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = busShipContainerDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("send_sealno")!=null){
				data.setSend_sealno(String.valueOf(jsonObject.get("send_sealno")));
			}else{
				data.setSend_sealno(null);
			}
			if(jsonObject.get("send_caiid")!=null){
				data.setSend_caiid(String.valueOf(jsonObject.get("send_caiid")));
			}else{
				data.setSend_caiid(null);
			}
			if(jsonObject.get("send_pws")!=null){
				data.setSend_pws(String.valueOf(jsonObject.get("send_pws")));
			}else{
				data.setSend_pws(null);
			}
			if(jsonObject.get("send_driver")!=null){
				data.setSend_driver(String.valueOf(jsonObject.get("send_driver")));
			}else{
				data.setSend_driver(null);
			}
			if(jsonObject.get("send_drivertel")!=null){
				data.setSend_drivertel(String.valueOf(jsonObject.get("send_drivertel")));
			}else{
				data.setSend_drivertel(null);
			}
			saveData(data);
		}		
	}

	public void removedBatchEditGrid(Object removedData) {
		JSONArray jsonArray = (JSONArray) removedData;
		BusShipContainer data = new BusShipContainer();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("id")!=null&&!String.valueOf(jsonObject.get("id")).isEmpty()&&Pattern.matches("^\\d+$", String.valueOf(jsonObject.get("id")))){
				ids += String.valueOf(jsonObject.get("id"))+",";
			}
		}
		if(!ids.isEmpty()){
			ids = ids.substring(0, ids.length()-1);
			String sql = "UPDATE bus_ship_container SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = ANY(ARRAY["+ids+"])";
			busShipContainerDao.executeSQL(sql);
		}
	}

	public void addBatchEditGrid(Object addData, long jobid, long shipid) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			BusShipContainer data = new BusShipContainer();
			data.setJobid(jobid);
			data.setShipid(shipid);
			if(jsonObject.get("cntypeid")!=null && !String.valueOf(jsonObject.get("cntypeid")).isEmpty()){
				data.setCntypeid(Long.parseLong(String.valueOf(jsonObject.get("cntypeid"))));
			}else{
				data.setCntypeid(null);
			}
			if(jsonObject.get("ldtype")!=null){
				if(!String.valueOf(jsonObject.get("ldtype")).isEmpty()){
					data.setLdtype(String.valueOf(jsonObject.get("ldtype")));
				}else{
					data.setLdtype(null);
				}
			}else{
				data.setLdtype(null);
			}
			if(jsonObject.get("goodsvalue")!=null && !String.valueOf(jsonObject.get("goodsvalue")).isEmpty()){
				data.setGoodsvalue(new BigDecimal(String.valueOf(jsonObject.get("goodsvalue"))));
			}else{
				data.setGoodsvalue(null);
			}
			if(jsonObject.get("piece2")!=null && !String.valueOf(jsonObject.get("piece2")).isEmpty()){
				data.setPiece(new Integer(String.valueOf(jsonObject.get("piece2"))));
			}else if(jsonObject.get("piece")!=null && !String.valueOf(jsonObject.get("piece")).isEmpty()){
				data.setPiece(new Integer(String.valueOf(jsonObject.get("piece"))));
			}else{
				data.setPiece(new Integer(0));
			}
			if(jsonObject.get("orderno")!=null && !String.valueOf(jsonObject.get("orderno")).isEmpty()){
				data.setOrderno(new Integer(String.valueOf(jsonObject.get("orderno"))));
			}else{
				data.setOrderno(0);
			}
			if(jsonObject.get("sono")!=null){
				data.setSono(String.valueOf(jsonObject.get("sono")));
			}else{
				data.setSono(null);
			}
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("sealno2")!=null){
				data.setSealno2(String.valueOf(jsonObject.get("sealno2")));
			}else{
				data.setSealno2(null);
			}
			if(jsonObject.get("packagee")!=null){
				data.setPackagee(String.valueOf(jsonObject.get("packagee")));
			}else{
				data.setPackagee(null);
			}
			if(jsonObject.get("markno")!=null){
				data.setMarkno(AppUtils.replaceStringByRegEx(String.valueOf(jsonObject.get("markno"))));
			}else{
				data.setMarkno(null);
			}
			if(jsonObject.get("cbm2")!=null && !String.valueOf(jsonObject.get("cbm2")).isEmpty()){
				data.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm2"))));
			}else if(jsonObject.get("cbm")!=null && !String.valueOf(jsonObject.get("cbm")).isEmpty()){
				data.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm"))));
			}else{
				data.setCbm(new BigDecimal(0));
			}
			if(jsonObject.get("declareno")!=null){
				data.setDeclareno(String.valueOf(jsonObject.get("declareno")));
			}else{
				data.setDeclareno(null);
			}
			if(jsonObject.get("netwgt")!=null && !String.valueOf(jsonObject.get("netwgt")).isEmpty()){
				data.setNetwgt(new BigDecimal(String.valueOf(jsonObject.get("netwgt"))));
			}else{
				data.setNetwgt(new BigDecimal(0));
			}
			if(jsonObject.get("vgm")!=null && !String.valueOf(jsonObject.get("vgm")).isEmpty()){
				data.setVgm(new BigDecimal(String.valueOf(jsonObject.get("vgm"))));
			}else{
				data.setVgm(new BigDecimal(0));
			}
			if(jsonObject.get("grswgtempty")!=null && !String.valueOf(jsonObject.get("grswgtempty")).isEmpty()){
				data.setGrswgtempty(new BigDecimal(String.valueOf(jsonObject.get("grswgtempty"))));
			}else{
				data.setGrswgtempty(new BigDecimal(0));
			}
			if(jsonObject.get("spacecbm")!=null && !String.valueOf(jsonObject.get("spacecbm")).isEmpty()){
				data.setSpacecbm(new BigDecimal(String.valueOf(jsonObject.get("spacecbm"))));
			}else{
				data.setSpacecbm(new BigDecimal(0));
			}
			if(jsonObject.get("ispace")!=null){
				data.setIspace(true);
			}else{
				data.setIspace(false);
			}
			if(jsonObject.get("goodsnamedesc")!=null){
				data.setGoodsnamee(AppUtils.replaceStringByRegEx((String.valueOf(jsonObject.get("goodsnamedesc")))));
			}else{
				data.setGoodsnamee(null);
			}
			if(jsonObject.get("goodsname")!=null){
				data.setGoodsname(AppUtils.replaceStringByRegEx(String.valueOf(jsonObject.get("goodsname"))));
			}else{
				data.setGoodsname(null);
			}
			if(jsonObject.get("isselect")!=null){
				data.setIsselect(new Boolean(String.valueOf(jsonObject.get("isselect"))));
			}else{
				data.setIsselect(false);
			}
			if(jsonObject.get("cntno")!=null){
				data.setCntno(String.valueOf(jsonObject.get("cntno")));
			}else{
				data.setCntno(null);
			}
			if(jsonObject.get("sealno")!=null){
				data.setSealno(String.valueOf(jsonObject.get("sealno")));
			}else{
				data.setSealno(null);
			}
			if(jsonObject.get("netcbm")!=null && !String.valueOf(jsonObject.get("netcbm")).isEmpty()){
				data.setNetcbm(new BigDecimal(String.valueOf(jsonObject.get("netcbm"))));
			}else{
				data.setNetcbm(new BigDecimal(0));
			}
			if(jsonObject.get("hscode")!=null){
				data.setHscode(String.valueOf(jsonObject.get("hscode")));
			}else{
				data.setHscode(null);
			}
			if(jsonObject.get("price")!=null && !String.valueOf(jsonObject.get("price")).isEmpty()){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(new BigDecimal(0));
			}
			if(jsonObject.get("grswgt2")!=null && !String.valueOf(jsonObject.get("grswgt2")).isEmpty()){
				data.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt2"))));
			}else if(jsonObject.get("grswgt")!=null && !String.valueOf(jsonObject.get("grswgt")).isEmpty()){
				data.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt"))));
			}else{
				data.setGrswgt(new BigDecimal(0));
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(jsonObject.get("date_gatein")!=null){
				try {
					data.setDate_gatein(sdf.parse((jsonObject.get("date_gatein")).toString()));
				} catch (ParseException e) {
					data.setSend_driver(null);
					e.printStackTrace();
				}
			}else{
				data.setSend_driver(null);
			}
			saveData(data);
		}
	}
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	/**
	 * 传入id栓粗字符串，把对应柜子的排序按顺序设置
	 * @param idsnums id的顺序字符串
	 * @param jobid
	 */
	public void setOrdernoOrder(String idsnums,Long jobid){
		List<BusShipContainer> containers = 
			busShipContainerDao.findAllByClauseWhere(" isdelete = FALSE AND jobid = "+jobid);
		if(!StrUtils.isNull(idsnums)){
			String[] splitids = idsnums.split(",");
			for(BusShipContainer bus:containers){
				bus.setOrderno(null);
				for(int i=0;i<splitids.length;i++){
					if(bus.getId()==Long.parseLong(splitids[i])){
						bus.setOrderno(i+1);
					}
					busShipContainerDao.modify(bus);
				}
			}
			String sql = "SELECT MAX(orderno) orderno FROM bus_ship_container WHERE isdelete = FALSE AND jobid = "+jobid;
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			int number ;
			if(m!=null&&m.get("orderno")!=null&&(Integer.parseInt(m.get("orderno").toString()))>0){
				number = Integer.parseInt(m.get("orderno").toString());
			}else{
				number = 1;
			}
			for(BusShipContainer bus:containers){//循环设置没有传入的柜子
				if(bus.getOrderno()==null){
					number+=1;
					bus.setOrderno(number);
					busShipContainerDao.modify(bus);
				}
			}
		}
	}
}