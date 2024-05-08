package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.Favorites;



/**
* 
 * @author neo
 */
@Component
public class FavoritesDao extends BaseDaoImpl<Favorites, Long>{
	
	protected FavoritesDao(Class<Favorites> persistancesClass) {
		super(persistancesClass);
	}

	public FavoritesDao() {
		this(Favorites.class);
	}
}