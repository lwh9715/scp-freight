package com.scp.view.module.lclload;

import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.del.DelLoadtl;
import com.scp.model.wms.WmsOutdtl;
import com.scp.service.wms.WmsOutdtlMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;

@ManagedBean(name = "pages.module.lclload.wmsstockchooserBean", scope = ManagedBeanScope.REQUEST)
public class WmsStockChooserBean extends EditGridView {

	@Bind
	public String customerid;

	@Bind
	public String outid;
	
	@Bind
	@SaveState
	@Accessible
	public Date intimesqry;
	
	@Bind
	@SaveState
	@Accessible
	public String innos;

	@Bind
	@SaveState
	public WmsOutdtl ddata = new WmsOutdtl();

	@ManagedProperty("#{wmsOutdtlMgrService}")
	private WmsOutdtlMgrService wmsOutdtlMgrService;
	

	@SaveState
	@Accessible
	public DelLoadtl delLoadtl = new DelLoadtl();

	@SaveState
	@Accessible
	public Long loadid;

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String code = AppUtils.getReqParam("loadid");
			loadid =Long.valueOf(code);
			String code2 = AppUtils.getReqParam("warehouseid");
			this.qryMap.put("warehouseid$", code2);
		}
	}

	@Override
	protected void update(Object companys) {

	}

	@Override
	public void save() {
		String goodscode = "";
		try {
			wmsOutdtlMgrService.saveLoadDtlDatas(modifiedData ,loadid);
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
		
		if(StrUtils.isNull(this.innos)){
			
			m.put("qryinnos", "1=1");
			
		}else{
			m.put("qryinnos", " nos like '%"+this.innos+"%'");
		}
		return m;
	}
}
