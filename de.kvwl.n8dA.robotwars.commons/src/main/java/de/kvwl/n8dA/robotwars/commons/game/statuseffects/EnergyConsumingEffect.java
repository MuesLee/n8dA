package de.kvwl.n8dA.robotwars.commons.game.statuseffects;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class EnergyConsumingEffect extends StatusEffect {

	private static final int _ENERGY_LOSS = 1;
	private static final long serialVersionUID = 8338221145711995544L;

	public EnergyConsumingEffect(int startDuration) {
		super(startDuration);
		setPositive(false);
		setIconName("stat_epLoss.png");
		
	}

	@Override
	public void performInitialRobotModification(Robot robot) {
		int energyPoints = robot.getEnergyPoints();
		robot.setEnergyPoints(energyPoints-getEnergyLoss());
	}

	@Override
	public void performEachRoundsModification(Robot robot) {
		//nothing to do here.
	}

	@Override
	public StatusEffect clone() {
		StatusEffect clonedEffect = new EnergyConsumingEffect(this.getStartDuration());
		clonedEffect.setRoundsLeft(this.getRoundsLeft());
		return clonedEffect;
	}

	public static int getEnergyLoss() {
		return _ENERGY_LOSS;
	}

}
