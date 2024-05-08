package com.scp.view.comp;

import org.operamasks.faces.annotation.Accessible;
import org.operamasks.faces.annotation.Action;
import org.operamasks.faces.annotation.Bind;
import org.operamasks.faces.annotation.SaveState;
import org.operamasks.faces.user.ajax.UpdateLevel;

import com.scp.util.MessageUtils;




/**
 * 编辑加选择列表
 * @author neo
 *
 */
public abstract class GridSelectView extends GridView{
	
	@Bind
	@SaveState
	public Long pkId = -100L;

	@Action
	protected void add(){
		this.pkId = -100L;
		update.markUpdate(true, UpdateLevel.Data, "pkId");
		update.markUpdate(true, UpdateLevel.Data, "editPanel");
	}
	
	@Action
	protected void save(){
//		boolean edit = isEdit;
//		try {
//			if(!super.baseOk())return;
//		} catch (Exception e) {
//			MsgUtil.showException(e);
//			return;
//		}
//		Object pkidBySave = selectedRowData.get(this.crudService.getPkColName());
//		String pkIdStr = String.valueOf(pkidBySave);
//		this.pkId = Long.parseLong(pkIdStr);
		update.markUpdate(true, UpdateLevel.Data, "pkId");
		if(grid != null){
			this.grid.reload();
		}
		MessageUtils.alert("保存成功!");
		doSaveAfter();
	}
	
	protected void doSaveAfter() {
		
	}
	
	
	@Action(id = "doDel")
	protected void doDel(){
//		if(!doDelCheck())return;
//		if(this.del()){
//			selectedRowData.clear();
//			update.markUpdate(UpdateLevel.Data, "editPanel");
			MessageUtils.alert("删除成功!");
//			doDelAfter();
//		}
	}
	
	
	@Action(id = "doPrint")
	protected void doPrint(){
		
	}
    
	@Accessible
    public int[] getGridSelIds(){
    	int[] sele = new int[3];
    	sele[0] = 0;
    	sele[1] = 3;
    	sele[2] = 4;
    	return sele;
    }
}
