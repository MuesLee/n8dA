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
		TypeEffect fireRes = new TypeEffect(RobotActionType.FIRE, TypeEffectModificationType.RESISTANCE, 1);
		fireRes.setId(100L);
		TypeEffect lightningRes = new TypeEffect(RobotActionType.LIGHTNING, TypeEffectModificationType.RESISTANCE, 1);
		lightningRes.setId(101L);
		TypeEffect waterRes = new TypeEffect(RobotActionType.WATER, TypeEffectModificationType.RESISTANCE, 1);
		waterRes.setId(102L);
		TypeEffect fireVul = new TypeEffect(RobotActionType.FIRE, TypeEffectModificationType.VULNERABILITY, 1);
		fireVul.setId(103L);
		TypeEffect lightningVul = new TypeEffect(RobotActionType.LIGHTNING, TypeEffectModificationType.VULNERABILITY, 1);
		lightningVul.setId(104L);
		TypeEffect waterVul = new TypeEffect(RobotActionType.WATER, TypeEffectModificationType.VULNERABILITY, 1);
		waterVul.setId(105L);

		TypeEffect fireResHeavy = new TypeEffect(RobotActionType.FIRE, TypeEffectModificationType.RESISTANCE, 2);
		fireResHeavy.setId(106L);
		TypeEffect lightningResHeavy = new TypeEffect(RobotActionType.LIGHTNING, TypeEffectModificationType.RESISTANCE, 2);
		lightningResHeavy.setId(107L);
		TypeEffect waterResHeavy = new TypeEffect(RobotActionType.WATER, TypeEffectModificationType.RESISTANCE, 2);
		waterResHeavy.setId(108L);
		TypeEffect fireVulHeavy = new TypeEffect(RobotActionType.FIRE, TypeEffectModificationType.VULNERABILITY, 2);
		fireVulHeavy.setId(109L);
		TypeEffect lightningVulHeavy = new TypeEffect(RobotActionType.LIGHTNING, TypeEffectModificationType.VULNERABILITY,
			2);
		lightningVulHeavy.setId(110L);
		TypeEffect waterVulHeavy = new TypeEffect(RobotActionType.WATER, TypeEffectModificationType.VULNERABILITY, 2);
		waterVulHeavy.setId(111L);
		
		
		TypeEffect fireResMega = new TypeEffect(RobotActionType.FIRE, TypeEffectModificationType.RESISTANCE, 3);
		fireResMega.setId(112L);
		TypeEffect lightningResMega = new TypeEffect(RobotActionType.LIGHTNING, TypeEffectModificationType.RESISTANCE, 3);
		lightningResMega.setId(113L);
		TypeEffect waterResMega = new TypeEffect(RobotActionType.WATER, TypeEffectModificationType.RESISTANCE, 3);
		waterResMega.setId(114L);
		
		
		statusEffects.add(lightningRes);
		statusEffects.add(fireRes);
		statusEffects.add(waterRes);
		statusEffects.add(fireVul);
		statusEffects.add(lightningVul);
		statusEffects.add(waterVul);
		statusEffects.add(lightningResHeavy);
		statusEffects.add(fireResHeavy);
		statusEffects.add(waterResHeavy);
		statusEffects.add(fireVulHeavy);
		statusEffects.add(lightningVulHeavy);
		statusEffects.add(waterVulHeavy);
		statusEffects.add(lightningResMega);
		statusEffects.add(fireResMega);
		statusEffects.add(waterResMega);
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
