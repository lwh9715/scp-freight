package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.Ediinttraphrase;


/**
 * @author neo
 */
@Component
public class EdiinttraphraseDao extends BaseDaoImpl<Ediinttraphrase, Long> {

    protected EdiinttraphraseDao(Class<Ediinttraphrase> ediinttraphrase) {
        super(ediinttraphrase);
    }

    public EdiinttraphraseDao() {
        this(Ediinttraphrase.class);
    }
}