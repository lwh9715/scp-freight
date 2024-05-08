package com.scp.view.module.finance.clspro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.clspro.shareprofitendBean", scope = ManagedBeanScope.REQUEST)
public class ShareProfitendBean extends GridView {

	

	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	@Bind
	@SaveState
	public String year;

	@Bind
	@SaveState
	public String period;
	
	@Bind
	@SaveState
	public Long profitshareid;
	
	
	
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}

			// 根据当前登录用户，找对应的帐套，过滤兑换率列表
			String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
					+ AppUtils.getUserSession().getActsetid();
			Map m;
			String year = "";
			String period = "";
			try {
				m = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				year = m.get("year").toString();
				period = m.get("period").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//设置 默认值    如果period = 1 那么 year -1 ,period = 12;
			if(Integer.valueOf(period)==1){
				this.year = (Integer.valueOf(year)-1)+"";
				this.period = 12+"";
			}else{
				this.year = (Integer.valueOf(year))+"";
				this.period =(Integer.valueOf(period)-1)+"";
			}
			
			

			update.markUpdate(true, UpdateLevel.Data, "year");
			update.markUpdate(true, UpdateLevel.Data, "period");
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");

			
		}
	}

	@Action
	public void showActdtl(){
		this.period = AppUtils.getReqParam("period");
		this.year = AppUtils.getReqParam("year");
		//arapIframe.load("../clspro/shareprofitlist.xhtml?period="+this.period+"&year="+this.year+"");
		refresh();
	}
	
	@Override
	public void grid_ondblclick() {
	}
	
	
	@Action
	public void add() {
	}
	
	@Action 
	public void setshare(){
		String url = "./shareprofit.xhtml";
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}
	
	
	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}

	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		//m.put("actsetid", AppUtil.getUserSession().getActsetid());
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND year = " + this.year ;
		qry += "\nAND period = " + this.period ;
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid() ;
		m.put("qry", qry);
		return m;
	}

	/*
	 */
	@Action
	public void autoGenerate() {
		if(this.profitshareid==null){
			MessageUtils.alert("请先下拉选择一个分成比例!");
			return;
		}
		
		//检查分成比例是否100%
		String ratesql = "SELECT SUM(rate) AS ratetltal FROM fs_config_sharedtl WHERE shareid = "+profitshareid+"";
	try {
		Map m = this.serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(ratesql);
		String total = ((BigDecimal) m.get("ratetltal")).toString();
		Double d = Double.valueOf(total);
		if (d < 1) {
			MessageUtils.alert("分成比例总和为100%,请检查!");
			return;
		}
	} catch (Exception e) {
		MessageUtils.alert("分成比例总和为100%,请检查!");
		return;
	}
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("Please choose one!");
			return;
		}else{
		String usercode = AppUtils.getUserSession().getUsercode();
		String ids2 = StrUtils.array2List(ids);
			String sql = "\nSELECT f_fs_arap2vch_share('vchid="+ids2+";usercode="+usercode+";shareid="+this.profitshareid+"');";
			try {
				this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
				MessageUtils.alert("OK");
				this.refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
		}
	}
	
	
	@Bind(id="shareid")
    public List<SelectItem> getShareid() {
		try {
			return CommonComBoxBean.getComboxItems("t.id","t.code||'  '||'['||(SELECT string_agg(d.comiddesc||' '||d.ratedesc,';') FROM _fs_config_sharedtl d WHERE d.shareid = t.id)||']'","fs_config_share AS t","WHERE 1=1 AND t.actsetid = "+AppUtils.getUserSession().getActsetid()+"","ORDER BY t.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }


}
