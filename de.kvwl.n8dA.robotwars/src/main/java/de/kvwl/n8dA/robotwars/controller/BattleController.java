package de.kvwl.n8dA.robotwars.controller;

import java.util.ArrayList;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.commons.exception.RobotsArentRdyToFightException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.visualization.AnimationPosition;
import de.kvwl.n8dA.robotwars.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.visualization.RobotPosition;

public class BattleController {
	
	private Robot robotLeft;
	private Robot robotRight;
	
	private List<RobotAction> allAttacks;
	private List<RobotAction> allDefends;
	private List<Robot> allRobots;
	
	private CinematicVisualizer cinematicVisualizer;
	
	public BattleController() {
		
		//TODO: this.cinematicVisualizer = 
	}
	
	
	public void fight() throws RobotsArentRdyToFightException
	{
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();
		RobotAction actionRobotRight = robotRight.getCurrentAction();
		
		
		if(actionRobotLeft == null || actionRobotRight == null)
		{
			throw new RobotsArentRdyToFightException();
		}
		
		cinematicVisualizer.battleIsAboutToStart();
		
	}
	
	/**
	 * Orders the RobotActions into an Array.
	 * 
	 * <p>2 Attacks -> Random Order</p>
	 * <p>1 Attack, 1 Defend -> 1st Attack, 2nd Defend</p>
	 * <p>2 Defends -> null</p>
	 * 
	 * @param actionRobotLeft
	 * @param actionRobotRight
	 * @return Array containing RobotActions in order. null if both are defends 
	 */
	void startActionsInOrder(RobotAction actionRobotLeft, RobotAction actionRobotRight)
	{
		ArrayList<AnimationPosition> order = new ArrayList<AnimationPosition>(2);
		AnimationPosition animationPosition1;
		AnimationPosition animationPosition2;
		
		if(actionRobotLeft instanceof Attack)
		{
			//Links ATT Rechts ATT
			if(actionRobotRight instanceof Attack)
			{
				double random = Math.random();
				if(random>=0.5)
				{
					animationPosition1 = new AnimationPosition(actionRobotLeft.getAnimation().getId(), RobotPosition.LEFT);
					animationPosition2 = new AnimationPosition(actionRobotRight.getAnimation().getId(), RobotPosition.RIGHT);
				
				}
				else {
					animationPosition1 = new AnimationPosition(actionRobotRight.getAnimation().getId(), RobotPosition.RIGHT);
					animationPosition2 = new AnimationPosition(actionRobotLeft.getAnimation().getId(), RobotPosition.LEFT);
				}
			}
			//Links ATT rechts DEF
			else {
				animationPosition1 = new AnimationPosition(actionRobotLeft.getAnimation().getId(), RobotPosition.LEFT);
				animationPosition2 = new AnimationPosition(actionRobotRight.getAnimation().getId(), RobotPosition.RIGHT);
			}
		}
		//Links DEF Rechts ATT
		else {
			if(actionRobotRight instanceof Attack)
			{
				animationPosition1 = new AnimationPosition(actionRobotRight.getAnimation().getId(), RobotPosition.RIGHT);
				animationPosition2 = new AnimationPosition(actionRobotLeft.getAnimation().getId(), RobotPosition.LEFT);
			}
			
			//Links DEF rechts DEF
			else {
				animationPosition1 = new AnimationPosition(actionRobotRight.getAnimation().getId(), RobotPosition.RIGHT);
				animationPosition2 = new AnimationPosition(actionRobotLeft.getAnimation().getId(), RobotPosition.LEFT);
				order.add(animationPosition1);
				order.add(animationPosition2);
				cinematicVisualizer.playAnimationForRobotsSimultaneously(order);
				return;
			}
		}
		
		order.add(animationPosition1);
		order.add(animationPosition2);
		
		cinematicVisualizer.playAnimationForRobotsWithDelayAfterFirst(order);
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
