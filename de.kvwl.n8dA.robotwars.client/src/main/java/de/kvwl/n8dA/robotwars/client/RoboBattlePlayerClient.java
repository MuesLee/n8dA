package de.kvwl.n8dA.robotwars.client;

import java.rmi.RemoteException;
import java.util.UUID;

import javax.jms.Message;

import de.kvwl.n8dA.robotwars.commons.network.messages.ClientNotificationType;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.exception.UnknownRobotException;

public class RoboBattlePlayerClient extends RoboBattleClient{
	

	
	private Robot robot;
	

	public RoboBattlePlayerClient() {
		
		robot = new Robot();
		

	}
	
	public static void main(String[] args) {
		RoboBattlePlayerClient client = new RoboBattlePlayerClient();
		client.init();
		client.registerClientWithRobotOnServer();
		client.updateRobot();
	}
	
	
	
	
	
	public void registerClientWithRobotOnServer()
	{
		try {
			server.registerRobotAndClientForBattle(robot, uuid);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NoFreeSlotInBattleArenaException e) {
		}
	}
	
	private void updateRobot()
	{
		try {
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
			
			
		}catch (ArrayIndexOutOfBoundsException e) {
		System.out.println("Wenn man keine Ahnung hat, einfach mal die Finger vom Code lassen!");
		}
		 catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public UUID getUuid() {
		return uuid;
	}

}
