package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Entity;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboModificator;

public abstract class StatusEffect extends Entity implements RoboModificator
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int startDuration;
	private int roundsLeft;

	private boolean isPositive = true;
	private String iconName;

	public StatusEffect()
	{
	}
	
	public String getModifierText()
	{
		String text = "";
			if(isPositive())
			{
				text = "Guter Effekt";
			}
			else {
				text = "Schlechter Effekt";
			}
		return text;
	}

	public StatusEffect(int startDuration)
	{
		super();
		this.startDuration = startDuration;
		this.roundsLeft = startDuration;
	}

	public int getStartDuration()
	{
		return startDuration;
	}

	public void setStartDuration(int startDuration)
	{
		this.startDuration = startDuration;
	}

	public int getRoundsLeft()
	{
		return roundsLeft;
	}

	public void setRoundsLeft(int roundsLeft)
	{
		this.roundsLeft = roundsLeft;
	}

	public double getDamageModificatorForRoboActionType(RobotActionType robotActionType)
	{
		return 1.0;
	}

	public void decreaseRoundsLeft(int sub)
	{
		roundsLeft -= sub;
	}

	public boolean resolveInteractionWith(StatusEffect newStatusEffect)
	{
		return false;

	}

	@Override
	public abstract StatusEffect clone();

	public boolean isPositive()
	{
		return isPositive;
	}

	public void setPositive(boolean isPositive)
	{
		this.isPositive = isPositive;
	}

	public String getIconName()
	{
		return iconName;
	}

	public void setIconName(String iconName)
	{
		this.iconName = iconName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((iconName == null) ? 0 : iconName.hashCode());
		result = prime * result + (isPositive ? 1231 : 1237);
		result = prime * result + roundsLeft;
		result = prime * result + startDuration;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatusEffect other = (StatusEffect) obj;
		if (iconName == null) {
			if (other.iconName != null)
				return false;
		} else if (!iconName.equals(other.iconName))
			return false;
		if (isPositive != other.isPositive)
			return false;
		if (roundsLeft != other.roundsLeft)
			return false;
		if (startDuration != other.startDuration)
			return false;
		return true;
	}
	
	

}
