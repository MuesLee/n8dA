package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ObjectAnimator {

	private Object finishLock = new Object();
	private AtomicBoolean running = new AtomicBoolean(false);

	private List<Animation> animations = new ArrayList<Animation>();

	public ObjectAnimator(Animation... animations) {

		Collections.addAll(this.animations, animations);
	}

	final void startAnimation(boolean wait) {
		running.set(true);

		if (wait) {
			synchronized (finishLock) {

				try {
					finishLock.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * Durchführen der Animation. <strong>!Nicht vergessen -
	 * {@link #animationFinished()} !</strong>
	 */
	final void animate(SceneObject obj, Graphics2D g, long elapsedTime) {

		boolean ready = true;

		Iterator<Animation> iterator = animations.iterator();
		while (iterator.hasNext()) {

			Animation animation = iterator.next();

			if (!animation.animate(obj, g, elapsedTime)) {

				ready = false;
			} else if (!animation.alive()) {

				iterator.remove();
			}
		}

		if (ready) {
			animationFinished();
		}
	}

	/**
	 * Tell that the animation is over, {@link #startAnimation(boolean)}
	 * returns.
	 */
	protected final void animationFinished() {

		synchronized (finishLock) {
			finishLock.notifyAll();
		}

		running.set(false);
	}

	public boolean isRunning() {

		return running.get();
	}

	public boolean isAlive() {
		// TODO alive
		return isRunning() || true;
	}
}
