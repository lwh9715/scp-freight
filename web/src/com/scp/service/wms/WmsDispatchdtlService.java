package com.scp.service.wms;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.wms.WmsDispatchDao;
import com.scp.dao.wms.WmsDispatchdtlDao;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.wms.WmsDispatch;
import com.scp.model.wms.WmsDispatchdtl;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class WmsDispatchdtlService {

	@Resource
	public WmsDispatchdtlDao wmsDispatchdtlDao;

	@Resource
	public WmsDispatchDao wmsDispatchDao;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveMasterData(WmsDispatch data) {
		if (0 == data.getId()) {
			wmsDispatchDao.create(data);

		} else {
			wmsDispatchDao.modify(data);
		}
	}

	public void saveDtlData(WmsDispatchdtl data) {

		if (0 == data.getId()) {

			wmsDispatchdtlDao.create(data);

		} else {
			wmsDispatchdtlDao.modify(data);
		}
	}

	public void removeDate(long id) {
		// TODO Auto-generated method stub

	}

	// public void removeDate(Long id) {
	// String sql =
	// "DELETE FROM quote_lcl_cost WHERE id = " + id +";";
	// sql += "\nDELETE FROM quote_lcl_costfee WHERE costid = " + id +";";
	// wmsPriceDao.executeSQL(sql);
	// // quoteLclAgentquoteLclAgentDao.findById(id);
	// // QuoteLclFees data = quoteLclFeesDao.findById(id);
	// // quoteLclFeesDao.remove(data);
	// }
	//
	// public void delBatch(String[] ids) {
	// String id = StrUtils.array2List(ids);
	// String sql = "Delete from wms_price_group  WHERE id in ("+id+")";
	// wmsPriceGroupDao.executeSQL(sql);
	//		
	// }
	//	
	/**
	 * 批量生成盘点明细
	 */
	public void saveCheckDtlDatas(Object modifiedData, Long outid) {
		String goodscode = "";
		JSONArray jsonArray = (JSONArray) modifiedData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			WmsDispatchdtl ddata = new WmsDispatchdtl();
			String pieceadd = String.valueOf(jsonObject.get("pieceadd"));
			String piececut = String.valueOf(jsonObject.get("piececut"));
			String piece = String.valueOf(jsonObject.get("piece"));
			String goodsnamec = String.valueOf(jsonObject.get("goodsnamec"));
			String goodssize = String.valueOf(jsonObject.get("goodssize"));
			String markno = String.valueOf(jsonObject.get("markno"));
			String goodscolor = String.valueOf(jsonObject.get("goodscolor"));
			Integer pieceinbox = Integer.parseInt(jsonObject.get("pieceinbox")
					.toString());
			Integer pieceNum = Integer.parseInt(jsonObject.get("piecenum")
					.toString());
			goodscode = (String) jsonObject.get("goodscode");
			if (StrUtils.isNull(goodscode) || goodscode.equals("null")) {
				goodscode = "";
			}
			
			if (StrUtils.isNull(pieceadd) || pieceadd.equals("null")) {
				pieceadd = "0";
			}
			if (StrUtils.isNull(piececut) || piececut.equals("null")) {
				piececut = "0";

			}
			if (StrUtils.isNull(goodsnamec)||goodsnamec.equals("null")) {
				goodsnamec = "";
			}
			if (StrUtils.isNull(goodssize)||goodssize.equals("null")) {
				goodssize = "";
			}
			if (StrUtils.isNull(markno)||markno.equals("null")) {
				markno = "";
			}
			if (StrUtils.isNull(goodscolor)||goodscolor.equals("null")) {
				goodscolor = "";
			}

			Integer pieceAdd = new Integer(Integer.parseInt(pieceadd));// 返回是long,不能直接将Long转为为integer
			Integer pieceCut = new Integer(Integer.parseInt(piececut));// 返回是long,不能直接将Long转为为integer
			if (pieceAdd % pieceinbox != 0) {
				throw new CommonRuntimeException("fail:" + goodscode
						+ "盘盈件数应该为每箱件数的倍数");

			}
			if (pieceCut % pieceinbox != 0) {
				throw new CommonRuntimeException("fail:" + goodscode
						+ "盘盈件数应该为每箱件数的倍数");
			}
			if (pieceAdd > 0 && pieceCut > 0) {
				throw new CommonRuntimeException("盘亏或者盘亏只能选择一项");
			}

			if (pieceAdd == 0 && pieceCut == 0) {
			} else {
				ddata.setPieceinbox(new BigDecimal(pieceinbox));
				ddata.setPiecestock(pieceNum);
				ddata.setDispatchid(outid);
				ddata.setPieceadd(pieceAdd);
				ddata.setPiececut(pieceCut);
				ddata.setGoodssize(goodssize);
				ddata.setGoodsnamec(goodsnamec);
				ddata.setMarkno(markno);
				ddata.setGoodscolor(goodscolor);
				ddata.setGoodscode(goodscode);
				saveDtlData(ddata);

			}
		}

	}

	public void saveCheckDtlDatas(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		if(jsonArray == null)return;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Long id = new Long(String.valueOf(jsonObject.get("id")));// 返回是long,不能直接将Long转为为integer
			WmsDispatchdtl ddata = wmsDispatchdtlDao.findById(id);
			String locdesc = String.valueOf(jsonObject.get("locdesc"));
			
			if(StrUtils.isNull(locdesc) || locdesc.equals("null")){
				locdesc=null;
			}
//			if (StrUtils.isNull(locdesc)) {
//				throw new CommonRuntimeException("locdesc is null");
			else {
				String data = StrUtils.toDBC(locdesc).replaceAll(" ", "")
						.replaceAll("\\n", "");
				String[] format = locdesc.split(",");
				String rex1 = "^[A-Za-z0-9]+:(([0-9][0-9][1-9])|([0-9][1-9]0)):(([0-9][0-9][1-9])|([0-9][1-9]0)):[1-9]\\d*$";
				for (int j = 0; j < format.length; j++) {

					if (format[j].matches(rex1) == false) {
						throw new CommonRuntimeException("fail:储位格式错误,请检查");
					} else {
					}
				}
			}
			String pieceadd = String.valueOf(jsonObject.get("pieceadd"));
			String piececut = String.valueOf(jsonObject.get("piececut"));
			if (StrUtils.isNull(pieceadd) || pieceadd.equals("null")) {
				pieceadd = "0";
			}
			if (StrUtils.isNull(piececut) || piececut.equals("null")) {
				piececut = "0";

			}
			
			Integer pieceAdd = new Integer(pieceadd);// 返回是long,不能直接将Long转为为integer

			Integer pieceCut = new Integer(piececut);// 返回是long,不能直接将Long转为为integer
			String goodscbm = String.valueOf(jsonObject
					.get("goodscbmall"));
			

			if (pieceAdd > 0 && pieceCut > 0) {
				throw new CommonRuntimeException("盘亏或者盘亏只能选择一项");

			} else if(pieceAdd > 0 &&(StrUtils.isNull(goodscbm) || goodscbm.equals("null"))){
				throw new CommonRuntimeException("盘盈货物请输入体积");
			}else {
				
			}
			
			if(pieceAdd > 0){
				BigDecimal cbmall = new BigDecimal(goodscbm);
				ddata.setGoodscbmall(cbmall);
				
				
			}else{
				
			}
			
			ddata.setPieceadd(pieceAdd);
			ddata.setPiececut(pieceCut);
			ddata.setLocdesc(locdesc);
			saveDtlData(ddata);
			
		}

	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_dispatchdtl SET isdelete=TRUE  WHERE id in (" + id
				+ ")";
		wmsDispatchDao.executeSQL(sql);
		
	}

	
}