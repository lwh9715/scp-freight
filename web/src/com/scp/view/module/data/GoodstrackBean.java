package com.scp.view.module.data;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.data.DatGoodstrack;
import com.scp.service.data.GoodstrackMgrService;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.data.goodstrackBean", scope = ManagedBeanScope.REQUEST)
public class GoodstrackBean extends GridFormView {

    @ManagedProperty("#{goodstrackMgrService}")
    public GoodstrackMgrService goodstrackMgrService;

    @SaveState
    @Accessible
    public DatGoodstrack selectedRowData = new DatGoodstrack();

    @Override
    public void add() {

        selectedRowData = new DatGoodstrack();
        super.add();
    }

    @Override
    protected void doServiceFindData() {
        selectedRowData = goodstrackMgrService.datGoodstrackDao.findById(this.pkVal);
    }

    @Override
    protected void doServiceSave() {
        goodstrackMgrService.saveData(selectedRowData);
        MessageUtils.alert("OK!");
        refresh();
    }

    @Action
    public void deleter() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length == 0 || ids.length > 1) {
            MessageUtils.alert("请选择单行记录");
            return;
        } else {
            goodstrackMgrService.removeDate(getGridSelectId());
            refresh();
        }
    }

    @Override
    public void del() {
        goodstrackMgrService.datGoodstrackDao.remove(selectedRowData);
        this.add();
        this.alert("OK");
        refresh();
    }

}