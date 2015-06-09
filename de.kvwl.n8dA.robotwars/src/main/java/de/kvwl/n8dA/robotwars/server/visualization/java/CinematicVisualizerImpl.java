package de.kvwl.n8dA.robotwars.server.visualization.java;

import game.engine.frame.FullScreenGameFrame;
import game.engine.frame.SwingGameFrame;
import game.engine.image.InternalImage;
import game.engine.stage.scene.object.SceneObject;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.server.visualization.java.audio.AudioController;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.GameScene;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.Animation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.Action;

public class CinematicVisualizerImpl implements CinematicVisualizer {

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private static CinematicVisualizerImpl instance;

	private AudioController audioController;
	private GameScene gameScene = new GameScene();
	private Object window;

	private CinematicVisualizerImpl(boolean fullscreen,
			GraphicsConfiguration config, boolean aot) {

		if (fullscreen) {

			window = new FullScreenGameFrame(config.getDevice(),
					config.getDisplayMode(), "RoboBattle");
		} else {

			window = new SwingGameFrame("RoboBattle");
		}

		this.audioController = new AudioController();
		setup(aot);
	}

	private void setup(boolean alwaysOnTop) {

		try {
			setIcon(InternalImage.loadFromPath(IMAGE_PATH, "icon.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setAlwaysOnTop(alwaysOnTop);

		setScene(gameScene);
		registerExitKey();

		audioController.startBackgroundMusic();
	}

	private void setAlwaysOnTop(boolean alwaysOnTop) {

		if (window instanceof FullScreenGameFrame) {

			throw new RuntimeException("Not supported for this window type");
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.setAlwaysOnTop(alwaysOnTop);
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	private void setScene(GameScene gameScene) {

		if (window instanceof FullScreenGameFrame) {

			FullScreenGameFrame frame = (FullScreenGameFrame) window;
			frame.setScene(gameScene);
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.setScene(gameScene);
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	private void setIcon(Image loadFromPath) {

		if (window instanceof FullScreenGameFrame) {

			FullScreenGameFrame frame = (FullScreenGameFrame) window;
			frame.setIcon(loadFromPath);
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.setIconImage(loadFromPath);
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	private void registerExitKey() {
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				switch (e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					setVisible(false);
					dispose();
					System.exit(0);
					break;
				case KeyEvent.VK_END:
					if (audioController.sequencerIsRunning()) {
						audioController.stopBackgroundMusic();
					} else {
						audioController.startBackgroundMusic();
					}
					break;
				}
			}
		});
	}

	private void dispose() {

		if (window instanceof FullScreenGameFrame) {

			FullScreenGameFrame frame = (FullScreenGameFrame) window;
			frame.dispose();
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.dispose();
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	private void setVisible(boolean b) {

		if (window instanceof FullScreenGameFrame) {

			FullScreenGameFrame frame = (FullScreenGameFrame) window;
			frame.setVisible(b);
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.setVisible(b);
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	private void addKeyListener(KeyListener lis) {

		if (window instanceof FullScreenGameFrame) {

			FullScreenGameFrame frame = (FullScreenGameFrame) window;
			frame.addKeyListener(lis);
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.addKeyListener(lis);
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	@SuppressWarnings("unused")
	private void addWindowListener(WindowListener lis) {

		if (window instanceof FullScreenGameFrame) {

			throw new RuntimeException("Not supported for this window type");
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.addWindowListener(lis);
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	@Override
	public void battleIsAboutToStart() {

		gameScene.battleIsAboutToStart();
	}

	@Override
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position,
			DataLoader loader) {
		gameScene.robotHasEnteredTheArena(robot, position, loader);
	}

	@Override
	public void roundIsAboutToStart(boolean wait) {
		audioController.playSound("fight");
		gameScene.roundIsAboutToStart(wait);
	}

	@Override
	public void prepareForNextRound(boolean wait) {

		audioController.playSound("prepareToFight");
		gameScene.prepareForNextRound(wait);
	}

	@Override
	public void playFightanimation(Action acLeft, Action acRight, boolean wait) {

		gameScene.playFightanimation(acLeft, acRight, wait);
	}

	@Override
	public void updateStats(Robot robot, RobotPosition position,
			boolean animated, boolean wait) {

		gameScene.updateStats(robot, position, animated, wait);
	}

	@Override
	public void updateEffects(Robot robot, RobotPosition position) {

		gameScene.updateEffects(robot, position);
	}

	@Override
	public void updateEnergypoints(Robot robot, RobotPosition position,
			boolean animated, boolean wait) {

		gameScene.updateEnergypoints(robot, position, animated, wait);
	}

	@Override
	public void updateHealthpoints(Robot robot, RobotPosition position,
			boolean animated, boolean wait) {

		gameScene.updateHealthpoints(robot, position, animated, wait);
	}

	@Override
	public void reset() {

		gameScene.reset();
	}

	@Override
	public void showAnimation(SceneObject obj, Animation animation,
			Rectangle2D bounds, boolean wait) {
		gameScene.showAnimation(obj, animation, bounds, wait);
	}

	@Override
	public void playSound(String soundName) {
		audioController.playSound(soundName);
	}

	public static CinematicVisualizerImpl get() {

		return get(false);
	}

	public static CinematicVisualizerImpl get(boolean fullscreen) {

		boolean alwaysOnTop = !fullscreen;

		return get(fullscreen, alwaysOnTop);
	}

	public static CinematicVisualizerImpl get(boolean fullscreen,
			boolean alwaysOnTop) {

		return get(fullscreen, false, alwaysOnTop);
	}

	public static CinematicVisualizerImpl get(boolean fullscreen,
			boolean recreate, boolean alwaysOnTop) {

		return get(fullscreen, GraphicsConfiguration.getDefaultConfig(),
				recreate, alwaysOnTop);
	}

	public static CinematicVisualizerImpl get(boolean fullscreen,
			GraphicsConfiguration config, boolean recreate, boolean alwaysOnTop) {

		if (recreate && instance != null) {

			instance.dispose();
			instance = null;
		}

		if (instance == null) {

			instance = new CinematicVisualizerImpl(fullscreen, config,
					alwaysOnTop);
			instance.setVisible(true);
		}

		return instance;
	}
}
