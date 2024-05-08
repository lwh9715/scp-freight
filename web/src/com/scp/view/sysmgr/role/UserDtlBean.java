package com.scp.view.sysmgr.role;

import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.MultiLanguageBean;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysUserinrole;
import com.scp.service.sysmgr.RoleMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.sysmgr.company.TreeDataProvider;
import com.scp.view.sysmgr.company.TreeNode;

@ManagedBean(name = "pages.sysmgr.role.userdtlBean", scope = ManagedBeanScope.REQUEST)
public class UserDtlBean extends GridView {
	
	@Bind
	public UITree navTree01;
	
	@Inject(value = "l")
	private MultiLanguageBean l;
	
	
	@ManagedProperty("#{roleMgrService}")
	public RoleMgrService roleMgrService;
    
	@Bind
	@SaveState
	public String roleid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			String roleid = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(roleid)) {
				this.roleid = roleid;
				refreshTree();
				this.update.markUpdate(UpdateLevel.Data,"roleid");
			}
		}
	}
	
	

	@Action
	public void save() {
		String[] ids = this.grid.getSelectedIds();
		if(ids==null ||ids.length==0){
			MessageUtils.alert("please choose one");
			return;
		}

		//找出新增的权限，添加授权日志
		for (String userid : ids) {
			String atsql = "SELECT EXISTS(SELECT 1 FROM sys_userinrole WHERE userid = " + userid + " AND roleid = " + roleid + " AND isdelete = false) as auth";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(atsql);
			if ("false".equals(map.get("auth").toString())) {
				String atsql2 = "SELECT name FROM sys_role WHERE id = " + roleid + " AND isdelete = false LIMIT 1;";
				Map map2 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(atsql2);
				String sql2 = "SELECT f_auto_optrace('jobid=" + userid + ";linkid=0;usr=" + AppUtils.getUserSession().getUsername() +
						";tbl=sys_userinrole;col=roleid;vnew=" + map2.get("name").toString() + ";remarks=新增授权：" + map2.get("name").toString() + "');";
				serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql2);
			}
		}

		roleMgrService.saveRoleUserSelect(roleid , ids);
		refresh();
		refresh2();
	}
	
	@Action
	public void remove() {
		String[] ids = this.gridRight.getSelectedIds();
		if(ids==null ||ids.length==0){
			MessageUtils.alert("please choose one");
			return;
		}
		roleMgrService.removeRoleUserSelect(ids, roleid);
		//MessageUtils.alert("OK!");
		refresh();
		refresh2();
		refreshTree();
	}
	
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap){
		Map<String, String> map = super.getQryClauseWhere(queryMap);
		map.put("roleid", roleid);
		return map;
	}
	
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMap2 = new HashMap<String, Object>();

	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid gridRight;

	@Bind(id = "gridRight", attribute = "dataProvider")
	protected GridDataProvider getRightGridDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".right.grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere2(qryMap2), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				return 10000;
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		map.put("roleid", roleid);
		return map;
	}

	@Override
	public void refresh() {
		this.grid.reload();
		
	}
	
	@Action
	public void refresh2(){
		this.gridRight.reload();
		refreshTree();
	}
	
	@Action
	public void clearQryKey2() {
		if (qryMap2 != null) {
			qryMap2.clear();
			update.markUpdate(true, UpdateLevel.Data, "gridRightPanel");
			this.refresh2();
		}
	}
	
	@Action
	public void navTree01_onclick() {
//		UITreeNode node = navTree01.getEventNode();
//		if (node != null && node.getUserData() != null) {
//			TreeNode selectedNode = (TreeNode) node.getUserData();
//			String id = selectedNode.getId();
//			
//		}
	}
	
	@Action
	public void refreshTree() {
		navTree01.setValue(new TreeDataProvider("" , l, company));
		navTree01.setLoadAllNodes(true);
		navTree01.repaint();
//		navTree01.reload();
	}
	
	@Action
	public void selectTreeNode() {
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		String id = treeNode.getId();
		//System.out.println(id + "--" +treeNode.getLevel() + "---" + treeNode.getName());
		try {
			roleMgrService.sysUserinroleDao.findOneRowByClauseWhere("orgid = "+id + " AND isdelete = false AND userid is null AND roleid = "+roleid+"");
		} catch (MoreThanOneRowException e) {
			e.printStackTrace();
		} catch (NoRowException e) {
			SysUserinrole sysUserinrole = new SysUserinrole();
			sysUserinrole.setSysRole(roleMgrService.sysRoleDao.findById(Long.valueOf(roleid)));
			sysUserinrole.setOrgid(Long.valueOf(id));
			roleMgrService.sysUserinroleDao.create(sysUserinrole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.gridRight.reload();
	}
	
	@Bind
	@SaveState
	public String company = "";
	
	@Action
	public void inquiry() {
		refreshTree();
	}
	
	
}
