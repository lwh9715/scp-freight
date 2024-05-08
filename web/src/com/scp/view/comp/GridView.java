package com.scp.view.comp;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.DownloadListener;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.cache.CommonDBCache;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysGridDef;
import com.scp.service.ServiceContext;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.action.ActionGridExport;

/**
 * 简单表格，grid组件
 * 
 * @author neo
 */
public abstract class GridView{

	@Inject
	protected PartialUpdateManager update;

	@ManagedProperty("#{serviceContext}")
	protected ServiceContext serviceContext;

	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	@Accessible
	public Map<String, Object> qryMap = new HashMap<String, Object>();

	@ManagedProperty("#{daoIbatisTemplate}")
	public DaoIbatisTemplate daoIbatisTemplate;
	
	
	/**
	 * neo 20170306 标记grid是否延时加载
	 * gridLazyLoad=true时，GridDataProvider中，数据库不查询直接返回
	 */
	@SaveState
	@Bind
	protected Boolean gridLazyLoad = false;

	/**
	 * @param isPostBack
	 */
	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
		}
	}

	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid grid;
	
	@SaveState
	public int starts=0;
	
	@SaveState
    public int limits=100;

	@Bind(id = "grid", attribute = "dataProvider")
	protected GridDataProvider getDataProvider() {
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				if(gridLazyLoad){
					return new Object[]{};
				}else{
					starts = start;
					limits = limit;
					String sqlId = getMBeanName() + ".grid.page";
					return daoIbatisTemplate.getSqlMapClientTemplate()
					.queryForList(sqlId, getQryClauseWhere(qryMap)).toArray();
				}
			}

			@Override
			public int getTotalCount() {
				if(gridLazyLoad){
					return 0;
				}else{
					String sqlId = getMBeanName() + ".grid.count";
					List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
							.queryForList(sqlId, getQryClauseWhere(qryMap));
					if (list == null || list.size() < 1)
						return 0;
					Long count = Long.parseLong(list.get(0).get("counts")
							.toString());
					return count.intValue();
				}
			}
		};
	}

	/**
	 * 返回当前行的id
	 * 
	 * @return
	 */
	public long getGridSelectId() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length <= 0) {
			return -1;
		}
		if(StrUtils.isNull(ids[0]) || ids[0].indexOf("-") > 0){
			return 0l;
		}else{
			return Long.valueOf(ids[0]);
		}
	}

	@Bind
	public UIWindow editWindow;

	@Bind
	public UIIFrame editIFrame;

	@Action
	public void grid_ondblclick() {
		// if(editWindow != null)editWindow.show();
		// editIFrame.load("");
	}

	@Action
	public void grid_onrowselect() {
		// String[] ids = grid.getSelectedIds();
		// String id = grid.getSelectedIds()[0];
		// AppUtil.openWindow("_newRP",
		// "./actpayrecedit.xhtml?type=new&id=-1&customerid="+id+"");
	}
	
	/**
	 * 刷新方法
	 */
	@Action
	public void refresh() {
		if (grid != null) {
			gridLazyLoad = false;
			this.grid.reload();
		}
	}

	@Action
	public void clearQryKey() {
		if (qryMap != null) {
			qryMap.clear();
			gridLazyLoad = false;
			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.qryRefresh();
		}
	}

	@Action
	public void qryRefresh() {
		gridLazyLoad = false;
		this.refresh();
	}

	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		StringBuilder buffer = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		buffer.append("\n1=1 ");
		if (queryMap != null && queryMap.size() >= 1) {
			Set<String> set = queryMap.keySet();
			for (String key : set) {
				Object val = queryMap.get(key);
				String qryVal = "";

				if (val != null && !StrUtils.isNull(val.toString())) {
					qryVal = val.toString();
					if (val instanceof Date) {
						Date dateVal = (Date) val;
						long dateValLong = dateVal.getTime();
						Date d = new Date(dateValLong);
						Format format = new
						SimpleDateFormat("yyyy-MM-dd");
						String dVar = format.format(dateVal);
						buffer.append("\nAND CAST(" + key + " AS DATE) ='" + dVar + "'");
					} else {
						int index = key.indexOf("$");
						if (index > 0) {
							buffer.append("\nAND " + key.substring(0, index) + "=" + val);
						} else {
							val = val.toString().replaceAll("'", "''");
							int indexNot = val.toString().indexOf("!");
							if (indexNot > 0) {
								val = val.toString().substring(0, indexNot);
								buffer.append("\nAND " + key + " NOT ILIKE '%'||" +"TRIM('"+ val+"')" + "||'%'");
							}else{
								buffer.append("\nAND " + key + " ILIKE '%'||" +"TRIM('"+ val+"')" + "||'%'");
							}
						}
					}
				}
			}
		}
		String qry = StrUtils.isNull(buffer.toString()) ? "" : buffer
				.toString();
		map.put("limit", limits+"");
		map.put("start", starts+"");
		map.put("qry", qry);
		return map;
	}

	@Action
	public void export() {
		ActionGridExport actionGridExport = new ActionGridExport();
		actionGridExport.setKeys((String) AppUtils.getReqParam("key"));
		actionGridExport.setVals((String) AppUtils.getReqParam("value"));
		int limitsNew = limits;
		int startsNew = starts;
		try {
			limits = 100000;
			starts = 0;
			
			String sqlId = getMBeanName() + ".grid.page";
			List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(this.qryMap));
			
			if(grid != null){
				List<Map> listNew = new ArrayList<Map>();
				String[] ids = this.grid.getSelectedIds();
				boolean flag = false;
				if(ids != null && ids.length != 0) {
					flag = true;
					for (Map map : list) {
						for (String id : ids) {
							if(StrUtils.getMapVal(map, "id").equals(id)){
								listNew.add(map);
							}
						}
					}
				}
				if(flag){
					list = listNew;
				}
			}
			
			actionGridExport.execute(list);
			Browser.execClientScript("simulateExport.fireEvent('click');");
			qryRefresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
		} finally{
			limits = limitsNew;
			starts = startsNew;
		}
	}

	@DownloadListener
	public void doSimulateExport(FacesContext context, ResponseStream out) {
		ActionGridExport actionGridExport = new ActionGridExport();
		try {
			actionGridExport.exportExcelFile(context, out);
		} catch (IOException e) {
			MessageUtils.showException(e);
		}
	}

	public String getMBeanName() {
		ManagedBean ma = this.getClass().getAnnotation(ManagedBean.class);
		String mbeanName = ma.name();
		return mbeanName;
	}

	protected void alert(String message) {
		Browser.execClientScript("window.alert('" + message + "');");
	}

