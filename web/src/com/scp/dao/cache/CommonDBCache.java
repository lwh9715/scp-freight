package com.scp.dao.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.scp.base.LMap;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysMl;
import com.scp.service.sysmgr.SysMlService;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.StrUtils;



@Component
public class CommonDBCache{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	//@Cacheable(value="getComboxItemsAsJson")
	public String getComboxItemsAsJson(String valCol,
			String tblName, String whereSql , String orderSql) throws Exception {
		String comboSqlFormat = "base.combox.qry.json";
		Map args = new HashMap();
		args.put("c", valCol);
		args.put("t", tblName);
		args.put("w", whereSql);
		args.put("o", orderSql);
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		////System.out.println("getComboxItemsAsJson无缓存的时候调用这里:"+args); 
    	List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(comboSqlFormat,args);
    	if(list != null && list.size() != 0){
    		return StrUtils.getMapVal(list.get(0), "json");
    	}
		return "";
	}

	
	public JSONArray getComboxItemsAsJsonArray(String valCol, String labCol,
			String tblName, String whereSql , String orderSql) throws Exception {
		String comboSqlFormat = "base.combox.qry";
		Map args = new HashMap();
		args.put("v", valCol);
		args.put("l", labCol);
		args.put("t", tblName);
		args.put("w", whereSql);
		args.put("o", orderSql);
		List<SelectItem> items = new ArrayList<SelectItem>();
		////System.out.println("getComboxItemsAsJsonArray无缓存的时候调用这里:"+args); 
    	List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(comboSqlFormat,args);
    	
    	JSONArray jsonArray = new JSONArray();
		JSONObject jsonObjectrRow = new JSONObject();
		
    	if(list!=null){
    		Object value = null;
    		Object lable = null;
    		
    		for (Map dept : list) {
    			JSONObject jsonObject = new JSONObject();
    			jsonObject.accumulate("text", dept.get("lable"));
    			jsonObject.accumulate("value", dept.get("value"));
    			
    			jsonArray.add(jsonObject);
    		}
    	}
		return jsonArray;
	}
	
	
	public List<SelectItem> getComboxItems(String valCol, String labColEn, String labColCh,
			String tblName, String whereSql , String orderSql) throws Exception {
		return getComboxItems(valCol, AppUtils.getColumnByCurrentLanguage(labColCh,labColEn), tblName, whereSql, orderSql);
	}
	
	//@Cacheable(value="getComboxItems")
	public List<SelectItem> getComboxItems(String valCol, String labCol,
			String tblName, String whereSql , String orderSql) throws Exception {
		String comboSqlFormat = "base.combox.qry";
		Map args = new HashMap();
		args.put("v", valCol);
		args.put("l", labCol);
		args.put("t", tblName);
		args.put("w", whereSql);
		args.put("o", orderSql);
		////System.out.println("getComboxItems无缓存的时候调用这里:"+args); 
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate)AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		List<SelectItem> items = new ArrayList<SelectItem>();
    	List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(comboSqlFormat,args);
    	if(list!=null){
    		Object value = null;
    		Object lable = null;
    		for (Map dept : list) {
    			lable = dept.get("lable");
    			value = dept.get("value");
				items.add(new SelectItem(String.valueOf(value),
						String.valueOf(lable)));
    		}	
    	}
		return items;
	}

	//@Cacheable(value="getGridUserDef")
	public String getGridUserDef(Long userid, String gridid, String gridJsvar){
		String querySql = "SELECT colkey,colwidth,ishidden FROM sys_griddef WHERE gridid='%s' AND userid=%s ORDER BY id DESC LIMIT 1;";
		querySql = String.format(querySql, gridid,userid);
		////System.out.println("getGridUserDef无缓存的时候调用这里:"+userid+"~"+gridid+"~"+gridJsvar);
		Map m = null;
		try{
			m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		}catch(NoRowException e){
		}
		String js = "";
		if(m!=null&&m.size()>0){
			String colkey = StrUtils.getMapVal(m, "colkey");
			String colwidth = StrUtils.getMapVal(m, "colwidth");
			String ishidden = StrUtils.getMapVal(m, "ishidden");
			js = "applyGridUserDef('"+colkey+"','"+colwidth+"','"+ishidden+"',"+gridJsvar+");";
		}
		return js;
	}
	
	@CacheEvict(value="getGridUserDef",allEntries=true)
	public void clearCacheGridUserDef() {
	}
	
