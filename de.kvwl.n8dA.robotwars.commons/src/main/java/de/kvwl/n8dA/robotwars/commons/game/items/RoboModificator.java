package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public interface RoboModificator {

	public void performInitialRobotModification(Robot robot);
	
	public void performEachRoundsModification(Robot robot);
	
}
