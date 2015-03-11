package de.kvwl.n8dA.robotwars.actions;

public  class Defense extends RobotAction {

	protected int blockValue;

	public Defense(RobotActionType robotActionType, int blockValue) {
	
		super(robotActionType);
		this.blockValue = blockValue;
	}

	public int getBlockValue() {
		return blockValue;
	}

	public void setBlockValue(int blockValue) {
		this.blockValue = blockValue;
	}
	
	
}
