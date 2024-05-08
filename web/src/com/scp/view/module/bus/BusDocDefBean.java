package com.scp.view.module.bus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.component.html.HtmlOutputLabel;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICheckBox;
import org.operamasks.faces.component.form.impl.UIDateField;
import org.operamasks.faces.component.form.impl.UITextArea;
import org.operamasks.faces.component.form.impl.UITextField;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.UICell;
import org.operamasks.faces.component.layout.UIPanelGrid;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.component.widget.UIButton;
import org.operamasks.faces.component.widget.UIForm;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.MultiLanguageBean;
import com.scp.model.bus.BusDocdef;
import com.scp.util.AppUtils;
import com.scp.util.KuaiDi100QueryAPI;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.bus.busdocdefBean", scope = ManagedBeanScope.REQUEST)
public class BusDocDefBean extends GridView {

	@SaveState
	@Accessible
	public BusDocdef selectedRowData = new BusDocdef();
	
	@Bind
	@SaveState
	public String usernamec;
	
	@Bind
	@SaveState
	public String msg;
	
	@Bind
	public UIWindow msgWindow;
	
	@Bind
	@SaveState
	@Accessible
	public String ids = "";
	
	//字段名定义
	private final String fromTag = "autofrom";
	private final String stateTag = "state_";
	private final String expcorpTag = "expcorp_";
	private final String queryTag = "query_";
	private final String expnoTag = "expno_";
	private final String resultTag = "result_";
	private final String senderTag = "sender_";
	private final String sendateTag = "sendate_";
	private final String markTag = "mark_";
	private final String getdateTaq = "getdate_";
	private final String filenameTag = "filename_";
	
	
	
