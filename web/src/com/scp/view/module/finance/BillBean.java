package com.scp.view.module.finance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.sys.SysLogDao;
import com.scp.model.data.DatAccount;
import com.scp.model.finance.FinaBill;
import com.scp.model.sys.SysCorporation;
import com.scp.model.sys.SysLog;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.view.module.ship.BusBillBean;

@ManagedBean(name = "pages.module.finance.billBean", scope = ManagedBeanScope.REQUEST)
public class BillBean extends GridFormView {
	
	@SaveState
	@Accessible
	public FinaBill selectedRowData = new FinaBill();
	
	@Override
	public void refresh() {
		super.refresh();
//		FinaBill data = new FinaBill();
//		data = this.serviceContext.billMgrService.finaBillDao.findById(pkVal);
//		this.isconfirm = data.getIsconfirm();
	}
	
	@Override
	public void add() {
		selectedRowData = new FinaBill();
		selectedRowData.setBilldate(new Date());
		selectedRowData.setCurrency(AppUtils.getUserSession().getBaseCurrency());
		
		refreshDtlFrame(true);
		super.add();
	}

	@Action
	public void clientGrid_onrowselect() {
		String id = clientGrid.getSelectedIds()[0];
		this.qryMap.put("clientid$", id);
		this.grid.reload();
	}
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		refreshDtlFrame(false);
	}

	private void refreshDtlFrame(boolean isNew){
		String ids[] = clientGrid.getSelectedIds();
		String id = "";
		if(ids == null || ids.length < 1){
			arapIframe.load("/common/blank.html");
		}else{
			id = clientGrid.getSelectedIds()[0];
		}
		if(!isNew){
			arapIframe.load("./billdtl.xhtml?type=edit&id="+this.pkVal + "&customerid="+id);
		}else{
			arapIframe.load("./billdtl.xhtml?type=new&customerid="+id+"&id=-1");
		}
		update.markAttributeUpdate(arapIframe, "src");	
	}

	
	@Bind
	public UIIFrame arapIframe;
	
	
	@Override
	public void save() {
		String billno = this.selectedRowData.getBillno();
		super.save();
		refreshDtlFrame(false);
		this.selectedRowData.setBillno(billno);
		doServiceSave();
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Action
	public void changeAccountAction(){
		String accountid = AppUtils.getReqParam("accountid");
		if(StrUtils.isNull(accountid)){
			
		}else{
			DatAccount datAccount = this.serviceContext.accountMgrService.datAccountDao.findById(Long.parseLong(accountid));
			this.selectedRowData.setAccountcont(datAccount.getAccountcont());
			update.markUpdate(true, UpdateLevel.Data, "accountcont");
		}
		
	}
	
	
	@Bind
	public UIDataGrid clientGrid;
	

	@Action
	public void preview() {
//		String rpturl = AppUtil.getContextPath();
//		try{
//			String filename = AppUtil.findReportFile();
//			String openUrl = rpturl + "/reportJsp/showReport.jsp?raq=/"+filename+"&pkid="+this.pkVal;
//			AppUtil.openWindow("_print", openUrl);
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//		}
	}
	
	
	@Action
	public void clientGrid_ondblclick() {
		add();
		String id = clientGrid.getSelectedIds()[0];
		String sql = "SELECT * FROM sys_corporation WHERE id="+id;
		this.selectedRowData.setClientid(Long.parseLong(id));
		try {
			SysCorporation sysCorporation = this.serviceContext.customerMgrService.sysCorporationDao.findById(Long.parseLong(id));
			this.selectedRowData.setClientname(sysCorporation.getNamec());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	private String qryCustomerKey;
	
	@Action
	public void customerQry(){
		this.customerChooseBean.qry(qryCustomerKey);
		this.clientGrid.reload();
	}
	
	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerChooseBean;
	
	@Bind(id = "clientGrid", attribute = "dataProvider")
	public GridDataProvider getClientDataProvider() {
		return this.customerChooseBean.getBillClientDataProvider();
	}

	@Override
	protected void doServiceFindData() {
		selectedRowData = this.serviceContext.billMgrService.finaBillDao.findById(pkVal);
	}

	@Override
	protected void doServiceSave() {
		this.serviceContext.billMgrService.saveData(selectedRowData);
		this.pkVal = selectedRowData.getId();
		
		this.alert("OK");
		doServiceFindData();
	}
	
	@Bind
	public UIWindow editWindow;

	

	@Action(id = "editWindow", event = "onclose")
	private void dtlEditDialogClose() {
		this.refresh();

	}
	
	@Bind(id="del")
	public UIButton del;
	@Action
	public void del() {
		if(this.selectedRowData.getId() < 1){
			MessageUtils.alert("账单还未保存,无需删除!");
			return;
		}
		try{
			this.serviceContext.billMgrService.removeBillAndDtl(this.selectedRowData.getId());
			Browser.execClientScript("showmsg()");
		}catch(Exception e){
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	public UIWindow setnosWindow;
	
	@Bind
	public String windowsbillno;//弹窗里的billno
	@Action
	public void creatbillno(){
		if("".equals(selectedRowData.getBillno()) == false){
			this.alert("单号必须为空!");
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        
		String sql = "SELECT substring(billno from (char_length(billno)-2) for char_length(billno)) AS num,billno FROM fina_bill WHERE isdelete = FALSE AND billno like " +
				"(SELECT prefix||'"+dateStr+"'||'%' FROM sys_busnodesc t ,sys_busnorule r WHERE  t.id = r.busnotypeid AND code like 'fina_bill_nos%' AND t.isdelete = FALSE AND r.isdelete = FALSE order by r.datefm desc LIMIT 1)";
		try {
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(null != map && map.size() > 0 && null != map.get("num")){
				String billno = String.valueOf(map.get("billno"));
				if(Integer.valueOf(String.valueOf(map.get("num")))>98){
					windowsbillno = billno.substring(0,billno.length()-3) + (Integer.valueOf(String.valueOf(map.get("num")))+1);
				}else if(Integer.valueOf(String.valueOf(map.get("num")))>8){
					windowsbillno = billno.substring(0,billno.length()-3) + "0"+(Integer.valueOf(String.valueOf(map.get("num")))+1);
				}else{
					windowsbillno = billno.substring(0,billno.length()-3) + "00"+(Integer.valueOf(String.valueOf(map.get("num")))+1);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(StrUtils.isNull(windowsbillno)){
				windowsbillno = "GSZ"+dateStr+"001";
			}
			selectedRowData.setBillno(windowsbillno);
		}
//		this.grid.reload();
//		setnosWindow.show();
	}
	
	/**
	 * 开票申请
	 */
	@Action
	public void kpsq(){
		String[] ids = this.grid.getSelectedIds();
		String id = "";
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}else{
			for (int i = 0; i < ids.length; i++) {
				if("".equals(id)){
					id = ids[i];
				}else{
					id += ","+ids[i];
				}	
			}
		}
		
		String url = ConfigUtils.findSysCfgVal("sys_bms_kpsq_url");//获取系统配置UMS URL
		SynUfmsAndBms synUfmsAndBms = new SynUfmsAndBms();
		String sql = "SELECT STRING_AGG(billno,';') AS billno FROM fina_bill WHERE isdelete = FALSE AND id IN ("+id+")";
		try{
			Map map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(null != map && map.size() > 0){
				synUfmsAndBms.expense(url+String.valueOf(null == map.get("billno")? "" : map.get("billno")));//跳转BMS开票申请  与打开请款方式一致
			}
		}catch(Exception e){
			SysLogDao sysLogDao = (SysLogDao) AppUtils.getBeanFromSpringIoc("sysLogDao");
			SysLog syslog = new SysLog();
			syslog.setInputer(AppUtils.getUserSession().getUsername());
			syslog.setLogdesc(e.toString());
			syslog.setLogtime(new Date());
			sysLogDao.create(syslog);
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	@SaveState
	public Boolean isconfirm = false;
	
	@Action
	public void isconfirm_oncheck(){
//		save();
		try {
			String sql;
			if(this.selectedRowData == null || this.selectedRowData.getId() <=0){
				alert("请先保存");
				if(isconfirm = true){
					isconfirm = false;
				}else{
					isconfirm = true;
				}
				return;
			}
			BusBillBean busBill = new BusBillBean();
			isconfirm = selectedRowData.getIsconfirm();
			if(isconfirm){
				// boolean flag = busBill.billConfirm(selectedRowData);//确认前优先BMS确认
				// if(!flag){//确认失败
				// 	isconfirm = false;
				// 	return;
				// }
				sql = "UPDATE fina_bill SET isconfirm = TRUE , confirmdate = now() ,updater = '"+AppUtils.getUserSession().getUsercode()+"' ,updatetime = NOW(), confirmer = '"+AppUtils.getUserSession().getUsercode()+"' WHERE id = "+this.selectedRowData.getId()+";";
			}else{
				// boolean flag = busBill.billConfirmCancle(selectedRowData);//取消确认前需要先取消BMS确认
				// if(!flag){//取消确认失败
				// 	isconfirm = true;
				// 	return;
				// }else{
				// 	isconfirm = false;
				// }
				sql = "UPDATE fina_bill SET isconfirm = FALSE , confirmdate = NULL ,updater = '"+AppUtils.getUserSession().getUsercode()+"' ,updatetime = NOW(), confirmer = NULL WHERE id = "+this.selectedRowData.getId()+";";
			}
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(sql);
//			this.selectedRowData.setIsconfirm(this.selectedRowData.getIsconfirm()!=null?(!this.selectedRowData.getIsconfirm()):true);
			this.selectedRowData.setConfirmer(isconfirm?AppUtils.getUserSession().getUsername():null);
			this.selectedRowData.setConfirmdate(isconfirm?Calendar.getInstance().getTime():null);
		} catch (Exception e) {
			if(isconfirm = true){
				isconfirm = true;
			}else{
				isconfirm = false;
			}
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
}
