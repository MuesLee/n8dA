package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import game.engine.image.ImageUtils;
import game.engine.image.InternalImage;
import game.engine.stage.scene.Scene;
import game.engine.stage.scene.object.ImageSceneObject;
import game.engine.stage.scene.object.Point;

import java.awt.Graphics2D;
import java.util.EventListener;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;
import de.kvwl.n8dA.robotwars.server.visualization.Position;

public class StatusScene implements Scene {

	private static final Logger LOG = LoggerFactory
			.getLogger(StatusScene.class);

	private static final double SIDE_SPACE = 0.04;
	private static final double BAR_WIDTH = 0.33;
	private static final double TOP_SPACE = 0.01;
	private static final double BAR_HEIGHT = 0.03;
	private static final double BAR_HEIGHT_EFFECT_ADD = 1.5;

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private HealthPoints healthLeft = new HealthPoints();
	private HealthPoints healthRight = new HealthPoints();

	private EnergyPoints energyRight = new EnergyPoints();
	private EnergyPoints energyLeft = new EnergyPoints();

	private EffectLabel effectLeft = new EffectLabel();
	private EffectLabel effectRight = new EffectLabel();

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

	private RoundLabel lblRound = new RoundLabel();

	public StatusScene() {

		healthLeft.setPos(Position.LEFT);
		healthRight.setPos(Position.RIGHT);

		energyLeft.setPos(Position.LEFT);
		energyRight.setPos(Position.RIGHT);

		effectLeft.setOrientation(Position.LEFT);
		effectRight.setOrientation(Position.RIGHT);
	}

	@Override
	public void paintScene(Graphics2D g2d, int width, int height,
			long elapsedTime) {

		revalidate(width, height);

		healthLeft.paintOnScene(g2d, elapsedTime);
		healthRight.paintOnScene(g2d, elapsedTime);

		energyLeft.paintOnScene(g2d, elapsedTime);
		energyRight.paintOnScene(g2d, elapsedTime);

		effectLeft.paintOnScene(g2d, elapsedTime);
		effectRight.paintOnScene(g2d, elapsedTime);

		imgEnergyLeft.paintOnScene(g2d, elapsedTime);
		imgEnergyRight.paintOnScene(g2d, elapsedTime);

		imgHealthLeft.paintOnScene(g2d, elapsedTime);
		imgHealthRight.paintOnScene(g2d, elapsedTime);

		lblRound.paintOnScene(g2d, elapsedTime);
	}

	private void revalidate(int width, int height) {

		revalidateIcons(width, height);
		revalidateBars(width, height);
		revalidateEffects(width, height);
		revalidateRoundLabel(width, height);
	}

	private void revalidateRoundLabel(int width, int height) {

		double _x = width * SIDE_SPACE * 2 + width * BAR_WIDTH;
		double _y = height * TOP_SPACE;

		double _width = width - _x * 2;
		double _height = height * (1 / BAR_WIDTH) * 0.04;

		lblRound.setSize((int) _width, (int) _height);
		lblRound.setTopLeftPosition(new Point((int) _x, (int) _y));
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

	private void revalidateEffects(int width, int height) {

		double _width = width * BAR_WIDTH;
		double _height = height * BAR_HEIGHT;

		double _x = width * SIDE_SPACE;
		double _y = height * TOP_SPACE;

		effectLeft.setSize((int) _width,
				(int) (_height * BAR_HEIGHT_EFFECT_ADD));
		effectLeft.setTopLeftPosition(new Point((int) _x,
				(int) (_y + 2 * _height)));

		effectRight.setSize((int) _width,
				(int) (_height * BAR_HEIGHT_EFFECT_ADD));
		effectRight.setTopLeftPosition(new Point((int) (width - _x - _width),
				(int) (_y + 2 * _height)));
	}

	private void revalidateBars(int width, int height) {

		double _width = width * BAR_WIDTH;
		double _height = height * BAR_HEIGHT;

		double _x = width * SIDE_SPACE;
		double _y = height * TOP_SPACE;

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

	public void startHealthPointAnimation(Position pos, double value,
			boolean wait) {

		if (pos == Position.LEFT) {

			healthLeft.startValueAnimation(value, wait);
		} else {

			healthRight.startValueAnimation(value, wait);
		}
	}

	public void setHealthPoints(Position pos, int value) {

		LOG.debug("Setting health for {} -> {}", pos, value);

		if (pos == Position.LEFT) {

			healthLeft.setValue(value);
		} else {

			healthRight.setValue(value);
		}
	}

	public void setMaxHealthPoints(Position pos, int value) {

		LOG.debug("Setting max health for {} -> {}", pos, value);

		if (pos == Position.LEFT) {

			healthLeft.setMaxValue(value);
		} else {

			healthRight.setMaxValue(value);
		}
	}

	public void startEnergyPointAnimation(Position pos, double value,
			boolean wait) {

		if (pos == Position.LEFT) {

			energyLeft.startValueAnimation(value, wait);
		} else {

			energyRight.startValueAnimation(value, wait);
		}
	}

	public void setEnergyPoints(Position pos, int value) {
		LOG.debug("Setting energy for {} -> {}", pos, value);

		if (pos == Position.LEFT) {

			energyLeft.setValue(value);
		} else {

			energyRight.setValue(value);
		}
	}

	public void setMaxEnergyPoints(Position pos, int value) {
		LOG.debug("Setting max energy for {} -> {}", pos, value);

		if (pos == Position.LEFT) {

			energyLeft.setMaxValue(value);
		} else {

			energyRight.setMaxValue(value);
		}
	}

	public void setEffects(Position pos, List<StatusEffect> effects) {

		LOG.debug("Setting effect for{} -> {}", pos, effects);

		if (pos == Position.LEFT) {

			effectLeft.setEffects(effects);
		} else {

			effectRight.setEffects(effects);
		}
	}

	public void resetEffects(Position pos) {

		LOG.debug("Resetting effects for {}", pos);

		if (pos == Position.LEFT) {

			effectLeft.reset();
		} else {

			effectRight.reset();
		}
	}

	public int getRound() {

		return lblRound.getRound();
	}

	public void setRound(int round) {

		lblRound.setRound(round);
	}

}
