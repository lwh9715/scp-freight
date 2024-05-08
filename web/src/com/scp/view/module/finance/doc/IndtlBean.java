package com.scp.view.module.finance.doc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;
import org.springframework.transaction.support.TransactionTemplate;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.NoRowException;
import com.scp.model.finance.fs.FsAct;
import com.scp.model.finance.fs.FsActset;
import com.scp.model.finance.fs.FsVch;
import com.scp.model.finance.fs.FsVchdesc;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.JSONUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.ufms.base.db.DaoUtil;
import com.ufms.base.db.SqlObject;

@ManagedBean(name = "pages.module.finance.doc.indtlBean", scope = ManagedBeanScope.REQUEST)
public class IndtlBean extends GridFormView {
	
	@Resource(name="hibTtrTemplate")  
    private TransactionTemplate transactionTemplate;  
	
	/**
	 *  凭证主表id
	 */
	@Bind
	@SaveState
	public Long mPkVal = -1L;

	@Bind
	@SaveState
	public Long actsetid = -1L; // 如果是新增进来,那么取当前人绑定的帐套,如果是pk传入进来
								// ,取这个凭证对应的actsetid
	@Bind
	@SaveState
	public String json = "[]";
	
	@Bind
	@SaveState
	public String jsonData = "[]";//明细json
	@Bind
	@SaveState
	public String rateData = "''";//兑换率json
	
	@Bind
	@SaveState
	public String actsetcode = ""; // 如果是新增进来,那么取当前人绑定的帐套,如果是pk传入进来
									// ,取这个凭证对应的actsetid

	@SaveState
	@Accessible
	public FsVch selectedRowData = new FsVch();

	@SaveState
	@Accessible
	public FsAct act = new FsAct();

	@SaveState
	@Accessible
	public FsVchdesc vchdesc = new FsVchdesc();//摘要模版

	@Bind
	public UIWindow astWindow;//核算代码窗口

	@Bind
	public UIWindow vchdescWindow;//摘要编辑

	@Bind
	public UIDataGrid gridVchdesc;

	@SaveState
	@Accessible
	public FsActset fsActset = new FsActset();

	@Bind
	@SaveState
	public String basecy = "";
	@Bind
	@SaveState
	public String corpid = "";

	@SaveState
	public String year = "";

	@SaveState
	public String period = "";

	@Bind
	public UIButton del;
	
	@Bind
	@SaveState
	public String src = "";
	
