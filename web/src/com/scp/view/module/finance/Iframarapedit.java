package com.scp.view.module.finance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scp.base.ConstantBean.Module;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;
import com.scp.model.bus.BusAir;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusShipping;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;
import com.scp.view.comp.GridView;
import com.ufms.web.view.sysmgr.LogBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.operamasks.faces.annotation.*;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean(name = "pages.module.finance.iframarapeditBean", scope = ManagedBeanScope.REQUEST)
public class Iframarapedit extends FormView {

	@Resource(name="hibTtrTemplate")
    private TransactionTemplate transactionTemplate;

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
	private Long copid = -100L;

	@Bind
	@SaveState
	private String copabbr = "";

	@Bind
	@SaveState
	private String copnamec = "";

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

	@Bind
	@SaveState
	public BigDecimal airpice;

	@Bind
	@SaveState
	public boolean issettlement = false;

	@Bind
	@SaveState
	private Boolean isAddOnDown = false;

	@Bind
	@SaveState
	private JSONArray array;

	@Bind
	@SaveState
	private boolean flag = true;

	@Bind
	@SaveState
	private String qdflag = "";

    @Bind
    @SaveState
    private boolean isgqflag = false;

    @Bind
    @SaveState
    public String amendfeeiinfo = "";

    @Bind
    @SaveState
    public String arTip;

    @Bind
    @SaveState
    public String apTip;

	@Bind
	@SaveState
	public String currency="USD";

	@Override
	public void beforeRender(boolean isPostBack) {
		StringBuffer sb = new StringBuffer();
		sb.append("Iframarapedit,beforeRender开始");

		this.userid = AppUtils.getUserSession().getUserid();
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			sb.append(",initCtrl开始");
			initCtrl();
			sb.append(",usrCfg开始");
			usrCfg();
			sb.append(",findCurrencu开始");
			findCurrencu();
			String jobid = AppUtils.getReqParam("jobid");
			String arapid = AppUtils.getReqParam("arapid");
			editType = AppUtils.getReqParam("editType");
			sb.append(",arapid为").append(arapid);
			if (StrUtils.isNull(editType) || (!"add".equals(editType) && !"substitute".equals(editType) && !"substitutechange".equals(editType) && !"importArap".equals(editType))){
				jsonData = batchEditInit(jobid,arapid);
			} else if ("substitutechange".equals(editType)) {
				jsonData = substituteEditInit(jobid,arapid);
			} else if ("importArap".equals(editType)) {
				jsonData = importArapEditInit(jobid,arapid);
			} else {
				this.jobid = Long.parseLong(jobid);
				jobs_lock(this.jobid);
				initAutoData();
				jsonData = "''";
			}
			this.update.markUpdate(UpdateLevel.Data, "jsonData");
			try{
				isAddOnDown = true;//标记是否新增在最下面行，默认没有个人设置则为下
				String findUserCfgVal = ConfigUtils.findUserCfgVal("isAddOnUp",  AppUtils.getUserSession().getUserid());//个人设置是新增行是否在最上面
				if("Y".equalsIgnoreCase(findUserCfgVal)){
					isAddOnDown = false;
				}else{
					isAddOnDown = true;
				}

				String quoteCfgVal = ConfigUtils.findUserCfgVal("show_quote",  AppUtils.getUserSession().getUserid());//个人设置是新增行是否在最上面
				if("Y".equalsIgnoreCase(quoteCfgVal)){
					showquote = true;
				}else{
					showquote = false;
				}

			}catch(Exception e){
				isAddOnDown = true;
			}
			this.update.markUpdate(UpdateLevel.Data,"isAddOnDown");
			LogBean.insertLog(new StringBuffer().append(sb));
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
		try {
			String sys_settlement_method = ConfigUtils.findSysCfgVal("sys_settlement_method");
			if("Y".equalsIgnoreCase(sys_settlement_method)){
				issettlement = true;
			}else{
				issettlement = false;
			}
		} catch (Exception e) {
			issettlement = false;
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
			if(null == serviceContext.customerMgrService.findJoinCorporationById(fj.getCustomerid())){
				SysCorporation s = this.serviceContext.customerMgrService.sysCorporationDao.findById(fj.getCustomerid());
				if(s!=null&&s.getId()>0L&&s.getAbbr()!=null&&s.getNamec()!=null){
					this.customerid = s.getId();
					this.customerabbr = s.getAbbr();
					this.customernamec = s.getNamec();
					this.customernamec = this.customernamec.replace("\"", "'");
					this.customerabbr = this.customerabbr.replace("\"", "'");
				}
			}else{
				customerid = -1L;
				customerabbr = "";
				customernamec = "";
			}
			update.markUpdate(UpdateLevel.Data, "customerid");
			update.markUpdate(UpdateLevel.Data, "customerabbr");
			update.markUpdate(UpdateLevel.Data, "customernamec");

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
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			List<Map> coperatin = serviceContext.customerMgrService.findAgentorcarrById(jobid);
			if (coperatin != null && coperatin.get(0) != null
					&& coperatin.get(0).get("id") != null
					&& coperatin.get(0).get("namec") != null
					&& coperatin.get(0).get("abbr") != null) {
				this.copid = Long.parseLong(coperatin.get(0).get("id").toString());
				this.copabbr = coperatin.get(0).get("abbr").toString();
				this.copnamec = coperatin.get(0).get("namec").toString();
			}else{
				copid = -1L;
				copabbr = "";
				copnamec = "";
			}
		} catch (Exception e) {
			copid = -1L;
			copabbr = "";
			copnamec = "";
		}finally{
			update.markUpdate(UpdateLevel.Data, "copid");
			update.markUpdate(UpdateLevel.Data, "copabbr");
			update.markUpdate(UpdateLevel.Data, "copnamec");
		}

		try {
			BusAir air = serviceContext.busAirMgrService.busAirDao.findOneRowByClauseWhere(" jobid ="+jobid);
			if(air!=null && !StrUtils.isNull(air.getChargeweight())&& StrUtils.isNumber(air.getChargeweight())){
				//1581空运，费用录入时，数量，默认带委托中的计费重数据
				airpice = new BigDecimal(air.getChargeweight());
			}else{
				airpice = new BigDecimal(1);
			}
			//System.out.println(airpice);
		} catch (Exception e) {
			airpice = new BigDecimal(1);
			//e.printStackTrace();
		}
	}

	@SaveState
	private Integer num = 1;

