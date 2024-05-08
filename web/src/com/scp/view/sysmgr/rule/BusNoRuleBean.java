package com.scp.view.sysmgr.rule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.sys.SysBusnodesc;
import com.scp.model.sys.SysBusnorule;
import com.scp.util.DateTimeUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.sysmgr.rule.busnoruleBean", scope = ManagedBeanScope.REQUEST)
public class BusNoRuleBean extends GridFormView {

	@SaveState
	@Accessible
	public SysBusnorule selectedRowData = new SysBusnorule();

	@Bind
	public UICombo qryCorpid;

	public Long getCorpid() {
		if ((this.qryCorpid.getValue() == null)
				|| (this.qryCorpid.getValue().equals(""))) {
			return Long.valueOf(-1L);
		}
		return Long.valueOf(this.qryCorpid.getValue().toString());
	}

	public void add() {
		if (getCorpid().longValue() == -1L) {
			MessageUtils.alert("请先选择公司!");
			return;
		}
		this.selectedRowData = new SysBusnorule();
		this.selectedRowData.setCorpid(getCorpid());
		this.selectedRowData.setSerialno(Integer.valueOf(0));
		this.selectedRowData.setIsyear(true);
		this.selectedRowData.setIsmonth(true);
		this.selectedRowData.setResettype("M");
		this.selectedRowData.setSeriallen((short) 4);
		this.selectedRowData.setYearlen((short)2);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			this.selectedRowData.setDatefm(df.parse(DateTimeUtil.getFirstDay()));
			this.selectedRowData.setDateto(df.parse(DateTimeUtil.getLastDay()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		super.add();
		
	}
	
	
	
	@Action
	public void last() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Map<String,String> map = DateTimeUtil.getFirstday_Lastday_Month(selectedRowData.getDatefm());
			this.selectedRowData.setDatefm(df.parse(map.get("first")));
			this.selectedRowData.setDateto(df.parse(map.get("last")));
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Action
	public void current() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = Calendar.getInstance().getTime();
			date.setMonth(date.getMonth()+1);
			Map<String,String> map = DateTimeUtil.getFirstday_Lastday_Month(date);
			this.selectedRowData.setDatefm(df.parse(map.get("first")));
			this.selectedRowData.setDateto(df.parse(map.get("last")));
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	@Action
	public void next() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = selectedRowData.getDatefm();
			date.setMonth(date.getMonth()+2);
			Map<String,String> map = DateTimeUtil.getFirstday_Lastday_Month(date);
			this.selectedRowData.setDatefm(df.parse(map.get("first")));
			this.selectedRowData.setDateto(df.parse(map.get("last")));
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	protected void doServiceFindData() {
		this.selectedRowData = ((SysBusnorule) this.serviceContext.busNoRuleMgrService.sysBusnoruleDao
				.findById(this.pkVal));
	}

	protected void doServiceSave() {
		this.serviceContext.busNoRuleMgrService.saveData(this.selectedRowData);
		MessageUtils.alert("OK!");
		refresh();
	}
	
	@Action
	public void delGrid() {
		String ids[] = this.grid.getSelectedIds();
		if(ids.length==0 || ids ==null){
			MessageUtils.alert("请选择删除的行");
			return;
		}
		try {
			for (String id : ids) {
				this.serviceContext.busNoRuleMgrService.sysBusnoruleDao.removeDate(Long.valueOf(id));
			}
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	public void del() {
		this.serviceContext.busNoRuleMgrService.sysBusnoruleDao.remove(this.selectedRowData);
		add();
		alert("OK");
		refresh();
	}

	@Bind(id = "busnotypeiddesc")
	public List<SelectItem> getBusnotypeiddesc() {
		try {
			return CommonComBoxBean.getComboxItems("d.id",
					"code||'/'||COALESCE(remark,'')", "sys_busnodesc AS d",
					"WHERE corpid =" + getCorpid() + "AND isdelete = false",
					"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		return null;
	}

	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry = qry + "\nAND corpid =" + getCorpid();
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	public UIIFrame iframe;
	
	
	@Bind
	public UIWindow iframeWindow;
	
	@Action
	public void addBusnotype() {
		iframeWindow.show();
		iframe.load("./busnotype.xhtml");
		update.markAttributeUpdate(iframe, "src");
	}
	
	@Action
	public void release() {
		String ids[] = this.grid.getSelectedIds();
		if(ids.length==0 || ids ==null){
			MessageUtils.alert("请勾选需要发布的行");
			return;
		}
		try {
			for (String id : ids) {
				SysBusnorule sysBusnorule = this.serviceContext.busNoRuleMgrService.sysBusnoruleDao.findById(Long.valueOf(id));
				SysBusnodesc sysBusnodesc = this.serviceContext.busNoRuleMgrService.sysBusnodescDao.findById(sysBusnorule.getBusnotypeid());
				this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_auto_busno_release('corpid=0;code="+sysBusnodesc.getCode()+"');");
			}
			this.refresh();
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		Browser.execClientScript("resettypeOnchange()");
	}
	
	
}