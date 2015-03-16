package de.kvwl.n8dA.robotwars.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.commons.exception.RobotsArentRdyToFightException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.util.GameEndingType;
import de.kvwl.n8dA.robotwars.server.visualization.AnimationPosition;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.server.visualization.RobotPosition;

public class BattleController {
	
	
	 static final double NEUTRAL_DEFENSE_BLOCK_FACTOR = 0.5;
	 static final double STRONG_DEFENSE_REFLECTION_FACTOR = 0.5;

	private static final Logger LOG = LoggerFactory.getLogger(BattleController.class);
	
	private static final int ENERGY_REGENERATION_RATE = 0;
	
	private Robot robotLeft;
	private Robot robotRight;
	
	private List<RobotAction> allAttacks;
	private List<RobotAction> allDefends;
	private List<Robot> allRobots;
	private List<RoboItem> allItems;
	
	private CinematicVisualizer cinematicVisualizer;
	
	public BattleController() {
		
		BasicConfigurator.configure();
		
		//TODO: this.cinematicVisualizer = 
	}
	
	
	public void startTheBattle()
	{
		LOG.info("The Battle has begun!");
		
		performInitialModificationOfRobot(robotLeft);
		performInitialModificationOfRobot(robotRight);
		
		cinematicVisualizer.battleIsAboutToStart();
		
		try {
			fightNextBattleRound();
		} catch (RobotsArentRdyToFightException e) {
			
			LOG.error("Robots arent rdy", e);
		}
	}
	
	private void performInitialModificationOfRobot(Robot robot)
	{
		
		
	List<RoboItem> equippedItems = robot.getEquippedItems();
		
		for (RoboItem roboItem : equippedItems) {
			roboItem.performInitialRobotModification(robot);
			LOG.info("Robot " + robot + " has received an initial upgrade: " + roboItem);
		}
	}
	
	
	private void performEachRoundsModificationOfRobot(Robot robot)
	{
		List<RoboItem> equippedItems = robot.getEquippedItems();
		
		for (RoboItem roboItem : equippedItems) {
			roboItem.performEachRoundsModification(robot);
			LOG.info("Robot " + robot + " has received an upgrade: " + roboItem);
		}
	}
	
	
	
	public void fightNextBattleRound() throws RobotsArentRdyToFightException
	{
		LOG.debug("Next Battleround triggered");
		
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();
		RobotAction actionRobotRight = robotRight.getCurrentAction();
		
		
		if(actionRobotLeft == null || actionRobotRight == null)
		{
			throw new RobotsArentRdyToFightException();
		}
		
		cinematicVisualizer.roundIsAboutToStart();;
		
		
		//ruft in der Methode cinematicVisualizer auf
		startAnimationsInOrder(robotLeft, robotRight);
		computeBattleOutcome(robotLeft, robotRight);
		
		checkForGameEnding(robotLeft, robotRight);
		
		regenerateEnergyOfRobots(robotLeft, robotRight, ENERGY_REGENERATION_RATE);
		
		performEachRoundsModificationOfRobot(robotLeft);
		performEachRoundsModificationOfRobot(robotRight);
		
		//TODO: Siegbedingung
	}
	
	GameEndingType checkForGameEnding(Robot robotLeft, Robot robotRight) {
		
		GameEndingType result = GameEndingType.ITS_STILL_ON_MOFO;
		
		int healthPointsLeft = robotLeft.getHealthPoints();
		int healthPointsRight = robotRight.getHealthPoints();
		
		
		if(healthPointsLeft <= 0)
		{
			if(healthPointsRight <= 0)
			{
result = GameEndingType.DRAW;				
			}
			else {
				result = GameEndingType.VICTORY_RIGHT;
			}
		}
		else {
			if(healthPointsRight <=0)
			{
				result = GameEndingType.VICTORY_LEFT;
			}
		}
		
		return result;
	}


	private void regenerateEnergyOfRobots(Robot robotLeft, Robot robotRight, int energyReg) {

		int energyRobotLeft = robotLeft.getEnergyPoints();
		int energyRobotRight = robotRight.getEnergyPoints();
		
		energyRobotLeft += energyReg;	
		energyRobotRight += energyReg; 	
		
		robotLeft.setEnergyPoints(energyRobotLeft);
		robotRight.setEnergyPoints(energyRobotRight);

		LOG.info("Robots regenerated energy: " +energyReg);
	}


