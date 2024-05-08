package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatAddressDao;
import com.scp.model.data.DatAddress;

@Component
public class AddressMgrService {

    @Resource
    public DatAddressDao datAddressDao;

    public void saveData(DatAddress data) {
        if (0 == data.getId()) {
            datAddressDao.create(data);
        } else {
            datAddressDao.modify(data);
        }
    }

    public void removeDate(Long id) {
        DatAddress data = datAddressDao.findById(id);
        datAddressDao.remove(data);
    }
}