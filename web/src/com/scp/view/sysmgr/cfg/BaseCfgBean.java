package com.scp.view.sysmgr.cfg;

import java.util.*;

import org.operamasks.faces.annotation.SaveState;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

public abstract class BaseCfgBean {
	
	public BaseCfgBean() {
		super();
	}

	@SaveState
	private Map<String, String> cfgDataMap;

	protected void initData() throws Exception {
		this.cfgDataMap = new LinkedHashMap<String,String>();
		String str1 = getQuerySql();
		if (!StrUtils.isNull(str1)) {
			String str2 = getClauseWhere();
			if (StrUtils.isNull(str2)) return;
			str1 = getQuerySql() + getClauseWhere();
			
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
			List<Map> localList = daoIbatisTemplate.queryWithUserDefineSql(str1);
			if (localList != null) {
				Iterator localIterator = localList.iterator();
				while (localIterator.hasNext()) {
					Map<String,String> localMap = (Map) localIterator.next();
					this.cfgDataMap.put(localMap.get("key"), localMap.get("val"));
				}
			}
		}
	}

	protected abstract String getQuerySql();

	private String getClauseWhere() {
		Vector<String> localVector = defineCfgKey();
		if ((localVector == null) || (localVector.size() <= 0))
			return null;
		String str1 = "";
		Object localObject = localVector.iterator();
		while (((Iterator) localObject).hasNext()) {
			String str2 = (String) ((Iterator) localObject).next();
			str1 = str1 + "'" + str2 + "'" + ",";
		}
		str1 = str1.substring(0, str1.length() - 1);
		localObject = "\nAND key IN (" + str1 + ")";
		return (String) localObject;
	}


	protected abstract Vector<String> defineCfgKey();

	public Map<String, String> getCfgDataMap() {
		return this.cfgDataMap;
	}
}
