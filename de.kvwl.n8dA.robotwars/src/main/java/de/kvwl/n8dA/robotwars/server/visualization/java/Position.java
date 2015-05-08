package de.kvwl.n8dA.robotwars.server.visualization.java;

import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

public enum Position {

	LEFT, RIGHT;

	public static Position from(RobotPosition pos)
	{

		Position position;

		if (pos == RobotPosition.LEFT)
		{

			position = Position.LEFT;
		}
		else if (pos == RobotPosition.RIGHT)
		{

			position = Position.RIGHT;
		}
		else
		{

			throw new RuntimeException("Unbekannte Position");
		}

		return position;
	}

}
