package com.scp.view.module.finance.doc;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.NoRowException;
import com.scp.model.finance.fs.FsActset;
import com.scp.model.finance.fs.FsConfigRp;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.doc.configrpBean", scope = ManagedBeanScope.REQUEST)
public class ConfigRpBean extends GridView {

	@SaveState
	@Accessible
	public FsConfigRp selectedRowData = new FsConfigRp();
	
	
	@SaveState
	@Accessible
	public FsActset actset = new FsActset();
	
	@SaveState
	public Long actsetid;
	
	@SaveState
	public Long rpid;
	
	@Bind
	@SaveState
	public String year;

	@Bind
	@SaveState
	public String period;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			actsetid = AppUtils.getUserSession().getActsetid();
			if(actsetid == null) {
				MessageUtils.alert("未检测到帐套信息，请重新打开页面！");
				return;
			}
			actset = serviceContext.accountsetMgrService.fsActsetDao.findById(actsetid);
			String sql=" actsetid ="+actsetid+" AND year = "+actset.getYear()+" AND peroid = "+actset.getPeriod();
			try{
				selectedRowData= serviceContext.fsConfigRpMgrService.fsConfigRpDao.findOneRowByClauseWhere(sql);
			}catch(NoRowException e){				
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
		serviceContext.fsConfigRpMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK");
		this.update.markUpdate(true, UpdateLevel.Data, "gridPanel");
	}
	
	
	@Action
	public void showActdtl(){
		try {
			this.period = AppUtils.getReqParam("period");
			this.year = AppUtils.getReqParam("year");
			String sql = " actsetid =" + actsetid + " AND year = " + this.year
					+ " AND peroid = " + this.period;
			selectedRowData = serviceContext.fsConfigRpMgrService.fsConfigRpDao
					.findOneRowByClauseWhere(sql);
			this.rpid = selectedRowData.getId();
			this.update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		} catch (Exception e) {
			MessageUtils.alert("不存在这样的期间!");
		}
		
	}

}
