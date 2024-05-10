package com.scp.view.module.data;

import com.scp.model.data.DatCurrency;
import com.scp.service.data.CurrencyMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "pages.module.data.currencyBean", scope = ManagedBeanScope.REQUEST)
public class CurrencyBean extends GridFormView {
	
	@ManagedProperty("#{currencyMgrService}")
	public CurrencyMgrService currencyMgrService;
	
	@SaveState
	@Accessible
	public DatCurrency selectedRowData = new DatCurrency();
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			//this.add();
		}
	}

	@Override
	public void add() {
		selectedRowData = new DatCurrency();
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		super.add();
	}


	@Override
	protected void doServiceFindData() {
		selectedRowData = currencyMgrService.datCurrencyDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		currencyMgrService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			currencyMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}
	
	@Action
	public void syncAct() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			alert("Please select a line");
		} else {
			accountWindow.show();
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = SaasUtil.filterByCorpid("t");
		map.put("filter", filter);
		return map;
	}
	
	
	
	@Bind
	@SaveState
	public Date begindate;
	
	@Action
	public void confirm(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			alert("Please select a line");
		} else {
			String[] accids = this.accountgrid.getSelectedIds();
			if(accids == null || accids.length < 1){
				MessageUtils.alert("请勾选一条账套记录！");
				actsetids = "";
				update.markUpdate(true, UpdateLevel.Data, "actsetids");
				return;
			}
			if(null == begindate){
				MessageUtils.alert("请选择开始时间！");
				return;
			}
			try {
				for (String actsetid : accids) {
					for (String id : ids) {
						DatCurrency datCurrency = this.serviceContext.currencyMgrService.datCurrencyDao.findById(Long.valueOf(id));
						String cyidStr = datCurrency.getCode();
						String sql = "SELECT f_auto_filldata_currency('actsetid="+actsetid+";cyid="+cyidStr+";datefm="+begindate+"');";
						//System.out.println(sql);
						currencyMgrService.datCurrencyDao.executeQuery(sql);
					}
				}
				this.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Override
	public void del() {
		if(selectedRowData.getId()==0){
			this.add();	
		}else{
			currencyMgrService.removeDate(selectedRowData.getId());
		refresh();
		this.add();
		this.alert("OK");
		}
	}

	@Bind
	@SaveState
	public String actsetids;
	
	@Bind
	public UIDataGrid accountgrid;
	
	@Bind
	public UIWindow accountWindow;
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMap = new HashMap<String, Object>();
	
	@Bind(id = "accountgrid", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.initconfig.accountBean.grid.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMap), start, limit)
						.toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.initconfig.accountBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMap));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		return super.getQryClauseWhere(queryMap);
	}

	
}
