package de.kvwl.n8dA.robotwars.server.controller;

import game.engine.stage.scene.object.CachedLabelSceneObject;
import game.engine.stage.scene.object.LabelSceneObject;
import game.engine.time.TimeUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.exception.RobotHasInsufficientEnergyException;
import de.kvwl.n8dA.robotwars.commons.exception.RobotsArentRdyToFightException;
import de.kvwl.n8dA.robotwars.commons.exception.UnknownRobotException;
import de.kvwl.n8dA.robotwars.commons.exception.WrongGameStateException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.items.EPRegItem;
import de.kvwl.n8dA.robotwars.commons.game.items.HPRegItem;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboModificator;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.EnergyConsumingEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.HealthConsumingEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.network.RoboBattleServer;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.server.visualization.java.AnimationPosition;
import de.kvwl.n8dA.robotwars.server.visualization.java.CinematicVisualizerImpl;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.Animation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.DelayAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.QueuedAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.ScaleAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.Action;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.ActionType;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.RobotScene;

public class BattleController {

	static final double NEUTRAL_DEFENSE_DAMAGE_FACTOR = 0.75;

	private static final Logger LOG = LoggerFactory
			.getLogger(BattleController.class);

	private static final int ENERGY_REGENERATION_RATE = 2;

	private Robot robotLeft;
	private Robot robotRight;

	private List<Attack> allAttacks;
	private List<Defense> allDefends;
	private List<RoboItem> allItems;
	private List<StatusEffect> allStatusEffects;

	private GameStateType currentGameState = GameStateType.GAME_HASNT_BEGUN;

	private RoboBattleServer server;

	private CinematicVisualizer cinematicVisualizer;
	private DataLoader loader;

	public BattleController(DataLoader loader) {

		// XXX Timo: Fenster schließen, wenn Spiel beendet wird

		this.loader = loader;

		this.setCinematicVisualizer(CinematicVisualizerImpl.get());
		this.getCinematicVisualizer().reset();
	}

	private void startTheBattle() {
		LOG.info("The Battle has begun!");

		setCurrentGameState(GameStateType.GAME_HAS_BEGUN);
		getCinematicVisualizer().battleIsAboutToStart();
		server.sendGameStateInfoToClients(currentGameState);
		
		setCurrentGameState(GameStateType.WAITING_FOR_PLAYER_INPUT);
		
		getCinematicVisualizer().prepareForNextRound(true);
		server.sendGameStateInfoToClients(currentGameState);
	}

	public void fightNextBattleRound() throws RobotsArentRdyToFightException {

		LOG.debug("Next Battleround triggered");

		RobotAction actionRobotLeft = robotLeft.getCurrentAction();
		RobotAction actionRobotRight = robotRight.getCurrentAction();

		if (actionRobotLeft == null || actionRobotRight == null) {
			throw new RobotsArentRdyToFightException();
		}

		LOG.info("Battleround started");
		setCurrentGameState(GameStateType.BATTLE_IS_ACTIVE);
		server.sendGameStateInfoToClients(currentGameState);
		
		getCinematicVisualizer().roundIsAboutToStart(true);

		computeBattleOutcome(robotLeft, robotRight);

		consumeStatusEffects(robotLeft);
		consumeStatusEffects(robotRight);

		cinematicVisualizer.updateHealthpoints(robotRight, RobotPosition.RIGHT,
				true, true);
		cinematicVisualizer.updateHealthpoints(robotLeft, RobotPosition.LEFT,
				true, true);
		cinematicVisualizer.updateEnergypoints(robotLeft, RobotPosition.LEFT,
				true, false);
		cinematicVisualizer.updateEnergypoints(robotRight, RobotPosition.RIGHT,
				true, false);
		if (currentGameState == GameStateType.WAITING_FOR_PLAYER_INPUT) {

			regenerateEnergyOfRobot(robotLeft,ENERGY_REGENERATION_RATE);
			performEachRoundsModificationOfRobot(robotLeft);
			cinematicVisualizer.updateStats(robotLeft, RobotPosition.LEFT,
					true, true);
			
			regenerateEnergyOfRobot(robotRight,ENERGY_REGENERATION_RATE);
			performEachRoundsModificationOfRobot(robotRight);
			cinematicVisualizer.updateStats(robotRight, RobotPosition.RIGHT,
					true, true);
		}
		
		
		updateGameState(robotLeft, robotRight);
		server.sendGameStateInfoToClients(currentGameState);
	}

