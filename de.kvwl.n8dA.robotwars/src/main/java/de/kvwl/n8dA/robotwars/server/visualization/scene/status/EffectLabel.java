package de.kvwl.n8dA.robotwars.server.visualization.scene.status;

import game.engine.image.InternalImage;
import game.engine.stage.scene.object.SceneObject;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kvwl.n8dA.robotwars.commons.game.statuseffects.StatusEffect;

//TODO Marvin: Effect Anzeige
public class EffectLabel extends SceneObject
{

	private static final Logger LOG = LoggerFactory.getLogger(StatusScene.class);

	private static final String IMAGE_PATH = "/de/kvwl/n8dA/robotwars/commons/images/";

	private List<StatusEffect> effects = new LinkedList<StatusEffect>();

	@Override
	protected void paint(Graphics2D g2d, long time)
	{

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

			paintEffectAtPos(g2d, index, img);
			index++;
		}

	}

	private void paintEffectAtPos(Graphics2D g2d, int pos, Image img)
	{
		double _width = getWidth() * (1.0 / 3.0);
		double _height = getHeight();

		_height = _width = Math.min(_width, _height);

		double _space = _width * 0.1;

		int _x = (int) ((_width + _space) * pos);
		int _y = 0;

		g2d.drawImage(img, _x, _y, (int) (_x + _width), (int) (_y + _height), 0, 0, img.getWidth(null),
			img.getHeight(null), null);
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
