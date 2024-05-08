package com.scp.view.module.stock;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.service.wms.WmsInMgrService;
import com.scp.util.AppUtils;
import com.scp.util.ApplicationUtilBase;
import com.scp.util.CommonUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.stock.warehouseinBean", scope = ManagedBeanScope.REQUEST)
public class WarehouseInBean extends GridView {
	
	public Long userid;
	
	@Bind
	@SaveState
	public boolean isubmit;
	
	@Bind
	@SaveState
	public boolean ischeck;
	
	@Bind
	@SaveState
	public boolean nosubmit;
	
	@Bind
	@SaveState
	public boolean nocheck;
	
	/*
	 * 已拼柜
	 */
	@Bind
	@SaveState
	public boolean isunion = false;
	
	/*
	 * 未拼柜
	 */
	@Bind
	@SaveState
	public boolean nounion;
	
	@Bind
	@SaveState
	public boolean isreturn;
	
	@Bind
	@SaveState
	private String week;
	
	
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
	public void isunionChoose_oncheck() {
		
		isunion = !isunion;
		this.refresh();
	}
	
	@Action
	public void nounionChoose_oncheck() {
		nounion =!nounion;
		this.refresh();
	}
	
	@Action
	public void isreturnChoose_oncheck() {
		isreturn = !isreturn;
		this.refresh();
	}
	
	@Action
	public void add() {
		String winId = "_edit_wms_in";
		String url = "./wmsinedit.xhtml";
		ApplicationUtilBase.openWindow(winId, url);
	}
	
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_edit_wms_in";
		String url = "./wmsinedit.xhtml?id="+this.getGridSelectId();
		ApplicationUtilBase.openWindow(winId, url);
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
	private String warehouseid;
	
	
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
	@Bind
	@SaveState
	private String polwarehouseid;
	
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
	private String year;
	

	/**
	 * 动态生成勾选的线路
	 */
	@Bind
	@SaveState
	private String linesQry;
	
	@Bind
	@SaveState
	public String linesQryChoose;


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
		
		//初始化
		dynamicClauseWhere = "\n AND 1=1 ";
		
		if(isubmit){
			qry += "\n AND t.isubmit = true";
		}
		if(ischeck){
			qry += "\n AND t.ischeck = true";
		}
		if(nosubmit){
			qry += "\n AND t.isubmit = false";
		}
		if(nocheck){
			qry += "\n AND t.ischeck = false";
		}
		
		if(isunion){
			qry += "\n AND EXISTS(SELECT 1 FROM  del_loadtl x , wms_outdtlref y , wms_indtl z , wms_in zz WHERE x.outdtlid = y.outdtlid AND y.indtlid = z.id AND zz.id = t.id AND zz.id = z.inid)";
		}
		if(nounion){
			qry += "\n AND NOT EXISTS(SELECT 1 FROM  del_loadtl x , wms_outdtlref y , wms_indtl z , wms_in zz WHERE x.outdtlid = y.outdtlid AND y.indtlid = z.id AND zz.id = t.id AND zz.id = z.inid)";
		}
		if(isreturn){
			qry += "\n AND t.isreturn = true";
		}
		
		
		if(!StrUtils.isNull(week) && !StrUtils.isNull(year)){
			String str = CommonUtil.getWeekDays(year,Integer.parseInt(week));
			qry += "\n AND t.traindate BETWEEN '"+str.substring(0, str.indexOf("~"))+"' AND '"+str.substring(str.indexOf("~")+1).trim()+"'";
		}
		
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
		if(!StrUtils.isNull(warehouseid)){
			dynamicClauseWhere +="\nAND t.warehouseid ='"+Long.parseLong(warehouseid)+"'";
		}
		//装箱地仓库
		if(!StrUtils.isNull(polwarehouseid)){
			dynamicClauseWhere +="\nAND t.polwarehouseid = '"+Long.parseLong(polwarehouseid)+"'";
		}
		
		//路线
		if(!StrUtils.isNull(linesqry)){
			dynamicClauseWhere +="\nAND t.lines ='"+linesqry+"' " ;
		}
		if(!StrUtils.isNull(poaqry)){
			dynamicClauseWhere +="\nAND t.poa ILIKE '%"+poaqry+"%' ";	
		}
		
