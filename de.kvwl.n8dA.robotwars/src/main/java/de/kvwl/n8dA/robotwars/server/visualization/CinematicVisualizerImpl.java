package de.kvwl.n8dA.robotwars.server.visualization;

import game.engine.frame.FullScreenGameFrame;

import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;

// TODO Marvin: CinematicVisualizer implementieren
public class CinematicVisualizerImpl extends FullScreenGameFrame implements
		CinematicVisualizer {

	public CinematicVisualizerImpl() {

		this(GraphicsConfiguration.getDefaultConfig());
	}

	public CinematicVisualizerImpl(GraphicsConfiguration config) {

		super(config.getDevice(), config.getDisplayMode(), "RoboBattle");
	}

	@Override
	public void battleIsAboutToStart() {

	}

	@Override
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position) {

	}

	@Override
	public void roundIsAboutToStart() {

	}

	@Override
	public void playAnimationForRobotsWithDelayAfterFirst(
			List<AnimationPosition> animations) {

	}

	@Override
	public void playAnimationForRobotsSimultaneously(
			List<AnimationPosition> animations) {

	}

}
