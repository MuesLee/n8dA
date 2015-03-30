package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public abstract class TypeResistance extends StatusEffect  {

	private static final long serialVersionUID = 1L;

	public TypeResistance(int startDuration, int roundsLeft) {
		super(startDuration, roundsLeft);
	}
	
	public TypeResistance() {
	}
		
	@Override
	public void performInitialRobotModification(Robot robot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performEachRoundsModification(Robot robot) {
		// TODO Auto-generated method stub
		
	}

}
