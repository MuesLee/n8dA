package de.kvwl.n8dA.robotwars.server.controller;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.kvwl.n8dA.robotwars.commons.exception.RobotsArentRdyToFightException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffect;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.TypeEffectModificationType;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;

public class BattleControllerTest {

	private BattleController battleController;

	private Robot robotLeft;
	private Robot robotRight;

	@Mock
	private CinematicVisualizer cinematicVisualizerMock;

	@Before
	public void setUp() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {

		MockitoAnnotations.initMocks(this);

		robotLeft = new Robot();
		robotRight = new Robot();
		robotLeft.setHealthPoints(100);
		robotLeft.setRobotPosition(RobotPosition.LEFT);
		robotRight.setHealthPoints(100);
		robotRight.setRobotPosition(RobotPosition.RIGHT);

		battleController = new BattleController(null);
		Field field = battleController.getClass().getDeclaredField(
				"cinematicVisualizer");
		field.setAccessible(true);
		field.set(battleController, cinematicVisualizerMock);

		battleController.setRobotLeft(robotLeft);
		battleController.setRobotRight(robotRight);

	}
	
	
	@Test
	public void testEmptyStatusEffectConsumption() throws Exception {
		
		battleController.consumeStatusEffects(robotLeft);
		
		assertTrue(robotLeft.getStatusEffects().isEmpty());
	}
	
	@Test
	public void testStatusEffectConsumption() throws Exception {
		
		// RoundsLeft should be lowered by 1 
		
		List<StatusEffect> givenEffects = new ArrayList<>();
		TypeEffect givenEffect = new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.RESISTANCE, 1);
		givenEffects.add(givenEffect);
		robotLeft.setStatusEffects(givenEffects );
		
		battleController.consumeStatusEffects(robotLeft);
		
