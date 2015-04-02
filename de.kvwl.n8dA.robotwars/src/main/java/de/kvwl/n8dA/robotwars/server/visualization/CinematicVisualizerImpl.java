package de.kvwl.n8dA.robotwars.server.visualization;

import game.engine.frame.SwingGameFrame;
import game.engine.image.InternalImage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.visualization.scene.GameScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.Action;

//TODO Marvin Wechsel zu Fullscreen
public class CinematicVisualizerImpl extends SwingGameFrame implements CinematicVisualizer
{

	private static final long serialVersionUID = 1L;
	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private static CinematicVisualizerImpl instance;

	private GameScene gameScene = new GameScene();

	private CinematicVisualizerImpl()
	{

		this(GraphicsConfiguration.getSystemDefault());
	}

	private CinematicVisualizerImpl(GraphicsConfiguration config)
	{

		// super(config.getDevice(), config.getDisplayMode(), "RoboBattle");
		super();

		setup();
	}

	private void setup()
	{

		setIconImage(InternalImage.loadFromPath(IMAGE_PATH, "icon.png"));
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
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position, DataLoader loader)
	{
		gameScene.robotHasEnteredTheArena(robot, position, loader);
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

	@Override
	public void updateStats(Robot robot, RobotPosition position, boolean animated, boolean wait)
	{

		gameScene.updateStats(robot, position, animated, wait);
	}

	@Override
	public void reset()
	{

		gameScene.reset();
	}

	public static CinematicVisualizerImpl get()
	{

		if (instance == null)
		{

			instance = new CinematicVisualizerImpl();
			instance.setVisible(true);
		}

		return instance;
	}
}
