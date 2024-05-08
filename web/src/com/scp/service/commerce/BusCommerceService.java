package com.scp.service.commerce;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.commerce.BusCommerceDao;
import com.scp.model.commerce.BusCommerce;

/**
 * @author CIMC
 */
@Component
public class BusCommerceService {

    @Resource
    public BusCommerceDao busCommerceDao;

    public void saveData(BusCommerce data) {
        busCommerceDao.create(data);
    }

}
