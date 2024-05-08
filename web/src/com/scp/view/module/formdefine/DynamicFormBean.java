package com.scp.view.module.formdefine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICheckBox;
import org.operamasks.faces.component.form.impl.UIDateField;
import org.operamasks.faces.component.form.impl.UINumberField;
import org.operamasks.faces.component.form.impl.UITextArea;
import org.operamasks.faces.component.form.impl.UITextField;
import org.operamasks.faces.component.layout.UIPanelGrid;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.MultiLanguageBean;
import com.scp.exception.NoRowException;
import com.scp.model.sys.SysFormdefVal;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.FormView;

@ManagedBean(name = "pages.module.formdefine.dynamicformBean", scope = ManagedBeanScope.REQUEST)
public class DynamicFormBean extends FormView{
	
	@Inject(value = "l")
	protected MultiLanguageBean l;
	
	@Accessible
	@SaveState
	public Map<String, Object> dynamicObject = new HashMap<String, Object>();
	
	@Accessible
	@SaveState
	public List<ComBox> comBoxs = new ArrayList<ComBox>();
	
	@Bind
	public UIPanelGrid rootPanelGrid;
	
	@Bind
	@SaveState
	public String mBeanName;
	
	@Bind
	public String tblname;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			mBeanName = AppUtils.getReqParam("m");
			tblname = AppUtils.getReqParam("t");
			String p = AppUtils.getReqParam("p");
			////System.out.println("mBeanName :" + mBeanName);
			if(!StrUtils.isNull(p)){
				this.pkVal = Long.valueOf(p);
			}
			if(!StrUtils.isNull(mBeanName)){
				initDynamicForm();
				refresh();
			}
			this.update.markUpdate(UpdateLevel.Data, "panelGrid");
			if(!getSysformcfg(mBeanName).equals("")){
				String js = "setSysformcfg('"+getSysformcfg(mBeanName).replaceAll("\"","\\\\\"")+"')";
				Browser.execClientScript(js);
			}
		}
	}

	public void initDynamicForm() {
		
		StringBuilder jsExt = new StringBuilder();
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		//String mBeanName = this.getMBeanName();
		String sql = "SELECT * FROM sys_formdef where beaname = '"+mBeanName+"' ORDER BY orderno";
		List<Map> list = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sql);
		
		//dynamicObject.put("chayanri", Calendar.getInstance().getTime());
		for (Map map : list) {
			String inputype = StrUtils.getMapVal(map, "inputype");
			String inputlable = StrUtils.getMapVal(map, "inputlable");
			String columnname = StrUtils.getMapVal(map, "columnname");
			String defvalue = StrUtils.getMapVal(map, "defvalue");
			String colx = StrUtils.getMapVal(map, "colx");
			String coly = StrUtils.getMapVal(map, "coly");
			String ishidden = StrUtils.getMapVal(map, "ishidden");
			String colw = StrUtils.getMapVal(map, "colw");
			String colh = StrUtils.getMapVal(map, "colh");
			String colstyle = StrUtils.getMapVal(map, "colstyle");
			String srcdata = StrUtils.getMapVal(map, "srcdata");
			try {
				SysFormdefVal sysFormdefVal = this.serviceContext.sysFormdefService.sysFormdefValDao.findOneRowByClauseWhere("beaname = '"+mBeanName+"' AND columnname = '"+columnname+"' AND linkid = " + this.pkVal);
				String objtype = sysFormdefVal.getObjtype();
				String objval = sysFormdefVal.getObjval();
				//System.out.println(objtype + ":" + objval + ":" +sysFormdefVal.getColumnname());
				if("java.lang.String".equals(objtype)){
					dynamicObject.put(columnname, objval);
				}
				if("java.lang.Long".equals(objtype)){
					Long val = null;
					if(!StrUtils.isNull(objval) && StrUtils.isNumber(objval)){
						val = Long.valueOf(objval);
					}
					dynamicObject.put(columnname, val);
				}
				if("java.util.Date".equals(objtype)){
					java.util.Date val = null;
					if(!StrUtils.isNull(objval)){
						SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd" , Locale.CHINA);
						format.setLenient(false);
						try {
							val = format.parse(objval);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					dynamicObject.put(columnname, val);
				}
				
			} catch (NoRowException e) {
				//System.out.println(e.getLocalizedMessage() + "beaname="+mBeanName+":columnname="+columnname);
				dynamicObject.put(columnname, "");
			}
			
			if("UICheckBox".equals(inputype)){
				UIPanelGrid panelGrid = new UIPanelGrid();
				panelGrid.setId(columnname+"_key");
				HtmlOutputLabel label_state = new HtmlOutputLabel();
				label_state.setValue((String)l.m.get(inputlable));
				panelGrid.getChildren().add(label_state);
				UICheckBox comp = new UICheckBox();
				
				
				comp.setValue(dynamicObject.get(columnname));
				comp.setId(columnname);
				comp.setValueBinding("value", app.createValueBinding("#{"+this.getMBeanName()+".dynamicObject['"+columnname+"']}"));
				panelGrid.setColumns(2);
				panelGrid.setWidth("300");
				panelGrid.getChildren().add(comp);
				rootPanelGrid.getChildren().add(panelGrid);
			}
			
			if("UIDateField".equals(inputype)){
				UIPanelGrid panelGrid = new UIPanelGrid();
				panelGrid.setId(columnname+"_key");
				HtmlOutputLabel label_state = new HtmlOutputLabel();
				label_state.setValue((String)l.m.get(inputlable));
				panelGrid.getChildren().add(label_state);
				UIDateField comp = new UIDateField();
				comp.setSelectOnFocus(true);
				comp.setWidth(100);
				comp.setId(columnname);
				String jsVar = columnname+"JsVar";
				comp.setJsvar(jsVar);
				comp.setFormat("yyyy-MM-dd");
				if(dynamicObject.get(columnname) == null){
					jsExt.append(jsVar+".setValue('');");
					dynamicObject.put(columnname, "");
				}
				try {
					comp.setValue(dynamicObject.get(columnname)==null ? defvalue : dynamicObject.get(columnname));
				} catch (Exception e) {
					comp.setValue(dynamicObject.get(columnname));
				}
				comp.setValueBinding("value", app.createValueBinding("#{"+this.getMBeanName()+".dynamicObject['"+columnname+"']}"));
				panelGrid.getChildren().add(comp);
				panelGrid.setColumns(2);
				panelGrid.setWidth("200");
				rootPanelGrid.getChildren().add(panelGrid);
			}
			
			
			if("HtmlOutputLabel".equals(inputype)){
				UIPanelGrid panelGrid = new UIPanelGrid();
				panelGrid.setId(columnname+"_key");
				HtmlOutputLabel label_state = new HtmlOutputLabel();
				label_state.setValue((String)l.m.get(inputlable));
				label_state.setStyle(!StrUtils.isNull(colstyle)?colstyle:"font-size:20px");
				panelGrid.getChildren().add(label_state);
				
				panelGrid.setColumns(1);
				panelGrid.setWidth("300");
				
				if(!StrUtils.isNull(colw) && StrUtils.isNumber(colw)){
					Integer panelGridWidth = Integer.parseInt(colw)+label_state.getValue().toString().length()*20;
					panelGrid.setWidth(panelGridWidth.toString());
				}
				
				rootPanelGrid.getChildren().add(panelGrid);
			}
			
			if("UITextField".equals(inputype)){
				UIPanelGrid panelGrid = new UIPanelGrid();
				panelGrid.setId(columnname+"_key");
				HtmlOutputLabel label_state = new HtmlOutputLabel();
				label_state.setValue((String)l.m.get(inputlable));
				panelGrid.getChildren().add(label_state);
				UITextField comp = new UITextField();
				try {
					comp.setValue(dynamicObject.get(columnname)==null ? defvalue : dynamicObject.get(columnname));
				} catch (Exception e) {
					comp.setValue(dynamicObject.get(columnname));
				}
				comp.setSelectOnFocus(true);
				comp.setId(columnname);
				comp.setJsvar(columnname+"JsVar");
				comp.setValueBinding("value", app.createValueBinding("#{"+this.getMBeanName()+".dynamicObject['"+columnname+"']}"));
				
				//org.operamasks.faces.component.layout.UICell
				
				panelGrid.getChildren().add(comp);
				panelGrid.setColumns(2);
				panelGrid.setWidth("300");
				
				if(!StrUtils.isNull(colw) && StrUtils.isNumber(colw)){
					comp.setWidth(Integer.parseInt(colw));
					Integer panelGridWidth = Integer.parseInt(colw)+label_state.getValue().toString().length()*20;
					panelGrid.setWidth(panelGridWidth.toString());
				}
				if(!StrUtils.isNull(colh) && StrUtils.isNumber(colh)){
					comp.setHeight(Integer.parseInt(colh));
				}
				
				rootPanelGrid.getChildren().add(panelGrid);
			}
			
			if("UITextArea".equals(inputype)){
				UIPanelGrid panelGrid = new UIPanelGrid();
				panelGrid.setId(columnname+"_key");
				
				HtmlOutputLabel label_state = new HtmlOutputLabel();
				label_state.setValue((String)l.m.get(inputlable));
				panelGrid.getChildren().add(label_state);
				UITextArea comp = new UITextArea();
				try {
					comp.setValue(dynamicObject.get(columnname)==null ? defvalue : dynamicObject.get(columnname));
				} catch (Exception e) {
					comp.setValue(dynamicObject.get(columnname));
				}
				
				comp.setId(columnname);
				comp.setValueBinding("value", app.createValueBinding("#{"+this.getMBeanName()+".dynamicObject['"+columnname+"']}"));
				panelGrid.getChildren().add(comp);
				panelGrid.setColumns(2);
				panelGrid.setWidth("300");
				
				if(!StrUtils.isNull(colw) && StrUtils.isNumber(colw)){
					comp.setWidth(Integer.parseInt(colw));
					Integer panelGridWidth = Integer.parseInt(colw)+label_state.getValue().toString().length()*20;
					panelGrid.setWidth(panelGridWidth.toString());
				}
				if(!StrUtils.isNull(colh) && StrUtils.isNumber(colh)){
					comp.setHeight(Integer.parseInt(colh));
				}
				
				rootPanelGrid.getChildren().add(panelGrid);
			}
			
			if("UINumberField".equals(inputype)){
				UIPanelGrid panelGrid = new UIPanelGrid();
				panelGrid.setId(columnname+"_key");
				HtmlOutputLabel label_state = new HtmlOutputLabel();
				label_state.setValue((String)l.m.get(inputlable));
				panelGrid.getChildren().add(label_state);
				UINumberField comp = new UINumberField();
				try {
					comp.setValue(dynamicObject.get(columnname)==null ? defvalue : dynamicObject.get(columnname));
				} catch (Exception e) {
					comp.setValue(dynamicObject.get(columnname));
				}
				comp.setSelectOnFocus(true);
				comp.setId(columnname);
				comp.setValueBinding("value", app.createValueBinding("#{"+this.getMBeanName()+".dynamicObject['"+columnname+"']}"));
				panelGrid.getChildren().add(comp);
				panelGrid.setColumns(2);
				panelGrid.setWidth("300");
				rootPanelGrid.getChildren().add(panelGrid);
			}
			
			if("UISelectBox".equals(inputype)){
				ComBox comBox = new ComBox();
				comBox.setComId(columnname);
				comBox.setComLabel((String)l.m.get(inputlable));
				comBox.setComJsVar(columnname+"JsVar");
				comBox.setComValue(columnname + ":" + dynamicObject.get(columnname));
				List<SelectItem> items = new ArrayList<SelectItem>();
				String sqlsplit = "SELECT regexp_split_to_table('"+srcdata+"',';') m";
				List<Map> listm = serviceContext.daoIbatisTemplate.queryWithUserDefineSql(sqlsplit);
				try {
					if (listm != null && listm.size() > 0) {
						for (int i = 0; i < listm.size(); i++) {
							items.add(new SelectItem(comBox.getComId() + ":"
									+ listm.get(i).get("m").toString(), listm.get(i).get("m").toString()));
						}
					}
				} catch (Exception e) {
				}
				comBox.setSelectItem(items);
				comBoxs.add(comBox);
//				
//				UIPanelGrid panelGrid = new UIPanelGrid();
//				panelGrid.setId(columnname+"_key");
//				HtmlOutputLabel label_state = new HtmlOutputLabel();
//				label_state.setValue((String)l.m.get(inputlable));
//				panelGrid.getChildren().add(label_state);
//				UICombo comp = new UICombo();
//				try {
//					comp.setValue(dynamicObject.get(columnname)==null ? defvalue : dynamicObject.get(columnname));
//				} catch (Exception e) {
//					comp.setValue(dynamicObject.get(columnname));
//				}
//				//comp.get
//				comp.setJsvar(columnname+"JsVar");
//				comp.setSelectOnFocus(true);
//				comp.setId(columnname);
//				comp.setOnselect("comboOnSelect");
//				comp.setValueBinding("value", app.createValueBinding("#{"+this.getMBeanName()+".dynamicObject['"+columnname+"']}"));
//				panelGrid.getChildren().add(comp);
//				panelGrid.setColumns(2);
//				panelGrid.setWidth("300");
//				rootPanelGrid.getChildren().add(panelGrid);
			}
			rootPanelGrid.repaint();
		}
	}
	
	
	
	
	@Override
	public void refresh(){
		update.markUpdate(true, UpdateLevel.Data, "pkVal");
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
		editPanel.repaint();
		try {
			applyFormUserDef();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Browser.execClientScript("setSysformcfg(requiredcolums)");
		
		//Browser.execClientScript("location.reload();");
	}
	
	@Action
	public void saveDateAjax(){
		try {
			String mapStr = AppUtils.getReqParam("comboNew");
			//System.out.println(mapStr);
			JSONObject jsonObject = JSONObject.fromObject(mapStr); 
			//System.out.println(jsonObject);
			
			Set<String> set = dynamicObject.keySet();
			for (String key : set) {
				Object obj = dynamicObject.get(key);
				SysFormdefVal sysFormdefVal;
				try {
					sysFormdefVal = this.serviceContext.sysFormdefService.sysFormdefValDao.findOneRowByClauseWhere("beaname = '"+mBeanName+"' AND columnname = '"+key+"' AND linkid = " + this.pkVal);
				} catch (NoRowException e) {
					sysFormdefVal = new SysFormdefVal();
					sysFormdefVal.setId(0l);
				}
				if(obj == null){
					sysFormdefVal.setObjval(null);
				}else{
					////System.out.println(key + ":" + obj + ":" + obj.getClass().getName());
					if("java.util.Date".equals(obj.getClass().getName())){
						SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
						sysFormdefVal.setObjval(format.format(obj));
					}else{
						sysFormdefVal.setObjval(obj.toString());
					}
					sysFormdefVal.setObjtype(obj.getClass().getName());
				}
				
				sysFormdefVal.setBeaname(mBeanName);
				sysFormdefVal.setLinkid(this.pkVal);
				sysFormdefVal.setLinktbl(tblname);
				sysFormdefVal.setColumnname(key);
				
				if(!StrUtils.isNull(mapStr) && !"{}".equals(mapStr)){
					for (int i = 0; i < comBoxs.size(); i++) {
						ComBox comBox = comBoxs.get(i);
						if(sysFormdefVal.getColumnname() == comBoxs.get(i).getComId()){
							System.out.println(comBox.getComId() + ":new--->" + jsonObject.get(comBox.getComId()));
							if(jsonObject.get(comBox.getComId()) != null){
								sysFormdefVal.setObjval(jsonObject.get(comBox.getComId()).toString());
							}
						}
					}
				}
				
				this.serviceContext.sysFormdefService.saveDataDtl(sysFormdefVal);
			}
			Browser.execClientScript("remove()");
			//this.refresh();
			Browser.execClientScript("alert('OK')");
			//Browser.execClientScript("showmsg()");
			//Browser.execClientScript("remove()");
			
			Browser.execClientScript("location.reload();");
			
		} catch (Exception e) {
			MessageUtils.showException(e);
			e.printStackTrace();
		} finally{
			//Browser.execClientScript("remove()");
		}
	}
	
	@Action
	public void applyFormUserDef() throws Exception {
		//查询自己或者是自定义表单编辑用户
		String querySql = "SELECT 'form:'||columnname||'_key' AS colid,colx,coly,ishidden FROM sys_formdef where beaname = '"+mBeanName+"' AND colx > -900 AND coly > -900"; 
		List<Map> lists = null;
		lists = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql(querySql);
		if(lists != null && lists.size() > 0) {
			String keys = "";
			String xs = "";
			String ys = "";
			String ishiddens = "";
			for(int i = 0; i<lists.size(); i++) {
				Map map = lists.get(i);
				String colid = StrUtils.getMapVal(map, "colid");
				String colx = StrUtils.getMapVal(map, "colx");
				String coly = StrUtils.getMapVal(map, "coly");
				String ishidden = StrUtils.getMapVal(map, "ishidden");
				
				System.out.println(colid + ":" + colx + ":" + coly);
				
				keys  += colid;
				xs += colx;
				ys += coly;
				ishiddens += ishidden;
				if(i<lists.size() - 1) {
					keys  += ",";
					xs += ",";
					ys += ",";
					ishiddens += ",";
				}
				
			}
			
			//applyFormUserDef('form:applyuser_key','200','200','false');
			String js = "applyFormUserDef('"+keys+"','"+xs+"','"+ys+"','"+ishiddens+"');";
			System.out.println(js);
			Browser.execClientScript(js);
		}
	}
	
	@Action
	public void doDefineOrder(){
		String keys = (String)AppUtils.getReqParam("key");
		String names = (String)AppUtils.getReqParam("names");
		String x = (String)AppUtils.getReqParam("xstr");
		String y = (String)AppUtils.getReqParam("ystr");
		
		System.out.println(keys);
		System.out.println(names);
		System.out.println(x);
		System.out.println(y);
		
		if(keys != null && keys.length() > 0) {
			String[] key = keys.split(",");
			String[] name = names.split(",");
			String[] xstr = x.split(",");
			String[] ystr = y.split(",");
			
			Long userid = AppUtils.getUserSession().getUserid();
			StringBuffer buffer = new StringBuffer();
			for(int i = 0; i<key.length; i++) {
				String updateSql = "\nUPDATE sys_formdef SET colx = "+xstr[i]+", coly = "+ystr[i]+" where beaname = '"+mBeanName+"' AND 'form:'||columnname||'_key' = '"+key[i]+"';";
				
				buffer.append(updateSql);
			}
			if(buffer != null && buffer.length() > 0) {
				this.serviceContext.sysFormdefService.sysFormdefDao.executeSQL(buffer.toString());
			}
			Browser.alert("ok!");
			//refresh();
			Browser.execClientScript("location.reload();");
		}
	}
	@Action
	public void doFormComeBack(){
		String updateSql = "\nUPDATE sys_formdef SET colx = -1000, coly = -1000 where beaname = '"+mBeanName+"';";
		this.serviceContext.sysFormdefService.sysFormdefDao.executeSQL(updateSql);
		Browser.alert("ok!");
		//refresh();
		Browser.execClientScript("location.reload();");
	}
	@Action
	public void setFormProperty(){
		
	}
	
}
