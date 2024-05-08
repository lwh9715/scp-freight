package com.scp.dao.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysRole;
import com.scp.model.sys.SysUser;



/**
* 
 * @author neo
 */
@Component
public class SysRoleDao extends BaseDaoImpl<SysRole, Long>{
	
	@Resource
	public SysUserDao sysUserDao;
	
	protected SysRoleDao(Class<SysRole> persistancesClass) {
		super(persistancesClass);
	}

	public SysRoleDao() {
		this(SysRole.class);
	}

	public SysRole findByRoletypeC(String userid) {
		SysRole sysRole = null;
		try {
			SysUser sysUser = sysUserDao.findById(Long.parseLong(userid));
			sysRole = this.findOneRowByClauseWhere("roletype = 'C' AND code = '"+sysUser.getCode()+"' AND isdelete = false");
		} catch (NoRowException e) {
			//e.printStackTrace();
		}
		return sysRole;
	}
	
	public String findIdByRoletypeC(String userid) {
		SysRole sysRole = findByRoletypeC(userid);
		return String.valueOf(sysRole==null?"":sysRole.getId());
	}

	public String findIdByRoleTypeOrg(String linkid) {
		SysRole sysRole = null;
		try {
			sysRole = this.findOneRowByClauseWhere("linkid = "+linkid+" AND isdelete = false");
		} catch (NoRowException e) {
			//e.printStackTrace();
		}
		return String.valueOf(sysRole==null?"":sysRole.getId());
	}
}