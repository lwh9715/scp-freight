package com.scp.view.module.finance.initconfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIPanel;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.exception.LoginException;
import com.scp.model.finance.fs.FsActset;
import com.scp.model.sys.SysUser;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerChooseBean;
import com.scp.vo.Period;
import com.scp.vo.Xrate;

@ManagedBean(name = "pages.module.finance.initconfig.accountBean", scope = ManagedBeanScope.REQUEST)
public class AccountBean extends GridFormView {

	@ManagedProperty("#{customerchooseBean}")
	private CustomerChooseBean customerService;

	@SaveState
	@Accessible
	public FsActset selectedRowData = new FsActset();

	@SaveState
	@Accessible
	public Period period = new Period();

	@SaveState
	@Accessible
	public Xrate fsperiod = new Xrate();

	@Bind
	@SaveState
	@Accessible
	public Long actSetid;

	@Bind
	@SaveState
	@Accessible
	public Long actSetiddel;

	@Bind
	@SaveState
	@Accessible
	public String[] corpid;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;

	public void init() {
		actSetid = AppUtils.getUserSession().getActsetid();
		update.markUpdate(true, UpdateLevel.Data, "actSetid");
	}

	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			// 设置帐套名称
			try {
				actsetcode=AppUtils.getActsetcode();
				actSetid = AppUtils.getUserSession().getActsetid();
				update.markUpdate(true, UpdateLevel.Data, "actSetid");
			} catch (FsActSetNoSelectException e) {
			}
		}

	};

	@Override
	public void refresh() {
		super.refresh();
		update.markUpdate(true, UpdateLevel.Data, "actSetid");
	}

	@Override
	public void grid_ondblclick() {
		chooseAct();
	}

	@Override
	public void save() {
		serviceContext.accountsetMgrService.saveData(selectedRowData);
		this.alert("OK");
		this.refresh();
	}

	@Override
	protected void doServiceFindData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doServiceSave() {
		// TODO Auto-generated method stub

	}

	@Action
	public void delBatch() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		}
		try {
			serviceContext.customerContactsMgrService.delBatch(ids);
			MessageUtils.alert("OK");
			refresh();
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}

	}

	@Action
	public void chooseAct() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		} else if (ids.length > 1) {
			MessageUtils.alert("只能勾选一个帐套");
			return;
		}
//		if (isLimits(ids) == true) {
			try {
				SysUser user = new SysUser();
				user = serviceContext.userMgrService.sysUserDao
						.findById(AppUtils.getUserSession().getUserid());
				Long actsetid = Long.valueOf(ids[0]);
				user.setActsetid(actsetid);
				serviceContext.userMgrService.saveUser(user);
				AppUtils.getUserSession().setActsetid(actsetid);
				MessageUtils.alert("已切换账套，请关闭财务部分已经打开的模块后再操作!");
				actSetid = actsetid;
				//Browser.execClientScript("parent.location.reload();");			
				this.grid.reload();
				actsetcode=AppUtils.getActsetcode();
				update.markUpdate(true, UpdateLevel.Data, "actsetcode");
				
			} catch (Exception e) {
				MessageUtils.showException(e);
				return;
			}
