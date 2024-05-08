package com.scp.view.module.oa;

import java.math.BigDecimal;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.oa.SalaryBill;
import com.scp.service.oa.SalaryBillMgrService;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.MastDtlView;

@ManagedBean(name = "pages.module.oa.salarydtlBean", scope = ManagedBeanScope.REQUEST)
public class SalarydtlBean extends MastDtlView {

	@SaveState
	@Accessible
	public String id;

	@Bind
	@SaveState
	public SalaryBill selectedRowData = new SalaryBill();

	@ManagedProperty("#{salaryBillMgrService}")
	public SalaryBillMgrService salaryBillMgrService;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			id = AppUtils.getReqParam("id");
			this.qryMap.put("id$",id);
		}
		init();
	}

	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND id = " + id;
		m.put("qry", qry);
		return m;
	}

	@Override
	public void doServiceSaveMaster() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		selectedRowData = new SalaryBill();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			
			this.refreshMasterForm();
		

		} else {
			
		}
		
	}

	@Override
	public void refreshMasterForm() {
		this.selectedRowData = salaryBillMgrService.salaryBillDao.findById(this.mPkVal);
		if(selectedRowData!=null){
			selectedRowData.setAmt(decrypt(selectedRowData.getAmt()));
			selectedRowData.setPay_base(decrypt(selectedRowData.getPay_base()));
			selectedRowData.setPay_add_ft(decrypt(selectedRowData.getPay_add_ft()));
			selectedRowData.setPay_add_ovt(decrypt(selectedRowData.getPay_add_ovt()));
			selectedRowData.setPay_add_oth(decrypt(selectedRowData.getPay_add_oth()));
			selectedRowData.setPay_cut_leave(decrypt(selectedRowData.getPay_cut_leave()));
			selectedRowData.setPay_cut_late(decrypt(selectedRowData.getPay_cut_late()));
			selectedRowData.setPay_cut_fund(decrypt(selectedRowData.getPay_cut_fund()));
			selectedRowData.setPay_cut_security(decrypt(selectedRowData.getPay_cut_security()));
			selectedRowData.setPay_cut_tax(decrypt(selectedRowData.getPay_cut_tax()));
			selectedRowData.setPay_cut_oth(decrypt(selectedRowData.getPay_cut_oth()));
			selectedRowData.setAmtadjust(decrypt(selectedRowData.getAmtadjust()));
			selectedRowData.setOverseasadd(decrypt(selectedRowData.getOverseasadd()));

		}
		
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		
	}
	
	private BigDecimal decrypt(BigDecimal key) {
		
		DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils.getBeanFromSpringIoc("daoIbatisTemplate");
		String sql="SELECT f_decrypt("+key+")::NUMERIC(18,2) AS result";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		BigDecimal result =(BigDecimal) m.get("result");
		
		return result;
	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doServiceFindData() {
		
		
		
	}
}