	@Action
	@Transactional
	private void saveAjaxSubmit() {
		try{
//			Object id = transactionTemplate.execute(new TransactionCallback() {  
//		        @Override  
//		        public String doInTransaction(TransactionStatus status) {  
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
		    			if(StrUtils.isNull(editType) || (!"add".equals(editType) && !"substitute".equals(editType) && !"substitutechange".equals(editType) && !"importArap".equals(editType))){
		    				if (ids != null && ids.length() > 0) {
		    					listOld = gson.fromJson(serviceContext.arapMgrService
		    							.getArapsJsonByJobid(jobid, ids.split(","),AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent()),
		    							listType);
		    				} else {
		    					listOld = gson.fromJson(serviceContext.arapMgrService
		    							.getArapsJsonByJobid(jobid, null,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent()), listType);
		    				}
		    			}

		    			List<FinaArap> finaArapList = new ArrayList<FinaArap>();

		    			for (com.scp.vo.finance.FinaArap li : list) {

		    				//工作单导入，将查询出来的费用ID清空
							if ("importArap".equals(editType)) {
								li.setId(-100);
							}

		    				if ("".equals(li.getCurrency()) || -100 == li.getFeeitemid()) {
//		    					continue;
		    				}
		    				FinaArap finaArap = new FinaArap();
		    				if (-100 != li.getId()) {
		    					finaArap = serviceContext.arapMgrService.finaArapDao.findById(li.getId());
		    					idsNewArray.add(li.getId());
		    				}
		    				finaArap.setTaxrate(li.getTaxrate());
		    				finaArap.setAraptype(li.getAraptype());
		    				finaArap.setArapdate(li.getArapdate());
		    				finaArap.setCustomerid(li.getCustomerid());
		    				finaArap.setFeeitemid(li.getFeeitemid());
		    				finaArap.setAmount(li.getAmount());
		    				finaArap.setCurrency(li.getCurrency());
		    				if(li.getRemarks()!=null){
		    					finaArap.setRemarks(li.getRemarks());   //界面无备注，这里会全部变成空的 neo 20170202
		    				}
							//工作单导入，报价金额and报价币种
//							if ("importArap".equals(editType)) {
//								finaArap.setQuoteamount(li.getQuoteamount());
//								finaArap.setQuotecurrency(li.getQuotecurrency());
//							}
		    				finaArap.setPiece(li.getPiece());
		    				finaArap.setPrice(li.getPrice());
		    				finaArap.setPricenotax(li.getPrice());
		    				finaArap.setUnit(li.getUnit());
		    				finaArap.setCustomercode(li.getCustomecode());
		    				finaArap.setPpcc(li.getPpcc());
		    				finaArap.setSharetype("N");
		    				finaArap.setJobid(jobid);
		    				finaArap.setPayplace(li.getPayplace());
		    				//代垫费用录入
							if("substitute".equals(editType)){
								finaArap.setRptype("D");
							} else if (!"substitute".equals(editType) && !"substitutechange".equals(editType) &&
									finaArap.getRptype()!=null && finaArap.getRptype().equals("D")){
								MessageUtils.alert("代垫费用请在 其他-代垫费用申请 进行修改!");
								return;
							}
//		    				if(li.getId() == -100L){//新增费用,结算地取当前所选分公司,选全部则取帐号所在分公司
//		    					//finaArap.setCorpid(AppUtils.getUserSession().getCorpidCurrent() < 0 ? AppUtils.getUserSession().getCorpid() : AppUtils.getUserSession().getCorpidCurrent());
//		    					finaArap.setCorpid(AppUtils.getUserSession().getCorpid()); //neo 20200513 改为取当前用户所在分公司
//		    				}
		    				finaArap.setCorpid(li.getCorpid());
		    				finaArap.setIsamend(li.getIsamend());
		    				long etime1=System.currentTimeMillis()+num*1000;//延时函数，单位毫秒，这里是延时了num秒钟
		    				finaArap.setInputtime(new Date(etime1));
		    				num ++;

//		    				if(finaArap.getTaxrate() != null && "null".equals(finaArap.getTaxrate())){
//		    					if(finaArap.getTaxrate().intValue() !=0){
//		    						finaArap.setPricenotax(li.getPrice().divide(finaArap.getTaxrate()));//税前单价=单价除以税率(税率不等于0)
//		    					}
//		    				}else{
////		    					finaArap.setPricenotax(li.getPrice());//税前单价=单价(税率等于0)
//		    				}


		    				//判断是否数据一样(是否修改)，不一样就不add
		    				FinaArap arap = serviceContext.arapMgrService.finaArapDao.findById(li.getId());
		    				if(arap!=null){
			    				boolean ifAraptype = finaArap.getAraptype().equals(arap.getAraptype());
			    				boolean ifPayplace = finaArap.getPayplace().equals(arap.getPayplace());
			    				boolean ifCustomerid = finaArap.getCustomerid().equals(arap.getCustomerid());
			    				boolean ifFeeitemid = finaArap.getFeeitemid().equals(arap.getFeeitemid());
			    				boolean ifCurrency = finaArap.getCurrency().equals(arap.getCurrency());
			    				boolean ifRemarks = finaArap.getRemarks().equals(arap.getRemarks());
			    				boolean ifAmount = finaArap.getAmount().equals(arap.getAmount());
			    				boolean ifPiece = finaArap.getPiece().compareTo(arap.getPiece())==0;
			    				boolean ifPrice = finaArap.getPrice().equals(arap.getPrice());
			    				boolean ifUnit = finaArap.getUnit().equals(arap.getUnit());
			    				//boolean ifCustomercode = finaArap.getCustomercode().equals(arap.getCustomercode());
			    				boolean ifPpcc = finaArap.getPpcc().equals(arap.getPpcc());
			    				boolean ifSharetype = finaArap.getSharetype().equals(arap.getSharetype());
			    				boolean ifJobid = finaArap.getJobid().equals(arap.getJobid());
			    				boolean ifCorpid = finaArap.getCorpid()!=null&&finaArap.getCorpid().equals(arap.getCorpid());
			    				if(ifAraptype&&ifCustomerid&&ifFeeitemid&&ifCurrency&&ifRemarks&&ifAmount&&ifAmount&&ifPiece&&ifPrice&&
			    						ifUnit&&ifPpcc&&ifSharetype&&ifJobid&&ifCorpid&ifPayplace)		    				{
			    					continue;
			    				}
		    				}
		    				finaArapList.add(finaArap);
		    			}

						//代垫费用录入验证
						if("substitute".equals(editType) || "substitutechange".equals(editType)){
							//判断是否录入偶数条费用
							if (finaArapList.size() % 2 == 0) {
                                if (!areListsEqual(finaArapList)) {
									MessageUtils.alert("录入代垫费用格式不正确，请确保应收和应付金额，币种，结算地，费用类型一致!");
									return;
                                }
							} else {
								MessageUtils.alert("录入代垫费用格式不正确，请确保应收和应付金额，币种，结算地，费用类型一致!");
								return;
							}
						}

		    			serviceContext.arapMgrService.saveOrModify(finaArapList);

						//报价金额录入
						String quoteCfgVal = ConfigUtils.findUserCfgVal("show_quote",  AppUtils.getUserSession().getUserid());//个人设置是新增行是否在最上面

						if("Y".equalsIgnoreCase(quoteCfgVal)){
							showquote = true;
						}else{
							showquote = false;
						}

                        if (showquote) {
                            for (int i = 0; i < finaArapList.size(); i++) {
								com.scp.vo.finance.FinaArap finaArap = list.get(i);
								if (!StrUtils.isNull(list.get(i).getQuoteamount().toString()) && !StrUtils.isNull(list.get(i).getQuotecurrency().toString())) {
                                    String sql = "UPDATE fina_arap_link_quote Set isdelete = TRUE WHERE arapid = "+finaArapList.get(i).getId()+";";
                                    sql += "\nINSERT INTO fina_arap_link_quote(id ,arapid ,quoteamount ,quotecurrency )" +
                                            "\nSELECT getid()," + finaArapList.get(i).getId() + "," + new BigDecimal(list.get(i).getQuoteamount().toString()) + ",'"+list.get(i).getQuotecurrency().toString()+"'" +
                                            "\nFROM _virtual WHERE NOT EXISTS(SELECT 1 FROM fina_arap_link_quote WHERE isdelete = FALSE AND arapid = " + finaArapList.get(i).getId() +");";
                                    daoIbatisTemplate.updateWithUserDefineSql(sql);
                                }
                            }
                        }

		    			if (listOld != null && listOld.size() > 0) {
		    				for (com.scp.vo.finance.FinaArap li : listOld) {
		    					idsOldArray.add(li.getId());
		    				}
		    				List<Long> lists = getDiffrent(idsOldArray, idsNewArray);
		    				if (!lists.isEmpty()) {

		    					if (roleDelete) {
		    						serviceContext.arapMgrService.removes(lists);
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
//		    			Browser.execClientScript("initData();");
		    		}
//		        return "";
//		        }
//			});
		}catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}

	private List<FinaArap> initArapDate(){
		List<FinaArap> finaArapList = new ArrayList<FinaArap>();
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
			if(StrUtils.isNull(editType) || (!"add".equals(editType) && !"substitute".equals(editType) && !"substitutechange".equals(editType))){
				if (ids != null && ids.length() > 0) {
					listOld = gson.fromJson(serviceContext.arapMgrService
							.getArapsJsonByJobid(jobid, ids.split(","),AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent()),
							listType);
				} else {
					listOld = gson.fromJson(serviceContext.arapMgrService
							.getArapsJsonByJobid(jobid, null,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent()), listType);
				}
			}

			for (com.scp.vo.finance.FinaArap li : list) {
				if ("".equals(li.getCurrency()) || -100 == li.getFeeitemid()) {
//		    					continue;
				}
				FinaArap finaArap = new FinaArap();
				if (-100 != li.getId()) {
					finaArap = serviceContext.arapMgrService.finaArapDao.findById(li.getId());
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
				finaArap.setPricenotax(li.getPrice());
				finaArap.setUnit(li.getUnit());
				finaArap.setCustomercode(li.getCustomecode());
				finaArap.setPpcc(li.getPpcc());
				finaArap.setSharetype("N");
				finaArap.setJobid(jobid);
				finaArap.setPayplace(li.getPayplace());
				//代垫费用录入
				if("substitute".equals(editType)){
					finaArap.setRptype("D");
				}
				finaArap.setIsamend(true);
				finaArap.setTaxrate(li.getTaxrate());
//				if(li.getId() == -100L && (li.getCorpid() == null || li.getCorpid() == 0)){//新增费用,结算地取当前所选分公司,选全部则取帐号所在分公司,香港除外
//					finaArap.setCorpid(AppUtils.getUserSession().getCorpid()); //neo 20200513 改为取当前用户所在分公司
//				}else{
//					finaArap.setCorpid(li.getCorpid());
//				}

				//报价金额录入
				String quoteCfgVal = ConfigUtils.findUserCfgVal("show_quote",  AppUtils.getUserSession().getUserid());//个人设置是新增行是否在最上面
				if("Y".equalsIgnoreCase(quoteCfgVal)){
					finaArap.setDescinfo(li.getQuotecurrency());//Descinfo增减费用临时借用，报价币种
					finaArap.setInvoiceamount(li.getQuoteamount());//amount增减费用临时借用，报价金额
				}

				finaArap.setCorpid(li.getCorpid());
				long etime1=System.currentTimeMillis()+num*1000;//延时函数，单位毫秒，这里是延时了num秒钟
				finaArap.setInputtime(new Date(etime1));
				num ++;

//				if(finaArap.getTaxrate() != null && "null".equals(finaArap.getTaxrate())){
//					if(finaArap.getTaxrate().intValue() !=0){
//						finaArap.setPricenotax(li.getPrice().divide(finaArap.getTaxrate()));//税前单价=单价除以税率(税率不等于0)
//					}
//				}else{
////		    					finaArap.setPricenotax(li.getPrice());//税前单价=单价(税率等于0)
//				}


				//判断是否数据一样(是否修改)，不一样就不add
				FinaArap arap = serviceContext.arapMgrService.finaArapDao.findById(li.getId());
				if(arap!=null){
    				boolean ifAraptype = finaArap.getAraptype().equals(arap.getAraptype());
    				boolean ifPayplace = finaArap.getPayplace().equals(arap.getPayplace());
    				boolean ifCustomerid = finaArap.getCustomerid().equals(arap.getCustomerid());
    				boolean ifFeeitemid = finaArap.getFeeitemid().equals(arap.getFeeitemid());
    				boolean ifCurrency = finaArap.getCurrency().equals(arap.getCurrency());
    				boolean ifRemarks = finaArap.getRemarks().equals(arap.getRemarks());
    				boolean ifAmount = finaArap.getAmount().equals(arap.getAmount());
    				boolean ifPiece = finaArap.getPiece().compareTo(arap.getPiece())==0;
    				boolean ifPrice = finaArap.getPrice().equals(arap.getPrice());
    				boolean ifUnit = finaArap.getUnit().equals(arap.getUnit());
    				//boolean ifCustomercode = finaArap.getCustomercode().equals(arap.getCustomercode());
    				boolean ifPpcc = finaArap.getPpcc().equals(arap.getPpcc());
    				boolean ifSharetype = finaArap.getSharetype().equals(arap.getSharetype());
    				boolean ifJobid = finaArap.getJobid().equals(arap.getJobid());
    				boolean ifCorpid = finaArap.getCorpid()!=null&&finaArap.getCorpid().equals(arap.getCorpid());
    				if(ifAraptype&&ifCustomerid&&ifFeeitemid&&ifCurrency&&ifRemarks&&ifAmount&&ifAmount&&ifPiece&&ifPrice&&
    						ifUnit&&ifPpcc&&ifSharetype&&ifJobid&&ifCorpid&ifPayplace)		    				{
    					continue;
    				}
				}
				finaArapList.add(finaArap);
			}
		}
		return finaArapList;
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

	@SaveState
	public String payplaceadd = "";

	@SaveState
	public String arapbranch = "";

	/*
	 * 查个人设置
	 * */
	public void usrCfg(){
		Map map = ConfigUtils.findUserCfgVals(new String[]{"fee_profits_cy2","fee_add_cyid","fee_add_payplace"}, AppUtils.getUserSession().getUserid());
		currencycyadd = AppUtils.getUserSession().getBaseCurrency();
		if(map != null && map.size() > 0){
			if(map.get("fee_add_cyid")!=null&&!map.get("fee_add_cyid").equals("")){
				this.currencycyadd = map.get("fee_add_cyid").toString();
			}
			String payplace = StrUtils.getMapVal(map, "fee_add_payplace");
			if(!StrUtils.isNull(payplace)){
				payplaceadd = payplace;
			}
		}
		String jobid = AppUtils.getReqParam("jobid");
		String sql = "SELECT c.abbcode,c.id,(CASE WHEN c.id = (SELECT corpid FROM sys_user u WHERE u.isdelete = FALSE AND u.id = "+AppUtils.getUserSession().getUserid()+" LIMIT 1) THEN '1' ELSE '2' END) AS choose FROM sys_corporation c WHERE isdelete = FALSE AND id IN (SELECT corpid AS ids FROM fina_jobs WHERE isdelete = FALSE AND id = "+jobid+" UNION SELECT corpidOP AS ids FROM fina_jobs WHERE isdelete = FALSE AND id = "+jobid+" UNION SELECT corpidOP2 AS ids FROM fina_jobs WHERE isdelete = FALSE AND id = "+jobid+" UNION SELECT corpid FROM fina_corp c WHERE c.jobid = "+jobid+" AND c.isdelete = FALSE)";
		List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		for (int i = 0; i < list.size(); i++) {
			Map m = list.get(i);
			if("2".equals(String.valueOf(m.get("choose")))){
				arapbranch += "<option value='"+m.get("id")+"'>"+String.valueOf(m.get("abbcode"))+"</option>";
			}else{
				arapbranch += "<option value='"+m.get("id")+"' selected>"+String.valueOf(m.get("abbcode"))+"</option>";
			}
		}
		arapbranch = arapbranch.replace("'", "");

		Map map3 = ConfigUtils.findUserCfgVals(new String[]{"fee_profits_cy2","fee_add_cyid"}, AppUtils.getUserSession().getUserid());
		if(map3 != null && map3.size() > 0){
			if(map3.get("fee_profits_cy2")!=null&&!map3.get("fee_profits_cy2").equals("")){
				this.currency = map3.get("fee_profits_cy2").toString();
			}
		}
	}

	@Bind
	@SaveState
	public String currencys;

	public void findCurrencu(){
		String sql = "SELECT String_agg(code,',') AS currencys FROM dat_currency WHERE isdelete = FALSE ";
		Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(m!=null&&m.get("currencys")!=null){
			currencys = m.get("currencys").toString();
		}else{
			currencys = "";
		}

	}

	@Bind
	@SaveState
	public String taskname;

	@SaveState
	@Accessible
	public Map<String, Object> qryMapUser = new HashMap<String, Object>();

	@SaveState
	@Accessible
	public Map<String, Object> qryMapArap = new HashMap<String, Object>();

	@Bind
	public UIDataGrid gridUser;

	@Bind(id = "gridUser", attribute = "dataProvider")
	@SuppressWarnings("deprecation")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.page";
				Object[] obj = serviceContext.daoIbatisTemplate
				.getSqlMapClientTemplate().queryForList(sqlId,
						getQryClauseWhere2(qryMapUser), start, limit).toArray();
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser), start, limit).toArray();

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.ship.jobseditBean.gridUser.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapUser));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	@Action
	public void applyBPMform() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("applyBPMform开始");
			if("BPM".equals(ConfigUtils.findSysCfgVal("sys_bpm"))){
				if(StrUtils.isNull(nextAssignUser))nextAssignUser="0";
				String processCode = "BPM-968CACDE";

                setQdReviewer();
                //增减费用青岛，单独走一条流程
                if (isgqflag) {
					processCode = "BPM-CA59ADF7";
				}
				BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '" + processCode + "'");
				String sql = "SELECT count(*) FROM bpm_processinstance WHERE processid = '" + bpmProcess.getId() + "' AND refid = '" + 0 + "' AND isdelete = false AND state <> 9 AND state <>8";
				Map s = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
				String str = s.get("count").toString();
				if(Long.valueOf(str) == 0){
					Browser.execClientScript("existsLastBPMJsVar.setValue(0);");
				}else{
					Browser.execClientScript("existsLastBPMJsVar.setValue(1);");
				}

				//途曦的增减费用 默认审核人
				if (AppUtils.getUserSession().getCorpid() == 11540072274L) {
					Browser.execClientScript("userJs.setValue('" + AppUtils.getUserSession().getUsername() + ",');");
					this.user = AppUtils.getUserSession().getUsername().toString() + ",";
					this.nextAssignUser = AppUtils.getUserSession().getUserid() + ",";
				} else if (isgqflag && (bpmProcess.getId() == 1749260462274L || bpmProcess.getId() == 88830872274L)) {
					sql = "SELECT userid as id, (SELECT namec || ',' FROM sys_user WHERE ID = userid LIMIT 1) AS namec FROM dat_reviewedor" +
                            " WHERE processid = " + bpmProcess.getId() + " AND assignname = '" + qdflag + "' AND isdelete = FALSE LIMIT 1;";
					List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
					if (list.size() > 0) {
						String manageid = null == list.get(0).get("id") ? "0" : list.get(0).get("id").toString();
						String name = null == list.get(0).get("namec") ? "0" : list.get(0).get("namec").toString();

						if (Long.valueOf(manageid) > 0) {
							Browser.execClientScript("userJs.setValue('" + name + "');");
							Browser.execClientScript("nextAssignUserJs.setValue(" + manageid + ",);");
							this.user = name;
							this.nextAssignUser = manageid;
						}
					} else {
						Browser.execClientScript("$('#delBPM').click();");
					}
                    isgqflag = false;
				} else {
					sql = "SELECT id,namec||',' AS namec FROM sys_user WHERE jobdesc = '经理' AND deptid = ANY(SELECT deptid FROM sys_user WHERE isdelete = FALSE AND id = (SELECT saleid FROM fina_jobs WHERE id = " + jobid + " AND isdelete = FALSE LIMIT 1))";
					List<Map> list = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
					if (list.size() > 0) {
						String manageid = null == list.get(0).get("id") ? "0" : list.get(0).get("id").toString();
						String name = null == list.get(0).get("namec") ? "0" : list.get(0).get("namec").toString();

						if (Long.valueOf(manageid) > 0) {
							Browser.execClientScript("userJs.setValue('" + name + "');");
							Browser.execClientScript("nextAssignUserJs.setValue(" + manageid + ",);");
						}
					} else {
						Browser.execClientScript("$('#delBPM').click();");
					}
				}

				Browser.execClientScript("bpmWindowJsVar.show();");

				bpmReview(sb);
				LogBean.insertLog(new StringBuffer().append(sb));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Action
	public void bpmReviewstart(){
		try {
			String msg = "";
			String sql = "SELECT f_arap_addamend_check('json="+array+"',"+AppUtils.getUserSession().getUserid()+") AS msg;";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			msg = String.valueOf(map.get("msg"));
			if(msg.indexOf("ERROR!") > -1){
				MessageUtils.alert(msg);
				return;
			}

			String processinsVar = "";
			String processCode = "";
			BpmProcess bpmProcess = null;

			//增减费用青岛，单独走一条流程
			JSONObject jsonObject = JSONObject.fromObject(array.get(0));
			if (jsonObject.get("corpid") != null && jsonObject.get("corpid").toString().equals("1122274")) {
				processinsVar = "利润减少" + "-" + false + "-" + "利润减少";
				taskname = qdflag;
				processCode = "BPM-CA59ADF7";
				bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '" + processCode + "'");
			} else {
				processinsVar = "利润减少" + "-" + false + "-" + "利润减少";
				taskname = flag ? "经理审核2" : "经理审核1";
				if (StrUtils.isNull(nextAssignUser) || "0".equals(nextAssignUser)) {    //没有指派审核人默认指派人为部门经理
					String userSql = "SELECT COALESCE((SELECT id FROM sys_user WHERE isdelete = false AND " +
							"deptid = ANY(SELECT deptid  FROM sys_user WHERE isdelete = false " +
							"AND id = (SELECT saleid FROM fina_jobs where isdelete = FALSE AND id = " + jobid + ") limit 1) " +
							"AND jobdesc = '经理' LIMIT 1),0) as userid";        //当前用户查询不到部门经理则默认为空
					Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(userSql);
					nextAssignUser = String.valueOf(m.get("userid"));
				}
				processCode = "BPM-968CACDE";
				bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '" + processCode + "'");
			}

			String remark = AppUtils.getReqParam("bpmremark");    //流程备注
			String sqlsub = "SELECT f_bpm_processStart('processid=" + bpmProcess.getId() + ";userid=" + AppUtils.getUserSession().getUserid() +
					";assignuserid=" + nextAssignUser + ";bpmremarks=" + remark + ";taskname=" + taskname + ";refno=" + msg +
					";refid=" + jobid + ";arapinfo=" + array + ";processinsVar=" + processinsVar + "') AS rets;";
			Map sm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlsub);
			String sub = sm.get("rets").toString();
			MessageUtils.alert("OK");
			Browser.execClientScript("bpmWindowJsVar.hide();");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Bind
	@SaveState
	public String qryuserdesc = "";

	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		GridView grid = new GridView() {
		};
		Map map = grid.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(map, "qry");
		qryuserdesc = StrUtils.getSqlFormat(qryuserdesc);
		qryuserdesc = qryuserdesc.toUpperCase();
		if(!StrUtils.isNull(qryuserdesc) ){
			qry += "AND (code ILIKE '%"+qryuserdesc+"%' OR namec ILIKE '%"+qryuserdesc+"%' OR namee ILIKE '%"+qryuserdesc+"%' OR depter2 ILIKE '%"+qryuserdesc+"%' OR company ILIKE '%"+qryuserdesc+"%')";
			map.put("qry", qry);
		}
		return map;
	}

	@Bind
	@SaveState
	public String user = "";

	@Action
	public void confirm() {
		String[] ids = this.gridUser.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}
		this.nextAssignUser = ids[0];
		Map m = null;
		try {
			m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT namec||',' AS name FROM sys_user where isdelete = false and id ="+ids[0]);
			this.user = m.get("name").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}

	@Action
	public void confirmAndClose(){
		this.confirm();
		Browser.execClientScript("userWindowJs.hide();");
	}

	@Action
	public void gridUser_ondblclick() {
		confirmAndClose();
	}

	@Action
	public void qryuser() {
		this.gridUser.reload();
	}

	@Bind(id="taskDefine")
    public List<SelectItem> getTaskDefine() {
		List<SelectItem> lists = new ArrayList<SelectItem>();
//		try{
//			String processCode = "BPM-968CACDE";
//			BpmProcess bpmProcess = serviceContext.bpmProcessService.bpmProcessDao.findOneRowByClauseWhere("code = '"+processCode+"'");
//			String sql = "SELECT * FROM bpm_assign WHERE process_id = "+bpmProcess.getId()+" AND isdelete = false AND taskname NOT IN('Start','End') order by step";
//			List<Map> maps = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//			for(Map map:maps){
//				if("提交".equals(map.get("taskname"))||"副总审核".equals(map.get("taskname"))||"结束".equals(map.get("taskname"))){
//					continue;
//				}
//				SelectItem selectItem = new SelectItem(StrUtils.getMapVal(map, "taskname"));
//				lists.add(selectItem);
//			}
//		}catch(NoRowException e) {
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		SelectItem selectItem = new SelectItem("经理审核");//只展示一条经理审核
		lists.add(selectItem);
		return lists;
    }

	@Bind
	@SaveState
	public String nextAssignUser = "";

	@Bind
	@SaveState
	public String bpmremark = "";
	/**
	 * 增减费用流程
	 * @param sb
	 */
	@Action
	public void bpmReview(StringBuffer sb){
		try {
			sb.append("，bpmReview开始");

			List<FinaArap> list = initArapDate();
			double usd = 0d;
			double cny = 0d;
			double hkd = 0d;
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setInputer(AppUtils.getUserSession().getUsername());
				String ar = list.get(i).getAraptype();
				list.get(i).setIsamend(true);
				if("USD".equals(list.get(i).getCurrency())){
					if("AR".equals(list.get(i).getAraptype())){
						usd += Double.valueOf(String.valueOf(list.get(i).getAmount()));
					}else{
						usd -= Double.valueOf(String.valueOf(list.get(i).getAmount()));
					}
				}
				if("CNY".equals(list.get(i).getCurrency())){
					if("AR".equals(list.get(i).getAraptype())){
						cny	+= Double.valueOf(String.valueOf(list.get(i).getAmount()));
					}else{
						cny	-= Double.valueOf(String.valueOf(list.get(i).getAmount()));
					}
				}
				if("HKD".equals(list.get(i).getCurrency())){
					if("AR".equals(list.get(i).getAraptype())){
						hkd += Double.valueOf(String.valueOf(list.get(i).getAmount()));
					}else{
						hkd -= Double.valueOf(String.valueOf(list.get(i).getAmount()));
					}
				}
			}

			//代垫费用录入验证--增减费用
			if("substitute".equals(editType)){
				//判断是否录入偶数条费用
				if (list.size() % 2 == 0) {
					if (!areListsEqual(list)) {
						MessageUtils.alert("录入代垫费用格式不正确，请确保应收和应付金额，币种，结算地，费用类型一致!");
						Browser.execClientScript("bpmWindowJsVar.hide();");
						return;
					}
				} else {
					MessageUtils.alert("录入代垫费用格式不正确，请确保应收和应付金额，币种，结算地，费用类型一致!");
					Browser.execClientScript("bpmWindowJsVar.hide();");
					return;
				}
			}

			String amtsql = "";
			if(cny != 0d){
				amtsql = "SELECT f_amtto(NOW(), 'CNY', 'USD',"+cny+")";
			}
			if(hkd != 0d){
				if(StrUtils.isNull(amtsql)){
					amtsql = "select f_amtto(NOW(), 'HKD', 'USD',"+hkd+")";
				}else{
					amtsql += "+f_amtto(NOW(), 'HKD', 'USD',"+hkd+") AS f_amtto";
				}
			}
			if(!StrUtils.isNull(amtsql)){
				Map map2 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(amtsql);
				flag = (Double.valueOf(String.valueOf(map2.get("f_amtto")))+usd)<-200;
			}else{
				flag = usd<-200;
			}

			array = JSONArray.fromObject(list);
			sb.append("，已获取到array为").append(array.toString());

			String profitDisclosure = null == ConfigUtils.findSysCfgVal("fina_arap_profit_disclosure") ? "N" : ConfigUtils.findSysCfgVal("fina_arap_profit_disclosure");
			String sql = "\nSELECT f_findarapinfo('jobid=" + this.jobid
					+ ";tax=Y;userid=" + AppUtils.getUserSession().getUserid()
					+ ";currency=" + this.currency
					+ ";profitDisclosure=" + profitDisclosure
					+ "') AS arapinfo";
			Map m = this.serviceContext.arapMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			String arapinfo = StrUtils.getMapVal(m, "arapinfo");
			String[] tips = arapinfo.split(",");
			if (tips != null && tips.length == 5) {
				arTip = tips[0];
				apTip = tips[1];
			}

			sb.append("，已获取到arTip为").append(arTip.toString());
			sb.append("，已获取到apTip为").append(arTip.toString());

			amendfeeiinfo = array.toString();
			profitGrid.reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Action
	public void delBPM() {
		this.nextAssignUser ="";
		this.user = "";
		this.bpmremark = "";
		update.markUpdate(true, UpdateLevel.Data, "bpmpanel");
	}


	@Bind
	public UIDataGrid profitGrid;

	@Bind(id = "profitGrid", attribute = "dataProvider")
	protected GridDataProvider getProfitGrid() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				if(StrUtils.isNull(amendfeeiinfo)){
					return null;
				}
				net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(amendfeeiinfo);
				double[] d = new double[6];
				if(arTip.indexOf("CNY:")>-1){
					d[0] = Double.valueOf(arTip.split("CNY:")[1].split("（")[0]);
				}
				if(arTip.indexOf("HKD:")>-1){
					d[1] = Double.valueOf(arTip.split("HKD:")[1].split("（")[0]);
				}
				if(arTip.indexOf("USD:")>-1){
					d[2] = Double.valueOf(arTip.split("USD:")[1].split("（")[0]);
				}
				if(apTip.indexOf("CNY:")>-1){
					d[0] = d[0]-Double.valueOf(apTip.split("CNY:")[1].split("（")[0]);
				}
				if(apTip.indexOf("HKD:")>-1){
					d[1] = d[1]-Double.valueOf(apTip.split("HKD:")[1].split("（")[0]);
				}
				if(apTip.indexOf("USD:")>-1){
					d[2] = d[2]-Double.valueOf(apTip.split("USD:")[1].split("（")[0]);
				}

				DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				Map<String,String> dateMap = (Map)((Map) array.get(0)).get("arapdate");
				String sql = "SELECT x.xtype||x.rate AS hkdtousd,(SELECT xtype||rate AS cnytousd FROM _dat_exchangerate WHERE datafm < '"+fmt.format(dateMap.get("time"))+"'	AND currencyto = 'USD'	AND currencyfm = 'CNY'	ORDER BY datafm DESC LIMIT 1)	FROM _dat_exchangerate x WHERE x.datafm < '"+fmt.format(dateMap.get("time"))+"'	AND x.currencyto = 'USD'	AND x.currencyfm = 'HKD'	ORDER BY x.datafm DESC LIMIT 1";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				Map<String,String> maps = new HashMap<String, String>();
				maps.put("profit_cny", d[0]+"");
				maps.put("profit_hkd", d[1]+"");
				maps.put("profit_usd", d[2]+"");
				double usd = 0d;
				double usd2 = 0d;
				if(d[1] != 0){
					if(String.valueOf(m.get("hkdtousd")).indexOf("/")>-1){
						usd2 = d[1]/Double.valueOf(String.valueOf(m.get("hkdtousd")).replace("/",""));
					}else{
						usd2 = d[1]*Double.valueOf(String.valueOf(m.get("hkdtousd")).replace("*",""));
					}
				}
				if(d[2] != 0){
					if(String.valueOf(m.get("cnytousd")).indexOf("/")>-1){
						usd = d[0]/Double.valueOf(String.valueOf(m.get("cnytousd")).replace("/",""));
					}else{
						usd = d[0]*Double.valueOf(String.valueOf(m.get("cnytousd")).replace("*",""));
					}
				}
				maps.put("profit", String.format("%.2f",(d[2]+usd2+usd)));
				maps.put("amenddesc", "增减前");
				Object[] objs = {maps,maps};
				maps = new HashMap<String, String>();

				for (int i = 0; i < array.size(); i++) {
					Map map = (Map) array.get(i);
					if("CNY".equals(String.valueOf(map.get("currency"))) && "AR".equals(String.valueOf(map.get("araptype")))){
						d[0] = d[0]+Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("HKD".equals(String.valueOf(map.get("currency"))) && "AR".equals(String.valueOf(map.get("araptype")))){
						d[1] = d[1]+Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("USD".equals(String.valueOf(map.get("currency"))) && "AR".equals(String.valueOf(map.get("araptype")))){
						d[2] = d[2]+Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("CNY".equals(String.valueOf(map.get("currency"))) && "AP".equals(String.valueOf(map.get("araptype")))){
						d[0] = d[0]-Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("HKD".equals(String.valueOf(map.get("currency"))) && "AP".equals(String.valueOf(map.get("araptype")))){
						d[1] = d[1]-Double.valueOf(String.valueOf(map.get("amount")));
					}
					if("USD".equals(String.valueOf(map.get("currency"))) && "AP".equals(String.valueOf(map.get("araptype")))){
						d[2] = d[2]-Double.valueOf(String.valueOf(map.get("amount")));
					}
				}
				if(d[1] != 0){
					if(String.valueOf(m.get("hkdtousd")).indexOf("/")>-1){
						usd2 = d[1]/Double.valueOf(String.valueOf(m.get("hkdtousd")).replace("/",""));
					}else{
						usd2 = d[1]*Double.valueOf(String.valueOf(m.get("hkdtousd")).replace("*",""));
					}
				}
				if(d[2] != 0){
					if(String.valueOf(m.get("cnytousd")).indexOf("/")>-1){
						usd = d[0]/Double.valueOf(String.valueOf(m.get("cnytousd")).replace("/",""));
					}else{
						usd = d[0]*Double.valueOf(String.valueOf(m.get("cnytousd")).replace("*",""));
					}
				}
				maps.put("profit_cny", d[0]+"");
				maps.put("profit_hkd", d[1]+"");
				maps.put("profit_usd", d[2]+"");
				maps.put("profit", String.format("%.2f",(d[2]+usd2+usd)));
				maps.put("amenddesc", "增减后");
				objs[1] = maps;
				return objs;
			}

			@Override
			public int getTotalCount() {
				int count = 2;
				return count;
			}
		};
	}

	/**
	 * 代垫费用修改
	 * @param jobid
	 * @param arapid
	 * @return
	 */
	private String substituteEditInit(String jobid, String arapid) {
		if (!arapid.contains(",")) {
			MessageUtils.alert("请选择对应的代垫类型费用进行修改!");
			return null;
		}
		String ret = "''";
		this.jobid = Long.parseLong(jobid);
		jobs_lock(this.jobid);
		initAutoData();
		if (!StrUtils.isNull(arapid)) {
			String[] ids = arapid.split(",");
			this.ids = arapid;
			ret = this.serviceContext.arapMgrService.getArapsJsonByJobid(Long.parseLong(jobid), ids,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent());
		} else {
			ret = this.serviceContext.arapMgrService.getArapsJsonByJobid(Long.parseLong(jobid), null,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent());
		}
		return StrUtils.isNull(ret) ? "''" : ret;
	}

	/**
	 * 比较多个费用对象 是否相等
	 * @param lists
	 * @param <T>
	 * @return
	 */
	public boolean areListsEqual(List<FinaArap>... lists) {
		List<FinaArap> firstList = lists[0];
		List<FinaArap> ArList = new ArrayList<FinaArap>();
		List<FinaArap> ApList = new ArrayList<FinaArap>();
		for (int i = 0; i < firstList.size(); i++) {
			if (firstList.get(i).getAraptype().equals("AR")) {
				ArList.add(firstList.get(i));
			}else{
				ApList.add(firstList.get(i));
			}
		}
		boolean isAreEqual = false;
		//判斷应收应付是否数量一致
		if (ArList.size() == ApList.size()) {
			BigDecimal arSum = BigDecimal.ZERO;
			BigDecimal apSum = BigDecimal.ZERO;
			//应收相加
			for (FinaArap ar : ArList) {
				arSum = arSum.add(ar.getAmount());
			}
			//应付相加
			for (FinaArap ap : ApList) {
				apSum = apSum.add(ap.getAmount());
			}
			//代垫应收应付金额是否一致
			if (!arSum.equals(apSum)) {
				return false;
			}
			//判断金额 币种 结算地是否一致
			for (int i = 0; i < ArList.size(); i++) {
				for (int j = 0; j < ApList.size(); j++) {
					if (ArList.get(i).getAmount().equals(ApList.get(j).getAmount()) && ArList.get(i).getCorpid().equals(ApList.get(j).getCorpid())
							&& ArList.get(i).getCurrency().equals(ApList.get(j).getCurrency()) && ArList.get(i).getRptype().equals(ApList.get(j).getRptype())) {
						ApList.remove(j);
						if (ApList.size() == 0) {
							isAreEqual = true;
						}
						break;
					} else {
						isAreEqual = false;
						continue;
					}
				}
			}

		} else {
			return false;
		}
		return isAreEqual;
	}

	@Bind
	public UIButton impOtherJobs;

	@Action
	public void impJobs() {
		String[] ids = this.gridArap.getSelectedIds();
		if(ids == null){
			MessageUtils.alert("请勾选一条记录！");
			return;
		}

		StringBuffer sb = new StringBuffer();
		for (String s : ids) {
			sb.append(s);
			sb.append(",");
		}
		Browser.execClientScript("arapchooserWindowJs.hide();");
		Browser.execClientScript("parent.refreshIFrameArapSubmit.addParam('iframArapData','"+sb.substring(0, sb.lastIndexOf(","))+"');");
		Browser.execClientScript("parent.refreshIFrameArapSubmit.submit();");
	}

	@Bind
	public UIDataGrid gridArap;

	@Bind(id = "gridArap", attribute = "dataProvider")
	protected GridDataProvider getArapGrid() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.other.arapchooserBean.grid.page";
				Object[] objects = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere3(qryMapArap), start, limit).toArray();
				return objects;

			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.other.arapchooserBean.grid.count";
				List<Map> list = serviceContext.daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId,getQryClauseWhere3(qryMapArap));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}

	//过滤条件
	public Map getQryClauseWhere3(Map<String, Object> queryMap) {
		GridView grid = new GridView() {};

		if(StrUtils.isNull((String)queryMap.get("jobno")) && StrUtils.isNull((String)queryMap.get("refno"))){
			queryMap.put("jobid$", jobid);
		}else {
			queryMap.remove("jobid$");
		}

		Map map = grid.getQryClauseWhere(queryMap);

		String jobno = StrUtils.getSqlFormat(StrUtils.getMapVal(queryMap, "jobno"));

		String sql = "";
		sql = "\nAND t.jobid = ANY(select x.id FROM fina_jobs x where x.nos ILIKE '%"+jobno+"%')";
		//（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
		sql += "\nAND ( EXISTS(SELECT 1 FROM fina_jobs x WHERE  x.saleid = "+AppUtils.getUserSession().getUserid() + " AND x.id = t.jobid AND x.isdelete = false)"
				+ "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
				+ "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this',"+AppUtils.getUserSession().getUserid()+")) " + //能看所有外办订到本公司的单权限的人能看到对应单
				"AND EXISTS(SELECT 1 FROM fina_jobs x WHERE x.corpidop = "+AppUtils.getUserSession().getCorpid()+" AND x.id = t.jobid AND x.corpid <> x.corpidop ))"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_user y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND y.userid = "+AppUtils.getUserSession().getUserid()
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and EXISTS(SELECT 1 FROM fina_jobs x WHERE z.id = x.saleid AND x.id = t.jobid AND x.isdelete = false) AND z.isdelete = false) " //关联的业务员的单，都能看到
				+ ")"
				+ "\n	OR EXISTS"
				+ "\n				(SELECT "
				+ "\n					1 "
				+ "\n				FROM sys_custlib x , sys_custlib_role y  "
				+ "\n				WHERE y.custlibid = x.id  "
				+ "\n					AND EXISTS (SELECT * FROM sys_userinrole z WHERE z.userid = "+AppUtils.getUserSession().getUserid()+" AND z.roleid = y.roleid)"
				+ "\n					AND x.libtype = 'S'  "
				+ "\n					AND x.userid = t.saleid  " //组关联业务员的单，都能看到
				+ ")"
				//过滤工作单指派
				+ "\n	OR EXISTS(SELECT 1 FROM sys_user_assign s WHERE s.linktype = 'J' AND EXISTS(SELECT 1 FROM _fina_jobs x WHERE s.linkid = x.shipid AND x.id = t.jobid AND x.isdelete = false) AND s.userid ="
				+ AppUtils.getUserSession().getUserid() + ")"
				+ "\n)" +
				"\n	AND (EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid='||t.jobid||'" + ";userid="
				+ AppUtils.getUserSession().getUserid()
				+ ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()+"')  x WHERE x.id = t.id) " +
				" OR (t.corpid = 157970752274 AND EXISTS(SELECT 1 FROM fina_jobs j WHERE j.id = t.jobid AND j.isdelete = FALSE AND j.corpid = ANY(SELECT COALESCE(xx.corpid,0) from sys_user_corplink xx WHERE xx.userid = "+AppUtils.getUserSession().getUserid()+" and ischoose = true))))";
		map.put("filter", sql);

		return map;
	}

	@Action
	public void qryarap() {
		this.gridArap.reload();
	}

	/**
	 * 导入工作单费用修改
	 * @param jobid
	 * @param arapid
	 * @return
	 */
	private String importArapEditInit(String jobid,String arapid) {
		String ret = "''";
		this.jobid = Long.parseLong(jobid);
		initAutoData();
		if (!StrUtils.isNull(arapid)) {
			String[] ids = arapid.split(",");
			this.ids = arapid;
			ret = this.serviceContext.arapMgrService.getArapsJsonByArapid(ids,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent());
		} else {
			ret = this.serviceContext.arapMgrService.getArapsJsonByArapid(null,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent());
		}
		return StrUtils.isNull(ret) ? "''" : ret;
	}

	@Bind
	@SaveState
	public boolean showquote = false;

	@Action
	public void showQuoteAction() {
		String newpage = "";
		String str = AppUtils.getReqParam("showQuote");
		if ("true".equals(str)) {
			newpage = "Y";
		} else {
			newpage = "N";
		}
		try {
			ConfigUtils.refreshUserCfg("show_quote", newpage, AppUtils.getUserSession().getUserid());
			Browser.execClientScript("parent.refreshIFrameQuoteSubmit.submit();");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增减费用流程--青岛自动获取审核人
	 */
	@Action
	public void setQdReviewer(){
		try {
			List<FinaArap> list = initArapDate();
			double usd = 0d;
			for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCorpid() != 1122274L) {
                    return;
                }
				list.get(i).setInputer(AppUtils.getUserSession().getUsername());
				String ar = list.get(i).getAraptype();
				list.get(i).setIsamend(true);
                if ("USD".equals(list.get(i).getCurrency())) {
                    if ("AR".equals(list.get(i).getAraptype())) {
                        usd += Double.valueOf(String.valueOf(list.get(i).getAmount()));
                    } else {
                        usd -= Double.valueOf(String.valueOf(list.get(i).getAmount()));
                    }
                } else {
                    String sql3 = "SELECT (f_amtto(now(), '" + list.get(i).getCurrency() + "', 'USD', " + list.get(i).getAmount() + ")) AS conusd";
                    Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql3);
                    if ("AR".equals(list.get(i).getAraptype())) {
                        usd += Double.valueOf(String.valueOf(m.get("conusd")));
                    } else {
                        usd -= Double.valueOf(String.valueOf(m.get("conusd")));
                    }
                }
			}
			if (usd >= 0) {
				qdflag = "经理审核1";
			} else if (usd < 0 && usd >= -100) {
				qdflag = "经理审核3";
			} else if (usd < -100) {
				qdflag = "经理审核4";
			}
			isgqflag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
