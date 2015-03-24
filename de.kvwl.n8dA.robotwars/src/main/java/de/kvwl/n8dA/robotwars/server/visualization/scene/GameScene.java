package de.kvwl.n8dA.robotwars.server.visualization.scene;

import game.engine.stage.scene.Scene;

import java.awt.Graphics2D;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

import de.kvwl.n8dA.robotwars.server.visualization.scene.background.BackgroundScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.status.StatusScene;

public class GameScene implements Scene {

	StatusScene status = new StatusScene();
	BackgroundScene background = new BackgroundScene();

	@Override
	public void paintScene(Graphics2D g2d, int width, int height,
			long elapsedTime) {

		background.paintScene(g2d, width, height, elapsedTime);
		status.paintScene(g2d, width, height, elapsedTime);
	}

	@Override
	public EventListener[] getEventListeners() {

		List<EventListener> lis = new LinkedList<EventListener>();

		addListener(status.getEventListeners(), lis);
		addListener(background.getEventListeners(), lis);

		return lis.toArray(new EventListener[lis.size()]);
	}

	private void addListener(EventListener[] eventListeners,
			List<EventListener> lis) {

		if (eventListeners == null || lis == null) {
			return;
		}

		for (int i = 0; i < eventListeners.length; i++) {
			lis.add(eventListeners[i]);
		}
	}

}
