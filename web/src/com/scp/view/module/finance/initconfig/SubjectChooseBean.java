package com.scp.view.module.finance.initconfig;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.StrUtils;

/**
 * @author Neo
 *
 */
@ManagedBean(name = "subjectchooseBean", scope = ManagedBeanScope.SESSION)
public class SubjectChooseBean {
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	private String popQryKey;
	
	@SaveState
	private String qrySql = "\nAND 1=1";
	
	public void qry(String popQryKey) {
		if(StrUtils.isNull(popQryKey)){
			this.qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey))this.qrySql = "\nAND 1=1";
		this.popQryKey = this.popQryKey.replaceAll("'", "''");
		qrySql = 
			"\nAND  category = '" + this.popQryKey + "'" +
			"\n	 )";
	}
	
	
	
	/**
	 * 根据关键字查找客户
	 * @param qryCustomerKey
	 * @return
	 * @throws Exception
	 */
	public List<Map> findSubject(String qryCustomerKey) throws Exception{
		qryCustomerKey = qryCustomerKey.toUpperCase();
		String querySql = 
			"\nSELECT " +
			"\n* " +
			"\nFROM _fs_acounttitle a " +
			"\nWHERE isdelete = false" +
			"\nAND  category = '" + this.popQryKey + "'" +
			"\n	 )";
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			
		return list;
	}


	/**
	 * 通用弹窗查客户
	 * @return
	 */
	public GridDataProvider getSubjectDataProvider(){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _fs_acounttitle a " +
					"\nWHERE isdelete = false " +
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
					"\nFROM _fs_acounttitle a " +
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
					"\nFROM _fs_acounttitle a " +
					"\nWHERE isdelete = false"+
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
	
	
	

	public void setQrySql(String qrySql) {
		this.qrySql = qrySql;
	}
	
	
}