	GameStateType getCurrentGameState(Robot robotLeft, Robot robotRight) {

		GameStateType result = GameStateType.WAITING_FOR_PLAYER_INPUT;

		if (robotLeft == null || robotRight == null) {
			return GameStateType.GAME_HASNT_BEGUN;
		} else {
			if (robotLeft.getCurrentAction() != null
					&& robotRight.getCurrentAction() != null) {
				result = GameStateType.BATTLE_IS_ACTIVE;
			}
		}

		int healthPointsLeft = robotLeft.getHealthPoints();
		int healthPointsRight = robotRight.getHealthPoints();

		if (healthPointsLeft <= 0) {
			if (healthPointsRight <= 0) {
				result = GameStateType.DRAW;
			} else {
				result = GameStateType.VICTORY_RIGHT;
			}
		} else {
			if (healthPointsRight <= 0) {
				result = GameStateType.VICTORY_LEFT;
			}
		}

		return result;
	}

	/**
	 * Computes effects of RobotActions Clears currentAction of Robots Consumes
	 * robots energy
	 * 
	 * @param robotLeft
	 * @param robotRight
	 */
	void computeBattleOutcome(Robot robotLeft, Robot robotRight) {

		RobotAction actionRobotRight = robotRight.getCurrentAction();
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();

		// Links ATT
		if (actionRobotLeft instanceof Attack) {
			// Links ATT Rechts ATT
			if (actionRobotRight instanceof Attack) {
				LOG.info("Both robots are attacking");
				computeOutcomeATTvsATT(robotLeft, robotRight);
			}
			// Links ATT Rechts DEF
			else {
				LOG.info("Robot: " + robotLeft + " attacks Robot: "
						+ robotRight);
				computeOutcomeATTvsDEF(robotLeft, robotRight);
			}
		}
		// Links DEF
		else {
			// Links DEF Rechts ATT
			if (actionRobotRight instanceof Attack) {
				LOG.info("Robot: " + robotRight + " attacks Robot: "
						+ robotLeft);
				computeOutcomeATTvsDEF(robotRight, robotLeft);
			}

			// Links DEF rechts DEF
			else {
				computeOutcomeDEFvsDEF(robotLeft, robotRight);
				LOG.info("Both Robots defended... boring");
			}
		}

		consumeEnergyForRobotAction(robotLeft);
		consumeEnergyForRobotAction(robotRight);

		robotLeft.setCurrentAction(null);
		robotRight.setCurrentAction(null);
	}

	private void showHPModificationNumberText(RobotPosition robotPosition, String text,boolean positive,
			boolean block) {
		Font font = new Font("Verdana", Font.BOLD, 8);

		LabelSceneObject obj;
		if(positive)
		{
			obj = new CachedLabelSceneObject("+" + text);
			obj.setColor(Color.GREEN);
			obj.setOutlineColor(Color.BLACK);
		}
		else {
			obj = new CachedLabelSceneObject("-" + text);
			obj.setColor(Color.RED);
			obj.setOutlineColor(Color.BLACK);
		}
		obj.setFont(font);

		Rectangle2D bounds;
		if (robotPosition == RobotPosition.LEFT) {
			bounds = new Rectangle2D.Double(RobotScene.SPACE_SIDE / 2,
					RobotScene.SPACE_BOTTOM*2, 0.1, 0.1);

		} else {
			bounds = new Rectangle2D.Double(RobotScene.SPACE_SIDE * 10,
					RobotScene.SPACE_BOTTOM*2,0.1, 0.1);
		}

		Animation animation = new ScaleAnimation(0.1, 1,
				TimeUtils.NanosecondsOfSeconds(1));

		cinematicVisualizer.showAnimation(obj, animation, bounds, block);
	}
	
