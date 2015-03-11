package de.kvwl.n8dA.robotwars.client;

import java.rmi.Naming;

import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;

public class RoboBattleClient {
	
	private static final String url = "//127.0.0.1/RoboBattleServer";

	public RoboBattleClient() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		try {
		      RoboBattleHandler server = (RoboBattleHandler)Naming.lookup(url);
		      server.setActionForRobot(null, null);
		    }
		    catch (Exception ex)
		    {
		      
		    }
		
	}

}
