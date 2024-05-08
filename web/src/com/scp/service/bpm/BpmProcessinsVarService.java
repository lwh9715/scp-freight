package com.scp.service.bpm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmProcessinsVarDao;
import com.scp.model.bpm.BpmProcessinsVar;

@Component
@Lazy(true)
public class BpmProcessinsVarService {

	@Resource
	public BpmProcessinsVarDao bpmProcessinsVarDao;

	public void saveData(BpmProcessinsVar data) {
		if (0 == data.getId()) {
			bpmProcessinsVarDao.create(data);
		} else {
			bpmProcessinsVarDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BpmProcessinsVar data = bpmProcessinsVarDao.findById(id);
		bpmProcessinsVarDao.remove(data);
	}

	public void save(Long processinstanceid, String name, String val) {
		BpmProcessinsVar data = new BpmProcessinsVar();
		data.setProcessinstanceid(processinstanceid);
		data.setName(name);
		data.setVal(val);
		bpmProcessinsVarDao.create(data);
	}
	
	public void save(Long processinstanceid, String name, String val,String lable) {
		String whereSql = "processinstanceid = "+processinstanceid+"  AND name = '"+name+"' ";
		BpmProcessinsVar data;
		try {
			data = this.bpmProcessinsVarDao.findOneRowByClauseWhere(whereSql);
		} catch (Exception e) {
			data = new BpmProcessinsVar();
		}
		data.setProcessinstanceid(processinstanceid);
		data.setName(name);
		data.setVal(val);
		data.setLable(lable);
		bpmProcessinsVarDao.create(data);
	}
	
	public String getBpmProcessinsVarValone(Long processinstanceid, String name) {
		String whereSql = "processinstanceid = "+processinstanceid+"  AND name = '"+name+"' ";
		try{
			List<BpmProcessinsVar> bpmProcessinsVars = bpmProcessinsVarDao.findAllByClauseWhere(whereSql);
			if(bpmProcessinsVars!=null&&bpmProcessinsVars.size()>0){
				return bpmProcessinsVars.get(0).getVal();
			}else{
				return "";
			}
		}catch(Exception e){
			return "";
		}
	} 
	
	
	public List<BpmProcessinsVar> getBpmProcessinsVars(Long processinstanceid) {
		String whereSql = "processinstanceid = "+processinstanceid+" ' ";
		return this.bpmProcessinsVarDao.findAllByClauseWhere(whereSql);
	} 
	
	public List<BpmProcessinsVar> getBpmProcessinsVar(Long processinstanceid, String name) {
		String whereSql = "processinstanceid = "+processinstanceid+"  AND name = '"+name+"' ";
		List<BpmProcessinsVar> lists =  this.bpmProcessinsVarDao.findAllByClauseWhere(whereSql);
		if(lists == null){ //neo 20200511 容错
			lists = new ArrayList<BpmProcessinsVar>();
		}
		if(lists.size()==0){
			lists.add(new BpmProcessinsVar());
		}
		return lists;
	} 

}