	private void showEnergyRegNumber(RobotPosition robotPosition, String text, boolean positive,
			boolean block) {
		Font font = new Font("Verdana", Font.BOLD, 8);
		
		LabelSceneObject obj;
		if(positive)
		{
			obj = new CachedLabelSceneObject("+" + text);
			obj.setColor(Color.CYAN);
			obj.setOutlineColor(Color.BLACK);
		}
		else {
			obj = new CachedLabelSceneObject("-" + text);
			obj.setColor(Color.ORANGE);
			obj.setOutlineColor(Color.BLACK);
		}
		obj.setFont(font);
		
		Rectangle2D bounds;
		if (robotPosition == RobotPosition.LEFT) {
			bounds = new Rectangle2D.Double(RobotScene.SPACE_SIDE,
					RobotScene.SPACE_BOTTOM*3, 0.1, 0.1);
			
		} else {
			bounds = new Rectangle2D.Double(RobotScene.SPACE_SIDE * 10,
					RobotScene.SPACE_BOTTOM*3,0.1, 0.1);
		}
		
		Animation animation = new ScaleAnimation(0.1, 1,
				TimeUtils.NanosecondsOfSeconds(1));
		
		cinematicVisualizer.showAnimation(obj, animation, bounds, block);
	}

	private void showDamageNumber(RobotPosition robotPosition, String text,
			boolean block) {

		Font font = new Font("Verdana", Font.BOLD, 10);
		LabelSceneObject obj = new CachedLabelSceneObject("-" + text);

		obj.setColor(Color.RED);
		obj.setOutlineColor(Color.BLACK);
		obj.setFont(font);

		Rectangle2D bounds;
		if (robotPosition == RobotPosition.LEFT) {
			bounds = new Rectangle2D.Double(RobotScene.SPACE_SIDE / 2,
					RobotScene.SPACE_BOTTOM, 0.3, 0.3);

		} else {
			bounds = new Rectangle2D.Double(RobotScene.SPACE_SIDE * 8,
					RobotScene.SPACE_BOTTOM, 0.3, 0.3);
		}

		Animation animation = new ScaleAnimation(0.1, 1,
				TimeUtils.NanosecondsOfSeconds(1));

		cinematicVisualizer.showAnimation(obj, animation, bounds, block);
	}

	/**
	 * ATT vs Strong DEF -> Some dmg reflected, no dmg to DEF ATT vs Weak DEF ->
	 * full dmg to DEF ATT vs. neutral DEF -> some dmg blocked, some dmg to DEF
	 * 
	 * @param attacker
	 * @param defender
	 */
	
