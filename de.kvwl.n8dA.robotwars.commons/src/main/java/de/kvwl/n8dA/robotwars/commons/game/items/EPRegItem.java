package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class EPRegItem extends RoboItem
{

	private static final long serialVersionUID = 1L;
	private int epReg = 1;

	public EPRegItem()
	{
		setId(53);
		setName("Energiepunkte Regeneration: " + epReg);
		setConfigurationPointCosts(125);
	}

	@Override
	public void performInitialRobotModification(Robot robot)
	{
	}

	@Override
	public void performEachRoundsModification(Robot robot)
	{

		robot.setEnergyPoints(Math.min(robot.getEnergyPoints() + epReg, robot.getMaxEnergyPoints()));
	}

	@Override
	public EPRegItem clone()
	{
		return new EPRegItem();
	}

}
