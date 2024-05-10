package com.scp.view.module.data;

import com.scp.model.data.DatLine;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

@ManagedBean(name = "pages.module.data.lineBean", scope = ManagedBeanScope.REQUEST)
public class LineBean extends GridFormView {

	@Bind
	public UIWindow knowledgeBaseWindow;
	
	@Bind
	public UIIFrame knowledgeBaseIframe;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}

	@SaveState
	@Accessible
	public DatLine selectedRowData = new DatLine();

	@Override
	public void add() {
		selectedRowData = new DatLine();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.lineMgrService.datLineDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.lineMgrService.saveData(selectedRowData);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			try {
				this.serviceContext.lineMgrService.removeDateIsdelete(getGridSelectId());
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
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
				this.serviceContext.lineMgrService.removeDateIsdelete(Long.valueOf(ids[0]));
				this.add();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Action
	public void showKnowledgeBase(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			knowledgeBaseIframe.load( "../../sysmgr/knowledge/knowledgeBase.xhtml?linkid="+getGridSelectId()+"&tablename=dat_line");
			knowledgeBaseWindow.show();
		}
		
	}
	
}