		if(!StrUtils.isNull(polqry)){
			dynamicClauseWhere +="\nAND t.pol ILIKE '%"+polqry+"%' ";	
		}
		if(!StrUtils.isNull(pddqry)){
			dynamicClauseWhere +="\nAND t.pdd ILIKE '%"+pddqry+"%' ";	
		}
		if(!StrUtils.isNull(podqry)){
			dynamicClauseWhere +="\nAND t.pod ILIKE '%"+podqry+"%' ";	
		}
		
		if(!StrUtils.isNull(destinationqry)){
			dynamicClauseWhere +="\nAND t.destination ILIKE '%"+destinationqry+"%' ";	
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
		m.put("dynamicClauseWhere", dynamicClauseWhere);
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	@SaveState
	public int splitCount;
	
	@Action
	public void splitwms() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length > 1) {
			MessageUtils.alert("请只选择一个入仓单！");
			return;
		}
		if(!(splitCount>0)) {
			MessageUtils.alert("请选择拆分份数(不能为0)！");
			return;
			
		}
		String usercode = AppUtils.getUserSession().getUsercode();
		String sql = "SELECT f_wms_in_copyorplit('type=split;inid="+ids[0]+";splitcount="+splitCount+";usercode="+usercode+"');";
		try {
			this.serviceContext.busCustomsMgrService.busCustomsDao.executeQuery(sql);
			MessageUtils.alert("OK");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void copyadd(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length ==0 || ids.length > 1) {
			MessageUtils.alert("请只选择一个入仓单！");
			return;
		}
		try {
			String usercode = AppUtils.getUserSession().getUsercode();
			String sql = "SELECT f_wms_in_copyorplit('type=copy;inid="+ids[0]+";usercode="+usercode+"');";
			this.serviceContext.busCustomsMgrService.busCustomsDao.executeQuery(sql);
			MessageUtils.alert("OK");
			this.refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Action
	public void submitBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选行");
			return;
		}
		try {
			wmsInMgrService.updateSubmit(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		
	}

	@Action
	public void cancelSubmitBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选行");
			return;
		}
		
		try {
			wmsInMgrService.updateCanceSubmit(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选行");
			return;
		}
		try {
			wmsInMgrService.delBatch(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void searchfee() {
		this.qryRefresh();
	}
	
	@Action
	public void clear() {
		this.clearQryKey();
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
	@SaveState
	public String sortlabel_4;
	
	@Bind
	@SaveState
	public String sortvalue_4;
	
	@Bind
	@SaveState
	public String sortlabel_5;
	
	@Bind
	@SaveState
	public String sortvalue_5;
	
	
	
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
		
		sortlabel_4 = null;
		sortvalue_4 = "asc";
		sortlabel_5 = null;
		sortvalue_5 = "asc";
		
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
						case 4:
							sortlabel_4 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_4 = "desc";
							}else{
								sortvalue_4 = "asc";
							}
							break;
						case 5:
							sortlabel_5 = sorts[i].substring(0, sorts[i].indexOf(" "));
							if(sorts[i].endsWith(" desc")){
								sortvalue_5 = "desc";
							}else{
								sortvalue_5 = "asc";
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
		
		sortlabel_4 = null;
		sortvalue_4 = "asc";
		sortlabel_5 = null;
		sortvalue_5 = "asc";
		
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
		if(!StrUtils.isNull(sortlabel_4)&&!StrUtils.isNull(sortvalue_4)){
			colorder += sortlabel_4 + " " + sortvalue_4 + ",";
		}
		if(!StrUtils.isNull(sortlabel_5)&&!StrUtils.isNull(sortvalue_5)){
			colorder += sortlabel_5 + " " + sortvalue_5 + ",";
		}
		
		colorder = colorder.endsWith(",") ? colorder.substring(0, colorder.length()-1) : colorder;
		
		AppUtils.createOrModifyUserColorder(getMBeanName()+".grid", colorder);
		MessageUtils.alert("OK!");
	}
	
}
