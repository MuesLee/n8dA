package de.kvwl.n8dA.robotwars.server.visualization.java.scene.animation;

import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;

public interface Animation
{

	/*
	 * Wird vor dem Beginn jedes Animationszyklus aufgerufen.
	 */
	public void prepare();

	/**
	 * Wird vor dem zeichnen des eigentlichen {@link SceneObject}s aufgerufen. Somit werden alle
	 * Zeichenoperationen des {@link SceneObject}s manipuliert.
	 */
	public void animatePre(SceneObject obj, Graphics2D g, long elapsedTime);

	/**
	 * Wird nach dem zeichnen des eigentlichen {@link SceneObject}s aufgerufen. Somit kann das
	 * bereits gezeichnete {@link SceneObject} manipuliert werden.
	 */
	public void animatePost(SceneObject obj, Graphics2D g, long elapsedTime);

	/**
	 * Gibt an, ob die Animation beendet ist, oder weiterhin als laufend betrachtet werden soll.
	 * Blockiert möglicherweise die aufrufende Methode der Animation.
	 */
	public boolean isRunning();

	/**
	 * Gibt an, ob die Animation, wenn sie nicht mehr running ({@link #isRunning()}) ist, trotzdem
	 * weiter gezeichnet werden soll. Somit können z.B. Endzustände der Animation beibehalten
	 * werden, wenn sie von dem Normalzustand des {@link SceneObject} abweichen.
	 */
	public boolean isAlive();
}
