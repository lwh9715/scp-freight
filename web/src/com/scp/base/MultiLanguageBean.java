package com.scp.base;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;

import com.scp.base.LMapBase.MLType;

@ManagedBean(name="l", scope=ManagedBeanScope.REQUEST)
public class MultiLanguageBean {
	
	@ManagedProperty("#{lmap}")
	@Accessible
	public LMap m;
	
	
	public String find(String key){
		if(m == null)initLs(MLType.ch);
		return m.get(key) == null? key : String.valueOf(m.get(key));
	}
	
	public void initLs(MLType mlType){
		m.setMlType(mlType);
		m.initLocal(mlType);
//		daoIbatisTemplate = (DaoIbatisTemplate)AppUtil.getBeanFromSpringIoc("daoIbatisTemplate");
//		
//		if(mlType.equals(MLType.ch))return;        
//		
//		String querySql = "base.language.init";
//		try {
//			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(querySql);
//			for (Map map : list) {
//				String ch = StrTools.getMapVal(map, "ch");
//				String en = StrTools.getMapVal(map, "en");
//				String tw = StrTools.getMapVal(map, "tw");
//				
//				if("en".equals(mlType.name())){
//					m.put(ch, en);
//				}else{
//					m.put(ch, tw);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
}