//		}
	}

	@Override
	public void add() {
		selectedRowData = new FsActset();
		// 设置为总部的id
		selectedRowData.setCorpid(100L);
		selectedRowData.setLen1((short) 4);
		selectedRowData.setLen2((short) 2);
		selectedRowData.setLen3((short) 2);
		selectedRowData.setLen4((short) 2);
		selectedRowData.setLen5((short) 2);
		selectedRowData.setLen6((short) 2);
		selectedRowData.setPeriod((short) 1);
		String[] data = this.getDateforYearAndDay();
		selectedRowData.setStartyear(Short.valueOf(data[0]));
		selectedRowData.setStartperiod(Short.valueOf(data[1]));
		selectedRowData.setBasecy("CNY");
		selectedRowData.setName(this.getComforuserid());
		editWindow1.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel1");
		// companyGrid.reload();

	}

	@Bind
	public UIWindow editWindow1;

	@Bind
	public UIPanel editPanel1;

	@Bind
	public UIWindow editWindow2;

	@Bind
	public UIPanel editPanel2;

	@Bind
	public UIWindow editWindow3;

	@Bind
	public UIPanel editPanel3;

	@Action
	public void back1() {

	}

	@Action
	public void go1() {
		editWindow1.close();
		editWindow3.show();
		update.markUpdate(true, UpdateLevel.Data, "editPanel2");
	}

	@Action
	public void cancel1() {
		editWindow1.close();
	}

	// @Action
	// public void back2(){
	// editWindow2.close();
	// editWindow1.show();
	// update.markUpdate(true,UpdateLevel.Data,"editPanel");
	// }
	//	
	// @Action
	// public void go2(){
	// editWindow2.close();
	// editWindow3.show();
	// update.markUpdate(true, UpdateLevel.Data, "editPanel3");
	// }
	//	
	// @Action
	// public void cancel2(){
	// editWindow2.close();
	// }

	@Action
	public void back3() {
		editWindow3.close();
		editWindow1.show();
		update.markUpdate(true, UpdateLevel.Data, "editPane2");
	}

	@Bind
	public UIDataGrid actGrid;

	@Bind(id = "actGrid", attribute = "dataProvider")
	public GridDataProvider getActGridDataProvider() {
		return customerService.getActDataProvider();
	}

	@Action
	public void cancel3() {
		editWindow3.close();
	}

	// ok
	@Action
	public void go3() {
		// corpid=this.companyGrid.getSelectedIds();
		// if (corpid == null || corpid.length == 0) {
		// MsgUtil.alert("请先勾选公司");
		// return;
		// }else if(corpid.length >1){
		// MsgUtil.alert("只能勾选一个公司");
		// return;
		// }
		// 保存对象
		if (StrUtils.isNull(selectedRowData.getCode())
				|| StrUtils.isNull(selectedRowData.getName())
				|| StrUtils.isNull(selectedRowData.getYear() + "")
				|| StrUtils.isNull(selectedRowData.getBasecy())) {
			MessageUtils.alert("帐套代码,名称,年,本位币不能为空,请检查");
			return;
		}
		try {
			selectedRowData.setYear(selectedRowData.getStartyear());
			selectedRowData.setPeriod(selectedRowData.getStartperiod());
			serviceContext.accountsetMgrService.saveData(selectedRowData);
			this.pkVal = selectedRowData.getId();
			// ApplicationUtils.debug(this.pkVal);
			this.refresh();
			// 调取初始化函数
			String sql = "SELECT f_auto_filldata_act('actsetid=" + this.pkVal
					+ "');";
			serviceContext.userMgrService.sysUserDao.executeQuery(sql);
			MessageUtils.alert("该帐套初始化OK,接下来请设置兑换率!");
			this.selectedRowData.setIsinitok(true);
			serviceContext.accountsetMgrService.saveData(selectedRowData);
			editWindow3.close();
			// 弹出设置兑换率窗口
			setrate(this.pkVal);
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}

	public void setrate(Long actsetid) {
		String url = "./exchangerate.xhtml?actsetid=" + actsetid;
		dtlIFrame.setSrc(url);
		update.markAttributeUpdate(dtlIFrame, "src");
		update.markUpdate(true, UpdateLevel.Data, dtlDialog);
		dtlDialog.show();
	}

	@Bind
	private UIWindow dtlDialog;
	@Bind
	private UIIFrame dtlIFrame;

	@Action(id = "dtlDialog", event = "onclose")
	private void dtlEditDialogClose() {
		refresh();
	}

	@Bind
	public UIWindow editWindowDel;

	@Bind
	public UIPanel editPanelDel;

	@Bind
	public String usercode1;

	@Bind
	public String password1;

	@Bind
	public String usercode2;

	@Bind
	public String password2;

	// 删除逻辑
	@Action
	public void delactset() {
		String[] ids = this.grid.getSelectedIds();
		if (ids == null || ids.length == 0) {
			MessageUtils.alert("请先勾选要修改的行");
			return;
		} else if (ids.length > 1) {
			MessageUtils.alert("只能勾选一个帐套");
			return;
		}

		editWindowDel.show();
		this.actSetiddel = Long.valueOf(ids[0]);
		update.markUpdate(true, UpdateLevel.Data, "actSetiddel");
		update.markUpdate(true, UpdateLevel.Data, "editPanelDel");
	}

	@Action
	public void confirmdel() {
		try {
			// 验证密码
			serviceContext.loginService.loginView(usercode1, password1);
			serviceContext.loginService.loginView(usercode2, password2);
			String sql = "SELECT f_fs_actset_del('id=" + this.actSetiddel + "');";
			this.serviceContext.accountMgrService.datAccountDao.executeQuery(sql);
			MessageUtils.alert("delete OK!");
			this.refresh();
		} catch (LoginException e) {
			MessageUtils.alert(e.getMessage());
			return;
		} catch (Exception e) {
			MessageUtils.showException(e);
		}

	}

	@Action
	public void canceldel() {
		editWindowDel.close();
		this.refresh();
	}

	public String[] getDateforYearAndDay() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
		String data = sdf.format(date);
		return data.split(",");
	}

	/**
	 * 根据当前用户寻找所在分公司名称
	 */
	public String getComforuserid() {
		String sql = "SELECT t.abbr FROM sys_user s,sys_corporation t WHERE s.id = "
				+ AppUtils.getUserSession().getUserid()
				+ " AND  s.corpid = t.id AND t.isdelete = FALSE AND s.isdelete = FALSE";
		Map m = serviceContext.daoIbatisTemplate
				.queryWithUserDefineSql4OnwRow(sql);
		return m.get("abbr").toString();

	}

