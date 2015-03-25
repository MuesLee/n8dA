package de.kvwl.n8dA.robotwars.server.visualization.scene;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.EventListener;

import javax.imageio.ImageIO;

import de.kvwl.n8dA.robotwars.server.visualization.Position;
import de.kvwl.n8dA.robotwars.server.visualization.scene.robot.RobotScene;
import de.kvwl.n8dA.robotwars.server.visualization.scene.status.StatusScene;
import game.engine.frame.SwingGameFrame;
import game.engine.image.InternalImage;
import game.engine.image.sprite.DefaultSprite;
import game.engine.stage.scene.FPSScene;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.time.TimeUtils;

public class SceneTest {

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/server/images/";

	public static void main(String[] args) throws Exception {

		Scene scene = getRoboScene();

		SwingGameFrame disp = new SwingGameFrame();
		disp.setLocationRelativeTo(null);

		disp.setScene(new FPSScene(scene));

		disp.setVisible(true);
	}

	private static Scene getRoboScene() throws IOException {

		final StatusScene stats = new StatusScene();
		RobotScene scene = new RobotScene() {

			@Override
			public void paintScene(Graphics2D g2d, int width, int height,
					long elapsedTime) {

				Image bg = InternalImage.loadFromPath(IMAGE_PATH,
						"arena_bg.png");
				g2d.drawImage(bg, 0, 0, width, height, 0, 0, bg.getWidth(null),
						bg.getHeight(null), null);

				super.paintScene(g2d, width, height, elapsedTime);
				stats.paintScene(g2d, width, height, elapsedTime);

			}

			@Override
			public EventListener[] getEventListeners() {

				return new EventListener[] { new KeyAdapter() {
					public void keyReleased(java.awt.event.KeyEvent e) {

						if (e.getKeyCode() == KeyEvent.VK_SPACE) {

							playDamageAnimation(Position.LEFT, false);
						}
					};
				} };
			}
		};

		scene.setRobo(
				new AnimatedSceneObject(
						new DefaultSprite(
								ImageIO.read(new File(
										"../data/animations/robots/PillenRoboter/animation.png")),
								64, 128), TimeUtils
								.NanosecondsOfMilliseconds(100)), Position.LEFT);

		scene.setRobo(
				new AnimatedSceneObject(
						new DefaultSprite(
								ImageIO.read(new File(
										"../data/animations/robots/GreenRoboter/animation.png")),
								64, 128), TimeUtils
								.NanosecondsOfMilliseconds(100)),
				Position.RIGHT);

		return scene;
	}

	@SuppressWarnings("unused")
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
