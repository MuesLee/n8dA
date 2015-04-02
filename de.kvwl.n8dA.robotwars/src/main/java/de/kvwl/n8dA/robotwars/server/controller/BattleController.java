package de.kvwl.n8dA.robotwars.server.controller;

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
import de.kvwl.n8dA.robotwars.commons.game.items.RoboItem;
import de.kvwl.n8dA.robotwars.commons.game.items.RoboModificator;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.network.RoboBattleServer;
import de.kvwl.n8dA.robotwars.server.visualization.AnimationPosition;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizerImpl;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.Action;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.ActionType;

public class BattleController {

	static final double NEUTRAL_DEFENSE_BLOCK_FACTOR = 0.5;
	static final double STRONG_DEFENSE_REFLECTION_FACTOR = 0.5;

	private static final Logger LOG = LoggerFactory
			.getLogger(BattleController.class);

	private static final int ENERGY_REGENERATION_RATE = 2;

	private Robot robotLeft;
	private Robot robotRight;

	private List<Attack> allAttacks;
	private List<Defense> allDefends;
	private List<Robot> allRobots;
	private List<RoboItem> allItems;

	private GameStateType currentGameState = GameStateType.GAME_HASNT_BEGUN;

	private RoboBattleServer server;

	private CinematicVisualizer cinematicVisualizer;
	private DataLoader loader;

	public BattleController(DataLoader loader) {

		// TODO Timo: Anzeigen und ggf. schließen. Wird das Fenster nicht
		// korrekt
		// geschlossen, können die Grafikeinstellungen des Systems nicht
		// zurückgesetzt werden. Wird das Fenster nicht explizit geschlossen,
		// kann es sein, dass extrem viele ressourcen verschwendet werden.
		// Jenachdem wann der GC das Fenster entsorgt und somit die
		// Zeichenroutinen beendet. Am besten das Fenster immer offen lassen und
		// nur die Werte anpassen.

		this.loader = loader;

		this.setCinematicVisualizer(CinematicVisualizerImpl.get());
		this.getCinematicVisualizer().reset();
	}

