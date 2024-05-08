package com.scp.view.module.report;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.model.sys.SysReport;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.report.operateserviceBean", scope = ManagedBeanScope.REQUEST)
public class OperateServiceBean extends GridFormView {

	@SaveState
	@Accessible
	public SysReport selectedRowData = new SysReport();

	@SaveState
	@Accessible
	public String info;

	@Action
	public void grid_ondblclick() {

		this.pkVal = getGridSelectId();
		doServiceFindData();
		info = selectedRowData.getInfo();
		showReportChoosen(info);
	}

	@Bind
	private UIIFrame dtlIFrame;

	public void showReportChoosen(String url) {

		if (StrUtils.isNull(url)) {
			return;
		}

		dtlIFrame.setSrc("./service/" + info + ".xhtml");
		update.markAttributeUpdate(dtlIFrame, "src");
	}

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		String blankUrl = AppUtils.getContextPath()
				+ "/pages/module/common/blank.html";
		dtlIFrame.setSrc(blankUrl);
		update.markAttributeUpdate(dtlIFrame, "src");

	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.sysReportMgrService.sysReportDao
				.findById(this.pkVal);

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

}
