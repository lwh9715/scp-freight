package com.scp.view.sysmgr.dbmgr;

import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.Inject;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.PartialUpdateManager;
import org.operamasks.faces.user.ajax.UpdateLevel;
import org.operamasks.faces.user.util.Browser;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;
import com.scp.view.comp.FormView;
import com.ufms.base.db.DaoUtil;

@ManagedBean(name = "pages.sysmgr.dbmgr.dbqryBean", scope = ManagedBeanScope.REQUEST)
public class DbQryBean extends FormView{

	@Inject
	protected PartialUpdateManager update;

	@Override
	public void beforeRender(boolean isPostback) {
		if (!isPostback) {
			sqlText = "SELECT * FROM sys_user limit 10";
		}
	}

	@Bind
	@SaveState
	public String sqlText;

	@Bind
	public String sqlResult;

	@Action
	public void execQuery() {
		try {
			String querySql = sqlText.toUpperCase();
			checkSql(querySql);
			DaoIbatisTemplate daoIbatisTemplate = (DaoIbatisTemplate) AppUtils
					.getBeanFromSpringIoc("daoIbatisTemplate");
			sqlResult = DaoUtil.queryForJsonArrays(querySql);
			
			update.markUpdate(true, UpdateLevel.Data, "sqlResult");
			Browser.execClientScript("showSqlResult()");
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.showException(e);
		}
	}

	private void checkSql(String querySql) {
		if(querySql.indexOf("CREATE") > 0 || querySql.indexOf("DROP") > 0 || querySql.indexOf("DROP") > 0){
			
		}
	}
}
