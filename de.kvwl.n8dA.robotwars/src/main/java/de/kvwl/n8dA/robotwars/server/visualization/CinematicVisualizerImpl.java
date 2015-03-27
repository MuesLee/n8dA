package de.kvwl.n8dA.robotwars.server.visualization;

import game.engine.frame.FullScreenGameFrame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.visualization.scene.GameScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.Action;

public class CinematicVisualizerImpl extends FullScreenGameFrame implements CinematicVisualizer
{

	private GameScene gameScene = new GameScene();

	public CinematicVisualizerImpl()
	{

		this(GraphicsConfiguration.getDefaultConfig());
	}

	public CinematicVisualizerImpl(GraphicsConfiguration config)
	{

		super(config.getDevice(), config.getDisplayMode(), "RoboBattle");
		setup();
	}

	private void setup()
	{

		setScene(gameScene);
		registerExitKey();
	}

	private void registerExitKey()
	{
		addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyReleased(KeyEvent e)
			{

				switch (e.getKeyCode())
				{
					case KeyEvent.VK_ESCAPE:
						setVisible(false);
						dispose();
					break;
				}
			}
		});
	}

	@Override
	public void battleIsAboutToStart()
	{

		gameScene.battleIsAboutToStart();
	}

	@Override
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position)
	{

		gameScene.robotHasEnteredTheArena(robot, position);
	}

	@Override
	public void roundIsAboutToStart()
	{

		gameScene.roundIsAboutToStart();
	}

	@Override
	public void prepareForNextRound()
	{

		gameScene.prepareForNextRound();
	}

	@Override
	public void playFightanimation(Action acLeft, Action acRight, boolean wait)
	{

		gameScene.playFightanimation(acLeft, acRight, wait);
	}

}
