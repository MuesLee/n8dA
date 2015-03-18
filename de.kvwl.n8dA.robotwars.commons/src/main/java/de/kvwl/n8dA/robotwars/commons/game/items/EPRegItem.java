package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class EPRegItem extends RoboItem {

	private static final long serialVersionUID = 1L;
	private int epReg =10;

	@Override
	public void performInitialRobotModification(Robot robot) {
	}

	@Override
	public void performEachRoundsModification(Robot robot) {
		int energyPoints = robot.getEnergyPoints();
		energyPoints += epReg ;
		robot.setEnergyPoints(energyPoints);
	}

}
