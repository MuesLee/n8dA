package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.SceneObject;
import game.engine.stage.scene.object.Size;

import java.awt.Graphics2D;

import de.kvwl.n8dA.robotwars.commons.utils.Null;

public class AnimatedSceneObject extends SceneObject
{

	private SceneObject animatedObject;
	private Animation animation;

	private Object waitLock = new Object();

	public AnimatedSceneObject(SceneObject animatedObject, Animation animation)
	{
		super();

		if (Null.isAnyNull(animatedObject, animation))
		{
			throw new NullPointerException("SceneObject and Animation is required");
		}

		this.animatedObject = animatedObject;
		this.animation = animation;

		animatedObject.setTopLeftPosition(new Point(0, 0));
		animatedObject.setSize(getSize());
	}

	@Override
	protected void paint(Graphics2D g, long elapsedTime)
	{
		boolean initalRunningState = animation.isRunning();
		boolean animate = initalRunningState | animation.isAlive();

		if (animate)
		{
			animation.animatePre(animatedObject, g, elapsedTime);
		}

		animatedObject.paintOnScene(g, elapsedTime);

		if (animate)
		{
			animation.animatePost(animatedObject, g, elapsedTime);
		}

		boolean endRunningState = animation.isRunning();
		if (initalRunningState && !endRunningState)
		{
			stopAnimation();
		}
	}

	public boolean isAnimationRunning()
	{

		return animation.isRunning();
	}

	private void stopAnimation()
	{

		synchronized (waitLock)
		{
			waitLock.notifyAll();
		}
	}

	/**
	 * Start/Restart the animation
	 */
	public void startAnimation(boolean wait)
	{
		animation.prepare();

		if (wait)
		{
			synchronized (waitLock)
			{
				try
				{
					waitLock.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void setSize(Size size)
	{
		super.setSize(size);

		if (animatedObject == null)
		{
			return;
		}
		animatedObject.setSize(size);
	}

	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);

		if (animatedObject == null)
		{
			return;
		}
		animatedObject.setSize(width, height);
	}
}
