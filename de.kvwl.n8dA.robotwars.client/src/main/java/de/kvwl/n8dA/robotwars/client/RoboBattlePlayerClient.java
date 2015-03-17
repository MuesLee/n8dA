package de.kvwl.n8dA.robotwars.client;

import java.rmi.RemoteException;
import java.util.UUID;

import javax.jms.Message;

import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

/**
 * Player-Client for inputs and just obligatory information
 *
 */
public class RoboBattlePlayerClient extends RoboBattleClient
{

	private Robot robot;

	private RobotPosition positionOfOwnRobot;

	public RoboBattlePlayerClient()
	{

		robot = new Robot();
	}

	public static void main(String[] args)
	{
		RoboBattlePlayerClient client = new RoboBattlePlayerClient();
		client.init();

		//TODO Timo: Test Zeug entfernen
		client.registerClientWithRobotAtServer();
		client.getUpdatedRobot();
		client.sendRobotActionToServer(new Attack(RobotActionType.ROCK, 10));
	}

	public void registerClientWithRobotAtServer()
	{
		try
		{
			LOG.info("Client: " + uuid + " wants to register at server");
			RobotPosition ownRobotsPosition = server.registerRobotAndClientForBattle(robot, uuid);
			setPositionOfOwnRobot(ownRobotsPosition);
			LOG.info("Client: " + uuid + " plays the " + ownRobotsPosition.getDescription() + " robot.");

		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (NoFreeSlotInBattleArenaException e)
		{
		}
	}

	public void sendPlayerIsReadyToBattleToServer()
	{
		producer.sendReadyToBeginBattleToServer();
	}

	public void sendRobotActionToServer(RobotAction robotAction)
	{
		producer.sendRobotActionToServer(robotAction);
	}

	public Robot getUpdatedRobot()
	{
		try
		{
			LOG.info("Client: " + uuid + " requests robot update");
			robot = server.getSynchronizedRobot(uuid);
			return robot;
		}
		catch (RemoteException e)
		{
		}
		catch (UnknownRobotException e)
		{
		}

		return null;
	}

	@Override
	public void onMessage(Message message)
	{

		try
		{

			int intProperty = message.getIntProperty(GameStateType.getNotificationName());
			GameStateType gameStateType = GameStateType.values()[intProperty];

			LOG.info("Client: " + uuid + " received: " + gameStateType.name());

		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			LOG.debug("Wenn man keine Ahnung hat, einfach mal die Finger vom Code lassen!\n" + e.getStackTrace());
		}
		catch (Exception e)
		{
			LOG.error("####Bumm####", e);
		}

	}

	public UUID getUuid()
	{
		return uuid;
	}

	public RobotPosition getPositionOfOwnRobot()
	{
		return positionOfOwnRobot;
	}

	public void setPositionOfOwnRobot(RobotPosition positionOfOwnRobot)
	{
		this.positionOfOwnRobot = positionOfOwnRobot;
	}

}
