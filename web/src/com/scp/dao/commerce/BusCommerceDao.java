package com.scp.dao.commerce;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.commerce.BusCommerce;

/**
 * @author CIMC
 */
@Component
public class BusCommerceDao extends BaseDaoImpl<BusCommerce, Long> {

    protected BusCommerceDao(Class<BusCommerce> aClass) {
        super(aClass);
    }

    public BusCommerceDao() {
        this(BusCommerce.class);
    }
}
