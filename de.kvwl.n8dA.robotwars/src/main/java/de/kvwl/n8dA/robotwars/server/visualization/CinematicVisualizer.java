package de.kvwl.n8dA.robotwars.server.visualization;

import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

//TODO GUI umsetzen
public interface CinematicVisualizer
{

	/**
	 * Es haben sich zwei Robos angemeldet. Der Kampf beginnt
	 */
	public void battleIsAboutToStart();

	/**
	 * Ein Roboter hat sich auf dem Server angemeldet und tritt der Arena bei
	 * 
	 * @param robot
	 * @param position
	 */
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position);

	/**
	 * Für beide Robos stehen Aktionen bereit. Die Runde beginnt.
	 */
	public void roundIsAboutToStart();

	/**
	 * Nach 70% der Animationszeit der ersten Animation, soll die zweite Animation starten.
	 */
	public void playAnimationForRobotsWithDelayAfterFirst(List<AnimationPosition> animations);

	/**
	 * Beide Animationen sollen parallel starten
	 * 
	 * @param animations
	 */
	public void playAnimationForRobotsSimultaneously(List<AnimationPosition> animations);

}
