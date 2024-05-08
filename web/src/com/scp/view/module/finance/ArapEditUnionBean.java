package com.scp.view.module.finance;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.operamasks.faces.component.form.impl.UINumberField;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.FinaArap;
import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.arapeditunionBean", scope = ManagedBeanScope.REQUEST)
public class ArapEditUnionBean extends GridFormView {

	@SaveState
	@Accessible
	public FinaArap selectedRowData = new FinaArap();

	@SaveState
	@Accessible
	public FinaJobs jobs = new FinaJobs();

	@Bind
	@SaveState
	private Long arapfkid;

	@Bind
	@SaveState
	private Long jobid;

	@Bind
	@SaveState
	private Long customerid = -100l;

	@Bind
	@SaveState
	public String profitTipNoTax;


	@Bind
	public UIWindow batchEditWindow;

	@Bind
	public UIIFrame dtlIFrameArap;

	@Bind
	public UIButton add;

	@Bind
	public UIButton delBat;

	@Bind
	public UIButton showDynamic;

	@Bind
	public UIButton batchedit;

	@Bind
	public UIButton createrCurrent;

	@Bind
	public UIButton scanReport;

	@Bind
	public UIButton addwin;
	
	@Bind
	public UIButton copyadd;
	
	@Bind
	public UIButton save;
	
	@Bind
	public UIButton saveadd;
	
	@Bind
	public UIButton saveclose;
	
