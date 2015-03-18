package de.kvwl.n8dA.robotwars.server.input;

import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

public interface DataLoader {

	public List<Animation> loadAnimationsForRobots();

	public List<Animation> loadAnimationsForRobotActions();

	public List<Robot> loadRobots();

	public List<Robot> loadUserRobots(String userId);

	public List<Attack> loadRobotAttacks();

	public List<Defense> loadRobotDefends();

}
