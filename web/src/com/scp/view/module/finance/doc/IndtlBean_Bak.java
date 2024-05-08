package com.scp.view.module.finance.doc;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.BeforeRender;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.NoRowException;
import com.scp.model.finance.fs.FsAct;
import com.scp.model.finance.fs.FsActset;
import com.scp.model.finance.fs.FsVch;
import com.scp.model.finance.fs.FsVchdesc;
import com.scp.model.finance.fs.FsVchdtl;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;

@ManagedBean(name = "pages.module.finance.doc.indtl_bakBean", scope = ManagedBeanScope.REQUEST)
public class IndtlBean_Bak extends GridFormView {

	@Bind
	@SaveState
	public Long mPkVal = -1L;
	
	@Bind
	@SaveState
	public Long actsetid = -1L; //如果是新增进来,那么取当前人绑定的帐套,如果是pk传入进来 ,取这个凭证对应的actsetid
	
	
	@Bind
	@SaveState
	public String actsetcode = ""; //如果是新增进来,那么取当前人绑定的帐套,如果是pk传入进来 ,取这个凭证对应的actsetid

	@SaveState
	@Accessible
	public FsVch selectedRowData = new FsVch();

	@SaveState
	@Accessible
	public FsVchdtl ddata = new FsVchdtl();
	
	@SaveState
	@Accessible
	public FsAct act = new FsAct();
	
	@SaveState
	@Accessible
	public FsVchdesc vchdesc = new FsVchdesc();

	@Bind
	public UIWindow astWindow;
	
	@Bind
	public UIWindow vchdescWindow;

	@Bind
	public UIDataGrid gridVchdesc;
	
	@SaveState
	@Accessible
	public FsActset fsActset = new FsActset();

	@Bind
	@SaveState
	public String basecy = "";

	@SaveState
	public String year = "";

	@SaveState
	public String period = "";

	@Bind
	@SaveState
	public String actidHide;
	
	@Bind
	@SaveState
	public String astidHide;
	
	//凭证详细编辑弹窗界面第一次加载，用于显示如果是选择了勾选往来单位的科目，显示所选择的核算项目code/name
	@Bind
	@SaveState
	public String astdescShow;
	
	@Bind
	public UIWindow vchdtlEditWindow;
	
	@Bind
	public String vchdescid;
	
	@SaveState
	public boolean lazyload = false;//科目和兑换率的加载   只有在弹出编辑框的时候加载
	
	
	@SaveState
	public boolean astgridload = false; //  如果勾选往来单位，显示核销项目 其他情况下都不加载

