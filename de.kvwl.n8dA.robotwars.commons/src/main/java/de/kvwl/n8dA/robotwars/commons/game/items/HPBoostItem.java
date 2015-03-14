package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class HPBoostItem extends RoboItem {

	private static final long serialVersionUID = 1L;

	private int hpBoost;

	public HPBoostItem() {
		setId(80L);
	}

	@Override
	public void performInitialRobotModification(Robot robot) {

		int currentHealthPoints = robot.getHealthPoints();
		robot.setHealthPoints(currentHealthPoints + hpBoost);
	}

	@Override
	public void performEachRoundsModification(Robot robot) {
		
		//nichts zu tun
	}

}
