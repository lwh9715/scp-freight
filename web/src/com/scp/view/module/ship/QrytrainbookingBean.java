package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.ship.MgrTrainBooking;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.ship.qrytrainbookingBean", scope = ManagedBeanScope.REQUEST)
public class QrytrainbookingBean extends GridFormView{
	
	
	@SaveState
	@Accessible
	public MgrTrainBooking selectedRowData = new MgrTrainBooking();
	
	@Bind
	@SaveState
	public String trainno;
	
	
	@Bind
	@SaveState
	public String polid;
	
	@Bind
	@SaveState
	public String podid;
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(trainno)){
			qry += "\nAND t.trainno LIKE '%"+trainno+"%'";
		}
		
		if(!StrUtils.isNull(polid)){
			qry += "\nAND t.polid = "+polid+"";
		}
		if(!StrUtils.isNull(podid)){
			qry += "\nAND t.podid = "+podid+"";
		}
		m.put("qry", qry);
		return m;
	}

	@Override
	protected void doServiceFindData() {
		
	}

	@Override
	protected void doServiceSave() {
		
	}
}
