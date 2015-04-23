package de.kvwl.n8dA.robotwars.commons.game.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;

public class Robot extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6832716536424729251L;

	private UUID uuid;

	private int initialHealthPoints;
	private int initialEnergyPoints;

	private int maxHealthPoints;
	private int maxEnergyPoints;

	private int healthPoints;
	private int energyPoints;
	private boolean readyToFight;

	private Animation animation;

	private List<Attack> possibleAttacks;
	private List<Defense> possibleDefends;

	private List<RoboItem> equippedItems;

	private List<StatusEffect> statusEffects;

	private RobotAction currentAction;

	private RobotPosition robotPosition;

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
		this.statusEffects = new ArrayList<StatusEffect>(3);
	}

	public int getHealthPoints()
	{
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints)
	{
		this.healthPoints = Math.min(healthPoints, maxHealthPoints);
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

		return nickname;
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

		this.energyPoints = Math.min(energyPoints, maxEnergyPoints);
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

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (loadedAsUserRobot ? 1231 : 1237);
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
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
		if (loadedAsUserRobot != other.loadedAsUserRobot)
			return false;
		if (nickname == null)
		{
			if (other.nickname != null)
				return false;
		}
		else if (!nickname.equals(other.nickname))
			return false;
		if (uuid == null)
		{
			if (other.uuid != null)
				return false;
		}
		else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	public List<StatusEffect> getStatusEffects()
	{
		return statusEffects;
	}

	public void addStatusEffect(StatusEffect statusEffect)
	{
		statusEffects.add(statusEffect);
	}

	public void setStatusEffects(List<StatusEffect> statusEffects)
	{
		this.statusEffects = statusEffects;
	}

	public RobotPosition getRobotPosition()
	{
		return robotPosition;
	}

	public void setRobotPosition(RobotPosition robotPosition)
	{
		this.robotPosition = robotPosition;
	}

	public int getInitialHealthPoints()
	{
		return initialHealthPoints;
	}

	public void setInitialHealthPoints(int initialHealthPoints)
	{
		this.initialHealthPoints = initialHealthPoints;
	}

	public int getInitialEnergyPoints()
	{
		return initialEnergyPoints;
	}

	public void setInitialEnergyPoints(int initialEnergyPoints)
	{
		this.initialEnergyPoints = initialEnergyPoints;
	}

}
