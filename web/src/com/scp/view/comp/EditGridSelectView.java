package com.scp.view.comp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.action.ActionGridExport;
import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.util.MessageUtils;
import org.operamasks.faces.user.util.Browser;

/**
 * 编辑表格，grid组件
 * @author neo
 */
public abstract class EditGridSelectView extends GridView{
	
	
	/**
	 * @param isPostBack
	 */
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}

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
	protected GridDataProvider getEditDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				starts = start;
				limits = limit;
				String sqlId = getMBeanName() + ".grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMap)).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".grid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere(qryMap));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
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
    
    @Action(id = "remove")
    protected void remove(Object obj) {
    	
    }
    
    @Action(id = "update")
    protected void update(Object obj) {
    	
    }
    
    protected void add(Object obj) {
    	
    }
	
	
	@Bind
	public UIWindow editWindow;
	
	@Bind
	public UIIFrame editIFrame;
	
	@Action
	public void editGrid_ondblclick(){
		//if(editWindow != null)editWindow.show();
		//editIFrame.load("");
	}
	
	@Action
	public void editGrid_onrowselect() {
		//String id = editGrid.getSelectedIds()[0];
		//AppUtil.openWindow("_newRP", "./actpayrecedit.xhtml?type=new&id=-1&customerid="+id+"");
	}
	
	@Action
	public void editGrid_onrowdeselect() {
//		int id = editGrid.getSelectedRow();
//		//System.out.println(id);
		//String id = editGrid.getSelectedIds()[0];
		//AppUtil.openWindow("_newRP", "./actpayrecedit.xhtml?type=new&id=-1&customerid="+id+"");
	}

	/**
	 * 刷新方法
	 */
	@Action
	public void refresh() {
		if(editGrid != null){
			this.editGrid.reload();
		}
	}
	
	@Accessible
    public int[] getGridSelIds(){
    	int[] sele = new int[3];
    	sele[0] = 0;
    	sele[1] = 3;
    	sele[2] = 4;
    	return sele;
    }


	@Action
	public void doChangeGridPageSize() {
		String pageStr = (String) AppUtils.getReqParam("page");
		if(!StrUtils.isNull(pageStr) && StrUtils.isNumber(pageStr)) {
			//alert("pageStr:"+pageStr);
			Integer page = Integer.parseInt(pageStr);
			this.editGrid.setRows(page);
			gridLazyLoad = false;
			this.editGrid.rebind();
			//			//记录选择的行数到个人设置
			String mbeanId = this.getMBeanName();
			String girdId = mbeanId+".editGrid.pagesize";
			try {
				ConfigUtils.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				this.editGrid.reload();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			alert("Wrong pagesize：" + pageStr);
		}
	}

	@SaveState
	public Integer gridPageSize = 1000;


	@Override
	public Integer getGridPageSize() {
		String mbeanId = this.getMBeanName();
		String girdId = mbeanId + ".editGrid.pagesize";
		String pageSize;
		try {
			pageSize = ConfigUtils.findUserCfgVal(girdId, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
			return gridPageSize;
		}
		if (!StrUtils.isNull(pageSize) && StrUtils.isNumber(pageSize)) {
			Integer page = Integer.parseInt(pageSize);
			gridPageSize = page;
			return page;
		} else {
			return gridPageSize;
		}
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
			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(this.qryMap));

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
