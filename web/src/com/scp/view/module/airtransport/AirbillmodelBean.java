package com.scp.view.module.airtransport;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;

import com.scp.model.sys.SysReport;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridFormView;
@ManagedBean(name = "pages.module.airtransport.airbillmodelBean", scope = ManagedBeanScope.REQUEST)
public class AirbillmodelBean extends GridFormView{

	@SaveState
	@Accessible
	public SysReport selectedRowData = new SysReport();
	
	@SaveState
	public Long userid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		this.userid = AppUtils.getUserSession().getUserid();
	}
	
	@Action
	public void qryAdd(){
		selectedRowData.setId(0L);
		selectedRowData.setIsleaf("N");
		selectedRowData.setModcode("air");
		selectedRowData.setTemplete("[{\"title\":\"ORG\",\"backgroundImage\":\"url('images/HAB.jpg')\",\"printpc\":0,\"container\":{\"left\":188.609375,\"top\":57,\"right\":1886,\"bottom\":1226,\"width\":1697.390625,\"height\":1169},\"elements\":[]}]");
		this.add();
	}
	
	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.sysReportMgrService.sysReportDao.findById(this.pkVal);
		
	}

	@Override
	protected void doServiceSave() {
		serviceContext.sysReportMgrService.saveData(selectedRowData);
		
	}
	
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null) {
			MessageUtils.alert("请勾选一条记录!");
			return;
		}
		StringBuilder builder = new StringBuilder();
		for(int j=0;j<ids.length;j++){
			String id = ids[j];
			builder.append("UPDATE sys_report SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";");
		}
		this.serviceContext.userMgrService.sysUserDao.executeSQL(builder.toString());
		this.alert("OK!");
		this.refresh();
	}
	
	
	@Action
	public void design() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null) {
			MessageUtils.alert("请勾选一条记录!");
			return;
		}
		
		this.pkVal = getGridSelectId();
		this.selectedRowData = serviceContext.sysReportMgrService.sysReportDao.findById(this.pkVal);
		String winId = "_design_jobsair";
		String url = "../../../reportEdit/file/TemplateEditair.jsp?rp="+selectedRowData.getFilename()+"&u="+this.userid;
		AppUtils.openWindow(winId, url);
	}
	
}
