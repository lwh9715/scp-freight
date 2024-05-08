package com.scp.service.bus;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusGoodsDao;
import com.scp.model.bus.BusGoods;
import com.scp.util.StrUtils;

@Component
public class BusGoodsMgrService {

	
	@Resource
	public BusGoodsDao busGoodsDao;

	public void saveData(BusGoods data) {
		if(0 == data.getId()){
			busGoodsDao.create(data);
		}else{
			busGoodsDao.modify(data);
		}
	}

	
	public void removeDate(Long id,String usercode) {
		String sql = "UPDATE bus_goods  SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id = "+id+";";
		busGoodsDao.executeSQL(sql);
	}

	public void delBatch(String[] ids, String usercode) {
		String idlist = StrUtils.array2List(ids);
		String sql = "UPDATE bus_goods  SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id in ( "+idlist+");";
		busGoodsDao.executeSQL(sql);
		
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		BusGoods data = new BusGoods();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = busGoodsDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("goodsname")!=null){
				if(!String.valueOf(jsonObject.get("goodsname")).isEmpty()){
					data.setGoodsname(String.valueOf(jsonObject.get("goodsname")));
				}else{
					data.setGoodsname(null);
				}
			}else{
				data.setGoodsname(null);
			}
			if(jsonObject.get("markno")!=null){
				data.setMarkno(String.valueOf(jsonObject.get("markno")));
			}else{
				data.setMarkno(null);
			}
			if(jsonObject.get("piece3")!=null && !String.valueOf(jsonObject.get("piece3")).isEmpty()){
				data.setPiece(new Integer(String.valueOf(jsonObject.get("piece3"))));
			}else{
				data.setPiece(new Integer(0));
			}
			if(jsonObject.get("packagee")!=null && !String.valueOf(jsonObject.get("packagee")).isEmpty()){
				data.setPackagee(String.valueOf(jsonObject.get("packagee")));
			}else{
				data.setPackagee(null);
			}
			if(jsonObject.get("l")!=null && !String.valueOf(jsonObject.get("l")).isEmpty()){
				data.setL(new BigDecimal(String.valueOf(jsonObject.get("l"))));
			}else{
				data.setL(new BigDecimal(0));
			}
			if(jsonObject.get("w")!=null && !String.valueOf(jsonObject.get("w")).isEmpty()){
				data.setW(new BigDecimal(String.valueOf(jsonObject.get("w"))));
			}else{
				data.setW(new BigDecimal(0));
			}
			if(jsonObject.get("h")!=null && !String.valueOf(jsonObject.get("h")).isEmpty()){
				data.setH(new BigDecimal(String.valueOf(jsonObject.get("h"))));
			}else{
				data.setH(new BigDecimal(0));
			}
			if(jsonObject.get("cbm2")!=null && !String.valueOf(jsonObject.get("cbm2")).isEmpty()){
				data.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm2"))));
			}else{
				data.setCbm(new BigDecimal(0));
			}
			if(jsonObject.get("volumeweight2")!=null && !String.valueOf(jsonObject.get("volumeweight2")).isEmpty()){
				data.setVolumeweight(new BigDecimal(String.valueOf(jsonObject.get("volumeweight2"))));
			}else{
				data.setVolumeweight(new BigDecimal(0));
			}
			if(jsonObject.get("grswgt2")!=null && !String.valueOf(jsonObject.get("grswgt2")).isEmpty()){
				data.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt2"))));
			}else{
				data.setGrswgt(new BigDecimal(0));
			}
			if(jsonObject.get("chargeweight2")!=null && !String.valueOf(jsonObject.get("chargeweight2")).isEmpty()){
				data.setChargeweight(new BigDecimal(String.valueOf(jsonObject.get("chargeweight2"))));
			}else{
				data.setGdsprice(new BigDecimal(0));
			}
			if(jsonObject.get("currency")!=null && !String.valueOf(jsonObject.get("currency")).isEmpty()){
				data.setCurrency((String.valueOf(jsonObject.get("currency"))));
			}else{
				data.setCurrency("");
			}
			if(jsonObject.get("gdsvalue")!=null && !String.valueOf(jsonObject.get("gdsvalue")).isEmpty()){
				data.setGdsvalue(new BigDecimal(String.valueOf(jsonObject.get("gdsvalue"))));
			}else{
				data.setGdsvalue(new BigDecimal(0));
			}
			saveData(data);
		}		
	}
	
	public void addBatchEditGrid(Object addData,Long linkid) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			BusGoods data = new BusGoods();
			data.setLinkid(linkid);
			if(jsonObject.get("goodsname")!=null){
				if(!String.valueOf(jsonObject.get("goodsname")).isEmpty()){
					data.setGoodsname(String.valueOf(jsonObject.get("goodsname")));
				}else{
					data.setGoodsname(null);
				}
			}else{
				data.setGoodsname(null);
			}
			if(jsonObject.get("markno")!=null){
				data.setMarkno(String.valueOf(jsonObject.get("markno")));
			}else{
				data.setMarkno(null);
			}
			if(jsonObject.get("piece3")!=null && !String.valueOf(jsonObject.get("piece3")).isEmpty()){
				data.setPiece(new Integer(String.valueOf(jsonObject.get("piece3"))));
			}else{
				data.setPiece(new Integer(0));
			}
			if(jsonObject.get("packagee")!=null && !String.valueOf(jsonObject.get("packagee")).isEmpty()){
				data.setPackagee(String.valueOf(jsonObject.get("packagee")));
			}else{
				data.setPackagee(null);
			}
			if(jsonObject.get("l")!=null && !String.valueOf(jsonObject.get("l")).isEmpty()){
				data.setL(new BigDecimal(String.valueOf(jsonObject.get("l"))));
			}else{
				data.setL(new BigDecimal(0));
			}
			if(jsonObject.get("w")!=null && !String.valueOf(jsonObject.get("w")).isEmpty()){
				data.setW(new BigDecimal(String.valueOf(jsonObject.get("w"))));
			}else{
				data.setW(new BigDecimal(0));
			}
			if(jsonObject.get("h")!=null && !String.valueOf(jsonObject.get("h")).isEmpty()){
				data.setH(new BigDecimal(String.valueOf(jsonObject.get("h"))));
			}else{
				data.setH(new BigDecimal(0));
			}
			if(jsonObject.get("cbm2")!=null && !String.valueOf(jsonObject.get("cbm2")).isEmpty()){
				data.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm2"))));
			}else{
				data.setCbm(new BigDecimal(0));
			}
			if(jsonObject.get("volumeweight2")!=null && !String.valueOf(jsonObject.get("volumeweight2")).isEmpty()){
				data.setVolumeweight(new BigDecimal(String.valueOf(jsonObject.get("volumeweight2"))));
			}else{
				data.setVolumeweight(new BigDecimal(0));
			}
			if(jsonObject.get("grswgt2")!=null && !String.valueOf(jsonObject.get("grswgt2")).isEmpty()){
				data.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt2"))));
			}else{
				data.setGrswgt(new BigDecimal(0));
			}
			if(jsonObject.get("chargeweight2")!=null && !String.valueOf(jsonObject.get("chargeweight2")).isEmpty()){
				data.setChargeweight(new BigDecimal(String.valueOf(jsonObject.get("chargeweight2"))));
			}else{
				data.setGdsprice(new BigDecimal(0));
			}
			if(jsonObject.get("currency")!=null && !String.valueOf(jsonObject.get("currency")).isEmpty()){
				data.setCurrency((String.valueOf(jsonObject.get("currency"))));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("gdsvalue")!=null && !String.valueOf(jsonObject.get("gdsvalue")).isEmpty()){
				data.setGdsvalue(new BigDecimal(String.valueOf(jsonObject.get("gdsvalue"))));
			}else{
				data.setGdsvalue(new BigDecimal(0));
			}
			saveData(data);
		}		
	}

//	public void saveOrModify(List<BusGoods> goods) {
//		for (BusGoods busGoods : goods) {
//			saveData(busGoods);
//		}
//	}
	
}