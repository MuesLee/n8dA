package de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot;

import game.engine.image.InternalImage;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.CachedLabelSceneObject;
import game.engine.stage.scene.object.CachedNinePatchImageSceneObject;
import game.engine.stage.scene.object.LabelSceneObject;
import game.engine.stage.scene.object.Orientation.HorizontalOrientation;
import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.ScaleStrategy;
import game.engine.stage.scene.object.SpriteSceneObject;
import game.engine.time.TimeUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.EventListener;

import de.kvwl.n8dA.robotwars.server.visualization.java.CinematicVisualizerImpl;
import de.kvwl.n8dA.robotwars.server.visualization.java.Position;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.BackgroundObject;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.Insets;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.Action.DamagePhase;

public class RobotScene implements Scene
{

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private static final int ANZ_BLINK_FOR_DAMAGE = 3;
	private static final long AC_ANIMATION_SPEED = TimeUtils.NanosecondsOfMilliseconds(1800);

	public static final double HEIGHT_ROBOT = 0.55;
	private static final double WIDTH_TEXT = 0.33;
	public static final double SPACE_SIDE = 0.08;
	private static final double SPACE_SIDE_TEXT = 0.01;
	public static final double SPACE_BOTTOM = 0.08;
	private static final double SPACE_BOTTOM_TEXT = 0.01;

	private static final double AC_ATK_HEIGHT = HEIGHT_ROBOT * 0.4;
	private static final double AC_DEF_HEIGHT = HEIGHT_ROBOT * 0.8;
	private static final double DEF_OVERLAP = 0.4;
	private static final double ATK_OVERLAP = DEF_OVERLAP * 0.5;
	private static final double NEAR_FOR_DEFENSE = 0.7;

	private Robot leftRobo = new Robot();
	private Robot rightRobo = new Robot();

	private LabelSceneObject lblLeft = new CachedLabelSceneObject();
	private LabelSceneObject lblRight = new CachedLabelSceneObject();

	private BackgroundObject backedLabelLeft = new BackgroundObject(lblLeft, new CachedNinePatchImageSceneObject(
		InternalImage.loadFromPath(IMAGE_PATH, "txt_bg.9.png")), new Insets(0.08, 0.03, 0.03, 0.12));
	private BackgroundObject backedLabelRight = new BackgroundObject(lblRight, new CachedNinePatchImageSceneObject(
		InternalImage.loadFromPath(IMAGE_PATH, "txt_bg.9.png")), new Insets(0.08, 0.03, 0.03, 0.12));

	private Action acLeft;
	private Action acRight;
	private Object acWait = new Object();

	public RobotScene()
	{

		leftRobo.setInverted(true);
		rightRobo.setInverted(false);

		lblLeft.setColor(Color.BLACK);
		lblLeft.setHorizontalTextOrientation(HorizontalOrientation.Center);
		lblLeft.setScaleStrategy(ScaleStrategy.FitParent);

		lblRight.setColor(Color.BLACK);
		lblRight.setHorizontalTextOrientation(HorizontalOrientation.Center);
		lblRight.setScaleStrategy(ScaleStrategy.FitParent);
	}

	@Override
	public void paintScene(Graphics2D g2d, int width, int height, long elapsedTime)
	{

		revalidate(width, height, elapsedTime);
		paintRobos(g2d, width, height, elapsedTime);
		paintLabels(g2d, width, height, elapsedTime);
		paintActions(g2d, width, height, elapsedTime);
	}

	private void paintLabels(Graphics2D g2d, int width, int height2, long elapsedTime)
	{

		backedLabelLeft.paintOnScene(g2d, elapsedTime);
		backedLabelRight.paintOnScene(g2d, elapsedTime);
	}

	private void paintRobos(Graphics2D g2d, int width, int height, long elapsedTime)
	{

		if (leftRobo != null)
		{
			leftRobo.paintOnScene(g2d, elapsedTime);
		}

		if (rightRobo != null)
		{
			rightRobo.paintOnScene(g2d, elapsedTime);
		}
	}

	private void revalidate(int width, int height, long elapsedTime)
	{

		revalidateRobots(width, height);
		revalidateLabels(width, height, elapsedTime);
		revalidateActions(width, height, elapsedTime);

	}

	private void revalidateLabels(int width, int height, long elapsedTime)
	{

		int _y = (int) (height * SPACE_BOTTOM_TEXT);
		int _x = (int) (width * SPACE_SIDE_TEXT);

		int _height = (int) (height * (SPACE_BOTTOM - SPACE_BOTTOM_TEXT));
		int _width = (int) (width * WIDTH_TEXT);

		backedLabelLeft.setSize(_width, _height);
		backedLabelLeft.setPosition(_x, height - _height - _y);

		backedLabelRight.setSize(_width, _height);
		backedLabelRight.setPosition(width - _width - _x, height - _height - _y);
	}

