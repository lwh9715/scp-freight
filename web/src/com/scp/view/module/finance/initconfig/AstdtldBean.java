package com.scp.view.module.finance.initconfig;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.base.CommonComBoxBean;
import com.scp.model.finance.fs.FsAst;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridFormView;
import com.scp.view.module.customer.CustomerChooseBean;

@ManagedBean(name = "pages.module.finance.initconfig.astdtldBean", scope = ManagedBeanScope.REQUEST)
public class AstdtldBean extends GridFormView {

    @ManagedProperty("#{customerchooseBean}")
    private CustomerChooseBean customerService;

    @SaveState
    @Accessible
    public FsAst selectedRowData = new FsAst();


    @SaveState
    @Accessible
    public String astypecode;

    @Bind
    @SaveState
    @Accessible
    public Long arid;


    @Bind
    @SaveState
    @Accessible
    public Long apid;

    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
            astypecode = AppUtils.getReqParam("astypecode").trim();
        }
    }


    // 往来单位部门(astdept)
    @Bind(id = "astDept")
    public List<SelectItem> getAstDept() {
        try {
            String qryType = "部门";
            //			if("B".equals(astypecode)){
            //				qryType = "往来单位";
            //			}else if("C".equals(astypecode)){
            //				qryType = "部门";
            //			}else if("D".equals(astypecode)){
            //				qryType = "职员";
            //			}else if("E".equals(astypecode)){
            //				qryType = "事业部";
            //			}
            return CommonComBoxBean.getComboxItems("y.id",
                    "y.code||'/'||y.name ", "fs_ast y , fs_astype z", "WHERE y.astypeid = z.id AND y.isdelete = FALSE AND z.name = '"+qryType+"' AND y.actsetid = " + AppUtils.getUserSession().getActsetid(),
                    "ORDER BY y.code");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }


    @Override
    public void add() {
        selectedRowData = new FsAst();

        super.add();
    }

    @Override
    protected void doServiceFindData() {
        selectedRowData = serviceContext.fsAstMgrService.fsAstDao.findById(this.pkVal);
    }

    @Override
    protected void doServiceSave() {
        selectedRowData.setAstypeid(getAstypeid());
        selectedRowData.setActsetid(AppUtils.getUserSession().getActsetid());
        selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());

        serviceContext.fsAstMgrService.saveData(selectedRowData);
        MessageUtils.alert("OK!");
        refresh();

    }

    @Override
    public void del() {
        serviceContext.fsAstMgrService.fsAstDao.remove(selectedRowData);
        this.add();
        this.alert("OK");
        refresh();
    }


    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(m, "qry");
        qry += "\nAND actsetid = " + AppUtils.getUserSession().getActsetid()+"AND astypecode='"+astypecode+"'";
        m.put("qry", qry);
        return m;
    }




    public Long getAstypeid(){
        String sql="SELECT id FROM fs_astype WHERE actsetid ="+AppUtils.getUserSession().getActsetid()+" AND isdelete = false AND code ='"+astypecode+"'";
        Map m = serviceContext.fsAstMgrService.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
        return Long.valueOf(StrUtils.getMapVal(m, "id"));

    }

    @Action
    public void chooseCs() {
        String url="";
        if(astypecode.equals("D")){
            url = AppUtils.getContextPath() + "/pages/module/finance/initconfig/fsastuserchoose.xhtml?astypeid="+getAstypeid();
            dtlIFrame.setSrc(url);
            update.markAttributeUpdate(dtlIFrame, "src");
            update.markUpdate(true, UpdateLevel.Data, dtlDialog);
            dtlDialog.show();
        }else if(astypecode.equals("C")){
            url = AppUtils.getContextPath() + "/pages/module/finance/initconfig/fsastdeptchoose.xhtml?astypeid="+getAstypeid();
            dtlIFrame.setSrc(url);
            update.markAttributeUpdate(dtlIFrame, "src");
            update.markUpdate(true, UpdateLevel.Data, dtlDialog);
            dtlDialog.show();
        }
    }

    @Bind
    private UIWindow dtlDialog;
    @Bind
    private UIIFrame dtlIFrame;

    @Action(id = "dtlDialog", event = "onclose")
    private void dtlEditDialogClose() {
        refresh();
    }

    @Action
    public void addMessage(){
        selectedRowData = new FsAst();
    }

    @Action
    public void refreshParent(){
        if (!astypecode.equalsIgnoreCase("C")) {
            MessageUtils.alert("只有职员才有上级");
            return;
        }
        String[] ids = this.grid.getSelectedIds();
        if (ids == null || ids.length == 0||ids.length>1) {
            MessageUtils.alert("请选择单行记录");
            return;
        }
        boolean isError = false;
        for(String id:ids){
            String sql = "SELECT f_set_fs_ast_parentid('usercode="+AppUtils.getUserSession().getUsercode()+";astid="+id+"')";
            try{
                Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            }catch(Exception e){
                isError = true;
                MessageUtils.showException(e);
            }
        }
        if(!isError){
            this.refresh();
            MessageUtils.alert("OK");
        }
    }

}