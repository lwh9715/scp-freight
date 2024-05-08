package com.scp.view.module.finance.doc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.FsActSetNoSelectException;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;
import com.scp.view.module.finance.edi.SapExprot;

@ManagedBean(name = "pages.module.finance.doc.reportBean", scope = ManagedBeanScope.REQUEST)
public class ReportBean extends GridView {

	@Bind
	public UIWindow vchWindow;
	
	@Bind
	public UIIFrame vchIframe;
	
	@Bind
	@SaveState
	@Accessible
	public String actsetcode;
	
	@SaveState
	@Accessible
	public String qryYear;
	
	@SaveState
	@Accessible
	public String qryPeriodS;
	
	@SaveState
	@Accessible
	public String qryPeriodE;
	
	@SaveState
	@Accessible
	public Date vchDateS;
	
	@SaveState
	@Accessible
	public Date vchDateE;
	
	@Override
	public void beforeRender(boolean isPostBack) {
		super.beforeRender(isPostBack);
		if(!isPostBack) {
			
			if(AppUtils.getUserSession().getActsetid() == null || AppUtils.getUserSession().getActsetid() <= 0) {
				MessageUtils.alert("未选择帐套，请重新选择帐套！");
				return;
			}
			
			// 设置帐套名称
			try {
				actsetcode=AppUtils.getActsetcode();
			} catch (FsActSetNoSelectException e) {
				AppUtils.openWindow("", AppUtils.getBasePath()+"/pages/module/finance/initconfig/account.xhtml");
				return;
			}
			this.update.markUpdate(UpdateLevel.Data, "gridPanel");
			String sql = "SELECT year,period FROM fs_actset WHERE isdelete = FALSE AND id = "
				+AppUtils.getUserSession().getActsetid();
			Map m = this.serviceContext.daoIbatisTemplate
			.queryWithUserDefineSql4OnwRow(sql);
			qryYear = m.get("year").toString();
			qryPeriodS = m.get("period").toString();
			qryPeriodE = m.get("period").toString();
			update.markUpdate(true, UpdateLevel.Data, "actsetcode");

			gridLazyLoad = true;
			//this.refresh();			
		}
	}
	
	@Override
	public void grid_ondblclick() {
		String[] vchdtlids = this.grid.getSelectedIds();
		if (vchdtlids == null || vchdtlids.length == 0) {
			MessageUtils.alert("请双击一行！");
			return;
		}
		// 根据凭证详细id查到凭证id，作为参数传递
		String sql = "SELECT vchid FROM fs_vchdtl WHERE id =  " + vchdtlids[0];
		String vchid = "";
		try {
			Map m = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
			vchid = m.get("vchid").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.vchWindow.show();
		this.vchIframe.load(AppUtils.getContextPath()
				+ "/pages/module/finance/doc/indtl_readonly.xhtml?id=" + vchid);
	}
	
	@Override
	public Map getQryClauseWhere(Map<String, Object> queryMap) {
		Map m = super.getQryClauseWhere(queryMap);
		String qry = StrUtils.getMapVal(m, "qry");
		
		qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid();
		
		if(!StrUtils.isNull(qryYear)) {
			qry += "\nAND year = " + qryYear; // 年查询
		}
		if(!StrUtils.isNull(qryPeriodS) || !StrUtils.isNull(qryPeriodE)) {
			// 期间查询
			qry += "\nAND period BETWEEN " + (StrUtils.isNull(qryPeriodS)?0:qryPeriodS) 
				+ " AND " + (StrUtils.isNull(qryPeriodE)?12:qryPeriodE);
		}
		// 凭证日期查询
		if(vchDateS != null || vchDateE!= null) {
			qry += "\nAND singtime BETWEEN '" + (vchDateS==null ? "0001-01-01" : vchDateS)
				+ "' AND '" + (vchDateE==null ? "9999-12-31" : vchDateE) + "'";
		}
		m.put("qry", qry);
		return m;
	}
	
	@Override
	public void clearQryKey() {
		qryYear = "";
		qryPeriodS = "";
		qryPeriodE = "";
		vchDateS = null;
		vchDateE = null;
		super.clearQryKey();
	}

    @Action
	public void showExport() {
		Browser.execClientScript("exc1()");
	}
    
    @SaveState
	public String temFileUrl;

    @Bind(id = "downloadexcle", attribute = "src")
	private File getDownload() {
		return new File(temFileUrl);
	}
    
    @Action
    public void exporting() {
		StringBuffer fileUrl = new StringBuffer();
		fileUrl.append(System.getProperty("java.io.tmpdir"));

		try {		
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("SELECT f_fs_vch_sap_new('qryYear="+qryYear+";qryPeriodS="+qryPeriodS+";qryPeriodE="+qryPeriodE+";userid="+AppUtils.getUserSession().getUserid().toString()+";') AS sap");
			Map map = this.serviceContext.userMgrService.daoIbatisTemplate
					.queryWithUserDefineSql4OnwRow(sbsql.toString());

			if(map==null || map.size() == 0 || map.containsKey("sap") == false){
				return;
			}
			JSONObject json = JSONObject.fromObject(map.get("sap"));
			JSONArray array = new JSONArray();
			JSONArray array2 = StrUtils.isNull(json.getString("data2"))?new JSONArray():JSONArray.fromObject(json.getString("data2"));

			fileUrl.append("fs_vch_sap.xlsx");
			this.temFileUrl = fileUrl.toString();
			File file = new File(fileUrl.toString());
			// 导出托书文件名,指定的模板，可以自定义某些部分未固定，比如抬头部分，可以写死，下面map中对应清空数据，即不填充
			String exportFileName = "fs_vch_sap.xlsx";

			// 模版所在路径
			String fromFileUrl = AppUtils.getHttpServletRequest()
					.getSession().getServletContext().getRealPath("")
					+ File.separator
					+ "upload"
					+ File.separator
					+ "finace"
					+ File.separator + exportFileName;
			if (!SapExprot.exportSapReport(new File(fromFileUrl), file,
					array, array2)) {
				return;
			}

            // this.grid.reload();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
