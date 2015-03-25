package de.kvwl.n8dA.robotwars.server.visualization.scene.robot;

import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.Size;

import java.awt.Graphics2D;
import java.util.EventListener;

//TODO Marvin: RobotsScene
public class RobotScene implements Scene {

	private static final double HEIGHT = 0.55;
	private static final double SPACE_SIDE = 0.035;
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

		revalidateRobots(width, height);
	}

	private void revalidateRobots(int width, int height) {

		int _y = (int) (height * SPACE_BOTTOM);
		int _x = (int) (width * SPACE_SIDE);

		int _height = (int) Math.min((height * HEIGHT), width * 2);

		Size tileSizeLeft = leftRobo.getTileSize();
		Size tileSizeRight = rightRobo.getTileSize();

		int _widtLeft = (int) (_height * (tileSizeLeft.getWidth() / (double) tileSizeLeft
				.getHeight()));
		int _widtRight = (int) (_height * (tileSizeRight.getWidth() / (double) tileSizeRight
				.getHeight()));

		if (leftRobo != null) {

			leftRobo.setTopLeftPosition(new Point(_x, height - _height - _y));
			leftRobo.setSize(_widtLeft, _height);
		}

		if (rightRobo != null) {

			rightRobo.setTopLeftPosition(new Point(width - _widtRight - _x,
					height - _height - _y));
			rightRobo.setSize(_widtRight, _height);
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
