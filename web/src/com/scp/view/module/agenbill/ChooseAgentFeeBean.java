package com.scp.view.module.agenbill;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.agenbill.chooseagentfeeBean", scope = ManagedBeanScope.REQUEST)
public class ChooseAgentFeeBean extends GridView {

	@SaveState
	@Accessible
	public String jobid;
	

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("id").trim();
			if(!StrUtils.isNull(id)) {
				jobid=id;
			}
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry +=  "\nAND jobid = " + this.jobid;
		m.put("qry", qry);
		return m;
	}

	@Action
    public void genAsJobFee(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <= 0){
			this.alert("Please choose one!");
			return;
		}
		this.serviceContext.agenBillMgrService.genAsJobFee(jobid,ids);
    	this.alert("OK");
    }
	
}
