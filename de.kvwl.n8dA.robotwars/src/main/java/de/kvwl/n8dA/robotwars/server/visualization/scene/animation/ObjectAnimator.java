package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ObjectAnimator
{

	private Object finishLock = new Object();
	private AtomicBoolean running = new AtomicBoolean(false);

	public ObjectAnimator()
	{
	}

	final void startAnimation(boolean wait)
	{
		running.set(true);

		if (wait)
		{
			synchronized (finishLock)
			{

				try
				{
					finishLock.wait();
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}

	/**
	 * Durchführen der Animation. <strong>!Nicht vergessen - {@link #animationFinished()} !</strong>
	 */
	abstract protected void animate(SceneObject obj, Graphics2D g, long elapsedTime);

	/**
	 * Tell that the animation is over, {@link #startAnimation(boolean)} returns.
	 */
	protected final void animationFinished()
	{

		synchronized (finishLock)
		{
			finishLock.notifyAll();
		}

		running.set(false);
	}

	public boolean isRunning()
	{

		return running.get();
	}
}
