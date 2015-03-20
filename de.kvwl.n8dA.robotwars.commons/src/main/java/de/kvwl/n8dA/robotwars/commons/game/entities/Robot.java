package de.kvwl.n8dA.robotwars.commons.game.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

public class Robot extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6832716536424729251L;

	private UUID uuid;

	private int maxHealthPoints;
	private int maxEnergyPoints;

	private int healthPoints;
	private int energyPoints;
	private boolean readyToFight;

	private Animation animation;

	private List<Attack> possibleAttacks;
	private List<Defense> possibleDefends;

	private List<RoboItem> equippedItems;

	private RobotAction currentAction;

	/**
	 * Wenn true sollte dieser Roboter nicht erneut als User roboter gespeichert werden, da er als
	 * solcher bereits geladen wurde. Außerdem werden Konfigurationen an solchen Robotern
	 * unterdrückt. Änderungen sind nicht mehr möglich.
	 */
	private boolean loadedAsUserRobot = false;

	private String nickname = "";

	public Robot()
	{
		super();
		setUuid(UUID.randomUUID());
		this.setPossibleAttacks(new ArrayList<Attack>(4));
		this.setPossibleDefends(new ArrayList<Defense>(4));
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
		result = prime * result + ((getPossibleAttacks() == null) ? 0 : getPossibleAttacks().hashCode());
		result = prime * result + ((getPossibleDefends() == null) ? 0 : getPossibleDefends().hashCode());
		result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
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
		if (getUuid() == null)
		{
			if (other.getUuid() != null)
				return false;
		}

		if (!getUuid().equals(other.getUuid()))
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

	public List<Attack> getPossibleAttacks()
	{
		return possibleAttacks;
	}

	public void setPossibleAttacks(List<Attack> possibleAttacks)
	{
		this.possibleAttacks = possibleAttacks;
	}

	public List<Defense> getPossibleDefends()
	{
		return possibleDefends;
	}

	public void setPossibleDefends(List<Defense> possibleDefends)
	{
		this.possibleDefends = possibleDefends;
	}

	public boolean isLoadedAsUserRobot()
	{
		return loadedAsUserRobot;
	}

	public void setLoadedAsUserRobot(boolean loadedAsUserRobot)
	{
		this.loadedAsUserRobot = loadedAsUserRobot;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public int getMaxEnergyPoints()
	{
		return maxEnergyPoints;
	}

	public void setMaxEnergyPoints(int maxEnergyPoints)
	{
		this.maxEnergyPoints = maxEnergyPoints;
	}

	public int getMaxHealthPoints()
	{
		return maxHealthPoints;
	}

	public void setMaxHealthPoints(int maxHealthPoints)
	{
		this.maxHealthPoints = maxHealthPoints;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

}
