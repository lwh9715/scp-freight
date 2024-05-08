package com.scp.view.module.lclload;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.del.DelLoad;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.lclload.shiplclcheckBean", scope = ManagedBeanScope.REQUEST)
public class ShipLclCheckBean extends GridView {
	
	@SaveState
	@Bind
	public String qryCntids;
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_shipdtl";
		DelLoad delLoad = serviceContext.delLoadMgrService.delLoadDao.findById(this.getGridSelectId());
		String url;
		if("H".equals(delLoad.getLoadtype())){
			url = "./trainlcldtlcheck.xhtml?id="+this.getGridSelectId();
		}else{
			url = "./shiplcldtlcheck.xhtml?id="+this.getGridSelectId();
		}
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void checkLcl() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			this.serviceContext.delLoadMgrService.updateCheck(ids);
			MessageUtils.alert("审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}

	@Action
	public void cancelCheckLcl() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		
		try {
			this.serviceContext.delLoadMgrService.updateCancelCheck(ids);
			MessageUtils.alert("取消审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(qryCntids)){
			qry += "\nAND EXISTS (SELECT 1 FROM wms_in zz, wms_indtl z, del_loadtl x , wms_outdtlref y WHERE zz.cntid ILIKE '%"+qryCntids+"%' AND x.outdtlid = y.outdtlid AND y.indtlid = z.id AND x.loadid = p.id AND zz.id = z.inid)";
		}
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void clearQryKey() {
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
		
		if(!StrUtils.isNull(qryCntids)){
			qryCntids = "";
		}
		
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		this.qryRefresh();
	}
	
}
