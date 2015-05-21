package de.kvwl.n8dA.infrastructure.rewards.gui;

import game.engine.frame.SwingGameFrame;
import game.engine.image.InternalImage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;
import de.kvwl.n8dA.infrastructure.commons.interfaces.HighscoreListVisualizer;
import de.kvwl.n8dA.infrastructure.rewards.gui.scene.HighscoreScene;

public class HighscoreListVisualizerImpl extends SwingGameFrame implements HighscoreListVisualizer
{

	private static final long serialVersionUID = 1L;
	private static final String IMAGE_PATH = "/de/kvwl/n8dA/infrastructure/rewards/commons/images/";

	HighscoreScene highscoreScene = new HighscoreScene();

	public HighscoreListVisualizerImpl()
	{

		this(GraphicsConfiguration.getSystemDefault());
	}

	public HighscoreListVisualizerImpl(GraphicsConfiguration systemDefault)
	{

		//		super(config.getDevice(), config.getDisplayMode(), "RoboBattle");
		super("Highscore");

		setup();
	}

	private void setup()
	{

		setIconImage(InternalImage.loadFromPath(IMAGE_PATH, "icon.png"));
		setAlwaysOnTop(true);

		setScene(highscoreScene);
		registerExitKey();
		addWindowListener();
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

	@Override
	public void showHighscoreList(String title, List<HighscoreEntry> entries)
	{
		highscoreScene.showHighscoreList(title, entries);
	}

}
