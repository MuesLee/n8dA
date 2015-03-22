package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class EPRegItem extends RoboItem {

	private static final long serialVersionUID = 1L;
	private int epReg = 10;

	public EPRegItem() {
		setName("EP Reg: " + epReg);
	}

	@Override
	public void performInitialRobotModification(Robot robot) {
	}

	@Override
	public void performEachRoundsModification(Robot robot) {

		robot.setEnergyPoints(Math.min(robot.getEnergyPoints() + epReg,
				robot.getMaxEnergyPoints()));
	}

}
