package com.scp.view.comp;

import java.io.Serializable;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.MessageUtils;


/**
 * 主从表
 * @author neo
 *
 */
public abstract class MastDtlView extends GridFormView{
	
	
	/**
	 * @param isPostBack
	 *  postback是回传 即页面在首次加载后向服务器提交数据,然后服务器把处理好的数据传递到客户端并显示出来,就叫postback, 
	 *  ispostback只是一个属性,即判断页面是否是回传,if(!Ispostback)就表示页面是首次加载,
	 *  这是很常用的一个判断方式.一个页面只能加载一次,但可以在加载后反复postback.
	 */
	@Override
	public void beforeRender(boolean isPostBack) {
		if (!isPostBack) {
			init();
		}
	}
	
	public abstract void init();
	
	
	@Bind
	@SaveState
	public Long mPkVal = -1L;
	
	@Accessible
	@SaveState
	protected Serializable mData;
	
	
	
	@Action
	public void addMaster(){
		this.mPkVal = -1L;
		update.markUpdate(true, UpdateLevel.Data, "masterEditPanel");
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
		this.grid.reload();
	}
	
	@Action
	public void saveMaster(){
		try {
			doServiceSaveMaster(); //Master
		} catch (Exception e) {
			MessageUtils.showException(e);
			return;
		}
		update.markUpdate(true, UpdateLevel.Data, "mPkVal");
	}
	
	@Action
	public void delMaster(){
		
	}
	

	public abstract void refreshMasterForm();
	
	public abstract void doServiceSaveMaster();


	@Override
	public void qryRefresh() {
		this.grid.reload();
	}
	
}
