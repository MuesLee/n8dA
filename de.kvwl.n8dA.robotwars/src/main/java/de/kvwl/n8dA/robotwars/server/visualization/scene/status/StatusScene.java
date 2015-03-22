package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import java.awt.Graphics2D;
import java.util.EventListener;

import de.kvwl.n8dA.robotwars.server.visualization.Position;
import game.engine.image.ImageUtils;
import game.engine.image.InternalImage;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.ImageSceneObject;
import game.engine.stage.scene.object.Point;

//TODO Marvin: Status Scene
public class StatusScene implements Scene {

	private static final double BAR_WIDTH = 0.33;
	private static final double TOP_SPACE = 0.01;
	private static final double BAR_HEIGHT = 0.03;

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/client/images/";

	private HealthPoints healthLeft = new HealthPoints();
	private HealthPoints healthRight = new HealthPoints();

	private EnergyPoints energyRight = new EnergyPoints();
	private EnergyPoints energyLeft = new EnergyPoints();

	private ImageSceneObject imgHealthLeft = new ImageSceneObject(
			ImageUtils.BufferedImage(InternalImage.loadFromPath(IMAGE_PATH,
					"life.png")));
	private ImageSceneObject imgHealthRight = new ImageSceneObject(
			ImageUtils.BufferedImage(InternalImage.loadFromPath(IMAGE_PATH,
					"life.png")));

	private ImageSceneObject imgEnergyLeft = new ImageSceneObject(
			ImageUtils.BufferedImage(InternalImage.loadFromPath(IMAGE_PATH,
					"energy.png")));
	private ImageSceneObject imgEnergyRight = new ImageSceneObject(
			ImageUtils.BufferedImage(InternalImage.loadFromPath(IMAGE_PATH,
					"energy.png")));

	public StatusScene() {

		healthLeft.setPos(Position.LEFT);
		healthRight.setPos(Position.RIGHT);

		energyLeft.setPos(Position.LEFT);
		energyRight.setPos(Position.RIGHT);
	}

	@Override
	public void paintScene(Graphics2D g2d, int width, int height,
			long elapsedTime) {

		revalidate(width, height);

		healthLeft.paintOnScene(g2d, elapsedTime);
		healthRight.paintOnScene(g2d, elapsedTime);

		energyLeft.paintOnScene(g2d, elapsedTime);
		energyRight.paintOnScene(g2d, elapsedTime);

		imgEnergyLeft.paintOnScene(g2d, elapsedTime);
		imgEnergyRight.paintOnScene(g2d, elapsedTime);

		imgHealthLeft.paintOnScene(g2d, elapsedTime);
		imgHealthRight.paintOnScene(g2d, elapsedTime);
	}

	private void revalidate(int width, int height) {

		revalidateIcons(width, height);
		revalidateBars(width, height);
	}

	private void revalidateIcons(int width, int height) {

		double _width = width * BAR_HEIGHT;
		double _height = height * BAR_HEIGHT;

		double _x = width * 0.01;
		double _y = height * TOP_SPACE;

		imgHealthLeft.setSize((int) _width, (int) _height);
		imgHealthLeft.setTopLeftPosition(new Point((int) _x, (int) _y));

		imgEnergyLeft.setSize((int) _width, (int) _height);
		imgEnergyLeft.setTopLeftPosition(new Point((int) _x,
				(int) (_y + _height)));

		imgHealthRight.setSize((int) _width, (int) _height);
		imgHealthRight.setTopLeftPosition(new Point(
				(int) (width - _x - _width), (int) _y));

		imgEnergyRight.setSize((int) _width, (int) _height);
		imgEnergyRight.setTopLeftPosition(new Point(
				(int) (width - _x - _width), (int) (_y + _height)));
	}

	private void revalidateBars(int width, int height) {

		double _width = width * BAR_WIDTH;
		double _height = height * BAR_HEIGHT;

		double _x = width * 0.04;
		double _y = height * 0.01;

		healthLeft.setSize((int) _width, (int) _height);
		healthLeft.setTopLeftPosition(new Point((int) _x, (int) _y));

		healthRight.setSize((int) _width, (int) _height);
		healthRight.setTopLeftPosition(new Point((int) (width - _x - _width),
				(int) _y));

		energyLeft.setSize((int) _width, (int) _height);
		energyLeft
				.setTopLeftPosition(new Point((int) _x, (int) (_y + _height)));

		energyRight.setSize((int) _width, (int) _height);
		energyRight.setTopLeftPosition(new Point((int) (width - _x - _width),
				(int) (_y + _height)));
	}

	@Override
	public EventListener[] getEventListeners() {
		return null;
	}

	public void setHealthPoints(Position pos, int value) {

		if (pos == Position.LEFT) {

			healthLeft.setValue(value);
		} else {

			healthRight.setValue(value);
		}
	}

	public void setMaxHealthPoints(Position pos, int value) {

		if (pos == Position.LEFT) {

			healthLeft.setMaxValue(value);
		} else {

			healthRight.setMaxValue(value);
		}
	}

	public void setEnergyPoints(Position pos, int value) {

		if (pos == Position.LEFT) {

			energyLeft.setValue(value);
		} else {

			energyRight.setValue(value);
		}
	}

	public void setMaxEnergyPoints(Position pos, int value) {

		if (pos == Position.LEFT) {

			energyLeft.setMaxValue(value);
		} else {

			energyRight.setMaxValue(value);
		}
	}
}