	@Bind
	@SaveState
	public String inputer = "";
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			init();
			initPrintConfig();//初始化打印数据
			if (selectedRowData.getIscheck() == null) {

			} else {
				disableAllButton(selectedRowData.getIscheck());
			}
			src = AppUtils.getReqParam("src");
		}
	}

	@Bind
	@SaveState
	public String center_w;
	@Bind
	@SaveState
	public String table_w;
	@Bind
	@SaveState
	public String paper_w;
	@Bind
	@SaveState
	public String paper_h;
	@Bind
	@SaveState
	public String page_w;
	@Bind
	@SaveState
	public String page_h;
	@Bind
	@SaveState
	public String page_back_top;
	@Bind
	@SaveState
	public String page_back_left;
	@Bind
	@SaveState
	public String paper_name;
	
	@Bind
	@SaveState
	public String intOrient;
	
	@Bind
	public UIWindow showPringSeting;
	
	@Action
	public void showPrintConfig(){
		initPrintConfig();
		showPringSeting.show();
	}
	
	private void initPrintConfig(){
		try {
			
			Long userid = AppUtils.getUserSession().getUserid();
			
			center_w = ConfigUtils.findUserCfgVal("jsprint_config_center_w", userid);
			center_w = StrUtils.isNull(center_w) ? "750px" : center_w;
			table_w = ConfigUtils.findUserCfgVal("jsprint_config_table_w", userid);
			table_w = StrUtils.isNull(table_w) ? "750" : table_w;
			paper_w = ConfigUtils.findUserCfgVal("jsprint_config_paper_w", userid);
			paper_w = StrUtils.isNull(paper_w) ? "2410" : paper_w;
			paper_h = ConfigUtils.findUserCfgVal("jsprint_config_paper_h", userid);
			paper_h = StrUtils.isNull(paper_h) ? "1400" : paper_h;
			page_w = ConfigUtils.findUserCfgVal("jsprint_config_page_w", userid);
			page_w = StrUtils.isNull(page_w) ? "100%" : page_w;
			page_h = ConfigUtils.findUserCfgVal("jsprint_config_page_h", userid);
			page_h = StrUtils.isNull(page_h) ? "100%" : page_h;
			page_back_top = ConfigUtils.findUserCfgVal("jsprint_config_page_back_top", userid);
			page_back_top = StrUtils.isNull(page_back_top) ? "0" : page_back_top;
			page_back_left = ConfigUtils.findUserCfgVal("jsprint_config_page_back_left", userid);
			page_back_left = StrUtils.isNull(page_back_left) ? "1mm" : page_back_left;
			intOrient = ConfigUtils.findUserCfgVal("jsprint_config_intOrient", userid);
			intOrient = StrUtils.isNull(intOrient) ? "1" : intOrient;
			paper_name = ConfigUtils.findUserCfgVal("jsprint_config_paper_name", userid);
			paper_name = StrUtils.isNull(paper_name) ? "vch" : paper_name;
			} catch (Exception e) {
			MessageUtils.showException(e);
			}
			update.markUpdate(true,UpdateLevel.Data,"printPanel");
	}
	
	@Action
	public void savePrintConfig(){
		try {
			showPringSeting.close();
			ConfigUtils.refreshUserCfg("jsprint_config_center_w",center_w,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_table_w",table_w,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_paper_w",paper_w,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_paper_h",paper_h,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_page_w",page_w,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_page_h",page_h,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_page_back_top",page_back_top,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_intOrient",intOrient,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_page_back_left",page_back_left,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_paper_name",paper_name,AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void restorePrintConfig(){
		try {
			ConfigUtils.refreshUserCfg("jsprint_config_center_w","750px",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_table_w","750",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_paper_w","2410",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_paper_h","1400",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_page_w","100%",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_page_h","100%",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_page_back_top","0",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_intOrient","1",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_page_back_left","1mm",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_paper_name","vch",AppUtils.getUserSession().getUserid());
			initPrintConfig();
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Override
	public void clearQryKey() {
		super.clearQryKey();
	}

	public void init() {
		selectedRowData = new FsVch();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.mPkVal);
			this.setActsetid();
			
			try {
				inputer = serviceContext.userMgrService.sysUserDao.findOneRowByClauseWhere(" code = '" + this.selectedRowData.getInputer() + "' AND isdelete = FALSE").getNamec();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.jsonData = this.serviceContext.vchdtlMgrService.getVchdtlByVchId(this.mPkVal);
			this.initRate();
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
			update.markUpdate(true, UpdateLevel.Data, "jsonData");
		} else {
			addMaster();
		}
		if(this.selectedRowData != null && this.selectedRowData.getIscheck() != null && this.selectedRowData.getIscheck()){
			this.del.setDisabled(true);
		}
	}
	/**
	 * 初始化兑换率(一定要在初始化selectRowData和actsetid之后调用)
	 */
	public void initRate(){
		this.rateData = this.serviceContext.vchMgrService.vchXrate(actsetid, selectedRowData.getYear(), selectedRowData.getPeriod());
		update.markUpdate(true, UpdateLevel.Data, "rateData");
	}
	@Action
	public void refurbishRateAjax(){
		this.initRate();
	}
	
	@Override
	public void refresh() {
		this.setActsetid();
		if(this.mPkVal <= 0){
			this.addMaster();
		}else{
			this.selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.mPkVal);
			this.jsonData = this.serviceContext.vchdtlMgrService.getVchdtlByVchId(this.mPkVal);
			this.initRate();
			if(this.selectedRowData != null && this.selectedRowData.getIscheck() != null && this.selectedRowData.getIscheck()){
				this.saveMaster.setDisabled(true);
				this.del.setDisabled(true);
			}else{
				this.saveMaster.setDisabled(false);
				this.del.setDisabled(false);
			}
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
			update.markUpdate(true, UpdateLevel.Data, "jsonData");
			Browser.execClientScript("clearRows();");
			Browser.execClientScript("init();");
		}
	}
	
	protected void doServiceFindData() {
		
	}

	/**
	 * 取当前的年
	 * neo 2016-03-22
	 * @return
	 */
	public String getCurrentYear() {
		if(selectedRowData.getYear() == null) {
			return year;
		}else {
			return String.valueOf(selectedRowData.getYear());
		}
	}
	/**
	 * 取当前的期间，如果编辑bean有值直接取bean里面的值，如果没有取变量的值
	 * 之前兑换率取的总是当前期间的汇率，不对，凭证可能是任何期间的，应该取当前凭证的期间
	 * neo 2015-10-21
	 * @return
	 */
	public String getCurrentPeriod() {
		if(selectedRowData.getPeriod() == null) {
			return period;
		}else {
			return String.valueOf(selectedRowData.getPeriod());
		}
	}
	//
	//
	//
	
	
	@Action
	public void addMaster() {
		
		if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
			MessageUtils.alert("未选择帐套，请重新选择帐套！");
			return;
		}
		
		String y = "";
		String p = "";
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("\n SELECT name,COALESCE(year,0) AS year,COALESCE(period,0) AS period");
			sql.append("\n FROM fs_actset t");
			sql.append("\n WHERE id ="+AppUtils.getUserSession().getActsetid()+" AND isdelete = false LIMIT 1");
			Map map = this.serviceContext.userMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql.toString());
			if(map != null){
				y = map.get("year").toString();
				p = map.get("period").toString();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
			dateformat.applyPattern("yyyy");
			this.selectedRowData.setYear(new Short(dateformat.format(date)));
			dateformat.applyPattern("MM");
			this.selectedRowData.setPeriod(new Short(dateformat.format(date)));
		}
		try {
			if(new SimpleDateFormat("M").format(date).equals(p)){
				date = dateformat.parse(y+"-"+p+"-"+new SimpleDateFormat("dd").format(date));
			}else{
				date = dateformat.parse(y+"-"+p+"-01");
			}
		} catch (ParseException e) {
			this.selectedRowData.setSingtime(date);
			e.printStackTrace();
		}
		this.mPkVal = -1L;
		this.selectedRowData = new FsVch();
		this.selectedRowData.setPeriod(new Short(p));
		this.selectedRowData.setYear(new Short(y));
		this.selectedRowData.setSingtime(date);
		this.selectedRowData.setActsetid(this.actsetid);
		this.selectedRowData.setSrctype("H");
		this.jsonData = "[]";
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		update.markUpdate(true, UpdateLevel.Data, "jsonData");
		setActsetid();
		this.initRate();
		
		Long vchtypeid = 0l;
		try {
			String sql = "SELECT id FROM fs_vchtype where actsetid = "+this.actsetid+" and isdelete = false and isdefault = true order by code DESC limit 1";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			vchtypeid = (Long)map.get("id");
			this.selectedRowData.setVchtypeid(vchtypeid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//FsVchtype fs = this.serviceContext.vchtypeMgrService.fsVchtypeDao.findOneRowByClauseWhere(" actsetid="+this.actsetid+" AND name = '记'");
		try {
			inputer = serviceContext.userMgrService.sysUserDao.findById(AppUtils.getUserSession().getUserid()).getNamec();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		saveMaster.setDisabled(false);
		Browser.execClientScript("clearRows();");
		Browser.execClientScript("addindtlJs.fireEvent('click');");
	}

	/*
	 * 预览凭证
	 */
	@Action
	public void showVch() {
		String url = "";
		String pk = this.mPkVal.toString();
		pk = pk.substring(0, pk.length()-3);
		url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/v.raq&i="
				+ pk;
		AppUtils.openWindow(true,"indtl_vch", url ,false);
	}

	
	@Bind
	public UIButton saveMaster;

	@Bind
	public UIButton add;

//	@Bind
//	@SaveState
//	public String jsonDataStr = "''";
	
	/**
	 * 控制按钮是否可用
	 * 
	 * @param isCheck
	 */
	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	
	@Action
	private void saveAjaxSubmit() {
		if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
			MessageUtils.alert("未选择帐套，请重新选择帐套！");
			return;
		}
		try{
			StringBuilder sbMaster = new StringBuilder();
			StringBuilder sbDetail = new StringBuilder();
			String dataMaster = AppUtils.getReqParam("dataMaster");
			Long newPkId = 0l;
			if(mPkVal <= 0){
				newPkId = DaoUtil.getPkId();
				SqlObject sqlObject = new SqlObject("fs_vch" , dataMaster , AppUtils.getUserSession().getUsercode());
				sqlObject.setOpType("I");
				sqlObject.setFkVal("id" , newPkId.toString());
				sbMaster.append("\n"+sqlObject.toSql());
			}else{
				newPkId = mPkVal;
				SqlObject sqlObject = new SqlObject("fs_vch" , dataMaster , AppUtils.getUserSession().getUsercode());
				sbMaster.append("\n"+sqlObject.toSql());
			}
			
    		String dataDetail = AppUtils.getReqParam("dataDetail");
    		if(!StrUtils.isNull(dataDetail) && !"[]".equals(dataDetail)){
    			SqlObject sqlObject = new SqlObject("fs_vchdtl" , dataDetail , AppUtils.getUserSession().getUsercode());
        		sqlObject.setFkVals("vchid" , newPkId.toString());
        		sbDetail.append("\n"+sqlObject.toSqls());
        		//System.out.println("\n"+sb.toString());
    		}
    		String sql = sbDetail.toString() + sbMaster.toString(); //明细表要放到主表之前，主表有给检查凭证 fs_vch_bu  f_fs_checkvch 要子表执行完后才是正确数据
    		//System.out.println("\nIndtlBean.save:"+sql);
    		if(!StrUtils.isNull(sql)){
    			daoIbatisTemplate.updateWithUserDefineSql(sql);
    		}
			
    		selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(newPkId);//凭证号在触发器中生成，create后要重新load一次
    		if(newPkId > 0){
    			this.mPkVal = newPkId;
    		}
    		
			jsonData = serviceContext.vchdtlMgrService.getVchdtlByVchId(mPkVal);
			jsonData = jsonData.trim();
			if(StrUtils.isNull(jsonData))jsonData="[]";
			selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(mPkVal);
			
			update.markUpdate(true, UpdateLevel.Data, "jsonData");
			update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
			update.markUpdate(true, UpdateLevel.Data, "editPanel");
			
			
			MessageUtils.alert("OK");
			Browser.execClientScript("init();addLastEnterForNewRow();");
		}catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
	}
	
//	@Action
//	@Transactional
//	private void saveMaster(){
//		
//		if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
//			MessageUtils.alert("未选择帐套，请重新选择帐套！");
//			return;
//		}
//		
////		Object id = transactionTemplate.execute(new TransactionCallback() {  
////	        @Override  
////	        public String doInTransaction(TransactionStatus status) {
//	        	try {
//	    			if(mPkVal <= 0){
//	    				serviceContext.vchMgrService.fsVchDao.create(selectedRowData);
//	    				selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(selectedRowData.getId());//凭证号在触发器中生成，create后要重新load一次
//	    			}
//	    			FsVch selectedRowDataTmp = serviceContext.vchMgrService.fsVchDao.findById(selectedRowData.getId());
//	    			mPkVal = selectedRowData.getId();
//	    			//if(StrUtils.isNull(selectedRowData.getNos())){
//	    			//	selectedRowData.setNos(selectedRowDataTmp.getNos());
//	    			//}
//	    			update.markUpdate(true, UpdateLevel.Data, "editPanel");
//	    			update.markUpdate(true, UpdateLevel.Data, "mPkVal");
//	    			List<Long> idsOldArray = new ArrayList<Long>();
//	    			idsOldArray.clear();
//	    			List<Long> idsNewArray = new ArrayList<Long>();
//	    			idsNewArray.clear();
//	    			// 保存明细数据
//	    			if (!StrUtils.isNull(jsonDataStr) && !"null".equals(jsonDataStr) && !"''".equals(jsonDataStr)) {
//	    				Type listType = new TypeToken<ArrayList<com.scp.vo.finance.fs.FsVchdtl>>() {
//	    				}.getType();// TypeToken内的泛型就是Json数据中的类型
//	    				Gson gson = new Gson();
//	    				ArrayList<com.scp.vo.finance.fs.FsVchdtl> list = gson.fromJson(
//	    						jsonDataStr, listType);// 使用该class属性，获取的对象均是list类型的
//	    				ArrayList<com.scp.vo.finance.fs.FsVchdtl> listOld = gson.fromJson(
//	    						serviceContext.vchdtlMgrService.getVchdtlByVchId(mPkVal),
//	    						listType);
//	    				List<FsVchdtl> fsVchDtlList = new ArrayList<FsVchdtl>();
//	    				for (com.scp.vo.finance.fs.FsVchdtl li : list) {
//	    					FsVchdtl fsVchdtl = new FsVchdtl();
//	    					
//	    					if (li.getId() > 0) {
//	    						fsVchdtl = serviceContext.vchdtlMgrService.fsVchdtlDao.findById(li.getId());
//	    						idsNewArray.add(li.getId());
//	    					}
//	    					fsVchdtl.setActsetid(actsetid);
//	    					fsVchdtl.setVchid(selectedRowData.getId());
//	    					fsVchdtl.setActid(li.getActid());
//	    					fsVchdtl.setVchdesc(li.getVchdesc());
//	    					fsVchdtl.setCyid(li.getCyid());
//	    					fsVchdtl.setXtype(li.getXtype());
//	    					fsVchdtl.setXrate(li.getXrate());
//	    					fsVchdtl.setOamt(li.getOamt());
//	    					fsVchdtl.setDamt(li.getDamt());
//	    					fsVchdtl.setCamt(li.getCamt());
//	    					fsVchdtl.setVdorder(li.getVdorder());
//	    					fsVchdtl.setSrctype(li.getSrctype());
//	    					fsVchdtl.setIsautogen(li.getIsautogen());
//	    					fsVchdtl.setCorpid(li.getCorpid());
//	    					fsVchdtl.setAstid(li.getAstid());
//	    					fsVchDtlList.add(fsVchdtl);
//	    				}
//	    				serviceContext.vchdtlMgrService.saveOrModify(fsVchDtlList);
//	    				if (listOld != null && listOld.size() > 0) {
//	    					for (com.scp.vo.finance.fs.FsVchdtl li : listOld) {
//	    						idsOldArray.add(li.getId());
//	    					}
//	    					List<Long> lists = getDiffrent(idsOldArray, idsNewArray);
//
//	    					if (!lists.isEmpty()) {
//	    						serviceContext.vchdtlMgrService.removes(lists);
//	    					}
//	    				}
//	    			}
//	    			
//	    			serviceContext.vchMgrService.fsVchDao.modify(selectedRowData);
//	    			
//	    			jsonData = serviceContext.vchdtlMgrService.getVchdtlByVchId(mPkVal);
//	    			update.markUpdate(true, UpdateLevel.Data, "jsonData");
//	    			
//	    			selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(selectedRowData.getId());
//	    			update.markUpdate(true, UpdateLevel.Data, "vchno");
//	    			
//	    			MessageUtils.alert("OK!");
//	    		}catch (Exception e) {
//	    			MessageUtils.showException(e);
//	    			e.printStackTrace();
//	    		}
////	        	return "";
////	        }
////		});
//		
//	}
	/**
	 * 获取两个List的不同元素
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
//	private List<Long> getDiffrent(List<Long> list1, List<Long> list2) {
//		long st = System.nanoTime();
//		List<Long> diff = new ArrayList<Long>();
//		for (Long str : list1) {
//			if (!list2.contains(str)) {
//				diff.add(str);
//			}
//		}
//		return diff;
//	}
	
	@Override
	public void del() {
		super.del();
		if(this.mPkVal <= 0){
			MessageUtils.alert("请先保存!(Please save first!)");
			return;
		}
		try {
			this.serviceContext.vchMgrService.removeDate(new String[]{""+this.mPkVal} , AppUtils.getUserSession().getUsercode());
			this.mPkVal = -100L;
			this.addMaster();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}	
	// 凭证字
	@Bind(id = "vchtype")
	public List<SelectItem> getVchtype() {
		try {
			return CommonComBoxBean.getComboxItems("d.id", "d.name",
					"fs_vchtype AS d", "WHERE d.actsetid=" + this.actsetid,
					"ORDER BY d.code");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}
	
	//核算科目查询
	@Action
	public void clearQryKeyAst() {
		if (qryMapAst != null) {
			qryMapAst.clear();
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.qryRefreshAst();
		}
	}
	
	@Bind
	public UIDataGrid gridAst;

	@SaveState
	@Accessible
	public Map<String, Object> qryMapAst = new HashMap<String, Object>();

	@Bind(id = "gridAst", attribute = "dataProvider")
	protected GridDataProvider getDataProvider1() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridAst.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere1(qryMapAst),
								start, limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridAst.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere1(qryMapAst));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	@SaveState
	private String changesid = "-1";
	
	@SaveState
	private boolean isastypeb = false;
	
	@SaveState
	private boolean isastypec = false;
	
	@SaveState
	private boolean isastyped = false;
	
	@SaveState
	private boolean isastypee = false;
	
	@Action
	public void astgridreload() {
		String id = AppUtils.getReqParam("id");
		String isastypebstr = AppUtils.getReqParam("isastypeb");
		String isastypecstr = AppUtils.getReqParam("isastypec");
		String isastypedstr = AppUtils.getReqParam("isastyped");
		String isastypeestr = AppUtils.getReqParam("isastypee");
		this.changesid = id == null ? changesid : id;
		this.isastypeb = isastypebstr == null ? isastypeb : Boolean.parseBoolean(isastypebstr);
		this.isastypec = isastypecstr == null ? isastypec : Boolean.parseBoolean(isastypecstr);
		this.isastyped = isastypedstr == null ? isastyped : Boolean.parseBoolean(isastypedstr);
		this.isastypee = isastypeestr == null ? isastypee : Boolean.parseBoolean(isastypeestr);
		this.gridAst.rebind();
	}
	
	//根据对应的核算项目类别提取核算代码
	public Map getQryClauseWhere1(Map<String, Object> queryMapAst) {
		Map m = super.getQryClauseWhere(queryMapAst);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND t.actsetid = " + this.actsetid;
		String arg="";
		if(isastypeb||isastypec||isastyped||isastypee){
			arg += isastypeb ? ",'B'" : "";
			arg += isastypec ? ",'C'" : "";
			arg += isastyped ? ",'D'" : "";
			arg += isastypee ? ",'E'" : "";
			arg = arg != null ? arg.substring(1) : arg;
			qry += "\nAND t.astypecode IN (" + arg+")";
		}else{
			qry += "\nAND t.astypecode = ''";
		}
		
		if(isastypec && isastyped){
			m.put("filter1", "\nAND 1=2");
			m.put("filter2", "\nAND 1=1");
		}else{
			m.put("filter1", "\nAND 1=1");
			m.put("filter2", "\nAND 1=2");
		}
		
		m.put("qry", qry);
		return m;
	}
	
	//核算列表双击事件
	@Action
	public void gridAst_ondblclick() {
		String id = gridAst.getSelectedIds()[0];
		String sql = "SELECT (code||'/'||name) AS astdesc FROM fs_ast WHERE isdelete = FALSE AND id = " + id;
		try {
			Map m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			String astdescShow = String.valueOf(m.get("astdesc"));
			astdescShow = astdescShow.replaceAll("'", "");
			Browser.execClientScript("gridAst_ondblclick("+changesid+","+id+",'"+astdescShow+"');");
			this.astWindow.close();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 设置帐套
	 */
	public void setActsetid(){
		//新增进来 
		if(this.selectedRowData.getId()==0){
			String sql = "SELECT code||'/'||substring(COALESCE(name,'') , 1 , 5) AS actsetcode  FROM fs_actset WHERE isdelete = FALSE AND id = "
					+ AppUtils.getUserSession().getActsetid();
			Map m;
			
				try {
					m = this.serviceContext.daoIbatisTemplate
							.queryWithUserDefineSql4OnwRow(sql);
					this.actsetid = AppUtils.getUserSession().getActsetid();
					this.actsetcode= "账套:" + (String)m.get("actsetcode");
					selectedRowData.setActsetid(this.actsetid);
					//保存进来
				} catch (Exception e) {
					MessageUtils.alert("当前用户没有绑定帐套,不能新增凭证");
					return;
				}
				//查看明细... 或者保存以后 ... 
		}else{
			
			String sql = "SELECT code||'/'||substring(COALESCE(name,'') , 1 , 5) AS actsetcode  FROM fs_actset WHERE isdelete = FALSE AND id = abs("+ this.selectedRowData.getActsetid()+")";
			Map m;
		
			try {
				m = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				this.actsetid = Math.abs(this.selectedRowData.getActsetid());
				this.actsetcode= "账套:" + (String)m.get("actsetcode");
				//保存进来
			} catch (Exception e) {
				String rp2vchtemp = ConfigUtils.findSysCfgVal("rp2vchtemp");
				if(!StrUtils.isNull(rp2vchtemp) && "Y".equals(rp2vchtemp)){
					
				}else{
					MessageUtils.alert("当前用户凭证所在帐套已经删除,请检查!");
				}
				return;
			}
			//查看明细... 或者保存以后 ... 
		}
		this.setBaseCurrency();
		update.markUpdate(true, UpdateLevel.Data, "actsetid");
		update.markUpdate(true, UpdateLevel.Data, "actsetcode");
	}
	/**
	 * 设置本位币
	 * NEO 20150205
	 */
	public void setBaseCurrency(){
		//必须和新增一样 根据是否新增 或者 显示 进来 处理.否则会出现问题
		String sql = "SELECT year,period,basecy,corpid FROM fs_actset WHERE isdelete = FALSE AND id = "
				+ this.actsetid;
		Map m;
		try {
			m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			year = m.get("year").toString();
			period = m.get("period").toString();
			basecy = m.get("basecy").toString();
			corpid = m.get("corpid").toString();
			
		} catch (Exception e) {
			year = "";
			period = "";
			basecy = "CNY";
			corpid = AppUtils.getUserSession().getCorpid().toString();
		}finally{
			this.update.markUpdate(UpdateLevel.Data, "basecy");
		}
	}
	@Action
	public void qryRefreshAst() {
		this.gridAst.reload();
	}
	@SaveState
	@Accessible
	public Map<String, Object> qryMapVchdesc = new HashMap<String, Object>();

	@Bind(id = "gridVchdesc", attribute = "dataProvider")
	protected GridDataProvider getDataProvider4(){
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.doc.indtlBean.gridVchdesc.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere4(qryMapVchdesc), start, limit)
						.toArray();
			}
			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.finance.doc.indtlBean.gridVchdesc.count";
				List<Map> list = serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere4(qryMapVchdesc));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	public Map getQryClauseWhere4(Map<String, Object> queryMapVchdesc) {
		return super.getQryClauseWhere(queryMapVchdesc);
	}
	
	@Action
	public void qryRefreshVchdesc(){
		this.gridVchdesc.reload();
	}
	
	@Action
	public void saveVchdescsAjax(){
		String vchdescstr = AppUtils.getReqParam("vchdesc");
		if(!vchdescstr.isEmpty()){
			try {
				this.vchdesc = new FsVchdesc();
				this.vchdesc.setActsetid(AppUtils.getUserSession().getActsetid());
				this.vchdesc.setName(vchdescstr);
				this.serviceContext.vchdescMgrService.saveData(vchdesc);
				MessageUtils.alert("OK");
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	
	@Action
	public void pageUp(){
		String nos = this.selectedRowData.getNos();
		if(nos.contains("-")){
			nos =nos.split("-")[0];
		}
		if(StrUtils.isNull(nos)){
			nos ="0";
		}
		Long vchtypeid = this.selectedRowData.getVchtypeid();
		Integer nosnum=Integer.parseInt(nos);
		String seach = ConfigUtils.findUserSysCfgVal("pages.module.finance.doc.inBean.search", AppUtils.getUserSession().getUserid());
		String whereSql = "SELECT id FROM _fs_vch_main AS v WHERE "+seach+" AND isdelete = FALSE AND srctype != 'I' AND actsetid = "
			+ this.selectedRowData.getActsetid() + " AND year = "+this.selectedRowData.getYear()+" AND period = "+this.selectedRowData.getPeriod()+" AND nosnum <= "
			+nosnum + " ORDER BY nosnum DESC,id DESC";
		List<Map> lists = this.daoIbatisTemplate.queryWithUserDefineSql(whereSql);
		Map<String, Long> datas = new HashMap<String, Long>();
		for (int i = 0; i < lists.size(); i++) {
			datas = lists.get(i);
			if (((Long)datas.get("id")).equals(this.mPkVal)) {
				if (i != (lists.size() - 1)) {
					this.mPkVal = (Long) lists.get(i + 1).get("id");
					Browser.execClientScript("skip('"+this.mPkVal+"');");
					return;
				} else {
					MessageUtils.alert("第一条！");
				}
			}
		}
	}
	
	@Action
	public void pageDown(){
		String nos = this.selectedRowData.getNos();
		if(nos.contains("-")){
			nos =nos.split("-")[0];
		}
		if(StrUtils.isNull(nos)){
			nos ="0";
		}
		Long vchtypeid = this.selectedRowData.getVchtypeid();
		Integer nosnum=Integer.parseInt(nos);
		String seach = ConfigUtils.findUserSysCfgVal("pages.module.finance.doc.inBean.search", AppUtils.getUserSession().getUserid());
		String whereSql = "SELECT id FROM _fs_vch_main v WHERE "+seach+" AND isdelete = FALSE AND srctype != 'I' AND actsetid = "
			+ this.selectedRowData.getActsetid() + " AND year = "+this.selectedRowData.getYear()+" AND period = "+this.selectedRowData.getPeriod()+" AND nosnum >= "
			+nosnum+ " ORDER BY nosnum ,id";
		List<Map> lists = this.daoIbatisTemplate.queryWithUserDefineSql(whereSql);
		Map<String, Long> datas = new HashMap<String, Long>();
		//Map<Integer, Long> datas = new HashMap<Integer, Long>();
		for (int i = 0; i < lists.size(); i++) {
			datas = lists.get(i);
			if (((Long)datas.get("id")).equals(this.mPkVal)) {
				
				if (i != (lists.size() - 1)) {
					this.mPkVal = (Long) lists.get(i + 1).get("id");
					Browser.execClientScript("skip('"+this.mPkVal+"');");
					return;
				} else {
					MessageUtils.alert("最后一条！");
				}
			}
		}
	}

	@Override
	protected void doServiceSave() {
		
	}
	
	@Action
	public void ischeckAjaxSubmit(){
		if(this.selectedRowData != null && this.selectedRowData.getIscheck() != null){
			try {
				if(this.mPkVal > 0){
					boolean ischeck = this.selectedRowData.getIscheck();
					this.selectedRowData = this.serviceContext.vchMgrService.fsVchDao.findById(this.selectedRowData.getId());
					this.selectedRowData.setIscheck(ischeck);
					this.selectedRowData.setCheckter(AppUtils.getUserSession().getUsercode());
					this.selectedRowData.setChecktime(new Date());
					this.serviceContext.vchMgrService.fsVchDao.modify(this.selectedRowData);
					MessageUtils.alert("OK!");
					refresh();
				}else{
					MessageUtils.alert("Please save the voucher first!");
				}
			} catch (Exception e) {
				MessageUtils.showException(e);
				this.refresh();
			}
		}
	}
	
	@Action
	public void showPringView(){
		if(this.mPkVal <= 0 ){
			MessageUtils.alert("请先保存凭证再打印!");
			return;
		}
		try {
			String jsonSql = "SELECT f_print_vch_loop('ids="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";') AS json;";
			Map map2 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(jsonSql);
			if(map2 != null && map2.size() > 0 && map2.containsKey("json")){
				this.json = JSONUtil.getDescodeJSONStr(map2.get("json").toString());
				update.markUpdate(UpdateLevel.Data, "json");
				Browser.execClientScript("printVch();");
			}else{
				 throw new NoRowException();
			}
		} catch (NoRowException e) {
			MessageUtils.alert("没有找到凭证!");
		}catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
	
	@Action
	public void copyNewAction(){
		if(mPkVal <= 0){
			MessageUtils.alert("Please save first!");
			return;
		}
		this.mPkVal = -100l;
		FsVch selectedRowDataOld = selectedRowData;
		selectedRowData = new FsVch();
		this.selectedRowData.setId(-100l);
		this.selectedRowData.setActsetid(selectedRowDataOld.getActsetid());
		this.selectedRowData.setYear(selectedRowDataOld.getYear());
		this.selectedRowData.setSingtime(selectedRowDataOld.getSingtime());
		this.selectedRowData.setPeriod(selectedRowDataOld.getPeriod());
		this.selectedRowData.setSrctype("H");
		Browser.execClientScript("copyNew()");
		
	}
}
