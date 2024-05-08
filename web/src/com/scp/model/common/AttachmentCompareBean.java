package com.scp.model.common;

import java.util.List;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.common.attachmentcompareBean", scope = ManagedBeanScope.REQUEST)
public class AttachmentCompareBean extends FormView {
	
	@Bind
	@SaveState
	private Long jobid;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String jobid = AppUtils.getReqParam("linkid");
			if (!StrUtils.isNull(jobid)) {
				this.jobid = Long.parseLong(jobid);
				this.update.markUpdate(UpdateLevel.Data, "jobid");
			}
		}
	}
	
	@Bind(id="leftFile")
    public List<SelectItem> getLeftFile() {
    	try {
			return CommonComBoxBean.getComboxItems("d.id","COALESCE((SELECT x.name FROM sys_role x WHERE x.id = d.roleid LIMIT 1),'')||d.filename","COALESCE((SELECT x.name FROM sys_role x WHERE x.id = d.roleid LIMIT 1),'')||d.filename","sys_attachment d","WHERE d.isdelete = false AND d.linkid = " + this.jobid + " and (contenttype ILIKE '%pdf%' OR filename ILIKE '%.pdf' OR filename ILIKE '%.png' OR filename ILIKE '%.jpg' OR filename ILIKE '%.jpeg')","ORDER BY d.id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Bind(id="rightFile")
    public List<SelectItem> getRightFile() {
    	try {
			return CommonComBoxBean.getComboxItems("d.id","COALESCE((SELECT x.name FROM sys_role x WHERE x.id = d.roleid LIMIT 1),'')||d.filename","COALESCE((SELECT x.name FROM sys_role x WHERE x.id = d.roleid LIMIT 1),'')||d.filename","sys_attachment d","WHERE d.isdelete = false AND d.linkid = " + this.jobid + " and (contenttype ILIKE '%pdf%' OR filename ILIKE '%.pdf' OR filename ILIKE '%.png' OR filename ILIKE '%.jpg' OR filename ILIKE '%.jpeg')","ORDER BY d.id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
}
