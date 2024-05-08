package com.scp.service.data;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.data.DatWarehouseLocDao;

@Component
public class WarehouselocalMgrService {

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	@Resource
	public DatWarehouseLocDao datWarehouseLocalDao;

	public Long findlocalid(String s) {
		String[] s1 = s.split(",");
		String localcode = s1[0].toUpperCase();
		String warehouseid = s1[1];
		String areaid = s1[2];
		String sql = "\nSELECT " + "\nid AS id " + "\nFROM dat_warehouse_loc "
				+ "\nWHERE warehouseid = " + warehouseid + "\nAND "
				+ "areaid = " + areaid + "\nAND " + "code = '" + localcode
				+ "';";
		//AppUtils.debug(sql);
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		Long lid = (Long) m.get("id");
		return lid;
	}

	public String findlocdtl(String s) {
		String[] s1 = s.split(",");
		String localcode = s1[0].toUpperCase();
		String warehouseid = s1[1];
		String areaid = s1[2];
		String querySql = "\nSELECT"
				+ "\n(w.code || '/' || COALESCE(w.namec , '') || '-->' || a.code || '-->' || l.code) AS locdesc"
				+ "\nFROM dat_warehouse w , dat_warehouse_area a , dat_warehouse_loc l"
				+ "\nWHERE a.warehouseid =" + localcode + "\nAND a.id=l.areaid"
				+ "\nAND l.areaid = " + areaid + "\nAND l.code = '" + localcode
				+ "'";
		String locdesc = String.valueOf(daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(querySql).get("locdesc"));
		return locdesc;

	}

}