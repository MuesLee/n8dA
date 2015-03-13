package de.kvwl.n8dA.robotwars.controller;

import java.util.List;

import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.exception.RobotsArentRdyToFightException;
import de.kvwl.n8dA.robotwars.exception.UnknownRobotException;

public class BattleController {
	
	private Robot robotLeft;
	private Robot robotRight;
	
	private List<RobotAction> allAttacks;
	private List<RobotAction> allDefends;
	private List<Robot> allRobots;
	
	public BattleController() {
	}
	
	
	public void fight() throws RobotsArentRdyToFightException
	{
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();
		RobotAction actionRobotRight = robotRight.getCurrentAction();
		
		
		if(actionRobotLeft == null || actionRobotRight == null)
		{
			throw new RobotsArentRdyToFightException();
		}
		
	}


	public Robot getRobotLeft() {
		return robotLeft;
	}


	public void setRobotLeft(Robot robotLeft) {
		this.robotLeft = robotLeft;
	}


	public Robot getRobotRight() {
		return robotRight;
	}


	public void setRobotRight(Robot robotRight) {
		this.robotRight = robotRight;
	}


	public void setActionForRobot(RobotAction robotAction, Robot robot) throws UnknownRobotException, RobotHasInsufficientEnergyException{
		
		Robot localRobot = getLocalRobotForRemoteRobot(robot);
		
		if(localRobot.getEnergyPoints() >= robotAction.getEnergyCosts())
		{
			localRobot.setCurrentAction(robotAction);
		}
		else {
			throw new RobotHasInsufficientEnergyException();
		}
		
	}
	public Robot getLocalRobotForRemoteRobot(Robot robot) throws UnknownRobotException {
		Robot robotLeft = getRobotLeft();
		if (robot.equals(robotLeft)) {
			return robotLeft;
		}

		Robot robotRight = getRobotRight();
		if (robot.equals(robotRight)) {
			return robotRight;
		}

		throw new UnknownRobotException();
	}


	public List<RobotAction> getAllAttacks() {
		return allAttacks;
	}


	public void setAllAttacks(List<RobotAction> allAttacks) {
		this.allAttacks = allAttacks;
	}


	public List<RobotAction> getAllDefends() {
		return allDefends;
	}


	public void setAllDefends(List<RobotAction> allDefends) {
		this.allDefends = allDefends;
	}


	public List<Robot> getAllRobots() {
		return allRobots;
	}


	public void setAllRobots(List<Robot> allRobots) {
		this.allRobots = allRobots;
	}
}
