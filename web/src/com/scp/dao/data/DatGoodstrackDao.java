package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatGoodstrack;


/**
 * @author neo
 */
@Component
public class DatGoodstrackDao extends BaseDaoImpl<DatGoodstrack, Long> {

    protected DatGoodstrackDao(Class<DatGoodstrack> persistancesClass) {
        super(persistancesClass);
    }

    public DatGoodstrackDao() {
        this(DatGoodstrack.class);
    }
}