package com.scp.view.base;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.base.AppSessionLister;
import com.scp.dao.sys.SysUseronlineDao;
import com.scp.model.sys.SysUseronline;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "main.win10exitBean", scope = ManagedBeanScope.REQUEST)
public class Win10exitBean extends GridView{
	
	@Resource
	public SysUseronlineDao sysUseronlineDao;
	
	@Action
	public String exitAjax() {
		List<SysUseronline> sysUseronlines = sysUseronlineDao.findAllByClauseWhere("sessionid = '" + AppUtils.getUserSession().getSessionid() + "' AND isonline = 'Y'");
		if(sysUseronlines != null && sysUseronlines.size() > 0) {
			for (SysUseronline sysUseronline : sysUseronlines) {
				sysUseronline.setIsonline("N");
				sysUseronline.setLogouttime(new Date());
				sysUseronline.setIsvalid(false);
				sysUseronlineDao.modify(sysUseronline);
			}
		}
		AppSessionLister.deleteSession(AppUtils.getHttpSession());
		AppUtils.getHttpSession().invalidate();
		return "view:redirect:login";
	}
}