	private void startTheBattle() {
		LOG.info("The Battle has begun!");

		setCurrentGameState(GameStateType.GAME_HAS_BEGUN);
		getCinematicVisualizer().battleIsAboutToStart();
		setCurrentGameState(GameStateType.WAITING_FOR_PLAYER_INPUT);
		getCinematicVisualizer().prepareForNextRound();
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

		regenerateEnergyOfRobots(robotLeft, robotRight,
				ENERGY_REGENERATION_RATE);
		performEachRoundsModificationOfRobot(robotLeft);
		performEachRoundsModificationOfRobot(robotRight);

		getCinematicVisualizer().roundIsAboutToStart();

		computeBattleOutcome(robotLeft, robotRight);
		getCinematicVisualizer().updateStats(robotLeft, RobotPosition.LEFT,
				true, true);
		getCinematicVisualizer().updateStats(robotRight, RobotPosition.RIGHT,
				true, true);
		updateGameState(robotLeft, robotRight);
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
	// TODO Timo: Statuseffekte einbauen
	void computeBattleOutcome(Robot robotLeft, Robot robotRight) {

		RobotAction actionRobotRight = robotRight.getCurrentAction();
		RobotAction actionRobotLeft = robotLeft.getCurrentAction();

		if (actionRobotLeft instanceof Attack) {
			// Links ATT Rechts ATT
			if (actionRobotRight instanceof Attack) {
				LOG.info("Both robots are attacking");
				computeOutcomeATTvsATT(robotLeft, robotRight);
			}
			// Links ATT rechts DEF
			else {
				LOG.info("Robot: " + robotLeft + " attacks Robot: "
						+ robotRight);
				computeOutcomeATTvsDEF(robotLeft, robotRight);
			}
		}
		// Links DEF Rechts ATT
		else {
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
		if (attackType.beats(defenseType)) {

			// Voller Schaden für DEF
			LOG.info("Weak defense!");
			actionTypeDefender = ActionType.DefenseWithDamage;
			dealDamageToRobot(defender, attackDamage,
					attack.getRobotActionType());
		} else if (defenseType.beats(attackType)) {

			// teilweise Reflektion an ATT, keinen Schaden für DEF
			LOG.info("Strong defense!");
			int reflectedDamage = (int) (attackDamage * STRONG_DEFENSE_REFLECTION_FACTOR);
			actionTypeDefender = ActionType.ReflectingDefense;
			dealDamageToRobot(attacker, reflectedDamage,
					attack.getRobotActionType());

		} else {
			LOG.info("Neutral defense!");
			int postBlockDamage = (int) (attackDamage * NEUTRAL_DEFENSE_BLOCK_FACTOR);
			actionTypeDefender = ActionType.DefenseWithDamage;
			dealDamageToRobot(defender, postBlockDamage,
					attack.getRobotActionType());
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

		} catch (IOException e) {
			LOG.error("boom", e);
		}

	}

	void computeOutcomeDEFvsDEF(Robot defenderLeft, Robot defenderRight) {
		Action acLeft = null;
		Action acRight = null;
		try {
			acLeft = Action.create(new AnimationPosition(defenderLeft
					.getCurrentAction().getAnimation(), RobotPosition.LEFT),
					ActionType.Defense, loader);
			acRight = Action.create(new AnimationPosition(defenderRight
					.getCurrentAction().getAnimation(), RobotPosition.RIGHT),
					ActionType.Defense, loader);
		} catch (IOException e) {
			LOG.error("boom", e);
		}
		getCinematicVisualizer().playFightanimation(acLeft, acRight, true);
	}

	void computeOutcomeATTvsATT(Robot attackerLeft, Robot attackerRight) {
		Attack attackLeft = (Attack) attackerLeft.getCurrentAction();
		Attack attackRight = (Attack) attackerRight.getCurrentAction();

		int damageLeft = attackLeft.getDamage();
		int damageRight = attackRight.getDamage();

		dealDamageToRobot(attackerLeft, damageRight,
				attackRight.getRobotActionType());
		dealDamageToRobot(attackerRight, damageLeft,
				attackLeft.getRobotActionType());

		Action acLeft = null;
		Action acRight = null;
		try {
			acLeft = Action.create(new AnimationPosition(attackerLeft
					.getCurrentAction().getAnimation(), RobotPosition.LEFT),
					ActionType.Attack, loader);
			acRight = Action.create(new AnimationPosition(attackerRight
					.getCurrentAction().getAnimation(), RobotPosition.RIGHT),
					ActionType.Attack, loader);
		} catch (IOException e) {
			LOG.error("boom", e);
		}
		getCinematicVisualizer().playFightanimation(acLeft, acRight, true);

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

		for (RoboModificator roboMod : equippedItems) {
			if (roboMod == null)
				continue;
			roboMod.performEachRoundsModification(robot);
			LOG.info("Robot " + robot + " has received an upgrade: " + roboMod);
		}

		List<StatusEffect> roboStats = robot.getStatusEffects();
		for (RoboModificator statusEffect : roboStats) {
			if (statusEffect == null)
				continue;
			statusEffect.performEachRoundsModification(robot);
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

		setCurrentGameState(getCurrentGameState(robotLeft, robotRight));

		switch (getCurrentGameState()) {

		case DRAW:
		case VICTORY_LEFT:
		case VICTORY_RIGHT:
			endGame(getCurrentGameState());
			break;
		case WAITING_FOR_PLAYER_INPUT:
			getCinematicVisualizer().prepareForNextRound();
		case BATTLE_IS_ACTIVE:
			break;
		case GAME_HASNT_BEGUN:
			break;
		default:
			break;
		}

	}

	private void endGame(GameStateType currentGameState) {

	}

	/**
	 * Inflicts the given robot with the status effects of the given robotAction
	 * This method considers the current active status effects for infliction:
	 * 
	 * @param robot
	 * @param robotAction
	 */
	void inflictStatusEffects(Robot robot, RobotAction robotAction) {
		// TODO Timo: InflictSE Weiter implementieren

		List<StatusEffect> actionsStatusEffects = robotAction
				.getStatusEffects();

		if (actionsStatusEffects == null || actionsStatusEffects.isEmpty())
			return;

		for (StatusEffect newStatusEffect : actionsStatusEffects) {
			resolveStatusEffect(robot, newStatusEffect);
		}
	}

	private void resolveStatusEffect(Robot robot, StatusEffect newStatusEffect) {

		if (newStatusEffect == null)
			return;

		List<StatusEffect> robotsCurrentStatusEffects = robot
				.getStatusEffects();

		if (robotsCurrentStatusEffects.isEmpty()) {
			robot.addStatusEffect(newStatusEffect);
		} else {

			ArrayList<StatusEffect> effectsToAdd = new ArrayList<>();

			for (StatusEffect statusEffect : robotsCurrentStatusEffects) {

				if (statusEffect == null)
					continue;

				StatusEffect resolveInteractionWith = statusEffect
						.resolveInteractionWith(newStatusEffect);

				if (resolveInteractionWith != null) {
					effectsToAdd.add(resolveInteractionWith);
				}
			}
			robotsCurrentStatusEffects.addAll(effectsToAdd);
		}
	}

	/**
	 * Modifies the given damage if the robot has relevant status effects and
	 * deals the computed damage to it.
	 * 
	 * @param robot
	 * @param damage
	 * @param robotActionType
	 */
	private void dealDamageToRobot(Robot robot, int damage,
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
	}

	private void regenerateEnergyOfRobots(Robot robotLeft, Robot robotRight,
			int energyReg) {

		int energyRobotLeft = robotLeft.getEnergyPoints();
		int energyRobotRight = robotRight.getEnergyPoints();

		energyRobotLeft += energyReg;
		energyRobotRight += energyReg;

		robotLeft.setEnergyPoints(Math.min(energyRobotLeft,
				robotLeft.getMaxEnergyPoints()));
		robotRight.setEnergyPoints(Math.min(energyRobotRight,
				robotRight.getMaxEnergyPoints()));

		LOG.info("Robots regenerated energy: " + energyReg);
	}

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
	 * Sets the Actual Game State and publishs it to the Clients
	 * 
	 * @param currentGameState
	 */
	public void setCurrentGameState(GameStateType currentGameState) {
		this.currentGameState = currentGameState;
		server.sendGameStateInfoToClients(currentGameState);
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
}
