package com.scp.view.sysmgr.company;

import java.util.HashMap;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.sysmgr.company.companyeditBean", scope = ManagedBeanScope.REQUEST)
public class CompanyeditBean extends MastDtlView {
	@Bind
	private String mzccode;

	@SaveState
	@Accessible
	public SysCorporation selectedRowData = new SysCorporation();

	@Override
	public void refreshForm() {

	}
	
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			Map map = new HashMap();
			try {
				map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT mzccode FROM sys_corpext WHERE customerid = "+selectedRowData.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(map.containsKey("mzccode") && null != map.get("mzccode")){
				mzccode = String.valueOf(map.get("mzccode"));
			}
		}
	}



	@Bind
	public UIIFrame deptIframe;
	
	@Bind
	public UIIFrame attachmentIframe;

	@Action
	public void showDept() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.xhtml";
				deptIframe.load(blankUrl);
			}else{
				deptIframe.load( "../dept/dept.xhtml?id=" + this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void showAttachmentIframe() {
		try {
			if (this.mPkVal == -1l) {
				String blankUrl = AppUtils.getContextPath()
						+ "/pages/module/common/blank.xhtml";
				attachmentIframe.load(blankUrl);
			} else {
				attachmentIframe.load(AppUtils.getContextPath()
						+ "/pages/module/common/attachment.xhtml?linkid="
						+ this.mPkVal);
			}
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void init() {
		selectedRowData = new SysCorporation();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
		} else {
			addMaster();
		}
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.customerMgrService.sysCorporationDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		showDept();
		showAttachmentIframe();
	}
	
	@Autowired(required=false)
    private StringRedisTemplate stringRedisTemplate;

	@Override
	public void doServiceSaveMaster() {
		
		try {
			serviceContext.customerMgrService.saveData(selectedRowData);
			this.mPkVal = selectedRowData.getId();
			this.refreshMasterForm();
			if(stringRedisTemplate != null)stringRedisTemplate.getConnectionFactory().getConnection().flushDb();
			this.alert("ok");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	@Override
	public void save() {
		//新增mzc编码字段维护
		if(!StrUtils.isNull(mzccode)){
			String sql = "UPDATE sys_corpext SET mzccode = '"+mzccode+"' WHERE customerid = " +selectedRowData.getId();
			serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
		}
		doServiceSaveMaster();
	}

	public void addMaster() {
		this.selectedRowData =new SysCorporation();
		this.selectedRowData.setIscustomer(false);
		this.selectedRowData.setParentid(100L);
		showDept();
		showAttachmentIframe();
	}

	@Override
	public void delMaster() {
		serviceContext.customerMgrService.removeDate(this.mPkVal);
		this.addMaster();
		this.alert("OK");
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh() {
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
	}

}
