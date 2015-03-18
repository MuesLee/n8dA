package de.kvwl.n8dA.infrastructure.rewardserver.server;

import de.kvwl.n8dA.infrastructure.rewardserver.dao.UserDaoSqlite;
import de.kvwl.n8dA.infrastructure.rewardserver.entity.Person;

public class RewardServer {
	
	private UserDaoSqlite userDao;
	
	public static void main(String[] args) {
		RewardServer rewardServer = new RewardServer();
		rewardServer.startServer();
		
	}

	public void startServer() {
		userDao = new UserDaoSqlite();
		//TODO: Timo: Test Zeug entfernen
		testStuff();
	}

	private void testStuff() {
		Person user = new Person();
		user.setName("Derp");
		userDao.add(user);
		
		Person findById = userDao.findById(user.getId());
		System.out.println(findById.getName() + " " + findById.getId());
		userDao.delete(user);
	}

}
