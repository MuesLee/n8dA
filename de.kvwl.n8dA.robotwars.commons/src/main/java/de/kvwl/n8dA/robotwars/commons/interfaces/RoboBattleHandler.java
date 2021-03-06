package de.kvwl.n8dA.robotwars.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import de.kvwl.n8dA.robotwars.commons.exception.NoFreeSlotInBattleArenaException;
import de.kvwl.n8dA.robotwars.commons.exception.ServerIsNotReadyForYouException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

public interface RoboBattleHandler extends Remote {
	
	public RobotPosition registerRobotAndClientForBattle(Robot robot, UUID clientUUID, String playerId) throws RemoteException, NoFreeSlotInBattleArenaException, ServerIsNotReadyForYouException;
	
	public Robot getSynchronizedRobot(UUID clientUUID) throws RemoteException, UnknownRobotException;
	public Robot getSynchronizedRobotOfEnemy(UUID ownUUID) throws RemoteException, UnknownRobotException;
	
	public List<Robot> getAllPossibleRobots(String playerId) throws RemoteException;
	public List<RoboItem> getAllPossibleItems() throws RemoteException;
	public List<Attack> getAllPossibleAttacks() throws RemoteException;
	public List<Defense> getAllPossibleDefends() throws RemoteException;
	public List<StatusEffect> getAllPossibleStatusEffects() throws RemoteException;
	
	
	
}
