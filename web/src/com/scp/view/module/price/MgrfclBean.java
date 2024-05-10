package com.scp.view.module.price;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.ManagedProperty;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.form.impl.UICombo;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.component.layout.impl.UIWindow;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.base.CommonComBoxBean;
import com.scp.exception.NoRowException;
import com.scp.model.price.PriceFcl;
import com.scp.model.price.PriceFclFeeadd;
import com.scp.model.sys.SysUser;
import com.scp.service.price.PricefclMgrService;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.price.mgrfclBean", scope = ManagedBeanScope.REQUEST)
public class MgrfclBean extends EditGridFormView {

    @ManagedProperty("#{pricefclMgrService}")
    public PricefclMgrService pricefclMgrService;

    @SaveState
    @Accessible
    public PriceFcl selectedRowData = new PriceFcl();

    public Long userid;

    public String nosislock = "";

    @Bind
    @SaveState
    public String isqryPol = "Y";

    @Bind
    @SaveState
    public String isshipline = "Y";

    @Bind
    @SaveState
    public String isremark_out = "Y";

    @Bind
    @SaveState
    public String plusorcover = "cover";

    @Bind
    @SaveState
    public String datetype = "A";

    @Bind
    @SaveState
    public String priceusers;

    @Bind
    @SaveState
    public String pricetype;

    @Override
    public void beforeRender(boolean isPostBack) {
        this.userid = AppUtils.getUserSession().getUserid();
        if (!isPostBack) {
            super.applyGridUserDef();
            qryMap.put("COALESCE(istop,FALSE)$", false);
        }
    }

    @Bind
    public String pricename;

    /**
     * 页面对应的queryMap
     */
    @SaveState
    @Accessible
    public Map<String, Object> qryMapExt = new HashMap<String, Object>();

    @Bind(id = "polJson")
    public String getPolJson() {
        try {
            String pod = CommonComBoxBean.getComboxItemsAsJson("DISTINCT pod As name", "price_fcl AS d", "WHERE pod <> ''", "ORDER BY pod");
            if (StrUtils.isNull(pod)) pod = "\"\"" + "}";
            String json = "{\"results\":" + pod;
            //AppUtils.debug(json);
            return json;
        } catch (Exception e) {
            MessageUtils.showException(e);
            return "";
        }
    }

