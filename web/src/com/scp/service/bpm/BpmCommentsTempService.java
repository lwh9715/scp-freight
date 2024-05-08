package com.scp.service.bpm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmCommentsTempDao;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmCommentsTemp;

@Component
@Lazy(true)
public class BpmCommentsTempService {

	@Resource
	public BpmCommentsTempDao bpmCommentsTempDao;

	public void saveData(BpmCommentsTemp data) {
		if (0 == data.getId()) {
			bpmCommentsTempDao.create(data);
		} else {
			bpmCommentsTempDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BpmCommentsTemp data = bpmCommentsTempDao.findById(id);
		bpmCommentsTempDao.remove(data);
	}

	
	public BpmCommentsTemp findBpmProcessByCode(String code){
		BpmCommentsTemp bpmCommentsTemp = null;
		try {
			bpmCommentsTemp = this.bpmCommentsTempDao.findOneRowByClauseWhere(" code = '"+code+"'");
		}catch (NoRowException e) {
			bpmCommentsTemp = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bpmCommentsTemp;
	}

}