	private void revalidateRobots(int width, int height)
	{

		int _y = (int) (height * SPACE_BOTTOM);
		int _x = (int) (width * SPACE_SIDE);

		int _height = (int) (height * HEIGHT_ROBOT);

		int _widtLeft = (int) (_height * leftRobo.getRatio());
		int _widtRight = (int) (_height * rightRobo.getRatio());

		if (leftRobo != null)
		{

			leftRobo.setTopLeftPosition(new Point(_x, height - _height - _y));
			leftRobo.setSize(_widtLeft, _height);
		}

		if (rightRobo != null)
		{

			rightRobo.setTopLeftPosition(new Point(width - _widtRight - _x, height - _height - _y));
			rightRobo.setSize(_widtRight, _height);
		}
	}

	private void paintActions(Graphics2D g2d, int width, int height, long elapsedTime)
	{

		// Situationen müssen unterschieden werden, damit atk immer über def
		// gezeichnet wird
		if (acLeft != null && acRight != null)
		{

			if (acLeft.getType().isDefendingType() && acLeft.isVisible())
			{

				acLeft.paintOnScene(g2d, elapsedTime);
				if (acRight.isVisible())
				{

					acRight.paintOnScene(g2d, elapsedTime);
				}
			}
			else
			{

				if (acRight.isVisible())
				{

					acRight.paintOnScene(g2d, elapsedTime);
				}

				if (acLeft.isVisible())
				{

					acLeft.paintOnScene(g2d, elapsedTime);
				}
			}
		}
		else
		{

			if (acLeft != null && acLeft.isVisible())
			{

				acLeft.paintOnScene(g2d, elapsedTime);
			}

			if (acRight != null && acRight.isVisible())
			{

				acRight.paintOnScene(g2d, elapsedTime);
			}
		}
	}

	private void revalidateActions(int width, int height, long elapsedTime)
	{
		if (acLeft == null && acRight == null)
		{
			return;
		}

		revalidateAction(acLeft, width, height);
		revalidateAction(acRight, width, height);

		calculateActionAnimation(width, height, elapsedTime);
	}

	private void revalidateAction(Action ac, int width, int height)
	{

		if (ac == null)
		{
			return;
		}

		double ratio = ac.getRatio();

		int _dif = 0;

		int _y = (int) (height * SPACE_BOTTOM);

		int _height = (int) (height * ((ac.getType() == ActionType.Attack) ? AC_ATK_HEIGHT : AC_DEF_HEIGHT));
		int _width = (int) (_height * ratio);

		if (ac == acLeft)
		{

			_dif = (leftRobo.getHeight() - _height) / 2;
		}
		else
		{

			_dif = (rightRobo.getHeight() - _height) / 2;
		}

		ac.setSize(_width, _height);
		ac.setTopLeftPosition(new Point(0, height - _height - _y - _dif));
	}

