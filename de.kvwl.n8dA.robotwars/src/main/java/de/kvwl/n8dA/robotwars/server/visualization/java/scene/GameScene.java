package de.kvwl.n8dA.robotwars.server.visualization.java.scene;

import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.CachedLabelSceneObject;
import game.engine.stage.scene.object.LabelSceneObject;
import game.engine.stage.scene.object.SceneObject;
import game.engine.time.TimeUtils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.network.RoboBattleServer;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.server.visualization.java.Position;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.Animation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.AnimationScene;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.CompoundAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.DelayAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.QueuedAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.RotateAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation.ScaleAnimation;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.background.BackgroundScene;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.Action;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.robot.RobotScene;
import de.kvwl.n8dA.robotwars.server.visualization.java.scene.status.StatusScene;

public class GameScene implements Scene, CinematicVisualizer
{

	private static final Logger LOG = LoggerFactory.getLogger(RoboBattleServer.class);

	private StatusScene status = new StatusScene();
	private BackgroundScene background = new BackgroundScene();
	private RobotScene robots = new RobotScene();
	private AnimationScene animations = new AnimationScene();

	private int round = 0;

	@Override
	public void paintScene(Graphics2D g2d, int width, int height, long elapsedTime)
	{

		background.paintScene(g2d, width, height, elapsedTime);
		robots.paintScene(g2d, width, height, elapsedTime);
		status.paintScene(g2d, width, height, elapsedTime);
		animations.paintScene(g2d, width, height, elapsedTime);
	}

	@Override
	public EventListener[] getEventListeners()
	{

		List<EventListener> lis = new LinkedList<EventListener>();

		addListener(status.getEventListeners(), lis);
		addListener(background.getEventListeners(), lis);
		addListener(robots.getEventListeners(), lis);

		return lis.toArray(new EventListener[lis.size()]);
	}

	private void addListener(EventListener[] eventListeners, List<EventListener> lis)
	{

		if (eventListeners == null || lis == null)
		{
			return;
		}

		for (int i = 0; i < eventListeners.length; i++)
		{
			lis.add(eventListeners[i]);
		}
	}

	@Override
	public void battleIsAboutToStart()
	{

		round = 0;
	}

	@Override
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position, DataLoader loader)
	{

		Position pos = Position.from(position);

		try
		{
			robots.setRobo((robot != null) ? loader.createAnimatedSceneObject(robot.getAnimation()) : null, pos);
			robots.setRobotName((robot != null) ? robot.getNickname() : "", pos);
		}
		catch (IOException e)
		{

			robots.setRobotName("", pos);
			throw new RuntimeException("Roboter nicht gefunden. Laden nicht möglich.");
		}

		updateStats(false, false, position, robot);
	}

	private void updateStats(boolean animated, boolean wait, RobotPosition pos, Robot robo)
	{

		updateHealthpoints(robo, pos, animated, wait);
		updateEnergypoints(robo, pos, animated, wait);
		updateEffects(robo, pos);
	}

	@Override
	public void updateEffects(Robot robo, RobotPosition pos)
	{

		LOG.debug("Update Effect Statistics for Robot {} at Position {}", robo, pos);

		Position position = Position.from(pos);

		if (robo == null)
		{

			return;
		}

		List<StatusEffect> statusEffects = robo.getStatusEffects();
		status.setEffects(position, statusEffects);
	}

	@Override
	public void updateEnergypoints(Robot robo, RobotPosition pos, boolean animated, boolean wait)
	{

		LOG.debug("Update Energy Statistics for Robot {} at Position {}", robo, pos);

		Position position = Position.from(pos);

		if (robo == null)
		{

			return;
		}

		status.setMaxEnergyPoints(position, robo.getMaxEnergyPoints());

		if (animated)
		{

			status.startEnergyPointAnimation(position, robo.getEnergyPoints(), wait);
		}
		else
		{

			status.setEnergyPoints(position, robo.getEnergyPoints());
		}
	}

	@Override
	public void updateHealthpoints(Robot robo, RobotPosition pos, boolean animated, boolean wait)
	{

		LOG.debug("Update Health Statistics for Robot {} at Position {}", robo, pos);

		Position position = Position.from(pos);

		if (robo == null)
		{

			return;
		}

		status.setMaxHealthPoints(position, robo.getMaxHealthPoints());

		if (animated)
		{

			status.startHealthPointAnimation(position, robo.getHealthPoints(), wait);
		}
		else
		{

			status.setHealthPoints(position, robo.getHealthPoints());
		}
	}

	@Override
	public void playFightanimation(Action acLeft, Action acRight, boolean wait)
	{
		LOG.debug("Play Fight Animation Left: {} Right: {} Wait: {}", acLeft, acRight, wait);

		robots.playActionAnimation(acLeft, acRight, wait);
	}

	@Override
	public void showAnimation(SceneObject obj, Animation animation, Rectangle2D bounds, boolean wait)
	{

		animations.showAnimation(obj, animation, bounds, wait);
	}

	@Override
	public void prepareForNextRound(boolean wait)
	{

		round++;
		status.setRound(round);
	}

	@Override
	public void roundIsAboutToStart(boolean wait)
	{

		LabelSceneObject obj = new CachedLabelSceneObject("Fight");
		obj.setOutlineColor(Color.LIGHT_GRAY);
		obj.setColor(new Color(0, 0, 0));
		obj.setStroke(new BasicStroke(1.5f));
		obj.setFont(new Font("Comic Sans MS", Font.BOLD, 8));

		Rectangle2D bounds = new Rectangle2D.Float(0, 0, 1, 1);

		Animation animation = new QueuedAnimation(new CompoundAnimation(new ScaleAnimation(0, 1,
			TimeUtils.NanosecondsOfSeconds(1)), new RotateAnimation(0, Math.PI * 2, false,
			TimeUtils.NanosecondsOfSeconds(1))), new DelayAnimation(TimeUtils.NanosecondsOfSeconds(1)));

		showAnimation(obj, animation, bounds, wait);
	}

	@Override
	public void updateStats(Robot robot, RobotPosition position, boolean animated, boolean wait)
	{

		updateStats(animated, wait, position, robot);
	}

	@Override
	public void reset()
	{

		robots.setRobo(null, Position.LEFT);
		robots.setRobo(null, Position.RIGHT);

		robots.setRobotName("", Position.LEFT);
		robots.setRobotName("", Position.RIGHT);

		round = 0;
		status.setRound(1);

		status.setEnergyPoints(Position.LEFT, 100);
		status.setMaxEnergyPoints(Position.LEFT, 100);

		status.setEnergyPoints(Position.RIGHT, 100);
		status.setMaxEnergyPoints(Position.RIGHT, 100);

		status.setHealthPoints(Position.LEFT, 100);
		status.setMaxHealthPoints(Position.LEFT, 100);

		status.setHealthPoints(Position.RIGHT, 100);
		status.setMaxHealthPoints(Position.RIGHT, 100);

		status.resetEffects(Position.LEFT);
		status.resetEffects(Position.RIGHT);
	}

	@Override
	public void playSound(String string)
	{
		//TODO Timo: Not implemented playSound
	}
}
