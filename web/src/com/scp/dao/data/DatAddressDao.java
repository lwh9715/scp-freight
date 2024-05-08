package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatAddress;


/**
 * @author neo
 */
@Component
public class DatAddressDao extends BaseDaoImpl<DatAddress, Long> {

    protected DatAddressDao(Class<DatAddress> persistancesClass) {
        super(persistancesClass);
    }

    public DatAddressDao() {
        this(DatAddress.class);
    }
}