	void computeOutcomeATTvsDEF(Robot attacker, Robot defender) {
		Attack attack = (Attack) attacker.getCurrentAction();
		Defense defense = (Defense) defender.getCurrentAction();
	
		RobotActionType attackType = attack.getRobotActionType();
		RobotActionType defenseType = defense.getRobotActionType();
	
		LOG.info("Robot: " + attacker + " attacks with: " + attack
				+ "\nRobot: " + defender + " defends with: " + defense);
	
		ActionType actionTypeDefender = null;
		Action acLeft = null;
		Action acRight = null;
	
		int attackDamage = attack.getDamage();
		int damageReceived = 0;
		RobotPosition damagedRobotsPosition = RobotPosition.LEFT;
		if (attackType.beats(defenseType)) {
	
			// Voller Schaden für DEF
			LOG.info("Weak defense!");
			actionTypeDefender = ActionType.DefenseWithDamage;
	
			inflictStatusEffects(defender, defense);
	
			damageReceived = dealDamageToRobot(defender, attackDamage,
					attack.getRobotActionType());
			damagedRobotsPosition = defender.getRobotPosition();
	
			inflictStatusEffects(defender, attack);
	
		} else if (defenseType.beats(attackType)) {
	
			// teilweise Reflektion an ATT, keinen Schaden für DEF
			LOG.info("Strong defense!");
			actionTypeDefender = ActionType.ReflectingDefense;
	
			int reflectedDamage = (int) (attackDamage * defense
					.getBonusOnDefenseFactor());
			damageReceived = dealDamageToRobot(attacker, reflectedDamage,
					attack.getRobotActionType());
			damagedRobotsPosition = attacker.getRobotPosition();
			inflictStatusEffects(attacker, attack);
	
			inflictStatusEffects(defender, defense);
			// heilung für Reflektor
			// int postBlockDamage = (int) (attackDamage *
			// (NEUTRAL_DEFENSE_BLOCK_FACTOR-defense.getBonusOnDefenseFactor()));
			// dealDamageToRobot(defender, postBlockDamage, attackType);
	
		} else {
			LOG.info("Neutral defense!");
			int postBlockDamage = (int) (attackDamage * (NEUTRAL_DEFENSE_DAMAGE_FACTOR - defense
					.getBonusOnDefenseFactor()));
			actionTypeDefender = ActionType.DefenseWithDamage;
	
			inflictStatusEffects(defender, defense);
	
			damageReceived = dealDamageToRobot(defender, postBlockDamage,
					attack.getRobotActionType());
			damagedRobotsPosition = defender.getRobotPosition();
			inflictStatusEffects(defender, attack);
		}
	
		// Spiele Entsprechende Animation
		try {
	
			if (attacker.getRobotPosition().equals(RobotPosition.LEFT)) {
				acLeft = Action.create(
						new AnimationPosition(attacker.getCurrentAction()
								.getAnimation(), RobotPosition.LEFT),
						ActionType.Attack, loader);
				acRight = Action.create(
						new AnimationPosition(defender.getCurrentAction()
								.getAnimation(), RobotPosition.RIGHT),
						actionTypeDefender, loader);
			} else {
				acRight = Action.create(
						new AnimationPosition(attacker.getCurrentAction()
								.getAnimation(), RobotPosition.RIGHT),
						ActionType.Attack, loader);
				acLeft = Action.create(
						new AnimationPosition(defender.getCurrentAction()
								.getAnimation(), RobotPosition.LEFT),
						actionTypeDefender, loader);
			}
			getCinematicVisualizer().playFightanimation(acLeft, acRight, true);
	
			showDamageNumber(damagedRobotsPosition,
					Integer.toString(damageReceived), true);
	
		} catch (IOException e) {
			LOG.error("boom", e);
		}
	}

	void computeOutcomeDEFvsDEF(Robot defenderLeft, Robot defenderRight) {

		inflictStatusEffects(defenderLeft, defenderLeft.getCurrentAction());
		inflictStatusEffects(defenderRight, defenderRight.getCurrentAction());
		Action acLeft = null;
		Action acRight = null;
		try {
			acLeft = Action.create(new AnimationPosition(defenderLeft
					.getCurrentAction().getAnimation(), RobotPosition.LEFT),
					ActionType.Defense, loader);
			acRight = Action.create(new AnimationPosition(defenderRight
					.getCurrentAction().getAnimation(), RobotPosition.RIGHT),
					ActionType.Defense, loader);
			getCinematicVisualizer().playFightanimation(acLeft, acRight, true);
		} catch (IOException e) {
			LOG.error("boom", e);
		}
	}

