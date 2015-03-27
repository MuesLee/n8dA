package de.kvwl.n8dA.robotwars.server.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.kvwl.n8dA.robotwars.commons.exception.RobotsArentRdyToFightException;
import de.kvwl.n8dA.robotwars.commons.game.actions.Attack;
import de.kvwl.n8dA.robotwars.commons.game.actions.Defense;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotAction;
import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;
import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.GameStateType;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.commons.gui.Animation;
import de.kvwl.n8dA.robotwars.server.controller.BattleController;
import de.kvwl.n8dA.robotwars.server.visualization.AnimationPosition;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;


public class BattleControllerTest {
	
	@Captor ArgumentCaptor<List<AnimationPosition>> argumentAnimationPosition;
	
	private BattleController battleController;
	
	private Robot robotLeft;
	private Robot robotRight;
	
	@Mock
	private CinematicVisualizer cinematicVisualizerMock;
	
	@Before
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
			
		MockitoAnnotations.initMocks(this);
		
		robotLeft = new Robot();
		robotRight = new Robot();
		robotLeft.setHealthPoints(100);
		robotRight.setHealthPoints(100);
		
		battleController = new BattleController();
		battleController.setRobotLeft(robotLeft);
		battleController.setRobotRight(robotRight);
		
		Field field = battleController.getClass().getDeclaredField("cinematicVisualizer");
		field.setAccessible(true);
		field.set(battleController, cinematicVisualizerMock);
		
		
		
	}
	
	@Test
	public void testCheckForGameResult_VictoryLeft() throws Exception {
		
		robotRight.setHealthPoints(0);
		robotLeft.setHealthPoints(100);
		
		GameStateType actualGameEnding = battleController.getCurrentGameState(robotLeft, robotRight);
			
		GameStateType expectedGameEnding = GameStateType.VICTORY_LEFT;
		
		assertEquals(expectedGameEnding, actualGameEnding);
	}
	@Test
	public void testCheckForGameResult_VictoryRight() throws Exception {
		
		robotRight.setHealthPoints(100);
		robotLeft.setHealthPoints(0);
		
		GameStateType actualGameEnding = battleController.getCurrentGameState(robotLeft, robotRight);
		
		GameStateType expectedGameEnding = GameStateType.VICTORY_RIGHT;
		
		assertEquals(expectedGameEnding, actualGameEnding);
	}
	@Test
	public void testCheckForGameResult_Draw() throws Exception {
		
		robotRight.setHealthPoints(0);
		robotLeft.setHealthPoints(0);
		
		GameStateType actualGameEnding = battleController.getCurrentGameState(robotLeft, robotRight);
		
		GameStateType expectedGameEnding = GameStateType.DRAW;
		
		assertEquals(expectedGameEnding, actualGameEnding);
	}
	@Test
	public void testCheckForGameResult_WaitingForInput() throws Exception {
		
		robotRight.setHealthPoints(10);
		robotLeft.setHealthPoints(10);
		
		GameStateType actualGameEnding = battleController.getCurrentGameState(robotLeft, robotRight);
		
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
		
		int expectedHPLeft =  (int) (startHPLeft - (attackDmg * BattleController.STRONG_DEFENSE_REFLECTION_FACTOR));
		int expectedHPRight =  startHPRight;
				
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
		
		int expectedHPLeft =  startHPLeft;
		int expectedHPRight =  startHPRight -attackDmg;
		
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
		
		int expectedHPLeft =  startHPLeft;
		int expectedHPRight =  (int) (startHPRight - (attackDmg *BattleController.NEUTRAL_DEFENSE_BLOCK_FACTOR));
		
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
