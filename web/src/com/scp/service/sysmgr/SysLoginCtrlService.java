package com.scp.service.sysmgr;

import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.base.LMapBase.MLType;
import com.scp.dao.sys.SysLoginctrlDao;
import com.scp.dao.sys.SysUserDao;
import com.scp.exception.LoginException;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysLoginctrl;
import com.scp.model.sys.SysUser;
import com.scp.service.LoginService;
import com.scp.util.DesUtil;
import com.scp.util.StrUtils;

@Component
public class SysLoginCtrlService{
	
	@Resource
	public SysLoginctrlDao sysLoginctrlDao;
	
	@Resource
	public SysUserDao sysUserDao;
	
	@Resource
	public LoginService loginService; 
	
	public void saveData(SysLoginctrl data) {
		if(0 == data.getId()){
			sysLoginctrlDao.create(data);
		}else{
			sysLoginctrlDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysLoginctrl data = sysLoginctrlDao.findById(id);
		sysLoginctrlDao.remove(data);
	}

	public void login(SysLoginctrl data, MLType mlType) {
		String username = data.getUsername();
		String encryptPassword = data.getPassword();
		
		//System.out.println("解密前："+encryptPassword);
		
		String decryPassword = "";
        try {
        	String SKEY = "UFMSufms";
            Charset CHARSET = Charset.forName("UTF-8");
            decryPassword = DesUtil.decrypt(encryptPassword, CHARSET, SKEY);
            //System.out.println("解密后："+decryPassword);
            data.setPassword(decryPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		if(StrUtils.isNull(username)) {
			throw new LoginException("用户名为空");
		}
		SysUser sysUser;
		try {
			sysUser = sysUserDao.findOneRowByClauseWhere("UPPER(code) = '" + username.toUpperCase() + "'");
		} catch (NoRowException e) {
			throw new LoginException("用户名不存在" + "["+username+"]");
		} catch (MoreThanOneRowException e) {
			throw new LoginException("用户名重复" + "["+username+"]");
		}
		
		
//			loginService.loginView(data.getUsername(), data.getPassword(),
//					mlType);
		if(3 != sysUser.getSecuritylevel()) {//3级为无限制级别，3以下都需要通过客户端登陆 或者是网络客户系统用户，不检查
			//1.check
			List<SysLoginctrl> sysLoginctrls = sysLoginctrlDao.findAllByClauseWhere(
					"mac = '"+data.getMac()+"' AND cpuid = '"+data.getCpuid()+"' AND diskid = '"+data.getDiskid()+"'");
			if(sysLoginctrls == null || sysLoginctrls.size() != 1) {
				
				data.setApplyuser(data.getUsername());
				data.setApplytime(data.getLastlogintime());
				
				saveData(data);
				throw new LoginException("未开通，已提交申请，请联系管理员授权开通");
			}else {
				SysLoginctrl find = sysLoginctrls.get(0);
				if(true == find.getIsallow()) {
					find.setLastloginusr(data.getLastloginusr());
					find.setLastlogintime(data.getLastlogintime());
					saveData(find);
					loginService.loginView(data.getUsername() , data.getPassword() , mlType);
				}else {
					throw new LoginException("已申请，但还未授权开通，请联系管理员！");
				}
			}
		}else{
			loginService.loginView(data.getUsername() , decryPassword , mlType);
		}
		
	}

	public void modifyUserSecurityAllow(String ids, boolean isallow) {
		String dmlSqlBefore = 
			"\nUPDATE sys_loginctrl SET isallow = "+isallow+" WHERE " +
			"\nid  IN( "
				+ ids + ");";
		sysUserDao.executeSQL(dmlSqlBefore);
	} 
}
