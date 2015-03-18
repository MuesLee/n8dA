package de.kvwl.n8dA.robotwars.client;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import javax.jms.Message;

import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

/**
 * Player-Client for inputs and just obligatory information
 *
 */
public class RoboBattlePlayerClient extends RoboBattleClient {

	private RobotPosition positionOfOwnRobot;

	public RoboBattlePlayerClient() {

	}

	public static void main(String[] args) {
		RoboBattlePlayerClient client = new RoboBattlePlayerClient();
		client.init();

		// TODO Timo: Test Zeug entfernen
		try {
			client.registerClientWithRobotAtServer(new Robot());
		} catch (NoFreeSlotInBattleArenaException e) {
			e.printStackTrace();
		}
		client.getUpdatedRobot();
		client.sendRobotActionToServer(new Attack(RobotActionType.ROCK, 10));
	}

	/**
	 * Meldet den Roboter zum Kampf am Server an. Client UUID wird automatisch
	 * mitgeschickt.
	 * 
	 * @param robot
	 * @throws NoFreeSlotInBattleArenaException
	 */
	public void registerClientWithRobotAtServer(Robot robot)
			throws NoFreeSlotInBattleArenaException {
		try {
			LOG.info("Client: " + uuid + " wants to register at server");
			RobotPosition ownRobotsPosition = server
					.registerRobotAndClientForBattle(robot, uuid);
			setPositionOfOwnRobot(ownRobotsPosition);
			LOG.info("Client: " + uuid + " plays the "
					+ ownRobotsPosition.getDescription() + " robot.");

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Der Spieler ist bereit. Der Kampf kann beginnen.
	 */
	public void sendPlayerIsReadyToBattleToServer() {
		producer.sendReadyToBeginBattleToServer();
	}

	/**
	 * Übermittelt die nächste Aktion des eigenen Roboters an den Server
	 * 
	 * @param robotAction
	 */
	public void sendRobotActionToServer(RobotAction robotAction) {
		producer.sendRobotActionToServer(robotAction);
	}

	/**
	 * Fordert den aktuellen Stand des gegnerischen Roboters an und gibt ihn
	 * zurück
	 * 
	 * @return
	 */
	public Robot getUpdatedRobotOfEnemy() {
		try {
			server.getSynchronizedRobotOfEnemy(uuid);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (UnknownRobotException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fordert den aktuellen Stand des eigen Roboters an und gibt ihn zurück
	 */
	public Robot getUpdatedRobot() {
		try {
			LOG.info("Client: " + uuid + " requests robot update");
			Robot robot = server.getSynchronizedRobot(uuid);
			return robot;
		} catch (RemoteException e) {
		} catch (UnknownRobotException e) {
		}

		return null;
	}

	@Override
	public void onMessage(Message message) {
		try {
			int intProperty = message.getIntProperty(GameStateType
					.getNotificationName());
			GameStateType gameStateType = GameStateType.values()[intProperty];

			LOG.info("Client: " + uuid + " received: " + gameStateType.name());

		} catch (ArrayIndexOutOfBoundsException e) {
			LOG.debug("Wenn man keine Ahnung hat, einfach mal die Finger vom Code lassen!\n"
					+ e.getStackTrace());
		} catch (Exception e) {
			LOG.error("####Bumm####", e);
		}

	}
	
	public void disconnectFromServer()
	{
		producer.sendDisconnectFromServer();
	}
	
	public List<Attack> getAllPossibleAttacksFromServer()
	{
		try {
			return server.getAllPossibleAttacks();
		} catch (RemoteException e) {
		}
		
		return null;
	}
	public List<Defense> getAllPossibleDefendsFromServer()
	{
		try {
			return server.getAllPossibleDefends();
		} catch (RemoteException e) {
		}
		
		return null;
	}
	public List<RoboItem> getAllPossibleItemsFromServer()
	{
		try {
			return server.getAllPossibleItems();
		} catch (RemoteException e) {
		}
		
		return null;
	}
	public List<Robot> getAllPossibleRobotsFromServer()
	{
		try {
			return server.getAllPossibleRobots();
		} catch (RemoteException e) {
		}
		
		return null;
	}

	public UUID getUuid() {
		return uuid;
	}

	public RobotPosition getPositionOfOwnRobot() {
		return positionOfOwnRobot;
	}

	public void setPositionOfOwnRobot(RobotPosition positionOfOwnRobot) {
		this.positionOfOwnRobot = positionOfOwnRobot;
	}

}
