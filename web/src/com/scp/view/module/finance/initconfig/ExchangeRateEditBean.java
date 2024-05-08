package com.scp.view.module.finance.initconfig;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.exception.CommonRuntimeException;
import com.scp.model.finance.fs.FsXrate;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;

@ManagedBean(name = "pages.module.finance.initconfig.exchangerateeditBean", scope = ManagedBeanScope.REQUEST)
public class ExchangeRateEditBean extends EditGridView {

	@SaveState
	@Accessible
	public String periodid;

	@Bind
	@SaveState
	public FsXrate ddata = new FsXrate();

	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			periodid = AppUtils.getReqParam("periodid");
			this.qryMap.put("periodid$", periodid);
		}
	}

	@Override
	protected void update(Object companys) {

	}

	@Override
	public void save() {
		String goodscode = "";
		try {
			serviceContext.actrateMgrService.saveDtlDatas(modifiedData);
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
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND periodid = " + periodid;
		m.put("qry", qry);
		return m;
	}
}
