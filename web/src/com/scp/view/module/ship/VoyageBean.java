package com.scp.view.module.ship;

import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.ship.Voyage;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.ship.voyageBean", scope = ManagedBeanScope.REQUEST)
public class VoyageBean extends GridFormView {


    @SaveState
    public Voyage selectedRowData = new Voyage();

    @Bind
    @SaveState
    public String bookingid2;

    @Bind
    @SaveState
    public String bookingid2Str;


    @Bind
    @SaveState
    public String jobid;


    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
            String id = AppUtils.getReqParam("id");
            jobid = AppUtils.getReqParam("jobid");
            bookingid2 = AppUtils.getReqParam("bookingid2");
            bookingid2Str = StrUtils.isNull(this.bookingid2) ? "-111" : this.bookingid2;

            addvoyage();
        }
    }


    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("qry", "\n1=1");
        map.put("start", "0");
        map.put("limit", "10000");
        map.put("filter", "\nAND isdelete = false AND ((linkid = " + bookingid2Str + ") or (linkid = " + jobid + "))");
        return map;
    }


    @Action
    public void addvoyage() {
        this.selectedRowData = new Voyage();
        this.selectedRowData.setId(0L);
        this.selectedRowData.setLinkid(Long.valueOf(this.jobid));
        this.selectedRowData.setInputer("addmanually");

        getVoyagenumber();

        // editWindow.show();
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
    }

    @Action
    public void delvoyage() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length <= 0) {
            MessageUtils.alert("Please choose one!");
            return;
        }
        for (String id : ids) {
            try {
                Voyage thisVoyage = this.serviceContext.voyageService.voyageDao.findById(Long.valueOf(id));
                if (!"addmanually".equals(thisVoyage.getInputer())) {
                    MessageUtils.alert("新增的航程才允许修改删除");
                    return;
                }
                this.serviceContext.voyageService.removeDate(Long.parseLong(id));
            } catch (Exception e) {
                MessageUtils.showException(e);
                return;
            }
        }
        MessageUtils.alert("OK!");
        this.grid.reload();
    }


    @Action
    public void refreshvoyage() {
        this.grid.reload();
    }

    @Action
    public void savevoyage() {
        try {
            if (!"addmanually".equals(selectedRowData.getInputer())) {
                MessageUtils.alert("新增的航程才允许修改删除");
                return;
            }

            this.serviceContext.voyageService.voyageDao.createOrModify(selectedRowData);
            this.pkVal = selectedRowData.getId();
            update.markUpdate(true, UpdateLevel.Data, "pkVal");
            MessageUtils.alert("OK");
            refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;
        }
    }


    @Override
    protected void doServiceFindData() {
        selectedRowData = this.serviceContext.voyageService.voyageDao.findById(this.pkVal);

    }

    @Override
    protected void doServiceSave() {
    }

    @Override
    public void grid_ondblclick() {
        this.pkVal = getGridSelectId();
        //		this.data = getViewControl().findById(this.pkVal);
        doServiceFindData();
        this.refreshForm();
        // if(editWindow != null)editWindow.show();
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
    }

    public void getVoyagenumber() {
        String sqlfk = "select count(1)+1 as voyagenumber  FROM bus_ship_book_vesvoy t  where 1=1  AND isdelete = false AND ((linkid = " + bookingid2Str + ") or (linkid = " + jobid + "))";
        try {
            Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlfk);
            if (m != null && m.get("voyagenumber") != null) {
                selectedRowData.setVoyagenumber((Long) m.get("voyagenumber"));
            }
        } catch (Exception e) {

        }
    }


}