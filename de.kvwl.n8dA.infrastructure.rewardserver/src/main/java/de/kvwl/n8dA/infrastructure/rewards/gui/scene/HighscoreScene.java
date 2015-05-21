package de.kvwl.n8dA.infrastructure.rewards.gui.scene;

import java.awt.Graphics2D;
import java.util.EventListener;
import java.util.List;

import de.kvwl.n8dA.infrastructure.commons.entity.HighscoreEntry;
import de.kvwl.n8dA.infrastructure.commons.interfaces.HighscoreListVisualizer;
import game.engine.stage.scene.Scene;

public class HighscoreScene implements Scene, HighscoreListVisualizer
{

	@Override
	public EventListener[] getEventListeners()
	{
		return null;
	}

	@Override
	public void paintScene(Graphics2D g, int width, int height, long elapsedTime)
	{

		//TODO Marvin: paintScene
	}

	@Override
	public void showHighscoreList(String title, List<HighscoreEntry> entries)
	{
		//TODO Marvin: showHighscoreList

	}

}
