package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

public class CompoundAnimation implements Animation
{

	private Animation[] animations;

	private boolean isRunning = false;
	private boolean isAlive = false;

	public CompoundAnimation(Animation... animations)
	{
		this.animations = animations;
	}

	@Override
	public void prepare()
	{
		isRunning = true;
		isAlive = true;

		for (Animation animation : animations)
		{

			animation.prepare();
		}
	}

	@Override
	public void animatePre(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		boolean running = false;
		boolean alive = false;

		for (Animation animation : animations)
		{

			animation.animatePre(obj, g, elapsedTime);

			if (animation.isRunning())
			{
				running = true;
			}

			if (animation.isAlive())
			{
				alive = true;
			}
		}

		isRunning = running;
		isAlive = alive;
	}

	@Override
	public void animatePost(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		boolean running = false;
		boolean alive = false;

		for (Animation animation : animations)
		{

			animation.animatePost(obj, g, elapsedTime);

			if (animation.isRunning())
			{
				running = true;
			}

			if (animation.isAlive())
			{
				alive = true;
			}
		}

		isRunning = running;
		isAlive = alive;
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
