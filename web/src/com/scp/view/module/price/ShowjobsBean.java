package com.scp.view.module.price;

import java.util.Map;

import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.price.showjobsBean", scope = ManagedBeanScope.REQUEST)
public class ShowjobsBean extends GridFormView{

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@SaveState
	public Long priceid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack){
			String id = AppUtils.getReqParam("priceid");
			if (!StrUtils.isNull(id)){
				priceid = Long.parseLong(id);
			}else{
				priceid = -1L;
			}
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry");
		map.remove("qry");
		map.put("qry",qry);
		map.put("priceid", ""+priceid+"");
		return map;
	}
}
