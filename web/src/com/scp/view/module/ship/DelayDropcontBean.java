package com.scp.view.module.ship;

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

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.ship.delaydropcontBean", scope = ManagedBeanScope.REQUEST)
public class DelayDropcontBean extends GridView {
	
	
    
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	
	/**
	 * 延船  0 
	 * 甩柜 1
	 */
	@Action
	public void delordropAction(){
		String[] soids = this.grid.getSelectedIds();
		if(soids==null||soids.length ==0){
			MessageUtils.alert("请勾选订舱号");
			return ;
		}
		
		String[] shipids = this.rightgrid.getSelectedIds();
		if(shipids==null||shipids.length ==0||shipids.length>1){
			MessageUtils.alert("请勾选一个船期");
			return ;
		}
		String type = (String) AppUtils.getReqParam("type");
		
		try {
			serviceContext.busBookingMgrService.delordrop(soids, shipids[0],
					AppUtils.getUserSession().getActsetcode(), type);
			refresh();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}


	
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid rightgrid;
	@Bind(id = "rightgrid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".rightgrid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();
				
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".rightgrid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap){
		return super.getQryClauseWhere(queryMap);
	}

	@Action
	public void clearQryKeysc() {
		if (qryMapShip != null) {
			qryMapShip.clear();
			update.markUpdate(true, UpdateLevel.Data, "gridRightPanel");
			this.rightgrid.reload();
		}
	}
	
	
	@Action
	public void qryRefreshShip() {
		this.rightgrid.reload();
	}
	

}