	private void calculateActionAnimation(int width, int height, long elapsedTime)
	{

		// System.out.println(acLeft + " | " + acRight);
		double elapsedAni = elapsedTime / (double) AC_ANIMATION_SPEED;

		// Positionsbestimmung
		int _x;

		if (acLeft != null)
		{

			// Standard Verteidigungsposition
			_x = (int) (leftRobo.getX() + leftRobo.getWidth() * (1 - DEF_OVERLAP));

			// Links greift an
			if (!acLeft.getType().isDefendingType())
			{

				// Für den Angriff Position nach der Zeit vorsetzen
				acLeft.setDone(Math.min(acLeft.getDone() + elapsedAni, 1));

				double possibleWidth = (rightRobo.getX() + rightRobo.getWidth() * ATK_OVERLAP) - _x - acLeft.getWidth();
				_x += (int) (acLeft.getDone() * possibleWidth);

				if (acLeft.getDone() >= 1 && acLeft.getDamage() == DamagePhase.Not)
				{
					acLeft.setVisible(false);

					if ((acRight == null || acRight.getType().isDamageConsuming()))
					{

						playAcDamageAnimation(acLeft, Position.RIGHT);
					}
					else
					{

						acLeft.setDamage(DamagePhase.End);
					}
				}
			}
			else
			{
				acLeft.setDamage(DamagePhase.End);
				acLeft.setVisible(false);

				boolean isOtherDefendingOrNull = acRight == null || acRight.getType().isDefendingType();
				boolean isAtkNear = acRight != null && acRight.getDone() > NEAR_FOR_DEFENSE;
				boolean isAtkFinished = !isOtherDefendingOrNull && acRight.getDone() >= 1;

				// Verteidigung mit der Zeit ausblenden
				if (isOtherDefendingOrNull || isAtkNear)
				{

					acLeft.setVisible(true);
					acLeft.setDone(Math.min(acLeft.getDone() + elapsedAni, 1));
				}

				if (isAtkFinished)
				{

					acRight.setVisible(false);

					if (acLeft.getType() == ActionType.ReflectingDefense)
					{

						acLeft = acRight;
						acRight = null;

						acLeft.invert();
						acLeft.setVisible(true);
						acLeft.setDone(0);
						acLeft.setDamage(DamagePhase.Not);
					}
				}
			}

			acLeft.setTopLeftPosition(new Point(_x, acLeft.getTopLeftPosition().getY()));
		}

		if (acRight != null)
		{

			_x = (int) (rightRobo.getX() - rightRobo.getWidth() * DEF_OVERLAP);

			if (!acRight.getType().isDefendingType())
			{

				// Angriff berechnen
				acRight.setDone(Math.min(acRight.getDone() + elapsedAni, 1));

				double possibleWidth = _x - (leftRobo.getX() + leftRobo.getWidth() * (1 - ATK_OVERLAP));
				_x -= (int) (acRight.getDone() * possibleWidth);

				if (acRight.getDone() >= 1 && acRight.getDamage() == DamagePhase.Not)
				{
					acRight.setVisible(false);

					if ((acLeft == null || acLeft.getType().isDamageConsuming()))
					{

						playAcDamageAnimation(acRight, Position.LEFT);
					}
					else
					{

						acRight.setDamage(DamagePhase.End);
					}
				}
			}
			else
			{
				acRight.setDamage(DamagePhase.End);
				acRight.setVisible(false);

				boolean isOtherDefendingOrNull = acLeft == null || acLeft.getType().isDefendingType();
				boolean isAtkNear = acLeft != null && acLeft.getDone() > NEAR_FOR_DEFENSE;
				boolean isAtkFinished = !isOtherDefendingOrNull && acLeft.getDone() >= 1;

				// Verteidigung mit der Zeit ausblenden
				if (isOtherDefendingOrNull || isAtkNear)
				{

					acRight.setVisible(true);
					acRight.setDone(Math.min(acRight.getDone() + elapsedAni, 1));
				}

				if (isAtkFinished)
				{

					acLeft.setVisible(false);

					if (acRight.getType() == ActionType.ReflectingDefense)
					{
						acRight = acLeft;
						acLeft = null;

						acRight.invert();
						acRight.setVisible(true);
						acRight.setDone(0);
						acRight.setDamage(DamagePhase.Not);
						CinematicVisualizerImpl.get().playSound("defenseReflection");
					}
				}
			}

			acRight.setTopLeftPosition(new Point(_x, acRight.getTopLeftPosition().getY()));
		}

		checkActionState();
	}

	private void playAcDamageAnimation(final Action ac, final Position pos)
	{
		ac.setDamage(DamagePhase.Now);

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{

				playDamageAnimation(pos, true);
				ac.setDamage(DamagePhase.End);
			}
		}).start();
	}

	private void checkActionState()
	{
		boolean ready = true;

		if (acLeft != null)
		{

			if (acLeft.getDone() < 1 || acLeft.getDamage() != DamagePhase.End)
			{
				ready = false;
			}
		}

		if (acRight != null)
		{

			if (acRight.getDone() < 1 || acRight.getDamage() != DamagePhase.End)
			{
				ready = false;
			}
		}

		if (ready)
		{
			actionAnimationFinished();
		}
	}

	@Override
	public EventListener[] getEventListeners()
	{
		return null;
	}

	public void playDamageAnimation(Position pos, boolean wait)
	{

		if (pos == Position.LEFT)
		{

			leftRobo.blink(ANZ_BLINK_FOR_DAMAGE, wait);
			CinematicVisualizerImpl.get().playSound("default_hit");
		}
		else
		{
			rightRobo.blink(ANZ_BLINK_FOR_DAMAGE, wait);
			CinematicVisualizerImpl.get().playSound("default_hit");
		}
	}

	public void playActionAnimation(Action acLeft, Action acRight, boolean wait)
	{

		this.acLeft = acLeft;
		this.acRight = acRight;

		if (wait)
		{

			synchronized (acWait)
			{
				try
				{
					acWait.wait();
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}

	private void actionAnimationFinished()
	{

		acLeft = null;
		acRight = null;

		synchronized (acWait)
		{
			acWait.notifyAll();
		}
	}

	public void setRobo(SpriteSceneObject robo, Position pos)
	{

		if (pos == Position.LEFT)
		{

			this.leftRobo.setRobo(robo);
		}
		else
		{

			this.rightRobo.setRobo(robo);
		}
	}

	public void setRobotName(String name, Position pos)
	{

		if (pos == Position.LEFT)
		{

			this.lblLeft.setText(name + " ");
		}
		else
		{

			this.lblRight.setText(name + " ");
		}
	}

}
