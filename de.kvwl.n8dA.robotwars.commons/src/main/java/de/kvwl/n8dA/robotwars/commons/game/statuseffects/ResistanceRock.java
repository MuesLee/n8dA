package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class ResistanceRock extends StatusEffect{




	private static final long serialVersionUID = -4617625202592386425L;

	public ResistanceRock(int startDuration, int roundsLeft) {
		super(startDuration, roundsLeft);
		setName("Resistance to Rock");
	}
	
	@Override
	public double getDamageModificatorForRoboActionType(
			RobotActionType robotActionType) {
		double result = 1.0;
		
		switch (robotActionType) {
		case ROCK:
			result = 0.5;
			break;
		case PAPER:
		case SCISSOR:
		default:
			break;
		}
		
		return result;
	
	}
	
	@Override
	public void performInitialRobotModification(Robot robot) {
		
	}

	@Override
	public void performEachRoundsModification(Robot robot) {
		
	}

}