//	@Bind
//	protected UIWindow importFileWindow;
//
//	@Action
//	protected void importFile() {
//		importFileWindow.show();
//	}
//	
//	
	@Action
	public void saveGridUserDefSetDefault() {
		Long userid = AppUtils.getUserSession().getUserid();
		String gridid = AppUtils.getReqParam("gridid");
		try {
			if(gridid!=null&&gridid.length()>0){
				String sql = "userid = "+userid+" AND gridid = '"+gridid+"'";
				SysGridDef sysGridDef = this.serviceContext.sysGridDefService.sysGridDefDao.findOneRowByClauseWhere(sql);
				if(sysGridDef!=null){
					this.serviceContext.sysGridDefService.sysGridDefDao.remove(sysGridDef);
				}
				MessageUtils.alert("OK!");
				commonDBCache.clearCacheGridUserDef();
			}
		} catch (Exception e) {
			MessageUtils.alert("已经是初始数据");
		}
		
	}
	
	@Action
	public void saveGridUserDef() {
		Long userid = AppUtils.getUserSession().getUserid();
		String gridid = AppUtils.getReqParam("gridid");
		String colkey = AppUtils.getReqParam("colkey");
		String colwidth = AppUtils.getReqParam("colwidth");
		String ishidden = AppUtils.getReqParam("ishidden");
		SysGridDef sysGridDef = null;
		try {
			if(gridid!=null&&gridid.length()>0){
				String sql = "userid = "+userid+" AND gridid = '"+gridid+"'";
				sysGridDef = this.serviceContext.sysGridDefService.sysGridDefDao.findOneRowByClauseWhere(sql);
			}
		} catch (NoRowException e) {
			sysGridDef = new SysGridDef();
		} catch (Exception e) {
			//System.out.println(e);
			return;
		}
		sysGridDef.setGridid(gridid);
		sysGridDef.setUserid(userid);
		sysGridDef.setColkey(colkey);
		sysGridDef.setColwidth(colwidth);
		sysGridDef.setIshidden(ishidden);
		sysGridDef.setIsselect("t");
		sysGridDef.setConfigurename("定制1");
		this.serviceContext.sysGridDefService.saveData(sysGridDef);
		MessageUtils.alert("OK!");
		commonDBCache.clearCacheGridUserDef();
	}
	
	
	@Resource
	private CommonDBCache commonDBCache;
	
	/**
	 * 设置表格属性
	 */
	protected void applyGridUserDef(String gridid , String gridJsvar) {
		try {
			Long userid = AppUtils.getUserSession().getUserid();
//			String querySql = "SELECT colkey,colwidth,ishidden FROM sys_griddef WHERE gridid='%s' AND userid=%s;";
//			querySql = String.format(querySql, gridid,userid);
//			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
//			String colkey = StrUtils.getMapVal(m, "colkey");
//			String colwidth = StrUtils.getMapVal(m, "colwidth");
//			String ishidden = StrUtils.getMapVal(m, "ishidden");
//			String js = "applyGridUserDef('"+colkey+"','"+colwidth+"','"+ishidden+"',"+gridJsvar+");";
			////System.out.println("js:"+js);
			String js = commonDBCache.getGridUserDef(userid,gridid,gridJsvar);
			//System.out.println("js:"+js);
			if(!StrUtils.isNull(js)){
				Browser.execClientScript(js);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void applyGridUserDef() {
		String gridid  = this.getMBeanName() + ".grid";
		String gridJsvar = "gridJsvar";
		applyGridUserDef(gridid , gridJsvar);
	}
	
	@Action
	public void columnRefresh(){//页面选择栏目刷新事件
		applyGridUserDef();
	}
	
	@Bind
	protected UIWindow importFileWindow;

	@Action
	protected void importFile() {
		importFileWindow.show();
	}
	
	/**
	 * grid在前台设置每页显示的行数
	 */
	@Action
	public void doChangeGridPageSize() {
		String pageStr = (String)AppUtils.getReqParam("page");
		if(!StrUtils.isNull(pageStr) && StrUtils.isNumber(pageStr)) {
			//alert("pageStr:"+pageStr);
			Integer page = Integer.parseInt(pageStr);
			this.grid.setRows(page);
			gridLazyLoad = false;
			this.grid.rebind();
//			//记录选择的行数到个人设置
			String mbeanId = this.getMBeanName();
			String girdId = mbeanId+".grid.pagesize";
			try {
//				CfgUtil.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				ConfigUtils.refreshUserCfg(girdId, pageStr, AppUtils.getUserSession().getUserid());
				applyGridUserDef();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			alert("Wrong pagesize：" + pageStr);
		}
	}
	
	
	public Integer gridPageSize = 100;
	
	/**
	 * 由个人设置中提取行数，若找不到则返回默认的100行
	 * 
	 * @return
	 */
	public Integer getGridPageSize() {
		String mbeanId = this.getMBeanName();
		String girdId = mbeanId + ".grid.pagesize";
		String pageSize;
		try {
			pageSize = ConfigUtils.findUserCfgVal(girdId, AppUtils.getUserSession().getUserid());
		} catch (Exception e) {
			e.printStackTrace();
			return gridPageSize;
		}
		if (!StrUtils.isNull(pageSize) && StrUtils.isNumber(pageSize)) {
			Integer page = Integer.parseInt(pageSize);
			gridPageSize = page;
			return page;
		} else {
			return gridPageSize;
		}
	}
	
	/**
	 * @return返回对应bean表单设置
	 */
	public String getSysformcfg(){
		String mBeanName = this.getMBeanName();
		String sql = "SELECT array_to_json (ARRAY_AGG(row_to_json(T))) :: TEXT AS json FROM ("
					+"		SELECT * from sys_formcfg WHERE formid = '"+mBeanName+"' AND COALESCE(cfgtype,'') <> 'js'"
					+") AS T";
		Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		if(m!=null&&m.get("json")!=null){
			return m.get("json").toString();
		}else{
			return "";
		}
	}
	
	/**
	 * @param ctrlcode sys_module表的code
	 * @param userid
	 * @return 返回是否有此权限
	 */
	public boolean getCheckright(String ctrlcode,Long userid){
		try {
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow("SELECT f_checkright('"+ctrlcode+"',"+userid+") AS result");
			if(m!=null&&m.get("result")!=null&&m.get("result").toString().equals("1")){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Action
	public void saveColModelMultipleFunAjax() {
		try {
			Long userid = AppUtils.getUserSession().getUserid();
			String gridid = AppUtils.getReqParam("gridid");
			String colkey = AppUtils.getReqParam("colkey");
			String colwidth = AppUtils.getReqParam("colwidth");
			String ishidden = AppUtils.getReqParam("ishidden");
			String flag = AppUtils.getReqParam("flag");
			if (gridid != null && gridid.length() > 0) {
				if (gridid != null && gridid.length() > 0) {
					if ("".equals(flag)) {
						String sql = "userid = " + userid + " AND gridid = '" + gridid + "'";
						List<SysGridDef> thislist = (List<SysGridDef>) this.serviceContext.sysGridDefService.sysGridDefDao.findAllByClauseWhere(sql);

						if (thislist.isEmpty()) {
							SysGridDef sysGridDef = new SysGridDef();
							sysGridDef.setGridid(gridid);
							sysGridDef.setUserid(userid);
							sysGridDef.setColkey(colkey);
							sysGridDef.setColwidth(colwidth);
							sysGridDef.setIshidden(ishidden);
							sysGridDef.setIsselect("t");
							sysGridDef.setConfigurename("定制1");
							this.serviceContext.sysGridDefService.saveData(sysGridDef);
						} else {

							boolean ishavethisCol = false;
							for (SysGridDef oldsysGridDef : thislist) {
								if (colkey.equals(oldsysGridDef.getColkey())) {
									ishavethisCol = true;
								}
							}
							if (!ishavethisCol) {
								String updateSql = "update sys_griddef set isselect ='f' where userid = " + userid + " AND gridid = '" + gridid + "'";
								daoIbatisTemplate.updateWithUserDefineSql(updateSql);

								SysGridDef sysGridDef = new SysGridDef();
								sysGridDef.setGridid(gridid);
								sysGridDef.setUserid(userid);
								sysGridDef.setColkey(colkey);
								sysGridDef.setColwidth(colwidth);
								sysGridDef.setIshidden(ishidden);
								sysGridDef.setIsselect("t");
								sysGridDef.setConfigurename("定制" + (thislist.size() + 1));
								this.serviceContext.sysGridDefService.saveData(sysGridDef);
							}

						}
					} else if ("selectcolumn".equals(flag)) {
						String sql = "userid = " + userid + " AND gridid = '" + gridid + "'"+ " AND configurename = '" + configurenameValue + "'";
						SysGridDef sysGridDef =  this.serviceContext.sysGridDefService.sysGridDefDao.findOneRowByClauseWhere(sql);
						sysGridDef.setGridid(gridid);
						sysGridDef.setUserid(userid);
						sysGridDef.setColkey(colkey);
						sysGridDef.setColwidth(colwidth);
						sysGridDef.setIshidden(ishidden);
						this.serviceContext.sysGridDefService.saveData(sysGridDef);
					}
				}
			}

			Browser.execClientScript("location.reload();");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void deleteconfigurenameAjax() {
		try {
			Long userid = AppUtils.getUserSession().getUserid();
			String gridid = AppUtils.getReqParam("gridid");
			String colkey = AppUtils.getReqParam("colkey");
			String colwidth = AppUtils.getReqParam("colwidth");
			String ishidden = AppUtils.getReqParam("ishidden");
			if (gridid != null && gridid.length() > 0) {
				if (gridid != null && gridid.length() > 0) {
					String sql = "userid = " + userid + " AND gridid = '" + gridid + "'  and isselect = 't'";
					SysGridDef sysGridDef =  this.serviceContext.sysGridDefService.sysGridDefDao.findOneRowByClauseWhere(sql);
					this.serviceContext.sysGridDefService.removeDate(sysGridDef.getId());

					String updateSql = "update sys_griddef set isselect ='t' where id = (select id from sys_griddef where  userid = " + userid + " AND gridid = '" + gridid + "' limit 1)";
					daoIbatisTemplate.updateWithUserDefineSql(updateSql);
				}
			}

			Browser.execClientScript("location.reload();");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}


	@Bind
	@SaveState
	private String configurenameValue;

	protected void applyGridUserMultipleDef() {
		String gridid = this.getMBeanName() + ".grid";
		String gridJsvar = "gridJsvar";

		try {
			Long userid = AppUtils.getUserSession().getUserid();

			String querySql = "SELECT colkey,colwidth,ishidden,configurename FROM sys_griddef WHERE gridid='%s' AND userid=%s and isselect = 't' ORDER BY id DESC LIMIT 1;";
			querySql = String.format(querySql, gridid, userid);
			Map m = null;
			try {
				m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
			} catch (NoRowException e) {
			}
			String js = "";
			if (m != null && m.size() > 0) {
				String colkey = StrUtils.getMapVal(m, "colkey");
				String colwidth = StrUtils.getMapVal(m, "colwidth");
				String ishidden = StrUtils.getMapVal(m, "ishidden");
				configurenameValue = StrUtils.getMapVal(m, "configurename");
				js = "applyGridUserDef('" + colkey + "','" + colwidth + "','" + ishidden + "'," + gridJsvar + ");";
			}

			if (!StrUtils.isNull(js)) {
				Browser.execClientScript(js);
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Bind(id="configurename")
	public List<SelectItem> getConfigurename() {
		try {
			Long userid = AppUtils.getUserSession().getUserid();
			String gridid = this.getMBeanName() + ".grid";
			return CommonComBoxBean.getComboxItems("DISTINCT id","configurename"
					,"sys_griddef AS d","where userid = " + userid + " AND gridid = '" + gridid + "'","ORDER BY id ");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
	}

	@Action
	public void reductionconfigurenameAjax() {
		try {
			String reductionconfigurenamevalue = AppUtils.getReqParam("reductionconfigurenamevalue");
			Long userid = AppUtils.getUserSession().getUserid();
			String gridid = this.getMBeanName() + ".grid";

			String updateSql0 = "update sys_griddef set isselect ='f' where userid = " + userid + " AND gridid = '" + gridid + "'";
			daoIbatisTemplate.updateWithUserDefineSql(updateSql0);

			String updateSql1 = "update sys_griddef set isselect ='t' where id = '" + reductionconfigurenamevalue + "'";
			daoIbatisTemplate.updateWithUserDefineSql(updateSql1);

			Browser.execClientScript("location.reload();");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
}
