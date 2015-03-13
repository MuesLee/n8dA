package de.kvwl.n8dA.robotwars.visualization;

import java.util.List;

import de.kvwl.n8dA.robotwars.entities.Robot;

public interface CinematicVisualizer {

	public void battleIsAboutToStart();
	
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position);
	
	public void robotsAreReadyToFight();
	
	public void playAnimationForRobotsWithDelayAfterFirst(List<AnimationPosition> animations);
	
	public void playAnimationForRobotsSimultaneously(List<AnimationPosition> animations);
	
	
	
	
	
}
