package com.scp.model.common;

import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.common.attachmentbatchBean", scope = ManagedBeanScope.REQUEST)
public class AttachmentBatchBean extends FormView {

	@Bind
	@SaveState
	private String linkid;
	
	@Bind
	@SaveState
	private Long userId;
	
	@Bind
	@SaveState
	private String code;
	
	@Bind
	@SaveState
	private String src;
	
	
	@Bind
	@SaveState
	private float attachmentsize;
    
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			this.code = AppUtils.getReqParam("code");
			src = AppUtils.getReqParam("src");
			this.linkid = AppUtils.getReqParam("linkid");
			
			userId = AppUtils.getUserSession().getUserid();
			
			String str = ConfigUtils.findSysCfgVal("sys_attachment_size");
			if(StrUtils.isNull(str)){
				attachmentsize = 5L;
			}else{
				float size = Float.parseFloat(str);
				attachmentsize = size;
			}
			
			String roleSql = "SELECT d.id FROM sys_role d WHERE d.isdelete = false AND name = '水单' AND d.roletype = 'F' LIMIT 1";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(roleSql);
			roleId_value = StrUtils.getMapVal(map, "id");
			update.markUpdate(true, UpdateLevel.Data, "roleId_value");
		}
	}
	
	@Bind
	@SaveState
	public String fileName;
	
	@Bind
	@SaveState
	public String contentType;
	
	@Bind
	@SaveState
	public String roleId_value;

}