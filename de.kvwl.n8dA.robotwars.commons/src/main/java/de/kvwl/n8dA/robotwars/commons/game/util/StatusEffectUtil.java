package de.kvwl.n8dA.robotwars.commons.game.util;

import java.util.ArrayList;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffectModificationType;

public class StatusEffectUtil
{

	public static List<StatusEffect> getAllStatusEffects()
	{
		List<StatusEffect> statusEffects = new ArrayList<>();
		statusEffects.add(new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.RESISTANCE, 1));
		statusEffects.add(new TypeEffect(RobotActionType.SCISSOR, TypeEffectModificationType.RESISTANCE, 1));
		statusEffects.add(new TypeEffect(RobotActionType.PAPER, TypeEffectModificationType.RESISTANCE, 1));
		statusEffects.add(new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.VULNERABILITY, 1));
		statusEffects.add(new TypeEffect(RobotActionType.SCISSOR, TypeEffectModificationType.VULNERABILITY, 1));
		statusEffects.add(new TypeEffect(RobotActionType.PAPER, TypeEffectModificationType.VULNERABILITY, 1));
		return statusEffects;
	}

	public static StatusEffect cloneStatusEffectById(long statusEffectId)
	{

		List<StatusEffect> StatusEffects = getAllStatusEffects();

		for (StatusEffect item : StatusEffects)
		{

			if (item.getId() == statusEffectId)
			{

				return clone(item);
			}
		}

		return null;
	}

	private static StatusEffect clone(StatusEffect statusEffect)
	{

		Class<? extends StatusEffect> clone = statusEffect.getClass();

		try
		{
			StatusEffect clonedStatusEffect = clone.newInstance();

			return clonedStatusEffect;
		}
		catch (InstantiationException | IllegalAccessException e)
		{
		}

		return null;
	}
}
