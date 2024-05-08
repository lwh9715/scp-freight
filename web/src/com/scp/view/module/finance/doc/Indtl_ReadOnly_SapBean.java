package com.scp.view.module.finance.doc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.exception.NoRowException;
import com.scp.model.finance.fs.FsVch;
import com.scp.model.finance.fs.FsVchdtl;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.JSONUtil;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.finance.doc.indtl_readonly_sapBean", scope = ManagedBeanScope.REQUEST)
public class Indtl_ReadOnly_SapBean extends GridView {

    @Bind
    @SaveState
    public Long pkVal;

    @Bind
    @SaveState
    public Long mPkVal = -1L;

    @Bind
    @SaveState
    public String inputer = "";

    @SaveState
    @Accessible
    public FsVch selectedRowData = new FsVch();

    @SaveState
    @Accessible
    public FsVchdtl ddata = new FsVchdtl();

    @Bind
    public UIWindow astWindow;

    //	@Bind
    //	@SaveState
    //	public String vchSrcType = "";


    @SaveState
    @Accessible
    public int countend = 0;

    @Override
    protected GridDataProvider getDataProvider() {
        return new GridDataProvider() {
            @Override
            public Object[] getElements() {
                String sqlId = getMBeanName() + ".grid.page";
                return daoIbatisTemplate.getSqlMapClientTemplate()
                        .queryForList(sqlId, getQryClauseWhere(qryMap), start,
                                limit).toArray();

            }

            @Override
            public int getTotalCount() {
                String sqlId = getMBeanName() + ".grid.count";
                List<Map> list = daoIbatisTemplate.getSqlMapClientTemplate()
                        .queryForList(sqlId, getQryClauseWhere(qryMap));
                Long count = (Long) list.get(0).get("counts");
                countend = count.intValue();
                return countend;
            }
        };
    }

    @Override
    public void beforeRender(boolean isPostBack) {
        if (!isPostBack) {
            init();
            initPrintConfig();//初始化打印数据
        }
    }


