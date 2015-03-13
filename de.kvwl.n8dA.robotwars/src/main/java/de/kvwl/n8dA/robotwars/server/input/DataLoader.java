package de.kvwl.n8dA.robotwars.server.input;

import java.util.List;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.entities.Robot;

public interface DataLoader {
	
	public List<String> loadAnimationsForRobots();
	public List<String> loadAnimationsForRobotActions();
	
	public List<Robot> loadRobots();
	public List<RobotAction> loadRobotAttacks();
	public List<RobotAction> loadRobotDefends();

}
