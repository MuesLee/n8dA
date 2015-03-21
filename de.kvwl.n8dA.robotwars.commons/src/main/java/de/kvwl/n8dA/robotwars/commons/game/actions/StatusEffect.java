package de.kvwl.n8dA.robotwars.commons.game.actions;


public class StatusEffect {
	
	private StatusEffectType statusEffectType;
	
	private int startDuration;
	private int roundsLeft;
	
	public StatusEffect(StatusEffectType statusEffectType, int startDuration,
			int roundsLeft) {
		super();
		this.statusEffectType = statusEffectType;
		this.startDuration = startDuration;
		this.roundsLeft = roundsLeft;
	}
	public StatusEffectType getStatusEffectType() {
		return statusEffectType;
	}
	public void setStatusEffectType(StatusEffectType statusEffectType) {
		this.statusEffectType = statusEffectType;
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
	
	

}
