package com.scp.login_new;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.AppSessionLister;
import com.scp.base.UserSession;
import com.scp.dao.sys.SysUseronlineDao;
import com.scp.model.sys.SysUseronline;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "login_new.useronlineBean", scope = ManagedBeanScope.REQUEST)
public class UserOnLineBean extends GridView {
	
	@Bind
	@SaveState
	public String onlinenumber;
	
	@Bind
	@SaveState
	public String online;
	
	@Bind
	@SaveState
	public Integer sysuser;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		getnlinenumber();
	}

	public void getnlinenumber(){
		
		Map onlineSessionMap  = AppSessionLister.onlineSessionMap;
		
		Set<Long> set = onlineSessionMap.keySet();
		StringBuilder stringBuilder = new StringBuilder();
		for (Long key : set) {
			UserSession session = (UserSession) onlineSessionMap.get(key);
			String code = session.getUsercode();
			String name = session.getUsername();
			String id = session.getSessionid();
			
			code = code.substring(0, 1) + "**********";
			name = name.substring(0, 1) + "**********";
			
			stringBuilder.append("code["+ code + "]		name[" + name + "]\n");
			
			
		}
		onlinenumber = ""+AppSessionLister.getOnLineUser();
		online = stringBuilder.toString();
		sysuser = AppUtils.getSysUser();
	}

	@Action
	public void isfresh(){
		getnlinenumber();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void setisvalids(){
		
	}
	
	@Action
	public void del() {
		
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		Map onlineSessionMap  = AppSessionLister.onlineSessionMap;
		Set<Long> set = onlineSessionMap.keySet();
		StringBuilder stringBuilder = new StringBuilder();
		for (Long key : set) {
			UserSession session = (UserSession) onlineSessionMap.get(key);
			String sessionid = session.getSessionid();
			stringBuilder.append(" sessionid = '"+sessionid+"' \n OR");
		}
		if(stringBuilder.length() > 0){
			m.put("qry", stringBuilder.toString().substring(0,stringBuilder.length()-2));
		}else{
			m.put("qry", "1=2");
		}
		
		return m;
	}
	
	@Resource
	public SysUseronlineDao sysUseronlineDao;
	
	@Action
	public void removeuser(){
		String[] ids = grid.getSelectedIds();
		if (ids == null || ids.length <= 0 || ids.length > 1) {
			MessageUtils.alert("请选中一行!");
			return;
		}
		Long id = Long.valueOf(ids[0]);
		List<SysUseronline> sysUseronlines = sysUseronlineDao.findAllByClauseWhere("userid = " + id + " AND isonline = 'Y'");
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
		AppSessionLister.deleteSession(AppUtils.getHttpSession());
		AppUtils.getHttpSession().invalidate();
		MessageUtils.alert("OK!");
	}

}
