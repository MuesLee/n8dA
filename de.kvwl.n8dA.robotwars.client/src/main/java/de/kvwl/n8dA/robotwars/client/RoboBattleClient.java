package de.kvwl.n8dA.robotwars.client;

import java.rmi.Naming;
import java.util.UUID;

import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.client.communication.RoboBattleJMSReceiver;
import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;
import de.kvwl.n8dA.robotwars.commons.utils.NetworkUtils;

/**
 * Abstract Client Class
 * Establishes a connection to the gameserver und listens to the JMS-Queue
 * 
 */
public abstract class RoboBattleClient implements MessageListener{

	protected final Logger LOG = LoggerFactory.getLogger(RoboBattleClient.class);
	
	protected static final String url = "//"+NetworkUtils.HOST_IP_ADDRESS+"/"+NetworkUtils.SERVER_NAME;
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
		      LOG.info("Client: " + uuid + " connected to Server");
		    }
		    catch (Exception ex)
		    {
		      ex.printStackTrace();
		    }
	}
	
	private void listenToJMSReceiver()
	{	
		
		roboBattleJMSReceiver.setMessageListener(this);
		LOG.info("Client: " + uuid + "is listening on JSM-Queue");
	}
	
}
