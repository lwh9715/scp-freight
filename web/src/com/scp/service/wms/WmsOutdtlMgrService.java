package com.scp.service.wms;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.wms.WmsOutDao;
import com.scp.dao.wms.WmsOutdtlDao;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.wms.WmsOut;
import com.scp.model.wms.WmsOutdtl;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class WmsOutdtlMgrService {

	@Resource
	public WmsOutdtlDao wmsOutdtlDao;

	@Resource
	public WmsOutDao wmsOutDao;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveMasterData(WmsOut data) {
		if (0 == data.getId()) {
			wmsOutDao.create(data);
		} else {
			wmsOutDao.modify(data);
		}
	}

	public void saveDtlData(WmsOutdtl data) {
		if (0 == data.getId()) {
			wmsOutdtlDao.create(data);
		} else {
			wmsOutdtlDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		String sql = "update wms_outdtl set isdelete=TRUE WHERE id = " + id
				+ ";";
		wmsOutDao.executeSQL(sql);
	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_outdtl SET isdelete=TRUE  WHERE id in (" + id
				+ ")";
		wmsOutdtlDao.executeSQL(sql);

	}

	public int getStocks(Long customerid, String goodscode, String goodsnamec,
			String goodssize, String markno, String goodscolor) {

		String sql = ""
				+ "\nSELECT "
				+ "\nsum(piece) AS piece "
				+ "\nFROM _wms_stock "
				+ "\nWHERE  customerid  = "
				+ customerid
				+ " AND goodscode = '"
				+ goodscode
				+ "'"
				+ " AND goodsnamec = '"
				+ goodsnamec
				+ "'"
				+ " AND goodssize = '"
				+ goodssize
				+ "'"
				+ " AND markno = '"
				+ markno
				+ "'"
				+ " AND goodscolor = '"
				+ goodscolor
				+ "'"
				+ "\nGROUP BY goodscode , customerid , goodsnamec,markno,goodssize,goodscolor"
				+ ";";
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			int stocknum = ((BigDecimal) m.get("piece")).intValue(); //
			return stocknum;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String getCustomerAbbr(Long id) {
		String sql = "\nSELECT abbr FROM sys_corporation WHERE id =" + id;
		try {
			Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String abbr = (String) m.get("abbr"); //
			return abbr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 批量生成明细，取json对象
	 * 
	 * @param modifiedData
	 * @throws Exception
	 */
	public void saveDtlDatas(Object modifiedData, Long outid) throws Exception {
		JSONArray jsonArray = (JSONArray) modifiedData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			WmsOutdtl ddata = new WmsOutdtl();
			String pieceout = String.valueOf(jsonObject.get("pieceOut")==null?"":jsonObject.get("pieceOut"));
			String goodsnamec = String.valueOf(jsonObject.get("goodsnamec")==null?"":jsonObject.get("goodsnamec"));
			String goodssize = String.valueOf(jsonObject.get("goodssize")==null?"":jsonObject.get("goodssize"));
			String markno = String.valueOf(jsonObject.get("markno")==null?"":jsonObject.get("markno"));
			String goodscolor = String.valueOf(jsonObject.get("goodscolor")==null?"":jsonObject.get("goodscolor"));
			Double pieceinbox = Double.parseDouble(jsonObject.get("pieceinbox")==null?"0":(jsonObject.get("pieceinbox")).toString());
			String goodscode = String.valueOf(jsonObject.get("goodscode")==null?"":jsonObject.get("goodscode"));
			if (StrUtils.isNull(pieceout)) {
				pieceout = "0";
			}
			if (StrUtils.isNull(goodsnamec)) {
				goodsnamec = "";
			}
			if (StrUtils.isNull(goodssize)) {
				goodssize = "";
			}
			if (StrUtils.isNull(markno)) {
				markno = "";
			}
			if (StrUtils.isNull(goodscolor)) {
				goodscolor = "";
			}
			Integer pieceOut = new Integer(Integer.parseInt(pieceout));// 返回是long,不能直接将Long转为为integer
			if (pieceOut % pieceinbox != 0) {
				throw new CommonRuntimeException("fail:" + goodscode
						+ "出库件数应该为每箱件数的倍数");
			}
			if (pieceOut == null || pieceOut == 0) {
			} else {
				String sql = " SELECT "
						+ "CAST(10000 AS BIGINT) AS counts where 1=1 AND   exists (SELECT  1 from  wms_outdtl t WHERE  '"
						+ goodscode
						+ "' =t.goodscode AND '"
						+ goodsnamec
						+ "'=t.goodsnamec AND '"
						+ goodssize
						+ "' =t.goodssize AND '"
						+ markno
						+ "'=t.markno AND '"
						+ goodscolor
						+ "'=t.goodscolor  AND t.isdelete = false AND t.outid = "
						+ outid + ")";
				List<Map> m = daoIbatisTemplate.queryWithUserDefineSql(sql);

				if (m.size() > 0) {
					throw new CommonRuntimeException("fail:不能重复出货品" + goodscode);
				} else {
					ddata.setPieceinbox(new BigDecimal(pieceinbox));
					ddata.setOutid(outid);
					ddata.setPiece(pieceOut);
					ddata.setGoodssize(goodssize);
					ddata.setGoodsnamec(goodsnamec);
					ddata.setMarkno(markno);
					ddata.setGoodscolor(goodscolor);
					ddata.setGoodscode(goodscode);
					saveDtlData(ddata);
				}
			}
		}
	}

	/**
	 * 批量生成明细，取json对象
	 * 
	 * @param modifiedData
	 * @throws Exception
	 */
	public void saveLoadDtlDatas(Object modifiedData, Long loadid)
			throws Exception {
		JSONArray jsonArray = (JSONArray) modifiedData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			// WmsOutdtl ddata = new WmsOutdtl();
			String customerid = String.valueOf(jsonObject.get("customerid"));
			String linkid = String.valueOf(jsonObject.get("linkid"));
			String warehouseid = String.valueOf(jsonObject.get("warehouseid"));
			Long inid = Long.valueOf(String.valueOf(jsonObject.get("inid")));
			String channelid = String.valueOf(jsonObject.get("channelid")== null?"0":jsonObject.get("channelid"));
			String pieceout = String.valueOf(jsonObject.get("pieceOut")== null?"0":jsonObject.get("pieceOut"));
			String goodsnamec = String.valueOf(jsonObject.get("goodsnamec")== null?"":jsonObject.get("goodsnamec"))
					.replaceAll("'", "''");
			String goodssize = String.valueOf(jsonObject.get("goodssize")== null?"":jsonObject.get("goodssize"))
					.replaceAll("'", "''");
			String markno = String.valueOf(jsonObject.get("markno")== null?"":jsonObject.get("markno"))
					.replaceAll("'", "''");
			String goodscolor = String.valueOf(jsonObject.get("goodscolor")== null?"":jsonObject.get("goodscolor"))
					.replaceAll("'", "''");
			Double pieceinbox = Double.parseDouble(jsonObject.get("pieceinbox")== null?"0":jsonObject.get("pieceinbox").toString());
			String goodscode = String.valueOf(jsonObject.get("goodscode")== null?"":jsonObject.get("goodscode")).replaceAll("'", "''");
			String consignee = String.valueOf(jsonObject.get("consignee") == null?"":jsonObject.get("consignee")).replaceAll("'", "''");
			
			String remarks = String.valueOf(jsonObject.get("remarks") == null?"": jsonObject.get("remarks")).replaceAll(
					"'", "''");

			if (StrUtils.isNull(pieceout)) {
				pieceout = "0";
			}
			if (StrUtils.isNull(goodsnamec)) {
				goodsnamec = "";
			}
			if (StrUtils.isNull(goodssize)) {
				goodssize = "";
			}
			if (StrUtils.isNull(markno)) {
				markno = "";
			}
			if (StrUtils.isNull(goodscolor)) {
				goodscolor = "";
			}
			if (StrUtils.isNull(consignee)) {
				consignee = "";
			}

			if (StrUtils.isNull(linkid)) {
				linkid = "";
			}
			Integer pieceOut = new Integer(Integer.parseInt(pieceout));// 返回是long,不能直接将Long转为为integer
			if (pieceOut % pieceinbox != 0) {
				throw new CommonRuntimeException("fail:" + goodscode
						+ "出库件数应该为每箱件数的倍数");
			}
			if (pieceOut == null || pieceOut == 0) {
			} else {
				//					
				String sql2 = "SELECT f_bus_ship_load('loadid=" + loadid
						+ ";customerid=" + customerid + ";warehouseid="
						+ warehouseid + ";linkid=" + linkid + ";goodscode="
						+ goodscode + ";goodsnamec=" + goodsnamec
						+ ";goodssize=" + goodssize + ";markno=" + markno
						+ ";goodscolor=" + goodscolor + ";pieceinbox="
						+ pieceinbox + ";pieceout=" + pieceOut + ";updater="
						+ AppUtils.getUserSession().getUsercode() + ";corpid="
						+ AppUtils.getUserSession().getCorpid() + ";inid="
						+ inid + ";remark=" + remarks + ";consignee="+consignee+";channelid="+channelid+"');";
				wmsOutdtlDao.executeQuery(sql2);

			}
		}
	}

}