package com.scp.view.module.lclload;

import java.math.BigDecimal;
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
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.model.wms.WmsIn;
import com.scp.model.wms.WmsIndtl;
import com.scp.service.wms.WmsInMgrService;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.CommonUtil;
import com.scp.util.MessageUtils;
import com.scp.util.SaasUtil;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridView;

@ManagedBean(name = "pages.module.lclload.trainBean", scope = ManagedBeanScope.REQUEST)
public class TrainBean extends EditGridView {
	
	public Long userid;
	
	@Bind
	@SaveState
	public String code;
	
	@Bind
	@SaveState
	public String qryValues="";//用于存所以的查询值
	
	/**
	 * 动态生成勾选的线路
	 */
	@Bind
	@SaveState
	private String linesQry;
	
	@Bind
	@SaveState
	public String linesQryChoose;
	@Bind
	@SaveState
	private Integer customvotes;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		this.userid = AppUtils.getUserSession().getUserid();
		if (!isPostBack) {
			super.applyGridUserDef();
			String querySql = "SELECT string_agg(namec,'@') AS lines FROM dat_line where lintype = 'T' AND isdelete = FALSE;";
			Map map = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			linesQry = StrUtils.getMapVal(map, "lines");
		}
	}
	
	
	@ManagedProperty("#{wmsInMgrService}")
	public WmsInMgrService wmsInMgrService;
	

	@Action
	public void add() {
		String winId = "_edit_wms_in";
		String url = "../stock/wmsinedit.xhtml";
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	@Action
	public void linkEdit(){
		String pkid = AppUtils.getReqParam("pkid");
		String type = AppUtils.getReqParam("type");
		if(StrUtils.isNull(pkid)){
			this.alert("无效pkid");
			return;
		}
		selectedRowData = serviceContext.wmsInMgrService().wmsInDao.findById(Long.parseLong(pkid));
		
		wmsIndtl = serviceContext.wmsIndtlMgrService().findByInid(selectedRowData.getId());
		
		if("remark".equals(type)){
			remark2 = selectedRowData.getRemark();
			update.markUpdate(true, UpdateLevel.Data, "remark2");
			remarkWindow.show();
		}
		if("loadgoodsrequire".equals(type)){
			loadgoodsrequire2 = selectedRowData.getLoadgoodsrequire();
			update.markUpdate(true, UpdateLevel.Data, "loadgoodsrequire2");
			loadgoodsrequireWindow.show();
		}
		if("remarkunusual".equals(type)){
			remarkunusual2 = selectedRowData.getRemarkunusual();
			update.markUpdate(true, UpdateLevel.Data, "remarkunusual2");
			remarkunusualWindow.show();
		}
		
		
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_wms_in";
		String url = "../stock/wmsincheckedit.xhtml?id="+this.getGridSelectId();
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	@Action
	public void checkBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			wmsInMgrService.updateCheck(ids);
			MessageUtils.alert("审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}
	
	@Action
	public void returnBtn() {
		String[] ids = this.editGrid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			wmsInMgrService.updateReturn(ids);
			MessageUtils.alert("退仓成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}

	@Action
	public void cancelCheckBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		
		try {
			wmsInMgrService.updateCancelCheck(ids);
			MessageUtils.alert("取消审核成功");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMapExp = new HashMap<String, Object>();
	
	@Bind(id = "gridExp")
	public UIDataGrid gridExp;
	
	@Bind(id = "gridExp", attribute = "dataProvider")
	protected GridDataProvider getUserGridDataProvider() {
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridExp.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere2(qryMapExp), start,
								limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridExp.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere2(qryMapExp));
				if (list == null || list.size() < 1)
					return 0;
				Long count = Long.parseLong(list.get(0).get("counts")
						.toString());
				return count.intValue();
			}
		};
	}
	
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = "\n1=1";
		String qry1 = "\n1=1";
		/*if(!StrUtils.isNull(code)){
			qry1 += "\nAND R.code LIKE '%"+code+"%' OR R.namec LIKE '%"+code+"%' OR R.namee LIKE '%"+code+"%' OR R.abbr LIKE '%"+code+"%'";
		}*/
		
		/*String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(m.containsKey("ordersql")){
			m.remove("ordersql");
		}
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql;
			m.put("ordersql", ordersql);
		}*/
		
		if(!qryValues.equals("")){
			String sql = "";
			String[] split = qryValues.split(",");
			for(String s:split){
				if(sql.equals("")){
					sql+="\nAND ((code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR abbr ILIKE '%"+StrUtils.getSqlFormat(s)+"%')";
				}else{
					sql+=" OR(code ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namec ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR namee ILIKE '%"+StrUtils.getSqlFormat(s)+"%' OR abbr ILIKE '%"+StrUtils.getSqlFormat(s)+"%')";
				}
			}
			sql+=")";
			m.put("filter2",sql);
		}
		m.put("qry1", qry);
		m.put("qry",qry);
		String filter = SaasUtil.filterByCorpid("t");
		m.put("filter", filter);
		return m;
	}
	
	@Action
	public void qrysAction(){
		if(code!=null&&!code.equals("")){
			qryValues += code+",";
		}
		update.markUpdate(UpdateLevel.Data, "qryValues");
		this.gridExp.reload();
	}
	
	@Action
	public void refreshForCustomer() {
		
		if (gridExp != null) {
			if(code!=null&&!code.equals("")){
				qryValues = code+",";
			}
			this.gridExp.reload();
		}
		
	}
	
	@SaveState
	String dynamicClauseWhere  ="";
	
	@Bind
	@SaveState
	private boolean isdate=false;
	
	@Bind
	@SaveState
	private String dateastart;
	
	@Bind
	@SaveState
	private String dateend;
	
	@Bind
	@SaveState
	private String dates;
	
	@Bind
	@SaveState
	private String numbers;
	
	@Bind
	@SaveState
	private boolean iscntid;
	
	@Bind
	@SaveState
	private boolean isagentcnt;
	
	@Bind
	@SaveState
	private boolean istrainno;
	
	@Bind
	@SaveState
	private boolean isagentname;
	
	@Bind
	@SaveState
	private boolean isfactory;
	
	@Bind
	@SaveState
	private boolean iscntno;
	
	@Bind
	@SaveState
	private long customerid=0;
	
	@Bind
	@SaveState
	private long saleid=0;
	
	@Bind
	@SaveState
	private String warehouseidqry;
	
	@Bind
	@SaveState
	private String polwarehouseid;
	
	@Bind
	@SaveState
	private Long polwarehouseidedit;
	
	@Bind
	@SaveState
	private String polqry;
	
	@Bind
	@SaveState
	private String pddqry;
	
	@Bind
	@SaveState
	private String podqry;
	
	@Bind
	@SaveState
	private String destinationqry;
	
	@Bind
	@SaveState
	private String poaqry;
	
	
	@Bind
	@SaveState
	public String warehouseid;
	
	@Bind
	@SaveState
	private String year;
	
	@SaveState
	public String[] ids;
	//SLC add
	@Bind 
	@SaveState
	private String gdsintype; //进仓模式
	
	@Bind
	@SaveState 
	private String customtype; //报关方式
	
	@Bind 
	@SaveState
	private String custominfo;//报关资料
	
	@Bind 
	@SaveState
	private String export;//出口海岸
	
	@Bind
	@SaveState
	private String channelid;//渠道
	
	@Bind
	@SaveState 
	private String qryinputer;//录入人
	
	@Bind
	@SaveState 
	private String qrysubmiter;//提交人
	
	@Bind
	@SaveState
	private String qrycheckter;//审核人
	
	
	@Bind
	@SaveState
	private String  linesqry; //线路
	
	@Bind
	@SaveState
	private String refnoqy;//参考单号
	
	

	@Bind
	@SaveState
	private String customernoqy;//客户单号
	

	@Bind
	@SaveState
	private String remarkunusualqy;//异常情况

	@Bind
	@SaveState
	private String goodsnamecsqy;//中文品名
	@Bind
	@SaveState
	private String goodsnameesqy;//英文品名
	

	@Bind
	@SaveState
	private String marknosqy;//唛头
	

	@Bind
	@SaveState
	private String remarkqy;//货物备注
	
	@Bind
	@SaveState
	private String loadgoodsrequireqy;//装货要求
	
	@Bind
	@SaveState
	private String driverinfoqy;//司机信息


	@Bind
	@SaveState
	private String otherfilesqy;//其他文件

	@Bind
	@SaveState
	private String consigneeqy;//收货人
	
	
	//SLCadd
	@Bind(id="qryLine")
    public List<SelectItem> getqryLine() {
    	try {
    		return CommonComBoxBean.getComboxItems("code","namec","dat_line AS d"
    				,"WHERE isdelete = false ","ORDER BY convert_to(code,'GBK')");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		//qry += "\nAND warehouseid = " + AppUtils.getUserSession().getWarehouseid();
		
		//qry += "\nAND  warehouseid IN ( SELECT linkid FROM sys_user_assign WHERE userid = " + AppUtils.getUserSession().getUserid() + " AND linktype='W' )";
		if(warehouseid!=null&&!StrUtils.isNull(warehouseid.toString())){
			qry += "AND (warehouseid = "+warehouseid+" OR cntno LIKE '%"+warehouseid+"%')";
		}
		//qry += "\nAND EXISTS(SELECT 1 FROM _wms_stock WHERE inid = t.id AND piece > 0)";//有库存
		
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(m.containsKey("ordersql")){
			m.remove("ordersql");
		}
		if(!StrUtils.isNull(ordersql)){
			ordersql = "ORDER BY " + ordersql + "";
		}else{
			ordersql = "ORDER BY id DESC";
		}
		m.put("ordersql", ordersql);
		
		//初始化
		dynamicClauseWhere = "\n AND 1=1 ";
		//高级检索中日期区间查询拼接语句
		if(isdate){	
				dynamicClauseWhere  += "\nAND "+dates+"::DATE BETWEEN '"
					+ (StrUtils.isNull(dateastart) ? "0001-01-01" : dateastart)
					+ "' AND '"
					+ (StrUtils.isNull(dateend) ? "9999-12-31" : dateend)
					+ "'";
			
		}
		
		
		//高级检索中单号查询拼接
		if(iscntid||isagentcnt||istrainno||isagentname||isfactory||iscntno){
			dynamicClauseWhere += "\nAND (FALSE ";
			if(iscntid){
				dynamicClauseWhere +="\nOR t.cntid ILIKE '%"+numbers+"%'";			
			}
			if(isagentcnt){
				dynamicClauseWhere +="\nOR t.agentcnt ILIKE '%"+numbers+"%'";	
			}
			if(istrainno){
				dynamicClauseWhere +="\nOR t.trainno ILIKE '%"+numbers+"%' ";	
			}
			if(isagentname){
				dynamicClauseWhere +="\nOR t.agentname ILIKE '%"+numbers+"%' ";	
			}
			if(isfactory){
				dynamicClauseWhere +="\nOR t.factory ILIKE '%"+numbers+"%' ";	
			}
			if(iscntno){
				dynamicClauseWhere +="\nOR t.cntno ILIKE '%"+numbers+"%' ";	
			}
			
			dynamicClauseWhere += ")";
		}
		
		//客户
		if(customerid!=0){
			dynamicClauseWhere +="\nAND t.customerid ='"+customerid+"'";
		}
		//业务员
		if(saleid!=0){
			dynamicClauseWhere +="\nAND t.saleid = '"+saleid+"'";
		}
		
		//收货地仓库
		if(!StrUtils.isNull(warehouseidqry)){
			dynamicClauseWhere +="\nAND t.warehouseid ='"+Long.parseLong(warehouseidqry)+"'";
		}
		//装箱地仓库
		if(!StrUtils.isNull(polwarehouseid)){
			dynamicClauseWhere +="\nAND t.polwarehouseid = '"+Long.parseLong(polwarehouseid)+"'";
		}
		
		if(!StrUtils.isNull(poaqry)){
			dynamicClauseWhere +="\nOR t.poa ILIKE '%"+poaqry+"%' ";	
		}
		
		if(!StrUtils.isNull(polqry)){
			dynamicClauseWhere +="\nOR t.pol ILIKE '%"+polqry+"%' ";	
		}
		if(!StrUtils.isNull(pddqry)){
			dynamicClauseWhere +="\nOR t.pdd ILIKE '%"+pddqry+"%' ";	
		}
		if(!StrUtils.isNull(podqry)){
			dynamicClauseWhere +="\nOR t.pod ILIKE '%"+podqry+"%' ";	
		}
		if(!StrUtils.isNull(destinationqry)){
			dynamicClauseWhere +="\nOR t.destination ILIKE '%"+destinationqry+"%' ";	
		}
		if(!StrUtils.isNull(week) && !StrUtils.isNull(year)){
			String str = CommonUtil.getWeekDays(year,Integer.parseInt(week));
			qry += "\n AND t.traindate BETWEEN '"+str.substring(0, str.indexOf("~"))+"' AND '"+str.substring(str.indexOf("~")+1).trim()+"'";
		}
		
		if(ids != null && ids.length != 0){
			dynamicClauseWhere +="\nAND t.customerid IN ("+StrUtils.array2List(ids)+")";
		}
		//进仓模式
		if(!StrUtils.isNull(gdsintype)){
			dynamicClauseWhere +="\nAND t.gdsintype ILIKE '%"+ destinationqry+"%'";
		}
		//报关方式
		if(!StrUtils.isNull(customtype)){
			dynamicClauseWhere +="\nAND t.customtype ILIKE '%"+customtype+"%'";
		}
		//报关资料
	
		if(!StrUtils.isNull(custominfo)){
			dynamicClauseWhere +="\nAND t.custominfo ILIKE  '%"+custominfo+"%'";
		}
		//出口海岸
		if(!StrUtils.isNull(export)){
			dynamicClauseWhere +="\nAND t.export ILIKE '%"+export+"%'";
		}
	    //渠道
		
		if(!StrUtils.isNull(channelid)){
			dynamicClauseWhere +="\nAND t.id=ANY(select channelid from dat_channel where isdelete = false AND channel = '"+Long.parseLong(channelid)+"'";
		}
		//录入人
		if(!StrUtils.isNull(qryinputer)){
			dynamicClauseWhere +="\nAND t.inputer ILIKE '%"+qryinputer+"%'";
		}
		//提交人
		if(!StrUtils.isNull(qrysubmiter)){
			dynamicClauseWhere +="\nAND t.submiter ILIKE '%"+qrysubmiter+"%'";
		}
		//审核人
		if(!StrUtils.isNull(qrycheckter)){
			dynamicClauseWhere +="\nAND t.checkter ILIKE '%"+qrycheckter+"%'";
		}
		//参考号
		if(!StrUtils.isNull(refnoqy)){
			dynamicClauseWhere +="\nAND t.refno ILIKE '%"+refnoqy+"%'";
		}
		//客户单号
		if(!StrUtils.isNull(customernoqy)){
			dynamicClauseWhere +="\nAND t.customerno ILIKE '%"+customernoqy+"%'";
		}
		//异常情况
		if(!StrUtils.isNull(remarkunusualqy)){
			dynamicClauseWhere +="\nAND t.remarkunusual ILIKE '%"+remarkunusualqy+"%'";
		}
		//中文品名
		if(!StrUtils.isNull(goodsnamecsqy)){
			dynamicClauseWhere +="\nAND t.id=ANY(select inid from wms_indtl where isdelete = false AND goodsnamec ILIKE '%"+goodsnamecsqy+"%')";
		}
		//英文品名
		if(!StrUtils.isNull(goodsnameesqy)){
			dynamicClauseWhere +="\nAND t.id=ANY(select inid from wms_indtl where  isdelete = false AND goodsnamee ILIKE '%"+goodsnameesqy+"%')";
		}
		//备注
		if(!StrUtils.isNull(remarkqy)){
			dynamicClauseWhere +="\nAND t.remark ILIKE '%"+remarkqy+"%'";
		}
		//装货要求
		if(!StrUtils.isNull(loadgoodsrequireqy)){
			dynamicClauseWhere +="\nAND t.loadgoodsrequire ILIKE '%"+loadgoodsrequireqy+"%'";
		}
		//司机信息
		if(!StrUtils.isNull(driverinfoqy)){
			dynamicClauseWhere +="\nAND t.driverinfo ILIKE '%"+driverinfoqy+"%'";
		}
		//其他文件
		if(!StrUtils.isNull(otherfilesqy)){
			dynamicClauseWhere +="\nAND t.otherfiles ILIKE '%"+otherfilesqy+"%'";
		}
		//收货人
		if(!StrUtils.isNull(consigneeqy)){
			dynamicClauseWhere +="\nAND t.consignee ILIKE '%"+consigneeqy+"%'";
		}
		//唛头
		if(!StrUtils.isNull(marknosqy)){
			dynamicClauseWhere +="\nAND t.id=ANY(select inid from wms_indtl where isdelete = false AND markno ILIKE '%"+marknosqy+"%')";
		}
		if(!StrUtils.isNull(linesQryChoose)){
			dynamicClauseWhere +="\nAND t.lines = ANY(SELECT code FROM dat_line WHERE namec = ANY(regexp_split_to_array('"+linesQryChoose+"','@')))";	
		}
		//路线
		if(!StrUtils.isNull(linesqry)){
			dynamicClauseWhere +="\nAND t.lines ='"+linesqry+"' " ;
		}

		
		dynamicClauseWhere += "\nAND NOT EXISTS(SELECT 1 FROM wms_outdtlref x , wms_indtl y , wms_outdtl z WHERE y.inid = t.id AND x.indtlid = y.id AND z.id = x.outdtlid AND y.isdelete = FALSE AND z.isdelete = FALSE)";
		
		m.put("dynamicClauseWhere", dynamicClauseWhere);
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	@SaveState
	private String week;
	
	@Bind
	@SaveState
	private Long cntypeid;
	
	@Bind
	@SaveState
	private Date loadtime;
	
	@Bind
	@SaveState
	private String cntno;
	
	@Bind
	@SaveState
	private String remark;
	
	@Bind
	public UIWindow infoWindow;
	
	@Bind
	public UIWindow remarkWindow;
	
	@Bind
	public UIWindow loadgoodsrequireWindow;
	
	@Bind
	public UIWindow remarkunusualWindow;
	
	@Bind
	@SaveState
	private java.util.Date intimeedit;
	
	@Bind
	@SaveState
	private java.util.Date nationaldateedit;
	
	@Bind
	@SaveState
	private java.util.Date traindateedit;
	
	@Bind
	@SaveState
	private String remarkedit;
	
	@Bind
	@SaveState
	private String remark2;
	
	@Bind
	@SaveState
	private String loadgoodsrequire2;
	
	@Bind
	@SaveState
	private String remarkunusual2;
	
	
	@Bind
	@SaveState
	private String remarkunusual;
	
	@Bind
	@SaveState
	private Integer pieceedit;
	
	@Bind
	@SaveState
	private BigDecimal wgttotleedit;
	
	@Bind
	@SaveState
	private BigDecimal sovoledit;
	
	@Bind
	@SaveState
	private BigDecimal cbmtotleedit;
	
	@Bind
	@SaveState
	private String packageedit;
	
	@Bind
	@SaveState
	private String custominfoedit;
	
	@Action
	public void btnSave() {
		String[] ids = this.editGrid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			this.serviceContext.wmsInMgrService().batchupUpdateForTrain(ids, intimeedit, nationaldateedit, remarkedit, remarkunusual,  sovoledit,custominfoedit,traindateedit, AppUtils.getUserSession().getUsercode(),customvotes);
			this.serviceContext.wmsIndtlMgrService().batchUpdateForTrainInfo(ids, pieceedit, wgttotleedit, packageedit, cbmtotleedit, AppUtils.getUserSession().getUsercode());
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	@SaveState
	@Accessible
	public WmsIn selectedRowData = new WmsIn();
	
	@SaveState
	@Accessible
	public WmsIndtl wmsIndtl  = new WmsIndtl();
	
	@Action
	public void showRemark(){
		confirminfo();
	}
	
	@Action
	public void confirminfo(){
		String[] ids = this.editGrid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			if(ids.length == 1){
				selectedRowData = serviceContext.wmsInMgrService().wmsInDao.findById(Long.parseLong(ids[0]));
				
				wmsIndtl = serviceContext.wmsIndtlMgrService().findByInid(selectedRowData.getId());
				remarkedit = selectedRowData.getRemark();
				remarkunusual = selectedRowData.getRemarkunusual();
				custominfoedit = selectedRowData.getCustominfo();
				customvotes=selectedRowData.getCustomvotes();
				if(selectedRowData.getIntime() != null){
					intimeedit = selectedRowData.getIntime();
				}
				if(selectedRowData.getNationaldate() != null){
					nationaldateedit = selectedRowData.getNationaldate();
				}
				
				if(selectedRowData.getTraindate() != null){
					traindateedit = selectedRowData.getTraindate();
				}
				if(wmsIndtl.getCbmtotle() != null){
					cbmtotleedit = wmsIndtl.getCbmtotle();
				}
				
				if(selectedRowData.getPolwarehouseid() != null){
					polwarehouseidedit = selectedRowData.getPolwarehouseid();
				}
				
				pieceedit = wmsIndtl.getPiece();
				wgttotleedit = wmsIndtl.getWgttotle();
				packageedit = wmsIndtl.getPackagee();
				sovoledit = selectedRowData.getSovol();
				
				update.markUpdate(true, UpdateLevel.Data, "polwarehouseidedit");
				update.markUpdate(true, UpdateLevel.Data, "intimeedit");
				update.markUpdate(true, UpdateLevel.Data, "nationaldateedit");
				update.markUpdate(true, UpdateLevel.Data, "traindateedit");
				update.markUpdate(true, UpdateLevel.Data, "cbmtotleedit");
				update.markUpdate(true, UpdateLevel.Data, "remarkedit");
				update.markUpdate(true, UpdateLevel.Data, "remarkunusual");
				update.markUpdate(true, UpdateLevel.Data, "custominfoedit");
				update.markUpdate(true, UpdateLevel.Data, "pieceedit");
				update.markUpdate(true, UpdateLevel.Data, "wgttotleedit");
				update.markUpdate(true, UpdateLevel.Data, "packageedit");
				update.markUpdate(true, UpdateLevel.Data, "sovoledit");
				update.markUpdate(true, UpdateLevel.Data, "customvotes");
				infoWindow.show();
			}else{
				infoWindow.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	
	@Action
	public void createJobs(){
		try{
			String[] ids = this.editGrid.getSelectedIds();
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要生成的行");
				return;
			}
			String sql = "SELECT f_wms_createjobs('wmsid="+StrUtils.array2List(ids)
				+";usercode="+AppUtils.getUserSession().getUsercode()+";cntypeid="+cntypeid+";loadtime="+loadtime+";cntno="+cntno+";remark="+remark+"')";
			
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			MessageUtils.alert("OK");
			Browser.execClientScript("createJobsSwindowJs.hide()");
			this.refresh();
		}catch(Exception e){
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
	@Bind
	@SaveState
	@Accessible
	public String showwmsinfilename;
	
	@Action
	public void preview() {
		try {
			String[] ids = this.editGrid.getSelectedIds();
			if(ids == null || ids.length == 0) {
				MessageUtils.alert("请先勾选要预览的行");
				return;
			}
			String args = "&ids=" + StrUtils.array2List(ids);
			if (showwmsinfilename == null || "".equals(showwmsinfilename)) {
				MessageUtils.alert("请选择格式！");
				return;
			}
			String rpturl = AppUtils.getRptUrl();
			String openUrl;
			if("wmsin_preview_list.raq".equals(showwmsinfilename)){
				openUrl = rpturl+ "/reportJsp/showReport.jsp?raq=/static/stock/"+ showwmsinfilename;
			}else{
				openUrl = rpturl+ "/reportJsp/showReport.jsp?raq=/static/train/"+ showwmsinfilename;
			}
			
			AppUtils.openWindow("_shipbillReport", openUrl + args
					+ "&userid="
					+ AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bind(id = "reportinformat")
	public List<SelectItem> getReportinformat() {
		try {
			return CommonComBoxBean.getComboxItems("d.filename", "d.namec",
					"sys_report AS d", " WHERE modcode = 'train' AND isdelete = FALSE",
					"order by namec");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Override
	protected void update(Object modifiedData) {
		try {
			serviceContext.wmsInMgrService().updateBatchEditGrid(modifiedData);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void searchfee() {
		ids = this.gridExp.getSelectedIds();
		this.qryRefresh();
	}
	
	@Action
	public void clear() {
		this.clearQryKey();
		this.clearQryCode();
	}
	
	@Action
	public void clearQryCode(){
		this.qryValues = "";
		this.code = "";
		this.gridExp.reload();
	}
	
	@Override
	public void clearQryKey() {
		isdate=false;
		dates="";
		dateastart="";
		dateend="";
		iscntid=false;
		isagentcnt=false;
		istrainno=false;
		isagentname=false;
		isfactory=false;
		iscntno=false;
		numbers="";
		customerid=0;
		saleid=0;
		warehouseid="";
		polwarehouseid="";
		poaqry="";
		polqry="";
		pddqry="";
		podqry="";
		destinationqry="";
		Browser.execClientScript("$('#pol_input').val('')");
		Browser.execClientScript("$('#pdd_input').val('')");
		Browser.execClientScript("$('#pod_input').val('')");
		Browser.execClientScript("$('#destination_input').val('')");
		Browser.execClientScript("$('#sale_input').val('')");
		Browser.execClientScript("$('#customer_input').val('')");
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
		}
	}
	
	@Bind
	@SaveState
	public String sortlabel_1;
	
	@Bind
	@SaveState
	public String sortvalue_1;
	
	@Bind
	@SaveState
	public String sortlabel_2;
	
	@Bind
	@SaveState
	public String sortvalue_2;
	
	@Bind
	@SaveState
	public String sortlabel_3;
	
	@Bind
	@SaveState
	public String sortvalue_3;
	
	@Bind
	public UIWindow sortWindow;
	
	@Action
	public void showSortWindow(){
		sortlabel_1 = null;
		sortvalue_1 = "asc";
		sortlabel_2 = null;
		sortvalue_2 = "asc";
		sortlabel_3 = null;
		sortvalue_3 = "asc";
		String ordersql = AppUtils.getUserColorder(getMBeanName()+".grid");
		if(!StrUtils.isNull(ordersql)){
			String[] sorts = ordersql.split(",");
			if(sorts != null){
				for (int i = 0; i < sorts.length; i++) {
						switch (i+1) {
						case 1:
							sortlabel_1 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_1 = "desc";
							}else{
								sortvalue_1 = "asc";
							}
							break;
						case 2:
							sortlabel_2 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_2 = "desc";
							}else{
								sortvalue_2 = "asc";
							}
							break;
						case 3:
							sortlabel_3 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_3 = "desc";
							}else{
								sortvalue_3 = "asc";
							}
							break;
						}
				}
			}
		}
		update.markUpdate(true,UpdateLevel.Data, "sortpanel");
		sortWindow.show();
	}
	@Action
	public void resetUserColorder(){
		sortlabel_1 = null;
		sortvalue_1 = "asc";
		sortlabel_2 = null;
		sortvalue_2 = "asc";
		sortlabel_3 = null;
		sortvalue_3 = "asc";
		update.markUpdate(true,UpdateLevel.Data, "sortpanel");
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", null);
		MessageUtils.alert("OK!");
	}
	@Action
	public void saveUserColorder(){
		String colorder = "";
		if(!StrUtils.isNull(sortlabel_1)&&!StrUtils.isNull(sortvalue_1)){
			colorder += sortlabel_1 + " " + sortvalue_1 + ",";
		}
		if(!StrUtils.isNull(sortlabel_2)&&!StrUtils.isNull(sortvalue_2)){
			colorder += sortlabel_2 + " " + sortvalue_2 + ",";
		}
		if(!StrUtils.isNull(sortlabel_3)&&!StrUtils.isNull(sortvalue_3)){
			colorder += sortlabel_3 + " " + sortvalue_3 + ",";
		}
		
		colorder = colorder.endsWith(",") ? colorder.substring(0, colorder.length()-1) : colorder;
		
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", colorder);
		MessageUtils.alert("OK!");
	}
	
}
