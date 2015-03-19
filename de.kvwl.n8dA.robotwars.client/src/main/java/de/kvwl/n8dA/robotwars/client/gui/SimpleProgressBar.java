package de.kvwl.n8dA.robotwars.client.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JProgressBar;

public class SimpleProgressBar extends JProgressBar
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g)
	{

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(getBackground());
		g.fillRect(3, 3, getWidth() - 6, getHeight() - 6);

		g.setColor(getForeground());
		double percent = (100.0 / getMaximum()) * getValue();
		g.fillRect(3, 3, (int) Math.round((getWidth() - 6) * percent), getHeight() - 6);
	}
}
