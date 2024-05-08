package com.scp.view.bpm;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.bpm.BpmProcessDao;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmAssign;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bpm.BpmWorkItem;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "bpm.bpmapplyBean", scope = ManagedBeanScope.REQUEST)
public class BpmApplyBean extends GridFormView{
	
	@SaveState
	public BpmProcess data = new BpmProcess();
	
	@Resource
	public BpmProcessDao bpmProcessDao;
	

	@Override
	protected void doServiceFindData() {
		this.pkVal = getGridSelectId();
		this.data = serviceContext.bpmProcessService.bpmProcessDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		serviceContext.bpmProcessService.saveData(data);
		
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		return map;
	}
	
	@Override
	public void add() {
		super.add();
		data = new BpmProcess();
		data.setId(0l);
	}
	
	@Action
	public void edit(){
		this.pkVal = getGridSelectId();
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
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
				serviceContext.bpmProcessService.removeDate(Long.parseLong(id));
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
		MessageUtils.alert("OK!");
		this.grid.reload();
	}
	
	@Bind
	public UIWindow showStartProcessWindow;
	
	@Bind
	public UIIFrame startProcessIframe;
	
	
	@Bind
	public UIWindow showStartProcessUserDefineWindow;
	
	@Bind
	public UIIFrame startProcessUserDefineIframe;
	
	@Override
	public void grid_ondblclick(){
		
		this.pkVal = getGridSelectId();
		this.data = serviceContext.bpmProcessService.bpmProcessDao.findById(this.pkVal);
		
		//Start 中如果定义了url。取定义的url打开界面，并返回retur
		try {
			List<BpmAssign> lists = serviceContext.bpmAssignService.bpmAssignDao.findAllByClauseWhere("process_id = '"+this.pkVal+"' AND taskname = 'Start'");
			if(lists != null && lists.size() > 0){
				String url = lists.get(0).getUrl();
				if(!StrUtils.isNull(url)){
					
					String actionJsText="";
					List<BpmWorkItem> bpmWorkItems = serviceContext.bpmWorkItemService.bpmWorkItemDao.findAllByClauseWhere("process_id = "+this.pkVal+" AND taskname = 'Start' AND actiontype = 'windows.js'");
					for (BpmWorkItem bpmWorkItem : bpmWorkItems) {
						actionJsText+=bpmWorkItem.getActions();
					}
					//System.out.println(actionJsText);
					if(!StrUtils.isNull(actionJsText)){
						Browser.execClientScript(actionJsText);
					}
					startProcessUserDefineIframe.load(url);
					showStartProcessUserDefineWindow.show();
					showStartProcessWindow.close();
					return;//***********************************************
				}
			}
		} catch (NoRowException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//否则按默认的自定义界面打开
		startProcessIframe.load("./bpmtaskstart.xhtml?processid="+this.pkVal+"&m=bpm.bpmdesignerBean."+this.data.getCode()+"&p="+AppUtils.getUserSession().getUserid());
		showStartProcessWindow.show();
		showStartProcessUserDefineWindow.close();
		
	}
	
	@Action
	public void startProcess() {
		grid_ondblclick();
	}
	
	@Bind
	public UIWindow showAttachWindow;
	
	@Bind
	public UIIFrame attachIframe;
	
	@Action
	public void attachment(){
		this.pkVal = getGridSelectId();
		attachIframe.load("/scp/pages/module/common/attachment.xhtml?code=bpm&linkid="+this.pkVal);
		showAttachWindow.show();
	}
	
	
}
