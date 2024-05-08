package com.scp.view.module.crm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.crm.clientshareBean", scope = ManagedBeanScope.REQUEST)
public class ClientshareBean extends GridView{
	
	@Bind
	@SaveState
	public String qrycode;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			super.applyGridUserDef();
			qryMap.put("iscustomer$", true);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		
		if(!StrUtils.isNull(qrycode)){
			map.put("filter2", "\nAND (namee ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR code ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR abbr ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' " +
								"OR (EXISTS(SELECT 1 FROM sys_user x WHERE x.isdelete = FALSE AND EXISTS(SELECT 1 FROM sys_user_assign y WHERE y.isdelete = FALSE AND y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid=x.id)" +
								"\nAND (x.namee ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR x.namec ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%' OR x.code ILIKE '%"+StrUtils.getSqlFormat(qrycode)+"%'))"+
								"))");
		}else{
			map.put("filter2", "\nAND 1=1");
		}
		return map;
	}
	
	
	
	@Action
	public void syscustIn() {
		try {
			String sql = "\nSELECT f_sys_cust_share('type=in;userid="+AppUtils.getUserSession().getUserid()+"');";
			this.serviceContext.customerMgrService.sysCorporationDao.executeQuery(sql);
			this.alert("OK!");
			this.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@Bind
	public UIDataGrid gridUser;
	
	@Bind(id = "gridUser", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit)
						.toArray();

			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	public String qryuserdesc = "";
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String qry = map.get("qry").toString();
		if(!StrUtils.isNull(qryuserdesc) ){
			qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
			qryuserdesc = qryuserdesc.toUpperCase();
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
		}
		map.put("qry", qry);
		return map;
	}
	
	@SaveState
	public int starts=0;
	
	@SaveState
    public int limits=100;
	
	
	
	@Action
	public void clearQryKeysc() {
		if (qryMapUser != null) {
			qryMapUser.clear();
			update.markUpdate(true, UpdateLevel.Data, "userPanel");
			this.gridUser.reload();
		}
	}

	@Action
	public void qryuser() {
		this.gridUser.reload();
	}
	
	@Bind
    public UIWindow userWindow;
	
	@Action
	public void syscustAssign() {
		String ids[] = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1) {
			this.alert("请至少选择一行!");
			return;
		}
		userWindow.show();
	}
	
	
	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null || ids.length > 1) {
			this.alert("请选择一行!");
			return;
		}
		
		String[] gridids = this.grid.getSelectedIds();
		for (String id : gridids) {
			try {
				String sql = "\nSELECT f_sys_cust_share('type=assign;customerid="+Long.valueOf(id)+";salesid="+Long.valueOf(ids[0].toString())+";userid="+AppUtils.getUserSession().getUserid()+"');";
				this.serviceContext.customerMgrService.sysCorporationDao.executeQuery(sql);
				this.alert("OK!");
				this.refresh();
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
			}
		}
		
	}
	
	@Action
	public void syscustGet() {
		String ids[] = this.grid.getSelectedIds();
		if(ids == null || ids.length < 1) {
			this.alert("请至少选择一行!");
			return;
		}
		for (String id : ids) {
			try {
				String sql = "\nSELECT f_sys_cust_share('type=get;customerid="+Long.valueOf(id)+";salesid="+AppUtils.getUserSession().getUserid()+";userid="+AppUtils.getUserSession().getUserid()+"');";
				this.serviceContext.customerMgrService.sysCorporationDao.executeQuery(sql);
				this.alert("OK!");
				this.refresh();
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
			}
		}
	}
	
	
	
}
