package com.scp.view.module.finance;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIEditDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.FinaPrepaid;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.prepaideditBean", scope = ManagedBeanScope.REQUEST)
public class PrepaidEditBean extends GridView {
	
	@Accessible
	@SaveState
	public FinaPrepaid data;
	
	@Bind
	public Long agentcorpid;
	
	
	@Bind
	private Boolean isRpDateCheck = false;
	
	@Bind
	@SaveState
	private Long prepaid = -1l;
	
	@Bind
	private String customerid = "-1";
	
//	@Bind
//	public UIButton del;
	
	@Bind
	public UIButton save;
	
	@Bind
	private String customercode;
	
	@Bind
	private String customername;
	
	
	@Bind
	@SaveState
	public boolean ispublic = false;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack){
			String type = (String) AppUtils.getReqParam("type");;
			//if(!StrUtils.isNull(type))super.applyGridUserDef("pages.module.finance.actpayreceditBean.arapGrid", "arapGridJsvar");
			String id = AppUtils.getReqParam("id");
			if(!StrUtils.isNull(id)&&!id.equals("null")){
				prepaid = Long.valueOf(id);
			}
			if(!StrUtils.isNull(AppUtils.getReqParam("customerid"))){
				customerid = (String) AppUtils.getReqParam("customerid");
			}
			
			if("edit".equals(type)){
				this.data = serviceContext.finaPrepaidMgrService.finaPrepaidDao.findById(prepaid);
				agentcorpid = this.data.getCorpid_account();
				if(this.data == null)add();
				this.customerid = String.valueOf(data.getClientid());
				this.isRpDateCheck = this.data.getRpdate() == null ? false:true;
			}else{
				add();
			}
			
			SysCorporation sco = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(customerid));
			customername = StrUtils.isNull(sco.getNamec())?sco.getNamee():sco.getNamec();;
			customercode = sco.getCode() + "/" + sco.getAbbr();
			qryMap.clear();
			clearQryKey();
			this.update.markUpdate(UpdateLevel.Data,"prepaid");
			this.update.markUpdate(UpdateLevel.Data,"customerid");
			this.update.markUpdate(UpdateLevel.Data,"customercode");
			this.update.markUpdate(UpdateLevel.Data,"customername");
			this.update.markUpdate(UpdateLevel.Data,"editPanel");
			this.update.markUpdate(UpdateLevel.Data, "agentcorpid");
		}
	}
	
	
	@Action
	public void grid_ondblclick() {
//		chooseArapWin.show();
	}
	
	@Bind(id="bank")
    public List<SelectItem> getBank() {
		String corpid = data.getCorpid().toString();
    	try {
			return CommonComBoxBean.getComboxItems("d.id","d.code ||'/'||  COALESCE(d.abbr,'')","_dat_bank AS d","WHERE (d.corpid IS NULL OR d.corpid="+corpid+")","" );
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	/**
	 * 刷新方法
	 */
	@Override
	public void refresh() {
		if(prepaid>0){
			this.data = (FinaPrepaid) serviceContext.finaPrepaidMgrService.finaPrepaidDao.findById(prepaid);
		}
		this.rpSumGrid.reload();
		this.update.markUpdate(UpdateLevel.Data,"f1");
	}
	
	@Action
	public void add(){
		this.data = new FinaPrepaid();
		data.setClientid(Long.valueOf(customerid));
		agentcorpid = null;
		this.data.setSingtime(Calendar.getInstance().getTime());
		try {
			this.data.setCorpid(this.serviceContext.accountsetMgrService.fsActsetDao.findById(AppUtils.getUserSession().getActsetid()).getCorpid());
		} catch (Exception e) {
			this.data.setCorpid(AppUtils.getUserSession().getCorpid());
		}
		prepaid = -1l;
		this.rpSumGrid.reload();
		Browser.execClientScript("addInit();");
		//this.update.markUpdate(true,UpdateLevel.Data,"editPanel");
		this.update.markUpdate(true,UpdateLevel.Data,"prepaid");
		
//		del.setDisabled(false);
		save.setDisabled(false);
//		delBatch.setDisabled(false);
	}
	
	@Action
	public void save(){
		if(prepaid != -1l){
			data.setId(prepaid);
		}
		try {
			this.serviceContext.finaPrepaidMgrService.save(data , modifiedDataRpSum , false);
			prepaid = data.getId();
			this.refresh();
		} catch (Exception e) {
			if(data != null && data.getId() > 0){
				prepaid = data.getId();
			}
			MessageUtils.showException(e);
			this.refresh();
		}
	}
	
	@Action
	public void del(){
		if(prepaid == -1l){
			MessageUtils.alert("Data is not save,please save before!");
			return;
		}
		try {
			this.serviceContext.finaPrepaidMgrService.finaPrepaidDao.remove(data);
			MessageUtils.alert("Current is deleted!");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Bind
	private UIEditDataGrid rpSumGrid;
	
	
	@Bind(id = "rpSumGrid", attribute = "modifiedData")
	@SaveState
    public List<Map> modifiedDataRpSum;
    
	
	@Bind(id = "rpSumGrid", attribute = "dataProvider")
	public GridDataProvider getRpSumDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".grid.rpsum";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhereRpSum(), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				return 1000;
			}
		};
	}
	
	public Map getQryClauseWhereRpSum(){
		Map map = super.getQryClauseWhere(qryMap);
		map.put("prepaid", prepaid);
		String qry = StrUtils.getMapVal(map, "qry");
		map.put("qry", qry);
		return map;
	}

	
	@Override
	public void qryRefresh() {
		super.qryRefresh();
	}

	
	@Action
	public void generateRP() {
		if(prepaid>0){
			try {
				String actpayrecid = serviceContext.finaPrepaidMgrService.createRP(prepaid+"" , AppUtils.getUserSession().getUsercode());
				MessageUtils.alert("生成成功");
				String url = AppUtils.chaosUrlArs("../finance/actpayreceditnew.xhtml") + "&type=edit&id=" + actpayrecid;
				AppUtils.openWindow("_showPrepaiActpayrec_" + actpayrecid,url);
			} catch (Exception e) {
				MessageUtils.showException(e);
				e.printStackTrace();
			}
		}else{
			MessageUtils.alert("请先保存!");
			return;
		}
	}
}
