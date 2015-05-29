package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class EnergyConsumingEffect extends StatusEffect {

	private static final int _ENERGY_LOSS = 2;
	private static final long serialVersionUID = 1L;

	public EnergyConsumingEffect(int startDuration) {
		super(startDuration);
		setPositive(false);
		setIconName("stat_energy.png");

	}

	@Override
	public void performInitialRobotModification(Robot robot) {
		int energyPoints = robot.getEnergyPoints();
		robot.setEnergyPoints(Math.min(energyPoints - getEnergyLoss(), 0));
	}

	@Override
	public void performEachRoundsModification(Robot robot) {
		// nothing to do here.
	}

	@Override
	public String getModifierText() {

		return "Entzug von " + _ENERGY_LOSS;
	}

	@Override
	public boolean resolveInteractionWith(StatusEffect newStatusEffect) {
		return false;
	}

	@Override
	public StatusEffect clone() {
		StatusEffect clonedEffect = new EnergyConsumingEffect(
				this.getStartDuration());
		clonedEffect.setRoundsLeft(this.getRoundsLeft());
		return clonedEffect;
	}

	public static int getEnergyLoss() {
		return _ENERGY_LOSS;
	}

	@Override
	public String toString() {

		return "ENERGYCONSUMINGEFFECT - RATE: " + _ENERGY_LOSS;
	}

}
