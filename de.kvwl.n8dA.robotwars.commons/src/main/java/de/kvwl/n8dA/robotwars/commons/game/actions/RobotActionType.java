package de.kvwl.n8dA.robotwars.commons.game.actions;

public enum RobotActionType {

	FIRE("Feuer"), WATER("Wasser"), LIGHTNING("Blitz");

	private String humanReadable;

	private RobotActionType(String humanReadable)
	{
		this.humanReadable = humanReadable;
	}

	private RobotActionType()
	{

		humanReadable = this.toString();
	}

	public boolean beats(RobotActionType otherRobotActionType)
	{

		switch (this)
		{
			case WATER:
				return otherRobotActionType.equals(RobotActionType.FIRE) ? true : false;
			case FIRE:
				return otherRobotActionType.equals(RobotActionType.LIGHTNING) ? true : false;
			case LIGHTNING:
				return otherRobotActionType.equals(RobotActionType.WATER) ? true : false;
			default:
				return false;
		}
	}

	public String getHumanReadableString()
	{

		return humanReadable;
	}

	public String getIconName()
	{
		String icName;

		switch (this)
		{
			case WATER:
				icName = "stat_Paper.png";
			break;
			case FIRE:
				icName = "stat_Rock.png";
			break;
			case LIGHTNING:
				icName = "stat_Scissor.png";
			break;
			default:
				throw new RuntimeException("Unbekannter enum value");
		}

		return icName;
	}
}
