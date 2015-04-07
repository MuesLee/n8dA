package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class EPBoostItem extends RoboItem
{

	private static final long serialVersionUID = 1L;

	private int epBoost = 5;

	public EPBoostItem()
	{
		setName("EP Boost: " + epBoost);
		setId(52);
		setConfigurationPointCosts(25);
	}

	@Override
	public void performInitialRobotModification(Robot robot)
	{
		int energyPoints = robot.getMaxEnergyPoints();
		energyPoints += epBoost;
		robot.setMaxEnergyPoints(energyPoints);
		robot.setEnergyPoints(energyPoints);
	}

	@Override
	public void performEachRoundsModification(Robot robot)
	{

	}

	@Override
	public EPBoostItem clone()
	{
		return new EPBoostItem();
	}

}
