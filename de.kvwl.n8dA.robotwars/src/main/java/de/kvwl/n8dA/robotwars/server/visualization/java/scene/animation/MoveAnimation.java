package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.SceneObject;
import game.engine.time.TimeUtils;

import java.awt.Graphics2D;

public class MoveAnimation implements Animation
{

	private boolean alive = false;
	private boolean running = false;

	private long elapsedAnimationTime = 0;
	private long animationTime;
	private double speedX;
	private double speedY;

	private double pX = 0;
	private double pY = 0;

	/**
	 * 
	 * @param speedX px per millisecond
	 * @param speedY px per millisecond
	 * @param animationTime
	 */
	public MoveAnimation(double speedX, double speedY, long animationTime)
	{

		this.speedX = speedX;
		this.speedY = speedY;
		this.animationTime = animationTime;
	}

	@Override
	public void prepare()
	{

		elapsedAnimationTime = 0;
		pX = 0;
		pY = 0;

		alive = true;
		running = true;
	}

	@Override
	public void animatePost(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		postAnimate(obj, g, elapsedTime);
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

		preAnimate(obj, g, elapsedTime);

		if (!running)
		{
			//			alive = false;
		}
	}

	private void postAnimate(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		Point position = obj.getPosition();
		obj.setPosition((int) Math.round(position.getX() - pX), (int) Math.round(position.getY() - pY));
	}

	private void preAnimate(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		double time = TimeUtils.Milliseconds(elapsedTime);

		double addX = (pX += time * speedX);
		double addY = (pY += time * speedY);

		Point position = obj.getPosition();
		obj.setPosition((int) Math.round(position.getX() + addX), (int) Math.round(position.getY() + addY));
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
