package com.scp.view.module.salesmgr;

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
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.data.DatLine;
import com.scp.model.price.PriceFclUserule;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.salesmgr.pricefcluseruleBean", scope = ManagedBeanScope.REQUEST)
public class PriceFclUseruleBean extends GridFormView {
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();
	
	@SaveState
	@Accessible
	public PriceFclUserule selectedRowData = new PriceFclUserule();
	
	@Bind
	@SaveState
	public String qrycorporation;
	
	@Bind
	@SaveState
	public String qryinputer;
	
	@Bind
	@SaveState
	public String qryline;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			//this.add();
		}
	}
	
	@Action
	public void user2Libs() {
		
		String[] userIds = this.customGrid.getSelectedIds();
		if (userIds == null || userIds.length <= 0) {
			MessageUtils.alert("请选择客户");
			return;
		}
		
		
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选择运价组");
			return;
		}
		StringBuilder stringBuffer = new StringBuilder();
		for (String uid : userIds) {
			for (String cid : ids) {
				stringBuffer.append("\nINSERT INTO price_fcl_userule_ref(id, useruleid, customerid) SELECT  getid(), "+cid+", "+uid+" FROM _virtual WHERE NOT EXISTS(SELECT 1 FROM price_fcl_userule_ref WHERE useruleid="+cid+" AND customerid="+uid+");");
			}
		}
		////System.out.println(stringBuffer);
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			MessageUtils.alert("OK");
			this.grid.reload();
			this.customGrid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void userClearLibs() {
		String[] userIds = this.customGrid.getSelectedIds();
		if (userIds == null || userIds.length <= 0) {
			MessageUtils.alert("请选择用户");
			return;
		}
		
		
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			MessageUtils.alert("请选择运价组");
			return;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		for (String uid : userIds) {
			for (String cid : ids) {
				stringBuffer.append("\nDELETE FROM price_fcl_userule_ref WHERE useruleid="+cid+" AND customerid="+uid+";");
			}
		}
		////System.out.println(stringBuffer);
		try {
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(stringBuffer.toString());
			MessageUtils.alert("OK");
			this.grid.reload();
			this.customGrid.reload();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	/**
	 * 刷新方法
	 */
	@Action
	public void refreshUser() {
		if (customGrid != null) {
			this.customGrid.reload();
		}
	}

	@Action
	public void clearQryKeyUser() {
		if (qryMapUser != null) {
			qryMapUser.clear();

			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.qryRefresh();
		}
	}
	
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid customGrid;

	@Bind(id = "customGrid", attribute = "dataProvider")
	protected GridDataProvider getUserGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".customGrid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere2(qryMapUser), start,
								limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".customGrid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere2(qryMapUser));
				if (list == null || list.size() < 1)
					return 0;
				Long count = Long.parseLong(list.get(0).get("counts")
						.toString());
				return count.intValue();
			}
		};
	}
	

	@Override
	public void add() {
		selectedRowData = new PriceFclUserule();
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.priceFclUseruleService.priceFclUseruleDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		this.serviceContext.priceFclUseruleService.saveData(selectedRowData);
		this.alert("OK");
	}
	
	
	@Override
	public void del() {
		String ids[] = this.grid.getSelectedIds();
		this.serviceContext.priceFclUseruleService.delete(ids);
		this.grid.reload();
		alert("OK");
	}
	

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		//AppUtils.openWindow("_lib_edit", "./custlibedit.xhtml?libid="+this.getGridSelectId());
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = m.get("qry").toString();
		if(!StrUtils.isNull(qrycorporation)){
			qry += "\nAND EXISTS(SELECT 1 FROM price_fcl_userule_ref x,sys_corporation y " 
				+  "\nWHERE y.isdelete = FALSE AND x.useruleid = b.id AND y.id = x.customerid AND (y.namec ILIKE '%"+qrycorporation+"%' OR y.code ILIKE '%"+qrycorporation+"%' OR y.namee ILIKE '%"+qrycorporation+"%'))";
		}
		if(!StrUtils.isNull(qryinputer)){
			qry += "\nAND EXISTS(SELECT 1 FROM sys_user y WHERE y.code = b.inputer  AND (y.namec ILIKE '%"+qryinputer+"%' OR y.code ILIKE '%"+qryinputer+"%' OR y.namee ILIKE '%"+qryinputer+"%'))";
		}
		if(!StrUtils.isNull(qryline)){
			qry += "\nAND EXISTS(SELECT 1 FROM dat_line WHERE code = ANY(regexp_split_to_array(b.lines,',')) and namec ILIKE '%"+qryline+"%')";
		}
		m.put("qry", qry);
		StringBuilder sb = new StringBuilder();
		
		String filterParent = 
			//开发组的人可以看到所有的，其他的人按录入人条件过滤（上级可以看到所有下级的）
			"\nAND (CASE WHEN EXISTS (SELECT 1 FROM sys_role x ,sys_userinrole y WHERE x.code = 'dev' AND y.roleid = x.id AND y.userid = "+AppUtils.getUserSession().getUserid() + ")" +
			"\nTHEN TRUE "+
			"\nELSE (inputer = ANY(ARRAY(WITH RECURSIVE rc AS ("
			+"\n						SELECT * FROM sys_user WHERE id = "+AppUtils.getUserSession().getUserid()
			+"\n						UNION "
			+"\n						SELECT a.* FROM sys_user a,rc WHERE a.parentid = rc.id"
			+"\n						)"
			+"\n						SELECT code FROM rc "
			+"\n						UNION SELECT '"+AppUtils.getUserSession().getUsercode()+"' "
			+"\n						))"
			+ ") " +
			"\n END)"
			;
		m.put("filter", filterParent);
		return m;
	}

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		Long userid = AppUtils.getUserSession().getUserid();
		
		String linkSalesSql = 
				  "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = a.salesid AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = a.salesid) " //组关联业务员的单，都能看到
				+ ")"
				;
		
		String filterByInputerOrUpdaterConfig = ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2link");
		String filterByInputOrUpdate = "\nOR inputer = '"+AppUtils.getUserSession().getUsercode()+"'" +"\nOR updater = '"+AppUtils.getUserSession().getUsercode()+"'";
		map.put("filter", " AND (salesid = "+userid
				+ ("Y".equals(filterByInputerOrUpdaterConfig)?filterByInputOrUpdate:"")
				+"\n" + ("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2assign"))?"OR EXISTS(SELECT 1 FROM _sys_user_assign y WHERE y.linkid=a.id AND linktype='C' AND y.userid="+userid+")":"")
				+"\n" + ("Y".equals(ConfigUtils.findSysCfgVal("sys_cfg_salesmgr_cus2saleslink"))?linkSalesSql:"")
				
				+"\nOR EXISTS(SELECT 1 FROM _sys_user_assign y WHERE y.linkid=a.id AND linktype='C' AND roletype='S' AND y.userid="+userid+"))");
		return map;
	}
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid lineGrid;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapLin = new HashMap<String, Object>();

	@Bind(id = "lineGrid", attribute = "dataProvider")
	protected GridDataProvider getLineGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.data.lineBean.grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere(qryMapLin), start,
								limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.data.lineBean.grid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere(qryMapLin));
				if (list == null || list.size() < 1)
					return 0;
				Long count = Long.parseLong(list.get(0).get("counts")
						.toString());
				return count.intValue();
			}
		};
	}
	
	@Action
	public void qryLine() {
		this.lineGrid.reload();
	}
	
	@Action
	public void confirm() {
		try {
			String[] ids = this.lineGrid.getSelectedIds();
			if(ids == null){
				MessageUtils.alert("请勾选一条记录！");
				return;
			}
			String lineCode = "";
			for (String id : ids) {
				DatLine dl = serviceContext.lineMgrService.datLineDao.findById(Long.valueOf(id));
				lineCode = lineCode + dl.getCode() + ",";
			}
			lineCode = lineCode.substring(0, lineCode.length()-1);
			this.selectedRowData.setLines(lineCode);
			
			update.markUpdate(true, UpdateLevel.Data, "lines");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
