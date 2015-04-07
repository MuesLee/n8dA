package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Entity;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboModificator;

public abstract class StatusEffect extends Entity implements RoboModificator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int startDuration;
	private int roundsLeft;
	
	public StatusEffect() {
	}

	public StatusEffect(int startDuration) {
		super();
		this.startDuration = startDuration;
		this.roundsLeft = startDuration;
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
	
	public void decreaseRoundsLeft(int sub)
	{
		roundsLeft-=sub;
	}

	public StatusEffect resolveInteractionWith(StatusEffect newStatusEffect) {
		return null;
		
	}

}
