package de.kvwl.n8dA.robotwars.server.visualization;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class GraphicsConfiguration
{

	private GraphicsDevice device;
	private DisplayMode mode;

	public GraphicsConfiguration(GraphicsDevice device, DisplayMode mode)
	{
		this.device = device;
		this.mode = mode;
	}

	public GraphicsDevice getDevice()
	{
		return device;
	}

	public DisplayMode getDisplayMode()
	{
		return mode;
	}

	/**
	 * Default config mit 800x600px und dem Default Screen
	 */
	public static GraphicsConfiguration getDefaultConfig()
	{

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice device = env.getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();

		DisplayMode[] displayModes = device.getDisplayModes();
		for (int i = 0; i < displayModes.length; i++)
		{

			DisplayMode displayMode = displayModes[i];

			if (displayMode.getWidth() == 800 && displayMode.getHeight() == 600)
			{

				mode = displayMode;
				break;
			}
		}

		return new GraphicsConfiguration(device, mode);
	}

	public static GraphicsConfiguration getSystemDefault()
	{

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice device = env.getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();

		return new GraphicsConfiguration(device, mode);
	}

}
