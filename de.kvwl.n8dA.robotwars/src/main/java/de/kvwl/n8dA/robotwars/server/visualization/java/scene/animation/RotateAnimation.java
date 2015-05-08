package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

public class RotateAnimation implements Animation {

	private boolean alive = false;
	private boolean running = false;

	private long elapsedAnimationTime = 0;
	private long animationTime;

	private double startAngle;
	private double endAngle;
	private double angle;
	private boolean positiveDirection;

	public RotateAnimation(double startAngle, double endAngle,
			boolean positiveDirection, long animationTime) {

		this.startAngle = startAngle;
		this.angle = startAngle;
		this.endAngle = endAngle;
		this.positiveDirection = positiveDirection;
		this.animationTime = animationTime;
	}

	@Override
	public void prepare() {

		angle = startAngle;
		elapsedAnimationTime = 0;

		alive = true;
		running = true;
	}

	@Override
	public void animatePost(SceneObject obj, Graphics2D g, long elapsedTime) {
	}

	@Override
	public void animatePre(SceneObject obj, Graphics2D g, long elapsedTime) {

		if (!alive) {
			running = false;
			return;
		}

		if (running) {
			elapsedAnimationTime += elapsedTime;
			if (elapsedAnimationTime >= animationTime) {

				elapsedAnimationTime = animationTime;
				running = false;
			}
		}

		double increasePerTime = (endAngle - startAngle) / animationTime;
		angle = (increasePerTime * elapsedAnimationTime);

		if (startAngle < endAngle) {

			angle = Math.min(angle, endAngle);
		} else {

			angle = Math.max(endAngle, angle);
		}

		animate(obj, g, elapsedTime);

		if (!running && endAngle == 1) {
			alive = false;
		}
	}

	private void animate(SceneObject obj, Graphics2D g, long elapsedTime) {

		g.translate(+obj.getWidth() * 0.5, +obj.getHeight() * 0.5);

		double angle = (positiveDirection) ? this.angle : -this.angle;
		g.rotate(angle);

		g.translate(-obj.getWidth() * 0.5, -obj.getHeight() * 0.5);

	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public boolean isRunning() {

		return running;
	}
}
