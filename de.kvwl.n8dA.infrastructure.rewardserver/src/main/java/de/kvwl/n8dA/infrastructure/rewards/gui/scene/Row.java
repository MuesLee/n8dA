package de.kvwl.n8dA.infrastructure.rewards.gui.scene;

import java.awt.Color;
import java.awt.Graphics2D;

import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;
import game.engine.stage.scene.object.LabelSceneObject;
import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.ScaleStrategy;
import game.engine.stage.scene.object.SceneObject;
import game.engine.stage.scene.object.Orientation.HorizontalOrientation;
import game.engine.stage.scene.object.Orientation.VerticalOrientation;

public class Row extends SceneObject
{

	private static final Color COLOR_ENTRY_FG = Color.BLACK;
	private static final double SPACE = 0.03;
	private static final double WIDTH_RANG = 0.2;
	private static final double WIDTH_NAME = 0.6;

	private static final HorizontalOrientation POS_RANG = HorizontalOrientation.West;
	private static final HorizontalOrientation POS_NAME = HorizontalOrientation.Center;
	private static final HorizontalOrientation POS_POINTS = HorizontalOrientation.East;

	private HighscoreEntry entry;
	private LabelSceneObject row = new LabelSceneObject();
	private int rang = 0;

	public Row()
	{

		row.setPaint(COLOR_ENTRY_FG);
		row.setOutlinePaint(Color.WHITE);

		row.setHorizontalTextOrientation(HorizontalOrientation.Center);
		row.setVerticalTextOrientation(VerticalOrientation.Center);
		row.setScaleStrategy(ScaleStrategy.FitParentHeight);
	}

	@Override
	protected void paint(Graphics2D g, long elapsedTime)
	{
		int width = (int) (getWidth() - 2 * (getWidth() * SPACE));

		int posRang = (int) (getWidth() * SPACE);
		int widthRang = (int) (width * WIDTH_RANG);

		int posName = posRang + widthRang;
		int widthName = (int) (width * WIDTH_NAME);

		int posPoints = posName + widthName;
		int widthPoints = width - widthRang - widthName;

		row.setTopLeftPosition(new Point(posRang, 0));
		row.setSize(widthRang, (int) (getHeight() * 0.9));
		row.setHorizontalTextOrientation(POS_RANG);
		row.setText(String.format("%d.", rang));
		row.paintOnScene(g, 0);

		row.setTopLeftPosition(new Point(posName, 0));
		row.setSize(widthName, (int) (getHeight() * 0.9));
		row.setHorizontalTextOrientation(POS_NAME);
		row.setText(String.format("%s", entry.getName()));
		row.paintOnScene(g, 0);

		row.setTopLeftPosition(new Point(posPoints, 0));
		row.setSize(widthPoints, (int) (getHeight() * 0.9));
		row.setHorizontalTextOrientation(POS_POINTS);
		row.setText(String.format("%d", entry.getPoints()));
		row.paintOnScene(g, 0);
	}

	public HighscoreEntry getEntry()
	{
		return entry;
	}

	public void setEntry(HighscoreEntry entry)
	{
		this.entry = entry;
	}

	public int getRang()
	{
		return rang;
	}

	public void setRang(int rang)
	{
		this.rang = rang;
	}

}
