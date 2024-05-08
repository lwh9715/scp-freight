package com.scp.view.module.stock;

import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.wms.WmsOutdtl;
import com.scp.service.wms.WmsOutdtlMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;

@ManagedBean(name = "pages.module.stock.wmsstockchooserBean", scope = ManagedBeanScope.REQUEST)
public class WmsStockChooserBean extends EditGridView {

	@Bind
	public String customerid;

	@Bind
	public String outid;
	
	@Bind
	public String transid;
	
	@Bind
	@SaveState
	@Accessible
	public Date intimesqry;

	@Bind
	@SaveState
	public WmsOutdtl ddata = new WmsOutdtl();

	@ManagedProperty("#{wmsOutdtlMgrService}")
	private WmsOutdtlMgrService wmsOutdtlMgrService;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			customerid = AppUtils.getReqParam("customerid");
			outid = AppUtils.getReqParam("outid");
			transid = AppUtils.getReqParam("transid");
			this.qryMap.put("customerid$", customerid);
		}
	}

	@Override
	protected void update(Object companys) {

	}

	@Override
	public void save() {
		String goodscode = "";
		try {
			if(!StrUtils.isNull(transid)){
				serviceContext.wmsTransdtlMgrService.saveDtlDatas(modifiedData, Long.valueOf(transid));
			}else{
				wmsOutdtlMgrService.saveDtlDatas(modifiedData , Long.valueOf(outid));
			}
			MessageUtils.alert("OK");
			refresh();
		} catch (CommonRuntimeException e) {	
			MessageUtils.alert(e.getLocalizedMessage());
			return;
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void editGrid_ondblclick() {

	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		if (intimesqry==null) {
			m.put("qryIntime", "1=1");
		} else {
			long dateValLong = intimesqry.getTime();
			Date  d = new Date(dateValLong);
//			Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			qryVal = format.format(d.toGMTString());
			m.put("qryIntime", " CAST( min(checktime) AS DATE )='" + d.toLocaleString() + "'");
			
		}
		return m;
	}
}
