package de.kvwl.n8dA.robotwars.server.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import de.kvwl.n8dA.robotwars.server.network.RoboBattleServer;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;

public class BattleControllerTest {

	private BattleController battleController;

	private Robot robotLeft;
	private Robot robotRight;

	@Mock
	private CinematicVisualizer cinematicVisualizerMock;
	
	@Mock
	private RoboBattleServer battleServerMock;

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
		
		Field field3 = battleController.getClass().getDeclaredField(
				"server");
		field3.setAccessible(true);
		field3.set(battleController, battleServerMock);
		
		Field field1 = battleController.getClass().getDeclaredField(
				"robotLeft");
		field1.setAccessible(true);
		field1.set(battleController, robotLeft);
		Field field2 = battleController.getClass().getDeclaredField(
				"robotRight");
		field2.setAccessible(true);
		field2.set(battleController, robotRight);
	}

	@Test
	public void testGetEloWinFactorForPlayer1() {

		int pointsA = 2806;
		int pointsB = 2577;
		double eloWinFactorForPlayer = battleController
				.getEloWinFactorForPlayer(pointsA, pointsB);

		assertEquals(0.789, eloWinFactorForPlayer, 0.001);
	}
	@Test
	public void testGetEloWinFactorForPlayer2() {
		
		int pointsA = 2577;
		int pointsB = 2806;
		double eloWinFactorForPlayer = battleController
				.getEloWinFactorForPlayer(pointsA, pointsB);
		
		assertEquals(0.211, eloWinFactorForPlayer, 0.001);
	}
	
	@Test
	public void testGetEloMatchPoints1() throws Exception {
	
		double pointsPlayerLeft = 2806;
		double eloWinFactorForPlayerLeft = 0.789;
		double modForMatchPlayerLeft = 1;
		double pointFactor = 10;
		int calculatedEloPointsForPlayer = (int) battleController.getCalculatedEloPointsForPlayer(pointsPlayerLeft, eloWinFactorForPlayerLeft, modForMatchPlayerLeft, pointFactor);
		assertEquals(2808, calculatedEloPointsForPlayer);
	}
	@Test
	public void testGetEloMatchPoints2() throws Exception {
		
		double pointsPlayerLeft = 2577;
		double eloWinFactorForPlayerLeft = 0.211; 
		double modForMatchPlayerLeft = 1;
		double pointFactor = 10;
		int calculatedEloPointsForPlayer = (int) Math.round(battleController.getCalculatedEloPointsForPlayer(pointsPlayerLeft, eloWinFactorForPlayerLeft, modForMatchPlayerLeft, pointFactor));
		assertEquals(2585, calculatedEloPointsForPlayer);
	}
	
	@Test
	public void testPointCalculationComplete() throws Exception {
		String playerLeft ="Timo";
		String playerRight = "Marvin";
		GameStateType matchResult = GameStateType.VICTORY_LEFT;
		
		Mockito.when(battleServerMock.getConfigurationPointsForPlayer(playerLeft)).thenReturn(1200);
		Mockito.when(battleServerMock.getConfigurationPointsForPlayer(playerRight)).thenReturn(1370);
		
		Mockito.when(battleServerMock.getRoboBattlePointsForPlayer(playerLeft)).thenReturn(0);
		Mockito.when(battleServerMock.getRoboBattlePointsForPlayer(playerRight)).thenReturn(10);
		
		
//		int pointsLeft = Mockito.verify(battleServerMock.persistPointsForPlayer(Mockito.eq("playerLeft"), Mockito.anyInt())).intValue();
//		int pointsRight = Mockito.verify(battleServerMock.persistPointsForPlayer(Mockito.eq("playerRight"), Mockito.anyInt())).intValue();
		battleController.calculatePointsForMatch(playerLeft, playerRight, matchResult);
		
//		System.out.println("LEFT " + pointsLeft);
//		System.out.println("RIGHT " + pointsRight);		
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
		TypeEffect givenEffect = new TypeEffect(RobotActionType.FIRE,
				TypeEffectModificationType.RESISTANCE, 1);
		givenEffects.add(givenEffect);
		robotLeft.setStatusEffects(givenEffects);

		battleController.consumeStatusEffects(robotLeft);

		List<StatusEffect> actualEffects = robotLeft.getStatusEffects();
		StatusEffect actualEffect = actualEffects.get(0);
		StatusEffect expectedEffect = new TypeEffect(RobotActionType.FIRE,
				TypeEffectModificationType.RESISTANCE, 0);
		assertEquals(expectedEffect.getRoundsLeft(),
				actualEffect.getRoundsLeft());
	}

	@Test
	public void testStatusEffectConsumption1() throws Exception {

		// RoundsLeft should be lowered by 1
		// StatusEffect should be removed

		List<StatusEffect> givenEffects = new ArrayList<>();
		TypeEffect givenEffect = new TypeEffect(RobotActionType.FIRE,
				TypeEffectModificationType.RESISTANCE, 0);
		givenEffects.add(givenEffect);
		robotLeft.setStatusEffects(givenEffects);

		battleController.consumeStatusEffects(robotLeft);

		assertTrue(robotLeft.getStatusEffects().isEmpty());
	}

	@Test
	public void testStatusEffectConsumption2() throws Exception {

		// RoundsLeft should be lowered by 1
		// 1 StatusEffect should be removed

		List<StatusEffect> givenEffects = new ArrayList<>();
		TypeEffect givenEffect = new TypeEffect(RobotActionType.FIRE,
				TypeEffectModificationType.RESISTANCE, 0);
		TypeEffect givenEffect2 = new TypeEffect(RobotActionType.WATER,
				TypeEffectModificationType.RESISTANCE, 1);
		TypeEffect givenEffect3 = new TypeEffect(RobotActionType.LIGHTNING,
				TypeEffectModificationType.VULNERABILITY, 3);
		givenEffects.add(givenEffect);
		givenEffects.add(givenEffect2);
		givenEffects.add(givenEffect3);
		robotLeft.setStatusEffects(givenEffects);

		battleController.consumeStatusEffects(robotLeft);

		assertTrue(!robotLeft.getStatusEffects().isEmpty());
		assertEquals(givenEffects.get(0).getRoundsLeft(), 0);
		assertEquals(givenEffects.get(1).getRoundsLeft(), 2);
	}

	// 1 Res Light, 1 Vul Water on empty Status
	@Test
	public void testInflictStatusEffect() {
		Attack robotAction = new Attack(RobotActionType.LIGHTNING, 10);

		TypeEffect typeEffect = new TypeEffect(RobotActionType.LIGHTNING,
				TypeEffectModificationType.RESISTANCE, 1);
		TypeEffect typeEffect2 = new TypeEffect(RobotActionType.WATER,
				TypeEffectModificationType.VULNERABILITY, 1);

		ArrayList<StatusEffect> expectedStatusEffects = new ArrayList<StatusEffect>();
		expectedStatusEffects.add(typeEffect);
		expectedStatusEffects.add(typeEffect2);
		robotAction.setStatusEffects(expectedStatusEffects);

		battleController.inflictStatusEffects(robotLeft, robotAction);

		List<StatusEffect> actualStatusEffects = robotLeft.getStatusEffects();

		assertEquals(expectedStatusEffects, actualStatusEffects);
	}

	// 1 Res Light 2 Vul Water on 1 Res Fire 1 Vul Light
	@Test
	public void testInflictStatusEffect2() {
		Attack robotAction = new Attack(RobotActionType.LIGHTNING, 10);
		TypeEffect typeEffect = new TypeEffect(RobotActionType.WATER,
				TypeEffectModificationType.VULNERABILITY, 2);
		TypeEffect typeEffect2 = new TypeEffect(RobotActionType.LIGHTNING,
				TypeEffectModificationType.RESISTANCE, 1);

		ArrayList<StatusEffect> actionsStatusEffects = new ArrayList<StatusEffect>();
		actionsStatusEffects.add(typeEffect);
		actionsStatusEffects.add(typeEffect2);
		robotAction.setStatusEffects(actionsStatusEffects);

		robotLeft.addStatusEffect(new TypeEffect(RobotActionType.FIRE,
				TypeEffectModificationType.RESISTANCE, 1));
		robotLeft.addStatusEffect(new TypeEffect(RobotActionType.LIGHTNING,
				TypeEffectModificationType.VULNERABILITY, 1));

		battleController.inflictStatusEffects(robotLeft, robotAction);

		ArrayList<StatusEffect> expectedStatusEffects = new ArrayList<StatusEffect>();
		expectedStatusEffects.add(new TypeEffect(RobotActionType.FIRE,
				TypeEffectModificationType.RESISTANCE, 1));
		expectedStatusEffects.add(new TypeEffect(RobotActionType.LIGHTNING,
				TypeEffectModificationType.VULNERABILITY, 0));
		expectedStatusEffects.add(new TypeEffect(RobotActionType.WATER,
				TypeEffectModificationType.VULNERABILITY, 2));
		List<StatusEffect> actualStatusEffects = robotLeft.getStatusEffects();

		assertEquals(expectedStatusEffects, actualStatusEffects);
	}

	@Test
	public void testInflictStatusEffect3() {
		Attack robotAction = new Attack(RobotActionType.LIGHTNING, 10);
		TypeEffect typeEffect = new TypeEffect(RobotActionType.LIGHTNING,
				TypeEffectModificationType.VULNERABILITY, 2);

		ArrayList<StatusEffect> actionsStatusEffects = new ArrayList<StatusEffect>();
		actionsStatusEffects.add(typeEffect);
		robotAction.setStatusEffects(actionsStatusEffects);

		robotLeft.addStatusEffect(new TypeEffect(RobotActionType.FIRE,
				TypeEffectModificationType.RESISTANCE, 1));

		battleController.inflictStatusEffects(robotLeft, robotAction);

		ArrayList<StatusEffect> expectedStatusEffects = new ArrayList<StatusEffect>();
		expectedStatusEffects.add(new TypeEffect(RobotActionType.FIRE,
				TypeEffectModificationType.RESISTANCE, 1));
		expectedStatusEffects.add(new TypeEffect(RobotActionType.LIGHTNING,
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
		Attack attack = new Attack(RobotActionType.FIRE, attackDmg);
		Defense defense = new Defense(RobotActionType.WATER, 0);
		defense.setBonusOnDefenseFactor(0.3);

		robotLeft.setCurrentAction(attack);
		robotRight.setCurrentAction(defense);

		int startHPLeft = battleController.getRobotLeft().getHealthPoints();
		int startHPRight = battleController.getRobotRight().getHealthPoints();

		battleController.computeOutcomeATTvsDEF(robotLeft, robotRight);

		int actualHPLeft = battleController.getRobotLeft().getHealthPoints();
		int actualHPRight = battleController.getRobotRight().getHealthPoints();

		int expectedHPLeft = (int) (startHPLeft - (attackDmg * (defense
				.getBonusOnDefenseFactor())));
		int expectedHPRight = startHPRight;

		assertEquals(expectedHPLeft, actualHPLeft);
		assertEquals(expectedHPRight, actualHPRight);
	}

	@Test
	public void testATTvsDEF_ROCKvsSCISSOR() throws Exception {

		int attackDmg = 10;
		Attack attack = new Attack(RobotActionType.FIRE, attackDmg);
		Defense defense = new Defense(RobotActionType.LIGHTNING, 0);

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
		Attack attack = new Attack(RobotActionType.FIRE, attackDmg);
		Defense defense = new Defense(RobotActionType.FIRE, 0);

		robotLeft.setCurrentAction(attack);
		robotRight.setCurrentAction(defense);

		int startHPLeft = battleController.getRobotLeft().getHealthPoints();
		int startHPRight = battleController.getRobotRight().getHealthPoints();

		battleController.computeOutcomeATTvsDEF(robotLeft, robotRight);

		int actualHPLeft = battleController.getRobotLeft().getHealthPoints();
		int actualHPRight = battleController.getRobotRight().getHealthPoints();

		int expectedHPLeft = startHPLeft;
		int expectedHPRight = (int) (startHPRight - (attackDmg * BattleController.NEUTRAL_DEFENSE_DAMAGE_FACTOR));

		assertEquals(expectedHPLeft, actualHPLeft);
		assertEquals(expectedHPRight, actualHPRight);
	}

	@Test(expected = RobotsArentRdyToFightException.class)
	public void testFightRobotRightNotRdy() throws Exception {

		Attack attack = new Attack(RobotActionType.FIRE, 10);

		robotLeft.setCurrentAction(attack);

		battleController.fightNextBattleRound();

	}

	@Test(expected = RobotsArentRdyToFightException.class)
	public void testFightRobotLeftNotRdy() throws Exception {

		Defense defense = new Defense(RobotActionType.LIGHTNING, 10);

		robotRight.setCurrentAction(defense);

		battleController.fightNextBattleRound();

	}

}
