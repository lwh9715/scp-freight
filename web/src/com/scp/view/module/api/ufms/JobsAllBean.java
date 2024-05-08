package com.scp.view.module.api.ufms;

import java.util.Map;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.component.html.impl.UIIFrame;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.model.finance.FinaJobs;
import com.scp.util.AppUtils;
import com.scp.util.ConfigUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.module.api.ufms.jobsAllBean", scope = ManagedBeanScope.REQUEST)
public class JobsAllBean extends GridView {

    @Bind
    @SaveState
    public String sonoq;

    @Bind
    @SaveState
    public String cntnoq;

    @Bind
    @SaveState
    public String mblnoq;

    @Bind
    @SaveState
    public String hblnoq;

    public Long userid;
    
    @Bind
    @SaveState
    public String ufmsUrl;
    
    
    @Bind
    @SaveState
    public String tipsTextArea;

    @Override
    public void beforeRender(boolean isPostBack) {
        this.userid = AppUtils.getUserSession().getUserid();
        if (!isPostBack) {
            super.applyGridUserDef();
            init();
        }
    }

    private void init() {
    	ufmsUrl = ConfigUtils.findSysCfgVal("sys_api_ufms_url");
    	update.markUpdate(true, UpdateLevel.Data, "ufmsUrl");
	}

	@Override
    public void grid_ondblclick() {
        long id = this.getGridSelectId();
        FinaJobs jobs = serviceContext.jobsMgrService.finaJobsDao.findByjobId(id);
        String winId = "_edit_jobsland";
        String url = "";
        if (jobs != null && !StrUtils.isNull(jobs.getJobtype()) && jobs.getJobtype().equals("S")) {
            url = "../../ship/jobsedit.xhtml?id=" + id;
        } else if (jobs != null && !StrUtils.isNull(jobs.getJobtype()) && jobs.getJobtype().equals("A")) {
            url = "../../air/jobsedit.xhtml?id=" + id;
        } else if (jobs != null && !StrUtils.isNull(jobs.getJobtype()) && jobs.getJobtype().equals("L")) {
            url = "../../land/jobsedit.xhtml?id=" + id;
        } else if (jobs != null && !StrUtils.isNull(jobs.getJobtype()) && jobs.getJobtype().equals("C")) {
            url = "../../customs/jobsedit.xhtml?id=" + id;
        } else {
            return;
        }
        AppUtils.openWindow(winId, url);
    }


    @Bind
    @SaveState
    String dynamicClauseWhere = "";

    @Override
    public Map getQryClauseWhere(Map<String, Object> queryMap) {
        Map m = super.getQryClauseWhere(queryMap);

        //初始化
        dynamicClauseWhere = " AND 1=1 AND nos IS NOT NULL AND refno IS NOT NULL AND refno <> '' AND trim(refno) <> ''";
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
        if (!StrUtils.isNull(sonoq)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.id AND isdelete = FALSE AND sono ILIKE '%" + sonoq + "%')";
        }
        if (!StrUtils.isNull(cntnoq)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_ship_container WHERE jobid = t.id AND isdelete = FALSE AND cntno ILIKE '%" + cntnoq + "%')";
        }
        if (!StrUtils.isNull(mblnoq)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.id AND isdelete = FALSE AND mblno ILIKE '%" + mblnoq + "%')";
        }
        if (!StrUtils.isNull(hblnoq)) {
            qry += "\n AND EXISTS(SELECT 1 FROM bus_shipping WHERE jobid = t.id AND isdelete = FALSE AND hblno ILIKE '%" + hblnoq + "%')";
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
        sonoq = "";
        cntnoq = "";
        mblnoq = "";
        hblnoq = "";
        super.clearQryKey();
    }


    @Bind
    public UIIFrame taskCommentsIframe;
    @Bind
    public UIIFrame traceIframe;

    @Action
    public void showTaskCheckInfo() {
        String jobid = AppUtils.getReqParam("jobid");
        String sql = "SELECT id FROM bpm_processinstance WHERE refid = '" + jobid + "' LIMIT 1";
        Map m = null;
        try {
            m = serviceContext.daoIbatisTemplate.queryWithUserDefineSql4OnwRow(sql);
        } catch (Exception e) {
        }
        taskCommentsIframe.load("../../../../bpm/bpmshowcomments.jsp?processinstanceid=" + ((m != null && m.get("id") != null) ? m.get("id") : "0")
                + "&userid=" + AppUtils.getUserSession().getUserid() + "&language=" + AppUtils.getUserSession().getMlType().name());
        traceIframe.load("../../../../bpm/model/trace.html?language=" + AppUtils.getUserSession().getMlType()
                + "&id=" + ((m != null && m.get("id") != null) ? m.get("id") : "0")
                + "&userid=" + AppUtils.getUserSession().getUserid() + "&language=" + AppUtils.getUserSession().getMlType().name());
        Browser.execClientScript("taskCheckInfoWindowJsVar.show();traceWindow.show()");
    }

    
    @Action
    public void export() {
        try {
        	String[] ids = this.grid.getSelectedIds();
    		if (ids == null || ids.length <= 0) {
                MessageUtils.alert("请选择一行数据");
    			return;
    		}
    		String usercode = AppUtils.getUserSession().getUsercode();
            String jobid = StrUtils.array2List(ids);
           // String querySql = "SELECT f_api_ufms('type:jobs;jobid:" + jobid + ";user:adm') AS json;";
            String querySql = "SELECT f_api_ufms_jobs_from('type:jobs;jobid:"+jobid+";user:"+usercode+"') AS json;";
            Map map = daoIbatisTemplate.queryWithUserDefineSql4OnwRow(querySql);
            if (map != null && map.containsKey("json") && map.get("json") != null) {
            	tipsTextArea = StrUtils.getMapVal(map, "json");
            	Browser.execClientScript("exportBatchExcelJsonShowWindowJsVar.show();");
            	update.markUpdate(true, UpdateLevel.Data, "tipsTextArea");
            }
        } catch (Exception e) {
            MessageUtils.showException(e);
        }
    }

}
