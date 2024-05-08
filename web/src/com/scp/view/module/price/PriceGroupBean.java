package com.scp.view.module.price;

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
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.price.PriceGroup;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.price.pricegroupBean", scope = ManagedBeanScope.REQUEST)
public class PriceGroupBean extends GridFormView {
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	
	@SaveState
	@Accessible
	public PriceGroup selectedRowData = new PriceGroup();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			//this.add();
		}
	}
	
	
	
	@Action
	public void user2Libs() {
		
		String[] userIds = this.userGrid.getSelectedIds();
		if (userIds == null || userIds.length <= 0) {
			MessageUtils.alert("请选择用户");
			return;
		}
		
		
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选择运价组");
			return;
		}
		StringBuilder stringBuffer = new StringBuilder();
		for (String uid : userIds) {
			for (String cid : ids) {
				stringBuffer.append("\nINSERT INTO price_group_refuser(id, groupid, userid) SELECT  getid(), "+cid+", "+uid+" FROM _virtual WHERE NOT EXISTS(SELECT 1 FROM price_group_refuser WHERE groupid="+cid+" AND userid="+uid+");");
			}
		}
		////System.out.println(stringBuffer);
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			MessageUtils.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void userClearLibs() {
		String[] userIds = this.userGrid.getSelectedIds();
		if (userIds == null || userIds.length <= 0) {
			MessageUtils.alert("请选择用户");
			return;
		}
		
		
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选择运价组");
			return;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		for (String uid : userIds) {
			for (String cid : ids) {
				stringBuffer.append("\nDELETE FROM price_group_refuser WHERE groupid="+cid+" AND userid="+uid+";");
			}
		}
		////System.out.println(stringBuffer);
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			MessageUtils.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 刷新方法
	 */
	@Action
	public void refreshUser() {
		if (userGrid != null) {
			this.userGrid.reload();
		}
	}

	@Action
	public void clearQryKeyUser() {
		if (qryMapUser != null) {
			qryMapUser.clear();

			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.qryRefresh();
		}
	}
	
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid userGrid;

	@Bind(id = "userGrid", attribute = "dataProvider")
	protected GridDataProvider getUserGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".userGrid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere(qryMapUser), start,
								limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".userGrid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere(qryMapUser));
				if (list == null || list.size() < 1)
					return 0;
				Long count = Long.parseLong(list.get(0).get("counts")
						.toString());
				return count.intValue();
			}
		};
	}
	

	@Override
	public void add() {
		selectedRowData = new PriceGroup();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.priceGroupService.priceGroupDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		this.serviceContext.priceGroupService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	
	@Override
	public void del() {
		String ids[] = this.grid.getSelectedIds();
		this.serviceContext.priceGroupService.delete(ids);
		alert("OK");
	}
	

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		//AppUtils.openWindow("_lib_edit", "./custlibedit.xhtml?libid="+this.getGridSelectId());
	}
}