	@Bind
	@SaveState
	public Long pkVal = -1L;
	
	
	@Bind(id="autofrom")
	public UIForm from;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if (!isPostBack) {
			String id =AppUtils.getReqParam("linkid").trim();
			if(!StrUtils.isNull(id)){
				this.pkVal=Long.valueOf(id);
				//qryMap.put("linkid$", this.shipid);
				update.markUpdate(UpdateLevel.Data,"pkVal");
			}
			initView();
		}
	}
	
	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	public void initView(){
		UIPanelGrid panelGrid = new UIPanelGrid();
		panelGrid.setColumns(10);
		
		//初始化头部
		HtmlOutputLabel label_state = new HtmlOutputLabel();
		label_state.setValue((String)l.m.get("状态"));
		HtmlOutputLabel label_namec = new HtmlOutputLabel();
		label_namec.setValue((String)l.m.get("名称")+"(CN)");
		HtmlOutputLabel label_getdate = new HtmlOutputLabel();
		label_getdate.setValue((String)l.m.get("取得日期"));
		HtmlOutputLabel label_filename = new HtmlOutputLabel();
		label_filename.setValue((String)l.m.get("文件标题"));
		HtmlOutputLabel label_expcorp = new HtmlOutputLabel();
		label_expcorp.setValue((String)l.m.get("快递公司"));
		HtmlOutputLabel label_expno = new HtmlOutputLabel();
		label_expno.setValue((String)l.m.get("快递单号"));
		HtmlOutputLabel label_expresult = new HtmlOutputLabel();
		label_expresult.setValue((String)l.m.get("状态跟踪"));
		HtmlOutputLabel label_sender = new HtmlOutputLabel();
		label_sender.setValue((String)l.m.get("寄单人"));
		HtmlOutputLabel label_sendate = new HtmlOutputLabel();
		label_sendate.setValue((String)l.m.get("寄单日期"));
		
		
		HtmlOutputLabel label_mark = new HtmlOutputLabel();
		label_mark.setValue((String)l.m.get("备注"));
		
		panelGrid.getChildren().add(label_state);
		panelGrid.getChildren().add(label_namec);
		panelGrid.getChildren().add(label_getdate);
		panelGrid.getChildren().add(label_filename);
		panelGrid.getChildren().add(label_expcorp);
		panelGrid.getChildren().add(label_expno);
		panelGrid.getChildren().add(label_expresult);
		panelGrid.getChildren().add(label_sender);
		panelGrid.getChildren().add(label_sendate);
		panelGrid.getChildren().add(label_mark);
		
		//初始化数据
		List<BusDocdef> docdefs = this.serviceContext.busDocdefMgrService.getDocdefByShipId(this.pkVal);
		int index = 0;
		for (BusDocdef busDocdef : docdefs) {
			
			UICheckBox state_body = new UICheckBox();
			state_body.setChecked(busDocdef.getIschoose());
			state_body.setJsvar(stateTag + busDocdef.getId());
			state_body.setId(stateTag + busDocdef.getId());
			state_body.setOncheck("checkedbox('"+busDocdef.getId()+"','"+fromTag+":"+stateTag+"','"+fromTag+":"+getdateTaq+"','"+fromTag+":"+filenameTag+"','"+fromTag+":"+expnoTag+"','"+fromTag+":"+senderTag+"','"+fromTag+":"+sendateTag+"');");
			
			HtmlOutputLabel namec_body = new HtmlOutputLabel();
			namec_body.setValue((String)l.m.get(busDocdef.getNamec()));
			
			UIDateField getdate_body = new UIDateField();
			getdate_body.setWidth(90);
			getdate_body.setValue(busDocdef.getGetdate());
			getdate_body.setJsvar(getdateTaq + busDocdef.getId());
			getdate_body.setId(getdateTaq + busDocdef.getId());
			
			UITextField filename_body = new UITextField();
			filename_body.setWidth(100);
			filename_body.setValue(busDocdef.getFilename());
			filename_body.setJsvar(filenameTag + busDocdef.getId());
			filename_body.setId(filenameTag + busDocdef.getId());
			
			UITextField expcorp_body = new UITextField();
			expcorp_body.setWidth(90);
			expcorp_body.setSelectOnFocus(true);
			expcorp_body.setValue(busDocdef.getExpcorp());
			expcorp_body.setJsvar(expcorpTag + busDocdef.getId());
			expcorp_body.setId(expcorpTag + busDocdef.getId());
			expcorp_body.setEmptyText((String)l.m.get("点击快捷输入"));
			expcorp_body.setOnfocus("openExpcorpWindow('"+expcorpTag+busDocdef.getId()+"');");
			
			UITextField expno_body = new UITextField();
			expno_body.setWidth(150);
			expno_body.setValue(busDocdef.getExpno());
			expno_body.setJsvar(expnoTag + busDocdef.getId());
			expno_body.setId(expnoTag + busDocdef.getId());
			
			UIButton queryBtn = new UIButton();
			queryBtn.setValue((String)l.m.get("查快递"));
			queryBtn.setOnclick("quertByExpcorp('"+busDocdef.getExpcorp()+"','"+busDocdef.getExpno()+"')");
			queryBtn.setId(queryTag+busDocdef.getId());
			
			
			UIPanelGrid expresult_pg = new UIPanelGrid();
			expresult_pg.setColumns(2);
			expresult_pg.getChildren().add(queryBtn);
			
			UICell expresult_cell = new UICell();
			expresult_cell.setColspan(1);
			expresult_cell.getChildren().add(expresult_pg);
			
			UITextField sender_body = new UITextField();
			sender_body.setWidth(100);
			sender_body.setValue(busDocdef.getSender());
			sender_body.setJsvar(senderTag + busDocdef.getId());
			sender_body.setId(senderTag + busDocdef.getId());
			
			UIDateField sendate_body = new UIDateField();
			sendate_body.setWidth(90);
			sendate_body.setValue(busDocdef.getSendate());
			sendate_body.setJsvar(sendateTag + busDocdef.getId());
			sendate_body.setId(sendateTag + busDocdef.getId());
			
			UITextArea mark_body = new UITextArea();
			mark_body.setWidth(300);
			mark_body.setValue(busDocdef.getMark());
			mark_body.setJsvar(markTag + busDocdef.getId());
			mark_body.setId(markTag + busDocdef.getId());
			
			panelGrid.getChildren().add(state_body);
			panelGrid.getChildren().add(namec_body);
			panelGrid.getChildren().add(getdate_body);
			panelGrid.getChildren().add(filename_body);
			panelGrid.getChildren().add(expcorp_body);
			panelGrid.getChildren().add(expno_body);
			panelGrid.getChildren().add(expresult_cell);
			panelGrid.getChildren().add(sender_body);
			panelGrid.getChildren().add(sendate_body);
			panelGrid.getChildren().add(mark_body);
			
			ids += busDocdef.getId() + ",";
		}
		if(ids.endsWith(",")){
			ids = ids.substring(0, ids.length()-1);
		}
		this.usernamec = AppUtils.getUserSession().getUsername();
		update.markUpdate(UpdateLevel.Data, "usernamec");
		update.markUpdate(true,UpdateLevel.Data, "ids");
		from.getChildren().add(panelGrid);
		from.setGroupId("f1");
	}
	
	@Action
	public void reloadGrid(){
		expcorpGrid.reload();
	}
	
	@Bind
	public UIDataGrid expcorpGrid;

	@SaveState
	@Accessible
	public Map<String, Object> qryExpcorp = new HashMap<String, Object>();
	
	@Bind(id = "expcorpGrid", attribute = "dataProvider")
	protected GridDataProvider getInvoiceDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = "pages.module.bus.busdocdefBean.expcorpGrid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, qryExpcorp, start,
								limit).toArray();
			}

			@Override
			public int getTotalCount() {
				String sqlId = "pages.module.bus.busdocdefBean.expcorpGrid.count";
				List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
						.queryForList(sqlId, qryExpcorp);
				if (list == null || list.size() < 1){
					return 0;
				}
				String countStr = list.get(0).get("counts").toString();
				Long count = Long.parseLong(StrUtils.isNull(countStr)?"0":countStr);
				return count.intValue();
			}
		};
	}
	
	@Action
	public void save() {
		Vector<String> batchsql = new Vector<String>();
		StringBuffer stringBuffer = new StringBuffer();
		String[] id = ids.split(",");
//		try {
			for (String s : id) {
				String state = AppUtils.getHttpServletRequest().getParameter(fromTag+":"+stateTag+s);
				String expno = AppUtils.getHttpServletRequest().getParameter(fromTag+":"+expnoTag+s);
				String getdate = AppUtils.getHttpServletRequest().getParameter(fromTag+":"+getdateTaq+s);
				String filename = AppUtils.getHttpServletRequest().getParameter(fromTag+":"+filenameTag+s);
				String sender = AppUtils.getHttpServletRequest().getParameter(fromTag+":"+senderTag+s);
				String sendate = AppUtils.getHttpServletRequest().getParameter(fromTag+":"+sendateTag+s);
				String expcorp = AppUtils.getHttpServletRequest().getParameter(fromTag+":"+expcorpTag+s);
				String mark = AppUtils.getHttpServletRequest().getParameter(fromTag+":"+markTag+s);
				
				if(state != null && "on".equals(state)){
					if(sendate != null && sendate.length() > 0){
						stringBuffer.append("\nUPDATE bus_docdef SET ischoose = TRUE,expno = '"+expno+"',sender = '"+sender+"',sendate = '"+sendate+"',expcorp = '"+expcorp+"',mark = '" + mark + "'" + (StrUtils.isNull(getdate)?",getdate = NULL":",getdate = '"+getdate+"'") + ",filename = '"+filename+"' WHERE isdelete = FALSE AND linkid = "+this.pkVal+" AND id = "+s+";");
					}else{
						stringBuffer.append("\nUPDATE bus_docdef SET ischoose = TRUE,expno = '"+expno+"',sender = '"+sender+"',sendate = NULL,expcorp = '"+expcorp+"',mark = '" + mark + "'" + (StrUtils.isNull(getdate)?",getdate = NULL":",getdate = '"+getdate+"'") + ",filename = '"+filename+"' WHERE isdelete = FALSE AND linkid = "+this.pkVal+" AND id = "+s+";");
					}
				}else{
					stringBuffer.append("\nUPDATE bus_docdef SET ischoose = FALSE,expno = '',sender = '',sendate = NULL,expcorp = NULL,mark = NULL WHERE isdelete = FALSE AND linkid = "+this.pkVal+" AND id = "+s+";");
				}
			}
			String sql = stringBuffer.toString();
			////System.out.println(sql);
			if(!StrUtils.isNull(sql)){
				this.serviceContext.userMgrService.daoIbatisTemplate.updateWithUserDefineSql(sql);
			}
//		} catch (SQLException e) {
//			MessageUtils.showException(e);
//		}
		Browser.execClientScript("refre();");	
		Browser.execClientScript("showmsg()");
	}
	
	@Action
	public void queryBtn(){
		String corp = AppUtils.getReqParam("corp");
		String no = AppUtils.getReqParam("no");
		if(StrUtils.isNull(corp)||StrUtils.isNull(no)){
			MessageUtils.alert((String)l.m.get("快递公司和单号都不能为空"));
			return;
		}
		String string = corp==null?corp:"-1";
		String code = serviceContext.filedataMgrService.findExCodeByExcorp(corp);
		if(!StrUtils.isNull(code)){
			KuaiDi100QueryAPI api = new KuaiDi100QueryAPI();
			try {
				msg = api.queryByJson(code, no);
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtils.showException(e);
			}
			if(msgWindow!=null){
				msgWindow.show();
				////System.out.println(sbmsg.toString());
				update.markUpdate(UpdateLevel.Data, "msg");
			}
		}else{
			String s = l.m.get("未找到此快递公司对应代码,请联系系统管理员").toString();
			MessageUtils.alert(s);
		}
	}
	
	
