package de.kvwl.n8dA.robotwars.server.visualization.scene.background;

import game.engine.image.ImageUtils;
import game.engine.image.InternalImage;
import game.engine.stage.scene.Scene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.EventListener;

public class BackgroundScene implements Scene {

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/server/images/";

	BufferedImage bg = ImageUtils.BufferedImage(InternalImage.loadFromPath(
			IMAGE_PATH, "arena_bg.png"));

	@Override
	public void paintScene(Graphics2D g2d, int width, int height,
			long elapsedTime) {

		g2d.drawImage(bg, 0, 0, width, height, 0, 0, bg.getWidth(),
				bg.getHeight(), null);
	}

	@Override
	public EventListener[] getEventListeners() {
		return null;
	}

}
