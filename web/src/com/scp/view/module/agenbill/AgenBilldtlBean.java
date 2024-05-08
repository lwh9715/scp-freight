package com.scp.view.module.agenbill;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;

import com.scp.model.finance.FinaAgenBilldtl;
import com.scp.service.finance.AgenBilldtlMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;


@ManagedBean(name = "pages.module.agenbill.agenbilldtlBean", scope = ManagedBeanScope.REQUEST)
public class AgenBilldtlBean extends GridFormView {

	@SaveState
	@Accessible
	public Long agenbillid;
	
	@ManagedProperty("#{agenBilldtlMgrService}")
	public AgenBilldtlMgrService agenBilldtlMgrService;
	
	@SaveState
	@Accessible
	public FinaAgenBilldtl selectedRowData = new FinaAgenBilldtl();
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
				String id =AppUtils.getReqParam("agenbillid").trim();
				String flag =AppUtils.getReqParam("isCheck").trim();
				if(!StrUtils.isNull(id)) {
					agenbillid=Long.valueOf(id);
					Boolean isCheck=Boolean.parseBoolean(flag);
					disableAllButton(isCheck);
				}
			}
			refresh();
	}
	
	
	@Override
	public void add() {
		selectedRowData = new FinaAgenBilldtl();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = agenBilldtlMgrService.finaAgenBilldtlDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		try {
			agenBilldtlMgrService.saveData(selectedRowData);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void del() {
		agenBilldtlMgrService.finaAgenBilldtlDao.remove(selectedRowData);
		
		this.alert("OK");
		refresh();
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry +=  "\nAND t.agenbillid = " + this.agenbillid;
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	public UIButton save;
	@Bind
	public UIButton del;
	
    public void disableAllButton(Boolean ischeck) {
		save.setDisabled(ischeck);
		del.setDisabled(ischeck);
	}


	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
	}
    
    
    
	
}
