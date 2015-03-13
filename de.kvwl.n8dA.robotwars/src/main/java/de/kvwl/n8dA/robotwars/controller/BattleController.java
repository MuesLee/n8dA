package de.kvwl.n8dA.robotwars.controller;

import java.util.ArrayList;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.commons.exception.RobotsArentRdyToFightException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.visualization.AnimationPosition;
import de.kvwl.n8dA.robotwars.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.visualization.RobotPosition;

public class BattleController {
	
	private static final int ENERGY_REGENERATION_RATE = 0;
	
	private Robot robotLeft;
	private Robot robotRight;
	
	private List<RobotAction> allAttacks;
	private List<RobotAction> allDefends;
	private List<Robot> allRobots;
	private List<RoboItem> allItems;
	
	private CinematicVisualizer cinematicVisualizer;
	
	public BattleController() {
		
		//TODO: this.cinematicVisualizer = 
	}
	
	
	public void startTheBattle()
	{
		performInitialModificationOfRobot(robotLeft);
		performInitialModificationOfRobot(robotRight);
		
		cinematicVisualizer.battleIsAboutToStart();
	}
	
	private void performInitialModificationOfRobot(Robot robot)
	{
	List<RoboItem> equippedItems = robot.getEquippedItems();
		
		for (RoboItem roboItem : equippedItems) {
			roboItem.performInitialRobotModification(robot);
		}
	}
	
	public void fightNextBattleRound() throws RobotsArentRdyToFightException
	{
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();
		RobotAction actionRobotRight = robotRight.getCurrentAction();
		
		
		if(actionRobotLeft == null || actionRobotRight == null)
		{
			throw new RobotsArentRdyToFightException();
		}
		
		cinematicVisualizer.roundIsAboutToStart();;
		
		//ruft in der Methode cinematicVisualizer auf
		startAnimationsInOrder(actionRobotLeft, actionRobotRight);
		
		computeOutcomeOfBattleRound(robotLeft, robotRight);
		
		regenerateEnergyOfRobots(robotLeft, robotRight);
		
	}
	
	private void regenerateEnergyOfRobots(Robot robotLeft, Robot robotRight) {

		int energyRobotLeft = robotLeft.getEnergyPoints();
		int energyRobotRight = robotRight.getEnergyPoints();
		
		energyRobotLeft += ENERGY_REGENERATION_RATE;	
		energyRobotRight += ENERGY_REGENERATION_RATE; 	
		
		robotLeft.setEnergyPoints(energyRobotLeft);
		robotRight.setEnergyPoints(energyRobotRight);
	}


	private void computeOutcomeOfBattleRound(Robot robotLeft, Robot robotRight) {
		
		//TODO: implement me further
		
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();
		RobotAction actionRobotRight = robotRight.getCurrentAction();
		
		consumeEnergyForRobotAction(robotLeft);
		consumeEnergyForRobotAction(robotRight);
		
		if(actionRobotLeft instanceof Attack)
		{
			Attack attackLeft = (Attack) actionRobotLeft;
			
			if(actionRobotRight instanceof Attack)
			{
				Attack attackRight = (Attack) actionRobotRight;
				
				int healthPointsRoboLeft = robotLeft.getHealthPoints();
				int healthPointsRoboRight = robotRight.getHealthPoints();
				
				healthPointsRoboLeft  -= attackRight.getDamage();
				healthPointsRoboRight -= attackLeft.getDamage();
				
				robotRight.setHealthPoints(healthPointsRoboRight);
				robotLeft.setHealthPoints(healthPointsRoboLeft);
			}
		}
		else {
			Defense defenseLeft = (Defense) actionRobotLeft;
			
			
		}
	}
	
	private void consumeEnergyForRobotAction(Robot robot)
	{
		RobotAction actionRobot = robot.getCurrentAction();
		
		int energyRobot = robot.getEnergyPoints();
		
		energyRobot -= actionRobot.getEnergyCosts();	
		
		robot.setEnergyPoints(energyRobot);
	}

	/**
	 * Orders the RobotActions into an Array.
	 * 
	 * <p>2 Attacks -> Random Order</p>
	 * <p>1 Attack, 1 Defend -> 1st Attack, 2nd Defend</p>
	 * <p>2 Defends -> Simultaneously</p>
	 * 
	 * @param actionRobotLeft
	 * @param actionRobotRight
	 * @return Array containing RobotActions in order. null if both are defends 
	 */
	void startAnimationsInOrder(RobotAction actionRobotLeft, RobotAction actionRobotRight)
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


	public List<RoboItem> getAllItems() {
		return allItems;
	}


	public void setAllItems(List<RoboItem> allItems) {
		this.allItems = allItems;
	}
}
