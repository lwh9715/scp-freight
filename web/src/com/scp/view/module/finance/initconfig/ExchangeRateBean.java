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
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.model.finance.fs.FsActset;
import com.scp.model.finance.fs.FsPeriod;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.initconfig.exchangerateBean", scope = ManagedBeanScope.REQUEST)
public class ExchangeRateBean extends GridFormView {

	@SaveState
	@Accessible
	public FsPeriod selectedRowData = new FsPeriod();
	
	

	@SaveState
	@Accessible
	public Long periodid ;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	//不能再初始化时候 qrymap设置 ,这样按钮 清空 ..还是会清理掉,,应该在getclause where 中设置
	@Bind
	@SaveState
	@Accessible
	public Long actsetid;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			
			
			String actsetinit = AppUtils.getReqParam("actsetid");
			//为空是自然加载 ,不为空 是帐套初始化加载
			if(!StrUtils.isNull(actsetinit)){
				actsetid = Long.valueOf(actsetinit);
			}else{
				actsetid = AppUtils.getUserSession().getActsetid();
			}
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			FsActset fsActset = this.serviceContext.accountsetMgrService.fsActsetDao.findById(actsetid);
			this.qryMap.put("year$", fsActset.getYear());
			
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			update.markUpdate(true, UpdateLevel.Data, "actsetid");
			showReportChoosen(this.pkVal);
		}
	}

	@Action
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		showReportChoosen(this.pkVal);
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	@Bind
	private UIIFrame dtlIFrame;

	
	public void showReportChoosen(Long periodid) {
		dtlIFrame.setSrc("exchangerateedit.xhtml?periodid= "+periodid);
		update.markAttributeUpdate(dtlIFrame, "src");
	}


	@Action(id = "dtlDialog",event="onclose")
	private void dtlEditDialogClose() {
		String blankUrl = AppUtils.getContextPath() + "/pages/module/common/blank.html";
		dtlIFrame.setSrc(blankUrl);
		update.markAttributeUpdate(dtlIFrame, "src");
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
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + this.actsetid;
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	public UIWindow shipscheduleWindow;

	@Bind
	public UIDataGrid gridbooksload;
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@Action
	public void syncAction(){
//		String[] ids = this.grid.getSelectedIds();
//		if (ids == null || ids.length <= 0) {
//			MsgUtil.alert("请选择一条数据!");
//			return;
//		}else if(ids.length > 1){
//			MsgUtil.alert("只能勾选一行期间汇率同步!");
//			return;
//		}
//		 if(this.pkVal==null){
//			 MsgUtil.alert("请双击查看该期间的汇率在同步!");
//			 return;
//		 }
		shipscheduleWindow.show();
		this.gridbooksload.reload();
	}
	
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
			MessageUtils.alert("至少选择一行!");
			return;
		}
		try {
			for (String actsetid : chooseactset) {
				String sql = "SELECT f_fs_xrate_sync('actsetidfm="+AppUtils.getUserSession().getActsetid()+";yearfm="+this.qryMap.get("year$")+";actsetidto="+actsetid+";usercode="+AppUtils.getUserSession().getUsercode()+"')AS result";
				serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				shipscheduleWindow.close();
			}
			MessageUtils.alert("ok");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void raterefresh(){
		this.gridbooksload.reload();
	}
	
	@Action
	public void rateclearQryKey(){
		qryMapShip.clear();
		raterefresh();
	}

	@Action
	public void outData(){
			String rpturl = AppUtils.getRptUrl();
			String openUrl = rpturl + "/reportJsp/showPreview.jsp?raq=/finance/acc_all.raq";
			AppUtils.openWindow("_shipbillReport", openUrl);
	}
	
}
