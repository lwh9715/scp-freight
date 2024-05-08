package com.scp.view.module.somgr;

import java.util.Map;

import javax.annotation.Resource;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.service.ship.BusShippingMgrService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.somgr.gridCntTracksBean", scope = ManagedBeanScope.REQUEST)
public class GridCntTracksBean extends GridView {


	@Bind
	@SaveState
	public String sono;

	@Resource
	public BusShippingMgrService busShippingMgrService;

	@Bind
	@SaveState
	public String cntno = "";


	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			sono = AppUtils.getReqParam("sono").trim();
			cntno = AppUtils.getReqParam("cntno");
		}
	}

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map map = super.getQryClauseWhere(queryMap);
        map.put("filter", "\nAND sono = '" + sono + "' \nAND bsbcCntno = '" + StrUtils.getSqlFormat(cntno) + "'");
        return map;
    }


	@Action
	public void clearQryqryMapSo(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ){
		qryMap.clear();
		this.grid.reload();
	}

}
