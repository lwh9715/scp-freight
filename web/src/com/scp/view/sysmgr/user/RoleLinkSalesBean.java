package com.scp.view.sysmgr.user;

import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.component.tree.impl.UITreeNode;

import com.scp.base.MultiLanguageBean;
import com.scp.exception.MoreThanOneRowException;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysCustlibRole;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysRole;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;
import com.scp.view.sysmgr.company.TreeDataProvider;
import com.scp.view.sysmgr.company.TreeNode;

@ManagedBean(name = "pages.sysmgr.user.rolelinksalesBean", scope = ManagedBeanScope.REQUEST)
public class RoleLinkSalesBean extends GridSelectView  {
	
	@SaveState
	@Bind
	public String roleid;
	
	@Bind
	@SaveState
	public String usercorper;
	
	@Bind
	@SaveState
	public String userdepter;
	
	@Bind
	@SaveState
	public String userjobdesc;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if(!isPostBack){
			String roleidStr = (String) AppUtils.getReqParam("id");
			if(!StrUtils.isNull(roleidStr))roleid=roleidStr;
			
			String type = (String) AppUtils.getReqParam("type");
			String src = (String) AppUtils.getReqParam("src");
			
			if("orgframework".equals(src)){
				String refid = AppUtils.getReqParam("refid");
				SysRole sysRole = null;
				try {
					sysRole = this.serviceContext.roleMgrService.sysRoleDao.findOneRowByClauseWhere("linkid = "+refid+" AND isdelete = false");
				} catch (NoRowException e) {
					sysRole = new SysRole();
					sysRole.setLinkid(Long.valueOf(refid));
					if("org".equalsIgnoreCase(type)){
						SysCorporation org = this.serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(refid));
						sysRole.setRoletype("O");
						sysRole.setCode(org.getCode());
						sysRole.setName(org.getNamec());
					}else{
						SysDepartment department = this.serviceContext.sysDeptService.sysDepartmentDao.findById(Long.valueOf(refid));
						sysRole.setRoletype("D");
						sysRole.setCode(department.getCode());
						sysRole.setName(department.getName());
					}
					this.serviceContext.roleMgrService.sysRoleDao.create(sysRole);
				}
				this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT * FROM f_sys_corporation_framework_sync('refid="+refid+"');");
				roleid=sysRole.getId()+"";
			}
			refreshTree();
			////System.out.println(useridStr);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Action
	public void save() {
		String[] ids = this.grid.getSelectedIds();
		for (String id : ids) {
			try {
				this.serviceContext.custLibMgrService.sysCustlibRoleDao.findOneRowByClauseWhere("custlibid = "+id + " AND roleid = "+roleid+" AND parentid IS NULL");
			} catch (MoreThanOneRowException e) {
				
			} catch (NoRowException e) {
				SysCustlibRole sysCustlibRole = new SysCustlibRole();
				sysCustlibRole.setCustlibid(Long.valueOf(id));
				sysCustlibRole.setRoleid(Long.valueOf(roleid));
				this.serviceContext.custLibMgrService.sysCustlibRoleDao.create(sysCustlibRole);
			}
		}
		refresh2();
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
				String sqlId = getMBeanName() + ".gridRight.page";
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

	
	
	@Bind
	public UITree navTree01;
	
	@Inject(value = "l")
	private MultiLanguageBean l;
	
	@Action
	public void refresh2(){
		this.gridRight.reload();
		refreshTree();
	}
	
	@Action
	public void remove() {
		String[] ids = this.gridRight.getSelectedIds();
		if(ids==null ||ids.length==0){
			MessageUtils.alert("please choose one");
			return;
		}
		String dmlSql = "\nDELETE FROM sys_custlib_role WHERE id IN ("+StrUtils.array2List(ids)+");";
		this.serviceContext.custLibMgrService.sysCustlibRoleDao.executeSQL(dmlSql);
		//MessageUtils.alert("OK!");
		refresh();
		refresh2();
		refreshTree();
	}
	
	@Action
	public void navTree01_onclick() {
		UITreeNode node = navTree01.getEventNode();
		if (node != null && node.getUserData() != null) {
			TreeNode selectedNode = (TreeNode) node.getUserData();
			String id = selectedNode.getId();
			
		}
	}
	
	@Action
	public void refreshTree() {
		navTree01.setValue(new TreeDataProvider("" , l, company));
		navTree01.setLoadAllNodes(true);
		navTree01.repaint();
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
			this.serviceContext.custLibMgrService.sysCustlibRoleDao.findOneRowByClauseWhere("orgid = "+id + " AND roleid = "+roleid+"");
		} catch (MoreThanOneRowException e) {
			
		} catch (NoRowException e) {
			SysCustlibRole sysCustlibRole = new SysCustlibRole();
			sysCustlibRole.setOrgid(Long.valueOf(id));
			sysCustlibRole.setRoleid(Long.valueOf(roleid));
			this.serviceContext.custLibMgrService.sysCustlibRoleDao.create(sysCustlibRole);
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


