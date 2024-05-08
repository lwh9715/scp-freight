package com.scp.view.comp;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;

/**
 * 编辑表格+Form组件
 * @author neo
 */
public abstract class EditGridFormView extends EditGridView{
	
	
	@Bind
	public UIWindow editWindow;
	
	@Bind
	public UIPanel editPanel;
	
	@Bind
	@SaveState
	public Long pkVal;
	
	
	protected abstract void doServiceFindData();

	@Action
	public void refreshForm() {
		
	}

	@Action
	public void addForm(){
		this.pkVal = -1L;
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	@Action
	public void qryAdd(){
		this.addForm();
	}
	
	@Action
	public void saveForm(){
//		this.pkVal = getViewControl().save(this.pkVal , data);
		try {
			doServiceSave();
		} catch (Exception e) {
			MessageUtils.showException(e);
			this.addForm();
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		refresh();
	}
	
	protected abstract void doServiceSave();

	@Action
	public void del(){
	}
	
	@Action
	public void refresh(){
		this.editGrid.reload();
	}
	
	@Override
	public void grid_ondblclick() {
		//super.grid_ondblclick();
	}
	
	@Action
	public void linkEdit(){
		String pkid = AppUtils.getReqParam("pkid");
		if(StrUtils.isNull(pkid)){
			this.alert("无效pkid");
			return;
		}
		this.pkVal = Long.parseLong(pkid);
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editWindow.show();
	}
	
}
