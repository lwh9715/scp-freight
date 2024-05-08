package com.scp.view.comp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.action.ActionGridExport;

/**
 * 编辑表格，grid组件
 * @author neo
 */
public abstract class EditGridView extends GridView{
	
	
	/**
	 * @param isPostBack
	 */
//	@Override
//	public void beforeRender(boolean isPostBack) {
//		if (!isPostBack) {
//		}
//	}

	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIEditDataGrid editGrid;
	
	@Bind(id = "editGrid", attribute = "addedData")
	@SaveState
    protected Object addedData;
	
    @Bind(id = "editGrid", attribute = "modifiedData")
    @SaveState
	protected Object modifiedData;
    
    @Bind(id = "editGrid", attribute = "removedData")
    @SaveState
    protected Object removedData;
    
	@Bind(id = "editGrid", attribute = "dataProvider")
	protected GridDataProvider getEditGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				if(gridLazyLoad){
					return new Object[]{};
				}else{
					starts = start;
					limits = limit;
					String sqlId = getMBeanName() + ".grid.page";
					return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMap)).toArray();
				}
			}

			@Override
			public int getTotalCount() {
				if(gridLazyLoad){
					return 1;
				}else{
					String sqlId = getMBeanName() + ".grid.count";
					List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere(qryMap));
					Long count = (Long)list.get(0).get("counts");
					return count.intValue();
				}
			}
		};
	}
	
	
	@Action(id = "add")
    public void insert() {
		editGrid.appendRow();
    }

    @Action(id = "addWithData")
    public void insertWithData() {
    	//editGrid.insertRow(0, new Company(UUID.randomUUID().toString(),"Microsoft","Windows, Office","天河软件园","http://www.microsoft.com","800 810 1818",""));
    }

    @Action(id = "remove")
    public void remove() {
    	editGrid.remove();
    }
    
    @Action(id = "save")
    public void save() {
        try {
            if (addedData != null) {
                add(addedData);
            }
            if (modifiedData != null) {
                update(modifiedData);
            }
            if (removedData != null) {
                remove(removedData);
            }
            editGrid.commit();
            editGrid.reload();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }
    
    @Action(id = "saveBatch")
    public void saveBatch() {
        try {
            if (modifiedData != null) {
            	saveBatch1(modifiedData);
            }
            editGrid.commit();
            editGrid.reload();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }
    
    @Action(id = "remove")
    protected void remove(Object obj) {
    	
    }
    
    @Action(id = "update")
    protected void update(Object obj) {
    	
    }
    
    protected void add(Object obj) {
    	
    }
    
    @Action(id = "saveBatch1")
    protected void saveBatch1(Object obj) {
    	
    }
	
	
	@Bind
	public UIWindow editWindow;
	
	@Bind
	public UIIFrame editIFrame;
	
	/**
	 * 返回当前行的id
	 * 
	 * @return
	 */
	public long getGridSelectId() {
		String[] ids = this.editGrid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			return -1;
		}
		return Long.valueOf(ids[0]);
	}
	
	@Action
	public void editGrid_ondblclick(){
		//if(editWindow != null)editWindow.show();
		//editIFrame.load("");
	}
	
	@Action
	public void editGrid_onrowselect() {
		String ids[] = editGrid.getSelectedIds();
		if(ids != null && ids.length > 0){
			String id = ids[0];
			//AppUtil.openWindow("_newRP", "./actpayrecedit.xhtml?type=new&id=-1&customerid="+id+"");
		}
	}

	/**
	 * 刷新方法
	 */
	@Override
	public void refresh() {
		if(editGrid != null){
			gridLazyLoad = false;
			this.editGrid.reload();
		}
	}
	
	@Override
	public void qryRefresh() {
		gridLazyLoad = false;
		this.refresh();
	}
	
	/**
	 * grid在前台设置每页显示的行数
	 */
	@Override
	public void doChangeGridPageSize() {
		String pageStr = (String)AppUtils.getReqParam("page");
		if(!StrUtils.isNull(pageStr) && StrUtils.isNumber(pageStr)) {
			//alert("pageStr:"+pageStr);
			Integer page = Integer.parseInt(pageStr);
			this.editGrid.setRows(page);
			gridLazyLoad = false;
			this.editGrid.rebind();
//			this.editGrid.reload();
//			//记录选择的行数到个人设置
			String mbeanId = this.getMBeanName();
			String girdId = mbeanId+".grid.pagesize";
			try {
//				CfgUtil.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				ConfigUtils.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				 applyGridUserDef();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			alert("Wrong pagesize：" + pageStr);
		}
	}
	@Override
	protected void applyGridUserDef() {
		String gridid  = this.getMBeanName() + ".editGrid";
		String gridJsvar = "editGridJsvar";
		applyGridUserDef(gridid , gridJsvar);
	}
	
	
	@Override
	public void export() {
		ActionGridExport actionGridExport = new ActionGridExport();
		actionGridExport.setKeys((String) AppUtils.getReqParam("key"));
		actionGridExport.setVals((String) AppUtils.getReqParam("value"));
		int limitsNew = limits;
		int startsNew = starts;
		try {
			limits = 100000;
			starts = 0;
			
			String sqlId = getMBeanName() + ".grid.page";
			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
					.queryForList(sqlId, getQryClauseWhere(this.qryMap));
			
			if(editGrid != null){
				List<Map> listNew = new ArrayList<Map>();
				String[] ids = this.editGrid.getSelectedIds();
				boolean flag = false;
				if(ids != null && ids.length != 0) {
					flag = true;
					for (Map map : list) {
						for (String id : ids) {
							if(StrUtils.getMapVal(map, "id").equals(id)){
								listNew.add(map);
							}
						}
					}
				}
				if(flag){
					list = listNew;
				}
			}
			
			actionGridExport.execute(list);
			Browser.execClientScript("simulateExport.fireEvent('click');");
			qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally{
			limits = limitsNew;
			starts = startsNew;
		}
	}

}
