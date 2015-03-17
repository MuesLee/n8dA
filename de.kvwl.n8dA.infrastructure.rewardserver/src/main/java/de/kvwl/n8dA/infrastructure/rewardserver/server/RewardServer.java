package de.kvwl.n8dA.infrastructure.rewardserver.server;

import de.kvwl.n8dA.infrastructure.rewardserver.dao.UserDaoSqlite;

public class RewardServer {
	
	private UserDaoSqlite userDao;
	
	public static void main(String[] args) {
		RewardServer rewardServer = new RewardServer();
		rewardServer.startServer();
	}

	public void startServer() {
		userDao = new UserDaoSqlite();
		
	}

}
