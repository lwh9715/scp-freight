package com.scp.view.module.price;

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

import com.scp.service.price.PriceHomepageMgrService;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.price.pricehomepageBean", scope = ManagedBeanScope.REQUEST)
public class pricehomepageBean extends EditGridFormView{
    
    @ManagedProperty("#{priceHomepageMgrService}")
	public PriceHomepageMgrService priceHomepageMgrService;

	@Override
	protected void update(Object obj) {
		priceHomepageMgrService.updateBatchEditGrid(obj);
	}

	
	@Action
    private void clearQry2() {
		this.clearQryKey();
	}
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid homepageGrid;
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMaph = new HashMap<String, Object>();
	
	@Bind(id = "homepageGrid", attribute = "dataProvider")
	protected GridDataProvider getHomepageGrid() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
					String sqlId = getMBeanName() + ".gridHomepage.pageh";
					return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereHomepage(), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
					String sqlId = getMBeanName() + ".gridHomepage.counth";
					List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhereHomepage());
					Long count = (Long)list.get(0).get("counts");
					return count.intValue();
			}
		};
	}
	
	@Bind
	@SaveState
	private String polqre;
	
	@Bind
	@SaveState
	private String podqre;
	
	@Bind
	@SaveState
	private String shippingqre;
	
	public Map getQryClauseWhereHomepage() {
		StringBuilder buffer = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		buffer.append("\n1=1 ");
		if(polqre!=null&&!polqre.equals("")){
			buffer.append("\nAND t.pol LIKE '%" + polqre +"%'");
		}
		if(podqre!=null&&!podqre.equals("")){
			buffer.append("\nAND t.pod LIKE '%" + podqre +"%'");
		}
		if(shippingqre!=null&&!shippingqre.equals("")){
			buffer.append("\nAND t.shipping LIKE '%" + shippingqre +"%'");
		}
		String qry = StrUtils.isNull(buffer.toString()) ? "" : buffer.toString();
		map.put("qry", qry);
		return map;
	}


	@Action
	private void saveHomepage() {
		String[] ids = this.homepageGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		serviceContext.priceHomepageMgrService.saveHomepage(ids);
		qryRefresh();
	}
	
	@Action
	public void qryRefreshh() {
		if(homepageGrid != null){
			gridLazyLoad = false;
			this.homepageGrid.reload();
		}
	}
	
	@Action
	public void clearQryKeyh() {
		polqre = "";
		podqre = "";
		shippingqre = "";
		this.qryRefreshh();
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Action
	public void delHomepage(){
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请勾选一行记录!");
			return;
		}
		serviceContext.priceHomepageMgrService.delHomepage(ids);
		qryRefresh();
	}
}
