package com.scp.service.wms;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.wms.WmsInDao;
import com.scp.dao.wms.WmsIndtlDao;
import com.scp.model.wms.WmsIn;
import com.scp.model.wms.WmsIndtl;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class WmsIndtlMgrService {

	@Resource
	public WmsIndtlDao wmsIndtlDao;

	@Resource
	public WmsInDao wmsInDao;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveMasterData(WmsIn data) {
		if (0 == data.getId()) {
			wmsInDao.create(data);
		} else {
			wmsInDao.modify(data);
		}
	}

	public void saveDtlData(WmsIndtl data) {

		if (0 == data.getId()) {
			wmsIndtlDao.create(data);
		} else {
			wmsIndtlDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		String sql = "update wms_indtl set isdelete=TRUE WHERE id = " + id
				+ ";";
		wmsInDao.executeSQL(sql);

	}

	/**
	 * 更改储位
	 * 
	 * @param ids
	 *            勾选的indtlid
	 * @param locid
	 *            后来选择的储位
	 * @param beforeLocid
	 *            可能存在拆分储位,如果不传之前的locid过来,会分不清
	 * @throws Exception
	 */
	public void changeLocal(String[] ids, Long locid, Long beforeLocid)
			throws Exception {

		try {
			for (int i = 0; i < ids.length; i++) {
				Long indtl = new Long(Integer.parseInt(ids[i]));
				String sql = "SELECT f_changelocal(" + indtl + "," + locid
						+ "," + beforeLocid + ");";
				//System.out.println(sql);
				wmsIndtlDao.executeQuery(sql);
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public void splitLocal(Long id, String dataTex) {
		String sql = "select f_splitlocal(" + id + ",'" + dataTex + "');";
		wmsIndtlDao.executeQuery(sql);
	}

	public void updateLocal(Long id, String data) {
		String sql = "select f_splitlocalbatch(" + id + ",'" + data + "');";
		wmsIndtlDao.executeQuery(sql);

	}

	public void changeLocal(String[] ids, Long localId, Long beforeLocid,
			Integer piecesDesc, Integer pieceout) {

	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_indtl SET isdelete=TRUE  WHERE id in (" + id
				+ ")";
		wmsIndtlDao.executeSQL(sql);

	}
	
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		WmsIndtl data = new WmsIndtl();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = wmsIndtlDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("goodscode")!=null){
				data.setGoodscode(String.valueOf(jsonObject.get("goodscode")));
			}else{
				data.setGoodscode(null);
			}
			if(jsonObject.get("goodsnamec")!=null){
				data.setGoodsnamec(String.valueOf(jsonObject.get("goodsnamec")));
			}else{
				data.setGoodsnamec(null);
			}
			if(jsonObject.get("goodsnamee")!=null){
				data.setGoodsnamee(String.valueOf(jsonObject.get("goodsnamee")));
			}else{
				data.setGoodsnamee(null);
			}
			if(jsonObject.get("goodssize")!=null){
				data.setGoodssize(String.valueOf(jsonObject.get("goodssize")));
			}else{
				data.setGoodssize(null);
			}
			if(jsonObject.get("markno")!=null){
				data.setMarkno(AppUtils.replaceStringByRegEx(String.valueOf(jsonObject.get("markno"))));
			}else{
				data.setMarkno(null);
			}
			if(jsonObject.get("box")!=null && !String.valueOf(jsonObject.get("box")).isEmpty()){
				data.setBox(new Integer(String.valueOf(jsonObject.get("box"))));
			}else{
				data.setBox(0);
			}
			if(jsonObject.get("pieceinbox")!=null && !String.valueOf(jsonObject.get("pieceinbox")).isEmpty()){
				data.setPieceinbox(new BigDecimal(String.valueOf(jsonObject.get("pieceinbox"))));
			}else{
				data.setPieceinbox(new BigDecimal(0));
			}
			if(jsonObject.get("piece")!=null && !String.valueOf(jsonObject.get("piece")).isEmpty()){
				data.setPiece(new Integer(String.valueOf(jsonObject.get("piece"))));
			}else{
				data.setPiece(0);
			}
			if(jsonObject.get("price")!=null && !String.valueOf(jsonObject.get("price")).isEmpty()){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(new BigDecimal(0));
			}
			if(jsonObject.get("goodsl")!=null && !String.valueOf(jsonObject.get("goodsl")).isEmpty()){
				data.setGoodsl(new BigDecimal(String.valueOf(jsonObject.get("goodsl"))));
			}else{
				data.setGoodsl(new BigDecimal(0));
			}
			if(jsonObject.get("goodsw")!=null && !String.valueOf(jsonObject.get("goodsw")).isEmpty()){
				data.setGoodsw(new BigDecimal(String.valueOf(jsonObject.get("goodsw"))));
			}else{
				data.setGoodsw(new BigDecimal(0));
			}
			if(jsonObject.get("goodsh")!=null && !String.valueOf(jsonObject.get("goodsh")).isEmpty()){
				data.setGoodsh(new BigDecimal(String.valueOf(jsonObject.get("goodsh"))));
			}else{
				data.setGoodsh(new BigDecimal(0));
			}
			if(jsonObject.get("gdscbm")!=null && !String.valueOf(jsonObject.get("gdscbm")).isEmpty()){
				data.setGdscbm(new BigDecimal(String.valueOf(jsonObject.get("gdscbm"))));
			}else{
				data.setGdscbm(new BigDecimal(0));
			}
			
			
			if(jsonObject.get("cbmtotle")!=null && !String.valueOf(jsonObject.get("cbmtotle")).isEmpty()){
				data.setCbmtotle(new BigDecimal(String.valueOf(jsonObject.get("cbmtotle"))));
			}else{
				data.setCbmtotle(new BigDecimal(0));
			}
			if(jsonObject.get("wgttotle")!=null && !String.valueOf(jsonObject.get("wgttotle")).isEmpty()){
				data.setWgttotle(new BigDecimal(String.valueOf(jsonObject.get("wgttotle"))));
			}else{
				data.setWgttotle(new BigDecimal(0));
			}
			if(jsonObject.get("gdswgt")!=null && !String.valueOf(jsonObject.get("gdswgt")).isEmpty()){
				data.setGdswgt(new BigDecimal(String.valueOf(jsonObject.get("gdswgt"))));
			}else{
				data.setGdswgt(new BigDecimal(0));
			}
			if(jsonObject.get("goodsvalue")!=null && !String.valueOf(jsonObject.get("goodsvalue")).isEmpty()){
				data.setGoodsvalue(new BigDecimal(String.valueOf(jsonObject.get("goodsvalue"))));
			}else{
				data.setGoodsvalue(new BigDecimal(0));
			}
			if(jsonObject.get("packagee")!=null){
				data.setPackagee(String.valueOf(jsonObject.get("packagee")));
			}else{
				data.setPackagee(null);
			}
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("itemno")!=null && !String.valueOf(jsonObject.get("itemno")).isEmpty()){
				data.setItemno(new Short(String.valueOf(jsonObject.get("itemno"))));
			}else{
				data.setItemno(null);
			}
			if(jsonObject.get("remark")!=null){
				data.setRemark(String.valueOf(jsonObject.get("remark")));
			}else{
				data.setRemark(null);
			}
			if(jsonObject.get("chargeweight")!=null && !String.valueOf(jsonObject.get("chargeweight")).isEmpty()){
				data.setChargeweight(new BigDecimal(String.valueOf(jsonObject.get("chargeweight"))));
			}else{
				data.setChargeweight(new BigDecimal(0));
			}
			if(jsonObject.get("volwgt")!=null && !String.valueOf(jsonObject.get("volwgt")).isEmpty()){
				data.setVolwgt(new BigDecimal(String.valueOf(jsonObject.get("volwgt"))));
			}else{
				data.setVolwgt(new BigDecimal(0));
			}
			saveDtlData(data);
		}		
	}
	
	
	
	
	public void addBatchEditGrid(Object addData, long wmsinid) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			WmsIndtl data = new WmsIndtl();
			data.setInid(wmsinid);
			if(jsonObject.get("goodscode")!=null){
				data.setGoodscode(String.valueOf(jsonObject.get("goodscode")));
			}else{
				data.setGoodscode(null);
			}
			if(jsonObject.get("goodsnamec")!=null){
				data.setGoodsnamec(String.valueOf(jsonObject.get("goodsnamec")));
			}else{
				data.setGoodsnamec(null);
			}
			if(jsonObject.get("goodsnamee")!=null){
				data.setGoodsnamee(String.valueOf(jsonObject.get("goodsnamee")));
			}else{
				data.setGoodsnamee(null);
			}
			if(jsonObject.get("goodssize")!=null){
				data.setGoodssize(String.valueOf(jsonObject.get("goodssize")));
			}else{
				data.setGoodssize(null);
			}
			if(jsonObject.get("markno")!=null){
				data.setMarkno(AppUtils.replaceStringByRegEx(String.valueOf(jsonObject.get("markno"))));
			}else{
				data.setMarkno(null);
			}
			if(jsonObject.get("box")!=null && !String.valueOf(jsonObject.get("box")).isEmpty()){
				data.setBox(new Integer(String.valueOf(jsonObject.get("box"))));
			}else{
				data.setBox(0);
			}
			if(jsonObject.get("pieceinbox")!=null && !String.valueOf(jsonObject.get("pieceinbox")).isEmpty()){
				data.setPieceinbox(new BigDecimal(String.valueOf(jsonObject.get("pieceinbox"))));
			}else{
				data.setPieceinbox(new BigDecimal(0));
			}
			if(jsonObject.get("piece")!=null && !String.valueOf(jsonObject.get("piece")).isEmpty()){
				data.setPiece(new Integer(String.valueOf(jsonObject.get("piece"))));
			}else{
				data.setPiece(0);
			}
			if(jsonObject.get("price")!=null && !String.valueOf(jsonObject.get("price")).isEmpty()){
				data.setPrice(new BigDecimal(String.valueOf(jsonObject.get("price"))));
			}else{
				data.setPrice(new BigDecimal(0));
			}
			if(jsonObject.get("goodsl")!=null && !String.valueOf(jsonObject.get("goodsl")).isEmpty()){
				data.setGoodsl(new BigDecimal(String.valueOf(jsonObject.get("goodsl"))));
			}else{
				data.setGoodsl(new BigDecimal(0));
			}
			if(jsonObject.get("goodsw")!=null && !String.valueOf(jsonObject.get("goodsw")).isEmpty()){
				data.setGoodsw(new BigDecimal(String.valueOf(jsonObject.get("goodsw"))));
			}else{
				data.setGoodsw(new BigDecimal(0));
			}
			if(jsonObject.get("goodsh")!=null && !String.valueOf(jsonObject.get("goodsh")).isEmpty()){
				data.setGoodsh(new BigDecimal(String.valueOf(jsonObject.get("goodsh"))));
			}else{
				data.setGoodsh(new BigDecimal(0));
			}
			if(jsonObject.get("gdscbm")!=null && !String.valueOf(jsonObject.get("gdscbm")).isEmpty()){
				data.setGdscbm(new BigDecimal(String.valueOf(jsonObject.get("gdscbm"))));
			}else{
				data.setGdscbm(new BigDecimal(0));
			}
			
			
			if(jsonObject.get("cbmtotle")!=null && !String.valueOf(jsonObject.get("cbmtotle")).isEmpty()){
				data.setCbmtotle(new BigDecimal(String.valueOf(jsonObject.get("cbmtotle"))));
			}else{
				data.setCbmtotle(new BigDecimal(0));
			}
			if(jsonObject.get("wgttotle")!=null && !String.valueOf(jsonObject.get("wgttotle")).isEmpty()){
				data.setWgttotle(new BigDecimal(String.valueOf(jsonObject.get("wgttotle"))));
			}else{
				data.setWgttotle(new BigDecimal(0));
			}
			if(jsonObject.get("gdswgt")!=null && !String.valueOf(jsonObject.get("gdswgt")).isEmpty()){
				data.setGdswgt(new BigDecimal(String.valueOf(jsonObject.get("gdswgt"))));
			}else{
				data.setGdswgt(new BigDecimal(0));
			}
			if(jsonObject.get("goodsvalue")!=null && !String.valueOf(jsonObject.get("goodsvalue")).isEmpty()){
				data.setGoodsvalue(new BigDecimal(String.valueOf(jsonObject.get("goodsvalue"))));
			}else{
				data.setGoodsvalue(new BigDecimal(0));
			}
			if(jsonObject.get("packagee")!=null){
				data.setPackagee(String.valueOf(jsonObject.get("packagee")));
			}else{
				data.setPackagee(null);
			}
			if(jsonObject.get("currency")!=null){
				data.setCurrency(String.valueOf(jsonObject.get("currency")));
			}else{
				data.setCurrency(null);
			}
			if(jsonObject.get("itemno")!=null && !String.valueOf(jsonObject.get("itemno")).isEmpty()){
				data.setItemno(new Short(String.valueOf(jsonObject.get("itemno"))));
			}else{
				data.setItemno(null);
			}
			if(jsonObject.get("remark")!=null){
				data.setRemark(String.valueOf(jsonObject.get("remark")));
			}else{
				data.setRemark(null);
			}
			saveDtlData(data);
		}
	}

	public WmsIndtl findByInid(long id) {
		String sql = " inid = "+id+" AND isdelete = FALSE ORDER BY id";
		java.util.List<WmsIndtl> list =  wmsIndtlDao.findAllByClauseWhere(sql);
		return list.get(0);
	}

	public void batchUpdateInfo(String[] ids, Integer pieceedit,BigDecimal wgttotleedit, String packageedit, String usercode) {
		StringBuilder dtlBuilder = new StringBuilder();
		dtlBuilder.append("UPDATE wms_indtl SET ");
		if(pieceedit != null) dtlBuilder.append("piece = "+pieceedit+",");
		if(wgttotleedit != null) dtlBuilder.append("wgttotle = "+wgttotleedit+",");
		if(!StrUtils.isNull(packageedit)) dtlBuilder.append("packagee = '"+packageedit+"',");
		dtlBuilder.append("updater = '"+usercode+"', updatetime= NOW() where id = (SELECT id FROM wms_indtl WHERE inid IN ("+(StrUtils.array2List(ids))+") AND isdelete = FALSE ORDER BY id LIMIT 1)");
		wmsIndtlDao.executeSQL(dtlBuilder.toString());
	}
	
	public void batchUpdateForTrainInfo(String[] ids, Integer pieceedit,BigDecimal wgttotleedit, String packageedit, BigDecimal cbmtotleedit, String usercode) {
		StringBuilder dtlBuilder = new StringBuilder();
		dtlBuilder.append("UPDATE wms_indtl SET ");
		if(pieceedit != null) dtlBuilder.append("piece = "+pieceedit+",");
		if(wgttotleedit != null) dtlBuilder.append("wgttotle = "+wgttotleedit+",");
		if(!StrUtils.isNull(packageedit)) dtlBuilder.append("packagee = '"+packageedit+"',");
		if(cbmtotleedit != null) dtlBuilder.append("cbmtotle = '"+cbmtotleedit+"',");
		dtlBuilder.append("updater = '"+usercode+"', updatetime= NOW() where id = (SELECT id FROM wms_indtl WHERE inid IN ("+(StrUtils.array2List(ids))+") AND isdelete = FALSE ORDER BY id LIMIT 1)");
		wmsIndtlDao.executeSQL(dtlBuilder.toString());
	}
	
	
}