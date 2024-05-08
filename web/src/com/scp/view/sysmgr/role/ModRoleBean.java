package com.scp.view.sysmgr.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.component.tree.impl.UITreeNode;

import com.scp.base.ApplicationConf;
import com.scp.base.MultiLanguageBean;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.model.sys.SysRole;
import com.scp.service.sysmgr.RoleMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.role.modroleBean", scope = ManagedBeanScope.REQUEST)
public class ModRoleBean extends GridSelectView {
	
	@ManagedProperty("#{roleMgrService}")
	public RoleMgrService roleMgrService;

	@Bind
	@SaveState
	private String roleid;

	@Bind
	public UITree navTree01;
	
	@Inject(value = "l")
	private MultiLanguageBean l;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			String roleid = (String) AppUtils.getReqParam("id");
			String type = (String) AppUtils.getReqParam("type");
			String src = (String) AppUtils.getReqParam("src");
			
			if("user".equals(type)){
				String roleidStr = roleMgrService.sysRoleDao.findIdByRoletypeC(roleid);
				if(!StrUtils.isNull(roleidStr))roleid=roleidStr;
			}
			
			if("orgframework".equals(src)){
				String refid = AppUtils.getReqParam("refid");
				SysRole sysRole = null;
				try {
					sysRole = roleMgrService.sysRoleDao.findOneRowByClauseWhere("linkid = "+refid+" AND isdelete = false");
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
					roleMgrService.sysRoleDao.create(sysRole);
				}
				this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT * FROM f_sys_corporation_framework_sync('refid="+refid+"');");
				roleid=sysRole.getId()+"";
			}
			
			if(!StrUtils.isNull(roleid)) {
				this.roleid = roleid;
				if(!"role".equals(src)){
					navTree01.setValue(new TreeDataProvider(roleid , l , ((ApplicationConf) AppUtils.getBeanFromSpringIoc("applicationConf")).isSaas()));
				}else{
					navTree01.setValue(new TreeDataProvider(roleid , l));
				}
			}
		}
	}

	@Action
	public void saveModule() {

		List<UITree> trees = new ArrayList<UITree>();
		trees.add(navTree01);

		for (UITree uiTree : trees) {
			List<UITreeNode> uiTreeNodes = uiTree.getCheckedNodes();
			List<String> ids = new ArrayList<String>();
			for (UITreeNode uiTreeNode : uiTreeNodes) {
				TreeNode treeNode = (TreeNode) uiTreeNode.getUserData();

				String atsql = "SELECT EXISTS(SELECT 1 FROM sys_modinrole WHERE roleid = " + roleid +
						" AND moduleid = " + treeNode.getId() + " AND isdelete = false) as auth";
				Map map2 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(atsql);

				if ("false".equals(map2.get("auth").toString())) {
					String srsql = "SELECT (CASE WHEN EXISTS(SELECT 1 FROM sys_role sr WHERE sr.id = " + roleid + ") " +
							" THEN (SELECT (SELECT id FROM sys_user WHERE code = sr.code LIMIT 1) FROM sys_role sr " +
							" WHERE sr.id = " + roleid + " LIMIT 1) ELSE " + roleid + " END) as id;";
					Map usermap = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(srsql);

					if (usermap.get("id") != null) {
						String sql2 = "SELECT f_auto_optrace('jobid=" + usermap.get("id") + ";linkid=0;usr=" + AppUtils.getUserSession().getUsername() +
								";tbl=sys_modinrole;col=moduleid;vnew=" + treeNode.getName() +
								";remarks=新增授权：" + treeNode.getName() + "');";
						serviceContext.jobsMgrService.finaJobsDao.executeQuery(sql2);
					}
				}
			}
		}

		roleMgrService.removeRoleModSelect(roleid);
		
		String dmlSql = "";
		for (UITree uiTree : trees) {
			List<UITreeNode> uiTreeNodes = uiTree.getCheckedNodes();
			List<String> ids = new ArrayList<String>();
			for (UITreeNode uiTreeNode : uiTreeNodes) {
				TreeNode treeNode = (TreeNode) uiTreeNode.getUserData();
				ids.add(treeNode.getId());
			}

			uiTreeNodes = uiTree.getPartlyCheckedNodes();
			for (UITreeNode uiTreeNode : uiTreeNodes) {
				TreeNode treeNode = (TreeNode) uiTreeNode.getUserData();
				ids.add(treeNode.getId());
			}

			roleMgrService.saveRoleModSelect(roleid, ids);
		}

		MessageUtils.alert("OK!");
	}
}
