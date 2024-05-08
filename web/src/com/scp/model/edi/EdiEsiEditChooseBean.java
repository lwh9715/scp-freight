package com.scp.model.edi;

import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.model.data.Ediesi;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.edi.ediesieditchooseBean", scope = ManagedBeanScope.REQUEST)
public class EdiEsiEditChooseBean extends GridView {


    @SaveState
    public Ediesi ediesi = new Ediesi();

    @SaveState
    @Accessible
    public Long ediesiid;


    @Override
    public void beforeRender(boolean isPostBack) {
        super.beforeRender(isPostBack);
        if (!isPostBack) {
        	String esiidStr = AppUtils.getReqParam("ediesiid");
        	if(!StrUtils.isNull(esiidStr)){
        		ediesiid = Long.valueOf(esiidStr);
                ediesi = serviceContext.ediesiService.ediesiDao.findById(this.ediesiid);
                
        	}
            
            update.markUpdate(true, UpdateLevel.Data, "gridPanel");

        }
    }


    @Action
    public void importShipjoin() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请先勾选要修改的行");
            return;
        }
        try {
            chooseShip(ids);
            MessageUtils.alert("OK");
            refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;

        }
    }

    public void chooseShip(String[] ids) {
    	try {
			String sql = "SELECT f_edi_esi_choose('cntids=" + StrUtils.array2List(ids) + ";esiid=" + ediesiid + ";type=CNT2DTL');";
			System.out.println(sql);
			serviceContext.ediesiService.ediesiDao.executeQuery(sql);
		} catch (Exception e) {
			MessageUtils.showException(e);
	        return;
		}
    }

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(m, "qry");
        m.put("qry", qry);
        
        String filter = "";
        if(queryMap.isEmpty()){
        	String sonos = this.ediesi.getSono();
        	if(!StrUtils.isNull(sonos)){
        		sonos = sonos.replaceAll(" ", "");
           	 	filter = "\nAND sono <> '' AND sono IS NOT NULL AND sono = ANY(SELECT regexp_split_to_table(COALESCE(TRIM('"+sonos+"'),''), CHR(10)))";
        	}else{
        		filter = "\nAND 1=2";
        	}
        }
        m.put("filter", filter);
        return m;
    }


}
