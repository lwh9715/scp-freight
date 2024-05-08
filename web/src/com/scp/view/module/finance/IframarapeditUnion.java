package com.scp.view.module.finance;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scp.base.ConstantBean.Module;
import com.scp.exception.NoRowException;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.finance.iframarapeditunionBean", scope = ManagedBeanScope.REQUEST)
public class IframarapeditUnion extends FormView {

	@Bind
	@SaveState
	private String jsonData;
	
	@Bind
	@SaveState
	private Long customerid = -100L;
	
	@Bind
	@SaveState
	private String customerabbr = "";
	
	@Bind
	@SaveState
	private String customernamec = "";
	
	@Bind
	@SaveState
	private Long shipid = -100L;
	
	@Bind
	@SaveState
	private String shipabbr = "";
	
	@Bind
	@SaveState
	private String shipnamec = "";

	@Bind
	@SaveState
	private Long jobid;

	@Bind
	@SaveState
	private String editType;
	
	public Long userid;
	
	@Bind
	private String ids;

	@Bind
	public UIButton add;

	@Bind
	public UIButton copyadd;

	@Bind
	public UIButton btn_save;

	@Bind
	public UIButton btn_delete;

	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			initCtrl();
			usrCfg();
			String jobid = AppUtils.getReqParam("jobid");
			String arapid = AppUtils.getReqParam("arapid");
			editType = AppUtils.getReqParam("editType");
			if(StrUtils.isNull(editType) || !"add".equals(editType)){
				jsonData = batchEditInit(jobid,arapid);
			}else{
				this.jobid = Long.parseLong(jobid);
				jobs_lock(this.jobid);
				initAutoData();
				jsonData = "''";
			}
			this.update.markUpdate(UpdateLevel.Data, "jsonData");
		}
	}

	private String batchEditInit(String jobid, String arapid) {
		String ret = "''";
		this.jobid = Long.parseLong(jobid);
		jobs_lock(this.jobid);
		initAutoData();
		if (!StrUtils.isNull(arapid)) {
			String[] ids = arapid.split(",");
			this.ids = arapid;
			ret = this.serviceContext.arapMgrService.getArapsJsonByJobid(
					Long.parseLong(jobid), ids,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent());
		} else {
			ret = this.serviceContext.arapMgrService.getArapsJsonByJobid(
					Long.parseLong(jobid), null,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent());
		}
		return StrUtils.isNull(ret) ? "''" : ret;
	}

	private void initCtrl() {
		add.setDisabled(true);
		copyadd.setDisabled(true);
		btn_save.setDisabled(true);
		btn_delete.setDisabled(true);
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_arap.getValue())) {
			if (s.endsWith("_add")) {
				add.setDisabled(false);
				copyadd.setDisabled(false);
				btn_save.setDisabled(false);
			} else if (s.endsWith("_update")) {
				btn_save.setDisabled(false);
			} else if (s.endsWith("_delete")) {
				btn_delete.setDisabled(false);
			}
		}
	}
	
	private void jobs_lock(Long id){
		try {
			FinaJobs fj = this.serviceContext.jobsMgrService.finaJobsDao.findById(id);
			if(fj != null && fj.getIslock() != null && fj.getIslock()){
				add.setDisabled(true);
				copyadd.setDisabled(true);
				btn_save.setDisabled(true);
				btn_delete.setDisabled(true);
			}
		}catch(NoRowException e){
			//System.out.println("NoRowException:FinaJobs");
		} catch(IllegalArgumentException e){
			//System.out.println("IllegalArgumentException:FinaJobs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Action
	private void refreshAjaxSubmit() {
		String ret = "''";
		ret = this.serviceContext.arapMgrService.getArapsJsonByJobid(jobid,
				null,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent());
		ret = StrUtils.isNull(ret) ? "''" : ret;
		jsonData = ret;
		////System.out.println("refreshAjaxSubmit");
		update.markUpdate(true, UpdateLevel.Data, "jsonData");

	}
	
	private void initAutoData(){
		FinaJobs fj = this.serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
		if(fj!=null&&fj.getCustomerid()!=null&&fj.getCustomerabbr()!=null){
			SysCorporation s = this.serviceContext.customerMgrService.sysCorporationDao.findById(fj.getCustomerid());
			if(s!=null&&s.getId()>0L&&s.getAbbr()!=null&&s.getNamec()!=null){
				this.customerid = s.getId();
				this.customerabbr = s.getAbbr();
				this.customernamec = s.getNamec();
				this.customernamec = this.customernamec.replace("\"", "'");
				this.customerabbr = this.customerabbr.replace("\"", "'");
				update.markUpdate(UpdateLevel.Data, "customerid");
				update.markUpdate(UpdateLevel.Data, "customerabbr");
				update.markUpdate(UpdateLevel.Data, "customernamec");
			}
		}
		BusShipping bs;
		try {
			bs = this.serviceContext.busShippingMgrService.busShippingDao.findOneRowByClauseWhere("jobid = "+this.jobid+" AND isdelete = FALSE");
			if(bs!=null&&bs.getCarrierid()!=null){
				SysCorporation s = this.serviceContext.customerMgrService.sysCorporationDao.findById(bs.getCarrierid());
				if(s!=null&&s.getId()>0L&&s.getAbbr()!=null&&s.getNamec()!=null){
					this.shipid = s.getId();
					this.shipabbr = s.getAbbr();
					this.shipnamec = s.getNamec();
					update.markUpdate(UpdateLevel.Data, "shipid");
					update.markUpdate(UpdateLevel.Data, "shipabbr");
					update.markUpdate(UpdateLevel.Data, "shipnamec");
				}
			}
		} catch (NoRowException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	@Action
	private void saveAjaxSubmit() {
		// 判断是否有费用模块权限
		boolean roleDelete = false;
		for (String s : AppUtils.getUserRoleModuleCtrl(Module.fina_arap.getValue())) {
			if (s.endsWith("_delete")) {
				roleDelete = true;
			}
		}
		List<Long> idsOldArray = new ArrayList<Long>();
		idsOldArray.clear();
		List<Long> idsNewArray = new ArrayList<Long>();
		idsNewArray.clear();
		////System.out.println(this.jobid);
		String data = AppUtils.getReqParam("data");
		if (!StrUtils.isNull(data) && !"null".equals(data)) {
			Type listType = new TypeToken<ArrayList<com.scp.vo.finance.FinaArap>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			ArrayList<com.scp.vo.finance.FinaArap> list = gson.fromJson(data,
					listType);// 使用该class属性，获取的对象均是list类型的
			ArrayList<com.scp.vo.finance.FinaArap> listOld = null;
			if(StrUtils.isNull(editType) || !"add".equals(editType)){
				if (ids != null && ids.length() > 0) {
					listOld = gson.fromJson(this.serviceContext.arapMgrService
							.getArapsJsonByJobid(this.jobid, ids.split(","),AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent()),
							listType);
				} else {
					listOld = gson.fromJson(this.serviceContext.arapMgrService
							.getArapsJsonByJobid(this.jobid, null,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent()), listType);
				}
			}

			List<FinaArap> finaArapList = new ArrayList<FinaArap>();

			for (com.scp.vo.finance.FinaArap li : list) {
				if ("".equals(li.getCurrency()) || -100 == li.getFeeitemid()) {
					continue;
				}
				FinaArap finaArap = new FinaArap();
				if (-100 != li.getId()) {
					finaArap = serviceContext.arapMgrService.finaArapDao
							.findById(li.getId());
					idsNewArray.add(li.getId());
				}
				finaArap.setAraptype(li.getAraptype());
				finaArap.setArapdate(li.getArapdate());
				finaArap.setCustomerid(li.getCustomerid());
				finaArap.setFeeitemid(li.getFeeitemid());
				finaArap.setAmount(li.getAmount());
				finaArap.setCurrency(li.getCurrency());
				if(li.getRemarks()!=null){
					finaArap.setRemarks(li.getRemarks());   //界面无备注，这里会全部变成空的 neo 20170202
				}
				finaArap.setPiece(li.getPiece());
				finaArap.setPrice(li.getPrice());
				finaArap.setUnit(li.getUnit());
				finaArap.setCustomercode(li.getCustomecode());
				finaArap.setPpcc(li.getPpcc());
				finaArap.setSharetype(li.getSharetype());
				finaArap.setJobid(this.jobid);
				if(li.getId() == -100L){//新增费用,结算地取当前所选分公司,选全部则取帐号所在分公司
					finaArap.setCorpid(AppUtils.getUserSession().getCorpidCurrent() < 0 ? AppUtils.getUserSession().getCorpid() : AppUtils.getUserSession().getCorpidCurrent());
				}
				finaArap.setIsamend(li.getIsamend());
				finaArapList.add(finaArap);
			}
			try {
				serviceContext.arapMgrService.saveOrModify(finaArapList);
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}

			if (listOld != null && listOld.size() > 0) {
				for (com.scp.vo.finance.FinaArap li : listOld) {
					idsOldArray.add(li.getId());
				}
				List<Long> lists = getDiffrent(idsOldArray, idsNewArray);
				if (!lists.isEmpty()) {

					if (roleDelete) {
						try {
							serviceContext.arapMgrService.removes(lists);
						} catch (Exception e) {
							MessageUtils.showException(e);
							return;
						}
					} else {
						MessageUtils.alert("您没有删除权限!");
					}
				}
			}
			MessageUtils.alert("OK");
			if(!StrUtils.isNull(editType) && "add".equals(editType)){
				Browser.execClientScript("clearRowsNoTips();");
			}
			//Browser.execClientScript("window.parent","refresh.submit();");
			Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
		}
	}
	
	/**
	 * 获取两个List的不同元素
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<Long> getDiffrent(List<Long> list1, List<Long> list2) {
		long st = System.nanoTime();
		List<Long> diff = new ArrayList<Long>();
		for (Long str : list1) {
			if (!list2.contains(str)) {
				diff.add(str);
			}
		}
		////System.out.println("getDiffrent total times " + (System.nanoTime() - st));
		return diff;
	}
	
	@SaveState
	public String currencycyadd = "CNY";
	
	/*
	 * 查个人设置
	 * */
	public void usrCfg(){
		Map map = ConfigUtils.findUserCfgVals(new String[]{"fee_profits_cy2","fee_add_cyid"}, AppUtils.getUserSession().getUserid());
		if(map != null && map.size() > 0){
			if(map.get("fee_add_cyid")!=null){
				this.currencycyadd = map.get("fee_add_cyid").toString();
			}
		}
	}
}
