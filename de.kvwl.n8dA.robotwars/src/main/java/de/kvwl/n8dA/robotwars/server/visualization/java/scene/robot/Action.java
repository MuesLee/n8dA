package de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot;

import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.SceneObject;
import game.engine.stage.scene.object.Size;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.visualization.java.AnimationPosition;

public class Action extends SceneObject
{

	private AnimatedSceneObject animation = null;

	private ActionType type = ActionType.Defense;

	private double done = 0;
	private boolean visible = true;
	private DamagePhase damageDone = DamagePhase.Not;

	private boolean inverted = false;

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

		if (inverted)
		{
			double hWidth = getWidth() * 0.5;
			double hHeight = getHeight() * 0.5;

			AffineTransform beforeTransform = g.getTransform();

			AffineTransform transform = new AffineTransform();
			transform.concatenate(beforeTransform);
			transform.translate(hWidth, hHeight);

			transform.scale(-1, 1);

			transform.translate(-hWidth, -hHeight);
			g.setTransform(transform);
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

	public void invert()
	{

		setInverted(!isInverted());
	}

	public boolean isInverted()
	{
		return inverted;
	}

	public void setInverted(boolean inverted)
	{
		this.inverted = inverted;
	}

	public static Action create(AnimationPosition ani, ActionType type, DataLoader loader) throws IOException
	{

		AnimatedSceneObject anima = loader.createAnimatedSceneObject(ani.getAnimation());

		Action ac = new Action(anima, type);
		ac.setDone(0);
		ac.setDamage(DamagePhase.Not);
		ac.setVisible(true);

		if (ani.getPosition() == RobotPosition.LEFT)
		{

			ac.invert();
		}

		return ac;
	}

	@Override
	public String toString()
	{

		return String.format("Type: %s Done: %.2f Visible: %b DamagePhase: %s Inverted: %b", type, done, visible,
			damageDone, inverted);
	}

	public static enum DamagePhase {

		Not, Now, End;
	}
}
