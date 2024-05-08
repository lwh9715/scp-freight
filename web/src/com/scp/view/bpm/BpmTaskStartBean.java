package com.scp.view.bpm;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.module.formdefine.DynamicFormBean;

/**
 * 
 * 流程发起页面（动态form） 
 * @author neo
 */
@ManagedBean(name = "bpm.bpmtaskstartBean", scope = ManagedBeanScope.REQUEST)
public class BpmTaskStartBean extends DynamicFormBean{
	
	
	@Bind
	@SaveState
	public String processid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			processid = AppUtils.getReqParam("processid");
			this.update.markUpdate(UpdateLevel.Data, "processid");
		}
	}
	
	@Action
	public void next(){
		try{
			String sqlsub = "SELECT f_bpm_processStart('processid="+this.processid+";userid="+AppUtils.getUserSession().getUserid()+"') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub.toString());
			String sub =  sm.get("rets").toString();
			MessageUtils.alert("OK");
			Browser.execClientScript("parent","closeTaskWindows()");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Action
	public void last(){
		this.refresh();
	}
}
