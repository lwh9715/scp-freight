package com.scp.view.sysmgr.company;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.tree.impl.UITree;
import org.operamasks.faces.component.tree.impl.UITreeNode;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.MultiLanguageBean;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysDepartment;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.company.orgframeworkBean", scope = ManagedBeanScope.REQUEST)
public class OrgFrameworkBean extends GridSelectView {
	

	@Bind
	public UITree navTree01;
	
	@Inject(value = "l")
	private MultiLanguageBean l;

	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			refresh();
		}
	}
	
	@Override
	public void refresh() {
		navTree01.setValue(new TreeDataProvider("" , l, ""));
		navTree01.setLoadAllNodes(true);
		navTree01.repaint();
	}
	
	@Bind
	public UIIFrame dtlIFrame;
	
	@Action
	public void navTree01_onclick() {
		UITreeNode node = navTree01.getEventNode();
		if (node != null && node.getUserData() != null) {
			TreeNode selectedNode = (TreeNode) node.getUserData();
			String id = selectedNode.getId();
			dtlIFrame.setSrc("/scp/pages/sysmgr/user/user.aspx?src=orgframework&refid="+id);
			update.markAttributeUpdate(dtlIFrame, "src");
			update.markUpdate(true, UpdateLevel.Data, dtlIFrame);
			
			Browser.execClientScript("authorWindowJsVar.hide()");
			Browser.execClientScript("editDeptWindowJsVar.hide()");
		}
	}
	
	
	@SaveState
	public SysDepartment dept;
	
	
	@Bind
	public String deptPkVal;
	
	@Action
	public void addDept(){
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		String id = treeNode.getId();
		//System.out.println(id + "--" +treeNode.getLevel() + "---" + treeNode.getName());
		
		dept = new SysDepartment();
		//公司
		if("0".equals(treeNode.getLevel())){
			SysCorporation org = this.serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(id));
			dept.setSysCorporation(org);
		}else{//部门
			SysDepartment department = this.serviceContext.sysDeptService.sysDepartmentDao.findById(Long.valueOf(id));
			dept.setParentid(department.getId());
			dept.setSysCorporation(department.getSysCorporation());
		}
		update.markUpdate(true, UpdateLevel.Data, "editDeptPanel");
		Browser.execClientScript("editDeptWindowJsVar.show()");
	}
	
	
	
	@Action
	public void delDept(){
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		String id = treeNode.getId();
		//System.out.println(id + "--" +treeNode.getLevel() + "---" + treeNode.getName());
		//公司
		if("0".equals(treeNode.getLevel())){
			this.alert("Please select department!");
			return;
		}else{//部门
			SysDepartment department = this.serviceContext.sysDeptService.sysDepartmentDao.findById(Long.valueOf(id));
			this.serviceContext.sysDeptService.sysDepartmentDao.remove(department);
			this.alert("OK");
			this.refresh();
		}
	}
	
	@Bind
	public UIIFrame authorIFrame;
	
	@Action
	public void author(){
		Browser.execClientScript("authorWindowJsVar.hide()");
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		String id = treeNode.getId();
		//System.out.println(id + "--" +treeNode.getLevel() + "---" + treeNode.getName());
		String type = "dept";
		if("0".equals(treeNode.getLevel())){//公司
			type = "org";
		}else{//部门
			type = "dept";
		}
		authorIFrame.setSrc("/scp/pages/sysmgr/role/modrole.aspx?src=orgframework&type="+type+"&refid="+id);
		update.markAttributeUpdate(authorIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, authorIFrame);
		
		Browser.execClientScript("authorWindowJsVar.show()");
	}
	
	@Action
	public void linkSales(){
		Browser.execClientScript("authorWindowJsVar.hide()");
		if(navTree01.getSelectedNode() == null){
			this.alert("Please choose one!");
			return;
		}
		TreeNode treeNode = (TreeNode)navTree01.getSelectedNode().getUserData();
		String id = treeNode.getId();
		//System.out.println(id + "--" +treeNode.getLevel() + "---" + treeNode.getName());
		String type = "dept";
		if("0".equals(treeNode.getLevel())){//公司
			type = "org";
		}else{//部门
			type = "dept";
		}
		authorIFrame.setSrc("/scp/pages/sysmgr/user/rolelinksales.aspx?src=orgframework&type="+type+"&refid="+id);
		update.markAttributeUpdate(authorIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, authorIFrame);
		
		Browser.execClientScript("authorWindowJsVar.show()");
	}
	

	@Action
	public void saveDept() {
		this.serviceContext.sysDeptService.sysDepartmentDao.createOrModify(dept);
		Browser.execClientScript("editDeptWindowJsVar.hide()");
		this.alert("OK");
		this.refresh();
		
	}



	
}
