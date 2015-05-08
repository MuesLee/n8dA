package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

public class CompoundAnimation implements Animation
{

	private Animation[] animations;

	public CompoundAnimation(Animation... animations)
	{
		this.animations = animations;
	}

	@Override
	public void prepare()
	{

		for (Animation animation : animations)
		{

			animation.prepare();
		}
	}

	@Override
	public void animatePre(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		for (Animation animation : animations)
		{

			animation.animatePre(obj, g, elapsedTime);
		}
	}

	@Override
	public void animatePost(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		for (Animation animation : animations)
		{

			animation.animatePost(obj, g, elapsedTime);
		}
	}

	@Override
	public boolean isRunning()
	{

		boolean isRunning = false;

		for (Animation animation : animations)
		{

			if (animation.isRunning())
			{

				isRunning = true;
				break;
			}
		}

		return isRunning;
	}

	@Override
	public boolean isAlive()
	{

		boolean isAlive = false;

		for (Animation animation : animations)
		{

			if (animation.isAlive())
			{

				isAlive = true;
				break;
			}
		}

		return isAlive;
	}

}
