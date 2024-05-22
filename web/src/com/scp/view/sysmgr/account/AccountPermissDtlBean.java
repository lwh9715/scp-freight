package com.scp.view.sysmgr.account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.util.StrUtils;
import com.scp.view.comp.GridSelectView;

@ManagedBean(name = "pages.sysmgr.account.accountpermissdtlBean", scope = ManagedBeanScope.REQUEST)
public class AccountPermissDtlBean extends GridSelectView {

    @Bind
    @SaveState
    private String userid = "";


    @Override
    public void beforeRender(boolean isPostBack) {
        super.beforeRender(isPostBack);
        if (!isPostBack) {
            userid = (String) AppUtils.getReqParam("userid");
            this.update.markUpdate(UpdateLevel.Data, "userid");
        }

    }

    @Override
    public int[] getGridSelIds() {
        String sql;
        if ("-1".equals(userid)) {
            return null;
        } else if (!StrUtils.isNull(userid)) {
            sql = "\nselect "
                    + "\n(CASE WHEN EXISTS(SELECT 1 FROM fs_actsetctrl WHERE userid = "
                    + userid + " AND actsetid = a.id)"
                    + " THEN TRUE ELSE FALSE END) AS flag"
                    + "\nFROM fs_actset a" + "\nWHERE a.isdelete = FALSE;";
            try {
                List<Map> list = this.serviceContext.daoIbatisTemplate
                        .queryWithUserDefineSql(sql);
                //AppUtils.debug(1);
                List<Integer> rowList = new ArrayList<Integer>();
                int rownum = 0;
                for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                    Map map = (Map) iterator.next();
                    if ((Boolean) map.get("flag"))
                        rowList.add(rownum);
                    rownum++;
                }
                int row[] = new int[rowList.size()];
                for (int i = 0; i < rowList.size(); i++) {
                    row[i] = rowList.get(i);
                }
                //AppUtils.debug(row);
                return row;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void save() {
        String[] ids = this.grid.getSelectedIds();

        if (userid == null || "".equals(userid)) {
            MessageUtils.alert("请选择需要授权帐套的用户!");
            return;
        }

        if (ids == null) {
            MessageUtils.alert("请选择至少一个需要授权帐套");
            return;
        }

        this.delAll(userid);//此方法作用重置授权
        String sql = "";
        for (String id : ids) {
            sql += "\nINSERT INTO fs_actsetctrl(id,userid,actsetid)VALUES(getid(),"
                    + userid + "," + id + ");";
        }
        this.grid.repaint();
        this.update.markUpdate(UpdateLevel.Data, "gridPanel");
        MessageUtils.alert("授权成功!");
    }

    /**
     * 先将选中的用户所有权套删除，在重新添加上去.
     */
    public void delAll(String userid) {
    }
}