	void computeOutcomeATTvsATT(Robot attackerLeft, Robot attackerRight) {
		Attack attackLeft = (Attack) attackerLeft.getCurrentAction();
		Attack attackRight = (Attack) attackerRight.getCurrentAction();

		int damageLeft = dealDamageToRobot(attackerLeft,
				attackRight.getDamage(), attackRight.getRobotActionType());

		int damageRight = dealDamageToRobot(attackerRight,
				attackLeft.getDamage(), attackLeft.getRobotActionType());

		inflictStatusEffects(attackerRight, attackLeft);
		inflictStatusEffects(attackerLeft, attackRight);

		Action acLeft = null;
		Action acRight = null;
		try {
			acLeft = Action.create(new AnimationPosition(attackerLeft
					.getCurrentAction().getAnimation(), RobotPosition.LEFT),
					ActionType.Attack, loader);
			acRight = Action.create(new AnimationPosition(attackerRight
					.getCurrentAction().getAnimation(), RobotPosition.RIGHT),
					ActionType.Attack, loader);
			getCinematicVisualizer().playFightanimation(acLeft, acRight, true);

			showDamageNumber(RobotPosition.RIGHT, "" + damageRight, true);
			showDamageNumber(RobotPosition.LEFT, "" + damageLeft, true);

		} catch (IOException e) {
			LOG.error("boom", e);
		}

	}

	public void performInitialModificationOfRobot(Robot robot) {

		List<RoboItem> equippedItems = robot.getEquippedItems();

		for (RoboItem roboItem : equippedItems) {
			roboItem.performInitialRobotModification(robot);
			LOG.info("Robot " + robot + " has received an initial upgrade: "
					+ roboItem);
		}
	}

	private void performEachRoundsModificationOfRobot(Robot robot) {
		List<RoboItem> equippedItems = robot.getEquippedItems();

		int hpRegged = 0;
		for (RoboModificator roboMod : equippedItems) {
			if (roboMod == null)
				continue;
			roboMod.performEachRoundsModification(robot);
			LOG.info("Robot " + robot + " has received an upgrade: " + roboMod);
			if (roboMod instanceof HPRegItem) {
			HPRegItem hpReg = (HPRegItem) roboMod;
			hpRegged += hpReg.getHpReg();
			}
		}

		List<StatusEffect> roboStats = robot.getStatusEffects();
		int hpLost =0;
		int epLost =0;
		for (RoboModificator statusEffect : roboStats) {
			if (statusEffect == null)
				continue;
			statusEffect.performEachRoundsModification(robot);
			
			if(statusEffect instanceof HealthConsumingEffect)
			{
				hpLost += HealthConsumingEffect.getHpLoss();
			}
			else if (statusEffect instanceof EnergyConsumingEffect) {
				epLost += EnergyConsumingEffect.getEnergyLoss();
			}
		}
		
		if(epLost>0)
		{
			showEnergyRegNumber(robot.getRobotPosition(), Integer.toString(epLost),false, false);
		}
		if(hpLost>0)
		{
			showHPModificationNumberText(robot.getRobotPosition(),Integer.toString(hpLost),false, true);
		}
		if(hpRegged>0)
		{
			showHPModificationNumberText(robot.getRobotPosition(),Integer.toString(hpRegged), true,false);
		}

	}

	/**
	 * Computes and updates the GameState and calls necessary functions
	 * depending on the new GameState
	 * 
	 * @param robotLeft
	 * @param robotRight
	 */
	private void updateGameState(Robot robotLeft, Robot robotRight) {

		currentGameState = getCurrentGameState(robotLeft, robotRight);

		switch (getCurrentGameState()) {

		case DRAW:
		case VICTORY_LEFT:
		case VICTORY_RIGHT:
			endGame(getCurrentGameState());
			break;
		case WAITING_FOR_PLAYER_INPUT:
			getCinematicVisualizer().prepareForNextRound(false);
		case BATTLE_IS_ACTIVE:
			break;
		case GAME_HASNT_BEGUN:
			break;
		default:
			break;
		}

		setCurrentGameState(currentGameState);
	}

