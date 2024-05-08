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
import com.scp.dao.wms.WmsTransDao;
import com.scp.dao.wms.WmsTransdtlDao;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.wms.WmsTrans;
import com.scp.model.wms.WmsTransdtl;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class WmsTransdtlMgrService {

	@Resource
	public WmsTransdtlDao wmsTransdtlDao;

	@Resource
	public WmsTransDao wmsTransDao;

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveMasterData(WmsTrans data) {
		if (0 == data.getId()) {
			wmsTransDao.create(data);
		} else {
			wmsTransDao.modify(data);
		}
	}

	public void saveDtlData(WmsTransdtl data) {
		if (0 == data.getId()) {
			wmsTransdtlDao.create(data);
		} else {
			wmsTransdtlDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		String sql = "update wms_outdtl set isdelete=TRUE WHERE id = " + id
				+ ";";
		wmsTransdtlDao.executeSQL(sql);
	}

	public void delBatch(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE wms_outdtl SET isdelete=TRUE  WHERE id in (" + id
				+ ")";
		wmsTransdtlDao.executeSQL(sql);

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
	public void saveDtlDatas(Object modifiedData, Long transid) throws Exception {
		JSONArray jsonArray = (JSONArray) modifiedData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			WmsTransdtl ddata = new WmsTransdtl();
			String pieceout = String.valueOf(jsonObject.get("pieceOut")==null?"":jsonObject.get("pieceOut"));
			String goodsnamec = String.valueOf(jsonObject.get("goodsnamec")==null?"":jsonObject.get("goodsnamec"));
			String goodssize = String.valueOf(jsonObject.get("goodssize")==null?"":jsonObject.get("goodssize"));
			String markno = String.valueOf(jsonObject.get("markno")==null?"":jsonObject.get("markno"));
			String goodscolor = String.valueOf(jsonObject.get("goodscolor")==null?"":jsonObject.get("goodscolor"));
			Double pieceinbox = Double.parseDouble(jsonObject.get("pieceinbox")==null?"0":(jsonObject.get("pieceinbox")).toString());
			String goodscode = String.valueOf(jsonObject.get("goodscode")==null?"":jsonObject.get("goodscode"));
			Long customerid = Long.valueOf(jsonObject.get("customerid")==null?"0":(jsonObject.get("customerid")).toString());
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
						+ "CAST(10000 AS BIGINT) AS counts where 1=1 AND   exists (SELECT  1 from  wms_transdtl t WHERE  '"
						+ goodscode
						+ "' =t.goodscode AND '"
						+ goodsnamec
						+ "'=t.goodsnamec AND '"
						+ goodssize
						+ "' =t.goodssize AND '"
						+ markno
						+ "'=t.markno AND '"
						+ goodscolor
						+ "'=t.goodscolor  AND t.isdelete = false AND t.transid = "
						+ transid + ")";
				List<Map> m = daoIbatisTemplate.queryWithUserDefineSql(sql);

				if (m.size() > 0) {
					throw new CommonRuntimeException("fail:不能重复出货品" + goodscode);
				} else {
					ddata.setPieceinbox(new BigDecimal(pieceinbox));
					ddata.setTransid(transid);
					ddata.setPiece(pieceOut);
					ddata.setGoodssize(goodssize);
					ddata.setGoodsnamec(goodsnamec);
					ddata.setMarkno(markno);
					ddata.setGoodscolor(goodscolor);
					ddata.setGoodscode(goodscode);
					ddata.setCustomerid(customerid);
					saveDtlData(ddata);
				}
			}
		}
	}


}