package com.scp.view.module.data;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "agentchooseserviceBean", scope = ManagedBeanScope.SESSION)
public class AgentChooseService{
	
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
			"\n			customerabbr ILIKE '%" + popQryKey + "%' " +
			"\n		 OR name ILIKE '%" + popQryKey + "%' " +
			"\n		 OR customernamee ILIKE '%" + popQryKey + "%' " +
			"\n		 OR customeraddresse ILIKE '%" + popQryKey + "%' " +
			"\n		 OR contactxt ILIKE '%" + popQryKey + "%' " +
			"\n	 )";
	}

	public List<Map> findPort(String qryAgentKey) {
		qryAgentKey = qryAgentKey.toUpperCase();
		String querySql = 
			"\nSELECT " +
			"\n* " +
			"\nFROM _sys_corpcontacts a " +
			"\nWHERE isdelete = false  AND isagentdes  = TRUE" +
			"\nAND " +
			"\n	(	" +
			"\n			customercode ILIKE '%" + qryAgentKey + "%' " +
			"\n		 OR customernamee ILIKE '%" + qryAgentKey + "%' " +
			"\n		 OR contactxt ILIKE '%" + qryAgentKey + "%'" +
			"\n	 )";
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			
		return list;
	}

	public GridDataProvider getPortDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _sys_corpcontacts a " +
					"\nWHERE isdelete = false AND isagentdes  = TRUE AND contactype = 'B' AND contactype2 = 'A'"+
					  qrySql +
					"\nORDER BY customerid"+
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
					"\nFROM _sys_corpcontacts a " +
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
					"\nFROM _sys_corpcontacts a " +
					"\nWHERE isdelete = false AND isagentdes  = TRUE AND contactype = 'B' AND contactype2 = 'A'"+
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
