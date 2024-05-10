package com.scp.view.module.data;

import com.scp.model.data.DatBank;
import com.scp.model.finance.fs.FsActset;
import com.scp.service.data.BankMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;
import org.operamasks.faces.annotation.*;

import java.util.List;
import java.util.Map;


@ManagedBean(name = "pages.module.data.bankBean", scope = ManagedBeanScope.REQUEST)
public class BankBean extends MastDtlView {

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			String corper = serviceContext.userMgrService
					.getCorporationNameByUserId(AppUtils.getUserSession()
							.getUserid());
			this.qryMap.put("corper", corper);
		}
	}

	@ManagedProperty("#{bankMgrService}")
	public BankMgrService bankMgrService;

	@SaveState
	@Accessible
	public DatBank selectedRowData = new DatBank();

	@Override
	public void add() {
		selectedRowData = new DatBank();
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = bankMgrService.datBankDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			bankMgrService.saveData(selectedRowData);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			alert("Please select a line");
		} else {
			try {
				this.removeDateForBankId(ids);
				this.refresh();
				this.add();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Action
	public void syncAct() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			alert("Please select a line");
		} else {
			try {
				for (String id : ids) {
					DatBank datBank = bankMgrService.datBankDao.findById(Long.valueOf(id));
					List<FsActset> fsActsets = this.serviceContext.accountsetMgrService.fsActsetDao.findAllByClauseWhere("corpid = " + datBank.getCorpid());
					for (FsActset fsActset : fsActsets) {
						String sql = "SELECT f_auto_fixbug_act('type=bank;bankid="+id+";actsetid="+fsActset.getId()+"');";
						bankMgrService.datBankDao.executeQuery(sql);
					}
				}
				
				this.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	

	public void removeDateForBankId(String[] ids) {
		String sql = "UPDATE dat_bank SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id IN ("
				+ StrUtils.array2List(ids) + ");";
		bankMgrService.datBankDao.executeSQL(sql);
	}

	@Override
	public void doServiceSaveMaster() {
	}

	@Override
	public void init() {

	}

	@Override
	public void refreshMasterForm() {

	}
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0 || ids.length > 1) {
			MessageUtils.alert("请选择单行记录");
			return;
		}else{
			bankMgrService.removeDate(getGridSelectId());
			refresh();
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		return AppUtils.filterByCorperChoose(super.getQryClauseWhere(queryMap) , "t");
	}

	
}
