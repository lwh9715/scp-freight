package com.scp.service.sysmgr;

import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.base.LMapBase;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.CsUserDao;
import com.scp.dao.sys.SysCorporationDao;
import com.scp.dao.sys.SysDepartmentDao;
import com.scp.dao.sys.SysUserDao;
import com.scp.model.sys.CsUser;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysUser;
import com.scp.service.BaseUserMgrService;
import com.scp.util.AppUtils;
import com.scp.util.CommonUtil;
import com.scp.util.EncoderHandler;

@Component
public class UserMgrService{
	
	@Resource
	public SysUserDao sysUserDao;
	
	@Resource
	public CsUserDao csUserDao;
	
	@Resource
	public SysCorporationDao sysCorporationDao; 
	
	@Resource
	public SysDepartmentDao sysDepartmentDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	private BaseUserMgrService baseUserMgrService = new BaseUserMgrService();
	
	
	public String decrypt(String ciphertext, String secretkey) {
		return baseUserMgrService.decrypt(ciphertext, secretkey);
	}
	
	public Vector<String> encrypt(String pwd) {
		return baseUserMgrService.encrypt(pwd);
	}
	
	public boolean checkPwd(String ciphertext, String secretkey , String oldPWD) throws Exception {
		return baseUserMgrService.checkPwd(ciphertext, secretkey, oldPWD);
	}

	public void saveUser(SysUser data, Long corporationid, Long departmentid) {
		if(corporationid != null)data.setSysCorporation((SysCorporation) sysCorporationDao.findById(corporationid));
		if(departmentid != null)data.setSysDepartment((SysDepartment) sysDepartmentDao.findById(departmentid));
		if(0 == data.getId()){
			sysUserDao.create(data);
		}else{
			sysUserDao.modify(data);
		}
	} 
	
	public void saveUser(SysUser data) {
		if(0 == data.getId()){
			sysUserDao.create(data);
		}else{
			sysUserDao.modify(data);
		}
	} 
	
	public void saveCsUser(CsUser data) {
		if(0 == data.getId()){
			csUserDao.create(data);
		}else{
			csUserDao.modify(data);
		}
	} 
	
	public void rasCrypto(SysUser sysUser) throws Exception {
//		String newPWD1 = "ufms@147963";
//		Vector<String> retVector = baseUserMgrService.encrypt(newPWD1);
//		sysUser.setSecretkey(retVector.get(0));
//		sysUser.setCiphertext(retVector.get(1));
		String newPWD1 = "ufms@22969686";
		String salt = CommonUtil.getRandom(5);
		sysUser.setCiphertext(EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(newPWD1)+salt));
		sysUser.setSecretkey(salt);
	}

	public boolean checkPwd(SysUser user, String oldPWD) throws Exception {
		String ciphertext = user.getCiphertext();
		String secretkey = user.getSecretkey();
		
		String oldCiphertext = EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(oldPWD)+secretkey);
		if(ciphertext.equals(oldCiphertext)){
			return true;
		}else{
			return false;
		}
		
