package de.kvwl.n8dA.robotwars.server.visualization;

import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.SceneObject;

import java.awt.geom.Rectangle2D;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.Animation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.Action;

public interface CinematicVisualizer {

	/**
	 * Es haben sich zwei Robos angemeldet. Der Kampf beginnt
	 */
	public void battleIsAboutToStart();

	/**
	 * Ein Roboter hat sich auf dem Server angemeldet und tritt der Arena bei.
	 * Nur das Bild wird gesetzt.
	 * 
	 */
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position,
			DataLoader loader);

	/**
	 * Eine Kanpanimation abspielen. Reflection usw. wird automatisch beachtet
	 * 
	 */
	public void playFightanimation(Action acLeft, Action acRight, boolean wait);

	/**
	 * Update leben und energie eines roboters
	 */
	public void updateStats(Robot robot, RobotPosition position,
			boolean animated, boolean wait);

	public void updateEffects(Robot robot, RobotPosition position);

	public void updateEnergypoints(Robot robot, RobotPosition position,
			boolean animated, boolean wait);

	public void updateHealthpoints(Robot robot, RobotPosition position,
			boolean animated, boolean wait);

	/**
	 * Null werte werden durch defaults ersetzt. Nur der text und die Animation
	 * sind Pflicht. Bounds sind relative positionsangaben. Relativ zur
	 * {@link Scene}. Nur Werte zwischen 0.0 und 1.0 sind sichtbar.
	 *
	 * @see CachedLabelObject Bessere Performance beim animieren
	 */
	public void showAnimation(SceneObject obj, Animation animation,
			Rectangle2D bounds, boolean wait);

	/**
	 * Phase von beginn der Aktionsauswahl bis zum Beginn einer Runde.
	 */
	public void prepareForNextRound(boolean wait);

	/**
	 * Für beide Robos stehen Aktionen bereit. Das Runde beginnt.
	 */
	public void roundIsAboutToStart(boolean wait);

	/**
	 * Auf startzustand zurücksetzen
	 */
	public void reset();

	public void playSound(String string);
}
