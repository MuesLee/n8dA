package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class TypeEffect extends StatusEffect
{

	private static final long serialVersionUID = 1L;
	private RobotActionType actionType;
	private TypeEffectModificationType modificationType;

	public TypeEffect(RobotActionType actionType, TypeEffectModificationType modificationType, int startDuration)
	{
		super(startDuration);
		this.actionType = actionType;
		this.modificationType = modificationType;
		this.setName("Elementareffekt: ");
	}

	public TypeEffect()
	{

	}

	@Override
	public double getDamageModificatorForRoboActionType(RobotActionType robotActionType)
	{
		double result = 1.0;

		if (robotActionType == actionType)
		{
			if (modificationType == TypeEffectModificationType.RESISTANCE)
				result = 0.5;
			else if (modificationType == TypeEffectModificationType.VULNERABILITY)
				result = 1.5;
		}

		return result;
	}

	/**
	 * Computes the Interaction between two StatusEffects. Considers just TypeEffects.
	 * 
	 * Modifies the duration of the Effect between same RobotActionTypes or creates a new
	 * StatusEffect
	 * 
	 * Resistance + Vulnerability = No Status Effect Vulnerability + Vulnerability = Longer Duration
	 * Resistance + Resistance = Longer Duration
	 * 
	 * 
	 * 
	 * @param otherStatusEffect
	 * @return Returns a new StatusEffect if necessary
	 */
	@Override
	public StatusEffect resolveInteractionWith(StatusEffect otherStatusEffect)
	{

		if (otherStatusEffect == null || !(otherStatusEffect instanceof TypeEffect))
			return otherStatusEffect;

		TypeEffect otherTypeEffect = (TypeEffect) otherStatusEffect;

		if (this.actionType != otherTypeEffect.getActionType())
		{
			return otherStatusEffect;
		}

		TypeEffect resolvedTypeEffect = null;

		// add duration
		if (this.modificationType == otherTypeEffect.getModificationType())
		{
			int thisRoundsLeft = this.getRoundsLeft();
			int otherRoundsLeft = otherTypeEffect.getRoundsLeft();

			this.setRoundsLeft(thisRoundsLeft + otherRoundsLeft);
		}

		// sub duration
		else
		{
			int thisRoundsLeft = this.getRoundsLeft();
			int otherRoundsLeft = otherTypeEffect.getRoundsLeft();

			int computedDuration = thisRoundsLeft - otherRoundsLeft;
			this.setRoundsLeft(computedDuration);

			if (computedDuration < 0)
			{
				resolvedTypeEffect = new TypeEffect(this.actionType, otherTypeEffect.getModificationType(),
					computedDuration * -1);
			}
		}

		return resolvedTypeEffect;
	}

	@Override
	public void performInitialRobotModification(Robot robot)
	{

	}

	@Override
	public void performEachRoundsModification(Robot robot)
	{

	}

	public RobotActionType getActionType()
	{
		return actionType;
	}

	public void setActionType(RobotActionType actionType)
	{
		this.actionType = actionType;
	}

	public TypeEffectModificationType getModificationType()
	{
		return modificationType;
	}

	public void setModificationType(TypeEffectModificationType modificationType)
	{
		this.modificationType = modificationType;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result + ((modificationType == null) ? 0 : modificationType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeEffect other = (TypeEffect) obj;
		if (actionType != other.actionType)
			return false;
		if (modificationType != other.modificationType)
			return false;
		if (getRoundsLeft() != other.getRoundsLeft())
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return name + " " + getModificationType() + " " + getActionType() + " " + getRoundsLeft();
	}

	@Override
	public TypeEffect clone()
	{

		TypeEffect efk = new TypeEffect(actionType, modificationType, getStartDuration());
		efk.setRoundsLeft(getRoundsLeft());

		return efk;
	}

}
