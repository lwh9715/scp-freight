package com.scp.view.module.stock;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.wms.WmsDispatchdtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.EditGridView;

@ManagedBean(name = "pages.module.stock.wmsstockcheckchooserBean", scope = ManagedBeanScope.REQUEST)
public class WmsStockCheckChooserBean extends EditGridView {

	@Bind
	public String customerid;

	@Bind
	public String warehouseid;

	@Bind
	public String outid;

	@Bind
	@SaveState
	public WmsDispatchdtl ddata = new WmsDispatchdtl();

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			customerid = AppUtils.getReqParam("customerid");
			warehouseid = AppUtils.getReqParam("warehouseid");
			outid = AppUtils.getReqParam("outid");
			// MessageUtils.alert(outid);
			this.qryMap.put("customerid$", customerid);
			this.qryMap.put("warehouseid$", warehouseid);
		}
	}

	@Override
	protected void update(Object companys) {

	}

	@Override
	public void save() {
		try {
			serviceContext.wmsDispatchdtlService().saveCheckDtlDatas(
					modifiedData, Long.valueOf(outid));
			MessageUtils.alert("OK");
		} catch (CommonRuntimeException e) {
			MessageUtils.alert(e.getLocalizedMessage());
			return;
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Override
	public void editGrid_ondblclick() {

	}

	// @Override
	// public Map getQryClauseWhere(Map<String, Object> queryMap) {
	// Map m = super.getQryClauseWhere(queryMap);
	// String qry = StrUtils.getMapVal(m, "qry");
	// qry += "\nAND piece > 0 ";
	// m.put("qry", qry);
	// return m;
	// }
}
