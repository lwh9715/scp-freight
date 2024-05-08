package com.scp.view.module.finance.clspro;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.fs.FsConfigShare;
import com.scp.model.finance.fs.FsConfigSharedtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.finance.clspro.shareprofitdtlBean", scope = ManagedBeanScope.REQUEST)
public class ShareProfitdtlBean extends MastDtlView {

	

	@SaveState
	@Accessible
	public FsConfigSharedtl selectedRowData = new FsConfigSharedtl();

	@Override
	public void init() {
		mData = new FsConfigShare();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh();
		}else{
			addMaster();
		}
	}

	@Override
	public void addMaster() {
		this.mData = new FsConfigShare();
		this.mPkVal = -1l;
		((FsConfigShare) this.mData).setActsetid(AppUtils.getUserSession().getActsetid());
		super.addMaster();
	}

	@Override
	public void add() {
		this.selectedRowData = new FsConfigSharedtl();
		if(this.mPkVal==-1l){
			MessageUtils.alert("请先保存主表");
			return;
		}
		super.add();
		this.selectedRowData.setShareid(this.mPkVal);
	}

	@Override
	public void refreshMasterForm() {
		this.mData = serviceContext.fsConfigShareMgrService.fsConfigShareDao
				.findById(this.mPkVal);
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = serviceContext.fsConfigShareMgrService.fsConfigSharedtlDao.findById(this.pkVal);
	}

	@Override
	public void doServiceSaveMaster() {
		
		serviceContext.fsConfigShareMgrService.saveData((FsConfigShare) mData);

		this.mPkVal = ((FsConfigShare) mData).getId();
		
		
		this.alert("OK");
	}

	@Override
	protected void doServiceSave() {
		if (this.mPkVal == -1l)
			return;

		serviceContext.fsConfigShareMgrService.saveDatadtl(selectedRowData);
		this.alert("OK");
		this.add();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND shareid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	@Action
	public void addHFee() {
		if(this.mPkVal == -1l){
			this.alert("Please save master first!");
			return;
		}
		
		this.alert("OK");
		refresh();
	}

	@Override
	public void delMaster() {
		serviceContext.fsConfigShareMgrService.removeData(this.mPkVal);
		this.addMaster();
		this.alert("OK");
	}
	

	@Override
	public void del() {
		serviceContext.fsConfigShareMgrService.removeDatadtl(this.pkVal);
		this.add();
		this.alert("OK");
		refresh();
	}
	
	@Bind(id="com")
    public List<SelectItem> getCom() {
		try {
			return CommonComBoxBean.getComboxItems("d.id","COALESCE(code,'')||'/'||COALESCE(namec,'') ","sys_corporation AS d","WHERE 1=1 AND iscustomer = false AND ishare = TRUE","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
}
