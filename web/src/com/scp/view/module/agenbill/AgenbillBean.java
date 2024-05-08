package com.scp.view.module.agenbill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.annotation.SelectItems;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.model.finance.FinaAgenBill;
import com.scp.model.ship.BusShipping;
import com.scp.service.finance.AgenBillMgrService;
import com.scp.service.ship.BusShippingMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.agenbill.agenbillBean", scope = ManagedBeanScope.REQUEST)
public class AgenbillBean extends FormView {
    
    @SaveState
    @Accessible
    public Long jobid;
    
    @ManagedProperty("#{agenBillMgrService}")
    public AgenBillMgrService agenBillMgrService;
    
    @ManagedProperty("#{busShippingMgrService}")
    public BusShippingMgrService busShippingMgrService;
    
    @SaveState
    @Accessible
    public FinaAgenBill selectedRowData = new FinaAgenBill();

    @SaveState
	@Accessible
	public BusShipping busShipping = new BusShipping();
    
    /**
     * 收据单号下拉
     */
    @Bind
    @SelectItems
    @SaveState
    private List<SelectItem> nos;

    
    @SaveState
    @Accessible
    public String sql;
    

	@Bind
	public UIIFrame dtlIframe;
	

	@Bind
	public UIIFrame arapIframe;
	
	
	@Bind
	public UIIFrame chooseFeeIframe;
	

    @Override
    public void beforeRender(boolean isPostBack) {
        super.beforeRender(isPostBack);
     
        if (!isPostBack) {
            String jobid = AppUtils.getReqParam("jobid").trim();
          //  this.pkVal = 1l;
            if(!StrUtils.isNull(jobid)) {

                this.jobid = Long.valueOf(jobid);
            } else {
                this.jobid = -1L;
                }
            }
        this.initCombox();
        refresh();  
     
    }
    
	@Override
	public void refresh() {
		if(this.pkVal==null || this.pkVal < 0){
			dtlIframe.load("../agenbill/agenbilldtl.xhtml?agenbillid=" + 0l);
			arapIframe.load("../agenbill/arapedit.xhtml?agenbillid=" + 0l);
		}else{
			Boolean isCheck = selectedRowData.getIscheck();
//			String temp="";
//			if(isCheck){
//				temp="t";
//			}else{
//				temp="f";
//			}
			dtlIframe.load("../agenbill/agenbilldtl.xhtml?agenbillid=" + this.pkVal+"&isCheck="+isCheck);
			arapIframe.load("../agenbill/arapedit.xhtml?agenbillid=" + this.pkVal+"&isCheck="+isCheck);
		}	
	}

    public void initCombox() {
      if (this.jobid != null && this.jobid != -1) {
          List<FinaAgenBill> finaagenbillList = this.serviceContext.agenBillMgrService.getFinaAgenBillListByJobid(this.jobid);
          if(finaagenbillList != null && finaagenbillList.size() > 0) {
              List<SelectItem> items = new ArrayList<SelectItem>();
              for(FinaAgenBill fb : finaagenbillList) {
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                  String data = sdf.format(fb.getInputtime()==null?Calendar.getInstance().getTime():fb.getInputtime());
                  items.add(new SelectItem(fb.getId(), fb.getNos()+'/'+fb.getInputer()+'/'+data));
              }
              items.add(new SelectItem(null, ""));
              this.nos = items;
              if(this.pkVal == null || this.pkVal == -1L) {
                  this.pkVal = finaagenbillList.get(0).getId();
              }
              this.refreshForm();
          //  this.refreshDtlFrame();
          } else {
              if(this.nos != null)this.nos.clear();
              
              this.add();
          }
      }else {
          List<SelectItem> items = new ArrayList<SelectItem>();
          FinaAgenBill finaAgenBill = this.serviceContext.agenBillMgrService.finaAgenBillDao.findById(this.pkVal);
          if(finaAgenBill != null) {
              items.add(new SelectItem(this.pkVal,finaAgenBill.getNos()));
          }
          this.nos = items;
      }
  }
    
