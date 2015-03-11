package de.kvwl.n8dA.robotwars.entities;

import de.kvwl.n8dA.robotwars.actions.RobotAction;

public class Robot {
	
	protected String name;
	protected int healthPoints;
	
	protected RobotAction action;
	

	public Robot(String name, int healthPoints) {
		super();
		this.name = name;
		this.healthPoints = healthPoints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	public RobotAction getAction() {
		return action;
	}

	public void setAction(RobotAction action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
	
	return name+": " + healthPoints + "HP";
	}
	

}
