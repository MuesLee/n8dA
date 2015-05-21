package de.kvwl.n8dA.infrastructure.rewards.gui.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;
import de.kvwl.n8dA.infrastructure.commons.interfaces.HighscoreListVisualizer;
import game.engine.image.InternalImage;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.CachedLabelSceneObject;
import game.engine.stage.scene.object.CachedNinePatchImageSceneObject;
import game.engine.stage.scene.object.LabelSceneObject;
import game.engine.stage.scene.object.NinePatchImageSceneObject;
import game.engine.stage.scene.object.ScaleStrategy;
import game.engine.stage.scene.object.Orientation.VerticalOrientation;
import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.Orientation.HorizontalOrientation;
import game.engine.utils.Null;

public class HighscoreScene implements Scene, HighscoreListVisualizer
{

	private static final Color COLOR_BG_1 = Color.LIGHT_GRAY;
	private static final Color COLOR_BG_2 = Color.GRAY;
	private static final Color COLOR_CAPTION_BG = Color.DARK_GRAY;
	private static final Color COLOR_CAPTION_FG = Color.YELLOW;
	private static final Color COLOR_BG_CAPTION_LINE = Color.BLACK;
	private static final Color COLOR_ENTRY_FG = Color.BLACK;

	private static final double CAPTION_HEIGHT = 0.15;
	private static final String IMAGE_PATH = "/de/kvwl/n8dA/infrastructure/commons/images/";

	private Object lock = new Object();
	private List<HighscoreEntry> highscore = new ArrayList<HighscoreEntry>();

	private NinePatchImageSceneObject bg = new CachedNinePatchImageSceneObject(InternalImage.loadFromPath(IMAGE_PATH,
		"txt_bg.9.png"));
	private LabelSceneObject caption = new CachedLabelSceneObject("Highscore");
	private LabelSceneObject row = new LabelSceneObject();

	public HighscoreScene()
	{

		bg.setTopLeftPosition(new Point(0, 0));

		caption.setTopLeftPosition(new Point(0, 0));
		caption.setPaint(COLOR_CAPTION_FG);
		caption.setHorizontalTextOrientation(HorizontalOrientation.Center);
		caption.setVerticalTextOrientation(VerticalOrientation.Center);
		caption.setScaleStrategy(ScaleStrategy.FitParent);

		row.setTopLeftPosition(new Point(0, 0));
		row.setPaint(COLOR_ENTRY_FG);
		row.setHorizontalTextOrientation(HorizontalOrientation.Center);
		row.setVerticalTextOrientation(VerticalOrientation.Center);
		row.setScaleStrategy(ScaleStrategy.FitParentHeight);
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
		int innerWidth = width - 2 * spaceX;
		int innerHeight = height - 2 * spaceY;

		int captionStartX = spaceX;
		int captionStartY = spaceY;
		int captionWidth = innerWidth;
		int captionHeight = (int) Math.round(innerHeight * CAPTION_HEIGHT);

		Graphics2D captionGraphics = (Graphics2D) g.create(captionStartX, captionStartY, captionWidth, captionHeight);
		captionGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		paintCaption(captionGraphics, captionWidth, captionHeight);
		captionGraphics.dispose();

		int rowStartX = spaceX;
		int rowStartY = captionStartY + captionHeight;
		int rowWidth = innerWidth;
		int rowHeight = innerHeight - captionHeight;

		Graphics2D rowGraphics = (Graphics2D) g.create(rowStartX, rowStartY, rowWidth, rowHeight);
		paintRows(rowGraphics, rowWidth, rowHeight);
		rowGraphics.dispose();
	}

	private void paintCaption(Graphics2D g, int width, int height)
	{

		g.setColor(COLOR_CAPTION_BG);
		g.fillRect(0, 0, width, height);

		int _height = (int) (height * 0.8);
		int _space = (int) (height * 0.1);

		caption.setSize(width, _height);
		caption.paintOnScene(g, 0);

		g.setColor(COLOR_BG_CAPTION_LINE);
		g.fillRect(0, _height + _space, width, (height - _height - _space));
	}

	private void paintBackground(Graphics2D g, int width, int height)
	{

		bg.setSize(width, height);
		bg.paintOnScene(g, 0);
	}

	private void paintRows(Graphics2D g, int width, int height)
	{
		int rowHeight = height / Math.max(10, highscore.size());
		int rows = height / rowHeight;
		int lastRowHeight = height - ((rows - 1) * rowHeight);

		for (int i = 0; i < rows; i++)
		{
			int _height = (i == rows - 1) ? lastRowHeight : rowHeight;

			Graphics2D gRow = (Graphics2D) g.create(0, (i * (rowHeight)), width, _height);

			paintRowBackground(gRow, width, _height, (i % 2 == 0) ? true : false);

			HighscoreEntry entry = null;
			if (i < highscore.size())
			{
				entry = highscore.get(i);
			}
			paintRowValue(gRow, width, _height, entry);

			gRow.dispose();
		}
	}

	private void paintRowValue(Graphics2D g, int width, int height, HighscoreEntry entry)
	{

		if (entry == null)
		{
			return;
		}

		row.setSize(width, (int) (height * 0.9));
		row.setText(String.format("%s - %d", entry.getName(), entry.getPoints()));
		row.paintOnScene(g, 0);
	}

	private void paintRowBackground(Graphics2D g, int width, int height, boolean r2)
	{

		if (r2)
		{
			g.setColor(COLOR_BG_1);
		}
		else
		{
			g.setColor(COLOR_BG_2);
		}

		g.fillRect(0, 0, width, height);
	}

	@Override
	public void showHighscoreList(String title, List<HighscoreEntry> entries)
	{
		synchronized (lock)
		{

			this.caption.setText(Null.nvl(title, ""));
			this.highscore = Null.nvl(entries, new ArrayList<HighscoreEntry>());
		}
	}

}
