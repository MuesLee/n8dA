package de.kvwl.n8dA.robotwars.client;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;


public interface BattleClientListener
{

	public void startActionSelection();

	public void gameOver(GameStateType result);
	
	public void updateRobot();
	
	public void receiveEnemyRobotAction(RobotAction robotAction);

	public void updateNotSoSmartBot(GameStateType gameStateType);
}
