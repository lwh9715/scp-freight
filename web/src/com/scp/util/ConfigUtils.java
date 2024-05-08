package com.scp.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.scp.dao.BaseDaoImpl;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.cache.CommonDBCache;
import com.scp.exception.NoRowException;

/**
 * @author Neo
 */
public class ConfigUtils {
	
	/**
	 * 先到个人设置中找，个人设置中没有再到系统设置中找配置
	 * @param cfgKey
	 * @return
	 */
	public static String findUserSysCfgVal(String cfgKey , Long userid) {
		CommonDBCache commonDBCache = (CommonDBCache)AppUtils.getBeanFromSpringIoc("commonDBCache");
		String val = commonDBCache.findUserCfgVal(cfgKey , userid);
		if(StrUtils.isNull(val)){
			val = commonDBCache.findSysCfgVal(cfgKey);
		}
		return val;
	}
	
	public static String findSysCfgVal(String cfgKey) {
		CommonDBCache commonDBCache = (CommonDBCache)AppUtils.getBeanFromSpringIoc("commonDBCache");
		return commonDBCache.findSysCfgVal(cfgKey);
	}

	public static Map findSysCfgVals(String[] cfgKeys) {
		HashMap map = new HashMap();
		if ((cfgKeys == null) || (cfgKeys.length < 1)){
			return map;
		}
		String key = "";
		for (String cfgKey : cfgKeys)
			key = key + "'" + (String) cfgKey + "'" + ",";
		int i = key.lastIndexOf(",");
		key = key.substring(0, i);
		String sql = 
			"\nSELECT * " + 
			"\nFROM sys_config "+ 
			"\nWHERE key IN(" + key + ");";
		try {
//			List<Map> list = AppDaoUtil.query(sql);
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
			
			for (Map localMap : list) {
				map.put(localMap.get("key"), localMap.get("val"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String findUserCfgVal(String key , Long userid) {
		Map localMap = findUserCfgVals(new String[] { key } , userid);
		return localMap.get(key) == null ? null : String
				.valueOf(localMap.get(key));
	}

	public static Map findUserCfgVals(String[] keys , Long userid) {
		HashMap localHashMap = new HashMap();
		if ((keys == null) || (keys.length < 1)){
			return localHashMap;
		}
		String key = "";
		for (String str2 : keys){
			key = key + "'" + str2 + "'" + ",";
		}
		int i = key.lastIndexOf(",");
		key = key.substring(0, i);
		String sql = 
			"SELECT * " + 
			"\nFROM sys_configuser "+ 
			"\nWHERE key IN(" + key + ")"+ 
			"\nAND userid = " + userid;
		try {
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) ApplicationUtilBase.getBeanFromSpringIoc("daoIbatisTemplate");
			List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
			
			for (Map map : list) {
				localHashMap.put(map.get("key"), map.get("val"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localHashMap;
	}

	public static enum UsrCfgKey {
		grid_page_size,
		view_tip_color,
		
		email_srv_smtp,
		email_srv_pop3,
		email_srv_port,
		email_pop3_account,
		email_pop3_pwd,
		
		desk_back_img,
		;
	}

	public static enum SysCfgKey {
		rpt_srv_url,
		login_leftimg,
		login_logo,
		dbbackup_dbname,
		dbbackup_username,
		dbbackup_backuppath,
		dbbackup_dbpath,
		dbbackup_time,
		dbbackup_filecount
	}


	public static void refreshUserCfg(String cfgKey , String cfgVal , Long userid) throws Exception{
		Map<String , String> map = new HashMap<String, String>();
		map.put(cfgKey, cfgVal);
		refreshUserCfg(map , userid);
	}
	
	public static void refreshSysCfg(String cfgKey , String cfgVal , Long userid) throws Exception{
		Map<String , String> map = new HashMap<String, String>();
		map.put(cfgKey, cfgVal);
		refreshSysCfg(map , userid);
	}
	public static void refreshRobotMap(String cfgKey , String cfgVal , Long userid) throws Exception{
		Map<String , String> map = new HashMap<String, String>();
		map.put(cfgKey, cfgVal);
		refreshRobotCfg(map , userid);
	}
	
	public static void refreshUserCfg(Map<String , String> map , Long userid) throws Exception{
		Set<String> set = map.keySet();
		String dmlSql = 
			"\nINSERT INTO sys_configuser(id,userid,key,val,inputer,inputtime) " +
			"\nSELECT getid() , %s, '%s', null ,%s,NOW()" +
			"\nFROM _virtual " +
			"\nWHERE NOT EXISTS(" +
			"\n\t\t\t\t\t\t\t\t	SELECT 1 " +
			"\n\t\t\t\t\t\t\t\t	FROM sys_configuser " +
			"\n\t\t\t\t\t\t\t\t	WHERE userid=%s " +
			"\n\t\t\t\t\t\t\t\t	AND key='%s' " +
			"\n\t\t\t\t\t\t\t\t	);" +
			"\nUPDATE sys_configuser SET val = '%s' , updater = '%s' , updatetime = NOW() WHERE userid=%s AND key = '%s' AND COALESCE(val,'') <> '%s';";
		
		String exeSql = "";
		
		for (String key : set) {
			String cfgKey = key;
			String cfgVal = map.get(key);
			if(cfgVal == null || "null".equals(cfgVal)){
				cfgVal = "";
			}
			cfgVal = cfgVal.trim();
			exeSql += String.format(dmlSql, userid , cfgKey , userid , userid , cfgKey ,  cfgVal, userid , userid , cfgKey ,  cfgVal);
		}
		BaseDaoImpl sysUserDao = (BaseDaoImpl) ApplicationUtilBase.getBeanFromSpringIoc("sysUserDao");
		sysUserDao.executeSQL(exeSql);
	}
	
	public static void refreshSysCfg(Map<String , String> map , Long userid) throws Exception{
		Set<String> set = map.keySet();
		String dmlSql = 
			"\nINSERT INTO sys_config(id,key,val,inputer,inputtime) " +
			"\nSELECT getid() ,'%s', null ,%s,NOW() " +
			"\nFROM _virtual " +
			"\nWHERE NOT EXISTS(" +
			"\n\t\t\t\t\t\t\t\t SELECT 1 " +
			"\n\t\t\t\t\t\t\t\t FROM sys_config " +
			"\n\t\t\t\t\t\t\t\t	WHERE key='%s' " +
			"\n\t\t\t\t\t\t\t\t	);" +
			"\nUPDATE sys_config SET val = '%s' , updater = '%s' , updatetime = NOW() WHERE key = '%s' AND COALESCE(val,'') <> '%s';";
		
		String exeSql = "";
		
		for (String key : set) {
			String cfgKey = key;
			String cfgVal = map.get(key);
			if(cfgVal == null || "null".equals(cfgVal)){
				cfgVal = "";
			}
			cfgVal = cfgVal.trim();
			exeSql +=  String.format(dmlSql,cfgKey , userid , cfgKey , cfgVal , userid , cfgKey , cfgVal);
		}
		BaseDaoImpl sysUserDao = (BaseDaoImpl) ApplicationUtilBase.getBeanFromSpringIoc("sysUserDao");
		sysUserDao.executeSQL(exeSql);
	}
	public static void refreshRobotCfg(Map<String , String> map , Long userid) throws Exception{
		Set<String> set = map.keySet();
		String dmlSql = 
			"\nINSERT INTO api_robot_config(id,userid,key,val,inputer,inputtime) " +
			"\nSELECT getid() , %s, '%s', null ,%s,NOW()" +
			"\nFROM _virtual " +
			"\nWHERE NOT EXISTS(" +
			"\n\t\t\t\t\t\t\t\t	SELECT 1 " +
			"\n\t\t\t\t\t\t\t\t	FROM api_robot_config " +
			"\n\t\t\t\t\t\t\t\t	WHERE userid=%s " +
			"\n\t\t\t\t\t\t\t\t	AND key='%s' " +
			"\n\t\t\t\t\t\t\t\t	);" +
			"\nUPDATE api_robot_config SET val = '%s' , updater = '%s' , updatetime = NOW() WHERE userid=%s AND key = '%s' AND COALESCE(val,'') <> '%s';";
		
		String exeSql = "";
		
		for (String key : set) {
			String cfgKey = key;
			String cfgVal = map.get(key);
			if(cfgVal == null || "null".equals(cfgVal)){
				cfgVal = "";
			}
			cfgVal = cfgVal.trim();
			exeSql += String.format(dmlSql, userid , cfgKey , userid , userid , cfgKey ,  cfgVal, userid , userid , cfgKey ,  cfgVal);
		}
		BaseDaoImpl sysUserDao = (BaseDaoImpl) ApplicationUtilBase.getBeanFromSpringIoc("sysUserDao");
		sysUserDao.executeSQL(exeSql);
	}
	
	public static String findRobotCfgVal(String cfgKey) {
		String val = "";
		if (StrUtils.isNull(cfgKey)){
			return "";
		}
		String key =  "'" + cfgKey + "'";
		String sql = 
			"\nSELECT * " + 
			"\nFROM api_robot_config "+ 
			"\nWHERE key = " + key +
			"\nAND val IS NOT NULL AND val <> ''" +
			"\nORDER BY id LIMIT 1";
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
	
	public static String findRobotCfgVal(String cfgKey,String usercode) {
		String val = "";
		String usersql ="(select id from sys_user where code='"+usercode+"' and isdelete = FALSE limit 1)";
		if (StrUtils.isNull(cfgKey)){
			return "";
		}
		String key =  "'" + cfgKey + "'";
		String sql = 
			"\nSELECT * " + 
			"\nFROM api_robot_config "+ 
			"\nWHERE key = " + key +
			"\nAND userid =" + usersql +
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
