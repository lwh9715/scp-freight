package com.scp.view.module.customer;

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
 * 弹窗查联系人
 */
@ManagedBean(name = "customerconchooseBean", scope = ManagedBeanScope.SESSION)
public class CustomerConChooseBean{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	private String popQryKey;
	
	@SaveState
	private String qrySql = "\nAND 1=1";
	
	

	
	@SaveState
	private String sql = "\nAND 1=1";
	
	
	
	public void qry(String popQryKey) {
		if(StrUtils.isNull(popQryKey)){
			this.qrySql = "\nAND 1=1";
			return;
		}
		this.popQryKey = popQryKey;
		this.popQryKey = this.popQryKey.trim();
		
		if(StrUtils.isNull(this.popQryKey))this.qrySql = "\nAND 1=1";
		this.popQryKey = this.popQryKey.replaceAll("'", "''");
		this.popQryKey = this.popQryKey.toUpperCase();
		
		qrySql = 
			"\nAND " +
			"\n	(	" +
			"\n		 UPPER(customerabbr) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(name) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(customercode) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(customernamee) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(customeraddresse) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(contactxt) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(inputer) LIKE '%" + this.popQryKey + "%' " +
			"\n		 OR UPPER(updater) LIKE '%" + this.popQryKey + "%' " +
			"\n	 )";
	}
	
	
	
	/**
	 * 根据关键字查找客户
	 * @param qryCustomerKey
	 * @return
	 * @throws Exception
	 */
	public List<Map> findCustomer(String qryCustomerKey) throws Exception{
		qryCustomerKey = qryCustomerKey.toUpperCase();
		String querySql = 
			"\nSELECT " +
			"\n* " +
			"\nFROM _sys_corpcontacts a " +
			"\nWHERE isdelete = FALSE" +
			"\nAND " +
			"\n	(	" +
			"\n			UPPER(customerabbr) LIKE '%" + qryCustomerKey + "%' " +
			"\n		 OR UPPER(customercode) LIKE '%" + qryCustomerKey + "%' " +
			"\n		 OR UPPER(name) LIKE '%" + qryCustomerKey + "%' " +
			"\n		 OR UPPER(customernamee) LIKE '%" + qryCustomerKey + "%' " +
			"\n		 OR UPPER(customeraddresse) LIKE '%" + qryCustomerKey + "%' " +
			"\n		 OR UPPER(contactxt) LIKE '%" + qryCustomerKey + "%' " +
			"\n	 )";
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
			
		return list;
	}
	
	



	/**
	 * 通用弹窗查客户
	 * @return
	 */
	public GridDataProvider getCustomerDataProvider(){
		return getCustomerDataProvider(sql);
	}
	
	/**
	 * 
	 * @param extClauseWhere
	 * @return
	 */
	public GridDataProvider getCustomerDataProvider(final String extClauseWhere){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n*,convert(substring(name from '[\u4e00-\u9fa5A-Za-z]+')::bytea,'UTF-8','GBK') AS namecn " +//查出中英文，再按中英文排序
					"\nFROM _sys_corpcontacts a " +
					"\nWHERE isdelete = FALSE " + 
						extClauseWhere +
						qrySql +
					"\nORDER BY namecn,name,customerid"+
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
					"\nWHERE isdelete =false "+extClauseWhere+
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