	private void endGame(GameStateType currentGameState) {

		// TODO Timo: endGame Text einbauen

		String textGameOver = "GAME OVER";
		Font font = new Font("Verdana", Font.BOLD, 20);

		LabelSceneObject labelGameOver = new CachedLabelSceneObject(
				textGameOver);
		labelGameOver.setColor(Color.BLACK);
		labelGameOver.setOutlineColor(Color.WHITE);
		labelGameOver.setFont(font);
		Rectangle2D bounds = new Rectangle2D.Double(0, 0, 1, 1);
		Animation aniScale = new ScaleAnimation(0.5, 1,
				TimeUtils.NanosecondsOfSeconds(2));
		Animation aniDelay = new DelayAnimation(
				TimeUtils.NanosecondsOfSeconds(1));
		Animation animation = new QueuedAnimation(aniScale, aniDelay);
		cinematicVisualizer.showAnimation(labelGameOver, animation, bounds,
				true);

		String textShowWinner = getShowWinnerText();
		LabelSceneObject labelShowWinner = new CachedLabelSceneObject(
				textShowWinner);
		labelShowWinner.setColor(Color.RED);
		labelShowWinner.setOutlineColor(Color.BLACK);
		labelShowWinner.setFont(font);
		aniScale = new ScaleAnimation(0.5, 1, TimeUtils.NanosecondsOfSeconds(2));
		aniDelay = new DelayAnimation(TimeUtils.NanosecondsOfSeconds(1));
		animation = new QueuedAnimation(aniScale, aniDelay);
		cinematicVisualizer.showAnimation(labelShowWinner, animation, bounds,
				true);
		server.sendGameStateInfoToClients(currentGameState);
		LOG.info("############# I AM FREEE ##############");
	}

	private String getShowWinnerText() {

		String text = "";

		switch (currentGameState) {
		case DRAW:
			text = "DRAW";
			break;
		case VICTORY_LEFT:
			text = robotLeft.getNickname() + " WON";
			break;
		case VICTORY_RIGHT:
			text = robotRight.getNickname() + " WON";
			break;
		default:
			text = "ZAWORSKI!";
			break;
		}
		return text;
	}

	/**
	 * Inflicts the given robot with the status effects of the given robotAction
	 * This method considers the current active status effects for infliction
	 * 
	 * @param robot
	 * @param robotAction
	 */
	void inflictStatusEffects(Robot robot, RobotAction robotAction) {
		List<StatusEffect> statusEffectsToBeInflicted = robotAction
				.getStatusEffects();

		if (statusEffectsToBeInflicted == null
				|| statusEffectsToBeInflicted.isEmpty())
			return;

		for (StatusEffect statusEffect : statusEffectsToBeInflicted) {
			resolveStatusEffect(robot, statusEffect);
		}
	}

	private void resolveStatusEffect(Robot robot, StatusEffect newStatusEffect) {

		LOG.info("Robot " + robot + " is receiving StatusEffect: "
				+ newStatusEffect);

		if (newStatusEffect == null)
			return;

		List<StatusEffect> robotsCurrentStatusEffects = robot
				.getStatusEffects();

		if (robotsCurrentStatusEffects.isEmpty()) {
			LOG.info("Robot " + robot
					+ " has no active effects. No interaction.");
			robot.addStatusEffect(newStatusEffect);
			LOG.info("Robot " + robot + " has received StatusEffect: "
					+ newStatusEffect);
		} else {

			boolean effectWasResolved = false;

			for (StatusEffect statusEffect : robotsCurrentStatusEffects) {

				if (statusEffect == null)
					continue;

				boolean resolvedInteractionWith = statusEffect
						.resolveInteractionWith(newStatusEffect);

				if (resolvedInteractionWith) {
					effectWasResolved = true;
				}
			}
			if (!effectWasResolved) {
				robotsCurrentStatusEffects.add(newStatusEffect);
			}

			LOG.info("Robot " + robot + " now has following StatusEffects: "
					+ robot.getStatusEffects());
		}
	}

