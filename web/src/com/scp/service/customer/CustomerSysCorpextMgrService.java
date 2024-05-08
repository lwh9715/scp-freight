package com.scp.service.customer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.sys.SysCorpextDao;
import com.scp.model.sys.SysCorpext;

@Component
public class CustomerSysCorpextMgrService {

    @Resource
    public SysCorpextDao SysCorpextDao;

    public void saveData(SysCorpext data) {
        if (0 == data.getId()) {
            SysCorpextDao.create(data);
        } else {
            SysCorpextDao.modify(data);
        }
    }

    public void removeDate(Long id) {
        SysCorpext data = SysCorpextDao.findById(id);
        SysCorpextDao.remove(data);
    }

    public void removeDatedel(Long pkid) {
        SysCorpext data = SysCorpextDao.findById(pkid);
        SysCorpextDao.remove(data);
    }

}