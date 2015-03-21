package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class ResistancePaper extends StatusEffect{



	private static final long serialVersionUID = -2175817525960480378L;

	public ResistancePaper(int startDuration, int roundsLeft) {
		super(startDuration, roundsLeft);
		setName("Resistance to Paper");
	}
	
	@Override
	public double getDamageModificatorForRoboActionType(
			RobotActionType robotActionType) {
		double result = 1.0;
		
		switch (robotActionType) {
		case PAPER:
			result = 0.5;
			break;
		case SCISSOR:
		case ROCK:
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
