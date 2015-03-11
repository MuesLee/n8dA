package de.kvwl.n8dA.robotwars.actions;

public  class Defense extends RobotAction {

	protected double blockValue;

	public Defense(RobotActionType robotActionType, double blockValue) {
	
		super(robotActionType);
		this.blockValue = blockValue;
	}

	public double getBlockValue() {
		return blockValue;
	}

	public void setBlockValue(double blockValue) {
		this.blockValue = blockValue;
	}
	
	
}
