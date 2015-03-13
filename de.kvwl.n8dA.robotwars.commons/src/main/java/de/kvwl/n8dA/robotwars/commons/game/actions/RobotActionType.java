package de.kvwl.n8dA.robotwars.commons.game.actions;

public enum RobotActionType {

	ROCK, PAPER, SCISSOR;

	public boolean beats(RobotActionType otherRobotActionType) {

		switch (this) {
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
}
