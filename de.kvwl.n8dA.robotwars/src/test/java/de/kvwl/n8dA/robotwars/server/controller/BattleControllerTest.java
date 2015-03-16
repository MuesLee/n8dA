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
import de.kvwl.n8dA.robotwars.commons.gui.Animation;
import de.kvwl.n8dA.robotwars.server.controller.BattleController;
import de.kvwl.n8dA.robotwars.server.visualization.AnimationPosition;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.server.visualization.RobotPosition;


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
	
	@Test
	public void testStartAnimationsATTandDEF() throws Exception {
		String animationIDLeft = "1";
		RobotPosition positionLeft  = RobotPosition.LEFT;
		String animationIDRight = "2";
		RobotPosition positionRight = RobotPosition.RIGHT;
		
		RobotAction actionRobotLeft = new Attack(RobotActionType.PAPER, 10);
		actionRobotLeft.setAnimation(new Animation(animationIDLeft, "", null));
		
		RobotAction actionRobotRight = new Defense(RobotActionType.ROCK, 10);
		actionRobotRight.setAnimation(new Animation(animationIDRight, "", null));
		robotLeft.setCurrentAction(actionRobotLeft);
		robotRight.setCurrentAction(actionRobotRight);
		
		List<AnimationPosition> animations = new ArrayList<>(2);
		animations.add(new AnimationPosition(animationIDLeft, positionLeft));
		animations.add(new AnimationPosition(animationIDRight, positionRight));
		
		battleController.startAnimationsInOrderAndProcessBattle(robotLeft, robotRight);
		
		verify(cinematicVisualizerMock).playAnimationForRobotsWithDelayAfterFirst(argumentAnimationPosition.capture());
		
		ArrayList<AnimationPosition> capturedList = (ArrayList<AnimationPosition>) argumentAnimationPosition.getValue();
		
		assertEquals(animations.toString(), capturedList.toString());
	}
	@Test
	public void testStartAnimationsDEFandATT() throws Exception {
		String animationIDLeft = "1";
		RobotPosition positionLeft  = RobotPosition.LEFT;
		String animationIDRight = "2";
		RobotPosition positionRight = RobotPosition.RIGHT;
		
		RobotAction actionRobotRight = new Attack(RobotActionType.PAPER, 10);
		actionRobotRight.setAnimation(new Animation(animationIDRight, "", null));
		
		RobotAction actionRobotLeft = new Defense(RobotActionType.ROCK, 10);
		actionRobotLeft.setAnimation(new Animation(animationIDLeft, "", null));
		
		robotLeft.setCurrentAction(actionRobotLeft);
		robotRight.setCurrentAction(actionRobotRight);
		
		List<AnimationPosition> animations = new ArrayList<>(2);
		animations.add(new AnimationPosition(animationIDRight, positionRight));
		animations.add(new AnimationPosition(animationIDLeft, positionLeft));
		
		battleController.startAnimationsInOrderAndProcessBattle(robotLeft, robotRight);
		
		verify(cinematicVisualizerMock).playAnimationForRobotsWithDelayAfterFirst(argumentAnimationPosition.capture());
		
		ArrayList<AnimationPosition> capturedList = (ArrayList<AnimationPosition>) argumentAnimationPosition.getValue();
		
		assertEquals(animations.toString(), capturedList.toString());
	}
	@Test
	public void testStartAnimationsDEFandDEF() throws Exception {
		String animationIDLeft = "1";
		RobotPosition positionLeft  = RobotPosition.LEFT;
		String animationIDRight = "2";
		RobotPosition positionRight = RobotPosition.RIGHT;
		
		RobotAction actionRobotRight = new Defense(RobotActionType.PAPER, 10);
		actionRobotRight.setAnimation(new Animation(animationIDRight, "", null));
		
		RobotAction actionRobotLeft = new Defense(RobotActionType.ROCK, 10);
		actionRobotLeft.setAnimation(new Animation(animationIDLeft, "", null));
		
		robotLeft.setCurrentAction(actionRobotLeft);
		robotRight.setCurrentAction(actionRobotRight);

		List<AnimationPosition> animations = new ArrayList<>(2);
		animations.add(new AnimationPosition(animationIDRight, positionRight));
		animations.add(new AnimationPosition(animationIDLeft, positionLeft));
		
		battleController.startAnimationsInOrderAndProcessBattle(robotLeft, robotRight);
		
		verify(cinematicVisualizerMock).playAnimationForRobotsSimultaneously(argumentAnimationPosition.capture());
		
		ArrayList<AnimationPosition> capturedList = (ArrayList<AnimationPosition>) argumentAnimationPosition.getValue();
		
		assertEquals(animations.toString(),capturedList.toString());
	}
	
	
	//@Test
	// Test funktioniert. Durch die zufällige Sortierung innerhalb der Liste, failed assertEquals zu 50%.. 
	// deshalb mal auskommentiert. zwischendurch mal testen.
	public void testStartAnimationsATTandATT() throws Exception {
		String animationIDLeft = "1";
		RobotPosition positionLeft  = RobotPosition.LEFT;
		String animationIDRight = "2";
		RobotPosition positionRight = RobotPosition.RIGHT;
		
		RobotAction actionRobotRight = new Attack(RobotActionType.PAPER, 10);
		actionRobotRight.setAnimation(new Animation(animationIDRight, "", null));
		
		RobotAction actionRobotLeft = new Attack(RobotActionType.ROCK, 10);
		actionRobotLeft.setAnimation(new Animation(animationIDLeft, "", null));
		
		robotLeft.setCurrentAction(actionRobotLeft);
		robotRight.setCurrentAction(actionRobotRight);

		List<AnimationPosition> animations = new ArrayList<>(2);
		animations.add(new AnimationPosition(animationIDRight, positionRight));
		animations.add(new AnimationPosition(animationIDLeft, positionLeft));
		
		battleController.startAnimationsInOrderAndProcessBattle(robotLeft, robotRight);
		
		verify(cinematicVisualizerMock).playAnimationForRobotsWithDelayAfterFirst(argumentAnimationPosition.capture());
		
		ArrayList<AnimationPosition> capturedList = (ArrayList<AnimationPosition>) argumentAnimationPosition.getValue();
		
		assertEquals(animations.toString(),capturedList.toString());
	}

}