	/**
	 * Lowers the duration of the active effects by 1 and deletes the ones with
	 * less than 0 duration left
	 * 
	 * @param robot
	 */
	void consumeStatusEffects(Robot robot) {
		List<StatusEffect> statusEffects = robot.getStatusEffects();
		List<StatusEffect> toDelete = new ArrayList<StatusEffect>();

		LOG.debug("Consuming statusEffects for Robot: " + robot.getNickname());
		LOG.debug("Current statusEffects on Robot: " + robot.getStatusEffects());

		for (StatusEffect statusEffect : statusEffects) {

			if (statusEffect == null)
				continue;

			LOG.debug("Now inspecting: " + statusEffect.getName());
			if (statusEffect.getRoundsLeft() > 0) {
				LOG.debug("Decreasing duration by one for "
						+ statusEffect.getName());
				statusEffect.decreaseRoundsLeft(1);
			} else {
				LOG.debug("Deleting " + statusEffect.getName());
				toDelete.add(statusEffect);
			}
		}
		statusEffects.removeAll(toDelete);
		robot.setStatusEffects(statusEffects);
		LOG.debug("Consuming completed for Robot: " + robot.getNickname());
		LOG.debug("Current statusEffects on Robot: " + robot.getStatusEffects());
	}

	/**
	 * Modifies the given damage if the robot has relevant status effects and
	 * deals the computed damage to it.
	 * 
	 * @param robot
	 * @param damage
	 * @param robotActionType
	 */
	private int dealDamageToRobot(Robot robot, int damage,
			RobotActionType robotActionType) {
		LOG.info("Robot: " + robot + " would receive " + damage + " damage.");

		int healthPoints = robot.getHealthPoints();

		List<StatusEffect> statusEffects = robot.getStatusEffects();
		for (StatusEffect statusEffect : statusEffects) {
			if (statusEffect == null)
				continue;
			double damageModificator = statusEffect
					.getDamageModificatorForRoboActionType(robotActionType);
			damage = (int) (damage * damageModificator);
			LOG.info("Robot: " + robot + " modifies the damage by "
					+ damageModificator + " because of: "
					+ statusEffect.getName());
		}

		healthPoints -= damage;

		robot.setHealthPoints(healthPoints);
		LOG.info("Robot: " + robot + " has " + healthPoints + " HP left.");

		return damage;
	}

	private void regenerateEnergyOfRobot(Robot robot, int energyReg) {

		int energyRobot= robot.getEnergyPoints();
		int regeneratedEnergyCombined = energyReg;

		energyRobot += energyReg;

		robot.setEnergyPoints(Math.min(energyRobot,
				robot.getMaxEnergyPoints()));

		LOG.info("Robot " + robot + " regenerated energy: " + energyReg);
		
		regeneratedEnergyCombined += getRobotsEnergyRegThroughItems(robot);
		showEnergyRegNumber(robot.getRobotPosition(), Integer.toString(regeneratedEnergyCombined),true, false);
	}
	
	private int getRobotsEnergyRegThroughItems(Robot robot)
	{
		int energyRegThroughItems = 0;
		
		List<RoboItem> equippedItems = robot.getEquippedItems();
		for (RoboItem roboItem : equippedItems) {
			if(roboItem != null && roboItem instanceof EPRegItem)
			{
				EPRegItem epRegItem = (EPRegItem) roboItem;
				energyRegThroughItems += epRegItem.getEpReg();
			}
		}
		
		return energyRegThroughItems;
	}

	/**
	 * Substracts the energycosts of the currentAction from the robots
	 * energypoints
	 * 
	 * @param robot
	 */
	private void consumeEnergyForRobotAction(Robot robot) {
		RobotAction actionRobot = robot.getCurrentAction();

		int energyRobot = robot.getEnergyPoints();

		int actionsEnergyCosts = actionRobot.getEnergyCosts();
		energyRobot -= actionsEnergyCosts;

		robot.setEnergyPoints(energyRobot);

		LOG.info("Robot " + robot + " has lost " + actionsEnergyCosts
				+ " Energy.");
		LOG.info("Robot " + robot + " has " + energyRobot + " EP left.");
	}

