package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

public class ScaleAnimation implements Animation {

	boolean finished = false;
	boolean alive = true;

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

		if (!alive) {
			return true;
		}

		if (!finished) {
			elapsedAnimationTime += elapsedTime;
			if (elapsedAnimationTime >= animationTime) {

				elapsedAnimationTime = animationTime;
				finished = true;
			}
		}

		double increasePerTime = (endFactor - startFactor) / animationTime;
		factor = (increasePerTime * elapsedAnimationTime);

		if (startFactor < endFactor) {

			factor = Math.min(factor, endFactor);
		} else {

			factor = Math.max(endFactor, factor);
		}

		_animate(obj, g, elapsedTime);

		return finished;
	}

	private void _animate(SceneObject obj, Graphics2D g, long elapsedTime) {

		g.translate(factor * 30, factor * 30);

		// if (factor == 0) {
		//
		// factor =
		// 0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001;
		// }
		//
		// g.scale(factor, factor);
	}

	@Override
	public boolean alive() {
		return alive;
	}

}
