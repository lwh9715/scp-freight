package com.scp.view.module.customer;

import com.scp.model.sys.SysContract;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import java.util.Map;

/**
 * @author CIMC
 */
@ManagedBean(name = "pages.module.customer.sysContractBean", scope = ManagedBeanScope.REQUEST)
public class SysContractBean extends GridFormView {

    @SaveState
    @Accessible
    public SysContract selectedRowData = new SysContract();

    @Bind
    @SaveState
    @Accessible
    public Long pkid;

    @SaveState
    @Accessible
    public String customerid;

    @Bind
    public UIButton save;

    @Bind
    public UIButton add;

    @Bind
    public UIButton delBatch;

    @Override
    public void beforeRender(boolean isPostBack) {
        try {
            super.beforeRender(isPostBack);
            if (!isPostBack) {

            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        // TODO Auto-generated method stub
        Map m = super.getQryClauseWhere(queryMap);
        String qry = m.get("qry").toString();

        if (AppUtils.getReqParam("id") != null) {
            this.customerid = AppUtils.getReqParam("id");
            qry += " AND customerid = " + AppUtils.getReqParam("id") + "";
        }

        m.put("qry", qry);
        return m;
    }

    @Override
    public void save() {
        try {
            if (selectedRowData.getContnamec() == null) {
                MessageUtils.alert("合同名称为空");
                return;
            }

            selectedRowData.setCustomerid(Long.parseLong(this.customerid));
            serviceContext.customerContractMgrService.saveData(selectedRowData);
            MessageUtils.alert("OK");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void delBatch() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请先勾选要修改的行");
            return;
        }
        try {
            serviceContext.customerContractMgrService.delBatch(ids);
            MessageUtils.alert("OK");
            refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;
        }

    }

    public void initAdd() {
        selectedRowData = new SysContract();
        this.update.markUpdate(UpdateLevel.Data, "editPanel");
    }

    @Override
    public void add() {
        super.add();
        initAdd();
    }

    @Override
    protected void doServiceFindData() {
    }

    @Override
    protected void doServiceSave() {
    }

}



