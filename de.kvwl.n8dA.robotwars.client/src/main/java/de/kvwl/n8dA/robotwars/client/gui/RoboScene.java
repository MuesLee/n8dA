package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.Point;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.EventListener;

public class RoboScene implements Scene
{
	private AnimatedSceneObject roboAnimation;

	@Override
	public EventListener[] getEventListeners()
	{
		return null;
	}

	@Override
	public void paintScene(Graphics2D g, int width, int height, long time)
	{
		synchronized (this)
		{

			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

			paintBackground(g, width, height);

			if (roboAnimation == null)
			{
				return;
			}

			roboAnimation.setTopLeftPosition(new Point(0, 0));
			roboAnimation.setSize(width, height);

			roboAnimation.paintOnScene(g, time);
		}
	}

	private void paintBackground(Graphics2D g, int width, int height)
	{

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		g.setColor(new Color(104, 208, 97));
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));

		for (int y = 0; y < height; y += 12)
		{
			for (int x = 0; x < width; x += 12)
			{

				if (Math.random() < 0.5)
				{

					int number = (int) Math.round(Math.random() * 9);
					g.drawString("" + number, x, y);
				}
			}
		}

		g.setColor(new Color(255, 255, 255, 100));
		g.fillRect(0, 0, width, height);
	}

	public synchronized AnimatedSceneObject getRoboAnimation()
	{
		return roboAnimation;
	}

	public synchronized void setRoboAnimation(AnimatedSceneObject roboAnimation)
	{
		this.roboAnimation = roboAnimation;
	}

}
