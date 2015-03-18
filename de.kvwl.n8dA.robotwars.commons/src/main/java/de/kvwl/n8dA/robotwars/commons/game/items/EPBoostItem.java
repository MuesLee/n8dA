package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class EPBoostItem extends RoboItem {

	private static final long serialVersionUID = 1L;

	private int epBoost = 10;
	
	@Override
	public void performInitialRobotModification(Robot robot) {
		int energyPoints = robot.getEnergyPoints();
		energyPoints += epBoost;
		robot.setEnergyPoints(energyPoints);
		
	}

	@Override
	public void performEachRoundsModification(Robot robot) {

	}

}
