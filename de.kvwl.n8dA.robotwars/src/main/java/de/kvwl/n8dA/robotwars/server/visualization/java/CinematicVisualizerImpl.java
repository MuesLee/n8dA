package de.kvwl.n8dA.robotwars.server.visualization.java;

import game.engine.frame.SwingGameFrame;
import game.engine.image.InternalImage;
import game.engine.stage.scene.object.SceneObject;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.server.visualization.java.audio.AudioController;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.GameScene;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.Animation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.Action;

//TODO Marvin Wechsel zu Fullscreen
public class CinematicVisualizerImpl extends SwingGameFrame implements CinematicVisualizer
{

	private static final long serialVersionUID = 1L;
	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private static CinematicVisualizerImpl instance;

	private AudioController audioController;

	private GameScene gameScene = new GameScene();

	private CinematicVisualizerImpl()
	{

		this(GraphicsConfiguration.getSystemDefault());
	}

	private CinematicVisualizerImpl(GraphicsConfiguration config)
	{

		// super(config.getDevice(), config.getDisplayMode(), "RoboBattle");
		super("RoboBattle");
		this.audioController = new AudioController();

		setup();
	}

	private void setup()
	{

		setIconImage(InternalImage.loadFromPath(IMAGE_PATH, "icon.png"));
		setAlwaysOnTop(true);

		setScene(gameScene);
		registerExitKey();
		addWindowListener();

		audioController.startBackgroundMusic();
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
						System.exit(0);
					break;
				}
			}
		});
	}

	private void addWindowListener()
	{

		addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{

				System.exit(0);
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
	public void roundIsAboutToStart(boolean wait)
	{
		audioController.playSound("fight");
		gameScene.roundIsAboutToStart(wait);
	}

	@Override
	public void prepareForNextRound(boolean wait)
	{

		audioController.playSound("prepareToFight");
		gameScene.prepareForNextRound(wait);
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
	public void updateEfects(Robot robot, RobotPosition position)
	{

		gameScene.updateEfects(robot, position);
	}

	@Override
	public void updateEnergypoints(Robot robot, RobotPosition position, boolean animated, boolean wait)
	{

		gameScene.updateEnergypoints(robot, position, animated, wait);
	}

	@Override
	public void updateHealthpoints(Robot robot, RobotPosition position, boolean animated, boolean wait)
	{

		gameScene.updateHealthpoints(robot, position, animated, wait);
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

	@Override
	public void showAnimation(SceneObject obj, Animation animation, Rectangle2D bounds, boolean wait)
	{

		gameScene.showAnimation(obj, animation, bounds, wait);
	}
}
