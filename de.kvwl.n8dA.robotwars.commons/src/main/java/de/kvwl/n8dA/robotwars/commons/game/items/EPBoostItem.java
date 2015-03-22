package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

//TODO Timo: Der Boost ändert nur max, Startwert ändert sich nicht
public class EPBoostItem extends RoboItem {

	private static final long serialVersionUID = 1L;

	private int epBoost = 10;

	public EPBoostItem() {
		setName("EP Boost: " + epBoost);
	}

	@Override
	public void performInitialRobotModification(Robot robot) {
		int energyPoints = robot.getMaxEnergyPoints();
		energyPoints += epBoost;
		robot.setMaxEnergyPoints(energyPoints);

	}

	@Override
	public void performEachRoundsModification(Robot robot) {

	}

}