	void computeBattleOutcome(Robot robotLeft, Robot robotRight) {
		
		RobotAction actionRobotRight = robotRight.getCurrentAction();
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();
		
		if(actionRobotLeft instanceof Attack)
		{
			//Links ATT Rechts ATT
			if(actionRobotRight instanceof Attack)
			{
				computeOutcomeATTvsATT(robotLeft, robotRight);
			}
			//Links ATT rechts DEF
			else {
				computeOutcomeATTvsATT(robotLeft, robotRight);
			}
		}
		//Links DEF Rechts ATT
		else {
			if(actionRobotRight instanceof Attack)
			{
				computeOutcomeATTvsATT(robotRight, robotLeft);
			}
			
			//Links DEF rechts DEF
			else {
				// Nichts passiert. Langweilig...
			}
		}
		
		consumeEnergyForRobotAction(robotLeft);
		consumeEnergyForRobotAction(robotRight);
	}


	/**
	 * ATT vs Strong DEF -> Some dmg reflected, no dmg to DEF
	 * ATT vs Weak DEF -> full dmg to DEF
	 * ATT vs. neutral DEF -> some dmg blocked, some dmg to DEF
	 * 
	 * @param attacker
	 * @param defender
	 */
	 void computeOutcomeATTvsDEF(Robot attacker, Robot defender) {
		Attack attack = (Attack) attacker.getCurrentAction();
		Defense defense = (Defense) defender.getCurrentAction();
		
		RobotActionType attackType = attack.getRobotActionType();
		RobotActionType defenseType = defense.getRobotActionType();
		
		LOG.info("Robot: " + attacker + " attacks with: " + attack +"\nRobot: " +defender + " defends with: " + defense );
		int attackDamage = attack.getDamage();
		if(attackType.beats(defenseType))
		{
			
			// Voller Schaden für DEF
			LOG.info("Weak defense!");
			dealDamageToRobot(defender, attackDamage);
		}
		else if (defenseType.beats(attackType))
				{
			
			// teilweise Reflektion an ATT, keinen Schaden für DEF
			LOG.info("Strong defense!");
			int reflectedDamage = (int) (attackDamage*STRONG_DEFENSE_REFLECTION_FACTOR);
			dealDamageToRobot(attacker, reflectedDamage);
			
		}
		else {
			LOG.info("Neutral defense!");
			int postBlockDamage = (int) (attackDamage*NEUTRAL_DEFENSE_BLOCK_FACTOR);
			dealDamageToRobot(defender, postBlockDamage);
		}
	}
	 
	 void computeOutcomeATTvsATT(Robot attackerLeft, Robot attackerRight) {
		 Attack attackLeft = (Attack) attackerLeft.getCurrentAction();
		 Attack attackRight = (Attack) attackerRight.getCurrentAction();
			
			int damageLeft = attackLeft.getDamage();
			int damageRight = attackRight.getDamage();
			
			dealDamageToRobot(attackerLeft, damageRight);
			dealDamageToRobot(attackerRight, damageLeft);
	}
	 
	 private void dealDamageToRobot(Robot robot, int damage)
	 {
		 
		 LOG.info("Robot: " + robot + " has received " + damage + " damage.");
		 int healthPoints = robot.getHealthPoints();
		 healthPoints -= damage;
		 
			 robot.setHealthPoints(healthPoints);
			 LOG.info("Robot: " + robot + " has " + healthPoints+ " HP left.");
	 }
	
	private void consumeEnergyForRobotAction(Robot robot)
	{
		RobotAction actionRobot = robot.getCurrentAction();
		
		int energyRobot = robot.getEnergyPoints();
		
		int actionsEnergyCosts = actionRobot.getEnergyCosts();
		energyRobot -= actionsEnergyCosts;	
		
		robot.setEnergyPoints(energyRobot);
		
		LOG.info("Robot " + robot + " has lost " + actionsEnergyCosts + " Energy.");
		LOG.info("Robot " + robot + " has " + energyRobot+ " EP left.");
	}

	/**
	 * <p>2 Attacks -> Random Order</p>
	 * <p>1 Attack, 1 Defend -> 1st Attack, 2nd Defend</p>
	 * <p>2 Defends -> Simultaneously</p>
	 * 
	 * @param actionRobotLeft
	 * @param actionRobotRight
	 * @return Array containing RobotActions in order. null if both are defends 
	 */
	void startAnimationsInOrder(Robot robotLeft, Robot robotRight)
	{
		RobotAction actionRobotRight = robotRight.getCurrentAction();
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();
		
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
