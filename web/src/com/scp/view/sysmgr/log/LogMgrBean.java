package com.scp.view.sysmgr.log;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.log.logmgrBean", scope = ManagedBeanScope.REQUEST)
public class LogMgrBean extends GridView {
	@Bind
	public UIWindow showlogmgrWindow;
	
	@SaveState
	@Accessible
	@Bind
	public String inputername;
	
	
	@SaveState
	@Accessible
	@Bind
	public String time;
	
	@SaveState
	@Accessible
	@Bind
	public String logMessage;
	
	@SaveState
	private String logtype;
	
	@Bind
	public UIButton manydelete;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			if(!StrUtils.isNull(AppUtils.getReqParam("logtype"))){
				logtype = AppUtils.getReqParam("logtype");
				if("O".equals(logtype)){
					manydelete.setDisabled(true);
				}
			}
			super.applyGridUserDef();
		}
	}
	
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		this.showlogmessage();
		this.update.markUpdate(UpdateLevel.Data, "masterPanel");
		showlogmgrWindow.show();
	}
	 
	public void showlogmessage(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "SELECT logdesc,logtime,inputer FROM sys_log WHERE id ='" + this.getGridSelectId()+"'";
		this.update.markUpdate(UpdateLevel.Data, "logMessage");
		this.update.markUpdate(UpdateLevel.Data, "time");
		this.update.markUpdate(UpdateLevel.Data, "inputername");
	}
	
	@Action
	public void manydelete() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		StringBuffer sql = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			sql.append("\ndelete from sys_log where id = '" +ids[i]+ "' AND isdelete = FALSE;");
		}
		MessageUtils.alert("OK!");
		this.refresh(); 
	}
	
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map =  super.getQryClauseWhere(queryMap);
		String code = AppUtils.getUserSession().getUsercode();
		String sql = "";
		if("demo".equals(code)){
			 //sql = "AND EXISTS(SELECT 1 FROM sys_log where logtype = 'BI' AND id = t.id)";
		}else{
			 sql += "AND NOT EXISTS(SELECT 1 FROM sys_log where (logtype = 'BI' OR logtype = 'DEBUG') AND id = t.id)";
		}
		if(!StrUtils.isNull(logtype)){
			sql += "AND EXISTS(SELECT 1 FROM sys_log where logtype = '"+logtype+"' AND id = t.id)";
		}else{
			sql += "AND NOT EXISTS(SELECT 1 FROM sys_log where logtype = 'web' AND id = t.id)";
		}
		if("O".equals(logtype)){
			if((AppUtils.getUserSession().getUsercode().equals("STEVEN JIANG"))||(AppUtils.getUserSession().getUsercode().equals("DICKY MIAO"))||(AppUtils.getUserSession().getUsercode().equals("demo"))){
			}else{
				sql += "AND EXISTS(SELECT 1 FROM sys_log where inputer = '"+AppUtils.getUserSession().getUsercode()+"' AND id = t.id)";
			}
		}
		
		map.put("filter", sql);
		
		String qry = StrUtils.getMapVal(map, "qry");
		if(dateastart != ""){
			qry  += "\nAND logtime::DATE BETWEEN '"
				+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
				+ "' AND '"
				+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
				+ "'";
		}
		map.put("qry", qry);
		return map;
	}
	
	@Override
	public void clearQryKey() {
		dateastart="";
		dateend="";
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
	}
}
