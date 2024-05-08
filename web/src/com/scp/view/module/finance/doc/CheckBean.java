package com.scp.view.module.finance.doc;

import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.ConstantBean.Module;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsVch;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.doc.checkBean", scope = ManagedBeanScope.REQUEST)
public class CheckBean extends GridFormView {

	@SaveState
	@Accessible
	public FsVch selectedRowData = new FsVch();

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@SaveState
	@Accessible
	public String qryYear;
	
	@SaveState
	@Accessible
	public String qryPeriodS;
	
	@SaveState
	@Accessible
	public String qryPeriodE;
	
	@SaveState
	@Accessible
	public Date vchDateS;
	
	@SaveState
	@Accessible
	public Date vchDateE;
	
	@Bind
	public UIButton vchCheck;
	
	@Bind
	public UIButton vchCheckCancel;
	
	@Bind
	public UIButton vchbatchCheck;
	
	@Bind
	public UIButton vchbatchCheckCancel;
	
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.vchMgrService.fsVchDao
				.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.vchMgrService.saveData(selectedRowData);
		this.alert("OK");
	}

	@Bind
	public UIIFrame iframe;
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			initCtrl();
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}//当前帐套信息
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			this.grid.reload();
			String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
					+AppUtils.getUserSession().getActsetid();
			Map m = this.serviceContext.daoIbatisTemplate
			.queryWithUserDefineSql4OnwRow(sql);
			qryYear = m.get("year").toString();
			qryPeriodS = m.get("period").toString();
			qryPeriodE = m.get("period").toString();
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			this.refresh();
		}
	}

	private void initCtrl(){
		vchCheckCancel.setDisabled(true);
		vchCheck.setDisabled(true);
		vchbatchCheckCancel.setDisabled(true);
		vchbatchCheck.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fs_vch.getValue())) {
			if (s.endsWith("_check")) {
				vchCheckCancel.setDisabled(false);
				vchCheck.setDisabled(false);
				vchbatchCheckCancel.setDisabled(false);
				vchbatchCheck.setDisabled(false);
			}
		}
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.getGridSelectId());
		String winId = "_fsin";
		String url = "";
		//不能编辑
		
		if(selectedRowData.getSrctype().equals("S")||selectedRowData.getSrctype().equals("A")){
			url = "/pages/module/finance/doc/indtl_readonly.xhtml?id="+this.getGridSelectId();
			 
		}else{
			url = "./indtl.xhtml?id="+this.getGridSelectId();
		}
		
		
		iframe.load(url);
		
	}

	@Action
	public void add() {
		super.grid_ondblclick();
		String winId = "_fsin";
		String url = "./indtl.xhtml";
		iframe.load(url);
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid();
		if(!StrUtils.isNull(qryYear)) {
			qry += "\nAND year = " + qryYear; // 年查询
		}
		if(!StrUtils.isNull(qryPeriodS) || !StrUtils.isNull(qryPeriodE)) {
			// 期间查询
			qry += "\nAND period BETWEEN " + (StrUtils.isNull(qryPeriodS)?0:qryPeriodS) + " AND " + (StrUtils.isNull(qryPeriodE)?12:qryPeriodE);
		}
		// 凭证日期查询
		if(vchDateS != null || vchDateE!= null) {
			qry += "\nAND singtime BETWEEN '" + (vchDateS==null ? "0001-01-01" : vchDateS)
				+ "' AND '" + (vchDateE==null ? "9999-12-31" : vchDateE) + "'";
		}
		m.put("qry", qry);
		return m;
	}

	@Action(id = "editWindow", event = "onclose")
	private void editWindowClose() {
		refresh();
	}

	/*
	 * 审核/取消审核
	 */
	@Action
	public void checkAction() {
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fs_vch.getValue())) {
			if (s.endsWith("_check")) {
				String[] ids = this.grid.getSelectedIds();
				if (ids == null || ids.length == 0) {
					MessageUtils.alert("Please choose one!");
					return;
				}
				String type = AppUtils.getReqParam("type");
				Boolean flag;
				if ("T".equals(type)) {
					flag = true;
				} else {
					flag = false;
				}
				String sql = "";
				String usercode = AppUtils.getUserSession().getUsercode();
				for (String id : ids) {
					sql = "\nUPDATE fs_vch SET ischeck = " + flag
							+ ",checktime = NOW(),checkter = '" + usercode
							+ "' WHERE id = " + id + ";";
					try {
						this.serviceContext.vchMgrService.fsVchDao.executeSQL(sql);
					} catch (Exception e) {
						MessageUtils.showException(e);
						this.refresh();
						continue;
					}
				}
			}
		}
		MessageUtils.alert("OK");
		this.refresh();
	}
	
	@Override
	public void clearQryKey() {
		qryYear = "";
		qryPeriodS = "";
		qryPeriodE = "";
		vchDateS = null;
		vchDateE = null;
		super.clearQryKey();
	}
	
	
	/*
	 * 批量审核/批量取消审核
	 */
	@Action
	public void batchcheckAction() {
		boolean isError = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fs_vch.getValue())) {
			if (s.endsWith("_check")) {
				String type = AppUtils.getReqParam("batchtype");
				Boolean flag;
				if ("T".equals(type)) {
					flag = true;
				} else {
					flag = false;
				}
				String sql = "";
				String usercode = AppUtils.getUserSession().getUsercode();
				sql = "\nUPDATE fs_vch SET ischeck = " +flag+ ",checktime = NOW(),checkter = '" + usercode+ "' " +
						"WHERE actsetid = "+AppUtils.getUserSession().getActsetid()+ " AND year = "+qryYear+" AND period BETWEEN "+ (StrUtils.isNull(qryPeriodS)?0:qryPeriodS) + " AND " + (StrUtils.isNull(qryPeriodE)?12:qryPeriodE) + ";";
				try {
					this.serviceContext.vchMgrService.fsVchDao.executeSQL(sql);
				} catch (Exception e) {
					isError = true;
					MessageUtils.showException(e);
					this.refresh();
				}
			}
		}
		if(isError == false){
			MessageUtils.alert("OK");
			this.refresh();
		}
	}
}
