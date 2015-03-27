package de.kvwl.n8dA.robotwars.server.visualization.scene.robot;

import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.SceneObject;
import game.engine.stage.scene.object.Size;

import java.awt.Color;
import java.awt.Graphics2D;

import de.kvwl.n8dA.robotwars.server.visualization.AnimationPosition;

public class Action extends SceneObject
{

	private AnimatedSceneObject animation = null;
	private ActionType type = ActionType.Defense;

	private double done = 0;
	private boolean visible = true;
	private DamagePhase damageDone = DamagePhase.Not;

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

		animation.setSize(getSize());
		animation.paintOnScene(g, elapsedTime);
	}

	public AnimatedSceneObject getAnimation()
	{
		return animation;
	}

	public ActionType getType()
	{
		return type;
	}

	public void setType(ActionType type)
	{
		this.type = type;
	}

	public double getRatio()
	{

		Size ts = animation.getTileSize();

		return ts.getWidth() / (double) ts.getHeight();
	}

	public double getDone()
	{
		return done;
	}

	public void setDone(double done)
	{
		this.done = done;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public DamagePhase getDamage()
	{
		return damageDone;
	}

	public void setDamage(DamagePhase damageDone)
	{
		this.damageDone = damageDone;
	}

	public static enum DamagePhase {

		Not, Now, End;
	}

	public static Action create(AnimationPosition ani)
	{

		Action ac = new Action();
		ac.setDone(0);
		ac.setDamage(DamagePhase.Not);
		ac.setVisible(true);

		return ac;
	}
}
