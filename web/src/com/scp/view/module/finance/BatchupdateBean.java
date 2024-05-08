package com.scp.view.module.finance;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.ApplicationConf;
import com.scp.base.CommonComBoxBean;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.customer.CustomerChooseBean;
@ManagedBean(name = "pages.module.finance.batchupdateBean", scope = ManagedBeanScope.REQUEST)
public class BatchupdateBean extends GridView {
	
	@SaveState
	private String iswarehouse;
	
	@SaveState
	private Long customerid;
	
	@Bind
	@SaveState
	private String customercode;
	
	
	@Bind
	@SaveState
	private String remarks = "";
	
	@Bind
	@SaveState
	private Long feeitemid;
	
	@Bind
	@SaveState
	private String arapdate;
	
	@SaveState
	private String jobid="-1";
	
	@SaveState
	private String ids;
	
	@Bind
	@SaveState
	private Long corpid;
	
	@Bind
	@SaveState
	private Long corpid2;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			iswarehouse = AppUtils.getReqParam("iswarehouse");
			ids = AppUtils.getReqParam("ids");
			jobid = AppUtils.getReqParam("jobid");
		}
	}
	
	@Action
	public void changearap() { 
		String araptype = AppUtils.getReqParam("araptype");
		String ppcctype = AppUtils.getReqParam("ppcctype");
		String currency = AppUtils.getReqParam("currency");
		String sharetype = AppUtils.getReqParam("sharetype");
		Long feeitemid = StrUtils.isNull(AppUtils.getReqParam("feeitemid"))?-1L:Long.parseLong(AppUtils.getReqParam("feeitemid"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date arapdate = null;
		try {
			arapdate = StrUtils.isNull(this.arapdate)?sdf.parse("2001-01-01"):sdf.parse(this.arapdate);
		} catch (Exception e1) {
			try {
				arapdate = sdf.parse("2001-01-01");
			} catch (Exception e) {
			}
		}
		String customercode = AppUtils.getReqParam("customercode");
		BigDecimal pricenotax = StrUtils.isNull(AppUtils.getReqParam("pricenotax"))?new BigDecimal(-1):new BigDecimal(AppUtils.getReqParam("pricenotax"));
		BigDecimal parities = StrUtils.isNull(AppUtils.getReqParam("parities"))?new BigDecimal(-1):new BigDecimal(AppUtils.getReqParam("parities"));
		BigDecimal piece = StrUtils.isNull(AppUtils.getReqParam("piece"))?new BigDecimal(-1):new BigDecimal(AppUtils.getReqParam("piece"));
		BigDecimal corpid = StrUtils.isNull(AppUtils.getReqParam("corpid"))?new BigDecimal(-1):new BigDecimal(AppUtils.getReqParam("corpid"));
		BigDecimal corpid2 = StrUtils.isNull(AppUtils.getReqParam("corpid2"))?new BigDecimal(-1):new BigDecimal(AppUtils.getReqParam("corpid2"));
		
		String payplace = AppUtils.getReqParam("payplace");
		try {
			serviceContext.arapMgrService.editParities(ids.split(","),araptype,ppcctype,currency,feeitemid,arapdate,customerid == null ?-1L:customerid,customercode,pricenotax,parities,piece,corpid,corpid2,AppUtils.getUserSession().getUsercode() , remarks,sharetype,payplace);
			refresh();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	@Bind
	private String qryCustomerKey;

	@Action
	public void customerQry() {
		this.customerService.qry(qryCustomerKey);
		this.customerGrid.reload();
	}

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@Bind
	public UIWindow showCustomerWindow;

	@Bind
	public UIDataGrid customerGrid;

	@Bind(id = "customerGrid", attribute = "dataProvider")
	public GridDataProvider getCustomerGridDataProvider() {
		String filter = "";
		String value = ConfigUtils.findSysCfgVal("sys_factoryneedcheck");
		if(value!=null&&value.equals("Y")){
			filter = "\nAND (CASE WHEN isfactory = TRUE THEN ischeck = TRUE ELSE 1=1 END)";//neo 20161118 if factory filter uncheck data
		}
		return this.customerService.getAllCustomerDataProvider(filter);
	}

	@Action
	public void showCustomer() {
		String customercode = (String) AppUtils.getReqParam("customercode");
		qryCustomerKey = customercode;
		int index = qryCustomerKey.indexOf("/");
		if (index > 1)
			qryCustomerKey = qryCustomerKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
			showCustomerWindow.show();
			customerQry();
			Browser.execClientScript("sc.focus");
			return;
		}
		try {
			List<Map> cs = customerService.findCustomer(qryCustomerKey);
			if (cs.size() == 1) {
//				this.selectedRowData.setCustomerid((Long) cs.get(0).get("id"));
				this.customercode = cs.get(0).get("code") + "/" + cs.get(0).get("abbr");
//				this.update.markUpdate(UpdateLevel.Data, "customerid");
				this.update.markUpdate(UpdateLevel.Data, "customercode");

				showCustomerWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryCustomerKey");
				showCustomerWindow.show();
				customerQry();
				Browser.execClientScript("sc.focus");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	@Action
	public void customerGrid_ondblclick() {
		Object[] objs = customerGrid.getSelectedValues();
		Map m = (Map) objs[0];
//		this.selectedRowData.setCustomerid((Long) m.get("id"));
		this.customerid = (Long) m.get("id");
		this.customercode=(String) m.get("code")+"/"+(String) m.get("abbr");
//		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customercode");
		showCustomerWindow.close();
	}
	
	/**
	 * 费用名称
	 * 
	 * @return
	 */
	@Bind(id = "feeitemtype")
	public List<SelectItem> getFeeitemtype() {
		try {
			if(null == iswarehouse) {
				iswarehouse = "N";
			}
			
			return CommonComBoxBean.getComboxItems("d.id", "COALESCE(d.code,'')||'/'||COALESCE(d.name,'')",
					"dat_feeitem AS d", "WHERE ( iswarehouse = '" + iswarehouse
							+ "' OR ispublic = TRUE )", "ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	@Bind
	@SaveState
	private String qryFeeitemKey;

	@Action
	public void feeitemQry() {
		this.customerService.qryFeeitem(qryFeeitemKey);
		this.feeitemGrid.reload();
	}

	

	@Bind
	public UIWindow showFeeitemWindow;

	@Bind
	public UIDataGrid feeitemGrid;
	

	@Bind(id = "feeitemGrid", attribute = "dataProvider")
	public GridDataProvider getFeeitemGridDataProvider(){
		return this.customerService.getFeeitemDataProvider("\nAND iswarehouse='"+iswarehouse+"'");
	}
	

	@Action
	public void showFeeitem() {
		String feeitemcode = (String) AppUtils.getReqParam("feeitemcode");
		qryFeeitemKey = feeitemcode;
		int index = qryFeeitemKey.indexOf("/");
		if (index > 1)
			qryFeeitemKey = qryFeeitemKey.substring(0, index);

		String type = (String) AppUtils.getReqParam("type");
		if ("1".equals(type)) {
			this.update.markUpdate(UpdateLevel.Data, "qryFeeitemKey");
			showFeeitemWindow.show();
			feeitemQry();
			Browser.execClientScript("sf.focus");
			return;
		}
		try {
			List<Map> cs = customerService.findFeeitem(qryFeeitemKey,iswarehouse);
			if (cs.size() == 1) {
				Map map = cs.get(0);
				this.feeitemid = (Long) map.get("id");
				this.update.markUpdate(UpdateLevel.Data, "feeitemid");
				showFeeitemWindow.close();
			} else {
				this.update.markUpdate(UpdateLevel.Data, "qryFeeitemKey");
				showFeeitemWindow.show();
				feeitemQry();
				Browser.execClientScript("sf.focus");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
	@Action
	public void feeitemGrid_ondblclick() {
		Object[] objs = feeitemGrid.getSelectedValues();
		Map map = (Map) objs[0];
		this.feeitemid = ((Long) map.get("id"));
		this.update.markUpdate(UpdateLevel.ValueOnly, "feeitemid");
		showFeeitemWindow.close();
	}
	
	@Resource
	public ApplicationConf applicationConf;
	
	/**
	 * 费用结算地，只提取工作单里面的接单公司或操作公司，2选1
	 * @return
	 */
	@Bind(id="corpAbbr")
    public List<SelectItem> getCorpAbbr() {
    	try {
    		String qry = "\nAND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE x.corpid = d.id AND x.ischoose = TRUE AND userid ="+AppUtils.getUserSession().getUserid()+"))";
    		if(!applicationConf.isSaas())qry = "";//非saas模式不控制
			return CommonComBoxBean.getComboxItems("d.id","COALESCE(d.abbcode,'')","sys_corporation d ,fina_jobs j ","WHERE d.iscustomer = false " +
					"\nAND (CASE WHEN EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = j.corpid)" +
					"\n 	          AND EXISTS(SELECT 1 FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice()) AND x.id = j.corpidop) "+
					"\n 		THEN  "+
					"\n 			d.id = ANY(SELECT id FROM sys_corporation x WHERE x.iscustomer = FALSE AND x.isdelete = FALSE AND (x.corpidlink IS NOT NULL OR x.id = f_getheadoffice())) "+
					"\n 		ELSE  "+
					"\n			(d.id = j.corpid OR d.id = j.corpidop OR d.id = j.corpidop2 OR d.id = ANY(SELECT corpid FROM fina_corp c WHERE c.jobid = j.id AND c.isdelete = FALSE)) "+
					"\n 		END) " +
					"\nAND j.id = "+this.jobid+""+ qry,"ORDER BY d.id");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
}
