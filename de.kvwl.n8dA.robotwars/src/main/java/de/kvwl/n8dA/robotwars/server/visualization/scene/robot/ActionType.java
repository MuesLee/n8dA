package de.kvwl.n8dA.robotwars.server.visualization.scene.robot;

public enum ActionType {

	Attack, Defense, ReflectingDefense;

	public boolean isDefendingType()
	{

		if (this == Defense || this == ReflectingDefense)
		{

			return true;
		}

		return false;
	}
}