		List<StatusEffect> actualEffects = robotLeft.getStatusEffects();
		StatusEffect actualEffect = actualEffects.get(0);
		StatusEffect expectedEffect = new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.RESISTANCE, 0);
		assertEquals(expectedEffect.getRoundsLeft(), actualEffect.getRoundsLeft());
	}
	@Test
	public void testStatusEffectConsumption1() throws Exception {
		
		// RoundsLeft should be lowered by 1 
		// StatusEffect should be removed
		
		List<StatusEffect> givenEffects = new ArrayList<>();
		TypeEffect givenEffect = new TypeEffect(RobotActionType.ROCK, TypeEffectModificationType.RESISTANCE, 0);
		givenEffects.add(givenEffect);
		robotLeft.setStatusEffects(givenEffects );
		
		battleController.consumeStatusEffects(robotLeft);
		
		assertTrue(robotLeft.getStatusEffects().isEmpty());
	}

	@Test
	public void testInflictStatusEffect() {
		Attack robotAction = new Attack(RobotActionType.SCISSOR, 10);
		TypeEffect typeEffect = new TypeEffect(RobotActionType.SCISSOR,
				TypeEffectModificationType.RESISTANCE, 1);

		ArrayList<StatusEffect> expectedStatusEffects = new ArrayList<StatusEffect>();
		expectedStatusEffects.add(typeEffect);
		robotAction.setStatusEffects(expectedStatusEffects);

		battleController.inflictStatusEffects(robotLeft, robotAction);

		List<StatusEffect> actualStatusEffects = robotLeft.getStatusEffects();

		assertEquals(expectedStatusEffects, actualStatusEffects);
	}

	@Test
	public void testInflictStatusEffect2() {
		Attack robotAction = new Attack(RobotActionType.SCISSOR, 10);
		TypeEffect typeEffect = new TypeEffect(RobotActionType.SCISSOR,
				TypeEffectModificationType.VULNERABILITY, 2);

		ArrayList<StatusEffect> actionsStatusEffects = new ArrayList<StatusEffect>();
		actionsStatusEffects.add(typeEffect);
		robotAction.setStatusEffects(actionsStatusEffects);

		robotLeft.addStatusEffect(new TypeEffect(RobotActionType.SCISSOR,
				TypeEffectModificationType.RESISTANCE, 1));

		battleController.inflictStatusEffects(robotLeft, robotAction);

		ArrayList<StatusEffect> expectedStatusEffects = new ArrayList<StatusEffect>();
		expectedStatusEffects.add(new TypeEffect(RobotActionType.SCISSOR,
				TypeEffectModificationType.RESISTANCE, -1));
		expectedStatusEffects.add(new TypeEffect(RobotActionType.SCISSOR,
				TypeEffectModificationType.VULNERABILITY, 1));
		List<StatusEffect> actualStatusEffects = robotLeft.getStatusEffects();

		assertEquals(expectedStatusEffects, actualStatusEffects);
	}
	@Test
	public void testInflictStatusEffect3() {
		Attack robotAction = new Attack(RobotActionType.SCISSOR, 10);
		TypeEffect typeEffect = new TypeEffect(RobotActionType.SCISSOR,
				TypeEffectModificationType.VULNERABILITY, 2);
		
		ArrayList<StatusEffect> actionsStatusEffects = new ArrayList<StatusEffect>();
		actionsStatusEffects.add(typeEffect);
		robotAction.setStatusEffects(actionsStatusEffects);
		
		robotLeft.addStatusEffect(new TypeEffect(RobotActionType.ROCK,
				TypeEffectModificationType.RESISTANCE, 1));
		
		battleController.inflictStatusEffects(robotLeft, robotAction);
		
		ArrayList<StatusEffect> expectedStatusEffects = new ArrayList<StatusEffect>();
		expectedStatusEffects.add(new TypeEffect(RobotActionType.ROCK,
				TypeEffectModificationType.RESISTANCE, 1));
		expectedStatusEffects.add(new TypeEffect(RobotActionType.SCISSOR,
				TypeEffectModificationType.VULNERABILITY, 2));
		List<StatusEffect> actualStatusEffects = robotLeft.getStatusEffects();
		
		assertEquals(expectedStatusEffects, actualStatusEffects);
	}

	@Test
	public void testCheckForGameResult_VictoryLeft() throws Exception {

		robotRight.setHealthPoints(0);
		robotLeft.setHealthPoints(100);

		GameStateType actualGameEnding = battleController.getCurrentGameState(
				robotLeft, robotRight);

		GameStateType expectedGameEnding = GameStateType.VICTORY_LEFT;

		assertEquals(expectedGameEnding, actualGameEnding);
	}

	@Test
	public void testCheckForGameResult_VictoryRight() throws Exception {

		robotRight.setHealthPoints(100);
		robotLeft.setHealthPoints(0);

		GameStateType actualGameEnding = battleController.getCurrentGameState(
				robotLeft, robotRight);

		GameStateType expectedGameEnding = GameStateType.VICTORY_RIGHT;

		assertEquals(expectedGameEnding, actualGameEnding);
	}

	@Test
	public void testCheckForGameResult_Draw() throws Exception {

		robotRight.setHealthPoints(0);
		robotLeft.setHealthPoints(0);

		GameStateType actualGameEnding = battleController.getCurrentGameState(
				robotLeft, robotRight);

		GameStateType expectedGameEnding = GameStateType.DRAW;

		assertEquals(expectedGameEnding, actualGameEnding);
	}

	@Test
	public void testCheckForGameResult_WaitingForInput() throws Exception {

		robotRight.setHealthPoints(10);
		robotLeft.setHealthPoints(10);

		GameStateType actualGameEnding = battleController.getCurrentGameState(
				robotLeft, robotRight);

		GameStateType expectedGameEnding = GameStateType.WAITING_FOR_PLAYER_INPUT;

		assertEquals(expectedGameEnding, actualGameEnding);
	}

	@Test
	public void testATTvsDEF_ROCKvsPAPER() throws Exception {

		int attackDmg = 10;
		Attack attack = new Attack(RobotActionType.ROCK, attackDmg);
		Defense defense = new Defense(RobotActionType.PAPER, 0);

		robotLeft.setCurrentAction(attack);
		robotRight.setCurrentAction(defense);

		int startHPLeft = battleController.getRobotLeft().getHealthPoints();
		int startHPRight = battleController.getRobotRight().getHealthPoints();

		battleController.computeOutcomeATTvsDEF(robotLeft, robotRight);

		int actualHPLeft = battleController.getRobotLeft().getHealthPoints();
		int actualHPRight = battleController.getRobotRight().getHealthPoints();

		int expectedHPLeft = (int) (startHPLeft - (attackDmg * BattleController.STRONG_DEFENSE_REFLECTION_FACTOR));
		int expectedHPRight = startHPRight;

		assertEquals(expectedHPLeft, actualHPLeft);
		assertEquals(expectedHPRight, actualHPRight);
	}

	@Test
	public void testATTvsDEF_ROCKvsSCISSOR() throws Exception {

		int attackDmg = 10;
		Attack attack = new Attack(RobotActionType.ROCK, attackDmg);
		Defense defense = new Defense(RobotActionType.SCISSOR, 0);

		robotLeft.setCurrentAction(attack);
		robotRight.setCurrentAction(defense);

		int startHPLeft = battleController.getRobotLeft().getHealthPoints();
		int startHPRight = battleController.getRobotRight().getHealthPoints();

		battleController.computeOutcomeATTvsDEF(robotLeft, robotRight);

		int actualHPLeft = battleController.getRobotLeft().getHealthPoints();
		int actualHPRight = battleController.getRobotRight().getHealthPoints();

		int expectedHPLeft = startHPLeft;
		int expectedHPRight = startHPRight - attackDmg;

		assertEquals(expectedHPLeft, actualHPLeft);
		assertEquals(expectedHPRight, actualHPRight);
	}

	@Test
	public void testATTvsDEF_ROCKvsROCK() throws Exception {

		int attackDmg = 10;
		Attack attack = new Attack(RobotActionType.ROCK, attackDmg);
		Defense defense = new Defense(RobotActionType.ROCK, 0);

		robotLeft.setCurrentAction(attack);
		robotRight.setCurrentAction(defense);

		int startHPLeft = battleController.getRobotLeft().getHealthPoints();
		int startHPRight = battleController.getRobotRight().getHealthPoints();

		battleController.computeOutcomeATTvsDEF(robotLeft, robotRight);

		int actualHPLeft = battleController.getRobotLeft().getHealthPoints();
		int actualHPRight = battleController.getRobotRight().getHealthPoints();

		int expectedHPLeft = startHPLeft;
		int expectedHPRight = (int) (startHPRight - (attackDmg * BattleController.NEUTRAL_DEFENSE_BLOCK_FACTOR));

		assertEquals(expectedHPLeft, actualHPLeft);
		assertEquals(expectedHPRight, actualHPRight);
	}

	@Test(expected = RobotsArentRdyToFightException.class)
	public void testFightRobotRightNotRdy() throws Exception {

		Attack attack = new Attack(RobotActionType.ROCK, 10);

		robotLeft.setCurrentAction(attack);

		battleController.fightNextBattleRound();

	}

	@Test(expected = RobotsArentRdyToFightException.class)
	public void testFightRobotLeftNotRdy() throws Exception {

		Defense defense = new Defense(RobotActionType.SCISSOR, 10);

		robotRight.setCurrentAction(defense);

		battleController.fightNextBattleRound();

	}

}
