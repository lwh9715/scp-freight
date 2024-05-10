package com.scp.view.module.data;

import com.scp.model.data.DatAddress;
import com.scp.service.data.AddressMgrService;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;

@ManagedBean(name = "pages.module.data.addressBean", scope = ManagedBeanScope.REQUEST)
public class AddressBean extends GridFormView {

    @ManagedProperty("#{addressMgrService}")
    public AddressMgrService addressMgrService;

    @SaveState
    @Accessible
    public DatAddress selectedRowData = new DatAddress();

    @Override
    public void add() {

        selectedRowData = new DatAddress();
        super.add();
    }

    @Override
    protected void doServiceFindData() {
        selectedRowData = addressMgrService.datAddressDao.findById(this.pkVal);
    }

    @Override
    protected void doServiceSave() {
        addressMgrService.saveData(selectedRowData);
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
            addressMgrService.removeDate(getGridSelectId());
            refresh();
        }
    }

    @Override
    public void del() {
        addressMgrService.datAddressDao.remove(selectedRowData);
        this.add();
        this.alert("OK");
        refresh();
    }

}