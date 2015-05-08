package de.kvwl.n8dA.robotwars.server.visualization.java.scene.status;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;

import game.engine.stage.scene.object.SceneObject;

public class RoundLabel extends SceneObject
{

	private static final double RATIO = 0.6;

	private int round = 1;
	private Color textColor = Color.WHITE;

	@Override
	protected void paint(Graphics2D g2d, long time)
	{

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		String txt1 = "Runde";
		String txt2 = "" + round;

		double height1 = getHeight() * RATIO;
		double height2 = getHeight() * (1 - RATIO);

		g2d.setColor(textColor);

		Font f1 = getFont(height1, getWidth(), g2d, txt1);
		g2d.setFont(f1);
		FontMetrics metrics = g2d.getFontMetrics();
		LineMetrics lineMetrics = metrics.getLineMetrics(txt1, g2d);
		float strWidth = metrics.stringWidth(txt1);
		float strHeight = lineMetrics.getAscent() + lineMetrics.getDescent();
		g2d.drawString(txt1, (float) (getWidth() * 0.5 - strWidth * 0.5),
			(float) (lineMetrics.getAscent() + (height1 - (strHeight)) * 0.5));

		Font f2 = getFont(height2, getWidth(), g2d, txt2);
		g2d.setFont(f2);
		metrics = g2d.getFontMetrics();
		lineMetrics = metrics.getLineMetrics(txt2, g2d);
		strWidth = metrics.stringWidth(txt2);
		strHeight = lineMetrics.getAscent() + lineMetrics.getDescent();
		g2d.drawString(txt2, (float) (getWidth() * 0.5 - strWidth * 0.5),
			(float) (lineMetrics.getAscent() + (height2 - (strHeight)) * 0.5) + (float) height1);
	}

	private Font getFont(double height, int width, Graphics2D g2d, String s)
	{

		Font f = new Font(Font.SANS_SERIF, Font.BOLD, (int) height + 1);
		double _height, _width;

		do
		{

			f = new Font(Font.SANS_SERIF, Font.BOLD, f.getSize() - 1);

			FontMetrics metrics = g2d.getFontMetrics(f);
			LineMetrics lineMetrics = metrics.getLineMetrics(s, g2d);

			_width = metrics.stringWidth(s);
			_height = lineMetrics.getAscent() + lineMetrics.getDescent();

		}
		while (_height > height || _width > width);

		return f;
	}

	public int getRound()
	{
		return round;
	}

	public void setRound(int round)
	{
		this.round = round;
	}

	public Color getTextColor()
	{
		return textColor;
	}

	public void setTextColor(Color textColor)
	{
		this.textColor = textColor;
	}

}
