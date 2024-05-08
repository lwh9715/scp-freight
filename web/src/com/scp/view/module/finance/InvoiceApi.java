package com.scp.view.module.finance;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.user.util.Browser;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.invoiceapiBean", scope = ManagedBeanScope.REQUEST)
public class InvoiceApi extends GridView{
	
	@Bind
	@SaveState
	public String cutomname;
	
	@Bind
	@SaveState
	public String cutomnameInfo;
	
	@Bind
	@SaveState
	public String dataArrayJson;
	
	@Bind
	@SaveState
	public Long clientid;
	
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		String linkid = AppUtils.getReqParam("linkid");
		if(!StrUtils.isNull(linkid)){
			clientid = Long.parseLong(linkid);
		}
	}

	@Action
	public void qryInvoice(){
		String returnjson = AppUtils.sendGet("https://efp.szhtxx.cn/apiv3/api/common/companyinfo/searchCodeByKeyWord","keyWord="+java.net.URLEncoder.encode(cutomname));
		cutomnameInfo = returnjson;
		//System.out.println(returnjson);
		this.invoiceApiGrid.reload();
	}
	
	@Bind
	public UIDataGrid invoiceApiGrid;
	
	@Bind(id = "invoiceApiGrid", attribute = "dataProvider")
	public GridDataProvider getInvoiceApiGridProvider() {
		JSONObject fromObject = JSONObject.fromObject(cutomnameInfo);
		try{
			dataArrayJson = fromObject.getString("datas");
		}catch(Exception e){
			dataArrayJson = "";
		}
		return new GridDataProvider() {
			@Override
			public Object[] getElements() {
				if(StrUtils.isNull(dataArrayJson)){
					return new Object[]{};
				}
				String querySql = 
					 "\nselect row_number() OVER() AS id,* from json_to_recordset('"+dataArrayJson+"')"
					+"\nas x(name TEXT, \"taxNo\" text, address text, telephone text, \"buyerBankAcc\" text, \"buyerBankNum\" text, code text, \"createDate\" text)"
					+"\nORDER BY id"+
					"\nLIMIT " + this.limit + " OFFSET " + start;
				try {
					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
					if(list==null) return null;
					return list.toArray(); 
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			public Object[] getElementsById(String[] id) {
//				String querySql = 
//					"\nSELECT " +
//					"\n* " +
//					"\nFROM _dat_goods a " +
//					"\nWHERE id= " +id[0];
//				try {
//					List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(querySql);
//					if(list==null) return null;
//					return list.toArray(); 
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
				return null;
			}

			@Override
			public int getTotalCount() {
				if(StrUtils.isNull(dataArrayJson)){
					return 0;
				}
				String countSql = 
					"\nSELECT COUNT(*) AS counts FROM("
					+"\nselect row_number() OVER() AS id,* from json_to_recordset('"+dataArrayJson+"')"
					+"\nas x(name TEXT, \"taxNo\" text, address text, telephone text, \"buyerBankAcc\" text, \"buyerBankNum\" text, code text, \"createDate\" text)"
					+"\n) t";
				try {
					Map m = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(countSql);
					Long count = (Long)m.get("counts");
					return count.intValue();
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		};
	}
	
	@Action
	public void importInvoice(){
		String[] ids = this.invoiceApiGrid.getSelectedIds();
		if(ids==null||ids.length<1){
			alert("请勾选需要导入的行！");
			return;
		}
		String sql = "SELECT * FROM f_import_onvoice_corpinv('clientid="+clientid+";jsonarray="+dataArrayJson+";ids="+StrUtils.array2List(ids)+"')";
		try{
			serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			alert("OK");
			Browser.execClientScript("parent.refreshAjaxSubmit.submit();");
		}catch(Exception e){
			MessageUtils.showException(e);
			e.printStackTrace();
		}
	}
	
}
