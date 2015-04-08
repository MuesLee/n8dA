package de.kvwl.n8dA.robotwars.commons.game.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import de.kvwl.n8dA.robotwars.commons.game.actions.RobotActionType;


public class RobotActionTypeTest {
	
	@Test
	public void testROCKvsSCISSOR() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.FIRE;
		RobotActionType otherRobotActionType = RobotActionType.LIGHTNING;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertTrue(result);
	}
	@Test
	public void testROCKvsPAPER() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.FIRE;
		RobotActionType otherRobotActionType = RobotActionType.WATER;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertFalse(result);
	}
	@Test
	public void testPAPERvsSCISSOR() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.WATER;
		RobotActionType otherRobotActionType = RobotActionType.LIGHTNING;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertFalse(result);
	}
	@Test
	public void testSCISSORvsROCK() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.LIGHTNING;
		RobotActionType otherRobotActionType = RobotActionType.FIRE;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertFalse(result);
	}
	@Test
	public void testPAPERvsROCK() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.WATER;
		RobotActionType otherRobotActionType = RobotActionType.FIRE;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertTrue(result);
	}
	@Test
	public void testSCISSORvsPAPER() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.LIGHTNING;
		RobotActionType otherRobotActionType = RobotActionType.WATER;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertTrue(result);
	}
	@Test
	public void testSCISSORvsSCISSOR() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.LIGHTNING;
		RobotActionType otherRobotActionType = RobotActionType.LIGHTNING;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertFalse(result);
	}
	@Test
	public void testPAPERvsPAPER() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.WATER;
		RobotActionType otherRobotActionType = RobotActionType.WATER;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertFalse(result);
	}
	@Test
	public void testROCKvsROCK() throws Exception {
		
		RobotActionType robotActionType = RobotActionType.FIRE;
		RobotActionType otherRobotActionType = RobotActionType.FIRE;
		
		boolean result = robotActionType.beats(otherRobotActionType);
		
		assertFalse(result);
	}

}
