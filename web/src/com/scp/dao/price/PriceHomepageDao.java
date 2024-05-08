package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceHomepage;

@Component
public class PriceHomepageDao extends BaseDaoImpl<PriceHomepage, Long> {

	protected PriceHomepageDao(Class<PriceHomepage> persistancesClass) {
		super(persistancesClass);
	}

	public PriceHomepageDao() {
		this(PriceHomepage.class);
	}

}
