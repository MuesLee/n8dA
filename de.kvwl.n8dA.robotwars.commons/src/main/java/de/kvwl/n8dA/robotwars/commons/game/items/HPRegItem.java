package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class HPRegItem extends RoboItem
{

	private static final long serialVersionUID = 1L;

	private int hpReg = 1;

	public HPRegItem()
	{
		setName("Lebenspunkte: " + hpReg);
		setId(51);
		setConfigurationPointCosts(150);
	}

	@Override
	public void performInitialRobotModification(Robot robot)
	{
		// nichts zu tun

	}

	@Override
	public void performEachRoundsModification(Robot robot)
	{

		robot.setHealthPoints(Math.min(robot.getHealthPoints() + hpReg, robot.getMaxHealthPoints()));
	}

	@Override
	public HPRegItem clone()
	{

		return new HPRegItem();
	}

}
