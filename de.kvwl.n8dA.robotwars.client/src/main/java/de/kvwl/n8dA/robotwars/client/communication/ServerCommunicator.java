package de.kvwl.n8dA.robotwars.client.communication;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;

public interface ServerCommunicator {
	
	public void sendReadyToBeginBattleToServer();
	
	public void sendRobotActionToServer(RobotAction robotAction);
	
	public void closeConnections();

}
