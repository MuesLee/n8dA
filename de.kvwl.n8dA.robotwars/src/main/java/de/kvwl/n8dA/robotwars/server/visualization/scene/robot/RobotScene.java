package de.kvwl.n8dA.robotwars.server.visualization.scene.robot;

import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.stage.scene.object.Point;

import java.awt.Graphics2D;
import java.util.EventListener;

import de.kvwl.n8dA.robotwars.server.visualization.Position;

//TODO Marvin: RobotsScene
public class RobotScene implements Scene {

	private static final int ANZ_BLINK_FOR_DAMAGE = 3;

	private static final double HEIGHT = 0.55;
	private static final double SPACE_SIDE = 0.035;
	private static final double SPACE_BOTTOM = 0.08;

	private Robot leftRobo = new Robot();
	private Robot rightRobo = new Robot();

	@Override
	public void paintScene(Graphics2D g2d, int width, int height,
			long elapsedTime) {

		revalidate(width, height);
		paintRobos(g2d, width, height, elapsedTime);
	}

	private void paintRobos(Graphics2D g2d, int width, int height,
			long elapsedTime) {

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

		int _widtLeft = (int) (_height * leftRobo.getRatio());
		int _widtRight = (int) (_height * rightRobo.getRatio());

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

	public void playDamageAnimation(Position pos, boolean wait) {

		if (pos == Position.LEFT) {

			leftRobo.blink(ANZ_BLINK_FOR_DAMAGE, wait);
		} else {

			rightRobo.blink(ANZ_BLINK_FOR_DAMAGE, wait);
		}
	}

	public void setRobo(AnimatedSceneObject leftRobo, Position pos) {

		if (pos == Position.LEFT) {

			this.leftRobo.setRobo(leftRobo);
		} else {

			this.rightRobo.setRobo(leftRobo);
		}
	}

}
