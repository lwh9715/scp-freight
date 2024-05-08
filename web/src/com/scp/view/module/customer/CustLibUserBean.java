package com.scp.view.module.customer;

import java.util.HashMap;
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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.customer.custlibuserBean", scope = ManagedBeanScope.REQUEST)
public class CustLibUserBean extends GridView {
	
	
	@Bind
	@SaveState
	public String libid = "-1";
	
	
	
//	@Bind
//	@SaveState
//	public String tips = "";
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("libid");
			//tips = AppUtils.getReqParam("tips");
			//this.update.markUpdate(UpdateLevel.Data, "tips");
			if(!StrUtils.isNull(id)) {
				libid = id;
			}
		}
	}
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("libid", libid);
		return map;
	}



	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	
	
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		this.serviceContext.custLibMgrService.removeUser(libid,ids);
		this.alert("OK!");
		this.grid.reload();
		this.gridFrom.reload();
	}
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapFrom = new HashMap<String, Object>();

	
	@Bind
	public UIDataGrid gridFrom;

	@Bind(id = "gridFrom", attribute = "dataProvider")
	protected GridDataProvider getDataFromProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".from.grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMapFrom), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".from.grid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere(qryMapFrom));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Action
	public void chooseBtn() {
		String[] ids = this.gridFrom.getSelectedIds();
		this.serviceContext.custLibMgrService.addUser(libid,ids);
		//this.alert("OK!");
		this.grid.reload();
		this.gridFrom.reload();
	}
	
	
	@Action
	public void clearQryKey(){
		if(qryMapFrom != null){
			qryMapFrom.clear();

			update.markUpdate(true, UpdateLevel.Data, "gridFromPanel");
			this.qryRefreshFrom();
		}
	}
	
	@Action
	public void qryRefreshFrom(){
		this.gridFrom.reload();
	}
}
