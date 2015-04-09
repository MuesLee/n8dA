package de.kvwl.n8dA.robotwars.server.visualization;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.concurrent.atomic.AtomicReference;

import de.kvwl.n8dA.robotwars.commons.utils.Null;
import game.engine.stage.scene.object.SceneObject;

public class LabelObject extends SceneObject
{

	private AtomicReference<String> text = new AtomicReference<String>();
	private AtomicReference<Color> color = new AtomicReference<Color>();
	private AtomicReference<Font> font = new AtomicReference<Font>();
	private AtomicReference<Double> posX = new AtomicReference<Double>();

	public LabelObject()
	{

		color.set(Color.BLACK);
		text.set("");
		font.set(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		posX.set(0.5);
	}

	@Override
	protected void paint(Graphics2D g2d, long time)
	{

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		drawString(g2d);
	}

	private void drawString(Graphics2D g2d)
	{

		g2d.setColor(color.get());
		g2d.setFont(font.get());

		String text = this.text.get();

		float _x = 0;
		float _y = 0;

		double _width = 0;
		double _height = 0;

		Font font = new Font(g2d.getFont().getName(), g2d.getFont().getStyle(), getHeight() + 1);
		FontMetrics metrics = g2d.getFontMetrics(font);

		do
		{
			font = new Font(font.getName(), font.getStyle(), font.getSize() - 1);
			metrics = g2d.getFontMetrics(font);

			_width = metrics.stringWidth(text);
			_height = metrics.getHeight() + metrics.getMaxAscent() + metrics.getMaxDescent();
		}
		while (_width > getWidth() || _height > getHeight());
		g2d.setFont(font);

		int strHeight = metrics.getAscent() + metrics.getDescent();

		_x = (float) ((getWidth() - _width) * getPosX());
		_y = (float) (float) (metrics.getAscent() + (getHeight() - (strHeight)) * 0.5);

		g2d.drawString(text, _x, _y);
	}

	public String getText()
	{
		return text.get();
	}

	public void setText(String text)
	{
		this.text.set(Null.nvl(text, ""));
	}

	public Color getColor()
	{
		return color.get();
	}

	public void setColor(Color color)
	{
		this.color.set(Null.nvl(color, Color.BLACK));
	}

	public Font getFont()
	{
		return font.get();
	}

	public void setFont(Font font)
	{
		this.font.set(Null.nvl(font, new Font(Font.SANS_SERIF, Font.BOLD, 12)));
	}

	public double getPosX()
	{
		return posX.get();
	}

	public void setPosX(double posX)
	{
		this.posX.set(posX);
	}

}
