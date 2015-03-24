package de.kvwl.n8dA.robotwars.server.visualization;

import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

public interface CinematicVisualizer {

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
	 * Nach 70% der Animationszeit der ersten Animation, soll (falls vorhanden)
	 * die zweite Animation starten.
	 */
	public void playAnimationForRobotsWithDelayAfterFirst(
			List<AnimationPosition> animations);

	/**
	 * Beide Animationen sollen parallel starten
	 * 
	 * @param animations
	 */
	public void playAnimationForRobotsSimultaneously(
			List<AnimationPosition> animations);

	/**
	 * Phase von beginn der Aktionsauswahl bis zum Beginn einer Runde.
	 */
	public void prepareForNextRound();

	/**
	 * Für beide Robos stehen Aktionen bereit. Das Runde beginnt.
	 */
	public void roundIsAboutToStart();
}
