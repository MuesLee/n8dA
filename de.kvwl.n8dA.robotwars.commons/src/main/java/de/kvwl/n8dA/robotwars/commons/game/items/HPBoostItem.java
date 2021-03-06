package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class HPBoostItem extends RoboItem
{

	private static final long serialVersionUID = 1L;

	private int hpBoost = 10;

	public HPBoostItem()
	{
		setId(50);
		setName("Lebenspunkte Boost: " + hpBoost);
		setConfigurationPointCosts(75);
	}

	@Override
	public void performInitialRobotModification(Robot robot)
	{

		int healthPoints = robot.getMaxHealthPoints();
		healthPoints += hpBoost;

		robot.setMaxHealthPoints(healthPoints);
		robot.setHealthPoints(healthPoints);
	}

	@Override
	public void performEachRoundsModification(Robot robot)
	{

		// nichts zu tun
	}

	@Override
	public HPBoostItem clone()
	{

		return new HPBoostItem();
	}
}
