package com.scp.view.module.api.data;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.base.CommonComBoxBean;
import com.scp.model.api.ApiData;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.api.data.dataBean", scope = ManagedBeanScope.REQUEST)
public class DataBean extends GridFormView {


    @SaveState
    @Accessible
    public ApiData selectedRowData = new ApiData();


    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(m, "qry");
        m.put("qry", qry);

        return m;
    }

    @Override
    public void add() {
        selectedRowData = new ApiData();
        super.add();
    }

    @Override
    protected void doServiceFindData() {
        selectedRowData = this.serviceContext.apiDataMgrService.dataDao.findById(this.pkVal);
    }


    @Override
    protected void doServiceSave() {
    	this.serviceContext.apiDataMgrService.saveData(selectedRowData);
        this.alert("OK");
    }

    @Action
    public void deleter() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length == 0 || ids.length > 1) {
            MessageUtils.alert("请选择单行记录");
            return;
        } else {
        	this.serviceContext.apiDataMgrService.removeDate(getGridSelectId());
            refresh();
        }
    }


    @Bind(id="srctype")
    public List<SelectItem> getSrctype() {
        try {
            return CommonComBoxBean.getComboxItems("d.srctype","d.srctype","api_data d","WHERE  isdelete = FALSE GROUP BY srctype","order by srctype");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Bind(id="maptype")
    public List<SelectItem> getMaptype() {
        try {
            return CommonComBoxBean.getComboxItems("d.maptype","d.maptype","api_data d","WHERE  isdelete = FALSE GROUP BY maptype","order by maptype");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }
}
