package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class ScaleAnimation implements Animation
{

	private boolean alive = false;
	private boolean running = false;

	private long elapsedAnimationTime = 0;
	private long animationTime;

	private double startFactor;
	private double endFactor;
	private double factor;

	public ScaleAnimation(double startFactor, double endFactor, long animationTime)
	{

		this.startFactor = startFactor;
		this.factor = startFactor;
		this.endFactor = endFactor;
		this.animationTime = animationTime;
	}

	@Override
	public void prepare()
	{

		factor = startFactor;
		elapsedAnimationTime = 0;

		alive = true;
		running = false;
	}

	@Override
	public void animatePost(SceneObject obj, Graphics2D g, long elapsedTime)
	{
	}

	@Override
	public void animatePre(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		if (!alive)
		{
			running = false;
			return;
		}

		if (running)
		{
			elapsedAnimationTime += elapsedTime;
			if (elapsedAnimationTime >= animationTime)
			{

				elapsedAnimationTime = animationTime;
				running = false;
			}
		}

		double increasePerTime = (endFactor - startFactor) / animationTime;
		factor = (increasePerTime * elapsedAnimationTime);

		if (startFactor < endFactor)
		{

			factor = Math.min(factor, endFactor);
		}
		else
		{

			factor = Math.max(endFactor, factor);
		}

		_animate(obj, g, elapsedTime);

		if (!running && endFactor == 1)
		{
			alive = false;
		}
	}

	private void _animate(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		AffineTransform transform = new AffineTransform();

		transform.translate(+obj.getWidth() * 0.5, +obj.getHeight() * 0.5);

		double factor = this.factor;
		if (factor == 0)
		{

			factor = 0.1;
		}
		transform.scale(factor, factor);

		transform.translate(-obj.getWidth() * 0.5, -obj.getHeight() * 0.5);

		g.transform(transform);

	}

	@Override
	public boolean isAlive()
	{
		return alive;
	}

	@Override
	public boolean isRunning()
	{

		return running;
	}
}