	@BeforeRender
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
			if(selectedRowData.getIscheck() == null){

			} else {
				disableAllButton(selectedRowData.getIscheck());
			}
		}
	}

	@Override
	public void clearQryKey() {
		super.clearQryKey();
	}

	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND vchid = " + mPkVal;
		m.put("qry", qry);
		return m;
	}

	public void init() {
		selectedRowData = new FsVch();
		String id = AppUtils.getReqParam("id");
		if (!StrUtils.isNull(id)) {
			this.mPkVal = Long.parseLong(id);
			this.refreshMasterForm();
			refresh(); // 从表也刷新

		} else {
			this.mPkVal = -1L;
			addMaster();
			refresh();
		}
		
		
	}
	
	@Override
	public void add() {
		if(this.mPkVal==-1L){
			MessageUtils.alert("请先保存凭证主表");
			return;
		}
		
		String vchdesc = "";
		if(ddata != null) vchdesc = ddata.getVchdesc();
		this.ddata = new FsVchdtl();
		//手工凭证
		//子表
		//selectedRowData.setSrctype("H");
		ddata.setSrctype(this.selectedRowData.getSrctype());
		ddata.setVchdesc(vchdesc);
		//ddata.setActsetid(AppUtil.getUserSession().getActsetid());
		ddata.setActsetid(this.actsetid);
		ddata.setVchid(mPkVal);
		ddata.setSrctype("H");
		astidHide = "0";
		astdescShow = "";
		update.markUpdate(true, UpdateLevel.Data, "astdescShow");
		//加载一次后边就不用加载了
		if(this.lazyload == true){
			
		}else{
		this.lazyload = true;
		this.gridAct.reload();
		this.gridRate.reload();
		}
		
		super.add();
		//this.gridAst.reload();
		
	}
	
	@Action
	public void add2(){
		this.add();
	}

	@Override
	protected void doServiceSave() {
		if(!StrUtils.isNull(astidHide)) {
			this.ddata.setAstid(Long.valueOf(astidHide));
		}
		String vchSql = "SELECT EXISTS(SELECT 1 FROM fs_act WHERE isdelete = FALSE AND isinuse = TRUE AND parentid = "
			+ (StrUtils.isNull(actidHide) ? 0 : actidHide) + ") AS ihc";
		try {
			Map m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(vchSql);
			if (Boolean.parseBoolean(m.get("ihc").toString())) {
				MessageUtils.alert("请选择明细科目！");
				this.refresh();
				return;
			}
			serviceContext.vchdtlMgrService.saveDtlData(ddata);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	@Action
	public void saveAndClose() {
		doServiceSave();
		this.editWindow.close();
		this.refresh();
	}
	
	
	@Action
	public void saveAndAdd() {
		this.save();
		this.add();
	}
	
	
//	@Action
//	public void copy() {
//		String args = "id="+this.mPkVal+";user="+AppUtil.getUserSession().getUsercode()+";";
//		String sql = "SELECT f_fs_vch_addcopy('"+args+"') AS vchid";
//		try {
//			List list = this.serviceContext.vchMgrService.fsVchDao.executeQuery(sql);
//			String vchNew = (String) list.get(0);
//			MsgUtil.alert(vchNew);
//			this.mPkVal = Long.parseLong(vchNew.split("-")[0]);
//			refreshMasterForm();
//			this.refresh();
//		} catch (Exception e) {
//			e.printStackTrace();
//			MsgUtil.showException(e);
//		}
//	}

	@Override
	public void del() {
		
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先点击选择需要删除的分录");
			return;
		}
		try {
			if (selectedRowData.getIscheck()) {
				MessageUtils.alert("请先取消审核以后才能删除");
				return;
			} else {
				serviceContext.vchdtlMgrService.removeDatedtl(this
						.getGridSelectId());
				this.alert("OK");
				refresh();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void gridAst_ondblclick() {
		String id = gridAst.getSelectedIds()[0];
		String sql = "SELECT (code||'/'||name) AS astdesc FROM fs_ast WHERE isdelete = FALSE AND id = " + id;
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			astdescShow = String.valueOf(m.get("astdesc"));
			
			astidHide = id;
			
			this.update.markUpdate(UpdateLevel.Data, "astidHide");
			this.update.markUpdate(UpdateLevel.Data, "astdescShow");
			this.astWindow.close();
		} catch (Exception e) {
			astdescShow = "";
			//e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * 凭证列表双击打开弹窗编辑界面
	 */
	@Override
	public void grid_ondblclick() {
		this.pkVal = getGridSelectId();
		doServiceFindData();
		Long astid = ddata.getAstid();
		if(astid != null) {
			astidHide = astid.toString();//初始化核算项目id
			String sql = "SELECT (code||'/'||name) AS astdesc FROM fs_ast WHERE isdelete = FALSE AND id = " + astid;
			try {
				Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				astdescShow = String.valueOf(m.get("astdesc"));
			} catch (Exception e) {
				astdescShow = "";
				//e.printStackTrace();
			}
		} else {
			astidHide = "0";//初始化核算项目id
			astdescShow = "";
		}
	
		if(this.lazyload == true){
			
		}else{
			this.lazyload = true;
			this.gridAct.reload();
			this.gridRate.reload();
		}
		if(editWindow != null)editWindow.show();
		
		this.update.markUpdate(UpdateLevel.Data, "astdescShow");
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		
		//this.gridAst.rebind();  
		
	}

	@Override
	public void refreshForm() {
		this.ddata = serviceContext.vchdtlMgrService.fsVchdtlDao.findById(pkVal);
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
	}

	public void refreshMasterForm() {
		this.selectedRowData = serviceContext.vchMgrService.fsVchDao
				.findById(this.mPkVal);
		setActsetid();
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		
		
	}

	protected void doServiceFindData() {
		this.ddata = serviceContext.vchdtlMgrService.fsVchdtlDao
				.findById(this.pkVal);
	}

	public void doServiceSaveMaster() {
		try {
			serviceContext.vchMgrService.saveData(selectedRowData);
			this.mPkVal = selectedRowData.getId();
			this.refreshMasterForm();
			//save();
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	@Action
	public void ischeckAjaxSubmit() {

		Boolean isCheck = selectedRowData.getIscheck();
		if (this.mPkVal == -1l) {
			MessageUtils.alert("请保存主表,并录入子表数据");
			selectedRowData.setIscheck(!isCheck);
			return;
		}
		String updater = AppUtils.getUserSession().getUsercode();
		String sql = "";
		if (isCheck) {
			sql = "UPDATE fs_vch SET ischeck = TRUE ,checktime = NOW()"
					+ ",checkter = '" + updater + "' WHERE id =" + this.mPkVal
					+ ";";
		} else {
			sql = "UPDATE fs_vch SET ischeck = FALSE ,checktime = NOW()"
					+ ",checkter = '" + updater + "' WHERE id =" + this.mPkVal
					+ ";";
		}
		try {
			serviceContext.vchdtlMgrService.fsVchdtlDao.executeSQL(sql);
			refreshMasterForm();
			this.disableAllButton(isCheck);
		} catch (Exception e) {
			selectedRowData.setIscheck(!isCheck);
			selectedRowData.setCheckter(isCheck ? null : AppUtils
					.getUserSession().getUsercode());
			selectedRowData.setChecktime(isCheck ? null : Calendar
					.getInstance().getTime());
			refreshMasterForm();
			this.disableAllButton(!isCheck);
			MessageUtils.showException(e);

		}
	}

	@Action
	public void addMaster() {
		//保存之后又新增的情况,这时候需要检查
		if(this.mPkVal!=-1){
			String isreq=checkVchAdd(this.mPkVal);
			if(isreq.equals("0")){
				MessageUtils.alert("当前凭证借贷双方不平!暂时不能新增其他凭证!");
				return;
			}
		}
		//处理保存一个凭证,分录界面没关,然后重新新增一个凭证,会出问题
		if(editWindow != null)editWindow.close();
		disableAllButton(false);
		this.selectedRowData = new FsVch();
		this.mPkVal = -1l;
		setActsetid();
		try {
			Long vchtypeid = null;
			String sql = "SELECT id FROM fs_vchtype WHERE isdefault=true AND isdelete = FALSE AND actsetid = " + AppUtils.getUserSession().getActsetid();
			Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			vchtypeid = Long.valueOf(String.valueOf(m.get("id")));
			if(vchtypeid != null) selectedRowData.setVchtypeid(vchtypeid);
		} catch (Exception e) {
		}
		
		selectedRowData.setActsetid(AppUtils.getUserSession().getActsetid());
		selectedRowData.setSrctype("H");
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(year), Integer.parseInt(period)-1, 1);
		selectedRowData.setSingtime(c.getTime());
		selectedRowData.setYear(Short.valueOf(year));
		selectedRowData.setPeriod(Short.valueOf(period));
		
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		refresh();
	}

	@Action
	public void delMaster() {
		try {
			if (selectedRowData.getIscheck()) {
				MessageUtils.alert("请先取消提交以后才能删除");
				return;
			} else {
				serviceContext.vchdtlMgrService.removeDate(this.mPkVal);
				this.addMaster();
				this.alert("OK");
				refresh();
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	/*
	 * 预览凭证
	 */
	@Action
	public void showVch() {
		String url = "";
		url = AppUtils.getContextPath()
				+ "/reportJsp/showReport.jsp?raq=/finance/vch.raq&vchid="
				+ this.mPkVal + "&actsetid="
				+ this.actsetid;

		AppUtils.openWindow("indtl_vch", url);
	}

	@Action
	public void saveMaster() {
		if(selectedRowData.getVchtypeid() == null) {
			MessageUtils.alert("请选择凭证字!");
			return;
		}
		if(selectedRowData.getYear() == null) {
			MessageUtils.alert("请选择年!");
			return;
		}
		if(selectedRowData.getPeriod() == null) {
			MessageUtils.alert("请选择期间!");
			return;
		}
		try {
			doServiceSaveMaster(); // Master
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}
	
	@Bind
	public UIButton saveMaster;
//	@Bind
//	public UIButton delMaster;

	@Bind
	public UIButton add;

	@Bind
	public UIButton del;
	
	@Bind
	public UIButton add2;
	
	
	@Bind
	public UIButton saveAndAdd;
	
	
	@Bind
	public UIButton saveAndClose;
	
	
	@Bind
	public UIButton save;
	
	

	/**
	 * 控制按钮是否可用
	 * 
	 * @param isCheck
	 */
	private void disableAllButton(Boolean isCheck) {
		saveMaster.setDisabled(isCheck);
		//delMaster.setDisabled(isCheck);
		add.setDisabled(isCheck);
		del.setDisabled(isCheck);
		add2.setDisabled(isCheck);
		saveAndAdd.setDisabled(isCheck);
		saveAndClose.setDisabled(isCheck);
		save.setDisabled(isCheck);
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Bind
	public UIDataGrid gridAst;

	@SaveState
	@Accessible
	public Map<String, Object> qryMapAst = new HashMap<String, Object>();

	@Bind(id = "gridAst", attribute = "dataProvider")
	protected GridDataProvider getDataProvider1() {
//		if(this.astgridload==false){
//			return null;
//		}else{
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
		//}
	}
	//根据对应的核算项目类别提取核算代码
	public Map getQryClauseWhere1(Map<String, Object> queryMapAst) {
		Map m = super.getQryClauseWhere(queryMapAst);
		String qry = StrUtils.getMapVal(m, "qry");
		qry += "\nAND t.actsetid = " + this.actsetid;
		if (this.ddata.getActid()!=null){
			String arg="";
			act = new FsAct();
			act =  this.serviceContext.subjectMgrService.fsActDao.findById(this.ddata.getActid());
			if(act.getIsastypeb()==true){
				arg += ",'B'";
			}
			
			if(act.getIsastypec()==true){
				arg += ",'C'";
			}
			
			if(act.getIsastyped()==true){
				arg += ",'D'";
			}
			
			if(act.getIsastypee()==true){
				arg = ",'E'";
			}
			
			if(!StrUtils.isNull(arg)){
				arg = arg.substring(1);
				
			}else{
				
			}
			
			qry += "\nAND t.astypecode IN (" + arg+")";
			
		} else{
			qry += "\nAND t.astypecode = ''";
		}
		m.put("qry", qry);
		return m;
	}
	
	
	@Action
	public void astgridreload(){
		
//		if(this.astgridload == true){
//			
//		}else{
		//this.astgridload = true;
		this.gridAst.rebind();
		//}
	}

	@Action
	public void clearQryKeyAst() {
		if (qryMapAst != null) {
			qryMapAst.clear();

			update.markUpdate(true, UpdateLevel.Data, "gridPanel");
			this.qryRefreshAst();
		}
	}

	@Action
	public void qryRefreshAst() {
		this.gridAst.reload();
	}

	/**
	 * 上一条  点击上一条 下一条,需要检查,凭证是否平了,没有提示出来(优化写法 ,不取那么多出来)
	 */
	@Action
	public void pageUp() {
		String nos = this.selectedRowData.getNos();
		if(nos.contains("-")){
			nos =nos.split("-")[0];
		}
		if(StrUtils.isNull(nos)){
			nos ="0";
		}
		Long vchtypeid = this.selectedRowData.getVchtypeid();
		Integer nosnum=Integer.parseInt(nos);
		String whereSql = "SELECT id FROM _fs_vch_main WHERE isdelete = FALSE AND srctype != 'I' AND actsetid = "
			+ this.selectedRowData.getActsetid() + " AND year = "+this.selectedRowData.getYear()+" AND period = "+this.selectedRowData.getPeriod()+" AND nosnum <= "
			+nosnum + " AND vchtypeid = " + vchtypeid +" ORDER BY nosnum DESC,id DESC limit 10";
		List<Map> lists = this.daoIbatisTemplate.queryWithUserDefineSql(whereSql);
		Map<String, Long> datas = new HashMap<String, Long>();
		for (int i = 0; i < lists.size(); i++) {
			datas = lists.get(i);
			if (((Long)datas.get("id")).equals(this.mPkVal)) {
				if (i != (lists.size() - 1)) {
					this.mPkVal = (Long) lists.get(i + 1).get("id");
					this.refreshMasterForm();
					this.refresh();
					this.checkVch(this.mPkVal);
					return;
				} else {
					MessageUtils.alert("第一条！");
				}
			}
		}
	}

	/**
	 * 下一条
	 */
	@Action
	public void pageDown() {
		String nos = this.selectedRowData.getNos();
		if(nos.contains("-")){
			nos =nos.split("-")[0];
		}
		if(StrUtils.isNull(nos)){
			nos ="0";
		}
		Long vchtypeid = this.selectedRowData.getVchtypeid();
		Integer nosnum=Integer.parseInt(nos);
		String whereSql = "SELECT id FROM _fs_vch_main WHERE isdelete = FALSE AND srctype != 'I' AND actsetid = "
			+ this.selectedRowData.getActsetid() + " AND year = "+this.selectedRowData.getYear()+" AND period = "+this.selectedRowData.getPeriod()+" AND nosnum >= "
			+nosnum+ " AND vchtypeid = " + vchtypeid +" ORDER BY nosnum ,id  limit 10";
		List<Map> lists = this.daoIbatisTemplate.queryWithUserDefineSql(whereSql);
		Map<String, Long> datas = new HashMap<String, Long>();
		//Map<Integer, Long> datas = new HashMap<Integer, Long>();
		for (int i = 0; i < lists.size(); i++) {
			datas = lists.get(i);
			if (((Long)datas.get("id")).equals(this.mPkVal)) {
				
				if (i != (lists.size() - 1)) {
					this.mPkVal = (Long) lists.get(i + 1).get("id");
					this.refreshMasterForm();
					this.refresh();
					this.checkVch(this.mPkVal);
					return;
				} else {
					MessageUtils.alert("最后一条！");
				}
			}
		}
	}
	
	@Bind
	public UIDataGrid gridRate;

	@SaveState
	@Accessible
	public Map<String, Object> qryMapRate = new HashMap<String, Object>();

	@Bind(id = "gridRate", attribute = "dataProvider")
	protected GridDataProvider getDataProvider2() {
		if(this.lazyload==false){
			return null;
		}else{
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridRate.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere2(qryMapRate),
								start, limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridRate.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere2(qryMapRate));
				Long count = (Long) list.get(0).get("counts");
				return count.intValue();
			}
		};
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

	public Map getQryClauseWhere2(Map<String, Object> queryMapRate) {
		Map m = super.getQryClauseWhere(queryMapRate);
		String qry = StrUtils.getMapVal(m, "qry");

		qry += "\nAND t.actsetid = " +this.actsetid;
		qry += "\nAND yeardesc = '" + getCurrentYear() + "'";
		qry += "\nAND perioddesc = '" + getCurrentPeriod() + "'";
		m.put("qry", qry);
		return m;
	}
	
	@Bind
	public UIDataGrid gridAct;

	@SaveState
	@Accessible
	public Map<String, Object> qryMapAct = new HashMap<String, Object>();

	@Bind(id = "gridAct", attribute = "dataProvider")
	protected GridDataProvider getDataProvider3() {
		if(this.lazyload==false){
			return null;
		}else{
		return new GridDataProvider() {

			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridAct.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, getQryClauseWhere3(qryMapAct),
								start, limit).toArray();
			}

			@Override
			public int getTotalCount() {
				
				return 10000;
			}
		};
		}
	}

	public Map getQryClauseWhere3(Map<String, Object> queryMapAct) {
		Map m = super.getQryClauseWhere(queryMapAct);
		String qry = StrUtils.getMapVal(m, "qry");
		
		qry += "\nAND t.actsetid = " + this.actsetid;
		m.put("qry", qry);
		return m;
	}

	
	
	// 暂时注销 调用会出现很多bug
	// @Action
	// public void insertdtl(){
	// super.insert();
	// }
	
	public void setActsetid(){
		//新增进来 
		if(this.selectedRowData.getId()==0){
			String sql = "SELECT code||'/'||name AS actsetcode  FROM fs_actset WHERE isdelete = FALSE AND id = "
					+ AppUtils.getUserSession().getActsetid();
			Map m;
			
				try {
					m = this.serviceContext.daoIbatisTemplate
							.queryWithUserDefineSql4OnwRow(sql);
					this.actsetid = AppUtils.getUserSession().getActsetid();
					this.actsetcode=(String)m.get("actsetcode");
					//保存进来
				} catch (Exception e) {
					MessageUtils.alert("当前用户没有绑定帐套,不能新增凭证");
					return;
				}
				//查看明细... 或者保存以后 ... 
		}else{
			
			String sql = "SELECT code||'/'||name AS actsetcode  FROM fs_actset WHERE isdelete = FALSE AND id = "
				+ this.selectedRowData.getActsetid();
			Map m;
		
			try {
				m = this.serviceContext.daoIbatisTemplate
						.queryWithUserDefineSql4OnwRow(sql);
				this.actsetid = this.selectedRowData.getActsetid();
				this.actsetcode=(String)m.get("actsetcode");
				//保存进来
			} catch (Exception e) {
				MessageUtils.alert("当前用户凭证所在帐套已经删除,请检查!");
				return;
			}
			//查看明细... 或者保存以后 ... 
		}
		this.setBaseCurrency();
		update.markUpdate(true, UpdateLevel.Data, "actsetid");
		update.markUpdate(true, UpdateLevel.Data, "actsetcode");
	}
	
	//凭证字
	@Bind(id="vchtype")
    public List<SelectItem> getVchtype() {
		try {
			return CommonComBoxBean.getComboxItems("d.id","d.name","fs_vchtype AS d","WHERE d.actsetid="+this.actsetid,"ORDER BY d.code");
			//return CommonComBoxBean.getComboxItems("d.id","d.name","fs_vchtype AS d","WHERE d.actsetid="+this.actsetid,"and d.isdefault=true");		
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	
	//科目ID
	@Bind(id="actid")
    public List<SelectItem> getActid() {
		try {
			return CommonComBoxBean.getComboxItems("d.id","d.code||'/'||d.name ","fs_act AS d","WHERE d.isdelete = FALSE AND d.isinuse = TRUE AND d.actsetid="+this.actsetid,"ORDER BY CAST(d.code AS VARCHAR)");
		} catch (Exception e) {
			MessageUtils.showException(e);
			return null;
		}
    }
	/**
	 * 
	 * NEO 20150205
	 */
	public void setBaseCurrency(){
		//必须和新增一样 根据是否新增 或者 显示 进来 处理.否则会出现问题
		String sql = "SELECT year,period,basecy FROM fs_actset WHERE isdelete = FALSE AND id = "
				+ this.actsetid;
		Map m;
		try {
			m = this.serviceContext.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sql);
			year = m.get("year").toString();
			period = m.get("period").toString();
			basecy = m.get("basecy").toString();
			
		} catch (Exception e) {
			year = "";
			period = "";
			basecy = "CNY";
		}finally{
			this.update.markUpdate(UpdateLevel.Data, "basecy");
		}
	}

	/**
	 * 保存摘要
	 * neo
	 */
	@Action
	public void saveVchdesc() {
		try {
			doServiceSaveVchdesc(); // Vchdesc
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}

	private void doServiceSaveVchdesc() {
		try {
			this.vchdesc = new FsVchdesc();
			this.vchdesc.setActsetid(AppUtils.getUserSession().getActsetid());
			this.vchdesc.setName(ddata.getVchdesc());
			this.serviceContext.vchdescMgrService.saveData(vchdesc);
			MessageUtils.alert("OK");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 选择摘要
	 * neo
	 */
	@Action
	public void chooseVchdesc() {
		vchdescWindow.show();
		this.gridVchdesc.reload();
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
	public void gridVchdesc_ondblclick() {
		if (this.ddata.getVchdesc() == null) {
			vchdesc = new FsVchdesc();
		} else {
			vchdesc = serviceContext.vchdescMgrService.fsVchdescDao.
			findById(Long.valueOf(this.gridVchdesc
				.getSelectedIds()[0]));	
		}
		this.ddata.setVchdesc(vchdesc.getName());
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "vchdescid");
		vchdescWindow.close();
	
	}
	
	public void checkVch(Long vchid) {
		String sql = "SELECT f_fs_checkvch('actsetid="+AppUtils.getUserSession().getActsetid()+";vchid="+vchid+"') AS iseq;";
		Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String iseq = String.valueOf(m.get("iseq"));
		if("0".equals(iseq)) {
			MessageUtils.alert("当前凭证借贷方总金额不相等，请核对！");
		}
	}
	
	
	public String checkVchAdd(Long vchid) {
		String sql = "SELECT f_fs_checkvch('actsetid="+AppUtils.getUserSession().getActsetid()+";vchid="+vchid+"') AS iseq;";
		Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
		String iseq = String.valueOf(m.get("iseq"));
		return iseq;
	}
	
	/**
	 * 上一条 
	 */
	@Action
	public void prev() {
		Long vchtypeid = this.selectedRowData.getVchtypeid();
		String whereSql = "SELECT id FROM fs_vchdtl WHERE isdelete = FALSE AND actsetid = "
			+ this.selectedRowData.getActsetid() + "AND (CASE WHEN COALESCE(vdorder,0)=0 THEN id < "+this.pkVal+" ELSE vdorder < (SELECT x.vdorder FROM fs_vchdtl x where id = "+this.pkVal+") END) AND vchid = "+this.mPkVal+" ORDER BY vdorder DESC,id DESC limit 1";
		try {
			Map map = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(whereSql);
			this.pkVal = (Long) map.get("id");
			this.refreshForm();
			return;
		} catch (NoRowException e) {
			MessageUtils.alert("第一条！");
			e.printStackTrace();
		}
	}

	/**
	 * 下一条
	 */
	@Action
	public void next() {
		Long vchtypeid = this.selectedRowData.getVchtypeid();
		String whereSql = "SELECT id FROM fs_vchdtl WHERE isdelete = FALSE AND actsetid = "
			+ this.selectedRowData.getActsetid() + "AND (CASE WHEN COALESCE(vdorder,0)=0 THEN id > "+this.pkVal+" ELSE vdorder > (SELECT x.vdorder FROM fs_vchdtl x where id = "+this.pkVal+") END) AND vchid = "+this.mPkVal+" ORDER BY vdorder,id limit 1";
		try {
			Map map = this.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(whereSql);
			this.pkVal = (Long) map.get("id");
			this.refreshForm();
			return;
		} catch (NoRowException e) {
			MessageUtils.alert("最后一条！");
			e.printStackTrace();
		}
	}
}

