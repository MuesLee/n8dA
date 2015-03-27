package de.kvwl.n8dA.robotwars.server.visualization;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.Action;

public interface CinematicVisualizer
{

	/**
	 * Es haben sich zwei Robos angemeldet. Der Kampf beginnt
	 */
	public void battleIsAboutToStart();

	/**
	 * Ein Roboter hat sich auf dem Server angemeldet und tritt der Arena bei. Nur das Bild wird
	 * gesetzt.
	 * 
	 * @param robot
	 * @param position
	 */
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position);

	/**
	 * Eine Kanpanimation abspielen. Reflection usw. wird automatisch beachtet
	 * 
	 * @param animations
	 */
	public void playFightanimation(Action acLeft, Action acRight, boolean wait);

	/**
	 * Update leben und energie eines roboters
	 * 
	 * @param robot
	 * @param position
	 * @param animated
	 * @param wait
	 */
	public void updateStats(Robot robot, RobotPosition position, boolean animated, boolean wait);

	/**
	 * Phase von beginn der Aktionsauswahl bis zum Beginn einer Runde.
	 */
	public void prepareForNextRound();

	/**
	 * Für beide Robos stehen Aktionen bereit. Das Runde beginnt.
	 */
	public void roundIsAboutToStart();
}
