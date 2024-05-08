package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatProduct;


/**
 * @author neo
 */
@Component
public class DatProductDao extends BaseDaoImpl<DatProduct, Long> {

    protected DatProductDao(Class<DatProduct> persistancesClass) {
        super(persistancesClass);
    }

    public DatProductDao() {
        this(DatProduct.class);
    }
}