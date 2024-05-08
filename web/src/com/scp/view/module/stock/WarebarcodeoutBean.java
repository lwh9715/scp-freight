package com.scp.view.module.stock;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EdiGridView;

@ManagedBean(name = "pages.module.stock.warebarcodeoutBean", scope = ManagedBeanScope.REQUEST)
public class WarebarcodeoutBean  extends EdiGridView{
	
	@Bind
	@SaveState
	public String scanningContent = "";
	
	@Bind
	@SaveState
	public String backstagePrompts = "";
	
	@Action
	public void confirmOut(){
		try {
			if(!StrUtils.isNull(scanningContent)){
				String barcode = scanningContent.replaceAll("\n", ",");
				if(barcode.startsWith("'") || barcode.indexOf("'") > 0){
					alert("Illegal characters!");
					return;
				}
				String sql = "SELECT f_wms_out_barcode('"+barcode+"')";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				backstagePrompts = "";
				scanningContent = "";
				this.update.markUpdate(UpdateLevel.Data, "backstagePrompts");
				this.update.markUpdate(UpdateLevel.Data, "scanningContent");
				MessageUtils.alert("OK!");
			}
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
	}
	
	
	
	@Action
	public void getbarinfo() {
		try {
			String str = scanningContent.replaceAll("\r|\n", "");
			if(str.length() < 13){
				MessageUtils.alert("条码错误!");
				backstagePrompts= "";
			}else{
				String str1 = str.substring(str.length()-13, str.length());
				String sql = "SELECT f_wms_getinfobybarcode('codeval="+str1+"') AS barinfo";
				Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
				String barinfo = StrUtils.getMapVal(m, "barinfo");
				backstagePrompts += barinfo + "\n";
				//System.out.println("barinfos--->" +barinfo);
				//System.out.println("backstagePrompts--->" +backstagePrompts);
			}
			this.update.markUpdate(UpdateLevel.Data, "backstagePrompts");
		} catch (Exception e) {
			MessageUtils.showException(e);
		}
		
	}
	
}
