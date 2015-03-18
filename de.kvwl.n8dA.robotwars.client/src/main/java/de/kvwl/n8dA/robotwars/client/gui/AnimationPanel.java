package de.kvwl.n8dA.robotwars.client.gui;

import game.engine.stage.SwingStage;
import game.engine.stage.scene.object.AnimatedSceneObject;
import game.engine.time.Clock;

public class AnimationPanel extends SwingStage
{

	private static final long serialVersionUID = 1L;

	private AnimationScene scene = new AnimationScene();

	private Clock clk = new Clock();

	private boolean paused = false;

	public AnimationPanel()
	{

		clk.start();
	}

	public void pause()
	{

		paused = true;
		clk.setPaused(true);
	}

	public void resume()
	{

		paused = false;
		clk.setPaused(false);
	}

	public boolean isPaused()
	{

		return paused;
	}

	public void setAnimation(AnimatedSceneObject animation)
	{

		scene.setAnimation(animation);
	}

	@Override
	protected void finalize() throws Throwable
	{
		clk.destroy();

		super.finalize();
	}
}
