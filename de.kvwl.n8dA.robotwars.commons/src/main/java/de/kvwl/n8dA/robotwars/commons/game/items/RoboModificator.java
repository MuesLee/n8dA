package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public interface RoboModificator {
	
	/**
	 * Einmalige Veränderung zu Spielbeginn am Roboter 
	 */
	public void performInitialRobotModification(Robot robot);
	
	/**
	 *	Veränderung, welche jede Runde getriggert wird 
	 */
	public void performEachRoundsModification(Robot robot);
	
}
