package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import game.engine.stage.scene.object.SceneObject;
import game.engine.time.TimeUtils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;

import de.kvwl.n8dA.robotwars.server.visualization.java.Position;

public class HealthPoints extends SceneObject {

	private static int BORDER_WIDTH = 2;

	private static long VALUE_TIME = TimeUtils.NanosecondsOfMilliseconds(100);
	private static double VALUE_TIME_DIFF_BONUS = 0.1;

	private Color maxColor = new Color(0, 255, 0);
	private Color minColor = new Color(255, 0, 0);

	private double maxValue = 100;
	private double value = 100;

	private double aimValue = 100;
	private double missingTime = 0;
	private Object valueWait = new Object();

	private Position pos = Position.LEFT;
	private boolean paintValueString = true;

	@Override
	protected void paint(Graphics2D g2d, long time) {

		calculateValue(time);

		setRedneringHints(g2d);

		g2d.setColor(Color.LIGHT_GRAY);
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

	private void calculateValue(long time) {

		if (value == aimValue) {
			return;
		}

		double missingValue = Math.abs(value - aimValue);

		double valuePerTime = (double) VALUE_TIME
				/ Math.max(1, (missingValue * VALUE_TIME_DIFF_BONUS));
		double elapsedValue = (time + missingTime) / valuePerTime;

		double usedValue = Math.min(elapsedValue, missingValue);
		missingTime = (time + missingTime) - (elapsedValue * valuePerTime);

		if (value < aimValue) {

			value += usedValue;
		} else {

			value -= usedValue;
		}

		if (value == aimValue) {

			reachedValue();
		}
	}

	private void reachedValue() {

		synchronized (valueWait) {

			valueWait.notifyAll();
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

		Color c = new Color(range(0, r, 255), range(0, g, 255),
				range(0, b, 255));

		return c;
	}

	private int range(int min, int soll, int max) {

		return Math.min(Math.max(min, soll), max);
	}

	private void setRedneringHints(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void startValueAnimation(double value, boolean wait) {

		this.aimValue = value;

		if (this.aimValue == this.value) {
			return;
		}

		if (wait) {

			synchronized (valueWait) {

				try {
					valueWait.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
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
		this.aimValue = value;

		reachedValue();
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
