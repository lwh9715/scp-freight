package com.scp.view.module.data;

import com.scp.model.data.DatProduct;
import com.scp.service.data.ProductMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;

@ManagedBean(name = "pages.module.data.productBean", scope = ManagedBeanScope.REQUEST)
public class ProductBean extends GridFormView {

    @ManagedProperty("#{productMgrService}")
    public ProductMgrService productMgrService;

    @SaveState
    @Accessible
    public DatProduct selectedRowData = new DatProduct();

    @Override
    public void add() {

        selectedRowData = new DatProduct();
        super.add();
    }

    @Override
    protected void doServiceFindData() {
        selectedRowData = productMgrService.datProductDao
                .findById(this.pkVal);
    }

    @Override
    protected void doServiceSave() {
        selectedRowData.setCorpid(String.valueOf(AppUtils.getUserSession().getCorpid()));
        productMgrService.saveData(selectedRowData);
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
            productMgrService.removeDate(getGridSelectId());
            refresh();
        }
    }

    @Override
    public void del() {
        productMgrService.datProductDao.remove(selectedRowData);
        this.add();
        this.alert("OK");
        refresh();
    }

}