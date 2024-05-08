package com.scp.view.comp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.CheckboxSelectionModel;
import org.operamasks.faces.component.grid.GridColumnModel;
import org.operamasks.faces.component.grid.GridHeaderModel;
import org.operamasks.faces.component.grid.GridSelectionModel;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.grid.provider.GridRowDataProvider;
import org.operamasks.faces.component.grid.provider.GridViewProvider;
import org.operamasks.faces.user.ajax.PartialUpdateManager;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.service.ServiceContext;


/**
 * 简单表格，grid组件
 * @author neo
 */
public abstract class DynamicGridView{
	
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	@SaveState
	protected UIDataGrid grid;
	
	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;
	
	
	
	@ManagedProperty("#{daoIbatisTemplate}")
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Inject
	protected PartialUpdateManager update;
	
	@SaveState
	public List<Map> gridCollection = new ArrayList<Map>();
	
	@Bind(id = "grid", attribute = "dataProvider")
	public GridDataProvider getDataProvider() {
		
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				if (gridCollection == null)
					return null;
				List<Map> pageList = new ArrayList<Map>();
				for (; limit > 0 && start < getTotalCount(); ++start, --limit) {
					pageList.add(gridCollection.get(start));
				}
				Object[] objs = pageList.toArray();
				return objs;
			}

			@Override
			public int getTotalCount() {
				if (gridCollection == null)
					return 0;
				return gridCollection.size();
			}

			@Override
			public Object[] getElementsById(String[] ids) {
				if (ids == null)
					return null;
				int length = ids.length;
				if (length == 0)
					return null;

				Map map = gridCollection.get(Integer.parseInt(ids[0]));
				Set<String> set = map.keySet();
				Object[] objs = new Object[set.size()];
				int i = 0;
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					objs[i] = map.get(key);
					i++;
				}
				return objs;
			}
		};
	}
	
	@Bind(id = "grid", attribute = "viewProvider")
	private GridViewProvider viewProvider = new GridViewProvider() {
		public GridColumnModel getColumnModel(Object input) {
			return createDynamicGridColumnModel(gridCollection);
		}

		public GridHeaderModel getHeaderModel(Object input) {
			return createDynamicGridHeaderModel(gridCollection);
		}

		public GridSelectionModel getSelectionModel(Object input) {
			return new CheckboxSelectionModel();
		}
		
		
	};
	
	
	@Bind(id = "grid", attribute = "rowDataProvider")
    private GridRowDataProvider rowProvider = getDinamicGridRowProvider();
	
	protected abstract GridRowDataProvider getDinamicGridRowProvider();
	
	public Map<String, Object> getGridConfig() {
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("paged", Boolean.TRUE);
		config.put("rows", new Integer(20));
		config.put("trackMouseOver", Boolean.FALSE);
		config.put("honorViewState", Boolean.FALSE);
		config.put("forceFit", Boolean.FALSE);
		config.put("trdisableSelection", Boolean.TRUE);

		config.put("enableColumnHide", Boolean.FALSE);
		config.put("enableColumnMove", Boolean.FALSE);
		config.put("enableDragDrop", Boolean.FALSE);

		config.put("enableColumnResize", Boolean.FALSE);
		config.put("loadMask", Boolean.TRUE);
		config.put("selectable", Boolean.TRUE);
		config.put("showRowNumber", Boolean.TRUE);
		config.put("singleSelect", Boolean.TRUE);
		config.put("stripeRows", Boolean.TRUE);

		return config;
	}

	protected abstract GridColumnModel createDynamicGridColumnModel(List<Map> list);

	protected abstract GridHeaderModel createDynamicGridHeaderModel(List<Map> list);
}