	@Bind
	public UIButton del;

	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {

			String id = AppUtils.getReqParam("id");
			String jobid = AppUtils.getReqParam("jobid");
			String customerid = AppUtils.getReqParam("customerid");

			if (!StrUtils.isNull(id)) {
				arapfkid = Long.parseLong(id);
			}
			if (!StrUtils.isNull(jobid)) {
				this.jobid = Long.parseLong(jobid);
				this.update.markUpdate(UpdateLevel.Data, "jobid");
			} else {
				MessageUtils.alert("参数jobid为空!");
			}

			if (!StrUtils.isNull(customerid) && !"null".equals(customerid)) {
				this.customerid = Long.parseLong(customerid);
			}

			if (this.jobid != null) {
				jobs = serviceContext.jobsMgrService.finaJobsDao
						.findById(this.jobid);
				// qryMap.put("jobid$", this.jobid);
				initAdd();
				// this.grid.repaint();
			}
		}
	}

	

	@SaveState
	private boolean isShowJoin = true;

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map map = super.getQryClauseWhere(queryMap);

		// neo 控制费用显示
		map.put("jobs", "EXISTS(SELECT 1 FROM f_fina_arap_filter('jobid="
				+ this.jobid + ";userid="
				+ AppUtils.getUserSession().getUserid()
				+ ";corpidcurrent="+AppUtils.getUserSession().getCorpidCurrent()
				+ "') x WHERE x.id = t.id)");

		if (isShowJoin) {// 显示合计，不显示明细
			map.put("isShowJoin", "(t.parentid IS NULL)");
		} else {// 显示明细，不显示合计
			map.put("isShowJoin", "(rptype <> 'O')");
		}

		return map;
	}

	public String getCustomercode() {
		String sql = "SELECT code ,abbr FROM sys_corporation  WHERE id ="
				+ customerid;
		List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
		return (String) list.get(0).get("code") + "/"
				+ (String) list.get(0).get("abbr");
	}



	protected void initAdd() {
		selectedRowData = new FinaArap();
		if (-100l == this.customerid) {
		} else {
			this.selectedRowData.setCustomercode(this.getCustomercode());
		}
		this.selectedRowData.setSharetype("N");
		this.selectedRowData.setFkid(arapfkid);
		this.selectedRowData.setCustomerid(customerid);
		this.selectedRowData.setPpcc("PP");
		this.selectedRowData.setRptype("O");
		this.selectedRowData.setJobid(jobid);
		this.selectedRowData.setCurrency(AppUtils.getUserSession()
				.getBaseCurrency());
		this.selectedRowData.setAraptype("AR");
		this.selectedRowData.setArapdate(new Date());
		this.selectedRowData.setTaxrate(new BigDecimal(1));
		this.selectedRowData.setPiece(new BigDecimal(1));
		this.update.markUpdate(UpdateLevel.Data, "editPanel");
	}

	@Action
	public void addwin() {
		add();
	}

	@Override
	public void add() {
		// grid.selectedIds = null;
		// String[] ids = this.grid.getSelectedIds();
		String[] ids = null;
		// String[] ids =
		// (java.lang.String[])GenComponentUtils.getValueFromExpression(this.grid,
		// "selectedIds");
		if (ids == null) {
			dtlIFrameArap.setSrc("./iframarapeditunion.xhtml?jobid=" + this.jobid+"&editType=add");
		} else {
			StringBuffer sb = new StringBuffer();
			for (String s : ids) {
				sb.append(s);
				sb.append(",");
			}
			dtlIFrameArap.setSrc("./iframarapeditunion.xhtml?jobid=" + this.jobid
					+ "&arapid=" + sb.substring(0, sb.lastIndexOf(",")));
		}
		update.markAttributeUpdate(dtlIFrameArap, "src");
		if (batchEditWindow != null)
			batchEditWindow.show();
	}

	@Override
	public void qryAdd() {
		super.qryAdd();
		initAdd();
	}

	@Action
	public void delBat() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			this.alert("Please choose one row!");
			return;
		}

		try {
			this.serviceContext.arapMgrService.removeDate(ids, AppUtils
					.getUserSession().getUsercode());
			this.alert("OK");
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Override
	public void del() {
		try {
			String[] ids = new String[] { String.valueOf(this.pkVal) };
			this.serviceContext.arapMgrService.removeDate(ids, AppUtils
					.getUserSession().getUsercode());
			this.alert("OK");
			this.add();
			this.grid.reload();
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	@Action
	public void copyadd() {
		this.pkVal = -100L;

		FinaArap temp = selectedRowData;

		FinaArap newObj = new FinaArap();
		newObj.setJobid(temp.getJobid());
		newObj.setAraptype(temp.getAraptype());
		newObj.setCustomerid(temp.getCustomerid());
		newObj.setCustomercode(temp.getCustomercode());
		newObj.setFkid(temp.getFkid());
		newObj.setFeeitemid(temp.getFeeitemid());
		newObj.setCurrency(temp.getCurrency());
		newObj.setPiece(temp.getPiece());
		newObj.setPpcc(temp.getPpcc());
		newObj.setUnit(temp.getUnit());
		newObj.setCorpid(temp.getCorpid());
		

		this.selectedRowData = newObj;
		this.update.markUpdate(true, UpdateLevel.Data, "pkId");
		// super.initAdd();
		this.selectedRowData.setAmount(new BigDecimal(0));
		this.selectedRowData.setBillid(null);
		this.selectedRowData.setBillxrate(new BigDecimal(0));
		this.selectedRowData.setBillamount(new BigDecimal(0));

		this.selectedRowData.setPrice(new BigDecimal(0));
		this.selectedRowData.setArapdate(new Date());
		this.selectedRowData.setTaxrate(new BigDecimal(1));
		this.selectedRowData.setRptype("O");

		this.update.markUpdate(UpdateLevel.Data, "amount");
		this.update.markUpdate(UpdateLevel.Data, "piece");
		this.update.markUpdate(UpdateLevel.Data, "price");
	}

	@Action
	public void copyaddAdd() {
		initAdd();
	}

	@Action
	public void share() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			this.alert("Please one row!");
			return;
		}
		try {
			String ret = this.serviceContext.arapMgrService.shareFee(ids,
					AppUtils.getUserSession().getUsercode());
			this.alert("OK!");
			this.refresh();
			//MessageUtils.showMsg("OK!\n" + ret);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
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
		return this.customerService.getAllCustomerDataProvider("");
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
				this.selectedRowData.setCustomerid((Long) cs.get(0).get("id"));
				this.selectedRowData.setCustomercode(cs.get(0).get("code")
						+ "/" + cs.get(0).get("abbr"));
				this.update.markUpdate(UpdateLevel.Data, "customerid");
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
		this.selectedRowData.setCustomerid((Long) m.get("id"));
		String customercode = (String) m.get("code") + "/"
				+ (String) m.get("abbr");
		this.selectedRowData.setCustomercode(customercode);
		this.update.markUpdate(UpdateLevel.Data, "customerid");
		this.update.markUpdate(UpdateLevel.Data, "customercode");
		showCustomerWindow.close();
	}

	@Override
	protected void doServiceFindData() {
		this.selectedRowData = this.serviceContext.arapMgrService.finaArapDao
				.findById(this.getGridSelectId());
	}

	@Override
	protected void doServiceSave() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		this.serviceContext.arapMgrService.saveData(selectedRowData);
		// initArapInfo();
	}

	/**
	 * 费用名称
	 * 
	 * @return
	 */
	@Bind(id = "feeitemtype")
	public List<SelectItem> getFeeitemtype() {
		try {
			String iswarehouse = "N";

			return CommonComBoxBean
					.getComboxItems(
							"d.id",
							"COALESCE(d.code,'')||'/'||COALESCE(d.name,'')",
							"dat_feeitem AS d",
							"WHERE ( iswarehouse = '"
									+ iswarehouse
									+ "' OR ispublic = TRUE ) AND NOT EXISTS(SELECT 1 FROM dat_feeitem_join x WHERE x.idfm = d.id AND x.jointype = 'J')",
							"ORDER BY d.code");
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
	public GridDataProvider getFeeitemGridDataProvider() {
		return this.customerService
				.getFeeitemDataProvider("\nAND iswarehouse='N'");
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
			List<Map> cs = customerService.findFeeitem(qryFeeitemKey,
					"N");
			if (cs.size() == 1) {
				Map map = cs.get(0);
				this.selectedRowData.setFeeitemid((Long) map.get("id"));
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
		this.selectedRowData.setFeeitemid(((Long) map.get("id")));
		this.update.markUpdate(UpdateLevel.ValueOnly, "feeitemid");
		showFeeitemWindow.close();
	}

	/*
	 * 保存新增
	 */
	@Action
	protected void saveadd() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		try {
			this.serviceContext.arapMgrService.saveData(selectedRowData);
			refresh();
			add();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/*
	 * 保存并关闭
	 */
	@Action
	protected void saveclose() {
		if (jobid <= 0) {
			MessageUtils.showMsg("Can't find jobid , please refresh!");
			return;
		}
		try {
			this.serviceContext.arapMgrService.saveData(selectedRowData);
			refresh();
			this.editWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public void refresh() {
		super.refresh();
	}

	@Action
	public void refreshAjaxSubmit(){
		this.refresh();
	}
	@Bind
	public UIWindow showbatchupdateWindow;

	@Bind
	public UIIFrame batchupdateFrame;

	@Action
	public void batchedit() {
		String[] ids = null;
		// String[] ids =
		// (java.lang.String[])GenComponentUtils.getValueFromExpression(this.grid,
		// "selectedIds");
		if (ids == null) {
			dtlIFrameArap.setSrc("./iframarapeditunion.xhtml?jobid=" + this.jobid+"");
		} else {
			StringBuffer sb = new StringBuffer();
			for (String s : ids) {
				sb.append(s);
				sb.append(",");
			}
			dtlIFrameArap.setSrc("./iframarapeditunion.xhtml?jobid=" + this.jobid
					+ "&arapid=" + sb.substring(0, sb.lastIndexOf(",")));
		}
		update.markAttributeUpdate(dtlIFrameArap, "src");
		if (batchEditWindow != null)
			batchEditWindow.show();
	}

	@Action
	public void batchadd(){
		this.add();
	}
	public void closeShowbatchupdateWindow() {
		showbatchupdateWindow.close();
	}

	@Action(id = "showbatchupdateWindow", event = "onclose")
	private void dtlEditDialogClose() {
		this.refresh();

	}


	@Bind
	@SaveState
	public UINumberField w_price;

	@Bind
	@SaveState
	public UINumberField w_pricenotax;

	@Bind
	@SaveState
	@Accessible
	public String priceFlag = "B-A";

	/**
	 * 税前单价/税后单价输入转换
	 */
	@Action
	public void switchPrice() {
		if (StrUtils.isNull(w_pricenotax.getCls())) {
			this.priceFlag = "A-B";
		} else {
			this.priceFlag = "B-A";
		}

		w_pricenotax
				.setCls(StrUtils.isNull(w_pricenotax.getCls()) ? "under_line"
						: "");
		w_price.setCls(StrUtils.isNull(w_price.getCls()) ? "under_line" : "");

		this.w_pricenotax.repaint();
		this.w_price.repaint();
	}


	@Action
	private void saveAjaxSubmit() {
		List<Long> idsOldArray = new ArrayList<Long>();
		idsOldArray.clear();
		List<Long> idsNewArray = new ArrayList<Long>();
		idsNewArray.clear();

		String data = AppUtils.getReqParam("data");
		if (!StrUtils.isNull(data) && !"null".equals(data)) {
			Type listType = new TypeToken<ArrayList<com.scp.vo.finance.FinaArap>>() {
			}.getType();// TypeToken内的泛型就是Json数据中的类型
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			ArrayList<com.scp.vo.finance.FinaArap> list = gson.fromJson(data,
					listType);// 使用该class属性，获取的对象均是list类型的

			ArrayList<com.scp.vo.finance.FinaArap> listOld = gson.fromJson(
					this.serviceContext.arapMgrService.getArapsJsonByJobid(this.jobid, null,AppUtils.getUserSession().getUserid(),AppUtils.getUserSession().getCorpidCurrent()), listType);

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
				finaArap.setRemarks(li.getRemarks());
				finaArap.setPiece(li.getPiece());
				finaArap.setPrice(li.getPrice());
				finaArap.setUnit(li.getUnit());
				finaArap.setCustomercode(li.getCustomecode());
				finaArap.setPpcc(li.getPpcc());
				finaArap.setSharetype("N");
				finaArap.setJobid(this.jobid);
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
					try {
						serviceContext.arapMgrService.removes(lists);
					} catch (Exception e) {
						MessageUtils.showException(e);
						return;
					}
				}
			}
			MessageUtils.alert("OK");
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
		////System.out.println("getDiffrent total times "+ (System.nanoTime() - st));
		return diff;
	}

	@Action
	public void deleteShare() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 0) {
			this.alert("Please one row!");
			return;
		}
		try {
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder stringBuilder2 = new StringBuilder();
			
			for (String id : ids) {
				stringBuilder.append("\nUPDATE fina_arap SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE fkid = "+id+" AND isdelete = FALSE;");
				stringBuilder2.append("\nUPDATE fina_arap SET rptype = 'G' , remarks = NULL WHERE id = "+id+" AND isdelete = FALSE;");
			}
			this.serviceContext.arapMgrService.finaArapDao.executeSQL(stringBuilder.toString());
			this.serviceContext.arapMgrService.finaArapDao.executeSQL(stringBuilder2.toString());
			this.refresh();
			Browser.execClientScript("showmsg()");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
}
