package com.scp.view.module.data;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.CommonRuntimeException;
import com.scp.model.data.DatFeeitem;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "pages.module.data.feeitemBean", scope = ManagedBeanScope.REQUEST)
public class FeeItemBean extends GridFormView {

	@SaveState
	@Accessible
	public DatFeeitem selectedRowData = new DatFeeitem();
	
	
	@Override
	public void add() {
		selectedRowData = new DatFeeitem();
		selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
		super.add();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.feeItemMgrService.datFeeitemDao.findById(this.pkVal);
	}


	@Override
	protected void doServiceSave() {
		serviceContext.feeItemMgrService.saveData(selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Override
	public void del() {
		try {
			serviceContext.feeItemMgrService.datFeeitemDao.remove(selectedRowData);
			this.add();
			this.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);
		String filter = SaasUtil.filterByCorpid("t");
		map.put("filter", filter);
		return map;
	}
	

	@Bind
	public UIWindow join2Window;
	
	
	/**
	 * 只关联不合并
	 */
	@Action
	public void linkConfirm() {
		join(false);
	}
	
	/**
	 * 直接合并
	 */
	@Action
	public void joinConfirm() {
		join(true);
	}
	
	
	private void join(boolean isJoin) {
		String ids[] = this.grid.getSelectedIds();
		try {
			String idto = join2Feeitemid;
			for (String id : ids) {
				//AppUtils.debug(id);
				String sql = new String();
				String idfm = id;
				if(idfm.equals(idto))continue;
				sql = "\nSELECT f_dat_feeitem_join('idfm="+idfm+";idto="+idto+";user="+AppUtils.getUserSession().getUsercode()+ ";jointype="+(isJoin?"J":"L") + "');";
				//AppUtils.debug(sql);
				this.serviceContext.feeItemMgrService.datFeeitemDao.executeQuery(sql);
			}
			this.alert("OK!");
			this.refresh();
			join2Window.close();
			ishow = false;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}



	@SaveState
	private boolean ishow;
	
	@Bind
	@SaveState
	private String join2Feeitemid;

	@Bind
	@SaveState
	private String code;

	/**
	 * 合并费用项目
	 */
	@Action
	public void join() {
		String ids[] = this.grid.getSelectedIds();

		if(ids == null || ids.length < 2) {
			this.alert("请至少选择两行!");
			return;
		}
		ishow = true;
		join2Window.show();
		this.update.markUpdate(UpdateLevel.Data, "join2Feeitemid");
		//ishow = false;
	}
	
	@Bind(id="join2Feeitem")
    public List<SelectItem> getJoin2Feeitem() {
		if(!ishow)return null;
		String ids[] = this.grid.getSelectedIds();
		if(ids == null || ids.length < 2) {
			return null;
		}
		String id = StrUtils.array2List(ids);
    	try {
			return CommonComBoxBean.getComboxItems("d.id","d.code||'/'||COALESCE(name,'')||'/'||COALESCE(namee,'')||'/'||id","dat_feeitem AS d","WHERE d.id IN ("+id+")","ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Action
	public void deleter(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}else{
			try {
				for (String id : ids) {
					serviceContext.feeItemMgrService.removeDate(Long.valueOf(id));
				}
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
			refresh();
		}
	}
	
	
	@Action
	public void isext(){
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请选择记录");
			return;
		}else{
			serviceContext.feeItemMgrService.updateExtBatch(ids);
			refresh();
		}
	}


	@Action
	private void checkRepeatAjaxSubmit() {
		String name = AppUtils.getReqParam("name");
		String value = AppUtils.getReqParam("value");
		if (StrUtils.isNull(value)) {
			this.alert("不能为空!");
			return;
		}
		try {
			String sql = "select * from dat_feeitem df where isdelete =false and code ='" + value + "'";
			List list = serviceContext.feeItemMgrService.daoIbatisTemplate.queryWithUserDefineSql(sql);
			if (list != null && list.size() > 0) {
				code = "";
				update.markUpdate(true, UpdateLevel.Data, "editWindow");
				this.alert("编码重复!");
			}
		} catch (CommonRuntimeException e) {
			String exception = e.getLocalizedMessage();
			this.alert(exception);
		}
	}
}
