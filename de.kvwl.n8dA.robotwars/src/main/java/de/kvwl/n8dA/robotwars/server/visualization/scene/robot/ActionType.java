package de.kvwl.n8dA.robotwars.server.visualization.scene.robot;

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
