package com.scp.view.module.customs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;

import com.scp.base.CommonComBoxBean;
import com.scp.base.LMapBase;
import com.scp.base.MultiLanguageBean;
import com.scp.base.LMapBase.MLType;
import com.scp.dao.ship.BusCustomsDao;
import com.scp.model.finance.FinaJobs;
import com.scp.model.ship.BusCustoms;
import com.scp.model.ship.BusShipContainer;
import com.scp.model.sys.SysCorporation;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.customs.buscustomsBean", scope = ManagedBeanScope.REQUEST)
public class BusCustomsBean extends EditGridFormView {

	@Resource
	private BusCustomsDao busCustomsDao;

	@SaveState
	@Accessible
	public BusCustoms selectedRowData = new BusCustoms();


	@SaveState
	@Accessible
	public BusShipContainer dtlData = new BusShipContainer();
	
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
	 * 报关关联的所有货柜id
	 * 拼接：xxx,xxx,xxx
	 */
	@Bind
	@SaveState
	public String containerids;
	
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
	private UIButton chooseship;
	
	@Bind
	@Accessible
	public UIWindow showDtlWindow;
	
	@Bind
	@Accessible
	public String dtlContent;
	
	@SaveState
	public String customcompany="";
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if(!getSysformcfg().equals("")){
				String js = "setSysformcfg('"+getSysformcfg().replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
			
			if(selectedRowData.getCustomid()!=null&&this.selectedRowData.getCustomid()>0){
				SysCorporation sysCorporation = 
					serviceContext.customerMgrService.sysCorporationDao.findById(this.selectedRowData.getCustomid());
				String abbr = sysCorporation.getAbbr();
				this.customcompany=(abbr!=null&&!abbr.equals("")?abbr:sysCorporation.getNamec());
			}
			
		}
		super.applyGridUserDef();
	}

	public void init() {
		selectedRowData = new BusCustoms();
		String jobid = AppUtils.getReqParam("id");
		this.jobid = Long.valueOf(jobid);
		this.initCombox();
		String message = this.ShowMessage();
		if(message !=null&&!message.equals("")){
    		showDtlWindow.show();
    		this.dtlContent = message;
    		this.update.markUpdate(UpdateLevel.Data, "dtlContent");	
		}
		if(this.jobid != null) {
			FinaJobs finaJobs = this.serviceContext.jobsMgrService.finaJobsDao.findById(this.jobid);
			this.selectedRowData.setJobid(this.jobid);
			this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.pkVal);
			update.markUpdate(true, UpdateLevel.Data, "containerids");

			if (finaJobs.getIscheck()) {
				Browser.execClientScript("ischeckedited();");
			}
		}


	}
	
	public void initCombox() {
		if (this.jobid != null) {
			try {
				BusCustoms cusroms = busCustomsDao.findOneRowByClauseWhere(" jobid = " + jobid+ " AND custype = 'C'");
				this.pkVal = cusroms.getId();
				this.refreshMasterForm();
			} catch (Exception e) {
				addMaster();
			}
		}
	}

	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	@Action
	public void addMaster() {
		this.selectedRowData = new BusCustoms();
		this.selectedRowData.setJobid(this.jobid);
		// 设置默认签单时间为当天
		this.selectedRowData.setSingtime(new Date());
		// 设置默认状态为“初始”
		this.selectedRowData.setCustomstate("I");
		this.customstatedesc = (String)l.m.get("初始");
		this.pkVal = -1l;
		this.refreshMasterForm();
		refresh();

	}

	@Action
	public void delMaster() {
		if (selectedRowData.getId() == 0) {
			this.addMaster();
		} else {
			try {
				serviceContext.busCustomsMgrService.removeDate(selectedRowData
						.getId());
				this.addMaster();
				this.initCombox();
				this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.pkVal);
				update.markUpdate(true, UpdateLevel.Data, "containerids");
				Browser.execClientScript("showmsg()");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}

	/*
	 * 保存
	 */
	@Action
	public void saveMaster() {
		//地域标记,默认设为C,中国
		this.selectedRowData.setAreatype("C");
		//拿到页面上的报关公司id
		Long customid = this.selectedRowData.getCustomid();
		//根据id拿到公司的namec赋值到customabbr
		if(customid != null) {
			SysCorporation sc = this.serviceContext.customerMgrService.sysCorporationDao.findById(customid);
			this.selectedRowData.setCustomabbr(sc.getNamec());
		}
		//设置custype字段为C来区分报关  触发器中以此来区分是不是自动生成单号
		this.selectedRowData.setCustype("C");
		//保存
		this.serviceContext.busCustomsMgrService.saveData(this.selectedRowData);
		this.pkVal = this.selectedRowData.getId();
		this.initCombox();
		this.editGrid.commit();
		if (modifiedData != null) {
		    update(modifiedData);
		}
		if (addedData != null) {
		    add(addedData);
		}
		if(removedData != null){
			remove(removedData);
		}
	}
	
	@Override
	protected void update(Object modifiedData) {
		serviceContext.busShipContainerMgrService.updateBatchEditGrid(modifiedData);
	}
	
	@Override
	protected void add(Object addedData) {
		serviceContext.busShipContainerMgrService.addBatchEditGrid(addedData,jobid,selectedRowData.getId());
	}
	
	@Override
	protected void remove(Object removedData) {
		serviceContext.busShipContainerMgrService.removedBatchEditGrid(removedData);
	}

	@Action
	public void refreshMasterForm() {
		if(this.pkVal != -1L) {
			this.selectedRowData = this.busCustomsDao.findById(pkVal);
			if(this.selectedRowData != null) {
				if("I".equals( this.selectedRowData.getCustomstate())) {
					this.customstatedesc = (String)l.m.get("初始");
					this.disableAllButton(false);
				} else {
					this.customstatedesc = (String)l.m.get("完成");
					this.disableAllButton(true);
				}
				try {
					SysCorporation sysCorporation = serviceContext.sysCorporationService.sysCorporationDao.findById(selectedRowData.getOperationid());
					String js = "$('#customer_input').val('"+ (sysCorporation != null ? sysCorporation.getNamec() : "" )+ "');";
					Browser.execClientScript(js);
				} catch (Exception e) {
					Browser.execClientScript("$('#customer_input').val('');");
				}

				try {
					SysCorporation sysCorporation1 = serviceContext.sysCorporationService.sysCorporationDao.findById(Long.valueOf(selectedRowData.getDelivercompany()));
					String js1 = "$('#delivercompany_input').val('"+ (sysCorporation1 != null ? sysCorporation1.getNamec() : "" )+ "');";
					Browser.execClientScript(js1);
				} catch (Exception e) {
					Browser.execClientScript("$('#delivercompany_input').val('');");
				}
			}
		} else {
			this.disableAllButton(false);
		}
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true, UpdateLevel.Data, "gridPanel");
		this.refresh();
		this.editGrid.setSelections(getGridSelIds());
	}

	@Action
	public void addwindow() {
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		this.editWindow.show();
	}

	@Override
	public void del() {
		String[] ids = this.editGrid.getSelectedIds();
		if(ids == null || ids.length ==0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		Pattern p = Pattern.compile("\\d*");
		for(String id : ids) {
			if(p.matcher(id).matches()){
				try {
					serviceContext.busShipContainerMgrService.removeDate(Long.valueOf(id));
				} catch (Exception e) {
					MessageUtils.showException(e);
					return;
				}
			}
		}
		editGrid.remove();
		Browser.execClientScript("showmsg()");
	}

	@Override
	protected void doServiceFindData() {
		this.dtlData = serviceContext.busShipContainerMgrService.busShipContainerDao
				.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		serviceContext.busShipContainerMgrService.saveData(dtlData);
		Browser.execClientScript("showmsg()");
	}

	/*
	 * 关联
	 */
	@Action
	public void chooseship() {
		if (this.pkVal == -1l) {
			MessageUtils.alert((String)l.m.get("请先保存报关单"));
			return;
		}
		// 获取选中的柜子id
		String[] ids = this.editGrid.getSelectedIds();
		// 关联报关和柜子
		try {
			// 主柜和拆分柜不能同时选择
			if(ids != null && ids.length > 0) {
				for(String id : ids) {
					String sql = "SELECT COALESCE((SELECT parentid FROM bus_ship_container WHERE id = "+id+" AND isdelete = FALSE),0) IN ("+StrUtils.array2List(ids)+") AS flag";
					Map m = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
					Boolean flag = Boolean.valueOf(m.get("flag").toString());
					if(flag) {
						MessageUtils.alert((String)l.m.get("主柜和拆分柜不能同时选择")+"!");
						return;
					}
				}
			}
			
			this.serviceContext.busCustomsMgrService.busCustomsLink(this.pkVal, ids);
			// 根据报关id获得所有柜子id
			String cotainerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.pkVal);
			this.containerids = cotainerids;
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			update.markUpdate(UpdateLevel.Data, "containerids");
			this.refresh();
			this.editGrid.setSelections(getGridSelIds());
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/*
	 * Neo 2014/5/6
	 * 完成，改变状态
	 */
	@Action
	public void finish() {
		String customsState = this.selectedRowData.getCustomstate();
		String updater=AppUtils.getUserSession().getUsername();
		if(this.pkVal == -1L) {
			MessageUtils.alert((String)l.m.get("请先保存")+"!");
			return;
		}
		if("F".equals(customsState)) {
			MessageUtils.alert((String)l.m.get("已完成,请勿重复点击")+"!");
			return;
		} else {
			try {
				String sql = "UPDATE bus_customs SET customstate = 'F',updater='" + updater+ "',updatetime=NOW() WHERE id = "+this.pkVal+";";
//				if(!StrTools.isNull(selectedRowData.getTaxretno())) { //如果核销单号不为空，则插入一条退税核销纪录
//					sql += "\nINSERT INTO bus_customs_taxret (id,customsid) VALUES(getid(),"+this.pkVal+");";
//				}
				this.serviceContext.busCustomsMgrService.busCustomsDao.executeSQL(sql);
//				passCus();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		
		this.refreshMasterForm();
	}
	
	@Action
	public void cancel() {
		String customState = this.selectedRowData.getCustomstate();
		String updater=AppUtils.getUserSession().getUsername();
		if(this.pkVal == -1L) {
			MessageUtils.alert((String)l.m.get("请先保存")+"！");
			return;
		}
		if("I".equals(customState)) {
			MessageUtils.alert((String)l.m.get("尚未完成，无需取消")+"!");
			return;
		} else {
			try {
				String sql = "UPDATE bus_customs SET customstate = 'I',updater='" + updater+ "',updatetime=NOW() WHERE id = "+this.pkVal+";";
				sql += "\nDELETE FROM bus_customs_taxret WHERE customsid = "+this.pkVal+";";
				this.serviceContext.busCustomsMgrService.busCustomsDao.executeSQL(sql);
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
		this.refreshMasterForm();
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
					this.containerids = this.serviceContext.busCustomsMgrService.getLinkContainersid(this.pkVal);
					update.markUpdate(true, UpdateLevel.Data, "containerids");
					this.refreshMasterForm();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
	
	@Accessible
	public int[] getGridSelIds() {

		String sql = 
				"\nSELECT "+
				"\n(CASE WHEN EXISTS(SELECT 1 FROM bus_customs_link WHERE customsid=" + this.pkVal + " AND containerid = a.id) THEN TRUE ELSE FALSE END) AS flag"+
				"\nFROM _bus_ship_container a "+
				"\nWHERE a.isdelete = FALSE " +
				"\nAND a.jobid = " + this.jobid +
				"\nORDER BY cntno,sealno,ldtype";
		try {
			List<Map> list = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql(sql);
			List<Integer> rowList = new ArrayList<Integer>();
			int rownum = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				if ((Boolean) map.get("flag"))
					rowList.add(rownum);
				rownum++;
			}
			int row[] = new int[rowList.size()];
			for (int i = 0; i < rowList.size(); i++) {
				row[i] = rowList.get(i);
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += 
			//"\nAND (NOT EXISTS (SELECT 1 FROM bus_customs_link x WHERE x.containerid = t.id)" +
			//"\nOR EXISTS(SELECT 1 FROM bus_customs_link c WHERE c.customsid =" + this.pkVal + " AND c.containerid = t.id))"+
			"\nAND t.jobid = " + this.jobid;
		m.put("qry", qry);
		return m;
	}
	
	@Bind(id="reportbuscustomsmat")
	public List<SelectItem> getReportbuscustomsmat(){
		try{
			MLType mlType = AppUtils.getUserSession().getMlType();
			return CommonComBoxBean.getComboxItems("d.filename",mlType.equals(LMapBase.MLType.en)?"COALESCE(d.namee,d.namec)":"d.namec",
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
		Long userid = AppUtils.getUserSession().getUserid();
		String args = "";
		args += "&id=" + this.pkVal + "&userid=" + userid;
		return args;
	}
	
	private void disableAllButton(Boolean flag) {
		saveMaster.setDisabled(flag);
		delMaster.setDisabled(flag);
		chooseship.setDisabled(flag);
	}
	
	@Bind
	@SaveState
	public String splitCount;
	
	/*
	 * 拆柜
	 */
	@Action
	public void splitCnt() {
		String[] ids = this.editGrid.getSelectedIds();
		if(ids.length == 0 || ids == null) {
			MessageUtils.alert((String)l.m.get("选择一个柜子拆分")+"!");
			return;
		}
		if(StrUtils.isNull(splitCount) || "0".equals(splitCount)) {
			MessageUtils.alert((String)l.m.get("请选择拆分份数(不能为0)")+"!");
			return;
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "SELECT f_bus_ship_container_split('cntid="+ids[0]+";splitcount="+splitCount+";usercode="+usercode+"');";
		try {
			this.serviceContext.busCustomsMgrService.busCustomsDao.executeQuery(sql);
			Browser.execClientScript("showmsg()");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/*
	 * 选舱
	 */
	@Action
	public void chooseBook() {
		
		Long containerid = 0L;
		String[] ids = this.editGrid.getSelectedIds();
		if(ids==null||ids.length==0){
			containerid = 0L;
			
		}else if(ids.length>1){
			MessageUtils.alert((String)l.m.get("请选择单行记录")+"!");
			return;
		}else{
			
			containerid=Long.valueOf(ids[0]);
		}
		
		String sql = "SELECT id FROM bus_shipping WHERE isdelete = FALSE AND jobid =" + this.jobid + ";";
		String shipid = "";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			shipid = m.get("id").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = AppUtils.getContextPath() + "/pages/module/ship/shipbookingchoose.xhtml?shipid="+shipid+"&containerid="+containerid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}
	
	/*
	 * 取消选舱
	 */
	@Action
	public void cancelBook() {
		
		String[] ids = this.editGrid.getSelectedIds();
		if(ids==null||ids.length==0||ids.length<1){
			MessageUtils.alert((String)l.m.get("请勾选行")+"!");
			return;
		}
		try {
			serviceContext.busBookingMgrService.cancelBook(ids, AppUtils.getUserSession().getUsercode());
			Browser.execClientScript("showmsg()");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/*
	 * 保存关闭
	 */
	@Action
	protected void save2() {
		try {
			serviceContext.busShipContainerMgrService.saveData(dtlData);
			refresh();
			this.editWindow.close();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}
	
//	//点击完成按钮,自动完成改任务
//	public void passCus(){
//		try{
//				String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(this.jobid, WorkFlowEnumerateShip.CUS_END, "id");
//				WorkFlowUtil.passProcessBatch(workids);
//				MessageUtils.alert((String)l.m.get("已完成，自动流转到下一个环节"));
//				//正常走流程
//		} catch (EngineException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (KernelException e) {
//			MessageUtils.alert(e.getErrorMsg());
//			e.printStackTrace();
//		} catch (CommonRuntimeException e) {
//			MessageUtils.alert(e.getMessage());
//		}
//	}

	@Action
	public void showdtlaction() {
		String isShow = AppUtils.getReqParam("isShow");
		if ("1".equals(isShow)) {
			String message = this.ShowMessage();
			if (!StrUtils.isNull(message)) {
				showDtlWindow.show();
				this.dtlContent = message;
				this.update.markUpdate(UpdateLevel.Data, "dtlContent");
			} else {
				//MessageUtils.alert((String)l.m.get("无报关要求"));
			}
		}else{
			showDtlWindow.close();
		}
	}

//	public String ShowMessage() {
//		if (this.jobid != null) {
//			BusShipping busShipping = serviceContext.busShippingMgrService.busShippingDao
//					.findOneRowByClauseWhere("jobid = " + this.jobid + "");
//			String ClaimClear = busShipping.getClaimClear();
//			if (ClaimClear == null ||"".equals(ClaimClear)) {
//				return null;
//			}else{
//				return "\n报关要求\n" + ClaimClear + "";
//			}
//		}
//		return null;
//	}
	
	
	public String ShowMessage() {
//		String[] workids = WorkFlowUtil.getWorkitemIdsByJobid(this.jobid, WorkFlowEnumerateShip.CUS_END, "id");
		String re = "";
		String ins = "";
//		if(workids == null){
//			ins = "";
//		}else{
//			IWorkflowSession workflowSession = AppUtils.getWorkflowSession();
//			IWorkItem workItem = workflowSession
//					.findWorkItemById(workids[0]);
//			String processinstranseid = workItem.getTaskInstance().getProcessInstanceId();
//			ins=WorkFlowUtil.getActivityConsByWorkitem(processinstranseid, workids[0]);
//		}
		return re;
	}
	
	@Action
	public void addContainer(){
		dtlData = new BusShipContainer();
		dtlData.setJobid(this.jobid);
		dtlData.setShipid(this.selectedRowData.getId());
		try {
			List<BusShipContainer> temps = serviceContext.busShipContainerMgrService.busShipContainerDao.findAllByClauseWhere("jobid = "+this.jobid+" AND isdelete = false AND parentid IS NULL ORDER BY orderno");
			
			if(addedData != null){
				JSONArray jsonArray = (JSONArray) addedData;
				if(jsonArray != null && jsonArray.size() > 0){
					JSONObject jsonObject = (JSONObject) jsonArray.get(jsonArray.size()-1);
					if(jsonObject.get("cntypeid")!=null && !String.valueOf(jsonObject.get("cntypeid")).isEmpty()){
						dtlData.setCntypeid(Long.parseLong(String.valueOf(jsonObject.get("cntypeid"))));
					}else{
						dtlData.setCntypeid(null);
					}
					if(jsonObject.get("ldtype")!=null && !String.valueOf(jsonObject.get("ldtype")).isEmpty()){
						dtlData.setLdtype(String.valueOf(jsonObject.get("ldtype")));
					}else{
						dtlData.setLdtype(null);
					}
					if(jsonObject.get("packagee")!=null && !String.valueOf(jsonObject.get("packagee")).isEmpty()){
						dtlData.setPackagee(String.valueOf(jsonObject.get("packagee")));
					}else{
						dtlData.setPackagee(null);
					}
					if(temps == null || temps.size()<=0){
						dtlData.setOrderno(jsonArray.size()+1);
					}else{
						dtlData.setOrderno(temps.size()+jsonArray.size()+1);
					}
					if(jsonObject.get("grswgt2")!=null && !String.valueOf(jsonObject.get("grswgt2")).isEmpty()){
						dtlData.setGrswgt(new BigDecimal(String.valueOf(jsonObject.get("grswgt2"))));
					}else{
						dtlData.setGrswgt(new BigDecimal(0));
					}
					if(jsonObject.get("cbm2")!=null && !String.valueOf(jsonObject.get("cbm2")).isEmpty()){
						dtlData.setCbm(new BigDecimal(String.valueOf(jsonObject.get("cbm2"))));
					}else{
						dtlData.setCbm(new BigDecimal(0));
					}
					if(jsonObject.get("vgm")!=null && !String.valueOf(jsonObject.get("vgm")).isEmpty()){
						dtlData.setVgm(new BigDecimal(String.valueOf(jsonObject.get("vgm"))));
					}else{
						dtlData.setVgm(new BigDecimal(0));
					}
					if(jsonObject.get("piece2")!=null && !String.valueOf(jsonObject.get("piece2")).isEmpty()){
						dtlData.setPiece(new Integer(String.valueOf(jsonObject.get("piece2"))));
					}else{
						dtlData.setPiece(new Integer(0));
					}
				}else{
					String[] ids = editGrid.getSelectedIds();
					if(ids != null && ids.length > 0){//找到选择的最后一行
						String id = ids[ids.length-1];
						if(StrUtils.isNumber(id)){
							BusShipContainer temp = serviceContext.busShipContainerMgrService.busShipContainerDao.findById(Long.valueOf(id));
							dtlData.setCntypeid(temp.getCntypeid());
							dtlData.setLdtype(temp.getLdtype());
							dtlData.setPackagee(temp.getPackagee());
							dtlData.setGrswgt(temp.getGrswgt());
							dtlData.setPiece(temp.getPiece());
							dtlData.setCbm(temp.getCbm());
							dtlData.setVgm(temp.getVgm());
						}
					}else{
						try {//没有勾选的情况下，找到最后一行
							if(temps != null && temps.size()>0){
								dtlData.setCntypeid(temps.get(temps.size()-1).getCntypeid());
								dtlData.setLdtype(temps.get(temps.size()-1).getLdtype());
								dtlData.setPackagee(temps.get(temps.size()-1).getPackagee());
								dtlData.setGrswgt(temps.get(temps.size()-1).getGrswgt());
								dtlData.setPiece(temps.get(temps.size()-1).getPiece());
								dtlData.setCbm(temps.get(temps.size()-1).getCbm());
								dtlData.setVgm(temps.get(temps.size()-1).getVgm());
							}else{
								dtlData.setGrswgt(new BigDecimal(0));
								dtlData.setPiece(0);
								dtlData.setCbm(new BigDecimal(0));
								dtlData.setVgm(new BigDecimal(0));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					dtlData.setOrderno(temps.size()+1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		editGrid.appendRow(dtlData);
	}	
	
	@Action
	public void saveContainer(){
		try {
			editGrid.commit();
			boolean isChangeFlag = false;
			//如果列表里面修改的柜子，数据没有保存，先保存再处理后面数据  20190507 
			if (modifiedData != null) {
			    update(modifiedData);
			    JSONArray jsonArray = (JSONArray) modifiedData;
			    if(jsonArray.size()>0)isChangeFlag=true;
			}
			if (addedData != null) {
			    add(addedData);
			    JSONArray jsonArray = (JSONArray) addedData;
			    if(jsonArray.size()>0)isChangeFlag=true;
			}
			if(removedData != null){
				remove(removedData);
				JSONArray jsonArray = (JSONArray) removedData;
			    if(jsonArray.size()>0)isChangeFlag=true;				
			}
			if(isChangeFlag){
				editGrid.reload();
			}
			alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
