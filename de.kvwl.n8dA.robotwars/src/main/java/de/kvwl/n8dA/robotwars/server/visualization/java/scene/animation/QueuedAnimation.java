package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

/**
 * Animationen werden eine nach der anderen abgespielt. {@link Animation#isAlive()} wird nur bei der
 * letzten animation in der Reihe berücksichtigt. Alle abgearbeiteten Animationen werden, wenn sie
 * weiterhin {@link Animation#isAlive()}= true sind, in ihrem zustand mit abgespielt.
 */
public class QueuedAnimation implements Animation
{

	private Animation[] animations;
	private int actualAnimation;

	private boolean isRunning = false;
	private boolean isAlive = false;

	public QueuedAnimation(Animation... animations)
	{

		this.animations = animations;
	}

	@Override
	public void prepare()
	{
		isRunning = true;
		isAlive = true;

		actualAnimation = 0;

		for (Animation animation : animations)
		{
			animation.prepare();
		}
	}

	@Override
	public void animatePre(SceneObject obj, Graphics2D g, long elapsedTime)
	{

		//Alle alten, fertigen, aber nicht toten Animationen zeichnen
		for (int i = 0; i < actualAnimation; i++)
		{
			Animation animationAlive = animations[i];
			if (animationAlive.isAlive())
			{
				animationAlive.animatePre(obj, g, elapsedTime);
			}
		}

		//Jetzt die aktuelle Animation zeichen
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

		//Alle alten, fertigen, aber nicht toten Animationen zeichnen
		for (int i = 0; i < actualAnimation; i++)
		{
			Animation animationAlive = animations[i];
			if (animationAlive.isAlive())
			{
				animationAlive.animatePost(obj, g, elapsedTime);
			}
		}

		//Jetzt die aktuelle Animation zeichen
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

		Animation animation = (actualAnimation < animations.length) ? animations[actualAnimation] : null;
		if (animation != null)
		{
			if (animation.isRunning())
			{
				next = animation;
			}
			else if ((animations.length - actualAnimation) > 1 || !animation.isAlive())
			{
				actualAnimation++;
			}
		}
		else
		{

			isRunning = false;
			isAlive = false;
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
