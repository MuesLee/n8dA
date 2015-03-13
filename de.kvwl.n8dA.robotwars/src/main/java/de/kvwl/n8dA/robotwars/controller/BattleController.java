package de.kvwl.n8dA.robotwars.controller;

import java.util.List;

import de.kvwl.n8dA.robotwars.actions.Attack;
import de.kvwl.n8dA.robotwars.actions.RobotAction;
import de.kvwl.n8dA.robotwars.entities.Robot;
import de.kvwl.n8dA.robotwars.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.exception.RobotsArentRdyToFightException;
import de.kvwl.n8dA.robotwars.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.visualization.CinematicVisualizerImpl;

public class BattleController {
	
	private Robot robotLeft;
	private Robot robotRight;
	
	private List<RobotAction> allAttacks;
	private List<RobotAction> allDefends;
	private List<Robot> allRobots;
	
	private CinematicVisualizer cinematicVisualizer;
	
	public BattleController() {
		
		this.cinematicVisualizer = new CinematicVisualizerImpl();
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
		
		RobotAction[] orderOfActions = decideOrderOfActions(actionRobotLeft, actionRobotRight);
		
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
	RobotAction[] decideOrderOfActions(RobotAction actionRobotLeft, RobotAction actionRobotRight)
	{
		RobotAction[] order = new RobotAction[2];
		
		if(actionRobotLeft instanceof Attack)
		{
			//Links ATT Rechts ATT
			if(actionRobotRight instanceof Attack)
			{
				double random = Math.random();
				if(random>=0.5)
				{
					order[0] = actionRobotLeft;					
					order[1] = actionRobotRight;					
				}
				else {
					order[0] = actionRobotRight;					
					order[1] = actionRobotLeft;					
				}
			}
			//Links ATT rechts DEF
			else {
				order[1] = actionRobotRight;					
				order[0] = actionRobotLeft;	
			}
		}
		//Links DEF Rechts ATT
		else {
			if(actionRobotRight instanceof Attack)
			{
				order[0] = actionRobotRight;					
				order[1] = actionRobotLeft;	
			}
			
			//Links DEF rechts DEF
			else {
				order = null;
			}
		}
		
		return order;
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
