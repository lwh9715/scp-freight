package com.scp.view.comp;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.MessageUtils;


/**
 * 编辑 + 选择列表
 * @author neo
 *
 */
public abstract class GridSelectFormView extends GridSelectView{
	
	
	@Bind
	public UIWindow editWindow;
	
	@Bind
	public UIPanel editPanel;
	
	@Bind
	@SaveState
	public Long pkVal;
	
	@Action
	public void grid_ondblclick(){
		this.pkVal = getGridSelectId();
//		this.data = getViewControl().findById(this.pkVal);
		doServiceFindData();
		this.refreshForm();
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	protected abstract void doServiceFindData();

	public void refreshForm() {
		
	}

	@Action
	public void add(){
		this.pkVal = -1L;
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	@Action
	public void qryAdd(){
		this.add();
	}
	
	@Action
	public void save(){
//		this.pkVal = getViewControl().save(this.pkVal , data);
		try {
			doServiceSave();
		} catch (Exception e) {
			MessageUtils.showException(e);
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
		this.grid.reload();
	}
	
}
