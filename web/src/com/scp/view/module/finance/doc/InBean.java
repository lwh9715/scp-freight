package com.scp.view.module.finance.doc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.exception.NoRowException;
import com.scp.model.finance.fs.FsVch;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.doc.inBean", scope = ManagedBeanScope.REQUEST)
public class InBean extends GridFormView {
	
	@SaveState
	@Accessible
	public FsVch selectedRowData = new FsVch();
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@Bind
	@SaveState
	public UIWindow checkWindow;
	
	@SaveState
	@Accessible
	public String qryYear;
	
	@SaveState
	@Accessible
	public String checkYear;
	
	@SaveState
	@Accessible
	public String checkPeriod;
	
	@SaveState
	@Accessible
	public String checkVchType;
	
	@SaveState
	@Accessible
	public Integer checkVchnoS;
	
	@SaveState
	@Accessible
	public Integer checkVchnoE;
	
	@Bind
	@SaveState
	public boolean checkVchNo = true;
	
	@Bind
	@SaveState
	public boolean checkSameNo = true;
	
	
	@Bind
	@SaveState
	public String checkResult;
	
	@Bind
	@SaveState
	public boolean checkVchDC = true;
	
	@Bind
	@SaveState
	public boolean checkVchRate = true;
	
	@Bind
	@SaveState
	public boolean other = true;
	
	@SaveState
	@Accessible
	public String qryPeriodS;
	
	@SaveState
	@Accessible
	public String qryPeriodE;
	
	@SaveState
	@Accessible
	public String inputername;
	
	@SaveState
	@Accessible
	public Date vchDateS;
	
	@SaveState
	@Accessible
	public Date vchDateE;

	@SaveState
	@Accessible
	public Integer vchnoS;
	
	@SaveState
	@Accessible
	public Integer vchnoE;
	
	@Bind
	@SaveState
	public Long vchtypeid = null;
	@Bind
	@SaveState
	public Date firstvchtype;
	@Bind
	@SaveState
	public Date tailvchtype;
	
	@Bind
	@SaveState
	public boolean isa4;
	
	@Bind
	@SaveState
	public String json = "[]";
	
