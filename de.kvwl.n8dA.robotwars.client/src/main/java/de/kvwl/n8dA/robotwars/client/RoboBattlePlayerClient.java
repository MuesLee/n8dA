package de.kvwl.n8dA.robotwars.client;

import java.rmi.RemoteException;
import java.util.UUID;

import javax.jms.Message;

import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.network.messages.ClientNotificationType;


/**
 * Player-Client for inputs and just obligatory information 
 *
 */
public class RoboBattlePlayerClient extends RoboBattleClient{
	
	private Robot robot;
	

	public RoboBattlePlayerClient() {
		
		robot = new Robot();
		

	}
	
	public static void main(String[] args) {
		RoboBattlePlayerClient client = new RoboBattlePlayerClient();
		client.init();
		client.registerClientWithRobotAtServer();
		client.updateRobot();
	}
	
	
	
	
	
	public void registerClientWithRobotAtServer()
	{
		try {
			LOG.info("Client: " + uuid + " wants to register at server");
			server.registerRobotAndClientForBattle(robot, uuid);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NoFreeSlotInBattleArenaException e) {
		}
	}
	
	private void updateRobot()
	{
		try {
			LOG.info("Client: " + uuid + " requests robot update");
			robot = server.getSynchronizedRobot(uuid);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (UnknownRobotException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		
		try {
			
			int intProperty = message.getIntProperty(ClientNotificationType.getNotificationName());
			ClientNotificationType clientNotificationType = ClientNotificationType.values()[intProperty];
			
			LOG.info("Client: " + uuid + " received: " + clientNotificationType.name());
			
			
		}catch (ArrayIndexOutOfBoundsException e) {
		LOG.debug("Wenn man keine Ahnung hat, einfach mal die Finger vom Code lassen!\n" + e.getStackTrace());
		}
		 catch (Exception e) {
			LOG.error("####Bumm####", e);
		}
		
	}

	public UUID getUuid() {
		return uuid;
	}

}
