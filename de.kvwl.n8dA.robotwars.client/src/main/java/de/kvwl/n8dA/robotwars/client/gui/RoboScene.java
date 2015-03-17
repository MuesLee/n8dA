package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.Point;

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

			if (roboAnimation == null)
			{
				return;
			}

			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

			roboAnimation.setTopLeftPosition(new Point(0, 0));
			roboAnimation.setSize(width, height);

			roboAnimation.paintOnScene(g, time);
		}
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
