package de.kvwl.n8dA.robotwars.server.visualization.scene;

import java.awt.Graphics2D;
import java.awt.Image;

import de.kvwl.n8dA.robotwars.server.visualization.Position;
import de.kvwl.n8dA.robotwars.server.visualization.scene.status.StatusScene;
import game.engine.frame.SwingGameFrame;
import game.engine.image.InternalImage;
import game.engine.stage.scene.FPSScene;
import game.engine.stage.scene.Scene;

public class SceneTest {

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/server/images/";

	public static void main(String[] args) {

		Scene scene = getGameScene();

		SwingGameFrame disp = new SwingGameFrame();
		disp.setLocationRelativeTo(null);

		disp.setScene(new FPSScene(scene));

		disp.setVisible(true);
	}

	private static Scene getGameScene() {

		return new GameScene();
	}

	@SuppressWarnings("unused")
	private static Scene getStatusScene() {

		final StatusScene scene = new StatusScene() {

			@Override
			public void paintScene(Graphics2D g2d, int width, int height,
					long elapsedTime) {

				Image bg = InternalImage.loadFromPath(IMAGE_PATH,
						"arena_bg.png");
				g2d.drawImage(bg, 0, 0, width, height, 0, 0, bg.getWidth(null),
						bg.getHeight(null), null);

				super.paintScene(g2d, width, height, elapsedTime);
			}
		};

		new Thread(new Runnable() {

			int value = 100;
			double round = 0;

			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}

					round += 0.25;
					if (round >= 21) {
						round = 1;
					}

					value -= 1;
					if (value < 0) {
						value = 100;
					}

					if (Math.random() > 0.5) {

						scene.setEnergyPoints(Position.RIGHT, value);
						scene.setHealthPoints(Position.RIGHT, value);
					}

					scene.setRound((int) round);

					scene.setEnergyPoints(Position.LEFT, value);
					scene.setHealthPoints(Position.LEFT, value);
				}
			}
		}).start();

		return scene;
	}

}
