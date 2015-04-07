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
		TypeEffect rockRes = new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.RESISTANCE, 1);
		rockRes.setId(100L);
		TypeEffect scissorRes = new TypeEffect(RobotActionType.SCISSOR, TypeEffectModificationType.RESISTANCE, 1);
		scissorRes.setId(101L);
		TypeEffect paperRes = new TypeEffect(RobotActionType.PAPER, TypeEffectModificationType.RESISTANCE, 1);
		paperRes.setId(102L);
		TypeEffect rockVul = new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.VULNERABILITY, 1);
		rockVul.setId(103L);
		TypeEffect scissorVul = new TypeEffect(RobotActionType.SCISSOR, TypeEffectModificationType.VULNERABILITY, 1);
		scissorVul.setId(104L);
		TypeEffect paperVul = new TypeEffect(RobotActionType.PAPER, TypeEffectModificationType.VULNERABILITY, 1);
		paperVul.setId(105L);

		TypeEffect rockResHeavy = new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.RESISTANCE, 2);
		rockResHeavy.setId(106L);
		TypeEffect scissorResHeavy = new TypeEffect(RobotActionType.SCISSOR, TypeEffectModificationType.RESISTANCE, 2);
		scissorResHeavy.setId(107L);
		TypeEffect paperResHeavy = new TypeEffect(RobotActionType.PAPER, TypeEffectModificationType.RESISTANCE, 2);
		paperResHeavy.setId(108L);
		TypeEffect rockVulHeavy = new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.VULNERABILITY, 2);
		rockVulHeavy.setId(109L);
		TypeEffect scissorVulHeavy = new TypeEffect(RobotActionType.SCISSOR, TypeEffectModificationType.VULNERABILITY,
			2);
		scissorVulHeavy.setId(110L);
		TypeEffect paperVulHeavy = new TypeEffect(RobotActionType.PAPER, TypeEffectModificationType.VULNERABILITY, 2);
		paperVulHeavy.setId(111L);

		statusEffects.add(scissorRes);
		statusEffects.add(rockRes);
		statusEffects.add(paperRes);
		statusEffects.add(rockVul);
		statusEffects.add(scissorVul);
		statusEffects.add(paperVul);
		statusEffects.add(scissorResHeavy);
		statusEffects.add(rockResHeavy);
		statusEffects.add(paperResHeavy);
		statusEffects.add(rockVulHeavy);
		statusEffects.add(scissorVulHeavy);
		statusEffects.add(paperVulHeavy);
		return statusEffects;
	}

	public static StatusEffect cloneStatusEffectById(long statusEffectId)
	{

		List<StatusEffect> StatusEffects = getAllStatusEffects();

		for (StatusEffect item : StatusEffects)
		{

			if (item.getId() == statusEffectId)
			{

				return (StatusEffect) item.clone();
			}
		}

		return null;
	}

}
