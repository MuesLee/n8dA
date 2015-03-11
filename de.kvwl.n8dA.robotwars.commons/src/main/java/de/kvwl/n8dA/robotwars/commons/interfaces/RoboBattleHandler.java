package de.kvwl.n8dA.robotwars.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.exception.UnknownRobotException;

public interface RoboBattleHandler extends Remote {
	
	public void setActionForRobot(RobotAction RobotAction, Robot robot) throws RemoteException, UnknownRobotException, RobotHasInsufficientEnergyException;
	
	public void registerRobotAndClientForBattle(Robot robot) throws RemoteException, NoFreeSlotInBattleArenaException;
	
	public Robot getSynchronizedRobot(Robot robot) throws RemoteException, UnknownRobotException;
	
}
