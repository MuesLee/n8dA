package de.kvwl.n8dA.infrastructure.rewards.gui;

import game.engine.frame.FullScreenGameFrame;
import game.engine.frame.SwingGameFrame;
import game.engine.image.InternalImage;
import game.engine.stage.scene.Scene;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;
import de.kvwl.n8dA.infrastructure.commons.interfaces.HighscoreListVisualizer;
import de.kvwl.n8dA.infrastructure.rewards.gui.scene.HighscoreScene;

public class HighscoreListVisualizerImpl implements HighscoreListVisualizer {

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/infrastructure/commons/images/";

	private HighscoreScene highscoreScene = new HighscoreScene();
	private Object window;

	public HighscoreListVisualizerImpl() {

		this(false);
	}

	public HighscoreListVisualizerImpl(boolean fullscreen) {

		this(fullscreen, GraphicsConfiguration.getSystemDefault(), !fullscreen);
	}

	public HighscoreListVisualizerImpl(boolean fullscreen,
			GraphicsConfiguration config, boolean aot) {

		if (fullscreen) {

			window = new FullScreenGameFrame(config.getDevice(),
					config.getDisplayMode(), "Highscore");
		} else {

			window = new SwingGameFrame("Highscore");
		}

		setup(aot);
	}

	private void setup(boolean alwaysOnTop) {

		try {
			setIcon(InternalImage.loadFromPath(IMAGE_PATH, "icon.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setAlwaysOnTop(alwaysOnTop);

		setScene(highscoreScene);
		registerExitKey();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {

				System.exit(0);
			}
		});
	}

	public void setAlwaysOnTop(boolean alwaysOnTop) {

		if (window instanceof FullScreenGameFrame) {

			System.err.println("Not supported for this window type");
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.setAlwaysOnTop(alwaysOnTop);
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	public void setScene(Scene scene) {

		if (window instanceof FullScreenGameFrame) {

			FullScreenGameFrame frame = (FullScreenGameFrame) window;
			frame.setScene(scene);
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.setScene(scene);
		} else {

			throw new RuntimeException("Unknown window type");
		}
	}

	public void setIcon(Image loadFromPath) {

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

	public void dispose() {

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

	public void setVisible(boolean b) {

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

	public void addKeyListener(KeyListener lis) {

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

	public void addWindowListener(WindowListener lis) {

		if (window instanceof FullScreenGameFrame) {

			System.err.println("Not supported for this window type");
		} else if (window instanceof SwingGameFrame) {

			SwingGameFrame frame = (SwingGameFrame) window;
			frame.addWindowListener(lis);
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
					break;
				}
			}
		});
	}

	@Override
	public void showHighscoreList(String title, List<HighscoreEntry> entries) {
		highscoreScene.showHighscoreList(title, entries);
	}

}
