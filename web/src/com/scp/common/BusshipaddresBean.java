package com.scp.common;

import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;

/**
 * @author Administrator
 *
 */
@ManagedBean(name = "common.busshipaddresBean", scope = ManagedBeanScope.REQUEST)
public class BusshipaddresBean extends EditGridView {
	
	@SaveState
	@Accessible
	@Bind
	public Long customsid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String id = AppUtils.getReqParam("linkid").trim();
		if(id!=null&&id!=""){
			customsid = Long.valueOf(id);
		}
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.busShipAddresMgrService.updateBatchEditGrid(modifiedData);
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.busShipAddresMgrService.addBatchEditGrid(addedData, customsid);
	}
	@Override
	protected void remove(Object removedData) {
		serviceContext.busShipAddresMgrService.removedBatchEditGrid(removedData);
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry+="\n AND linkid =" + customsid;
		m.put("qry", qry);
		return m;
	}
	
	@Action(id = "removes")
    public void removes() {
    	String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录");
			return;
		}
		serviceContext.busShipAddresMgrService.removeSelections(ids);
		editGrid.remove();
    }
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid historyGrid;
	
	@Bind(id = "historyGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
		return this.getJobEmailDataProvider();
	}
	
	@SaveState
	private String qrySql = "";
	
	/**
	 * 工作单附件导入和清除
	 * @return
	 */
	public GridDataProvider getJobEmailDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String querySql = 
					"\nSELECT * FROM bus_ship_addres x"+
					 qrySql +
					"\nWHERE EXISTS(SELECT 1 FROM bus_customs a,fina_jobs b WHERE a.jobid = b.id AND a.id = x.linkid AND "+
					"\nEXISTS(SELECT 1 FROM fina_jobs f WHERE f.customerid = b.customerid AND EXISTS(SELECT 1 FROM bus_customs WHERE jobid = f.id AND id = "+customsid+")))"+
					"\nORDER BY inputtime DESC"+
					"\nLIMIT " + 100 + " OFFSET " + start;
				List<Map> list;
				try {
					list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
				} catch (Exception e) {
					MessageUtils.showException(e);
					return null;
				}
				if(list==null) return null;
				return list.toArray(); 
			}
			
			@Override
			public int getTotalCount() {
				String countSql = 
					"\nSELECT COUNT(*) AS counts FROM bus_ship_addres x"+
					 qrySql +
					"\nWHERE EXISTS(SELECT 1 FROM bus_customs a,fina_jobs b WHERE a.jobid = b.id AND a.id = x.linkid AND "+
					"\nEXISTS(SELECT 1 FROM fina_jobs f WHERE f.customerid = b.customerid AND EXISTS(SELECT 1 FROM bus_customs WHERE jobid = f.id AND id = "+customsid+")))"+
					"";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return 10000;
				} catch (Exception e) {
					MessageUtils.showException(e);
					return 0;
				}
			}
		};
	}
	
	@Action
	public void setHistory(){
		String[] ids = this.historyGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请至少选择一行记录");
			return;
		}
		serviceContext.busShipAddresMgrService.selectAdd(ids,customsid);
		this.editGrid.reload();
	}
}

