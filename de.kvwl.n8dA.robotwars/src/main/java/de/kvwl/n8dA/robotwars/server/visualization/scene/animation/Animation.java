package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

public interface Animation {

	/**
	 * true, wenn animation beendet
	 */
	public boolean animate(SceneObject obj, Graphics2D g, long elapsedTime);

	public boolean alive();
}
