package com.scp.view.module.data;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.data.DatGoodsDao;
import com.scp.util.StrUtils;

@ManagedBean(name = "goodsstockBean", scope = ManagedBeanScope.SESSION)
public class GoodsStockBean{
	

	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	private String popQryKey;
	
	@SaveState
	private String qrySql = "\nAND 1=1";
	
	@Resource
	public DatGoodsDao datGoodsDao; 

	
	
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
			"\n			UPPER(code) LIKE '%" + this.popQryKey + "%' " +
			"\n	 )";
	}
	
	
	/**
	 * 通用弹窗查客户
	 * @return
	 */
	public GridDataProvider getgoodsDataProvider(){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT " +
					"\n* " +
					"\nFROM _dat_goods a " +
                    
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
					"\nFROM _dat_goods a " +
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
					"\nFROM _dat_goods a " +
					
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