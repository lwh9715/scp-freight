package com.scp.view.module.data;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;

import java.util.List;
import java.util.Map;

@ManagedBean(name = "pages.module.data.userassignBean", scope = ManagedBeanScope.REQUEST)
public class UserAssignBean extends GridFormView {
	
	@Bind
	@SaveState
	public String linkid;
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind(id = "gridAssign")
	public UIDataGrid gridAssign;
	
	@Action
	public void choose() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null) {
			MessageUtils.alert("Please choose one!");
			return;
		} 
		for(String id : ids) {
			String sql = "INSERT INTO sys_user_assign (id, linkid, linktype, userid) VALUES(getid(), " + this.linkid + ", 'W', " + id + ")";
			this.serviceContext.sysUserAssignMgrService.sysUserAssignDao.executeSQL(sql);
		}
		this.refresh();
	}
	
	@Action
	public void remove() {
		String[] ids = this.gridAssign.getSelectedIds();
		if(ids == null) {
			MessageUtils.alert("Please choose one!");
			return;
		} 
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM sys_user_assign WHERE linkid=" + this.linkid + "AND linktype='W' AND userid IN(");
		for(int i=0;i<ids.length;i++) {
			if(i == (ids.length - 1)) {
				sql.append(ids[i]);
			} else {
				sql.append(ids[i] + ",");
			}
			
		}
		sql.append(")");
		this.serviceContext.sysUserAssignMgrService.sysUserAssignDao.executeSQL(sql.toString());
		this.refresh();
		
	}
	
	@Bind(id = "gridAssign", attribute = "dataProvider")
	protected GridDataProvider getAssignDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere1(null), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".grid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere1(null));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Override
	public void add() {
//		selectedRowData = new DatFiledata();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
//		selectedRowData = serviceContext.filedataMgrService.datFiledataDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
//		serviceContext.filedataMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.gridAssign.reload();
	}

	@Override
	public void del() {
		try {
			serviceContext.sysUserAssignMgrService.removeDate(this.getGridSelectId());
			//		this.add();
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	public Map getQryClauseWhere1(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND id IN (SELECT userid FROM sys_user_assign WHERE linktype='W' AND linkid=" + this.linkid + ")";
		m.put("qry", qry);
		return m;
	}
	
	

//	@Override
//	public Map getQryClauseWhere(Map<String, Object> queryMap) {
//		Map m = super.getQryClauseWhere(queryMap);
//		String qry = StrTools.getMapVal(m, "qry");
//		qry += "\nAND id NOT IN (SELECT userid FROM sys_user_assign WHERE linktype='W')";
//		m.put("qry", qry);
//		return m;
//	}

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.linkid = AppUtils.getReqParam("linkid");
		}
	}
	
}
