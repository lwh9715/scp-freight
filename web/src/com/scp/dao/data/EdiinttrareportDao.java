package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.Ediinttrareport;


/**
 * @author neo
 */
@Component
public class EdiinttrareportDao extends BaseDaoImpl<Ediinttrareport, Long> {

    protected EdiinttrareportDao(Class<Ediinttrareport> ediinttrareport) {
        super(ediinttrareport);
    }

    public EdiinttrareportDao() {
        this(Ediinttrareport.class);
    }
}