package com.scp.view.module.finance.edi;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.filechooser.FileSystemView;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EdiGridView;

@ManagedBean(name = "pages.module.finance.edi.kisBean", scope = ManagedBeanScope.REQUEST)
public class KisBean  extends EdiGridView{
	
	@Bind
	@SaveState
	private String startDate;

	@Bind
	@SaveState
	private String endDate;
	
	@Bind
	@SaveState
	public String qryYear;
	
	@SaveState
	@Accessible
	public String qryPeriodS;
	
	@SaveState
	@Accessible
	public String qryPeriodE;

	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		try {
			if (!isPostBack) {
				if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
					MessageUtils.alert("未选择帐套，请重新选择帐套！");
					return;
				}
				initData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	@Action
	public void exporting(){
		
	}
	
	public boolean running(String jobids){
		try {
			String urlArgs2 = "";
			String sqlQry = "SELECT * FROM f_edi_vch_kis('"+urlArgs2 +"')";
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sqlQry);
			Object object = m.get("f_sdi_inttra");
			String str = "";
			if(object!=null){
				str = object.toString();
			}
			return writer(str);
		} catch (Exception e) {
			MessageUtils.showException(e);
			return false;
		}
	}
	
	@Bind
	public UIDataGrid gridaccount;
	
	@SaveState
	@Accessible
	public Map<String, Object> qryMapShip = new HashMap<String, Object>();
	
	@Bind(id = "gridaccount", attribute = "dataProvider")
	protected GridDataProvider getGridScheduleDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.finance.edi.kisBean.gridaccount.page";
				return serviceContext.daoIbatisTemplate
						.getSqlMapClientTemplate().queryForList(sqlId,
								getQryClauseWhere2(qryMapShip), start, limit)
						.toArray();

			}

			@Override
			public int getTotalCount() {
				return 10000;
			}
		};
	}
	
	public Map getQryClauseWhere2(Map<String, Object> queryMap) {
		return super.getQryClauseWhere(queryMap);
	}
	
	/*
	 * 写入文件
	 * */
	public  boolean writer(String str){
		Random ra =new Random();
		//获得当前系统桌面路径
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String absolutePath = desktopDir.getAbsolutePath(); 
		String randomString = getRandomString(3);
		File file = new File(absolutePath+"/edi/inttra");
		if (!file .exists()  && !file .isDirectory()) {     
			file.mkdirs();
		}
		String path=absolutePath+"/edi/vch/kis"+randomString+".txt";
		File newFile = newFile(path);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
			bw.write(str);
			bw.newLine();
			//关闭资源
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
		
	}
	
	@SaveState
	public String gridaccountids;
	
	@Bind
	public UIIFrame iframe;
	
	@Override
	public void grid_ondblclick() {
		super.grid_ondblclick();
		String winId = "_fsin";
		String url = "";
		//不能编辑
		url = "/pages/module/finance/doc/indtl_readonly.xhtml?id="+this.getGridSelectId();

		iframe.load(url);
		if(editWindow != null)editWindow.show();
	}
	
	@Action
	public void qryRefreshs(){
		String[] ids = this.gridaccount.getSelectedIds();
		gridaccountids = ids!=null&&ids.length>0?StrUtils.array2List(ids):"";
		qryRefresh();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		if(!StrUtils.isNull(gridaccountids)){
			qry += "\nAND actsetid = ANY(array["+gridaccountids+"])";
		}
		if(!StrUtils.isNull(qryYear)) {
			qry += "\nAND year = " + qryYear; // 年查询
		}
		if(!StrUtils.isNull(qryPeriodS) || !StrUtils.isNull(qryPeriodE)) {
			// 期间查询
			qry += "\nAND period BETWEEN " + (StrUtils.isNull(qryPeriodS)?0:qryPeriodS) 
				+ " AND " + (StrUtils.isNull(qryPeriodE)?12:qryPeriodE);
		}
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	@SaveState
	private String url;
	
	@SaveState
	private Map<String, String> cfgDataMap;
	
	@Action
	public void savesetExportin(){
		try {
			ConfigUtils.refreshUserCfg(cfgDataMap, AppUtils.getUserSession().getUserid());
			MessageUtils.alert("OK!");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		this.url  = this.getCfgDataMap().get("kis_url");
		update.markUpdate(true, UpdateLevel.Data, "url");
	}
	
	public Map<String, String> getCfgDataMap() {
		return this.cfgDataMap;
	}
	
	protected void initData() throws Exception {
		String cfgDataMap [] = {"kis_url"};
		this.cfgDataMap = ConfigUtils.findUserCfgVals(cfgDataMap, AppUtils.getUserSession().getUserid());
		if(!StrUtils.isNull(this.cfgDataMap.get("kis_url"))&&this.cfgDataMap.get("kis_url")!=null){
			url = this.cfgDataMap.get("kis_url");
		}
	}
	
}
