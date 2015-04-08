package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import static java.lang.Math.round;
import game.engine.image.InternalImage;
import game.engine.stage.scene.object.SceneObject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;

//TODO Marvin: Effect Anzeige
public class EffectLabel extends SceneObject
{

	private static final double BORDER_OUTLINE = 0.3;
	private static final double BORDER = 0.15;
	private static final double SPACE = 0.1;

	private static final Logger LOG = LoggerFactory.getLogger(StatusScene.class);

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private List<StatusEffect> effects = new LinkedList<StatusEffect>();

	@Override
	protected void paint(Graphics2D g2d, long time)
	{

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int index = 0;
		for (StatusEffect ef : effects)
		{

			if (ef == null || ef.getIconName() == null)
			{
				LOG.debug("Nullpointer found < drawing effect -> {}", ef);
				continue;
			}

			Image img;
			try
			{
				img = InternalImage.loadFromPath(IMAGE_PATH, ef.getIconName());
			}
			catch (Exception e)
			{

				LOG.debug("Image not found for effect painting -> {} -> {}", ef, ef.getIconName());
				continue;
			}

			paintEffectAtPos(g2d, index, img, (ef.isPositive()) ? Color.GREEN : Color.RED);
			index++;
		}

	}

	private void paintEffectAtPos(Graphics2D g2d, int pos, Image img, Color bg)
	{
		double _width = getWidth() * (1.0 / effects.size());
		double _height = getHeight();
		double _size = Math.min(_width, _height);

		double _space = _size * SPACE;

		int _x = (int) ((_size + _space) * pos);
		int _y = 0;

		int _border = (int) round(_size * BORDER);
		int _borderInner = (int) round(_border * BORDER_OUTLINE);

		g2d.setColor(Color.BLACK);
		g2d.fillOval(_x, _y, (int) round(_size), (int) round(_size));

		g2d.setColor(bg);

		Ellipse2D roundCircle = new Ellipse2D.Double((int) round(_x + _borderInner), (int) round(_y + _borderInner),
			(int) round(_size - 2 * _borderInner), (int) round(_size - 2 * _borderInner));
		g2d.fill(roundCircle);

		g2d.drawImage(img, (int) (_x + _border), (int) (_y + _border), (int) (_x + _size - _border),
			(int) (_y + _size - _border), 0, 0, img.getWidth(null), img.getHeight(null), null);
	}

	public void setEffects(List<StatusEffect> effects)
	{

		this.effects.clear();
		this.effects.addAll(effects);
	}

	public void reset()
	{

		effects.clear();
	}
}
