package de.kvwl.n8dA.robotwars.server.visualization.scene;

import de.kvwl.n8dA.robotwars.server.visualization.Position;
import de.kvwl.n8dA.robotwars.server.visualization.scene.status.StatusScene;
import game.engine.frame.SwingGameFrame;
import game.engine.stage.scene.Scene;

public class SceneTest {

	public static void main(String[] args) {

		Scene scene = getStatusScene();

		SwingGameFrame disp = new SwingGameFrame();
		disp.setLocationRelativeTo(null);

		disp.setScene(scene);

		disp.setVisible(true);
	}

	private static Scene getStatusScene() {

		final StatusScene scene = new StatusScene();

		new Thread(new Runnable() {

			int value = 100;

			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}

					value -= 1;
					if (value < 0) {
						value = 100;
					}

					if (Math.random() > 0.5) {

						scene.setEnergyPoints(Position.RIGHT, value);
						scene.setHealthPoints(Position.RIGHT, value);
					}

					scene.setEnergyPoints(Position.LEFT, value);
					scene.setHealthPoints(Position.LEFT, value);
				}
			}
		}).start();

		return scene;
	}

}
