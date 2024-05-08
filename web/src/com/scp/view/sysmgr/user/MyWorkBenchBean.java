package com.scp.view.sysmgr.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.ManagedBean;
import org.operamasks.faces.annotation.ManagedBeanScope;
import org.operamasks.faces.component.grid.impl.UIDataGrid;
import org.operamasks.faces.component.grid.provider.GridDataProvider;
import org.operamasks.faces.component.html.impl.UIIFrame;

import com.scp.model.sys.SysMemo;
import com.scp.util.AppUtils;
import com.scp.view.comp.GridView;

@ManagedBean(name = "pages.sysmgr.user.myworkbenchBean", scope = ManagedBeanScope.REQUEST)
public class MyWorkBenchBean extends GridView {
	
	@Accessible
	public String sysMemosTips;
	
	
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			sysMemosTips = initSysMemos();
			this.showMyTaskIframe();
		}
	}


	private String initSysMemos() {
		String sqlId = getMBeanName() + ".memo.grid.page";
		List<SysMemo> retList = this.serviceContext.sysMemoService.sysMemoDao.findAllByClauseWhere("isdelete = false"+
				"\nAND isvalid = true"+
			"\nAND (now() BETWEEN remindertimefm AND remindertimeend)"+
			"\nAND (ispublic = true OR UPPER(inputer) = UPPER('"+AppUtils.getUserSession().getUsercode()+"'))"+
			"\nORDER BY grade , name"+
			"");
		StringBuffer stringBuffer = new StringBuffer();
		for (SysMemo sysMemo : retList) {
			stringBuffer.append(sysMemo.getName()+ " Level[" + sysMemo.getGrade() + "]<br>" + sysMemo.getContents().replaceAll("\n", "<br>") );
			stringBuffer.append("@");
		}
		return stringBuffer.toString();
	}
	
	
	public Map getArgs(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("qry", "1=1");
		map.put("user", AppUtils.getUserSession().getUsercode());
		return map;
	}

	/**
	 * 数据显示构件，GRID
	 */
	@Bind
	public UIDataGrid msggrid;

	@Bind(id = "msggrid", attribute = "dataProvider")
	protected GridDataProvider getMsgGridDataProvider() {
		return new GridDataProvider() {
			
			@Override
			public Object[] getElements() {
				String sqlId = getMBeanName() + ".msg.grid.page";
				return daoIbatisTemplate.getSqlMapClientTemplate().queryForList(sqlId, getArgs()).toArray();
			}
			@Override
			public int getTotalCount() {
				return 10;
			}
			
			public Map getArgs(){
				Map<String, String> map = new HashMap<String, String>();
				map.put("qry", "1=1");
				map.put("user", AppUtils.getUserSession().getUsercode());
				return map;
			}
		};
	}
	
	@Bind
	public UIIFrame myTaskIframe;
	
	private void showMyTaskIframe() {
		myTaskIframe.load(AppUtils.getContextPath()+"/pages/ff/todo.xhtml");

		update.markAttributeUpdate(myTaskIframe, "src");
	}
	
}
