package de.kvwl.n8dA.robotwars.server.visualization.java.scene;

import game.engine.stage.scene.object.SceneObject;

public class Insets
{

	private double[] insets = new double[] { 0, 0, 0, 0 };

	public Insets(double left, double right, double top, double bottom)
	{

		insets[0] = left;
		insets[1] = right;
		insets[2] = top;
		insets[3] = bottom;
	}

	public double getLeft()
	{

		return insets[0];
	}

	public double getRight()
	{

		return insets[1];
	}

	public double getTop()
	{

		return insets[2];
	}

	public double getBottom()
	{

		return insets[3];
	}

	public Insets toAbsolute(SceneObject obj)
	{
		return new Insets(getLeft() * obj.getWidth(), getRight() * obj.getWidth(), getTop() * obj.getHeight(),
			getBottom() * obj.getHeight());
	}
}
