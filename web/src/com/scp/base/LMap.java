package com.scp.base;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.dao.cache.CommonDBCache;
import com.scp.exception.NoSessionException;
import com.scp.service.sysmgr.SysMlService;
import com.scp.util.AppUtils;

@ManagedBean(name="lmap", scope=ManagedBeanScope.APPLICATION)
public class LMap extends LMapBase{

	
	@Resource
	public CommonDBCache commonDBCache;
	
	@Resource
	private SysMlService sysMlService;
	
	public LMap() {
		super();
	}

	@Override
	public Object get(Object key) {
		Object val;
		MLType current = super.getMlType();
		try {
			if(AppUtils.getUserSession() != null && AppUtils.getUserSession().getMlType() != null){
				current = AppUtils.getUserSession().getMlType();
			}
		} catch (NoSessionException e) {
		}
		
		if(current.equals(MLType.ch)){
			//this.put(key, key);
			if(sysMlService == null) {
				sysMlService = (SysMlService) AppUtils.getBeanFromSpringIoc("sysMlService");
			}
			if(!AppUtils.isReadOnlyDB && AppUtils.isAutoFillLs) {
				sysMlService.saveAutoFill(key.toString());
			}
			val = key;
		}else {
			Object obj = super.get(key);
			if(obj != null) {
				return obj;
			}
			if(sysMlService == null) {
				sysMlService = (SysMlService) AppUtils.getBeanFromSpringIoc("sysMlService");
			}
			if(!AppUtils.isReadOnlyDB && AppUtils.isAutoFillLs) {
				sysMlService.saveAutoFill(key.toString());
			}
			
			val = commonDBCache.getLs(key.toString());
		}
		return val;
	}

	public void initLocal(MLType mlType) {
		if("en".equals(mlType.name())){
//			this.put("登录", "Login");
//			this.put("用户名", "User");
//			this.put("密码", "Password");
//			this.put("记住我", "Reme");
//			this.put("语言", "Language");
//			this.put("建议1024×768像素以上,推荐使用浏览器:", "Recommended 1024 × 768 pixels or more, recommended to use:");
//			this.put("如果有任何问题,请联系", "If you have any questions, please contact");
//			this.put("验证码", "Code");
//			this.put("刷新", "Refresh");
//			this.put("刷新验证码", "Refresh Code");
//			this.put("请输入验证码", "Please input ode");
//			this.put("请选择语言", "Please choose language");
//			this.put("请输入账号", "Please input account");
//			this.put("请输入密码", "Please input password");
//			this.put("航迅货代管理系统", "Hangxun freight management system");
//			this.put("航迅软件-企业版", "Hangxun - Enterprise Edition Software");
//			this.put("系统为货运代理公司量身定制，专业，易用，高效", "EL System is tailored for the freight Proxy Companies, Professional, Easy, Efficient");
//			this.put("简介", "Introduction");
//			this.put("功能模块", "Functions&Modules");
//			
//			this.put("包含海运空运，结算管理，财务管理，统计分析等功能", "Including FCL operation, Settlement management, Financial management, Statistical analysis and other functions");
		}else{
			//this.put("登录", "登陸");
			//this.put("用户名", "用戶名");
			//this.put("密码", "密碼");
			//this.put("语言", "語言");
		}
	}
}
