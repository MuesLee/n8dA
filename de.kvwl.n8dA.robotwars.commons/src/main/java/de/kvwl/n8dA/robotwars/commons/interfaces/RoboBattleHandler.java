package de.kvwl.n8dA.robotwars.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.exception.UnknownRobotException;

public interface RoboBattleHandler extends Remote {
	
	public void setActionForRobot(RobotAction RobotAction, UUID uuid) throws RemoteException, UnknownRobotException, RobotHasInsufficientEnergyException;
	
	public void registerRobotAndClientForBattle(Robot robot, UUID uuid) throws RemoteException, NoFreeSlotInBattleArenaException;
	
	public Robot getSynchronizedRobot(UUID clientUUID) throws RemoteException, UnknownRobotException;
	
}
