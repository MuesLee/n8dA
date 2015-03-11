package de.kvwl.n8dA.robotwars.controller;

import org.junit.Before;
import org.junit.Test;

import de.kvwl.n8dA.robotwars.actions.Attack;
import de.kvwl.n8dA.robotwars.actions.Defense;
import de.kvwl.n8dA.robotwars.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.RobotsArentRdyToFightException;


public class BattleControllerTest {
	
	private BattleController battleController;
	
	private Robot robotLeft;
	private Robot robotRight;
	
	@Before
	public void setUp(){
		robotLeft = new Robot();
		robotRight = new Robot();
		
		battleController = new BattleController();
		battleController.setRobotLeft(robotLeft);
		battleController.setRobotRight(robotRight);
		
	}
	
	@Test(expected = RobotsArentRdyToFightException.class)
	public void testFightRobotRightNotRdy() throws Exception {
		
		Attack attack = new Attack(RobotActionType.ROCK, 10);
		
		robotLeft.setCurrentAction(attack);
		
		battleController.fight();
		
	}
	@Test(expected = RobotsArentRdyToFightException.class)
	public void testFightRobotLeftNotRdy() throws Exception {
		
		Defense defense = new Defense(RobotActionType.SCISSOR, 10);
		
		robotRight.setCurrentAction(defense);
		
		battleController.fight();
		
	}

}
