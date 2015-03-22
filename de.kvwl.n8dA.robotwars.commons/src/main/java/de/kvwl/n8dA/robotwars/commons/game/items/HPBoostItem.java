package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

//TODO Timo: Der Boost ändert nur max, Startwert ändert sich nicht
public class HPBoostItem extends RoboItem {

	private static final long serialVersionUID = 1L;

	private int hpBoost = 10;

	public HPBoostItem() {
		setId(80L);
		setName("HP Boost: " + hpBoost);
	}

	@Override
	public void performInitialRobotModification(Robot robot) {

		int maxHealthPoints = robot.getMaxHealthPoints();
		robot.setMaxHealthPoints(maxHealthPoints + hpBoost);
	}

	@Override
	public void performEachRoundsModification(Robot robot) {

		// nichts zu tun
	}
}
