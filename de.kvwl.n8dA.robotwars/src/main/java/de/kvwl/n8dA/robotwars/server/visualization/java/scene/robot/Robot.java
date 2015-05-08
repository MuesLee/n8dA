package de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.SceneObject;
import game.engine.stage.scene.object.Size;
import game.engine.time.TimeUtils;

public class Robot extends SceneObject
{

	private static final long BLINK_TIME = TimeUtils.NanosecondsOfMilliseconds(100);

	private AnimatedSceneObject robo;

	private boolean visible = true;
	private int blinks = 0;
	private long timeSinceLastSwitch = 0;
	private Object blinkWait = new Object();

	private boolean inverted = false;

	@Override
	protected void paint(Graphics2D g2d, long time)
	{

		checkVisibility(time);

		if (visible)
		{

			revalidate();
			paintRobot(g2d, time);
		}
	}

	private void checkVisibility(long time)
	{

		if (blinks > 0)
		{

			if (timeSinceLastSwitch >= BLINK_TIME)
			{

				long anzBlinks = timeSinceLastSwitch / BLINK_TIME;

				if (anzBlinks % 2 != 0)
				{
					visible = !visible;
				}

				timeSinceLastSwitch %= BLINK_TIME;
				blinks -= anzBlinks;

				if (blinks <= 0)
				{
					blinkAnimationFinished();
				}
			}
			else
			{

				timeSinceLastSwitch += time;
			}
		}
		else
		{

			visible = true;
		}
	}

	private void blinkAnimationFinished()
	{

		synchronized (blinkWait)
		{

			blinkWait.notifyAll();
		}
	}

	private void revalidate()
	{
		if (robo == null)
		{

			return;
		}

		robo.setSize(getWidth(), getHeight());
	}

	private void paintRobot(Graphics2D g2d, long time)
	{

		if (robo != null)
		{

			if (inverted)
			{
				double hWidth = getWidth() * 0.5;
				double hHeight = getHeight() * 0.5;

				AffineTransform beforeTransform = g2d.getTransform();

				AffineTransform transform = new AffineTransform();
				transform.concatenate(beforeTransform);
				transform.translate(hWidth, hHeight);

				transform.scale(-1, 1);

				transform.translate(-hWidth, -hHeight);
				g2d.setTransform(transform);
			}

			robo.paintOnScene(g2d, time);
		}
	}

	public void blink(int times, boolean wait)
	{

		if (times < 1)
		{
			return;
		}

		blinks += 2 * (times + 1);

		if (wait)
		{
			synchronized (blinkWait)
			{

				try
				{
					blinkWait.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void setRobo(AnimatedSceneObject robo)
	{
		this.robo = robo;
	}

	public double getRatio()
	{

		if (robo == null)
		{

			return 0.5;
		}

		Size tS = robo.getTileSize();

		return tS.getWidth() / (double) tS.getHeight();
	}

	public boolean isInverted()
	{
		return inverted;
	}

	public void setInverted(boolean inverted)
	{
		this.inverted = inverted;
	}

}
