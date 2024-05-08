package com.scp.view.module.data;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.data.Hscode;
import com.scp.service.data.HscodeMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.data.hscodeBean", scope = ManagedBeanScope.REQUEST)
public class HscodeBean extends GridFormView {

    @ManagedProperty("#{hscodeMgrService}")
    public HscodeMgrService hscodeMgrService;

    @SaveState
    @Accessible
    public Hscode selectedRowData = new Hscode();

    @Override
    public void add() {
        selectedRowData = new Hscode();
        super.add();
    }

    @Override
    protected void doServiceFindData() {
        selectedRowData = hscodeMgrService.hscodeDao.findById(this.pkVal);
    }


    @Override
    protected void doServiceSave() {
        hscodeMgrService.saveData(selectedRowData);
        this.alert("OK");
    }

    @Action
    public void deleter() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length == 0 || ids.length > 1) {
            MessageUtils.alert("请选择单行记录");
            return;
        } else {
            hscodeMgrService.removeDate(getGridSelectId());
            refresh();
        }
    }

    @Override
    public void del() {
        if (selectedRowData.getId() == 0) {
            this.add();
        } else {
            hscodeMgrService.removeDate(selectedRowData.getId());
            refresh();
            this.add();
            this.alert("OK");
        }
    }
    
    @Bind
	public UIButton importData;
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_hscode";
				String args = "'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				this.grid.reload();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
}
