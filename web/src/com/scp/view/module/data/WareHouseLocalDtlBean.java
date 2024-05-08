package com.scp.view.module.data;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;

import com.scp.exception.CommonRuntimeException;
import com.scp.service.data.WarehouselocalMgrService;
import com.scp.service.wms.WmsIndtlMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;

@ManagedBean(name = "pages.module.data.warehouselocaldtlBean", scope = ManagedBeanScope.REQUEST)
public class WareHouseLocalDtlBean extends EditGridView {

	@ManagedProperty("#{warehouselocalMgrService}")
	private WarehouselocalMgrService warehouselocalMgrService;

	@ManagedProperty("#{wmsIndtlMgrService}")
	private WmsIndtlMgrService wmsIndtlMgrService;
	
	

	@SaveState
	@Bind
	private String locdesc;

	@Bind
	public UIDataGrid grid;

	@SaveState
	@Accessible
	private Long locid;

	@SaveState
	@Accessible
	private Integer pieces_desc;

	@SaveState
	@Accessible
	private Integer pieceout;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String code = AppUtils.getReqParam("code");
			String areaid = AppUtils.getReqParam("areaid");
			String querySql = "\nSELECT"
					+ "\n(w.code || '/' || COALESCE(w.namec , '') || '-->' || a.code || '-->' || l.code) AS locdesc"
					+ "\n,l.id AS id"
					+ "\nFROM dat_warehouse w , dat_warehouse_area a , dat_warehouse_loc l"
					+ "\nWHERE a.warehouseid = w.id " + "\nAND a.id=l.areaid"
					+ "\nAND l.areaid = " + areaid + "\nAND l.code = '" + code
					+ "'";
			try {
				Map m = daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(querySql);
				locdesc = String.valueOf(m.get("locdesc"));
				locid = (Long) m.get("id");
				//System.out.println(locid);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND d.locid = " + locid;
		m.put("qry", qry);
		return m;
	}

	
	@Override
	public void save() {
		try {
			serviceContext.wmsInMgrService().saveDtlDatas(
					modifiedData,locid);
			MessageUtils.alert("OK");
			refresh();
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			return;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

}
