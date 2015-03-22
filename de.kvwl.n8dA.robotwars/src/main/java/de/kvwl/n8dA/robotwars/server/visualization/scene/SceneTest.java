package de.kvwl.n8dA.robotwars.server.visualization.scene;

import de.kvwl.n8dA.robotwars.server.visualization.scene.status.StatusScene;
import game.engine.frame.SwingGameFrame;

public class SceneTest {

	public static void main(String[] args) {

		SwingGameFrame disp = new SwingGameFrame();
		disp.setLocationRelativeTo(null);

		disp.setScene(new StatusScene());

		disp.setVisible(true);
	}

}
