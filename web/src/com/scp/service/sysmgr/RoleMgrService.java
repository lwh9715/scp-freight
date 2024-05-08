package com.scp.service.sysmgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysModinroleDao;
import com.scp.dao.sys.SysModuleDao;
import com.scp.dao.sys.SysRoleDao;
import com.scp.dao.sys.SysUserDao;
import com.scp.dao.sys.SysUserinroleDao;
import com.scp.model.sys.SysModinrole;
import com.scp.model.sys.SysRole;
import com.scp.model.sys.SysUser;
import com.scp.model.sys.SysUserinrole;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class RoleMgrService{
	
	@Resource
	public SysUserDao sysUserDao;
	
	@Resource
	public SysRoleDao sysRoleDao;
	
	@Resource
	public SysModuleDao sysModuleDao;
	
	@Resource
	public SysUserinroleDao sysUserinroleDao;
	
	
	@Resource
	public SysModinroleDao sysModinroleDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	

	public void saveRole(SysRole instance) {
		sysRoleDao.createOrModify(instance);
	}


	public int[] qryRoleUserSelect(String roleid) {
		String sql = "pages.sysmgr.role.modroleBean.roleUserSelect";
		try {
			Map args = new HashMap();
			args.put("roleid", roleid);
			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sql , args);
			List<Integer> rowList = new ArrayList<Integer>();
			int rownum = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if((Boolean)map.get("flag"))rowList.add(rownum);
				rownum++;
			}
			int row[] = new int[rowList.size()];
			for (int i = 0; i < rowList.size(); i++) {
				row[i] = rowList.get(i);
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public void removeRoleUserSelect(String[] ids , String roleId) {
		String dmlSql = "\nDELETE FROM sys_userinrole WHERE id IN ("+StrUtils.array2List(ids)+");";
		sysUserinroleDao.executeSQL(dmlSql);
	}
	
	public void saveRoleUserSelect(String roleid, String[] ids) {
		SysRole sysRole = sysRoleDao.findById(Long.parseLong(roleid));
		for (int i = 0; i < ids.length; i++) {
			SysUserinrole sysUserinrole = new SysUserinrole();
			sysUserinrole.setSysUser(sysUserDao.findById(Long.parseLong(ids[i])));
			sysUserinrole.setSysRole(sysRole);
			sysUserinroleDao.createOrModify(sysUserinrole);
		}
		
//		String dmlSql = "\nDELETE FROM sys_userinrole WHERE roleid = "+roleid+";";
//		for (int i = 0; i < ids.length; i++) {
//				dmlSql += "\nINSERT INTO sys_userinrole(id,userid,roleid) VALUES(getid()," + ids[i]+","+roleid+");";
//		}
//		if(!StrTools.isNull(dmlSql)){
//			sysUserinroleDao.executeSQL(dmlSql);
//		}
	}

	public void removeRoleModSelect(String roleid) {
		String dmlSqlBefore = 
			"\nDELETE FROM sys_modinrole WHERE " +
			"\nmoduleid NOT IN (SELECT id FROM sys_module x WHERE (x.id = 1000 OR x.pid = 1000))"+
			"\nAND roleid = "
				+ roleid + ";";
		sysRoleDao.executeSQL(dmlSqlBefore);
	}
	
	public void removeRole(String roleid) {
		String sql = 
			"\nUPDATE sys_role SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + roleid + ";";
		sysRoleDao.executeSQL(sql);
	}

	public void saveRoleModSelect(String roleid, List<String> ids) {
		SysRole sysRole = sysRoleDao.findById(Long.parseLong(roleid));
		if(sysRole == null){
			SysUser sysUser = sysUserDao.findById(Long.parseLong(roleid));
			if(sysUser == null){
				return;
			}
			SysRole sysRoleNew = sysRoleDao.findByRoletypeC(roleid);
			if(sysRoleNew == null){
				sysRoleNew = new SysRole();
			}
			
			sysRoleNew.setCode(sysUser.getCode());
			sysRoleNew.setName(sysUser.getNamec());
			sysRoleNew.setRoletype("C");
			sysRoleDao.createOrModify(sysRoleNew);
			
			sysRole = sysRoleDao.findById(sysRoleNew.getId());
			saveRoleUserSelect(String.valueOf(sysRoleNew.getId()), new String[]{String.valueOf(sysUser.getId())});
			
		}
		for (String id : ids) {
			SysModinrole sysModinrole = new SysModinrole();
			sysModinrole.setSysRole(sysRole);
			sysModinrole.setSysModule(sysModuleDao.findById(Long.parseLong(id)));
			sysModinroleDao.createOrModify(sysModinrole);
		}
	} 
	
}
