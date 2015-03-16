package de.kvwl.n8dA.robotwars.commons.game.actions;

public  class Defense extends RobotAction {

	private static final long serialVersionUID = 1L;
	protected double bonusOnDefenseFactor;

	public Defense(RobotActionType robotActionType, double bonusOnDefenseFactor) {
	
		super(robotActionType);
		this.bonusOnDefenseFactor = bonusOnDefenseFactor;
	}

	public double getBonusOnDefenseFactor() {
		return bonusOnDefenseFactor;
	}

	public void setBonusOnDefenseFactor(double bonusOnDefenseFactor) {
		this.bonusOnDefenseFactor = bonusOnDefenseFactor;
	}
	
	
}
