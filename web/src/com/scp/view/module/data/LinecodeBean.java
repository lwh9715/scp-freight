package com.scp.view.module.data;

import com.scp.model.data.DatLinecode;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;

@ManagedBean(name = "pages.module.data.linecodeBean", scope = ManagedBeanScope.REQUEST)
public class LinecodeBean extends GridFormView{

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.linecodeMgrService.datLinecodeDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.linecodeMgrService.saveData(selectedRowData);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}
	
	@SaveState
	@Accessible
	public DatLinecode selectedRowData = new DatLinecode();
	
	@Override
	public void add() {
		selectedRowData = new DatLinecode();
		super.add();
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			this.serviceContext.linecodeMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			alert("请选择一行记录!");
		} else {
			try {
				this.serviceContext.linecodeMgrService.removeDate(Long.valueOf(ids[0]));
				this.add();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
}