//	/**
//	 * 判断是否有帐套权限
//	 */
//	public boolean isLimits(String[] ids) {
//		String sql = "SELECT actsetid FROM fs_actsetctrl WHERE userid = "
//				+ AppUtil.getUserSession().getUserid();
//		String message = "";
//		int ii =0;
//		//System.out.print(AppUtil.getUserSession().getUserid());
//			List list = serviceContext.accountMgrService.datAccountDao
//					.executeQuery(sql);
//			if (list.size()>0 &&list != null) {
//				for (String id : ids) {
//					for (int i = 0; i < list.size(); i++) {
//						if (((BigInteger) list.get(i)).equals(BigInteger.valueOf(Long.valueOf(id)))) {
//							  			ii++;
//						}
//					}
//				}
//				if (ii==0) {
//					MsgUtil.alert("您尚未授予" + getCorperName(ids[0]) + "帐套权限使用,请联系管理员授予!");
//					return false;
//				}
//			} else {
//				MsgUtil.alert("您尚未有帐套权限,请联系管理员授予!");
//				return false;
//			}
//		return true;
//	}
//
//	public String getCorperName(String actsetid) {
//		String sql = "SELECT  (SELECT COALESCE(x.code,'') ||'/'|| COALESCE(x.abbr,'') FROM sys_corporation x where x.id = s.corpid) AS corper "
//				+ "\nFROM fs_actset s WHERE s.isdelete = FALSE AND id = "
//				+ actsetid;
//		Map map = serviceContext.daoIbatisTemplate
//				.queryWithUserDefineSql4OnwRow(sql);
//		return (String) map.get("corper");
//	}

	// @Bind
	// public UIDataGrid companyGrid;
	//	
	// @Bind(id = "companyGrid", attribute = "dataProvider")
	// public GridDataProvider getCompanyGridDataProvider() {
	// return customerService.getCompanyDataProvider();
	// }
	//	

	// //window1 日期类型
	// @Bind
	// @SaveState
	// @Accessible
	// public String datetype;
	//	
	// //window1 开始时间
	// @Bind
	// @SaveState
	// @Accessible
	// public Date starttime ;

	// @Bind
	// public UIDataGrid periodGrid;
	//	
	// @Bind(id = "periodGrid", attribute = "dataProvider")
	// public GridDataProvider getPeriodGridDataProvider() {
	//		
	// return customerService.getPeriodDataProvider();
	// }
	//	
	//	
	// @Bind
	// public UIEditDataGrid xrateGrid;
	//	
	// @Bind(id = "xrateGrid", attribute = "addedData")
	// protected Object addedData;
	// @Bind(id = "xrateGrid", attribute = "modifiedData")
	// protected Object modifiedData;
	// @Bind(id = "xrateGrid", attribute = "removedData")
	// protected Object removedData;
	//	
	//	
	// @Bind(id = "xrateGrid", attribute = "dataProvider")
	// public GridDataProvider getXrateDataProvider() {
	// return customerService.getXrateDataProvider();
	// }
	//	
	// //window1 生成期间1
	// @Action
	// public void crateperiod(){
	//		
	// periodGrid.reload();
	// }

	// 加载两个load

	// @Bind(id = "periodGrid")
	// private UIDataGrid periodGrid;
	//
	// @Bind(id = "xrateGrid")
	// private UIEditDataGrid xrateGrid;
	//    
	// @Bind(id = "xrateGrid", attribute = "addedData")
	// protected Object addedData;
	// @Bind(id = "xrateGrid", attribute = "modifiedData")
	// protected Object modifiedData;
	// @Bind(id = "xrateGrid", attribute = "removedData")
	// protected Object removedData;
	//    
	// @SaveState
	// private int periodid;
	//
	//    
	//    
	// @Bind(id = "periodGrid")
	// private List<Period> getPeriods() {
	// return serviceContext.periodXrateService.findAllPeriod();
	// }
	//
	// @Bind(id = "xrateGrid")
	// private List<Xrate> getXrates() {
	// return serviceContext.periodXrateService.findByPeroid(periodid);
	// }

	// @Action(id = "periodGrid", event = "onrowselect")
	// public void periodGrid_onrowselect() {
	// //Object[] periods =periodGrid.getSelectedValues();
	// Object[] periods =this.periodGrid.getSelectedValues();
	// if (periods.length > 0) {
	// Period peroid = (Period) periods[0];
	// periodid = peroid.getId();
	// }
	// xrateGrid.reload();
	// }

	// @Action(id = "periodGrid", event = "onrowselect")
	// public void periodGrid_onrowselect() {
	// //Object[] periods =periodGrid.getSelectedValues();
	// Object[] periods =this.periodGrid.getSelectedValues();
	// if (periods.length > 0) {
	// Period peroid = (Period) periods[0];
	// periodid = peroid.getId();
	// }
	// xrateGrid.setSelections(null);
	// xrateGrid.reload();
	// }

	// @Action
	// public void periodGrid_ondblclick(){
	// periodid= 1;
	// xrateGrid.setSelections(null);
	// xrateGrid.reload();
	//    	
	// }

}
