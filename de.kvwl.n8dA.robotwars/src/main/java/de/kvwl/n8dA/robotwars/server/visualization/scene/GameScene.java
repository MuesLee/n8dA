package de.kvwl.n8dA.robotwars.server.visualization.scene;

import game.engine.stage.scene.Scene;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

import de.kvwl.n8dA.robotwars.commons.game.entities.Robot;
import de.kvwl.n8dA.robotwars.commons.game.util.RobotPosition;
import de.kvwl.n8dA.robotwars.server.input.DataLoader;
import de.kvwl.n8dA.robotwars.server.visualization.CinematicVisualizer;
import de.kvwl.n8dA.robotwars.server.visualization.Position;
import de.kvwl.n8dA.robotwars.server.visualization.scene.background.BackgroundScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.Action;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.RobotScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.status.StatusScene;

//TODO Marvin: Game Scene optional
public class GameScene implements Scene, CinematicVisualizer
{

	private StatusScene status = new StatusScene();
	private BackgroundScene background = new BackgroundScene();
	private RobotScene robots = new RobotScene();

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

	}

	@Override
	public void robotHasEnteredTheArena(Robot robot, RobotPosition position, DataLoader loader)
	{

		Position pos;

		if (position == RobotPosition.LEFT)
		{

			pos = Position.LEFT;
		}
		else if (position == RobotPosition.RIGHT)
		{

			pos = Position.RIGHT;
		}
		else
		{
			throw new RuntimeException("Unbekannte Position");
		}

		try
		{
			robots.setRobo(loader.createAnimatedSceneObject(robot.getAnimation()), pos);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Roboter nicht gefunden. Laden nicht möglich.");
		}

		updateStats(false, false, position, robot);
	}

	private void updateStats(boolean animated, boolean wait, RobotPosition pos, Robot robo)
	{

		Position position;

		if (pos == RobotPosition.LEFT)
		{

			position = Position.LEFT;
		}
		else if (pos == RobotPosition.RIGHT)
		{

			position = Position.RIGHT;
		}
		else
		{

			throw new RuntimeException("Unbekannte Position");
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
	}

	@Override
	public void playFightanimation(Action acLeft, Action acRight, boolean wait)
	{

		robots.playActionAnimation(acLeft, acRight, wait);
	}

	@Override
	public void prepareForNextRound()
	{

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
}
