package de.kvwl.n8dA.robotwars.client.communication;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;

/**
 * Asynchrone Kommunikation zum Server 
 *
 */
public interface AsyncServerCommunicator {
	
	public void sendReadyToBeginBattleToServer();
	
	public void sendRobotActionToServer(RobotAction robotAction);
	
	public void closeConnections();

	public void sendDisconnectFromServer();

}