    @Override
    public void clearQryKey() {
        super.clearQryKey();
    }

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);
        String qry = StrUtils.getMapVal(m, "qry");
        qry += "\nAND vchid = " + mPkVal+" AND actsetid = " + AppUtils.getUserSession().getActsetid();
        m.put("qry", qry);
        return m;
    }

    public void init() {
        selectedRowData = new FsVch();
        String id = AppUtils.getReqParam("id");
        if (!StrUtils.isNull(id)) {
            this.mPkVal = Long.parseLong(id);
            this.refreshMasterForm();
            refresh(); // 从表也刷新


        } else {
            refresh();

        }
    }

    public void refreshMasterForm() {
        this.selectedRowData = serviceContext.vchMgrService.fsVchDao.findById(this.mPkVal);
        try {
            inputer = serviceContext.userMgrService.sysUserDao.findOneRowByClauseWhere(" code = '" + this.selectedRowData.getInputer() + "' AND isdelete = FALSE").getNamec();
            //	vchSrcType = this.selectedRowData.getSrctype();
        } catch (Exception e) {
            e.printStackTrace();
        }

        update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
        update.markUpdate(true, UpdateLevel.Data, "mPkVal");
        update.markUpdate(true, UpdateLevel.Data, "countend");


    }

    @Override
    public void refresh() {
        if (grid != null) {
            this.grid.reload();
        }
        //Browser.execClientScript("adddtl("+countend+")");
    }


    /*
     * 预览凭证 \
     * 好像无效
     */
    @Action
    public void showVch() {
        String pk = this.mPkVal.toString();
        pk = pk.substring(0, pk.length()-3);
        String url = "";
        url = AppUtils.getRptUrl()
                + "/reportJsp/showReport.jsp?raq=/v.raq&i="
                + pk;
        AppUtils.openWindow(true,"indtl_vch", url ,false);
    }


    /**
     * 上一条  点击上一条 下一条,需要检查,凭证是否平了,没有提示出来(优化写法 ,不取那么多出来)
     */
    @Action
    public void pageUp() {
        String nos = this.selectedRowData.getNos();
        if(nos.contains("-")){
            nos =nos.split("-")[0];
        }
        if(StrUtils.isNull(nos)){
            nos ="0";
        }
        Long vchtypeid = this.selectedRowData.getVchtypeid();
        Integer nosnum=Integer.parseInt(nos);
        String whereSql = "SELECT id FROM _fs_vch_main WHERE isdelete = FALSE AND srctype != 'I' AND actsetid = "
                + this.selectedRowData.getActsetid() + " AND year = "+this.selectedRowData.getYear()+" AND period = "+this.selectedRowData.getPeriod()+" AND nosnum <= "
                +nosnum+ " AND vchtypeid = " + vchtypeid +" ORDER BY nosnum DESC,id DESC limit 10";
        List<Map> lists = this.daoIbatisTemplate.queryWithUserDefineSql(whereSql);
        Map<String, Long> datas = new HashMap<String, Long>();
        for (int i = 0; i < lists.size(); i++) {
            datas = lists.get(i);
            if (((Long)datas.get("id")).equals(this.mPkVal)) {
                if (i != (lists.size() - 1)) {
                    this.mPkVal = (Long) lists.get(i + 1).get("id");
                    this.refreshMasterForm();
                    this.refresh();
                    this.checkVch(this.mPkVal);
                    return;
                } else {
                    MessageUtils.alert("第一条！");
                }
            }
        }
    }

    /**
     * 下一条
     */
    @Action
    public void pageDown() {
        String nos = this.selectedRowData.getNos();
        if(nos.contains("-")){
            nos =nos.split("-")[0];
        }
        if(StrUtils.isNull(nos)){
            nos ="0";
        }

        Long vchtypeid = this.selectedRowData.getVchtypeid();

        Integer nosnum=Integer.parseInt(nos);
        String whereSql = "SELECT id FROM _fs_vch_main WHERE isdelete = FALSE AND srctype != 'I' AND actsetid = "
                + this.selectedRowData.getActsetid() + " AND year = "+this.selectedRowData.getYear()+" AND period = "+this.selectedRowData.getPeriod()+" AND nosnum >= " + nosnum
                + " AND vchtypeid = " + vchtypeid +" ORDER BY nosnum ,id  limit 10";
        List<Map> lists = this.daoIbatisTemplate.queryWithUserDefineSql(whereSql);
        Map<String, Long> datas = new HashMap<String, Long>();
        //Map<Integer, Long> datas = new HashMap<Integer, Long>();
        for (int i = 0; i < lists.size(); i++) {
            datas = lists.get(i);
            if (((Long)datas.get("id")).equals(this.mPkVal)) {

                if (i != (lists.size() - 1)) {
                    this.mPkVal = (Long) lists.get(i + 1).get("id");
                    this.refreshMasterForm();
                    this.refresh();
                    this.checkVch(this.mPkVal);
                    return;
                } else {
                    MessageUtils.alert("最后一条！");
                }
            }
        }
    }

    public void checkVch(Long vchid) {
        String sql = "SELECT f_fs_checkvch('actsetid="+AppUtils.getUserSession().getActsetid()+";vchid="+vchid+"') AS iseq;";
        Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
        String iseq = String.valueOf(m.get("iseq"));
        if("0".equals(iseq)) {
            MessageUtils.alert("当前凭证借贷方总金额不相等，请核对！");
        }
    }

    @Bind
    @SaveState
    public String json = "[]";

    @Action
    public void showPringView(){
        if(this.mPkVal <= 0 ){
            MessageUtils.alert("请先保存凭证再打印!");
            return;
        }
        try {
            String jsonSql = "SELECT f_print_vch_loop('ids="+this.mPkVal+";user="+AppUtils.getUserSession().getUsername()+";') AS json;";
            Map map2 = this.serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(jsonSql);
            if(map2 != null && map2.size() > 0 && map2.containsKey("json")){
                this.json = JSONUtil.getDescodeJSONStr(map2.get("json").toString());
                update.markUpdate(UpdateLevel.Data, "json");
                Browser.execClientScript("printVch();");
            }else{
                throw new NoRowException();
            }
        } catch (NoRowException e) {
            MessageUtils.alert("没有找到凭证!");
        }catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Bind
    @SaveState
    public String center_w;
    @Bind
    @SaveState
    public String table_w;
    @Bind
    @SaveState
    public String paper_w;
    @Bind
    @SaveState
    public String paper_h;
    @Bind
    @SaveState
    public String page_w;
    @Bind
    @SaveState
    public String page_h;
    @Bind
    @SaveState
    public String page_back_top;
    @Bind
    @SaveState
    public String page_back_left;
    @Bind
    @SaveState
    public String paper_name;

    private void initPrintConfig(){
        try {

            Long userid = AppUtils.getUserSession().getUserid();

            center_w = ConfigUtils.findUserCfgVal("jsprint_config_center_w", userid);
            center_w = StrUtils.isNull(center_w) ? "750px" : center_w;
            table_w = ConfigUtils.findUserCfgVal("jsprint_config_table_w", userid);
            table_w = StrUtils.isNull(table_w) ? "750" : table_w;
            paper_w = ConfigUtils.findUserCfgVal("jsprint_config_paper_w", userid);
            paper_w = StrUtils.isNull(paper_w) ? "2410" : paper_w;
            paper_h = ConfigUtils.findUserCfgVal("jsprint_config_paper_h", userid);
            paper_h = StrUtils.isNull(paper_h) ? "1400" : paper_h;
            page_w = ConfigUtils.findUserCfgVal("jsprint_config_page_w", userid);
            page_w = StrUtils.isNull(page_w) ? "100%" : page_w;
            page_h = ConfigUtils.findUserCfgVal("jsprint_config_page_h", userid);
            page_h = StrUtils.isNull(page_h) ? "100%" : page_h;
            page_back_top = ConfigUtils.findUserCfgVal("jsprint_config_page_back_top", userid);
            page_back_top = StrUtils.isNull(page_back_top) ? "0" : page_back_top;
            page_back_left = ConfigUtils.findUserCfgVal("jsprint_config_page_back_left", userid);
            page_back_left = StrUtils.isNull(page_back_left) ? "1mm" : page_back_left;
            paper_name = ConfigUtils.findUserCfgVal("jsprint_config_paper_name", userid);
            paper_name = StrUtils.isNull(paper_name) ? "vch" : paper_name;
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
        update.markUpdate(true,UpdateLevel.Data,"printPanel");
    }

    @Bind
    public UIWindow showPringSeting;

    @Action
    public void showPrintConfig(){
        initPrintConfig();
        showPringSeting.show();
    }



    //	@Action
    //	public void saveVchNo(){
    //		try {
    //			if(mPkVal <= 0)return;
    //			String newNos = selectedRowData.getNos();
    //			newNos = StrUtils.getSqlFormat(newNos);
    //			String dmlSql = "UPDATE fs_vch SET nos = '"+newNos+"' WHERE id = " + mPkVal + ";";
    //			this.serviceContext.daoIbatisTemplate.updateWithUserDefineSql(dmlSql);
    //			this.alert("OK");
    //		} catch (Exception e) {
    //			e.printStackTrace();
    //			MessageUtils.showException(e);
    //		}
    //	}
}
