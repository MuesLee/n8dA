package de.kvwl.n8dA.robotwars.commons.game.items;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;

public class HPRegItem extends RoboItem{

	private static final long serialVersionUID = 1L;
	
	private int hpReg = 2;
	
	public HPRegItem() {
		setName("HP Reg: " + hpReg);
	}
	
	@Override
	public void performInitialRobotModification(Robot robot) {
    //nichts zu tun
		
	}

	@Override
	public void performEachRoundsModification(Robot robot) {
		int healthPoints = robot.getHealthPoints();
		healthPoints += hpReg;
		robot.setHealthPoints(healthPoints);
	}

}
