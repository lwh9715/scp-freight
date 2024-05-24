package com.scp.view.sysmgr.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.AppSessionLister;
import com.scp.dao.sys.SysUseronlineDao;
import com.scp.model.sys.SysUseronline;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.user.useronlineBean", scope = ManagedBeanScope.REQUEST)
public class UserOnLineBean extends GridView {
	
	@Bind
	@SaveState
	private String onlinenumber;
	
	@Bind
	@SaveState
	private Integer sysuser;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		// TODO Auto-generated method stub
		super.beforeRender(isPostBack);
		getnlinenumber();
	}

	public void getnlinenumber(){
		Map onlineSessionMap  = AppSessionLister.onlineSessionMap;
		int count  = onlineSessionMap.size();
		onlinenumber = ""+count;
		
		sysuser = AppUtils.getSysUser();
	}

	@Action
	public void isfresh(){
		getnlinenumber();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Resource
	public SysUseronlineDao sysUseronlineDao;
	
	@Action
	public void setisvalids(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		serviceContext.userMgrService.modifyUserOnLineDate(StrUtils.array2List(ids), false);
		Long id = Long.valueOf(ids[0]);
		List<SysUseronline> sysUseronlines = sysUseronlineDao.findAllByClauseWhere("id = " + id + " AND isonline = 'Y'");
		if(sysUseronlines != null && sysUseronlines.size() > 0) {
			for (SysUseronline sysUseronline : sysUseronlines) {
				sysUseronline.setIsonline("N");
				sysUseronline.setLogouttime(new Date());
				sysUseronline.setIsvalid(false);
				sysUseronlineDao.modify(sysUseronline);
				
				Map onlineSessionMap  = AppSessionLister.onlineSessionMap;
				onlineSessionMap.remove(sysUseronline.getUserid());
				AppSessionLister.sessionMap.remove(sysUseronline.getSessionid());
			}
		}
		MessageUtils.alert("OK!");
		this.refresh();
	}
	
	
	
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		
		serviceContext.userMgrService.removeUserOnLineDate(StrUtils.array2List(ids));
		this.refresh();
	}

}
