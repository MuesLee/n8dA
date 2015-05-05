package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

public class ScaleAnimation implements Animation {

	boolean finished = false;

	private long elapsedAnimationTime = 0;
	private long animationTime;

	private double startFactor;
	private double endFactor;
	private double factor;

	public ScaleAnimation(double startFactor, double endFactor,
			long animationTime) {

		this.startFactor = startFactor;
		this.factor = startFactor;
		this.endFactor = endFactor;
		this.animationTime = animationTime;
	}

	@Override
	public boolean animate(SceneObject obj, Graphics2D g, long elapsedTime) {

		if (finished) {
			return true;
		}

		elapsedAnimationTime += elapsedTime;
		if (elapsedAnimationTime >= animationTime) {

			finished = true;
		}

		double increasePerTime = (endFactor - startFactor) / animationTime;
		factor = factor + (increasePerTime * elapsedTime);

		if (startFactor < endFactor) {

			factor = Math.min(factor, endFactor);
		} else {

			factor = Math.max(endFactor, factor);
		}

		_animate(obj, g, elapsedTime);

		return finished;
	}

	private void _animate(SceneObject obj, Graphics2D g, long elapsedTime) {
		// TODO ScaleAnimation
	}

}
