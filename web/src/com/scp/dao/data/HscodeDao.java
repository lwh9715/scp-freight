package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.Hscode;


/**
 * @author neo
 */
@Component
public class HscodeDao extends BaseDaoImpl<Hscode, Long> {

    protected HscodeDao(Class<Hscode> persistancesClass) {
        super(persistancesClass);
    }

    public HscodeDao() {
        this(Hscode.class);
    }
}