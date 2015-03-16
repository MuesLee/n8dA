package de.kvwl.n8dA.robotwars.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

public interface RoboBattleHandler extends Remote {
	
	public RobotPosition registerRobotAndClientForBattle(Robot robot, UUID uuid) throws RemoteException, NoFreeSlotInBattleArenaException;
	
	public Robot getSynchronizedRobot(UUID clientUUID) throws RemoteException, UnknownRobotException;
	
}
