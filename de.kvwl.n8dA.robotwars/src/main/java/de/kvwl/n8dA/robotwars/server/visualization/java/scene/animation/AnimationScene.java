package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class AnimationScene implements Scene
{
	private Queue<AnimationContainer> animations = new LinkedList<AnimationContainer>();

	@Override
	public void paintScene(Graphics2D g2d, int width, int height, long elapsedTime)
	{

		synchronized (animations)
		{
			doSyncDrawing(g2d, width, height, elapsedTime);
		}
	}

	private void doSyncDrawing(Graphics2D g2d, int width, int height, long elapsedTime)
	{

		Iterator<AnimationContainer> iterator = animations.iterator();
		while (iterator.hasNext())
		{
			AnimationContainer aniContainer = iterator.next();
			boolean isRunning = aniContainer.getAnimation().isAnimationRunning();

			if (!isRunning)
			{

				iterator.remove();
				continue;
			}

			drawAnimationContainer(g2d, width, height, elapsedTime, aniContainer);
		}
	}

	private void drawAnimationContainer(Graphics2D g2d, int width, int height, long elapsedTime,
		AnimationContainer aniContainer)
	{

		AnimatedSceneObject ani = aniContainer.getAnimation();
		Rectangle2D bounds = aniContainer.getBounds();

		ani.setSize((int) Math.round(width * bounds.getWidth()), (int) Math.round(height * bounds.getHeight()));
		ani.setTopLeftPosition(new Point((int) Math.round(width * bounds.getX()), (int) Math.round(height
			* bounds.getY())));

		ani.paintOnScene(g2d, elapsedTime);

	}

	public void showAnimation(SceneObject obj, Animation animation, Rectangle2D bounds, boolean wait)
	{

		AnimatedSceneObject ani = new AnimatedSceneObject(obj, animation);

		synchronized (animations)
		{
			animations.add(new AnimationContainer(ani, bounds));
		}

		ani.startAnimation(wait);
	}

	@Override
	public EventListener[] getEventListeners()
	{
		return null;
	}

	private static class AnimationContainer
	{

		private AnimatedSceneObject animation;
		private Rectangle2D bounds;

		public AnimationContainer(AnimatedSceneObject animation, Rectangle2D bounds)
		{
			this.setAnimation(animation);
			this.setBounds(bounds);
		}

		public AnimatedSceneObject getAnimation()
		{
			return animation;
		}

		public void setAnimation(AnimatedSceneObject animation)
		{
			this.animation = animation;
		}

		public Rectangle2D getBounds()
		{
			return bounds;
		}

		public void setBounds(Rectangle2D bounds)
		{
			this.bounds = bounds;
		}
	}
}
