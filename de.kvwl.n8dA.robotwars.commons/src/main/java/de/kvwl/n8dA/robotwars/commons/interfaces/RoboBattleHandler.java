package de.kvwl.n8dA.robotwars.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.entities.Robot;

public interface RoboBattleHandler extends Remote {
	
	public void setActionForRobot(RobotAction RobotAction, Robot robot) throws RemoteException;

}
