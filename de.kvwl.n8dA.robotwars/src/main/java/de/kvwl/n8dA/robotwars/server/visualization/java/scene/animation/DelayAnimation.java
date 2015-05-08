package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

public class DelayAnimation implements Animation
{

	private long animationTime;
	private long elapsedTime;

	private boolean running = false;

	public DelayAnimation(long animationTime)
	{
		this.animationTime = animationTime;
	}

	@Override
	public void prepare()
	{
		running = true;
		elapsedTime = 0;
	}

	@Override
	public void animatePre(SceneObject obj, Graphics2D g, long elapsedTime)
	{
	}

	@Override
	public void animatePost(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		this.elapsedTime += elapsedTime;

		if (this.elapsedTime >= animationTime)
		{
			running = false;
		}
	}

	@Override
	public boolean isRunning()
	{
		return running;
	}

	@Override
	public boolean isAlive()
	{
		return isRunning();
	}

}
