package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class HealthConsumingEffect extends StatusEffect {

	private static final int _HP_LOSS = 2;
	private static final long serialVersionUID = -5912015607779717900L;

	public HealthConsumingEffect(int startDuration) {
		super(startDuration);
		setPositive(false);
		setIconName("stat_life.png");
	}

	@Override
	public void performEachRoundsModification(Robot robot) {
		int robotHealth = robot.getHealthPoints();
		robotHealth -= getHpLoss();
		robot.setHealthPoints(robotHealth);
	}

	@Override
	public String getModifierText() {

		return "Entzug von " + _HP_LOSS;
	}

	@Override
	public void performInitialRobotModification(Robot robot) {
		// nothing to do here.

	}
	@Override
	public boolean resolveInteractionWith(StatusEffect newStatusEffect) {
		return false;
	}

	@Override
	public StatusEffect clone() {
		StatusEffect clonedEffect = new HealthConsumingEffect(
				this.getStartDuration());
		clonedEffect.setRoundsLeft(this.getRoundsLeft());
		return clonedEffect;
	}

	public static int getHpLoss() {
		return _HP_LOSS;
	}

	@Override
	public String toString() {

		return "HEALTHCONSUMINGEFFECT - RATE: " + _HP_LOSS;
	}
}
