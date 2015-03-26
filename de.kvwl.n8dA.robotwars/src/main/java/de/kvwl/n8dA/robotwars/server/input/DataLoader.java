package de.kvwl.n8dA.robotwars.server.input;

import java.io.IOException;
import java.util.List;

import org.jdom2.JDOMException;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.entities.Entity;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

public interface DataLoader {

	public List<Animation> loadAnimationsForRobots();

	public List<Animation> loadAnimationsForRobotActions();

	public List<Robot> loadRobots();

	public List<Robot> loadUserRobots(String userId);

	public List<Attack> loadRobotAttacks();

	public List<Defense> loadRobotDefends();

	/**
	 * Erzeugt einen neuen Benutzerroboter.
	 * 
	 * @return die vergebene id -> {@link Entity#getId()}
	 */
	public long createUserRobot(Robot robot, String userId) throws IOException,
			JDOMException;

}
