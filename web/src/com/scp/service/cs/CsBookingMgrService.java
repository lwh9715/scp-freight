package com.scp.service.cs;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.cs.CsBookingDao;
import com.scp.exception.NoRowException;
import com.scp.view.module.cs.CsBooking;

@Component
@Lazy(true)
public class CsBookingMgrService {

	@Resource
	public CsBookingDao csBookingDao;

	public void saveData(CsBooking data) {
		if (0 == data.getId()) {
			csBookingDao.create(data);
		} else {
			csBookingDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		CsBooking data = csBookingDao.findById(id);
		csBookingDao.remove(data);
	}
	public CsBooking findjobsByJobid(Long jobid){
		CsBooking csBooking = null;
		try {
			csBooking = this.csBookingDao.findOneRowByClauseWhere("isdelete = FALSE AND jobid = "+jobid);
		}catch (NoRowException e) {
			csBooking = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return csBooking;
	}
}