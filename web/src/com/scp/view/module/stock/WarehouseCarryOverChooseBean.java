package com.scp.view.module.stock;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;

import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.stock.warehousecarryoverchooseBean", scope = ManagedBeanScope.REQUEST)
public class WarehouseCarryOverChooseBean extends GridView {

	
	@Action
	public void carryOver() {
		String[] ids = this.grid.getSelectedIds();
		
		try {
			if (ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要修改的行");
				return;
			}

			else {
				for (int i = 0; i < ids.length; i++) {

					Long warehouseid = new Long((Integer.parseInt(ids[i])));
					String sql = "select f_wms_carryover(" + warehouseid + ")";
					serviceContext.wmsOutdtlMgrService().wmsOutDao.executeQuery(sql);
					
				}
			    MessageUtils.alert("OK");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

}
