package com.scp.view.module.data;

import com.scp.model.data.DatFiledata;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.ajax.UpdateLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "pages.module.data.filedataBean", scope = ManagedBeanScope.REQUEST)
public class FiledataBean extends GridFormView {
	
	@SaveState
	@Accessible
	public DatFiledata selectedRowData = new DatFiledata();
	
	@SaveState
    private Short fkcode;
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapExp = new HashMap<String, Object>();
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind(id = "gridExp")
	public UIDataGrid gridExp;
	
	@Bind(id = "gridExp", attribute = "dataProvider")
	protected GridDataProvider getExpDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridExp.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere2(qryMapExp), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridExp.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere2(qryMapExp));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String filter = SaasUtil.filterByCorpid("t");
		m.put("filter", filter);
		return m;
	}
	
	@Bind(id = "grid", attribute = "dataProvider")
	protected GridDataProvider getGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere1(qryMap), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".grid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere1(qryMap));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Override
	public void add() {
		selectedRowData = new DatFiledata();
		selectedRowData.setFkcode(this.fkcode);
		selectedRowData.setIsleaf(true);
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.filedataMgrService.datFiledataDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.filedataMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.gridExp.reload();
	}

	@Override
	public void del() {
		try {
			serviceContext.filedataMgrService.removeDate(this.getGridSelectId(),AppUtils.getUserSession().getUsercode());
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Action(id = "gridExp", event = "onrowselect")
	public void gridExp_onrowselect(){
		fkcode = Short.parseShort(this.gridExp.getSelectedIds()[0]);
		
		this.grid.reload();
	}
	
	@Action
	public void clearQryKeyExp() {
		if(qryMapExp != null){
			qryMapExp.clear();

			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.gridExp.reload();
		}
	}
	
	public Map getQryClauseWhere1(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND fkcode = " + this.fkcode;
		m.put("qry", qry);
		String filter = SaasUtil.filterByCorpid("t");
		m.put("filter", filter);
		return m;
	}
	
}
