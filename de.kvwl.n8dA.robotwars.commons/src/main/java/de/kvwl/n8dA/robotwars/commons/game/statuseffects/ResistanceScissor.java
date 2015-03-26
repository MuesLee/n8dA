package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class ResistanceScissor extends StatusEffect{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6581901488738326678L;

	public ResistanceScissor(int startDuration, int roundsLeft) {
		super(startDuration, roundsLeft);
		setName("Resistance to Scissor");
	}
	
	public ResistanceScissor() {
		super();
		}
	
	@Override
	public double getDamageModificatorForRoboActionType(
			RobotActionType robotActionType) {
		double result = 1.0;
		
		switch (robotActionType) {
		case SCISSOR:
			result = 0.5;
			break;
		case ROCK:
		case PAPER:
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
