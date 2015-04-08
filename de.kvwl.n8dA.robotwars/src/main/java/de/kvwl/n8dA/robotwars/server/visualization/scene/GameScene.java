package de.kvwl.n8dA.robotwars.server.visualization.scene;

import game.engine.stage.scene.Scene;

import java.awt.Graphics2D;
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
import de.kvwl.n8dA.robotwars.server.visualization.Position;
import de.kvwl.n8dA.robotwars.server.visualization.scene.background.BackgroundScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.Action;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.RobotScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.status.StatusScene;

//TODO Marvin: Game Scene optional
public class GameScene implements Scene, CinematicVisualizer
{

	private static final Logger LOG = LoggerFactory.getLogger(RoboBattleServer.class);

	private StatusScene status = new StatusScene();
	private BackgroundScene background = new BackgroundScene();
	private RobotScene robots = new RobotScene();

	private int round = 0;

	@Override
	public void paintScene(Graphics2D g2d, int width, int height, long elapsedTime)
	{

		background.paintScene(g2d, width, height, elapsedTime);
		robots.paintScene(g2d, width, height, elapsedTime);
		status.paintScene(g2d, width, height, elapsedTime);
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
		}
		catch (IOException e)
		{
			throw new RuntimeException("Roboter nicht gefunden. Laden nicht möglich.");
		}

		updateStats(false, false, position, robot);
	}

	private void updateStats(boolean animated, boolean wait, RobotPosition pos, Robot robo)
	{

		LOG.debug("Update Statistics for Robot {} at Position {}", robo, pos);

		Position position = Position.from(pos);

		if (robo == null)
		{

			return;
		}

		status.setMaxHealthPoints(position, robo.getMaxHealthPoints());
		status.setMaxEnergyPoints(position, robo.getMaxEnergyPoints());

		if (animated)
		{

			status.startHealthPointAnimation(position, robo.getHealthPoints(), wait);
			status.startEnergyPointAnimation(position, robo.getEnergyPoints(), wait);
		}
		else
		{

			status.setHealthPoints(position, robo.getHealthPoints());
			status.setEnergyPoints(position, robo.getEnergyPoints());
		}

		List<StatusEffect> statusEffects = robo.getStatusEffects();
		status.setEffects(position, statusEffects);

	}

	@Override
	public void playFightanimation(Action acLeft, Action acRight, boolean wait)
	{
		LOG.debug("Play Fight Animation Left: {} Right: {} Wait: {}", acLeft, acRight, wait);

		robots.playActionAnimation(acLeft, acRight, wait);
	}

	@Override
	public void prepareForNextRound()
	{

		round++;
		status.setRound(round);
	}

	@Override
	public void roundIsAboutToStart()
	{

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
}
