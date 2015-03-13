package de.kvwl.n8dA.robotwars.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public interface RoboBattleHandler extends Remote {
	
	public void setActionForRobot(RobotAction RobotAction, UUID uuid) throws RemoteException, UnknownRobotException, RobotHasInsufficientEnergyException;
	
	public void registerRobotAndClientForBattle(Robot robot, UUID uuid) throws RemoteException, NoFreeSlotInBattleArenaException;
	
	public Robot getSynchronizedRobot(UUID clientUUID) throws RemoteException, UnknownRobotException;
	
}