//	@Override
//	public void editGrid_onrowselect() {
//		super.editGrid_onrowselect();
//		String[] ids = this.editGrid.getSelectedIds();
//		for (String id : ids) {
//			String js = "setGridData("+id+" , '"+AppUtils.getUserSession().getUsercode()+"');";
//			Browser.execClientScript(js);
//		}
//	}
	
	
//	@Override
//	public int[] getGridSelIds() {
//		
//		String sql;
//		if("-1".equals(shipid)){
//			return null;
//		}else{
//			sql = 
//				"\nselect " +
//				"\n ischoose AS flag"+
//				"\nFROM _bus_docdef a " +
//				"\nWHERE a.isdelete = false AND a.linkid = "+shipid+
//				"\nORDER BY a.code";
//		}
//		try {
//			List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
//			List<Integer> rowList = new ArrayList<Integer>();
//			int rownum = 0;
//			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//				Map map = (Map) iterator.next();
//				if((Boolean)map.get("flag"))rowList.add(rownum);
//				rownum++;
//			}
//			int row[] = new int[rowList.size()];
//			for (int i = 0; i < rowList.size(); i++) {
//				row[i] = rowList.get(i);
//			}
//			return row;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	/**
//	 * 一个都没有勾选,全部设置为false
//	 */
//	@Override
//	public void save() {
//		serviceContext.busDocdefMgrService.saveDtlDatas(modifiedData);
//		String[] ids = this.editGrid.getSelectedIds();
//		String dmlSql = "\nUPDATE bus_docdef SET ischoose = false WHERE linkid = "+shipid+";";
//		for (int i = 0; i < ids.length; i++) {
//			dmlSql += "\nUPDATE bus_docdef SET ischoose = true WHERE id = " + ids[i]+";";
//		}
//		if(!StrUtils.isNull(dmlSql)){
//			try {
//				this.serviceContext.busDocdefMgrService.busDocdefDao.executeSQL(dmlSql);
//			} catch (Exception e) {
//				MessageUtils.showException(e);
//			}
//		}
//		this.alert("OK");
//		this.editGrid.reload();
//	}


	
}
