package com.scp.view.module.data;

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

@ManagedBean(name = "portchooseserviceBean", scope = ManagedBeanScope.SESSION)
public class PortChooseService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	
	@SaveState
	@Accessible
	private  String popQryKey;
	
	@SaveState
	@Accessible
	private String qrySql = "\nAND 1=1";

	public void qryPort(String popQryKey) {
		if(StrUtils.isNull(popQryKey)){
			qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey)) this.qrySql = "\nAND 1=1";
		this.popQryKey =this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		
		qrySql = 
			"\nAND " +
			"\n	(	" +
			"\n			UPPER(code) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(namec) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(namee) LIKE '%" + this.popQryKey + "%'" +
			"\n	 )";
	}

	public List<Map> findPort(String qryPortKey, String portsql) {
		qryPortKey = qryPortKey.toUpperCase();
		String querySql = 
			"\nSELECT " +
			"\n* " +
			"\nFROM dat_port a " +
			"\nWHERE isdelete = false " +portsql+
			"\nAND " +
			"\n	(	" +
			"\n			UPPER(code) LIKE '%" + qryPortKey + "%' " +
			"\n		 OR UPPER(namec) LIKE '%" + qryPortKey + "%' " +
			"\n		 OR UPPER(namee) LIKE '%" + qryPortKey + "%'" +
			"\n	 )";
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			
		return list;
	}

	public GridDataProvider getPortDataProvider(final String portsql) {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM dat_port a " +
					"\nWHERE isdelete = false "+portsql+
					  qrySql +
					"\nORDER BY code"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			public Object[] getElementsById(String[] id) {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM dat_port a " +
					"\nWHERE id= " +id[0];
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public int getTotalCount() {
				String countSql = 
					"\nSELECT " +
					"\nCOUNT(*) AS counts  " +
					"\nFROM dat_port a " +
					"\nWHERE isdelete = false "+portsql+
					  qrySql +
					";";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
	
}
