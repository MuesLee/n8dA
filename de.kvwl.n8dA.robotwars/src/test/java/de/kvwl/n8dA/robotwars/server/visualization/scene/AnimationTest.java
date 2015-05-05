package de.kvwl.n8dA.robotwars.server.visualization.scene;

import game.engine.frame.SwingGameFrame;
import game.engine.stage.scene.FPSScene;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.Point;
import game.engine.time.TimeUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.EventListener;

import de.kvwl.n8dA.robotwars.server.visualization.scene.animation.Label;
import de.kvwl.n8dA.robotwars.server.visualization.scene.animation.ObjectAnimator;
import de.kvwl.n8dA.robotwars.server.visualization.scene.animation.ScaleAnimation;

public class AnimationTest {

	public static void main(String[] args) throws Exception {

		Scene scene = getScene();

		SwingGameFrame disp = new SwingGameFrame();
		disp.setLocationRelativeTo(null);

		disp.setScene(new FPSScene(scene));

		disp.setVisible(true);
	}

	private static Scene getScene() throws IOException {

		final Label lbl = new Label();
		lbl.setTopLeftPosition(new Point(0, 0));
		lbl.setText("Animation...");

		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					lbl.startAnimator(new ObjectAnimator(new ScaleAnimation(0,
							1.0, TimeUtils.NanosecondsOfSeconds(2))), true);
				}
			}
		}).start();

		final Scene scene = new Scene() {

			@Override
			public void paintScene(Graphics2D g, int width, int height,
					long time) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, width, height);

				lbl.setSize(width, height);
				lbl.paintOnScene(g, time);
			}

			@Override
			public EventListener[] getEventListeners() {
				return new EventListener[] { new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {

						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

							System.exit(0);
						}
					}
				} };
			}
		};

		return scene;
	}
}
