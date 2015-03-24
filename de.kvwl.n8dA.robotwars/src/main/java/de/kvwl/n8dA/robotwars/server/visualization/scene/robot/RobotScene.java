package de.kvwl.n8dA.robotwars.server.visualization.scene.robot;

import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.Point;

import java.awt.Graphics2D;
import java.util.EventListener;

//TODO Marvin: RobotsScene
public class RobotScene implements Scene {

	private static final double HEIGHT = 0.55;
	private static final double SPACE_SIDE = 0.07;
	private static final double SPACE_BOTTOM = 0.08;

	private AnimatedSceneObject leftRobo;
	private AnimatedSceneObject rightRobo;

	@Override
	public void paintScene(Graphics2D g2d, int width, int height,
			long elapsedTime) {

		revalidate(width, height);
		paintRobos(g2d, width, height, elapsedTime);
	}

	private void paintRobos(Graphics2D g2d, int width, int height,
			long elapsedTime) {

		System.out.println(leftRobo + " " + rightRobo);
		if (leftRobo != null) {
			leftRobo.paintOnScene(g2d, elapsedTime);
		}

		if (rightRobo != null) {
			rightRobo.paintOnScene(g2d, elapsedTime);
		}
	}

	private void revalidate(int width, int height) {

		int _width = 0;
		int _height = 0;

		int _y;
		int _x;

		_y = (int) (height * SPACE_BOTTOM);
		_x = (int) (width * SPACE_SIDE);

		_height = (int) Math.min((height * HEIGHT), width * 2);
		_width = (int) (_height * 0.5);

		if (leftRobo != null) {

			leftRobo.setTopLeftPosition(new Point(_x, height - _height - _y));
			leftRobo.setSize(_width, _height);
		}

		if (rightRobo != null) {

			rightRobo.setTopLeftPosition(new Point(width - _width - _x, height
					- _height - _y));
			rightRobo.setSize(_width, _height);
		}
	}

	@Override
	public EventListener[] getEventListeners() {
		return null;
	}

	public void setLeftRobo(AnimatedSceneObject leftRobo) {
		this.leftRobo = leftRobo;
	}

	public void setRightRobo(AnimatedSceneObject rightRobo) {
		this.rightRobo = rightRobo;
	}

}