//		return baseUserMgrService.checkPwd(ciphertext, secretkey , oldPWD);
	}

	public void modifyPwd(String newPWD1, SysUser data) throws Exception {
//		String ciphertext = data.getCiphertext();
//		String secretkey = data.getSecretkey();
		
//		Vector<String> retVector = baseUserMgrService.encrypt(newPWD1);
//		data.setSecretkey(retVector.get(0));
//		data.setCiphertext(retVector.get(1));
		
		String salt = CommonUtil.getRandom(5);
		data.setCiphertext(EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(newPWD1)+salt));
		data.setSecretkey(salt);
		sysUserDao.modify(data);
	}
	
	public void modifyPwd(String newPWD1, CsUser data) throws Exception {
//		String ciphertext = data.getCiphertext();
//		String secretkey = data.getSecretkey();
		String salt = CommonUtil.getRandom(5);
		data.setCiphertext(EncoderHandler.encodeByMD5(EncoderHandler.encodeByMD5(newPWD1)+salt));
		data.setSecretkey(salt);
		csUserDao.modify(data);
	}

	public void removeUserOnLineDate(String ids) {
		String dmlSqlBefore = 
			"\nDELETE FROM sys_useronline WHERE " +
			"\nid  IN( "
				+ ids + ");";
		sysUserDao.executeSQL(dmlSqlBefore);
		
	}
	
	public void modifyUserOnLineDate(String ids, boolean is) {
		String dmlSqlBefore = 
			"\nUPDATE sys_useronline SET isvalid = "+is+" WHERE " +
			"\nid  IN( "
				+ ids + ");";
		sysUserDao.executeSQL(dmlSqlBefore);
	}

	public void modifyUserInvalid(String ids , boolean flag) {
		String dmlSqlBefore = 
			"\nUPDATE sys_user SET isinvalid = "+flag+" WHERE " +
			"\nid  IN( "
				+ ids + ");";
		sysUserDao.executeSQL(dmlSqlBefore);
	}

	public void delUser(String ids) {
		String dmlSqlBefore = 
			"\nUPDATE sys_user SET isdelete = true WHERE " +
			"\nid  IN( "
				+ ids + ");";
		sysUserDao.executeSQL(dmlSqlBefore);
	}
	
	public void delCsUser(String ids) {
		String dmlSqlBefore = 
			"\nDELETE FROM cs_user WHERE " +
			"\nid  IN( "
				+ ids + ");";
		csUserDao.executeSQL(dmlSqlBefore);
	}

	public void modifyUserSecurityLevel(String ids, int i) {
		String dmlSqlBefore = 
			"\nUPDATE sys_user SET securitylevel = "+i+" WHERE " +
			"\nid  IN( "
				+ ids + ");";
		sysUserDao.executeSQL(dmlSqlBefore);
	}
	
	/**
	 * @param userid
	 * @return
	 */
	public Long findWareHaousId(Long userid) {
		String querySql = "SELECT MAX(id) AS id FROM dat_warehouse WHERE id = ANY(SELECT linkid FROM sys_user_assign WHERE userid=" + userid + " AND linktype='W') AND isdelete = FALSE;";
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		Long id = (Long) map.get("id");
		if(id == null){
			querySql = "SELECT MIN(id) AS id FROM dat_warehouse WHERE isdelete = FALSE;";
			map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			id = (Long) map.get("id");
		}
		return id;
	}
	
	/**
	 * 查询网上账号对应的客户信息
	 * @return
	 */
	public String getCsUserInfo(Long id) {
		String querySql = "SELECT f_sys_user_getcsinfo('userid="+id+"') AS info;";
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
		return (String) map.get("info");
	}
	/**
	 * 获取公司名
	 * @param id
	 * @return
	 */
	public String getCorporationNameByUserId(Long id) {
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT");
		querySql.append("\n 	abbr");
		querySql.append("\n FROM");
		querySql.append("\n sys_corporation AS a");
		querySql.append("\n WHERE");
		querySql.append("\n 	a.isdelete = FALSE");
		querySql.append("\n AND a.iscustomer = FALSE");
		querySql.append("\n AND");
		querySql.append("\n 	a.id = (SELECT x.corpid FROM sys_user x WHERE x.id="+id+")");
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql.toString());
		return (String) map.get("abbr");
	}
	
	/**
	 * 获取部门名
	 * @param id
	 * @return
	 */
	public String getDepartNameByUserId(Long id) {
		MLType mlType = AppUtils.getUserSession().getMlType();
		StringBuffer querySql = new StringBuffer();
		querySql.append("SELECT");
		querySql.append(mlType.equals(LMapBase.MLType.en)?"\n COALESCE(namee,name) AS name":"\n name");
		querySql.append("\n FROM");
		querySql.append("\n sys_department AS a");
		querySql.append("\n WHERE");
		querySql.append("\n a.isdelete = FALSE");
		querySql.append("\n AND EXISTS(SELECT 1 FROM sys_user x WHERE a.id = x.deptid AND x.id="+id+")");
		Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql.toString());
		return (String) map.get("name");
	}
}
