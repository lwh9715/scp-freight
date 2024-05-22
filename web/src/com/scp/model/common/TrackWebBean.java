package com.scp.model.common;

import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.dao.cache.CommonDBCache;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.common.trackWebBean", scope = ManagedBeanScope.REQUEST)
public class TrackWebBean extends FormView {
	
	@Resource
	public CommonDBCache commonDBCache;
	
	@Bind
	@SaveState
	public String jobid = "-1";
	
	@Bind
	@SaveState
	public String trackShipp;
	
	@Bind
	@SaveState
	public String cntno;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			String jobidStr = AppUtils.getReqParam("jobid");
			if(!StrUtils.isNull(jobidStr)){
				jobid=jobidStr;
				update.markUpdate(UpdateLevel.Data, "jobid");
				//getTrackShipping();
			}
		}
	}
	
	
	/**
	 * @return
	 */
	@Bind(id="cntnos")
    public List<SelectItem> getCntnos() {
    	try {
			return commonDBCache.getComboxItems("COALESCE(d.cntno,'')","COALESCE(d.cntno,'')","bus_ship_container AS d","WHERE d.cntno <> '' AND d.cntno IS NOT NULL AND parentid IS NULL AND jobid=" + jobid,"ORDER BY cntno");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Action
	public void track(){
		if(StrUtils.isNull(cntno)){
			MessageUtils.showMsg("Please choose container no!");
			return;
		}
	}
	
	@Action
	public void getTrackShipping(){
	}
}
