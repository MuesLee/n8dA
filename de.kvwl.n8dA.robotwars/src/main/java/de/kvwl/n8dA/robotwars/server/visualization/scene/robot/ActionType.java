package de.kvwl.n8dA.robotwars.server.visualization.scene.robot;

/**
 * 
 * Verhalten der Aktionen. Die Verteidigungen sind unterteilt in die einfache Verteidigung(totaler
 * Block), Verteidigung mit Eigenschaden() Verteidigung mit Reflektion(kein Eigenschaden)
 *
 */
public enum ActionType {

	Attack, Defense, DefenseWithDamage, ReflectingDefense;

	public boolean isDefendingType()
	{

		if (this == Defense || this == ReflectingDefense || this == DefenseWithDamage)
		{

			return true;
		}

		return false;
	}

	public boolean isDamageConsuming()
	{

		if (this == Attack || this == DefenseWithDamage)
		{

			return true;
		}

		return false;
	}
}
