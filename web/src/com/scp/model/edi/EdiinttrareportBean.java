package com.scp.model.edi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.data.Ediinttrareport;
import com.scp.service.data.EdiinttrareportMgrService;
import com.scp.util.FtpUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.edi.ediinttrareportBean", scope = ManagedBeanScope.REQUEST)
public class EdiinttrareportBean extends GridFormView {

    @ManagedProperty("#{ediinttrareportMgrService}")
    public EdiinttrareportMgrService ediinttrareportMgrService;

    @SaveState
    @Accessible
    public Ediinttrareport selectedRowData = new Ediinttrareport();

    @Override
    protected void doServiceFindData() {
        selectedRowData = ediinttrareportMgrService.ediinttrareportDao.findById(this.pkVal);
    }

    @SaveState
    @Accessible
    public String reportid;

    @Override
    protected void doServiceSave() {
        ediinttrareportMgrService.saveData(selectedRowData);
        this.alert("OK");
    }


    @Override
    public void del() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length <= 0) {
            MessageUtils.alert("Please choose one!");
            return;
        }
        try {
            for (String id : ids) {
                ediinttrareportMgrService.removeDate(Long.parseLong(id));
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;
        }
        MessageUtils.alert("OK!");
        this.grid.reload();
    }

    @Bind
    public UIWindow reportdtlWindow;

    @Override
    public void grid_ondblclick() {
    	reportid = this.grid.getSelectedIds()[0];
        this.pkVal = Long.valueOf(reportid);
        this.selectedRowData = ediinttrareportMgrService.ediinttrareportDao.findById(this.pkVal);
        gridReportdtl.reload();
        reportdtlWindow.show();
        
        update.markUpdate(true, UpdateLevel.Data, "reportdtlPanel");
    }


    @SaveState
    @Accessible
    public Map<String, Object> qryMapGridReportdtl = new HashMap<String, Object>();

    @Bind
    public UIDataGrid gridReportdtl;

    @Bind(id = "gridReportdtl", attribute = "dataProvider")
    protected GridDataProvider getGridScheduleDataProvider() {
        return new GridDataProvider() {
            public Object[] getElements() {
                String sqlId = "pages.module.edi.ediinttrareportBean.gridReportdtl.page";
                return serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere2(qryMapGridReportdtl)).toArray();
            }

            public int getTotalCount() {
                String sqlId = "pages.module.edi.ediinttrareportBean.gridReportdtl.count";
                List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere2(qryMapGridReportdtl));
                Long count = (Long) list.get(0).get("counts");
                return count.intValue();
            }
        };
    }

    public Map getQryClauseWhere2(Map<String, Object> queryMap) {
        Map m = getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(m, "qry");
        qry += "\nAND reportid = " + reportid;
        m.put("qry", qry);
        return m;
    }
    
    @Action
    public void qryshipcarrier() {
        this.gridReportdtl.reload();
    }

    @Action
    public void downEdi() {
    	try {
			FtpUtils.parseSoEdi();
			MessageUtils.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
    }
    
    @Action
    public void phraseEdi() {
		String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length <= 0) {
            MessageUtils.alert("Please choose one!");
            return;
        }
        try {
            for (String id : ids) {
            	String sql = "SELECT f_edi_inttra_phrase_process('" + id + "');";
        		serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;
        }
        MessageUtils.alert("OK!");
        this.refresh();
    }

    @Action
    public void cleanup() {
        try {
            String sql =
            		"\ndelete from edi_inttra_reportdtl where reportid = ANY (select id from edi_inttra_report where inputtime < (now() + '-6MONTH'));" +
                    "\ndelete from edi_inttra_report where inputtime < (now() + '-6MONTH');";
            daoIbatisTemplate.updateWithUserDefineSql(sql);
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;
        }
        MessageUtils.alert("OK!");
        this.refresh();
    }

}