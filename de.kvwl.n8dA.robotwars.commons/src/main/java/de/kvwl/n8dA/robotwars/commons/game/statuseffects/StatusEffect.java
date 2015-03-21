package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;

public abstract class StatusEffect {

	private int startDuration;
	private int roundsLeft;

	public StatusEffect(int startDuration, int roundsLeft) {
		super();
		this.startDuration = startDuration;
		this.roundsLeft = roundsLeft;
	}

	public int getStartDuration() {
		return startDuration;
	}

	public void setStartDuration(int startDuration) {
		this.startDuration = startDuration;
	}

	public int getRoundsLeft() {
		return roundsLeft;
	}

	public void setRoundsLeft(int roundsLeft) {
		this.roundsLeft = roundsLeft;
	}

	public double getDamageModificatorForRoboActionType(
			RobotActionType robotActionType) {
		return 1.0;
	}

}
