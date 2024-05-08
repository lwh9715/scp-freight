package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
@ManagedBean(name = "pages.module.stock.wmsbarcodeBean", scope = ManagedBeanScope.REQUEST)
public class WmsbarcodeBean extends GridView{
	
	@SaveState
	@Bind
	public Long refid;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		initPrintConfig();//初始化打印数据
		String id = AppUtils.getReqParam("refid").trim();
		if(id!=null&&id!=""){
			refid = Long.valueOf(id);
		}
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry+="\n AND refid =" + refid;
		m.put("qry", qry);
		return m;
	}
	
	
	@Action
	public void del() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			String id = StrUtils.array2List(ids);
			String sql = "delete from bus_barcode where id in ("+id+");";
			serviceContext.wmsIndtlMgrService().wmsIndtlDao.executeSQL(sql);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Bind
	@SaveState
	public String jsons ="[]";
	
	@Action
	public void preview() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length != 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			String id = StrUtils.array2List(ids);
			String sql = "\nSELECT "+
			"\narray_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json"+ 
			"\nFROM (SELECT"+ 
			"\n*,(SELECT nos FROM wms_in x where x.isdelete = false AND x.id = (SELECT inid FROM wms_indtl x where x.id = b.refid)) AS nos,(SELECT refno FROM wms_in x where x.isdelete = false AND x.id = (SELECT inid FROM wms_indtl x where x.id = b.refid)) AS refno, (SELECT pieceinbox FROM wms_indtl x where x.id = b.refid) AS pieceinbox" +
			"\nFROM bus_barcode b WHERE id in ("+id+")" +
			"\n) AS T";
			String ret = "";
			Map<String, Object> map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				ret = map.get("json").toString();
			}else {
				ret = "{'label': ''}";
			}
			this.jsons = ret;
			String  sql2 ="UPDATE bus_barcode set isprinted = TRUE where id in ("+id+")";
			serviceContext.wmsIndtlMgrService().wmsIndtlDao.executeSQL(sql2);
			Browser.execClientScript("preview();");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void print() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length < 1) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		try {
			String id = StrUtils.array2List(ids);
			String sql = "\nSELECT "+
			"\narray_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json"+ 
			"\nFROM (SELECT"+ 
			"\n*,(SELECT nos FROM wms_in x where x.isdelete = false AND x.id = (SELECT inid FROM wms_indtl x where x.id = b.refid)) AS nos,(SELECT refno FROM wms_in x where x.isdelete = false AND x.id = (SELECT inid FROM wms_indtl x where x.id = b.refid)) AS refno, (SELECT pieceinbox FROM wms_indtl x where x.id = b.refid) AS pieceinbox" +
			"\nFROM bus_barcode b WHERE id in ("+id+")" +
			"\n) AS T";
			String ret = "";
			Map<String, Object> map = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			if(map != null && map.containsKey("json")&&map.get("json")!=null){
				ret = map.get("json").toString();
			}else {
				ret = "{'label': ''}";
			}
			this.jsons = ret;
			String  sql2 ="UPDATE bus_barcode set isprinted = TRUE where id in ("+id+")";
			serviceContext.wmsIndtlMgrService().wmsIndtlDao.executeSQL(sql2);
			Browser.execClientScript("print();");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	@Bind
	@SaveState
	public String print1_w;
	@Bind
	@SaveState
	public String print_w;
	@Bind
	@SaveState
	public String print_h;
	@Bind
	@SaveState
	public String printpage_w;
	@Bind
	@SaveState
	public String printpage_h;
	@Bind
	@SaveState
	public String print_top;
	@Bind
	@SaveState
	public String print_left;
	@Bind
	@SaveState
	public String print_name;
	
	@Bind
	public UIWindow showPringSeting;
	
	
	private void initPrintConfig(){
		try {
			Long userid = AppUtils.getUserSession().getUserid();
			print1_w = ConfigUtils.findUserCfgVal("jsprint_config_print1_w", userid);
			print1_w = StrUtils.isNull(print1_w) ? "750px" : print1_w;
			print_w = ConfigUtils.findUserCfgVal("jsprint_config_print_w", userid);
			print_w = StrUtils.isNull(print_w) ? "465" : print_w;
			print_h = ConfigUtils.findUserCfgVal("jsprint_config_print_h", userid);
			print_h = StrUtils.isNull(print_h) ? "900" : print_h;
			printpage_w = ConfigUtils.findUserCfgVal("jsprint_config_printpage_w", userid);
			printpage_w = StrUtils.isNull(printpage_w) ? "345" : printpage_w;
			printpage_h = ConfigUtils.findUserCfgVal("jsprint_config_printpage_h", userid);
			printpage_h = StrUtils.isNull(printpage_h) ? "750" : printpage_h;
			print_top = ConfigUtils.findUserCfgVal("jsprint_config_print_top", userid);
			print_top = StrUtils.isNull(print_top) ? "82" : print_top;
			print_left = ConfigUtils.findUserCfgVal("jsprint_config_print_left", userid);
			print_left = StrUtils.isNull(print_left) ? "20" : print_left;
			print_name = ConfigUtils.findUserCfgVal("jsprint_config_print_name", userid);
			print_name = StrUtils.isNull(print_name) ? "barcode" : print_name;
			} catch (Exception e) {
			MessageUtils.showException(e);
			}
			update.markUpdate(true,UpdateLevel.Data,"printPanel");
	}
	
	@Action
	public void showPrintConfig(){
		initPrintConfig();
		showPringSeting.show();
	}
	
	@Action
	public void savePrintConfig(){
		try {
			showPringSeting.close();
			ConfigUtils.refreshUserCfg("jsprint_config_print1_w",print1_w,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_w",print_w,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_h",print_h,AppUtils.getUserSession().getUserid());
			
			ConfigUtils.refreshUserCfg("jsprint_config_printpage_w",printpage_w,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_printpage_h",printpage_h,AppUtils.getUserSession().getUserid());
			
			ConfigUtils.refreshUserCfg("jsprint_config_print_top",print_top,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_left",print_left,AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_name",print_name,AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void restorePrintConfig(){
		try {
			ConfigUtils.refreshUserCfg("jsprint_config_print1_w","750px",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_w","465",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_h","900",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_printpage_w","345",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_printpage_h","750",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_top","82",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_left","20",AppUtils.getUserSession().getUserid());
			ConfigUtils.refreshUserCfg("jsprint_config_print_name","barcode",AppUtils.getUserSession().getUserid());
			initPrintConfig();
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
}
