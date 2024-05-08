package com.scp.model.edi;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;

import com.scp.model.data.Ediesi;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.edi.ediesiBean", scope = ManagedBeanScope.REQUEST)
public class EdiEsiBean extends GridView {

    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
            super.applyGridUserDef();
            init();
        }
    }

    private void init() {
        String id = AppUtils.getReqParam("id");
    }

    @Bind
    public UIWindow editWindow;

    @Bind
    public UIIFrame editIframe;
    
    
    @Action
    public void add() {
    	String url = "./ediesiedit.xhtml?type=add";
//	 	editIframe.load(url);
//	 	editWindow.show();
//	 	
//	 	String title = "IFTMIN--补料";
//	 	editWindow.setTitle(title);
	 	AppUtils.openNewPage(url);
    }

	@Override
	public void grid_ondblclick() {
		String gridid = this.grid.getSelectedIds()[0];
		
		Ediesi ediesi = serviceContext.ediesiService.ediesiDao.findById(Long.valueOf(gridid));
		
	 	String url = "./ediesiedit.xhtml?id=" + gridid;
	 	
	 	AppUtils.openNewPage(url);
	 	
//	 	editIframe.load(url);
//	 	editWindow.show();
//	 	
//	 	
//	 	String type = ediesi.getEsitype();
//	 	String title = type;
//	 	if("IFTMIN".equals(type)){
//	 		title = "IFTMIN--补料";
//	 	}
//	 	
//	 	editWindow.setTitle(title);
	}

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        return m;
    }



    @Action
    public void del() {
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请选择记录");
            return;
        }
        try {
        	StringBuffer dmlSql = new StringBuffer();
            for (String id : ids) {
                    dmlSql.append("\nDELETE FROM edi_esi WHERE id = " + id + ";");
                    dmlSql.append("\nDELETE FROM edi_esidtlcnt WHERE ediesiid = " + id + ";");
                    dmlSql.append("\nDELETE FROM edi_esidtlcntlist WHERE ediesiid = " + id + ";");
            }
            if(dmlSql.length()>0){
            	daoIbatisTemplate.updateWithUserDefineSql(dmlSql.toString());
            }
            MessageUtils.alert("OK!");
            this.grid.reload();
        } catch (Exception e) {
            MessageUtils.showException(e);
            return;
        }
       
    }
}