//	@CacheEvict(value={"getComboxItems","getComboxItemsAsJson"},allEntries=true)
//	public void clearCacheComboxItems() {
//	}

	@CacheEvict(value="cacheCombox",allEntries=true,beforeInvocation=true)
	public void clearCacheComboxItems() {
		
	}

	//@Cacheable(value="getYear")
	public List<SelectItem> getYear() {
		List<SelectItem> years = new ArrayList<SelectItem>(90);
		for(int i=2015;i<2050;i++) {
			years.add(new SelectItem(i,i+""));
		}
		return years;
	}
	
	public List<SelectItem> getWeek() {
		List<SelectItem> weeks = new ArrayList<SelectItem>(90);
		for(int i=1;i<53;i++) {
			weeks.add(new SelectItem(i,i+""));
		}
		return weeks;
	}

	//@Cacheable(value="getPeriod")
	public List<SelectItem> getPeriod() {
		List<SelectItem> periods = new ArrayList<SelectItem>(12);
		for(int i=1;i<13;i++) {
			periods.add(new SelectItem(i,i+""));
		}
		return periods;
	}
	
	//@Cacheable(value="getActsetcode")
	public String getActsetcode(Long actsetid){
		////System.out.println("getActsetcode无缓存的时候调用这里:"+actsetid); 
		LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
		String tips = (String)l.get("请先选择帐套!");// "请先选择帐套!";
		String sql = "\nSELECT "
				+ "\n(name "
				+ "\n|| '['|| (SELECT COALESCE(x.code,'') ||'/'|| COALESCE(x.abbr,'') FROM sys_corporation x where x.id = t.corpid) || ']' "
				+ "\n|| '['|| COALESCE(year,0) ||']' "
				+ "\n|| '['|| COALESCE(period,0) ||'][' "
				+ "\n|| (CASE WHEN istart THEN '"+(String)l.get("启用")+"' ELSE '"+(String)l.get("未启用")+"' END)||']') AS name "
				+ "\nFROM fs_actset t " + "\nWHERE id =" + actsetid
				+ " AND isdelete = false ";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String name = (String) StrUtils.getMapVal(m, "name");
		if(StrUtils.isNull(name)){
			return tips;
		}
		return  name;
	}
	//@Cacheable(value="getActsetcode2")
	public String getActsetcode2(Long actsetid) {
		LMap l = (LMap)AppUtils.getBeanFromSpringIoc("lmap");
		String tips = (String)l.get("请先选择帐套!");
		////System.out.println("getActsetcode2无缓存的时候调用这里:"+actsetid); 
		String sql = "SELECT name AS name FROM fs_actset WHERE id ="
				+ actsetid + " AND isdelete = false ";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String name = (String) StrUtils.getMapVal(m, "name");
		if(StrUtils.isNull(name)){
			return tips;
		}
		return name;
	}

	//@Cacheable(value="getUserRoleModuleCtrl")
	/**
	 * 授权后有点问题，暂时不加缓存 neo 20170726
	 * @param modulepid
	 * @param userid
	 * @return
	 */
	public List<String> getUserRoleModuleCtrl(Long modulepid, Long userid) {
		////System.out.println("getUserRoleModuleCtrl无缓存的时候调用这里:"+userid+"~"+userid); 
		StringBuilder sb = new StringBuilder();
		sb.append("\n SELECT code FROM sys_module a WHERE a.isdelete =FALSE");
		sb.append("\n AND EXISTS (");
		sb.append("\n 	SELECT 1 FROM sys_modinrole mr WHERE mr.isdelete = FALSE");
		sb.append("\n 	AND EXISTS (SELECT 1 FROM sys_role r WHERE");
		sb.append("\n 				r.isdelete = FALSE");
		sb.append("\n 				AND EXISTS (SELECT 1 FROM sys_userinrole a WHERE a.isdelete = FALSE AND a.userid="
						+ userid
						+ " AND a.roleid = r.id)");
		sb.append("\n 				AND r.id = mr.roleid");
		sb.append("\n 	)AND mr.moduleid = a.id");
		sb.append("\n )AND a.isleaf = 'Y'");
		sb.append("\n AND a.isctrl = 'Y'");
		sb.append("\n AND a.pid=" + modulepid);
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sb.toString());
		// ////System.out.println("按钮权限个数:"+list.size());
		List<String> roleList = new ArrayList<String>();
		for (Map s : list) {
			roleList.add(s.get("code").toString());
		}
		return roleList;
	}
	
	//@Cacheable(value="getLs")
	public String getLs(String key) {
		//System.out.println("getLs无缓存的时候调用这里:"+key); 
		String val = "";
		SysMlService sysMlService = (SysMlService) AppUtils.getBeanFromSpringIoc("sysMlService");
		List<SysMl> sysMls = sysMlService.findByCh(key);
		if(sysMls == null || sysMls.size() != 1) {
			val = key;
		}else {
			SysMl sysMl = sysMls.get(0);
			String en = sysMl.getEn();
			if(StrUtils.isNull(en)) {
				val = key;
			}else {
				val = en;
			}
		}
		return val;
	}
	
	@Cacheable(value="cacheCommon")
	public String findSysCfgVal(String cfgKey) {
		String val = "";
		if (StrUtils.isNull(cfgKey)){
			return "";
		}
		String key =  "'" + cfgKey + "'";
		String sql = 
			"\nSELECT * " + 
			"\nFROM sys_config "+ 
			"\nWHERE key = " + key +
			"\nORDER BY id DESC LIMIT 1";
		try {
//			List<Map> list = AppDaoUtil.query(sql);
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			val = StrUtils.getMapVal(map, "val");
		} catch (NoRowException e){
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
	
	@Cacheable(value="cacheCommon")
	public String findUserCfgVal(String keys , Long userid) {
		String val = "";
		if (StrUtils.isNull(keys)){
			return "";
		}
		String key =  "'" + keys + "'";
		String sql = 
			"SELECT * " + 
			"\nFROM sys_configuser "+ 
			"\nWHERE key = " + key + ""+ 
			"\nAND userid = " + userid+
			"\nORDER BY id DESC LIMIT 1";
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			val = StrUtils.getMapVal(map, "val");
		} catch (NoRowException e){
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
}
