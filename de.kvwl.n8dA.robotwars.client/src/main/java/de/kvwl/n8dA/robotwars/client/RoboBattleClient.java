package de.kvwl.n8dA.robotwars.client;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.jms.Message;
import javax.jms.MessageListener;

import de.kvwl.n8dA.robotwars.commons.interfaces.RoboBattleHandler;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.exception.UnknownRobotException;

public class RoboBattleClient implements Serializable, MessageListener{
	
	private static final long serialVersionUID = 1L;


	private static final String url = "//127.0.0.1/RoboBattleServer";

	
	private Robot robot;
	
	private RoboBattleHandler server;
	
	private RoboBattleJMSReceiver roboBattleJMSReceiver;


	
	public RoboBattleClient() {
		
		robot = new Robot();
		
		roboBattleJMSReceiver = new RoboBattleJMSReceiver();
	}
	
	public static void main(String[] args) {
		RoboBattleClient client = new RoboBattleClient();
		client.connectToServer(url);
		client.listenToJMSReceiver();
		client.registerClientWithRobotOnServer();
		
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
	
	
	
	public void registerClientWithRobotOnServer()
	{
	
		try {
			server.registerRobotAndClientForBattle(robot);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NoFreeSlotInBattleArenaException e) {
		}
	}
	
	private void updateRobot()
	{
		try {
			robot = server.getSynchronizedRobot(robot);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (UnknownRobotException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		System.out.println(message);
		
	}


}
