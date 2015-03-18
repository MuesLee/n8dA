package de.kvwl.n8dA.infrastructure.rewardserver.server;

import de.kvwl.n8dA.infrastructure.rewardserver.dao.UserDaoSqlite;
import de.kvwl.n8dA.infrastructure.rewardserver.entity.Person;

public class RewardServer {
	
	private UserDaoSqlite userDao;
	
	public static void main(String[] args) {
		RewardServer rewardServer = new RewardServer();
		rewardServer.startServer();
		
		//TODO: Timo: persistence.xml -> drop and create zu create-or-extend-tables ändern
		
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
		
		Person findById = userDao.findById(user.getName());
		System.out.println(findById.getName() + " " + findById.getName());
		userDao.delete(user);
	}

}
