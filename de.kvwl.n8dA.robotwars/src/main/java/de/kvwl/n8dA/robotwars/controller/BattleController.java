package de.kvwl.n8dA.robotwars.controller;

import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.RobotsArentRdyToFightException;

public class BattleController {
	
	private Robot robotLeft;
	private Robot robotRight;
	
	public BattleController(Robot robotLeft, Robot robotRight) {
		this.robotLeft = robotLeft;
		this.robotRight = robotRight;
	}
	
	
	public void fight() throws RobotsArentRdyToFightException
	{
		if(robotLeft.getCurrentAction() == null || robotRight.getCurrentAction() == null)
		{
			throw new RobotsArentRdyToFightException();
		}
		
		
	}
	
}
