package de.kvwl.n8dA.robotwars.commons.game.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

public class Robot extends Entity
{

	private static final long serialVersionUID = 1L;

	private UUID uuid;

	private int healthPoints;
	private int energyPoints;
	private boolean readyToFight;

	private Animation animation;

	private List<RobotAction> possibleAttacks;
	private List<RobotAction> possibleDefends;

	private List<RoboItem> equippedItems;

	private RobotAction currentAction;

	public Robot()
	{
		super();
		uuid = UUID.randomUUID();
		this.setPossibleAttacks(new ArrayList<RobotAction>(4));
		this.setPossibleDefends(new ArrayList<RobotAction>(4));
		this.setEquippedItems(new ArrayList<RoboItem>(4));
	}

	public int getHealthPoints()
	{
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints)
	{
		this.healthPoints = healthPoints;
	}

	public RobotAction getCurrentAction()
	{
		return currentAction;
	}

	public void setCurrentAction(RobotAction action)
	{
		this.currentAction = action;
	}

	@Override
	public String toString()
	{

		return name + ": " + healthPoints + "HP";
	}

	public Animation getAnimation()
	{
		return animation;
	}

	public void setAnimation(Animation animation)
	{
		this.animation = animation;
	}

	public List<RobotAction> getPossibleAttacks()
	{
		return possibleAttacks;
	}

	public void setPossibleAttacks(List<RobotAction> possibleAttacks)
	{
		this.possibleAttacks = possibleAttacks;
	}

	public List<RobotAction> getPossibleDefends()
	{
		return possibleDefends;
	}

	public void setPossibleDefends(List<RobotAction> possibleDefends)
	{
		this.possibleDefends = possibleDefends;
	}

	public int getEnergyPoints()
	{
		return energyPoints;
	}

	public void setEnergyPoints(int energyPoints)
	{
		this.energyPoints = energyPoints;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((possibleAttacks == null) ? 0 : possibleAttacks.hashCode());
		result = prime * result + ((possibleDefends == null) ? 0 : possibleDefends.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Robot other = (Robot) obj;
		if (uuid == null)
		{
			if (other.uuid != null)
				return false;
		}

		if (!uuid.equals(other.uuid))
			return false;
		else
		{
			return true;
		}
	}

	public List<RoboItem> getEquippedItems()
	{
		return equippedItems;
	}

	public void setEquippedItems(List<RoboItem> equippedItems)
	{
		this.equippedItems = equippedItems;
	}

	public boolean isReadyToFight()
	{
		return readyToFight;
	}

	public void setReadyToFight(boolean readyToFight)
	{
		this.readyToFight = readyToFight;
	}

}
