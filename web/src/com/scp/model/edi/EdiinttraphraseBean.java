package com.scp.model.edi;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.data.Ediinttraphrase;
import com.scp.service.data.EdiinttraphraseMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.edi.ediinttraphraseBean", scope = ManagedBeanScope.REQUEST)
public class EdiinttraphraseBean extends GridFormView {

    @ManagedProperty("#{ediinttraphraseMgrService}")
    public EdiinttraphraseMgrService ediinttraphraseMgrService;

    @SaveState
    @Accessible
    public Ediinttraphrase selectedRowData = new Ediinttraphrase();

    @Override
    protected void doServiceFindData() {
        selectedRowData = ediinttraphraseMgrService.ediinttraphraseDao.findById(this.pkVal);
    }


    @Override
    protected void doServiceSave() {
        ediinttraphraseMgrService.saveData(selectedRowData);
        this.alert("OK");
    }

    @Override
    public void add() {
        selectedRowData =  new Ediinttraphrase();
        super.add();
    }

    @Override
    public void del() {
        if (selectedRowData.getId() == 0) {
            this.add();
        } else {
            ediinttraphraseMgrService.removeDate(selectedRowData.getId());
            refresh();
            this.add();
            this.alert("OK");
        }
    }

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
                String callFunction = "f_imp_ediinttraphrase";
                String args = "'" + AppUtils.getUserSession().getUsercode() + "'";
                this.serviceContext.commonDBService.addBatchFromExcelText(importDataText, callFunction, args, false);
                MessageUtils.alert("OK!");
                this.grid.reload();
            } catch (Exception e) {
                MessageUtils.showException(e);
            }
        }
    }


}