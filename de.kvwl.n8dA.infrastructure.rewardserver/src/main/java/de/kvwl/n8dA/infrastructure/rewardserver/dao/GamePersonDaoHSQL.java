package de.kvwl.n8dA.infrastructure.rewardserver.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import de.kvwl.n8dA.infrastructure.commons.entity.GamePerson;

public class GamePersonDaoHSQL extends BaseDaoHSQL<GamePerson> {

	@SuppressWarnings("unchecked")
	public List<GamePerson> findAllGamesByPersonName(String personName) {
		Query createNamedQuery = getEntityManager().createNamedQuery(
				"findAllGamesForPersonName");
		createNamedQuery.setParameter("personName", personName);
		return createNamedQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<GamePerson> findAllPersonsForGameName(String gameName) {
		Query createNamedQuery = getEntityManager().createNamedQuery(
				"findAllPersonsForGameName");
		createNamedQuery.setParameter("gameName", gameName);
		return createNamedQuery.getResultList();
	}

	public GamePerson findPersonInGame(String gameName, String personName) {
		Query createNamedQuery = getEntityManager().createNamedQuery(
				"findPersonInGame");
		createNamedQuery.setParameter("gameName", gameName);
		createNamedQuery.setParameter("personName", personName);

		GamePerson gamePerson;
		try {
			gamePerson = (GamePerson) createNamedQuery.getSingleResult();
		} catch (NonUniqueResultException e1) {
			gamePerson = null;
		} catch (NoResultException e) {
			gamePerson = null;
		}

		return gamePerson;

	}

	@SuppressWarnings("unchecked")
	public List<GamePerson> findAllGamePersons() {
		Query createNamedQuery = getEntityManager().createNamedQuery(
				"findAllGamePersons");
		return createNamedQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<GamePerson> findFirst10GamePersons() {
		Query createNamedQuery = getEntityManager().createNamedQuery(
				"findFirst10GamePersons");
		createNamedQuery.setMaxResults(10);
		return createNamedQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<GamePerson> findFirst10PersonsForGameName(String gameName) {
		Query createNamedQuery = getEntityManager().createNamedQuery(
				"findFirst10PersonsForGameName");
		createNamedQuery.setParameter("gameName", gameName);
		createNamedQuery.setMaxResults(10);
		return createNamedQuery.getResultList();
	}

	public GamePerson findGamePersonForPersonAndGame(String person,
			String gameName) {
		Query createNamedQuery = getEntityManager().createNamedQuery(
				"findGamePersonForPersonAndGame");
		createNamedQuery.setParameter("gameName", gameName);
		createNamedQuery.setParameter("personName", person);

		try {
			GamePerson singleResult = (GamePerson) createNamedQuery.getSingleResult();
			return singleResult;
		} catch (NoResultException e) {
			return null;
		}

	}

}
