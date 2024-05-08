package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.EdiinttrareportDao;
import com.scp.model.data.Ediinttrareport;

@Component
public class EdiinttrareportMgrService {

    @Resource
    public EdiinttrareportDao ediinttrareportDao;

    public void saveData(Ediinttrareport data) {
        if (0 == data.getId()) {
            ediinttrareportDao.create(data);
        } else {
            ediinttrareportDao.modify(data);
        }
    }

    public void removeDate(Long id) {
        Ediinttrareport data = ediinttrareportDao.findById(id);
        ediinttrareportDao.remove(data);
    }
}