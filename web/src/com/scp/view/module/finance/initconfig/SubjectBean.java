package com.scp.view.module.finance.initconfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsAct;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.subjectBean", scope = ManagedBeanScope.REQUEST)
public class SubjectBean extends GridFormView {

	@SaveState
	@Accessible
	public FsAct selectedRowData = new FsAct();
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Bind
	@SaveState
	@Accessible
	public String actypename = "资产";

	@Override
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			this.qryMap.put("actypename", actypename);
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			this.grid.reload();
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid();
		m.put("qry", qry);
		return m;
	}

	@Override
	public void add() {
		this.selectedRowData = new FsAct();
		this.selectedRowData.setActsetid(AppUtils.getUserSession().getActsetid());
		this.selectedRowData.setCyid("*");
		String sql = "SELECT id FROM fs_actype WHERE name='"+actypename+"' AND actsetid="+AppUtils.getUserSession().getActsetid();
		Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		// 默认为当前选择的这个类别
		this.selectedRowData.setActypeid(Long.valueOf(m.get("id").toString()));
		String[] ids = this.grid.getSelectedIds();
		if(ids != null && ids.length != 0  ) {
			// 父节点为列表选择的那一行，如果没有选择则空着
			Long selectId = Long.valueOf(ids[0]);
			this.selectedRowData.setParentid(selectId);
			FsAct fsact = this.serviceContext.subjectMgrService.fsActDao.findById(selectId);
			this.selectedRowData.setDir(fsact.getDir());
		}
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = this.serviceContext.subjectMgrService.fsActDao
				.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			this.serviceContext.subjectMgrService.saveData(selectedRowData);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void showActdtl(){
		actypename = AppUtils.getReqParam("actypename");
		this.qryMap.put("actypename", actypename);
		this.refresh();
	}
	
	@Action
	public void del(){
		String ids[] = this.grid.getSelectedIds();
		if(ids.length==0 || ids ==null){
			MessageUtils.alert("请选择删除的行");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.subjectMgrService.removeDate(Long.valueOf(id));
			}
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind
	public UIButton importData;
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_fs_act";
				String args = AppUtils.getUserSession().getActsetid() + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Action
	public void outData(){
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl + "/reportJsp/showPreview.jsp?raq=/finance/fs_act.raq";
			AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}
	
	private String getArgs() {
		String args = "";
		args += "&actsetid=" + AppUtils.getUserSession().getActsetid();
		return args;
	}
	@Action
	protected void startImport() {
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
		importDataBatch();
	}
	/*@Override
	public void processUpload(FileUploadItem fileUploadItem) throws IOException {
		super.processUpload(fileUploadItem);
		importDataText = analyzeExcelData(1, 1);
	}*/
	
	@Bind
	public UIWindow shipscheduleWindow;
	
	@Bind
	public UIDataGrid gridbooksload;
	
	@Action
	public void syncAction(){
		shipscheduleWindow.show();
		this.gridbooksload.reload();
	}
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@Bind(id = "gridbooksload", attribute = "dataProvider")
	protected GridDataProvider getGridHisEmailDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.initconfig.exchangerateBean.gridbooksload.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.initconfig.exchangerateBean.gridbooksload.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND id NOT IN("+AppUtils.getUserSession().getActsetid()+")";
		m.put("qry", qry);
		return m;
	}
	
	@Action
	public void setQuery(){
		String[] chooseactset = this.gridbooksload.getSelectedIds();
		if (chooseactset == null || chooseactset.length <= 0) {
			MessageUtils.alert("至少选择一行账套!");
			return;
		}
		String ids[] = this.grid.getSelectedIds();
		if(ids.length==0 || ids ==null){
			MessageUtils.alert("请选择要同步的科目");
			return;
		}
		try {
			String sql = "SELECT f_fs_act_sync('actsetidfm="+AppUtils.getUserSession().getActsetid()+";subjiectids="+StrUtils.array2List(ids)
							+";actsetidto="+StrUtils.array2List(chooseactset)+";usercode="+AppUtils.getUserSession().getUsercode()+"')AS result";
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
//			shipscheduleWindow.close();
			MessageUtils.alert("ok");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
