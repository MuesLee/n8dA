package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;

import de.kvwl.n8dA.robotwars.server.visualization.Position;
import game.engine.stage.scene.object.SceneObject;

public class HealthPoints extends SceneObject {

	private static int BORDER_WIDTH = 2;

	private Color maxColor = new Color(0, 255, 0);
	private Color minColor = new Color(255, 0, 0);

	private double maxValue = 100;
	private double value = 100;

	private Position pos = Position.LEFT;

	private boolean paintValueString = true;

	@Override
	protected void paint(Graphics2D g2d, long time) {

		setRedneringHints(g2d);

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		double percent = (1.0 / maxValue) * value;

		g2d.setColor(getColorForValue(percent));
		if (pos == Position.LEFT) {

			g2d.fillRect(BORDER_WIDTH, BORDER_WIDTH,
					(int) ((getWidth() - 2 * BORDER_WIDTH) * percent),
					getHeight() - 2 * BORDER_WIDTH);
		} else {

			int valWidth = (int) ((getWidth() - 2 * BORDER_WIDTH) * percent);

			g2d.fillRect(getWidth() - BORDER_WIDTH - valWidth, BORDER_WIDTH,
					valWidth, getHeight() - 2 * BORDER_WIDTH);
		}

		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(BORDER_WIDTH));
		g2d.drawRect(0, 0, getWidth() - BORDER_WIDTH, getHeight()
				- BORDER_WIDTH);

		if (paintValueString) {

			g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, getHeight()));

			String txt = String.format("%d / %d", (int) value, (int) maxValue);
			int strWidth = getWidth() - 2 * BORDER_WIDTH;
			int strHeight = getHeight() - 2 * BORDER_WIDTH;

			FontMetrics metrics;
			LineMetrics lineMetrics;

			double height;
			double width;

			do {
				g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, g2d.getFont()
						.getSize() - 1));

				metrics = g2d.getFontMetrics();
				lineMetrics = metrics.getLineMetrics(txt, g2d);

				height = lineMetrics.getDescent() + lineMetrics.getAscent();
				width = metrics.stringWidth(txt);

			} while (width > strWidth || height > strHeight);

			g2d.drawString(
					txt,
					(float) (getWidth() * 0.5 - width * 0.5),
					(float) (lineMetrics.getAscent() + (getHeight() - (height)) * 0.5));
		}
	}

	private Color getColorForValue(double percent) {

		int r, g, b;

		r = (int) (maxColor.getRed() * percent + minColor.getRed()
				* (1 - percent)) % 256;
		g = (int) (maxColor.getGreen() * percent + minColor.getGreen()
				* (1 - percent)) % 256;
		b = (int) (maxColor.getBlue() * percent + minColor.getBlue()
				* (1 - percent)) % 256;

		Color c = new Color(r, g, b);

		return c;
	}

	private void setRedneringHints(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Color getMaxColor() {
		return maxColor;
	}

	public void setMaxColor(Color maxColor) {
		this.maxColor = maxColor;
	}

	public Color getMinColor() {
		return minColor;
	}

	public void setMinColor(Color minColor) {
		this.minColor = minColor;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public boolean isPaintValueString() {
		return paintValueString;
	}

	public void setPaintValueString(boolean paintValueString) {
		this.paintValueString = paintValueString;
	}

}
