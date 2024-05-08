package com.scp.view.module.finance.doc;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.finance.FinaConfigarap;
import com.scp.model.finance.fs.FsActset;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.finance.doc.configarapBean", scope = ManagedBeanScope.REQUEST)
public class ConfigarapBean extends GridView{
	
	@SaveState
	@Accessible
	public FinaConfigarap selectedRowData = new FinaConfigarap();
	
	
	@SaveState
	@Accessible
	public FsActset actset = new FsActset();
	
	@SaveState
	public Long actsetid;
	
	@SaveState
	public Long rpid;
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			actsetid = AppUtils.getUserSession().getActsetid();
			if(actsetid == null) {
				MessageUtils.alert("未检测到帐套信息，请重新打开页面！");
				return;
			}
			actset = serviceContext.accountsetMgrService.fsActsetDao.findById(actsetid);
			String sql=" actsetid ="+actsetid;
			try{
				selectedRowData= serviceContext.finaConfigarapService.finaConfigarapDao.findOneRowByClauseWhere(sql);
			}catch(Exception e){
			}
			this.rpid = selectedRowData.getId();
			this.update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		}
	}
	
	@Action
	public void config() {
		if(actsetid == null) {
			MessageUtils.alert("未检测到帐套信息，请重新打开页面！");
			return;
		}
		serviceContext.finaConfigarapService.saveData(selectedRowData);
		MessageUtils.alert("OK");
		this.update.markUpdate(true, UpdateLevel.Data, "gridPanel");
	}
}