    @Action
    public void del() {
        try {
            serviceContext.agenBillMgrService.removeDate(this.pkVal ,AppUtils.getUserSession().getUsercode());
            this.add();
            MessageUtils.alert("OK");
            this.initCombox();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }
    
    public void refreshForm() {
        selectedRowData =  this.serviceContext.agenBillMgrService.finaAgenBillDao
            .findById(this.pkVal);
		disableAllButton(this.selectedRowData.getIscheck());
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
        update.markUpdate(true, UpdateLevel.Data, "pkVal");
    }
    
	/**
	 * 代理账单单号下拉变化时，更新代理账单信息
	 */
	@Action
    private void changeAgenBillInfo() {
		String agenbillid = AppUtils.getReqParam("id");
        if(!StrUtils.isNull(agenbillid)) {
        	Long agenbillidLong = Long.parseLong(agenbillid);
        	this.selectedRowData = this.serviceContext.agenBillMgrService.finaAgenBillDao
        		.findById(agenbillidLong);
        	if(this.selectedRowData != null) {
        		this.pkVal = this.selectedRowData.getId();
        		this.refreshForm();
        		this.refresh();
        	}
        }
    }
	
	@Bind
	public UIWindow showVchWindow;
	
	@Action
	public void showVchWin(){
		showVchWindow.show();
		vchGrid.reload();
	}
	
	@Action
	public void showVchWinAction(){
		String id = AppUtils.getReqParam("id");
		showVchEdit(id);
	}
	
	@Bind
	public UIDataGrid vchGrid;
	
	@ManagedProperty("#{daoIbatisTemplate}")
	public DaoIbatisTemplate daoIbatisTemplate;

	@Bind(id="vchGrid")
	public List<Map> getVchGrids() throws Exception{
		List<Map> list = null;
		if(this.pkVal > 0){
			Map map = new HashMap();
			map.put("srcid", this.pkVal);
			list = this.daoIbatisTemplate.getSqlMapClientTemplate().queryForList("pages.module.agenbill.agenbillBean.grid.vch", map);
		}
		return list;
	}
	
	@Bind
	public UIIFrame vchIfFrame;
	
	@Bind
	public UIWindow showVchEditWindow;
	
	private void showVchEdit(String id) {
		showVchEditWindow.show();
		vchIfFrame.load("../finance/doc/indtl.xhtml?id="+id+"");
	}
	
	
	@Action
    private void genVch() {
		String sql = "SELECT f_fs_agenbill2vch('id="+this.pkVal+";usercode="+AppUtils.getUserSession().getUsercode()+"') AS vchid;";
		Map m = this.serviceContext.agenBillMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String vchid = StrUtils.getMapVal(m, "vchid");
		showVchWin();
		showVchEdit(vchid);
	}
    
    @Override
    public void add() {
    
    	this.busShipping=this.serviceContext.busShippingMgrService.findByjobId(jobid);
        selectedRowData = new FinaAgenBill();
     
        selectedRowData.setJobid(this.jobid);
        selectedRowData.setSigndate(new Date());
        selectedRowData.setCyidto("CNY");
        selectedRowData.setEtd(this.busShipping.getEtd());
		selectedRowData.setEta(this.busShipping.getEta());
		this.pkVal = -1L;
		refresh();
		
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
        update.markUpdate(true, UpdateLevel.Data, "pkVal");
        
    }

    protected void doServiceFindData() {
        selectedRowData =  this.serviceContext.agenBillMgrService.finaAgenBillDao
            .findById(this.pkVal);
    }
    
    @Override
    public void save() {
        super.save();
        try {
             this.serviceContext.agenBillMgrService.saveData(selectedRowData);
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        this.pkVal = selectedRowData.getId();
    
        //this.initCombox();
        MessageUtils.alert("OK");
        doServiceFindData();
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
        
    }
    
    @Action
    public void saveAction(){
        this.save();
        this.add();
    }
    /**
     * 审核
     * 
     * */
    @Action
	public void ischeckAjaxSubmit() {
		Boolean isCheck = selectedRowData.getIscheck();
		String updater=AppUtils.getUserSession().getUsername();
		String sql="";
		if(this.pkVal == null || this.pkVal == -1L) {
			MessageUtils.alert("请先保存！");
			
		}else{
			if (isCheck) {
				sql = "UPDATE fina_agenbill SET ischeck = TRUE ,checktime = NOW(),checker = '"+updater+"' WHERE id ="+this.pkVal+";";
			}else {
				sql = "UPDATE fina_agenbill SET ischeck = FALSE ,checktime = NULL,checker = NULL WHERE id ="+this.pkVal+";";
			}
			try {
				serviceContext.agenBillMgrService.finaAgenBillDao.executeSQL(sql);
				refreshForm();
			}catch (Exception e) {
				MessageUtils.showException(e);
				selectedRowData.setIscheck(!isCheck);
				selectedRowData.setChecker(isCheck?null:AppUtils.getUserSession().getUsercode());
				selectedRowData.setChecktime(isCheck?null:Calendar.getInstance().getTime());
				update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
				this.disableAllButton(!isCheck);
			}
		}	
	}
	@Bind
	public UIButton save;
	@Bind
	public UIButton del;
	@Override
    public void disableAllButton(Boolean ischeck) {
		save.setDisabled(ischeck);
		del.setDisabled(ischeck);
	}

    @Bind
    public UIWindow editWindow;

    @Action(id = "editWindow", event = "onclose")
    private void dtlEditDialogClose() {
        this.refreshForm();
    }
    
    @Action
    public void genAsJobPorfit(){
    	this.pkVal = this.serviceContext.agenBillMgrService.genAsJobPorfit(jobid);
    	this.refreshForm();
    }
    
    @Action
    public void genProfitAsLink(){
    	this.pkVal = this.serviceContext.agenBillMgrService.genAsJobPorfitLink(jobid);
    	this.refreshForm();
    }
    
    @Action
    public void genAsJobFee(){
		chooseFeeIframe.load("./chooseagentfee.xhtml?id=" + this.jobid);
    	editWindow.show();
    }
}