    @Bind(id = "qryPricename")
    public List<SelectItem> getQryPricename() {
        try {
            return CommonComBoxBean.getComboxItems("DISTINCT pricename", "pricename", "price_fcl", "WHERE isdelete = FALSE AND pricename IS NOT NULL AND pricename <> ''", "ORDER BY pricename");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Bind(id = "qryPol")
    public List<SelectItem> getQryPol() {
        try {
            List<SelectItem> items = new ArrayList<SelectItem>();
            //2015 运价维护界面下拉框，船公司和起运港，按分公司过滤提取
            String sql = "SELECT DISTINCT pol FROM price_fcl p WHERE isdelete = false AND pod <> '' AND pod is not null " +
                    " AND (p.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid ="
                    + AppUtils.getUserSession().getUserid() + ")) " +
                    " ORDER BY pol;";
            List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
            if (list != null) {
                Object value = null;
                for (Map dept : list) {
                    value = dept.get("pol");
                    items.add(new SelectItem(String.valueOf(value),
                            String.valueOf(value)));
                }
            }
            return items;
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Bind(id = "qryPod")
    public List<SelectItem> getQryPod() {
        try {
            List<SelectItem> items = new ArrayList<SelectItem>();
            //1733 运价维护及运价查询列表调整(起运港提取：收货地 UNION ALL 运价起运港数据)
            String sql = "WITH rc_pol AS("
                    + "\n SELECT DISTINCT pod FROM price_fcl WHERE isdelete = false and pod <> '' and pod is not null"
                    + "\n UNION ALL"
                    + "\n SELECT DISTINCT x.namee FROM dat_port x WHERE isdelete = false and isship = TRUE AND x.ispod = TRUE and exists (SELECT 1 FROM dat_port child where child.link = x.namee))"
                    + "\n SELECT DISTINCT pod FROM rc_pol ORDER BY pod;";
            List<Map> list = daoIbatisTemplate.queryWithUserDefineSql(sql);
            if (list != null) {
                Object value = null;
                for (Map dept : list) {
                    value = dept.get("pod");
                    items.add(new SelectItem(String.valueOf(value),
                            String.valueOf(value)));
                }
            }
            return items;
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Bind(id = "qryLine")
    public List<SelectItem> getQryLine() {
        try {
            return CommonComBoxBean.getComboxItems("line", "line", "price_fcl AS d"
                    , "WHERE isdelete = false and line <> '' and line is not null group by line", "ORDER BY convert_to(line,'GBK')");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Bind(id = "qryLinecode")
    public List<SelectItem> getQryLinecode() {
        try {
            return CommonComBoxBean.getComboxItems("DISTINCT shipline", "shipline", "price_fcl AS d"
                    , "WHERE isdelete = false and shipline <> '' and shipline is not null", "ORDER BY shipline");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Bind(id = "qryCar")
    public List<SelectItem> getQryCar() {
        try {
            return CommonComBoxBean.getComboxItems("DISTINCT shipping", "shipping", "price_fcl AS d"
                    , "WHERE isdelete = false and shipping <> '' and shipping is not null " +
                            " AND (d.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid =" + AppUtils.getUserSession().getUserid() + ")) ", "ORDER BY shipping");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }


    @Bind(id = "qryInputer")
    public List<SelectItem> getQryInputer() {
        try {
            return CommonComBoxBean.getComboxItems("DISTINCT inputer", "inputer", "price_fcl AS d", "WHERE 1=1", "ORDER BY inputer");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Bind(id = "qryUpdater")
    public List<SelectItem> getQryUpdater() {
        try {
            return CommonComBoxBean.getComboxItems("DISTINCT updater", "updater", "price_fcl AS d", "WHERE updater is not null", "ORDER BY updater");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }

    @Bind(id = "qrySchedule")
    public List<SelectItem> getQrySchedule() {
        try {
            return CommonComBoxBean.getComboxItems("DISTINCT schedule", "schedule", "price_fcl AS d", "WHERE 1=1", "ORDER BY schedule");
        } catch (Exception e) {
            MessageUtils.showException(e);
            return null;
        }
    }


    @Bind
    public UICombo qryPolCom;

    @Bind
    public UICombo qryPodCom;

    @Bind
    public UICombo qryCarCom;

    @Bind
    @SaveState
    public String feedesc;

    @Override
    public void clearQryKey() {
        if (qryMapExt != null) {
            qryMapExt.clear();
        }
        super.clearQryKey();
        lineQry = "";
        pol = "";
        pod = "";
        shiplinev = "";
        datefm = null;
        dateto = null;
        datetypefm = "=";
        datetypeto = "=";
        remark_outv = "";
        priceusers = "";
        Browser.execClientScript("$('#fclpol_input').val('');");
        Browser.execClientScript("$('#fclpod_input').val('');");
        if (qryPolCom != null) qryPolCom.repaint();
        if (qryPodCom != null) qryPodCom.repaint();
        if (qryCarCom != null) qryCarCom.repaint();
    }


    @Override
    protected void doServiceFindData() {
        //this.pkVal = getGridSelectId();
        this.selectedRowData = pricefclMgrService.pricefclDao.findById(this.pkVal);
        feedesc = pricefclMgrService.refreshFeeDesc(this.pkVal);
    }

    @Override
    protected void doServiceSave() {
        try {
            pricefclMgrService.pricefclDao.createOrModify(selectedRowData);
            this.pkVal = selectedRowData.getId();
            if (moduleList != null) {
                savefclfee(this.pkVal);
            }
            moduleList = null;
            //this.alert("OK!");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Bind
    public UIWindow importDataWindow;

    @Bind
    @SaveState
    @Accessible
    public String importDataText;

    @Action
    protected void startImport() {
        importDataBatch();
    }

    @Action
    public void importData() {
        importDataText = "";
        importDataWindow.show();
        this.update.markUpdate(UpdateLevel.Data, "importDataText");
    }

    @Action
    public void importDataBatch() {
        if (StrUtils.isNull(importDataText) || StrUtils.isNull(pricename) || pricename.trim().isEmpty()) {
            Browser.execClientScript("window.alert('" + "Data is null" + "');");
            return;
        } else {
            try {
                //2156 覆盖就删除
                if (plusorcover != null && plusorcover.equals("plus")) this.serviceContext.pricefclMgrService.removePriceByPriceName(pricename);
                Long corpid = AppUtils.getUserSession().getCorpid();
                String callFunction = "f_imp_price_fcl";
                String args = corpid + ",'"
                        + AppUtils.getUserSession().getUsercode() + "','" + pricename + "'";
                this.serviceContext.commonDBService.addBatchFromExcelText(
                        importDataText, callFunction, args, false);
                pricename = "";
                update.markUpdate(true, UpdateLevel.Data, pricename);
                MessageUtils.alert("OK!");
                this.refresh();
            } catch (Exception e) {
                MessageUtils.showException(e);
            }
        }


    }

    @Action
    public void release() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请勾选要发布的记录!");
                return;
            }
            pricefclMgrService.release(ids);
            MessageUtils.alert("OK!");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void releaseThis() {
        try {
            pricefclMgrService.release(new String[]{String.valueOf(this.pkVal)});
            MessageUtils.alert("OK!");
            this.refreshForm();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }


    @Action
    public void del() {
        String[] ids = this.editGrid.getSelectedIds();
        String username = AppUtils.getUserSession().getUsername();
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请勾选一行记录!");
            return;
        }
        try {
            pricefclMgrService.removes(ids, username);
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }


    @Override
    public void refresh() {
        super.refresh();
        update.markUpdate(true, UpdateLevel.Data, "qryPolCom");
        update.markUpdate(true, UpdateLevel.Data, "qryPodCom");
        update.markUpdate(true, UpdateLevel.Data, "qryCarCom");

        if (!qryMapExt.isEmpty()) {
            Set<String> set = qryMapExt.keySet();
            for (String object : set) {
                this.qryMap.put(object, qryMapExt.get(object));
            }
        }
        int[] s = {};
        this.editGrid.setSelections(s);
    }


    @Bind
    public UIIFrame localChargeIframe;

    @Override
    public void refreshForm() {
        if (this.pkVal != null && this.pkVal > 0) {
            this.selectedRowData = pricefclMgrService.pricefclDao.findById(this.pkVal);
            feedesc = pricefclMgrService.refreshFeeDesc(this.pkVal);

            localChargeIframe.load("localcharge.xhtml?linkid=" + this.pkVal + "&type=edit");

            update.markUpdate(true, UpdateLevel.Data, "editPanel");
        }
    }

    @Action
    public void close() {

        this.editWindow.close();
    }

    @Action
    public void addForm() {
        this.pkVal = -1L;
        this.selectedRowData = new PriceFcl();
        this.selectedRowData.setCurrency("USD");
        this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
        if (editWindow != null) editWindow.show();
        if (this.selectedRowData.getPriceuserid() != null) {
            SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getPriceuserid());
            Browser.execClientScript("$('#priceuser_input').val('" + sysUser.getNamec() + "')");
        } else {
            Browser.execClientScript("$('#priceuser_input').val('')");
        }
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
        update.markUpdate(true, UpdateLevel.Data, "pkVal");
    }


    @Action
    public void linkEdit() {
        String pkid = AppUtils.getReqParam("pkid");
        if (StrUtils.isNull(pkid)) {
            this.alert("无效pkid");
            return;
        }
        this.pkVal = Long.parseLong(pkid);
        doServiceFindData();
        this.refreshForm();
        if (editWindow != null) editWindow.show();
        if (this.selectedRowData.getPriceuserid() != null) {
            SysUser sysUser = serviceContext.userMgrService.sysUserDao.findById(this.selectedRowData.getPriceuserid());
            Browser.execClientScript("$('#priceuser_input').val('" + sysUser.getNamec() + "')");
        } else {
            Browser.execClientScript("$('#priceuser_input').val('')");
        }
        update.markUpdate(true, UpdateLevel.Data, "editPanel");
        editWindow.show();
        Browser.execClientScript("setcost()");
    }


    @Action
    public void addcopy() {
        String[] ids = this.editGrid.getSelectedIds();
        if (ids == null || ids.length != 1) {
            MessageUtils.alert("请勾选一行记录!");
            return;
        }
        Long id = Long.parseLong(ids[0]);
        PriceFcl old = pricefclMgrService.pricefclDao.findById(id);
        this.addForm();
        this.selectedRowData.setCls(old.getCls());
        this.selectedRowData.setPriceuserid(old.getPriceuserid());
        this.selectedRowData.setContacter(old.getContacter());
        this.selectedRowData.setCost20(old.getCost20());
        this.selectedRowData.setCost40gp(old.getCost40gp());
        this.selectedRowData.setCost40hq(old.getCost40hq());
        this.selectedRowData.setCost45hq(old.getCost45hq());
        this.selectedRowData.setCntypeothercode(old.getCntypeothercode());
        this.selectedRowData.setPieceother(old.getPieceother());
        this.selectedRowData.setCountry(old.getCountry());
        this.selectedRowData.setDatefm(old.getDatefm());
        this.selectedRowData.setDateto(old.getDateto());
        this.selectedRowData.setEtd(old.getEtd());
        this.selectedRowData.setLine(old.getLine());
        this.selectedRowData.setPod(old.getPod());
        this.selectedRowData.setPol(old.getPol());
        this.selectedRowData.setRemark_fee(old.getRemark_fee());
        this.selectedRowData.setRemark_in(old.getRemark_in());
        this.selectedRowData.setRemark_out(old.getRemark_out());
        this.selectedRowData.setRemark_ship(old.getRemark_ship());
        this.selectedRowData.setSchedule(old.getSchedule());
        this.selectedRowData.setShipping(old.getShipping());
        this.selectedRowData.setTt(old.getTt());
        this.selectedRowData.setVia(old.getVia());
        this.selectedRowData.setBargepod(old.getBargepod());
        this.selectedRowData.setInputer(AppUtils.getUserSession().getUsercode());
        this.selectedRowData.setInputtime(Calendar.getInstance().getTime());
        this.selectedRowData.setTypestart(old.getTypestart());
        this.selectedRowData.setTypeend(old.getTypeend());
        this.selectedRowData.setShipline(old.getShipline());
        this.selectedRowData.setIsinvalid(old.getIsinvalid());
        this.selectedRowData.setCorpid(AppUtils.getUserSession().getCorpid());
        this.selectedRowData.setPollink(old.getPollink());
        if (!StrUtils.isNull(old.getCurrency())) {
            this.selectedRowData.setCurrency(old.getCurrency());
        } else {
            this.selectedRowData.setCurrency("USD");
        }
        addfclfeea(-1L);
    }

    @SaveState
    protected List<PriceFclFeeadd> moduleList = null;


    /**
     * 查询并添加附加费
     */
    public void addfclfeea(Long dataid) {
        String[] ids = this.editGrid.getSelectedIds();
        Long id = Long.parseLong(ids[0]);
        List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid=" + id + " AND isdelete = false ORDER BY id");
        moduleList = new ArrayList<PriceFclFeeadd>();
        for (PriceFclFeeadd fclFeeadd : list) {
            PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
            priceFclFeeadd.setAmt(fclFeeadd.getAmt());
            moduleList.add(priceFclFeeadd);
        }

    }


    /**
     * 查询保存附加费
     */
    public void savefclfee(Long dataid) {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            Long id = Long.parseLong(ids[0]);
            List<PriceFclFeeadd> list = serviceContext.pricefclfeeaddMgrService.pricefclfeeaddDao.findAllByClauseWhere("fclid=" + id + " AND isdelete = false ORDER BY id");
            moduleList = new ArrayList<PriceFclFeeadd>();
            for (PriceFclFeeadd fclFeeadd : list) {
                PriceFclFeeadd priceFclFeeadd = new PriceFclFeeadd();
                priceFclFeeadd.setAmt(fclFeeadd.getAmt());
                priceFclFeeadd.setAmt20(fclFeeadd.getAmt20());
                priceFclFeeadd.setAmt40gp(fclFeeadd.getAmt40gp());
                priceFclFeeadd.setAmt40hq(fclFeeadd.getAmt40hq());
                priceFclFeeadd.setAmt45hq(fclFeeadd.getAmt45hq());
                priceFclFeeadd.setAmtother(fclFeeadd.getAmtother());
                priceFclFeeadd.setCurrency(fclFeeadd.getCurrency());
                priceFclFeeadd.setFclid(dataid);
                priceFclFeeadd.setFeeitemid(fclFeeadd.getFeeitemid());
                priceFclFeeadd.setFeeitemcode(fclFeeadd.getFeeitemcode());
                priceFclFeeadd.setFeeitemname(fclFeeadd.getFeeitemname());
                priceFclFeeadd.setPpcc(fclFeeadd.getPpcc());
                priceFclFeeadd.setUnit(fclFeeadd.getUnit());
                priceFclFeeadd.setInputer(AppUtils.getUserSession().getUsercode());
                priceFclFeeadd.setInputtime(Calendar.getInstance().getTime());
                priceFclFeeadd.setCntypeid(fclFeeadd.getCntypeid());
                moduleList.add(priceFclFeeadd);
            }
            serviceContext.pricefclfeeaddMgrService.saveOrModify(moduleList);
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }


    @Action
    private void releaseCheck_oncheck() {
        if (this.selectedRowData.getIsrelease()) {
            this.selectedRowData.setDaterelease(Calendar.getInstance().getTime());
        } else {
            this.selectedRowData.setDaterelease(null);
        }
        this.saveForm();
    }


    @Bind
    @SaveState
    public String pol;

    @Bind
    @SaveState
    public String pod;

    @Bind
    @SaveState
    public String shiplinev;

    @Bind
    @SaveState
    public String remark_outv;

    @Bind
    @SaveState
    public String lineQry;


    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map<String, String> map = super.getQryClauseWhere(queryMap);
        String qry = map.get("qry");
        map.remove("qry");


        String corpfilter = "AND (t.corpid = ANY(SELECT DISTINCT x.corpid FROM sys_user_corplink x WHERE x.ischoose = TRUE AND userid =" + AppUtils.getUserSession().getUserid() + "))";
        map.put("corpfilter", corpfilter);

        if (!StrUtils.isNull(pol)) {
            if (isqryPol.equals("Y")) {
                //类似SHENZHEN 查询，提取下面关联的SHEKOU ， YANTIAN
                map.put("pol", "AND(pol ILIKE '%" + StrUtils.getSqlFormat(pol) + "%' OR EXISTS (SELECT 1 FROM dat_port x where isdelete = false and x.namee = pol AND (x.link ILIKE '%" + StrUtils.getSqlFormat(pol) + "%')))");
            } else {
                map.put("pol", "AND(pol NOT ILIKE '%" + StrUtils.getSqlFormat(pol) + "%' AND NOT EXISTS (SELECT 1 FROM dat_port x where isdelete = false and x.namee = pol AND (x.link ILIKE '%" + StrUtils.getSqlFormat(pol) + "%')))");
            }
        }
        if (!StrUtils.isNull(shiplinev)) {
            isshipline = StrUtils.getSqlFormat(isshipline);
            if (isshipline.equals("Y")) {
                //2164 运价维护中航线代码模糊匹配拆分处理
                qry += "\n AND (EXISTS(SELECT 1 FROM (SELECT (regexp_split_to_table(shipline,'/')) AS linesv) S WHERE S.linesv ILIKE '%" + shiplinev + "%')";
                qry += "\n OR COALESCE(shipline,'') ILIKE '%" + shiplinev + "%')";
            } else {
                qry += "\n AND ( NOT EXISTS(SELECT 1 FROM (SELECT (regexp_split_to_table(shipline,'/')) AS linesv) S WHERE S.linesv ILIKE '%" + shiplinev + "%')";
                qry += "\n OR COALESCE(shipline,'') NOT ILIKE '%" + shiplinev + "%')";
            }
        }
        if (!StrUtils.isNull(remark_outv)) {
            remark_outv = StrUtils.getSqlFormat(remark_outv);
            if (isremark_out.equals("Y")) {
                qry = qry + "\n AND COALESCE(remark_out,'') ILIKE '%" + remark_outv + "%'";
            } else {
                qry = qry + "\n AND COALESCE(remark_out,'') NOT ILIKE '%" + remark_outv + "%'";
            }
        }
        if (!StrUtils.isNull(pod))
            map.put("pod", "AND EXISTS (SELECT 1 FROM dat_port x where x.namee = pod AND (x.namee = '" + pod + "' OR x.link = '" + pod + "'))");
        if (!StrUtils.isNull(lineQry)) qry = qry + " AND line ='" + lineQry + "'";

        if (!StrUtils.isNull(priceusers)) qry = qry + " AND priceuserid='" + priceusers + "'";
        map.put("qry", qry);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filter = "AND 1=1";

        if (datetype.equals("A")) {
            if (datefm != null && dateto != null) {
                if ("!=".equals(datetypefm) && "!=".equals(datetypeto)) {
                    filter += "\n AND NOT (datefm = '" + sdf.format(datefm).toString() + "' AND dateto = '" + sdf.format(dateto).toString() + "')";
                } else {
                    filter += "\n AND (datefm " + datetypefm + " '" + sdf.format(datefm).toString() + "' AND dateto " + datetypeto + " '" + sdf.format(dateto).toString() + "')";
                }
            }
            if (datefm != null && dateto == null) {
                filter += "\n AND datefm " + datetypefm + " '" + sdf.format(datefm).toString() + "'";
            }
            if (datefm == null && dateto != null) {
                filter += "\n AND dateto " + datetypeto + " '" + sdf.format(dateto).toString() + "'";
            }
        } else if (datetype.equals("B")) {
            if (datefm != null && dateto != null) {
                if ("!=".equals(datetypefm) && "!=".equals(datetypeto)) {
                    filter += "\n AND NOT (bargedatefm = '" + sdf.format(datefm).toString() + "' AND bargedateto = '" + sdf.format(dateto).toString() + "')";
                } else {
                    filter += "\n AND (bargedatefm " + datetypefm + " '" + sdf.format(datefm).toString() + "' AND bargedateto " + datetypeto + " '" + sdf.format(dateto).toString() + "')";
                }
            }
            if (datefm != null && dateto == null) {
                filter += "\n AND bargedatefm " + datetypefm + " '" + sdf.format(datefm).toString() + "'";
            }
            if (datefm == null && dateto != null) {
                filter += "\n AND bargedateto " + datetypeto + " '" + sdf.format(dateto).toString() + "'";
            }
        } else if (datetype.equals("C")) {
            if (datefm != null && dateto != null) {
                filter += "\n AND (closingtime BETWEEN " + " '" + sdf.format(datefm).toString() + "' AND " + " '" + sdf.format(dateto).toString() + "')";
            }
            if (datefm != null && dateto == null) {
                filter += "\n AND closingtime >=" + " '" + sdf.format(datefm).toString() + "'";
            }
            if (datefm == null && dateto != null) {
                filter += "\n AND closingtime <=" + " '" + sdf.format(dateto).toString() + "'";
            }
        }

        map.put("filter", filter);

        return map;
    }


    @Bind
    public UIWindow feeAddWindows;

    @Action
    private void feeAdd() {
        feeAddWindows.show();
        String blankUrl = AppUtils.chaosUrlArs("./addfee.xhtml") + "&id=" + this.pkVal;
        addFeeIframe.load(blankUrl);
    }


    @Bind
    public UIIFrame addFeeIframe;


    @Bind
    @SaveState
    public String z20gp;

    @Bind
    @SaveState
    public String z40gp;

    @Bind
    @SaveState
    public String z40hq;

    @Bind
    @SaveState
    public String c20gp;

    @Bind
    @SaveState
    public String c40gp;

    @Bind
    @SaveState
    public String c40hq;

    @Bind
    @SaveState
    public String c45hq;

    @Bind
    @SaveState
    public String z45hq;

    @Bind
    @SaveState
    public String z45hqv;

    @Bind
    @SaveState
    public String b20gp;

    @Bind
    @SaveState
    public String b20gpv;

    @Bind
    @SaveState
    public String b40gp;

    @Bind
    @SaveState
    public String b40gpv;

    @Bind
    @SaveState
    public String b40hq;

    @Bind
    @SaveState
    public String b40hqv;

    @Bind
    @SaveState
    public String b45hq;

    @Bind
    @SaveState
    public String b45hqv;

    @Bind
    @SaveState
    public String bcntypeother;

    @Bind
    @SaveState
    public String bcntypeotherv;

    @Bind
    @SaveState
    public String cntypeothercodev;

    @Bind
    @SaveState
    public String batchSchedule;

    @Bind
    @SaveState
    public String batchTT;

    @Bind
    @SaveState
    public Date batchETD;

    @Bind
    @SaveState
    public String batchRemarksInner;
    @Bind
    @SaveState
    public String batchRemarksOuter;

    @Bind
    @SaveState
    public String batchRemarksShip;


    @Bind
    @SaveState
    public Date batchDateFr;
    @Bind
    @SaveState
    public Date batchDateTo;

    @Bind
    @SaveState
    public String batchIsinvalid;

    @Bind
    @SaveState
    public String batchIstop;

    @Bind
    @SaveState
    public String batchIsrelease;


    @Bind
    public UIWindow editBatchWindowCost;

    @Bind
    public UIWindow editBatchWindoVesVel;

    @Bind
    public UIWindow editBatchWindowData;

    @Bind
    public UIWindow editBatchWindowRemarks;

    @Bind
    public UIWindow editBatchWindoType;

    @Bind
    @SaveState
    private String editBatch;

    @Bind
    public String cother;

    @Bind
    public String zcntypeother;

    @Bind
    public String z20gpv;

    @Bind
    public String z40gpv;

    @Bind
    public String z40hqv;

    @Bind
    public String zcntypeotherv;

    @Bind
    public String batchCurrency;

    @Bind
    public String batchshipabbr;

    @Bind
    public String batchcarrierabbr;

    @Action(id = "editBatch", event = "onselect")
    private void editBatch() {
        ////AppUtils.debug(editBatch);
        String[] ids = this.editGrid.getSelectedIds();
        ////AppUtils.debug(ids);
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请勾选要修改的行!");
            return;
        }
        if (editBatch == null || editBatch.length() == 0) {
            MessageUtils.alert("请勾选要修改的行!");
            return;
        } else if (editBatch.equals("Cost")) {
            z20gp = "";
            z40gp = "";
            z40hq = "";
            z45hq = "";
            z20gpv = "";
            z40gpv = "";
            z40hqv = "";
            z45hqv = "";
            b20gp = "";
            b40gp = "";
            b40hq = "";
            b45hq = "";
            b20gpv = "";
            b40gpv = "";
            b40hqv = "";
            b45hqv = "";
            zcntypeother = "";
            zcntypeotherv = "";
            bcntypeother = "";
            bcntypeotherv = "";
            update.markUpdate(true, UpdateLevel.Data, "j");
            Browser.execClientScript("costupdate1Jsvar.setValue('');costupdate2Jsvar.setValue('');baseupdate1Jsvar.setValue('');baseupdate2Jsvar.setValue('');");
            editBatchWindowCost.show();
        } else if (editBatch.equals("AddFee")) {
            //Browser.execClientScript("showRptWindows()");
            feeAddWindows.show();
            String blankUrl = AppUtils.chaosUrlArs("./addfee.xhtml?batchRefIds=" + StrUtils.array2List(ids));
            addFeeIframe.load(blankUrl);
        } else if (editBatch.equals("VesVel")) {
            batchSchedule = "";
            batchTT = "";
            batchETD = null;
            batchline = "";
            batchshipline = "";
            batchpol = "";
            batchpdd = "";
            batchpod = "";
            batchbargepol = "";
            batchshipabbr = "";
            batchcarrierabbr = "";
            batchpollink = "";
            update.markUpdate(true, UpdateLevel.Data, "ve");
            editBatchWindoVesVel.show();
        } else if (editBatch.equals("Data")) {
            batchDateFr = null;
            batchDateTo = null;
            batchTypestart = "";
            batchTypeend = "";
            batchbargedatefm = null;
            batchbargedateto = null;
            batchbargetype = "";
            batchbargetypend = "";
            batchIsinvalid = "";
            batchIstop = "";
            batchIsrelease = "";
            batchstartbaron = "";
            batchstarton = "";
            batchendbaron = "";
            batchendon = "";
            batchbarstartbaron = "";
            batchbarendbaron = "";
            batchbarendon = "";
            update.markUpdate(true, UpdateLevel.Data, "da");
            editBatchWindowData.show();
        } else if (editBatch.equals("Remarks")) {
            batchRemarksInner = "";
            batchRemarksOuter = "";
            batchRemarksShip = "";
            update.markUpdate(true, UpdateLevel.Data, "b");
            editBatchWindowRemarks.show();
        }

        Browser.execClientScript("setTimeout('editGridJsvar.getSelectionModel().selectRows(linenum)','1000');");
    }

    @Action
    private void saveBatchCost() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (!StrUtils.isNull(z20gp)) {
                pricefclMgrService.updateBatch(ids, z20gp, c20gp, "20", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(z40gp)) {
                pricefclMgrService.updateBatch(ids, z40gp, c40gp, "40gp", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(z40hq)) {
                pricefclMgrService.updateBatch(ids, z40hq, c40hq, "40hq", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(z45hq)) {
                pricefclMgrService.updateBatch(ids, z45hq, c45hq, "45hq", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(zcntypeother)) {
                pricefclMgrService.updateBatchother(ids, zcntypeother, cother, AppUtils.getUserSession().getUsercode());
            }

            if (!StrUtils.isNull(b20gp)) {
                pricefclMgrService.updateBaseBatch(ids, b20gp, c20gp, "20", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(b40gp)) {
                pricefclMgrService.updateBaseBatch(ids, b40gp, c40gp, "40gp", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(b40hq)) {
                pricefclMgrService.updateBaseBatch(ids, b40hq, c40hq, "40hq", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(b45hq)) {
                pricefclMgrService.updateBaseBatch(ids, b45hq, c45hq, "45hq", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(bcntypeother)) {
                pricefclMgrService.updateBaseBatchother(ids, bcntypeother, cother, AppUtils.getUserSession().getUsercode());
            }

            if (!StrUtils.isNull(z20gpv)) {
                pricefclMgrService.updateBatchValue(ids, z20gpv, "20", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(z40gpv)) {
                pricefclMgrService.updateBatchValue(ids, z40gpv, "40gp", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(z40hqv)) {
                pricefclMgrService.updateBatchValue(ids, z40hqv, "40hq", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(z45hqv)) {
                pricefclMgrService.updateBatchValue(ids, z45hqv, "45hq", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(zcntypeotherv)) {
                pricefclMgrService.updateBatchotherValue(ids, zcntypeotherv, cother, AppUtils.getUserSession().getUsercode());
            }

            if (!StrUtils.isNull(b20gpv)) {
                pricefclMgrService.updateBaseBatchValue(ids, b20gpv, "20", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(b40gpv)) {
                pricefclMgrService.updateBaseBatchValue(ids, b40gpv, "40gp", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(b40hqv)) {
                pricefclMgrService.updateBaseBatchValue(ids, b40hqv, "40hq", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(b45hqv)) {
                pricefclMgrService.updateBaseBatchValue(ids, b45hqv, "45hq", AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(bcntypeotherv)) {
                pricefclMgrService.updateBaseBatchotherValue(ids, bcntypeotherv, AppUtils.getUserSession().getUsercode());
            }

            if (!StrUtils.isNull(batchCurrency)) {
                pricefclMgrService.updateBatch(ids, "currency", batchCurrency, AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(cntypeothercodev)) {
                pricefclMgrService.updateCntypeothercode(ids, cntypeothercodev);
            }
            MessageUtils.alert("OK!");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }


    @Bind
    @SaveState
    public String batchline;

    @Bind
    @SaveState
    public String batchshipline;

    @Bind
    @SaveState
    public String batchpol;

    @Bind
    @SaveState
    public String batchpdd;

    @Bind
    @SaveState
    public String batchpod;

    @Bind
    @SaveState
    public String batchbargepol;

    @Bind
    @SaveState
    public String batchpollink;

    @Action
    private void saveBatchVesVel() {

        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (batchSchedule != "") {
                pricefclMgrService.updateBatch(ids, "schedule", batchSchedule, AppUtils.getUserSession().getUsercode());
            }
            if (batchTT != "") {
                pricefclMgrService.updateBatch(ids, "tt", batchTT, AppUtils.getUserSession().getUsercode());
            }
            if (batchline != "") {
                pricefclMgrService.updateBatch(ids, "line", batchline, AppUtils.getUserSession().getUsercode());
            }
            if (batchshipline != "") {
                pricefclMgrService.updateBatch(ids, "shipline", batchshipline, AppUtils.getUserSession().getUsercode());
            }
            if (batchpol != "") {
                pricefclMgrService.updateBatch(ids, "pol", batchpol, AppUtils.getUserSession().getUsercode());
            }
            if (batchpdd != "") {
                pricefclMgrService.updateBatch(ids, "via", batchpdd, AppUtils.getUserSession().getUsercode());
            }
            if (batchpod != "") {
                pricefclMgrService.updateBatch(ids, "pod", batchpod, AppUtils.getUserSession().getUsercode());
            }
            if (batchbargepol != "") {
                pricefclMgrService.updateBatch(ids, "bargepod", batchbargepol, AppUtils.getUserSession().getUsercode());
            }
            if (batchETD != null) {
                pricefclMgrService.updateBatch(ids, "etd", batchETD.toLocaleString(), AppUtils.getUserSession().getUsercode());
            }
            if (batchshipabbr != "") {
                pricefclMgrService.updateBatch(ids, "shipping", batchshipabbr, AppUtils.getUserSession().getUsercode());
            }
            if (batchcarrierabbr != "") {
                pricefclMgrService.updateBatch(ids, "carrier", batchcarrierabbr, AppUtils.getUserSession().getUsercode());
            }
            if (batchpollink != "") {
                pricefclMgrService.updateBatch(ids, "pollink", batchpollink, AppUtils.getUserSession().getUsercode());
            }
            MessageUtils.alert("OK!");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }


    @Action
    private void saveBatchRemarks() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (batchRemarksInner != "") {
                pricefclMgrService.updateBatch(ids, "remark_in", batchRemarksInner, AppUtils.getUserSession().getUsercode());
            }

            if (batchRemarksOuter != "") {
                pricefclMgrService.updateBatch(ids, "remark_out", batchRemarksOuter, AppUtils.getUserSession().getUsercode());
            }
            if (batchRemarksShip != "") {
                pricefclMgrService.updateBatch(ids, "remark_ship", batchRemarksShip, AppUtils.getUserSession().getUsercode());
            }
            MessageUtils.alert("OK!");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    private void addBatchRemarks() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (batchRemarksInner != "") {
                pricefclMgrService.addUpdateBatch(ids, "remark_in", batchRemarksInner, AppUtils.getUserSession().getUsercode());
            }

            if (batchRemarksOuter != "") {
                pricefclMgrService.addUpdateBatch(ids, "remark_out", batchRemarksOuter, AppUtils.getUserSession().getUsercode());
            }
            if (batchRemarksShip != "") {
                pricefclMgrService.addUpdateBatch(ids, "remark_ship", batchRemarksShip, AppUtils.getUserSession().getUsercode());
            }
            MessageUtils.alert("OK!");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Bind
    @SaveState
    public Date batchbargedatefm;

    @Bind
    @SaveState
    public Date batchbargedateto;

    @Bind
    @SaveState
    public String batchstartbaron;

    @Bind
    @SaveState
    public String batchstarton;

    @Bind
    @SaveState
    public String batchendbaron;

    @Bind
    @SaveState
    public String batchendon;

    @Bind
    @SaveState
    public String batchbarstartbaron;

    @Bind
    @SaveState
    public String batchbarendbaron;

    @Bind
    @SaveState
    public String batchbarendon;


    @Action
    private void saveBatchData() {
        String[] ids = this.editGrid.getSelectedIds();
        try {
            if (batchDateFr != null) {
                pricefclMgrService.updateBatch(ids, "datefm", batchDateFr.toLocaleString(), AppUtils.getUserSession().getUsercode());
            }
            if (batchDateTo != null) {
                pricefclMgrService.updateBatch(ids, "dateto", batchDateTo.toLocaleString(), AppUtils.getUserSession().getUsercode());
            }

            if (batchbargedatefm != null) {
                pricefclMgrService.updateBatch(ids, "bargedatefm", batchbargedatefm.toLocaleString(), AppUtils.getUserSession().getUsercode());
            }
            if (batchbargedateto != null) {
                pricefclMgrService.updateBatch(ids, "bargedateto", batchbargedateto.toLocaleString(), AppUtils.getUserSession().getUsercode());
            }

            if (!StrUtils.isNull(batchIsinvalid)) {
                pricefclMgrService.updateBatch(ids, "isinvalid", batchIsinvalid, AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(batchIstop)) {
                pricefclMgrService.updateBatch(ids, "istop", batchIstop, AppUtils.getUserSession().getUsercode());
            }
            if (batchTypestart != "") {
                pricefclMgrService.updateBatch(ids, "typestart", batchTypestart, AppUtils.getUserSession().getUsercode());
            }
            if (batchTypeend != "") {
                pricefclMgrService.updateBatch(ids, "typeend", batchTypeend, AppUtils.getUserSession().getUsercode());
            }
            if (batchbargetype != "") {
                pricefclMgrService.updateBatch(ids, "bargetype", batchbargetype, AppUtils.getUserSession().getUsercode());
            }
            if (batchbargetypend != "") {
                pricefclMgrService.updateBatch(ids, "bargetypend", batchbargetypend, AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(batchIsrelease)) {
                pricefclMgrService.updateBatch(ids, "isrelease", batchIsrelease, AppUtils.getUserSession().getUsercode());
            }

            if (!StrUtils.isNull(batchstartbaron)) {
                pricefclMgrService.updateBatch(ids, "bartypestart2", batchstartbaron, AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(batchstarton)) {
                pricefclMgrService.updateBatch(ids, "typestart2", batchstarton, AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(batchendbaron)) {
                pricefclMgrService.updateBatch(ids, "bartypeend2", batchendbaron, AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(batchendon)) {
                pricefclMgrService.updateBatch(ids, "typeend2", batchendon, AppUtils.getUserSession().getUsercode());
            }

            if (!StrUtils.isNull(batchbarstartbaron)) {
                pricefclMgrService.updateBatch(ids, "bargetypestr2", batchbarstartbaron, AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(batchbarendbaron)) {
                pricefclMgrService.updateBatch(ids, "bargetypend2", batchbarendbaron, AppUtils.getUserSession().getUsercode());
            }
            if (!StrUtils.isNull(batchbarendon)) {
                pricefclMgrService.updateBatch(ids, "baronend", batchbarendon, AppUtils.getUserSession().getUsercode());
            }


            MessageUtils.alert("OK!");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }


    @Override
    protected void update(Object obj) {
        pricefclMgrService.updateBatchEditGrid(obj);
    }


    @Action
    private void clearQry2() {
        this.clearQryKey();
    }


    @Bind
    public String batchTypestart;

    @Bind
    public String batchTypeend;

    @Bind
    public String batchbargetype;

    @Bind
    public String batchbargetypend;

    @Bind
    public UIWindow bargefeeEditWindows;

    @Bind
    public UIIFrame bargefeeEditIframe;

    @Action
    public void bargefeeEdit() {
        String[] ids = this.editGrid.getSelectedIds();
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请勾选要修改的行!");
            return;
        }
        bargefeeEditWindows.show();
        String blankUrl = AppUtils.chaosUrlArs("./bargefeeEdit.xhtml")
                + "&priceid=" + StrUtils.array2List(ids) + "&line=" + qryMap.get("line");
        bargefeeEditIframe.load(blankUrl);
    }

    @Action
    public void bargefeeEditone() {
        bargefeeEditWindows.show();
        String blankUrl = AppUtils.chaosUrlArs("./bargefeeEdit.xhtml") + "&priceid=" + pkVal;
        bargefeeEditIframe.load(blankUrl);
    }

    @Bind
    public UIWindow nosislockwindow;

    @Action
    public void fcl2jobsupdate() {
        try {
            String sql = "SELECT f_price_fcl2jobs_update('priceid=" + this.selectedRowData.getId() + ";userid="
                    + AppUtils.getUserSession().getUserid() + "') AS nosislock";
            Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            if (m != null) {
                nosislockwindow.show();
                nosislock = m.get("nosislock").toString();
                String js = "setNosislock('" + nosislock + "')";
                Browser.execClientScript(js);
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void fcl2jobsupdateAll() {
        String[] ids = this.editGrid.getSelectedIds();
        if (ids == null || ids.length == 0) {
            MessageUtils.alert("请勾选要修改的行!");
            return;
        }
        String priceids = StrUtils.array2List(ids);
        String sql = "SELECT f_price_fcl2jobs_update('priceid=" + priceids + ";userid="
                + AppUtils.getUserSession().getUserid() + "') AS nosislock";
        Map m;
        try {
            m = serviceContext.daoIbatisTemplate
                    .queryWithUserDefineSql4OnwRow(sql);
            if (m != null) {
                nosislockwindow.show();
                nosislock = m.get("nosislock").toString();
                String js = "setNosislock('" + nosislock + "')";
                Browser.execClientScript(js);
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }


    @Override
    public void saveBatch1(Object obj) {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请勾选一条记录!");
                return;
            }
            if ("[]".equals(obj.toString())) {
                pricefclMgrService.saveBatch2EditGrid(ids);
            } else {
                pricefclMgrService.saveBatchEditGrid(obj);
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void unpublish() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请勾选要取消发布的记录!");
                return;
            }
            pricefclMgrService.unpublish(ids);
            MessageUtils.alert("OK!");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void stop() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请选择记录");
                return;
            }
            pricefclMgrService.stop(ids, true);
            MessageUtils.alert("OK!");
            this.refresh();
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Bind
    @SaveState
    public Date datefm;

    @Bind
    @SaveState
    public Date dateto;

    @Bind
    @SaveState
    public String datetypefm = "=";

    @Bind
    @SaveState
    public String datetypeto = "=";

    @Bind
    public UIIFrame emailIframe;

    @Action
    public void price2email() {

    }

    @Action
    public void delay() {
        String[] ids = this.editGrid.getSelectedIds();
        if (ids == null || ids.length <= 0) {
            MessageUtils.alert("请至少勾选一条记录!");
            return;
        }
        for (String id : ids) {
            PriceFcl priceFcl = serviceContext.pricefclMgrService.pricefclDao.findById(Long.parseLong(id));
            if (batchDateFr != null && batchDateFr != priceFcl.getDatefm()) {
                String sql = "UPDATE price_fcl SET updater = 'delay" + AppUtils.getUserSession().getUsercode()
                        + "',datefm ='" + batchDateFr + "' ,updatetime = now() WHERE id =" + priceFcl.getId();
                try {
                    serviceContext.pricefclMgrService.pricefclDao.executeSQL(sql);
                } catch (Exception e) {
                    MessageUtils.showException(e);
                    return;
                }
            }
            if (batchDateTo != null && batchDateTo != priceFcl.getDateto()) {
                String sql = "UPDATE price_fcl SET updater = 'delay" + AppUtils.getUserSession().getUsercode()
                        + "',dateto='" + batchDateTo + "' ,updatetime = now() WHERE id =" + priceFcl.getId();
                try {
                    serviceContext.pricefclMgrService.pricefclDao.executeSQL(sql);
                } catch (Exception e) {
                    MessageUtils.showException(e);
                    return;
                }
            }
        }
        showNosislock(ids);
        this.editGrid.reload();
        alert("OK");
    }

    @Action
    public void delay2() {
        String[] ids = this.editGrid.getSelectedIds();
        if (ids == null || ids.length <= 0) {
            MessageUtils.alert("请至少勾选一条记录!");
            return;
        }
        for (String id : ids) {
            PriceFcl priceFcl = serviceContext.pricefclMgrService.pricefclDao.findById(Long.parseLong(id));
            if (batchbargedatefm != null && batchbargedatefm != priceFcl.getBargedatefm()) {
                String sql = "UPDATE price_fcl SET " +
                        "updater = 'delay" + AppUtils.getUserSession().getUsercode()
                        + "',bargedatefm ='" + batchbargedatefm + "' ,updatetime = now() WHERE id =" + priceFcl.getId();
                try {
                    serviceContext.pricefclMgrService.pricefclDao.executeSQL(sql);
                } catch (Exception e) {
                    MessageUtils.showException(e);
                    return;
                }
            }
            if (batchbargedateto != null && batchbargedateto != priceFcl.getBargedateto()) {
                String sql = "UPDATE price_fcl SET " +
                        "updater = 'delay" + AppUtils.getUserSession().getUsercode()
                        + "',bargedateto='" + batchbargedateto + "' ,updatetime = now() WHERE id =" + priceFcl.getId();
                try {
                    serviceContext.pricefclMgrService.pricefclDao.executeSQL(sql);
                } catch (Exception e) {
                    MessageUtils.showException(e);
                    return;
                }
            }
        }
        showNosislock(ids);
        this.editGrid.reload();
        alert("OK");
    }

    /**
     * 运价有效期变更的时候如果工作单委托中运价类型有值就不会撤回运价，此时把没有撤回的工作单显示到页面上
     *
     * @param ids
     */
    public void showNosislock(String[] ids) {
        String sql = "SELECT logdesc FROM sys_log WHERE refid=any(ARRAY[" + StrUtils.array2List(ids) + "])";
        try {
            Map m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
            if (m != null) {
                nosislockwindow.show();
                nosislock = m.get("logdesc").toString();
                String js = "setNosislock('" + nosislock + "')";
                Browser.execClientScript(js);
            }
        } catch (NoRowException e) {
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void clearSubmit() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请至少勾选一条记录!");
                return;
            }
            String column = AppUtils.getReqParam("value");
            pricefclMgrService.clearBatch(ids, column, AppUtils.getUserSession().getUsercode());
            MessageUtils.alert("OK!");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void clearprice() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请至少勾选一条记录!");
                return;
            }
            pricefclMgrService.clearPriceBatch(ids, AppUtils.getUserSession().getUsercode());
            MessageUtils.alert("OK!");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

    @Action
    public void clearbaseprice() {
        try {
            String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请至少勾选一条记录!");
                return;
            }
            pricefclMgrService.clearBasePriceBatch(ids, AppUtils.getUserSession().getUsercode());
            MessageUtils.alert("OK!");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }
}
