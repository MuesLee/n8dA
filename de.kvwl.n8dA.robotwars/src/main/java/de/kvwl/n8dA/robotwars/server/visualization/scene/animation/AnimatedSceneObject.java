package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

public abstract class AnimatedSceneObject extends SceneObject {

	private List<ObjectAnimator> animations = new LinkedList<ObjectAnimator>();

	/**
	 * Override this method. Erste Anweisung muss super.paint sein, damit die
	 * Animationen abgespielt werden.
	 * 
	 * @see SceneObject#paint(Graphics2D, long)
	 */
	protected void paint(Graphics2D g, long elapsedTime) {

		synchronized (animations) {
			runAnimations(g, elapsedTime);
		}
	}

	private void runAnimations(Graphics2D g, long elapsedTime) {

		synchronized (animations) {

			for (ObjectAnimator animation : animations) {

				if (!animation.isAlive()) {
					animations.remove(animation);
					continue;
				}

				animation.animate(this, g, elapsedTime);
			}
		}
	}

	/**
	 * Erstellt eine neue Instanz der Animation und startet sie.
	 */
	public void startAnimator(Class<? extends ObjectAnimator> animation,
			boolean wait) throws InstantiationException, IllegalAccessException {

		ObjectAnimator animator = animation.newInstance();

		startAnimator(animator, wait);
	}

	/**
	 * Startet die Animation. Vorsichtig mit shared animations.
	 */
	public void startAnimator(ObjectAnimator animation, boolean wait) {

		animation.startAnimation(wait);

		synchronized (animations) {
			animations.add(animation);
		}
	}
}
