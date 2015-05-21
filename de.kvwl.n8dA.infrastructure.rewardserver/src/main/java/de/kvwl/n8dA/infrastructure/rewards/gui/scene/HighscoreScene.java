package de.kvwl.n8dA.infrastructure.rewards.gui.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;
import de.kvwl.n8dA.infrastructure.commons.interfaces.HighscoreListVisualizer;
import game.engine.image.InternalImage;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.CachedNinePatchImageSceneObject;
import game.engine.stage.scene.object.NinePatchImageSceneObject;
import game.engine.stage.scene.object.Point;
import game.engine.utils.Null;

public class HighscoreScene implements Scene, HighscoreListVisualizer
{

	private static final Color COLOR_BG_1 = Color.LIGHT_GRAY;
	private static final Color COLOR_BG_2 = Color.GRAY;

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/infrastructure/commons/images/";

	private Object lock = new Object();
	private String title = "";
	private List<HighscoreEntry> highscore = new ArrayList<HighscoreEntry>();

	private NinePatchImageSceneObject bg = new CachedNinePatchImageSceneObject(InternalImage.loadFromPath(IMAGE_PATH,
		"txt_bg.9.png"));

	public HighscoreScene()
	{

		bg.setTopLeftPosition(new Point(0, 0));
	}

	@Override
	public EventListener[] getEventListeners()
	{
		return null;
	}

	@Override
	public void paintScene(Graphics2D g, int width, int height, long elapsedTime)
	{

		synchronized (lock)
		{

			syncPaint(g, width, height, elapsedTime);
		}
	}

	private void syncPaint(Graphics2D g, int width, int height, long elapsedTime)
	{

		paintBackground(g, width, height);

		int spaceX = Math.min(30, width) / 2;
		int spaceY = Math.min(30, height) / 2;
		int rowWidth = width - 2 * spaceX;
		int rowHeight = height - 2 * spaceY;

		Graphics2D rowGraphics = (Graphics2D) g.create(spaceX, spaceY, rowWidth, rowHeight);
		paintRows(rowGraphics, rowWidth, rowHeight);
		rowGraphics.dispose();
	}

	private void paintBackground(Graphics2D g, int width, int height)
	{

		bg.setSize(width, height);
		bg.paintOnScene(g, 0);
	}

	private void paintRows(Graphics2D g, int width, int height)
	{
		int rowHeight = height / Math.max(10, highscore.size());
		paintRowBackground(g, rowHeight, height, width, 0);
	}

	private void paintRowBackground(Graphics2D g, int rowHeight, int height, int width, int y)
	{

		int rows = height / rowHeight;
		int lastRowHeight = height - ((rows - 1) * rowHeight);

		for (int i = 0; i < rows; i++)
		{
			int _height = (i == rows - 1) ? lastRowHeight : rowHeight;

			if (i % 2 == 0)
			{
				g.setColor(COLOR_BG_1);
			}
			else
			{
				g.setColor(COLOR_BG_2);
			}

			g.fillRect(0, y + (i * (rowHeight)), width, _height);
		}
	}

	@Override
	public void showHighscoreList(String title, List<HighscoreEntry> entries)
	{
		synchronized (lock)
		{

			this.title = Null.nvl(title, "");
			this.highscore = Null.nvl(entries, new ArrayList<HighscoreEntry>());
		}
	}

}
