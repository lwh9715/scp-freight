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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.salesmgr.pricecustomBean", scope = ManagedBeanScope.REQUEST)
public class PriceCustomBean extends GridView {
	
	
	@Bind
	@SaveState
	public String libid = "-1";
	
	
	
//	@Bind
//	@SaveState
//	public String tips = "";
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String id = AppUtils.getReqParam("libid");
			//tips = AppUtils.getReqParam("tips");
			//this.update.markUpdate(UpdateLevel.Data, "tips");
			if(!StrUtils.isNull(id)) {
				libid = id;
			}
		}
	}
	

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
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
		
		map.put("libid", libid);
		return map;
	}



	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}
	
	
	
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		this.serviceContext.priceFclUseruleService.removeCustomer(libid,ids);
		this.alert("OK!");
		this.grid.reload();
		this.gridFrom.reload();
	}
	
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapFrom = new HashMap<String, Object>();

	
	@Bind
	public UIDataGrid gridFrom;

	@Bind(id = "gridFrom", attribute = "dataProvider")
	protected GridDataProvider getDataFromProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".from.grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMapFrom), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".from.grid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere(qryMapFrom));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@Action
	public void chooseBtn() {
		String[] ids = this.gridFrom.getSelectedIds();
		try{
			this.serviceContext.priceFclUseruleService.addUser(libid,ids);
		}catch(Exception e){
			MessageUtils.showException(e);
		}
		//this.alert("OK!");
		this.grid.reload();
		this.gridFrom.reload();
	}
	
	
	@Action
	public void clearQryKey(){
		if(qryMapFrom != null){
			qryMapFrom.clear();

			update.markUpdate(true, UpdateLevel.Data, "gridFromPanel");
			this.qryRefreshFrom();
		}
	}
	
	@Action
	public void qryRefreshFrom(){
		this.gridFrom.reload();
	}
}