	public Robot getRobotLeft() {
		return robotLeft;
	}

	public void setRobotLeft(Robot robotLeft) {
		getCinematicVisualizer().robotHasEnteredTheArena(robotLeft,
				RobotPosition.LEFT, loader);
		this.robotLeft = robotLeft;
	}

	public Robot getRobotRight() {
		return robotRight;
	}

	public void setRobotRight(Robot robotRight) {
		getCinematicVisualizer().robotHasEnteredTheArena(robotRight,
				RobotPosition.RIGHT, loader);
		this.robotRight = robotRight;
	}

	/**
	 * Sets an action as the next Action of a robot.
	 * 
	 * Also triggers the next battle round, if an action was set for both
	 * robots.
	 * 
	 * @param robotAction
	 * @param robot
	 * @throws UnknownRobotException
	 * @throws RobotHasInsufficientEnergyException
	 * @throws WrongGameStateException
	 */
	public void setActionForRobot(RobotAction robotAction, Robot robot)
			throws UnknownRobotException, RobotHasInsufficientEnergyException,
			WrongGameStateException {

		if (!getCurrentGameState().equals(
				GameStateType.WAITING_FOR_PLAYER_INPUT)) {
			throw new WrongGameStateException();
		}

		Robot localRobot = getLocalRobotForRemoteRobot(robot);

		if (localRobot.getEnergyPoints() >= robotAction.getEnergyCosts()) {
			localRobot.setCurrentAction(robotAction);
		} else {
			throw new RobotHasInsufficientEnergyException();
		}

		try {
			fightNextBattleRound();
		} catch (RobotsArentRdyToFightException e) {
			LOG.error("Waiting for action of second robot");
		}
	}

	public Robot getLocalRobotForRemoteRobot(Robot robot)
			throws UnknownRobotException {
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

	public List<RoboItem> getAllItems() {
		return allItems;
	}

	public void setAllItems(List<RoboItem> allItems) {
		this.allItems = allItems;
	}

	public RoboBattleServer getServer() {
		return server;
	}

	public void setServer(RoboBattleServer server) {
		this.server = server;
	}

	public GameStateType getCurrentGameState() {
		return currentGameState;
	}

	/**
	 * Sets the Actual Game State
	 * 
	 * @param currentGameState
	 */
	public void setCurrentGameState(GameStateType currentGameState) {
		this.currentGameState = currentGameState;
	}

	public void setRobotIsReady(Robot robot) throws UnknownRobotException {
		Robot localRobot = getLocalRobotForRemoteRobot(robot);
		localRobot.setReadyToFight(true);

		LOG.info("Robot " + robot + " is ready");

		if (robotLeft != null && robotRight != null) {
			if (robotLeft.isReadyToFight() && robotRight.isReadyToFight()) {
				LOG.info("All robots are ready. Starting the fight...");
				startTheBattle();
			}
		}
	}

	public List<Defense> getAllDefends() {
		return allDefends;
	}

	public void setAllDefends(List<Defense> allDefends) {
		this.allDefends = allDefends;
	}

	public List<Attack> getAllAttacks() {
		return allAttacks;
	}

	public void setAllAttacks(List<Attack> allAttacks) {
		this.allAttacks = allAttacks;
	}

	public CinematicVisualizer getCinematicVisualizer() {
		return cinematicVisualizer;
	}

	public void setCinematicVisualizer(CinematicVisualizer cinematicVisualizer) {
		this.cinematicVisualizer = cinematicVisualizer;
	}

	public List<StatusEffect> getAllStatusEffects() {
		return allStatusEffects;
	}

	public void setAllStatusEffects(List<StatusEffect> allStatusEffects) {
		this.allStatusEffects = allStatusEffects;
	}
}
