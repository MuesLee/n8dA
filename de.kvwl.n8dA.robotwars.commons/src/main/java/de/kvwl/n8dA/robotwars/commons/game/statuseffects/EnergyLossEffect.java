package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class EnergyLossEffect extends StatusEffect {
	
	private static final long serialVersionUID = -2194036022393790937L;
	private static int energyLoss = 6;
	
	public EnergyLossEffect() {
	super(1);
	setPositive(false);
	setIconName("stat_energy.png");
	}

	@Override
	public void performInitialRobotModification(Robot robot) {
		int energyPoints = robot.getEnergyPoints();
		energyPoints = Math.max(0, energyPoints-getEnergyLoss());
		robot.setEnergyPoints(energyPoints);
	}

	@Override
	public void performEachRoundsModification(Robot robot) {

	}
	
	@Override
	public String getModifierText() {

		return "Verlust von " + getEnergyLoss();
	}

	@Override
	public StatusEffect clone() {
		StatusEffect clonedEffect = new EnergyConsumingEffect(
				this.getStartDuration());
		clonedEffect.setRoundsLeft(this.getRoundsLeft());
		return clonedEffect;
	}
	@Override
	public String toString() {

		return "ENERGYLOSSEFFECT - RATE: " + getEnergyLoss();
	}

	public static int getEnergyLoss() {
		return energyLoss;
	}

	public static void setEnergyLoss(int energyLoss) {
		EnergyLossEffect.energyLoss = energyLoss;
	}

}
