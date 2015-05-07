package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.SceneObject;
import game.engine.stage.scene.object.Size;

import java.awt.Graphics2D;

public class AnimatedSceneObject extends SceneObject
{

	private SceneObject animatedObject;
	private Animation animation;

	public AnimatedSceneObject(SceneObject animatedObject, Animation animation)
	{
		super();

		this.animatedObject = animatedObject;
		this.animation = animation;

		animatedObject.setTopLeftPosition(new Point(0, 0));
		animatedObject.setSize(getSize());
	}

	@Override
	protected void paint(Graphics2D g, long elapsedTime)
	{
		boolean animate = animation.isRunning() | animation.isAlive();

		if (animate)
		{
			animation.animatePre(animatedObject, g, elapsedTime);
		}

		animatedObject.paintOnScene(g, elapsedTime);

		if (animate)
		{
			animation.animatePost(animatedObject, g, elapsedTime);
		}
	}

	@Override
	public void setSize(Size size)
	{
		super.setSize(size);
		animatedObject.setSize(size);
	}

	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		animatedObject.setSize(width, height);
	}
}
