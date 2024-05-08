package com.scp.view.sysmgr.timetask;

import org.apache.commons.beanutils.BeanUtils;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.SysTimeTask;
import com.scp.schedule.ext.ScheduleMgr;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.timetask.systimetaskBean", scope = ManagedBeanScope.REQUEST)
public class SysTimeTaskBean extends GridFormView {
	
	@Bind
	private UIIFrame dtlIFrame;
	
	@Bind
	private UIIFrame cronIFrame;
	
	@SaveState
	public SysTimeTask data;
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String url = "./systimetasklog.xhtml?id=" + "-1";
			dtlIFrame.load(url);
			this.update.markUpdate(UpdateLevel.Data, "dtlIFrame");
		}
	}
	
	
	
	
	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.sysTimeTaskService.sysTimeTaskDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.sysTimeTaskService.saveDataTask(data);
	}
	
	@Override
	public void add() {
		super.add();
		data = new SysTimeTask();
		data.setId(0l);
	}
	
	
	@Action
	public void addCopy(){
		SysTimeTask dataOld = data;
		data = new SysTimeTask();
		try {
			BeanUtils.copyProperties(data, dataOld);
			data.setId(0l);
			data.setInputer("");
			data.setInputtime(null);
			data.setUpdater("");
			data.setUpdatetime(null);
		} catch (Exception e) {
			data = new SysTimeTask();
			data.setId(0l);
			e.printStackTrace();
		}
	}
	
	@Override
	public void del(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			for(String id : ids) {
				serviceContext.sysTimeTaskService.removeDateTask(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void refreshJobs(){
		ScheduleMgr scheduleMgr = (ScheduleMgr)AppUtils.getBeanFromSpringIoc("scheduleMgr");
		try {
			scheduleMgr.addJob();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Action
	public void grid_onrowselect() {
		String id = this.grid.getSelectedIds()[0];
		//System.out.println("id:"+this.grid.getSelectedRow());
		String url = "./systimetasklog.xhtml?id=" + id;
		dtlIFrame.load(url);
		this.update.markUpdate(UpdateLevel.Data, "dtlIFrame");
	}

	@Action
	public void cfgCron() {
		String url = "./easy-cron.html?cron=" + this.data.getCron();
		cronIFrame.load(url);
		this.update.markUpdate(UpdateLevel.Data, "cronIFrame");
	}
	
	
	
	@Action
	public void stop() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			serviceContext.sysTimeTaskService.stop(ids);
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Action
	public void start() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			serviceContext.sysTimeTaskService.start(ids);
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
}
