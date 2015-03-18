package de.kvwl.n8dA.infrastructure.rewardserver.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Implementation of BaseDao for an SQLite DB
 *
 */
public abstract class BaseDaoSqlite<T> implements BaseDao<T> {
	protected static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("n8da_rewards.sqlite");
	private static EntityManager em;

	protected BaseDaoSqlite() {
	}

	@SuppressWarnings("unchecked")
	public Class<T> getEntityBeanTyp() {
		return ((Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	public static EntityManager getEntityManager() {
		if (em == null)
			em = emf.createEntityManager();
		return em;
	}

	@Override
	public void add(T t) {
		EntityTransaction tx = getEntityManager().getTransaction();
		tx.begin();
		getEntityManager().persist(t);
		tx.commit();
	}

	@Override
	public void add(List<T> l) {
		EntityTransaction tx = getEntityManager().getTransaction();
		tx.begin();
		for (T t : l) {
			getEntityManager().persist(t);
		}
		tx.commit();
	}

	@Override
	public T findById(String id) {
		T emp = getEntityManager().find(getEntityBeanTyp(), id);
		return emp;
	}

	@Override
	public void update(T t) {
		EntityTransaction tx = getEntityManager().getTransaction();
		tx.begin();
		getEntityManager().merge(t);
		tx.commit();
	}

	@Override
	public void update(List<T> l) {
		EntityTransaction tx = getEntityManager().getTransaction();
		tx.begin();
		for (T t : l) {
			getEntityManager().merge(t);
		}
		tx.commit();
	}

	@Override
	public void delete(T t) {
		EntityTransaction tx = getEntityManager().getTransaction();
		tx.begin();
		getEntityManager().remove(t);
		tx.commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return getEntityManager().createQuery("from " + getEntityBeanTyp().getName())
				.getResultList();
	}
}
