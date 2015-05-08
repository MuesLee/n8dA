package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
public class QueuedAnimation implements Animation
{

	private Queue<Animation> animations = new LinkedList<Animation>();

	private boolean isRunning = false;
	private boolean isAlive = false;

	public QueuedAnimation(Animation... animations)
	{

		for (int i = 0; i < animations.length; i++)
		{

			this.animations.add(animations[i]);
		}
	}

	@Override
	public void prepare()
	{
		isRunning = true;
		isAlive = true;
	}

	@Override
	public void animatePre(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		Animation animation = getNextAnimation();
		if (animation == null)
		{
			return;
		}

		animation.animatePre(obj, g, elapsedTime);
	}

	@Override
	public void animatePost(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		Animation animation = getNextAnimation();
		if (animation == null)
		{
			return;
		}

		animation.animatePost(obj, g, elapsedTime);
	}

	private Animation getNextAnimation()
	{

		Animation next = null;

		Animation animation = animations.peek();
		if (animation != null)
		{
			if (animation.isRunning())
			{
				next = animation;
			}
			else if (animations.size() > 1 || !animation.isAlive())
			{
				animations.poll();
			}
		}

		return next;
	}

	@Override
	public boolean isRunning()
	{
		return isRunning;
	}

	@Override
	public boolean isAlive()
	{
		return isAlive;
	}

}
