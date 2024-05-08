package com.scp.view.module.customer;

import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;

import com.scp.model.sys.SysCorpext;
import com.scp.service.customer.CustomerSysCorpextMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.customer.sysCorpextBean", scope = ManagedBeanScope.REQUEST)
public class SysCorpextBean extends GridFormView {

    @ManagedProperty("#{customerSysCorpextMgrService}")
    public CustomerSysCorpextMgrService customerSysCorpextMgrService;

    @SaveState
    @Accessible
    public SysCorpext selectedRowData = new SysCorpext();

    @Bind
    @SaveState
    @Accessible
    public Long pkid;

    @SaveState
    @Accessible
    public String customerid;

    @Bind
    public UIButton del;
    @Bind
    public UIButton save;

    public void beforeRender(boolean isPostBack) {
        try {
            if (!isPostBack) {
                customerid = AppUtils.getReqParam("customerid");
                selectedRowData.setCustomerid(Long.valueOf(customerid));

                List<SysCorpext> list = customerSysCorpextMgrService.SysCorpextDao.findAllByClauseWhere("customerid =" + customerid);
                if (!list.isEmpty() && list.size() == 1) {
                    selectedRowData = list.get(0);
                }
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }

        String sql = "select f_checkright('customereditpermission',(SELECT id FROM sys_user x WHERE x.isdelete = FALSE AND x.code = '"+AppUtils.getUserSession().getUsercode()+"')) = 0 as customereditpermission";
        try {
            Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            if (m != null && m.get("customereditpermission") != null&& m.get("customereditpermission").toString().equals("true")) {
                del.setDisabled(true);
                save.setDisabled(true);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void doServiceFindData() {

    }

    @Override
    protected void doServiceSave() {

    }


    public void save() {
        try {
            if (selectedRowData.getMerchantcode()==null) {
                MessageUtils.alert("客商编码为空");
                return;
            }
            customerSysCorpextMgrService.saveData(selectedRowData);
            MessageUtils.alert("OK");
//            this.alert("OK");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }


    @Override
    public void del() {
        try {
            if (selectedRowData.getId() == 0) {
            } else {
                customerSysCorpextMgrService.removeDate(selectedRowData.getId());
                selectedRowData = new SysCorpext();
                selectedRowData.setCustomerid(Long.valueOf(customerid));
                MessageUtils.alert("OK");
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Override
    public void add() {
        pkid = -1L;
    }


}



