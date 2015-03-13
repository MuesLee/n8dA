package de.kvwl.n8dA.robotwars.actions;

import de.kvwl.n8dA.robotwars.entities.Entity;
import de.kvwl.n8dA.robotwars.gui.Animation;

public abstract class RobotAction extends Entity {
	
	private static final long serialVersionUID = 1L;

	

	public RobotAction(RobotActionType robotActionType) {
		this.robotActionType = robotActionType;
	}

	protected String name;
	protected RobotActionType robotActionType;
	protected Animation animation;
	protected int energyCosts;
	
	
	public boolean beats (RobotAction otherAction)
	{
		return this.robotActionType.beats(otherAction.getRobotActionType());
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Animation getAnimation() {
		return animation;
	}
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	public RobotActionType getRobotActionType() {
		return robotActionType;
	}
	public void setRobotActionType(RobotActionType robotActionType) {
		this.robotActionType = robotActionType;
	}
	
	@Override
	public String toString() {
		
		return name + " Typ: " + robotActionType;
	}

	public int getEnergyCosts() {
		return energyCosts;
	}

	public void setEnergyCosts(int energyCosts) {
		this.energyCosts = energyCosts;
	}
}
