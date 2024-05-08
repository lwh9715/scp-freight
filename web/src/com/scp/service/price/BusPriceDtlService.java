package com.scp.service.price;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.base.LMapBase;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.price.BusPriceDtlDao;
import com.scp.model.price.BusPriceDtl;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class BusPriceDtlService{
	
	@Resource
	public BusPriceDtlDao busPriceDtlDao;
	
	public void saveData(BusPriceDtl data) {
		if (0 > data.getId()) {
			busPriceDtlDao.create(data);
		} else {
			busPriceDtlDao.modify(data);
		}
	}
	
	
	public String getPricedtlById(Long priceid){
		boolean isen = AppUtils.getUserSession().getMlType().equals(LMapBase.MLType.en);
		String sql = 
					"\nSELECT"+
					"\n	array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json"+
					"\nFROM"+
					"\n	("+
					"\n			SELECT"+
					"\n				id,"+
					"\n				priceid,"+
					"\n				feeitemid,"+
					"\n				feeitemcode,"+
				
					//1582 英文翻译，订单
					(isen?"\n		 (SELECT namee FROM dat_feeitem WHERE id = d.feeitemid) AS feeitemname,":"\n	feeitemname,")+
					"\n				ppcc,"+
					"\n				currency,"+
					(isen?"\n		(CASE WHEN unit='箱型' THEN 'Cnt type' WHEN unit='箱' THEN 'box' ELSE 'Bill' END) AS unit,":"\n	unit,")+
					"\n				amt20,"+
					"\n				amt20_ar,"+
					"\n				amt40gp,"+
					"\n				amt40gp_ar,"+
					"\n				amt40hq,"+
					"\n				amt40hq_ar,"+
					"\n				amtother,"+
					"\n				amtother_ar,"+
					"\n				amt,"+
					"\n				amt_ar,"+
					"\n				piece20,"+
					"\n				piece40gp,"+
					"\n				piece40hq,"+
					"\n				pieceother,"+
					"\n				piece,"+
					"\n				cntypeothercode"+
					"\n			FROM"+
					"\n				bus_pricedtl d"+
					"\n			WHERE"+
					"\n				d.priceid = "+priceid+
					"\n				AND d.isdelete = FALSE"+
					"\n			ORDER BY ID"+
					"\n		) T;";
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		Map<String, String> map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		
		if(map != null && 1 == map.size()){
			return StrUtils.getMapVal(map, "json");
		}
		return "''";
	}
	
	public void saveOrModify(List<BusPriceDtl> moduleList) {
		for (BusPriceDtl instance : moduleList) {
			busPriceDtlDao.createOrModify(instance);
		}
	}
	
	public void removes(List<Long> lists) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Long id : lists) {
			String sql = "\nUPDATE bus_pricedtl SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		busPriceDtlDao.executeSQL(stringBuffer.toString());
	}
}