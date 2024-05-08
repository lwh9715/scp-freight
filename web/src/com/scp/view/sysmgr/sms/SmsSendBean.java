package com.scp.view.sysmgr.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.scp.model.sys.SysSms;
import com.scp.service.sysmgr.SysSmsService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.sms.smssendBean", scope = ManagedBeanScope.REQUEST)
public class SmsSendBean extends GridView {

	@ManagedProperty("#{sysSmsService}")
	public SysSmsService sysSmsService;
	
	@Bind
	@SaveState
	public String sendnos;
	
	@Bind
	public String content;
	
	@Bind
	public String tips = "温馨提示:\n1.多个号码群发可以使用回车区分\n2.短信服务为预付费，请勿发送与工作无关信息\n3.请勿发送任何不和谐信息\n4.内容按65字符/条";
	
	@Bind
	public UIWindow addresslistWindow;
	
	@Bind
	public UIWindow smsHisWindow;
	
	@Bind
	public UIWindow importContexTempletWindow;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String mobilephone = AppUtils.getReqParam("mobilephone");
		if(!"null".equals(mobilephone)) {
			this.sendnos = mobilephone;
		}
	}

	@Action
	public void showAddresslist(){
		addresslistWindow.show();
		this.grid.reload();
	}
	
	@Action
	public void showImportHis(){
		smsHisWindow.show();
	}
	
	@Action
	public void showImportContexTemplet() {
		importContexTempletWindow.show();
	}
	
	@Action
	public void importAddsPhoneNos(){
		String[] ids = this.grid.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String nos = sysSmsService.findAddressPhoneNos(id);
		this.sendnos = StrUtils.isNull(this.sendnos) ? nos: (this.sendnos+"\n"+ nos);
		this.update.markUpdate(UpdateLevel.Data, "sendnos");
	}
	
	
	
	@Action
	public void openUsrAddres(){
		String winId = "_UsrAddressMgr";
		String url = "../addresslist/usraddress.xhtml";
		int width = 1024;
		int height = 740;
		AppUtils.openWindow(winId, url);
	}
	
	@Action
	public void importHis(){
		String[] ids = this.gridHis.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String[] nos = sysSmsService.findHisSms(id);
//		this.sendnos = nos[0];
		this.content = nos[1];
//		this.update.markUpdate(UpdateLevel.Data, "sendnos");
		this.update.markUpdate(UpdateLevel.Data, "content");
	}
	
	@Action
	public void importHisWithPhones(){
		String[] ids = this.gridHis.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String[] nos = sysSmsService.findHisSms(id);
		this.sendnos = nos[0];
		this.content = nos[1];
		this.update.markUpdate(UpdateLevel.Data, "sendnos");
		this.update.markUpdate(UpdateLevel.Data, "content");
	}
	
	
	@Action
	public void send(){
		if(StrUtils.isNull(sendnos)){
			MessageUtils.alert("send no is null!");
			return;
		}
		
		if(StrUtils.isNull(content)){
			MessageUtils.alert("Msg content is null!");
			return;
		}
		String nos[] = sendnos.split("\n");
		
		List<SysSms> datas = new ArrayList<SysSms>();
		for (int i = 0; i < nos.length; i++) {
			String mobiles = nos[i];
			if(!StrUtils.isMobileNO(mobiles)) {
				MessageUtils.alert("第"+(i+1)+"个号码不合法，请检查!");
				return ;
			}
			SysSms data = new SysSms();
			data.setPhone(mobiles);
			
			data.setContent(content);
			datas.add(data);
		}
		
		try {
			sysSmsService.saveDatas(datas);
			MessageUtils.alert("OK!");
			sendnos = "";
			content = "";
			this.update.markUpdate(UpdateLevel.Data, "sendnos");
			this.update.markUpdate(UpdateLevel.Data, "content");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid gridHis;

	@Bind(id = "gridHis", attribute = "dataProvider")
	protected GridDataProvider getGridHisDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridHis.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMapExt), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridHis.count";
				qryMapExt.put("inputer", AppUtils.getUserSession().getUsercode());
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere(qryMapExt));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	public Map<String, Object> qryMapExt = new HashMap<String, Object>();
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	public Map<String, Object> qryMapExt2 = new HashMap<String, Object>();
	
	/**
	 * 页面对应的queryMap
	 */
	@SaveState
	public Map<String, Object> qryMapExt3 = new HashMap<String, Object>();
	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid gridTemplet;

	@Bind(id = "gridTemplet", attribute = "dataProvider")
	protected GridDataProvider getGridTempletDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".gridTemplet.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getQryClauseWhere(qryMapExt3), start , limit ).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = getMBeanName() + ".gridTemplet.count";
				qryMapExt2.put("inputer", AppUtils.getUserSession().getUsercode());
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId , getQryClauseWhere(qryMapExt3));
				Long count = (Long)list.get(0).get("counts");
				return count.intValue();
			}
		};
	}
	
	
	@Action
	public void importTemplet(){
		String[] ids = this.gridTemplet.getSelectedIds();
		if(ids == null || ids.length <=0) {
			MessageUtils.alert("Please choose one!");
			return;
		}
		String id = StrUtils.array2List(ids);
		String nos = sysSmsService.findTemplet(id);
		this.content = nos;
		this.update.markUpdate(UpdateLevel.Data, "content");
	}
	
	
	@Action
	public void openTempletMgr(){
		String winId = "_TempletMgr";
		String url = "../addresslist/template.xhtml";
		int width = 1024;
		int height = 740;
		AppUtils.openWindow(winId, url);
	}

	@Override
	public void qryRefresh() {
		if(grid != null){
			this.grid.reload();
		}
		if(gridHis != null){
			this.gridHis.reload();
		}
		if(gridTemplet != null){
			this.gridTemplet.reload();
		}
	}
	
	
	
}
