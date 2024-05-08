package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.api.ApiDataDao;
import com.scp.model.api.ApiData;

@Component
public class ApiDataMgrService {

    @Resource
    public ApiDataDao dataDao;

    public void saveData(ApiData data) {
        if (0 == data.getId()) {
            dataDao.create(data);
        } else {
            dataDao.modify(data);
        }
    }

    public void removeDate(Long id) {
        ApiData data = dataDao.findById(id);
        dataDao.remove(data);
    }

}
