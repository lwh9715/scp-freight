package com.scp.view.module.ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.finance.FinaBillCntdtl;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.module.ship.busbillcntsBean", scope = ManagedBeanScope.REQUEST)
public class BusBillCntsBean extends GridSelectView {

	@Bind
	@SaveState
	@Accessible
	public Long jobid = -100L;
	
	@Bind
	@SaveState
	@Accessible
	public Long billid = -100L;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id = AppUtils.getReqParam("jobid");
			String bid = AppUtils.getReqParam("billid");
			if(StrUtils.isNull(id)||StrUtils.isNull(id)){
				
			}else{
				this.jobid = Long.parseLong(id);
				this.billid = Long.parseLong(bid);
			}
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		m.put("jobid", jobid);
		return m;
	}
	
	@Override
	public int[] getGridSelIds(){
		if(this.jobid > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("\n SELECT (CASE WHEN (SELECT id FROM fina_billcntdtl x WHERE x.billid ="+this.billid+" AND x.cntid = a.id) IS NULL THEN FALSE ELSE TRUE END) ischoose");
			sb.append("\n FROM _bus_ship_container a WHERE isdelete = FALSE AND parentid IS NULL AND jobid ="+this.jobid);
			sb.append("\n ORDER BY orderno");
			
			List<Map> list = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql(sb.toString());
			if(list != null && list.size() > 0){
				ArrayList<Integer> li = new ArrayList<Integer>();
				for(int i = 0; i < list.size() ; i++){
					if(Boolean.parseBoolean(list.get(i).get("ischoose").toString())){
						li.add(i);
					}
				}
				int[] tmp = new int[li.size()];
				for (int i =0;i<li.size();i++) {
					tmp[i] = li.get(i);
				}
				return tmp;
			}
			return null;
		}else{
			return null;
		}
	}
	
	@Override
	protected void doSaveAfter() {
		String[] ids = this.grid.getSelectedIds();
		this.serviceContext.finaBillCntdtlMgrService.removeDate(this.billid);
		for (String id : ids) {
			FinaBillCntdtl data = new FinaBillCntdtl();
			data.setCntid(Long.parseLong(id));
			data.setBillid(this.billid);
			this.serviceContext.finaBillCntdtlMgrService.saveData(data);
		}
	}
}
