package de.kvwl.n8dA.robotwars.server.visualization.scene.robot;

import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.SceneObject;

import java.awt.Color;
import java.awt.Graphics2D;

public class Action extends SceneObject
{

	private AnimatedSceneObject animation = null;
	private ActionType type = ActionType.Defense;

	public Action()
	{
	}

	public Action(AnimatedSceneObject ani, ActionType type)
	{

		animation = ani;
		this.type = type;
	}

	@Override
	protected void paint(Graphics2D g, long elapsedTime)
	{

		if (animation == null)
		{
			g.setColor(Color.MAGENTA);
			g.fillRect(0, 0, getWidth(), getHeight());
			return;
		}

		animation.paintOnScene(g, elapsedTime);
	}

	public AnimatedSceneObject getAnimation()
	{
		return animation;
	}

	public void setAnimation(AnimatedSceneObject animation)
	{
		this.animation = animation;
	}

	public ActionType getType()
	{
		return type;
	}

	public void setType(ActionType type)
	{
		this.type = type;
	}

}