	@Override
	protected void doServiceFindData() {
		selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.pkVal);
	}

	@Override
	protected void doServiceSave() {
		try {
			serviceContext.vchMgrService.saveData(selectedRowData);
			this.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@BeforeRender
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			
			
			super.applyGridUserDef();
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			
			this.gridLazyLoad = true;
			
			// 根据当前登录用户，找对应的帐套，过滤兑换率列表
			String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
					+ AppUtils.getUserSession().getActsetid();
			try {
				Map m = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				qryYear = m.get("year").toString();
				copyYear = qryYear;
				qryPeriodS = m.get("period").toString();
				qryPeriodE = m.get("period").toString();
				copyPeriod = qryPeriodE;
				copyActset = AppUtils.getUserSession().getActsetid();
				checkYear = qryYear;
				checkPeriod = qryPeriodS;
			} catch (Exception e) {
				e.printStackTrace();
			}
			initPrintConfig();//初始化打印数据
			update.markUpdate(true, UpdateLevel.Data, "copyActset");
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");
			update.markUpdate(true, UpdateLevel.Data, "copyYear");
			update.markUpdate(true, UpdateLevel.Data, "copyPeriod");
			update.markUpdate(true, UpdateLevel.Data, "checkPanel");
			//this.refresh();
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
	
	@Bind
	public UIIFrame iframe;
	
	@Override
	public void grid_ondblclick() {
		try {
			super.grid_ondblclick();
			selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.getGridSelectId());
			String winId = "_fsin";
			String url = "";
			String srcType = selectedRowData.getSrctype();
			
			
			String vchSap = ConfigUtils.findSysCfgVal("fs_vch_sap");
			String vhcUrl = "indtl.xhtml";
			if("Y".equalsIgnoreCase(vchSap)){
				vhcUrl = "indtl_sap.xhtml";
				editWindow.setTitle("凭证明细SAP");
				editWindow.setWidth(1600);
			}
			
			if("S".equals(srcType) || "A".equals(srcType) || "T".equals(srcType) || "B".equals(srcType) || "J".equals(srcType)){
				if ("Y".equalsIgnoreCase(vchSap)) {
					url = "/pages/module/finance/doc/indtl_readonly_sap.xhtml?id=" + this.getGridSelectId();//不能编辑
				} else {
					url = "/pages/module/finance/doc/indtl_readonly.xhtml?id="+this.getGridSelectId();//不能编辑
				}
			}else{
				url = "./"+vhcUrl+"?id="+this.getGridSelectId();
			}
			if("Y".equalsIgnoreCase(ConfigUtils.findSysCfgVal("fs_vch_isamend_allowededit"))){
				url = "./"+vhcUrl+"?id="+this.getGridSelectId();
			}
			iframe.load(url);
			
			if("Y".equalsIgnoreCase(vchSap)){
				Browser.execClientScript("editWindowJsVar.setWidth(1350)");
			}
			
			
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Override
	public void grid_onrowselect() {
		selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.getGridSelectId());
		String vchnos = selectedRowData.getNos();
		if(StrUtils.isNumber(vchnos)){
			vchnoFilter = vchnos;
			update.markUpdate(true, UpdateLevel.Data, "vchnoFilter");
		}
		 
	}
	
	@Action
	public void add() {
		
		if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
			MessageUtils.alert("未选择帐套，请重新选择帐套！");
			return;
		}
		
		super.grid_ondblclick();
		String winId = "_fsin";
		String url ="";
		String vchSap = ConfigUtils.findSysCfgVal("fs_vch_sap");
		if (!"Y".equalsIgnoreCase(vchSap)) {
			url = "./indtl.xhtml";
		} else {
			url = "./indtl_sap.xhtml";
			Browser.execClientScript("editWindowJsVar.setWidth(1350)");
		}
		iframe.load(url);
	}
	
	@Bind
	@SaveState
	public String copyYear;
	
	@Bind
	@SaveState
	public String copyPeriod;
	

	@Bind
	@SaveState
	public Long copyActset;
	
	@Action
	public void copy() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0){
			MessageUtils.alert("Please choose one!");
			return;
		}
		
		if(StrUtils.isNull(copyYear)){
			MessageUtils.alert("Copy year is null!");
			return;
		}
		
		if(StrUtils.isNull(copyPeriod)){
			MessageUtils.alert("Copy month is null!");
			return;
		}
		
		if(ids.length>1){
			try {
				StringBuffer stringBuffer = new StringBuffer();
				for (String id : ids) {
					String args = "id="+id+";user="+AppUtils.getUserSession().getUsercode()+";actsetid="+copyActset+";year="+copyYear+";period="+copyPeriod;
					String sql = "SELECT f_fs_vch_addcopy('"+args+"') AS vchid";
					List list = this.serviceContext.vchMgrService.fsVchDao.executeQuery(sql);
					String vchNew = (String) list.get(0);
					stringBuffer.append(vchNew);
					stringBuffer.append("\n");
				}
				MessageUtils.showMsg(stringBuffer.toString());
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
			}
		}else{
			String args = "id="+ids[0]+";user="+AppUtils.getUserSession().getUsercode()+";actsetid="+copyActset+";year="+copyYear+";period="+copyPeriod;
			String sql = "SELECT f_fs_vch_addcopy('"+args+"') AS vchid";
			try {
				List list = this.serviceContext.vchMgrService.fsVchDao.executeQuery(sql);
				String vchNew = (String) list.get(0);
				MessageUtils.alert(vchNew);
				this.refresh();
				
				this.pkVal = Long.parseLong(vchNew.split("-")[0]);
//				this.data = getViewControl().findById(this.pkVal);
				doServiceFindData();
				this.refreshForm();
				if(editWindow != null)editWindow.show();
				update.markUpdate(true, UpdateLevel.Data, "editPanel");
				
				String url = "./indtl.aspx?id="+this.pkVal;
				iframe.load(url);
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
			}
		}
	}
	
	
	
	@Override
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0){
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			this.serviceContext.vchMgrService.removeDate(ids , AppUtils.getUserSession().getUsercode());
			this.refresh();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid();
		if(!StrUtils.isNull(qryYear)) {
			qry += "\nAND year = " + qryYear; // 年查询
		}
		if(!StrUtils.isNull(qryPeriodS) || !StrUtils.isNull(qryPeriodE)) {
			// 期间查询
			qry += "\nAND period BETWEEN " + (StrUtils.isNull(qryPeriodS)?0:qryPeriodS) 
				+ " AND " + (StrUtils.isNull(qryPeriodE)?12:qryPeriodE);
		}
		// 凭证日期查询
		if(vchDateS != null || vchDateE!= null) {
			qry += "\nAND singtime BETWEEN '" + (vchDateS==null ? "0001-01-01" : new SimpleDateFormat("yyyy-MM-dd").format(vchDateS))
				+ "' AND '" + (vchDateE==null ? "9999-12-31" : new SimpleDateFormat("yyyy-MM-dd").format(vchDateE)) + "'";
		}
		//凭证号查询
		qry += vchnoS != null && vchnoS >= 0 ? " \n AND (nos::INTEGER >= "+vchnoS+")" : "";
		qry += vchnoE != null && vchnoE >= 0 ? " \n AND (nos::INTEGER <= "+vchnoE+")" : "";
		
		if(!StrUtils.isNull(inputername)) {
			inputername = StrUtils.getSqlFormat(inputername);
			qry += "\nAND EXISTS(SELECT 1 FROM sys_user x where x.code = v.inputer AND x.namec = '"+inputername+"' AND x.isdelete = false)"; // 年查询
		}
		
		m.put("qry", qry);
		
		try {
			ConfigUtils.refreshUserCfg("pages.module.finance.doc.inBean.search", StrUtils.getSqlFormat(qry),AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return m;
	}

	@Override
	public void clearQryKey() {
		//qryYear = "";
		//qryPeriodS = "";
		//qryPeriodE = "";
		vchDateS = null;
		vchDateE = null;
		vchnoFilter = "";
		inputername = "";
		super.clearQryKey();
	}
	
	@Action
	public void checkVch() {
		String sql = "SELECT f_fs_checkvch('actsetid="+AppUtils.getUserSession().getActsetid()+";vchid="+this.getGridSelectId()+"') AS iseq;";
		Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String iseq = String.valueOf(m.get("iseq"));
		if("0".equals(iseq)) {
//			super.grid_ondblclick();
//			selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.getGridSelectId());
//			String winId = "_fsin";
//			String url = "";
//			//不能编辑
//			
//			if(selectedRowData.getSrctype().equals("S")||selectedRowData.getSrctype().equals("A")){
//				url = "/pages/module/finance/doc/indtl_readonly.xhtml?id="+this.getGridSelectId();
//				 
//			}else{
//				url = "./indtl.xhtml?id="+this.getGridSelectId();
//			}
//			
//			
//			iframe.load(url);
//			MsgUtil.alert("当前凭证借贷方总金额不相等，不能关闭!请核对！");
			selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.getGridSelectId());
			MessageUtils.alert("当前关闭的凭证借贷不平!请检查!年:"+selectedRowData.getYear()+"期间:"+selectedRowData.getPeriod()+"凭证号:"+selectedRowData.getNos());
		}
	}
	
	@Bind
	public UIWindow printBatch;
	
	@SaveState
	@Bind
	public int firstBatch = 1;
	
	@SaveState
	@Bind
	public int tailBatch = 1;
	
	@Bind
	public String searchtype = "number" ;
	
	@Action
	public void showprintBatch(){
		printBatch.show();
		update.markUpdate(true, UpdateLevel.Data, "printBatchPanel");
	}
	
	@Action
	public void printBatchingforJs(){
		String sql = "SELECT string_agg(id::TEXT,',') AS ids FROM fs_vch WHERE" +
		"\nisdelete = FALSE AND srctype != 'I' " ;
		sql += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid();
		if(searchtype.equals("number")){
			if(vchtypeid !=null){
				sql += " AND (vchtypeid = "+vchtypeid +" OR EXISTS(SELECT 1 FROM fs_vchtype  WHERE isdelete = FALSE AND name = '*' AND actsetid = "+AppUtils.getUserSession().getActsetid()+" AND id = "+vchtypeid+") )";
			}
			if(firstBatch != 0&&tailBatch != 0){
				sql += "\nAND nos::INTEGER BETWEEN "+firstBatch+" AND " +tailBatch + "\n " ;
			}
			sql += "\nAND year = " + qryYear + "\nAND period = " + qryPeriodS;
		}
		if(searchtype.equals("during")){
			sql += "\nAND year = " + qryYear + "\nAND period = " + qryPeriodS;
		}
		if(searchtype.equals("date")){
			sql += " AND singtime::DATE BETWEEN '" + (firstvchtype==null ? "0001-01-01" : new SimpleDateFormat("yyyy-MM-dd").format(firstvchtype))+
				"' AND '" + (tailvchtype==null ? "9999-12-31" : new SimpleDateFormat("yyyy-MM-dd").format(tailvchtype)) + "'";
		}
		try {
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map != null && map.size() > 0 && map.containsKey("ids")){
				if(map.get("ids") == null || map.get("ids").toString().isEmpty()){
					throw new NoRowException();
				}else{
					String jsonSql = "SELECT f_print_vch_loop('ids="+map.get("ids").toString()+";user="+AppUtils.getUserSession().getUsername()+";') AS json;";
					Map map2 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(jsonSql);
					if(map2 != null && map2.size() > 0 && map2.containsKey("json")){
						this.json = map2.get("json").toString();
						update.markUpdate(UpdateLevel.Data, "json");
						Browser.execClientScript("printVch();");
					}else{
						 throw new NoRowException();
					}
				}
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
	public void printBatching() {
		StringBuffer stringBuffer = new StringBuffer();
		String sql = "SELECT id FROM fs_vch WHERE" +
					"\nisdelete = FALSE AND srctype != 'I' " ;
		sql += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid();
		if(searchtype.equals("number")){
			if(vchtypeid !=null){
				sql += " AND (vchtypeid = "+vchtypeid +" OR EXISTS(SELECT 1 FROM fs_vchtype  WHERE isdelete = FALSE AND name = '*' AND actsetid = "+AppUtils.getUserSession().getActsetid()+" AND id = "+vchtypeid+") )";
			}
			if(firstBatch != 0&&tailBatch != 0){
				sql += "\nAND nos::INTEGER BETWEEN "+firstBatch+" AND " +tailBatch + "\n " ;
			}
		}
		if(searchtype.equals("during")){
			sql += "\nAND year = " + qryYear + "AND period = " + qryPeriodS;
		}
		if(searchtype.equals("date")){
			sql += " AND singtime BETWEEN '" + (firstvchtype==null ? "0001-01-01" : new SimpleDateFormat("yyyy-MM-dd").format(firstvchtype))+
					"' AND '" + (tailvchtype==null ? "9999-12-31" : new SimpleDateFormat("yyyy-MM-dd").format(tailvchtype)) + "'";
		}
		sql += " ORDER BY nos::INTEGER" ;
//		//System.out.println(sql);
//		String sql = "SELECT id FROM fs_vch WHERE" +
//					"\nisdelete = FALSE AND srctype != 'I' " +
//					"\nAND nos::INTEGER BETWEEN "+firstBatch+" AND " +tailBatch +
//						"\nAND year = " + qryYear + 
//						"\nAND period = " + qryPeriodS +
//						" AND actsetid = " + AppUtils.getUserSession().getActsetid()+
//						
//						" AND singtime BETWEEN '" + (firstvchtype==null ? "0001-01-01" : new SimpleDateFormat("yyyy-MM-dd").format(firstvchtype))+
//						"' AND '" + (tailvchtype==null ? "9999-12-31" : new SimpleDateFormat("yyyy-MM-dd").format(tailvchtype)) + "'";
		
		List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		if(list == null || list.size() <= 0){
			MessageUtils.alert("查不到凭证!");
			return;
		}
		
		StringBuffer printSql = new StringBuffer();
		for (Map map : list) {
			stringBuffer.append("{");
			if(isa4){
				stringBuffer.append("/v4.raq");
			}else{
				stringBuffer.append("/v.raq");
			}
			stringBuffer.append("(");
			String pk = StrUtils.getMapVal(map, "id");
			pk = pk.substring(0, pk.length()-3);
			stringBuffer.append("i="+pk);
			//stringBuffer.append(";actsetid="+AppUtils.getUserSession().getActsetid());
			stringBuffer.append(")");
			stringBuffer.append("}");
			
			printSql.append("\nUPDATE fs_vch set printcount = COALESCE(printcount,0) + 1 WHERE id = "+StrUtils.getMapVal(map, "id")+";");
		}
		
		//MessageUtils.alert("批量打印调用ActiveX控件，firefox浏览器请安装IETab插件，或使用IE内核浏览器(如QQ，360，搜狗等)");
		String args = stringBuffer.toString();
//		String ip = AppUtils.getHttpServletRequest().getLocalAddr();
//		int remotePort = AppUtils.getHttpServletRequest().getLocalPort();
		
		String js  = AppUtils.getRptUrl()+"/reportJsp/p.jsp?report="+args;
		
		if(js.length()>2083){
			MessageUtils.alert("当前打印数量太多了ie url长度限制2083,当前长度："+js.length()+" 减少一些再试试吧!");
			return;
		}
		
		AppUtils.openWindow(true,"_rptBatch", js , false);
		
		String pSql = printSql.toString();
		if(!StrUtils.isNull(pSql)){
			//System.out.println(pSql);
			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(pSql);
			this.grid.reload();
		}
	}
	
	

	
	@Action
	public void outputData() {
		String id = "-1";
		String[] ids = this.grid.getSelectedIds();
		if(ids != null && ids.length > 1){
			MessageUtils.alert("Please choose one!");
			return;
		}
		if(ids == null || ids.length == 0){
			id = "-1";
		}else{
			id = StrUtils.array2List(ids);
		}
		
		String url = "";
		url = AppUtils.getRptUrl()
				+ "/reportJsp/showReport.jsp?raq=/finance/fs_vch_output.raq"
				+ "&actsetid=" + AppUtils.getUserSession().getActsetid()
				+ "&year=" + qryYear
				+ "&periodfm=" + qryPeriodS
				+ "&periodto=" + qryPeriodE
				+ "&id=" + id;
		AppUtils.openWindow("vch_output", url);
	}
	
	@Bind
	public UIButton importData;
	
	@Bind
	@SaveState
	@Accessible
	public String importDataText;

	@Bind
	public UIWindow importDataWindow;

	@Action
	public void importData() {
		importDataText = "";
		importDataWindow.show();
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
	}

	@Action
	public void importDataBatch() {
		if (StrUtils.isNull(importDataText)) {
			Browser.execClientScript("window.alert('" + "Data is null" + "');");
			return;
		} else {
			try {
				String callFunction = "f_imp_fs_vch";
				String args = AppUtils.getUserSession().getActsetid() + ",'"
						+ AppUtils.getUserSession().getUsercode() + "'";
				this.serviceContext.commonDBService.addBatchFromExcelText(
						importDataText, callFunction, args);
				MessageUtils.alert("OK!");
				refresh();
			} catch (Exception e) {
				MessageUtils.showException(e);
			}
		}
	}
	@Action
	protected void startImport() {
		this.update.markUpdate(UpdateLevel.Data, "importDataText");
		importDataBatch();
	}

	/*@Override
	public void processUpload(FileUploadItem fileUploadItem) throws IOException {
		super.processUpload(fileUploadItem);
		importDataText = analyzeExcelData(1, 1);
	}*/
//	@Action
//	public void doChangeGridPageSize() {
//		//System.out.println("+++++++++++++++");
//	}
	
	@Action
	public void focusRowAction() {
		if(!StrUtils.isNull(vchnoFilter)){
			//Vector<Integer> vector = new Vector<Integer>();
			//String sqlId = "pages.module.finance.doc.inBean.grid.page";
			//Object[] obj = daoIbatisTemplate.getSqlMapClientTemplate()
			//		.queryForList(sqlId, getQryClauseWhere(this.qryMap), 0,
			//				this.gridPageSize).toArray();
			
			//for (int i = 0; i < obj.length; i++) {
			//	HashMap<String,Object> map = (HashMap<String,Object>)obj[i];
//				//System.out.println(map);
			//	String nos = (String)map.get("nos");
			//	if(!StrUtils.isNull(nos) && (nos.equalsIgnoreCase(vchnoFilter))){
			//		vector.add(i);
			//	}
			//}
			//int[] selections = new int[vector.size()];
			//for (int i = 0; i < vector.size(); i++) {
			//	selections[i] = vector.get(i);
			//	
			//}
			//if(vector.size()>0){
				//Browser.execClientScript("gridJsvar.view.focusRow("+selections[vector.size()-1]+")");
			//	Browser.execClientScript("scrollToRow(gridJsvar,"+selections[vector.size()-1]+")");
			//	grid.setSelections(selections);
			//}else{
				this.qryMap.put("nos", vchnoFilter);
				this.grid.reload();
			//}
		}else{
			this.qryMap.put("nos", "");
			this.grid.reload();
		}
	}
	
	@Bind
	public String vchnoFilter;

	@Override
	public void refresh() {
		this.gridLazyLoad = false;
		this.grid.reload();
	}
	
	@Action
	public void showCheckWindow(){
		if(checkWindow!=null){
			checkWindow.show();
		}
	}
	
	@Action
	public void checkNow(){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM f_fs_check_vchdata('actsetid=");
		sb.append(AppUtils.getUserSession().getActsetid());
		sb.append(";checkYear=");
		sb.append(checkYear);
		sb.append(";checkPeriod=");
		sb.append(checkPeriod);
		sb.append(";checkVchType=");
		sb.append(checkVchType);
		sb.append(";checkVchnoS=");
		sb.append(checkVchnoS);
		sb.append(";checkVchnoE=");
		sb.append(checkVchnoE);
		
		
		sb.append(";checkVchNo=");
		sb.append(checkVchNo);
		sb.append(";checkSameNo=");
		sb.append(checkSameNo);
		sb.append(";checkVchDC=");
		sb.append(checkVchDC);
		sb.append(";checkVchRate=");
		sb.append(checkVchRate);
		sb.append(";other=");
		sb.append(other);
		sb.append(";') AS result");
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sb.toString());
			
			//System.out.println(sb.toString());
			if(m!=null && m.containsKey("result")){
				checkResult = m.get("result").toString();
				update.markUpdate(true, UpdateLevel.Data, "checkPanel");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void closecheckWindow(){
		if(checkWindow!=null){
			checkWindow.close();
		}
	}
}
