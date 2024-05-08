package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.HscodeDao;
import com.scp.model.data.Hscode;

@Component
public class HscodeMgrService {

    @Resource
    public HscodeDao hscodeDao;

    public void saveData(Hscode data) {
        if (0 == data.getId()) {
            hscodeDao.create(data);
        } else {
            hscodeDao.modify(data);
        }
    }

    public void removeDate(Long id) {
        Hscode data = hscodeDao.findById(id);
        hscodeDao.remove(data);
    }

}
