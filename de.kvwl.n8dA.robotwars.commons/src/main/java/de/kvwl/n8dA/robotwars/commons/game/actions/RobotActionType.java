package de.kvwl.n8dA.robotwars.commons.game.actions;

public enum RobotActionType {

	ROCK("Feuer"), PAPER("Wasser"), SCISSOR("Blitz");

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
			case PAPER:
				return otherRobotActionType.equals(RobotActionType.ROCK) ? true : false;
			case ROCK:
				return otherRobotActionType.equals(RobotActionType.SCISSOR) ? true : false;
			case SCISSOR:
				return otherRobotActionType.equals(RobotActionType.PAPER) ? true : false;
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
			case PAPER:
				icName = "stat_Paper.png";
			break;
			case ROCK:
				icName = "stat_Rock.png";
			break;
			case SCISSOR:
				icName = "stat_Scissor.png";
			break;
			default:
				throw new RuntimeException("Unbekannter enum value");
		}

		return icName;
	}
}
