package de.kvwl.n8dA.infrastructure.rewardserver.dao;

import java.util.List;

import javax.persistence.Query;

import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;

public class GamePersonDaoHSQL extends BaseDaoHSQL<GamePerson> {
	
	@SuppressWarnings("unchecked")
	public List<GamePerson> findAllGamesByPersonName (String personName)
	{
		Query createNamedQuery = getEntityManager().createNamedQuery("findAllGamesForPersonName");
		createNamedQuery.setParameter("personName", personName);
		return createNamedQuery.getResultList();
	}

}
