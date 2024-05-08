package com.scp.view.sysmgr.message;

import java.util.Date;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.sys.Unsubscribe;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.sysmgr.message.unsubscribeBean", scope = ManagedBeanScope.REQUEST)
public class UnsubscribeBean extends GridFormView{
	
	@Bind
	private UIButton save;
	
	@Bind
	private UIButton qryAdd;
	
	@Bind
	public UIButton del;
	
	@Bind
	public UIButton edit;

	
	@SaveState
	@Accessible
	public Unsubscribe selectedRowData = new Unsubscribe();
	

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			
		}
	}
	
	@Action
	public void qryAdd(){
		selectedRowData.setId(0L);
		this.add();
	}
	
	

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.unsubscribeService.unsubscribeDao.findById(this.getGridSelectId());
		
	}

	@Override
	protected void doServiceSave() {
		try {
			selectedRowData.setDate(new Date());			
			this.serviceContext.unsubscribeService.saveData(selectedRowData);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.alert("保存出错");
		}		
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		//System.out.println(ids[0]);

		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
		try {
			this.serviceContext.unsubscribeService.removeDate(ids);
			MessageUtils.alert("OK!");
			this.grid.reload();
		} catch (Exception e) {
			//MessageUtils.showException(e);
			e.printStackTrace();
			MessageUtils.alert("删除失败");
		}
		
	}
	
	
	@Action
	public void edit() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one row!");
			return;
		}
				
		
		this.pkVal = getGridSelectId();
		doServiceFindData();
		
		if(this.selectedRowData.getId()!=0){
			Unsubscribe unsubscribe = serviceContext.unsubscribeService.unsubscribeDao.findById(this.selectedRowData.getId());
		}else{
			MessageUtils.alert("Please choose one row!");
			return;
		}
		if(editWindow != null)editWindow.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		
	}
	
	
}
