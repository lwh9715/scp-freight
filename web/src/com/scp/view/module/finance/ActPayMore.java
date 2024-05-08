package com.scp.view.module.finance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.actpaymoreBean", scope = ManagedBeanScope.REQUEST)
public class ActPayMore extends GridFormView {
	
	@Bind
	@SaveState
	public Long actpayrecid = -1L;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			actpayrecid = Long.valueOf((String) AppUtils.getReqParam("actpayrecid"));
			this.update.markUpdate(UpdateLevel.Data,"actpayrecid");
		}
		}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	

	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actpayrecid = " + actpayrecid;
		m.put("qry", qry);
		return m;
	}
	
	@Action
	public void delcus() {
		String ids[] = this.grid.getSelectedIds();
		if(ids==null || ids.length <1){
			MessageUtils.alert("请至少选择一个客户");
			return;
		}else{
			try {
				this.serviceContext.actPayRecService.delcus(ids);
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;
	
	@Bind
	public String popQryKey;

	@Action
	public void popQry() {
		customerService.qry(popQryKey);
		this.customerGrid.reload();
	}

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		//选过的就不能再选了
		String sql = "AND NOT EXISTS (SELECT 1 FROM fina_actpayrec_clients c WHERE c.actpayrecid = "+this.actpayrecid+" AND c.clientid = a.id AND c.isdelete = FALSE)";
		return customerService.getCustomerDataProvider(sql);
	}

	@Action
	public void showCustomer() {
		//this.popQryKey = AppUtil.getReqParam("customercode");
		customerService.qry(null);
		showCustomerWindow.show();
		customerGrid.reload();
	}
	/**
	 * 加入客户
	 */
	@Action
	public void choose() {
		String ids[] = this.customerGrid.getSelectedIds();
		if(ids==null || ids.length <1){
			MessageUtils.alert("请至少选择一个客户");
			return;
		}else{
			try {
				this.serviceContext.actPayRecService.choosecus(ids,
						this.actpayrecid);
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
			
		}
	}
	
	@Bind
	public UIWindow showsearchWindow;
	
	
	@Bind
	public String customergridPanel;
	
	@Bind
	public UIDataGrid customergrid;
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@Action
	public void search() {
		
		this.clearQryKey2();
		showsearchWindow.show();
		//this.customergrid.reload();
		update.markUpdate(true,UpdateLevel.Data,customergridPanel);
	}
	/**
	 * 标记,点高级查询时才加载grid
	 */
	@SaveState
	public boolean isLoad = false;

	@Bind(id = "customergrid", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				if(!isLoad) return null;
				String sqlId = "pages.module.finance.actpaymoreBean.customergrid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				if(!isLoad) return 0;
				String sqlId = "pages.module.finance.actpaymoreBean.customergrid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	public String qryJobno;
	@Bind
	@SaveState
	public String qrySono;
	@Bind
	@SaveState
	public String qryCntno;

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		
		String filter = "";
		if(!StrUtils.isNull(qryJobno)){
			qryJobno = StrUtils.getSqlFormat(qryJobno);
			filter += "\nAND EXISTS(SELECT 1 FROM fina_jobs x , fina_arap y WHERE x.nos ILIKE '%"+qryJobno+"%' AND y.customerid = c.id AND x.id = y.jobid)";
		}
		if(!StrUtils.isNull(qrySono)){
			qrySono = StrUtils.getSqlFormat(qrySono);
			filter += "\nAND EXISTS(SELECT 1 FROM fina_jobs x , fina_arap y , bus_shipping z WHERE z.sono ILIKE '%"+qrySono+"%' AND y.customerid = c.id AND x.id = y.jobid AND z.jobid = x.id)";
		}
		if(!StrUtils.isNull(qryCntno)){
			qryCntno = StrUtils.getSqlFormat(qryCntno);
			filter += "\nAND EXISTS(SELECT 1 FROM fina_jobs x , fina_arap y , bus_ship_container z WHERE z.cntno ILIKE '%"+qryCntno+"%' AND y.customerid = c.id AND x.id = y.jobid AND z.jobid = x.id)";
		}
		map.put("filter", filter);
		return map;
	}
	
	@Action
	public void clearQryKey2() {
		if (qryMapShip != null) {
			qryMapShip.clear();
			update.markUpdate(true, UpdateLevel.Data, "customergridPanel");
			this.refresh2();
		}
	}
	
	@Action
	public void refresh2() {
		isLoad = true;
		this.customergrid.reload();
		update.markUpdate(true, UpdateLevel.Data, "customergridPanel");
	}
	
	@Action
	public void okMaster(){
		String[] ids = this.customergrid.getSelectedIds();
		if(ids==null || ids.length <1){
			MessageUtils.alert("请至少选择一个客户");
			return;
		}else{
			try {
				this.serviceContext.actPayRecService.choosecus(ids,
						this.actpayrecid);
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
			
		}
		}

}
