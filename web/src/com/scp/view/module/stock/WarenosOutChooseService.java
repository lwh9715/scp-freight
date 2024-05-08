package com.scp.view.module.stock;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;

/**
 * kai 出仓单 弹窗选择
 * 
 * @author Administrator
 * 
 */
@ManagedBean(name = "warenosoutchooseserviceBean", scope = ManagedBeanScope.SESSION)
public class WarenosOutChooseService {

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	@SaveState
	@Accessible
	private String popQryKey;

	@SaveState
	@Accessible
	private String qrySql = "\nAND 1=1";

	public void qryNos(String popQryKey) {
		if (StrUtils.isNull(popQryKey)) {
			qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();

		if (StrUtils.isNull(this.popQryKey))
			this.qrySql = "\nAND 1=1";
		this.popQryKey = this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		qrySql = "\nAND " + "\n	(	" + "\n			UPPER(nos) LIKE '%"
				+ this.popQryKey + "%' "
				+ "\n		 OR UPPER(customerabbr) LIKE '%" + this.popQryKey + "%'"
				+ "\n		 OR UPPER(warehousedesc) LIKE '%" + this.popQryKey
				+ "%'" + "\n	 )";
	}

	public List<Map> findJobsnos(String qryPortKey, String portsql) {
		qryPortKey = qryPortKey.toUpperCase();
		String querySql = "\nSELECT " + "\n* " + "\nFROM _wms_out a "
				+ "\nWHERE isdelete = false " + portsql + "\nAND " + "\n	(	"
				+ "\n			UPPER(nos) LIKE '%" + qryPortKey + "%' " + "\n	 )";
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);

		return list;
	}

	public GridDataProvider getJobsnosDataProvider(final String jobnossql) {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = "\nSELECT " + "\n* " + "\nFROM _wms_out a "
						+ "\nWHERE isdelete = false  " + jobnossql + qrySql
						+ "\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate
							.queryWithUserDefineSql(querySql);
					if (list == null)
						return null;
					return list.toArray();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			// @Override
			// public Object[] getElementsById(String[] id) {
			// String querySql =
			// "\nSELECT " +
			// "\n* " +
			// "\nFROM _fina_jobs a " +
			// "\nWHERE isdelete = false  "+
			// "\nAND id= " +id[0];
			// ////System.out.println(querySql);
			// try {
			// List<Map> list =
			// daoIbatisTemplate.queryWithUserDefineSql(querySql);
			// if(list==null) return null;
			// return list.toArray();
			// } catch (Exception e) {
			// e.printStackTrace();
			// return null;
			// }
			// }

			@Override
			public int getTotalCount() {
				String countSql = "\nSELECT " + "\nCOUNT(*) AS counts  "
						+ "\nFROM _wms_out a " + "\nWHERE isdelete = false  "
						+ jobnossql + qrySql;
				try {
					Map m = daoIbatisTemplate
							.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long) m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
}
