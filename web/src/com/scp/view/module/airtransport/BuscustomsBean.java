package com.scp.view.module.airtransport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.UIPanelGrid;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.component.widget.UIForm;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.base.MultiLanguageBean;
import com.scp.dao.ship.BusCustomsDao;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusCustoms;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
@ManagedBean(name = "pages.module.airtransport.buscustomsBean", scope = ManagedBeanScope.REQUEST)
public class BuscustomsBean extends FormView{
	
	@Resource
	private BusCustomsDao busCustomsDao;

	@SaveState
	@Accessible
	public BusCustoms selectedRowData = new BusCustoms();


	/**
	 * 报关单号下拉
	 */
	@Bind
    @SelectItems
    @SaveState
    private List<SelectItem> customsnoses;

	/**
	 * 工作id
	 */
	@SaveState
	@Accessible
	public Long jobid;
	
	
	/**
	 * 状态描述（I：初始；F：完成）
	 */
	@Bind
	@SaveState
	public String customstatedesc;
	
	@Bind
	private UIButton saveMaster;
	
	@Bind
	private UIButton delMaster;
	
	
	
	@Bind
	@SaveState
	@Accessible
	public String dtlContent;
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			init();
		}
	}

	
	public void init() {
		this.pkVal = -1l;
		selectedRowData = new BusCustoms();
		String jobid = AppUtils.getReqParam("id");
		if(!StrUtils.isNull(jobid)){
			this.jobid = Long.valueOf(jobid);
			this.initCombox();
			if(this.jobid != null) {
				FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
				this.selectedRowData.setJobid(this.jobid);
				update.markUpdate(true, UpdateLevel.Data, "containerids");
			}
		}
	}
	
	public void initCombox() {
		if (this.jobid != null) {
			List<BusCustoms> buscustomsList = this.serviceContext.busCustomsMgrService.getBusCustomsListByJobid(this.jobid);
			if(buscustomsList != null && buscustomsList.size() > 0) {
				List<SelectItem> items = new ArrayList<SelectItem>();
				for(BusCustoms bc : buscustomsList) {
					items.add(new SelectItem(bc.getNos(), bc.getNos()));
				}
				items.add(new SelectItem(null, ""));
				this.customsnoses = items;
				if(this.pkVal == -1L) {
					this.pkVal = buscustomsList.get(0).getId();
				}
				this.refresh();
			} else {
				this.add();
			}
		}
	}

	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	
	@Bind
	public UIPanelGrid panelGrid;
	
	@Bind
	public UIForm from;
	
	public void add() {
		super.add();
		this.selectedRowData = new BusCustoms();
		this.selectedRowData.setJobid(this.jobid);
		// 设置默认签单时间为当天
		this.selectedRowData.setSingtime(new Date());
		// 设置默认状态为“初始”
		this.selectedRowData.setCustomstate("I");
		this.customstatedesc = (String)l.m.get("初始");
		this.pkVal = -1l;
		this.refresh();
		refresh();
		
	}

	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.add();
		} else {
			try {
				serviceContext.busCustomsMgrService.removeDate(selectedRowData
						.getId());
				this.add();
				this.initCombox();
				MessageUtils.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	/*
	 * 保存
	 */
	public void save() {
		//地域标记,默认设为C,中国
		this.selectedRowData.setAreatype("C");
		//拿到页面上的报关公司id
		Long customid = this.selectedRowData.getCustomid();
		//根据id拿到公司的namec赋值到customabbr
		if(customid != null) {
			SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(customid);
			this.selectedRowData.setCustomabbr(sc.getNamec());
		}
		//保存
		this.serviceContext.busCustomsMgrService.saveData(this.selectedRowData);
		this.pkVal = this.selectedRowData.getId();
		this.initCombox();
		
		MessageUtils.alert("OK");
	}
	
	
	@Bind
	public UIWindow formdefineWindow;
	
	@Bind
	public UIIFrame formdefineIframe;
	
	@Action
	public void showformdefine(){
		formdefineIframe.setSrc("/scp/pages/module/formdefine/dynamicform.xhtml?m="+this.getMBeanName()+"&p="+this.pkVal);
		formdefineWindow.show();
		update.markAttributeUpdate(formdefineIframe, "src");
	}
	

	@SuppressWarnings("deprecation")
	@Action
	public void refresh() {
		if(this.pkVal != -1L) {
			this.selectedRowData = this.busCustomsDao.findById(this.pkVal);
			if(this.selectedRowData != null) {
				if("I".equals( this.selectedRowData.getCustomstate())) {
					this.customstatedesc = (String)l.m.get("初始");
				} else {
					this.customstatedesc = (String)l.m.get("完成");
				}
			}
			if(this.selectedRowData.getCustomid()!=null&&this.selectedRowData.getCustomid()>0){
				SysCorporation syscorp = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getCustomid());
				if(syscorp!=null){
					Browser.execClientScript("$('#customer_input').val('"+syscorp.getNamec()+"');");
				}
			}
		}
		update.markUpdate(true, UpdateLevel.Data, "panelGrid");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}
	
	
	@Accessible
	@SaveState
	public Map dynamicObject = new HashMap<String, Object>();
	
	
	



	@Override
	public void del() {
		
	}

	
	/*
	 * 报关单号下拉变化时，更新报关信息
	 */
	@Action
    private void changeCustomsInfo() {
		String customsnos = this.selectedRowData.getNos();
        if(!StrUtils.isNull(customsnos)) {
        	try {
				this.selectedRowData = this.serviceContext.busCustomsMgrService.busCustomsDao.findByNo(customsnos);
				if(this.selectedRowData != null) {
					this.pkVal = this.selectedRowData.getId();
					update.markUpdate(true, UpdateLevel.Data, "containerids");
					this.refresh();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
	

	
	@Bind(id="reportbuscustomsmat")
	public List<SelectItem> getReportbuscustomsmat(){
		try{
			 return CommonComBoxBean.getComboxItems("d.filename","d.namec",
					"sys_report AS d"," WHERE modcode = 'CustomRpt' AND isdelete = FALSE",
					"order by filename");
		}catch(Exception e){
			MessageUtils.showException(e);
			return null;
		}
	}
	@Bind
	@SaveState
	@Accessible
	public String showbuscustomsname="";
	
	@Action
	public void scanReport() {
		if (showbuscustomsname == null || "".equals(showbuscustomsname)) {
			MessageUtils.alert((String)l.m.get("请选择格式！")+"!");
			return;
		}
		String rpturl = AppUtils.getRptUrl();
		String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/ship/"+showbuscustomsname;
		AppUtils.openWindow("_shipbillReport", openUrl + getArgs());
	}
	private String getArgs() {
		String args="";
		args+="&id="+this.pkVal;
		return args;
	}
}
