package de.kvwl.n8dA.robotwars.entities;

import java.io.Serializable;
import java.util.ArrayList;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.gui.Animation;

public class Robot implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int healthPoints;
	
	private Animation animation;
	
	private ArrayList<RobotAction> possibleAttacks;
	private ArrayList<RobotAction> possibleDefends;	
	
	private RobotAction currentAction;
	

	public Robot(String name, int healthPoints) {
		super();
		this.name = name;
		this.healthPoints = healthPoints;
		
		this.setPossibleAttacks(new ArrayList<RobotAction>(4));
		this.setPossibleDefends(new ArrayList<RobotAction>(4));
		
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

	public RobotAction getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(RobotAction action) {
		this.currentAction = action;
	}
	
	@Override
	public String toString() {
	
	return name+": " + healthPoints + "HP";
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public ArrayList<RobotAction> getPossibleAttacks() {
		return possibleAttacks;
	}

	public void setPossibleAttacks(ArrayList<RobotAction> possibleAttacks) {
		this.possibleAttacks = possibleAttacks;
	}

	public ArrayList<RobotAction> getPossibleDefends() {
		return possibleDefends;
	}

	public void setPossibleDefends(ArrayList<RobotAction> possibleDefends) {
		this.possibleDefends = possibleDefends;
	}
	

}
