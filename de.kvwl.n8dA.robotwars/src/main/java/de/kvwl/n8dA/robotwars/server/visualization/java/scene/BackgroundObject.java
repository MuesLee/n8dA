package de.kvwl.n8dA.robotwars.server.visualization.java.scene;

import java.awt.Graphics2D;

import de.kvwl.n8dA.robotwars.commons.utils.Null;
import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.SceneObject;
import game.engine.stage.scene.object.Size;

public class BackgroundObject extends SceneObject
{

	private SceneObject foreground;
	private SceneObject background;
	private Insets insets;

	public BackgroundObject(SceneObject foreground, SceneObject background, Insets insets)
	{
		this.foreground = foreground;
		this.background = background;
		this.insets = Null.nvl(insets, new Insets(0, 0, 0, 0));

		background.setTopLeftPosition(new Point(0, 0));

		updateLayout();
	}

	@Override
	protected void paint(Graphics2D g, long elapsedTime)
	{

		background.paintOnScene(g, elapsedTime);
		foreground.paintOnScene(g, elapsedTime);
	}

	private void updateLayout()
	{

		Insets insets = this.insets.toAbsolute(this);

		foreground
			.setTopLeftPosition(new Point((int) (Math.ceil(insets.getLeft())), (int) (Math.ceil(insets.getTop()))));
		foreground.setSize((int) (getWidth() - Math.ceil(insets.getLeft() + insets.getRight())),
			(int) (getHeight() - Math.ceil(insets.getTop() + insets.getBottom())));
	}

	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);

		if (background == null)
		{
			return;
		}
		background.setSize(width, height);
		updateLayout();
	}

	@Override
	public void setSize(Size size)
	{
		super.setSize(size);

		if (background == null)
		{
			return;
		}
		background.setSize(size);
		updateLayout();
	}
}
