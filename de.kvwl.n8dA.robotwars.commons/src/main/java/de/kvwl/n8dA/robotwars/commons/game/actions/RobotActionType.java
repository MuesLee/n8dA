package de.kvwl.n8dA.robotwars.commons.game.actions;

public enum RobotActionType {

	FIRE("Feuer", 0), WATER("Wasser", 1), LIGHTNING("Blitz", 2);

	private String humanReadable;
private int index;
	
	private RobotActionType(String humanReadable, int index)
	{	
		this.setIndex(index);
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
