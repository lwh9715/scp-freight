package com.scp.view.module.ship;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.EditGridFormView;

@ManagedBean(name = "pages.module.ship.loadBillMergeBean", scope = ManagedBeanScope.REQUEST)
public class LoadBillMergeBean extends EditGridFormView {
    @Bind
    @SaveState
    public String nos;

    @Bind
    @SaveState
    public String mblno;

    @Bind
    @SaveState
    public String sono;

    @Bind
    @SaveState
    public String vessel;

    @Bind
    @SaveState
    public String voyage;

    @Bind
    @SaveState
    public String etdstart;

    @Bind
    @SaveState
    public String etdend;

    @Bind
    @SaveState
    public String hblno;

    public Long userid;



    @Override
    public void beforeRender(boolean isPostBack) {
        this.userid = AppUtils.getUserSession().getUserid();
        if (!isPostBack) {
            super.applyGridUserDef();
            init();
        }
    }

    private void init() {
    }


    @Override
    public void grid_ondblclick() {
    }

    @Override
    protected void doServiceFindData() {

    }

    @Override
    protected void doServiceSave() {

    }

    @Bind
    @SaveState
    String dynamicClauseWhere = "";

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);

        //初始化
        dynamicClauseWhere = " AND 1=1 AND t.nos IS NOT NULL AND t.refno IS NOT NULL AND t.refno <> '' AND trim(t.refno) <> ''";
        m.put("dynamicClauseWhere", dynamicClauseWhere);

        //（录入人，修改人，业务员，客户组，委托人指派，工作单指派）
        String sql = "\nAND ( t.saleid = " + AppUtils.getUserSession().getUserid()
                + "\n	OR (t.inputer ='" + AppUtils.getUserSession().getUsercode() + "')" //录入人有权限
                + "\n	OR (EXISTS(SELECT 1 where 1 = f_checkright('user_info_mgr_showall2this'," + AppUtils.getUserSession().getUserid() + ")) " + //能看所有外办订到本公司的单权限的人能看到对应单
                "AND t.corpid <> t.corpidop AND t.corpidop = " + AppUtils.getUserSession().getCorpidCurrent() + ")"
                + "\n	OR EXISTS"
                + "\n				(SELECT "
                + "\n					1 "
                + "\n				FROM sys_custlib x , sys_custlib_user y  "
                + "\n				WHERE y.custlibid = x.id  "
                + "\n					AND y.userid = " + AppUtils.getUserSession().getUserid()
                + "\n					AND x.libtype = 'S'  "
                //+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid and z.id = t.saleid AND z.isdelete = false) " //关联的业务员的单，都能看到
                + "\n					AND x.userid = t.saleid "
                + ")"
                + "\n	OR EXISTS"
                + "\n				(SELECT "
                + "\n					1 "
                + "\n				FROM sys_custlib x , sys_custlib_role y  "
                + "\n				WHERE y.custlibid = x.id  "
                + "\n					AND EXISTS (SELECT 1 FROM sys_userinrole z WHERE z.userid = " + AppUtils.getUserSession().getUserid() + " AND z.roleid = y.roleid)"
                + "\n					AND x.libtype = 'S'  "
                //+ "\n					AND EXISTS(SELECT 1 FROM sys_user z where z.id = x.userid AND z.isdelete = false AND z.id = t.saleid) " //组关联业务员的单，都能看到
                + "\n					AND x.userid = t.saleid "
                + ")"

                //过滤工作单指派
                + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_shipping y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid =" + AppUtils.getUserSession().getUserid() + ")"
                + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_air y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid =" + AppUtils.getUserSession().getUserid() + ")"
                + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x, bus_truck y WHERE x.linkid = y.id AND y.jobid = t.id AND x.linktype = 'J' AND x.userid =" + AppUtils.getUserSession().getUserid() + ")"
                + "\n	OR EXISTS(SELECT 1 FROM sys_user_assign  x WHERE x.linkid = t.id AND x.linktype = 'J' AND x.userid =" + AppUtils.getUserSession().getUserid() + ")"
                + "\n)";

        // 权限控制 neo 2014-05-30
        m.put("filter", sql);
        String corpfilter = " AND (EXISTS(SELECT 1 FROM sys_user_corplink x WHERE (x.corpid = t.corpid OR x.corpid = t.corpidop) AND x.ischoose = TRUE AND userid ="
                + AppUtils.getUserSession().getUserid() + ") " +
                //"\n OR t.saleid ="+AppUtils.getUserSession().getUserid()+" OR COALESCE(t.saleid,0) <= 0)";
                "\n OR COALESCE(t.saleid,0) <= 0)";
        m.put("corpfilter", corpfilter);
        String qry = m.get("qry").toString();
        if (!StrUtils.isNull(nos)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping bs WHERE jobid = t.id AND bs.isdelete = FALSE AND nos ILIKE '%" + nos + "%')";
        }
        if (!StrUtils.isNull(mblno)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping bs WHERE jobid = t.id AND bs.isdelete = FALSE AND mblno ILIKE '%" + mblno + "%')";
        }
        if (!StrUtils.isNull(sono)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping bs WHERE jobid = t.id AND bs.isdelete = FALSE AND sono ILIKE '%" + sono + "%')";
        }
        if (!StrUtils.isNull(vessel)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping bs WHERE jobid = t.id AND bs.isdelete = FALSE AND vessel ILIKE '%" + vessel + "%')";
        }
        if (!StrUtils.isNull(voyage)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping bs WHERE jobid = t.id AND bs.isdelete = FALSE AND voyage ILIKE '%" + voyage + "%')";
        }
        if (!StrUtils.isNull(etdstart)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping bs WHERE jobid = t.id AND bs.isdelete = FALSE AND etd >= '" + etdstart + "')";
        }
        if (!StrUtils.isNull(etdend)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping bs WHERE jobid = t.id AND bs.isdelete = FALSE AND etd <= '" + etdend + "')";
        }
        if (!StrUtils.isNull(hblno)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping bs WHERE jobid = t.id AND bs.isdelete = FALSE AND hblno ILIKE '%" + hblno + "%')";
        }
        m.put("qry", qry);

        String ordersql = AppUtils.getUserColorder(getMBeanName() + ".grid");
        if (m.containsKey("ordersql")) {
            m.remove("ordersql");
        }
        if (!StrUtils.isNull(ordersql)) {
            ordersql = "ORDER BY " + ordersql;
            m.put("ordersql", ordersql);
        }
        return m;
    }

    @Override
    public void clearQryKey() {
        nos = "";
        mblno = "";
        sono = "";
        vessel = "";
        voyage = "";
        etdstart = "";
        etdend = "";
        hblno = "";
        super.clearQryKey();
    }


    @Action
    public void hebing() {
        try {
            JSONArray jsonArray = (JSONArray) modifiedData;
            if (jsonArray == null || jsonArray.size() != 1) {
                MessageUtils.alert("请选择一行主单");
                return;
            }
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            String parentjobid = String.valueOf(jsonObject.get("id"));

            String[] ids = this.editGrid.getSelectedIds();
            if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请选择一行分单");
                return;
            }


            String querySql = "SELECT f_fina_jobs_billunion('parentjobid:" + parentjobid + ";childjobids:" + StrUtils.array2List(ids) + "') AS json;";
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            alert("OK");
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

}
