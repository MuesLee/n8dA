package de.kvwl.n8dA.robotwars.commons.game.util;

import java.util.ArrayList;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.statuseffects.ResistancePaper;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.ResistanceRock;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.ResistanceScissor;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.VulnerabilityPaper;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.VulnerabilityRock;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.VulnerabilityScissor;

public class StatusEffectUtil
{

	public static List<StatusEffect> getAllStatusEffects()
	{
		List<StatusEffect> statusEffects = new ArrayList<>();
		statusEffects.add(new VulnerabilityPaper());
		statusEffects.add(new VulnerabilityRock());
		statusEffects.add(new VulnerabilityScissor());
		statusEffects.add(new ResistanceRock());
		statusEffects.add(new ResistanceScissor());
		statusEffects.add(new ResistancePaper());
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
