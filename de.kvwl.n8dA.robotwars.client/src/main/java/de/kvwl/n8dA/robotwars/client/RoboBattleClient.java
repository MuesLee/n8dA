package de.kvwl.n8dA.robotwars.client;

import java.rmi.Naming;
import java.util.UUID;

import javax.jms.MessageListener;

import de.kvwl.n8dA.robotwars.client.communication.RoboBattleJMSReceiver;
import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;

/**
 * Abstract Client Class
 * Establishes a connection to the gameserver und listens to the JMS-Queue
 * 
 */
public abstract class RoboBattleClient implements MessageListener{

	protected static final String url = "//127.0.0.1/RoboBattleServer";
	protected UUID uuid;
	protected RoboBattleHandler server;
	protected RoboBattleJMSReceiver roboBattleJMSReceiver;

	public RoboBattleClient() {
		
		this.uuid = UUID.randomUUID();
		
		roboBattleJMSReceiver = new RoboBattleJMSReceiver(uuid);
	
	}
	
	public void init()
	{
		connectToServer(url);
		listenToJMSReceiver();
	}

	private void connectToServer(String url) {
		try {
		      server = (RoboBattleHandler)Naming.lookup(url);
		      System.out.println("Connected to Server");
		    }
		    catch (Exception ex)
		    {
		      ex.printStackTrace();
		    }
	}
	
	private void listenToJMSReceiver()
	{
		roboBattleJMSReceiver.setMessageListener(this);
	}
	
}
