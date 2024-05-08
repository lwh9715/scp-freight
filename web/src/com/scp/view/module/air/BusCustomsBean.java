package com.scp.view.module.air;

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

@ManagedBean(name = "pages.module.air.buscustomsBean", scope = ManagedBeanScope.REQUEST)
public class BusCustomsBean extends FormView {

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
			//dynamicFormBean.initDynamicForm(panelGrid , this.getMBeanName() , dynamicObject);
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
	
	
//	@Inject(value = "pages.module.formdefine.dynamicformBean")
//	protected DynamicFormBean dynamicFormBean;
	
	
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
		
//		String mBeanName = this.getMBeanName();
//		dynamicFormBean.save(mBeanName, dynamicObject, EntityUtil.getTableName(selectedRowData.getClass()));
//		Set<String> set = dynamicObject.keySet();
//		for (String key : set) {
//			Object obj = dynamicObject.get(key);
//			//System.out.println(key + ":" + obj + ":" + obj.getClass().getName());
//			SysFormdefVal sysFormdefVal;
//			try {
//				sysFormdefVal = this.serviceContext.sysFormdefService.sysFormdefValDao.findOneRowByClauseWhere("beaname = '"+mBeanName+"' AND columnname = '"+key+"'");
//			} catch (NoRowException e) {
//				sysFormdefVal = new SysFormdefVal();
//				sysFormdefVal.setId(0l);
//			}
//			sysFormdefVal.setObjtype(obj.getClass().getName());
//			sysFormdefVal.setObjval(obj.toString());
//			sysFormdefVal.setBeaname(mBeanName);
//			sysFormdefVal.setLinkid(this.pkVal);
//			sysFormdefVal.setLinktbl(EntityUtil.getTableName(selectedRowData.getClass()));
//			sysFormdefVal.setColumnname(key);
//			this.serviceContext.sysFormdefService.saveDataDtl(sysFormdefVal);
//		}
		
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
	
	
	
//	public void initDynamicForm(UIPanelGrid panelGrid) {
//		
//		Application app = FacesContext.getCurrentInstance().getApplication();
//		String mBeanName = this.getMBeanName();
//		String sql = "SELECT * FROM sys_formdef where beaname = '"+mBeanName+"' ORDER BY orderno";
//		List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//		
//		
//		//dynamicObject.put("chayanri", Calendar.getInstance().getTime());
//		
//		for (Map map : list) {
//			String inputype = StrUtils.getMapVal(map, "inputype");
//			String inputlable = StrUtils.getMapVal(map, "inputlable");
//			String columnname = StrUtils.getMapVal(map, "columnname");
//			
//			
//			try {
//				SysFormdefVal sysFormdefVal = this.serviceContext.sysFormdefService.sysFormdefValDao.findOneRowByClauseWhere("beaname = '"+mBeanName+"' AND columnname = '"+columnname+"'");
//				
//				String objtype = sysFormdefVal.getObjtype();
//				String objval = sysFormdefVal.getObjval();
//				//System.out.println(objtype + ":" + objval);
//				if("java.lang.String".equals(objtype)){
//					dynamicObject.put(columnname, objval);
//				}
//				if("java.lang.Long".equals(objtype)){
//					Long val = null;
//					if(!StrUtils.isNull(objval) && StrUtils.isNumber(objval)){
//						val = Long.valueOf(objval);
//					}
//					dynamicObject.put(columnname, val);
//				}
//			} catch (NoRowException e) {
//				e.printStackTrace();
//			}
//			
//			
//			
//			if("UICheckBox".equals(inputype)){
//				HtmlOutputLabel label_state = new HtmlOutputLabel();
//				label_state.setValue((String)l.m.get(inputlable));
//				panelGrid.getChildren().add(label_state);
//				UICheckBox comp = new UICheckBox();
//				comp.setValue(dynamicObject.get(columnname));
//				comp.setValueBinding("value", app.createValueBinding("#{"+mBeanName+".dynamicObject['"+columnname+"']}"));
//				panelGrid.getChildren().add(comp);
//			}
//			
//			if("UIDateField".equals(inputype)){
//				HtmlOutputLabel label_state = new HtmlOutputLabel();
//				label_state.setValue((String)l.m.get(inputlable));
//				panelGrid.getChildren().add(label_state);
//				UIDateField comp = new UIDateField();
//				dynamicObject.put(columnname, Calendar.getInstance().getTime());
//				comp.setValue(dynamicObject.get(columnname));
//				comp.setValueBinding("value", app.createValueBinding("#{"+mBeanName+".dynamicObject['"+columnname+"']}"));
//				panelGrid.getChildren().add(comp);
//			}
//			
//			if("UITextField".equals(inputype)){
//				HtmlOutputLabel label_state = new HtmlOutputLabel();
//				label_state.setValue((String)l.m.get(inputlable));
//				panelGrid.getChildren().add(label_state);
//				UITextField comp = new UITextField();
//				comp.setValue(dynamicObject.get(columnname));
//				comp.setValueBinding("value", app.createValueBinding("#{"+mBeanName+".dynamicObject['"+columnname+"']}"));
//				panelGrid.getChildren().add(comp);
//			}
//			
//			if("UITextArea".equals(inputype)){
//				HtmlOutputLabel label_state = new HtmlOutputLabel();
//				label_state.setValue((String)l.m.get(inputlable));
//				panelGrid.getChildren().add(label_state);
//				UITextArea comp = new UITextArea();
//				comp.setValue(dynamicObject.get(columnname));
//				comp.setValueBinding("value", app.createValueBinding("#{"+mBeanName+".dynamicObject['"+columnname+"']}"));
//				panelGrid.getChildren().add(comp);
//			}
//			
//			if("UINumberField".equals(inputype)){
//				HtmlOutputLabel label_state = new HtmlOutputLabel();
//				label_state.setValue((String)l.m.get(inputlable));
//				panelGrid.getChildren().add(label_state);
//				UINumberField comp = new UINumberField();
//				comp.setValue(dynamicObject.get(columnname));
//				comp.setValueBinding("value", app.createValueBinding("#{"+mBeanName+".dynamicObject['"+columnname+"']}"));
//				panelGrid.getChildren().add(comp);
//			}
//		}
		
//		HtmlOutputLabel label_state = new HtmlOutputLabel();
//		label_state.setValue((String)l.m.get("改单"));
//		panelGrid.getChildren().add(label_state);
//		
//		UITextField state_body = new UITextField();
//		state_body.setValue(selectedRowData.getAddress());
//		state_body.setValueBinding("value", app.createValueBinding("#{pages.module.air.buscustomsBean.selectedRowData.address}"));
//		panelGrid.getChildren().add(state_body);
//		
//		UICheckBox uiCheckBox = new UICheckBox();
//		uiCheckBox.setValue(selectedRowData.getLessgoods());
//		uiCheckBox.setValueBinding("value", app.createValueBinding("#{pages.module.air.buscustomsBean.selectedRowData.lessgoods}"));
//		panelGrid.getChildren().add(uiCheckBox);
//		
//		
//		UIDateField uiDateField = new UIDateField();
//		uiDateField.setValue(selectedRowData.getReleasetime());
//		uiDateField.setValueBinding("value", app.createValueBinding("#{pages.module.air.buscustomsBean.selectedRowData.releasetime}"));
//		panelGrid.getChildren().add(uiDateField);
		
//		panelGrid.repaint();
//	}


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
